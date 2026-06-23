package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterDoubleValField<T> extends FieldWriter<T> {
    FieldWriterDoubleValField(String str, int i, String str2, String str3, Field field) {
        super(str, i, 0L, str2, null, str3, Double.TYPE, Double.TYPE, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return Double.valueOf(getFieldValueDouble(obj));
    }

    public double getFieldValueDouble(Object obj) {
        if (obj == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            if (this.fieldOffset != -1) {
                return JDKUtils.UNSAFE.getDouble(obj, this.fieldOffset);
            }
            return this.field.getDouble(obj);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        writeDouble(jSONWriter, getFieldValueDouble(t));
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        double fieldValueDouble = getFieldValueDouble(t);
        if (this.decimalFormat != null) {
            jSONWriter.writeDouble(fieldValueDouble, this.decimalFormat);
        } else {
            jSONWriter.writeDouble(fieldValueDouble);
        }
    }
}
