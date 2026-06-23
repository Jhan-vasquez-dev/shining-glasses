package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
class FieldWriterFloatField<T> extends FieldWriter<T> {
    protected FieldWriterFloatField(String str, int i, long j, String str2, String str3, Field field) {
        super(str, i, j, str2, null, str3, Float.class, Float.class, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        if (obj == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            if (this.fieldOffset != -1 && !this.fieldClass.isPrimitive()) {
                return JDKUtils.UNSAFE.getObject(obj, this.fieldOffset);
            }
            return this.field.get(obj);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        Float f = (Float) getFieldValue(t);
        if (f == null) {
            long features = jSONWriter.getFeatures(this.features);
            if (((JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask) & features) == 0 || (features & JSONWriter.Feature.NotWriteDefaultValue.mask) != 0) {
                return false;
            }
            writeFieldName(jSONWriter);
            jSONWriter.writeDecimalNull();
            return true;
        }
        writeFieldName(jSONWriter);
        float fFloatValue = f.floatValue();
        if (this.decimalFormat != null) {
            jSONWriter.writeFloat(fFloatValue, this.decimalFormat);
        } else {
            jSONWriter.writeFloat(fFloatValue);
        }
        return true;
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
