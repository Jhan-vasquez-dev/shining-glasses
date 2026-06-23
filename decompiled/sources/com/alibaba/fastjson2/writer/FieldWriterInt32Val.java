package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterInt32Val<T> extends FieldWriterInt32<T> {
    FieldWriterInt32Val(String str, int i, long j, String str2, String str3, Field field) {
        super(str, i, j, str2, str3, Integer.TYPE, Integer.TYPE, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return Integer.valueOf(getFieldValueInt(t));
    }

    public int getFieldValueInt(T t) {
        if (t == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            if (this.fieldOffset != -1) {
                return JDKUtils.UNSAFE.getInt(t, this.fieldOffset);
            }
            return this.field.getInt(t);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt32, com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        int fieldValueInt = getFieldValueInt(t);
        if (fieldValueInt == 0 && jSONWriter.isEnabled(JSONWriter.Feature.NotWriteDefaultValue)) {
            return false;
        }
        writeInt32(jSONWriter, fieldValueInt);
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt32, com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        jSONWriter.writeInt32(getFieldValueInt(t));
    }
}
