package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
class FieldReaderMapFieldReadOnly<T> extends FieldReaderMapField<T> {
    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean isReadOnly() {
        return true;
    }

    FieldReaderMapFieldReadOnly(String str, Type type, Class cls, int i, long j, String str2, JSONSchema jSONSchema, Field field, String str3, BiConsumer biConsumer) {
        super(str, type, cls, i, j, str2, null, null, jSONSchema, field, str3, biConsumer);
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

    @Override // com.alibaba.fastjson2.reader.FieldReaderMapField, com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (obj == null) {
            return;
        }
        try {
            Map map = (Map) this.field.get(t);
            if (map == Collections.EMPTY_MAP || map == null || "java.util.Collections$UnmodifiableMap".equals(map.getClass().getName())) {
                return;
            }
            map.putAll((Map) obj);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void processExtra(JSONReader jSONReader, Object obj) {
        try {
            Map map = (Map) this.field.get(obj);
            String fieldName = jSONReader.getFieldName();
            map.put(fieldName, getItemObjectReader(jSONReader).readObject(jSONReader, null, fieldName, 0L));
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void acceptExtra(Object obj, String str, Object obj2) {
        try {
            ((Map) this.field.get(obj)).put(str, obj2);
        } catch (Exception unused) {
            throw new JSONException("set " + this.fieldName + " error");
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderMapField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        Object object;
        if (this.arrayToMapKey != null && jSONReader.isArray()) {
            try {
                arrayToMap((Map) this.field.get(t), jSONReader.readArray(this.valueType), this.arrayToMapKey, this.namingStrategy, JSONFactory.getObjectReader(this.valueType, this.features), this.arrayToMapDuplicateHandler);
            } catch (Exception unused) {
                throw new JSONException("set " + this.fieldName + " error");
            }
        } else {
            if (this.initReader == null) {
                this.initReader = jSONReader.getContext().getObjectReader(this.fieldType);
            }
            if (jSONReader.jsonb) {
                object = this.initReader.readJSONBObject(jSONReader, this.fieldType, this.fieldName, this.features);
            } else {
                object = this.initReader.readObject(jSONReader, this.fieldType, this.fieldName, this.features);
            }
            accept(t, object);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderMapField, com.alibaba.fastjson2.reader.FieldReader
    protected void acceptAny(T t, Object obj, long j) {
        if (this.arrayToMapKey != null && (obj instanceof Collection)) {
            try {
                arrayToMap((Map) this.field.get(t), (Collection) obj, this.arrayToMapKey, this.namingStrategy, JSONFactory.getObjectReader(this.valueType, this.features | j), this.arrayToMapDuplicateHandler);
                return;
            } catch (Exception unused) {
                throw new JSONException("set " + this.fieldName + " error");
            }
        }
        super.acceptAny(t, obj, j);
    }
}
