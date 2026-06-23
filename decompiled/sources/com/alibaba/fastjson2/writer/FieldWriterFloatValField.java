package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterFloatValField<T> extends FieldWriter<T> {
    FieldWriterFloatValField(String str, int i, long j, String str2, String str3, Field field) {
        super(str, i, j, str2, null, str3, Float.TYPE, Float.TYPE, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return Float.valueOf(getFieldValueFloat(t));
    }

    public float getFieldValueFloat(T t) {
        if (t == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            if (this.fieldOffset != -1) {
                return JDKUtils.UNSAFE.getFloat(t, this.fieldOffset);
            }
            return this.field.getFloat(t);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        writeFloat(jSONWriter, getFieldValueFloat(t));
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        float fieldValueFloat = getFieldValueFloat(t);
        if (this.decimalFormat != null) {
            jSONWriter.writeFloat(fieldValueFloat, this.decimalFormat);
        } else {
            jSONWriter.writeFloat(fieldValueFloat);
        }
    }
}
