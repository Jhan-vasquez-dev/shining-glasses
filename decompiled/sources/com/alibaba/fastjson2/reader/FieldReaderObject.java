package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class FieldReaderObject<T> extends FieldReader<T> {
    protected final BiConsumer function;
    protected ObjectReader initReader;

    public FieldReaderObject(String str, Type type, Class cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Method method, Field field, BiConsumer biConsumer) {
        super(str, type, cls, i, j, str2, locale, obj, jSONSchema, method, field);
        this.function = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public ObjectReader getInitReader() {
        return this.initReader;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public ObjectReader getObjectReader(JSONReader jSONReader) {
        ObjectReader objectReader = this.initReader;
        if (objectReader != null) {
            return objectReader;
        }
        if (this.reader != null) {
            return this.reader;
        }
        ObjectReader objectReaderCreateFormattedObjectReader = createFormattedObjectReader(this.fieldType, this.fieldClass, this.format, this.locale);
        if (objectReaderCreateFormattedObjectReader != null) {
            this.reader = objectReaderCreateFormattedObjectReader;
            return objectReaderCreateFormattedObjectReader;
        }
        if (this.fieldClass != null && Map.class.isAssignableFrom(this.fieldClass)) {
            ObjectReader objectReaderOf = ObjectReaderImplMap.of(this.fieldType, this.fieldClass, this.features);
            this.reader = objectReaderOf;
            return objectReaderOf;
        }
        if (this.fieldClass != null && Collection.class.isAssignableFrom(this.fieldClass)) {
            ObjectReader objectReaderOf2 = ObjectReaderImplList.of(this.fieldType, this.fieldClass, this.features);
            this.reader = objectReaderOf2;
            return objectReaderOf2;
        }
        ObjectReader objectReader2 = jSONReader.getObjectReader(this.fieldType);
        this.reader = objectReader2;
        return objectReader2;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public ObjectReader getObjectReader(JSONReader.Context context) {
        if (this.reader != null) {
            return this.reader;
        }
        ObjectReader objectReaderCreateFormattedObjectReader = createFormattedObjectReader(this.fieldType, this.fieldClass, this.format, this.locale);
        if (objectReaderCreateFormattedObjectReader != null) {
            this.reader = objectReaderCreateFormattedObjectReader;
            return objectReaderCreateFormattedObjectReader;
        }
        if (Map.class.isAssignableFrom(this.fieldClass)) {
            ObjectReader objectReaderOf = ObjectReaderImplMap.of(this.fieldType, this.fieldClass, this.features);
            this.reader = objectReaderOf;
            return objectReaderOf;
        }
        if (Collection.class.isAssignableFrom(this.fieldClass)) {
            ObjectReader objectReaderOf2 = ObjectReaderImplList.of(this.fieldType, this.fieldClass, this.features);
            this.reader = objectReaderOf2;
            return objectReaderOf2;
        }
        ObjectReader objectReader = context.getObjectReader(this.fieldType);
        this.reader = objectReader;
        return objectReader;
    }

    /* JADX WARN: Removed duplicated region for block: B:67:0x0104  */
    @Override // com.alibaba.fastjson2.reader.FieldReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void readFieldValue(com.alibaba.fastjson2.JSONReader r11, T r12) {
        /*
            Method dump skipped, instruction units count: 357
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.FieldReaderObject.readFieldValue(com.alibaba.fastjson2.JSONReader, java.lang.Object):void");
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValueJSONB(JSONReader jSONReader, T t) {
        if (!this.fieldClassSerializable && jSONReader.getType() != -110) {
            long features = jSONReader.getContext().getFeatures();
            if ((JSONReader.Feature.IgnoreNoneSerializable.mask & features) != 0) {
                jSONReader.skipValue();
                return;
            } else if ((features & JSONReader.Feature.ErrorOnNoneSerializable.mask) != 0 && (this.fieldClass != Object.class || jSONReader.isObject() || jSONReader.getType() == -110)) {
                throw new JSONException("not support none-Serializable");
            }
        }
        if (this.initReader == null) {
            this.initReader = jSONReader.getContext().getObjectReader(this.fieldType);
        }
        if (jSONReader.isReference()) {
            String reference = jSONReader.readReference();
            if ("..".equals(reference)) {
                accept(t, t);
                return;
            } else {
                addResolveTask(jSONReader, t, reference);
                return;
            }
        }
        Object jSONBObject = this.initReader.readJSONBObject(jSONReader, this.fieldType, this.fieldName, this.features);
        if (jSONBObject == null && (jSONReader.features(this.features) & JSONReader.Feature.ErrorOnNullForPrimitives.mask) != 0 && this.fieldClass.isPrimitive()) {
            throw new JSONException(jSONReader.info("primitive value not support input null"));
        }
        accept(t, jSONBObject);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, boolean z) {
        accept(t, Boolean.valueOf(z));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, byte b) {
        accept(t, Byte.valueOf(b));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, short s) {
        accept(t, Short.valueOf(s));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, int i) {
        accept(t, Integer.valueOf(i));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        accept(t, Long.valueOf(j));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, float f) {
        accept(t, Float.valueOf(f));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, double d) {
        accept(t, Double.valueOf(d));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, char c) {
        accept(t, Character.valueOf(c));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (this.schema != null) {
            this.schema.assertValidate(obj);
        }
        if (obj != null || (this.features & JSONReader.Feature.IgnoreSetNullValue.mask) == 0) {
            if (this.fieldClass == Character.TYPE && (obj instanceof String)) {
                String str = (String) obj;
                if (str.length() > 0) {
                    obj = Character.valueOf(str.charAt(0));
                } else {
                    obj = (char) 0;
                }
            }
            if (obj != null && !this.fieldClass.isInstance(obj)) {
                obj = TypeUtils.cast(obj, this.fieldType);
            }
            try {
                BiConsumer biConsumer = this.function;
                if (biConsumer != null) {
                    biConsumer.accept(t, obj);
                } else if (this.method != null) {
                    this.method.invoke(t, obj);
                } else {
                    JDKUtils.UNSAFE.putObject(t, this.fieldOffset, obj);
                }
            } catch (Exception e) {
                throw new JSONException("set " + (this.function != null ? super.toString() : this.fieldName) + " error", e);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        Object object;
        if (this.initReader == null) {
            this.initReader = getObjectReader(jSONReader);
        }
        if (jSONReader.jsonb) {
            object = this.initReader.readJSONBObject(jSONReader, this.fieldType, this.fieldName, this.features);
        } else {
            object = this.initReader.readObject(jSONReader, this.fieldType, this.fieldName, this.features);
        }
        Function buildFunction = this.initReader.getBuildFunction();
        return buildFunction != 0 ? buildFunction.apply(object) : object;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void processExtra(JSONReader jSONReader, Object obj) {
        if (this.initReader == null) {
            this.initReader = getObjectReader(jSONReader);
        }
        if ((this.initReader instanceof ObjectReaderBean) && this.field != null) {
            FieldReader fieldReader = this.initReader.getFieldReader(jSONReader.getFieldName());
            if (fieldReader != 0) {
                try {
                    Object objCreateInstance = this.field.get(obj);
                    if (objCreateInstance == null) {
                        objCreateInstance = this.initReader.createInstance(this.features);
                        accept(obj, objCreateInstance);
                    }
                    fieldReader.readFieldValue(jSONReader, objCreateInstance);
                    return;
                } catch (Exception e) {
                    throw new JSONException("read unwrapped field error", e);
                }
            }
        }
        jSONReader.skipValue();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public BiConsumer getFunction() {
        return this.function;
    }

    static void arrayToMap(final Map map, Collection collection, final String str, final PropertyNamingStrategy propertyNamingStrategy, final ObjectReader objectReader, final BiConsumer biConsumer) {
        collection.forEach(new Consumer() { // from class: com.alibaba.fastjson2.reader.FieldReaderObject$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                FieldReaderObject.lambda$arrayToMap$0(str, propertyNamingStrategy, objectReader, map, biConsumer, obj);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void lambda$arrayToMap$0(String str, PropertyNamingStrategy propertyNamingStrategy, ObjectReader objectReader, Map map, BiConsumer biConsumer, Object obj) {
        Object fieldValue;
        boolean z = obj instanceof Map;
        if (z) {
            fieldValue = ((Map) obj).get(str);
        } else if (obj != null) {
            fieldValue = JSONFactory.getObjectWriter(obj.getClass(), 0L).getFieldValue(obj, str);
        } else {
            throw new JSONException("key not found " + str);
        }
        if (propertyNamingStrategy != null && (fieldValue instanceof String)) {
            fieldValue = propertyNamingStrategy.fieldName((String) fieldValue);
        }
        if (!objectReader.getObjectClass().isInstance(obj)) {
            if (z) {
                obj = objectReader.createInstance((Map) obj, new JSONReader.Feature[0]);
            } else {
                throw new JSONException("can not accept " + JSON.toJSONString(obj, JSONWriter.Feature.ReferenceDetection));
            }
        }
        Object objPutIfAbsent = map.putIfAbsent(fieldValue, obj);
        if ((objPutIfAbsent != null) && (biConsumer != 0)) {
            biConsumer.accept(objPutIfAbsent, obj);
        }
    }
}
