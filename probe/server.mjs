#!/usr/bin/env node

import crypto from "node:crypto";
import fs from "node:fs/promises";
import http from "node:http";
import path from "node:path";
import { fileURLToPath } from "node:url";

const HOST = "127.0.0.1";
const PORT = Number(process.env.PORT || 8787);
const ROOT = path.join(path.dirname(fileURLToPath(import.meta.url)), "public");
const KEY = Buffer.from("32672f7974ad43451d9c6c894a0e8764", "hex");

const contentTypes = {
  ".html": "text/html; charset=utf-8",
  ".js": "text/javascript; charset=utf-8",
  ".css": "text/css; charset=utf-8",
};

function encryptCommand(command, args) {
  const commandBytes = Buffer.from(command, "ascii");
  const argumentBytes = Buffer.from(args);
  const bodyLength = commandBytes.length + argumentBytes.length;
  if (bodyLength > 255) throw new Error("Command body is too long");

  const blockLength = Math.max(16, Math.ceil((bodyLength + 1) / 16) * 16);
  const plaintext = crypto.randomBytes(blockLength);
  plaintext[0] = bodyLength;
  commandBytes.copy(plaintext, 1);
  argumentBytes.copy(plaintext, 1 + commandBytes.length);

  const cipher = crypto.createCipheriv("aes-128-ecb", KEY, null);
  cipher.setAutoPadding(false);
  return Buffer.concat([cipher.update(plaintext), cipher.final()]);
}

function decryptCommand(encrypted) {
  if (encrypted.length === 0 || encrypted.length % 16 !== 0) {
    throw new Error("Encrypted data must contain complete 16-byte blocks");
  }
  const decipher = crypto.createDecipheriv("aes-128-ecb", KEY, null);
  decipher.setAutoPadding(false);
  const plaintext = Buffer.concat([decipher.update(encrypted), decipher.final()]);
  const length = plaintext[0];
  const body = plaintext.subarray(1, 1 + length);
  return { length, bodyHex: body.toString("hex") };
}

async function readJson(request) {
  const chunks = [];
  for await (const chunk of request) chunks.push(chunk);
  return JSON.parse(Buffer.concat(chunks).toString("utf8"));
}

function sendJson(response, status, value) {
  response.writeHead(status, { "content-type": "application/json; charset=utf-8" });
  response.end(JSON.stringify(value));
}

const server = http.createServer(async (request, response) => {
  try {
    if (request.method === "POST" && request.url === "/crypto") {
      const body = await readJson(request);
      if (body.mode === "encrypt") {
        const encrypted = encryptCommand(body.command, body.args || []);
        return sendJson(response, 200, { hex: encrypted.toString("hex") });
      }
      if (body.mode === "decrypt") {
        return sendJson(response, 200, decryptCommand(Buffer.from(body.hex, "hex")));
      }
      return sendJson(response, 400, { error: "Unknown crypto mode" });
    }

    const pathname = new URL(request.url, `http://${HOST}:${PORT}`).pathname;
    const relative = pathname === "/" ? "index.html" : pathname.slice(1);
    const target = path.resolve(ROOT, relative);
    if (!target.startsWith(`${ROOT}${path.sep}`) && target !== path.join(ROOT, "index.html")) {
      response.writeHead(403).end("Forbidden");
      return;
    }
    const file = await fs.readFile(target);
    response.writeHead(200, {
      "content-type": contentTypes[path.extname(target)] || "application/octet-stream",
      "cache-control": "no-store",
    });
    response.end(file);
  } catch (error) {
    if (error.code === "ENOENT") {
      response.writeHead(404).end("Not found");
    } else {
      sendJson(response, 500, { error: error.message });
    }
  }
});

server.listen(PORT, HOST, () => {
  console.log(`Shining Glasses probe: http://${HOST}:${PORT}`);
});
