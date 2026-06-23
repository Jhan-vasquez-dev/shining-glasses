package com.icwork.shiningglass.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.app.LanguageUtil;
import com.icwork.shiningglass.ui.utils.ToastUtil;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseActivity extends AppCompatActivity {
    public Activity mActivity;
    public Context mContext;
    private int mPermissionIdx = 16;
    private SparseArray<GrantedResult> mPermissions = new SparseArray<>();
    private ProgressDialog progressDialog;

    protected abstract void bindListener();

    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract void initView();

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            this.mActivity = this;
            this.mContext = this;
            setContentView(getLayoutId());
            initView();
            initData();
            bindListener();
            AppManager.getAppManager().addActivity(this);
        } catch (Exception unused) {
            ToastUtil.showToast(getString(R.string.error_restart));
        }
    }

    protected void toActivity(Class cls) {
        startActivity(new Intent(this, (Class<?>) cls));
    }

    protected void toActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(this, (Class<?>) cls);
        intent.putExtra(cls.getSimpleName(), bundle);
        startActivity(intent);
    }

    protected View inflate(int i) {
        return LayoutInflater.from(this).inflate(i, (ViewGroup) null);
    }

    public void requestPermission(final String[] strArr, String str, GrantedResult grantedResult) {
        if (grantedResult == null) {
            return;
        }
        grantedResult.mGranted = false;
        if (strArr == null || strArr.length == 0) {
            grantedResult.mGranted = true;
            runOnUiThread(grantedResult);
            return;
        }
        final int i = this.mPermissionIdx;
        this.mPermissionIdx = i + 1;
        this.mPermissions.put(i, grantedResult);
        boolean z = true;
        for (String str2 : strArr) {
            z = z && checkSelfPermission(str2) == 0;
        }
        if (z) {
            grantedResult.mGranted = true;
            runOnUiThread(grantedResult);
            return;
        }
        boolean z2 = true;
        for (String str3 : strArr) {
            z2 = z2 && !shouldShowRequestPermissionRationale(str3);
        }
        if (!z2) {
            new AlertDialog.Builder(this).setMessage(str).setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.base.BaseActivity.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    BaseActivity.this.requestPermissions(strArr, i);
                }
            }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.base.BaseActivity.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    dialogInterface.dismiss();
                    GrantedResult grantedResult2 = (GrantedResult) BaseActivity.this.mPermissions.get(i);
                    if (grantedResult2 == null) {
                        return;
                    }
                    grantedResult2.mGranted = false;
                    BaseActivity.this.runOnUiThread(grantedResult2);
                }
            }).create().show();
        } else {
            requestPermissions(strArr, i);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        GrantedResult grantedResult = this.mPermissions.get(i);
        if (grantedResult == null) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            grantedResult.mGranted = true;
        }
        runOnUiThread(grantedResult);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(context, LanguageUtil.getSaveLanguage(context)));
    }

    public static abstract class GrantedResult implements Runnable {
        private boolean mGranted;

        public abstract void onResult(boolean z);

        @Override // java.lang.Runnable
        public void run() {
            onResult(this.mGranted);
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    public void showProgressDialog(Context context, String str) {
        if (this.progressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            this.progressDialog = progressDialog;
            progressDialog.setProgressStyle(0);
        }
        this.progressDialog.setMessage(str);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
        new Handler().postDelayed(new Runnable() { // from class: com.icwork.shiningglass.base.BaseActivity.3
            @Override // java.lang.Runnable
            public void run() {
                BaseActivity.this.dismissProgressDialog();
            }
        }, 10000L);
    }

    public void showProgressDialog1(Context context, String str) {
        if (this.progressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            this.progressDialog = progressDialog;
            progressDialog.setProgressStyle(0);
        }
        this.progressDialog.setMessage(str);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    public Boolean dismissProgressDialog() {
        try {
            try {
                ProgressDialog progressDialog = this.progressDialog;
                if (progressDialog != null && progressDialog.isShowing()) {
                    if (!isFinishing() && !isDestroyed()) {
                        this.progressDialog.dismiss();
                        return true;
                    }
                    Log.w("BaseActivity", "Skip dismissProgressDialog(): Activity is finishing or destroyed");
                }
            } catch (Exception e) {
                Log.e("BaseActivity", "dismissProgressDialog() failed: " + e.getMessage());
            }
            return false;
        } finally {
            this.progressDialog = null;
        }
    }
}
