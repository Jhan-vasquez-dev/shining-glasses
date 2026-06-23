package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.ToLongFunction;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterInt64ValFunc<T> extends FieldWriterInt64<T> {
    final ToLongFunction function;

    FieldWriterInt64ValFunc(String str, int i, long j, String str2, String str3, Field field, Method method, ToLongFunction toLongFunction) {
        super(str, i, j, str2, str3, Long.TYPE, field, method);
        this.function = toLongFunction;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return Long.valueOf(this.function.applyAsLong(t));
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt64, com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            writeInt64(jSONWriter, this.function.applyAsLong(t));
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt64, com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        jSONWriter.writeInt64(this.function.applyAsLong(t));
    }
}
