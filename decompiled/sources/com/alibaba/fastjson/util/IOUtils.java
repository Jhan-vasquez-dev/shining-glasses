package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson2.internal.asm.Opcodes;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
public class IOUtils {
    public static final char[] CA;
    static final int[] IA;
    public static final Charset UTF8 = StandardCharsets.UTF_8;
    public static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static final boolean[] firstIdentifierFlags = new boolean[256];
    public static final boolean[] identifierFlags = new boolean[256];
    public static final byte[] specicalFlags_doubleQuotes = new byte[Opcodes.IF_ICMPLT];
    public static final byte[] specicalFlags_singleQuotes = new byte[Opcodes.IF_ICMPLT];
    public static final boolean[] specicalFlags_doubleQuotesFlags = new boolean[Opcodes.IF_ICMPLT];
    public static final boolean[] specicalFlags_singleQuotesFlags = new boolean[Opcodes.IF_ICMPLT];
    public static final char[] replaceChars = new char[93];

    static {
        char[] charArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
        CA = charArray;
        int[] iArr = new int[256];
        IA = iArr;
        Arrays.fill(iArr, -1);
        int length = charArray.length;
        for (int i = 0; i < length; i++) {
            IA[CA[i]] = i;
        }
        IA[61] = 0;
        char c = 0;
        while (true) {
            boolean[] zArr = firstIdentifierFlags;
            if (c >= zArr.length) {
                break;
            }
            if (c >= 'A' && c <= 'Z') {
                zArr[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                zArr[c] = true;
            } else if (c == '_' || c == '$') {
                zArr[c] = true;
            }
            c = (char) (c + 1);
        }
        char c2 = 0;
        while (true) {
            boolean[] zArr2 = identifierFlags;
            if (c2 >= zArr2.length) {
                break;
            }
            if (c2 >= 'A' && c2 <= 'Z') {
                zArr2[c2] = true;
            } else if (c2 >= 'a' && c2 <= 'z') {
                zArr2[c2] = true;
            } else if (c2 == '_') {
                zArr2[c2] = true;
            } else if (c2 >= '0' && c2 <= '9') {
                zArr2[c2] = true;
            }
            c2 = (char) (c2 + 1);
        }
        byte[] bArr = specicalFlags_doubleQuotes;
        bArr[0] = 4;
        bArr[1] = 4;
        bArr[2] = 4;
        bArr[3] = 4;
        bArr[4] = 4;
        bArr[5] = 4;
        bArr[6] = 4;
        bArr[7] = 4;
        bArr[8] = 1;
        bArr[9] = 1;
        bArr[10] = 1;
        bArr[11] = 4;
        bArr[12] = 1;
        bArr[13] = 1;
        bArr[34] = 1;
        bArr[92] = 1;
        byte[] bArr2 = specicalFlags_singleQuotes;
        bArr2[0] = 4;
        bArr2[1] = 4;
        bArr2[2] = 4;
        bArr2[3] = 4;
        bArr2[4] = 4;
        bArr2[5] = 4;
        bArr2[6] = 4;
        bArr2[7] = 4;
        bArr2[8] = 1;
        bArr2[9] = 1;
        bArr2[10] = 1;
        bArr2[11] = 4;
        bArr2[12] = 1;
        bArr2[13] = 1;
        bArr2[92] = 1;
        bArr2[39] = 1;
        for (int i2 = 14; i2 <= 31; i2++) {
            specicalFlags_doubleQuotes[i2] = 4;
            specicalFlags_singleQuotes[i2] = 4;
        }
        for (int i3 = 127; i3 < 160; i3++) {
            specicalFlags_doubleQuotes[i3] = 4;
            specicalFlags_singleQuotes[i3] = 4;
        }
        for (int i4 = 0; i4 < 161; i4++) {
            specicalFlags_doubleQuotesFlags[i4] = specicalFlags_doubleQuotes[i4] != 0;
            specicalFlags_singleQuotesFlags[i4] = specicalFlags_singleQuotes[i4] != 0;
        }
        char[] cArr = replaceChars;
        cArr[0] = '0';
        cArr[1] = '1';
        cArr[2] = '2';
        cArr[3] = '3';
        cArr[4] = '4';
        cArr[5] = '5';
        cArr[6] = '6';
        cArr[7] = '7';
        cArr[8] = 'b';
        cArr[9] = 't';
        cArr[10] = 'n';
        cArr[11] = 'v';
        cArr[12] = 'f';
        cArr[13] = 'r';
        cArr[34] = '\"';
        cArr[39] = '\'';
        cArr[47] = '/';
        cArr[92] = TokenParser.ESCAPE;
    }

    public static void decode(CharsetDecoder charsetDecoder, ByteBuffer byteBuffer, CharBuffer charBuffer) {
        try {
            CoderResult coderResultDecode = charsetDecoder.decode(byteBuffer, charBuffer, true);
            if (!coderResultDecode.isUnderflow()) {
                coderResultDecode.throwException();
            }
            CoderResult coderResultFlush = charsetDecoder.flush(charBuffer);
            if (coderResultFlush.isUnderflow()) {
                return;
            }
            coderResultFlush.throwException();
        } catch (CharacterCodingException e) {
            throw new JSONException("utf8 decode error, " + e.getMessage(), e);
        }
    }

    public static byte[] decodeBase64(String str) {
        int i;
        int length = str.length();
        int i2 = 0;
        if (length == 0) {
            return new byte[0];
        }
        int i3 = length - 1;
        int i4 = 0;
        while (i4 < i3 && IA[str.charAt(i4) & 255] < 0) {
            i4++;
        }
        while (i3 > 0 && IA[str.charAt(i3) & 255] < 0) {
            i3--;
        }
        int i5 = str.charAt(i3) == '=' ? str.charAt(i3 + (-1)) == '=' ? 2 : 1 : 0;
        int i6 = (i3 - i4) + 1;
        if (length > 76) {
            i = (str.charAt(76) == '\r' ? i6 / 78 : 0) << 1;
        } else {
            i = 0;
        }
        int i7 = (((i6 - i) * 6) >> 3) - i5;
        byte[] bArr = new byte[i7];
        int i8 = (i7 / 3) * 3;
        int i9 = 0;
        int i10 = 0;
        while (i9 < i8) {
            int[] iArr = IA;
            int i11 = iArr[str.charAt(i4 + 3)] | (iArr[str.charAt(i4)] << 18) | (iArr[str.charAt(i4 + 1)] << 12) | (iArr[str.charAt(i4 + 2)] << 6);
            int i12 = i4 + 4;
            bArr[i9] = (byte) (i11 >> 16);
            bArr[i9 + 1] = (byte) (i11 >> 8);
            bArr[i9 + 2] = (byte) i11;
            i9 += 3;
            if (i <= 0 || (i10 = i10 + 1) != 19) {
                i4 = i12;
            } else {
                i4 += 6;
                i10 = 0;
            }
        }
        if (i9 < i7) {
            int i13 = 0;
            while (i4 <= i3 - i5) {
                i2 |= IA[str.charAt(i4)] << (18 - (i13 * 6));
                i13++;
                i4++;
            }
            int i14 = 16;
            while (i9 < i7) {
                bArr[i9] = (byte) (i2 >> i14);
                i14 -= 8;
                i9++;
            }
        }
        return bArr;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    public static void getChars(byte b, int i, char[] cArr) {
        com.alibaba.fastjson2.util.IOUtils.getChars((int) b, i, cArr);
    }

    public static void getChars(int i, int i2, char[] cArr) {
        com.alibaba.fastjson2.util.IOUtils.getChars(i, i2, cArr);
    }

    public static void getChars(long j, int i, char[] cArr) {
        com.alibaba.fastjson2.util.IOUtils.getChars(j, i, cArr);
    }

    public static int stringSize(int i) {
        return com.alibaba.fastjson2.util.IOUtils.stringSize(i);
    }

    public static int stringSize(long j) {
        return com.alibaba.fastjson2.util.IOUtils.stringSize(j);
    }

    public static int decodeUTF8(byte[] bArr, int i, int i2, char[] cArr) {
        return com.alibaba.fastjson2.util.IOUtils.decodeUTF8(bArr, i, i2, cArr);
    }

    public static boolean isIdent(char c) {
        boolean[] zArr = identifierFlags;
        return c < zArr.length && zArr[c];
    }

    public static boolean isValidJsonpQueryParam(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt != '.' && !isIdent(cCharAt)) {
                return false;
            }
        }
        return true;
    }
}
