package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.math.BigDecimal;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderBigDecimalField<T> extends FieldReaderObjectField<T> {
    FieldReaderBigDecimalField(String str, Class cls, int i, long j, String str2, BigDecimal bigDecimal, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, j, str2, null, bigDecimal, jSONSchema, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        BigDecimal bigDecimal = jSONReader.readBigDecimal();
        if (this.schema != null) {
            this.schema.assertValidate(bigDecimal);
        }
        try {
            this.field.set(t, bigDecimal);
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
            this.field.set(t, BigDecimal.valueOf(i));
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
            this.field.set(t, BigDecimal.valueOf(j));
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        BigDecimal bigDecimal = TypeUtils.toBigDecimal(obj);
        if (this.schema != null) {
            this.schema.assertValidate(bigDecimal);
        }
        try {
            this.field.set(t, bigDecimal);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
