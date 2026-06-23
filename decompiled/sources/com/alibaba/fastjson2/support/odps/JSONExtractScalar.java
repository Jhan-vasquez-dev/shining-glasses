package com.alibaba.fastjson2.support.odps;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ValueConsumer;
import com.alibaba.fastjson2.util.IOUtils;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.Writable;
import com.aliyun.odps.udf.UDF;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class JSONExtractScalar extends UDF {
    final JSONPath path;
    JSONWritable text = new JSONWritable();
    ExtractValueConsumer valueConsumer = new ExtractValueConsumer();
    static final byte[] BYTES_TRUE = "true".getBytes(StandardCharsets.UTF_8);
    static final byte[] BYTES_FALSE = "false".getBytes(StandardCharsets.UTF_8);
    static final byte[] BYTES_NULL = "null".getBytes(StandardCharsets.UTF_8);

    public JSONExtractScalar(String str) {
        this.path = JSONPath.of(str);
    }

    public Writable eval(Text text) {
        this.path.extractScalar(JSONReader.of(text.getBytes(), 0, text.getLength(), StandardCharsets.UTF_8), this.valueConsumer);
        return this.text;
    }

    class ExtractValueConsumer implements ValueConsumer {
        ExtractValueConsumer() {
        }

        @Override // com.alibaba.fastjson2.reader.ValueConsumer
        public void accept(byte[] bArr, int i, int i2) {
            JSONExtractScalar.this.text.bytes = bArr;
            JSONExtractScalar.this.text.off = i;
            JSONExtractScalar.this.text.length = i2;
        }

        @Override // com.alibaba.fastjson2.reader.ValueConsumer
        public void acceptNull() {
            JSONExtractScalar.this.text.set(JSONExtractScalar.BYTES_NULL);
        }

        @Override // com.alibaba.fastjson2.reader.ValueConsumer
        public void accept(boolean z) {
            JSONExtractScalar.this.text.set(z ? JSONExtractScalar.BYTES_TRUE : JSONExtractScalar.BYTES_FALSE);
        }

        @Override // com.alibaba.fastjson2.reader.ValueConsumer
        public void accept(int i) {
            int iStringSize = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
            JSONExtractScalar.this.text.setCapacity(iStringSize, false);
            IOUtils.getChars(i, iStringSize, JSONExtractScalar.this.text.bytes);
            JSONExtractScalar.this.text.length = iStringSize;
        }

        @Override // com.alibaba.fastjson2.reader.ValueConsumer
        public void accept(long j) {
            int iStringSize = j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j);
            JSONExtractScalar.this.text.setCapacity(iStringSize, false);
            IOUtils.getChars(j, iStringSize, JSONExtractScalar.this.text.bytes);
            JSONExtractScalar.this.text.length = iStringSize;
        }

        @Override // com.alibaba.fastjson2.reader.ValueConsumer
        public void accept(Number number) {
            if (number instanceof Integer) {
                accept(number.intValue());
            } else if (number instanceof Long) {
                accept(number.longValue());
            } else {
                JSONExtractScalar.this.text.set(number.toString());
            }
        }

        @Override // com.alibaba.fastjson2.reader.ValueConsumer
        public void accept(String str) {
            JSONExtractScalar.this.text.set(str);
        }

        @Override // com.alibaba.fastjson2.reader.ValueConsumer
        public void accept(Map map) {
            JSONExtractScalar.this.text.set(JSON.toJSONBytes(map));
        }

        @Override // com.alibaba.fastjson2.reader.ValueConsumer
        public void accept(List list) {
            JSONExtractScalar.this.text.set(JSON.toJSONBytes(list));
        }
    }
}
