package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.function.ToShortFunction;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterInt16ValFunc extends FieldWriterInt16 {
    final ToShortFunction function;

    FieldWriterInt16ValFunc(String str, int i, long j, String str2, String str3, Field field, Method method, ToShortFunction toShortFunction) {
        super(str, i, j, str2, str3, Short.TYPE, field, method);
        this.function = toShortFunction;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt16, com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, Object obj) {
        jSONWriter.writeInt32(this.function.applyAsShort(obj));
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return Short.valueOf(this.function.applyAsShort(obj));
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt16, com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, Object obj) {
        try {
            writeInt16(jSONWriter, this.function.applyAsShort(obj));
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }
}
