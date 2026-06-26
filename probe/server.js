#!/usr/bin/env node

const crypto = require("crypto");
const fs = require("fs");
const https = require("https");
const http = require("http");
const path = require("path");

const HOST = process.env.HOST || "127.0.0.1";
const PORT = Number(process.env.PORT || 8787);
const ROOT = path.join(__dirname, "public");
const KEY = Buffer.from("32672f7974ad43451d9c6c894a0e8764", "hex");
const SUPABASE_URL = process.env.SUPABASE_URL || "";
const SUPABASE_SERVICE_ROLE_KEY = process.env.SUPABASE_SERVICE_ROLE_KEY || "";
const DESIGNS_TABLE = process.env.DESIGNS_TABLE || "shining_glasses_designs";
const ANIMATIONS_TABLE = process.env.ANIMATIONS_TABLE || "shining_glasses_animations";
const LIBRARY_ID = process.env.LIBRARY_ID || "default";

const contentTypes = {
  ".html": "text/html; charset=utf-8",
  ".js": "text/javascript; charset=utf-8",
  ".css": "text/css; charset=utf-8",
  ".svg": "image/svg+xml; charset=utf-8",
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
  const body = plaintext.slice(1, 1 + length);
  return { length, bodyHex: body.toString("hex") };
}

function readJson(request, callback) {
  const chunks = [];
  request.on("data", (chunk) => chunks.push(chunk));
  request.on("end", () => {
    try {
      callback(null, JSON.parse(Buffer.concat(chunks).toString("utf8")));
    } catch (error) {
      callback(error);
    }
  });
  request.on("error", callback);
}

function sendJson(response, status, value) {
  response.writeHead(status, { "content-type": "application/json; charset=utf-8" });
  response.end(JSON.stringify(value));
}

function isSharedLibraryConfigured() {
  return Boolean(SUPABASE_URL && SUPABASE_SERVICE_ROLE_KEY);
}

function normalizeDesign(row) {
  return {
    id: row.id,
    version: 1,
    name: row.name,
    width: row.width,
    height: row.height,
    cells: row.cells,
    savedAt: row.saved_at || row.updated_at,
  };
}

function normalizeAnimation(row) {
  return {
    id: row.id,
    version: 1,
    name: row.name,
    manyModeByte: row.many_mode_byte,
    frames: row.frames,
    savedAt: row.saved_at || row.updated_at,
  };
}

function validateDesign(design) {
  if (!design || typeof design.name !== "string" || !design.name.trim()) {
    throw new Error("Design name is required");
  }
  if (design.width !== 36 || design.height !== 12 || !Array.isArray(design.cells) || design.cells.length !== 432) {
    throw new Error("Design must be a 36x12 grid with 432 cells");
  }
}

function validateAnimation(animation) {
  if (!animation || typeof animation.name !== "string" || !animation.name.trim()) {
    throw new Error("Animation name is required");
  }
  if (!Number.isInteger(animation.manyModeByte) || animation.manyModeByte < 0 || animation.manyModeByte > 255) {
    throw new Error("Animation MANY mode must be between 0 and 255");
  }
  if (!Array.isArray(animation.frames) || animation.frames.length === 0) {
    throw new Error("Animation must contain at least one frame");
  }
  for (const frame of animation.frames) {
    if (!frame || typeof frame.designName !== "string" || !frame.designName.trim()) {
      throw new Error("Each animation frame must reference a design name");
    }
    if (!Number.isInteger(frame.repeat) || frame.repeat < 1 || frame.repeat > 20) {
      throw new Error("Each animation frame repeat must be between 1 and 20");
    }
  }
}

function supabaseRequest(method, requestPath, body, callback) {
  if (!isSharedLibraryConfigured()) {
    callback(new Error("Shared library is not configured"));
    return;
  }

  const baseUrl = new URL(SUPABASE_URL);
  const payload = body ? JSON.stringify(body) : undefined;
  const options = {
    method,
    hostname: baseUrl.hostname,
    path: `/rest/v1/${requestPath}`,
    headers: {
      apikey: SUPABASE_SERVICE_ROLE_KEY,
      authorization: `Bearer ${SUPABASE_SERVICE_ROLE_KEY}`,
      "content-type": "application/json",
      accept: "application/json",
      prefer: "return=representation,resolution=merge-duplicates",
    },
  };

  if (payload) options.headers["content-length"] = Buffer.byteLength(payload);

  const supabaseRequest = https.request(options, (supabaseResponse) => {
    const chunks = [];
    supabaseResponse.on("data", (chunk) => chunks.push(chunk));
    supabaseResponse.on("end", () => {
      const text = Buffer.concat(chunks).toString("utf8");
      let data = null;
      if (text) {
        try {
          data = JSON.parse(text);
        } catch (parseError) {
          callback(parseError);
          return;
        }
      }
      if (supabaseResponse.statusCode < 200 || supabaseResponse.statusCode >= 300) {
        const error = new Error((data && data.message) || `Supabase returned ${supabaseResponse.statusCode}`);
        error.statusCode = supabaseResponse.statusCode;
        callback(error);
        return;
      }
      callback(null, data);
    });
  });

  supabaseRequest.on("error", callback);
  if (payload) supabaseRequest.write(payload);
  supabaseRequest.end();
}

function encodedFilterValue(value) {
  return encodeURIComponent(value);
}

function handleDesignsApi(request, response) {
  if (!isSharedLibraryConfigured()) {
    sendJson(response, 501, { error: "Shared library is not configured" });
    return;
  }

  if (request.method === "GET" && request.url === "/api/designs") {
    const libraryFilter = encodedFilterValue(`eq.${LIBRARY_ID}`);
    const requestPath = `${DESIGNS_TABLE}?library_id=${libraryFilter}&select=id,name,width,height,cells,saved_at,updated_at&order=name.asc`;
    supabaseRequest("GET", requestPath, null, (error, rows) => {
      if (error) {
        sendJson(response, error.statusCode || 500, { error: error.message });
        return;
      }
      sendJson(response, 200, { designs: rows.map(normalizeDesign) });
    });
    return;
  }

  if (request.method === "POST" && request.url === "/api/designs") {
    readJson(request, (error, design) => {
      try {
        if (error) throw error;
        validateDesign(design);
      } catch (validationError) {
        sendJson(response, 400, { error: validationError.message });
        return;
      }

      const row = {
        library_id: LIBRARY_ID,
        name: design.name.trim(),
        width: design.width,
        height: design.height,
        cells: design.cells,
        saved_at: design.savedAt || new Date().toISOString(),
      };
      const requestPath = `${DESIGNS_TABLE}?on_conflict=library_id,name`;
      supabaseRequest("POST", requestPath, row, (supabaseError, rows) => {
        if (supabaseError) {
          sendJson(response, supabaseError.statusCode || 500, { error: supabaseError.message });
          return;
        }
        sendJson(response, 200, { design: normalizeDesign(rows[0]) });
      });
    });
    return;
  }

  if (request.method === "DELETE" && request.url.indexOf("/api/designs/") === 0) {
    const name = decodeURIComponent(request.url.slice("/api/designs/".length));
    const libraryFilter = encodedFilterValue(`eq.${LIBRARY_ID}`);
    const nameFilter = encodedFilterValue(`eq.${name}`);
    const requestPath = `${DESIGNS_TABLE}?library_id=${libraryFilter}&name=${nameFilter}`;
    supabaseRequest("DELETE", requestPath, null, (error) => {
      if (error) {
        sendJson(response, error.statusCode || 500, { error: error.message });
        return;
      }
      sendJson(response, 200, { ok: true });
    });
    return;
  }

  sendJson(response, 404, { error: "API route not found" });
}

function handleAnimationsApi(request, response) {
  if (!isSharedLibraryConfigured()) {
    sendJson(response, 501, { error: "Shared library is not configured" });
    return;
  }

  if (request.method === "GET" && request.url === "/api/animations") {
    const libraryFilter = encodedFilterValue(`eq.${LIBRARY_ID}`);
    const requestPath = `${ANIMATIONS_TABLE}?library_id=${libraryFilter}&select=id,name,many_mode_byte,frames,saved_at,updated_at&order=name.asc`;
    supabaseRequest("GET", requestPath, null, (error, rows) => {
      if (error) {
        sendJson(response, error.statusCode || 500, { error: error.message });
        return;
      }
      sendJson(response, 200, { animations: rows.map(normalizeAnimation) });
    });
    return;
  }

  if (request.method === "POST" && request.url === "/api/animations") {
    readJson(request, (error, animation) => {
      try {
        if (error) throw error;
        validateAnimation(animation);
      } catch (validationError) {
        sendJson(response, 400, { error: validationError.message });
        return;
      }

      const row = {
        library_id: LIBRARY_ID,
        name: animation.name.trim(),
        many_mode_byte: animation.manyModeByte,
        frames: animation.frames,
        saved_at: animation.savedAt || new Date().toISOString(),
      };
      const requestPath = `${ANIMATIONS_TABLE}?on_conflict=library_id,name`;
      supabaseRequest("POST", requestPath, row, (supabaseError, rows) => {
        if (supabaseError) {
          sendJson(response, supabaseError.statusCode || 500, { error: supabaseError.message });
          return;
        }
        sendJson(response, 200, { animation: normalizeAnimation(rows[0]) });
      });
    });
    return;
  }

  if (request.method === "DELETE" && request.url.indexOf("/api/animations/") === 0) {
    const name = decodeURIComponent(request.url.slice("/api/animations/".length));
    const libraryFilter = encodedFilterValue(`eq.${LIBRARY_ID}`);
    const nameFilter = encodedFilterValue(`eq.${name}`);
    const requestPath = `${ANIMATIONS_TABLE}?library_id=${libraryFilter}&name=${nameFilter}`;
    supabaseRequest("DELETE", requestPath, null, (error) => {
      if (error) {
        sendJson(response, error.statusCode || 500, { error: error.message });
        return;
      }
      sendJson(response, 200, { ok: true });
    });
    return;
  }

  sendJson(response, 404, { error: "API route not found" });
}

function sendStaticFile(request, response) {
  const pathname = new URL(request.url, `http://${HOST}:${PORT}`).pathname;
  const relative = pathname === "/" ? "index.html" : pathname.slice(1);
  const target = path.resolve(ROOT, relative);

  if (!target.startsWith(`${ROOT}${path.sep}`) && target !== path.join(ROOT, "index.html")) {
    response.writeHead(403).end("Forbidden");
    return;
  }

  fs.readFile(target, (error, file) => {
    if (error) {
      response.writeHead(error.code === "ENOENT" ? 404 : 500).end(error.code === "ENOENT" ? "Not found" : error.message);
      return;
    }
    response.writeHead(200, {
      "content-type": contentTypes[path.extname(target)] || "application/octet-stream",
      "cache-control": "no-store",
    });
    response.end(file);
  });
}

const server = http.createServer((request, response) => {
  if (request.url === "/api/designs" || request.url.indexOf("/api/designs/") === 0) {
    handleDesignsApi(request, response);
    return;
  }

  if (request.url === "/api/animations" || request.url.indexOf("/api/animations/") === 0) {
    handleAnimationsApi(request, response);
    return;
  }

  if (request.method === "POST" && request.url === "/crypto") {
    readJson(request, (error, body) => {
      try {
        if (error) throw error;
        if (body.mode === "encrypt") {
          const encrypted = encryptCommand(body.command, body.args || []);
          sendJson(response, 200, { hex: encrypted.toString("hex") });
          return;
        }
        if (body.mode === "decrypt") {
          sendJson(response, 200, decryptCommand(Buffer.from(body.hex, "hex")));
          return;
        }
        sendJson(response, 400, { error: "Unknown crypto mode" });
      } catch (cryptoError) {
        sendJson(response, 500, { error: cryptoError.message });
      }
    });
    return;
  }

  sendStaticFile(request, response);
});

server.listen(PORT, HOST, () => {
  console.log(`Shining Glasses probe: http://${HOST}:${PORT}`);
});
