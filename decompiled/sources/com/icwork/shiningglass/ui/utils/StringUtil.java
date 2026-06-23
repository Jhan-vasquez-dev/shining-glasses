package com.icwork.shiningglass.ui.utils;

/* JADX INFO: loaded from: classes.dex */
public class StringUtil {
    public static int string2int(String str) {
        return string2int(str, 0);
    }

    public static int string2int(String str, int i) {
        try {
            return Integer.valueOf(str).intValue();
        } catch (Exception unused) {
            return i;
        }
    }
}
