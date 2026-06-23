package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.function.ToByteFunction;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterInt8ValFunc extends FieldWriterInt8 {
    final ToByteFunction function;

    FieldWriterInt8ValFunc(String str, int i, long j, String str2, String str3, Field field, Method method, ToByteFunction toByteFunction) {
        super(str, i, j, str2, str3, Byte.TYPE, field, method);
        this.function = toByteFunction;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return Byte.valueOf(this.function.applyAsByte(obj));
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt8, com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, Object obj) {
        jSONWriter.writeInt32(this.function.applyAsByte(obj));
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt8, com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, Object obj) {
        try {
            writeInt8(jSONWriter, this.function.applyAsByte(obj));
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }
}
