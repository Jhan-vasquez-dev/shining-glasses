package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderFloatMethod<T> extends FieldReaderObject<T> {
    FieldReaderFloatMethod(String str, int i, long j, String str2, Locale locale, Float f, JSONSchema jSONSchema, Method method) {
        super(str, Float.class, Float.class, i, j, str2, locale, f, jSONSchema, method, null, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        Float f = jSONReader.readFloat();
        if (this.schema != null) {
            this.schema.assertValidate(f);
        }
        try {
            this.method.invoke(t, f);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Float f = TypeUtils.toFloat(obj);
        if (this.schema != null) {
            this.schema.assertValidate(f);
        }
        try {
            this.method.invoke(t, f);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
