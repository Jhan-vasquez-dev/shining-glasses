package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
class FieldWriterDoubleMethod<T> extends FieldWriter<T> {
    protected FieldWriterDoubleMethod(String str, int i, long j, String str2, String str3, Type type, Class cls, Field field, Method method) {
        super(str, i, j, str2, null, str3, type, cls, field, method);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        try {
            return this.method.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new JSONException("invoke getter method error, " + this.fieldName, e);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            Double d = (Double) getFieldValue(t);
            writeFieldName(jSONWriter);
            if (d == null) {
                jSONWriter.writeNumberNull();
                return true;
            }
            double dDoubleValue = d.doubleValue();
            if (this.decimalFormat != null) {
                jSONWriter.writeDouble(dDoubleValue, this.decimalFormat);
                return true;
            }
            if ((this.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0) {
                jSONWriter.writeString(dDoubleValue);
                return true;
            }
            jSONWriter.writeDouble(dDoubleValue);
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        Double d = (Double) getFieldValue(t);
        if (d == null) {
            jSONWriter.writeNumberNull();
            return;
        }
        double dDoubleValue = d.doubleValue();
        if (this.decimalFormat != null) {
            jSONWriter.writeDouble(dDoubleValue, this.decimalFormat);
        } else {
            jSONWriter.writeDouble(dDoubleValue);
        }
    }
}
