package com.cdbwsoft.library.panchip;

import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public class DataOperate {
    public static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static byte[] hexStringToByte(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        char[] charArray = str.toCharArray();
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToInt(charArray[i2 + 1]) | (charToInt(charArray[i2]) << 4));
        }
        return bArr;
    }

    public static int charToInt(char c) {
        return "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] bytesConcat(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public static byte[] intToBytes(int i) {
        return new byte[]{(byte) i, (byte) (i >> 8)};
    }
}
