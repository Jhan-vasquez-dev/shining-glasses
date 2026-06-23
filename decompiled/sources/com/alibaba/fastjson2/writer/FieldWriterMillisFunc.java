package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.ToLongFunction;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterMillisFunc<T> extends FieldWriterDate<T> {
    final ToLongFunction function;

    FieldWriterMillisFunc(String str, int i, long j, String str2, String str3, Field field, Method method, ToLongFunction toLongFunction) {
        super(str, i, j, str2, str3, Long.TYPE, Long.TYPE, field, method);
        this.function = toLongFunction;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return Long.valueOf(this.function.applyAsLong(t));
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        long jApplyAsLong = this.function.applyAsLong(t);
        if (jApplyAsLong != 0) {
            writeDate(jSONWriter, jApplyAsLong);
            return true;
        }
        if (((this.features | jSONWriter.getFeatures()) & JSONWriter.Feature.WriteNulls.mask) == 0) {
            return false;
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeNull();
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        writeDate(jSONWriter, false, this.function.applyAsLong(t));
    }
}
