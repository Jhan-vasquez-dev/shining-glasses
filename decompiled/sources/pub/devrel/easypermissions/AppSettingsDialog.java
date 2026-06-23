package pub.devrel.easypermissions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

/* JADX INFO: loaded from: classes2.dex */
public class AppSettingsDialog implements Parcelable {
    public static final Parcelable.Creator<AppSettingsDialog> CREATOR = new Parcelable.Creator<AppSettingsDialog>() { // from class: pub.devrel.easypermissions.AppSettingsDialog.1
        @Override // android.os.Parcelable.Creator
        public AppSettingsDialog createFromParcel(Parcel parcel) {
            return new AppSettingsDialog(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public AppSettingsDialog[] newArray(int i) {
            return new AppSettingsDialog[i];
        }
    };
    public static final int DEFAULT_SETTINGS_REQ_CODE = 16061;
    static final String EXTRA_APP_SETTINGS = "extra_app_settings";
    private static final String TAG = "EasyPermissions";
    private Object mActivityOrFragment;
    private Context mContext;
    private final int mIntentFlags;
    private final String mNegativeButtonText;
    private final String mPositiveButtonText;
    private final String mRationale;
    private final int mRequestCode;
    private final int mThemeResId;
    private final String mTitle;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private AppSettingsDialog(Parcel parcel) {
        this.mThemeResId = parcel.readInt();
        this.mRationale = parcel.readString();
        this.mTitle = parcel.readString();
        this.mPositiveButtonText = parcel.readString();
        this.mNegativeButtonText = parcel.readString();
        this.mRequestCode = parcel.readInt();
        this.mIntentFlags = parcel.readInt();
    }

    private AppSettingsDialog(Object obj, int i, String str, String str2, String str3, String str4, int i2, int i3) {
        setActivityOrFragment(obj);
        this.mThemeResId = i;
        this.mRationale = str;
        this.mTitle = str2;
        this.mPositiveButtonText = str3;
        this.mNegativeButtonText = str4;
        this.mRequestCode = i2;
        this.mIntentFlags = i3;
    }

    static AppSettingsDialog fromIntent(Intent intent, Activity activity) {
        AppSettingsDialog appSettingsDialogBuild = (AppSettingsDialog) intent.getParcelableExtra(EXTRA_APP_SETTINGS);
        if (appSettingsDialogBuild == null) {
            Log.e(TAG, "Intent contains null value for EXTRA_APP_SETTINGS: intent=" + intent + ", extras=" + intent.getExtras());
            appSettingsDialogBuild = new Builder(activity).build();
        }
        appSettingsDialogBuild.setActivityOrFragment(activity);
        return appSettingsDialogBuild;
    }

    private void setActivityOrFragment(Object obj) {
        this.mActivityOrFragment = obj;
        if (obj instanceof Activity) {
            this.mContext = (Activity) obj;
        } else {
            if (obj instanceof Fragment) {
                this.mContext = ((Fragment) obj).getContext();
                return;
            }
            throw new IllegalStateException("Unknown object: " + obj);
        }
    }

    private void startForResult(Intent intent) {
        Object obj = this.mActivityOrFragment;
        if (obj instanceof Activity) {
            ((Activity) obj).startActivityForResult(intent, this.mRequestCode);
        } else if (obj instanceof Fragment) {
            ((Fragment) obj).startActivityForResult(intent, this.mRequestCode);
        }
    }

    public void show() {
        startForResult(AppSettingsDialogHolderActivity.createShowDialogIntent(this.mContext, this));
    }

    AlertDialog showDialog(DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        AlertDialog.Builder builder;
        if (this.mThemeResId != -1) {
            builder = new AlertDialog.Builder(this.mContext, this.mThemeResId);
        } else {
            builder = new AlertDialog.Builder(this.mContext);
        }
        return builder.setCancelable(false).setTitle(this.mTitle).setMessage(this.mRationale).setPositiveButton(this.mPositiveButtonText, onClickListener).setNegativeButton(this.mNegativeButtonText, onClickListener2).show();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mThemeResId);
        parcel.writeString(this.mRationale);
        parcel.writeString(this.mTitle);
        parcel.writeString(this.mPositiveButtonText);
        parcel.writeString(this.mNegativeButtonText);
        parcel.writeInt(this.mRequestCode);
        parcel.writeInt(this.mIntentFlags);
    }

    int getIntentFlags() {
        return this.mIntentFlags;
    }

    public static class Builder {
        private final Object mActivityOrFragment;
        private final Context mContext;
        private String mNegativeButtonText;
        private String mPositiveButtonText;
        private String mRationale;
        private String mTitle;
        private int mThemeResId = -1;
        private int mRequestCode = -1;
        private boolean mOpenInNewTask = false;

        public Builder(Activity activity) {
            this.mActivityOrFragment = activity;
            this.mContext = activity;
        }

        public Builder(Fragment fragment) {
            this.mActivityOrFragment = fragment;
            this.mContext = fragment.getContext();
        }

        public Builder setThemeResId(int i) {
            this.mThemeResId = i;
            return this;
        }

        public Builder setTitle(String str) {
            this.mTitle = str;
            return this;
        }

        public Builder setTitle(int i) {
            this.mTitle = this.mContext.getString(i);
            return this;
        }

        public Builder setRationale(String str) {
            this.mRationale = str;
            return this;
        }

        public Builder setRationale(int i) {
            this.mRationale = this.mContext.getString(i);
            return this;
        }

        public Builder setPositiveButton(String str) {
            this.mPositiveButtonText = str;
            return this;
        }

        public Builder setPositiveButton(int i) {
            this.mPositiveButtonText = this.mContext.getString(i);
            return this;
        }

        public Builder setNegativeButton(String str) {
            this.mNegativeButtonText = str;
            return this;
        }

        public Builder setNegativeButton(int i) {
            this.mNegativeButtonText = this.mContext.getString(i);
            return this;
        }

        public Builder setRequestCode(int i) {
            this.mRequestCode = i;
            return this;
        }

        public Builder setOpenInNewTask(boolean z) {
            this.mOpenInNewTask = z;
            return this;
        }

        public AppSettingsDialog build() {
            this.mRationale = TextUtils.isEmpty(this.mRationale) ? this.mContext.getString(R.string.rationale_ask_again) : this.mRationale;
            this.mTitle = TextUtils.isEmpty(this.mTitle) ? this.mContext.getString(R.string.title_settings_dialog) : this.mTitle;
            this.mPositiveButtonText = TextUtils.isEmpty(this.mPositiveButtonText) ? this.mContext.getString(android.R.string.ok) : this.mPositiveButtonText;
            this.mNegativeButtonText = TextUtils.isEmpty(this.mNegativeButtonText) ? this.mContext.getString(android.R.string.cancel) : this.mNegativeButtonText;
            int i = this.mRequestCode;
            if (i <= 0) {
                i = AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE;
            }
            this.mRequestCode = i;
            return new AppSettingsDialog(this.mActivityOrFragment, this.mThemeResId, this.mRationale, this.mTitle, this.mPositiveButtonText, this.mNegativeButtonText, this.mRequestCode, this.mOpenInNewTask ? 268435456 : 0);
        }
    }
}
