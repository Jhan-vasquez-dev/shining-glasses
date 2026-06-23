package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt64ValueMethod<T> extends FieldReaderObject<T> {
    FieldReaderInt64ValueMethod(String str, Type type, Class cls, int i, long j, String str2, Locale locale, Long l, JSONSchema jSONSchema, Method method) {
        super(str, type, cls, i, j, str2, locale, l, jSONSchema, method, null, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        long int64Value = jSONReader.readInt64Value();
        if (this.schema != null) {
            this.schema.assertValidate(int64Value);
        }
        try {
            this.method.invoke(t, Long.valueOf(int64Value));
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValueJSONB(JSONReader jSONReader, T t) {
        long int64Value = jSONReader.readInt64Value();
        if (this.schema != null) {
            this.schema.assertValidate(int64Value);
        }
        try {
            this.method.invoke(t, Long.valueOf(int64Value));
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        long longValue = TypeUtils.toLongValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(longValue);
        }
        try {
            this.method.invoke(t, Long.valueOf(longValue));
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return Long.valueOf(jSONReader.readInt64Value());
    }
}
