const SERVICE_UUID = "0000fff0-0000-1000-8000-00805f9b34fb";
const COMMAND_UUID = "d44bc439-abfd-45a2-b575-925416129600";
const DATA_UUID = "d44bc439-abfd-45a2-b575-92541612960a";
const NOTIFY_UUID = "d44bc439-abfd-45a2-b575-925416129601";

const elements = {
  connect: document.querySelector("#connect"),
  connectionCard: document.querySelector(".connection-card"),
  connectionLabel: document.querySelector("#connection-label"),
  connectionModal: document.querySelector("#connection-modal"),
  closeConnectionModal: document.querySelector("#close-connection-modal"),
  modalConnect: document.querySelector("#modal-connect"),
  showEditor: document.querySelector("#show-editor"),
  showLibrary: document.querySelector("#show-library"),
  editorView: document.querySelector("#editor-view"),
  libraryView: document.querySelector("#library-view"),
  imageFile: document.querySelector("#image-file"),
  importMode: document.querySelector("#import-mode"),
  brightnessThreshold: document.querySelector("#brightness-threshold"),
  coverageThreshold: document.querySelector("#coverage-threshold"),
  useSolidColor: document.querySelector("#use-solid-color"),
  solidColor: document.querySelector("#solid-color"),
  toolPaint: document.querySelector("#tool-paint"),
  toolErase: document.querySelector("#tool-erase"),
  mirrorHorizontal: document.querySelector("#mirror-horizontal"),
  moveLeft: document.querySelector("#move-left"),
  moveRight: document.querySelector("#move-right"),
  moveUp: document.querySelector("#move-up"),
  moveDown: document.querySelector("#move-down"),
  clearGrid: document.querySelector("#clear-grid"),
  undo: document.querySelector("#undo"),
  redo: document.querySelector("#redo"),
  designName: document.querySelector("#design-name"),
  savedDesigns: document.querySelector("#saved-designs"),
  saveDesign: document.querySelector("#save-design"),
  loadDesign: document.querySelector("#load-design"),
  deleteDesign: document.querySelector("#delete-design"),
  exportDesign: document.querySelector("#export-design"),
  importDesign: document.querySelector("#import-design"),
  importDesignFile: document.querySelector("#import-design-file"),
  stripLibrary: document.querySelector("#strip-library"),
  preview: document.querySelector("#preview"),
  ledGrid: document.querySelector("#led-grid"),
  sendImage: document.querySelector("#send-image"),
  status: document.querySelector("#status"),
  log: document.querySelector("#log"),
};

let device;
let commandCharacteristic;
let dataCharacteristic;
let notifyCharacteristic;
let notificationWaiter;
let sourceImage;
let ledCells = [];
let activeTool = "paint";
let isPointerDown = false;
let currentStrokeKey;
let undoStack = [];
let redoStack = [];
let libraryCache = [];
let sharedLibraryAvailable = false;
let sharedLibraryChecked = false;
let connectedDeviceName = "";

const DISPLAY_WIDTH = 36;
const DISPLAY_HEIGHT = 12;
const CELL_COUNT = DISPLAY_WIDTH * DISPLAY_HEIGHT;
const LIBRARY_STORAGE_KEY = "shiningGlassesDesigns";
const HISTORY_LIMIT = 80;
function log(message) {
  const timestamp = new Date().toLocaleTimeString();
  elements.log.textContent += `[${timestamp}] ${message}\n`;
  elements.log.scrollTop = elements.log.scrollHeight;
}

function setConnectionState(state, message) {
  elements.connectionCard.classList.toggle("is-connected", state === "connected");
  elements.connectionCard.classList.toggle("is-connecting", state === "connecting");
  elements.status.textContent = message;
  elements.connectionLabel.textContent = state === "connected"
    ? "Conectadas"
    : state === "connecting"
      ? "Conectando"
      : "Desconectadas";
  elements.connect.disabled = state === "connecting";
}

function openConnectionModal() {
  elements.connectionModal.classList.remove("is-hidden");
}

function closeConnectionModal() {
  elements.connectionModal.classList.add("is-hidden");
}

function isConnected() {
  return Boolean(commandCharacteristic && dataCharacteristic && notifyCharacteristic);
}

async function handleConnectRequest() {
  try {
    await connect();
    closeConnectionModal();
  } catch (error) {
    setConnectionState("disconnected", "Gafas sin conectar");
    log(`ERROR ${error.message}`);
  }
}

function showView(view) {
  const isEditor = view === "editor";
  elements.editorView.classList.toggle("is-hidden", !isEditor);
  elements.libraryView.classList.toggle("is-hidden", isEditor);
  elements.showEditor.setAttribute("aria-pressed", isEditor ? "true" : "false");
  elements.showLibrary.setAttribute("aria-pressed", isEditor ? "false" : "true");
  if (!isEditor) renderStripLibrary().catch((error) => log(`ERROR ${error.message}`));
}

function hexToBytes(hex) {
  return Uint8Array.from(hex.match(/.{2}/g) || [], (byte) => Number.parseInt(byte, 16));
}

async function cryptoRequest(payload) {
  const response = await fetch("/crypto", {
    method: "POST",
    headers: { "content-type": "application/json" },
    body: JSON.stringify(payload),
  });
  const result = await response.json();
  if (!response.ok) throw new Error(result.error || "Crypto request failed");
  return result;
}

async function write(characteristic, bytes) {
  if (typeof characteristic.writeValueWithoutResponse === "function") {
    await characteristic.writeValueWithoutResponse(bytes);
  } else {
    await characteristic.writeValue(bytes);
  }
}

async function sendCommand(command, args = []) {
  const { hex } = await cryptoRequest({ mode: "encrypt", command, args });
  await write(commandCharacteristic, hexToBytes(hex));
  log(`TX ${command} ${args.map((byte) => byte.toString(16).padStart(2, "0")).join(" ")}`);
}

function waitForNotification(timeout = 1800) {
  return new Promise((resolve) => {
    const timer = setTimeout(() => {
      notificationWaiter = undefined;
      resolve(undefined);
    }, timeout);
    notificationWaiter = (value) => {
      clearTimeout(timer);
      notificationWaiter = undefined;
      resolve(value);
    };
  });
}

async function onNotification(event) {
  const bytes = new Uint8Array(event.target.value.buffer.slice(
    event.target.value.byteOffset,
    event.target.value.byteOffset + event.target.value.byteLength,
  ));
  const hex = [...bytes].map((byte) => byte.toString(16).padStart(2, "0")).join("");
  let decoded = { bodyHex: hex };
  if (bytes.length % 16 === 0) {
    try {
      decoded = await cryptoRequest({ mode: "decrypt", hex });
    } catch {}
  }
  log(`RX ${decoded.bodyHex}`);
  if (notificationWaiter) notificationWaiter(decoded);
}

async function connect() {
  if (!navigator.bluetooth) throw new Error("Web Bluetooth no está disponible en este navegador");
  setConnectionState("connecting", "Buscando gafas...");
  device = await navigator.bluetooth.requestDevice({
    filters: [{ namePrefix: "GLASSES-" }],
    optionalServices: [SERVICE_UUID],
  });
  const server = await device.gatt.connect();
  const service = await server.getPrimaryService(SERVICE_UUID);
  [commandCharacteristic, dataCharacteristic, notifyCharacteristic] = await Promise.all([
    service.getCharacteristic(COMMAND_UUID),
    service.getCharacteristic(DATA_UUID),
    service.getCharacteristic(NOTIFY_UUID),
  ]);
  await notifyCharacteristic.startNotifications();
  notifyCharacteristic.addEventListener("characteristicvaluechanged", onNotification);
  device.addEventListener("gattserverdisconnected", () => {
    connectedDeviceName = "";
    commandCharacteristic = undefined;
    dataCharacteristic = undefined;
    notifyCharacteristic = undefined;
    setConnectionState("disconnected", "Gafas desconectadas");
    updateSendState();
  });
  connectedDeviceName = device.name || "GLASSES";
  setConnectionState("connected", `Conectadas a ${connectedDeviceName}`);
  updateSendState();
  log(`Conectado a ${device.name}`);
}

function encodeFrame(pixels) {
  const frame = new Uint8Array(1368);
  for (const { x, y, r, g, b } of pixels) {
    const column = (DISPLAY_WIDTH - 1) - x;
    const mask = (frame[column * 2] << 8) | frame[column * 2 + 1];
    const nextMask = mask | (0x8000 >>> y);
    frame[column * 2] = nextMask >>> 8;
    frame[column * 2 + 1] = nextMask & 0xff;
    const colorOffset = 72 + ((column * 12 + y) * 3);
    frame.set([r, g, b], colorOffset);
  }
  return frame;
}

function getDrawRect(image, fitMode) {
  if (fitMode === "stretch") {
    return { x: 0, y: 0, width: DISPLAY_WIDTH, height: DISPLAY_HEIGHT };
  }

  const imageRatio = image.naturalWidth / image.naturalHeight;
  const displayRatio = DISPLAY_WIDTH / DISPLAY_HEIGHT;
  const scale = fitMode === "cover"
    ? Math.max(DISPLAY_WIDTH / image.naturalWidth, DISPLAY_HEIGHT / image.naturalHeight)
    : Math.min(DISPLAY_WIDTH / image.naturalWidth, DISPLAY_HEIGHT / image.naturalHeight);
  const width = image.naturalWidth * scale;
  const height = image.naturalHeight * scale;

  return {
    x: (DISPLAY_WIDTH - width) / 2,
    y: (DISPLAY_HEIGHT - height) / 2,
    width: imageRatio && displayRatio ? width : DISPLAY_WIDTH,
    height: imageRatio && displayRatio ? height : DISPLAY_HEIGHT,
  };
}

function renderLedCanvas(canvas, pixels) {
  const context = canvas.getContext("2d");
  const cellWidth = canvas.width / DISPLAY_WIDTH;
  const cellHeight = canvas.height / DISPLAY_HEIGHT;
  const radius = Math.min(cellWidth, cellHeight) * 0.22;

  context.clearRect(0, 0, canvas.width, canvas.height);
  context.fillStyle = "#030707";
  context.fillRect(0, 0, canvas.width, canvas.height);

  for (let y = 0; y < DISPLAY_HEIGHT; y += 1) {
    for (let x = 0; x < DISPLAY_WIDTH; x += 1) {
      context.beginPath();
      context.fillStyle = "#0b1517";
      context.arc((x + 0.5) * cellWidth, (y + 0.5) * cellHeight, radius * 0.5, 0, Math.PI * 2);
      context.fill();
    }
  }

  context.globalCompositeOperation = "lighter";

  for (const { x, y, r, g, b } of pixels) {
    const centerX = (x + 0.5) * cellWidth;
    const centerY = (y + 0.5) * cellHeight;

    const backgroundBloom = context.createRadialGradient(centerX, centerY, radius * 1.2, centerX, centerY, radius * 6.4);
    backgroundBloom.addColorStop(0, `rgba(${r}, ${g}, ${b}, 0.2)`);
    backgroundBloom.addColorStop(0.55, `rgba(${r}, ${g}, ${b}, 0.1)`);
    backgroundBloom.addColorStop(1, `rgba(${r}, ${g}, ${b}, 0)`);
    context.fillStyle = backgroundBloom;
    context.beginPath();
    context.arc(centerX, centerY, radius * 6.4, 0, Math.PI * 2);
    context.fill();
  }

  for (const { x, y, r, g, b } of pixels) {
    const centerX = (x + 0.5) * cellWidth;
    const centerY = (y + 0.5) * cellHeight;

    const wideGlow = context.createRadialGradient(centerX, centerY, radius * 0.4, centerX, centerY, radius * 3.7);
    wideGlow.addColorStop(0, `rgba(${r}, ${g}, ${b}, 0.45)`);
    wideGlow.addColorStop(0.55, `rgba(${r}, ${g}, ${b}, 0.18)`);
    wideGlow.addColorStop(1, `rgba(${r}, ${g}, ${b}, 0)`);
    context.fillStyle = wideGlow;
    context.beginPath();
    context.arc(centerX, centerY, radius * 3.7, 0, Math.PI * 2);
    context.fill();
  }

  for (const { x, y, r, g, b } of pixels) {
    const centerX = (x + 0.5) * cellWidth;
    const centerY = (y + 0.5) * cellHeight;
    const glow = context.createRadialGradient(centerX, centerY, radius * 0.15, centerX, centerY, radius * 2.2);
    glow.addColorStop(0, `rgba(255, 255, 255, 0.95)`);
    glow.addColorStop(0.22, `rgba(${Math.min(255, r + 80)}, ${Math.min(255, g + 80)}, ${Math.min(255, b + 80)}, 0.82)`);
    glow.addColorStop(0.58, `rgba(${r}, ${g}, ${b}, 0.42)`);
    glow.addColorStop(1, `rgba(${r}, ${g}, ${b}, 0)`);
    context.fillStyle = glow;
    context.beginPath();
    context.arc(centerX, centerY, radius * 2.2, 0, Math.PI * 2);
    context.fill();

    context.fillStyle = `rgb(${Math.min(255, r + 110)} ${Math.min(255, g + 110)} ${Math.min(255, b + 110)})`;
    context.beginPath();
    context.arc(centerX, centerY, radius * 0.62, 0, Math.PI * 2);
    context.fill();
  }

  context.globalCompositeOperation = "source-over";
}

function renderPreview(pixels) {
  renderLedCanvas(elements.preview, pixels);
}

function hexColorToRgb(hex) {
  const value = hex.replace("#", "");
  return {
    r: Number.parseInt(value.slice(0, 2), 16),
    g: Number.parseInt(value.slice(2, 4), 16),
    b: Number.parseInt(value.slice(4, 6), 16),
  };
}

function getCellIndex(x, y) {
  return y * DISPLAY_WIDTH + x;
}

function getSolidColor() {
  return hexColorToRgb(elements.solidColor.value);
}

function colorToHex(color) {
  if (!color) return "";
  return `#${[color.r, color.g, color.b].map((channel) => channel.toString(16).padStart(2, "0")).join("")}`;
}

function cloneCells(cells = ledCells) {
  return cells.map((cell) => (cell ? { r: cell.r, g: cell.g, b: cell.b } : null));
}

function cellsSignature(cells = ledCells) {
  return cells.map(colorToHex).join("|");
}

function updateHistoryButtons() {
  elements.undo.disabled = undoStack.length === 0;
  elements.redo.disabled = redoStack.length === 0;
}

function pushHistory() {
  const snapshot = cloneCells();
  if (undoStack.length && cellsSignature(undoStack[undoStack.length - 1]) === cellsSignature(snapshot)) return;
  undoStack.push(snapshot);
  if (undoStack.length > HISTORY_LIMIT) undoStack.shift();
  redoStack = [];
  updateHistoryButtons();
}

function restoreCells(cells) {
  ledCells = cloneCells(cells);
  sourceImage = undefined;
  renderGrid();
  updateHistoryButtons();
}

function undo() {
  if (!undoStack.length) return;
  redoStack.push(cloneCells());
  restoreCells(undoStack.pop());
}

function redo() {
  if (!redoStack.length) return;
  undoStack.push(cloneCells());
  restoreCells(redoStack.pop());
}

function createDesign(name) {
  return {
    version: 1,
    name,
    width: DISPLAY_WIDTH,
    height: DISPLAY_HEIGHT,
    cells: ledCells.map(colorToHex),
    savedAt: new Date().toISOString(),
  };
}

function readLocalLibrary() {
  try {
    const value = localStorage.getItem(LIBRARY_STORAGE_KEY);
    const designs = value ? JSON.parse(value) : [];
    return Array.isArray(designs) ? designs : [];
  } catch (error) {
    log(`ERROR biblioteca local: ${error.message}`);
    return [];
  }
}

function writeLocalLibrary(designs) {
  localStorage.setItem(LIBRARY_STORAGE_KEY, JSON.stringify(designs));
}

async function apiRequest(path, options) {
  const response = await fetch(path, options);
  const result = await response.json();
  if (!response.ok) throw new Error(result.error || `API request failed: ${response.status}`);
  return result;
}

async function readLibrary() {
  try {
    const result = await apiRequest("/api/designs");
    const designs = Array.isArray(result.designs) ? result.designs : [];
    libraryCache = designs;
    writeLocalLibrary(designs);
    sharedLibraryAvailable = true;
    sharedLibraryChecked = true;
    return designs;
  } catch (error) {
    if (!sharedLibraryChecked && error.message !== "Shared library is not configured") {
      log(`Biblioteca compartida no disponible, usando local: ${error.message}`);
    }
    sharedLibraryAvailable = false;
    sharedLibraryChecked = true;
    libraryCache = readLocalLibrary();
    return libraryCache;
  }
}

async function saveDesignToLibrary(design) {
  if (sharedLibraryAvailable || !sharedLibraryChecked) {
    try {
      const result = await apiRequest("/api/designs", {
        method: "POST",
        headers: { "content-type": "application/json" },
        body: JSON.stringify(design),
      });
      await readLibrary();
      return result.design || design;
    } catch (error) {
      log(`Biblioteca compartida no guardó, usando local: ${error.message}`);
      sharedLibraryAvailable = false;
      sharedLibraryChecked = true;
    }
  }

  const designs = readLocalLibrary();
  const existingIndex = designs.findIndex((item) => item.name === design.name);
  if (existingIndex >= 0) {
    designs[existingIndex] = design;
  } else {
    designs.push(design);
  }
  designs.sort((left, right) => left.name.localeCompare(right.name));
  writeLocalLibrary(designs);
  libraryCache = designs;
  return design;
}

async function deleteDesignFromLibrary(name) {
  if (sharedLibraryAvailable) {
    try {
      await apiRequest(`/api/designs/${encodeURIComponent(name)}`, { method: "DELETE" });
      await readLibrary();
      return;
    } catch (error) {
      log(`Biblioteca compartida no borró, usando local: ${error.message}`);
      sharedLibraryAvailable = false;
    }
  }

  const designs = readLocalLibrary().filter((item) => item.name !== name);
  writeLocalLibrary(designs);
  libraryCache = designs;
}

async function renderLibrary(selectedName) {
  const designs = await readLibrary();
  elements.savedDesigns.innerHTML = "";

  if (!designs.length) {
    const option = document.createElement("option");
    option.value = "";
    option.textContent = "Sin diseños guardados";
    elements.savedDesigns.appendChild(option);
    renderStripLibrary(designs);
    return;
  }

  for (const design of designs) {
    const option = document.createElement("option");
    option.value = design.name;
    option.textContent = design.name;
    elements.savedDesigns.appendChild(option);
  }

  if (selectedName) elements.savedDesigns.value = selectedName;
  renderStripLibrary(designs);
}

function designToPixels(design) {
  validateDesign(design);
  const pixels = [];
  for (let y = 0; y < DISPLAY_HEIGHT; y += 1) {
    for (let x = 0; x < DISPLAY_WIDTH; x += 1) {
      const hex = design.cells[getCellIndex(x, y)];
      if (!hex) continue;
      const color = hexColorToRgb(hex);
      pixels.push({ x, y, r: color.r, g: color.g, b: color.b });
    }
  }
  return pixels;
}

async function renderStripLibrary(designs) {
  if (!designs) designs = await readLibrary();
  elements.stripLibrary.innerHTML = "";

  if (!designs.length) {
    const empty = document.createElement("p");
    empty.className = "empty-state";
    empty.textContent = "Guarda diseños para usarlos como control rápido.";
    elements.stripLibrary.appendChild(empty);
    return;
  }

  for (const design of designs) {
    const item = document.createElement("button");
    item.type = "button";
    item.className = "strip-item";
    item.title = `Enviar ${design.name}`;

    const canvas = document.createElement("canvas");
    canvas.width = 360;
    canvas.height = 72;
    renderLedCanvas(canvas, designToPixels(design));

    const meta = document.createElement("div");
    meta.className = "strip-meta";

    const label = document.createElement("span");
    label.className = "strip-label";
    label.textContent = design.name;

    const feedback = document.createElement("span");
    feedback.className = "strip-feedback";
    feedback.textContent = "Tocar para enviar";

    meta.appendChild(label);
    meta.appendChild(feedback);

    item.appendChild(canvas);
    item.appendChild(meta);
    item.addEventListener("click", () => {
      if (!isConnected()) {
        openConnectionModal();
        return;
      }
      item.classList.remove("is-sent", "is-error");
      item.classList.add("is-sending");
      item.disabled = true;
      feedback.textContent = "Enviando...";
      sendDesign(design)
        .then(() => {
          item.classList.remove("is-sending");
          item.classList.add("is-sent");
          feedback.textContent = "Listo";
          window.setTimeout(() => {
            item.classList.remove("is-sent");
            feedback.textContent = "Tocar para enviar";
          }, 1800);
        })
        .catch((error) => {
          item.classList.remove("is-sending");
          item.classList.add("is-error");
          feedback.textContent = "Error";
          elements.status.textContent = "El envío falló";
          log(`ERROR ${error.message}`);
        })
        .finally(() => {
          item.disabled = false;
        });
    });
    elements.stripLibrary.appendChild(item);
  }
}

function validateDesign(design) {
  if (!design || design.width !== DISPLAY_WIDTH || design.height !== DISPLAY_HEIGHT || !Array.isArray(design.cells)) {
    throw new Error("El diseño no tiene formato 36x12 válido");
  }
  if (design.cells.length !== CELL_COUNT) {
    throw new Error(`El diseño debe tener ${CELL_COUNT} celdas`);
  }
}

function loadDesignIntoGrid(design) {
  validateDesign(design);
  pushHistory();
  sourceImage = undefined;
  ledCells = design.cells.map((hex) => (hex ? hexColorToRgb(hex) : null));
  elements.designName.value = design.name || "";
  renderGrid();
}

async function saveCurrentDesign() {
  const name = elements.designName.value.trim();
  if (!name) throw new Error("Ponle un nombre al diseño antes de guardar");

  const design = createDesign(name);
  await saveDesignToLibrary(design);
  await renderLibrary(name);
  log(`Diseño guardado: ${name}`);
}

async function getSelectedDesign() {
  const name = elements.savedDesigns.value;
  const designs = libraryCache.length ? libraryCache : await readLibrary();
  const design = designs.find((item) => item.name === name);
  if (!design) throw new Error("Selecciona un diseño guardado");
  return design;
}

async function loadSelectedDesign() {
  const design = await getSelectedDesign();
  loadDesignIntoGrid(design);
  log(`Diseño cargado: ${design.name}`);
}

async function deleteSelectedDesign() {
  const name = elements.savedDesigns.value;
  if (!name) throw new Error("Selecciona un diseño guardado");
  await deleteDesignFromLibrary(name);
  await renderLibrary();
  log(`Diseño borrado: ${name}`);
}

async function exportSelectedDesign() {
  const name = elements.savedDesigns.value;
  const design = name ? await getSelectedDesign() : createDesign(elements.designName.value.trim() || "design");
  const blob = new Blob([JSON.stringify(design, null, 2)], { type: "application/json" });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = `${design.name || "shining-glasses-design"}.json`;
  document.body.appendChild(link);
  link.click();
  link.remove();
  URL.revokeObjectURL(url);
  log(`Diseño exportado: ${design.name}`);
}

function importDesignFile(file) {
  const reader = new FileReader();
  reader.onload = () => {
    try {
      const design = JSON.parse(reader.result);
      validateDesign(design);
      const name = design.name || file.name.replace(/\.json$/i, "");
      const importedDesign = Object.assign({}, design, { name });
      loadDesignIntoGrid(importedDesign);
      elements.designName.value = name;
      log(`Diseño importado: ${name}`);
    } catch (error) {
      log(`ERROR ${error.message}`);
    }
  };
  reader.readAsText(file);
}

function getLedPixels() {
  const pixels = [];
  for (let y = 0; y < DISPLAY_HEIGHT; y += 1) {
    for (let x = 0; x < DISPLAY_WIDTH; x += 1) {
      const color = ledCells[getCellIndex(x, y)];
      if (color) pixels.push({ x, y, r: color.r, g: color.g, b: color.b });
    }
  }
  return pixels;
}

function orientPoint(x, y) {
  return { x: DISPLAY_WIDTH - 1 - x, y };
}

function orientPixels(pixels) {
  return pixels.map((pixel) => {
    const point = orientPoint(pixel.x, pixel.y);
    return { x: point.x, y: point.y, r: pixel.r, g: pixel.g, b: pixel.b };
  });
}

function updateSendState() {
  const count = getLedPixels().length;
  elements.sendImage.disabled = count === 0;
}

function renderGrid() {
  const pixels = getLedPixels();
  for (let y = 0; y < DISPLAY_HEIGHT; y += 1) {
    for (let x = 0; x < DISPLAY_WIDTH; x += 1) {
      const index = getCellIndex(x, y);
      const cell = elements.ledGrid.children[index];
      const color = ledCells[index];
      cell.classList.toggle("is-on", Boolean(color));
      cell.style.backgroundColor = color ? `rgb(${color.r} ${color.g} ${color.b})` : "";
      cell.setAttribute("aria-pressed", color ? "true" : "false");
    }
  }
  renderPreview(pixels);
  updateSendState();
}

function setCellsFromPixels(pixels) {
  ledCells = Array.from({ length: CELL_COUNT }, () => null);
  for (const pixel of pixels) {
    setCell(pixel.x, pixel.y, { r: pixel.r, g: pixel.g, b: pixel.b });
  }
  renderGrid();
}

function setCell(x, y, color) {
  if (x < 0 || x >= DISPLAY_WIDTH || y < 0 || y >= DISPLAY_HEIGHT) return;
  ledCells[getCellIndex(x, y)] = color;
}

function paintCell(cell) {
  const x = Number(cell.dataset.x);
  const y = Number(cell.dataset.y);
  const strokeKey = `${activeTool}:${x}:${y}`;
  if (!currentStrokeKey) pushHistory();
  if (currentStrokeKey === strokeKey) return;
  currentStrokeKey = strokeKey;
  setCell(x, y, activeTool === "paint" ? getSolidColor() : null);
  renderGrid();
}

function buildGrid() {
  ledCells = Array.from({ length: CELL_COUNT }, () => null);
  elements.ledGrid.innerHTML = "";
  for (let y = 0; y < DISPLAY_HEIGHT; y += 1) {
    for (let x = 0; x < DISPLAY_WIDTH; x += 1) {
      const cell = document.createElement("button");
      cell.type = "button";
      cell.className = "led-cell";
      cell.dataset.x = String(x);
      cell.dataset.y = String(y);
      cell.setAttribute("aria-label", `LED ${x},${y}`);
      cell.setAttribute("aria-pressed", "false");
      cell.addEventListener("pointerdown", (event) => {
        event.preventDefault();
        isPointerDown = true;
        paintCell(cell);
      });
      cell.addEventListener("pointerenter", () => {
        if (isPointerDown) paintCell(cell);
      });
      elements.ledGrid.appendChild(cell);
    }
  }
}

function setActiveTool(tool) {
  activeTool = tool;
  elements.toolPaint.setAttribute("aria-pressed", tool === "paint" ? "true" : "false");
  elements.toolErase.setAttribute("aria-pressed", tool === "erase" ? "true" : "false");
}

function isPixelOn(r, g, b, alpha, threshold) {
  const brightness = (r * 0.299) + (g * 0.587) + (b * 0.114);
  return alpha >= 32 && brightness >= threshold;
}

function importImageByResize() {
  const sourceCanvas = document.createElement("canvas");
  sourceCanvas.width = DISPLAY_WIDTH;
  sourceCanvas.height = DISPLAY_HEIGHT;
  const context = sourceCanvas.getContext("2d", { willReadFrequently: true });
  context.imageSmoothingEnabled = elements.importMode.value !== "exact";
  context.clearRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);

  const drawRect = getDrawRect(sourceImage, elements.importMode.value === "exact" ? "stretch" : elements.importMode.value);
  context.drawImage(sourceImage, drawRect.x, drawRect.y, drawRect.width, drawRect.height);

  const threshold = Number(elements.brightnessThreshold.value);
  const solidColor = hexColorToRgb(elements.solidColor.value);
  const data = context.getImageData(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT).data;
  ledCells = Array.from({ length: CELL_COUNT }, () => null);

  for (let y = 0; y < DISPLAY_HEIGHT; y += 1) {
    for (let x = 0; x < DISPLAY_WIDTH; x += 1) {
      const offset = (y * DISPLAY_WIDTH + x) * 4;
      const r = data[offset];
      const g = data[offset + 1];
      const b = data[offset + 2];
      const alpha = data[offset + 3];

      if (isPixelOn(r, g, b, alpha, threshold)) {
        setCell(x, y, elements.useSolidColor.checked
          ? { x, y, r: solidColor.r, g: solidColor.g, b: solidColor.b }
          : { x, y, r, g, b });
      }
    }
  }
}

function importImageByCells() {
  const sourceCanvas = document.createElement("canvas");
  sourceCanvas.width = sourceImage.naturalWidth;
  sourceCanvas.height = sourceImage.naturalHeight;
  const context = sourceCanvas.getContext("2d", { willReadFrequently: true });
  context.imageSmoothingEnabled = false;
  context.drawImage(sourceImage, 0, 0);

  const threshold = Number(elements.brightnessThreshold.value);
  const coverageThreshold = Number(elements.coverageThreshold.value) / 100;
  const solidColor = getSolidColor();
  const data = context.getImageData(0, 0, sourceCanvas.width, sourceCanvas.height).data;
  ledCells = Array.from({ length: CELL_COUNT }, () => null);

  for (let y = 0; y < DISPLAY_HEIGHT; y += 1) {
    const startY = Math.floor((y * sourceCanvas.height) / DISPLAY_HEIGHT);
    const endY = Math.max(startY + 1, Math.floor(((y + 1) * sourceCanvas.height) / DISPLAY_HEIGHT));

    for (let x = 0; x < DISPLAY_WIDTH; x += 1) {
      const startX = Math.floor((x * sourceCanvas.width) / DISPLAY_WIDTH);
      const endX = Math.max(startX + 1, Math.floor(((x + 1) * sourceCanvas.width) / DISPLAY_WIDTH));
      let total = 0;
      let on = 0;
      let red = 0;
      let green = 0;
      let blue = 0;

      for (let sampleY = startY; sampleY < endY; sampleY += 1) {
        for (let sampleX = startX; sampleX < endX; sampleX += 1) {
          const offset = (sampleY * sourceCanvas.width + sampleX) * 4;
          const r = data[offset];
          const g = data[offset + 1];
          const b = data[offset + 2];
          const alpha = data[offset + 3];
          total += 1;

          if (isPixelOn(r, g, b, alpha, threshold)) {
            on += 1;
            red += r;
            green += g;
            blue += b;
          }
        }
      }

      if (total > 0 && on / total >= coverageThreshold) {
        setCell(x, y, elements.useSolidColor.checked
          ? { r: solidColor.r, g: solidColor.g, b: solidColor.b }
          : { r: Math.round(red / on), g: Math.round(green / on), b: Math.round(blue / on) });
      }
    }
  }
}

function rebuildImagePixels() {
  if (!sourceImage) return;

  pushHistory();
  if (elements.importMode.value === "cells") {
    importImageByCells();
  } else {
    importImageByResize();
  }

  renderGrid();
  log(`Importado al grid: ${getLedPixels().length} LEDs encendidos (${sourceImage.naturalWidth}x${sourceImage.naturalHeight})`);
}

function loadImage(file) {
  return new Promise((resolve, reject) => {
    const image = new Image();
    const url = URL.createObjectURL(file);
    image.onload = () => {
      URL.revokeObjectURL(url);
      resolve(image);
    };
    image.onerror = () => {
      URL.revokeObjectURL(url);
      reject(new Error("No se pudo leer la imagen"));
    };
    image.src = url;
  });
}

function clearGrid() {
  pushHistory();
  sourceImage = undefined;
  ledCells = Array.from({ length: CELL_COUNT }, () => null);
  renderGrid();
  log("Grid limpio");
}

function refreshSolidColor() {
  pushHistory();
  const color = getSolidColor();
  ledCells = ledCells.map((cell) => (cell ? { r: color.r, g: color.g, b: color.b } : null));
  renderGrid();
}

function movePattern(deltaX, deltaY) {
  pushHistory();
  const nextCells = Array.from({ length: CELL_COUNT }, () => null);
  for (let y = 0; y < DISPLAY_HEIGHT; y += 1) {
    for (let x = 0; x < DISPLAY_WIDTH; x += 1) {
      const color = ledCells[getCellIndex(x, y)];
      if (!color) continue;
      const nextX = x + deltaX;
      const nextY = y + deltaY;
      if (nextX >= 0 && nextX < DISPLAY_WIDTH && nextY >= 0 && nextY < DISPLAY_HEIGHT) {
        nextCells[getCellIndex(nextX, nextY)] = color;
      }
    }
  }
  ledCells = nextCells;
  renderGrid();
}

function mirrorHorizontal() {
  pushHistory();
  const nextCells = ledCells.slice();
  for (let y = 0; y < DISPLAY_HEIGHT; y += 1) {
    for (let x = 0; x < DISPLAY_WIDTH; x += 1) {
      const color = ledCells[getCellIndex(x, y)];
      if (color) nextCells[getCellIndex(DISPLAY_WIDTH - 1 - x, y)] = color;
    }
  }
  ledCells = nextCells;
  renderGrid();
  log("Espejo horizontal aplicado");
}

async function uploadFrame(frame) {
  const acknowledgement = waitForNotification();
  await sendCommand("DATS", [0x05, 0x58, 0x00, 0x48, 0x01]);
  const startResponse = await acknowledgement;
  if (!startResponse || startResponse.bodyHex !== "444154534f4b") {
    throw new Error(`Se esperaba DATSOK; llegó ${(startResponse && startResponse.bodyHex) || "sin respuesta"}`);
  }

  for (let sequence = 0, offset = 0; offset < frame.length; sequence += 1, offset += 98) {
    const payload = frame.slice(offset, offset + 98);
    const packet = new Uint8Array(payload.length + 2);
    packet[0] = payload.length + 1;
    packet[1] = sequence;
    packet.set(payload, 2);
    const packetAcknowledgement = waitForNotification();
    await write(dataCharacteristic, packet);
    const packetResponse = await packetAcknowledgement;
    if (!packetResponse || packetResponse.bodyHex !== "52454f4b") {
      throw new Error(`Paquete ${sequence}: se esperaba REOK; llegó ${(packetResponse && packetResponse.bodyHex) || "sin respuesta"}`);
    }
  }
  const completion = waitForNotification();
  await sendCommand("DATCP");
  const completionResponse = await completion;
  if (!completionResponse || completionResponse.bodyHex !== "44415443504f4b") {
    throw new Error(`Se esperaba DATCPOK; llegó ${(completionResponse && completionResponse.bodyHex) || "sin respuesta"}`);
  }
}

async function onImageSelected(event) {
  const file = event.target.files && event.target.files[0];
  if (!file) return;
  sourceImage = await loadImage(file);
  rebuildImagePixels();
  log(`Imagen cargada: ${file.name} (${sourceImage.naturalWidth}x${sourceImage.naturalHeight})`);
}

async function sendCurrentImage() {
  const pixels = getLedPixels();
  if (!pixels.length) {
    throw new Error("Primero importa o dibuja un patrón con LEDs visibles");
  }
  if (!isConnected()) {
    openConnectionModal();
    return;
  }
  elements.sendImage.disabled = true;
  try {
    elements.status.textContent = "Enviando patrón...";
    await uploadFrame(encodeFrame(orientPixels(pixels)));
    elements.status.textContent = "Patrón enviado; observa las gafas";
    log(`Patrón enviado con ${pixels.length} LEDs encendidos`);
  } finally {
    updateSendState();
  }
}

async function sendDesign(design) {
  if (!isConnected()) {
    openConnectionModal();
    return;
  }
  const pixels = designToPixels(design);
  if (!pixels.length) throw new Error(`El diseño "${design.name}" está vacío`);

  elements.status.textContent = `Enviando ${design.name}...`;
  await uploadFrame(encodeFrame(orientPixels(pixels)));
  elements.status.textContent = `Enviado: ${design.name}`;
  log(`Diseño enviado desde biblioteca rápida: ${design.name}`);
}

buildGrid();
renderPreview([]);
renderLibrary().catch((error) => log(`ERROR ${error.message}`));
updateHistoryButtons();

elements.connect.addEventListener("click", handleConnectRequest);
elements.modalConnect.addEventListener("click", handleConnectRequest);
elements.closeConnectionModal.addEventListener("click", closeConnectionModal);
elements.connectionModal.addEventListener("click", (event) => {
  if (event.target === elements.connectionModal) closeConnectionModal();
});
elements.showEditor.addEventListener("click", () => showView("editor"));
elements.showLibrary.addEventListener("click", () => showView("library"));
elements.imageFile.addEventListener("change", (event) => onImageSelected(event).catch((error) => log(`ERROR ${error.message}`)));
elements.importMode.addEventListener("change", rebuildImagePixels);
elements.brightnessThreshold.addEventListener("input", rebuildImagePixels);
elements.coverageThreshold.addEventListener("input", rebuildImagePixels);
elements.useSolidColor.addEventListener("change", refreshSolidColor);
elements.solidColor.addEventListener("input", refreshSolidColor);
elements.toolPaint.addEventListener("click", () => setActiveTool("paint"));
elements.toolErase.addEventListener("click", () => setActiveTool("erase"));
elements.undo.addEventListener("click", undo);
elements.redo.addEventListener("click", redo);
elements.mirrorHorizontal.addEventListener("click", mirrorHorizontal);
elements.moveLeft.addEventListener("click", () => movePattern(-1, 0));
elements.moveRight.addEventListener("click", () => movePattern(1, 0));
elements.moveUp.addEventListener("click", () => movePattern(0, -1));
elements.moveDown.addEventListener("click", () => movePattern(0, 1));
elements.clearGrid.addEventListener("click", clearGrid);
elements.saveDesign.addEventListener("click", () => {
  saveCurrentDesign().catch((error) => {
    log(`ERROR ${error.message}`);
  });
});
elements.loadDesign.addEventListener("click", () => {
  loadSelectedDesign().catch((error) => {
    log(`ERROR ${error.message}`);
  });
});
elements.deleteDesign.addEventListener("click", () => {
  deleteSelectedDesign().catch((error) => {
    log(`ERROR ${error.message}`);
  });
});
elements.exportDesign.addEventListener("click", () => {
  exportSelectedDesign().catch((error) => {
    log(`ERROR ${error.message}`);
  });
});
elements.importDesign.addEventListener("click", () => elements.importDesignFile.click());
elements.importDesignFile.addEventListener("change", (event) => {
  const file = event.target.files && event.target.files[0];
  if (file) importDesignFile(file);
  event.target.value = "";
});
window.addEventListener("pointerup", () => {
  isPointerDown = false;
  currentStrokeKey = undefined;
});
elements.sendImage.addEventListener("click", () => sendCurrentImage().catch((error) => {
  elements.status.textContent = "El envío falló";
  log(`ERROR ${error.message}`);
}));
