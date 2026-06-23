package pub.devrel.easypermissions.helper;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/* JADX INFO: loaded from: classes2.dex */
class SupportFragmentPermissionHelper extends BaseSupportPermissionsHelper<Fragment> {
    public SupportFragmentPermissionHelper(Fragment fragment) {
        super(fragment);
    }

    @Override // pub.devrel.easypermissions.helper.BaseSupportPermissionsHelper
    public FragmentManager getSupportFragmentManager() {
        return getHost().getChildFragmentManager();
    }

    @Override // pub.devrel.easypermissions.helper.PermissionHelper
    public void directRequestPermissions(int i, String... strArr) {
        getHost().requestPermissions(strArr, i);
    }

    @Override // pub.devrel.easypermissions.helper.PermissionHelper
    public boolean shouldShowRequestPermissionRationale(String str) {
        return getHost().shouldShowRequestPermissionRationale(str);
    }

    @Override // pub.devrel.easypermissions.helper.PermissionHelper
    public Context getContext() {
        return getHost().getActivity();
    }
}
