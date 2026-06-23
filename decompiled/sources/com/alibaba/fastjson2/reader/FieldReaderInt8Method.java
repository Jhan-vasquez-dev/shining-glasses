package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt8Method<T> extends FieldReaderObject<T> {
    FieldReaderInt8Method(String str, Type type, Class cls, int i, long j, String str2, Locale locale, Byte b, JSONSchema jSONSchema, Method method) {
        super(str, type, cls, i, j, str2, locale, b, jSONSchema, method, null, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        Integer int32 = jSONReader.readInt32();
        if (this.schema != null) {
            this.schema.assertValidate(int32);
        }
        try {
            this.method.invoke(t, int32 == null ? null : Byte.valueOf(int32.byteValue()));
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Byte b = TypeUtils.toByte(obj);
        if (this.schema != null) {
            this.schema.assertValidate(b);
        }
        try {
            this.method.invoke(t, b);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readInt32();
    }
}
