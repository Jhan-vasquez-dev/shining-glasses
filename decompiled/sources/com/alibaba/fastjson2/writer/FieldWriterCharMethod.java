package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterCharMethod<T> extends FieldWriter<T> {
    FieldWriterCharMethod(String str, int i, long j, String str2, String str3, Field field, Method method, Class cls) {
        super(str, i, j, str2, null, str3, cls, cls, field, method);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        try {
            return this.method.invoke(t, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new JSONException("invoke getter method error, " + this.fieldName, e);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        Character ch = (Character) getFieldValue(t);
        if (ch == null) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeChar(ch.charValue());
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        Character ch = (Character) getFieldValue(t);
        if (ch != null) {
            writeFieldName(jSONWriter);
            jSONWriter.writeChar(ch.charValue());
            return true;
        }
        if (((jSONWriter.context.getFeatures() | this.features) & JSONWriter.Feature.WriteNulls.mask) == 0) {
            return false;
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeNull();
        return true;
    }
}
