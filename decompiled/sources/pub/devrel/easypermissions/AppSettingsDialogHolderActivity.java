package pub.devrel.easypermissions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes2.dex */
public class AppSettingsDialogHolderActivity extends AppCompatActivity implements DialogInterface.OnClickListener {
    private static final int APP_SETTINGS_RC = 7534;
    private AlertDialog mDialog;
    private int mIntentFlags;

    public static Intent createShowDialogIntent(Context context, AppSettingsDialog appSettingsDialog) {
        Intent intent = new Intent(context, (Class<?>) AppSettingsDialogHolderActivity.class);
        intent.putExtra("extra_app_settings", appSettingsDialog);
        return intent;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        AppSettingsDialog appSettingsDialogFromIntent = AppSettingsDialog.fromIntent(getIntent(), this);
        this.mIntentFlags = appSettingsDialogFromIntent.getIntentFlags();
        this.mDialog = appSettingsDialogFromIntent.showDialog(this, this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        this.mDialog.dismiss();
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            Intent data = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", getPackageName(), null));
            data.addFlags(this.mIntentFlags);
            startActivityForResult(data, APP_SETTINGS_RC);
        } else {
            if (i == -2) {
                setResult(0);
                finish();
                return;
            }
            throw new IllegalStateException("Unknown button type: " + i);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        setResult(i2, intent);
        finish();
    }
}
