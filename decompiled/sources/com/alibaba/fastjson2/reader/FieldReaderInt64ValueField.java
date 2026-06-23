package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
class FieldReaderInt64ValueField<T> extends FieldReaderObjectField<T> {
    FieldReaderInt64ValueField(String str, Class cls, int i, long j, String str2, Long l, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, j, str2, null, l, jSONSchema, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        long int64Value = jSONReader.readInt64Value();
        if (this.schema != null) {
            this.schema.assertValidate(int64Value);
        }
        JDKUtils.UNSAFE.putLong(t, this.fieldOffset, int64Value);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValueJSONB(JSONReader jSONReader, T t) {
        readFieldValue(jSONReader, t);
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
        long longValue = TypeUtils.toLongValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(longValue);
        }
        JDKUtils.UNSAFE.putLong(t, this.fieldOffset, longValue);
    }
}
