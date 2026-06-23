package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.AfterFilter;
import com.alibaba.fastjson2.filter.BeforeFilter;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes.dex */
public final class ObjectWriterImplMap extends ObjectWriterPrimitiveImpl {
    static final ObjectWriterImplMap INSTANCE_1x;
    final boolean contentAs;
    final long features;
    final String format;
    final boolean jsonObject1;
    final Field jsonObject1InnerMap;
    final long jsonObject1InnerMapOffset;
    final byte[] jsonbTypeInfo;
    final Type keyType;
    volatile ObjectWriter keyWriter;
    final Class objectClass;
    final Type objectType;
    final char[] typeInfoUTF16;
    final byte[] typeInfoUTF8;
    final long typeNameHash;
    final Type valueType;
    final boolean valueTypeRefDetect;
    volatile ObjectWriter valueWriter;
    static final byte[] TYPE_NAME_JSONObject1O = JSONB.toBytes("JO10");
    static final long TYPE_HASH_JSONObject1O = Fnv.hashCode64("JO10");
    static final ObjectWriterImplMap INSTANCE = new ObjectWriterImplMap(String.class, Object.class, JSONObject.class, JSONObject.class, 0);

    static {
        if (TypeUtils.CLASS_JSON_OBJECT_1x == null) {
            INSTANCE_1x = null;
        } else {
            INSTANCE_1x = new ObjectWriterImplMap(String.class, Object.class, TypeUtils.CLASS_JSON_OBJECT_1x, TypeUtils.CLASS_JSON_OBJECT_1x, 0L);
        }
    }

    public ObjectWriterImplMap(Class cls, long j) {
        this(null, null, cls, cls, j);
    }

    public ObjectWriterImplMap(Type type, Type type2, Class cls, Type type3, long j) {
        this(type, type2, null, cls, type3, j);
    }

    public ObjectWriterImplMap(Type type, Type type2, String str, Class cls, Type type3, long j) {
        long jObjectFieldOffset;
        this.keyType = type;
        this.valueType = type2;
        this.format = str;
        this.objectClass = cls;
        this.objectType = type3;
        this.features = j;
        if (type2 == null) {
            this.valueTypeRefDetect = true;
        } else {
            this.valueTypeRefDetect = !ObjectWriterProvider.isNotReferenceDetect(TypeUtils.getClass(type2));
        }
        this.contentAs = (Long.MIN_VALUE & j) != 0;
        String typeName = TypeUtils.getTypeName(cls);
        String str2 = "\"@type\":\"" + cls.getName() + "\"";
        this.typeInfoUTF16 = str2.toCharArray();
        this.typeInfoUTF8 = str2.getBytes(StandardCharsets.UTF_8);
        boolean zEquals = "JO1".equals(typeName);
        this.jsonObject1 = zEquals;
        this.jsonbTypeInfo = JSONB.toBytes(typeName);
        this.typeNameHash = Fnv.hashCode64(typeName);
        if (zEquals) {
            Field declaredField = BeanUtils.getDeclaredField(cls, "map");
            this.jsonObject1InnerMap = declaredField;
            if (declaredField != null) {
                declaredField.setAccessible(true);
                jObjectFieldOffset = JDKUtils.UNSAFE.objectFieldOffset(declaredField);
            }
            this.jsonObject1InnerMapOffset = jObjectFieldOffset;
        }
        this.jsonObject1InnerMap = null;
        jObjectFieldOffset = -1;
        this.jsonObject1InnerMapOffset = jObjectFieldOffset;
    }

    public static ObjectWriterImplMap of(Class cls) {
        if (cls == JSONObject.class) {
            return INSTANCE;
        }
        if (cls == TypeUtils.CLASS_JSON_OBJECT_1x) {
            return INSTANCE_1x;
        }
        return new ObjectWriterImplMap(null, null, cls, cls, 0L);
    }

    public static ObjectWriterImplMap of(Type type) {
        return new ObjectWriterImplMap(TypeUtils.getClass(type), 0L);
    }

    public static ObjectWriterImplMap of(Type type, Class cls) {
        return of(type, null, cls);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.alibaba.fastjson2.writer.ObjectWriterImplMap of(java.lang.reflect.Type r10, java.lang.String r11, java.lang.Class r12) {
        /*
            boolean r0 = r10 instanceof java.lang.reflect.ParameterizedType
            if (r0 == 0) goto L1b
            r0 = r10
            java.lang.reflect.ParameterizedType r0 = (java.lang.reflect.ParameterizedType) r0
            r0.getRawType()
            java.lang.reflect.Type[] r0 = r0.getActualTypeArguments()
            int r1 = r0.length
            r2 = 2
            if (r1 != r2) goto L1b
            r1 = 0
            r1 = r0[r1]
            r2 = 1
            r0 = r0[r2]
            r4 = r0
            r3 = r1
            goto L1e
        L1b:
            r1 = 0
            r3 = r1
            r4 = r3
        L1e:
            com.alibaba.fastjson2.writer.ObjectWriterImplMap r2 = new com.alibaba.fastjson2.writer.ObjectWriterImplMap
            r8 = 0
            r7 = r10
            r5 = r11
            r6 = r12
            r2.<init>(r3, r4, r5, r6, r7, r8)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterImplMap.of(java.lang.reflect.Type, java.lang.String, java.lang.Class):com.alibaba.fastjson2.writer.ObjectWriterImplMap");
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriterPrimitiveImpl, com.alibaba.fastjson2.writer.ObjectWriter
    public void writeArrayMappingJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        JSONWriter jSONWriter2;
        jSONWriter.startObject();
        boolean zIsWriteNulls = jSONWriter.isWriteNulls();
        for (Map.Entry entry : ((Map) obj).entrySet()) {
            String str = (String) entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                jSONWriter.writeString(str);
                Class<?> cls = value.getClass();
                if (cls == String.class) {
                    jSONWriter.writeString((String) value);
                    jSONWriter2 = jSONWriter;
                } else {
                    jSONWriter2 = jSONWriter;
                    jSONWriter.getObjectWriter(cls).writeJSONB(jSONWriter2, value, str, this.valueType, this.features);
                }
                jSONWriter = jSONWriter2;
            } else if (zIsWriteNulls) {
                jSONWriter.writeString(str);
                jSONWriter.writeNull();
            }
        }
        jSONWriter.endObject();
    }

    /* JADX WARN: Removed duplicated region for block: B:129:0x024e  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0045  */
    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void writeJSONB(com.alibaba.fastjson2.JSONWriter r27, java.lang.Object r28, java.lang.Object r29, java.lang.reflect.Type r30, long r31) {
        /*
            Method dump skipped, instruction units count: 706
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterImplMap.writeJSONB(com.alibaba.fastjson2.JSONWriter, java.lang.Object, java.lang.Object, java.lang.reflect.Type, long):void");
    }

    static boolean isWriteAsString(Object obj, long j) {
        return ((j & (JSONWriter.Feature.WriteNonStringKeyAsString.mask | JSONWriter.Feature.BrowserCompatible.mask)) == 0 || !ObjectWriterProvider.isPrimitiveOrEnum(obj.getClass()) || (obj instanceof Temporal) || (obj instanceof Date)) ? false : true;
    }

    String mapKeyToString(Object obj, JSONWriter jSONWriter, long j) throws Throwable {
        int length;
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if ((obj instanceof Integer) || (obj instanceof Long)) {
            return obj.toString();
        }
        if (isWriteAsString(obj, j)) {
            return obj.toString();
        }
        String jSONString = JSON.toJSONString(obj, jSONWriter.getContext());
        if (jSONString == null || (length = jSONString.length()) <= 1) {
            return jSONString;
        }
        char c = jSONWriter.useSingleQuote ? '\'' : '\"';
        if (jSONString.charAt(0) != c) {
            return jSONString;
        }
        int i = length - 1;
        return jSONString.charAt(i) == c ? jSONString.substring(1, i) : jSONString;
    }

    String writeMapKey(Object obj, JSONWriter jSONWriter, long j) {
        if (obj == null) {
            jSONWriter.writeName("null");
            return null;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            jSONWriter.writeName(str);
            return str;
        }
        if (isWriteAsString(obj, j)) {
            String string = obj.toString();
            jSONWriter.writeName(string);
            return string;
        }
        if (obj instanceof Integer) {
            jSONWriter.writeName(((Integer) obj).intValue());
            return null;
        }
        if (obj instanceof Long) {
            jSONWriter.writeName(((Long) obj).longValue());
            return null;
        }
        jSONWriter.writeNameAny(obj);
        return null;
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public boolean writeTypeInfo(JSONWriter jSONWriter) {
        if (jSONWriter.utf8) {
            jSONWriter.writeNameRaw(this.typeInfoUTF8);
            return true;
        }
        jSONWriter.writeNameRaw(this.typeInfoUTF16);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:121:0x01f3  */
    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void write(com.alibaba.fastjson2.JSONWriter r24, java.lang.Object r25, java.lang.Object r26, java.lang.reflect.Type r27, long r28) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 517
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterImplMap.write(com.alibaba.fastjson2.JSONWriter, java.lang.Object, java.lang.Object, java.lang.reflect.Type, long):void");
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeWithFilter(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) throws Throwable {
        long j2;
        Class<?> cls;
        PropertyFilter propertyFilter;
        AfterFilter afterFilter;
        String path;
        if (obj == null) {
            jSONWriter.writeNull();
            return;
        }
        jSONWriter.startObject();
        Map treeMap = (Map) obj;
        long features = j | jSONWriter.getFeatures();
        long j3 = 0;
        if (((JSONWriter.Feature.MapSortField.mask | JSONWriter.Feature.SortMapEntriesByKeys.mask) & features) != 0 && !(treeMap instanceof SortedMap) && (treeMap.getClass() != LinkedHashMap.class || (JSONWriter.Feature.SortMapEntriesByKeys.mask & features) != 0)) {
            treeMap = new TreeMap(treeMap);
        }
        JSONWriter.Context context = jSONWriter.context;
        BeforeFilter beforeFilter = context.getBeforeFilter();
        if (beforeFilter != null) {
            beforeFilter.writeBefore(jSONWriter, obj);
        }
        PropertyPreFilter propertyPreFilter = context.getPropertyPreFilter();
        NameFilter nameFilter = context.getNameFilter();
        ValueFilter valueFilter = context.getValueFilter();
        PropertyFilter propertyFilter2 = context.getPropertyFilter();
        AfterFilter afterFilter2 = context.getAfterFilter();
        boolean zIsEnabled = context.isEnabled(JSONWriter.Feature.WriteNulls.mask);
        boolean zIsEnabled2 = context.isEnabled(JSONWriter.Feature.ReferenceDetection.mask);
        for (Map.Entry entry : treeMap.entrySet()) {
            Object value = entry.getValue();
            if (value != null || zIsEnabled) {
                String strMapKeyToString = mapKeyToString(entry.getKey(), jSONWriter, features);
                if (zIsEnabled2 && (path = jSONWriter.setPath(strMapKeyToString, value)) != null) {
                    jSONWriter.writeName(strMapKeyToString);
                    jSONWriter.writeColon();
                    jSONWriter.writeReference(path);
                    jSONWriter.popPath(value);
                } else {
                    if (propertyPreFilter != null) {
                        try {
                            if (!propertyPreFilter.process(jSONWriter, obj, strMapKeyToString)) {
                                if (zIsEnabled2) {
                                    jSONWriter.popPath(value);
                                }
                            }
                        } finally {
                            if (zIsEnabled2) {
                                jSONWriter.popPath(value);
                            }
                        }
                    }
                    if (nameFilter != null) {
                        strMapKeyToString = nameFilter.process(obj, strMapKeyToString, value);
                    }
                    if (propertyFilter2 == null || propertyFilter2.apply(obj, strMapKeyToString, value)) {
                        if (valueFilter != null) {
                            value = valueFilter.apply(obj, strMapKeyToString, value);
                        }
                        if (value == null) {
                            j2 = j3;
                            if ((jSONWriter.getFeatures(features) & JSONWriter.Feature.WriteNulls.mask) == j2) {
                            }
                            j3 = j2;
                        } else {
                            j2 = j3;
                        }
                        jSONWriter.writeName(strMapKeyToString);
                        jSONWriter.writeColon();
                        if (value == null) {
                            jSONWriter.writeNull();
                            propertyFilter = propertyFilter2;
                            afterFilter = afterFilter2;
                        } else {
                            if (this.contentAs) {
                                cls = (Class) this.valueType;
                            } else {
                                cls = value.getClass();
                            }
                            propertyFilter = propertyFilter2;
                            afterFilter = afterFilter2;
                            jSONWriter.getObjectWriter(cls).write(jSONWriter, value, obj2, type, this.features);
                        }
                        if (zIsEnabled2) {
                            jSONWriter.popPath(value);
                        }
                        propertyFilter2 = propertyFilter;
                        afterFilter2 = afterFilter;
                        j3 = j2;
                    } else if (zIsEnabled2) {
                        jSONWriter.popPath(value);
                    }
                }
            }
        }
        AfterFilter afterFilter3 = afterFilter2;
        if (afterFilter3 != null) {
            afterFilter3.writeAfter(jSONWriter, obj);
        }
        jSONWriter.endObject();
    }
}
