package com.cdbwsoft.library.panchip;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Handler;
import android.util.Log;
import com.cdbwsoft.library.ble.BleDevice;
import com.cdbwsoft.library.utils.LogUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class PanchipOtaManager implements BleDevice.OtaListener {
    private static final String TAG = "PanchipOtaManager";
    private static PanchipOtaManager sPanchipOtaManager;
    private VersionInfo combinationInfo;
    private BluetoothGattCharacteristic ctrlChar;
    private BluetoothGattDescriptor ctrlDescriptor;
    private BluetoothGattCharacteristic dataChar;
    private VersionInfo deviceVersionInfo;
    private long endMs;
    private byte[] fileBuf;
    private FileInfo fileInfo;
    BleDevice mBleDevice;
    private OtaLisenter mOtaLisenter;
    private int packIndex;
    private long startMs;
    private Handler mBleHandler = new Handler();
    private volatile int packetSize = 20;
    private volatile int sendDataIndex = 0;
    private volatile int sendingDataLen = 0;
    private volatile int lastProgress = 0;
    private volatile boolean flagCrcRsp = false;

    public static final class OtaService {
        static final byte opcode_crc = 3;
        static final byte opcode_reset = 4;
        static final byte opcode_size = 2;
        static final byte opcode_version = 1;
        static final byte rsp = -128;
        static final byte rsp_ack = 4;
        static final byte rsp_crc = 3;
        static final byte rsp_crc_succeed = 85;
        static final byte rsp_failed = 1;
        static final byte rsp_finished = 2;
        static final byte rsp_finished_succeed = 85;
        static final byte rsp_success = 0;
        static final UUID uuid_service = UUID.fromString("0000fd00-0000-1000-8000-00805f9b34fb");
        static final UUID uuid_characteristic_data = UUID.fromString("0000fd01-0000-1000-8000-00805f9b34fb");
        static final UUID uuid_characteristic_ctrl = UUID.fromString("0000fd02-0000-1000-8000-00805f9b34fb");
        static final UUID uuid_descriptor = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.OtaListener
    public void onChanged(final BleDevice bleDevice, byte[] bArr) {
        LogUtil.d("=====onChanged====" + Arrays.toString(bArr));
        Log.e(TAG, "onCharacteristicChanged - raw:0x" + DataOperate.bytesToHexString(bArr));
        if (bArr == null || bArr.length < 2) {
            Log.e(TAG, "onCharacteristicChanged length wrong!");
            printLog("onCharacteristicChanged length wrong!");
            return;
        }
        if (-128 != bArr[0]) {
            Log.e(TAG, "onCharacteristicChanged rsp error!");
            printLog("onCharacteristicChanged rsp error!");
            return;
        }
        byte b = bArr[1];
        if (1 == b) {
            Log.d(TAG, "got version");
            if (bArr.length < 8) {
                Log.e(TAG, "got version len error");
                printLog("got version len error");
                return;
            }
            try {
                VersionInfo versionInfo = new VersionInfo(Arrays.copyOfRange(bArr, 2, 8));
                this.deviceVersionInfo = versionInfo;
                OtaLisenter otaLisenter = this.mOtaLisenter;
                if (otaLisenter != null) {
                    otaLisenter.OtaDeviceInfo(versionInfo);
                }
                Log.d(TAG, "versionInfo toBytes is:0x" + DataOperate.bytesToHexString(this.deviceVersionInfo.toBytes()));
                printLog("got version success");
                startOTA(bleDevice);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (2 == b) {
            if (bArr[2] == 0) {
                Log.d(TAG, "start dfu success");
                printLog("开始发送OTA数据");
                this.startMs = System.currentTimeMillis();
                boolean zSendData = sendData(bleDevice, true);
                if (!zSendData) {
                    printLog("发送第一次:" + zSendData);
                    this.mBleHandler.postDelayed(new Runnable() { // from class: com.cdbwsoft.library.panchip.PanchipOtaManager.1
                        @Override // java.lang.Runnable
                        public void run() {
                            boolean zSendData2 = PanchipOtaManager.this.sendData(bleDevice, true);
                            PanchipOtaManager.this.printLog("发送第二次:" + zSendData2);
                            if (zSendData2) {
                                return;
                            }
                            PanchipOtaManager.this.mOtaLisenter.OtaFail("发送第二次" + zSendData2);
                        }
                    }, 200L);
                }
                printLog("开始发送OTA数据isResult:" + zSendData);
                return;
            }
            Log.d(TAG, "start dfu failed");
            printLog("开始OTA失败");
            this.mOtaLisenter.OtaFail("开始OTA失败1");
            return;
        }
        if (3 != b) {
            if (4 == b) {
                Log.e(TAG, "rsp_ack: ");
                this.sendingDataLen += this.packetSize - 2;
                int i = (this.sendingDataLen * 100) / this.fileInfo.codeSize;
                if (i > this.lastProgress) {
                    OtaLisenter otaLisenter2 = this.mOtaLisenter;
                    if (otaLisenter2 != null) {
                        otaLisenter2.OtaProgress(i);
                    }
                    this.lastProgress = i;
                }
                if (sendData(bleDevice, false)) {
                    return;
                }
                this.endMs = System.currentTimeMillis();
                sendCrc(bleDevice);
                return;
            }
            return;
        }
        this.flagCrcRsp = true;
        if (bArr[2] == 0) {
            Log.d(TAG, "crc succeed");
            printLog("crc校验成功");
            OtaLisenter otaLisenter3 = this.mOtaLisenter;
            if (otaLisenter3 != null) {
                otaLisenter3.OtaSuccess();
            }
            bleDevice.disconnect();
            this.mBleDevice.removeOtaListener();
            return;
        }
        Log.d(TAG, "crc error");
        printLog("crc校验失败");
        this.mOtaLisenter.OtaFail("crc校验失败");
        bleDevice.disconnect();
        this.mBleDevice.removeOtaListener();
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.OtaListener
    public void onWrite(BleDevice bleDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        LogUtil.d("=====onWrite====" + i);
        byte[] value = bluetoothGattCharacteristic.getValue();
        if (value == null) {
            Log.e(TAG, "onCharacteristicWrite get null");
            printLog("onCharacteristicWrite get null");
            return;
        }
        if (bluetoothGattCharacteristic.equals(this.ctrlChar)) {
            Log.d(TAG, "on write ctrl");
            if (i != 0) {
                Log.d(TAG, "on gatt error");
                return;
            }
            byte b = value[0];
            if (1 == b) {
                Log.d(TAG, "version sent");
                return;
            }
            if (2 == b) {
                Log.d(TAG, "send size succeed");
                printLog("发送数据长度成功");
                this.lastProgress = 0;
            } else if (3 == b) {
                Log.d(TAG, "crc sent");
            }
        }
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.OtaListener
    public void onServicesDiscovered(BleDevice bleDevice) {
        LogUtil.d("=====onServicesDiscovered====" + bleDevice);
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.OtaListener
    public void onReady(BleDevice bleDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic;
        LogUtil.d("=====onReady====" + bleDevice);
        if (bluetoothGattDescriptor.equals(this.ctrlDescriptor)) {
            if (!Arrays.equals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, bluetoothGattDescriptor.getValue())) {
                Log.e("onDescriptorWrite", "value error");
                printLog("onDescriptorWrite value error");
                return;
            }
            FileInfo fileInfo = this.fileInfo;
            if (fileInfo == null || (bluetoothGattCharacteristic = this.ctrlChar) == null || writeChar(bleDevice, bluetoothGattCharacteristic, fileInfo.createVersionPacket())) {
                return;
            }
            Log.e("request ver", "write error");
            printLog("request ver write error");
        }
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.OtaListener
    public void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
        BluetoothGattDescriptor bluetoothGattDescriptor;
        if (i2 == 0) {
            this.packetSize = i - 3;
            printLog("mtu设置成功:" + this.packetSize);
        } else {
            printLog("mtu设置失败:" + this.packetSize);
        }
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.ctrlChar;
        if (bluetoothGattCharacteristic == null || (bluetoothGattDescriptor = this.ctrlDescriptor) == null) {
            return;
        }
        enableNotify(bluetoothGatt, bluetoothGattCharacteristic, bluetoothGattDescriptor);
    }

    private PanchipOtaManager() {
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.mBleDevice = bleDevice;
        bleDevice.setOtaListener(this);
        this.ctrlChar = bleDevice.getCtrlChar();
        this.dataChar = bleDevice.getDataChar();
        this.ctrlDescriptor = bleDevice.getCtrlDescriptor();
    }

    public void selectFile(File file) {
        FileInputStream fileInputStream;
        int iAvailable;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fileInputStream = null;
        }
        try {
            iAvailable = fileInputStream.available();
        } catch (IOException e2) {
            e2.printStackTrace();
            iAvailable = 0;
        }
        if (iAvailable < 16) {
            OtaLisenter otaLisenter = this.mOtaLisenter;
            if (otaLisenter != null) {
                otaLisenter.OtaVerifyResult(false, "not OTA file");
                return;
            }
            return;
        }
        byte[] bArr = new byte[iAvailable];
        try {
            if (fileInputStream.read(bArr) != iAvailable) {
                OtaLisenter otaLisenter2 = this.mOtaLisenter;
                if (otaLisenter2 != null) {
                    otaLisenter2.OtaVerifyResult(false, "read file error");
                }
                fileInputStream.close();
                return;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, 0, 16);
        try {
            this.fileInfo = new FileInfo(bArrCopyOfRange);
            Log.d(TAG, "file head is:0x" + DataOperate.bytesToHexString(bArrCopyOfRange));
            Log.d(TAG, "buf length:" + iAvailable + " ota codeSize:" + this.fileInfo.codeSize);
            if (this.fileInfo.codeSize + 16 != iAvailable) {
                OtaLisenter otaLisenter3 = this.mOtaLisenter;
                if (otaLisenter3 != null) {
                    otaLisenter3.OtaVerifyResult(false, "not OTA file");
                }
                this.fileInfo = null;
                return;
            }
            OtaLisenter otaLisenter4 = this.mOtaLisenter;
            if (otaLisenter4 != null) {
                otaLisenter4.OtaFileInfo(this.fileInfo);
            }
            this.fileBuf = bArr;
        } catch (Exception e4) {
            e4.printStackTrace();
            OtaLisenter otaLisenter5 = this.mOtaLisenter;
            if (otaLisenter5 != null) {
                otaLisenter5.OtaVerifyResult(false, "getVersion failed");
            }
        }
    }

    public static PanchipOtaManager getInstance() {
        if (sPanchipOtaManager == null) {
            sPanchipOtaManager = new PanchipOtaManager();
        }
        return sPanchipOtaManager;
    }

    public void setOtaLisenter(OtaLisenter otaLisenter) {
        this.mOtaLisenter = otaLisenter;
    }

    public void getVersion(BleDevice bleDevice) {
        if (this.ctrlChar == null) {
            Log.e(TAG, "getVersion: 特征值为空>>>>error");
            return;
        }
        BluetoothGatt bluetoothGatt = bleDevice.getBluetoothGatt();
        if (bluetoothGatt == null) {
            return;
        }
        Log.e(TAG, "getVersion: ctrlChar:" + this.ctrlChar + "  ctrlDescriptor:" + this.ctrlDescriptor);
        setMtuSize(bluetoothGatt, 200);
    }

    public void setMtuSize1(BleDevice bleDevice, int i) {
        if (this.ctrlChar == null) {
            Log.e(TAG, "getVersion: 特征值为空>>>>error");
            return;
        }
        BluetoothGatt bluetoothGatt = bleDevice.getBluetoothGatt();
        if (bluetoothGatt == null) {
            return;
        }
        if (!bluetoothGatt.requestMtu(i + 3)) {
            LogUtil.d("设置Mtu大小失败");
            bleDevice.setSetMtuStatus(false);
        } else {
            LogUtil.d("设置Mtu大小成功");
            bleDevice.setSetMtuStatus(true);
        }
    }

    private void enableNotify(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGattDescriptor bluetoothGattDescriptor) {
        if (!bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true)) {
            Log.e(TAG, "setCharacteristicNotification() return false");
            printLog("setCharacteristicNotification() return false");
            OtaLisenter otaLisenter = this.mOtaLisenter;
            if (otaLisenter != null) {
                otaLisenter.OtaFail("setCharacteristicNotification fail");
                return;
            }
            return;
        }
        if (!bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)) {
            Log.e(TAG, "descriptor.setValue() return false");
            printLog("descriptor.setValue() return false");
            OtaLisenter otaLisenter2 = this.mOtaLisenter;
            if (otaLisenter2 != null) {
                otaLisenter2.OtaFail("setCharacteristicNotification fail1");
                return;
            }
            return;
        }
        if (!bluetoothGatt.writeDescriptor(bluetoothGattDescriptor)) {
            Log.e(TAG, "gatt.writeDescriptor() return false");
            printLog("gatt.writeDescriptor() return false");
            OtaLisenter otaLisenter3 = this.mOtaLisenter;
            if (otaLisenter3 != null) {
                otaLisenter3.OtaFail("setCharacteristicNotification fail2");
                return;
            }
            return;
        }
        Log.e(TAG, "enableNotify: >>>>" + bluetoothGattDescriptor.getUuid().toString());
    }

    public void startOTA(BleDevice bleDevice) {
        LogUtil.d("=====开始OTA升级");
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendSize(bleDevice);
    }

    private void sendSize(final BleDevice bleDevice) {
        Log.d(TAG, "send size");
        if (writeChar(bleDevice, this.ctrlChar, this.fileInfo.createSizePacket())) {
            return;
        }
        printLog("sendSize false0");
        this.mBleHandler.postDelayed(new Runnable() { // from class: com.cdbwsoft.library.panchip.PanchipOtaManager.2
            @Override // java.lang.Runnable
            public void run() {
                PanchipOtaManager panchipOtaManager = PanchipOtaManager.this;
                if (panchipOtaManager.writeChar(bleDevice, panchipOtaManager.ctrlChar, PanchipOtaManager.this.fileInfo.createSizePacket()) || PanchipOtaManager.this.mOtaLisenter == null) {
                    return;
                }
                PanchipOtaManager.this.printLog("sendSize false11");
                PanchipOtaManager.this.mOtaLisenter.OtaFail("sendSize false11");
            }
        }, 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean writeChar(BleDevice bleDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) {
        BluetoothGatt bluetoothGatt = bleDevice.getBluetoothGatt();
        LogUtil.d("====characteristic:" + bluetoothGattCharacteristic);
        if (bluetoothGattCharacteristic.setValue(bArr)) {
            return bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
        }
        return false;
    }

    private void compareVersion(BleDevice bleDevice) {
        OtaLisenter otaLisenter;
        VersionInfo versionInfo = this.deviceVersionInfo;
        if (versionInfo == null || this.fileInfo == null) {
            OtaLisenter otaLisenter2 = this.mOtaLisenter;
            if (otaLisenter2 != null) {
                otaLisenter2.OtaVerifyResult(false, "版本信息或OTA文件为空");
                return;
            }
            return;
        }
        VersionInfo versionInfoCopy = versionInfo.copy();
        this.combinationInfo = versionInfoCopy;
        if (versionInfoCopy == null) {
            Log.e(TAG, "deviceVersion.copy() == null");
            OtaLisenter otaLisenter3 = this.mOtaLisenter;
            if (otaLisenter3 != null) {
                otaLisenter3.OtaVerifyResult(false, "deviceVersion.copy() is null");
                return;
            }
            return;
        }
        if (this.fileInfo.app != 0) {
            if (this.fileInfo.app == this.deviceVersionInfo.app) {
                OtaLisenter otaLisenter4 = this.mOtaLisenter;
                if (otaLisenter4 != null) {
                    otaLisenter4.OtaVerifyResult(false, "同一版本无法升级1");
                    startOTA(bleDevice);
                    return;
                }
                return;
            }
            if (this.fileInfo.app > this.deviceVersionInfo.app) {
                OtaLisenter otaLisenter5 = this.mOtaLisenter;
                if (otaLisenter5 != null) {
                    otaLisenter5.OtaVerifyResult(true, "可以OTA1");
                    startOTA(bleDevice);
                }
            } else if (this.mOtaLisenter != null) {
                startOTA(bleDevice);
                this.mOtaLisenter.OtaVerifyResult(true, "可以OTA2");
            }
            this.combinationInfo.app = this.fileInfo.app;
            return;
        }
        if (this.fileInfo.dev != 0) {
            if (this.fileInfo.dev == this.deviceVersionInfo.dev) {
                OtaLisenter otaLisenter6 = this.mOtaLisenter;
                if (otaLisenter6 != null) {
                    otaLisenter6.OtaVerifyResult(false, "同一版本无法升级2");
                    startOTA(bleDevice);
                    return;
                }
                return;
            }
            if (this.fileInfo.dev <= this.deviceVersionInfo.dev && (otaLisenter = this.mOtaLisenter) != null) {
                otaLisenter.OtaVerifyResult(false, "Ota版本低于设备版本");
                startOTA(bleDevice);
            }
            this.combinationInfo.dev = this.fileInfo.dev;
            return;
        }
        OtaLisenter otaLisenter7 = this.mOtaLisenter;
        if (otaLisenter7 != null) {
            otaLisenter7.OtaVerifyResult(false, "OTA验证错误 ");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0047, code lost:
    
        if (r6.sendDataIndex < r6.fileBuf.length) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean sendData(com.cdbwsoft.library.ble.BleDevice r7, boolean r8) {
        /*
            Method dump skipped, instruction units count: 312
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdbwsoft.library.panchip.PanchipOtaManager.sendData(com.cdbwsoft.library.ble.BleDevice, boolean):boolean");
    }

    private void sendCrc(BleDevice bleDevice) {
        if (this.ctrlChar == null || this.fileInfo == null) {
            return;
        }
        Log.d(TAG, "send crc");
        this.flagCrcRsp = false;
        this.mBleHandler.postDelayed(new Runnable() { // from class: com.cdbwsoft.library.panchip.PanchipOtaManager.3
            @Override // java.lang.Runnable
            public void run() {
                if (PanchipOtaManager.this.flagCrcRsp) {
                    return;
                }
                PanchipOtaManager.this.printLog("获取CRC超时");
                if (PanchipOtaManager.this.mOtaLisenter != null) {
                    PanchipOtaManager.this.mOtaLisenter.OtaFail("获取CRC超时");
                }
            }
        }, 20000L);
        if (writeChar(bleDevice, this.ctrlChar, this.fileInfo.createCrcPacket())) {
            return;
        }
        OtaLisenter otaLisenter = this.mOtaLisenter;
        if (otaLisenter != null) {
            otaLisenter.OtaFail("sendData false");
        }
        printLog("sendCrc false");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void printLog(String str) {
        Log.i(TAG, str);
    }

    private void setMtuSize(BluetoothGatt bluetoothGatt, int i) {
        if (!bluetoothGatt.requestMtu(i + 3)) {
            LogUtil.d("设置MCU大小失败");
        } else {
            LogUtil.d("设置MCU大小成功");
        }
    }
}
