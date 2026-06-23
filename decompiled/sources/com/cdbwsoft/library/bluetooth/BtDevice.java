package com.cdbwsoft.library.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.text.TextUtils;
import android.util.Log;
import com.cdbwsoft.library.AppConfig;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class BtDevice {
    public static final int STATE_CONNECTED = 2563;
    public static final int STATE_CONNECTING = 2562;
    public static final int STATE_IDLE = 2561;
    public static final String TAG = "BtDevice";
    private static final int WRITE_TIMEOUT = 3000;
    private static int mDeviceIndex = 1;
    private String mAlias;
    private BluetoothSocket mBluetoothSocket;
    private BtManager mBtManager;
    private ConnectThread mConnectThread;
    private boolean mDelete;
    private BluetoothDevice mDevice;
    private int mIndex;
    private boolean mMaster;
    private String mName;
    private ReadThread mReadThread;
    private boolean mReading;
    private boolean mSecure;
    private int mState = STATE_IDLE;
    private final Object mLocker = new Object();
    private boolean mRewrite = false;

    public BtDevice(BtManager btManager, BluetoothDevice bluetoothDevice) {
        this.mBtManager = btManager;
        this.mDevice = bluetoothDevice;
        int i = mDeviceIndex;
        mDeviceIndex = i + 1;
        this.mIndex = i;
    }

    public String getAddress() {
        BluetoothDevice bluetoothDevice = this.mDevice;
        return bluetoothDevice != null ? bluetoothDevice.getAddress() : "";
    }

    public void setName(String str) {
        this.mName = str;
    }

    public void setAlias(String str) {
        this.mAlias = str;
    }

    public String getAlias() {
        return this.mAlias;
    }

    void setDelete(boolean z) {
        this.mDelete = z;
    }

    boolean getDelete() {
        return this.mDelete;
    }

    void setMaster(boolean z) {
        this.mMaster = z;
    }

    public boolean isMaster() {
        return this.mMaster;
    }

    public String getName() {
        BluetoothDevice bluetoothDevice = this.mDevice;
        String name = bluetoothDevice != null ? bluetoothDevice.getName() : "";
        if (!TextUtils.isEmpty(name)) {
            return name;
        }
        String str = this.mName;
        return str == null ? "" : str;
    }

    public boolean isConnecting() {
        return this.mState == 2562;
    }

    public boolean isConnected() {
        return this.mState == 2563;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public boolean writeData(byte[] bArr) {
        return writeData(bArr, false);
    }

    public void notifyWrite() {
        notifyWrite(false);
    }

    public void notifyWrite(boolean z) {
        this.mRewrite = z;
        synchronized (this.mLocker) {
            this.mLocker.notify();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0052, code lost:
    
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean writeData(byte[] r11, boolean r12) {
        /*
            r10 = this;
            android.bluetooth.BluetoothSocket r0 = r10.mBluetoothSocket
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            android.bluetooth.BluetoothSocket r0 = r10.mBluetoothSocket     // Catch: java.io.IOException -> L53
            java.io.OutputStream r0 = r0.getOutputStream()     // Catch: java.io.IOException -> L53
            if (r0 == 0) goto L52
            android.bluetooth.BluetoothSocket r2 = r10.mBluetoothSocket     // Catch: java.io.IOException -> L53
            boolean r2 = r2.isConnected()     // Catch: java.io.IOException -> L53
            if (r2 != 0) goto L17
            goto L52
        L17:
            r0.write(r11)     // Catch: java.io.IOException -> L53
            com.cdbwsoft.library.bluetooth.BtManager r0 = r10.mBtManager     // Catch: java.io.IOException -> L53
            if (r0 == 0) goto L21
            r0.onWriteData(r10, r11)     // Catch: java.io.IOException -> L53
        L21:
            r0 = 1
            if (r12 == 0) goto L51
            java.lang.Object r2 = r10.mLocker     // Catch: java.lang.InterruptedException -> L48 java.io.IOException -> L53
            monitor-enter(r2)     // Catch: java.lang.InterruptedException -> L48 java.io.IOException -> L53
            long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L45
            java.lang.Object r5 = r10.mLocker     // Catch: java.lang.Throwable -> L45
            r6 = 3000(0xbb8, double:1.482E-320)
            r5.wait(r6)     // Catch: java.lang.Throwable -> L45
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L45
            long r8 = java.lang.System.currentTimeMillis()     // Catch: java.lang.InterruptedException -> L48 java.io.IOException -> L53
            long r8 = r8 - r3
            int r2 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r2 < 0) goto L3d
            return r1
        L3d:
            boolean r2 = r10.mRewrite     // Catch: java.lang.InterruptedException -> L48 java.io.IOException -> L53
            if (r2 == 0) goto L44
            r10.mRewrite = r1     // Catch: java.lang.InterruptedException -> L48 java.io.IOException -> L53
            goto L6
        L44:
            return r0
        L45:
            r11 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L45
            throw r11     // Catch: java.lang.InterruptedException -> L48 java.io.IOException -> L53
        L48:
            r11 = move-exception
            boolean r12 = com.cdbwsoft.library.AppConfig.DEBUG     // Catch: java.io.IOException -> L53
            if (r12 == 0) goto L50
            r11.printStackTrace()     // Catch: java.io.IOException -> L53
        L50:
            return r1
        L51:
            return r0
        L52:
            return r1
        L53:
            r11 = move-exception
            boolean r12 = com.cdbwsoft.library.AppConfig.DEBUG
            if (r12 == 0) goto L5b
            r11.printStackTrace()
        L5b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdbwsoft.library.bluetooth.BtDevice.writeData(byte[], boolean):boolean");
    }

    void readData() throws IOException {
        InputStream inputStream;
        byte[] bArr;
        BluetoothSocket bluetoothSocket = this.mBluetoothSocket;
        if (bluetoothSocket == null || (inputStream = bluetoothSocket.getInputStream()) == null || !this.mBluetoothSocket.isConnected()) {
            return;
        }
        byte[] bArr2 = new byte[1024];
        int i = inputStream.read(bArr2);
        if (AppConfig.DEBUG) {
            Log.d(TAG, "Read Data size:" + i);
        }
        if (i < 0) {
            bArr = new byte[0];
        } else {
            byte[] bArr3 = new byte[i];
            System.arraycopy(bArr2, 0, bArr3, 0, i);
            bArr = bArr3;
        }
        BtManager btManager = this.mBtManager;
        if (btManager != null) {
            btManager.onReadData(this, bArr);
        }
    }

    class ConnectThread extends Thread {
        ConnectThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            if (AppConfig.DEBUG) {
                Log.d(BtDevice.TAG, "Starting connect secure=" + BtDevice.this.mSecure);
            }
            BtDevice.this.mState = BtDevice.STATE_CONNECTING;
            try {
                if (BtDevice.this.mBluetoothSocket != null && BtDevice.this.mBluetoothSocket.isConnected()) {
                    try {
                        BtDevice.this.mBluetoothSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (AppConfig.DEBUG) {
                    Log.d(BtDevice.TAG, "Creating BluetoothSocket");
                }
                if (BtDevice.this.mSecure) {
                    BtDevice btDevice = BtDevice.this;
                    btDevice.mBluetoothSocket = btDevice.mDevice.createRfcommSocketToServiceRecord(AppConfig.UUID_SECURE);
                } else {
                    BtDevice btDevice2 = BtDevice.this;
                    btDevice2.mBluetoothSocket = btDevice2.mDevice.createInsecureRfcommSocketToServiceRecord(AppConfig.UUID_INSECURE);
                }
                if (BtDevice.this.mBluetoothSocket == null) {
                    BtDevice.this.mState = BtDevice.STATE_IDLE;
                    BtDevice.this.mBtManager.onStateChanged(BtDevice.this.mState, BtDevice.this);
                    if (AppConfig.DEBUG) {
                        Log.d(BtDevice.TAG, "Creating BluetoothSocket failed");
                        return;
                    }
                    return;
                }
                try {
                    BtDevice.this.mBtManager.cancelDiscovery();
                    if (AppConfig.DEBUG) {
                        Log.d(BtDevice.TAG, "Connecting BluetoothSocket");
                    }
                    BtDevice.this.mBluetoothSocket.connect();
                    if (AppConfig.DEBUG) {
                        Log.d(BtDevice.TAG, "Connected BluetoothSocket success");
                    }
                    BtDevice.this.mState = BtDevice.STATE_CONNECTED;
                    BtDevice.this.mBtManager.onStateChanged(BtDevice.this.mState, BtDevice.this);
                    BtDevice.this.startWorking();
                    return;
                } catch (IOException e2) {
                    BtDevice.this.mState = BtDevice.STATE_IDLE;
                    if (AppConfig.DEBUG) {
                        e2.printStackTrace();
                    }
                    try {
                        if (BtDevice.this.mBluetoothSocket != null) {
                            BtDevice.this.mBluetoothSocket.close();
                        }
                    } catch (IOException e3) {
                        if (AppConfig.DEBUG) {
                            Log.e(BtDevice.TAG, "unable to close() mSecure=" + BtDevice.this.mSecure + " socket during connection failure", e3);
                        }
                    }
                    BtDevice.this.mBtManager.onStateChanged(BtDevice.this.mState, BtDevice.this);
                }
            } catch (IOException e4) {
                BtDevice.this.mState = BtDevice.STATE_IDLE;
                if (AppConfig.DEBUG) {
                    Log.e(BtDevice.TAG, "Socket Type: mSecure=" + BtDevice.this.mSecure + ",create() failed", e4);
                }
            }
            BtDevice.this.mBtManager.onStateChanged(BtDevice.this.mState, BtDevice.this);
        }
    }

    class ReadThread extends Thread {
        ReadThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            while (BtDevice.this.mReading && BtDevice.this.mState == 2563) {
                try {
                    BtDevice.this.readData();
                } catch (IOException e) {
                    if (AppConfig.DEBUG) {
                        e.printStackTrace();
                    }
                    BtDevice.this.close();
                    return;
                }
            }
        }
    }

    boolean connect(boolean z) {
        int i = this.mState;
        if (i != 2563 && i != 2562) {
            if (AppConfig.DEBUG) {
                Log.d(TAG, "Do connect secure=" + z);
            }
            this.mState = STATE_CONNECTING;
            this.mSecure = z;
            ConnectThread connectThread = this.mConnectThread;
            if (connectThread != null) {
                connectThread.interrupt();
            }
            ConnectThread connectThread2 = new ConnectThread();
            this.mConnectThread = connectThread2;
            connectThread2.start();
        }
        return true;
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.mDevice;
    }

    void closeInner() {
        this.mReading = false;
        this.mState = STATE_IDLE;
        if (AppConfig.DEBUG) {
            Log.d(TAG, "BtDevice close");
        }
        BluetoothSocket bluetoothSocket = this.mBluetoothSocket;
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
                this.mBluetoothSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ConnectThread connectThread = this.mConnectThread;
        if (connectThread != null) {
            connectThread.interrupt();
        }
        ReadThread readThread = this.mReadThread;
        if (readThread != null) {
            readThread.interrupt();
        }
        BtManager btManager = this.mBtManager;
        if (btManager != null) {
            btManager.onDisconnected(this);
        }
    }

    public void close() {
        if (this.mState == 2561) {
            return;
        }
        this.mBtManager.close(this);
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        if (bluetoothSocket == null) {
            return;
        }
        close();
        this.mBluetoothSocket = bluetoothSocket;
        if (bluetoothSocket.isConnected()) {
            this.mState = STATE_CONNECTED;
        }
        startWorking();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWorking() {
        if (this.mBluetoothSocket == null || this.mState != 2563) {
            return;
        }
        if (AppConfig.DEBUG) {
            Log.d(TAG, "startWorking");
        }
        ReadThread readThread = this.mReadThread;
        if (readThread != null) {
            this.mReading = false;
            readThread.interrupt();
        }
        ReadThread readThread2 = new ReadThread();
        this.mReadThread = readThread2;
        this.mReading = true;
        readThread2.start();
        if (AppConfig.DEBUG) {
            Log.d(TAG, "Start Reading");
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof BluetoothDevice) {
            return obj.equals(this.mDevice);
        }
        if (obj instanceof BtDevice) {
            return ((BtDevice) obj).mDevice.equals(this.mDevice);
        }
        return super.equals(obj);
    }

    public boolean sendOnePacket(byte[] bArr, int i) {
        return sendOnePacket(bArr, i, false);
    }

    public boolean sendOnePacket(byte[] bArr, int i, boolean z) {
        if (bArr.length > 20 || !isConnected()) {
            return false;
        }
        if (AppConfig.DEBUG) {
            Log.d(TAG, "BtDevice sendOneSppPacket");
        }
        boolean zWriteData = writeData(bArr, z);
        printData(bArr);
        if (!zWriteData) {
            return false;
        }
        if (i <= 0) {
            return true;
        }
        try {
            Thread.sleep(i);
            return true;
        } catch (InterruptedException e) {
            if (!AppConfig.DEBUG) {
                return true;
            }
            e.printStackTrace();
            return true;
        }
    }

    private void printData(byte[] bArr) {
        if (AppConfig.DEBUG) {
            String str = "";
            for (byte b : bArr) {
                str = str + String.format("0x%02x,", Byte.valueOf(b));
            }
            Log.d("DataSend:", "data[" + bArr.length + "]:\t" + str);
        }
    }
}
