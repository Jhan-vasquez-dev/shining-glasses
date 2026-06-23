package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderDoubleValueField<T> extends FieldReaderObjectField<T> {
    FieldReaderDoubleValueField(String str, Class cls, int i, long j, String str2, Double d, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, j, str2, null, d, jSONSchema, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        double doubleValue = jSONReader.readDoubleValue();
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        try {
            this.field.setDouble(t, doubleValue);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return Double.valueOf(jSONReader.readDoubleValue());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        double doubleValue = TypeUtils.toDoubleValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        try {
            this.field.set(t, Double.valueOf(doubleValue));
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
