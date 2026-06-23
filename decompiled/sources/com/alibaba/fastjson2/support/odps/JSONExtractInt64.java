package com.alibaba.fastjson2.support.odps;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.aliyun.odps.io.LongWritable;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.udf.UDF;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: classes.dex */
public class JSONExtractInt64 extends UDF {
    private final JSONPath path;
    private final LongWritable result = new LongWritable();

    public JSONExtractInt64(String str) {
        this.path = JSONPath.of(str);
    }

    public LongWritable eval(Text text) {
        JSONReader jSONReaderOf = JSONReader.of(text.getBytes(), 0, text.getLength(), StandardCharsets.UTF_8);
        long jExtractInt64Value = this.path.extractInt64Value(jSONReaderOf);
        if (jSONReaderOf.wasNull()) {
            return null;
        }
        this.result.set(jExtractInt64Value);
        return this.result;
    }
}
