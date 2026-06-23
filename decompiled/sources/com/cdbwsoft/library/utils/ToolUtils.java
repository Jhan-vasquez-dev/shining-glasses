package com.cdbwsoft.library.utils;

import android.os.Build;
import android.text.TextUtils;
import com.cdbwsoft.library.BaseApplication;
import com.cdbwsoft.library.cache.CacheManager;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class ToolUtils {
    public static final String UNIQUE_FILE = "unique_id";
    public static final String UNIQUE_ID = "unique_id";

    public static String getUniqueID() {
        String string;
        String str = SharedPreferenceUtils.get(BaseApplication.getInstance(), "unique_id");
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String cache = CacheManager.getCache("unique_id");
        if (!TextUtils.isEmpty(cache)) {
            return cache;
        }
        String str2 = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.DISPLAY.length() % 10) + (Build.HOST.length() % 10) + (Build.ID.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10) + (Build.TAGS.length() % 10) + (Build.TYPE.length() % 10) + (Build.USER.length() % 10);
        try {
            string = Build.class.getField("SERIAL").get(null).toString();
            cache = new UUID(str2.hashCode(), string.hashCode()).toString();
        } catch (Exception unused) {
            string = "serial";
        }
        if (cache == null) {
            cache = new UUID(str2.hashCode(), string.hashCode()).toString();
        }
        SharedPreferenceUtils.put(BaseApplication.getInstance(), "unique_id", cache);
        CacheManager.saveCache("unique_id", cache);
        return cache;
    }
}
