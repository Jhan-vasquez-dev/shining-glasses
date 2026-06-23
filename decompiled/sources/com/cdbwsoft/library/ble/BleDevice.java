package com.cdbwsoft.library.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.utils.ByteUtils;
import com.cdbwsoft.library.utils.LogUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BleDevice {
    public static final int CONNECT_TIME_OUT = 6000;
    public static final int DISCOVER_TIME_OUT = 4000;
    public static final int MAX_RETRY_TIMES = 1;
    public static final int MSG_CONNECT = 2049;
    public static final int MSG_DISCONNECT = 2050;
    public static final int MSG_DISCOVER_SERVICES = 2051;
    public static final int MSG_GATT_CLOSE = 2052;
    public static final int MSG_SET_NOTIFICATION = 2053;
    public static final int STATE_CONNECTED = 2307;
    public static final int STATE_CONNECTING = 2306;
    public static final int STATE_DISCOVERED = 2310;
    public static final int STATE_DISCOVERING = 2309;
    public static final int STATE_IDLE = 2305;
    public static final int STATE_WRITING_DESCRIPTOR = 2311;
    public static final int STATE_WROTE_DESCRIPTOR = 2312;
    public static final String TAG = "BleDevice";
    private static int mDeviceIndex;
    private static final Object mLocker = new Object();
    private BluetoothGattCharacteristic ctrlChar;
    private BluetoothGattDescriptor ctrlDescriptor;
    private BluetoothGattCharacteristic dataChar;
    private BluetoothGattDescriptor desc;
    private boolean mAutoConnect;
    private String mBleAddress;
    private BleGattCallback mBleGattCallback;
    private BleManager<BleDevice> mBleManager;
    private String mBleName;
    private BluetoothAdapter mBluetoothAdapter;
    public BluetoothGatt mBluetoothGatt;
    private Runnable mConnectTimeout;
    private boolean mConnecting;
    private int mConnectionState;
    private Context mContext;
    private boolean mDeleted;
    private BluetoothDevice mDevice;
    private DeviceListener<BleDevice> mDeviceListener;
    private boolean mDisconnecting;
    private Runnable mDiscoverTimeout;
    private BleDeviceHandler mHandler;
    private int mIndex;
    private List<BluetoothGattCharacteristic> mNotifyCharacteristics;
    private int mNotifyIndex;
    private OtaListener mOtaListener;
    private boolean mOtaUpdating;
    private BluetoothGattCharacteristic mOtaWriteCharacteristic;
    private boolean mReady;
    private int mReconnectTimes;
    private int mRediscoverTimes;
    private int mShoeType;
    private int mState;
    private BluetoothGattCharacteristic mWriteCharacteristic1;
    private BluetoothGattCharacteristic mWriteCharacteristicWrite2;
    private BluetoothGattCharacteristic mWriteCharacteristicWrite3;
    private BluetoothGattCharacteristic mWriteCharacteristicWrite4;
    private boolean setMtuStatus;

    public interface DeviceListener<T extends BleDevice> {
        void onChanged(T t, byte[] bArr);

        void onConnectionChanged(T t);

        void onRead(T t, byte[] bArr);

        void onReady(T t, BluetoothGattDescriptor bluetoothGattDescriptor);

        void onServiceReady(T t);

        void onServicesDiscovered(T t);

        void onWrite(T t, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i);
    }

    public interface OtaListener {
        void onChanged(BleDevice bleDevice, byte[] bArr);

        void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2);

        void onReady(BleDevice bleDevice, BluetoothGattDescriptor bluetoothGattDescriptor);

        void onServicesDiscovered(BleDevice bleDevice);

        void onWrite(BleDevice bleDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i);
    }

    protected void onChange(byte[] bArr) {
    }

    protected void onConnected() {
    }

    protected void onConnectionChanged() {
    }

    protected byte[] onDecrypt(byte[] bArr) {
        return bArr;
    }

    protected void onDisconnected() {
    }

    protected void onDisconnecting() {
    }

    protected byte[] onEncrypt(byte[] bArr) {
        return bArr;
    }

    protected void onFailed() {
    }

    protected void onReady() {
    }

    protected void onServiceReady() {
    }

    protected void onWrite() {
    }

    static /* synthetic */ int access$1808(BleDevice bleDevice) {
        int i = bleDevice.mNotifyIndex;
        bleDevice.mNotifyIndex = i + 1;
        return i;
    }

    static /* synthetic */ int access$808(BleDevice bleDevice) {
        int i = bleDevice.mReconnectTimes;
        bleDevice.mReconnectTimes = i + 1;
        return i;
    }

    public BluetoothGattCharacteristic getCtrlChar() {
        return this.ctrlChar;
    }

    public BluetoothGattCharacteristic getDataChar() {
        return this.dataChar;
    }

    public BluetoothGattDescriptor getCtrlDescriptor() {
        return this.ctrlDescriptor;
    }

    public BleDevice(BleManager<BleDevice> bleManager, String str, String str2) {
        BluetoothAdapter bluetoothAdapter;
        this.mShoeType = 1;
        this.setMtuStatus = false;
        this.mNotifyCharacteristics = new ArrayList();
        this.mNotifyIndex = 0;
        this.mIndex = 0;
        this.mConnectionState = 0;
        this.mOtaUpdating = false;
        this.mAutoConnect = true;
        this.mReady = false;
        this.mDeleted = false;
        this.mReconnectTimes = 0;
        this.mRediscoverTimes = 0;
        this.mConnecting = false;
        this.mDisconnecting = false;
        this.mState = STATE_IDLE;
        this.mConnectTimeout = new Runnable() { // from class: com.cdbwsoft.library.ble.BleDevice.1
            @Override // java.lang.Runnable
            public void run() {
                BleDevice.this.close();
            }
        };
        this.mDiscoverTimeout = new Runnable() { // from class: com.cdbwsoft.library.ble.BleDevice.2
            @Override // java.lang.Runnable
            public void run() {
                Log.e(BleDevice.TAG, "run: 获取服务超时");
                BleDevice.this.rediscoverServices();
            }
        };
        this.mContext = bleManager.getContext();
        this.mBleAddress = str;
        this.mBleName = str2;
        this.mBleGattCallback = new BleGattCallback();
        this.mBleManager = bleManager;
        this.mBluetoothAdapter = bleManager.getBluetoothAdapter();
        int i = mDeviceIndex;
        mDeviceIndex = i + 1;
        this.mIndex = i;
        if (!TextUtils.isEmpty(this.mBleAddress) && (bluetoothAdapter = this.mBluetoothAdapter) != null) {
            this.mDevice = bluetoothAdapter.getRemoteDevice(this.mBleAddress);
        }
        this.mHandler = new BleDeviceHandler(this, this.mContext.getMainLooper());
    }

    public BleDevice(String str, String str2) {
        this.mShoeType = 1;
        this.setMtuStatus = false;
        this.mNotifyCharacteristics = new ArrayList();
        this.mNotifyIndex = 0;
        this.mIndex = 0;
        this.mConnectionState = 0;
        this.mOtaUpdating = false;
        this.mAutoConnect = true;
        this.mReady = false;
        this.mDeleted = false;
        this.mReconnectTimes = 0;
        this.mRediscoverTimes = 0;
        this.mConnecting = false;
        this.mDisconnecting = false;
        this.mState = STATE_IDLE;
        this.mConnectTimeout = new Runnable() { // from class: com.cdbwsoft.library.ble.BleDevice.1
            @Override // java.lang.Runnable
            public void run() {
                BleDevice.this.close();
            }
        };
        this.mDiscoverTimeout = new Runnable() { // from class: com.cdbwsoft.library.ble.BleDevice.2
            @Override // java.lang.Runnable
            public void run() {
                Log.e(BleDevice.TAG, "run: 获取服务超时");
                BleDevice.this.rediscoverServices();
            }
        };
        this.mBleAddress = str;
        this.mBleName = str2;
    }

    public boolean isSetMtuStatus() {
        return this.setMtuStatus;
    }

    public void setSetMtuStatus(boolean z) {
        this.setMtuStatus = z;
    }

    public boolean isConnected() {
        return this.mConnectionState == 2;
    }

    public boolean isDisConnect() {
        return this.mConnectionState == 0;
    }

    public boolean isConnecting() {
        return this.mConnecting;
    }

    public int getConnectionState() {
        return this.mConnectionState;
    }

    public void setConnectionState(int i) {
        this.mConnectionState = i;
    }

    public void setShoeType(int i) {
        this.mShoeType = i;
    }

    public int getShoeType() {
        return this.mShoeType;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public void setIndex(int i) {
        this.mIndex = i;
    }

    public void setAutoConnect(boolean z) {
        this.mAutoConnect = z;
    }

    public boolean getAutoConnect() {
        return this.mAutoConnect;
    }

    public boolean getReady() {
        return this.mReady;
    }

    public void setDeleted(boolean z) {
        if (this.mDeleted || !z) {
            return;
        }
        this.mDeleted = true;
        if (this.mConnecting || this.mDisconnecting) {
            return;
        }
        if (isConnected()) {
            disconnect();
        } else {
            this.mBleManager.removeDevice(this);
        }
    }

    public boolean connect() {
        BluetoothAdapter bluetoothAdapter;
        if (this.mConnecting || this.mDeleted || (bluetoothAdapter = this.mBluetoothAdapter) == null || !bluetoothAdapter.isEnabled()) {
            Log.e(TAG, "connect: " + this.mConnecting);
            return false;
        }
        int i = this.mConnectionState;
        if (i == 2 || i == 1) {
            Log.e(TAG, "connect: " + this.mConnectionState);
            return true;
        }
        if (this.mHandler == null) {
            return false;
        }
        if (!this.mBleManager.addConnectingDevice(this)) {
            Log.e(TAG, "connect: addConnectingDevice");
            return false;
        }
        this.mConnecting = true;
        this.mAutoConnect = true;
        this.mState = STATE_CONNECTING;
        this.mHandler.obtainMessage(MSG_CONNECT).sendToTarget();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0090 A[Catch: all -> 0x00f2, TryCatch #0 {, blocks: (B:6:0x0006, B:8:0x000a, B:10:0x000e, B:12:0x0016, B:15:0x0020, B:17:0x0024, B:20:0x002a, B:22:0x0032, B:23:0x0034, B:25:0x0036, B:27:0x003f, B:29:0x0043, B:30:0x004a, B:31:0x004c, B:33:0x004e, B:35:0x0055, B:36:0x006f, B:38:0x0073, B:44:0x0082, B:46:0x0090, B:48:0x0094, B:49:0x009b, B:50:0x009d, B:52:0x009f, B:61:0x00b8, B:55:0x00a6, B:57:0x00aa, B:58:0x00b1, B:60:0x00b5, B:41:0x007b, B:43:0x007f, B:63:0x00ba, B:65:0x00be, B:66:0x00c5, B:67:0x00c7, B:69:0x00c9, B:71:0x00cd, B:73:0x00d6, B:75:0x00db, B:77:0x00ee, B:78:0x00f0), top: B:83:0x0006, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x009f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean connectInner(com.cdbwsoft.library.ble.BleDevice r6) {
        /*
            Method dump skipped, instruction units count: 245
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdbwsoft.library.ble.BleDevice.connectInner(com.cdbwsoft.library.ble.BleDevice):boolean");
    }

    public boolean reconnect() {
        BluetoothAdapter bluetoothAdapter;
        if (this.mConnecting || this.mDeleted || (bluetoothAdapter = this.mBluetoothAdapter) == null || !bluetoothAdapter.isEnabled() || this.mReconnectTimes >= 1) {
            return false;
        }
        int i = this.mConnectionState;
        if (i == 2 || i == 1) {
            return true;
        }
        if (this.mHandler == null || !this.mBleManager.addConnectingDevice(this)) {
            return false;
        }
        this.mConnecting = true;
        this.mState = STATE_CONNECTING;
        this.mReconnectTimes++;
        this.mHandler.obtainMessage(MSG_CONNECT).sendToTarget();
        return true;
    }

    public void disconnect() {
        BleDeviceHandler bleDeviceHandler;
        Log.e(TAG, "disconnect:断开连接 ");
        this.mAutoConnect = false;
        if (this.mDisconnecting || (bleDeviceHandler = this.mHandler) == null) {
            return;
        }
        this.mDisconnecting = true;
        bleDeviceHandler.obtainMessage(MSG_DISCONNECT).sendToTarget();
        DeviceListener<BleDevice> deviceListener = this.mDeviceListener;
        if (deviceListener != null) {
            deviceListener.onConnectionChanged(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void disconnectInner(BleDevice bleDevice) {
        BluetoothGatt bluetoothGatt;
        synchronized (mLocker) {
            if (bleDevice != null) {
                if (bleDevice.mConnectionState == 2) {
                    if (bleDevice.mBluetoothAdapter != null && (bluetoothGatt = bleDevice.mBluetoothGatt) != null) {
                        bleDevice.mConnectionState = 3;
                        bluetoothGatt.disconnect();
                        return;
                    }
                    if (AppConfig.DEBUG) {
                        Log.w(TAG, bleDevice.mBleAddress + " -- BluetoothAdapter not initialized");
                    }
                    return;
                }
            }
            if (AppConfig.DEBUG) {
                Log.w(TAG, "disconnectInner: device is null or not connected");
            }
        }
    }

    public boolean isActive() {
        return this.mConnecting || this.mDisconnecting;
    }

    public int getState() {
        return this.mState;
    }

    public boolean isReady() {
        return this.mReady;
    }

    public BluetoothGatt getBluetoothGatt() {
        return this.mBluetoothGatt;
    }

    public void discoverServices() {
        BleDeviceHandler bleDeviceHandler;
        if (this.mDeleted || this.mState >= 2309 || !isConnected() || (bleDeviceHandler = this.mHandler) == null) {
            return;
        }
        this.mState = STATE_DISCOVERING;
        bleDeviceHandler.obtainMessage(MSG_DISCOVER_SERVICES).sendToTarget();
    }

    public void rediscoverServices() {
        if (this.mDeleted || !isConnected() || this.mRediscoverTimes >= 1) {
            return;
        }
        if (isConnected() && this.mRediscoverTimes >= 1) {
            Log.e(TAG, "rediscoverServices:重试扫描服务失败   断开连接 ");
            close();
            this.mRediscoverTimes = 0;
        }
        BleDeviceHandler bleDeviceHandler = this.mHandler;
        if (bleDeviceHandler != null) {
            this.mState = STATE_DISCOVERING;
            this.mRediscoverTimes++;
            bleDeviceHandler.obtainMessage(MSG_DISCOVER_SERVICES).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void discoverServicesInner(BleDevice bleDevice) {
        synchronized (mLocker) {
            if (bleDevice != null) {
                if (bleDevice.isConnected() && !bleDevice.mDeleted) {
                    if (AppConfig.DEBUG) {
                        Log.w(TAG, "discoverServicesInner:" + bleDevice.getBleAddress());
                    }
                    if (bleDevice.mBluetoothGatt != null) {
                        Log.e(TAG, "discoverServicesInner: ++++");
                        bleDevice.mHandler.removeCallbacks(bleDevice.mDiscoverTimeout);
                        bleDevice.mHandler.postDelayed(bleDevice.mDiscoverTimeout, 4000L);
                        bleDevice.mBluetoothGatt.discoverServices();
                    }
                    return;
                }
            }
            if (AppConfig.DEBUG) {
                Log.w(TAG, "discoverServicesInner: device is null or deleted");
            }
        }
    }

    public void close() {
        BleDeviceHandler bleDeviceHandler = this.mHandler;
        if (bleDeviceHandler != null) {
            this.mConnectionState = 3;
            bleDeviceHandler.obtainMessage(MSG_GATT_CLOSE).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void closeInner(BleDevice bleDevice) {
        synchronized (mLocker) {
            if (bleDevice == null) {
                if (AppConfig.DEBUG) {
                    Log.w(TAG, "closeInner:device is null");
                }
                return;
            }
            if (bleDevice.mDeleted) {
                bleDevice.mBleManager.removeDevice(bleDevice);
            }
            BluetoothGatt bluetoothGatt = bleDevice.mBluetoothGatt;
            if (bluetoothGatt != null) {
                bluetoothGatt.close();
                bleDevice.mBluetoothGatt = null;
            }
            if (AppConfig.DEBUG) {
                Log.w(TAG, "closeInner:" + bleDevice.getBleAddress());
            }
            bleDevice.mHandler.removeCallbacks(bleDevice.mConnectTimeout);
            bleDevice.mHandler.removeCallbacks(bleDevice.mDiscoverTimeout);
            bleDevice.mWriteCharacteristic1 = null;
            bleDevice.mOtaWriteCharacteristic = null;
            bleDevice.mNotifyIndex = 0;
            bleDevice.mNotifyCharacteristics.clear();
            bleDevice.mDisconnecting = false;
            bleDevice.mConnecting = false;
            bleDevice.mConnectionState = 0;
            bleDevice.mState = STATE_IDLE;
            DeviceListener<BleDevice> deviceListener = bleDevice.mDeviceListener;
            if (deviceListener != null) {
                deviceListener.onConnectionChanged(bleDevice);
            }
        }
    }

    public void setEnableCharacteristicNotification() {
        List<BluetoothGattCharacteristic> list;
        if (this.mState < 2311 && this.mHandler != null && (list = this.mNotifyCharacteristics) != null && list.size() > 0) {
            List<BluetoothGattCharacteristic> list2 = this.mNotifyCharacteristics;
            int i = this.mNotifyIndex;
            this.mNotifyIndex = i + 1;
            setCharacteristicNotification(list2.get(i), true);
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BleDeviceHandler bleDeviceHandler = this.mHandler;
        if (bleDeviceHandler != null) {
            this.mState = STATE_WRITING_DESCRIPTOR;
            bleDeviceHandler.obtainMessage(MSG_SET_NOTIFICATION, z ? 1 : 0, 0, bluetoothGattCharacteristic).sendToTarget();
        }
    }

    public static void setCharacteristicNotificationInner(BleDevice bleDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BluetoothGatt bluetoothGatt;
        synchronized (mLocker) {
            if (bleDevice.mBluetoothAdapter == null || (bluetoothGatt = bleDevice.mBluetoothGatt) == null) {
                if (AppConfig.DEBUG) {
                    Log.w(TAG, bleDevice.mBleAddress + " -- BluetoothAdapter not initialized");
                }
                return;
            }
            try {
                bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, z);
                BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(bleDevice.mBleManager.getUuidDescriptor());
                if (descriptor != null) {
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    writeDescriptor(bleDevice.mBluetoothGatt, descriptor);
                }
            } catch (Exception e) {
                if (AppConfig.DEBUG) {
                    e.printStackTrace();
                }
                bleDevice.close();
            }
        }
    }

    public boolean writeCharacteristic(byte[] bArr) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            if (AppConfig.DEBUG) {
                Log.w(TAG, this.mBleAddress + " -- BluetoothAdapter not initialized");
            }
            return false;
        }
        try {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mWriteCharacteristic1;
            if (bluetoothGattCharacteristic == null) {
                return false;
            }
            bluetoothGattCharacteristic.setValue(onEncrypt(bArr));
            boolean zWriteCharacteristic = writeCharacteristic(this.mBluetoothGatt, this.mWriteCharacteristic1);
            if (AppConfig.DEBUG) {
                String str = TAG;
                Log.d(str, this.mBleAddress + " -- write data:" + ByteUtils.binaryToHexString(bArr));
                Log.d(str, this.mBleAddress + " -- write result:" + zWriteCharacteristic);
            }
            return zWriteCharacteristic;
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
            close();
            return false;
        }
    }

    public boolean writeCharacteristicBy2(byte[] bArr) {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            try {
                this.mWriteCharacteristicWrite2.setValue(onEncrypt(bArr));
                Log.e(TAG, "writeCharacteristicBy2: " + ByteUtils.binaryToHexString(bArr));
                return writeCharacteristic(this.mBluetoothGatt, this.mWriteCharacteristicWrite2);
            } catch (Exception e) {
                if (AppConfig.DEBUG) {
                    e.printStackTrace();
                }
                close();
            }
        }
        return false;
    }

    public boolean writeCharacteristicBy3(byte[] bArr) {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            try {
                BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mWriteCharacteristicWrite3;
                if (bluetoothGattCharacteristic == null) {
                    return false;
                }
                bluetoothGattCharacteristic.setValue(onEncrypt(bArr));
                return writeCharacteristic(this.mBluetoothGatt, this.mWriteCharacteristicWrite3);
            } catch (Exception e) {
                if (AppConfig.DEBUG) {
                    e.printStackTrace();
                }
                close();
            }
        }
        return false;
    }

    public boolean writeCharacteristicBy4(byte[] bArr) {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            try {
                BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mWriteCharacteristicWrite4;
                if (bluetoothGattCharacteristic == null) {
                    return false;
                }
                bluetoothGattCharacteristic.setValue(onEncrypt(bArr));
                return writeCharacteristic(this.mBluetoothGatt, this.mWriteCharacteristicWrite4);
            } catch (Exception e) {
                if (AppConfig.DEBUG) {
                    e.printStackTrace();
                }
                close();
            }
        }
        return false;
    }

    public boolean readCharacteristicByRead() {
        BluetoothGatt bluetoothGatt;
        BluetoothGattDescriptor bluetoothGattDescriptor;
        Log.d(TAG, "读取程序版本号");
        if (this.mBluetoothAdapter == null || (bluetoothGatt = this.mBluetoothGatt) == null || bluetoothGatt == null || (bluetoothGattDescriptor = this.desc) == null) {
            return false;
        }
        return bluetoothGatt.readDescriptor(bluetoothGattDescriptor);
    }

    public boolean writeOtaData(byte[] bArr) {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = this.mBluetoothGatt) == null) {
            if (AppConfig.DEBUG) {
                Log.w(TAG, this.mBleAddress + " -- BluetoothAdapter not initialized");
            }
            return false;
        }
        try {
            if (this.mOtaWriteCharacteristic == null) {
                this.mOtaUpdating = true;
                BluetoothGattService service = bluetoothGatt.getService(BleGlobalVariables.UUID_QUINTIC_OTA_SERVICE);
                if (service == null) {
                    return false;
                }
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(BleGlobalVariables.UUID_OTA_NOTIFY_CHARACTERISTIC);
                if (characteristic != null) {
                    this.mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                }
                this.mOtaWriteCharacteristic = service.getCharacteristic(BleGlobalVariables.UUID_OTA_WRITE_CHARACTERISTIC);
            }
            if (this.mOtaWriteCharacteristic == null || !BleGlobalVariables.UUID_OTA_WRITE_CHARACTERISTIC.equals(this.mOtaWriteCharacteristic.getUuid())) {
                return true;
            }
            this.mOtaWriteCharacteristic.setValue(bArr);
            boolean zWriteCharacteristic = writeCharacteristic(this.mBluetoothGatt, this.mOtaWriteCharacteristic);
            if (AppConfig.DEBUG) {
                String str = TAG;
                Log.d(str, this.mBleAddress + " -- write data:" + Arrays.toString(bArr));
                Log.d(str, this.mBleAddress + " -- write result:" + zWriteCharacteristic);
            }
            return zWriteCharacteristic;
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
            close();
            return false;
        }
    }

    public void otaUpdateComplete() {
        this.mOtaUpdating = false;
    }

    public void setOtaUpdating(boolean z) {
        this.mOtaUpdating = z;
    }

    public void setOtaListener(OtaListener otaListener) {
        this.mOtaListener = otaListener;
    }

    public void removeOtaListener() {
        this.mOtaListener = null;
    }

    public void setDeviceListener(DeviceListener deviceListener) {
        this.mDeviceListener = deviceListener;
    }

    public boolean writeCharacteristic(String str) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            if (AppConfig.DEBUG) {
                Log.w(TAG, this.mBleAddress + " -- BluetoothAdapter not initialized");
            }
            return false;
        }
        try {
            if (this.mWriteCharacteristic1 == null || !this.mBleManager.getUuidCharacteristic().equals(this.mWriteCharacteristic1.getUuid())) {
                return true;
            }
            this.mWriteCharacteristic1.setValue(str != null ? onEncrypt(str.getBytes()) : null);
            boolean zWriteCharacteristic = writeCharacteristic(this.mBluetoothGatt, this.mWriteCharacteristic1);
            if (AppConfig.DEBUG) {
                Log.d(TAG, this.mBleAddress + " -- write data:" + zWriteCharacteristic);
            }
            return zWriteCharacteristic;
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
            close();
            return false;
        }
    }

    public static boolean writeCharacteristic(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        synchronized (mLocker) {
            if (bluetoothGatt == null || bluetoothGattCharacteristic == null) {
                return false;
            }
            return bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
        }
    }

    public static boolean writeDescriptor(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor) {
        synchronized (mLocker) {
            if (bluetoothGatt == null || bluetoothGattDescriptor == null) {
                return false;
            }
            return bluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (this.mBluetoothGatt == null) {
            String str = TAG;
            Log.e(str, "getSupportedGattServices:mBluetoothGatt == null ");
            Log.e(str, "getSupportedGattServices: " + this.mBluetoothGatt.getServices().size());
        }
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null) {
            return null;
        }
        return bluetoothGatt.getServices();
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public String getBleAddress() {
        return this.mBleAddress;
    }

    public String getBleName() {
        return this.mBleName;
    }

    public boolean equals(Object obj) {
        if (obj instanceof BleDevice) {
            if (this.mBleAddress.equalsIgnoreCase(((BleDevice) obj).mBleAddress)) {
                return true;
            }
        } else if ((obj instanceof String) && this.mBleAddress.equalsIgnoreCase((String) obj)) {
            return true;
        }
        return super.equals(obj);
    }

    class BleGattCallback extends BluetoothGattCallback {
        BleGattCallback() {
        }

        /* JADX WARN: Removed duplicated region for block: B:66:0x0189 A[Catch: all -> 0x01b2, TryCatch #0 {, blocks: (B:4:0x0011, B:6:0x0015, B:7:0x0041, B:9:0x0060, B:10:0x0064, B:12:0x006c, B:14:0x0074, B:16:0x007d, B:17:0x009b, B:19:0x00af, B:64:0x017c, B:66:0x0189, B:67:0x01b0, B:22:0x00b9, B:24:0x00c2, B:26:0x00c6, B:27:0x00e4, B:29:0x00f8, B:31:0x0100, B:33:0x010c, B:35:0x0114, B:37:0x011c, B:45:0x0130, B:47:0x0138, B:39:0x0122, B:42:0x0129, B:44:0x012d, B:49:0x0144, B:55:0x0152, B:57:0x015f, B:59:0x0167, B:61:0x016f, B:63:0x0177, B:52:0x014b, B:54:0x014f), top: B:72:0x0011, inners: #1, #2 }] */
        @Override // android.bluetooth.BluetoothGattCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onConnectionStateChange(android.bluetooth.BluetoothGatt r6, int r7, int r8) {
            /*
                Method dump skipped, instruction units count: 437
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.cdbwsoft.library.ble.BleDevice.BleGattCallback.onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int):void");
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            LogUtil.d("onServicesDiscovered");
            super.onServicesDiscovered(bluetoothGatt, i);
            BleDevice.this.mHandler.removeCallbacks(BleDevice.this.mDiscoverTimeout);
            BleDevice.this.mRediscoverTimes = 0;
            synchronized (BleDevice.mLocker) {
                if (AppConfig.DEBUG) {
                    Log.e(BleDevice.TAG, BleDevice.this.mBleAddress + " -- onServicesDiscovered|status:" + i);
                }
                BleDevice.this.mState = BleDevice.STATE_DISCOVERED;
                if (i != 0) {
                    BleDevice.this.mConnectionState = 0;
                    BleDevice.this.close();
                    if (AppConfig.DEBUG) {
                        Log.w(BleDevice.TAG, BleDevice.this.mBleAddress + " -- onServicesDiscovered received: " + i);
                    }
                } else {
                    if (BleDevice.this.mBluetoothGatt == null) {
                        BleDevice.this.mBluetoothGatt = bluetoothGatt;
                    }
                    BleDevice.this.mReady = false;
                    BleDevice.this.mNotifyCharacteristics.clear();
                    BleDevice.this.mNotifyIndex = 0;
                    for (BluetoothGattService bluetoothGattService : BleDevice.this.getSupportedGattServices()) {
                        if (AppConfig.DEBUG) {
                            Log.i(BleDevice.TAG, BleDevice.this.mBleAddress + " -- service UUID:" + bluetoothGattService.getUuid());
                        }
                        if (!BleDevice.this.mBleManager.getUuidService().equals(bluetoothGattService.getUuid())) {
                            if (BleDevice.this.mBleManager.getUUID_OTA_SERVICE().equals(bluetoothGattService.getUuid())) {
                                if (BleDevice.this.mOtaListener != null) {
                                    BleDevice.this.mOtaListener.onServicesDiscovered(BleDevice.this);
                                }
                                Log.e(BleDevice.TAG, "onServicesDiscovered:获取OTA相关特征 ");
                                for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
                                    if (AppConfig.DEBUG) {
                                        Log.i(BleDevice.TAG, BleDevice.this.mBleAddress + "OTA Characteristic UUID:" + bluetoothGattCharacteristic.getUuid());
                                    }
                                    if (BleDevice.this.mBleManager.getUUID_OTA_CHARACTERISTIC_CTRL().equals(bluetoothGattCharacteristic.getUuid())) {
                                        BleDevice.this.ctrlChar = bluetoothGattCharacteristic;
                                        BleDevice bleDevice = BleDevice.this;
                                        bleDevice.ctrlDescriptor = bluetoothGattCharacteristic.getDescriptor(bleDevice.mBleManager.getUUID_OTA_DESCRIPTOR());
                                        Log.d(BleDevice.TAG, "=========ctrlDescriptor:" + BleDevice.this.ctrlDescriptor + "  ctrlChar:" + BleDevice.this.ctrlChar);
                                    } else if (BleDevice.this.mBleManager.getUUID_OTA_CHARACTERISTIC_DATA().equals(bluetoothGattCharacteristic.getUuid())) {
                                        BleDevice.this.dataChar = bluetoothGattCharacteristic;
                                        Log.d(BleDevice.TAG, "=========dataChar:" + BleDevice.this.dataChar);
                                    }
                                }
                            }
                        } else {
                            for (BluetoothGattCharacteristic bluetoothGattCharacteristic2 : bluetoothGattService.getCharacteristics()) {
                                if (AppConfig.DEBUG) {
                                    Log.i(BleDevice.TAG, BleDevice.this.mBleAddress + " -- Characteristic UUID:" + bluetoothGattCharacteristic2.getUuid());
                                }
                                if (BleDevice.this.mBleManager.getUUID_CHARACTERISTIC_WRITE1().equals(bluetoothGattCharacteristic2.getUuid())) {
                                    BleDevice.this.mWriteCharacteristic1 = bluetoothGattCharacteristic2;
                                    BleDevice.this.desc = bluetoothGattCharacteristic2.getDescriptors().get(0);
                                    Log.d("BleDevice", "=========" + BleDevice.this.desc);
                                } else if (BleDevice.this.mBleManager.getUUID_CHARACTERISTIC_WRITE2().equals(bluetoothGattCharacteristic2.getUuid())) {
                                    BleDevice.this.mWriteCharacteristicWrite2 = bluetoothGattCharacteristic2;
                                } else if (BleDevice.this.mBleManager.getUUID_CHARACTERISTIC_WRITE3().equals(bluetoothGattCharacteristic2.getUuid())) {
                                    BleDevice.this.mWriteCharacteristicWrite3 = bluetoothGattCharacteristic2;
                                } else if (BleDevice.this.mBleManager.getUUID_CHARACTERISTIC_NOTIFY1().equals(bluetoothGattCharacteristic2.getUuid())) {
                                    BleDevice.this.mWriteCharacteristicWrite4 = bluetoothGattCharacteristic2;
                                    BleDevice.this.mNotifyCharacteristics.add(bluetoothGattCharacteristic2);
                                }
                            }
                        }
                    }
                    if (BleDevice.this.mDeviceListener != null) {
                        BleDevice.this.mDeviceListener.onServicesDiscovered(BleDevice.this);
                    }
                    BleDevice.this.onServiceReady();
                    if (BleDevice.this.mDeviceListener != null) {
                        BleDevice.this.mDeviceListener.onServiceReady(BleDevice.this);
                    }
                }
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            byte[] value;
            Log.i(BleDevice.TAG, "onCharacteristicRead");
            super.onCharacteristicRead(bluetoothGatt, bluetoothGattCharacteristic, i);
            if (AppConfig.DEBUG) {
                Log.i(BleDevice.TAG, BleDevice.this.mBleAddress + " -- onCharacteristicRead: " + i);
            }
            if (BleDevice.this.mDeviceListener == null || (value = bluetoothGattCharacteristic.getValue()) == null || value.length <= 11) {
                return;
            }
            byte[] bArr = new byte[value.length - 1];
            System.arraycopy(value, 0, bArr, 0, value.length - 1);
            Log.e(BleDevice.TAG, "======读取到的数据1:".concat(new String(bArr)));
            BleDevice.this.mDeviceListener.onRead(BleDevice.this, bArr);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            super.onCharacteristicWrite(bluetoothGatt, bluetoothGattCharacteristic, i);
            synchronized (BleDevice.mLocker) {
                boolean z = AppConfig.DEBUG;
                if (i == 0) {
                    if (!BleDevice.this.mBleManager.getUUID_OTA_CHARACTERISTIC_CTRL().equals(bluetoothGattCharacteristic.getUuid()) && !BleDevice.this.mBleManager.getUUID_OTA_CHARACTERISTIC_DATA().equals(bluetoothGattCharacteristic.getUuid())) {
                        BleDevice.this.onWrite();
                        if (BleDevice.this.mDeviceListener != null) {
                            BleDevice.this.mDeviceListener.onWrite(BleDevice.this, bluetoothGattCharacteristic, i);
                        }
                    }
                    if (BleDevice.this.mOtaListener != null) {
                        BleDevice.this.mOtaListener.onWrite(BleDevice.this, bluetoothGattCharacteristic, i);
                    }
                }
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            super.onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
            synchronized (BleDevice.mLocker) {
                boolean z = AppConfig.DEBUG;
                if (!BleDevice.this.mBleManager.getUUID_OTA_CHARACTERISTIC_CTRL().equals(bluetoothGattCharacteristic.getUuid()) && !BleDevice.this.mBleManager.getUUID_OTA_CHARACTERISTIC_DATA().equals(bluetoothGattCharacteristic.getUuid())) {
                    byte[] bArrOnDecrypt = BleDevice.this.onDecrypt(bluetoothGattCharacteristic.getValue());
                    BleDevice.this.onChange(bArrOnDecrypt);
                    if (BleDevice.this.mDeviceListener != null) {
                        BleDevice.this.mDeviceListener.onChanged(BleDevice.this, bArrOnDecrypt);
                    }
                    return;
                }
                if (BleDevice.this.mOtaListener != null) {
                    BleDevice.this.mOtaListener.onChanged(BleDevice.this, bluetoothGattCharacteristic.getValue());
                }
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            byte[] value = bluetoothGattDescriptor.getValue();
            if (BleDevice.this.mDeviceListener != null && value != null && value != null && value.length > 11) {
                byte[] bArr = new byte[value.length - 1];
                System.arraycopy(value, 0, bArr, 0, value.length - 1);
                Log.e(BleDevice.TAG, "======读取到的数据2:".concat(new String(bArr)));
                BleDevice.this.mDeviceListener.onRead(BleDevice.this, bArr);
            }
            super.onDescriptorRead(bluetoothGatt, bluetoothGattDescriptor, i);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            Log.i(BleDevice.TAG, "onDescriptorWrite");
            super.onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor, i);
            synchronized (BleDevice.mLocker) {
                if (i == 0) {
                    if (BleDevice.this.ctrlDescriptor == null || !BleDevice.this.ctrlDescriptor.equals(bluetoothGattDescriptor)) {
                        LogUtil.d("ctrlDescriptor1:" + BleDevice.this.ctrlDescriptor);
                        if (BleDevice.this.mNotifyCharacteristics == null || BleDevice.this.mNotifyCharacteristics.size() <= 0 || BleDevice.this.mNotifyIndex >= BleDevice.this.mNotifyCharacteristics.size()) {
                            BleDevice.this.mState = BleDevice.STATE_WROTE_DESCRIPTOR;
                            BleDevice.this.mReady = true;
                            BleDevice.this.onReady();
                            if (BleDevice.this.mDeviceListener != null) {
                                BleDevice.this.mDeviceListener.onReady(BleDevice.this, bluetoothGattDescriptor);
                            }
                        } else {
                            BleDevice bleDevice = BleDevice.this;
                            bleDevice.setCharacteristicNotification((BluetoothGattCharacteristic) bleDevice.mNotifyCharacteristics.get(BleDevice.access$1808(BleDevice.this)), true);
                        }
                    } else {
                        LogUtil.d("ctrlDescriptor:" + BleDevice.this.ctrlDescriptor + " mOtaListener:" + BleDevice.this.mOtaListener);
                        if (BleDevice.this.mOtaListener != null) {
                            BleDevice.this.mOtaListener.onReady(BleDevice.this, bluetoothGattDescriptor);
                        }
                    }
                }
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onReliableWriteCompleted(BluetoothGatt bluetoothGatt, int i) {
            Log.i(BleDevice.TAG, "ReliableWriteCompleted");
            super.onReliableWriteCompleted(bluetoothGatt, i);
            if (AppConfig.DEBUG) {
                Log.i(BleDevice.TAG, BleDevice.this.mBleAddress + " -- onReliableWriteCompleted: " + i);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onReadRemoteRssi(BluetoothGatt bluetoothGatt, int i, int i2) {
            Log.i("onReadRemoteRssi", "onReadRemoteRssi");
            super.onReadRemoteRssi(bluetoothGatt, i, i2);
            if (AppConfig.DEBUG) {
                Log.i(BleDevice.TAG, BleDevice.this.mBleAddress + " -- onReadRemoteRssi: " + i2 + ",rssi:" + i);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
            Log.i("onMtuChanged", "onMtuChanged");
            super.onMtuChanged(bluetoothGatt, i, i2);
            if (AppConfig.DEBUG) {
                Log.i(BleDevice.TAG, BleDevice.this.mBleAddress + " -- onMtuChanged: " + i2 + ",mtu:" + i);
            }
            BleDevice.this.mOtaListener.onMtuChanged(bluetoothGatt, i, i2);
        }
    }

    public static class BleDeviceHandler extends Handler {
        WeakReference<BleDevice> reference;

        BleDeviceHandler(BleDevice bleDevice, Looper looper) {
            super(looper);
            this.reference = new WeakReference<>(bleDevice);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            BleDevice bleDevice;
            super.handleMessage(message);
            WeakReference<BleDevice> weakReference = this.reference;
            if (weakReference == null || (bleDevice = weakReference.get()) == null) {
                return;
            }
            switch (message.what) {
                case BleDevice.MSG_CONNECT /* 2049 */:
                    boolean zConnectInner = BleDevice.connectInner(bleDevice);
                    if (AppConfig.DEBUG) {
                        Log.i(BleDevice.TAG, bleDevice.mBleAddress + " -- connectInner:" + zConnectInner);
                    }
                    if (!zConnectInner) {
                        bleDevice.mState = BleDevice.STATE_IDLE;
                    }
                    if (zConnectInner) {
                        bleDevice.mHandler.removeCallbacks(bleDevice.mConnectTimeout);
                        bleDevice.mHandler.postDelayed(bleDevice.mConnectTimeout, 6000L);
                    }
                    break;
                case BleDevice.MSG_DISCONNECT /* 2050 */:
                    BleDevice.disconnectInner(bleDevice);
                    break;
                case BleDevice.MSG_DISCOVER_SERVICES /* 2051 */:
                    BleDevice.discoverServicesInner(bleDevice);
                    break;
                case BleDevice.MSG_GATT_CLOSE /* 2052 */:
                    BleDevice.closeInner(bleDevice);
                    break;
                case BleDevice.MSG_SET_NOTIFICATION /* 2053 */:
                    BleDevice.setCharacteristicNotificationInner(bleDevice, (BluetoothGattCharacteristic) message.obj, message.arg1 == 1);
                    break;
            }
        }
    }
}
