package com.cdbwsoft.library.ble;

import android.content.Context;
import com.cdbwsoft.library.ble.BleDevice;

/* JADX INFO: loaded from: classes.dex */
public class BleDeviceFactory<T extends BleDevice> {
    private Context mContext;

    public BleDeviceFactory(Context context) {
        this.mContext = context;
    }

    public T create(BleManager<T> bleManager, String str, String str2) throws Exception {
        return (T) new BleDevice(bleManager, str, str2);
    }

    public Context getContext() {
        return this.mContext;
    }
}
