package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderDoubleValueMethod<T> extends FieldReaderObject<T> {
    FieldReaderDoubleValueMethod(String str, Type type, Class cls, int i, long j, String str2, Locale locale, Double d, JSONSchema jSONSchema, Method method) {
        super(str, type, cls, i, j, str2, locale, d, jSONSchema, method, null, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        double doubleValue = jSONReader.readDoubleValue();
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        try {
            this.method.invoke(t, Double.valueOf(doubleValue));
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValueJSONB(JSONReader jSONReader, T t) {
        double doubleValue = jSONReader.readDoubleValue();
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        try {
            this.method.invoke(t, Double.valueOf(doubleValue));
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        double doubleValue = TypeUtils.toDoubleValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        try {
            this.method.invoke(t, Double.valueOf(doubleValue));
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, int i) {
        if (this.schema != null) {
            this.schema.assertValidate(i);
        }
        try {
            this.method.invoke(t, Double.valueOf(i));
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
