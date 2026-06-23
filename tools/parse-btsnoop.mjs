#!/usr/bin/env node

import fs from "node:fs";
import crypto from "node:crypto";

const input = process.argv[2];
if (!input) {
  console.error("Usage: node parse-btsnoop.mjs <btsnoop-file>");
  process.exit(1);
}

const data = fs.readFileSync(input);
if (data.subarray(0, 8).toString("ascii") !== "btsnoop\0") {
  throw new Error("Not a BTSnoop file");
}

const BTSNOOP_UNIX_EPOCH_US = 0x00dcddb30f2f8000n;
const COMMAND_AES_KEY = Buffer.from("32672f7974ad43451d9c6c894a0e8764", "hex");
const pendingL2cap = new Map();
const writes = [];
const notifications = [];
const services = [];
const characteristics = [];
let offset = 16;
let recordNumber = 0;

function timestampToIso(timestampUs) {
  const unixUs = timestampUs - BTSNOOP_UNIX_EPOCH_US;
  return new Date(Number(unixUs / 1000n)).toISOString();
}

function parseAtt(record, connectionHandle, direction, att) {
  if (att.length < 2) return;

  const opcode = att[0];
  if (opcode === 0x1b && att.length >= 3) {
    const attributeHandle = `0x${att.readUInt16LE(1).toString(16).padStart(4, "0")}`;
    const value = att.subarray(3);
    const notification = {
      record,
      connectionHandle: `0x${connectionHandle.toString(16).padStart(4, "0")}`,
      direction,
      attributeHandle,
      length: value.length,
      value: value.toString("hex"),
    };
    if (attributeHandle === "0x001b" && value.length % 16 === 0) {
      const decipher = crypto.createDecipheriv("aes-128-ecb", COMMAND_AES_KEY, null);
      decipher.setAutoPadding(false);
      const plaintext = Buffer.concat([decipher.update(value), decipher.final()]);
      const bodyLength = plaintext[0] ?? 0;
      notification.decrypted = {
        length: bodyLength,
        hex: plaintext.subarray(1, 1 + bodyLength).toString("hex"),
      };
    }
    notifications.push(notification);
    return;
  }

  if (opcode === 0x11) {
    const entryLength = att[1];
    for (let cursor = 2; cursor + entryLength <= att.length; cursor += entryLength) {
      services.push({
        startHandle: `0x${att.readUInt16LE(cursor).toString(16).padStart(4, "0")}`,
        endHandle: `0x${att.readUInt16LE(cursor + 2).toString(16).padStart(4, "0")}`,
        uuid: formatUuid(att.subarray(cursor + 4, cursor + entryLength)),
      });
    }
    return;
  }

  if (opcode === 0x09) {
    const entryLength = att[1];
    if (entryLength !== 7 && entryLength !== 21) return;
    for (let cursor = 2; cursor + entryLength <= att.length; cursor += entryLength) {
      characteristics.push({
        declarationHandle: `0x${att.readUInt16LE(cursor).toString(16).padStart(4, "0")}`,
        properties: `0x${att[cursor + 2].toString(16).padStart(2, "0")}`,
        valueHandle: `0x${att.readUInt16LE(cursor + 3).toString(16).padStart(4, "0")}`,
        uuid: formatUuid(att.subarray(cursor + 5, cursor + entryLength)),
      });
    }
    return;
  }

  if (att.length < 3) return;
  if (![0x12, 0x16, 0x52, 0xd2].includes(opcode)) return;

  const value = att.subarray(3);
  const write = {
    record,
    connectionHandle: `0x${connectionHandle.toString(16).padStart(4, "0")}`,
    direction,
    opcode: `0x${opcode.toString(16).padStart(2, "0")}`,
    attributeHandle: `0x${att.readUInt16LE(1).toString(16).padStart(4, "0")}`,
    length: value.length,
    value: value.toString("hex"),
  };

  if (write.attributeHandle === "0x0012" && value.length % 16 === 0) {
    const decipher = crypto.createDecipheriv("aes-128-ecb", COMMAND_AES_KEY, null);
    decipher.setAutoPadding(false);
    const plaintext = Buffer.concat([decipher.update(value), decipher.final()]);
    const commandLength = plaintext[0] ?? 0;
    const command = plaintext.subarray(1, 1 + commandLength);
    write.decrypted = {
      length: commandLength,
      ascii: command.toString("latin1").replace(/[\x00-\x1f\x7f-\xff]/g, (character) => `\\x${character.charCodeAt(0).toString(16).padStart(2, "0")}`),
      hex: command.toString("hex"),
    };
  }

  writes.push(write);
}

function formatUuid(bytes) {
  if (bytes.length === 2) {
    return `0x${bytes.readUInt16LE(0).toString(16).padStart(4, "0")}`;
  }
  if (bytes.length === 16) {
    const hex = Buffer.from(bytes).reverse().toString("hex");
    return `${hex.slice(0, 8)}-${hex.slice(8, 12)}-${hex.slice(12, 16)}-${hex.slice(16, 20)}-${hex.slice(20)}`;
  }
  return bytes.toString("hex");
}

function processL2cap(record, connectionHandle, direction, cid, payload) {
  if (cid === 0x0004) parseAtt(record, connectionHandle, direction, payload);
}

while (offset + 24 <= data.length) {
  const originalLength = data.readUInt32BE(offset);
  const includedLength = data.readUInt32BE(offset + 4);
  const flags = data.readUInt32BE(offset + 8);
  const timestampUs = data.readBigUInt64BE(offset + 16);
  offset += 24;

  if (offset + includedLength > data.length) break;
  const packet = data.subarray(offset, offset + includedLength);
  offset += includedLength;
  recordNumber += 1;

  if (packet.length < 5 || packet[0] !== 0x02) continue;

  const handleAndFlags = packet.readUInt16LE(1);
  const connectionHandle = handleAndFlags & 0x0fff;
  const packetBoundary = (handleAndFlags >>> 12) & 0x03;
  const aclLength = packet.readUInt16LE(3);
  const aclPayload = packet.subarray(5, 5 + aclLength);
  const direction = (flags & 1) === 0 ? "host->controller" : "controller->host";
  const record = {
    number: recordNumber,
    timestamp: timestampToIso(timestampUs),
    originalLength,
  };

  if (packetBoundary === 0x01) {
    const pending = pendingL2cap.get(connectionHandle);
    if (!pending) continue;
    pending.payload = Buffer.concat([pending.payload, aclPayload]);
    if (pending.payload.length >= pending.expectedLength) {
      processL2cap(record, connectionHandle, direction, pending.cid, pending.payload.subarray(0, pending.expectedLength));
      pendingL2cap.delete(connectionHandle);
    }
    continue;
  }

  if (aclPayload.length < 4) continue;
  const expectedLength = aclPayload.readUInt16LE(0);
  const cid = aclPayload.readUInt16LE(2);
  const l2capPayload = aclPayload.subarray(4);

  if (l2capPayload.length >= expectedLength) {
    processL2cap(record, connectionHandle, direction, cid, l2capPayload.subarray(0, expectedLength));
  } else {
    pendingL2cap.set(connectionHandle, {
      cid,
      expectedLength,
      payload: Buffer.from(l2capPayload),
    });
  }
}

const uniqueByJson = (items) => [...new Map(items.map((item) => [JSON.stringify(item), item])).values()];

function decodeUploads(allWrites) {
  const uploads = [];
  let current = null;

  for (const write of allWrites) {
    const commandHex = write.decrypted?.hex ?? "";
    if (commandHex.startsWith("44415453")) {
      const argumentsHex = commandHex.slice(8);
      current = {
        expectedSize: Number.parseInt(argumentsHex.slice(0, 4), 16),
        argumentsHex,
        chunks: new Map(),
      };
      continue;
    }

    if (current && write.attributeHandle === "0x0015") {
      const packet = Buffer.from(write.value, "hex");
      const declaredLength = packet[0];
      const sequence = packet[1];
      current.chunks.set(sequence, packet.subarray(2, 1 + declaredLength));
      continue;
    }

    if (current && commandHex.startsWith("4441544350")) {
      const payload = Buffer.concat([...current.chunks.entries()]
        .sort(([left], [right]) => left - right)
        .map(([, chunk]) => chunk));
      const nonzeroPositions = [];
      for (let index = 0; index < payload.length; index += 1) {
        if (payload[index] !== 0) nonzeroPositions.push(`${index}:0x${payload[index].toString(16).padStart(2, "0")}`);
      }
      const columns = [];
      for (let column = 0; column < 36; column += 1) {
        const bytes = payload.subarray(column * 38, (column + 1) * 38);
        if (bytes.some((byte) => byte !== 0)) {
          columns.push({ column, hex: bytes.toString("hex") });
        }
      }
      uploads.push({
        expectedSize: current.expectedSize,
        actualSize: payload.length,
        argumentsHex: current.argumentsHex,
        chunks: current.chunks.size,
        sha256: crypto.createHash("sha256").update(payload).digest("hex"),
        nonzeroCount: nonzeroPositions.length,
        nonzeroPositions,
        nonzeroColumns: columns,
      });
      current = null;
    }
  }

  return uploads;
}

const result = {
  input,
  records: recordNumber,
  services: uniqueByJson(services),
  characteristics: uniqueByJson(characteristics),
  writes,
  notifications,
  uploads: decodeUploads(writes),
};

const selectedResult = process.argv.includes("--uploads-only")
  ? result.uploads
  : process.argv.includes("--notifications-only")
    ? result.notifications
    : result;

console.log(JSON.stringify(selectedResult, null, 2));
