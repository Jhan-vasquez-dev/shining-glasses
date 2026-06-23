package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.ReferenceKey;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
class ObjectReaderImplMapTyped implements ObjectReader {
    final Function builder;
    final Constructor defaultConstructor;
    final long features;
    final Class instanceType;
    ObjectReader keyObjectReader;
    final Type keyType;
    final Class mapType;
    final boolean multiValue;
    final Class valueClass;
    ObjectReader valueObjectReader;
    final Type valueType;

    public ObjectReaderImplMapTyped(Class cls, Class cls2, Type type, Type type2, long j, Function function) {
        Constructor<?> constructor = null;
        type = type == Object.class ? null : type;
        this.mapType = cls;
        this.instanceType = cls2;
        this.keyType = type;
        this.valueType = type2;
        this.valueClass = TypeUtils.getClass(type2);
        this.features = j;
        this.builder = function;
        int i = 0;
        this.multiValue = cls2 != null && "org.springframework.util.LinkedMultiValueMap".equals(cls2.getName());
        Constructor<?>[] declaredConstructors = cls2.getDeclaredConstructors();
        int length = declaredConstructors.length;
        while (true) {
            if (i >= length) {
                break;
            }
            Constructor<?> constructor2 = declaredConstructors[i];
            if (constructor2.getParameterCount() == 0 && !Modifier.isPublic(constructor2.getModifiers())) {
                constructor2.setAccessible(true);
                constructor = constructor2;
                break;
            }
            i++;
        }
        this.defaultConstructor = constructor;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Class getObjectClass() {
        return this.mapType;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(Map map, long j) {
        Map map2;
        Object string;
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        Class cls = this.instanceType;
        if (cls == Map.class || cls == HashMap.class) {
            map2 = new HashMap();
        } else {
            map2 = (Map) createInstance(j);
        }
        for (Map.Entry entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            Type type = this.keyType;
            if (type == null || type == String.class) {
                string = key.toString();
            } else {
                string = TypeUtils.cast(key, type);
            }
            if (value != null) {
                Class<?> cls2 = value.getClass();
                if (this.valueType == Object.class) {
                    continue;
                } else if (cls2 == JSONObject.class || cls2 == TypeUtils.CLASS_JSON_OBJECT_1x) {
                    if (this.valueObjectReader == null) {
                        this.valueObjectReader = defaultObjectReaderProvider.getObjectReader(this.valueType);
                    }
                    value = this.valueObjectReader.createInstance((Map) value, j);
                } else if ((cls2 == JSONArray.class || cls2 == TypeUtils.CLASS_JSON_ARRAY_1x) && this.valueClass == List.class) {
                    if (this.valueObjectReader == null) {
                        this.valueObjectReader = defaultObjectReaderProvider.getObjectReader(this.valueType);
                    }
                    value = this.valueObjectReader.createInstance((List) value, j);
                } else {
                    Function typeConvert = defaultObjectReaderProvider.getTypeConvert(cls2, this.valueType);
                    if (typeConvert != null) {
                        value = typeConvert.apply(value);
                    } else if (value instanceof Map) {
                        if (this.valueObjectReader == null) {
                            this.valueObjectReader = defaultObjectReaderProvider.getObjectReader(this.valueType);
                        }
                        value = this.valueObjectReader.createInstance((Map) value, j);
                    } else if ((value instanceof Collection) && !this.multiValue) {
                        if (this.valueObjectReader == null) {
                            this.valueObjectReader = defaultObjectReaderProvider.getObjectReader(this.valueType);
                        }
                        value = this.valueObjectReader.createInstance((Collection) value, j);
                    } else if (!cls2.isInstance(value)) {
                        throw new JSONException("can not convert from " + cls2 + " to " + this.valueType);
                    }
                }
            }
            map2.put(string, value);
        }
        Function function = this.builder;
        return function != null ? function.apply(map2) : map2;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(long j) {
        Class cls = this.instanceType;
        if (cls != null && !cls.isInterface()) {
            try {
                Constructor constructor = this.defaultConstructor;
                if (constructor != null) {
                    return constructor.newInstance(new Object[0]);
                }
                return this.instanceType.newInstance();
            } catch (Exception e) {
                throw new JSONException("create map error", e);
            }
        }
        return new HashMap();
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        long j2;
        Function buildFunction;
        ObjectReader objectReaderCheckAutoType;
        Map map;
        Object fieldName;
        Object obj2;
        Object jSONBObject;
        Object any;
        Type type2;
        Function function = this.builder;
        if (jSONReader.getType() == -110) {
            objectReaderCheckAutoType = jSONReader.checkAutoType(this.mapType, 0L, this.features | j);
            if (objectReaderCheckAutoType == null || objectReaderCheckAutoType == this) {
                j2 = j;
                buildFunction = function;
            } else {
                buildFunction = objectReaderCheckAutoType.getBuildFunction();
                if (!(objectReaderCheckAutoType instanceof ObjectReaderImplMap) && !(objectReaderCheckAutoType instanceof ObjectReaderImplMapTyped)) {
                    return objectReaderCheckAutoType.readJSONBObject(jSONReader, type, obj, j);
                }
                j2 = j;
            }
        } else {
            j2 = j;
            buildFunction = function;
            objectReaderCheckAutoType = null;
        }
        byte type3 = jSONReader.getType();
        if (type3 == -81) {
            jSONReader.next();
            return null;
        }
        if (type3 == -90) {
            jSONReader.next();
        }
        long features = j2 | jSONReader.getContext().getFeatures();
        if (objectReaderCheckAutoType != null) {
            map = (Map) objectReaderCheckAutoType.createInstance(features);
        } else if (this.instanceType == HashMap.class) {
            map = new HashMap();
        } else {
            map = (Map) createInstance(j2);
        }
        Map map2 = map;
        int i = 0;
        while (jSONReader.getType() != -91) {
            if (this.keyType == String.class || jSONReader.isString()) {
                fieldName = jSONReader.readFieldName();
            } else {
                if (jSONReader.isReference()) {
                    String reference = jSONReader.readReference();
                    any = new ReferenceKey(i);
                    jSONReader.addResolveTask(map2, any, JSONPath.of(reference));
                } else {
                    if (this.keyObjectReader == null && (type2 = this.keyType) != null) {
                        this.keyObjectReader = jSONReader.getObjectReader(type2);
                    }
                    ObjectReader objectReader = this.keyObjectReader;
                    if (objectReader == null) {
                        any = jSONReader.readAny();
                    } else {
                        fieldName = objectReader.readJSONBObject(jSONReader, null, null, j2);
                    }
                }
                fieldName = any;
            }
            if (jSONReader.isReference()) {
                String reference2 = jSONReader.readReference();
                if ("..".equals(reference2)) {
                    map2.put(fieldName, map2);
                } else {
                    jSONReader.addResolveTask(map2, fieldName, JSONPath.of(reference2));
                    if (!(map2 instanceof ConcurrentMap)) {
                        map2.put(fieldName, null);
                    }
                }
            } else if (jSONReader.nextIfNull()) {
                map2.put(fieldName, null);
            } else {
                if (this.valueType == Object.class) {
                    jSONBObject = jSONReader.readAny();
                    obj2 = fieldName;
                } else {
                    ObjectReader objectReaderCheckAutoType2 = jSONReader.checkAutoType(this.valueClass, 0L, j);
                    if (objectReaderCheckAutoType2 != null && objectReaderCheckAutoType2 != this) {
                        obj2 = fieldName;
                        jSONBObject = objectReaderCheckAutoType2.readJSONBObject(jSONReader, this.valueType, obj2, j);
                    } else {
                        obj2 = fieldName;
                        if (this.valueObjectReader == null) {
                            this.valueObjectReader = jSONReader.getObjectReader(this.valueType);
                        }
                        jSONBObject = this.valueObjectReader.readJSONBObject(jSONReader, this.valueType, obj2, j);
                    }
                }
                if (jSONBObject != null || (JSONReader.Feature.IgnoreNullPropertyValue.mask & features) == 0) {
                    map2.put(obj2, jSONBObject);
                }
            }
            i++;
            j2 = j;
        }
        jSONReader.next();
        if (buildFunction == null) {
            return map2;
        }
        if (buildFunction == ObjectReaderImplMap.ENUM_MAP_BUILDER && map2.isEmpty()) {
            return new EnumMap((Class) this.keyType);
        }
        return buildFunction.apply(map2);
    }

    /* JADX WARN: Removed duplicated region for block: B:139:0x029f  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x02a9  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01ab  */
    @Override // com.alibaba.fastjson2.reader.ObjectReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object readObject(com.alibaba.fastjson2.JSONReader r15, java.lang.reflect.Type r16, java.lang.Object r17, long r18) {
        /*
            Method dump skipped, instruction units count: 706
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderImplMapTyped.readObject(com.alibaba.fastjson2.JSONReader, java.lang.reflect.Type, java.lang.Object, long):java.lang.Object");
    }
}
