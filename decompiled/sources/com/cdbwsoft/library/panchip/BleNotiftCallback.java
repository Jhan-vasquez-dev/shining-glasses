package com.cdbwsoft.library.panchip;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/* JADX INFO: loaded from: classes.dex */
public abstract class BleNotiftCallback<T> {
    public abstract void onChanged(T t, BluetoothGattCharacteristic bluetoothGattCharacteristic);

    public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
    }

    public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
    }

    public void onNotifySuccess(BluetoothGatt bluetoothGatt) {
    }

    public void onServicesDiscovered(BluetoothGatt bluetoothGatt) {
    }
}
