package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterInt8ValField<T> extends FieldWriterInt8<T> {
    FieldWriterInt8ValField(String str, int i, long j, String str2, String str3, Field field) {
        super(str, i, j, str2, str3, Byte.TYPE, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return Byte.valueOf(getFieldValueByte(t));
    }

    public byte getFieldValueByte(T t) {
        if (t == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            if (this.fieldOffset != -1) {
                return JDKUtils.UNSAFE.getByte(t, this.fieldOffset);
            }
            return this.field.getByte(t);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt8, com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        writeInt8(jSONWriter, getFieldValueByte(t));
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterInt8, com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        jSONWriter.writeInt32(getFieldValueByte(t));
    }
}
