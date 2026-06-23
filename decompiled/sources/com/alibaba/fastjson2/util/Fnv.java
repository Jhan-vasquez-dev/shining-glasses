package com.alibaba.fastjson2.util;

import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
public final class Fnv {
    public static final long MAGIC_HASH_CODE = -3750763034362895579L;
    public static final long MAGIC_PRIME = 1099511628211L;

    public static long hashCode64LCase(String str) {
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i < str.length()) {
                char cCharAt = str.charAt(i);
                if (cCharAt > 255 || (i == 0 && cCharAt == 0)) {
                    break;
                }
                if (cCharAt == '-' || cCharAt == '_' || cCharAt == ' ') {
                    i2++;
                }
                i++;
            } else if (str.length() - i2 <= 8) {
                int i3 = 0;
                long j = 0;
                for (int length = str.length() - 1; length >= 0; length--) {
                    char cCharAt2 = str.charAt(length);
                    if (cCharAt2 != '-' && cCharAt2 != '_' && cCharAt2 != ' ') {
                        if (cCharAt2 >= 'A' && cCharAt2 <= 'Z') {
                            cCharAt2 = (char) (cCharAt2 + TokenParser.SP);
                        }
                        j = i3 == 0 ? (byte) cCharAt2 : (j << 8) + ((long) cCharAt2);
                        i3++;
                    }
                }
                if (j != 0) {
                    return j;
                }
            }
        }
        long j2 = MAGIC_HASH_CODE;
        for (int i4 = 0; i4 < str.length(); i4++) {
            char cCharAt3 = str.charAt(i4);
            if (cCharAt3 != '-' && cCharAt3 != '_' && cCharAt3 != ' ') {
                if (cCharAt3 >= 'A' && cCharAt3 <= 'Z') {
                    cCharAt3 = (char) (cCharAt3 + TokenParser.SP);
                }
                j2 = (j2 ^ ((long) cCharAt3)) * MAGIC_PRIME;
            }
        }
        return j2;
    }

    public static long hashCode64(String... strArr) {
        if (strArr.length == 1) {
            return hashCode64(strArr[0]);
        }
        long jHashCode64 = MAGIC_HASH_CODE;
        for (String str : strArr) {
            jHashCode64 = (jHashCode64 ^ hashCode64(str)) * MAGIC_PRIME;
        }
        return jHashCode64;
    }

    public static long hashCode64(String str) {
        if (str.length() <= 8) {
            int i = 0;
            while (true) {
                if (i < str.length()) {
                    char cCharAt = str.charAt(i);
                    if (cCharAt > 255 || (i == 0 && cCharAt == 0)) {
                        break;
                    }
                    i++;
                } else {
                    int length = str.length() - 1;
                    long jCharAt = 0;
                    while (length >= 0) {
                        jCharAt = length == str.length() + (-1) ? (byte) r7 : (jCharAt << 8) + ((long) str.charAt(length));
                        length--;
                    }
                    if (jCharAt != 0) {
                        return jCharAt;
                    }
                }
            }
        }
        long jCharAt2 = MAGIC_HASH_CODE;
        for (int i2 = 0; i2 < str.length(); i2++) {
            jCharAt2 = (jCharAt2 ^ ((long) str.charAt(i2))) * MAGIC_PRIME;
        }
        return jCharAt2;
    }
}
