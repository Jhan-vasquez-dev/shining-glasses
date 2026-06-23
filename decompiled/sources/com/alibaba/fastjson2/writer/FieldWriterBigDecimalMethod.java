package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterBigDecimalMethod<T> extends FieldWriter<T> {
    FieldWriterBigDecimalMethod(String str, int i, long j, String str2, String str3, Field field, Method method) {
        super(str, i, j, str2, null, str3, BigDecimal.class, BigDecimal.class, null, method);
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
        jSONWriter.writeDecimal((BigDecimal) getFieldValue(t), this.features, this.decimalFormat);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            BigDecimal bigDecimal = (BigDecimal) getFieldValue(t);
            if (bigDecimal == null && ((this.features | jSONWriter.getFeatures()) & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask)) == 0) {
                return false;
            }
            writeFieldName(jSONWriter);
            jSONWriter.writeDecimal(bigDecimal, this.features, this.decimalFormat);
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }
}
