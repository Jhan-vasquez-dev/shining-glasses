package com.alibaba.fastjson2.util;

import com.alibaba.fastjson2.JSONWriter;
import java.nio.charset.StandardCharsets;
import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
public class StringUtils {
    protected static final long MASK_ESCAPE_NONE_ASCII = JSONWriter.Feature.EscapeNoneAscii.mask;
    protected static final long MASK_BROWSER_SECURE = JSONWriter.Feature.BrowserSecure.mask;

    public static boolean noneEscaped(long j, long j2) {
        return ((6944656592455360608L + j) & (-9187201950435737472L)) == -9187201950435737472L && (((j2 ^ j) + 72340172838076673L) & (-9187201950435737472L)) == -9187201950435737472L && (((j ^ (-6655295901103053917L)) + 72340172838076673L) & (-9187201950435737472L)) == -9187201950435737472L;
    }

    public static int writeLatin1(byte[] bArr, int i, byte[] bArr2, byte b) {
        int length = bArr2.length;
        bArr[i] = b;
        System.arraycopy(bArr2, 0, bArr, i + 1, length);
        int i2 = i + length;
        bArr[i2 + 1] = b;
        return i2 + 2;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:19:0x002b. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0057  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int writeLatin1Escaped(byte[] r3, int r4, byte[] r5, byte r6, long r7) {
        /*
            long r0 = com.alibaba.fastjson2.util.StringUtils.MASK_BROWSER_SECURE
            long r7 = r7 & r0
            r0 = 0
            int r7 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
            r8 = 0
            r0 = 1
            if (r7 == 0) goto Ld
            r7 = r0
            goto Le
        Ld:
            r7 = r8
        Le:
            int r1 = r4 + 1
            r3[r4] = r6
        L12:
            int r4 = r5.length
            if (r8 >= r4) goto L6b
            r4 = r5[r8]
            r2 = 40
            if (r4 == r2) goto L5d
            r2 = 41
            if (r4 == r2) goto L5d
            r2 = 60
            if (r4 == r2) goto L5d
            r2 = 62
            if (r4 == r2) goto L5d
            r2 = 92
            if (r4 == r2) goto L57
            switch(r4) {
                case 0: goto L51;
                case 1: goto L51;
                case 2: goto L51;
                case 3: goto L51;
                case 4: goto L51;
                case 5: goto L51;
                case 6: goto L51;
                case 7: goto L51;
                case 8: goto L57;
                case 9: goto L57;
                case 10: goto L57;
                case 11: goto L51;
                case 12: goto L57;
                case 13: goto L57;
                case 14: goto L51;
                case 15: goto L51;
                case 16: goto L51;
                case 17: goto L51;
                case 18: goto L51;
                case 19: goto L51;
                case 20: goto L51;
                case 21: goto L51;
                case 22: goto L51;
                case 23: goto L51;
                case 24: goto L51;
                case 25: goto L51;
                case 26: goto L51;
                case 27: goto L51;
                case 28: goto L51;
                case 29: goto L51;
                case 30: goto L51;
                case 31: goto L51;
                default: goto L2e;
            }
        L2e:
            if (r4 != r6) goto L37
            r3[r1] = r2
            int r4 = r1 + 1
            r3[r4] = r6
            goto L5a
        L37:
            if (r4 >= 0) goto L4c
            r2 = r4 & 255(0xff, float:3.57E-43)
            int r2 = r2 >> 6
            r2 = r2 | 192(0xc0, float:2.69E-43)
            byte r2 = (byte) r2
            r3[r1] = r2
            int r2 = r1 + 1
            r4 = r4 & 63
            r4 = r4 | 128(0x80, float:1.794E-43)
            byte r4 = (byte) r4
            r3[r2] = r4
            goto L5a
        L4c:
            int r2 = r1 + 1
            r3[r1] = r4
            goto L67
        L51:
            writeU4Hex2(r3, r1, r4)
        L54:
            int r1 = r1 + 6
            goto L68
        L57:
            writeEscapedChar(r3, r1, r4)
        L5a:
            int r1 = r1 + 2
            goto L68
        L5d:
            if (r7 == 0) goto L63
            writeU4HexU(r3, r1, r4)
            goto L54
        L63:
            int r2 = r1 + 1
            r3[r1] = r4
        L67:
            r1 = r2
        L68:
            int r8 = r8 + 1
            goto L12
        L6b:
            r3[r1] = r6
            int r1 = r1 + r0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.StringUtils.writeLatin1Escaped(byte[], int, byte[], byte, long):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int writeLatin1EscapedRest(char[] r7, int r8, byte[] r9, int r10, char r11, long r12) {
        /*
            com.alibaba.fastjson2.JSONWriter$Feature r0 = com.alibaba.fastjson2.JSONWriter.Feature.EscapeNoneAscii
            long r0 = r0.mask
            long r0 = r0 & r12
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            r1 = 0
            r4 = 1
            if (r0 == 0) goto Lf
            r0 = r4
            goto L10
        Lf:
            r0 = r1
        L10:
            com.alibaba.fastjson2.JSONWriter$Feature r5 = com.alibaba.fastjson2.JSONWriter.Feature.BrowserSecure
            long r5 = r5.mask
            long r12 = r12 & r5
            int r12 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r12 == 0) goto L1a
            r1 = r4
        L1a:
            int r12 = r9.length
            if (r10 >= r12) goto L6e
            r12 = r9[r10]
            r12 = r12 & 255(0xff, float:3.57E-43)
            char r12 = (char) r12
            r13 = 34
            r2 = 92
            if (r12 == r13) goto L5f
            r13 = 60
            if (r12 == r13) goto L54
            r13 = 62
            if (r12 == r13) goto L54
            if (r12 == r2) goto L4e
            switch(r12) {
                case 0: goto L48;
                case 1: goto L48;
                case 2: goto L48;
                case 3: goto L48;
                case 4: goto L48;
                case 5: goto L48;
                case 6: goto L48;
                case 7: goto L48;
                case 8: goto L4e;
                case 9: goto L4e;
                case 10: goto L4e;
                case 11: goto L48;
                case 12: goto L4e;
                case 13: goto L4e;
                case 14: goto L48;
                case 15: goto L48;
                case 16: goto L48;
                case 17: goto L48;
                case 18: goto L48;
                case 19: goto L48;
                case 20: goto L48;
                case 21: goto L48;
                case 22: goto L48;
                case 23: goto L48;
                case 24: goto L48;
                case 25: goto L48;
                case 26: goto L48;
                case 27: goto L48;
                case 28: goto L48;
                case 29: goto L48;
                case 30: goto L48;
                case 31: goto L48;
                default: goto L35;
            }
        L35:
            switch(r12) {
                case 39: goto L5f;
                case 40: goto L54;
                case 41: goto L54;
                default: goto L38;
            }
        L38:
            if (r0 == 0) goto L42
            r13 = 127(0x7f, float:1.78E-43)
            if (r12 <= r13) goto L42
            writeU4HexU(r7, r8, r12)
            goto L4b
        L42:
            int r13 = r8 + 1
            r7[r8] = r12
        L46:
            r8 = r13
            goto L6b
        L48:
            writeU4Hex2(r7, r8, r12)
        L4b:
            int r8 = r8 + 6
            goto L6b
        L4e:
            writeEscapedChar(r7, r8, r12)
            int r8 = r8 + 2
            goto L6b
        L54:
            if (r1 == 0) goto L5a
            writeU4HexU(r7, r8, r12)
            goto L4b
        L5a:
            int r13 = r8 + 1
            r7[r8] = r12
            goto L46
        L5f:
            if (r12 != r11) goto L66
            int r13 = r8 + 1
            r7[r8] = r2
            r8 = r13
        L66:
            int r13 = r8 + 1
            r7[r8] = r12
            goto L46
        L6b:
            int r10 = r10 + 1
            goto L1a
        L6e:
            r7[r8] = r11
            int r8 = r8 + r4
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.StringUtils.writeLatin1EscapedRest(char[], int, byte[], int, char, long):int");
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:24:0x0046. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0060  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int writeUTF16(byte[] r17, int r18, byte[] r19, byte r20, long r21) {
        /*
            Method dump skipped, instruction units count: 340
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.StringUtils.writeUTF16(byte[], int, byte[], byte, long):int");
    }

    public static void writeEscapedChar(byte[] bArr, int i, int i2) {
        IOUtils.putShortLE(bArr, i, LATIN1.ESCAPED_CHARS[i2 & 127]);
    }

    public static void writeU4Hex2(byte[] bArr, int i, int i2) {
        IOUtils.putIntUnaligned(bArr, i, LATIN1.U4);
        IOUtils.putShortLE(bArr, i + 4, IOUtils.hex2(i2));
    }

    public static void writeU4HexU(byte[] bArr, int i, int i2) {
        IOUtils.putShortUnaligned(bArr, i, LATIN1.U2);
        IOUtils.putIntLE(bArr, i + 2, IOUtils.hex4U(i2));
    }

    public static void writeEscapedChar(char[] cArr, int i, int i2) {
        IOUtils.putIntUnaligned(cArr, i, UTF16.ESCAPED_CHARS[i2 & 127]);
    }

    public static void writeU4Hex2(char[] cArr, int i, int i2) {
        IOUtils.putLongUnaligned(cArr, i, UTF16.U4);
        IOUtils.putIntLE(cArr, i + 4, IOUtils.utf16Hex2(i2));
    }

    public static void writeU4HexU(char[] cArr, int i, int i2) {
        IOUtils.putIntUnaligned(cArr, i, UTF16.U2);
        IOUtils.putLongLE(cArr, i + 2, IOUtils.utf16Hex4U(i2));
    }

    public static boolean escaped(byte[] bArr, byte b, long j) {
        int length = bArr.length & (-8);
        int i = 0;
        while (i < length) {
            if (!noneEscaped(IOUtils.getLongUnaligned(bArr, i), j)) {
                return true;
            }
            i += 8;
        }
        while (i < bArr.length) {
            byte b2 = bArr[i];
            if (b2 == b || b2 == 92 || b2 < 32) {
                return true;
            }
            i++;
        }
        return false;
    }

    public static final class LATIN1 {
        private static final short[] ESCAPED_CHARS;
        private static final short U2;
        private static final int U4;

        static {
            byte[] bytes = "\\u00".getBytes(StandardCharsets.UTF_8);
            U2 = JDKUtils.UNSAFE.getShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
            U4 = JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
            short[] sArr = new short[128];
            sArr[92] = (short) 23644;
            sArr[10] = (short) 28252;
            sArr[13] = (short) 29276;
            sArr[12] = (short) 26204;
            sArr[8] = (short) 25180;
            sArr[9] = (short) 29788;
            ESCAPED_CHARS = sArr;
        }
    }

    public static final class UTF16 {
        private static final int[] ESCAPED_CHARS;
        private static final int U2;
        private static final long U4;

        static {
            char[] charArray = "\\u00".toCharArray();
            U2 = JDKUtils.UNSAFE.getInt(charArray, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
            U4 = JDKUtils.UNSAFE.getLong(charArray, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
            char[] cArr = {TokenParser.ESCAPE, TokenParser.ESCAPE, '\n', 'n', TokenParser.CR, 'r', '\f', 'f', '\b', 'b', '\t', 't'};
            char[] cArr2 = {TokenParser.ESCAPE, 0};
            int[] iArr = new int[128];
            for (int i = 0; i < 12; i += 2) {
                cArr2[1] = cArr[i + 1];
                iArr[cArr[i]] = IOUtils.getIntUnaligned(cArr2, 0);
            }
            ESCAPED_CHARS = iArr;
        }
    }
}
