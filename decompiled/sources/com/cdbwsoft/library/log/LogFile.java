package com.cdbwsoft.library.log;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.BaseApplication;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class LogFile {
    public static final String TAG = "LogFile";
    private File mFile;
    private String mFileName;
    private String mFilePath;
    private Map<String, String> mDeviceInfo = new HashMap();
    private SimpleDateFormat mTimeFormatter = new SimpleDateFormat(AppConfig.LOG_TIME_FORMAT, Locale.getDefault());

    public LogFile(String str, String str2) {
        this.mFilePath = str;
        this.mFileName = str2;
        initInfo();
    }

    public String getFileName() {
        return this.mFileName;
    }

    private void initInfo() {
        this.mDeviceInfo.put("versionName", BaseApplication.getInstance().getVersionName());
        this.mDeviceInfo.put("versionCode", String.valueOf(BaseApplication.getInstance().getVersionCode()));
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.mDeviceInfo.put(field.getName(), field.get(null).toString());
                if (AppConfig.DEBUG) {
                    Log.d(TAG, field.getName() + " : " + field.get(null));
                }
            } catch (Exception e) {
                if (AppConfig.DEBUG) {
                    Log.e(TAG, "an error occured when collect crash info", e);
                }
            }
        }
    }

    public void write(String str) {
        BufferedWriter bufferedWriter;
        try {
            if (checkWritable()) {
                BufferedWriter bufferedWriter2 = null;
                try {
                    try {
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.mFile, true)));
                    } catch (Exception e) {
                        e = e;
                    }
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    bufferedWriter.write(this.mTimeFormatter.format(new Date()) + "：");
                    bufferedWriter.write(str);
                    bufferedWriter.close();
                } catch (Exception e2) {
                    e = e2;
                    bufferedWriter2 = bufferedWriter;
                    if (AppConfig.DEBUG) {
                        e.printStackTrace();
                    }
                    if (bufferedWriter2 != null) {
                        bufferedWriter2.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bufferedWriter2 = bufferedWriter;
                    if (bufferedWriter2 != null) {
                        bufferedWriter2.close();
                    }
                    throw th;
                }
            }
        } catch (IOException e3) {
            if (AppConfig.DEBUG) {
                e3.printStackTrace();
            }
        }
    }

    public boolean exists() {
        File file = this.mFile;
        return file != null && file.exists();
    }

    public void write(Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            sb.append(key).append("=").append(entry.getValue()).append("\n");
        }
        write(sb.toString());
    }

    private boolean checkWritable() throws IOException {
        if (this.mFile == null) {
            if (TextUtils.isEmpty(this.mFilePath)) {
                return false;
            }
            this.mFile = new File(this.mFilePath, this.mFileName);
        }
        if (!this.mFile.exists()) {
            if (!this.mFile.createNewFile()) {
                return false;
            }
            write(this.mDeviceInfo);
            this.mDeviceInfo.clear();
        }
        return this.mFile.canWrite();
    }
}
