package com.cdbwsoft.library;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import androidx.multidex.MultiDexApplication;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cdbwsoft.library.ble.BleDevice;
import com.cdbwsoft.library.ble.BleDeviceFactory;
import com.cdbwsoft.library.ble.BleListener;
import com.cdbwsoft.library.ble.BleManager;
import com.cdbwsoft.library.cache.CacheManager;
import com.cdbwsoft.library.device.DeviceManager;
import com.cdbwsoft.library.log.LogManager;
import com.cdbwsoft.library.net.HurlStack;
import com.cdbwsoft.library.setting.SettingManager;
import com.cdbwsoft.library.statistics.Running;

/* JADX INFO: loaded from: classes.dex */
public class BaseApplication extends MultiDexApplication {
    private static final String TAG = "BaseApplication";
    private static BaseApplication mApplication;
    private BleManager<? extends BleDevice> mBleManager;
    private CacheManager mCacheManager;
    private LogManager mLogManager;
    private RequestQueue mRequestQueue;
    private SettingManager mSettingManager;
    private int mVersionCode;
    private String mVersionName;

    public boolean isDebug() {
        return false;
    }

    @Override // android.app.Application
    public void onCreate() {
        mApplication = this;
        super.onCreate();
        AppConfig.init(this);
        String processName1 = getProcessName1();
        if (TextUtils.isEmpty(processName1) || processName1.indexOf(":") <= 0) {
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 16384);
                if (packageInfo != null) {
                    this.mVersionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                    this.mVersionCode = packageInfo.versionCode;
                }
            } catch (PackageManager.NameNotFoundException e) {
                if (AppConfig.DEBUG) {
                    Log.e(TAG, "an error occured when collect package info", e);
                }
            }
            AppConfig.DEBUG = isDebug();
            try {
                this.mCacheManager = new CacheManager(this);
                this.mSettingManager = new SettingManager(this);
                this.mLogManager = new LogManager(this);
                new CrashHandler(this);
                if (isWifi()) {
                    this.mLogManager.checkAndUpload();
                }
                DeviceManager.init();
                DeviceManager.syncData();
                DeviceManager.syncInstall();
                Running.app(1, "enter", String.valueOf(System.currentTimeMillis()));
                Runtime.getRuntime().addShutdownHook(new Thread() { // from class: com.cdbwsoft.library.BaseApplication.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        Running.app(2, "exit", String.valueOf(System.currentTimeMillis()));
                    }
                });
            } catch (Exception e2) {
                if (AppConfig.DEBUG) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public String getProcessName1() {
        int iMyPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) getSystemService("activity")).getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid == iMyPid) {
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }

    public String getVersionName() {
        return this.mVersionName;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public CacheManager getCacheManager() {
        return this.mCacheManager;
    }

    public SettingManager getSettingManager() {
        return this.mSettingManager;
    }

    public LogManager getLogManager() {
        return this.mLogManager;
    }

    public static BaseApplication getInstance() {
        return mApplication;
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            RequestQueue requestQueueNewRequestQueue = Volley.newRequestQueue(this, new HurlStack());
            this.mRequestQueue = requestQueueNewRequestQueue;
            requestQueueNewRequestQueue.start();
        }
        return this.mRequestQueue;
    }

    public boolean isWifi() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public <T extends BleDevice> BleManager<T> getBleManager(Class<T> cls, BleListener<T> bleListener, BleDeviceFactory<T> bleDeviceFactory) {
        BleManager<? extends BleDevice> bleManager = this.mBleManager;
        if (bleManager != null && bleManager.getDeviceClass() == cls) {
            BleManager<T> bleManager2 = (BleManager<T>) this.mBleManager;
            if (bleListener != null) {
                bleListener.setBleManager(bleManager2);
                bleManager2.registerBleListener(bleListener);
            }
            return bleManager2;
        }
        BleManager<? extends BleDevice> bleManager3 = this.mBleManager;
        if (bleManager3 != null) {
            bleManager3.release();
        }
        AnonymousClass2 anonymousClass2 = (BleManager<T>) new BleManager<T>(this, bleListener, bleDeviceFactory) { // from class: com.cdbwsoft.library.BaseApplication.2
        };
        this.mBleManager = anonymousClass2;
        return anonymousClass2;
    }

    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
