package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderAtomicReferenceMethodReadOnly<T> extends FieldReaderAtomicReference<T> {
    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean isReadOnly() {
        return true;
    }

    FieldReaderAtomicReferenceMethodReadOnly(String str, Type type, Class cls, int i, JSONSchema jSONSchema, Method method) {
        super(str, type, cls, i, 0L, null, jSONSchema, method, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (obj == null) {
            return;
        }
        try {
            ((AtomicReference) this.method.invoke(t, new Object[0])).set(obj);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
