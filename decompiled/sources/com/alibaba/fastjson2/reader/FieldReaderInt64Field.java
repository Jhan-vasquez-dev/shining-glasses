package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt64Field<T> extends FieldReaderObjectField<T> {
    FieldReaderInt64Field(String str, Class cls, int i, long j, String str2, Long l, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, j, str2, null, l, jSONSchema, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        Long int64 = jSONReader.readInt64();
        if (this.schema != null) {
            this.schema.assertValidate(int64);
        }
        try {
            this.field.set(t, int64);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readInt64();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, float f) {
        accept(t, Long.valueOf((long) f));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, double d) {
        accept(t, Long.valueOf((long) d));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Long l = TypeUtils.toLong(obj);
        if (this.schema != null) {
            this.schema.assertValidate(l);
        }
        try {
            this.field.set(t, l);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
