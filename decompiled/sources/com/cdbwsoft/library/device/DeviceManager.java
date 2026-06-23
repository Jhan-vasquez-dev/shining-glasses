package com.cdbwsoft.library.device;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.BaseApplication;
import com.cdbwsoft.library.net.FileListener;
import com.cdbwsoft.library.net.NetApi;
import com.cdbwsoft.library.net.ResponseListener;
import com.cdbwsoft.library.net.entity.ProgressFileBody;
import com.cdbwsoft.library.net.entity.Response;
import com.cdbwsoft.library.net.entity.SuperResponse;
import com.cdbwsoft.library.statistics.Running;
import com.cdbwsoft.library.utils.SharedPreferenceUtils;
import com.cdbwsoft.library.utils.ToolUtils;
import com.cdbwsoft.library.vo.AppVO;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DeviceManager {
    public static final String TAG = "DeviceManager";
    public static final String UUID = "uuid";
    public static boolean syncData = false;
    public static boolean syncInstalled = false;
    public static String uuid;

    public static void init() {
        if (AppConfig.BIND_DEVICE && TextUtils.isEmpty(uuid)) {
            String str = SharedPreferenceUtils.get(BaseApplication.getInstance(), UUID);
            if (!TextUtils.isEmpty(str)) {
                uuid = str;
                return;
            }
            String uniqueID = ToolUtils.getUniqueID();
            if (TextUtils.isEmpty(uniqueID)) {
                return;
            }
            NetApi.App.bindDevice(BaseApplication.getInstance().getPackageName(), uniqueID, Build.BRAND, Build.MODEL, AppConfig.PLATFORM, Build.VERSION.SDK_INT, new ResponseListener<Response>() { // from class: com.cdbwsoft.library.device.DeviceManager.1
                @Override // com.cdbwsoft.library.net.ResponseListener, com.android.volley.Response.Listener
                public void onResponse(Response response) {
                    if (response.isSuccess()) {
                        DeviceManager.uuid = response.getData();
                        SharedPreferenceUtils.put(BaseApplication.getInstance(), DeviceManager.UUID, DeviceManager.uuid);
                        try {
                            DeviceManager.syncData();
                            DeviceManager.syncInstall();
                        } catch (Exception e) {
                            if (AppConfig.DEBUG) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    public static void syncInstall() {
        if (AppConfig.UPLOAD_APP_INSTALLS && BaseApplication.getInstance().isWifi()) {
            String uniqueID = ToolUtils.getUniqueID();
            if (TextUtils.isEmpty(uniqueID) || syncInstalled) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
            if (packageManager == null) {
                return;
            }
            if (AppConfig.DEBUG) {
                Log.i(TAG, "App installed getting");
            }
            List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
            for (int i = 0; i < installedPackages.size(); i++) {
                PackageInfo packageInfo = installedPackages.get(i);
                if (packageInfo == null || TextUtils.isEmpty(packageInfo.packageName)) {
                    return;
                }
                AppVO appVO = new AppVO();
                appVO.app_name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                appVO.app_package = packageInfo.packageName;
                appVO.app_version_name = packageInfo.versionName;
                appVO.app_version_code = packageInfo.versionCode;
                appVO.install_time = packageInfo.firstInstallTime;
                appVO.update_time = packageInfo.lastUpdateTime;
                if (packageInfo.applicationInfo.metaData != null) {
                    appVO.app_channel = packageInfo.applicationInfo.metaData.getString(AppConfig.META_CHANNEL);
                }
                if ((packageInfo.applicationInfo.flags & 1) == 0) {
                    arrayList.add(appVO);
                }
            }
            if (AppConfig.DEBUG) {
                Log.i(TAG, "App installed uploading");
            }
            syncInstalled = true;
            NetApi.App.appInstalled(uniqueID, arrayList, new ResponseListener<Response>() { // from class: com.cdbwsoft.library.device.DeviceManager.2
                @Override // com.cdbwsoft.library.net.ResponseListener, com.android.volley.Response.Listener
                public void onResponse(Response response) {
                    if (response.isSuccess() && AppConfig.DEBUG) {
                        Log.i(DeviceManager.TAG, "App installed uploaded");
                    }
                }
            });
        }
    }

    public static void syncData() {
        String uniqueID = ToolUtils.getUniqueID();
        if (!AppConfig.UPLOAD_RUNNING_LOGS || TextUtils.isEmpty(uniqueID) || syncData || !BaseApplication.getInstance().isWifi()) {
            return;
        }
        syncData = true;
        NetApi.App.runningLog(uniqueID, BaseApplication.getInstance().getPackageName(), BaseApplication.getInstance().getVersionCode(), BaseApplication.getInstance().getVersionName(), new FileListener() { // from class: com.cdbwsoft.library.device.DeviceManager.3
            @Override // com.cdbwsoft.library.net.FileListener
            public List<ProgressFileBody> getFiles() {
                ArrayList arrayList = new ArrayList();
                List<File> files = Running.getFiles();
                if (files == null) {
                    return null;
                }
                if (files != null && files.size() > 0) {
                    int i = 0;
                    for (File file : files) {
                        if (file.exists()) {
                            arrayList.add(new ProgressFileBody(file, "file" + i));
                            i++;
                        }
                    }
                }
                return arrayList;
            }

            @Override // com.android.volley.Response.Listener
            public void onResponse(SuperResponse superResponse) {
                if (superResponse.isSuccess()) {
                    Iterator<ProgressFileBody> it = getFileRequest().getFiles().iterator();
                    while (it.hasNext()) {
                        it.next().getFile().delete();
                    }
                }
            }
        });
    }
}
