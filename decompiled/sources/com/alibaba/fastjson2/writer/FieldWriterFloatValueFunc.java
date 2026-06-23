package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.function.ToFloatFunction;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterFloatValueFunc extends FieldWriter {
    final ToFloatFunction function;

    FieldWriterFloatValueFunc(String str, int i, long j, String str2, String str3, Field field, Method method, ToFloatFunction toFloatFunction) {
        super(str, i, j, str2, null, str3, Float.TYPE, Float.TYPE, field, method);
        this.function = toFloatFunction;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return Float.valueOf(this.function.applyAsFloat(obj));
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, Object obj) {
        float fApplyAsFloat = this.function.applyAsFloat(obj);
        if (this.decimalFormat != null) {
            jSONWriter.writeDouble(fApplyAsFloat, this.decimalFormat);
        } else {
            jSONWriter.writeDouble(fApplyAsFloat);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, Object obj) {
        try {
            float fApplyAsFloat = this.function.applyAsFloat(obj);
            writeFieldName(jSONWriter);
            if (this.decimalFormat != null) {
                jSONWriter.writeFloat(fApplyAsFloat, this.decimalFormat);
                return true;
            }
            jSONWriter.writeFloat(fApplyAsFloat);
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }
}
