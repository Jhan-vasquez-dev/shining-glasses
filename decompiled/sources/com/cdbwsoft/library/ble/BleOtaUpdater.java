package com.cdbwsoft.library.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Handler;
import android.util.Log;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.ble.BleDevice;
import com.cdbwsoft.library.ble.BleGlobalVariables;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.Semaphore;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public class BleOtaUpdater implements BleDevice.OtaListener {
    public static final int OTA_BEFORE = 4;
    public static final int OTA_FAIL = 3;
    public static final int OTA_OVER = 2;
    public static final int OTA_UPDATE = 1;
    private static final String TAG = "BleOtaUpdater";
    private BleDevice mBleDevice;
    private Handler mHandler;
    private Thread mUpdateThread;
    private Semaphore semp;
    private int mStartOffset = 0;
    private int mPercent = 0;
    private final int mTimeout = 12;
    private final int mPacketSize = 256;
    private boolean mShouldStop = false;
    private String mFilePath = null;
    private int mByteRate = 0;
    private int mElapsedTime = 0;
    private int mIndex = 0;
    private BleGlobalVariables.OtaResult mRetValue = BleGlobalVariables.OtaResult.OTA_RESULT_SUCCESS;
    private Runnable mUpdateRunnable = new Runnable() { // from class: com.cdbwsoft.library.ble.BleOtaUpdater.1
        @Override // java.lang.Runnable
        public void run() {
            BleOtaUpdater bleOtaUpdater = BleOtaUpdater.this;
            bleOtaUpdater.otaUpdateProcess(bleOtaUpdater.mFilePath);
        }
    };

    @Override // com.cdbwsoft.library.ble.BleDevice.OtaListener
    public void onChanged(BleDevice bleDevice, byte[] bArr) {
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.OtaListener
    public void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.OtaListener
    public void onReady(BleDevice bleDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.OtaListener
    public void onServicesDiscovered(BleDevice bleDevice) {
    }

    @Override // com.cdbwsoft.library.ble.BleDevice.OtaListener
    public void onWrite(BleDevice bleDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
    }

    public BleOtaUpdater(Handler handler) {
        this.mHandler = handler;
    }

    public void setIndex(int i) {
        this.mIndex = i;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public BleDevice getBleDevice() {
        return this.mBleDevice;
    }

    public void otaPrintBytes(byte[] bArr, String str) {
        if (bArr != null) {
            StringBuilder sb = new StringBuilder(bArr.length);
            for (byte b : bArr) {
                sb.append(String.format("%02X ", Byte.valueOf(b)));
            }
            if (AppConfig.DEBUG) {
                Log.i(TAG, str + " :" + sb.toString());
            }
        }
    }

    /* JADX INFO: renamed from: com.cdbwsoft.library.ble.BleOtaUpdater$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$cdbwsoft$library$ble$BleGlobalVariables$OtaCmd;

        static {
            int[] iArr = new int[BleGlobalVariables.OtaCmd.values().length];
            $SwitchMap$com$cdbwsoft$library$ble$BleGlobalVariables$OtaCmd = iArr;
            try {
                iArr[BleGlobalVariables.OtaCmd.OTA_CMD_META_DATA.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$cdbwsoft$library$ble$BleGlobalVariables$OtaCmd[BleGlobalVariables.OtaCmd.OTA_CMD_BRICK_DATA.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$cdbwsoft$library$ble$BleGlobalVariables$OtaCmd[BleGlobalVariables.OtaCmd.OTA_CMD_DATA_VERIFY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$cdbwsoft$library$ble$BleGlobalVariables$OtaCmd[BleGlobalVariables.OtaCmd.OTA_CMD_EXECUTION_NEW_CODE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private byte cmdToValue(BleGlobalVariables.OtaCmd otaCmd) {
        int i = AnonymousClass2.$SwitchMap$com$cdbwsoft$library$ble$BleGlobalVariables$OtaCmd[otaCmd.ordinal()];
        byte b = 1;
        if (i != 1) {
            b = 2;
            if (i != 2) {
                b = 3;
                if (i != 3) {
                    b = 4;
                    if (i != 4) {
                        return (byte) 0;
                    }
                }
            }
        }
        return b;
    }

    private BleGlobalVariables.OtaCmd valueToCmd(int i) {
        int i2 = i & 255;
        if (i2 == 1) {
            return BleGlobalVariables.OtaCmd.OTA_CMD_META_DATA;
        }
        if (i2 == 2) {
            return BleGlobalVariables.OtaCmd.OTA_CMD_BRICK_DATA;
        }
        if (i2 == 3) {
            return BleGlobalVariables.OtaCmd.OTA_CMD_DATA_VERIFY;
        }
        if (i2 != 4) {
            return null;
        }
        return BleGlobalVariables.OtaCmd.OTA_CMD_EXECUTION_NEW_CODE;
    }

    private boolean otaWrite(byte[] bArr) throws InterruptedException {
        if (shouldStopUpdate()) {
            if (AppConfig.DEBUG) {
                Log.e(TAG, "otaWrite:Stopped for some reason");
            }
            return false;
        }
        if (!this.mBleDevice.writeOtaData(bArr)) {
            if (AppConfig.DEBUG) {
                Log.e(TAG, "Failed to write characteristic");
            }
            return false;
        }
        return waitWriteDataCompleted();
    }

    private boolean otaSendPacket(BleGlobalVariables.OtaCmd otaCmd, short s, byte[] bArr, int i) {
        int i2;
        byte[] bArr2;
        byte bCmdToValue = cmdToValue(otaCmd);
        byte[] bArr3 = {(byte) s, (byte) (s >> 8)};
        int i3 = AnonymousClass2.$SwitchMap$com$cdbwsoft$library$ble$BleGlobalVariables$OtaCmd[otaCmd.ordinal()];
        if (i3 == 1 || i3 == 2) {
            int i4 = i + 1;
            byte[] bArr4 = {(byte) i4, (byte) (i4 >> 8), bCmdToValue};
            int i5 = i + 5;
            byte[] bArr5 = new byte[i5];
            System.arraycopy(bArr4, 0, bArr5, 0, 3);
            System.arraycopy(bArr, 0, bArr5, 3, i);
            System.arraycopy(bArr3, 0, bArr5, 3 + i, 2);
            i2 = i5;
            bArr2 = bArr5;
        } else if (i3 == 3 || i3 == 4) {
            i2 = 5;
            bArr2 = new byte[]{1, 0, bCmdToValue, bArr3[0], bArr3[1]};
        } else {
            if (AppConfig.DEBUG) {
                Log.e(TAG, "otaSendPacket:unknown cmd type");
            }
            return false;
        }
        int i6 = i2;
        while (i6 > 0) {
            int i7 = i6 <= 20 ? i6 : 20;
            byte[] bArr6 = new byte[i7];
            System.arraycopy(bArr2, i2 - i6, bArr6, 0, i7);
            try {
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!otaWrite(bArr6)) {
                return false;
            }
            i6 -= i7;
        }
        return true;
    }

    private int otaSendMetaData(FileInputStream fileInputStream) throws IOException {
        byte[] bArr = new byte[2];
        fileInputStream.read(bArr);
        int i = (short) (((bArr[1] & UByte.MAX_VALUE) << 8) + (bArr[0] & UByte.MAX_VALUE));
        byte[] bArr2 = new byte[i];
        int i2 = fileInputStream.read(bArr2);
        if (i2 < 0) {
            return -1;
        }
        short sCmdToValue = cmdToValue(BleGlobalVariables.OtaCmd.OTA_CMD_META_DATA);
        for (int i3 = 0; i3 < i2; i3++) {
            sCmdToValue = (short) (sCmdToValue + (bArr2[i3] & UByte.MAX_VALUE));
        }
        if (otaSendPacket(BleGlobalVariables.OtaCmd.OTA_CMD_META_DATA, sCmdToValue, bArr2, i)) {
            return i2 + 2;
        }
        return -1;
    }

    private int otaSendBrickData(FileInputStream fileInputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = fileInputStream.read(bArr);
        if (i2 <= 0) {
            if (!AppConfig.DEBUG) {
                return -1;
            }
            Log.w(TAG, "otaSendBrickData:No data read from file");
            return -1;
        }
        if (i2 < i) {
            i = i2;
        }
        short sCmdToValue = cmdToValue(BleGlobalVariables.OtaCmd.OTA_CMD_BRICK_DATA);
        for (int i3 = 0; i3 < i; i3++) {
            sCmdToValue = (short) (sCmdToValue + (bArr[i3] & UByte.MAX_VALUE));
        }
        if (otaSendPacket(BleGlobalVariables.OtaCmd.OTA_CMD_BRICK_DATA, sCmdToValue, bArr, i)) {
            return i2;
        }
        if (!AppConfig.DEBUG) {
            return -2;
        }
        Log.e(TAG, "otaSendBrickData:failed to send packet");
        return -2;
    }

    private boolean otaSendVerifyCmd() {
        return otaSendPacket(BleGlobalVariables.OtaCmd.OTA_CMD_DATA_VERIFY, (short) cmdToValue(BleGlobalVariables.OtaCmd.OTA_CMD_DATA_VERIFY), null, 0) && waitVerifyCmdDone();
    }

    private void otaSendResetCmd() {
        otaSendPacket(BleGlobalVariables.OtaCmd.OTA_CMD_EXECUTION_NEW_CODE, cmdToValue(BleGlobalVariables.OtaCmd.OTA_CMD_EXECUTION_NEW_CODE), null, 0);
    }

    private void releaseSemaphore(Semaphore semaphore) {
        semaphore.release();
    }

    private boolean waitSemaphore(Semaphore semaphore) {
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i >= 12000) {
                return false;
            }
            if (semaphore.tryAcquire()) {
                return true;
            }
            try {
                Thread.sleep(1L);
                if (shouldStopUpdate()) {
                    return false;
                }
                i = i2;
            } catch (InterruptedException e) {
                if (AppConfig.DEBUG) {
                    e.printStackTrace();
                }
                return true;
            }
        }
    }

    private void setOffset(int i) {
        this.mStartOffset = i;
        releaseSemaphore(this.semp);
    }

    private int getOffset() {
        if (waitSemaphore(this.semp)) {
            return this.mStartOffset;
        }
        return -1;
    }

    private void notifyVerifyCmdDone() {
        releaseSemaphore(this.semp);
    }

    private boolean waitVerifyCmdDone() {
        return waitSemaphore(this.semp);
    }

    public void notifyWriteDataCompleted() {
        releaseSemaphore(this.semp);
    }

    private boolean waitWriteDataCompleted() {
        return waitSemaphore(this.semp);
    }

    private void notifyReadDataCompleted() {
        releaseSemaphore(this.semp);
    }

    private boolean waitReadDataCompleted() {
        return waitSemaphore(this.semp);
    }

    private boolean shouldStopUpdate() {
        return this.mShouldStop;
    }

    public void otaGetResult(byte[] bArr) {
        BleGlobalVariables.OtaCmd otaCmdValueToCmd = valueToCmd(bArr[2] & UByte.MAX_VALUE);
        if (otaCmdValueToCmd == null) {
            otaPrintBytes(bArr, "Notify data: ");
            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_RECEIVED_INVALID_PACKET);
            return;
        }
        byte b = bArr[3];
        if (b == 0) {
            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_SUCCESS);
        } else if (b == 1) {
            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_PKT_CHECKSUM_ERROR);
        } else if (b == 2) {
            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_PKT_LEN_ERROR);
        } else if (b == 3) {
            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_DEVICE_NOT_SUPPORT_OTA);
        } else if (b == 4) {
            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_FW_SIZE_ERROR);
        } else if (b == 5) {
            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_FW_VERIFY_ERROR);
        } else {
            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_INVALID_ARGUMENT);
        }
        if (this.mRetValue != BleGlobalVariables.OtaResult.OTA_RESULT_SUCCESS) {
            otaPrintBytes(bArr, "Notify data: ");
            return;
        }
        int i = AnonymousClass2.$SwitchMap$com$cdbwsoft$library$ble$BleGlobalVariables$OtaCmd[otaCmdValueToCmd.ordinal()];
        if (i == 1) {
            setOffset((short) ((bArr[4] & UByte.MAX_VALUE) + ((bArr[5] & UByte.MAX_VALUE) << 8)));
            return;
        }
        if (i == 2) {
            byte b2 = bArr[4];
            byte b3 = bArr[5];
            notifyReadDataCompleted();
        } else {
            if (i == 3) {
                notifyVerifyCmdDone();
                if (AppConfig.DEBUG) {
                    Log.i(TAG, "OTA_CMD_DATA_VERIFY");
                    return;
                }
                return;
            }
            if (i == 4) {
                if (AppConfig.DEBUG) {
                    Log.i(TAG, "This should never happened");
                }
            } else {
                if (AppConfig.DEBUG) {
                    Log.i(TAG, "Exit " + (bArr[2] & UByte.MAX_VALUE));
                }
                serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_INVALID_ARGUMENT);
            }
        }
    }

    private void serErrorCode(BleGlobalVariables.OtaResult otaResult) {
        this.mRetValue = otaResult;
    }

    public BleGlobalVariables.OtaResult otaStart(String str, BleDevice bleDevice) {
        if (!str.isEmpty() && bleDevice != null) {
            this.mFilePath = str;
            this.mBleDevice = bleDevice;
            bleDevice.setOtaListener(this);
            this.mShouldStop = false;
            this.mPercent = 0;
            this.mByteRate = 0;
            this.mElapsedTime = 0;
            this.semp = new Semaphore(0);
            Thread thread = new Thread(this.mUpdateRunnable);
            this.mUpdateThread = thread;
            thread.start();
            return BleGlobalVariables.OtaResult.OTA_RESULT_SUCCESS;
        }
        if (AppConfig.DEBUG) {
            Log.e(TAG, "otaUpdateInit:argument invalid");
        }
        return BleGlobalVariables.OtaResult.OTA_RESULT_INVALID_ARGUMENT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void otaUpdateProcess(String str) {
        try {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.obtainMessage(4, Integer.valueOf(this.mIndex)).sendToTarget();
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            int iAvailable = fileInputStream.available();
            if (iAvailable != 0 && !this.mShouldStop) {
                int iOtaSendMetaData = otaSendMetaData(fileInputStream);
                if (iOtaSendMetaData >= 0 && !this.mShouldStop) {
                    int offset = getOffset();
                    if (offset >= 0 && !this.mShouldStop) {
                        if (offset > 0) {
                            fileInputStream.skip(offset);
                        }
                        int i = iAvailable - iOtaSendMetaData;
                        if (AppConfig.DEBUG) {
                            Log.d(TAG, "offset=" + offset + " meta size " + iOtaSendMetaData);
                        }
                        long timeInMillis = Calendar.getInstance().getTimeInMillis();
                        int i2 = 0;
                        do {
                            int iOtaSendBrickData = otaSendBrickData(fileInputStream, 256);
                            if (iOtaSendBrickData >= 0 && !this.mShouldStop) {
                                if (waitReadDataCompleted() && !this.mShouldStop) {
                                    offset += iOtaSendBrickData;
                                    int i3 = (offset * 100) / iAvailable;
                                    this.mPercent = i3;
                                    Handler handler2 = this.mHandler;
                                    if (handler2 != null) {
                                        handler2.obtainMessage(1, i3, 0, Integer.valueOf(this.mIndex)).sendToTarget();
                                    }
                                    i2 += 256;
                                    long timeInMillis2 = Calendar.getInstance().getTimeInMillis() - timeInMillis;
                                    this.mElapsedTime = (int) (timeInMillis2 / 1000);
                                    this.mByteRate = (int) (((long) (i2 * 1000)) / timeInMillis2);
                                }
                                if (AppConfig.DEBUG) {
                                    Log.e(TAG, "waitReadDataCompleted timeout");
                                }
                                serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_DATA_RESPONSE_TIMEOUT);
                                Handler handler3 = this.mHandler;
                                if (handler3 != null) {
                                    handler3.obtainMessage(3, Integer.valueOf(this.mIndex)).sendToTarget();
                                    return;
                                }
                                return;
                            }
                            fileInputStream.close();
                            if (AppConfig.DEBUG) {
                                Log.e(TAG, "otaUpdateProcess Exit for some transfer issue");
                            }
                            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_DATA_RESPONSE_TIMEOUT);
                            Handler handler4 = this.mHandler;
                            if (handler4 != null) {
                                handler4.obtainMessage(3, Integer.valueOf(this.mIndex)).sendToTarget();
                                return;
                            }
                            return;
                        } while (offset < i);
                        if (otaSendVerifyCmd() && !this.mShouldStop) {
                            this.mPercent = 100;
                            otaSendResetCmd();
                            fileInputStream.close();
                            if (AppConfig.DEBUG) {
                                Log.i(TAG, "otaUpdateProcess Exit");
                            }
                            Handler handler5 = this.mHandler;
                            if (handler5 != null) {
                                handler5.obtainMessage(2, Integer.valueOf(this.mIndex)).sendToTarget();
                            }
                            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_SUCCESS);
                            return;
                        }
                        fileInputStream.close();
                        serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_FW_VERIFY_ERROR);
                        Handler handler6 = this.mHandler;
                        if (handler6 != null) {
                            handler6.obtainMessage(3, Integer.valueOf(this.mIndex)).sendToTarget();
                            return;
                        }
                        return;
                    }
                    if (AppConfig.DEBUG) {
                        Log.e(TAG, "wait cmd OTA_CMD_META_DATA timeout");
                    }
                    fileInputStream.close();
                    serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_META_RESPONSE_TIMEOUT);
                    Handler handler7 = this.mHandler;
                    if (handler7 != null) {
                        handler7.obtainMessage(3, Integer.valueOf(this.mIndex)).sendToTarget();
                        return;
                    }
                    return;
                }
                fileInputStream.close();
                serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_SEND_META_ERROR);
                Handler handler8 = this.mHandler;
                if (handler8 != null) {
                    handler8.obtainMessage(3, Integer.valueOf(this.mIndex)).sendToTarget();
                    return;
                }
                return;
            }
            fileInputStream.close();
            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_FW_SIZE_ERROR);
            Handler handler9 = this.mHandler;
            if (handler9 != null) {
                handler9.obtainMessage(3, Integer.valueOf(this.mIndex)).sendToTarget();
            }
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                Log.e(TAG, "send ota update error", e);
            }
            serErrorCode(BleGlobalVariables.OtaResult.OTA_RESULT_DATA_RESPONSE_TIMEOUT);
            Handler handler10 = this.mHandler;
            if (handler10 != null) {
                handler10.obtainMessage(3, Integer.valueOf(this.mIndex)).sendToTarget();
            }
        }
    }

    public BleGlobalVariables.OtaResult otaGetProcess(int[] iArr) {
        if (iArr.length < 8) {
            if (AppConfig.DEBUG) {
                Log.e(TAG, "buffer is too small,at least 8 intgent");
            }
            return BleGlobalVariables.OtaResult.OTA_RESULT_INVALID_ARGUMENT;
        }
        Arrays.fill(iArr, 0);
        iArr[0] = this.mPercent;
        iArr[1] = this.mByteRate;
        iArr[2] = this.mElapsedTime;
        return this.mRetValue;
    }

    public void otaStop() {
        this.mShouldStop = true;
        Thread thread = this.mUpdateThread;
        if (thread != null) {
            thread.interrupt();
        }
    }
}
