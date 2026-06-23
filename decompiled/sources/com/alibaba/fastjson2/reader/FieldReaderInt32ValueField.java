package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
class FieldReaderInt32ValueField<T> extends FieldReaderObjectField<T> {
    FieldReaderInt32ValueField(String str, Class cls, int i, String str2, Integer num, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, 0L, str2, null, num, jSONSchema, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        int int32Value = jSONReader.readInt32Value();
        if (this.schema != null) {
            this.schema.assertValidate(int32Value);
        }
        JDKUtils.UNSAFE.putInt(t, this.fieldOffset, int32Value);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValueJSONB(JSONReader jSONReader, T t) {
        accept((Object) t, jSONReader.readInt32Value());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, float f) {
        accept(t, Integer.valueOf((int) f));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, double d) {
        accept(t, Integer.valueOf((int) d));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        int intValue = TypeUtils.toIntValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(intValue);
        }
        JDKUtils.UNSAFE.putInt(t, this.fieldOffset, intValue);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        if (this.schema != null) {
            this.schema.assertValidate(j);
        }
        JDKUtils.UNSAFE.putInt(t, this.fieldOffset, (int) j);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return Integer.valueOf(jSONReader.readInt32Value());
    }
}
