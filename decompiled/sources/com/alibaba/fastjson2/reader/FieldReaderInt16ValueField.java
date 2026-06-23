package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt16ValueField<T> extends FieldReaderObjectField<T> {
    FieldReaderInt16ValueField(String str, Class cls, int i, long j, String str2, Short sh, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, j, str2, null, sh, jSONSchema, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        int int32Value = jSONReader.readInt32Value();
        if (this.schema != null) {
            this.schema.assertValidate(int32Value);
        }
        try {
            this.field.setShort(t, (short) int32Value);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, float f) {
        accept(t, Short.valueOf((short) f));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, double d) {
        accept(t, Short.valueOf((short) d));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        short shortValue = TypeUtils.toShortValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(shortValue);
        }
        try {
            this.field.setShort(t, shortValue);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, int i) {
        if (this.schema != null) {
            this.schema.assertValidate(i);
        }
        try {
            this.field.setShort(t, (short) i);
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
            this.field.setShort(t, (short) j);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return Short.valueOf((short) jSONReader.readInt32Value());
    }
}
