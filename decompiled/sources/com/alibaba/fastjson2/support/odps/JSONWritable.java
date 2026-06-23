package com.alibaba.fastjson2.support.odps;

import com.aliyun.odps.io.Writable;
import com.aliyun.odps.io.WritableUtils;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
public class JSONWritable implements Writable {
    private static final byte[] EMPTY_BYTES = new byte[0];
    byte[] bytes;
    int length;
    int off;

    public JSONWritable() {
        this.bytes = EMPTY_BYTES;
    }

    public JSONWritable(byte[] bArr) {
        this.bytes = bArr;
        this.off = 0;
        this.length = bArr.length;
    }

    public void write(DataOutput dataOutput) throws IOException {
        WritableUtils.writeVInt(dataOutput, this.length);
        dataOutput.write(this.bytes, this.off, this.length);
    }

    public void readFields(DataInput dataInput) throws IOException {
        int vInt = WritableUtils.readVInt(dataInput);
        setCapacity(vInt, false);
        dataInput.readFully(this.bytes, 0, vInt);
        this.length = vInt;
    }

    void setCapacity(int i, boolean z) {
        byte[] bArr = this.bytes;
        if (bArr == null || bArr.length < i) {
            byte[] bArr2 = new byte[i];
            if (bArr != null && z) {
                System.arraycopy(bArr, 0, bArr2, 0, this.length);
            }
            this.bytes = bArr2;
        }
    }

    public void set(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        this.bytes = bytes;
        this.length = bytes.length;
    }

    public void set(byte[] bArr) {
        set(bArr, 0, bArr.length);
    }

    public void set(byte[] bArr, int i, int i2) {
        setCapacity(i2, false);
        System.arraycopy(bArr, i, this.bytes, 0, i2);
        this.length = i2;
    }

    public String toString() {
        return new String(this.bytes, this.off, this.length, StandardCharsets.UTF_8);
    }
}
