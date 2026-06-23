package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.ToDoubleFunction;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterDoubleValueFunc extends FieldWriter {
    final ToDoubleFunction function;

    FieldWriterDoubleValueFunc(String str, int i, long j, String str2, String str3, Field field, Method method, ToDoubleFunction toDoubleFunction) {
        super(str, i, j, str2, null, str3, Double.TYPE, Double.TYPE, field, method);
        this.function = toDoubleFunction;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return Double.valueOf(this.function.applyAsDouble(obj));
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, Object obj) {
        double dApplyAsDouble = this.function.applyAsDouble(obj);
        if (this.decimalFormat != null) {
            jSONWriter.writeDouble(dApplyAsDouble, this.decimalFormat);
        } else {
            jSONWriter.writeDouble(dApplyAsDouble);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, Object obj) {
        try {
            double dApplyAsDouble = this.function.applyAsDouble(obj);
            writeFieldName(jSONWriter);
            if (this.decimalFormat != null) {
                jSONWriter.writeDouble(dApplyAsDouble, this.decimalFormat);
                return true;
            }
            jSONWriter.writeDouble(dApplyAsDouble);
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }
}
