# Shining Glasses LED Editor

Standalone reverse-engineering and web editor project for 36x12 Shining Glasses
LED glasses.

## What This Contains

- `probe/`: web app to draw/import 36x12 designs, save them in a library, and
  send them to the glasses with Web Bluetooth.
- `PROTOCOL.md`: protocol notes discovered from APK/decompiled code and Android
  HCI captures.
- `tools/`: local tooling for parsing Bluetooth captures.
- `apk/`, `decompiled/`, `extracted/`, `firmware/`: app and firmware analysis
  context used during reverse engineering.

Heavy capture files were intentionally not copied into this standalone project.
The useful protocol conclusions are preserved in `PROTOCOL.md` and
`probe/README.md`.

## Run Locally

```sh
cd probe
npm start
```

Open Chrome or Edge:

```text
http://127.0.0.1:8787
```

For Web Bluetooth on a phone or published URL, use HTTPS. `localhost` is the only
HTTP exception supported by browsers.

## Shared Library Deployment

The deployable app lives in `probe/`.

1. Create a Supabase project.
2. Run `probe/supabase-schema.sql` in Supabase SQL Editor.
3. Deploy `probe/` as a Node app on Render, Railway, or Fly.io.
4. Use `npm start` as the start command.
5. Configure:

```text
HOST=0.0.0.0
SUPABASE_URL=https://<project-ref>.supabase.co
SUPABASE_SERVICE_ROLE_KEY=<service-role-key>
LIBRARY_ID=<private-library-name-or-pin>
```

Desktop and mobile browsers that open the same deployed URL will share the same
design library through Supabase.

## Current Protocol Findings

- Custom DIY designs are sent as full 1,368-byte frame uploads using
  `DATS`/data/`DATCP`.
- Native/default visuals use short `IMAG <index>` commands, but those slots point
  to firmware/app visuals, not our custom web library.
- The web editor applies the required horizontal mirror internally before upload.

