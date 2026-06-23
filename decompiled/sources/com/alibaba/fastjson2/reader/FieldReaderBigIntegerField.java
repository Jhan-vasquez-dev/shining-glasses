package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.math.BigInteger;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderBigIntegerField<T> extends FieldReaderObjectField<T> {
    FieldReaderBigIntegerField(String str, Class cls, int i, long j, String str2, BigInteger bigInteger, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, j, str2, null, bigInteger, jSONSchema, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        BigInteger bigInteger = jSONReader.readBigInteger();
        if (this.schema != null) {
            this.schema.assertValidate(bigInteger);
        }
        try {
            this.field.set(t, bigInteger);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, int i) {
        if (this.schema != null) {
            this.schema.assertValidate(i);
        }
        try {
            this.field.set(t, BigInteger.valueOf(i));
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        if (this.schema != null) {
            this.schema.assertValidate(j);
        }
        try {
            this.field.set(t, BigInteger.valueOf(j));
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        BigInteger bigInteger = TypeUtils.toBigInteger(obj);
        if (this.schema != null) {
            this.schema.assertValidate(bigInteger);
        }
        try {
            this.field.set(t, bigInteger);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
