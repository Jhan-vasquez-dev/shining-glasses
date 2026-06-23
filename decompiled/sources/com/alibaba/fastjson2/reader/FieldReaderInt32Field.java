package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt32Field<T> extends FieldReaderObjectField<T> {
    FieldReaderInt32Field(String str, Class cls, int i, long j, String str2, Integer num, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, j, str2, null, num, jSONSchema, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        Integer int32 = jSONReader.readInt32();
        if (this.schema != null) {
            this.schema.assertValidate(int32);
        }
        try {
            this.field.set(t, int32);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readInt32();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, double d) {
        accept(t, Integer.valueOf((int) d));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, float f) {
        accept(t, Integer.valueOf((int) f));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Integer integer = TypeUtils.toInteger(obj);
        if (this.schema != null) {
            this.schema.assertValidate(integer);
        }
        if (obj != null || (this.features & JSONReader.Feature.IgnoreSetNullValue.mask) == 0) {
            try {
                this.field.set(t, integer);
            } catch (Exception e) {
                throw new JSONException("set " + this.fieldName + " error", e);
            }
        }
    }
}
