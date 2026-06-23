package com.alibaba.fastjson2.util;

import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.internal.asm.Opcodes;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalTime;
import kotlin.jvm.internal.ByteCompanionObject;
import okhttp3.internal.connection.RealConnection;
import okio.Utf8;

/* JADX INFO: loaded from: classes.dex */
public class IOUtils {
    static final int ALSE;
    static final long ALSE_64;
    public static final int[] DIGITS_K_32;
    public static final long[] DIGITS_K_64;
    public static final long DOT_X0;
    static final short DOT_ZERO_16;
    static final int DOT_ZERO_32;
    private static final byte[] MIN_LONG_BYTES;
    private static final char[] MIN_LONG_CHARS;
    static final int NULL_32;
    static final long NULL_64;
    public static final short[] PACKED_DIGITS;
    public static final int[] PACKED_DIGITS_UTF16;
    static final long[] POWER_TEN;
    static final int TRUE;
    static final long TRUE_64;
    static final short ZERO_DOT_16;
    static final int ZERO_DOT_32;
    private static final short ZERO_DOT_LATIN1;
    static final short ZERO_ZERO_16 = 12336;
    static final int ZERO_ZERO_32 = 3145776;
    static final int[] sizeTable;

    public static int digit(int i) {
        if (i < 0 || i > 9) {
            return -1;
        }
        return i;
    }

    private static int digit3(int i) {
        int i2 = 986895 & i;
        if (((15790320 & (394758 + i2)) | ((i & 15790320) - 3158064)) != 0) {
            return -1;
        }
        return ((((i & 15) * 10) + ((i2 >> 8) & 15)) * 10) + (i2 >> 16);
    }

    private static int digit4(int i) {
        int i2 = 252645135 & i;
        if ((((-252645136) & (101058054 + i2)) | ((i & (-252645136)) - 808464432)) != 0) {
            return -1;
        }
        return ((((((i & 15) * 10) + ((i2 >> 8) & 15)) * 10) + ((i2 >> 16) & 15)) * 10) + (i2 >> 24);
    }

    public static short hex2(int i) {
        int i2 = ((i & 15) << 8) | ((i & 240) >> 4);
        int i3 = (101058054 + i2) & 269488144;
        return (short) ((((i3 << 1) + (i3 >> 1)) - (i3 >> 4)) + 808464432 + i2);
    }

    public static short hex2U(int i) {
        int i2 = ((i & 15) << 8) | ((i & 240) >> 4);
        int i3 = (101058054 + i2) & 269488144;
        return (short) (((i3 >> 1) - (i3 >> 4)) + 808464432 + i2);
    }

    public static boolean isDigit(int i) {
        return i >= 48 && i <= 57;
    }

    private static boolean isDigitLatin1(int i) {
        return i >= 48 && i <= 57;
    }

    private static boolean notContains(long j, long j2) {
        long j3 = j ^ j2;
        return (((~j3) & (j3 - 72340172838076673L)) & (-9187201950435737472L)) == 0;
    }

    private static int reverseBytesExpand(int i) {
        return ((i & 15) << 24) | ((61440 & i) >> 12) | (i & 3840) | ((i & 240) << 12);
    }

    public static int stringSize(long j) {
        long j2 = 10;
        for (int i = 1; i < 19; i++) {
            if (j < j2) {
                return i;
            }
            j2 *= 10;
        }
        return 19;
    }

    public static int utf16Hex2(int i) {
        int i2 = ((i & 15) << 16) | ((i & 240) >> 4);
        int i3 = (393222 + i2) & 1048592;
        return (((i3 << 1) + (i3 >> 1)) - (i3 >> 4)) + ZERO_ZERO_32 + i2;
    }

    private static long utf16ReverseBytesExpand(long j) {
        return ((j & 15) << 48) | ((61440 & j) >> 12) | ((3840 & j) << 8) | ((240 & j) << 28);
    }

    static {
        DOT_ZERO_16 = JDKUtils.BIG_ENDIAN ? (short) 11824 : (short) 12334;
        DOT_ZERO_32 = JDKUtils.BIG_ENDIAN ? 3014704 : 3145774;
        ZERO_DOT_16 = JDKUtils.BIG_ENDIAN ? (short) 12334 : (short) 11824;
        ZERO_DOT_32 = JDKUtils.BIG_ENDIAN ? 3145774 : 3014704;
        NULL_32 = JDKUtils.BIG_ENDIAN ? 1853189228 : 1819047278;
        NULL_64 = JDKUtils.BIG_ENDIAN ? 30962749956423788L : 30399761348886638L;
        TRUE = JDKUtils.BIG_ENDIAN ? 1953658213 : 1702195828;
        TRUE_64 = JDKUtils.BIG_ENDIAN ? 32651586932375653L : 28429475166421108L;
        ALSE = JDKUtils.BIG_ENDIAN ? 1634497381 : 1702063201;
        ALSE_64 = JDKUtils.BIG_ENDIAN ? 27303536604938341L : 28429466576093281L;
        DOT_X0 = JDKUtils.BIG_ENDIAN ? 11776L : 46L;
        sizeTable = new int[]{9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};
        DIGITS_K_32 = new int[1024];
        DIGITS_K_64 = new long[1024];
        MIN_LONG_BYTES = "-9223372036854775808".getBytes();
        MIN_LONG_CHARS = "-9223372036854775808".toCharArray();
        POWER_TEN = new long[]{10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, RealConnection.IDLE_CONNECTION_HEALTHY_NS, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};
        short[] sArr = {ZERO_ZERO_16, 12592, 12848, 13104, 13360, 13616, 13872, 14128, 14384, 14640, 12337, 12593, 12849, 13105, 13361, 13617, 13873, 14129, 14385, 14641, 12338, 12594, 12850, 13106, 13362, 13618, 13874, 14130, 14386, 14642, 12339, 12595, 12851, 13107, 13363, 13619, 13875, 14131, 14387, 14643, 12340, 12596, 12852, 13108, 13364, 13620, 13876, 14132, 14388, 14644, 12341, 12597, 12853, 13109, 13365, 13621, 13877, 14133, 14389, 14645, 12342, 12598, 12854, 13110, 13366, 13622, 13878, 14134, 14390, 14646, 12343, 12599, 12855, 13111, 13367, 13623, 13879, 14135, 14391, 14647, 12344, 12600, 12856, 13112, 13368, 13624, 13880, 14136, 14392, 14648, 12345, 12601, 12857, 13113, 13369, 13625, 13881, 14137, 14393, 14649, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] iArr = {ZERO_ZERO_32, 3211312, 3276848, 3342384, 3407920, 3473456, 3538992, 3604528, 3670064, 3735600, 3145777, 3211313, 3276849, 3342385, 3407921, 3473457, 3538993, 3604529, 3670065, 3735601, 3145778, 3211314, 3276850, 3342386, 3407922, 3473458, 3538994, 3604530, 3670066, 3735602, 3145779, 3211315, 3276851, 3342387, 3407923, 3473459, 3538995, 3604531, 3670067, 3735603, 3145780, 3211316, 3276852, 3342388, 3407924, 3473460, 3538996, 3604532, 3670068, 3735604, 3145781, 3211317, 3276853, 3342389, 3407925, 3473461, 3538997, 3604533, 3670069, 3735605, 3145782, 3211318, 3276854, 3342390, 3407926, 3473462, 3538998, 3604534, 3670070, 3735606, 3145783, 3211319, 3276855, 3342391, 3407927, 3473463, 3538999, 3604535, 3670071, 3735607, 3145784, 3211320, 3276856, 3342392, 3407928, 3473464, 3539000, 3604536, 3670072, 3735608, 3145785, 3211321, 3276857, 3342393, 3407929, 3473465, 3539001, 3604537, 3670073, 3735609, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        PACKED_DIGITS = sArr;
        PACKED_DIGITS_UTF16 = iArr;
        int i = 0;
        while (true) {
            int i2 = 2;
            if (i < 1000) {
                if (i >= 10) {
                    i2 = i < 100 ? 1 : 0;
                }
                int i3 = (i / 100) + 48;
                int i4 = ((i / 10) % 10) + 48;
                int i5 = (i % 10) + 48;
                DIGITS_K_32[i] = (i3 << 8) + i2 + (i4 << 16) + (i5 << 24);
                DIGITS_K_64[i] = ((long) i2) + ((long) (i3 << 16)) + (((long) i4) << 32) + (((long) i5) << 48);
                i++;
            } else {
                ZERO_DOT_LATIN1 = JDKUtils.UNSAFE.getShort(new byte[]{JSONB.Constants.BC_INT32_BYTE_MIN, 46}, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                return;
            }
        }
    }

    private static short digitPair(int i) {
        return PACKED_DIGITS[i & 127];
    }

    public static void writeDigitPair(byte[] bArr, int i, int i2) {
        putShortLE(bArr, i, PACKED_DIGITS[i2 & 127]);
    }

    public static void writeDigitPair(char[] cArr, int i, int i2) {
        putIntLE(cArr, i, PACKED_DIGITS_UTF16[i2 & 127]);
    }

    public static int stringSize(int i) {
        int i2 = 0;
        while (i > sizeTable[i2]) {
            i2++;
        }
        return i2 + 1;
    }

    public static void getChars(int i, int i2, byte[] bArr) {
        int i3;
        boolean z = i < 0;
        if (!z) {
            i = -i;
        }
        while (i <= -100) {
            int i4 = i / 100;
            i2 -= 2;
            writeDigitPair(bArr, i2, (i4 * 100) - i);
            i = i4;
        }
        if (i < -9) {
            i3 = i2 - 2;
            writeDigitPair(bArr, i3, -i);
        } else {
            i3 = i2 - 1;
            putByte(bArr, i3, (byte) (48 - i));
        }
        if (z) {
            putByte(bArr, i3 - 1, (byte) 45);
        }
    }

    public static void getChars(int i, int i2, char[] cArr) {
        int i3;
        boolean z = i < 0;
        if (!z) {
            i = -i;
        }
        while (i <= -100) {
            int i4 = i / 100;
            i2 -= 2;
            writeDigitPair(cArr, i2, (i4 * 100) - i);
            i = i4;
        }
        if (i < -9) {
            i3 = i2 - 2;
            writeDigitPair(cArr, i3, -i);
        } else {
            i3 = i2 - 1;
            putChar(cArr, i3, (char) (48 - i));
        }
        if (z) {
            putChar(cArr, i3 - 1, '-');
        }
    }

    public static void getChars(long j, int i, byte[] bArr) {
        int i2;
        boolean z = j < 0;
        if (!z) {
            j = -j;
        }
        while (j <= -2147483648L) {
            long j2 = j / 100;
            i -= 2;
            writeDigitPair(bArr, i, (int) ((100 * j2) - j));
            j = j2;
        }
        int i3 = (int) j;
        while (i3 <= -100) {
            int i4 = i3 / 100;
            i -= 2;
            writeDigitPair(bArr, i, (i4 * 100) - i3);
            i3 = i4;
        }
        if (i3 < -9) {
            i2 = i - 2;
            writeDigitPair(bArr, i2, -i3);
        } else {
            i2 = i - 1;
            putByte(bArr, i2, (byte) (48 - i3));
        }
        if (z) {
            putByte(bArr, i2 - 1, (byte) 45);
        }
    }

    public static void getChars(long j, int i, char[] cArr) {
        int i2;
        boolean z = j < 0;
        if (!z) {
            j = -j;
        }
        while (j <= -2147483648L) {
            long j2 = j / 100;
            i -= 2;
            writeDigitPair(cArr, i, (int) ((100 * j2) - j));
            j = j2;
        }
        int i3 = (int) j;
        while (i3 <= -100) {
            int i4 = i3 / 100;
            i -= 2;
            writeDigitPair(cArr, i, (i4 * 100) - i3);
            i3 = i4;
        }
        if (i3 < -9) {
            i2 = i - 2;
            writeDigitPair(cArr, i2, -i3);
        } else {
            i2 = i - 1;
            putChar(cArr, i2, (char) (48 - i3));
        }
        if (z) {
            putChar(cArr, i2 - 1, '-');
        }
    }

    public static int writeDecimal(byte[] bArr, int i, long j, int i2) {
        if (j < 0) {
            putByte(bArr, i, (byte) 45);
            j = -j;
            i++;
        }
        if (i2 != 0) {
            int iStringSize = stringSize(j);
            int i3 = iStringSize - i2;
            if (i3 == 0) {
                putShortUnaligned(bArr, i, ZERO_DOT_LATIN1);
                i += 2;
            } else {
                int i4 = 0;
                if (i3 < 0) {
                    putShortUnaligned(bArr, i, ZERO_DOT_LATIN1);
                    i += 2;
                    while (i4 < (-i3)) {
                        putByte(bArr, i, JSONB.Constants.BC_INT32_BYTE_MIN);
                        i4++;
                        i++;
                    }
                } else {
                    long j2 = POWER_TEN[i2 - 1];
                    long j3 = j / j2;
                    long j4 = j - (j2 * j3);
                    int iWriteInt64 = writeInt64(bArr, i, j3);
                    putByte(bArr, iWriteInt64, (byte) 46);
                    if (i2 == 1) {
                        putByte(bArr, iWriteInt64 + 1, (byte) (j4 + 48));
                        return iWriteInt64 + 2;
                    }
                    if (i2 == 2) {
                        writeDigitPair(bArr, iWriteInt64 + 1, (int) j4);
                        return iWriteInt64 + 3;
                    }
                    int iStringSize2 = (iStringSize - stringSize(j4)) - i3;
                    while (i4 < iStringSize2) {
                        iWriteInt64++;
                        putByte(bArr, iWriteInt64, JSONB.Constants.BC_INT32_BYTE_MIN);
                        i4++;
                    }
                    return writeInt64(bArr, iWriteInt64 + 1, j4);
                }
            }
        }
        return writeInt64(bArr, i, j);
    }

    public static int writeDecimal(char[] cArr, int i, long j, int i2) {
        if (j < 0) {
            putChar(cArr, i, '-');
            j = -j;
            i++;
        }
        if (i2 != 0) {
            int iStringSize = stringSize(j);
            int i3 = iStringSize - i2;
            if (i3 == 0) {
                int i4 = i + 1;
                cArr[i] = '0';
                i += 2;
                cArr[i4] = '.';
            } else {
                int i5 = 0;
                if (i3 < 0) {
                    int i6 = i + 1;
                    cArr[i] = '0';
                    i += 2;
                    cArr[i6] = '.';
                    while (i5 < (-i3)) {
                        putChar(cArr, i, '0');
                        i5++;
                        i++;
                    }
                } else {
                    long j2 = POWER_TEN[i2 - 1];
                    long j3 = j / j2;
                    long j4 = j - (j2 * j3);
                    int iWriteInt64 = writeInt64(cArr, i, j3);
                    putChar(cArr, iWriteInt64, '.');
                    if (i2 == 1) {
                        putChar(cArr, iWriteInt64 + 1, (char) (j4 + 48));
                        return iWriteInt64 + 2;
                    }
                    if (i2 == 2) {
                        writeDigitPair(cArr, iWriteInt64 + 1, (int) j4);
                        return iWriteInt64 + 3;
                    }
                    int iStringSize2 = (iStringSize - stringSize(j4)) - i3;
                    while (i5 < iStringSize2) {
                        iWriteInt64++;
                        putChar(cArr, iWriteInt64, '0');
                        i5++;
                    }
                    return writeInt64(cArr, iWriteInt64 + 1, j4);
                }
            }
        }
        return writeInt64(cArr, i, j);
    }

    public static int encodeUTF8(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        byte[] bArr3;
        byte[] bArr4;
        int i4 = i + i2;
        int i5 = i3;
        while (i < i4) {
            char c = JDKUtils.UNSAFE.getChar(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
            int i6 = i + 2;
            if (c < 128) {
                bArr2[i5] = (byte) c;
                bArr3 = bArr;
                i5++;
            } else if (c < 2048) {
                bArr2[i5] = (byte) ((c >> 6) | Opcodes.CHECKCAST);
                bArr2[i5 + 1] = (byte) (128 | (c & '?'));
                i5 += 2;
                bArr3 = bArr;
            } else if (c >= 55296 && c <= 57343) {
                bArr3 = bArr;
                bArr4 = bArr2;
                utf8_char2(bArr3, i6, i4, c, bArr4, i5);
                i += 4;
                i5 += 4;
                bArr = bArr3;
                bArr2 = bArr4;
            } else {
                bArr3 = bArr;
                bArr4 = bArr2;
                bArr4[i5] = (byte) ((c >> '\f') | 224);
                bArr4[i5 + 1] = (byte) (((c >> 6) & 63) | 128);
                bArr4[i5 + 2] = (byte) ((c & '?') | 128);
                i5 += 3;
                i = i6;
                bArr = bArr3;
                bArr2 = bArr4;
            }
            bArr4 = bArr2;
            i = i6;
            bArr = bArr3;
            bArr2 = bArr4;
        }
        return i5;
    }

    public static int encodeUTF8(char[] cArr, int i, int i2, byte[] bArr, int i3) {
        char[] cArr2;
        byte[] bArr2;
        char c;
        int i4 = i + i2;
        int iMin = Math.min(i2, bArr.length) + i3;
        while (i3 < iMin && (c = cArr[i]) < 128) {
            i++;
            bArr[i3] = (byte) c;
            i3++;
        }
        int i5 = i3;
        while (i < i4) {
            int i6 = i + 1;
            char c2 = cArr[i];
            if (c2 < 128) {
                bArr[i5] = (byte) c2;
                cArr2 = cArr;
                i5++;
            } else if (c2 < 2048) {
                bArr[i5] = (byte) ((c2 >> 6) | Opcodes.CHECKCAST);
                bArr[i5 + 1] = (byte) ((c2 & '?') | 128);
                i5 += 2;
                cArr2 = cArr;
            } else if (c2 >= 55296 && c2 <= 57343) {
                cArr2 = cArr;
                bArr2 = bArr;
                utf8_char2(cArr2, i6, i4, c2, bArr2, i5);
                i += 2;
                i5 += 4;
                cArr = cArr2;
                bArr = bArr2;
            } else {
                cArr2 = cArr;
                bArr2 = bArr;
                bArr2[i5] = (byte) ((c2 >> '\f') | 224);
                bArr2[i5 + 1] = (byte) (((c2 >> 6) & 63) | 128);
                bArr2[i5 + 2] = (byte) ((c2 & '?') | 128);
                i5 += 3;
                i = i6;
                cArr = cArr2;
                bArr = bArr2;
            }
            bArr2 = bArr;
            i = i6;
            cArr = cArr2;
            bArr = bArr2;
        }
        return i5;
    }

    private static void utf8_char2(byte[] bArr, int i, int i2, char c, byte[] bArr2, int i3) {
        char c2;
        if (c > 56319 || i2 - i < 1 || (c2 = JDKUtils.UNSAFE.getChar(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i))) < 56320 || c2 > 57343) {
            throw new JSONException("malformed input off : " + i);
        }
        int i4 = ((c << '\n') + c2) - 56613888;
        bArr2[i3] = (byte) ((i4 >> 18) | 240);
        bArr2[i3 + 1] = (byte) (((i4 >> 12) & 63) | 128);
        bArr2[i3 + 2] = (byte) (((i4 >> 6) & 63) | 128);
        bArr2[i3 + 3] = (byte) ((i4 & 63) | 128);
    }

    private static void utf8_char2(char[] cArr, int i, int i2, char c, byte[] bArr, int i3) {
        char c2;
        if (c > 56319 || i2 - i < 1 || (c2 = cArr[i]) < 56320 || c2 > 57343) {
            throw new JSONException("malformed input off : " + i);
        }
        int i4 = ((c << '\n') + c2) - 56613888;
        bArr[i3] = (byte) ((i4 >> 18) | 240);
        bArr[i3 + 1] = (byte) (((i4 >> 12) & 63) | 128);
        bArr[i3 + 2] = (byte) (((i4 >> 6) & 63) | 128);
        bArr[i3 + 3] = (byte) ((i4 & 63) | 128);
    }

    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '+' || cCharAt == '-') {
                if (i != 0) {
                    return false;
                }
            } else if (cCharAt < '0' || cCharAt > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumber(char[] cArr, int i, int i2) {
        int i3 = i2 + i;
        while (i < i3) {
            char c = cArr[i];
            if (c == '+' || c == '-') {
                if (i != 0) {
                    return false;
                }
            } else if (c < '0' || c > '9') {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean isNumber(byte[] bArr, int i, int i2) {
        int i3 = i2 + i;
        while (i < i3) {
            char c = (char) bArr[i];
            if (c == '+' || c == '-') {
                if (i != 0) {
                    return false;
                }
            } else if (c < '0' || c > '9') {
                return false;
            }
            i++;
        }
        return true;
    }

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception unused) {
        }
    }

    public static int decodeUTF8(byte[] bArr, int i, int i2, byte[] bArr2) {
        int i3;
        int i4 = i2 + i;
        int i5 = 0;
        while (i < i4) {
            int i6 = i + 1;
            byte b = bArr[i];
            if (b >= 0) {
                bArr2[i5] = b;
                bArr2[i5 + 1] = 0;
                i5 += 2;
                i = i6;
            } else {
                if ((b >> 5) != -2 || (b & 30) == 0) {
                    if ((b >> 4) == -2) {
                        int i7 = i + 2;
                        if (i7 < i4) {
                            byte b2 = bArr[i6];
                            byte b3 = bArr[i7];
                            i += 3;
                            if ((b != -32 || (b2 & 224) != 128) && (b2 & JSONB.Constants.BC_INT64_SHORT_MIN) == 128 && (b3 & JSONB.Constants.BC_INT64_SHORT_MIN) == 128) {
                                char c = (char) (((b2 << 6) ^ (b << 12)) ^ ((-123008) ^ b3));
                                if (c >= 55296 && c < 57344) {
                                    return -1;
                                }
                                bArr2[i5] = (byte) c;
                                bArr2[i5 + 1] = (byte) (c >> '\b');
                            }
                        }
                        return -1;
                    }
                    if ((b >> 3) == -2 && (i3 = i + 3) < i4) {
                        byte b4 = bArr[i6];
                        byte b5 = bArr[i + 2];
                        byte b6 = bArr[i3];
                        i += 4;
                        int i8 = (((b << 18) ^ (b4 << 12)) ^ (b5 << 6)) ^ (3678080 ^ b6);
                        if ((b4 & JSONB.Constants.BC_INT64_SHORT_MIN) == 128 && (b5 & JSONB.Constants.BC_INT64_SHORT_MIN) == 128 && (b6 & JSONB.Constants.BC_INT64_SHORT_MIN) == 128 && i8 >= 65536 && i8 < 1114112) {
                            char c2 = (char) ((i8 >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                            bArr2[i5] = (byte) c2;
                            bArr2[i5 + 1] = (byte) (c2 >> '\b');
                            char c3 = (char) ((i8 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                            bArr2[i5 + 2] = (byte) c3;
                            bArr2[i5 + 3] = (byte) (c3 >> '\b');
                            i5 += 4;
                        }
                    }
                    return -1;
                }
                if (i6 < i4) {
                    i += 2;
                    byte b7 = bArr[i6];
                    if ((b7 & JSONB.Constants.BC_INT64_SHORT_MIN) != 128) {
                        return -1;
                    }
                    char c4 = (char) ((b7 ^ (b << 6)) ^ Utf8.MASK_2BYTES);
                    bArr2[i5] = (byte) c4;
                    bArr2[i5 + 1] = (byte) (c4 >> '\b');
                } else {
                    bArr2[i5] = b;
                    bArr2[i5 + 1] = 0;
                    return i5 + 2;
                }
                i5 += 2;
            }
        }
        return i5;
    }

    public static int decodeUTF8(byte[] bArr, int i, int i2, char[] cArr) {
        int i3;
        int i4 = i + i2;
        int iMin = Math.min(i2, cArr.length);
        int i5 = 0;
        while (i5 < iMin) {
            byte b = bArr[i];
            if (b < 0) {
                break;
            }
            i++;
            cArr[i5] = (char) b;
            i5++;
        }
        while (i < i4) {
            int i6 = i + 1;
            byte b2 = bArr[i];
            if (b2 >= 0) {
                cArr[i5] = (char) b2;
                i5++;
                i = i6;
            } else {
                if ((b2 >> 5) != -2 || (b2 & 30) == 0) {
                    if ((b2 >> 4) == -2) {
                        int i7 = i + 2;
                        if (i7 < i4) {
                            byte b3 = bArr[i6];
                            byte b4 = bArr[i7];
                            i += 3;
                            if ((b2 != -32 || (b3 & 224) != 128) && (b3 & JSONB.Constants.BC_INT64_SHORT_MIN) == 128 && (b4 & JSONB.Constants.BC_INT64_SHORT_MIN) == 128) {
                                char c = (char) (((b3 << 6) ^ (b2 << 12)) ^ ((-123008) ^ b4));
                                if (c >= 55296 && c < 57344) {
                                    return -1;
                                }
                                cArr[i5] = c;
                                i5++;
                            }
                        }
                        return -1;
                    }
                    if ((b2 >> 3) == -2 && (i3 = i + 3) < i4) {
                        byte b5 = bArr[i6];
                        byte b6 = bArr[i + 2];
                        byte b7 = bArr[i3];
                        i += 4;
                        int i8 = (((b2 << 18) ^ (b5 << 12)) ^ (b6 << 6)) ^ (3678080 ^ b7);
                        if ((b5 & JSONB.Constants.BC_INT64_SHORT_MIN) == 128 && (b6 & JSONB.Constants.BC_INT64_SHORT_MIN) == 128 && (b7 & JSONB.Constants.BC_INT64_SHORT_MIN) == 128 && i8 >= 65536 && i8 < 1114112) {
                            cArr[i5] = (char) ((i8 >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                            cArr[i5 + 1] = (char) ((i8 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                            i5 += 2;
                        }
                    }
                    return -1;
                }
                if (i6 >= i4) {
                    return -1;
                }
                i += 2;
                byte b8 = bArr[i6];
                if ((b8 & JSONB.Constants.BC_INT64_SHORT_MIN) != 128) {
                    return -1;
                }
                cArr[i5] = (char) ((b8 ^ (b2 << 6)) ^ Utf8.MASK_2BYTES);
                i5++;
            }
        }
        return i5;
    }

    public static long lines(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            long jLines = lines(fileInputStream);
            fileInputStream.close();
            return jLines;
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static long lines(InputStream inputStream) throws Exception {
        byte[] bArr = new byte[8192];
        long j = 0;
        while (true) {
            int i = 0;
            int i2 = inputStream.read(bArr, 0, 8192);
            if (i2 == -1) {
                return j;
            }
            int i3 = i2 & (-8);
            for (long j2 = JDKUtils.ARRAY_BYTE_BASE_OFFSET; i < i3 && notContains(JDKUtils.UNSAFE.getLong(bArr, j2), 723401728380766730L); j2 += 8) {
                i += 8;
            }
            while (i < i2) {
                if (bArr[i] == 10) {
                    j++;
                }
                i++;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int writeLocalDate(byte[] r4, int r5, int r6, int r7, int r8) {
        /*
            if (r6 >= 0) goto Lb
            int r0 = r5 + 1
            r1 = 45
            r4[r5] = r1
            int r6 = -r6
        L9:
            r5 = r0
            goto L16
        Lb:
            r0 = 9999(0x270f, float:1.4012E-41)
            if (r6 <= r0) goto L16
            int r0 = r5 + 1
            r1 = 43
            r4[r5] = r1
            goto L9
        L16:
            long r0 = (long) r6
            r2 = 1374389535(0x51eb851f, double:6.790386532E-315)
            long r0 = r0 * r2
            r2 = 37
            long r0 = r0 >> r2
            int r0 = (int) r0
            int r1 = r0 * 100
            int r1 = r6 - r1
            if (r6 < 0) goto L2f
            r2 = 10000(0x2710, float:1.4013E-41)
            if (r6 >= r2) goto L2f
            writeDigitPair(r4, r5, r0)
            int r5 = r5 + 2
            goto L34
        L2f:
            long r2 = (long) r0
            int r5 = writeInt32(r4, r5, r2)
        L34:
            short r6 = digitPair(r1)
            long r0 = (long) r6
            r2 = 49478026199040(0x2d00002d0000, double:2.4445392968979E-310)
            long r0 = r0 | r2
            short r6 = digitPair(r7)
            long r6 = (long) r6
            r2 = 24
            long r6 = r6 << r2
            long r6 = r6 | r0
            short r8 = digitPair(r8)
            long r0 = (long) r8
            r8 = 48
            long r0 = r0 << r8
            long r6 = r6 | r0
            putLongLE(r4, r5, r6)
            int r5 = r5 + 8
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.IOUtils.writeLocalDate(byte[], int, int, int, int):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int writeLocalDate(char[] r6, int r7, int r8, int r9, int r10) {
        /*
            if (r8 >= 0) goto Lb
            int r0 = r7 + 1
            r1 = 45
            r6[r7] = r1
            int r8 = -r8
        L9:
            r7 = r0
            goto L16
        Lb:
            r0 = 9999(0x270f, float:1.4012E-41)
            if (r8 <= r0) goto L16
            int r0 = r7 + 1
            r1 = 43
            r6[r7] = r1
            goto L9
        L16:
            long r0 = (long) r8
            r2 = 1374389535(0x51eb851f, double:6.790386532E-315)
            long r0 = r0 * r2
            r2 = 37
            long r0 = r0 >> r2
            int r0 = (int) r0
            int r1 = r0 * 100
            int r1 = r8 - r1
            if (r8 < 0) goto L2f
            r2 = 10000(0x2710, float:1.4013E-41)
            if (r8 >= r2) goto L2f
            writeDigitPair(r6, r7, r0)
            int r7 = r7 + 2
            goto L34
        L2f:
            long r2 = (long) r0
            int r7 = writeInt32(r6, r7, r2)
        L34:
            int[] r8 = com.alibaba.fastjson2.util.IOUtils.PACKED_DIGITS_UTF16
            r9 = r9 & 127(0x7f, float:1.78E-43)
            r9 = r8[r9]
            r0 = 65535(0xffff, float:9.1834E-41)
            r0 = r0 & r9
            long r2 = (long) r0
            r0 = 48
            long r2 = r2 << r0
            r4 = 193273528320(0x2d00000000, double:9.54898105934E-313)
            long r2 = r2 | r4
            r0 = r1 & 127(0x7f, float:1.78E-43)
            r0 = r8[r0]
            long r0 = (long) r0
            long r0 = r0 | r2
            putLongLE(r6, r7, r0)
            int r0 = r7 + 4
            r1 = -65536(0xffffffffffff0000, float:NaN)
            r9 = r9 & r1
            long r1 = (long) r9
            r9 = 16
            long r1 = r1 >> r9
            r3 = 2949120(0x2d0000, double:1.457059E-317)
            long r1 = r1 | r3
            r9 = r10 & 127(0x7f, float:1.78E-43)
            r8 = r8[r9]
            long r8 = (long) r8
            r10 = 32
            long r8 = r8 << r10
            long r8 = r8 | r1
            putLongLE(r6, r0, r8)
            int r7 = r7 + 8
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.IOUtils.writeLocalDate(char[], int, int, int, int):int");
    }

    public static void writeLocalTime(byte[] bArr, int i, int i2, int i3, int i4) {
        putLongLE(bArr, i, (((long) digitPair(i3)) << 24) | ((long) digitPair(i2)) | 63771678212096L | (((long) digitPair(i4)) << 48));
    }

    public static int writeLocalTime(byte[] bArr, int i, LocalTime localTime) {
        writeLocalTime(bArr, i, localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        int i2 = i + 8;
        int nano = localTime.getNano();
        return nano != 0 ? writeNano(bArr, i2, nano) : i2;
    }

    public static int writeNano(byte[] bArr, int i, int i2) {
        int i3;
        int i4 = (int) ((((long) i2) * 274877907) >> 38);
        int i5 = (int) ((((long) i4) * 274877907) >> 38);
        int i6 = i2 - (i4 * 1000);
        int[] iArr = DIGITS_K_32;
        putIntLE(bArr, i, (iArr[i5 & 1023] & InputDeviceCompat.SOURCE_ANY) | 46);
        int i7 = i + 4;
        if (i6 == 0) {
            int i8 = i4 - (i5 * 1000);
            if (i8 == 0) {
                return i7;
            }
            i3 = iArr[i8 & 1023];
        } else {
            i3 = iArr[(i4 - (i5 * 1000)) & 1023];
        }
        putShortLE(bArr, i7, (short) (i3 >> 8));
        int i9 = i + 6;
        if (i6 == 0) {
            putByte(bArr, i9, (byte) (i3 >> 24));
            return i + 7;
        }
        putIntLE(bArr, i9, (iArr[i6] & InputDeviceCompat.SOURCE_ANY) | (i3 >> 24));
        return i + 10;
    }

    public static int writeNano(char[] cArr, int i, int i2) {
        long j;
        int i3 = (int) ((((long) i2) * 274877907) >> 38);
        int i4 = (int) ((((long) i3) * 274877907) >> 38);
        int i5 = i2 - (i3 * 1000);
        long[] jArr = DIGITS_K_64;
        putLongLE(cArr, i, (jArr[i4 & 1023] & (-65536)) | DOT_X0);
        int i6 = i + 4;
        if (i5 == 0) {
            int i7 = i3 - (i4 * 1000);
            if (i7 == 0) {
                return i6;
            }
            j = jArr[i7 & 1023];
        } else {
            j = jArr[(i3 - (i4 * 1000)) & 1023];
        }
        putIntLE(cArr, i6, (int) (j >> 16));
        int i8 = i + 6;
        if (i5 == 0) {
            putChar(cArr, i8, (char) (j >> 48));
            return i + 7;
        }
        putLongLE(cArr, i8, (j >> 48) | ((-65536) & jArr[i5 & 1023]));
        return i + 10;
    }

    public static void writeLocalTime(char[] cArr, int i, int i2, int i3, int i4) {
        writeDigitPair(cArr, i, i2);
        putChar(cArr, i + 2, ':');
        writeDigitPair(cArr, i + 3, i3);
        putChar(cArr, i + 5, ':');
        writeDigitPair(cArr, i + 6, i4);
    }

    public static int writeLocalTime(char[] cArr, int i, LocalTime localTime) {
        writeLocalTime(cArr, i, localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        int i2 = i + 8;
        int nano = localTime.getNano();
        return nano != 0 ? writeNano(cArr, i2, nano) : i2;
    }

    private static int writeInt4(byte[] bArr, int i, int i2) {
        int i3 = (int) ((((long) i2) * 1374389535) >> 37);
        int i4 = i2 - (i3 * 100);
        short[] sArr = PACKED_DIGITS;
        int iReverseBytes = (sArr[i4 & 127] << 16) | sArr[i3 & 127];
        if (JDKUtils.BIG_ENDIAN) {
            iReverseBytes = Integer.reverseBytes(iReverseBytes);
        }
        JDKUtils.UNSAFE.putInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), iReverseBytes);
        return i + 4;
    }

    private static int writeInt4(char[] cArr, int i, int i2) {
        int i3 = (int) ((((long) i2) * 1374389535) >> 37);
        putLongUnaligned(cArr, i, mergeInt64(i2 - (i3 * 100), i3));
        return i + 4;
    }

    private static long mergeInt64(int i, int i2) {
        int[] iArr = PACKED_DIGITS_UTF16;
        long j = (((long) iArr[i & 127]) << 32) | ((long) iArr[i2 & 127]);
        return JDKUtils.BIG_ENDIAN ? Long.reverseBytes(j) : j;
    }

    private static int writeInt3(byte[] bArr, int i, int i2) {
        int i3 = DIGITS_K_32[i2 & 1023];
        byte b = (byte) i3;
        JDKUtils.UNSAFE.putInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), i3 >> ((b + 1) << 3));
        return (i + 3) - b;
    }

    private static int writeInt3(char[] cArr, int i, int i2) {
        long j = DIGITS_K_64[i2 & 1023];
        int i3 = (int) j;
        JDKUtils.UNSAFE.putLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1), j >> ((((short) i3) + 1) << 4));
        return (i + 3) - ((byte) i3);
    }

    private static int writeInt8(byte[] bArr, int i, int i2, int i3) {
        int i4 = (int) ((((long) i2) * 1374389535) >> 37);
        int i5 = (int) ((((long) i3) * 1374389535) >> 37);
        short[] sArr = PACKED_DIGITS;
        long jReverseBytes = (((long) sArr[(i3 - (i5 * 100)) & 127]) << 48) | ((long) ((sArr[(i2 - (i4 * 100)) & 127] << 16) | sArr[i4 & 127])) | (((long) sArr[i5 & 127]) << 32);
        if (JDKUtils.BIG_ENDIAN) {
            jReverseBytes = Long.reverseBytes(jReverseBytes);
        }
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), jReverseBytes);
        return i + 8;
    }

    private static int writeInt8(char[] cArr, int i, int i2, int i3) {
        int i4 = (int) ((((long) i2) * 1374389535) >> 37);
        int[] iArr = PACKED_DIGITS_UTF16;
        long jReverseBytes = ((long) iArr[i4 & 127]) | (((long) iArr[(i2 - (i4 * 100)) & 127]) << 32);
        int i5 = (int) ((((long) i3) * 1374389535) >> 37);
        long jReverseBytes2 = (((long) iArr[(i3 - (i5 * 100)) & 127]) << 32) | ((long) iArr[i5 & 127]);
        if (JDKUtils.BIG_ENDIAN) {
            jReverseBytes = Long.reverseBytes(jReverseBytes);
            jReverseBytes2 = Long.reverseBytes(jReverseBytes2);
        }
        long j = jReverseBytes2;
        long j2 = ((long) i) << 1;
        JDKUtils.UNSAFE.putLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + j2, jReverseBytes);
        JDKUtils.UNSAFE.putLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + j2 + 8, j);
        return i + 8;
    }

    public static int writeInt64(byte[] bArr, int i, long j) {
        int iWriteInt4;
        int iWriteInt42;
        if (j < 0) {
            if (j == Long.MIN_VALUE) {
                byte[] bArr2 = MIN_LONG_BYTES;
                System.arraycopy(bArr2, 0, bArr, i, bArr2.length);
                return i + bArr2.length;
            }
            j = -j;
            putByte(bArr, i, (byte) 45);
            i++;
        }
        if (j <= 2147483647L) {
            return writeInt32(bArr, i, (int) j);
        }
        long jMultiplyHigh = NumberUtils.MULTIPLY_HIGH.multiplyHigh(j, 7555786372591432342L) >> 12;
        int i2 = (int) (j - (jMultiplyHigh * 10000));
        long jMultiplyHigh2 = NumberUtils.MULTIPLY_HIGH.multiplyHigh(jMultiplyHigh, 1844674407370956L);
        int i3 = (int) (jMultiplyHigh - (jMultiplyHigh2 * 10000));
        if (jMultiplyHigh2 < 10000) {
            int i4 = (int) jMultiplyHigh2;
            if (i4 < 1000) {
                iWriteInt42 = writeInt3(bArr, i, i4);
            } else {
                iWriteInt42 = writeInt4(bArr, i, i4);
            }
            return writeInt8(bArr, iWriteInt42, i3, i2);
        }
        long jMultiplyHigh3 = NumberUtils.MULTIPLY_HIGH.multiplyHigh(jMultiplyHigh2, 1844674407370956L);
        int i5 = (int) (jMultiplyHigh2 - (jMultiplyHigh3 * 10000));
        if (jMultiplyHigh3 < 10000) {
            int i6 = (int) jMultiplyHigh3;
            if (i6 < 1000) {
                iWriteInt4 = writeInt4(bArr, writeInt3(bArr, i, i6), i5);
            } else {
                writeInt8(bArr, i, i6, i5);
                iWriteInt4 = i + 8;
            }
            return writeInt8(bArr, iWriteInt4, i3, i2);
        }
        long jMultiplyHigh4 = NumberUtils.MULTIPLY_HIGH.multiplyHigh(jMultiplyHigh3, 1844674407370956L);
        return writeInt8(bArr, writeInt8(bArr, writeInt3(bArr, i, (int) jMultiplyHigh4), (int) (jMultiplyHigh3 - (10000 * jMultiplyHigh4)), i5), i3, i2);
    }

    public static int writeInt64(char[] cArr, int i, long j) {
        int iWriteInt4;
        int iWriteInt42;
        if (j < 0) {
            if (j == Long.MIN_VALUE) {
                char[] cArr2 = MIN_LONG_CHARS;
                System.arraycopy(cArr2, 0, cArr, i, cArr2.length);
                return i + cArr2.length;
            }
            j = -j;
            putChar(cArr, i, '-');
            i++;
        }
        if (j <= 2147483647L) {
            return writeInt32(cArr, i, (int) j);
        }
        long jMultiplyHigh = NumberUtils.MULTIPLY_HIGH.multiplyHigh(j, 7555786372591432342L) >> 12;
        int i2 = (int) (j - (jMultiplyHigh * 10000));
        long jMultiplyHigh2 = NumberUtils.MULTIPLY_HIGH.multiplyHigh(jMultiplyHigh, 1844674407370956L);
        int i3 = (int) (jMultiplyHigh - (jMultiplyHigh2 * 10000));
        if (jMultiplyHigh2 < 10000) {
            int i4 = (int) jMultiplyHigh2;
            if (i4 < 1000) {
                iWriteInt42 = writeInt3(cArr, i, i4);
            } else {
                iWriteInt42 = writeInt4(cArr, i, i4);
            }
            return writeInt8(cArr, iWriteInt42, i3, i2);
        }
        long jMultiplyHigh3 = NumberUtils.MULTIPLY_HIGH.multiplyHigh(jMultiplyHigh2, 1844674407370956L);
        int i5 = (int) (jMultiplyHigh2 - (jMultiplyHigh3 * 10000));
        if (jMultiplyHigh3 < 10000) {
            int i6 = (int) jMultiplyHigh3;
            if (i6 < 1000) {
                iWriteInt4 = writeInt4(cArr, writeInt3(cArr, i, i6), i5);
            } else {
                writeInt8(cArr, i, i6, i5);
                iWriteInt4 = i + 8;
            }
            return writeInt8(cArr, iWriteInt4, i3, i2);
        }
        long jMultiplyHigh4 = NumberUtils.MULTIPLY_HIGH.multiplyHigh(jMultiplyHigh3, 1844674407370956L);
        return writeInt8(cArr, writeInt8(cArr, writeInt3(cArr, i, (int) jMultiplyHigh4), (int) (jMultiplyHigh3 - (10000 * jMultiplyHigh4)), i5), i3, i2);
    }

    public static int writeInt8(byte[] bArr, int i, byte b) {
        int i2 = b;
        if (b < 0) {
            putByte(bArr, i, (byte) 45);
            i++;
            i2 = -b;
        }
        int i3 = DIGITS_K_32[i2 & 1023];
        byte b2 = (byte) i3;
        if (b2 == 0) {
            putShortLE(bArr, i, (short) (i3 >> 8));
            i += 2;
        } else if (b2 == 1) {
            putByte(bArr, i, (byte) (i3 >> 16));
            i++;
        }
        putByte(bArr, i, (byte) (i3 >> 24));
        return i + 1;
    }

    public static int writeInt8(char[] cArr, int i, byte b) {
        int i2 = b;
        if (b < 0) {
            putChar(cArr, i, '-');
            i++;
            i2 = -b;
        }
        long j = DIGITS_K_64[i2 & 1023];
        byte b2 = (byte) j;
        if (b2 == 0) {
            putIntLE(cArr, i, (int) (j >> 16));
            i += 2;
        } else if (b2 == 1) {
            putChar(cArr, i, (char) (j >> 32));
            i++;
        }
        putChar(cArr, i, (char) (j >> 48));
        return i + 1;
    }

    public static int writeInt16(byte[] bArr, int i, short s) {
        int i2 = s;
        if (s < 0) {
            putByte(bArr, i, (byte) 45);
            i++;
            i2 = -s;
        }
        if (i2 < 1000) {
            int i3 = DIGITS_K_32[i2 & 1023];
            byte b = (byte) i3;
            if (b == 0) {
                putShortLE(bArr, i, (short) (i3 >> 8));
                i += 2;
            } else if (b == 1) {
                putByte(bArr, i, (byte) (i3 >> 16));
                i++;
            }
            putByte(bArr, i, (byte) (i3 >> 24));
            return i + 1;
        }
        int i4 = (int) ((((long) i2) * 274877907) >> 38);
        int[] iArr = DIGITS_K_32;
        int i5 = iArr[i4 & 1023];
        if (((byte) i5) == 1) {
            putByte(bArr, i, (byte) (i5 >> 16));
            i++;
        }
        putIntLE(bArr, i, (iArr[(i2 - (i4 * 1000)) & 1023] & InputDeviceCompat.SOURCE_ANY) | (i5 >> 24));
        return i + 4;
    }

    public static int writeInt16(char[] cArr, int i, short s) {
        int i2 = s;
        if (s < 0) {
            putChar(cArr, i, '-');
            i++;
            i2 = -s;
        }
        if (i2 < 1000) {
            long j = DIGITS_K_64[i2 & 1023];
            byte b = (byte) j;
            if (b == 0) {
                putIntLE(cArr, i, (int) (j >> 16));
                i += 2;
            } else if (b == 1) {
                putChar(cArr, i, (char) (j >> 32));
                i++;
            }
            putChar(cArr, i, (char) (j >> 48));
            return i + 1;
        }
        int i3 = (int) ((((long) i2) * 274877907) >> 38);
        long[] jArr = DIGITS_K_64;
        long j2 = jArr[i3 & 1023];
        if (((byte) j2) == 1) {
            putChar(cArr, i, (char) (j2 >> 32));
            i++;
        }
        putLongLE(cArr, i, (j2 >> 48) | (jArr[(i2 - (i3 * 1000)) & 1023] & (-65536)));
        return i + 4;
    }

    public static int writeInt32(byte[] bArr, int i, long j) {
        if (j < 0) {
            j = -j;
            putByte(bArr, i, (byte) 45);
            i++;
        }
        if (j < 10000) {
            int i2 = (int) j;
            if (i2 < 1000) {
                return writeInt3(bArr, i, i2);
            }
            return writeInt4(bArr, i, i2);
        }
        long j2 = (int) ((j * 1759218605) >> 44);
        int i3 = (int) (j - (j2 * 10000));
        if (j2 >= 10000) {
            long j3 = (int) ((1759218605 * j2) >> 44);
            return writeInt8(bArr, writeInt3(bArr, i, (int) j3), (int) (j2 - (j3 * 10000)), i3);
        }
        int i4 = (int) j2;
        if (i4 < 1000) {
            return writeInt4(bArr, writeInt3(bArr, i, i4), i3);
        }
        return writeInt8(bArr, i, i4, i3);
    }

    public static int writeInt32(char[] cArr, int i, long j) {
        if (j < 0) {
            j = -j;
            putChar(cArr, i, '-');
            i++;
        }
        if (j < 10000) {
            int i2 = (int) j;
            if (i2 < 1000) {
                return writeInt3(cArr, i, i2);
            }
            return writeInt4(cArr, i, i2);
        }
        long j2 = (int) ((j * 1759218605) >> 44);
        int i3 = (int) (j - (j2 * 10000));
        if (j2 >= 10000) {
            long j3 = (int) ((1759218605 * j2) >> 44);
            return writeInt8(cArr, writeInt3(cArr, i, (int) j3), (int) (j2 - (j3 * 10000)), i3);
        }
        int i4 = (int) j2;
        if (i4 < 1000) {
            return writeInt4(cArr, writeInt3(cArr, i, i4), i3);
        }
        return writeInt8(cArr, i, i4, i3);
    }

    public static byte getByte(byte[] bArr, int i) {
        return JDKUtils.UNSAFE.getByte(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
    }

    public static char getChar(char[] cArr, int i) {
        return JDKUtils.UNSAFE.getChar(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1));
    }

    public static char getChar(byte[] bArr, int i) {
        return JDKUtils.UNSAFE.getChar(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + (((long) i) << 1));
    }

    private static void putByte(byte[] bArr, int i, byte b) {
        JDKUtils.UNSAFE.putByte(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), b);
    }

    private static void putChar(char[] cArr, int i, char c) {
        JDKUtils.UNSAFE.putChar(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1), c);
    }

    public static void putShortBE(byte[] bArr, int i, short s) {
        JDKUtils.UNSAFE.putShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), convEndian(true, s));
    }

    public static void putShortLE(byte[] bArr, int i, short s) {
        JDKUtils.UNSAFE.putShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), convEndian(false, s));
    }

    public static void putIntBE(byte[] bArr, int i, int i2) {
        if (!JDKUtils.BIG_ENDIAN) {
            i2 = Integer.reverseBytes(i2);
        }
        JDKUtils.UNSAFE.putInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), i2);
    }

    public static void putIntLE(byte[] bArr, int i, int i2) {
        if (JDKUtils.BIG_ENDIAN) {
            i2 = Integer.reverseBytes(i2);
        }
        JDKUtils.UNSAFE.putInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), i2);
    }

    public static void putIntLE(char[] cArr, int i, int i2) {
        if (JDKUtils.BIG_ENDIAN) {
            i2 = Integer.reverseBytes(i2);
        }
        JDKUtils.UNSAFE.putInt(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1), i2);
    }

    public static void putShortUnaligned(byte[] bArr, int i, short s) {
        JDKUtils.UNSAFE.putShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), s);
    }

    public static void putIntUnaligned(char[] cArr, int i, int i2) {
        JDKUtils.UNSAFE.putInt(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1), i2);
    }

    public static void putIntUnaligned(byte[] bArr, int i, int i2) {
        JDKUtils.UNSAFE.putInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), i2);
    }

    public static void putLongLE(char[] cArr, int i, long j) {
        JDKUtils.UNSAFE.putLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1), convEndian(false, j));
    }

    public static void putLongUnaligned(char[] cArr, int i, long j) {
        JDKUtils.UNSAFE.putLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1), j);
    }

    public static void putLongUnaligned(byte[] bArr, int i, long j) {
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), j);
    }

    public static void putLongBE(byte[] bArr, int i, long j) {
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), convEndian(true, j));
    }

    public static void putLongLE(byte[] bArr, int i, long j) {
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), convEndian(false, j));
    }

    public static int putBoolean(byte[] bArr, int i, boolean z) {
        long j = JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i);
        if (z) {
            JDKUtils.UNSAFE.putInt(bArr, j, TRUE);
            return i + 4;
        }
        JDKUtils.UNSAFE.putByte(bArr, j, (byte) 102);
        JDKUtils.UNSAFE.putInt(bArr, j + 1, ALSE);
        return i + 5;
    }

    public static int putBoolean(char[] cArr, int i, boolean z) {
        long j = JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1);
        if (z) {
            JDKUtils.UNSAFE.putLong(cArr, j, TRUE_64);
            return i + 4;
        }
        JDKUtils.UNSAFE.putChar(cArr, j, 'f');
        JDKUtils.UNSAFE.putLong(cArr, 2 + j, ALSE_64);
        return i + 5;
    }

    public static boolean isALSE(byte[] bArr, int i) {
        return JDKUtils.UNSAFE.getInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i)) == ALSE;
    }

    public static boolean notALSE(byte[] bArr, int i) {
        return JDKUtils.UNSAFE.getInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i)) != ALSE;
    }

    public static boolean isALSE(char[] cArr, int i) {
        return getLongUnaligned(cArr, i) == ALSE_64;
    }

    public static boolean notALSE(char[] cArr, int i) {
        return getLongUnaligned(cArr, i) != ALSE_64;
    }

    public static boolean isNULL(byte[] bArr, int i) {
        return JDKUtils.UNSAFE.getInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i)) == NULL_32;
    }

    public static boolean notNULL(byte[] bArr, int i) {
        return JDKUtils.UNSAFE.getInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i)) != NULL_32;
    }

    public static boolean notTRUE(byte[] bArr, int i) {
        return JDKUtils.UNSAFE.getInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i)) != TRUE;
    }

    public static boolean notTRUE(char[] cArr, int i) {
        return JDKUtils.UNSAFE.getLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1)) != TRUE_64;
    }

    public static boolean isNULL(char[] cArr, int i) {
        return getLongUnaligned(cArr, i) == NULL_64;
    }

    public static boolean notNULL(char[] cArr, int i) {
        return getLongUnaligned(cArr, i) != NULL_64;
    }

    public static void putNULL(byte[] bArr, int i) {
        JDKUtils.UNSAFE.putInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), NULL_32);
    }

    public static void putNULL(char[] cArr, int i) {
        JDKUtils.UNSAFE.putLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1), NULL_64);
    }

    public static int digit4(char[] cArr, int i) {
        long longLE = getLongLE(cArr, i);
        long j = 4222189076152335L & longLE;
        if (((((-4222189076152336L) & longLE) - 13511005043687472L) | ((1688875630460934L + j) & 67555025218437360L)) != 0) {
            return -1;
        }
        return (int) (((((((longLE & 15) * 10) + ((j >> 16) & 15)) * 10) + (15 & (j >> 32))) * 10) + (j >> 48));
    }

    public static int digit4(byte[] bArr, int i) {
        return digit4(getIntLE(bArr, i));
    }

    public static int digit3(char[] cArr, int i) {
        long intLE = ((long) getIntLE(cArr, i)) + (((long) getChar(cArr, i + 2)) << 32);
        long j = 64425492495L & intLE;
        if ((((281410551218160L & intLE) - 206161575984L) | ((25770196998L + j) & 1030807879920L)) != 0) {
            return -1;
        }
        return (int) (((((intLE & 15) * 10) + (15 & (j >> 16))) * 10) + (j >> 32));
    }

    public static int digit3(byte[] bArr, int i) {
        return digit3((getByte(bArr, i + 2) << JSONB.Constants.BC_INT32_NUM_16) | getShortLE(bArr, i));
    }

    public static int digit2(char[] cArr, int i) {
        int iReverseBytes = JDKUtils.UNSAFE.getInt(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1));
        if (JDKUtils.BIG_ENDIAN) {
            iReverseBytes = Integer.reverseBytes(iReverseBytes);
        }
        int i2 = 983055 & iReverseBytes;
        if (((((-983056) & iReverseBytes) - ZERO_ZERO_32) | ((393222 + i2) & 15728880)) != 0) {
            return -1;
        }
        return ((iReverseBytes & 15) * 10) + (i2 >> 16);
    }

    public static int digit2(byte[] bArr, int i) {
        short sReverseBytes = JDKUtils.UNSAFE.getShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
        if (JDKUtils.BIG_ENDIAN) {
            sReverseBytes = Short.reverseBytes(sReverseBytes);
        }
        int i2 = sReverseBytes & 3855;
        if (((61680 & (i2 + 1542)) | ((sReverseBytes & 61680) - 12336)) != 0) {
            return -1;
        }
        return ((sReverseBytes & 15) * 10) + (i2 >> 8);
    }

    public static boolean isDigit2(byte[] bArr, int i) {
        short sReverseBytes = JDKUtils.UNSAFE.getShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
        if (JDKUtils.BIG_ENDIAN) {
            sReverseBytes = Short.reverseBytes(sReverseBytes);
        }
        return ((((sReverseBytes & 3855) + 1542) & 61680) | ((sReverseBytes & 61680) + (-12336))) == 0;
    }

    public static boolean isDigit2(char[] cArr, int i) {
        int iReverseBytes = JDKUtils.UNSAFE.getShort(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1));
        if (JDKUtils.BIG_ENDIAN) {
            iReverseBytes = Integer.reverseBytes(iReverseBytes);
        }
        return ((((iReverseBytes & 983055) + 393222) & 15728880) | (((-983056) & iReverseBytes) - ZERO_ZERO_32)) == 0;
    }

    public static int digit1(char[] cArr, int i) {
        int i2 = JDKUtils.UNSAFE.getByte(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1)) + JSONB.Constants.BC_INT64_BYTE_ZERO;
        if (i2 < 0 || i2 > 9) {
            return -1;
        }
        return i2;
    }

    public static int digit1(byte[] bArr, int i) {
        int i2 = JDKUtils.UNSAFE.getByte(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i)) + JSONB.Constants.BC_INT64_BYTE_ZERO;
        if (i2 < 0 || i2 > 9) {
            return -1;
        }
        return i2;
    }

    public static int indexOfQuote(byte[] bArr, int i, int i2, int i3) {
        if (JDKUtils.INDEX_OF_CHAR_LATIN1 == null) {
            return indexOfQuoteV(bArr, i, i2, i3);
        }
        try {
            return (int) JDKUtils.INDEX_OF_CHAR_LATIN1.invokeExact(bArr, i, i2, i3);
        } catch (Throwable th) {
            throw new JSONException(th.getMessage());
        }
    }

    public static int indexOfQuoteV(byte[] bArr, int i, int i2, int i3) {
        int i4 = ((i3 - i2) & (-8)) + i2;
        long j = i == 39 ? 2821266740684990247L : 2459565876494606882L;
        for (long j2 = JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i2); i2 < i4 && notContains(JDKUtils.UNSAFE.getLong(bArr, j2), j); j2 += 8) {
            i2 += 8;
        }
        return indexOfChar(bArr, i, i2, i3);
    }

    public static int indexOfDoubleQuote(byte[] bArr, int i, int i2) {
        if (JDKUtils.INDEX_OF_CHAR_LATIN1 == null) {
            return indexOfDoubleQuoteV(bArr, i, i2);
        }
        try {
            return (int) JDKUtils.INDEX_OF_CHAR_LATIN1.invokeExact(bArr, 34, i, i2);
        } catch (Throwable th) {
            throw new JSONException(th.getMessage());
        }
    }

    public static int indexOfDoubleQuoteV(byte[] bArr, int i, int i2) {
        int i3 = ((i2 - i) & (-8)) + i;
        for (long j = JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i); i < i3 && notContains(JDKUtils.UNSAFE.getLong(bArr, j), 2459565876494606882L); j += 8) {
            i += 8;
        }
        return indexOfChar(bArr, 34, i, i2);
    }

    public static int indexOfLineSeparator(byte[] bArr, int i, int i2) {
        if (JDKUtils.INDEX_OF_CHAR_LATIN1 == null) {
            return indexOfLineSeparatorV(bArr, i, i2);
        }
        try {
            return (int) JDKUtils.INDEX_OF_CHAR_LATIN1.invokeExact(bArr, 10, i, i2);
        } catch (Throwable th) {
            throw new JSONException(th.getMessage());
        }
    }

    public static int indexOfLineSeparatorV(byte[] bArr, int i, int i2) {
        int i3 = ((i2 - i) & (-8)) + i;
        for (long j = JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i); i < i3 && notContains(JDKUtils.UNSAFE.getLong(bArr, j), 723401728380766730L); j += 8) {
            i += 8;
        }
        return indexOfChar(bArr, 10, i, i2);
    }

    public static int indexOfSlash(byte[] bArr, int i, int i2) {
        if (JDKUtils.INDEX_OF_CHAR_LATIN1 == null) {
            return indexOfSlashV(bArr, i, i2);
        }
        try {
            return (int) JDKUtils.INDEX_OF_CHAR_LATIN1.invokeExact(bArr, 92, i, i2);
        } catch (Throwable th) {
            throw new JSONException(th.getMessage());
        }
    }

    public static int indexOfSlashV(byte[] bArr, int i, int i2) {
        int i3 = ((i2 - i) & (-8)) + i;
        for (long j = JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i); i < i3 && notContains(JDKUtils.UNSAFE.getLong(bArr, j), 6655295901103053916L); j += 8) {
            i += 8;
        }
        return indexOfChar(bArr, 92, i, i2);
    }

    public static int indexOfChar(byte[] bArr, int i, int i2, int i3) {
        while (i2 < i3) {
            if (bArr[i2] == i) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public static int indexOfChar(char[] cArr, int i, int i2, int i3) {
        while (i2 < i3) {
            if (cArr[i2] == i) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public static int hexDigit4(byte[] bArr, int i) {
        int intLE = getIntLE(bArr, i);
        int i2 = 252645135 & intLE;
        int i3 = intLE & 1077952576;
        int i4 = i2 + (((i3 << 1) | (i3 >> 2)) >>> 4);
        return ((251658240 & i4) >>> 24) + ((983040 & i4) >>> 12) + (i4 & 3840) + ((i4 & 15) << 12);
    }

    public static int hexDigit4(char[] cArr, int i) {
        long longLE = getLongLE(cArr, i);
        long j = 4222189076152335L & longLE;
        long j2 = longLE & 18014673391583296L;
        long j3 = j + (((j2 << 1) | (j2 >> 2)) >>> 4);
        return (int) (((4222124650659840L & j3) >>> 48) + ((64424509440L & j3) >>> 28) + ((983040 & j3) >> 8) + ((j3 & 15) << 12));
    }

    public static short getShortUnaligned(byte[] bArr, int i) {
        return JDKUtils.UNSAFE.getShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
    }

    public static short getShortBE(byte[] bArr, int i) {
        return convEndian(true, JDKUtils.UNSAFE.getShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i)));
    }

    public static short getShortLE(byte[] bArr, int i) {
        return convEndian(false, JDKUtils.UNSAFE.getShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i)));
    }

    public static boolean isUTF8BOM(byte[] bArr, int i) {
        return (getIntLE(bArr, i) & ViewCompat.MEASURED_SIZE_MASK) == 12565487;
    }

    public static int getIntBE(byte[] bArr, int i) {
        int i2 = JDKUtils.UNSAFE.getInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
        return !JDKUtils.BIG_ENDIAN ? Integer.reverseBytes(i2) : i2;
    }

    public static int getIntLE(byte[] bArr, int i) {
        int i2 = JDKUtils.UNSAFE.getInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
        return JDKUtils.BIG_ENDIAN ? Integer.reverseBytes(i2) : i2;
    }

    public static int getIntLE(char[] cArr, int i) {
        int i2 = JDKUtils.UNSAFE.getInt(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1));
        return JDKUtils.BIG_ENDIAN ? Integer.reverseBytes(i2) : i2;
    }

    public static int getIntUnaligned(byte[] bArr, int i) {
        return JDKUtils.UNSAFE.getInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
    }

    public static int getIntUnaligned(char[] cArr, int i) {
        return JDKUtils.UNSAFE.getInt(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1));
    }

    public static long getLongBE(byte[] bArr, int i) {
        long j = JDKUtils.UNSAFE.getLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
        return !JDKUtils.BIG_ENDIAN ? Long.reverseBytes(j) : j;
    }

    public static long getLongUnaligned(byte[] bArr, int i) {
        return JDKUtils.UNSAFE.getLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i));
    }

    public static long getLongUnaligned(char[] cArr, int i) {
        return JDKUtils.UNSAFE.getLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1));
    }

    public static long getLongLE(byte[] bArr, int i) {
        return convEndian(false, JDKUtils.UNSAFE.getLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i)));
    }

    public static long getLongLE(char[] cArr, int i) {
        long j = JDKUtils.UNSAFE.getLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1));
        return JDKUtils.BIG_ENDIAN ? Long.reverseBytes(j) : j;
    }

    public static int hex4U(int i) {
        int iReverseBytesExpand = reverseBytesExpand(i);
        return ((((101058054 + iReverseBytesExpand) & 269488144) * 7) >> 4) + 808464432 + iReverseBytesExpand;
    }

    public static long utf16Hex4U(long j) {
        long jUtf16ReverseBytesExpand = utf16ReverseBytesExpand(j);
        long j2 = (1688875630460934L + jUtf16ReverseBytesExpand) & 4503668347895824L;
        return ((j2 >> 1) - (j2 >> 4)) + 13511005043687472L + jUtf16ReverseBytesExpand;
    }

    public static int convEndian(boolean z, int i) {
        return z == JDKUtils.BIG_ENDIAN ? i : Integer.reverseBytes(i);
    }

    public static long convEndian(boolean z, long j) {
        return z == JDKUtils.BIG_ENDIAN ? j : Long.reverseBytes(j);
    }

    static short convEndian(boolean z, short s) {
        return z == JDKUtils.BIG_ENDIAN ? s : Short.reverseBytes(s);
    }

    public static boolean isLatin1(char[] cArr, int i, int i2) {
        int i3 = i + i2;
        int i4 = (i2 & (-8)) + i;
        long j = JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1);
        while (i < i4 && (convEndian(false, JDKUtils.UNSAFE.getLong(cArr, j) | JDKUtils.UNSAFE.getLong(cArr, 8 + j)) & (-71777214294589696L)) == 0) {
            j += 16;
            i += 8;
        }
        while (true) {
            int i5 = i + 1;
            if (i >= i3) {
                return true;
            }
            if ((convEndian(false, JDKUtils.UNSAFE.getShort(cArr, j)) & 65280) != 0) {
                return false;
            }
            j += 2;
            i = i5;
        }
    }

    public static boolean isASCII(String str) {
        if (JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER != null) {
            return JDKUtils.STRING_CODER.applyAsInt(str) == 0 && isASCII(JDKUtils.STRING_VALUE.apply(str));
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (str.charAt(i) > 127) {
                return false;
            }
        }
        return true;
    }

    public static boolean isASCII(byte[] bArr) {
        return isASCII(bArr, 0, bArr.length);
    }

    public static boolean isASCII(byte[] bArr, int i, int i2) {
        int i3 = i + i2;
        int i4 = (i2 & (-8)) + i;
        long j = JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i);
        while (i < i4 && (JDKUtils.UNSAFE.getLong(bArr, j) & (-9187201950435737472L)) == 0) {
            j += 8;
            i += 8;
        }
        while (true) {
            int i5 = i + 1;
            if (i >= i3) {
                return true;
            }
            long j2 = 1 + j;
            if ((JDKUtils.UNSAFE.getByte(bArr, j) & ByteCompanionObject.MIN_VALUE) != 0) {
                return false;
            }
            i = i5;
            j = j2;
        }
    }

    public static boolean isNonSlashASCII(byte[] bArr, int i, int i2) {
        int i3 = i + i2;
        int i4 = (i2 & (-8)) + i;
        long j = JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i);
        while (i < i4) {
            long j2 = JDKUtils.UNSAFE.getLong(bArr, j);
            long j3 = 6655295901103053916L ^ j2;
            if (((j2 | ((~j3) & (j3 - 72340172838076673L))) & (-9187201950435737472L)) != 0) {
                break;
            }
            j += 8;
            i += 8;
        }
        while (true) {
            int i5 = i + 1;
            if (i >= i3) {
                return true;
            }
            long j4 = 1 + j;
            byte b = JDKUtils.UNSAFE.getByte(bArr, j);
            if ((b & ByteCompanionObject.MIN_VALUE) != 0 || b == 92) {
                return false;
            }
            i = i5;
            j = j4;
        }
    }

    public static int parseInt(byte[] bArr, int i, int i2) {
        int i3;
        int iDigit2;
        byte b = bArr[i];
        int i4 = isDigitLatin1(b) ? 48 - b : (i2 == 1 || !(b == 45 || b == 43)) ? 1 : 0;
        int i5 = i + i2;
        int i6 = i + 1;
        while (true) {
            i3 = i6 + 1;
            if (i3 < i5 && (iDigit2 = digit2(bArr, i6)) != -1) {
                if (!(-21474836 <= i4) || !(i4 <= 0)) {
                    break;
                }
                i4 = (i4 * 100) - iDigit2;
                i6 += 2;
            } else {
                break;
            }
        }
        if (i6 < i5) {
            byte b2 = bArr[i6];
            if (isDigitLatin1(b2)) {
                if ((-214748364 <= i4) & (i4 <= 0)) {
                    i4 = ((i4 * 10) + 48) - b2;
                    i6 = i3;
                }
            }
        }
        if ((Integer.MIN_VALUE < i4 || b == 45) && ((i6 == i5) & (i4 <= 0))) {
            return b == 45 ? i4 : -i4;
        }
        throw new NumberFormatException(new String(bArr, i6, i2));
    }
}
