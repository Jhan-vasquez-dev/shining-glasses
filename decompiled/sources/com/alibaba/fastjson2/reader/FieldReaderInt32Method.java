package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt32Method<T> extends FieldReaderObject<T> {
    FieldReaderInt32Method(String str, int i, long j, String str2, Locale locale, Integer num, JSONSchema jSONSchema, Method method) {
        super(str, Integer.class, Integer.class, i, j, str2, locale, num, jSONSchema, method, null, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        Integer int32 = jSONReader.readInt32();
        if (this.schema != null) {
            this.schema.assertValidate(int32);
        }
        try {
            this.method.invoke(t, int32);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValueJSONB(JSONReader jSONReader, T t) {
        Integer int32 = jSONReader.readInt32();
        if (this.schema != null) {
            this.schema.assertValidate(int32);
        }
        try {
            this.method.invoke(t, int32);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Integer integer = TypeUtils.toInteger(obj);
        if (this.schema != null) {
            this.schema.assertValidate(integer);
        }
        try {
            this.method.invoke(t, integer);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readInt32();
    }
}
