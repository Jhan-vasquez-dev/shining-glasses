package pub.devrel.easypermissions.helper;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

/* JADX INFO: loaded from: classes2.dex */
class AppCompatActivityPermissionsHelper extends BaseSupportPermissionsHelper<AppCompatActivity> {
    public AppCompatActivityPermissionsHelper(AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
    }

    @Override // pub.devrel.easypermissions.helper.BaseSupportPermissionsHelper
    public FragmentManager getSupportFragmentManager() {
        return getHost().getSupportFragmentManager();
    }

    @Override // pub.devrel.easypermissions.helper.PermissionHelper
    public void directRequestPermissions(int i, String... strArr) {
        ActivityCompat.requestPermissions(getHost(), strArr, i);
    }

    @Override // pub.devrel.easypermissions.helper.PermissionHelper
    public boolean shouldShowRequestPermissionRationale(String str) {
        return ActivityCompat.shouldShowRequestPermissionRationale(getHost(), str);
    }

    @Override // pub.devrel.easypermissions.helper.PermissionHelper
    public Context getContext() {
        return getHost();
    }
}
