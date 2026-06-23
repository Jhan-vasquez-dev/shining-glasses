package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public final class ObjectReaderImplMap implements ObjectReader {
    static Function UNSAFE_OBJECT_CREATOR;
    final Function builder;
    final long features;
    final Type fieldType;
    volatile boolean instanceError;
    final Class instanceType;
    Object mapSingleton;
    final Class mapType;
    final long mapTypeHash;
    static final Function ENUM_MAP_BUILDER = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplMap$$ExternalSyntheticLambda7
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return ObjectReaderImplMap.lambda$static$0(obj);
        }
    };
    static final Class CLASS_SINGLETON_MAP = Collections.singletonMap(1, 1).getClass();
    static final Class CLASS_EMPTY_MAP = Collections.EMPTY_MAP.getClass();
    static final Class CLASS_EMPTY_SORTED_MAP = Collections.emptySortedMap().getClass();
    static final Class CLASS_EMPTY_NAVIGABLE_MAP = Collections.emptyNavigableMap().getClass();
    static final Class CLASS_UNMODIFIABLE_MAP = Collections.unmodifiableMap(Collections.emptyMap()).getClass();
    static final Class CLASS_UNMODIFIABLE_SORTED_MAP = Collections.unmodifiableSortedMap(Collections.emptySortedMap()).getClass();
    static final Class CLASS_UNMODIFIABLE_NAVIGABLE_MAP = Collections.unmodifiableNavigableMap(Collections.emptyNavigableMap()).getClass();
    public static final ObjectReaderImplMap INSTANCE = new ObjectReaderImplMap(null, HashMap.class, HashMap.class, 0, null);
    public static final ObjectReaderImplMap INSTANCE_OBJECT = new ObjectReaderImplMap(null, JSONObject.class, JSONObject.class, 0, null);

    static /* synthetic */ Object lambda$static$0(Object obj) {
        return new EnumMap((Map) obj);
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0154  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x015f  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0175  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x01f8  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0201  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0233  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x023b  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0244  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x00e6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.alibaba.fastjson2.reader.ObjectReader of(java.lang.reflect.Type r20, java.lang.Class r21, long r22) {
        /*
            Method dump skipped, instruction units count: 690
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderImplMap.of(java.lang.reflect.Type, java.lang.Class, long):com.alibaba.fastjson2.reader.ObjectReader");
    }

    static /* synthetic */ Map lambda$of$1(Map map) {
        Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
        return Collections.singletonMap(entry.getKey(), entry.getValue());
    }

    ObjectReaderImplMap(Class cls, long j, Object obj) {
        this(cls, cls, cls, j, null);
        this.mapSingleton = obj;
    }

    ObjectReaderImplMap(Type type, Class cls, Class cls2, long j, Function function) {
        this.fieldType = type;
        this.mapType = cls;
        this.mapTypeHash = Fnv.hashCode64(TypeUtils.getTypeName(cls));
        this.instanceType = cls2;
        this.features = j;
        this.builder = function;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Class getObjectClass() {
        return this.mapType;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Function getBuildFunction() {
        return this.builder;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(long j) {
        Class cls = this.instanceType;
        if (cls == HashMap.class) {
            return new HashMap();
        }
        if (cls == LinkedHashMap.class) {
            return new LinkedHashMap();
        }
        if (cls == JSONObject.class) {
            return new JSONObject();
        }
        Object obj = this.mapSingleton;
        if (obj != null) {
            return obj;
        }
        if (cls == CLASS_EMPTY_SORTED_MAP) {
            return Collections.emptySortedMap();
        }
        if (cls == CLASS_EMPTY_NAVIGABLE_MAP) {
            return Collections.emptyNavigableMap();
        }
        String name = cls.getName();
        name.hashCode();
        switch (name) {
            case "java.util.ImmutableCollections$Map1":
                return new HashMap();
            case "java.util.ImmutableCollections$MapN":
                return new LinkedHashMap();
            case "com.ali.com.google.common.collect.EmptyImmutableBiMap":
                try {
                    return JDKUtils.UNSAFE.allocateInstance(this.instanceType);
                } catch (InstantiationException unused) {
                    throw new JSONException("create map error : " + this.instanceType);
                }
            default:
                try {
                    return this.instanceType.newInstance();
                } catch (IllegalAccessException | InstantiationException unused2) {
                    throw new JSONException("create map error : " + this.instanceType);
                }
        }
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(Map map, long j) {
        if (this.mapType.isInstance(map)) {
            return map;
        }
        if (this.mapType == JSONObject.class) {
            return new JSONObject(map);
        }
        Map map2 = (Map) createInstance(j);
        map2.putAll(map);
        Function function = this.builder;
        return function != null ? function.apply(map2) : map2;
    }

    /* JADX WARN: Removed duplicated region for block: B:108:0x00ef A[EDGE_INSN: B:108:0x00ef->B:46:0x00ef BREAK  A[LOOP:0: B:44:0x00e7->B:102:0x01d8], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00fb A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0148  */
    @Override // com.alibaba.fastjson2.reader.ObjectReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object readJSONBObject(com.alibaba.fastjson2.JSONReader r18, java.lang.reflect.Type r19, java.lang.Object r20, long r21) {
        /*
            Method dump skipped, instruction units count: 480
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderImplMap.readJSONBObject(com.alibaba.fastjson2.JSONReader, java.lang.reflect.Type, java.lang.Object, long):java.lang.Object");
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object readObject(JSONReader jSONReader, Type type, Object obj, long j) {
        Map map;
        Class cls;
        if (jSONReader.jsonb) {
            return readJSONBObject(jSONReader, type, obj, j);
        }
        if (jSONReader.nextIfNull()) {
            return null;
        }
        JSONReader.Context context = jSONReader.getContext();
        Supplier<Map> objectSupplier = jSONReader.getContext().getObjectSupplier();
        if (objectSupplier != null && ((cls = this.mapType) == null || cls == JSONObject.class || "com.alibaba.fastjson.JSONObject".equals(cls.getName()))) {
            map = objectSupplier.get();
        } else {
            map = (Map) createInstance(context.getFeatures() | j);
        }
        if (jSONReader.isString() && !jSONReader.isTypeRedirect()) {
            String string = jSONReader.readString();
            if (!string.isEmpty()) {
                JSONReader jSONReaderOf = JSONReader.of(string, jSONReader.getContext());
                try {
                    jSONReaderOf.read(map, j);
                    if (jSONReaderOf != null) {
                        jSONReaderOf.close();
                    }
                } finally {
                }
            }
        } else {
            jSONReader.read(map, j);
        }
        jSONReader.nextIfComma();
        Function function = this.builder;
        return function != null ? function.apply(map) : map;
    }

    static Function createObjectSupplier(Class cls) {
        Function function = UNSAFE_OBJECT_CREATOR;
        if (function != null) {
            return function;
        }
        ObjectCreatorUF objectCreatorUF = new ObjectCreatorUF(cls);
        UNSAFE_OBJECT_CREATOR = objectCreatorUF;
        return objectCreatorUF;
    }

    static class ObjectCreatorUF implements Function {
        final Field map;
        final long mapOffset;
        final Class objectClass;

        ObjectCreatorUF(Class cls) {
            this.objectClass = cls;
            try {
                Field declaredField = cls.getDeclaredField("map");
                this.map = declaredField;
                this.mapOffset = JDKUtils.UNSAFE.objectFieldOffset(declaredField);
            } catch (NoSuchFieldException e) {
                throw new JSONException("field map not found", e);
            }
        }

        @Override // java.util.function.Function
        public Object apply(Object obj) {
            if (obj == null) {
                obj = new HashMap();
            }
            try {
                Object objAllocateInstance = JDKUtils.UNSAFE.allocateInstance(this.objectClass);
                JDKUtils.UNSAFE.putObject(objAllocateInstance, this.mapOffset, obj);
                return objAllocateInstance;
            } catch (InstantiationException e) {
                throw new JSONException("create " + this.objectClass.getName() + " error", e);
            }
        }
    }
}
