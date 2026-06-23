package com.cdbwsoft.library.base;

/* JADX INFO: loaded from: classes.dex */
public abstract class DataListener {
    protected abstract boolean doReadData(byte[] bArr);

    protected abstract boolean doWriteData(byte[] bArr);

    protected byte[] onDataRead(byte[] bArr) {
        return bArr;
    }

    protected byte[] onDataWrite(byte[] bArr) {
        return bArr;
    }

    protected void onReadComplete(byte[] bArr) {
    }

    protected void onWriteComplete(byte[] bArr) {
    }

    public boolean writeData(byte[] bArr) {
        byte[] bArrOnDataWrite = onDataWrite(bArr);
        if (!doWriteData(bArrOnDataWrite)) {
            return false;
        }
        onWriteComplete(bArrOnDataWrite);
        return true;
    }

    public boolean readData(byte[] bArr) {
        byte[] bArrOnDataRead = onDataRead(bArr);
        if (!doReadData(bArrOnDataRead)) {
            return false;
        }
        onReadComplete(bArrOnDataRead);
        return true;
    }
}
