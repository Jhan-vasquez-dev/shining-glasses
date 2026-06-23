package com.cdbwsoft.library.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.ble.BleDevice;
import com.cdbwsoft.library.statistics.Running;
import java.lang.ref.WeakReference;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public abstract class BleManager<T extends BleDevice> implements BleDevice.DeviceListener<T> {
    private static final int MAX_CONNECTING_DEVICE = 4;
    private static final int MSG_ON_CHANGE = 2311;
    private static final int MSG_ON_CONNECTION_CHANGED = 2310;
    public static final int MSG_ON_CONNECT_MAX = 2316;
    private static final int MSG_ON_DEVICE_CHANGED = 2308;
    private static final int MSG_ON_DEVICE_READ = 2306;
    private static final int MSG_ON_DEVICE_READY = 2307;
    private static final int MSG_ON_DEVICE_WRITE = 2305;
    private static final int MSG_ON_SERVICES_DISCOVERED = 2309;
    private static final int MSG_ON_START = 2312;
    private static final int MSG_ON_STOP = 2313;
    private static final int MSG_SCAN_START = 2314;
    private static final int MSG_SCAN_STOP = 2315;
    private static final int SCAN_TIME = 10000;
    private static final int STATE_SCANNING = 1;
    private static final int STATE_STOPPED = 0;
    private static final String TAG = "BleManager";
    public static final String UUID_CHARACTERISTIC_TEXT = "d44bc439-abfd-45a2-b575-925416129600";
    public static final String UUID_CHARACTERISTIC_TEXT_NOTIFY1 = "d44bc439-abfd-45a2-b575-925416129601";
    public static final String UUID_CHARACTERISTIC_TEXT_WRITE1 = "d44bc439-abfd-45a2-b575-925416129600";
    public static final String UUID_CHARACTERISTIC_TEXT_WRITE2 = "d44bc439-abfd-45a2-b575-92541612960a";
    public static final String UUID_CHARACTERISTIC_TEXT_WRITE3 = "d44bc439-abfd-45a2-b575-92541612960b";
    public static final String UUID_CHARACTERISTIC_TEXT_WRITE4 = "d44bc439-abfd-45a2-b575-925416129601";
    public static final String UUID_DESCRIPTOR_TEXT = "00002902-0000-1000-8000-00805f9b34fb";
    public static final String UUID_SERVICE_TEXT = "0000fff0-0000-1000-8000-00805f9b34fb";
    public UUID UUID_CHARACTERISTIC;
    public UUID UUID_CHARACTERISTIC_NOTIFY1;
    public UUID UUID_CHARACTERISTIC_WRITE1;
    public UUID UUID_CHARACTERISTIC_WRITE2;
    public UUID UUID_CHARACTERISTIC_WRITE3;
    public UUID UUID_DESCRIPTOR;
    private UUID UUID_OTA_CHARACTERISTIC_CTRL;
    private UUID UUID_OTA_CHARACTERISTIC_DATA;
    private UUID UUID_OTA_DESCRIPTOR;
    private UUID UUID_OTA_SERVICE;
    public UUID UUID_SERVICE;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean isScan;
    private BleDeviceFactory<T> mBleDeviceFactory;
    private List<BleListener> mBleListeners;
    private BleManager<T>.BleReceiver mBleReceiver;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private List<T> mConnectedDevices;
    private List<T> mConnectingDevices;
    private Context mContext;
    private List<T> mCurrentDevices;
    private final Class<T> mDeviceClass;
    private final List<T> mDevices;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private final Object mLocker;
    private final Handler mMessageHandler;
    private int mState;
    private Runnable mStopRunnable;
    private final ScanCallback scanCallback;

    public UUID getUUID_CHARACTERISTIC_WRITE1() {
        return this.UUID_CHARACTERISTIC_WRITE1;
    }

    public UUID getUUID_CHARACTERISTIC_WRITE2() {
        return this.UUID_CHARACTERISTIC_WRITE2;
    }

    public UUID getUUID_CHARACTERISTIC_WRITE3() {
        return this.UUID_CHARACTERISTIC_WRITE3;
    }

    public UUID getUUID_CHARACTERISTIC_NOTIFY1() {
        return this.UUID_CHARACTERISTIC_NOTIFY1;
    }

    public UUID getUUID_OTA_SERVICE() {
        return this.UUID_OTA_SERVICE;
    }

    public UUID getUUID_OTA_CHARACTERISTIC_DATA() {
        return this.UUID_OTA_CHARACTERISTIC_DATA;
    }

    public UUID getUUID_OTA_CHARACTERISTIC_CTRL() {
        return this.UUID_OTA_CHARACTERISTIC_CTRL;
    }

    public UUID getUUID_OTA_DESCRIPTOR() {
        return this.UUID_OTA_DESCRIPTOR;
    }

    public BleManager(Context context) {
        this(context, null, null);
    }

    public BleManager(Context context, BleListener<T> bleListener) {
        this(context, bleListener, null);
    }

    public BleManager(Context context, BleDeviceFactory<T> bleDeviceFactory) {
        this(context, null, bleDeviceFactory);
    }

    public BleManager(Context context, BleListener<T> bleListener, BleDeviceFactory<T> bleDeviceFactory) {
        this.UUID_SERVICE = UUID.fromString(UUID_SERVICE_TEXT);
        this.UUID_CHARACTERISTIC = UUID.fromString("d44bc439-abfd-45a2-b575-925416129600");
        this.UUID_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        this.UUID_CHARACTERISTIC_WRITE1 = UUID.fromString("d44bc439-abfd-45a2-b575-925416129600");
        this.UUID_CHARACTERISTIC_WRITE2 = UUID.fromString(UUID_CHARACTERISTIC_TEXT_WRITE2);
        this.UUID_CHARACTERISTIC_WRITE3 = UUID.fromString(UUID_CHARACTERISTIC_TEXT_WRITE3);
        this.UUID_CHARACTERISTIC_NOTIFY1 = UUID.fromString("d44bc439-abfd-45a2-b575-925416129601");
        this.UUID_OTA_SERVICE = UUID.fromString("0000fd00-0000-1000-8000-00805f9b34fb");
        this.UUID_OTA_CHARACTERISTIC_DATA = UUID.fromString("0000fd01-0000-1000-8000-00805f9b34fb");
        this.UUID_OTA_CHARACTERISTIC_CTRL = UUID.fromString("0000fd02-0000-1000-8000-00805f9b34fb");
        this.UUID_OTA_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        this.mDevices = new ArrayList();
        this.mBleListeners = new ArrayList();
        this.mBleReceiver = new BleReceiver();
        this.mConnectingDevices = new ArrayList();
        this.mCurrentDevices = new ArrayList();
        this.mConnectedDevices = new ArrayList();
        this.mLocker = new Object();
        this.mLeScanCallback = new BluetoothAdapter.LeScanCallback() { // from class: com.cdbwsoft.library.ble.BleManager.1
            @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
            public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
                Log.v("ruis", "onLeScan" + bluetoothDevice);
                while (true) {
                    for (BleListener bleListener2 : BleManager.this.mBleListeners) {
                        boolean z = z || bleListener2.filterDevice(bluetoothDevice, i, bArr);
                    }
                    return;
                }
            }
        };
        this.scanCallback = new ScanCallback() { // from class: com.cdbwsoft.library.ble.BleManager.2
            @Override // android.bluetooth.le.ScanCallback
            public void onScanResult(int i, ScanResult scanResult) {
                super.onScanResult(i, scanResult);
                while (true) {
                    for (BleListener bleListener2 : BleManager.this.mBleListeners) {
                        boolean z = z || bleListener2.filterDevice(scanResult.getDevice(), scanResult.getRssi(), scanResult.getScanRecord().getBytes());
                    }
                    return;
                }
            }

            @Override // android.bluetooth.le.ScanCallback
            public void onScanFailed(int i) {
                super.onScanFailed(i);
                Log.v("ruis", "onScanFailed" + i);
            }
        };
        this.mStopRunnable = new Runnable() { // from class: com.cdbwsoft.library.ble.BleManager.3
            @Override // java.lang.Runnable
            public void run() {
                if (BleManager.this.mState == 0) {
                    return;
                }
                if (BleManager.this.mState == 1) {
                    BleManager.this.mState = 0;
                    BleManager.this.mMessageHandler.obtainMessage(BleManager.MSG_ON_STOP).sendToTarget();
                }
                if (BleManager.this.mBluetoothAdapter.getState() != 12 || BleManager.this.bluetoothLeScanner == null) {
                    return;
                }
                BleManager.this.bluetoothLeScanner.stopScan(BleManager.this.scanCallback);
            }
        };
        if (bleListener != null) {
            bleListener.setBleManager(this);
            this.mBleListeners.add(bleListener);
        }
        this.mBleDeviceFactory = bleDeviceFactory;
        this.mContext = context;
        this.mMessageHandler = new MessageHandler(this, context.getMainLooper());
        this.mDeviceClass = getClass(((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0], 0);
        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UUID getUuidService() {
        return this.UUID_SERVICE;
    }

    public UUID getUuidCharacteristic() {
        return this.UUID_CHARACTERISTIC;
    }

    public UUID getUuidDescriptor() {
        return this.UUID_DESCRIPTOR;
    }

    private static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) {
            return getGenericClass((ParameterizedType) type, i);
        }
        if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0);
        }
        return (Class) type;
    }

    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Type type = parameterizedType.getActualTypeArguments()[i];
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getRawType();
        }
        if (type instanceof GenericArrayType) {
            return (Class) ((GenericArrayType) type).getGenericComponentType();
        }
        if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0);
        }
        return (Class) type;
    }

    public boolean initialize() {
        if (this.mBluetoothManager == null) {
            BluetoothManager bluetoothManager = (BluetoothManager) this.mContext.getSystemService("bluetooth");
            this.mBluetoothManager = bluetoothManager;
            if (bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        BluetoothAdapter adapter = this.mBluetoothManager.getAdapter();
        this.mBluetoothAdapter = adapter;
        if (adapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        if (this.mBleDeviceFactory == null) {
            this.mBleDeviceFactory = new BleDeviceFactory<>(this.mContext);
        }
        this.bluetoothLeScanner = this.mBluetoothAdapter.getBluetoothLeScanner();
        return true;
    }

    public Class<T> getDeviceClass() {
        return this.mDeviceClass;
    }

    public void onPause() {
        try {
            Context context = this.mContext;
            if (context != null) {
                context.unregisterReceiver(this.mBleReceiver);
            }
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void onResume() {
        try {
            Context context = this.mContext;
            if (context != null) {
                context.registerReceiver(this.mBleReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            }
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public List<BleListener> getBleListeners() {
        return this.mBleListeners;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return this.mBluetoothAdapter;
    }

    public BleDeviceFactory<T> getBleDeviceFactory() {
        return this.mBleDeviceFactory;
    }

    public int getState() {
        return this.mState;
    }

    public void addDevice(T t) {
        if (t == null) {
            return;
        }
        if (this.mDevices.contains(t)) {
            Log.e(TAG, "addDevice: 已经包含++++");
        }
        t.setDeviceListener(this);
        this.mDevices.add(t);
        this.mMessageHandler.obtainMessage(2311).sendToTarget();
    }

    public void addDevice(List<T> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        synchronized (this.mDevices) {
            for (T t : list) {
                if (!this.mDevices.contains(t)) {
                    this.mDevices.add(t);
                }
            }
        }
        this.mMessageHandler.obtainMessage(2311).sendToTarget();
    }

    public boolean addConnectingDevice(T t) {
        if (t == null || !this.mDevices.contains(t)) {
            return false;
        }
        synchronized (this.mLocker) {
            if (this.mCurrentDevices.contains(t)) {
                Log.e(TAG, "addConnectingDevice:已经包含设备");
                return true;
            }
            if (!this.mConnectingDevices.contains(t)) {
                return false;
            }
            if (this.mCurrentDevices.size() >= 4) {
                return false;
            }
            this.mCurrentDevices.add(t);
            return true;
        }
    }

    public List<T> getDevices() {
        return this.mDevices;
    }

    public void addDevice(BluetoothDevice bluetoothDevice) throws Exception {
        if (bluetoothDevice == null || contains(bluetoothDevice)) {
            return;
        }
        addDevice(this.mBleDeviceFactory.create(this, bluetoothDevice.getAddress(), bluetoothDevice.getName()));
    }

    public boolean contains(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            return false;
        }
        Iterator<T> it = this.mDevices.iterator();
        while (it.hasNext()) {
            if (it.next().equals(bluetoothDevice.getAddress())) {
                return true;
            }
        }
        return false;
    }

    public T getDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            return null;
        }
        for (T t : this.mDevices) {
            if (t.equals(bluetoothDevice.getAddress())) {
                return t;
            }
        }
        return null;
    }

    public T getDevice(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (T t : this.mDevices) {
            if (t.equals(str)) {
                return t;
            }
        }
        return null;
    }

    public boolean contains(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Iterator<T> it = this.mDevices.iterator();
        while (it.hasNext()) {
            if (it.next().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public T getBleDevice(int i) {
        if (i < 0) {
            return null;
        }
        for (T t : this.mDevices) {
            if (i == t.getIndex()) {
                return t;
            }
        }
        return null;
    }

    public BluetoothManager getBluetoothManager() {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) this.mContext.getSystemService("bluetooth");
        }
        return this.mBluetoothManager;
    }

    public List<T> getConnectingDevices() {
        return this.mConnectingDevices;
    }

    public List<T> getConnectedDevices() {
        return this.mConnectedDevices;
    }

    public Context getContext() {
        return this.mContext;
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.DeviceListener
    public void onWrite(T t, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        this.mMessageHandler.obtainMessage(2305, t.getIndex(), i, bluetoothGattCharacteristic).sendToTarget();
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.DeviceListener
    public void onRead(T t, byte[] bArr) {
        this.mMessageHandler.obtainMessage(2306, t.getIndex(), 0, bArr).sendToTarget();
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.DeviceListener
    public void onReady(T t, BluetoothGattDescriptor bluetoothGattDescriptor) {
        checkDeviceStep(t);
        this.mMessageHandler.obtainMessage(2307, t.getIndex(), 0, bluetoothGattDescriptor).sendToTarget();
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.DeviceListener
    public void onServiceReady(T t) {
        checkDeviceStep(t);
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.DeviceListener
    public void onChanged(T t, byte[] bArr) {
        this.mMessageHandler.obtainMessage(MSG_ON_DEVICE_CHANGED, t.getIndex(), 0, bArr).sendToTarget();
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.DeviceListener
    public void onServicesDiscovered(T t) {
        checkDeviceStep(t);
        this.mMessageHandler.obtainMessage(2309, t.getIndex(), 0).sendToTarget();
    }

    private void checkDeviceStep(T t) {
        if (t == null || !this.mDevices.contains(t)) {
            Log.e(TAG, "checkDeviceStep:mDevices不包含设备 ");
            return;
        }
        synchronized (this.mLocker) {
            int state = t.getState();
            if (state == 2310) {
                t.setEnableCharacteristicNotification();
            } else if (state != 2312) {
                switch (state) {
                    case 2305:
                        this.mConnectingDevices.remove(t);
                        this.mConnectedDevices.remove(t);
                        Log.e(TAG, "checkDeviceStep: mConnectedDevices被移除111");
                        if (this.mCurrentDevices.contains(t)) {
                            this.mCurrentDevices.remove(t);
                            if (this.mConnectingDevices.size() > 0) {
                                this.mConnectingDevices.get(0).connect();
                            }
                        }
                        break;
                    case 2306:
                        if (this.mCurrentDevices.contains(t)) {
                            if (this.mConnectingDevices.contains(t)) {
                                this.mConnectingDevices.remove(t);
                                Log.e(TAG, "checkDeviceStep:===STATE_CONNECTING111 ");
                            }
                            this.mCurrentDevices.remove(t);
                            Log.e(TAG, "checkDeviceStep:===STATE_CONNECTING222 ");
                        }
                        if (this.mConnectingDevices.size() > 0) {
                            T t2 = this.mConnectingDevices.get(0);
                            Log.e(TAG, "checkDeviceStep:===STATE_CONNECTING333 ");
                            t2.connect();
                        }
                        break;
                    case 2307:
                        Log.e(TAG, "checkDeviceStep:===STATE_CONNECTED222 ");
                        t.discoverServices();
                        break;
                }
            } else {
                String str = TAG;
                Log.e(str, "checkDeviceStep:===STATE_WROTE_DESCRIPTOR111 ");
                this.mConnectedDevices.add(t);
                this.mConnectingDevices.remove(t);
                this.mCurrentDevices.remove(t);
                Log.e(str, "checkDeviceStep:===STATE_WROTE_DESCRIPTOR222 ");
                if (this.mConnectingDevices.size() > 0) {
                    T t3 = this.mConnectingDevices.get(0);
                    Log.e(str, "checkDeviceStep:===STATE_WROTE_DESCRIPTOR333 ");
                    t3.connect();
                }
                try {
                    Running.app(3, "connect", t.getBleName() + "," + t.getBleAddress());
                } catch (Exception e) {
                    if (AppConfig.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.DeviceListener
    public void onConnectionChanged(T t) {
        checkDeviceStep(t);
        this.mMessageHandler.obtainMessage(2310, t.getIndex(), 0).sendToTarget();
    }

    public void deleteDevice(T t) {
        if (t == null) {
            return;
        }
        for (T t2 : this.mDevices) {
            if (t2 == t || t2.equals(t)) {
                t.setDeleted(true);
                return;
            }
        }
    }

    public void removeDevice(T t) {
        if (t == null) {
            return;
        }
        synchronized (this.mLocker) {
            if (this.mDevices.remove(t)) {
                this.mConnectingDevices.remove(t);
                this.mConnectedDevices.remove(t);
                Log.e(TAG, "removeDevice: mConnectedDevices被移除222");
                this.mCurrentDevices.remove(t);
                this.mMessageHandler.obtainMessage(2311).sendToTarget();
            }
        }
    }

    private void removeDevice(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        for (T t : this.mDevices) {
            if (t.equals(str)) {
                removeDevice(t);
            }
        }
    }

    public void registerBleListener(BleListener<T> bleListener) {
        if (this.mBleListeners.contains(bleListener)) {
            Log.e(TAG, "registerBleListener包含");
            return;
        }
        String str = TAG;
        Log.e(str, "registerBleListener");
        this.mBleListeners.add(bleListener);
        Log.e(str, "mBleListeners size:" + this.mBleListeners.size());
        if (bleListener.getBleManager() == null) {
            bleListener.setBleManager(this);
        }
    }

    public Object getLocker() {
        return this.mLocker;
    }

    public void unRegisterBleListener(BleListener bleListener) {
        if (bleListener != null && this.mBleListeners.contains(bleListener)) {
            this.mBleListeners.remove(bleListener);
        }
    }

    public boolean isScan() {
        return this.isScan;
    }

    public void scanDevice() {
        scanDevice(null);
    }

    public void scanDevice(UUID[] uuidArr) {
        this.isScan = true;
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            this.mBluetoothAdapter.enable();
        } else {
            this.mMessageHandler.obtainMessage(MSG_SCAN_START, uuidArr).sendToTarget();
        }
    }

    public void stopScan() {
        this.isScan = false;
        if (this.mState == 0) {
            return;
        }
        this.mMessageHandler.obtainMessage(MSG_SCAN_STOP).sendToTarget();
    }

    public boolean writeData(byte[] bArr) {
        List<T> list = this.mConnectedDevices;
        if (list == null || list.size() <= 0) {
            return false;
        }
        Iterator<T> it = this.mConnectedDevices.iterator();
        while (it.hasNext()) {
            try {
                it.next().writeCharacteristic(bArr);
            } catch (Exception e) {
                if (AppConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public boolean writeDataBy2(byte[] bArr) {
        List<T> list = this.mConnectedDevices;
        if (list == null || list.size() <= 0) {
            return false;
        }
        Iterator<T> it = this.mConnectedDevices.iterator();
        while (it.hasNext()) {
            try {
                it.next().writeCharacteristicBy2(bArr);
            } catch (Exception e) {
                if (AppConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public boolean writeDataBy3(byte[] bArr) {
        List<T> list = this.mConnectedDevices;
        if (list == null || list.size() <= 0) {
            return false;
        }
        Iterator<T> it = this.mConnectedDevices.iterator();
        while (it.hasNext()) {
            try {
                it.next().writeCharacteristicBy3(bArr);
            } catch (Exception e) {
                if (AppConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public boolean writeDataBy4(byte[] bArr) {
        List<T> list = this.mConnectedDevices;
        if (list == null || list.size() <= 0) {
            return false;
        }
        Iterator<T> it = this.mConnectedDevices.iterator();
        while (it.hasNext()) {
            try {
                it.next().writeCharacteristicBy4(bArr);
            } catch (Exception e) {
                if (AppConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public boolean connect(T... tArr) {
        Log.e(TAG, "connect: " + tArr);
        if (tArr == null || tArr.length == 0) {
            return false;
        }
        synchronized (this.mLocker) {
            ArrayList<BleDevice> arrayList = new ArrayList();
            boolean z = false;
            for (T t : tArr) {
                if (!this.mDevices.contains(t)) {
                    this.mDevices.add(t);
                }
                if (!this.mConnectingDevices.contains(t) && t.getState() == 2305) {
                    if (this.mConnectingDevices.size() + this.mConnectedDevices.size() < AppConfig.MAX_CONNECTED_DEVICE) {
                        this.mConnectingDevices.add(t);
                        if (this.mCurrentDevices.size() < 4 && !this.mCurrentDevices.contains(t)) {
                            Log.e(TAG, "connect: mCurrentDevices第一次被添加");
                            this.mCurrentDevices.add(t);
                            arrayList.add(t);
                        }
                    } else {
                        z = true;
                    }
                }
            }
            if (z) {
                this.mMessageHandler.obtainMessage(MSG_ON_CONNECT_MAX).sendToTarget();
            }
            if (arrayList.size() > 0) {
                for (BleDevice bleDevice : arrayList) {
                    Log.i(TAG, "Connect Current Device:" + bleDevice.getBleAddress());
                    bleDevice.connect();
                }
            }
        }
        return false;
    }

    public boolean connectAll() {
        if (this.mDevices.size() == 0) {
            return false;
        }
        synchronized (this.mLocker) {
            ArrayList<BleDevice> arrayList = new ArrayList();
            boolean z = false;
            for (T t : this.mDevices) {
                if (!this.mConnectingDevices.contains(t) && t.getState() == 2305) {
                    if (this.mConnectingDevices.size() + this.mConnectedDevices.size() < AppConfig.MAX_CONNECTED_DEVICE) {
                        this.mConnectingDevices.add(t);
                        if (this.mCurrentDevices.size() < 4 && !this.mCurrentDevices.contains(t)) {
                            this.mCurrentDevices.add(t);
                            arrayList.add(t);
                        }
                    } else {
                        z = true;
                    }
                }
            }
            if (z) {
                this.mMessageHandler.obtainMessage(MSG_ON_CONNECT_MAX).sendToTarget();
            }
            if (arrayList.size() > 0) {
                for (BleDevice bleDevice : arrayList) {
                    Log.i(TAG, "Connect Current Device:" + bleDevice.getBleAddress());
                    bleDevice.connect();
                }
            }
        }
        return false;
    }

    public void release() {
        this.mBleReceiver = null;
        clear();
    }

    public void clear() {
        synchronized (this.mLocker) {
            for (T t : this.mDevices) {
                t.disconnect();
                t.close();
            }
            this.mConnectedDevices.clear();
            this.mConnectingDevices.clear();
            this.mCurrentDevices.clear();
            this.mDevices.clear();
        }
    }

    public void setDevices(List<T> list) {
        clear();
        if (list == null || list.size() <= 0) {
            return;
        }
        synchronized (this.mDevices) {
            this.mDevices.addAll(list);
            Iterator<T> it = this.mDevices.iterator();
            while (it.hasNext()) {
                it.next().setDeviceListener(this);
            }
        }
    }

    public static class MessageHandler<T extends BleDevice> extends Handler {
        private WeakReference<BleManager<T>> mReference;

        MessageHandler(BleManager<T> bleManager, Looper looper) {
            super(looper);
            this.mReference = new WeakReference<>(bleManager);
        }

        @Override // android.os.Handler
        public void dispatchMessage(Message message) {
            BleManager<T> bleManager = this.mReference.get();
            if (bleManager == null) {
                return;
            }
            switch (message.what) {
                case 2305:
                    Iterator it = ((BleManager) bleManager).mBleListeners.iterator();
                    while (it.hasNext()) {
                        ((BleListener) it.next()).onWrite(bleManager.getBleDevice(message.arg1), (BluetoothGattCharacteristic) message.obj, message.arg2);
                    }
                    break;
                case 2306:
                    Log.d(BleManager.TAG, "MSG_ON_DEVICE_READ");
                    for (BleListener bleListener : ((BleManager) bleManager).mBleListeners) {
                        Log.d(BleManager.TAG, "msg.obj".concat(new String((byte[]) message.obj)));
                        bleListener.onRead(bleManager.getBleDevice(message.arg1), (byte[]) message.obj);
                    }
                    break;
                case 2307:
                    Iterator it2 = ((BleManager) bleManager).mBleListeners.iterator();
                    while (it2.hasNext()) {
                        ((BleListener) it2.next()).onReady(bleManager.getBleDevice(message.arg1), (BluetoothGattDescriptor) message.obj);
                    }
                    break;
                case BleManager.MSG_ON_DEVICE_CHANGED /* 2308 */:
                    for (int i = 0; i < ((BleManager) bleManager).mBleListeners.size(); i++) {
                        ((BleListener) ((BleManager) bleManager).mBleListeners.get(i)).onChanged(bleManager.getBleDevice(message.arg1), (byte[]) message.obj);
                    }
                    break;
                case 2309:
                    Iterator it3 = ((BleManager) bleManager).mBleListeners.iterator();
                    while (it3.hasNext()) {
                        ((BleListener) it3.next()).onServicesDiscovered(bleManager.getBleDevice(message.arg1));
                    }
                    break;
                case 2310:
                    Iterator it4 = ((BleManager) bleManager).mBleListeners.iterator();
                    while (it4.hasNext()) {
                        ((BleListener) it4.next()).onConnectionChanged(bleManager.getBleDevice(message.arg1));
                    }
                    break;
                case 2311:
                    Log.e(BleManager.TAG, "dispatchMessage: MSG_ON_CHANGE");
                    Iterator it5 = ((BleManager) bleManager).mBleListeners.iterator();
                    while (it5.hasNext()) {
                        ((BleListener) it5.next()).onChange();
                    }
                    break;
                case 2312:
                    Iterator it6 = ((BleManager) bleManager).mBleListeners.iterator();
                    while (it6.hasNext()) {
                        ((BleListener) it6.next()).onStart();
                    }
                    break;
                case BleManager.MSG_ON_STOP /* 2313 */:
                    Iterator it7 = ((BleManager) bleManager).mBleListeners.iterator();
                    while (it7.hasNext()) {
                        ((BleListener) it7.next()).onStop();
                    }
                    break;
                case BleManager.MSG_SCAN_START /* 2314 */:
                    Log.v("ruis", "MSG_SCAN_START+" + ((BleManager) bleManager).mState);
                    ((BleManager) bleManager).mMessageHandler.removeCallbacks(((BleManager) bleManager).mStopRunnable);
                    if (((BleManager) bleManager).mState != 1 && ((BleManager) bleManager).bluetoothLeScanner != null) {
                        Log.v("ruis", "startLeScan");
                        ((BleManager) bleManager).bluetoothLeScanner.startScan(((BleManager) bleManager).scanCallback);
                    }
                    ((BleManager) bleManager).mState = 1;
                    ((BleManager) bleManager).mMessageHandler.obtainMessage(2312).sendToTarget();
                    ((BleManager) bleManager).mMessageHandler.postDelayed(((BleManager) bleManager).mStopRunnable, 10000L);
                    break;
                case BleManager.MSG_SCAN_STOP /* 2315 */:
                    ((BleManager) bleManager).mMessageHandler.removeCallbacks(((BleManager) bleManager).mStopRunnable);
                    ((BleManager) bleManager).mStopRunnable.run();
                    break;
                case BleManager.MSG_ON_CONNECT_MAX /* 2316 */:
                    Iterator it8 = ((BleManager) bleManager).mBleListeners.iterator();
                    while (it8.hasNext()) {
                        ((BleListener) it8.next()).onError(BleManager.MSG_ON_CONNECT_MAX);
                    }
                    break;
                default:
                    super.dispatchMessage(message);
                    break;
            }
        }
    }

    class BleReceiver extends BroadcastReceiver {
        BleReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction()) && intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1) == 12) {
                BleManager.this.scanDevice();
            }
        }
    }
}
