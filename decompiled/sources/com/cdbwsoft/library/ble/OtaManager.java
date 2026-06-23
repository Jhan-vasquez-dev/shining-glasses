package com.cdbwsoft.library.ble;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ProgressBar;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.R;
import com.cdbwsoft.library.ble.BleGlobalVariables;
import java.io.File;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class OtaManager {
    public static final String TAG = "OtaManager";
    private Context mContext;
    private File mOtaFile;
    private ProgressBar mProgress;
    private ProgressDialog mUpdateDialog;
    private UpdateListener mUpdateListener;
    private MessageHandler mHandler = new MessageHandler(this);
    private SparseArray<BleOtaUpdater> mUpdateList = new SparseArray<>();
    private int mCounter = 0;
    private boolean mSingle = true;
    private boolean mStopUpdate = false;

    public interface UpdateListener {
        void onPreUpdate(BleDevice bleDevice);

        void onUpdateComplete(BleDevice bleDevice);

        void onUpdateFailed(BleDevice bleDevice);

        void onUpdating(BleDevice bleDevice, int i);
    }

    private static class MessageHandler extends Handler {
        private OtaManager mOtaManager;

        public MessageHandler(OtaManager otaManager) {
            this.mOtaManager = otaManager;
        }

        @Override // android.os.Handler
        public void dispatchMessage(Message message) {
            BleDevice bleDevice;
            if (this.mOtaManager != null) {
                if (AppConfig.DEBUG) {
                    Log.i(OtaManager.TAG, "dispatchMessage:" + message.what);
                }
                Integer num = (Integer) message.obj;
                BleOtaUpdater bleOtaUpdater = null;
                if (num != null) {
                    BleOtaUpdater bleOtaUpdater2 = (BleOtaUpdater) this.mOtaManager.mUpdateList.get(num.intValue());
                    bleOtaUpdater = bleOtaUpdater2;
                    bleDevice = bleOtaUpdater2 != null ? bleOtaUpdater2.getBleDevice() : null;
                } else {
                    bleDevice = null;
                }
                int i = message.what;
                if (i == 1) {
                    if (this.mOtaManager.mStopUpdate) {
                        return;
                    }
                    this.mOtaManager.showProgress(message.arg1);
                    if (this.mOtaManager.mUpdateListener != null) {
                        this.mOtaManager.mUpdateListener.onUpdating(bleDevice, message.arg1);
                        return;
                    }
                    return;
                }
                if (i == 2) {
                    if (this.mOtaManager.mUpdateDialog != null) {
                        this.mOtaManager.mUpdateDialog.dismiss();
                    }
                    if (bleOtaUpdater != null) {
                        this.mOtaManager.mUpdateList.remove(bleOtaUpdater.getIndex());
                    }
                    if (this.mOtaManager.mUpdateListener != null) {
                        this.mOtaManager.mUpdateListener.onUpdateComplete(bleDevice);
                        return;
                    }
                    return;
                }
                if (i == 3) {
                    if (this.mOtaManager.mUpdateDialog != null) {
                        this.mOtaManager.mUpdateDialog.dismiss();
                    }
                    if (this.mOtaManager.mUpdateListener != null) {
                        this.mOtaManager.mUpdateListener.onUpdateFailed(bleDevice);
                        return;
                    }
                    return;
                }
                if (i == 4) {
                    if (this.mOtaManager.mUpdateListener != null) {
                        this.mOtaManager.mUpdateListener.onPreUpdate(bleDevice);
                        return;
                    }
                    return;
                }
                super.dispatchMessage(message);
            }
        }
    }

    public OtaManager(Context context) {
        this.mContext = context;
    }

    public void setSingle(boolean z) {
        this.mSingle = z;
    }

    public UpdateListener getUpdateListener() {
        return this.mUpdateListener;
    }

    public void setUpdateListener(UpdateListener updateListener) {
        this.mUpdateListener = updateListener;
    }

    private void retryUpdate(BleOtaUpdater bleOtaUpdater) {
        if (this.mStopUpdate || bleOtaUpdater == null) {
            return;
        }
        this.mUpdateList.remove(bleOtaUpdater.getIndex());
        startOtaUpdate(this.mOtaFile, bleOtaUpdater.getBleDevice());
    }

    public boolean startOtaUpdate(File file, BleDevice bleDevice) {
        String canonicalPath;
        if (bleDevice == null || file == null || !file.exists() || !file.canRead() || (this.mSingle && this.mUpdateList.size() > 0)) {
            return false;
        }
        this.mCounter++;
        BleOtaUpdater bleOtaUpdater = new BleOtaUpdater(this.mHandler);
        bleOtaUpdater.setIndex(this.mCounter);
        this.mUpdateList.put(this.mCounter, bleOtaUpdater);
        try {
            this.mOtaFile = file;
            canonicalPath = file.getCanonicalPath();
            this.mStopUpdate = false;
        } catch (IOException e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return bleOtaUpdater.otaStart(canonicalPath, bleDevice) == BleGlobalVariables.OtaResult.OTA_RESULT_SUCCESS;
    }

    public void stopAll() {
        if (this.mStopUpdate) {
            return;
        }
        this.mStopUpdate = true;
        if (this.mUpdateList.size() > 0) {
            int size = this.mUpdateList.size();
            for (int i = 0; i < size; i++) {
                BleOtaUpdater bleOtaUpdater = this.mUpdateList.get(this.mUpdateList.keyAt(i));
                if (bleOtaUpdater != null) {
                    bleOtaUpdater.otaStop();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showProgress(int i) {
        if (this.mSingle) {
            if (this.mUpdateDialog == null) {
                ProgressDialog progressDialog = new ProgressDialog(this.mContext, 3);
                this.mUpdateDialog = progressDialog;
                progressDialog.setTitle(R.string.ota_title);
                this.mUpdateDialog.setMessage(this.mContext.getString(R.string.updating));
                this.mUpdateDialog.setCancelable(false);
                this.mUpdateDialog.setCanceledOnTouchOutside(false);
                this.mUpdateDialog.setProgressStyle(1);
            }
            if (!this.mUpdateDialog.isShowing()) {
                this.mUpdateDialog.show();
            }
            this.mUpdateDialog.setProgress(i);
        }
    }
}
