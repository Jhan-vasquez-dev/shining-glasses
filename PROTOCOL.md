# Shining Glasses 1.2.7 — BLE protocol notes

These notes describe the protocol observed from `com.icwork.shiningglass` 1.2.7
controlling a 36×12 RGB glasses display. They are based on the APK resources and
local Android HCI captures in `captures/`.

## Display

- Logical resolution: 36 columns × 12 rows = 432 pixels.
- Stored color order: RGB, one byte per channel.
- A complete DIY frame is 1,368 bytes:
  - 72-byte active-pixel mask: 36 columns × 2 bytes.
  - 1,296-byte color plane: 36 × 12 × 3 bytes RGB.
- In each two-byte mask, the top pixel uses bit `0x8000`. The following rows are
  expected to descend through the remaining high 12 bits.
- Color bytes are column-major: the RGB triplet for `(column, row)` begins at
  `72 + ((column * 12 + row) * 3)`.
- The app's visible coordinate transform is not a plain left-to-right mapping.
  The web probe originally used `storedColumn = (36 - visibleX) % 36`, but
  hardware testing showed this wraps visible columns `0` and `35` into adjacent
  stored columns. The corrected no-wrap transform used by the probe is
  `storedColumn = 35 - visibleX`.

## GATT profile

Primary service `0000fff0-0000-1000-8000-00805f9b34fb`:

| Handle | UUID | Use |
|---|---|---|
| `0x0012` | `d44bc439-abfd-45a2-b575-925416129600` | AES-encrypted commands |
| `0x0015` | `d44bc439-abfd-45a2-b575-92541612960a` | Bulk image data |
| `0x0018` | `d44bc439-abfd-45a2-b575-92541612960b` | Live DIY pixel updates |
| `0x001b` | `d44bc439-abfd-45a2-b575-925416129601` | Notifications |

The device also exposes service `0xfd00`, with characteristics `0xfd01` and
`0xfd02`; these were not needed for image control.

## Encrypted command framing

- AES-128-ECB, no standard padding.
- Static key: `32672f7974ad43451d9c6c894a0e8764`.
- Plaintext byte 0 is the meaningful body length.
- The body starts at byte 1 and contains an ASCII command followed by binary
  arguments.
- Remaining bytes up to a 16-byte AES block contain arbitrary/random padding and
  must be ignored according to the length byte.

Observed command bodies:

| Body | Meaning |
|---|---|
| `LIGHT <level>` | brightness |
| `ANIM <index>` | built-in animation |
| `SMVEW 01` | enter single-image DIY view |
| `SMVEW 02` | leave/switch DIY view |
| `DATS 05 58 00 48 01` | begin a 1,368-byte image upload |
| `DATCP` | image data transfer complete |
| `MANY <count> 01` | begin a multi-image/animation transaction |
| `MANCPOK` | finish a multi-image transaction |
| `SPEED <value>` | set DIY animation speed |
| `PLAY` | play the uploaded DIY animation; no arguments |
| `IMAG <index>` | select built-in/default image slot |

For this hardware/app combination, the captured encrypted constants are:

- `DATS 05 58 00 48 01` -> `cd047f35d26319c03b743533feb7979c`
- `DATCP` -> `e799ad01aa48ae0aee0b7203e8ede520`

## Live pixel command

Writes to UUID ending in `960b` are unencrypted, 20 bytes long:

```text
05 RR GG BB II 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
```

`II` is the live pixel index. Observed:

- red visible top-left pixel: `05 ff 00 00 00 ...`;
- red visible pixel immediately right: `05 ff 00 00 23 ...`.

The complete interpretation of the live index transform needs one additional
known coordinate.

## Bulk frame upload

1. Send encrypted `DATS` to UUID ending in `9600`.
2. Wait for the device acknowledgement notification (`DATSOK`).
3. Send data packets to UUID ending in `960a`.
4. Send encrypted `DATCP` to UUID ending in `9600`.

The device provides encrypted flow-control notifications on UUID ending in
`9601`:

- `DATSOK` after accepting `DATS`;
- `REOK` after each bulk packet;
- `DATCPOK` after accepting `DATCP`.

The sender must wait for each acknowledgement before continuing.

Each bulk packet is:

```text
[number of following bytes] [sequence] [payload]
```

For a 1,368-byte frame the app sends:

- sequence `00` through `0c`: 98 payload bytes each (`63 <seq> ...`);
- sequence `0d`: 94 payload bytes (`5f 0d ...`);
- total: 14 packets.

Native Android capture `BT_HCI_2026_0622_172404.cfa.curf` confirms that switching
between DIY/custom designs also uses this full upload path. The capture contained
7 `DATS`/`DATCP` uploads, each 1,368 bytes in 14 chunks. After that, switching
default/firmware designs used short `IMAG 00` through `IMAG 13` commands instead.

## Multi-frame animation

The decompiled `DiyMutiAgreement` and `ImageFragment` establish this sequence:

1. Send `MANY <frameCount> 01` and wait for `MANYOK`.
2. Upload every frame using the normal `DATS`/data/`DATCP` sequence.
3. Send `MANCPOK` and wait for `MANCPOK`.
4. Send `SPEED <value>`.
5. Send `PLAY` with no arguments.

Remaining work is to measure the `SPEED` scale and practical frame-count/storage
limits.

The local parser is `tools/parse-btsnoop.mjs`.
