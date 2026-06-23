package com.cdbwsoft.library.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import com.cdbwsoft.library.base.DataListener;
import com.cdbwsoft.library.ble.BleDevice;

/* JADX INFO: loaded from: classes.dex */
public abstract class BleListener<T extends BleDevice> extends DataListener {
    private BleManager<T> mBleManager;

    @Override // com.cdbwsoft.library.base.DataListener
    protected boolean doReadData(byte[] bArr) {
        return false;
    }

    public boolean filterDevice(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        return false;
    }

    public void onChange() {
    }

    public void onChanged(BleDevice bleDevice, byte[] bArr) {
    }

    public void onChangedOta(BleDevice bleDevice, byte[] bArr) {
    }

    public void onConnectionChanged(BleDevice bleDevice) {
    }

    public void onError(int i) {
    }

    public void onRead(BleDevice bleDevice, byte[] bArr) {
    }

    public void onReadOta(BleDevice bleDevice, byte[] bArr) {
    }

    public void onReady(BleDevice bleDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
    }

    public void onReadyOta(BleDevice bleDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
    }

    public void onServicesDiscovered(BleDevice bleDevice) {
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void onWrite(BleDevice bleDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
    }

    public void setBleManager(BleManager<T> bleManager) {
        this.mBleManager = bleManager;
    }

    public BleManager<T> getBleManager() {
        return this.mBleManager;
    }

    @Override // com.cdbwsoft.library.base.DataListener
    protected boolean doWriteData(byte[] bArr) {
        BleManager<T> bleManager = this.mBleManager;
        return bleManager != null && bleManager.writeData(bArr);
    }
}
