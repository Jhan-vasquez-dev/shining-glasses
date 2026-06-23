package com.cdbwsoft.library.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothSPPService implements Handler.Callback {
    private static final boolean D = true;
    private static final int MESSAGE_DEVICE_NAME = 18;
    private static final int MESSAGE_ERROR = 21;
    private static final int MESSAGE_READ = 19;
    private static final int MESSAGE_STATE_CHANGE = 17;
    private static final int MESSAGE_WRITE = 20;
    private static final String NAME_INSECURE = "BluetoothChatInsecure";
    private static final String NAME_SECURE = "BluetoothChatSecure";
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_NONE = 0;
    private static final String TAG = "BluetoothChatService";
    private BluetoothDeviceListener mBluetoothDeviceListener;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private final Context mContext;
    private Handler mHandler;
    private AcceptThread mInsecureAcceptThread;
    private AcceptThread mSecureAcceptThread;
    private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private int mState = 0;

    public interface BluetoothDeviceListener {
        void onConnected(BluetoothDevice bluetoothDevice);

        void onError(String str);

        void onRead(byte[] bArr);

        void onStateChanged(int i);

        void onWrite(byte[] bArr);
    }

    public BluetoothSPPService(Context context, BluetoothDeviceListener bluetoothDeviceListener) {
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper(), this);
        this.mBluetoothDeviceListener = bluetoothDeviceListener;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message == null) {
            return false;
        }
        switch (message.what) {
            case 17:
                BluetoothDeviceListener bluetoothDeviceListener = this.mBluetoothDeviceListener;
                if (bluetoothDeviceListener != null) {
                    bluetoothDeviceListener.onStateChanged(message.arg1);
                }
                break;
            case 18:
                if (this.mBluetoothDeviceListener != null && message.obj != null && (message.obj instanceof String)) {
                    this.mBluetoothDeviceListener.onConnected(this.mAdapter.getRemoteDevice((String) message.obj));
                    break;
                }
                break;
            case 19:
                BluetoothDeviceListener bluetoothDeviceListener2 = this.mBluetoothDeviceListener;
                if (bluetoothDeviceListener2 != null) {
                    bluetoothDeviceListener2.onRead((byte[]) message.obj);
                }
                break;
            case 20:
                BluetoothDeviceListener bluetoothDeviceListener3 = this.mBluetoothDeviceListener;
                if (bluetoothDeviceListener3 != null) {
                    bluetoothDeviceListener3.onWrite((byte[]) message.obj);
                }
                break;
            case 21:
                BluetoothDeviceListener bluetoothDeviceListener4 = this.mBluetoothDeviceListener;
                if (bluetoothDeviceListener4 != null) {
                    bluetoothDeviceListener4.onError(String.valueOf(message.obj));
                }
                break;
        }
        return false;
    }

    private synchronized void setState(int i) {
        Log.d(TAG, "setState() " + this.mState + " -> " + i);
        this.mState = i;
        this.mHandler.obtainMessage(17, i, 0).sendToTarget();
    }

    public synchronized int getState() {
        return this.mState;
    }

    public synchronized void start() {
        Log.d(TAG, "start");
        ConnectThread connectThread = this.mConnectThread;
        if (connectThread != null) {
            connectThread.cancel();
            this.mConnectThread = null;
        }
        ConnectedThread connectedThread = this.mConnectedThread;
        if (connectedThread != null) {
            connectedThread.cancel();
            this.mConnectedThread = null;
        }
        setState(1);
        if (this.mSecureAcceptThread == null) {
            AcceptThread acceptThread = new AcceptThread(D);
            this.mSecureAcceptThread = acceptThread;
            acceptThread.start();
        }
        if (this.mInsecureAcceptThread == null) {
            AcceptThread acceptThread2 = new AcceptThread(false);
            this.mInsecureAcceptThread = acceptThread2;
            acceptThread2.start();
        }
    }

    public synchronized void connect(BluetoothDevice bluetoothDevice, boolean z) {
        ConnectThread connectThread;
        Log.d(TAG, "connect to: " + bluetoothDevice);
        if (this.mState == 2 && (connectThread = this.mConnectThread) != null) {
            connectThread.cancel();
            this.mConnectThread = null;
        }
        ConnectedThread connectedThread = this.mConnectedThread;
        if (connectedThread != null) {
            connectedThread.cancel();
            this.mConnectedThread = null;
        }
        ConnectThread connectThread2 = new ConnectThread(bluetoothDevice, z);
        this.mConnectThread = connectThread2;
        connectThread2.start();
        setState(2);
    }

    public synchronized void connected(BluetoothSocket bluetoothSocket, BluetoothDevice bluetoothDevice, String str) {
        Log.d(TAG, "connected, Socket Type:" + str);
        ConnectThread connectThread = this.mConnectThread;
        if (connectThread != null) {
            connectThread.cancel();
            this.mConnectThread = null;
        }
        ConnectedThread connectedThread = this.mConnectedThread;
        if (connectedThread != null) {
            connectedThread.cancel();
            this.mConnectedThread = null;
        }
        AcceptThread acceptThread = this.mSecureAcceptThread;
        if (acceptThread != null) {
            acceptThread.cancel();
            this.mSecureAcceptThread = null;
        }
        AcceptThread acceptThread2 = this.mInsecureAcceptThread;
        if (acceptThread2 != null) {
            acceptThread2.cancel();
            this.mInsecureAcceptThread = null;
        }
        ConnectedThread connectedThread2 = new ConnectedThread(bluetoothSocket, str);
        this.mConnectedThread = connectedThread2;
        connectedThread2.start();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(18, bluetoothDevice.getAddress()));
        setState(3);
    }

    public synchronized void stop() {
        Log.d(TAG, "stop");
        ConnectThread connectThread = this.mConnectThread;
        if (connectThread != null) {
            connectThread.cancel();
            this.mConnectThread = null;
        }
        ConnectedThread connectedThread = this.mConnectedThread;
        if (connectedThread != null) {
            connectedThread.cancel();
            this.mConnectedThread = null;
        }
        AcceptThread acceptThread = this.mSecureAcceptThread;
        if (acceptThread != null) {
            acceptThread.cancel();
            this.mSecureAcceptThread = null;
        }
        AcceptThread acceptThread2 = this.mInsecureAcceptThread;
        if (acceptThread2 != null) {
            acceptThread2.cancel();
            this.mInsecureAcceptThread = null;
        }
        setState(0);
    }

    public void write(byte[] bArr) {
        synchronized (this) {
            if (this.mState != 3) {
                return;
            }
            this.mConnectedThread.write(bArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectionFailed() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(21, "无法连接到设备"));
        start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectionLost() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(21, "设备连接已断开"));
        start();
    }

    private class AcceptThread extends Thread {
        private String mSocketType;
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(boolean z) {
            BluetoothServerSocket bluetoothServerSocketListenUsingRfcommWithServiceRecord;
            this.mSocketType = z ? "Secure" : "Insecure";
            try {
                bluetoothServerSocketListenUsingRfcommWithServiceRecord = z ? BluetoothSPPService.this.mAdapter.listenUsingRfcommWithServiceRecord(BluetoothSPPService.NAME_SECURE, BluetoothSPPService.MY_UUID_SECURE) : BluetoothSPPService.this.mAdapter.listenUsingInsecureRfcommWithServiceRecord(BluetoothSPPService.NAME_INSECURE, BluetoothSPPService.MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.e(BluetoothSPPService.TAG, "Socket Type: " + this.mSocketType + "listen() failed", e);
                bluetoothServerSocketListenUsingRfcommWithServiceRecord = null;
            }
            this.mmServerSocket = bluetoothServerSocketListenUsingRfcommWithServiceRecord;
        }

        /* JADX WARN: Removed duplicated region for block: B:32:0x0065 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                r5 = this;
                java.lang.String r0 = "BluetoothChatService"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "Socket Type: "
                r1.<init>(r2)
                java.lang.String r2 = r5.mSocketType
                java.lang.StringBuilder r1 = r1.append(r2)
                java.lang.String r2 = "BEGIN mAcceptThread"
                java.lang.StringBuilder r1 = r1.append(r2)
                java.lang.StringBuilder r1 = r1.append(r5)
                java.lang.String r1 = r1.toString()
                android.util.Log.d(r0, r1)
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                java.lang.String r1 = "AcceptThread"
                r0.<init>(r1)
                java.lang.String r1 = r5.mSocketType
                java.lang.StringBuilder r0 = r0.append(r1)
                java.lang.String r0 = r0.toString()
                r5.setName(r0)
            L34:
                com.cdbwsoft.library.bluetooth.BluetoothSPPService r0 = com.cdbwsoft.library.bluetooth.BluetoothSPPService.this
                int r0 = com.cdbwsoft.library.bluetooth.BluetoothSPPService.access$300(r0)
                r1 = 3
                if (r0 == r1) goto L93
                android.bluetooth.BluetoothServerSocket r0 = r5.mmServerSocket     // Catch: java.io.IOException -> L76
                android.bluetooth.BluetoothSocket r0 = r0.accept()     // Catch: java.io.IOException -> L76
                if (r0 == 0) goto L34
                com.cdbwsoft.library.bluetooth.BluetoothSPPService r2 = com.cdbwsoft.library.bluetooth.BluetoothSPPService.this
                monitor-enter(r2)
                com.cdbwsoft.library.bluetooth.BluetoothSPPService r3 = com.cdbwsoft.library.bluetooth.BluetoothSPPService.this     // Catch: java.lang.Throwable -> L73
                int r3 = com.cdbwsoft.library.bluetooth.BluetoothSPPService.access$300(r3)     // Catch: java.lang.Throwable -> L73
                if (r3 == 0) goto L65
                r4 = 1
                if (r3 == r4) goto L59
                r4 = 2
                if (r3 == r4) goto L59
                if (r3 == r1) goto L65
                goto L71
            L59:
                com.cdbwsoft.library.bluetooth.BluetoothSPPService r1 = com.cdbwsoft.library.bluetooth.BluetoothSPPService.this     // Catch: java.lang.Throwable -> L73
                android.bluetooth.BluetoothDevice r3 = r0.getRemoteDevice()     // Catch: java.lang.Throwable -> L73
                java.lang.String r4 = r5.mSocketType     // Catch: java.lang.Throwable -> L73
                r1.connected(r0, r3, r4)     // Catch: java.lang.Throwable -> L73
                goto L71
            L65:
                r0.close()     // Catch: java.io.IOException -> L69 java.lang.Throwable -> L73
                goto L71
            L69:
                r0 = move-exception
                java.lang.String r1 = "BluetoothChatService"
                java.lang.String r3 = "Could not close unwanted socket"
                android.util.Log.e(r1, r3, r0)     // Catch: java.lang.Throwable -> L73
            L71:
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L73
                goto L34
            L73:
                r0 = move-exception
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L73
                throw r0
            L76:
                r0 = move-exception
                java.lang.String r1 = "BluetoothChatService"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = "Socket Type: "
                r2.<init>(r3)
                java.lang.String r3 = r5.mSocketType
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.String r3 = "accept() failed"
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.String r2 = r2.toString()
                android.util.Log.e(r1, r2, r0)
            L93:
                java.lang.String r0 = "BluetoothChatService"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "END mAcceptThread, socket Type: "
                r1.<init>(r2)
                java.lang.String r2 = r5.mSocketType
                java.lang.StringBuilder r1 = r1.append(r2)
                java.lang.String r1 = r1.toString()
                android.util.Log.i(r0, r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.cdbwsoft.library.bluetooth.BluetoothSPPService.AcceptThread.run():void");
        }

        public void cancel() {
            Log.d(BluetoothSPPService.TAG, "Socket Type" + this.mSocketType + "cancel " + this);
            try {
                this.mmServerSocket.close();
            } catch (IOException e) {
                Log.e(BluetoothSPPService.TAG, "Socket Type" + this.mSocketType + "close() of server failed", e);
            }
        }
    }

    private class ConnectThread extends Thread {
        private String mSocketType;
        private final BluetoothDevice mmDevice;
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice bluetoothDevice, boolean z) {
            BluetoothSocket bluetoothSocketCreateRfcommSocketToServiceRecord;
            this.mmDevice = bluetoothDevice;
            this.mSocketType = z ? "Secure" : "Insecure";
            try {
                bluetoothSocketCreateRfcommSocketToServiceRecord = z ? bluetoothDevice.createRfcommSocketToServiceRecord(BluetoothSPPService.MY_UUID_SECURE) : bluetoothDevice.createInsecureRfcommSocketToServiceRecord(BluetoothSPPService.MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.e(BluetoothSPPService.TAG, "Socket Type: " + this.mSocketType + "create() failed", e);
                bluetoothSocketCreateRfcommSocketToServiceRecord = null;
            }
            this.mmSocket = bluetoothSocketCreateRfcommSocketToServiceRecord;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Log.i(BluetoothSPPService.TAG, "BEGIN mConnectThread SocketType:" + this.mSocketType);
            setName("ConnectThread" + this.mSocketType);
            BluetoothSPPService.this.mAdapter.cancelDiscovery();
            try {
                try {
                    this.mmSocket.connect();
                    synchronized (BluetoothSPPService.this) {
                        BluetoothSPPService.this.mConnectThread = null;
                    }
                    BluetoothSPPService.this.connected(this.mmSocket, this.mmDevice, this.mSocketType);
                } catch (IOException e) {
                    Log.e(BluetoothSPPService.TAG, "unable to close() " + this.mSocketType + " socket during connection failure", e);
                    BluetoothSPPService.this.connectionFailed();
                }
            } catch (IOException unused) {
                this.mmSocket.close();
                BluetoothSPPService.this.connectionFailed();
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) {
                Log.e(BluetoothSPPService.TAG, "close() of connect " + this.mSocketType + " socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket mmSocket;

        public ConnectedThread(BluetoothSocket bluetoothSocket, String str) {
            InputStream inputStream;
            Log.d(BluetoothSPPService.TAG, "create ConnectedThread: " + str);
            this.mmSocket = bluetoothSocket;
            OutputStream outputStream = null;
            try {
                inputStream = bluetoothSocket.getInputStream();
            } catch (IOException e) {
                e = e;
                inputStream = null;
            }
            try {
                outputStream = bluetoothSocket.getOutputStream();
            } catch (IOException e2) {
                e = e2;
                Log.e(BluetoothSPPService.TAG, "temp sockets not created", e);
            }
            this.mmInStream = inputStream;
            this.mmOutStream = outputStream;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            byte[] bArr;
            Log.i(BluetoothSPPService.TAG, "BEGIN mConnectedThread");
            byte[] bArr2 = new byte[1024];
            while (true) {
                try {
                    int i = this.mmInStream.read(bArr2);
                    if (i < 0) {
                        bArr = new byte[0];
                    } else {
                        byte[] bArr3 = new byte[i];
                        System.arraycopy(bArr2, 0, bArr3, 0, i);
                        bArr = bArr3;
                    }
                    BluetoothSPPService.this.mHandler.obtainMessage(19, bArr).sendToTarget();
                } catch (IOException e) {
                    Log.e(BluetoothSPPService.TAG, "disconnected", e);
                    BluetoothSPPService.this.connectionLost();
                    BluetoothSPPService.this.start();
                    return;
                }
            }
        }

        public void write(byte[] bArr) {
            try {
                this.mmOutStream.write(bArr);
                BluetoothSPPService.this.mHandler.obtainMessage(20, bArr).sendToTarget();
            } catch (IOException e) {
                Log.e(BluetoothSPPService.TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) {
                Log.e(BluetoothSPPService.TAG, "close() of connect socket failed", e);
            }
        }
    }
}
