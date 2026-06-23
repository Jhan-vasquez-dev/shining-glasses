package com.alibaba.fastjson2.support.airlift;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ValueConsumer;
import com.alibaba.fastjson2.util.IOUtils;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SliceValueConsumer implements ValueConsumer {
    public Slice slice;

    @Override // com.alibaba.fastjson2.reader.ValueConsumer
    public void accept(byte[] bArr, int i, int i2) {
        this.slice = Slices.wrappedBuffer(bArr, i, i2);
    }

    @Override // com.alibaba.fastjson2.reader.ValueConsumer
    public void acceptNull() {
        this.slice = null;
    }

    @Override // com.alibaba.fastjson2.reader.ValueConsumer
    public void accept(int i) {
        int iStringSize = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        byte[] bArr = new byte[iStringSize];
        IOUtils.getChars(i, iStringSize, bArr);
        this.slice = Slices.wrappedBuffer(bArr);
    }

    @Override // com.alibaba.fastjson2.reader.ValueConsumer
    public void accept(boolean z) {
        byte[] bArr;
        if (z) {
            bArr = new byte[]{116, 114, 117, 101};
        } else {
            bArr = new byte[]{102, 97, 108, 115, 101};
        }
        this.slice = Slices.wrappedBuffer(bArr);
    }

    @Override // com.alibaba.fastjson2.reader.ValueConsumer
    public void accept(long j) {
        int iStringSize = j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j);
        byte[] bArr = new byte[iStringSize];
        IOUtils.getChars(j, iStringSize, bArr);
        this.slice = Slices.wrappedBuffer(bArr);
    }

    @Override // com.alibaba.fastjson2.reader.ValueConsumer
    public void accept(Number number) {
        if (number == null) {
            this.slice = null;
            return;
        }
        if (number instanceof Long) {
            long jLongValue = number.longValue();
            int iStringSize = jLongValue < 0 ? IOUtils.stringSize(-jLongValue) + 1 : IOUtils.stringSize(jLongValue);
            byte[] bArr = new byte[iStringSize];
            IOUtils.getChars(jLongValue, iStringSize, bArr);
            this.slice = Slices.wrappedBuffer(bArr);
            return;
        }
        if ((number instanceof Integer) || (number instanceof Short) || (number instanceof Byte)) {
            int iIntValue = number.intValue();
            int iStringSize2 = iIntValue < 0 ? IOUtils.stringSize(-iIntValue) + 1 : IOUtils.stringSize(iIntValue);
            byte[] bArr2 = new byte[iStringSize2];
            IOUtils.getChars(iIntValue, iStringSize2, bArr2);
            this.slice = Slices.wrappedBuffer(bArr2);
            return;
        }
        this.slice = Slices.utf8Slice(number.toString());
    }

    @Override // com.alibaba.fastjson2.reader.ValueConsumer
    public void accept(String str) {
        this.slice = Slices.utf8Slice(str);
    }

    @Override // com.alibaba.fastjson2.reader.ValueConsumer
    public void accept(Map map) {
        if (map.isEmpty()) {
            this.slice = Slices.wrappedBuffer(new byte[]{JSONB.Constants.BC_STR_UTF16, JSONB.Constants.BC_STR_UTF16BE});
            return;
        }
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8();
        try {
            jSONWriterOfUTF8.write((Map<?, ?>) map);
            this.slice = Slices.wrappedBuffer(jSONWriterOfUTF8.getBytes());
            if (jSONWriterOfUTF8 != null) {
                jSONWriterOfUTF8.close();
            }
        } catch (Throwable th) {
            if (jSONWriterOfUTF8 != null) {
                try {
                    jSONWriterOfUTF8.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    @Override // com.alibaba.fastjson2.reader.ValueConsumer
    public void accept(List list) {
        if (list.isEmpty()) {
            this.slice = Slices.wrappedBuffer(new byte[]{91, 93});
            return;
        }
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8();
        try {
            jSONWriterOfUTF8.write(list);
            this.slice = Slices.wrappedBuffer(jSONWriterOfUTF8.getBytes());
            if (jSONWriterOfUTF8 != null) {
                jSONWriterOfUTF8.close();
            }
        } catch (Throwable th) {
            if (jSONWriterOfUTF8 != null) {
                try {
                    jSONWriterOfUTF8.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}
