package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderBigIntegerMethod<T> extends FieldReaderObject<T> {
    FieldReaderBigIntegerMethod(String str, Type type, Class cls, int i, long j, String str2, Locale locale, BigInteger bigInteger, JSONSchema jSONSchema, Method method) {
        super(str, type, cls, i, j, str2, locale, bigInteger, jSONSchema, method, null, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        BigInteger bigInteger = jSONReader.readBigInteger();
        if (this.schema != null) {
            this.schema.assertValidate(bigInteger);
        }
        try {
            this.method.invoke(t, bigInteger);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        BigInteger bigInteger = TypeUtils.toBigInteger(obj);
        if (this.schema != null) {
            this.schema.assertValidate(bigInteger);
        }
        try {
            this.method.invoke(t, bigInteger);
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
            this.method.invoke(t, BigInteger.valueOf(i));
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        if (this.schema != null) {
            this.schema.assertValidate(j);
        }
        try {
            this.method.invoke(t, BigInteger.valueOf(j));
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
