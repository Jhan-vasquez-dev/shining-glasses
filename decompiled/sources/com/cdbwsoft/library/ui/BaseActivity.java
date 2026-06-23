package com.cdbwsoft.library.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cdbwsoft.library.R;
import com.cdbwsoft.library.RequestPermissionListener;

/* JADX INFO: loaded from: classes.dex */
public class BaseActivity extends AppCompatActivity implements RequestPermissionListener {
    private AlertDialog mAlertDialog;
    protected Handler mHandler;
    private ProgressDialog mProgressDialog;
    private Toast mToast;
    private int mPermissionIdx = 16;
    private SparseArray<Runnable> mPermissions = new SparseArray<>();
    protected boolean mImmersive = true;
    protected boolean mFullScreen = true;
    protected boolean mHideNavigation = true;
    private Runnable mHideBarRunnable = new Runnable() { // from class: com.cdbwsoft.library.ui.BaseActivity.1
        @Override // java.lang.Runnable
        public void run() {
        }
    };

    protected int getContentView() {
        return 0;
    }

    @Override // com.cdbwsoft.library.RequestPermissionListener
    public Activity getContext() {
        return this;
    }

    protected int getWindowVisibility() {
        int systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
        int i = systemUiVisibility | 256;
        if (this.mImmersive) {
            i = systemUiVisibility | 4352;
        }
        return this.mHideNavigation ? i | 514 : i;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        Runnable runnable = this.mPermissions.get(i);
        if (runnable != null) {
            this.mPermissions.remove(i);
        }
        if (iArr.length <= 0 || iArr[0] != 0) {
            return;
        }
        runOnUiThread(runnable);
    }

    @Override // com.cdbwsoft.library.RequestPermissionListener
    public void requestPermission(final String[] strArr, String str, Runnable runnable) {
        if (strArr == null || strArr.length == 0) {
            runOnUiThread(runnable);
            return;
        }
        final int i = this.mPermissionIdx;
        this.mPermissionIdx = i + 1;
        this.mPermissions.put(i, runnable);
        boolean z = true;
        for (String str2 : strArr) {
            z = z && checkSelfPermission(str2) == 0;
        }
        if (z) {
            runOnUiThread(runnable);
            return;
        }
        boolean z2 = true;
        for (String str3 : strArr) {
            z2 = z2 && !shouldShowRequestPermissionRationale(str3);
        }
        if (!z2) {
            View viewInflate = LayoutInflater.from(this).inflate(R.layout.dialog_request, (ViewGroup) null);
            final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setView(viewInflate).create();
            ((TextView) viewInflate.findViewById(R.id.tv_content)).setText(str);
            TextView textView = (TextView) viewInflate.findViewById(R.id.tv_cancel);
            TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_sure);
            textView.setOnClickListener(new View.OnClickListener() { // from class: com.cdbwsoft.library.ui.BaseActivity.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    alertDialogCreate.dismiss();
                }
            });
            textView2.setOnClickListener(new View.OnClickListener() { // from class: com.cdbwsoft.library.ui.BaseActivity.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    alertDialogCreate.dismiss();
                    BaseActivity.this.requestPermissions(strArr, i);
                }
            });
            alertDialogCreate.show();
            return;
        }
        requestPermissions(strArr, i);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int contentView = getContentView();
        if (contentView > 0) {
            setContentView(contentView);
        }
        this.mHandler = new Handler();
        if (this.mImmersive) {
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() { // from class: com.cdbwsoft.library.ui.BaseActivity.4
                @Override // android.view.View.OnSystemUiVisibilityChangeListener
                public void onSystemUiVisibilityChange(int i) {
                    BaseActivity.this.getWindow().getDecorView().setSystemUiVisibility(BaseActivity.this.getWindowVisibility());
                }
            });
        }
        getWindow().getDecorView().setSystemUiVisibility(getWindowVisibility());
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
    }

    protected void hideProgress() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected ProgressDialog showProgress(String str, String str2) {
        if (this.mProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            this.mProgressDialog = progressDialog;
            progressDialog.setCanceledOnTouchOutside(false);
            this.mProgressDialog.setIndeterminate(true);
        }
        this.mProgressDialog.setTitle(str);
        this.mProgressDialog.setMessage(str2);
        this.mProgressDialog.show();
        return this.mProgressDialog;
    }

    protected void showToast(String str) {
        showToast(str, -1);
    }

    protected void showToast(String str, int i) {
        if (this.mToast == null) {
            this.mToast = Toast.makeText(this, str, 1);
        }
        if (i > -1) {
            this.mToast.setDuration(i);
        }
        this.mToast.setText(str);
        this.mToast.show();
    }

    protected AlertDialog showDialog(String str, String str2) {
        return showDialog(str, str2, getResources().getString(R.string.btn_sure), null, null, new DialogInterface.OnClickListener() { // from class: com.cdbwsoft.library.ui.BaseActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    protected AlertDialog showDialog(String str, String str2, String str3) {
        return showDialog(str, str2, str3, null, null, new DialogInterface.OnClickListener() { // from class: com.cdbwsoft.library.ui.BaseActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    protected AlertDialog showDialog(String str, String str2, String str3, String str4, DialogInterface.OnClickListener onClickListener) {
        return showDialog(str, str2, str3, str4, null, onClickListener);
    }

    protected AlertDialog showDialog(String str, String str2, String str3, String str4, String str5, DialogInterface.OnClickListener onClickListener) {
        if (this.mAlertDialog == null) {
            this.mAlertDialog = new AlertDialog.Builder(this).create();
        }
        this.mAlertDialog.setCanceledOnTouchOutside(false);
        this.mAlertDialog.setTitle(str);
        this.mAlertDialog.setMessage(str2);
        this.mAlertDialog.setButton(-1, str3, onClickListener);
        this.mAlertDialog.setButton(-2, str4, new DialogInterface.OnClickListener() { // from class: com.cdbwsoft.library.ui.BaseActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        this.mAlertDialog.setButton(-3, str5, onClickListener);
        this.mAlertDialog.show();
        return this.mAlertDialog;
    }
}
