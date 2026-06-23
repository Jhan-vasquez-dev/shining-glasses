package com.icwork.shiningglass.base.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.vo.UpdateVO;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.App;
import com.icwork.shiningglass.base.app.C;
import com.icwork.shiningglass.base.app.LogInterceptor;
import com.icwork.shiningglass.provider.Provider;
import com.icwork.shiningglass.ui.utils.AppUtils;
import com.icwork.shiningglass.ui.utils.FileUtils;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.utils.SPUtils;
import com.icwork.shiningglass.ui.utils.TimeUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/* JADX INFO: loaded from: classes.dex */
public class UpdateManager {
    private static final int DOWN_BEFORE = 4;
    private static final int DOWN_FAIL = 3;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_UPDATE = 1;
    private Activity mContext;
    private AlertDialog mDownloadDialog;
    private DownloadListener mDownloadListener;
    private DownloadThread mDownloadThread;
    private ProgressBar mProgress;
    private boolean mShowDialog;
    private float mSize;
    private TextView mTvTotal;
    private boolean interceptFlag = false;
    private boolean mApk = true;
    private MessageHandler mHandler = new MessageHandler();

    public interface DownloadListener {
        void onDownloadComplete();

        void onDownloadFailed();

        void onDownloading(int i);

        void onInstall(File file);

        void onPreDownload(String str);
    }

    private class MessageHandler extends Handler {
        private MessageHandler() {
        }

        @Override // android.os.Handler
        public void dispatchMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                if (UpdateManager.this.mProgress != null) {
                    UpdateManager.this.mProgress.setProgress(message.arg1);
                }
                if (UpdateManager.this.mDownloadListener != null) {
                    UpdateManager.this.mDownloadListener.onDownloading(message.arg1);
                    return;
                }
                return;
            }
            if (i == 2) {
                if (UpdateManager.this.mDownloadListener != null) {
                    UpdateManager.this.mDownloadListener.onDownloadComplete();
                }
                UpdateManager.this.install((File) message.obj);
                return;
            }
            if (i != 3) {
                if (i == 4) {
                    if (UpdateManager.this.mDownloadListener != null) {
                        UpdateManager.this.mDownloadListener.onPreDownload((String) message.obj);
                        return;
                    }
                    return;
                }
                super.dispatchMessage(message);
                return;
            }
            if (UpdateManager.this.mDownloadDialog != null) {
                UpdateManager.this.mDownloadDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateManager.this.mContext);
                builder.setCancelable(false);
                builder.setMessage(UpdateManager.this.mContext.getResources().getString(R.string.download_error));
                final String str = (String) message.obj;
                final float f = UpdateManager.this.mSize;
                builder.setPositiveButton(UpdateManager.this.mContext.getResources().getString(R.string.update_retry), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.base.update.UpdateManager.MessageHandler.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        dialogInterface.dismiss();
                        UpdateManager.this.downloadFile(str, f, UpdateManager.this.mShowDialog);
                    }
                });
                builder.setNegativeButton(UpdateManager.this.mContext.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.base.update.UpdateManager.MessageHandler.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
            if (UpdateManager.this.mDownloadListener != null) {
                UpdateManager.this.mDownloadListener.onDownloadFailed();
            }
        }
    }

    public UpdateManager(Activity activity) {
        this.mContext = activity;
    }

    public void versionUpdate() {
        OkHttpClient okHttpClientBuild = new OkHttpClient.Builder().addInterceptor(new LogInterceptor()).build();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("app_id", AppUtils.getPackageName(this.mContext));
        builder.add("platform", AppConfig.PLATFORM);
        okHttpClientBuild.newCall(new Request.Builder().url("http://api.e-toys.cn/api/app/lastUpdate").post(builder.build()).build()).enqueue(new Callback() { // from class: com.icwork.shiningglass.base.update.UpdateManager.1
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                final UpdateVO updateVO;
                try {
                    JSONObject object = JSON.parseObject(response.body().string());
                    if (object.getInteger(NotificationCompat.CATEGORY_STATUS).intValue() == 0) {
                        String string = object.getString("data");
                        if (TextUtils.isEmpty(string) || (updateVO = (UpdateVO) JSONObject.parseObject(string, UpdateVO.class)) == null) {
                            return;
                        }
                        int versionCode = App.getInstance().getVersionCode();
                        if (TextUtils.isEmpty(updateVO.app_url) || versionCode < 0 || updateVO.app_version_number <= versionCode) {
                            return;
                        }
                        final String str = UpdateManager.this.mContext.getResources().getString(R.string.find_new_version) + ":" + updateVO.app_version + "\n\n" + UpdateManager.this.mContext.getResources().getString(R.string.size) + ":" + (((int) ((updateVO.app_size / 1024.0f) * 100.0f)) / 100) + "MB\n\n" + updateVO.app_update;
                        UpdateManager.this.mHandler.post(new Runnable() { // from class: com.icwork.shiningglass.base.update.UpdateManager.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                UpdateManager.this.showNoticeDialog(str, updateVO.app_url, updateVO.app_size);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showNoticeDialog(String str, String str2, float f) {
        showNoticeDialog(str, str2, f, true);
    }

    public void showNoticeDialog(String str, final String str2, final float f, final boolean z) {
        LogUtil.d("showNoticeDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setCancelable(false);
        builder.setTitle(this.mContext.getResources().getString(R.string.update_title));
        builder.setMessage(str);
        builder.setPositiveButton(this.mContext.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.base.update.UpdateManager.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                UpdateManager.this.downloadFile(str2, f, z);
            }
        });
        builder.setNegativeButton(this.mContext.getResources().getString(R.string.update_after), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.base.update.UpdateManager.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void setDownloadDialog(AlertDialog alertDialog) {
        if (alertDialog != this.mDownloadDialog) {
            this.mDownloadDialog = alertDialog;
        }
    }

    public void downloadFile(String str, float f, boolean z) {
        if (TextUtils.isEmpty(str)) {
            Activity activity = this.mContext;
            Toast.makeText(activity, activity.getResources().getString(R.string.download_url_invalid), 1).show();
            return;
        }
        File externalFilePath = FileUtils.getExternalFilePath(this.mContext, "apk");
        if (!externalFilePath.exists() && externalFilePath.mkdir()) {
            Activity activity2 = this.mContext;
            Toast.makeText(activity2, activity2.getResources().getString(R.string.no_permission), 1).show();
            return;
        }
        String strSubstring = str.substring(str.lastIndexOf("/"));
        File file = new File(externalFilePath, strSubstring);
        if (file.exists()) {
            install(file);
            return;
        }
        try {
            if (this.mDownloadDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
                builder.setCancelable(false);
                builder.setTitle(this.mContext.getResources().getString(R.string.update_title));
                builder.setView(R.layout.update_layout);
                this.mDownloadDialog = builder.create();
                builder.setNegativeButton(this.mContext.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.base.update.UpdateManager.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        UpdateManager.this.mDownloadThread.interrupt();
                        UpdateManager.this.interceptFlag = true;
                    }
                });
            }
            this.mSize = f;
            this.mDownloadDialog.show();
            View viewFindViewById = this.mDownloadDialog.findViewById(R.id.progress);
            if (viewFindViewById != null && (viewFindViewById instanceof ProgressBar)) {
                this.mProgress = (ProgressBar) viewFindViewById;
            }
            View viewFindViewById2 = this.mDownloadDialog.findViewById(R.id.tv_total);
            if (viewFindViewById2 != null && (viewFindViewById2 instanceof TextView)) {
                TextView textView = (TextView) viewFindViewById2;
                this.mTvTotal = textView;
                textView.setText(this.mContext.getString(R.string.total_size, new Object[]{Float.valueOf(this.mSize / 1024.0f)}));
            }
            this.mShowDialog = z;
            downloadApk(new File(externalFilePath, strSubstring + ".tmp"), str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDownloadDialog(String str, float f) {
        downloadFile(str, f, true);
    }

    private class DownloadThread extends Thread {
        String downloadUrl;
        File saveFile;

        DownloadThread(File file, String str) {
            this.saveFile = file;
            this.downloadUrl = str;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.downloadUrl).openConnection();
                httpURLConnection.connect();
                int contentLength = httpURLConnection.getContentLength();
                InputStream inputStream = httpURLConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(this.saveFile);
                UpdateManager.this.mHandler.obtainMessage(4, this.downloadUrl).sendToTarget();
                byte[] bArr = new byte[1024];
                int i = 0;
                while (true) {
                    int i2 = inputStream.read(bArr);
                    i += i2;
                    UpdateManager.this.mHandler.obtainMessage(1, (int) ((i / contentLength) * 100.0f), 0).sendToTarget();
                    if (i2 <= 0) {
                        File file = new File(this.saveFile.getCanonicalPath().replace(".tmp", ""));
                        this.saveFile.renameTo(file);
                        UpdateManager.this.mHandler.obtainMessage(2, file).sendToTarget();
                        break;
                    } else {
                        fileOutputStream.write(bArr, 0, i2);
                        if (UpdateManager.this.interceptFlag) {
                            break;
                        }
                    }
                }
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                UpdateManager.this.mHandler.obtainMessage(3, this.downloadUrl).sendToTarget();
            }
        }
    }

    public DownloadListener getDownloadListener() {
        return this.mDownloadListener;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
    }

    public boolean isApk() {
        return this.mApk;
    }

    public void setApk(boolean z) {
        this.mApk = z;
    }

    private void downloadApk(File file, String str) {
        DownloadThread downloadThread = this.mDownloadThread;
        if (downloadThread == null || !downloadThread.isAlive()) {
            DownloadThread downloadThread2 = new DownloadThread(file, str);
            this.mDownloadThread = downloadThread2;
            downloadThread2.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void install(File file) {
        AlertDialog alertDialog = this.mDownloadDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (file.exists()) {
            DownloadListener downloadListener = this.mDownloadListener;
            if (downloadListener != null) {
                downloadListener.onInstall(file);
            }
            if (this.mApk) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setFlags(268435456);
                String name = Provider.class.getName();
                LogUtil.d("packageName:" + name);
                Uri uriForFile = FileProvider.getUriForFile(this.mContext, name, file);
                intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
                grantUriPermission(this.mContext, uriForFile, intent);
                this.mContext.startActivity(intent);
            }
        }
    }

    private static void grantUriPermission(Context context, Uri uri, Intent intent) {
        Iterator<ResolveInfo> it = context.getPackageManager().queryIntentActivities(intent, 65536).iterator();
        while (it.hasNext()) {
            context.grantUriPermission(it.next().activityInfo.packageName, uri, 3);
        }
    }

    public static void uploadInstallInfo(final Context context) {
        if (((Boolean) SPUtils.get(context, C.SP.FIRST_INSTALL, true)).booleanValue()) {
            OkHttpClient okHttpClientBuild = new OkHttpClient.Builder().addInterceptor(new LogInterceptor()).build();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("app_package", AppUtils.getPackageName(context));
            builder.add("app_channel", AppUtils.getAppMetaData(context, AppConfig.META_CHANNEL));
            builder.add("phone_system", AppConfig.PLATFORM);
            builder.add("phone_brands", AppUtils.getDeviceBrand());
            builder.add("phone_model", AppUtils.getSystemModel());
            builder.add("phone_system_version", AppUtils.getSystemVersion());
            builder.add("run_time", TimeUtils.getNowTime());
            okHttpClientBuild.newCall(new Request.Builder().url("http://api.e-toys.cn/api/app/count").post(builder.build()).build()).enqueue(new Callback() { // from class: com.icwork.shiningglass.base.update.UpdateManager.5
                @Override // okhttp3.Callback
                public void onFailure(Call call, IOException iOException) {
                }

                @Override // okhttp3.Callback
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (JSON.parseObject(response.body().string()).getInteger(NotificationCompat.CATEGORY_STATUS).intValue() == 0) {
                            LogUtil.i("onResponse: 上传安装信息成功");
                            SPUtils.put(context, C.SP.FIRST_INSTALL, false);
                        } else {
                            LogUtil.e("onResponse: 上传安装信息失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
