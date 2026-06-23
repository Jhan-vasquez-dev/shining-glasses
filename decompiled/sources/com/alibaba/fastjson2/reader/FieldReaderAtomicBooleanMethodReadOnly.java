package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderAtomicBooleanMethodReadOnly<T> extends FieldReader<T> {
    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean isReadOnly() {
        return true;
    }

    FieldReaderAtomicBooleanMethodReadOnly(String str, Class cls, int i, JSONSchema jSONSchema, Method method) {
        super(str, cls, cls, i, 0L, null, null, null, jSONSchema, method, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (obj == null) {
            return;
        }
        try {
            AtomicBoolean atomicBoolean = (AtomicBoolean) this.method.invoke(t, new Object[0]);
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
