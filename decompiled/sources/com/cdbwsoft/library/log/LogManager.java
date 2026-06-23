package com.cdbwsoft.library.log;

import android.content.Context;
import android.text.TextUtils;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.BaseApplication;
import com.cdbwsoft.library.net.FileListener;
import com.cdbwsoft.library.net.NetApi;
import com.cdbwsoft.library.net.entity.ProgressFileBody;
import com.cdbwsoft.library.net.entity.SuperResponse;
import com.cdbwsoft.library.utils.ToolUtils;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class LogManager {
    public static final String TAG = "LogManager";
    private Context mContext;
    private LogFile mLogFile;
    private String mLogPath = AppConfig.PATH_LOGS;

    public LogManager(Context context) {
        this.mContext = context;
    }

    private LogFile getLogFile() {
        String fileName = getFileName();
        LogFile logFile = this.mLogFile;
        if (logFile != null && fileName.equalsIgnoreCase(logFile.getFileName())) {
            return this.mLogFile;
        }
        File file = new File(this.mLogPath);
        if ((!file.exists() && file.mkdirs()) || !file.exists()) {
            return null;
        }
        LogFile logFile2 = new LogFile(this.mLogPath, fileName);
        this.mLogFile = logFile2;
        return logFile2;
    }

    private String getFileName() {
        return new SimpleDateFormat(AppConfig.LOG_FILE_NAME, Locale.getDefault()).format(new Date()) + AppConfig.LOG_EXT;
    }

    public void log(String str) {
        LogFile logFile = getLogFile();
        if (logFile != null) {
            logFile.write(str);
        }
    }

    public void log(Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            sb.append(key).append("=").append(entry.getValue()).append("\n");
        }
        log(sb.toString());
    }

    public void log(Throwable th) {
        if (th == null) {
            return;
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        log(stringWriter.toString());
    }

    public void checkAndUpload() {
        File[] fileArrListFiles;
        if (!AppConfig.UPLOAD_ERROR_LOGS || TextUtils.isEmpty(this.mLogPath)) {
            return;
        }
        File file = new File(this.mLogPath);
        if (!file.exists() || (fileArrListFiles = file.listFiles()) == null || fileArrListFiles.length == 0) {
            return;
        }
        final ArrayList arrayList = new ArrayList();
        int i = Calendar.getInstance().get(6);
        int length = fileArrListFiles.length;
        for (int i2 = 0; i2 < length; i2++) {
            File file2 = fileArrListFiles[i2];
            if (!file2.isDirectory()) {
                String name = file2.getName();
                if (name.endsWith(AppConfig.LOG_EXT)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(file2.lastModified());
                    if (i != calendar.get(6) && !BaseApplication.getInstance().getCacheManager().hasCacheText(name)) {
                        arrayList.add(new ProgressFileBody(file2, "file" + i2));
                    }
                }
            }
        }
        if (arrayList.size() == 0) {
            return;
        }
        String uniqueID = ToolUtils.getUniqueID();
        if (TextUtils.isEmpty(uniqueID)) {
            uniqueID = "";
        }
        NetApi.App.uploadLog(this.mContext.getPackageName(), BaseApplication.getInstance().getVersionCode(), BaseApplication.getInstance().getVersionName(), uniqueID, new FileListener() { // from class: com.cdbwsoft.library.log.LogManager.1
            @Override // com.cdbwsoft.library.net.FileListener
            public List<ProgressFileBody> getFiles() {
                return new ArrayList(arrayList);
            }

            @Override // com.android.volley.Response.Listener
            public void onResponse(SuperResponse superResponse) {
                if (superResponse.isSuccess()) {
                    for (ProgressFileBody progressFileBody : arrayList) {
                        BaseApplication.getInstance().getCacheManager().cacheText(progressFileBody.getFile().getName(), "true");
                        progressFileBody.getFile().delete();
                    }
                }
            }
        });
    }
}
