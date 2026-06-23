package com.cdbwsoft.library;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public interface RequestPermissionListener {
    Context getContext();

    void requestPermission(String[] strArr, String str, Runnable runnable);
}
