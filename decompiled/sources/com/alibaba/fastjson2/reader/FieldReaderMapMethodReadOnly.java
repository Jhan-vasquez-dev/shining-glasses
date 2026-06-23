package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
class FieldReaderMapMethodReadOnly<T> extends FieldReaderMapMethod<T> {
    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean isReadOnly() {
        return true;
    }

    FieldReaderMapMethodReadOnly(String str, Type type, Class cls, int i, long j, String str2, JSONSchema jSONSchema, Method method, Field field, String str3, BiConsumer biConsumer) {
        super(str, type, cls, i, j, str2, null, null, jSONSchema, method, field, null, str3, biConsumer);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public ObjectReader getItemObjectReader(JSONReader jSONReader) {
        if (this.itemReader != null) {
            return this.itemReader;
        }
        ObjectReader objectReader = getObjectReader(jSONReader);
        if (objectReader instanceof ObjectReaderImplMap) {
            ObjectReaderImplString objectReaderImplString = ObjectReaderImplString.INSTANCE;
            this.itemReader = objectReaderImplString;
            return objectReaderImplString;
        }
        if (objectReader instanceof ObjectReaderImplMapTyped) {
            ObjectReader objectReader2 = jSONReader.getObjectReader(((ObjectReaderImplMapTyped) objectReader).valueType);
            this.itemReader = objectReader2;
            return objectReader2;
        }
        return ObjectReaderImplObject.INSTANCE;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Map readOnlyMap;
        if (obj == null || (readOnlyMap = getReadOnlyMap(t)) == Collections.EMPTY_MAP || readOnlyMap == null || "java.util.Collections$UnmodifiableMap".equals(readOnlyMap.getClass().getName())) {
            return;
        }
        if (this.schema != null) {
            this.schema.assertValidate(obj);
        }
        readOnlyMap.putAll((Map) obj);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void processExtra(JSONReader jSONReader, Object obj) {
        getReadOnlyMap(obj).put(jSONReader.getFieldName(), getItemObjectReader(jSONReader).readObject(jSONReader, getItemType(), this.fieldName, 0L));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void acceptExtra(Object obj, String str, Object obj2) {
        getReadOnlyMap(obj).put(str, obj2);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderMapMethod, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        Object object;
        if (this.arrayToMapKey != null && jSONReader.isArray()) {
            arrayToMap(getReadOnlyMap(t), jSONReader.readArray(this.valueType), this.arrayToMapKey, this.namingStrategy, JSONFactory.getObjectReader(this.valueType, this.features), this.arrayToMapDuplicateHandler);
            return;
        }
        if (this.initReader == null) {
            this.initReader = jSONReader.getContext().getObjectReader(this.fieldType);
        }
        if (jSONReader.jsonb) {
            object = this.initReader.readJSONBObject(jSONReader, getItemType(), this.fieldName, this.features);
        } else {
            object = this.initReader.readObject(jSONReader, getItemType(), this.fieldName, this.features);
        }
        accept(t, object);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderMapMethod, com.alibaba.fastjson2.reader.FieldReader
    protected void acceptAny(T t, Object obj, long j) {
        if (this.arrayToMapKey != null && (obj instanceof Collection)) {
            arrayToMap(getReadOnlyMap(t), (Collection) obj, this.arrayToMapKey, this.namingStrategy, JSONFactory.getObjectReader(this.valueType, this.features | j), this.arrayToMapDuplicateHandler);
        } else {
            super.acceptAny(t, obj, j);
        }
    }

    private Map getReadOnlyMap(Object obj) {
        try {
            return (Map) this.method.invoke(obj, new Object[0]);
        } catch (Exception unused) {
            throw new JSONException("set " + this.fieldName + " error");
        }
    }
}
