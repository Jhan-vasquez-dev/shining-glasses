package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterStringMethod<T> extends FieldWriter<T> {
    FieldWriterStringMethod(String str, int i, String str2, String str3, long j, Field field, Method method) {
        super(str, i, j, str2, null, str3, String.class, String.class, field, method);
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
    public void writeValue(JSONWriter jSONWriter, T t) {
        String strTrim = (String) getFieldValue(t);
        if (this.trim && strTrim != null) {
            strTrim = strTrim.trim();
        }
        if (this.symbol && jSONWriter.jsonb) {
            jSONWriter.writeSymbol(strTrim);
        } else if (this.raw) {
            jSONWriter.writeRaw(strTrim);
        } else {
            jSONWriter.writeString(strTrim);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            String strTrim = (String) getFieldValue(t);
            long features = this.features | jSONWriter.getFeatures();
            if (strTrim == null) {
                if (((JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask) & features) == 0) {
                    return false;
                }
            } else if (this.trim) {
                strTrim = strTrim.trim();
            }
            if (strTrim != null && strTrim.isEmpty() && (features & JSONWriter.Feature.IgnoreEmpty.mask) != 0) {
                return false;
            }
            writeString(jSONWriter, strTrim);
            return true;
        } catch (JSONException e) {
            if ((jSONWriter.getFeatures(this.features) | JSONWriter.Feature.IgnoreNonFieldGetter.mask) != 0) {
                return false;
            }
            throw e;
        }
    }
}
