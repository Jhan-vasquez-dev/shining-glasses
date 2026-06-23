package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class ObjectReaderNoneDefaultConstructor<T> extends ObjectReaderAdapter<T> {
    final BiFunction bifunction;
    final Function<Map<Long, Object>, T> creatorFunction;
    final FactoryFunction factoryFunction;
    final Function function;
    final Constructor noneDefaultConstructor;
    final Map<Long, FieldReader> paramFieldReaderMap;
    final String[] paramNames;
    final FieldReader[] setterFieldReaders;

    public ObjectReaderNoneDefaultConstructor(Class cls, String str, String str2, long j, Function<Map<Long, Object>, T> function, List<Constructor> list, String[] strArr, FieldReader[] fieldReaderArr, FieldReader[] fieldReaderArr2, Class[] clsArr, String[] strArr2) {
        super(cls, str, str2, j, null, null, null, clsArr, strArr2, null, concat(fieldReaderArr, fieldReaderArr2));
        this.paramNames = strArr;
        this.creatorFunction = function;
        this.setterFieldReaders = fieldReaderArr2;
        this.paramFieldReaderMap = new HashMap();
        for (FieldReader fieldReader : fieldReaderArr) {
            this.paramFieldReaderMap.put(Long.valueOf(fieldReader.fieldNameHash), fieldReader);
        }
        if (this.creatorFunction instanceof ConstructorFunction) {
            this.noneDefaultConstructor = ((ConstructorFunction) function).constructor;
        } else {
            this.noneDefaultConstructor = null;
        }
        if (function instanceof ConstructorFunction) {
            ConstructorFunction constructorFunction = (ConstructorFunction) function;
            this.bifunction = constructorFunction.biFunction;
            this.function = constructorFunction.function;
            this.factoryFunction = null;
            return;
        }
        if (function instanceof FactoryFunction) {
            FactoryFunction factoryFunction = (FactoryFunction) function;
            this.bifunction = factoryFunction.biFunction;
            this.function = factoryFunction.function;
            this.factoryFunction = factoryFunction;
            return;
        }
        this.bifunction = null;
        this.function = null;
        this.factoryFunction = null;
    }

    static FieldReader[] concat(FieldReader[] fieldReaderArr, FieldReader[] fieldReaderArr2) {
        if (fieldReaderArr2 == null) {
            return fieldReaderArr;
        }
        int length = fieldReaderArr.length;
        FieldReader[] fieldReaderArr3 = (FieldReader[]) Arrays.copyOf(fieldReaderArr, fieldReaderArr2.length + length);
        System.arraycopy(fieldReaderArr2, 0, fieldReaderArr3, length, fieldReaderArr2.length);
        return fieldReaderArr3;
    }

    public Collection<FieldReader> getParameterFieldReaders() {
        return this.paramFieldReaderMap.values();
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public T createInstanceNoneDefaultConstructor(Map<Long, Object> map) {
        return this.creatorFunction.apply(map);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v0, types: [com.alibaba.fastjson2.reader.ObjectReaderNoneDefaultConstructor, com.alibaba.fastjson2.reader.ObjectReaderNoneDefaultConstructor<T>] */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v10, types: [java.util.Map] */
    /* JADX WARN: Type inference failed for: r4v15, types: [java.util.Map] */
    /* JADX WARN: Type inference failed for: r4v16 */
    /* JADX WARN: Type inference failed for: r4v17 */
    /* JADX WARN: Type inference failed for: r4v18, types: [java.util.LinkedHashMap] */
    /* JADX WARN: Type inference failed for: r4v19, types: [java.util.LinkedHashMap] */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v22 */
    /* JADX WARN: Type inference failed for: r4v23 */
    /* JADX WARN: Type inference failed for: r4v24 */
    /* JADX WARN: Type inference failed for: r4v25 */
    /* JADX WARN: Type inference failed for: r4v3, types: [java.util.LinkedHashMap] */
    /* JADX WARN: Type inference failed for: r4v9 */
    @Override // com.alibaba.fastjson2.reader.ObjectReaderAdapter, com.alibaba.fastjson2.reader.ObjectReader
    public T readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        ?? EmptyMap;
        ObjectReader objectReaderAutoType;
        JSONReader jSONReader2;
        Object obj2;
        long j2;
        Type type2;
        ObjectReader objectReaderCheckAutoType;
        if (!this.serializable) {
            jSONReader.errorOnNoneSerializable(this.objectClass);
        }
        byte type3 = jSONReader.getType();
        HashMap map = null;
        if (type3 == -81) {
            jSONReader.next();
            return null;
        }
        if (type3 == -110 && (objectReaderCheckAutoType = jSONReader.checkAutoType(this.objectClass, this.typeNameHash, this.features | j)) != null && objectReaderCheckAutoType != this) {
            return (T) objectReaderCheckAutoType.readJSONBObject(jSONReader, type, obj, j);
        }
        int i = 0;
        if (jSONReader.isArray()) {
            if (jSONReader.isSupportBeanArray()) {
                int iStartArray = jSONReader.startArray();
                int i2 = 0;
                EmptyMap = 0;
                while (i2 < iStartArray) {
                    FieldReader fieldReader = this.fieldReaders[i2];
                    Object fieldValue = fieldReader.readFieldValue(jSONReader);
                    if (EmptyMap == 0) {
                        EmptyMap = new LinkedHashMap();
                    }
                    EmptyMap.put(Long.valueOf(fieldReader.fieldNameHash), fieldValue);
                    i2++;
                    EmptyMap = EmptyMap;
                }
            } else {
                throw new JSONException(jSONReader.info("expect object, but " + JSONB.typeName(jSONReader.getType())));
            }
        } else {
            jSONReader.nextIfObjectStart();
            int i3 = 0;
            HashMap map2 = null;
            EmptyMap = 0;
            while (!jSONReader.nextIfObjectEnd()) {
                long fieldNameHashCode = jSONReader.readFieldNameHashCode();
                if (fieldNameHashCode != 0) {
                    if (fieldNameHashCode == HASH_TYPE && i3 == 0) {
                        long typeHashCode = jSONReader.readTypeHashCode();
                        JSONReader.Context context = jSONReader.getContext();
                        ObjectReader objectReaderAutoType2 = context.getObjectReaderAutoType(typeHashCode);
                        if (objectReaderAutoType2 == null) {
                            String string = jSONReader.getString();
                            objectReaderAutoType = context.getObjectReaderAutoType(string, this.objectClass);
                            if (objectReaderAutoType == null) {
                                throw new JSONException(jSONReader.info("autoType not support : " + string));
                            }
                            jSONReader2 = jSONReader;
                            type2 = type;
                            obj2 = obj;
                            j2 = j;
                        } else {
                            objectReaderAutoType = objectReaderAutoType2;
                            jSONReader2 = jSONReader;
                            obj2 = obj;
                            j2 = j;
                            type2 = type;
                        }
                        T t = (T) objectReaderAutoType.readJSONBObject(jSONReader2, type2, obj2, j2);
                        jSONReader.nextIfComma();
                        return t;
                    }
                    FieldReader fieldReader2 = getFieldReader(fieldNameHashCode);
                    if (fieldReader2 == null) {
                        processExtra(jSONReader, null);
                    } else if (jSONReader.isReference()) {
                        jSONReader.next();
                        String string2 = jSONReader.readString();
                        if (map2 == null) {
                            map2 = new HashMap();
                        }
                        map2.put(Long.valueOf(fieldNameHashCode), string2);
                    } else {
                        Object fieldValue2 = fieldReader2.readFieldValue(jSONReader);
                        EmptyMap = EmptyMap;
                        if (EmptyMap == 0) {
                            EmptyMap = new LinkedHashMap();
                        }
                        EmptyMap.put(Long.valueOf(fieldReader2.fieldNameHash), fieldValue2);
                    }
                }
                i3++;
                EmptyMap = EmptyMap;
            }
            map = map2;
        }
        if (EmptyMap == 0) {
            EmptyMap = Collections.emptyMap();
        }
        T t2 = (T) createInstanceNoneDefaultConstructor(EmptyMap);
        if (this.setterFieldReaders != null) {
            while (true) {
                FieldReader[] fieldReaderArr = this.setterFieldReaders;
                if (i >= fieldReaderArr.length) {
                    break;
                }
                FieldReader fieldReader3 = fieldReaderArr[i];
                fieldReader3.accept(t2, EmptyMap.get(Long.valueOf(fieldReader3.fieldNameHash)));
                i++;
            }
        }
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                Long l = (Long) entry.getKey();
                String str = (String) entry.getValue();
                FieldReader fieldReader4 = getFieldReader(l.longValue());
                if ("..".equals(str)) {
                    fieldReader4.accept(t2, t2);
                } else {
                    fieldReader4.addResolveTask(jSONReader, t2, str);
                }
            }
        }
        return t2;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderAdapter, com.alibaba.fastjson2.reader.ObjectReader
    public T readArrayMappingObject(JSONReader jSONReader, Type type, Object obj, long j) {
        if (jSONReader.jsonb) {
            return readArrayMappingJSONBObject(jSONReader, type, obj, j);
        }
        if (!this.serializable) {
            jSONReader.errorOnNoneSerializable(this.objectClass);
        }
        jSONReader.nextIfArrayStart();
        LinkedHashMap linkedHashMap = null;
        int i = 0;
        while (i < this.fieldReaders.length) {
            FieldReader fieldReader = this.fieldReaders[i];
            Object fieldValue = fieldReader.readFieldValue(jSONReader);
            if (linkedHashMap == null) {
                linkedHashMap = new LinkedHashMap();
            }
            linkedHashMap.put(Long.valueOf(fieldReader.fieldNameHash), fieldValue);
            i++;
            linkedHashMap = linkedHashMap;
        }
        if (!jSONReader.nextIfArrayEnd()) {
            throw new JSONException(jSONReader.info("array not end, " + jSONReader.current()));
        }
        jSONReader.nextIfComma();
        Map<Long, Object> mapEmptyMap = linkedHashMap;
        if (linkedHashMap == null) {
            mapEmptyMap = Collections.emptyMap();
        }
        return createInstanceNoneDefaultConstructor(mapEmptyMap);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderBean, com.alibaba.fastjson2.reader.ObjectReader
    public T readObject(JSONReader jSONReader, Type type, Object obj, long j) {
        Object obj2;
        long j2;
        ObjectReader objectReaderAutoType;
        if (!this.serializable) {
            jSONReader.errorOnNoneSerializable(this.objectClass);
        }
        if (jSONReader.jsonb) {
            return readJSONBObject(jSONReader, type, obj, 0L);
        }
        int i = 0;
        LinkedHashMap linkedHashMap = null;
        if (jSONReader.isSupportBeanArray(j | this.features) && jSONReader.nextIfArrayStart()) {
            while (i < this.fieldReaders.length) {
                FieldReader fieldReader = this.fieldReaders[i];
                Object fieldValue = fieldReader.readFieldValue(jSONReader);
                if (linkedHashMap == null) {
                    linkedHashMap = new LinkedHashMap();
                }
                linkedHashMap.put(Long.valueOf(fieldReader.fieldNameHash), fieldValue);
                i++;
                linkedHashMap = linkedHashMap;
            }
            if (!jSONReader.nextIfArrayEnd()) {
                throw new JSONException(jSONReader.info("array not end, " + jSONReader.current()));
            }
            jSONReader.nextIfComma();
            Map<Long, Object> mapEmptyMap = linkedHashMap;
            if (linkedHashMap == null) {
                mapEmptyMap = Collections.emptyMap();
            }
            return createInstanceNoneDefaultConstructor(mapEmptyMap);
        }
        if (!jSONReader.nextIfObjectStart()) {
            if (jSONReader.isTypeRedirect()) {
                jSONReader.setTypeRedirect(false);
            } else if (jSONReader.nextIfNullOrEmptyString()) {
                return null;
            }
        }
        JSONReader.Context context = jSONReader.getContext();
        long features = this.features | j | context.getFeatures();
        int i2 = 0;
        LinkedHashMap linkedHashMap2 = null;
        IdentityHashMap identityHashMap = null;
        while (!jSONReader.nextIfObjectEnd()) {
            long fieldNameHashCode = jSONReader.readFieldNameHashCode();
            if (fieldNameHashCode != 0) {
                if (fieldNameHashCode == this.typeKeyHashCode && i2 == 0) {
                    long typeHashCode = jSONReader.readTypeHashCode();
                    if (typeHashCode != this.typeNameHash) {
                        if ((JSONReader.Feature.SupportAutoType.mask & features) != 0) {
                            objectReaderAutoType = jSONReader.getObjectReaderAutoType(typeHashCode, this.objectClass, this.features);
                        } else {
                            objectReaderAutoType = context.getObjectReaderAutoType(jSONReader.getString(), this.objectClass);
                        }
                        if (objectReaderAutoType == null) {
                            objectReaderAutoType = context.getObjectReaderAutoType(jSONReader.getString(), this.objectClass, this.features);
                        }
                        ObjectReader objectReader = objectReaderAutoType;
                        if (objectReader != null) {
                            T t = (T) objectReader.readObject(jSONReader, type, obj, 0L);
                            jSONReader.nextIfComma();
                            return t;
                        }
                    }
                } else if (!jSONReader.nextIfNull()) {
                    FieldReader fieldReader2 = getFieldReader(fieldNameHashCode);
                    FieldReader fieldReader3 = this.paramFieldReaderMap.get(Long.valueOf(fieldNameHashCode));
                    if (fieldReader3 != null && fieldReader2 != null && fieldReader3.fieldClass != null && !fieldReader3.fieldClass.equals(fieldReader2.fieldClass)) {
                        fieldReader2 = fieldReader3;
                    }
                    if (fieldReader2 == null && (JSONReader.Feature.SupportSmartMatch.mask & features) != 0 && (fieldReader2 = getFieldReaderLCase(jSONReader.getNameHashCodeLCase())) != null && linkedHashMap2 != null && linkedHashMap2.containsKey(Long.valueOf(fieldReader2.fieldNameHash))) {
                        fieldReader2 = null;
                    }
                    if (fieldReader2 == null) {
                        processExtra(jSONReader, null);
                    } else if (jSONReader.isReference()) {
                        String reference = jSONReader.readReference();
                        if (identityHashMap == null) {
                            identityHashMap = new IdentityHashMap();
                        }
                        identityHashMap.put(fieldReader2, reference);
                    } else {
                        Object fieldValue2 = fieldReader2.readFieldValue(jSONReader);
                        if (linkedHashMap2 == null) {
                            linkedHashMap2 = new LinkedHashMap();
                        }
                        if (fieldReader2 instanceof FieldReaderObjectParam) {
                            j2 = ((FieldReaderObjectParam) fieldReader2).paramNameHash;
                        } else {
                            j2 = fieldReader2.fieldNameHash;
                        }
                        linkedHashMap2.put(Long.valueOf(j2), fieldValue2);
                    }
                }
            }
            i2++;
        }
        if (this.hasDefaultValue) {
            if (linkedHashMap2 == null) {
                linkedHashMap2 = new LinkedHashMap();
            }
            for (FieldReader fieldReader4 : this.fieldReaders) {
                if (fieldReader4.defaultValue != null) {
                    linkedHashMap2.putIfAbsent(Long.valueOf(fieldReader4.fieldNameHash), fieldReader4.defaultValue);
                }
            }
        }
        T tApply = this.creatorFunction.apply(linkedHashMap2 == null ? Collections.emptyMap() : linkedHashMap2);
        if (this.setterFieldReaders != null && linkedHashMap2 != null) {
            while (true) {
                FieldReader[] fieldReaderArr = this.setterFieldReaders;
                if (i >= fieldReaderArr.length) {
                    break;
                }
                FieldReader fieldReader5 = fieldReaderArr[i];
                FieldReader fieldReader6 = this.paramFieldReaderMap.get(Long.valueOf(fieldReader5.fieldNameHash));
                if ((fieldReader6 == null || fieldReader6.fieldClass.equals(fieldReader5.fieldClass)) && (obj2 = linkedHashMap2.get(Long.valueOf(fieldReader5.fieldNameHash))) != null && (fieldReader6 == null || (fieldReader6.fieldName != null && fieldReader5.fieldName != null && fieldReader6.fieldName.equals(fieldReader5.fieldName)))) {
                    fieldReader5.accept(tApply, obj2);
                }
                i++;
            }
        }
        if (identityHashMap != null) {
            for (Map.Entry entry : identityHashMap.entrySet()) {
                ((FieldReader) entry.getKey()).addResolveTask(jSONReader, tApply, (String) entry.getValue());
            }
        }
        jSONReader.nextIfComma();
        return tApply;
    }

    public T readFromCSV(JSONReader jSONReader, Type type, Object obj, long j) {
        if (!this.serializable) {
            jSONReader.errorOnNoneSerializable(this.objectClass);
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (int i = 0; i < this.fieldReaders.length; i++) {
            FieldReader fieldReader = this.fieldReaders[i];
            linkedHashMap.put(Long.valueOf(fieldReader.fieldNameHash), fieldReader.readFieldValue(jSONReader));
        }
        jSONReader.nextIfMatch('\n');
        return createInstanceNoneDefaultConstructor(linkedHashMap);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.ObjectReaderAdapter, com.alibaba.fastjson2.reader.ObjectReader
    public T createInstance(Collection collection, long j) {
        long j2;
        Class<?> cls;
        Class<?> cls2;
        Function typeConvert;
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator it = collection.iterator();
        int i = 0;
        while (it.hasNext()) {
            Object next = it.next();
            if (i >= this.fieldReaders.length) {
                break;
            }
            FieldReader fieldReader = this.fieldReaders[i];
            if (next != null && (cls = next.getClass()) != (cls2 = fieldReader.fieldClass) && (typeConvert = defaultObjectReaderProvider.getTypeConvert(cls, cls2)) != 0) {
                next = typeConvert.apply(next);
            }
            if (fieldReader instanceof FieldReaderObjectParam) {
                j2 = ((FieldReaderObjectParam) fieldReader).paramNameHash;
            } else {
                j2 = fieldReader.fieldNameHash;
            }
            linkedHashMap.put(Long.valueOf(j2), next);
            i++;
        }
        return createInstanceNoneDefaultConstructor(linkedHashMap);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00d9  */
    /* JADX WARN: Type inference failed for: r10v0, types: [com.alibaba.fastjson2.reader.ObjectReaderNoneDefaultConstructor, com.alibaba.fastjson2.reader.ObjectReaderNoneDefaultConstructor<T>] */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v2, types: [java.util.Map] */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.util.Map] */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8, types: [java.util.LinkedHashMap] */
    /* JADX WARN: Type inference failed for: r3v9, types: [java.util.LinkedHashMap] */
    /* JADX WARN: Type inference failed for: r5v10, types: [java.util.function.Function] */
    /* JADX WARN: Type inference failed for: r6v2, types: [java.util.function.Function] */
    @Override // com.alibaba.fastjson2.reader.ObjectReaderAdapter, com.alibaba.fastjson2.reader.ObjectReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public T createInstance(java.util.Map r11, long r12) {
        /*
            Method dump skipped, instruction units count: 277
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderNoneDefaultConstructor.createInstance(java.util.Map, long):java.lang.Object");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T createInstance(Object[] objArr) {
        try {
            Function function = this.function;
            if (function != 0) {
                return (T) function.apply(objArr[0]);
            }
            BiFunction biFunction = this.bifunction;
            if (biFunction != 0) {
                return (T) biFunction.apply(objArr[0], objArr[1]);
            }
            FactoryFunction factoryFunction = this.factoryFunction;
            if (factoryFunction != null) {
                return (T) factoryFunction.createInstance(objArr);
            }
            return (T) this.noneDefaultConstructor.newInstance(objArr);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
            throw new JSONException("invoke constructor error, " + this.constructor, e);
        }
    }
}
