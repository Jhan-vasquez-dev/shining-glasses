package com.android.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageManager;
import com.android.volley.RequestQueue;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class Volley {
    private static final String DEFAULT_CACHE_DIR = "volley";

    public static RequestQueue newRequestQueue(Context context, HttpStack httpStack, int i) {
        RequestQueue requestQueue;
        File file = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        try {
            String packageName = context.getPackageName();
            String str = packageName + "/" + context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException unused) {
        }
        if (httpStack == null) {
            httpStack = new HurlStack();
        }
        BasicNetwork basicNetwork = new BasicNetwork(httpStack);
        if (i <= -1) {
            requestQueue = new RequestQueue(new DiskBasedCache(file), basicNetwork);
        } else {
            requestQueue = new RequestQueue(new DiskBasedCache(file, i), basicNetwork);
        }
        requestQueue.start();
        return requestQueue;
    }

    public static RequestQueue newRequestQueue(Context context, int i) {
        return newRequestQueue(context, null, i);
    }

    public static RequestQueue newRequestQueue(Context context, HttpStack httpStack) {
        return newRequestQueue(context, httpStack, -1);
    }

    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, (HttpStack) null);
    }
}
