package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderCollectionFieldReadOnly<T> extends FieldReaderObjectField<T> {
    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean isReadOnly() {
        return true;
    }

    FieldReaderCollectionFieldReadOnly(String str, Type type, Class cls, int i, long j, String str2, JSONSchema jSONSchema, Field field) {
        super(str, type, cls, i, j, str2, null, null, jSONSchema, field);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (obj == null) {
            return;
        }
        try {
            Collection collection = (Collection) this.field.get(t);
            if (collection == Collections.EMPTY_LIST || collection == Collections.EMPTY_SET || collection == null || collection.equals(obj)) {
                return;
            }
            String name = collection.getClass().getName();
            if ("java.util.Collections$UnmodifiableRandomAccessList".equals(name) || "java.util.Arrays$ArrayList".equals(name) || "java.util.Collections$SingletonList".equals(name) || name.startsWith("java.util.ImmutableCollections$")) {
                return;
            }
            collection.addAll((Collection) obj);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        if (this.initReader == null) {
            this.initReader = jSONReader.getContext().getObjectReader(this.fieldType);
        }
        accept(t, this.initReader.readObject(jSONReader, this.fieldType, this.fieldName, 0L));
    }
}
