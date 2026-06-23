package com.alibaba.fastjson2.support.odps;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.support.odps.JSONExtractScalar;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.Writable;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
public class JSONExtract extends JSONExtractScalar {
    public JSONExtract(String str) {
        super(str);
        this.valueConsumer = new ExtractValueConsumer();
    }

    @Override // com.alibaba.fastjson2.support.odps.JSONExtractScalar
    public Writable eval(Text text) {
        this.path.extract(JSONReader.of(text.getBytes(), 0, text.getLength(), StandardCharsets.UTF_8), this.valueConsumer);
        return this.text;
    }

    class ExtractValueConsumer extends JSONExtractScalar.ExtractValueConsumer {
        ExtractValueConsumer() {
            super();
        }

        @Override // com.alibaba.fastjson2.support.odps.JSONExtractScalar.ExtractValueConsumer, com.alibaba.fastjson2.reader.ValueConsumer
        public void accept(byte[] bArr, int i, int i2) {
            int i3;
            if (i > 0 && (i3 = i + i2) < bArr.length) {
                int i4 = i - 1;
                byte b = bArr[i4];
                byte b2 = bArr[i3];
                if (b == b2 && (b2 == 34 || b2 == 39)) {
                    JSONExtract.this.text.bytes = bArr;
                    JSONExtract.this.text.off = i4;
                    JSONExtract.this.text.length = i2 + 2;
                    return;
                }
            }
            JSONExtract.this.text.bytes = bArr;
            JSONExtract.this.text.off = i;
            JSONExtract.this.text.length = i2;
        }

        @Override // com.alibaba.fastjson2.support.odps.JSONExtractScalar.ExtractValueConsumer, com.alibaba.fastjson2.reader.ValueConsumer
        public void accept(String str) {
            int length = str.length();
            byte[] bArr = new byte[length + 2];
            bArr[0] = 34;
            bArr[length + 1] = 34;
            str.getBytes(0, str.length(), bArr, 1);
            JSONExtract.this.text.set(bArr);
        }
    }
}
