package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderFloatValueField<T> extends FieldReaderObjectField<T> {
    FieldReaderFloatValueField(String str, Class cls, int i, long j, String str2, Float f, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, j, str2, null, f, jSONSchema, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        float floatValue = jSONReader.readFloatValue();
        if (this.schema != null) {
            this.schema.assertValidate(floatValue);
        }
        try {
            this.field.setFloat(t, floatValue);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return Float.valueOf(jSONReader.readFloatValue());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        float floatValue = TypeUtils.toFloatValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(floatValue);
        }
        try {
            this.field.setFloat(t, floatValue);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
