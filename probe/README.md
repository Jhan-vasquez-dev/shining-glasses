# BLE image probe

This local web tool sends custom 36×12 DIY patterns to the glasses. It includes
a pixel editor for exact LED coordinates, a local design library, and direct BLE
upload to the `GLASSES-*` device.

Run the local server on the Mac:

```sh
node server.js
```

Then open Chrome or Edge on the Mac:

```text
http://127.0.0.1:8787
```

Use **Conectar gafas** from the desktop browser and select the `GLASSES-*` BLE
device. The official Shining Glasses mobile app must be disconnected first
because only one BLE central can control the glasses at a time.

## Publish with shared library

The app can be published as a small Node service and share designs through
Supabase. Web Bluetooth requires HTTPS outside `localhost`, so deploy it to a
host such as Render, Railway, or Fly.io.

1. Create a Supabase project.
2. Open Supabase SQL Editor and run `supabase-schema.sql`.
3. Deploy this `probe/` folder as a Node app.
4. Use `npm start` as the start command.
5. Set these environment variables in the host:

```text
HOST=0.0.0.0
PORT=<provided by host>
SUPABASE_URL=https://<project-ref>.supabase.co
SUPABASE_SERVICE_ROLE_KEY=<service-role-key>
LIBRARY_ID=<private-library-name-or-pin>
```

`LIBRARY_ID` is a simple shared-library namespace. Use the same value in the
deployed service to make desktop and phone read/write the same designs.

When Supabase is configured, **Guardar**, **Cargar**, **Borrar**, and
**Biblioteca** use the shared database. If the shared API is not configured, the
app falls back to `localStorage` for local development.

The UI has two views:

- **Editor** for desktop design work.
- **Biblioteca** as a mobile-first remote control for saved custom designs.

## Optional Android bridge

If you prefer to open the same interface on the Android phone connected by USB:

```sh
adb reverse tcp:8787 tcp:8787
adb shell am start -a android.intent.action.VIEW -d http://localhost:8787
```

Use Chrome on Android for Web Bluetooth.

## Exact pixel-art flow

1. Tap **Conectar gafas** and select the `GLASSES-*` BLE device.
2. Generate or draw a pixel-art image designed for a **36×12 LED grid**. Use
   black for off LEDs and a solid color for on LEDs.
3. Choose the image from the phone.
4. Keep **Import exacto por celdas** selected. The browser splits the original
   image into 36×12 regions and decides each LED by coverage/majority instead
   of sampling a single resized pixel.
5. Adjust **Cobertura mínima**:
   - lower values turn on a LED when only a small part of the cell is colored;
   - higher values require most of the cell to be colored.
6. Use the grid editor to paint, erase, move, mirror, or clear pixels.
7. Tap **Enviar imagen a gafas**.

## Output orientation

The web app applies the hardware's required horizontal mirror internally when
sending designs, so the editor preview can be treated as the source of truth.

## Editing history

The editor supports **Undo** and **Redo** for imports, painting, erasing,
moving, mirroring, clearing, loading designs, and color changes.

For AI-generated images, the best prompt should ask for a strict 36×12 pixel-art
LED matrix, black background, no antialiasing, and solid colors. A larger export
is OK if it is an exact scale-up of the same grid, for example 360×120 where each
LED is a 10×10 block.

The browser converts the editable grid locally to the 36×12 RGB frame format and
sends one DIY frame with the device's `DATSOK`/`REOK`/`DATCPOK` flow control. It
does not alter firmware.

## Design library

The web app stores designs in the shared Supabase library when the backend is
configured. Without Supabase it uses a local browser fallback:

- **Guardar diseño** saves the current 36×12 grid.
- **Cargar** restores a saved design.
- **Borrar** removes it from the library.
- **Exportar JSON** downloads a portable backup.
- **Importar JSON** loads a design exported from another browser or machine.

Saved designs also appear in the **Biblioteca** view as LED-strip previews.
Click a strip to send that design directly to the glasses.

## Custom library remote

The **Biblioteca** view works as a simple custom-design remote:

- **Actualizar lista** refreshes the saved custom list in the interface.
- Clicking a strip sends that single design as a static DIY frame using
  `DATS`/`DATCP`.
- It intentionally does not use `MANY`, because hardware testing showed that
  `MANY` plays the uploaded designs continuously instead of selecting individual
  custom slots.
- Native `IMAG <index>` slots appear to target the official app/firmware designs,
  not this web app's custom library. A native Android HCI capture confirmed that
  switching DIY/custom designs still sends full 1,368-byte `DATS`/`DATCP` uploads,
  while default designs use short `IMAG` index commands.

This does not write into the official Android app database. It keeps the designs
in this web app and sends them directly to the glasses when needed.

## Reference image modes

The other import modes are helpers for rough conversion:

- **Redimensionar 36×12** scales the image down and reads one pixel per LED.
- **Referencia completa** keeps the whole image and may add empty space.
- **Referencia recortada** fills the 36×12 frame and crops the excess.
- **Referencia estirada** forces the image into the LED aspect ratio.

After importing a reference, correct the final pattern in the grid editor before
sending it.
