package com.cdbwsoft.library.setting;

import android.content.Context;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.utils.SharedPreferenceUtils;

/* JADX INFO: loaded from: classes.dex */
public class SettingManager extends SharedPreferenceUtils {
    public SettingManager(Context context) {
        super(context, AppConfig.SETTING_FILE);
    }
}
