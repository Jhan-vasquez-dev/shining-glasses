package com.cdbwsoft.library.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.utils.SharedPreferenceUtils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class CacheManager extends SharedPreferenceUtils {
    public CacheManager(Context context) {
        super(context, AppConfig.CACHE_FILE);
    }

    public void cacheText(String str, String str2) {
        put(str, str2);
    }

    public String getCacheText(String str) {
        return get(str);
    }

    public boolean hasCacheText(String str) {
        return contains(str);
    }

    public static String getRollyName(String str) {
        if (str.contains("$date$")) {
            str = str.replace("$date$", new SimpleDateFormat(AppConfig.CACHE_DAILY, Locale.getDefault()).format(new Date()));
        }
        return str.contains("$time$") ? str.replace("$time$", new SimpleDateFormat(AppConfig.CACHE_TIMELY, Locale.getDefault()).format(new Date())) : str;
    }

    public static boolean saveCacheDaily(String str, String str2) {
        return saveCacheDaily(str, str2, false);
    }

    public static boolean saveCacheDaily(String str, String str2, boolean z) {
        return saveCache(getRollyName(str), str2, z);
    }

    public static boolean saveCache(String str, String str2) {
        return saveCache(str, str2, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0067 A[Catch: IOException -> 0x006b, TryCatch #5 {IOException -> 0x006b, blocks: (B:24:0x004a, B:36:0x0060, B:39:0x0067, B:40:0x006a), top: B:51:0x0037 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean saveCache(java.lang.String r5, java.lang.String r6, boolean r7) throws java.lang.Throwable {
        /*
            java.io.File r0 = new java.io.File
            java.lang.String r1 = com.cdbwsoft.library.AppConfig.CACHE_PATH
            r0.<init>(r1, r5)
            boolean r5 = r0.exists()
            r1 = 0
            if (r5 != 0) goto L2f
            java.io.File r5 = r0.getParentFile()     // Catch: java.io.IOException -> L26
            boolean r2 = r5.exists()     // Catch: java.io.IOException -> L26
            if (r2 != 0) goto L1f
            boolean r5 = r5.mkdirs()     // Catch: java.io.IOException -> L26
            if (r5 != 0) goto L1f
            return r1
        L1f:
            boolean r5 = r0.createNewFile()     // Catch: java.io.IOException -> L26
            if (r5 != 0) goto L2f
            return r1
        L26:
            r5 = move-exception
            boolean r6 = com.cdbwsoft.library.AppConfig.DEBUG
            if (r6 == 0) goto L2e
            r5.printStackTrace()
        L2e:
            return r1
        L2f:
            boolean r5 = r0.canWrite()
            if (r5 != 0) goto L36
            return r1
        L36:
            r5 = 0
            java.io.BufferedWriter r2 = new java.io.BufferedWriter     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            java.io.OutputStreamWriter r3 = new java.io.OutputStreamWriter     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r4.<init>(r0, r7)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L54
            r2.write(r6)     // Catch: java.lang.Exception -> L4e java.lang.Throwable -> L64
            r5 = 1
            r2.close()     // Catch: java.io.IOException -> L6b
            return r5
        L4e:
            r5 = move-exception
            goto L57
        L50:
            r6 = move-exception
            r2 = r5
            r5 = r6
            goto L65
        L54:
            r6 = move-exception
            r2 = r5
            r5 = r6
        L57:
            boolean r6 = com.cdbwsoft.library.AppConfig.DEBUG     // Catch: java.lang.Throwable -> L64
            if (r6 == 0) goto L5e
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L64
        L5e:
            if (r2 == 0) goto L73
            r2.close()     // Catch: java.io.IOException -> L6b
            goto L73
        L64:
            r5 = move-exception
        L65:
            if (r2 == 0) goto L6a
            r2.close()     // Catch: java.io.IOException -> L6b
        L6a:
            throw r5     // Catch: java.io.IOException -> L6b
        L6b:
            r5 = move-exception
            boolean r6 = com.cdbwsoft.library.AppConfig.DEBUG
            if (r6 == 0) goto L73
            r5.printStackTrace()
        L73:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdbwsoft.library.cache.CacheManager.saveCache(java.lang.String, java.lang.String, boolean):boolean");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r4v3, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r4v4 */
    public static String getCache(String str) throws Throwable {
        BufferedReader bufferedReader;
        String line;
        File file = new File(AppConfig.CACHE_PATH, str);
        ?? Exists = file.exists();
        try {
            try {
                if (Exists == 0) {
                    return null;
                }
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String str2 = "";
                    do {
                        try {
                            line = bufferedReader.readLine();
                            if (line != null) {
                                str2 = str2 + line;
                            }
                        } catch (Exception e) {
                            e = e;
                            if (AppConfig.DEBUG) {
                                e.printStackTrace();
                            }
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            return null;
                        }
                    } while (line != null);
                    bufferedReader.close();
                    return str2;
                } catch (Exception e2) {
                    e = e2;
                    bufferedReader = null;
                } catch (Throwable th) {
                    th = th;
                    Exists = 0;
                    if (Exists != 0) {
                        Exists.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e3) {
            if (AppConfig.DEBUG) {
                e3.printStackTrace();
            }
        }
    }

    public String saveBitmapCache(String str, Bitmap bitmap) {
        return saveBitmapCache(str, bitmap, Bitmap.CompressFormat.JPEG);
    }

    public String saveBitmapCache(String str, Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        if (!TextUtils.isEmpty(str) && bitmap != null) {
            File file = new File(this.mContext.getExternalCacheDir(), str);
            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(compressFormat, 100, bufferedOutputStream);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                return file.getAbsolutePath();
            } catch (IOException e) {
                if (AppConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x002c A[PHI: r1 r4
      0x002c: PHI (r1v5 android.graphics.Bitmap) = (r1v11 android.graphics.Bitmap), (r1v8 android.graphics.Bitmap) binds: [B:30:0x0046, B:14:0x002a] A[DONT_GENERATE, DONT_INLINE]
      0x002c: PHI (r4v7 'e' java.io.IOException) = (r4v6 'e' java.io.IOException), (r4v9 'e' java.io.IOException) binds: [B:30:0x0046, B:14:0x002a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x004e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.graphics.Bitmap getBitmapCache(java.lang.String r4) throws java.lang.Throwable {
        /*
            r3 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            r1 = 0
            if (r0 == 0) goto L8
            return r1
        L8:
            java.io.File r0 = new java.io.File
            android.content.Context r2 = r3.mContext
            java.io.File r2 = r2.getExternalCacheDir()
            r0.<init>(r2, r4)
            boolean r4 = r0.exists()
            if (r4 != 0) goto L1a
            return r1
        L1a:
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            r4.<init>(r0)     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L34
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeStream(r4)     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L4a
            r4.close()     // Catch: java.io.IOException -> L27
            goto L49
        L27:
            r4 = move-exception
            boolean r0 = com.cdbwsoft.library.AppConfig.DEBUG
            if (r0 == 0) goto L49
        L2c:
            r4.printStackTrace()
            goto L49
        L30:
            r0 = move-exception
            goto L36
        L32:
            r0 = move-exception
            goto L4c
        L34:
            r0 = move-exception
            r4 = r1
        L36:
            boolean r2 = com.cdbwsoft.library.AppConfig.DEBUG     // Catch: java.lang.Throwable -> L4a
            if (r2 == 0) goto L3d
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L4a
        L3d:
            if (r4 == 0) goto L49
            r4.close()     // Catch: java.io.IOException -> L43
            goto L49
        L43:
            r4 = move-exception
            boolean r0 = com.cdbwsoft.library.AppConfig.DEBUG
            if (r0 == 0) goto L49
            goto L2c
        L49:
            return r1
        L4a:
            r0 = move-exception
            r1 = r4
        L4c:
            if (r1 == 0) goto L5a
            r1.close()     // Catch: java.io.IOException -> L52
            goto L5a
        L52:
            r4 = move-exception
            boolean r1 = com.cdbwsoft.library.AppConfig.DEBUG
            if (r1 == 0) goto L5a
            r4.printStackTrace()
        L5a:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdbwsoft.library.cache.CacheManager.getBitmapCache(java.lang.String):android.graphics.Bitmap");
    }
}
