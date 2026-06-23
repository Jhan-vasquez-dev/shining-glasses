package com.alibaba.fastjson2.util;

import com.alibaba.fastjson2.JSONB;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import okhttp3.internal.connection.RealConnection;

/* JADX INFO: loaded from: classes.dex */
public final class NumberUtils {
    static final long INFI;
    static final long INFINITY;
    static final int MOD_DOUBLE_EXP = 2047;
    static final long MOD_DOUBLE_MANTISSA = 4503599627370495L;
    static final int MOD_FLOAT_EXP = 255;
    static final int MOD_FLOAT_MANTISSA = 8388607;
    static final LongBiFunction MULTIPLY_HIGH;
    static final double[] NEGATIVE_DECIMAL_POWER;
    static final char[][] NEGATIVE_DECIMAL_POWER_CHARS;
    static final long NITY;
    static final double[] POSITIVE_DECIMAL_POWER;
    static final char[][] POSITIVE_DECIMAL_POWER_CHARS;
    static final long[] POW10_LONG_VALUES;
    static final BigInteger[] POW5_BI_VALUES;
    static final long[] POW5_LONG_VALUES;
    static final short[] TWO_DIGITS_16_BITS;
    static final int[] TWO_DIGITS_32_BITS;

    @FunctionalInterface
    interface LongBiFunction {
        long multiplyHigh(long j, long j2);
    }

    static long multiplyHigh(long j, long j2) {
        long j3 = j >> 32;
        long j4 = j & 4294967295L;
        long j5 = j2 >> 32;
        long j6 = j2 & 4294967295L;
        long j7 = (j6 * j3) + ((j4 * j6) >>> 32);
        return (j3 * j5) + (j7 >> 32) + (((4294967295L & j7) + (j4 * j5)) >> 32);
    }

    static {
        LongBiFunction longBiFunctionInvokeExact;
        long j;
        int i;
        int i2;
        if (JDKUtils.JVM_VERSION <= 8 || JDKUtils.ANDROID) {
            longBiFunctionInvokeExact = null;
        } else {
            try {
                MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(NumberUtils.class);
                MethodType methodType = MethodType.methodType(Long.TYPE, Long.TYPE, Long.TYPE);
                longBiFunctionInvokeExact = (LongBiFunction) LambdaMetafactory.metafactory(lookupTrustedLookup, "multiplyHigh", MethodType.methodType(LongBiFunction.class), methodType, lookupTrustedLookup.findStatic(Math.class, "multiplyHigh", methodType), methodType).getTarget().invokeExact();
            } catch (Throwable unused) {
                longBiFunctionInvokeExact = null;
            }
        }
        if (longBiFunctionInvokeExact == null) {
            longBiFunctionInvokeExact = new LongBiFunction() { // from class: com.alibaba.fastjson2.util.NumberUtils$$ExternalSyntheticLambda0
                @Override // com.alibaba.fastjson2.util.NumberUtils.LongBiFunction
                public final long multiplyHigh(long j2, long j3) {
                    return NumberUtils.multiplyHigh(j2, j3);
                }
            };
        }
        MULTIPLY_HIGH = longBiFunctionInvokeExact;
        INFINITY = IOUtils.getLongUnaligned("Infinity".getBytes(StandardCharsets.ISO_8859_1), 0);
        char[] charArray = "Infinity".toCharArray();
        INFI = IOUtils.getLongUnaligned(charArray, 0);
        NITY = IOUtils.getLongUnaligned(charArray, 4);
        double[] dArr = new double[325];
        POSITIVE_DECIMAL_POWER = dArr;
        NEGATIVE_DECIMAL_POWER = new double[325];
        POW10_LONG_VALUES = new long[]{10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, RealConnection.IDLE_CONNECTION_HEALTHY_NS, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L, Long.MAX_VALUE};
        POW5_LONG_VALUES = new long[27];
        POW5_BI_VALUES = new BigInteger[343];
        int length = dArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            POSITIVE_DECIMAL_POWER[i3] = Double.valueOf("1.0E" + i3).doubleValue();
            NEGATIVE_DECIMAL_POWER[i3] = Double.valueOf("1.0E-" + i3).doubleValue();
        }
        double[] dArr2 = NEGATIVE_DECIMAL_POWER;
        dArr2[dArr2.length - 1] = Double.MIN_VALUE;
        int i4 = 0;
        long j2 = 1;
        while (true) {
            long[] jArr = POW5_LONG_VALUES;
            if (i4 >= jArr.length) {
                break;
            }
            jArr[i4] = j2;
            j2 *= 5;
            i4++;
        }
        BigInteger bigIntegerValueOf = BigInteger.valueOf(5L);
        POW5_BI_VALUES[0] = BigInteger.ONE;
        int i5 = 1;
        while (true) {
            BigInteger[] bigIntegerArr = POW5_BI_VALUES;
            if (i5 >= bigIntegerArr.length) {
                break;
            }
            bigIntegerArr[i5] = bigIntegerValueOf.pow(i5);
            i5++;
        }
        char[][] cArr = new char[325][];
        POSITIVE_DECIMAL_POWER_CHARS = cArr;
        NEGATIVE_DECIMAL_POWER_CHARS = new char[325][];
        int length2 = cArr.length;
        for (int i6 = 0; i6 < length2; i6++) {
            POSITIVE_DECIMAL_POWER_CHARS[i6] = ("1.0E" + i6).toCharArray();
            NEGATIVE_DECIMAL_POWER_CHARS[i6] = ("1.0E-" + i6).toCharArray();
        }
        char[][] cArr2 = NEGATIVE_DECIMAL_POWER_CHARS;
        cArr2[cArr2.length - 1] = "4.9E-324".toCharArray();
        TWO_DIGITS_32_BITS = new int[100];
        TWO_DIGITS_16_BITS = new short[100];
        for (long j3 = 0; j3 < 10; j3++) {
            for (long j4 = 0; j4 < 10; j4++) {
                if (JDKUtils.BIG_ENDIAN) {
                    j = ((j3 + 48) << 16) | (48 + j4);
                    i = (((int) j3) + 48) << 8;
                    i2 = (int) j4;
                } else {
                    j = ((j4 + 48) << 16) | (48 + j3);
                    i = (((int) j4) + 48) << 8;
                    i2 = (int) j3;
                }
                int i7 = i | (i2 + 48);
                int i8 = (int) ((j3 * 10) + j4);
                TWO_DIGITS_32_BITS[i8] = (int) j;
                TWO_DIGITS_16_BITS[i8] = (short) i7;
            }
        }
    }

    private NumberUtils() {
    }

    static long multiplyHighAndShift(long j, long j2, int i) {
        long jMultiplyHigh = MULTIPLY_HIGH.multiplyHigh(j, j2);
        if (i >= 64) {
            return jMultiplyHigh >>> (i - 64);
        }
        return ((j * j2) >>> i) | (jMultiplyHigh << (64 - i));
    }

    static long multiplyHighAndShift(long j, long j2, long j3, int i) {
        int i2 = i - 64;
        LongBiFunction longBiFunction = MULTIPLY_HIGH;
        long jMultiplyHigh = longBiFunction.multiplyHigh(j, j2);
        long j4 = j2 * j;
        long jMultiplyHigh2 = (longBiFunction.multiplyHigh(j, j3) << 32) + ((j * j3) >>> 32);
        long j5 = j4 + jMultiplyHigh2;
        if ((j4 | jMultiplyHigh2) < 0 && ((j4 & jMultiplyHigh2) < 0 || j5 >= 0)) {
            jMultiplyHigh++;
        }
        if (i2 >= 0) {
            return jMultiplyHigh >>> i2;
        }
        return (j5 >>> i) | (jMultiplyHigh << (-i2));
    }

    /* JADX WARN: Removed duplicated region for block: B:96:0x01a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.alibaba.fastjson2.util.Scientific doubleToScientific(double r28) {
        /*
            Method dump skipped, instruction units count: 430
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.NumberUtils.doubleToScientific(double):com.alibaba.fastjson2.util.Scientific");
    }

    public static int writeDouble(byte[] bArr, int i, double d, boolean z) {
        if (d == 0.0d) {
            if (Double.doubleToLongBits(d) == Long.MIN_VALUE) {
                bArr[i] = 45;
                i++;
            }
            bArr[i] = JSONB.Constants.BC_INT32_BYTE_MIN;
            IOUtils.putShortUnaligned(bArr, i + 1, IOUtils.DOT_ZERO_16);
            return i + 3;
        }
        if (d < 0.0d) {
            if (!z || d != Double.NEGATIVE_INFINITY) {
                bArr[i] = 45;
                i++;
            }
            d = -d;
        }
        int i2 = i;
        long j = (long) d;
        if (d == j) {
            int iStringSize = IOUtils.stringSize(j);
            return writeDecimal(j, iStringSize, iStringSize - 1, bArr, i2);
        }
        Scientific scientificDoubleToScientific = doubleToScientific(d);
        int i3 = scientificDoubleToScientific.e10;
        if (!scientificDoubleToScientific.b) {
            return writeDecimal(scientificDoubleToScientific.output, scientificDoubleToScientific.count, scientificDoubleToScientific.e10, bArr, i2);
        }
        if (scientificDoubleToScientific == Scientific.SCIENTIFIC_NULL) {
            if (z) {
                IOUtils.putIntUnaligned(bArr, i2, IOUtils.NULL_32);
                return i2 + 4;
            }
            if (d == Double.POSITIVE_INFINITY) {
                IOUtils.putLongUnaligned(bArr, i2, INFINITY);
                return i2 + 8;
            }
            bArr[i2] = JSONB.Constants.BC_STR_ASCII_FIX_5;
            bArr[i2 + 1] = 97;
            bArr[i2 + 2] = JSONB.Constants.BC_STR_ASCII_FIX_5;
            return i2 + 3;
        }
        int i4 = 0;
        if (i3 >= 0) {
            char[] cArr = POSITIVE_DECIMAL_POWER_CHARS[i3];
            int length = cArr.length;
            while (i4 < length) {
                bArr[i2] = (byte) cArr[i4];
                i4++;
                i2++;
            }
            return i2;
        }
        char[] cArr2 = NEGATIVE_DECIMAL_POWER_CHARS[-i3];
        int length2 = cArr2.length;
        while (i4 < length2) {
            bArr[i2] = (byte) cArr2[i4];
            i4++;
            i2++;
        }
        return i2;
    }

    public static int writeDouble(char[] cArr, int i, double d, boolean z) {
        if (d == 0.0d) {
            if (Double.doubleToLongBits(d) == Long.MIN_VALUE) {
                cArr[i] = '-';
                i++;
            }
            cArr[i] = '0';
            IOUtils.putIntUnaligned(cArr, i + 1, IOUtils.DOT_ZERO_32);
            return i + 3;
        }
        if (d < 0.0d) {
            if (!z || d != Double.NEGATIVE_INFINITY) {
                cArr[i] = '-';
                i++;
            }
            d = -d;
        }
        int i2 = i;
        long j = (long) d;
        if (d == j) {
            int iStringSize = IOUtils.stringSize(j);
            return writeDecimal(j, iStringSize, iStringSize - 1, cArr, i2);
        }
        Scientific scientificDoubleToScientific = doubleToScientific(d);
        int i3 = scientificDoubleToScientific.e10;
        if (!scientificDoubleToScientific.b) {
            return writeDecimal(scientificDoubleToScientific.output, scientificDoubleToScientific.count, i3, cArr, i2);
        }
        if (scientificDoubleToScientific != Scientific.SCIENTIFIC_NULL) {
            if (i3 >= 0) {
                char[] cArr2 = POSITIVE_DECIMAL_POWER_CHARS[i3];
                System.arraycopy(cArr2, 0, cArr, i2, cArr2.length);
                return i2 + cArr2.length;
            }
            char[] cArr3 = NEGATIVE_DECIMAL_POWER_CHARS[-i3];
            System.arraycopy(cArr3, 0, cArr, i2, cArr3.length);
            return i2 + cArr3.length;
        }
        if (z) {
            IOUtils.putLongUnaligned(cArr, i2, IOUtils.NULL_64);
            return i2 + 4;
        }
        if (d == Double.POSITIVE_INFINITY) {
            IOUtils.putLongUnaligned(cArr, i2, INFI);
            IOUtils.putLongUnaligned(cArr, i2 + 4, NITY);
            return i2 + 8;
        }
        cArr[i2] = 'N';
        cArr[i2 + 1] = 'a';
        cArr[i2 + 2] = 'N';
        return i2 + 3;
    }

    public static int writeFloat(byte[] bArr, int i, float f, boolean z) {
        int i2;
        if (Float.isNaN(f) || f == Float.POSITIVE_INFINITY || f == Float.NEGATIVE_INFINITY) {
            return writeSpecial(bArr, i, f, z);
        }
        if (f == 0.0f) {
            if (Float.floatToIntBits(f) == Integer.MIN_VALUE) {
                bArr[i] = 45;
                i++;
            }
            bArr[i] = JSONB.Constants.BC_INT32_BYTE_MIN;
            IOUtils.putShortUnaligned(bArr, i + 1, IOUtils.DOT_ZERO_16);
            return i + 3;
        }
        if (f < 0.0f) {
            bArr[i] = 45;
            f = -f;
            i2 = i + 1;
        } else {
            i2 = i;
        }
        Scientific scientificFloatToScientific = floatToScientific(f);
        return writeDecimal(scientificFloatToScientific.output, scientificFloatToScientific.count, scientificFloatToScientific.e10, bArr, i2);
    }

    public static int writeFloat(char[] cArr, int i, float f, boolean z) {
        int i2;
        if (Float.isNaN(f) || f == Float.POSITIVE_INFINITY || f == Float.NEGATIVE_INFINITY) {
            return writeSpecial(cArr, i, f, z);
        }
        if (f == 0.0f) {
            if (Float.floatToIntBits(f) == Integer.MIN_VALUE) {
                cArr[i] = '-';
                i++;
            }
            cArr[i] = '0';
            IOUtils.putIntUnaligned(cArr, i + 1, IOUtils.DOT_ZERO_32);
            return i + 3;
        }
        if (f < 0.0f) {
            cArr[i] = '-';
            f = -f;
            i2 = i + 1;
        } else {
            i2 = i;
        }
        Scientific scientificFloatToScientific = floatToScientific(f);
        return writeDecimal(scientificFloatToScientific.output, scientificFloatToScientific.count, scientificFloatToScientific.e10, cArr, i2);
    }

    private static int writeSpecial(byte[] bArr, int i, float f, boolean z) {
        if (z) {
            IOUtils.putIntUnaligned(bArr, i, IOUtils.NULL_32);
            return i + 4;
        }
        if (Float.isNaN(f)) {
            bArr[i] = JSONB.Constants.BC_STR_ASCII_FIX_5;
            bArr[i + 1] = 97;
            bArr[i + 2] = JSONB.Constants.BC_STR_ASCII_FIX_5;
            return i + 3;
        }
        if (f == Float.NEGATIVE_INFINITY) {
            bArr[i] = 45;
            i++;
        }
        IOUtils.putLongUnaligned(bArr, i, INFINITY);
        return i + 8;
    }

    private static int writeSpecial(char[] cArr, int i, float f, boolean z) {
        if (z) {
            IOUtils.putLongUnaligned(cArr, i, IOUtils.NULL_64);
            return i + 4;
        }
        if (Float.isNaN(f)) {
            cArr[i] = 'N';
            cArr[i + 1] = 'a';
            cArr[i + 2] = 'N';
            return i + 3;
        }
        if (f == Float.NEGATIVE_INFINITY) {
            cArr[i] = '-';
            i++;
        }
        IOUtils.putLongUnaligned(cArr, i, INFI);
        IOUtils.putLongUnaligned(cArr, i + 4, NITY);
        return i + 8;
    }

    /* JADX WARN: Removed duplicated region for block: B:79:0x019f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.alibaba.fastjson2.util.Scientific floatToScientific(float r24) {
        /*
            Method dump skipped, instruction units count: 444
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.NumberUtils.floatToScientific(float):com.alibaba.fastjson2.util.Scientific");
    }

    private static int writeDecimal(long j, int i, int i2, byte[] bArr, int i3) {
        long j2;
        int i4;
        int iWriteInt64;
        int i5 = i2;
        if ((j & 1) == 0 && j % 5 == 0) {
            j2 = j;
            int i6 = i;
            while (j2 % 100 == 0) {
                i6 -= 2;
                j2 /= 100;
                if (i6 == 1) {
                    break;
                }
            }
            if ((1 & j2) == 0 && j2 % 5 == 0 && j2 > 0) {
                i4 = i6 - 1;
                j2 /= 10;
            } else {
                i4 = i6;
            }
        } else {
            j2 = j;
            i4 = i;
        }
        if (i5 < -3 || i5 >= 7) {
            if (i4 == 1) {
                bArr[i3] = (byte) (j2 + 48);
                IOUtils.putShortUnaligned(bArr, i3 + 1, IOUtils.DOT_ZERO_16);
                iWriteInt64 = i3 + 3;
            } else {
                int i7 = i4 - 2;
                long j3 = POW10_LONG_VALUES[i7];
                int i8 = (int) (j2 / j3);
                bArr[i3] = (byte) (i8 + 48);
                bArr[i3 + 1] = 46;
                int i9 = i3 + 2;
                long j4 = j2 - (((long) i8) * j3);
                while (true) {
                    i7--;
                    if (i7 <= -1 || j4 >= POW10_LONG_VALUES[i7]) {
                        break;
                    }
                    bArr[i9] = JSONB.Constants.BC_INT32_BYTE_MIN;
                    i9++;
                }
                iWriteInt64 = IOUtils.writeInt64(bArr, i9, j4);
            }
            int i10 = iWriteInt64 + 1;
            bArr[iWriteInt64] = 69;
            if (i5 < 0) {
                bArr[i10] = 45;
                i5 = -i5;
                i10 = iWriteInt64 + 2;
            }
            if (i5 > 99) {
                int i11 = (int) ((((long) i5) * 1374389535) >> 37);
                bArr[i10] = (byte) (i11 + 48);
                IOUtils.putShortUnaligned(bArr, i10 + 1, TWO_DIGITS_16_BITS[i5 - (i11 * 100)]);
                return i10 + 3;
            }
            if (i5 > 9) {
                IOUtils.putShortUnaligned(bArr, i10, TWO_DIGITS_16_BITS[i5]);
                return i10 + 2;
            }
            int i12 = i10 + 1;
            bArr[i10] = (byte) (i5 + 48);
            return i12;
        }
        if (i5 < 0) {
            IOUtils.putShortUnaligned(bArr, i3, IOUtils.ZERO_DOT_16);
            int i13 = i3 + 2;
            if (i5 == -2) {
                bArr[i13] = JSONB.Constants.BC_INT32_BYTE_MIN;
                i13 = i3 + 3;
            } else if (i5 == -3) {
                IOUtils.putShortUnaligned(bArr, i13, (short) 12336);
                i13 = i3 + 4;
            }
            return IOUtils.writeInt64(bArr, i13, j2);
        }
        int i14 = (i4 - 1) - i5;
        if (i14 > 0) {
            int i15 = i14 - 1;
            long j5 = POW10_LONG_VALUES[i15];
            long j6 = (int) (j2 / j5);
            int iWriteInt32 = IOUtils.writeInt32(bArr, i3, j6);
            int i16 = iWriteInt32 + 1;
            bArr[iWriteInt32] = 46;
            long j7 = j2 - (j6 * j5);
            while (true) {
                i15--;
                if (i15 <= -1 || j7 >= POW10_LONG_VALUES[i15]) {
                    break;
                }
                bArr[i16] = JSONB.Constants.BC_INT32_BYTE_MIN;
                i16++;
            }
            return IOUtils.writeInt64(bArr, i16, j7);
        }
        int iWriteInt642 = IOUtils.writeInt64(bArr, i3, j2);
        int i17 = -i14;
        if (i17 > 0) {
            int i18 = 0;
            while (i18 < i17) {
                bArr[iWriteInt642] = JSONB.Constants.BC_INT32_BYTE_MIN;
                i18++;
                iWriteInt642++;
            }
        }
        IOUtils.putShortUnaligned(bArr, iWriteInt642, IOUtils.DOT_ZERO_16);
        return iWriteInt642 + 2;
    }

    private static int writeDecimal(long j, int i, int i2, char[] cArr, int i3) {
        long j2;
        int i4;
        int iWriteInt64;
        int i5 = i2;
        if ((j & 1) == 0 && j % 5 == 0) {
            j2 = j;
            int i6 = i;
            while (j2 % 100 == 0) {
                i6 -= 2;
                j2 /= 100;
                if (i6 == 1) {
                    break;
                }
            }
            if ((1 & j2) == 0 && j2 % 5 == 0 && j2 > 0) {
                i4 = i6 - 1;
                j2 /= 10;
            } else {
                i4 = i6;
            }
        } else {
            j2 = j;
            i4 = i;
        }
        if (i5 < -3 || i5 >= 7) {
            if (i4 == 1) {
                cArr[i3] = (char) (j2 + 48);
                IOUtils.putIntUnaligned(cArr, i3 + 1, IOUtils.DOT_ZERO_32);
                iWriteInt64 = i3 + 3;
            } else {
                int i7 = i4 - 2;
                long j3 = POW10_LONG_VALUES[i7];
                int i8 = (int) (j2 / j3);
                cArr[i3] = (char) (i8 + 48);
                cArr[i3 + 1] = '.';
                int i9 = i3 + 2;
                long j4 = j2 - (((long) i8) * j3);
                while (true) {
                    i7--;
                    if (i7 <= -1 || j4 >= POW10_LONG_VALUES[i7]) {
                        break;
                    }
                    cArr[i9] = '0';
                    i9++;
                }
                iWriteInt64 = IOUtils.writeInt64(cArr, i9, j4);
            }
            int i10 = iWriteInt64 + 1;
            cArr[iWriteInt64] = 'E';
            if (i5 < 0) {
                cArr[i10] = '-';
                i5 = -i5;
                i10 = iWriteInt64 + 2;
            }
            if (i5 > 99) {
                int i11 = (int) ((((long) i5) * 1374389535) >> 37);
                cArr[i10] = (char) (i11 + 48);
                IOUtils.putIntUnaligned(cArr, i10 + 1, TWO_DIGITS_32_BITS[i5 - (i11 * 100)]);
                return i10 + 3;
            }
            if (i5 > 9) {
                IOUtils.putIntUnaligned(cArr, i10, TWO_DIGITS_32_BITS[i5]);
                return i10 + 2;
            }
            int i12 = i10 + 1;
            cArr[i10] = (char) (i5 + 48);
            return i12;
        }
        if (i5 < 0) {
            IOUtils.putIntUnaligned(cArr, i3, IOUtils.ZERO_DOT_32);
            int i13 = i3 + 2;
            if (i5 == -2) {
                cArr[i13] = '0';
                i13 = i3 + 3;
            } else if (i5 == -3) {
                IOUtils.putIntUnaligned(cArr, i13, 3145776);
                i13 = i3 + 4;
            }
            return IOUtils.writeInt64(cArr, i13, j2);
        }
        int i14 = (i4 - 1) - i5;
        if (i14 > 0) {
            int i15 = i14 - 1;
            long j5 = POW10_LONG_VALUES[i15];
            long j6 = (int) (j2 / j5);
            int iWriteInt642 = IOUtils.writeInt64(cArr, i3, j6);
            int i16 = iWriteInt642 + 1;
            cArr[iWriteInt642] = '.';
            long j7 = j2 - (j6 * j5);
            while (true) {
                i15--;
                if (i15 <= -1 || j7 >= POW10_LONG_VALUES[i15]) {
                    break;
                }
                cArr[i16] = '0';
                i16++;
            }
            return IOUtils.writeInt64(cArr, i16, j7);
        }
        int iWriteInt643 = IOUtils.writeInt64(cArr, i3, j2);
        int i17 = -i14;
        if (i17 > 0) {
            int i18 = 0;
            while (i18 < i17) {
                cArr[iWriteInt643] = '0';
                i18++;
                iWriteInt643++;
            }
        }
        IOUtils.putIntUnaligned(cArr, iWriteInt643, IOUtils.DOT_ZERO_32);
        return iWriteInt643 + 2;
    }
}
