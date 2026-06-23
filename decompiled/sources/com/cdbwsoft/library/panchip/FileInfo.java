package com.cdbwsoft.library.panchip;

import java.util.Arrays;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public class FileInfo extends VersionInfo {
    public int codeSize;
    public byte[] codeSizeRaw;
    public byte[] crc32;
    public byte type;

    FileInfo(byte[] bArr) throws Exception {
        super(Arrays.copyOfRange(bArr, 8, 14));
        if (bArr.length < 16) {
            throw new Exception("buf length is less than 16");
        }
        this.codeSizeRaw = Arrays.copyOfRange(bArr, 0, 4);
        this.crc32 = Arrays.copyOfRange(bArr, 4, 8);
        byte[] bArr2 = this.codeSizeRaw;
        this.codeSize = (bArr2[0] & UByte.MAX_VALUE) + ((bArr2[1] & UByte.MAX_VALUE) << 8) + ((bArr2[2] & UByte.MAX_VALUE) << 16) + ((bArr2[3] & UByte.MAX_VALUE) << 24);
        this.type = bArr[14];
    }

    public byte[] createSizePacket() {
        return DataOperate.bytesConcat(new byte[]{2, this.type}, this.codeSizeRaw);
    }

    public byte[] createCrcPacket() {
        return DataOperate.bytesConcat(new byte[]{3}, this.crc32);
    }

    public byte[] createResetPacket() {
        return DataOperate.bytesConcat(new byte[]{4}, this.crc32);
    }
}
