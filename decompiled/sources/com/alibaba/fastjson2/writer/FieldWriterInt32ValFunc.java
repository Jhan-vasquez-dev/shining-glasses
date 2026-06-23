package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.ToIntFunction;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterInt32ValFunc extends FieldWriterInt32 {
    final ToIntFunction function;

    FieldWriterInt32ValFunc(String str, int i, long j, String str2, String str3, Field field, Method method, ToIntFunction toIntFunction) {
        super(str, i, j, str2, str3, Integer.TYPE, Integer.TYPE, field, method);
        this.function = toIntFunction;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return Integer.valueOf(this.function.applyAsInt(obj));
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt32, com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, Object obj) {
        try {
            writeInt32(jSONWriter, this.function.applyAsInt(obj));
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt32, com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, Object obj) {
        jSONWriter.writeInt32(this.function.applyAsInt(obj));
    }
}
