package com.alibaba.fastjson.util;

import androidx.exifinterface.media.ExifInterface;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.KotlinUtils;
import com.icwork.shiningglass.base.app.C;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
public class TypeUtils {
    static final long FNV1A_64_MAGIC_HASHCODE = -3750763034362895579L;
    static final long FNV1A_64_MAGIC_PRIME = 1099511628211L;
    public static boolean compatibleWithFieldName = false;
    public static boolean compatibleWithJavaBean = false;
    private static boolean setAccessibleEnable = true;
    private static Class<? extends Annotation> transientClass;
    private static boolean transientClassInited;

    public static <T> T cast(Object obj, Class<T> cls, ParserConfig parserConfig) {
        return (T) com.alibaba.fastjson2.util.TypeUtils.cast(obj, (Class) cls, parserConfig.getProvider());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T cast(Object obj, Type type, ParserConfig parserConfig) {
        if (obj == 0) {
            return null;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
        }
        if (type instanceof Class) {
            return (T) cast(obj, (Class) type, parserConfig);
        }
        if (type instanceof ParameterizedType) {
            return (T) cast(obj, (ParameterizedType) type, parserConfig);
        }
        if (type instanceof TypeVariable) {
            return obj;
        }
        throw new JSONException("can not cast to : " + type);
    }

    /* JADX WARN: Type inference failed for: r7v1, types: [T, java.util.ArrayList, java.util.Collection] */
    /* JADX WARN: Type inference failed for: r7v9, types: [T, java.util.HashMap, java.util.Map] */
    public static <T> T cast(Object obj, ParameterizedType parameterizedType, ParserConfig parserConfig) {
        Collection collection;
        Type rawType = parameterizedType.getRawType();
        if (rawType == List.class || rawType == ArrayList.class) {
            Type type = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof List) {
                List list = (List) obj;
                ?? r7 = (T) new ArrayList(list.size());
                castItemsTo(parserConfig, list, type, r7);
                return r7;
            }
        }
        if (rawType == Set.class || rawType == HashSet.class || rawType == TreeSet.class || rawType == Collection.class || rawType == List.class || rawType == ArrayList.class) {
            Type type2 = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof Iterable) {
                if (rawType == Set.class || rawType == HashSet.class) {
                    collection = (T) new HashSet();
                } else if (rawType == TreeSet.class) {
                    collection = (T) new TreeSet();
                } else {
                    collection = (T) new ArrayList();
                }
                castItemsTo(parserConfig, (Iterable) obj, type2, collection);
                return (T) collection;
            }
        }
        if (rawType == Map.class || rawType == HashMap.class) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type type3 = actualTypeArguments[0];
            Type type4 = actualTypeArguments[1];
            if (obj instanceof Map) {
                ?? r72 = (T) new HashMap();
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    r72.put(cast(entry.getKey(), type3, parserConfig), cast(entry.getValue(), type4, parserConfig));
                }
                return r72;
            }
        }
        if ((obj instanceof String) && ((String) obj).isEmpty()) {
            return null;
        }
        Type[] actualTypeArguments2 = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments2.length == 1 && (actualTypeArguments2[0] instanceof WildcardType)) {
            return (T) cast(obj, rawType, parserConfig);
        }
        if (rawType == Map.Entry.class && (obj instanceof Map)) {
            Map map = (Map) obj;
            if (map.size() == 1) {
                return (T) ((Map.Entry) map.entrySet().iterator().next());
            }
        }
        if (rawType instanceof Class) {
            if (parserConfig == null) {
                ParserConfig parserConfig2 = ParserConfig.global;
            }
            throw new JSONException("TODO : " + parameterizedType);
        }
        throw new JSONException("can not cast to : " + parameterizedType);
    }

    private static <T> void castItemsTo(ParserConfig parserConfig, Iterable iterable, Type type, Collection collection) {
        Object objCast;
        for (T t : iterable) {
            if (type instanceof Class) {
                if (t != null && t.getClass() == JSONObject.class) {
                    objCast = ((JSONObject) t).toJavaObject((Class) type, parserConfig, 0);
                } else {
                    objCast = cast((Object) t, (Class<Object>) type, parserConfig);
                }
            } else {
                objCast = cast(t, type, parserConfig);
            }
            collection.add(objCast);
        }
    }

    /* JADX WARN: Type inference failed for: r3v4, types: [T, java.util.Map] */
    public static <T> T castToJavaBean(Map<String, Object> map, Class<T> cls, ParserConfig parserConfig) {
        int iIntValue;
        try {
            if (cls == StackTraceElement.class) {
                String str = (String) map.get("className");
                String str2 = (String) map.get("methodName");
                String str3 = (String) map.get("fileName");
                Number number = (Number) map.get("lineNumber");
                if (number == null) {
                    iIntValue = 0;
                } else if (number instanceof BigDecimal) {
                    iIntValue = ((BigDecimal) number).intValueExact();
                } else {
                    iIntValue = number.intValue();
                }
                return (T) new StackTraceElement(str, str2, str3, iIntValue);
            }
            Object obj = map.get(JSON.DEFAULT_TYPE_KEY);
            if (obj instanceof String) {
                if (parserConfig == null) {
                    ParserConfig parserConfig2 = ParserConfig.global;
                }
                throw new JSONException("TODO");
            }
            if (cls.isInterface()) {
                if (map instanceof JSONObject) {
                } else {
                    new JSONObject(map);
                }
                if (parserConfig == null) {
                    ParserConfig.getGlobalInstance();
                }
                throw new JSONException("TODO");
            }
            if (cls == Locale.class) {
                Object obj2 = map.get(C.SP.LANGUAGE);
                Object obj3 = map.get("country");
                if (obj2 instanceof String) {
                    String str4 = (String) obj2;
                    if (obj3 instanceof String) {
                        return (T) new Locale(str4, (String) obj3);
                    }
                    if (obj3 == null) {
                        return (T) new Locale(str4);
                    }
                }
            }
            if (cls == String.class && (map instanceof JSONObject)) {
                return (T) map.toString();
            }
            if (cls == LinkedHashMap.class && (map instanceof JSONObject)) {
                ?? r3 = (T) ((JSONObject) map).getInnerMap();
                return r3 instanceof LinkedHashMap ? r3 : (T) new LinkedHashMap((Map) r3);
            }
            return (T) JSONFactory.getDefaultObjectReaderProvider().getObjectReader(cls).createInstance(map, 0L);
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static Type checkPrimitiveArray(GenericArrayType genericArrayType) {
        Type genericComponentType = genericArrayType.getGenericComponentType();
        String str = "[";
        while (genericComponentType instanceof GenericArrayType) {
            genericComponentType = ((GenericArrayType) genericComponentType).getGenericComponentType();
            str = str + str;
        }
        if (!(genericComponentType instanceof Class)) {
            return genericArrayType;
        }
        Class cls = (Class) genericComponentType;
        if (!cls.isPrimitive()) {
            return genericArrayType;
        }
        try {
            if (cls == Boolean.TYPE) {
                return Class.forName(str + "Z");
            }
            if (cls == Character.TYPE) {
                return Class.forName(str + "C");
            }
            if (cls == Byte.TYPE) {
                return Class.forName(str + "B");
            }
            if (cls == Short.TYPE) {
                return Class.forName(str + ExifInterface.LATITUDE_SOUTH);
            }
            if (cls == Integer.TYPE) {
                return Class.forName(str + "I");
            }
            if (cls == Long.TYPE) {
                return Class.forName(str + "J");
            }
            if (cls == Float.TYPE) {
                return Class.forName(str + "F");
            }
            return cls == Double.TYPE ? Class.forName(str + "D") : genericArrayType;
        } catch (ClassNotFoundException unused) {
            return genericArrayType;
        }
    }

    public static boolean isProxy(Class<?> cls) {
        return com.alibaba.fastjson2.util.TypeUtils.isProxy(cls);
    }

    public static boolean isGenericParamType(Type type) {
        Type genericSuperclass;
        if (type instanceof ParameterizedType) {
            return true;
        }
        return (type instanceof Class) && (genericSuperclass = ((Class) type).getGenericSuperclass()) != Object.class && isGenericParamType(genericSuperclass);
    }

    public static Type getGenericParamType(Type type) {
        return (!(type instanceof ParameterizedType) && (type instanceof Class)) ? getGenericParamType(((Class) type).getGenericSuperclass()) : type;
    }

    public static boolean isTransient(Method method) {
        if (method == null) {
            return false;
        }
        if (!transientClassInited) {
            try {
                transientClass = Class.forName("java.beans.Transient");
            } catch (Exception unused) {
            } catch (Throwable th) {
                transientClassInited = true;
                throw th;
            }
            transientClassInited = true;
        }
        Class<? extends Annotation> cls = transientClass;
        return (cls == null || getAnnotation(method, cls) == null) ? false : true;
    }

    public static String castToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static long fnv1a_64_lower(String str) {
        long j = -3750763034362895579L;
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt >= 'A' && cCharAt <= 'Z') {
                cCharAt = (char) (cCharAt + TokenParser.SP);
            }
            j = (j ^ ((long) cCharAt)) * 1099511628211L;
        }
        return j;
    }

    public static long fnv1a_64(String str) {
        long jCharAt = -3750763034362895579L;
        for (int i = 0; i < str.length(); i++) {
            jCharAt = (jCharAt ^ ((long) str.charAt(i))) * 1099511628211L;
        }
        return jCharAt;
    }

    public static long fnv1a_64_extract(String str) {
        long j = -3750763034362895579L;
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt != '_' && cCharAt != '-' && cCharAt != ' ') {
                if (cCharAt >= 'A' && cCharAt <= 'Z') {
                    cCharAt = (char) (cCharAt + TokenParser.SP);
                }
                j = (j ^ ((long) cCharAt)) * 1099511628211L;
            }
        }
        return j;
    }

    public static Long castToLong(Object obj) {
        return com.alibaba.fastjson2.util.TypeUtils.toLong(obj);
    }

    public static Integer castToInt(Object obj) {
        return com.alibaba.fastjson2.util.TypeUtils.toInteger(obj);
    }

    public static Boolean castToBoolean(Object obj) {
        return com.alibaba.fastjson2.util.TypeUtils.toBoolean(obj);
    }

    public static long longExtractValue(Number number) {
        if (number == null) {
            return 0L;
        }
        if (number instanceof BigDecimal) {
            return ((BigDecimal) number).longValueExact();
        }
        return number.longValue();
    }

    public static <A extends Annotation> A getAnnotation(Class<?> cls, Class<A> cls2) {
        A a;
        Class<?> mixInClass = getMixInClass(cls);
        return (mixInClass == null || (a = (A) getAnnotationOrCandidate(mixInClass, cls2)) == null) ? (A) getAnnotationOrCandidate(cls, cls2) : a;
    }

    private static <A extends Annotation> A getAnnotationOrCandidate(Class<?> cls, Class<A> cls2) {
        A a = (A) cls.getAnnotation(cls2);
        if (a == null) {
            for (Annotation annotation : cls.getAnnotations()) {
                a = (A) annotation.annotationType().getAnnotation(cls2);
                if (a != null) {
                    return a;
                }
            }
        }
        return a;
    }

    public static <A extends Annotation> A getAnnotation(Field field, Class<A> cls) {
        Field declaredField;
        A a;
        A a2 = (A) field.getAnnotation(cls);
        Class<?> mixInClass = getMixInClass(field.getDeclaringClass());
        if (mixInClass != null) {
            String name = field.getName();
            while (mixInClass != null && mixInClass != Object.class) {
                try {
                    declaredField = mixInClass.getDeclaredField(name);
                    break;
                } catch (NoSuchFieldException unused) {
                    mixInClass = mixInClass.getSuperclass();
                }
            }
            declaredField = null;
            if (declaredField != null && (a = (A) declaredField.getAnnotation(cls)) != null) {
                return a;
            }
        }
        return a2;
    }

    private static Class<?> getMixInClass(Class<?> cls) {
        Type mixInAnnotations = JSON.getMixInAnnotations(cls);
        if (mixInAnnotations instanceof Class) {
            return (Class) mixInAnnotations;
        }
        return null;
    }

    public static <A extends Annotation> A getAnnotation(Method method, Class<A> cls) {
        Method declaredMethod;
        A a;
        Class<?> mixInClass = getMixInClass(method.getDeclaringClass());
        if (mixInClass != null) {
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            while (mixInClass != null && mixInClass != Object.class) {
                try {
                    declaredMethod = mixInClass.getDeclaredMethod(name, parameterTypes);
                    break;
                } catch (NoSuchMethodException unused) {
                    mixInClass = mixInClass.getSuperclass();
                }
            }
            declaredMethod = null;
            if (declaredMethod != null && (a = (A) declaredMethod.getAnnotation(cls)) != null) {
                return a;
            }
        }
        return (A) method.getAnnotation(cls);
    }

    public static Double castToDouble(Object obj) {
        return com.alibaba.fastjson2.util.TypeUtils.toDouble(obj);
    }

    public static <T> T castToJavaBean(Object obj, Class<T> cls) {
        return (T) com.alibaba.fastjson2.util.TypeUtils.cast(obj, (Class) cls);
    }

    public static Class<?> getClass(Type type) {
        return com.alibaba.fastjson2.util.TypeUtils.getClass(type);
    }

    public static BigDecimal castToBigDecimal(Object obj) {
        return com.alibaba.fastjson2.util.TypeUtils.toBigDecimal(obj);
    }

    public static BigInteger castToBigInteger(Object obj) {
        return com.alibaba.fastjson2.util.TypeUtils.toBigInteger(obj);
    }

    public static Timestamp castToTimestamp(Object obj) {
        return (Timestamp) com.alibaba.fastjson2.util.TypeUtils.cast(obj, Timestamp.class);
    }

    public static Date castToSqlDate(Object obj) {
        return (Date) com.alibaba.fastjson2.util.TypeUtils.cast(obj, Date.class);
    }

    public static byte byteValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return (byte) 0;
        }
        int iScale = bigDecimal.scale();
        if (iScale >= -100 && iScale <= 100) {
            return bigDecimal.byteValue();
        }
        return bigDecimal.byteValueExact();
    }

    public static short shortValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return (short) 0;
        }
        int iScale = bigDecimal.scale();
        if (iScale >= -100 && iScale <= 100) {
            return bigDecimal.shortValue();
        }
        return bigDecimal.shortValueExact();
    }

    public static int intValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        }
        int iScale = bigDecimal.scale();
        if (iScale >= -100 && iScale <= 100) {
            return bigDecimal.intValue();
        }
        return bigDecimal.intValueExact();
    }

    public static long longValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0L;
        }
        int iScale = bigDecimal.scale();
        if (iScale >= -100 && iScale <= 100) {
            return bigDecimal.longValue();
        }
        return bigDecimal.longValueExact();
    }

    public static Character castToChar(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Character) {
            return (Character) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0) {
                return null;
            }
            if (str.length() != 1) {
                throw new JSONException("can not cast to char, value : " + obj);
            }
            return Character.valueOf(str.charAt(0));
        }
        throw new JSONException("can not cast to char, value : " + obj);
    }

    public static Short castToShort(Object obj) {
        return com.alibaba.fastjson2.util.TypeUtils.toShort(obj);
    }

    public static Byte castToByte(Object obj) {
        return com.alibaba.fastjson2.util.TypeUtils.toByte(obj);
    }

    public static Float castToFloat(Object obj) {
        return com.alibaba.fastjson2.util.TypeUtils.toFloat(obj);
    }

    public static java.util.Date castToDate(Object obj) {
        return com.alibaba.fastjson2.util.TypeUtils.toDate(obj);
    }

    public static java.util.Date castToDate(Object obj, String str) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return DateUtils.parseDate((String) obj, str, null);
        }
        return com.alibaba.fastjson2.util.TypeUtils.toDate(obj);
    }

    public static byte[] castToBytes(Object obj) {
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        if (obj instanceof String) {
            return IOUtils.decodeBase64((String) obj);
        }
        throw new JSONException("can not cast to byte[], value : " + obj);
    }

    public static List<FieldInfo> computeGetters(Class<?> cls, Map<String, String> map) {
        return computeGetters(cls, map, true);
    }

    public static List<FieldInfo> computeGetters(Class<?> cls, Map<String, String> map, boolean z) {
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        HashMap map2 = new HashMap();
        ParserConfig.parserAllFieldToCache(cls, map2);
        return computeGetters(cls, jSONType, map, map2, z, PropertyNamingStrategy.CamelCase);
    }

    /* JADX WARN: Code restructure failed: missing block: B:140:0x0293, code lost:
    
        if (r5 == null) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x044f, code lost:
    
        if (r0 == null) goto L266;
     */
    /* JADX WARN: Removed duplicated region for block: B:106:0x01f9  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0212  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x02ba  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x0378  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x03f9 A[PHI: r12
      0x03f9: PHI (r12v7 java.util.Map<java.lang.String, java.lang.reflect.Field>) = 
      (r12v6 java.util.Map<java.lang.String, java.lang.reflect.Field>)
      (r12v8 java.util.Map<java.lang.String, java.lang.reflect.Field>)
      (r12v8 java.util.Map<java.lang.String, java.lang.reflect.Field>)
     binds: [B:225:0x03f7, B:228:0x0401, B:238:0x0420] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0404  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0452 A[PHI: r0
      0x0452: PHI (r0v34 java.lang.String) = (r0v24 java.lang.String), (r0v36 java.lang.String) binds: [B:241:0x043f, B:243:0x0445] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0490  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00fb A[PHI: r6 r8 r19
      0x00fb: PHI (r6v14 java.lang.String[]) = (r6v1 java.lang.String[]), (r6v1 java.lang.String[]), (r6v16 java.lang.String[]) binds: [B:44:0x00c1, B:46:0x00cb, B:48:0x00d5] A[DONT_GENERATE, DONT_INLINE]
      0x00fb: PHI (r8v14 java.lang.annotation.Annotation[][]) = 
      (r8v1 java.lang.annotation.Annotation[][])
      (r8v1 java.lang.annotation.Annotation[][])
      (r8v16 java.lang.annotation.Annotation[][])
     binds: [B:44:0x00c1, B:46:0x00cb, B:48:0x00d5] A[DONT_GENERATE, DONT_INLINE]
      0x00fb: PHI (r19v3 java.lang.reflect.Constructor<?>[]) = 
      (r19v0 java.lang.reflect.Constructor<?>[])
      (r19v9 java.lang.reflect.Constructor<?>[])
      (r19v9 java.lang.reflect.Constructor<?>[])
     binds: [B:44:0x00c1, B:46:0x00cb, B:48:0x00d5] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0132  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0163  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x016e  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0185  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<com.alibaba.fastjson.util.FieldInfo> computeGetters(java.lang.Class<?> r30, com.alibaba.fastjson.annotation.JSONType r31, java.util.Map<java.lang.String, java.lang.String> r32, java.util.Map<java.lang.String, java.lang.reflect.Field> r33, boolean r34, com.alibaba.fastjson.PropertyNamingStrategy r35) {
        /*
            Method dump skipped, instruction units count: 1208
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.computeGetters(java.lang.Class, com.alibaba.fastjson.annotation.JSONType, java.util.Map, java.util.Map, boolean, com.alibaba.fastjson.PropertyNamingStrategy):java.util.List");
    }

    private static void computeFields(Class<?> cls, Map<String, String> map, PropertyNamingStrategy propertyNamingStrategy, Map<String, FieldInfo> map2, Field[] fieldArr) {
        int i;
        int i2;
        int i3;
        String strLabel;
        for (Field field : fieldArr) {
            if (!Modifier.isStatic(field.getModifiers())) {
                JSONField jSONField = (JSONField) getAnnotation(field, JSONField.class);
                String name = field.getName();
                if (jSONField == null) {
                    i = 0;
                    i2 = 0;
                    i3 = 0;
                    strLabel = null;
                } else if (jSONField.serialize()) {
                    int iOrdinal = jSONField.ordinal();
                    int iOf = SerializerFeature.of(jSONField.serialzeFeatures());
                    int iOf2 = Feature.of(jSONField.parseFeatures());
                    if (jSONField.name().length() != 0) {
                        name = jSONField.name();
                    }
                    strLabel = jSONField.label().length() != 0 ? jSONField.label() : null;
                    i = iOrdinal;
                    i2 = iOf;
                    i3 = iOf2;
                }
                if (map == null || (name = map.get(name)) != null) {
                    if (propertyNamingStrategy != null) {
                        name = propertyNamingStrategy.translate(name);
                    }
                    String str = name;
                    if (!map2.containsKey(str)) {
                        map2.put(str, new FieldInfo(str, null, field, cls, null, i, i2, i3, null, jSONField, strLabel));
                    }
                }
            }
        }
    }

    private static List<FieldInfo> getFieldInfos(Class<?> cls, boolean z, Map<String, FieldInfo> map) {
        ArrayList arrayList = new ArrayList();
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        String[] strArrOrders = jSONType != null ? jSONType.orders() : null;
        if (strArrOrders != null && strArrOrders.length > 0) {
            LinkedHashMap linkedHashMap = new LinkedHashMap(map.size());
            for (FieldInfo fieldInfo : map.values()) {
                linkedHashMap.put(fieldInfo.name, fieldInfo);
            }
            for (String str : strArrOrders) {
                FieldInfo fieldInfo2 = (FieldInfo) linkedHashMap.get(str);
                if (fieldInfo2 != null) {
                    arrayList.add(fieldInfo2);
                    linkedHashMap.remove(str);
                }
            }
            arrayList.addAll(linkedHashMap.values());
            return arrayList;
        }
        arrayList.addAll(map.values());
        if (z) {
            Collections.sort(arrayList);
        }
        return arrayList;
    }

    static void setAccessible(AccessibleObject accessibleObject) {
        if (setAccessibleEnable && !accessibleObject.isAccessible()) {
            try {
                accessibleObject.setAccessible(true);
            } catch (Throwable unused) {
                setAccessibleEnable = false;
            }
        }
    }

    public static boolean isKotlin(Class cls) {
        return KotlinUtils.isKotlin(cls);
    }

    public static Constructor getKotlinConstructor(Constructor[] constructorArr) {
        return getKotlinConstructor(constructorArr, null);
    }

    public static Constructor getKotlinConstructor(Constructor[] constructorArr, String[] strArr) {
        return KotlinUtils.getKotlinConstructor(constructorArr, strArr);
    }

    public static String[] getKoltinConstructorParameters(Class cls) {
        return KotlinUtils.getKoltinConstructorParameters(cls);
    }

    static boolean isKotlinIgnore(Class cls, String str) {
        return KotlinUtils.isKotlinIgnore(cls, str);
    }

    private static boolean isJSONTypeIgnore(Class<?> cls, String str) {
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        if (jSONType != null) {
            String[] strArrIncludes = jSONType.includes();
            if (strArrIncludes.length > 0) {
                for (String str2 : strArrIncludes) {
                    if (str.equals(str2)) {
                        return false;
                    }
                }
                return true;
            }
            for (String str3 : jSONType.ignores()) {
                if (str.equals(str3)) {
                    return true;
                }
            }
        }
        if (cls.getSuperclass() == Object.class || cls.getSuperclass() == null) {
            return false;
        }
        return isJSONTypeIgnore(cls.getSuperclass(), str);
    }

    public static JSONField getSuperMethodAnnotation(Class<?> cls, Method method) {
        Class<?>[] interfaces = cls.getInterfaces();
        if (interfaces.length > 0) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> cls2 : interfaces) {
                for (Method method2 : cls2.getMethods()) {
                    Class<?>[] parameterTypes2 = method2.getParameterTypes();
                    if (parameterTypes2.length == parameterTypes.length && method2.getName().equals(method.getName())) {
                        int i = 0;
                        while (true) {
                            if (i < parameterTypes.length) {
                                if (!parameterTypes2[i].equals(parameterTypes[i])) {
                                    break;
                                }
                                i++;
                            } else {
                                JSONField jSONField = (JSONField) getAnnotation(method2, JSONField.class);
                                if (jSONField != null) {
                                    return jSONField;
                                }
                            }
                        }
                    }
                }
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass != null && Modifier.isAbstract(superclass.getModifiers())) {
            Class<?>[] parameterTypes3 = method.getParameterTypes();
            for (Method method3 : superclass.getMethods()) {
                Class<?>[] parameterTypes4 = method3.getParameterTypes();
                if (parameterTypes4.length == parameterTypes3.length && method3.getName().equals(method.getName())) {
                    int i2 = 0;
                    while (true) {
                        if (i2 < parameterTypes3.length) {
                            if (!parameterTypes4[i2].equals(parameterTypes3[i2])) {
                                break;
                            }
                            i2++;
                        } else {
                            JSONField jSONField2 = (JSONField) getAnnotation(method3, JSONField.class);
                            if (jSONField2 != null) {
                                return jSONField2;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String getPropertyNameByCompatibleFieldName(Map<String, Field> map, String str, String str2, int i) {
        if (compatibleWithFieldName && !map.containsKey(str2)) {
            String strSubstring = str.substring(i);
            if (map.containsKey(strSubstring)) {
                return strSubstring;
            }
        }
        return str2;
    }

    public static String decapitalize(String str) {
        if (str == null || str.length() == 0 || (str.length() > 1 && Character.isUpperCase(str.charAt(1)) && Character.isUpperCase(str.charAt(0)))) {
            return str;
        }
        char[] charArray = str.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        return new String(charArray);
    }

    public static String getPropertyNameByMethodName(String str) {
        return Character.toLowerCase(str.charAt(3)) + str.substring(4);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x005c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.annotation.Annotation[][] getParameterAnnotations(java.lang.reflect.Constructor r9) {
        /*
            java.lang.Class r0 = r9.getDeclaringClass()
            java.lang.Class r0 = getMixInClass(r0)
            if (r0 == 0) goto L64
            java.lang.Class[] r1 = r9.getParameterTypes()
            java.util.ArrayList r2 = new java.util.ArrayList
            r3 = 2
            r2.<init>(r3)
            java.lang.Class r3 = r0.getEnclosingClass()
        L18:
            if (r3 == 0) goto L22
            r2.add(r3)
            java.lang.Class r3 = r3.getEnclosingClass()
            goto L18
        L22:
            int r3 = r2.size()
            r4 = r0
        L27:
            if (r4 == 0) goto L59
            java.lang.Class<java.lang.Object> r5 = java.lang.Object.class
            if (r4 == r5) goto L59
            if (r3 == 0) goto L4d
            int r5 = r1.length     // Catch: java.lang.NoSuchMethodException -> L52
            int r5 = r5 + r3
            java.lang.Class[] r5 = new java.lang.Class[r5]     // Catch: java.lang.NoSuchMethodException -> L52
            int r6 = r1.length     // Catch: java.lang.NoSuchMethodException -> L52
            r7 = 0
            java.lang.System.arraycopy(r1, r7, r5, r3, r6)     // Catch: java.lang.NoSuchMethodException -> L52
            r6 = r3
        L39:
            if (r6 <= 0) goto L48
            int r7 = r6 + (-1)
            java.lang.Object r8 = r2.get(r7)     // Catch: java.lang.NoSuchMethodException -> L52
            java.lang.Class r8 = (java.lang.Class) r8     // Catch: java.lang.NoSuchMethodException -> L52
            r5[r7] = r8     // Catch: java.lang.NoSuchMethodException -> L52
            int r6 = r6 + (-1)
            goto L39
        L48:
            java.lang.reflect.Constructor r0 = r0.getDeclaredConstructor(r5)     // Catch: java.lang.NoSuchMethodException -> L52
            goto L5a
        L4d:
            java.lang.reflect.Constructor r0 = r0.getDeclaredConstructor(r1)     // Catch: java.lang.NoSuchMethodException -> L52
            goto L5a
        L52:
            int r3 = r3 + (-1)
            java.lang.Class r4 = r4.getSuperclass()
            goto L27
        L59:
            r0 = 0
        L5a:
            if (r0 == 0) goto L64
            java.lang.annotation.Annotation[][] r0 = r0.getParameterAnnotations()
            int r1 = r0.length
            if (r1 != 0) goto L64
            return r0
        L64:
            java.lang.annotation.Annotation[][] r9 = r9.getParameterAnnotations()
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.getParameterAnnotations(java.lang.reflect.Constructor):java.lang.annotation.Annotation[][]");
    }

    public static class MethodInheritanceComparator implements Comparator<Method> {
        @Override // java.util.Comparator
        public int compare(Method method, Method method2) {
            int iCompareTo = method.getName().compareTo(method2.getName());
            if (iCompareTo != 0) {
                return iCompareTo;
            }
            Class<?> returnType = method.getReturnType();
            Class<?> returnType2 = method2.getReturnType();
            if (returnType.equals(returnType2)) {
                return 0;
            }
            if (returnType.isAssignableFrom(returnType2)) {
                return -1;
            }
            return returnType2.isAssignableFrom(returnType) ? 1 : 0;
        }
    }
}
