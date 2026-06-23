package com.cdbwsoft.library.panchip;

import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public class VersionInfo {
    public int app;
    public int dev;
    public int pro;

    VersionInfo(byte[] bArr) throws Exception {
        if (bArr.length < 4) {
            throw new Exception("buf length is less than 4");
        }
        int i = (bArr[0] & UByte.MAX_VALUE) + ((bArr[1] & UByte.MAX_VALUE) << 8);
        this.app = i;
        int i2 = (bArr[2] & UByte.MAX_VALUE) + ((bArr[3] & UByte.MAX_VALUE) << 8);
        this.dev = i2;
        int i3 = (bArr[4] & UByte.MAX_VALUE) + ((bArr[5] & UByte.MAX_VALUE) << 8);
        this.pro = i3;
        if (65535 == i && 65535 == i2 && 65535 == i3) {
            this.dev = 0;
            this.app = 0;
            this.pro = 0;
        }
    }

    byte[] toBytes() {
        int i = this.app;
        int i2 = this.dev;
        int i3 = this.pro;
        return new byte[]{(byte) i, (byte) (i >> 8), (byte) i2, (byte) (i2 >> 8), (byte) i3, (byte) (i3 >> 8), 0, 0};
    }

    byte[] createVersionPacket() {
        return DataOperate.bytesConcat(new byte[]{1}, toBytes());
    }

    VersionInfo copy() {
        try {
            return new VersionInfo(toBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
