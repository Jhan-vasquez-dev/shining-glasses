package com.cdbwsoft.library;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;
import java.io.IOException;
import java.lang.Thread;

/* JADX INFO: loaded from: classes.dex */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

    public CrashHandler(Context context) {
        this.mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        try {
            if (AppConfig.DEBUG) {
                th.printStackTrace();
            }
            BaseApplication.getInstance().getLogManager().log(th);
            if (thread.getId() != Looper.getMainLooper().getThread().getId()) {
                this.mDefaultHandler.uncaughtException(thread, th);
                return;
            }
            Toast.makeText(this.mContext, "抱歉，程序由于出错而终止！", 1).show();
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.mDefaultHandler;
            if (uncaughtExceptionHandler != null) {
                uncaughtExceptionHandler.uncaughtException(thread, th);
            }
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public static void onNativeCrashed() {
        new RuntimeException("crashed here (native trace should follow after the Java trace)").printStackTrace();
        try {
            Runtime.getRuntime().exec(new String[]{"logcat", "-d", "-v", "threadtime"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
