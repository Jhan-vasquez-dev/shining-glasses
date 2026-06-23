package com.alibaba.fastjson.util;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class Base64 {
    public static final char[] CA;
    public static final int[] IA;

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
    }

    public static byte[] decodeFast(char[] cArr, int i, int i2) {
        int i3;
        int i4 = 0;
        if (i2 == 0) {
            return new byte[0];
        }
        int i5 = (i + i2) - 1;
        while (i < i5 && IA[cArr[i]] < 0) {
            i++;
        }
        while (i5 > 0 && IA[cArr[i5]] < 0) {
            i5--;
        }
        int i6 = cArr[i5] == '=' ? cArr[i5 + (-1)] == '=' ? 2 : 1 : 0;
        int i7 = (i5 - i) + 1;
        if (i2 > 76) {
            i3 = (cArr[76] == '\r' ? i7 / 78 : 0) << 1;
        } else {
            i3 = 0;
        }
        int i8 = (((i7 - i3) * 6) >> 3) - i6;
        byte[] bArr = new byte[i8];
        int i9 = (i8 / 3) * 3;
        int i10 = 0;
        int i11 = 0;
        while (i10 < i9) {
            int[] iArr = IA;
            int i12 = iArr[cArr[i + 3]] | (iArr[cArr[i]] << 18) | (iArr[cArr[i + 1]] << 12) | (iArr[cArr[i + 2]] << 6);
            int i13 = i + 4;
            bArr[i10] = (byte) (i12 >> 16);
            bArr[i10 + 1] = (byte) (i12 >> 8);
            bArr[i10 + 2] = (byte) i12;
            i10 += 3;
            if (i3 <= 0 || (i11 = i11 + 1) != 19) {
                i = i13;
            } else {
                i += 6;
                i11 = 0;
            }
        }
        if (i10 < i8) {
            int i14 = 0;
            while (i <= i5 - i6) {
                i4 |= IA[cArr[i]] << (18 - (i14 * 6));
                i14++;
                i++;
            }
            int i15 = 16;
            while (i10 < i8) {
                bArr[i10] = (byte) (i4 >> i15);
                i15 -= 8;
                i10++;
            }
        }
        return bArr;
    }

    public static byte[] decodeFast(String str, int i, int i2) {
        int i3;
        int i4 = 0;
        if (i2 == 0) {
            return new byte[0];
        }
        int i5 = (i + i2) - 1;
        while (i < i5 && IA[str.charAt(i)] < 0) {
            i++;
        }
        while (i5 > 0 && IA[str.charAt(i5)] < 0) {
            i5--;
        }
        int i6 = str.charAt(i5) == '=' ? str.charAt(i5 + (-1)) == '=' ? 2 : 1 : 0;
        int i7 = (i5 - i) + 1;
        if (i2 > 76) {
            i3 = (str.charAt(76) == '\r' ? i7 / 78 : 0) << 1;
        } else {
            i3 = 0;
        }
        int i8 = (((i7 - i3) * 6) >> 3) - i6;
        byte[] bArr = new byte[i8];
        int i9 = (i8 / 3) * 3;
        int i10 = 0;
        int i11 = 0;
        while (i10 < i9) {
            int[] iArr = IA;
            int i12 = iArr[str.charAt(i + 3)] | (iArr[str.charAt(i)] << 18) | (iArr[str.charAt(i + 1)] << 12) | (iArr[str.charAt(i + 2)] << 6);
            int i13 = i + 4;
            bArr[i10] = (byte) (i12 >> 16);
            bArr[i10 + 1] = (byte) (i12 >> 8);
            bArr[i10 + 2] = (byte) i12;
            i10 += 3;
            if (i3 <= 0 || (i11 = i11 + 1) != 19) {
                i = i13;
            } else {
                i += 6;
                i11 = 0;
            }
        }
        if (i10 < i8) {
            int i14 = 0;
            while (i <= i5 - i6) {
                i4 |= IA[str.charAt(i)] << (18 - (i14 * 6));
                i14++;
                i++;
            }
            int i15 = 16;
            while (i10 < i8) {
                bArr[i10] = (byte) (i4 >> i15);
                i15 -= 8;
                i10++;
            }
        }
        return bArr;
    }

    public static byte[] decodeFast(String str) {
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
}
