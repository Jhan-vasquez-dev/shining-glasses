package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
class FieldWriterFloatMethod<T> extends FieldWriter<T> {
    protected FieldWriterFloatMethod(String str, int i, long j, String str2, String str3, Type type, Class cls, Field field, Method method) {
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
            Float f = (Float) getFieldValue(t);
            if (f == null) {
                long features = jSONWriter.getFeatures(this.features);
                if ((JSONWriter.Feature.WriteNulls.mask & features) == 0 || (features & JSONWriter.Feature.NotWriteDefaultValue.mask) != 0) {
                    return false;
                }
                writeFieldName(jSONWriter);
                jSONWriter.writeNumberNull();
                return true;
            }
            writeFieldName(jSONWriter);
            float fFloatValue = f.floatValue();
            if (this.decimalFormat != null) {
                jSONWriter.writeFloat(fFloatValue, this.decimalFormat);
            } else if ((this.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0) {
                jSONWriter.writeString(fFloatValue);
            } else {
                jSONWriter.writeFloat(fFloatValue);
            }
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
        Float f = (Float) getFieldValue(t);
        if (f == null) {
            jSONWriter.writeNumberNull();
            return;
        }
        float fFloatValue = f.floatValue();
        if (this.decimalFormat != null) {
            jSONWriter.writeFloat(fFloatValue, this.decimalFormat);
        } else {
            jSONWriter.writeFloat(fFloatValue);
        }
    }
}
