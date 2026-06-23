package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderAtomicBooleanFieldReadOnly<T> extends FieldReader<T> {
    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean isReadOnly() {
        return true;
    }

    FieldReaderAtomicBooleanFieldReadOnly(String str, Class cls, int i, String str2, AtomicBoolean atomicBoolean, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, 0L, str2, null, atomicBoolean, jSONSchema, null, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (obj == null) {
            return;
        }
        try {
            AtomicBoolean atomicBoolean = (AtomicBoolean) this.field.get(t);
            if (obj instanceof AtomicBoolean) {
                obj = Boolean.valueOf(((AtomicBoolean) obj).get());
            }
            atomicBoolean.set(((Boolean) obj).booleanValue());
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        accept(t, jSONReader.readBool());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readBool();
    }
}
