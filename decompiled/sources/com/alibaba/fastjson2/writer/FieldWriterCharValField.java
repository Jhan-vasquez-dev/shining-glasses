package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterCharValField<T> extends FieldWriter<T> {
    FieldWriterCharValField(String str, int i, long j, String str2, String str3, Field field) {
        super(str, i, j, str2, null, str3, Character.TYPE, Character.TYPE, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return Character.valueOf(getFieldValueChar(obj));
    }

    public char getFieldValueChar(Object obj) {
        if (obj == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            if (this.fieldOffset != -1) {
                return JDKUtils.UNSAFE.getChar(obj, this.fieldOffset);
            }
            return this.field.getChar(obj);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        char fieldValueChar = getFieldValueChar(t);
        writeFieldName(jSONWriter);
        jSONWriter.writeChar(fieldValueChar);
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, Object obj) {
        jSONWriter.writeChar(getFieldValueChar(obj));
    }
}
