package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
class FieldReaderAtomicIntegerMethodReadOnly<T> extends FieldReader<T> {
    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean isReadOnly() {
        return true;
    }

    FieldReaderAtomicIntegerMethodReadOnly(String str, Class cls, int i, JSONSchema jSONSchema, Method method) {
        super(str, cls, cls, i, 0L, null, null, null, jSONSchema, method, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (obj == null) {
            return;
        }
        try {
            ((AtomicInteger) this.method.invoke(t, new Object[0])).set(((Number) obj).intValue());
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        accept(t, jSONReader.readInt32());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        int int32Value = jSONReader.readInt32Value();
        if (jSONReader.wasNull()) {
            return null;
        }
        return new AtomicInteger(int32Value);
    }
}
