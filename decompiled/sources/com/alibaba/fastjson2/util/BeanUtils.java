package com.alibaba.fastjson2.util;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.annotation.JSONType;
import com.alibaba.fastjson2.codec.BeanInfo;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import okhttp3.HttpUrl;
import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
public abstract class BeanUtils {
    private static volatile Class RECORD_CLASS = null;
    private static volatile Method RECORD_COMPONENT_GET_NAME = null;
    private static volatile Method RECORD_GET_RECORD_COMPONENTS = null;
    public static final String SUPER = "$super$";
    static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
    static final ConcurrentMap<Class, Field[]> fieldCache = new ConcurrentHashMap();
    static final ConcurrentMap<Class, Map<String, Field>> fieldMapCache = new ConcurrentHashMap();
    static final ConcurrentMap<Class, Field[]> declaredFieldCache = new ConcurrentHashMap();
    static final ConcurrentMap<Class, Method[]> methodCache = new ConcurrentHashMap();
    static final ConcurrentMap<Class, Constructor[]> constructorCache = new ConcurrentHashMap();
    static final long[] IGNORE_CLASS_HASH_CODES = {-9214723784238596577L, -9030616758866828325L, -8335274122997354104L, -6963030519018899258L, -4863137578837233966L, -3653547262287832698L, -2819277587813726773L, -2669552864532011468L, -2458634727370886912L, -2291619803571459675L, -1811306045128064037L, -864440709753525476L, -779604756358333743L, 8731803887940231L, 1616814008855344660L, 2164749833121980361L, 2688642392827789427L, 3724195282986200606L, 3742915795806478647L, 3977020351318456359L, 4775491097662790952L, 4882459834864833642L, 6033839080488254886L, 7981148566008458638L, 8344106065386396833L, 9215465129261900012L};

    public static String[] getRecordFieldNames(Class<?> cls) {
        if (JDKUtils.JVM_VERSION < 14 && JDKUtils.ANDROID_SDK_INT < 33) {
            return new String[0];
        }
        try {
            if (RECORD_GET_RECORD_COMPONENTS == null) {
                RECORD_GET_RECORD_COMPONENTS = Class.class.getMethod("getRecordComponents", new Class[0]);
            }
            if (RECORD_COMPONENT_GET_NAME == null) {
                RECORD_COMPONENT_GET_NAME = Class.forName("java.lang.reflect.RecordComponent").getMethod("getName", new Class[0]);
            }
            Object[] objArr = (Object[]) RECORD_GET_RECORD_COMPONENTS.invoke(cls, new Object[0]);
            String[] strArr = new String[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                strArr[i] = (String) RECORD_COMPONENT_GET_NAME.invoke(objArr[i], new Object[0]);
            }
            return strArr;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to access Methods needed to support `java.lang.Record`: (%s) %s", e.getClass().getName(), e.getMessage()), e);
        }
    }

    public static void fields(Class cls, Consumer<Field> consumer) {
        if (TypeUtils.isProxy(cls)) {
            fields(cls.getSuperclass(), consumer);
            return;
        }
        ConcurrentMap<Class, Field[]> concurrentMap = fieldCache;
        Field[] fields = concurrentMap.get(cls);
        if (fields == null) {
            fields = cls.getFields();
            concurrentMap.putIfAbsent(cls, fields);
        }
        boolean zIsAssignableFrom = Enum.class.isAssignableFrom(cls);
        for (Field field : fields) {
            if ((!Modifier.isStatic(field.getModifiers()) || zIsAssignableFrom) && !ignore(field.getType())) {
                consumer.accept(field);
            }
        }
    }

    public static Method getMethod(Class cls, String str) {
        ConcurrentMap<Class, Method[]> concurrentMap = methodCache;
        Method[] methods = concurrentMap.get(cls);
        if (methods == null) {
            methods = getMethods(cls);
            concurrentMap.putIfAbsent(cls, methods);
        }
        for (Method method : methods) {
            if (method.getName().equals(str)) {
                return method;
            }
        }
        return null;
    }

    public static Method fluentSetter(Class cls, String str, Class cls2) {
        ConcurrentMap<Class, Method[]> concurrentMap = methodCache;
        Method[] methods = concurrentMap.get(cls);
        if (methods == null) {
            methods = getMethods(cls);
            concurrentMap.putIfAbsent(cls, methods);
        }
        for (Method method : methods) {
            if (method.getName().equals(str) && method.getReturnType() == cls && method.getParameterCount() == 1 && method.getParameterTypes()[0] == cls2) {
                return method;
            }
        }
        return null;
    }

    public static Method getMethod(Class cls, Method method) {
        if (cls != null && cls != Object.class && cls != Serializable.class) {
            ConcurrentMap<Class, Method[]> concurrentMap = methodCache;
            Method[] methods = concurrentMap.get(cls);
            if (methods == null) {
                methods = getMethods(cls);
                concurrentMap.putIfAbsent(cls, methods);
            }
            for (Method method2 : methods) {
                if (method2.getName().equals(method.getName()) && method2.getParameterCount() == method.getParameterCount()) {
                    Class<?>[] parameterTypes = method2.getParameterTypes();
                    Class<?>[] parameterTypes2 = method.getParameterTypes();
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (!parameterTypes[i].equals(parameterTypes2[i])) {
                            break;
                        }
                    }
                    return method2;
                }
            }
        }
        return null;
    }

    public static Field getDeclaredField(Class cls, String str) {
        ConcurrentMap<Class, Map<String, Field>> concurrentMap = fieldMapCache;
        Map<String, Field> map = concurrentMap.get(cls);
        if (map == null) {
            final HashMap map2 = new HashMap();
            declaredFields(cls, new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda9
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Field field = (Field) obj;
                    map2.put(field.getName(), field);
                }
            });
            concurrentMap.putIfAbsent(cls, map2);
            map = concurrentMap.get(cls);
        }
        return map.get(str);
    }

    public static Method getSetter(Class cls, final String str) {
        final Method[] methodArr = new Method[1];
        setters(cls, new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$getSetter$1(str, methodArr, (Method) obj);
            }
        });
        return methodArr[0];
    }

    static /* synthetic */ void lambda$getSetter$1(String str, Method[] methodArr, Method method) {
        if (str.equals(method.getName())) {
            methodArr[0] = method;
        }
    }

    public static void declaredFields(Class cls, Consumer<Field> consumer) {
        boolean zEquals;
        if (cls == null || consumer == null || ignore(cls) || cls.getName().contains("$$Lambda") || JdbcSupport.isStruct(cls)) {
            return;
        }
        if (TypeUtils.isProxy(cls)) {
            declaredFields(cls.getSuperclass(), consumer);
            return;
        }
        Class superclass = cls.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            zEquals = false;
        } else {
            zEquals = "com.google.protobuf.GeneratedMessageV3".equals(superclass.getName());
            if (!zEquals) {
                declaredFields(superclass, consumer);
            }
        }
        ConcurrentMap<Class, Field[]> concurrentMap = declaredFieldCache;
        Field[] declaredFields = concurrentMap.get(cls);
        if (declaredFields == null) {
            try {
                declaredFields = cls.getDeclaredFields();
                concurrentMap.put(cls, declaredFields);
            } catch (Throwable unused) {
                declaredFields = new Field[0];
            }
            int length = declaredFields.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (Modifier.isStatic(declaredFields[i].getModifiers())) {
                    boolean zIsAssignableFrom = Enum.class.isAssignableFrom(cls);
                    ArrayList arrayList = new ArrayList(declaredFields.length);
                    for (Field field : declaredFields) {
                        if (zIsAssignableFrom || !Modifier.isStatic(field.getModifiers())) {
                            arrayList.add(field);
                        }
                    }
                    declaredFields = (Field[]) arrayList.toArray(new Field[arrayList.size()]);
                } else {
                    i++;
                }
            }
            fieldCache.putIfAbsent(cls, declaredFields);
        }
        for (Field field2 : declaredFields) {
            int modifiers = field2.getModifiers();
            Class<?> type = field2.getType();
            if ((modifiers & 8) == 0 && !ignore(type)) {
                if (zEquals && "cardsmap_".equals(field2.getName()) && "com.google.protobuf.MapField".equals(type.getName())) {
                    return;
                }
                Class<?> declaringClass = field2.getDeclaringClass();
                if (declaringClass != AbstractMap.class && declaringClass != HashMap.class && declaringClass != LinkedHashMap.class && declaringClass != TreeMap.class && declaringClass != ConcurrentHashMap.class) {
                    consumer.accept(field2);
                }
            }
        }
    }

    public static void staticMethod(Class cls, Consumer<Method> consumer) {
        ConcurrentMap<Class, Method[]> concurrentMap = methodCache;
        Method[] methods = concurrentMap.get(cls);
        if (methods == null) {
            methods = getMethods(cls);
            concurrentMap.putIfAbsent(cls, methods);
        }
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers())) {
                consumer.accept(method);
            }
        }
    }

    public static Method buildMethod(Class cls, String str) {
        ConcurrentMap<Class, Method[]> concurrentMap = methodCache;
        Method[] methods = concurrentMap.get(cls);
        if (methods == null) {
            methods = getMethods(cls);
            concurrentMap.putIfAbsent(cls, methods);
        }
        for (Method method : methods) {
            if (!Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0 && method.getName().equals(str)) {
                return method;
            }
        }
        return null;
    }

    public static void constructor(Class cls, Consumer<Constructor> consumer) {
        ConcurrentMap<Class, Constructor[]> concurrentMap = constructorCache;
        Constructor<?>[] declaredConstructors = concurrentMap.get(cls);
        if (declaredConstructors == null) {
            declaredConstructors = cls.getDeclaredConstructors();
            concurrentMap.putIfAbsent(cls, declaredConstructors);
        }
        boolean zIsRecord = isRecord(cls);
        for (Constructor<?> constructor : declaredConstructors) {
            if (!zIsRecord || constructor.getParameterCount() != 0) {
                consumer.accept(constructor);
            }
        }
    }

    public static Constructor[] getConstructor(Class cls) {
        ConcurrentMap<Class, Constructor[]> concurrentMap = constructorCache;
        Constructor[] constructorArr = concurrentMap.get(cls);
        if (constructorArr != null) {
            return constructorArr;
        }
        Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
        concurrentMap.putIfAbsent(cls, declaredConstructors);
        return declaredConstructors;
    }

    public static boolean hasPublicDefaultConstructor(Class cls) {
        Constructor defaultConstructor = getDefaultConstructor(cls, false);
        return defaultConstructor != null && Modifier.isPublic(defaultConstructor.getModifiers());
    }

    public static Constructor getDefaultConstructor(Class cls, boolean z) {
        Class<?> declaringClass;
        if ((cls == StackTraceElement.class && JDKUtils.JVM_VERSION >= 9) || isRecord(cls)) {
            return null;
        }
        ConcurrentMap<Class, Constructor[]> concurrentMap = constructorCache;
        Constructor[] declaredConstructors = concurrentMap.get(cls);
        if (declaredConstructors == null) {
            declaredConstructors = cls.getDeclaredConstructors();
            concurrentMap.putIfAbsent(cls, declaredConstructors);
        }
        for (Constructor<?> constructor : declaredConstructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        if (z && (declaringClass = cls.getDeclaringClass()) != null) {
            for (Constructor<?> constructor2 : declaredConstructors) {
                if (constructor2.getParameterCount() == 1 && declaringClass.equals(constructor2.getParameterTypes()[0])) {
                    return constructor2;
                }
            }
        }
        return null;
    }

    public static void setters(Class cls, Consumer<Method> consumer) {
        setters(cls, null, null, consumer);
    }

    public static void setters(Class cls, Class cls2, Consumer<Method> consumer) {
        setters(cls, null, cls2, consumer);
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:123:0x01ab  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x01ae A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00cf  */
    /* JADX WARN: Type inference failed for: r12v5 */
    /* JADX WARN: Type inference failed for: r12v6, types: [int] */
    /* JADX WARN: Type inference failed for: r12v8 */
    /* JADX WARN: Type inference failed for: r14v0 */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v10 */
    /* JADX WARN: Type inference failed for: r14v11 */
    /* JADX WARN: Type inference failed for: r14v12 */
    /* JADX WARN: Type inference failed for: r14v13 */
    /* JADX WARN: Type inference failed for: r14v6 */
    /* JADX WARN: Type inference failed for: r14v7 */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* JADX WARN: Type inference failed for: r14v9 */
    /* JADX WARN: Type inference failed for: r6v0 */
    /* JADX WARN: Type inference failed for: r6v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r6v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void setters(java.lang.Class r16, com.alibaba.fastjson2.codec.BeanInfo r17, java.lang.Class r18, java.util.function.Consumer<java.lang.reflect.Method> r19) {
        /*
            Method dump skipped, instruction units count: 466
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.BeanUtils.setters(java.lang.Class, com.alibaba.fastjson2.codec.BeanInfo, java.lang.Class, java.util.function.Consumer):void");
    }

    static /* synthetic */ void lambda$setters$2(Annotation annotation, AtomicBoolean atomicBoolean, Method method) {
        try {
            if ("unwrapped".equals(method.getName()) && ((Boolean) method.invoke(annotation, new Object[0])).booleanValue()) {
                atomicBoolean.set(true);
            }
        } catch (Throwable unused) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void setters(java.lang.Class r7, boolean r8, java.util.function.Consumer<java.lang.reflect.Method> r9) {
        /*
            boolean r0 = ignore(r7)
            if (r0 == 0) goto L8
            goto L89
        L8:
            java.util.concurrent.ConcurrentMap<java.lang.Class, java.lang.reflect.Method[]> r0 = com.alibaba.fastjson2.util.BeanUtils.methodCache
            java.lang.Object r1 = r0.get(r7)
            java.lang.reflect.Method[] r1 = (java.lang.reflect.Method[]) r1
            if (r1 != 0) goto L19
            java.lang.reflect.Method[] r1 = getMethods(r7)
            r0.putIfAbsent(r7, r1)
        L19:
            int r7 = r1.length
            r0 = 0
        L1b:
            if (r0 >= r7) goto L89
            r2 = r1[r0]
            int r3 = r2.getParameterCount()
            r4 = 3
            if (r3 != 0) goto L5f
            java.lang.String r5 = r2.getName()
            if (r8 == 0) goto L3b
            int r6 = r5.length()
            if (r6 <= r4) goto L86
            java.lang.String r6 = "get"
            boolean r5 = r5.startsWith(r6)
            if (r5 != 0) goto L3b
            goto L86
        L3b:
            java.lang.Class r5 = r2.getReturnType()
            java.lang.Class<java.util.concurrent.atomic.AtomicInteger> r6 = java.util.concurrent.atomic.AtomicInteger.class
            if (r5 == r6) goto L5b
            java.lang.Class<java.util.concurrent.atomic.AtomicLong> r6 = java.util.concurrent.atomic.AtomicLong.class
            if (r5 == r6) goto L5b
            java.lang.Class<java.util.concurrent.atomic.AtomicBoolean> r6 = java.util.concurrent.atomic.AtomicBoolean.class
            if (r5 == r6) goto L5b
            java.lang.Class<java.util.concurrent.atomic.AtomicIntegerArray> r6 = java.util.concurrent.atomic.AtomicIntegerArray.class
            if (r5 == r6) goto L5b
            java.lang.Class<java.util.concurrent.atomic.AtomicLongArray> r6 = java.util.concurrent.atomic.AtomicLongArray.class
            if (r5 == r6) goto L5b
            java.lang.Class<java.util.Collection> r6 = java.util.Collection.class
            boolean r5 = r6.isAssignableFrom(r5)
            if (r5 == 0) goto L5f
        L5b:
            r9.accept(r2)
            goto L86
        L5f:
            r5 = 1
            if (r3 == r5) goto L63
            goto L86
        L63:
            int r3 = r2.getModifiers()
            boolean r3 = java.lang.reflect.Modifier.isStatic(r3)
            if (r3 == 0) goto L6e
            goto L86
        L6e:
            java.lang.String r3 = r2.getName()
            int r5 = r3.length()
            if (r8 == 0) goto L83
            if (r5 <= r4) goto L86
            java.lang.String r4 = "set"
            boolean r3 = r3.startsWith(r4)
            if (r3 != 0) goto L83
            goto L86
        L83:
            r9.accept(r2)
        L86:
            int r0 = r0 + 1
            goto L1b
        L89:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.BeanUtils.setters(java.lang.Class, boolean, java.util.function.Consumer):void");
    }

    public static void annotationMethods(Class cls, Consumer<Method> consumer) {
        ConcurrentMap<Class, Method[]> concurrentMap = methodCache;
        Method[] methods = concurrentMap.get(cls);
        if (methods == null) {
            methods = getMethods(cls);
            concurrentMap.putIfAbsent(cls, methods);
        }
        for (Method method : methods) {
            if (method.getParameterCount() == 0 && method.getDeclaringClass() != Object.class) {
                String name = method.getName();
                name.hashCode();
                switch (name) {
                    case "toString":
                    case "hashCode":
                    case "annotationType":
                        break;
                    default:
                        consumer.accept(method);
                        break;
                }
            }
        }
    }

    public static boolean isWriteEnumAsJavaBean(Class cls) {
        for (final Annotation annotation : getAnnotations(cls)) {
            JSONType jSONType = (JSONType) findAnnotation(annotation, JSONType.class);
            if (jSONType != null) {
                return jSONType.writeEnumAsJavaBean();
            }
            Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
            String name = clsAnnotationType.getName();
            final BeanInfo beanInfo = new BeanInfo(JSONFactory.getDefaultObjectWriterProvider());
            name.hashCode();
            if (name.equals("com.fasterxml.jackson.annotation.JsonFormat")) {
                if (JSONFactory.isUseJacksonAnnotation()) {
                    processJacksonJsonFormat(beanInfo, annotation);
                }
            } else if (name.equals("com.alibaba.fastjson.annotation.JSONType")) {
                annotationMethods(clsAnnotationType, new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        BeanUtils.processJSONType1x(beanInfo, annotation, (Method) obj);
                    }
                });
            }
            if (beanInfo.writeEnumAsJavaBean) {
                return true;
            }
        }
        return false;
    }

    public static String[] getEnumAnnotationNames(Class cls) {
        final Enum[] enumArr = (Enum[]) cls.getEnumConstants();
        int length = enumArr.length;
        final String[] strArr = new String[length];
        fields(cls, new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$getEnumAnnotationNames$6(enumArr, strArr, (Field) obj);
            }
        });
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (strArr[i2] == null) {
                i++;
            }
        }
        if (i == length) {
            return null;
        }
        return strArr;
    }

    static /* synthetic */ void lambda$getEnumAnnotationNames$6(Enum[] enumArr, final String[] strArr, Field field) {
        String name = field.getName();
        for (final int i = 0; i < enumArr.length; i++) {
            final String strName = enumArr[i].name();
            if (name.equals(strName)) {
                for (final Annotation annotation : field.getAnnotations()) {
                    Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
                    String name2 = clsAnnotationType.getName();
                    if ("com.alibaba.fastjson2.annotation.JSONField".equals(name2) || "com.alibaba.fastjson.annotation.JSONField".equals(name2)) {
                        annotationMethods(clsAnnotationType, new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda16
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                BeanUtils.lambda$getEnumAnnotationNames$4(annotation, strName, strArr, i, (Method) obj);
                            }
                        });
                    } else if ("com.fasterxml.jackson.annotation.JsonProperty".equals(name2)) {
                        annotationMethods(clsAnnotationType, new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda17
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                BeanUtils.lambda$getEnumAnnotationNames$5(annotation, strName, strArr, i, (Method) obj);
                            }
                        });
                    }
                }
                return;
            }
        }
    }

    static /* synthetic */ void lambda$getEnumAnnotationNames$4(Annotation annotation, String str, String[] strArr, int i, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            if ("name".equals(name)) {
                String str2 = (String) objInvoke;
                if (str2.length() == 0 || str2.equals(str)) {
                    return;
                }
                strArr[i] = str2;
            }
        } catch (Exception unused) {
        }
    }

    static /* synthetic */ void lambda$getEnumAnnotationNames$5(Annotation annotation, String str, String[] strArr, int i, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            if ("value".equals(name)) {
                String str2 = (String) objInvoke;
                if (str2.length() == 0 || str2.equals(str)) {
                    return;
                }
                strArr[i] = str2;
            }
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0070 A[PHI: r7
      0x0070: PHI (r7v8 java.lang.reflect.Member) = (r7v9 java.lang.reflect.Member), (r7v10 java.lang.reflect.Member), (r7v11 java.lang.reflect.Member) binds: [B:54:0x00cf, B:33:0x006e, B:40:0x0094] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0098  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.reflect.Member getEnumValueField(java.lang.Class r14, com.alibaba.fastjson2.modules.ObjectCodecProvider r15) {
        /*
            Method dump skipped, instruction units count: 302
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.BeanUtils.getEnumValueField(java.lang.Class, com.alibaba.fastjson2.modules.ObjectCodecProvider):java.lang.reflect.Member");
    }

    static /* synthetic */ void lambda$getEnumValueField$7(String str, AtomicReference atomicReference, Method method, Method method2) {
        if (method2.getName().equals(str) && isJSONField(method2)) {
            atomicReference.set(method);
        }
    }

    static /* synthetic */ void lambda$getEnumValueField$8(String str, AtomicReference atomicReference, Method method, Method method2) {
        if (method2.getName().equals(str) && isJSONField(method2)) {
            atomicReference.set(method);
        }
    }

    public static void getters(Class cls, Consumer<Method> consumer) {
        getters(cls, null, consumer);
    }

    public static void getters(Class cls, Class cls2, Consumer<Method> consumer) {
        getters(cls, cls2, false, consumer);
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0216  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x021d  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0234  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0268  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x0272 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0284 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x029a  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0303  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x0306 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void getters(java.lang.Class r29, java.lang.Class r30, boolean r31, java.util.function.Consumer<java.lang.reflect.Method> r32) {
        /*
            Method dump skipped, instruction units count: 868
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.BeanUtils.getters(java.lang.Class, java.lang.Class, boolean, java.util.function.Consumer):void");
    }

    private static Method[] getMethods(Class cls) {
        try {
            return cls.getMethods();
        } catch (NoClassDefFoundError unused) {
            return new Method[0];
        }
    }

    private static boolean isJSONField(AnnotatedElement annotatedElement) {
        for (Annotation annotation : annotatedElement.getAnnotations()) {
            String name = annotation.annotationType().getName();
            name.hashCode();
            switch (name) {
                case "com.fasterxml.jackson.annotation.JsonValue":
                case "com.fasterxml.jackson.annotation.JsonProperty":
                case "com.fasterxml.jackson.annotation.JsonRawValue":
                case "com.fasterxml.jackson.annotation.JsonUnwrapped":
                    if (JSONFactory.isUseJacksonAnnotation()) {
                        return true;
                    }
                    break;
                    break;
                case "com.alibaba.fastjson.annotation.JSONField":
                case "com.alibaba.fastjson2.annotation.JSONField":
                    return true;
            }
        }
        return false;
    }

    static boolean ignore(Class cls) {
        return cls == null || Arrays.binarySearch(IGNORE_CLASS_HASH_CODES, Fnv.hashCode64(cls.getName())) >= 0;
    }

    public static boolean isRecord(Class cls) {
        Class superclass = cls.getSuperclass();
        if (superclass == null) {
            return false;
        }
        if (RECORD_CLASS != null) {
            return superclass == RECORD_CLASS;
        }
        if (!"java.lang.Record".equals(superclass.getName())) {
            return false;
        }
        RECORD_CLASS = superclass;
        return true;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static String setterName(String str, String str2) {
        char c;
        if (str2 == null) {
            str2 = "CamelCase";
        }
        int length = str.length();
        byte b = 3;
        if (length <= 3) {
            return str;
        }
        int i = str.startsWith("set") ? 3 : 0;
        str2.hashCode();
        switch (str2.hashCode()) {
            case -2068429102:
                b = str2.equals("UpperCase") ? (byte) 0 : (byte) -1;
                break;
            case -1863045342:
                b = str2.equals("UpperCaseWithDots") ? (byte) 1 : (byte) -1;
                break;
            case -1112704575:
                b = str2.equals("NeverUseThisValueExceptDefaultValue") ? (byte) 2 : (byte) -1;
                break;
            case -46641534:
                if (!str2.equals("LowerCaseWithUnderScores")) {
                    b = -1;
                }
                break;
            case 572594479:
                b = str2.equals("UpperCamelCaseWithUnderScores") ? (byte) 4 : (byte) -1;
                break;
            case 601822360:
                b = str2.equals("UpperCaseWithDashes") ? (byte) 5 : (byte) -1;
                break;
            case 928600554:
                b = str2.equals("UpperCamelCaseWithDashes") ? (byte) 6 : (byte) -1;
                break;
            case 975280372:
                b = str2.equals("UpperCamelCaseWithDots") ? (byte) 7 : (byte) -1;
                break;
            case 1315531521:
                b = str2.equals("LowerCaseWithDots") ? (byte) 8 : (byte) -1;
                break;
            case 1336502620:
                b = str2.equals("PascalCase") ? (byte) 9 : (byte) -1;
                break;
            case 1371349591:
                b = str2.equals("UpperCamelCaseWithSpaces") ? (byte) 10 : (byte) -1;
                break;
            case 1460726553:
                b = str2.equals("KebabCase") ? (byte) 11 : (byte) -1;
                break;
            case 1488507313:
                b = str2.equals("LowerCase") ? (byte) 12 : (byte) -1;
                break;
            case 1492440247:
                b = str2.equals("LowerCaseWithDashes") ? (byte) 13 : (byte) -1;
                break;
            case 1655544038:
                b = str2.equals("CamelCase") ? (byte) 14 : (byte) -1;
                break;
            case 1839922637:
                b = str2.equals("CamelCase1x") ? (byte) 15 : (byte) -1;
                break;
            case 1976554305:
                b = str2.equals("UpperCaseWithUnderScores") ? JSONB.Constants.BC_INT32_NUM_16 : (byte) -1;
                break;
            case 2087942256:
                b = str2.equals("SnakeCase") ? (byte) 17 : (byte) -1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                int i2 = length - i;
                char[] cArr = new char[i2];
                str.getChars(i, length, cArr, 0);
                char c2 = cArr[0];
                for (int i3 = 0; i3 < i2; i3++) {
                    char c3 = cArr[i3];
                    if (c3 >= 'a' && c2 <= 'z') {
                        cArr[i3] = (char) (c3 - ' ');
                    }
                }
                return new String(cArr);
            case 1:
                return dots(str, i, true);
            case 2:
            case 14:
                int i4 = length - i;
                char[] cArr2 = new char[i4];
                str.getChars(i, length, cArr2, 0);
                char c4 = cArr2[0];
                boolean z = i4 > 1 && (c = cArr2[1]) >= 'A' && c <= 'Z';
                if (c4 >= 'A' && c4 <= 'Z' && !z) {
                    cArr2[0] = (char) (c4 + TokenParser.SP);
                }
                return new String(cArr2);
            case 3:
                return underScores(str, i, false);
            case 4:
                return upperCamelWith(str, i, '_');
            case 5:
                return dashes(str, i, true);
            case 6:
                return upperCamelWith(str, i, '-');
            case 7:
                return upperCamelWith(str, i, '.');
            case 8:
                return dots(str, i, false);
            case 9:
                return pascal(str, length, i);
            case 10:
                return upperCamelWith(str, i, TokenParser.SP);
            case 11:
                StringBuilder sb = new StringBuilder();
                for (int i5 = i; i5 < str.length(); i5++) {
                    char cCharAt = str.charAt(i5);
                    if (cCharAt >= 'A' && cCharAt <= 'Z') {
                        cCharAt = (char) (cCharAt + TokenParser.SP);
                        if (i5 > i) {
                            sb.append('-');
                        }
                    }
                    sb.append(cCharAt);
                }
                return sb.toString();
            case 12:
                return str.substring(i).toLowerCase();
            case 13:
                return dashes(str, i, false);
            case 15:
                char[] cArr3 = new char[length - i];
                str.getChars(i, length, cArr3, 0);
                char c5 = cArr3[0];
                if (c5 >= 'A' && c5 <= 'Z') {
                    cArr3[0] = (char) (c5 + TokenParser.SP);
                }
                return new String(cArr3);
            case 16:
                return underScores(str, i, true);
            case 17:
                return snakeCase(str, i);
            default:
                throw new JSONException("TODO : " + str2);
        }
    }

    public static String setterName(String str, int i) {
        char c;
        int length = str.length();
        int i2 = length - i;
        char[] cArr = new char[i2];
        str.getChars(i, length, cArr, 0);
        char c2 = cArr[0];
        boolean z = i2 > 1 && (c = cArr[1]) >= 'A' && c <= 'Z';
        if (c2 >= 'A' && c2 <= 'Z' && !z) {
            cArr[0] = (char) (c2 + TokenParser.SP);
        }
        return new String(cArr);
    }

    public static String getterName(Method method, String str) {
        return getterName(method, false, str);
    }

    public static String getterName(Method method, boolean z, String str) {
        int iIndexOf;
        Class<?> returnType;
        String name = method.getName();
        if (name.startsWith("is") && (((returnType = method.getReturnType()) != Boolean.class && returnType != Boolean.TYPE) || z)) {
            return name;
        }
        String strSubstring = getterName(name, str);
        if (z && (iIndexOf = strSubstring.indexOf(45)) != -1) {
            strSubstring = strSubstring.substring(0, iIndexOf);
        }
        if (strSubstring.length() > 2 && strSubstring.charAt(0) >= 'A' && strSubstring.charAt(0) <= 'Z' && strSubstring.charAt(1) >= 'A' && strSubstring.charAt(1) <= 'Z') {
            char[] charArray = strSubstring.toCharArray();
            charArray[0] = (char) (charArray[0] + TokenParser.SP);
            Field declaredField = getDeclaredField(method.getDeclaringClass(), new String(charArray));
            if (declaredField != null && Modifier.isPublic(declaredField.getModifiers())) {
                return declaredField.getName();
            }
        }
        return strSubstring;
    }

    public static Field getField(Class cls, final String str) {
        final Field[] fieldArr = new Field[1];
        declaredFields(cls, new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$getField$9(str, fieldArr, (Field) obj);
            }
        });
        return fieldArr[0];
    }

    static /* synthetic */ void lambda$getField$9(String str, Field[] fieldArr, Field field) {
        if (field.getName().equals(str)) {
            fieldArr[0] = field;
        }
    }

    public static Field getField(Class cls, final Method method) {
        boolean z;
        boolean z2;
        boolean z3;
        final String name = method.getName();
        final int length = name.length();
        Class<?> returnType = method.getReturnType();
        if (length > 2) {
            char cCharAt = name.charAt(0);
            char cCharAt2 = name.charAt(1);
            char cCharAt3 = name.charAt(2);
            if (cCharAt == 'i' && cCharAt2 == 's') {
                z = returnType == Boolean.class || returnType == Boolean.TYPE;
                z2 = false;
                z3 = z2;
            } else if (cCharAt == 'g' && cCharAt2 == 'e' && cCharAt3 == 't') {
                z2 = length > 3;
                z = false;
                z3 = false;
            } else {
                if (cCharAt == 's' && cCharAt2 == 'e' && cCharAt3 == 't') {
                    z3 = length > 3 && method.getParameterCount() == 1;
                    z = false;
                    z2 = false;
                }
                z = false;
                z2 = false;
                z3 = z2;
            }
        } else {
            z = false;
            z2 = false;
            z3 = z2;
        }
        final Field[] fieldArr = new Field[2];
        if (z || z2 || z3) {
            final Class<?> cls2 = (z || z2) ? returnType : method.getParameterTypes()[0];
            final int i = z ? 2 : 3;
            char[] cArr = new char[length - i];
            name.getChars(i, length, cArr, 0);
            final char c = cArr[0];
            declaredFields(cls, new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda11
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BeanUtils.lambda$getField$10(method, length, i, cls2, c, name, fieldArr, (Field) obj);
                }
            });
        }
        Field field = fieldArr[0];
        if (field == null) {
            field = fieldArr[1];
        }
        if (Throwable.class.isAssignableFrom(cls)) {
            if (returnType == String.class && ((field == null && "getMessage".equals(name)) || (field == null && "getLocalizedMessage".equals(name)))) {
                return getDeclaredField(cls, "detailMessage");
            }
            if (returnType == Throwable[].class && "getSuppressed".equals(name)) {
                return getDeclaredField(cls, "suppressedExceptions");
            }
        }
        return field;
    }

    static /* synthetic */ void lambda$getField$10(Method method, int i, int i2, Class cls, char c, String str, Field[] fieldArr, Field field) {
        if (field.getDeclaringClass() != method.getDeclaringClass()) {
            return;
        }
        String name = field.getName();
        int length = name.length();
        if (length == i - i2 && (field.getType() == cls || cls.isAssignableFrom(field.getType()))) {
            if (c >= 'A' && c <= 'Z' && c + TokenParser.SP == name.charAt(0) && name.regionMatches(1, str, i2 + 1, length - 1)) {
                fieldArr[0] = field;
                return;
            } else {
                if (name.regionMatches(0, str, i2, length)) {
                    fieldArr[1] = field;
                    return;
                }
                return;
            }
        }
        if (Boolean.TYPE == field.getType() && str.equals(name)) {
            fieldArr[0] = field;
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static String getterName(String str, String str2) {
        char c;
        if (str2 == null) {
            str2 = "CamelCase";
        }
        int length = str.length();
        boolean zStartsWith = str.startsWith("is");
        boolean zStartsWith2 = str.startsWith("get");
        int i = 3;
        int i2 = zStartsWith ? 2 : zStartsWith2 ? 3 : 0;
        if (length == i2) {
            return str;
        }
        str2.hashCode();
        byte b = -1;
        switch (str2.hashCode()) {
            case -2068429102:
                if (str2.equals("UpperCase")) {
                    b = 0;
                }
                break;
            case -1863045342:
                if (str2.equals("UpperCaseWithDots")) {
                    b = 1;
                }
                break;
            case -1112704575:
                if (str2.equals("NeverUseThisValueExceptDefaultValue")) {
                    b = 2;
                }
                break;
            case -46641534:
                if (str2.equals("LowerCaseWithUnderScores")) {
                    b = 3;
                }
                break;
            case 572594479:
                if (str2.equals("UpperCamelCaseWithUnderScores")) {
                    b = 4;
                }
                break;
            case 601822360:
                if (str2.equals("UpperCaseWithDashes")) {
                    b = 5;
                }
                break;
            case 928600554:
                if (str2.equals("UpperCamelCaseWithDashes")) {
                    b = 6;
                }
                break;
            case 975280372:
                if (str2.equals("UpperCamelCaseWithDots")) {
                    b = 7;
                }
                break;
            case 1315531521:
                if (str2.equals("LowerCaseWithDots")) {
                    b = 8;
                }
                break;
            case 1336502620:
                if (str2.equals("PascalCase")) {
                    b = 9;
                }
                break;
            case 1371349591:
                if (str2.equals("UpperCamelCaseWithSpaces")) {
                    b = 10;
                }
                break;
            case 1460726553:
                if (str2.equals("KebabCase")) {
                    b = 11;
                }
                break;
            case 1488507313:
                if (str2.equals("LowerCase")) {
                    b = 12;
                }
                break;
            case 1492440247:
                if (str2.equals("LowerCaseWithDashes")) {
                    b = 13;
                }
                break;
            case 1655544038:
                if (str2.equals("CamelCase")) {
                    b = 14;
                }
                break;
            case 1839922637:
                if (str2.equals("CamelCase1x")) {
                    b = 15;
                }
                break;
            case 1976554305:
                if (str2.equals("UpperCaseWithUnderScores")) {
                    b = JSONB.Constants.BC_INT32_NUM_16;
                }
                break;
            case 2087942256:
                if (str2.equals("SnakeCase")) {
                    b = 17;
                }
                break;
        }
        switch (b) {
            case 0:
                return str.substring(i2).toUpperCase();
            case 1:
                return dots(str, i2, true);
            case 2:
            case 14:
                int i3 = length - i2;
                char[] cArr = new char[i3];
                str.getChars(i2, length, cArr, 0);
                char c2 = cArr[0];
                boolean z = i3 > 1 && (c = cArr[1]) >= 'A' && c <= 'Z';
                if (c2 >= 'A' && c2 <= 'Z' && !z) {
                    cArr[0] = (char) (c2 + TokenParser.SP);
                }
                return new String(cArr);
            case 3:
                return underScores(str, i2, false);
            case 4:
                return upperCamelWith(str, i2, '_');
            case 5:
                return dashes(str, i2, true);
            case 6:
                return upperCamelWith(str, i2, '-');
            case 7:
                return upperCamelWith(str, i2, '.');
            case 8:
                return dots(str, i2, false);
            case 9:
                return pascal(str, length, i2);
            case 10:
                return upperCamelWith(str, i2, TokenParser.SP);
            case 11:
                StringBuilder sb = new StringBuilder();
                if (zStartsWith) {
                    i = 2;
                } else if (!zStartsWith2) {
                    i = 0;
                }
                for (int i4 = i; i4 < str.length(); i4++) {
                    char cCharAt = str.charAt(i4);
                    if (cCharAt >= 'A' && cCharAt <= 'Z') {
                        cCharAt = (char) (cCharAt + TokenParser.SP);
                        if (i4 > i) {
                            sb.append('-');
                        }
                    }
                    sb.append(cCharAt);
                }
                return sb.toString();
            case 12:
                return str.substring(i2).toLowerCase();
            case 13:
                return dashes(str, i2, false);
            case 15:
                char[] cArr2 = new char[length - i2];
                str.getChars(i2, length, cArr2, 0);
                char c3 = cArr2[0];
                if (c3 >= 'A' && c3 <= 'Z') {
                    cArr2[0] = (char) (c3 + TokenParser.SP);
                }
                return new String(cArr2);
            case 16:
                return underScores(str, i2, true);
            case 17:
                return snakeCase(str, i2);
            default:
                throw new JSONException("TODO : " + str2);
        }
    }

    private static String pascal(String str, int i, int i2) {
        char c;
        char c2;
        int i3 = i - i2;
        char[] cArr = new char[i3];
        str.getChars(i2, i, cArr, 0);
        char c3 = cArr[0];
        if (c3 >= 'a' && c3 <= 'z' && i3 > 1) {
            cArr[0] = (char) (c3 - ' ');
        } else if (c3 == '_' && i3 > 2 && (c = cArr[1]) >= 'a' && c <= 'z' && (c2 = cArr[2]) >= 'a' && c2 <= 'z') {
            cArr[1] = (char) (c - ' ');
        }
        return new String(cArr);
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static String fieldName(String str, String str2) {
        char cCharAt;
        char cCharAt2;
        if (str2 == null) {
            str2 = "CamelCase";
        }
        if (str == null || str.isEmpty()) {
            return str;
        }
        str2.hashCode();
        byte b = -1;
        switch (str2.hashCode()) {
            case -2068429102:
                if (str2.equals("UpperCase")) {
                    b = 0;
                }
                break;
            case -1863045342:
                if (str2.equals("UpperCaseWithDots")) {
                    b = 1;
                }
                break;
            case -1112704575:
                if (str2.equals("NeverUseThisValueExceptDefaultValue")) {
                    b = 2;
                }
                break;
            case -46641534:
                if (str2.equals("LowerCaseWithUnderScores")) {
                    b = 3;
                }
                break;
            case 246111473:
                if (str2.equals("NoChange")) {
                    b = 4;
                }
                break;
            case 572594479:
                if (str2.equals("UpperCamelCaseWithUnderScores")) {
                    b = 5;
                }
                break;
            case 601822360:
                if (str2.equals("UpperCaseWithDashes")) {
                    b = 6;
                }
                break;
            case 928600554:
                if (str2.equals("UpperCamelCaseWithDashes")) {
                    b = 7;
                }
                break;
            case 975280372:
                if (str2.equals("UpperCamelCaseWithDots")) {
                    b = 8;
                }
                break;
            case 1315531521:
                if (str2.equals("LowerCaseWithDots")) {
                    b = 9;
                }
                break;
            case 1336502620:
                if (str2.equals("PascalCase")) {
                    b = 10;
                }
                break;
            case 1371349591:
                if (str2.equals("UpperCamelCaseWithSpaces")) {
                    b = 11;
                }
                break;
            case 1460726553:
                if (str2.equals("KebabCase")) {
                    b = 12;
                }
                break;
            case 1488507313:
                if (str2.equals("LowerCase")) {
                    b = 13;
                }
                break;
            case 1492440247:
                if (str2.equals("LowerCaseWithDashes")) {
                    b = 14;
                }
                break;
            case 1655544038:
                if (str2.equals("CamelCase")) {
                    b = 15;
                }
                break;
            case 1839922637:
                if (str2.equals("CamelCase1x")) {
                    b = JSONB.Constants.BC_INT32_NUM_16;
                }
                break;
            case 1976554305:
                if (str2.equals("UpperCaseWithUnderScores")) {
                    b = 17;
                }
                break;
            case 2087942256:
                if (str2.equals("SnakeCase")) {
                    b = 18;
                }
                break;
        }
        switch (b) {
            case 0:
                return str.toUpperCase();
            case 1:
                return dots(str, 0, true);
            case 2:
            case 4:
            case 15:
                char cCharAt3 = str.charAt(0);
                char cCharAt4 = str.length() > 1 ? str.charAt(1) : (char) 0;
                if (cCharAt3 < 'A' || cCharAt3 > 'Z' || str.length() <= 1) {
                    return str;
                }
                if (cCharAt4 >= 'A' && cCharAt4 <= 'Z') {
                    return str;
                }
                char[] charArray = str.toCharArray();
                charArray[0] = (char) (cCharAt3 + TokenParser.SP);
                return new String(charArray);
            case 3:
                return underScores(str, 0, false);
            case 5:
                return upperCamelWith(str, 0, '_');
            case 6:
                return dashes(str, 0, true);
            case 7:
                return upperCamelWith(str, 0, '-');
            case 8:
                return upperCamelWith(str, 0, '.');
            case 9:
                return dots(str, 0, false);
            case 10:
                char cCharAt5 = str.charAt(0);
                if (cCharAt5 >= 'a' && cCharAt5 <= 'z' && str.length() > 1 && (cCharAt2 = str.charAt(1)) >= 'a' && cCharAt2 <= 'z') {
                    char[] charArray2 = str.toCharArray();
                    charArray2[0] = (char) (cCharAt5 - TokenParser.SP);
                    return new String(charArray2);
                }
                if (cCharAt5 != '_' || str.length() <= 1 || (cCharAt = str.charAt(1)) < 'a' || cCharAt > 'z') {
                    return str;
                }
                char[] charArray3 = str.toCharArray();
                charArray3[1] = (char) (cCharAt - TokenParser.SP);
                return new String(charArray3);
            case 11:
                return upperCamelWith(str, 0, TokenParser.SP);
            case 12:
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < str.length(); i++) {
                    char cCharAt6 = str.charAt(i);
                    if (cCharAt6 >= 'A' && cCharAt6 <= 'Z') {
                        cCharAt6 = (char) (cCharAt6 + TokenParser.SP);
                        if (i > 0) {
                            sb.append('-');
                        }
                    }
                    sb.append(cCharAt6);
                }
                return sb.toString();
            case 13:
                return str.toLowerCase();
            case 14:
                return dashes(str, 0, false);
            case 16:
                char cCharAt7 = str.charAt(0);
                if (cCharAt7 < 'A' || cCharAt7 > 'Z' || str.length() <= 1) {
                    return str;
                }
                char[] charArray4 = str.toCharArray();
                charArray4[0] = (char) (cCharAt7 + TokenParser.SP);
                return new String(charArray4);
            case 17:
                return underScores(str, 0, true);
            case 18:
                return snakeCase(str, 0);
            default:
                throw new JSONException("TODO : " + str2);
        }
    }

    static String snakeCase(String str, int i) {
        int length = str.length();
        char[] andSet = TypeUtils.CHARS_UPDATER.getAndSet(TypeUtils.CACHE, null);
        if (andSet == null) {
            andSet = new char[128];
        }
        int i2 = i;
        int i3 = 0;
        while (i2 < length) {
            try {
                char cCharAt = str.charAt(i2);
                if (cCharAt >= 'A' && cCharAt <= 'Z') {
                    cCharAt = (char) (cCharAt + TokenParser.SP);
                    if (i2 > i) {
                        andSet[i3] = '_';
                        i3++;
                    }
                }
                andSet[i3] = cCharAt;
                i2++;
                i3++;
            } finally {
                TypeUtils.CHARS_UPDATER.set(TypeUtils.CACHE, andSet);
            }
        }
        return new String(andSet, 0, i3);
    }

    static String upperCamelWith(String str, int i, char c) {
        int i2;
        char cCharAt;
        char cCharAt2;
        int i3;
        int i4;
        char cCharAt3;
        int i5;
        char cCharAt4;
        int i6;
        char cCharAt5;
        int length = str.length();
        char[] andSet = TypeUtils.CHARS_UPDATER.getAndSet(TypeUtils.CACHE, null);
        if (andSet == null) {
            andSet = new char[128];
        }
        int i7 = i;
        int i8 = 0;
        while (i7 < length) {
            try {
                char cCharAt6 = str.charAt(i7);
                if (i7 == i) {
                    if (cCharAt6 >= 'a' && cCharAt6 <= 'z' && (i6 = i7 + 1) < length && (cCharAt5 = str.charAt(i6)) >= 'a' && cCharAt5 <= 'z') {
                        cCharAt6 = (char) (cCharAt6 - ' ');
                    } else if (cCharAt6 == '_' && (i5 = i7 + 1) < length && (cCharAt4 = str.charAt(i5)) >= 'a' && cCharAt4 <= 'z') {
                        andSet[i8] = cCharAt6;
                        cCharAt6 = (char) (cCharAt4 - ' ');
                        i8++;
                        i7 = i5;
                    }
                } else if (cCharAt6 < 'A' || cCharAt6 > 'Z' || (i4 = i7 + 1) >= length || ((cCharAt3 = str.charAt(i4)) >= 'A' && cCharAt3 <= 'Z')) {
                    if (cCharAt6 >= 'A' && cCharAt6 <= 'Z' && i7 > i && (i2 = i7 + 1) < length && (cCharAt = str.charAt(i2)) >= 'A' && cCharAt <= 'Z' && (cCharAt2 = str.charAt(i7 - 1)) >= 'a' && cCharAt2 <= 'z') {
                        i3 = i8 + 1;
                        andSet[i8] = c;
                        i8 = i3;
                    }
                } else if (i7 > i) {
                    i3 = i8 + 1;
                    andSet[i8] = c;
                    i8 = i3;
                }
                andSet[i8] = cCharAt6;
                i7++;
                i8++;
            } finally {
                TypeUtils.CHARS_UPDATER.set(TypeUtils.CACHE, andSet);
            }
        }
        return new String(andSet, 0, i8);
    }

    static String underScores(String str, int i, boolean z) {
        int i2;
        int length = str.length();
        char[] andSet = TypeUtils.CHARS_UPDATER.getAndSet(TypeUtils.CACHE, null);
        if (andSet == null) {
            andSet = new char[128];
        }
        int i3 = i;
        int i4 = 0;
        while (i3 < length) {
            try {
                char cCharAt = str.charAt(i3);
                if (z) {
                    if (cCharAt < 'A' || cCharAt > 'Z') {
                        if (cCharAt >= 'a' && cCharAt <= 'z') {
                            i2 = cCharAt - ' ';
                            cCharAt = (char) i2;
                        }
                    } else if (i3 > i) {
                        andSet[i4] = '_';
                        i4++;
                    }
                } else if (cCharAt >= 'A' && cCharAt <= 'Z') {
                    if (i3 > i) {
                        andSet[i4] = '_';
                        i4++;
                    }
                    i2 = cCharAt + TokenParser.SP;
                    cCharAt = (char) i2;
                }
                andSet[i4] = cCharAt;
                i3++;
                i4++;
            } finally {
                TypeUtils.CHARS_UPDATER.set(TypeUtils.CACHE, andSet);
            }
        }
        return new String(andSet, 0, i4);
    }

    static String dashes(String str, int i, boolean z) {
        int i2;
        int length = str.length();
        char[] andSet = TypeUtils.CHARS_UPDATER.getAndSet(TypeUtils.CACHE, null);
        if (andSet == null) {
            andSet = new char[128];
        }
        int i3 = i;
        int i4 = 0;
        while (i3 < length) {
            try {
                char cCharAt = str.charAt(i3);
                if (z) {
                    if (cCharAt < 'A' || cCharAt > 'Z') {
                        if (cCharAt >= 'a' && cCharAt <= 'z') {
                            i2 = cCharAt - ' ';
                            cCharAt = (char) i2;
                        }
                    } else if (i3 > i) {
                        andSet[i4] = '-';
                        i4++;
                    }
                } else if (cCharAt >= 'A' && cCharAt <= 'Z') {
                    if (i3 > i) {
                        andSet[i4] = '-';
                        i4++;
                    }
                    i2 = cCharAt + TokenParser.SP;
                    cCharAt = (char) i2;
                }
                andSet[i4] = cCharAt;
                i3++;
                i4++;
            } finally {
                TypeUtils.CHARS_UPDATER.set(TypeUtils.CACHE, andSet);
            }
        }
        return new String(andSet, 0, i4);
    }

    static String dots(String str, int i, boolean z) {
        int i2;
        int length = str.length();
        char[] andSet = TypeUtils.CHARS_UPDATER.getAndSet(TypeUtils.CACHE, null);
        if (andSet == null) {
            andSet = new char[128];
        }
        int i3 = i;
        int i4 = 0;
        while (i3 < length) {
            try {
                char cCharAt = str.charAt(i3);
                if (z) {
                    if (cCharAt < 'A' || cCharAt > 'Z') {
                        if (cCharAt >= 'a' && cCharAt <= 'z') {
                            i2 = cCharAt - ' ';
                            cCharAt = (char) i2;
                        }
                    } else if (i3 > i) {
                        andSet[i4] = '.';
                        i4++;
                    }
                } else if (cCharAt >= 'A' && cCharAt <= 'Z') {
                    if (i3 > i) {
                        andSet[i4] = '.';
                        i4++;
                    }
                    i2 = cCharAt + TokenParser.SP;
                    cCharAt = (char) i2;
                }
                andSet[i4] = cCharAt;
                i3++;
                i4++;
            } finally {
                TypeUtils.CHARS_UPDATER.set(TypeUtils.CACHE, andSet);
            }
        }
        return new String(andSet, 0, i4);
    }

    public static Type getFieldType(TypeReference typeReference, Class<?> cls, Member member, Type type) {
        Class<?> declaringClass = member == null ? null : member.getDeclaringClass();
        while (cls != Object.class) {
            Type type2 = typeReference == null ? null : typeReference.getType();
            if (declaringClass == cls) {
                return resolve(type2, declaringClass, type);
            }
            Type genericSuperclass = cls.getGenericSuperclass();
            if (genericSuperclass == null) {
                break;
            }
            typeReference = TypeReference.get(resolve(type2, cls, genericSuperclass));
            cls = typeReference.getRawType();
        }
        return null;
    }

    public static Type getParamType(TypeReference typeReference, Class<?> cls, Class cls2, Parameter parameter, Type type) {
        while (cls != Object.class) {
            if (cls2 == cls) {
                return resolve(typeReference.getType(), cls2, type);
            }
            typeReference = TypeReference.get(resolve(typeReference.getType(), cls, cls.getGenericSuperclass()));
            cls = typeReference.getRawType();
        }
        return null;
    }

    public static ParameterizedType newParameterizedTypeWithOwner(Type type, Type type2, Type... typeArr) {
        return new ParameterizedTypeImpl(type, type2, typeArr);
    }

    public static GenericArrayType arrayOf(Type type) {
        return new GenericArrayTypeImpl(type);
    }

    public static WildcardType subtypeOf(Type type) {
        Type[] upperBounds;
        if (type instanceof WildcardType) {
            upperBounds = ((WildcardType) type).getUpperBounds();
        } else {
            upperBounds = new Type[]{type};
        }
        return new WildcardTypeImpl(upperBounds, EMPTY_TYPE_ARRAY);
    }

    public static WildcardType supertypeOf(Type type) {
        Type[] lowerBounds;
        Type[] typeArr = {Object.class};
        if (type instanceof WildcardType) {
            lowerBounds = ((WildcardType) type).getLowerBounds();
        } else {
            lowerBounds = new Type[]{type};
        }
        return new WildcardTypeImpl(typeArr, lowerBounds);
    }

    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Class cls = (Class) type;
            return cls.isArray() ? new GenericArrayTypeImpl(canonicalize(cls.getComponentType())) : cls;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return new ParameterizedTypeImpl(parameterizedType.getOwnerType(), parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            return new GenericArrayTypeImpl(((GenericArrayType) type).getGenericComponentType());
        }
        if (!(type instanceof WildcardType)) {
            return type;
        }
        WildcardType wildcardType = (WildcardType) type;
        return new WildcardTypeImpl(wildcardType.getUpperBounds(), wildcardType.getLowerBounds());
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            checkArgument(rawType instanceof Class);
            return (Class) rawType;
        }
        if (type instanceof GenericArrayType) {
            return Array.newInstance(getRawType(((GenericArrayType) type).getGenericComponentType()), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + (type == null ? "null" : type.getClass().getName()));
    }

    static boolean equal(Object obj, Object obj2) {
        return Objects.equals(obj, obj2);
    }

    public static boolean equals(Type type, Type type2) {
        if (type == type2) {
            return true;
        }
        if (type instanceof Class) {
            return type.equals(type2);
        }
        if (type instanceof ParameterizedType) {
            if (!(type2 instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType parameterizedType = (ParameterizedType) type;
            ParameterizedType parameterizedType2 = (ParameterizedType) type2;
            return equal(parameterizedType.getOwnerType(), parameterizedType2.getOwnerType()) && parameterizedType.getRawType().equals(parameterizedType2.getRawType()) && Arrays.equals(parameterizedType.getActualTypeArguments(), parameterizedType2.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            if (type2 instanceof GenericArrayType) {
                return equals(((GenericArrayType) type).getGenericComponentType(), ((GenericArrayType) type2).getGenericComponentType());
            }
            return false;
        }
        if (type instanceof WildcardType) {
            if (!(type2 instanceof WildcardType)) {
                return false;
            }
            WildcardType wildcardType = (WildcardType) type;
            WildcardType wildcardType2 = (WildcardType) type2;
            return Arrays.equals(wildcardType.getUpperBounds(), wildcardType2.getUpperBounds()) && Arrays.equals(wildcardType.getLowerBounds(), wildcardType2.getLowerBounds());
        }
        if (!(type instanceof TypeVariable) || !(type2 instanceof TypeVariable)) {
            return false;
        }
        TypeVariable typeVariable = (TypeVariable) type;
        TypeVariable typeVariable2 = (TypeVariable) type2;
        return typeVariable.getGenericDeclaration() == typeVariable2.getGenericDeclaration() && typeVariable.getName().equals(typeVariable2.getName());
    }

    static int hashCodeOrZero(Object obj) {
        if (obj != null) {
            return obj.hashCode();
        }
        return 0;
    }

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }

    static Type getGenericSupertype(Type type, Class<?> cls, Class<?> cls2) {
        if (cls2 == cls) {
            return type;
        }
        if (cls2.isInterface()) {
            Class<?>[] interfaces = cls.getInterfaces();
            int length = interfaces.length;
            for (int i = 0; i < length; i++) {
                Class<?> cls3 = interfaces[i];
                if (cls3 == cls2) {
                    return cls.getGenericInterfaces()[i];
                }
                if (cls2.isAssignableFrom(cls3)) {
                    return getGenericSupertype(cls.getGenericInterfaces()[i], interfaces[i], cls2);
                }
            }
        }
        if (cls != null && !cls.isInterface()) {
            while (cls != Object.class) {
                Class<? super Object> superclass = cls.getSuperclass();
                if (superclass == cls2) {
                    return cls.getGenericSuperclass();
                }
                if (cls2.isAssignableFrom(superclass)) {
                    return getGenericSupertype(cls.getGenericSuperclass(), superclass, cls2);
                }
                cls = superclass;
            }
        }
        return cls2;
    }

    public static Type resolve(Type type, Class<?> cls, Type type2) {
        return resolve(type, cls, type2, new HashMap());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:26:0x004b  */
    /* JADX WARN: Type inference failed for: r11v0, types: [java.lang.reflect.Type] */
    /* JADX WARN: Type inference failed for: r11v1, types: [java.lang.reflect.Type] */
    /* JADX WARN: Type inference failed for: r11v11, types: [java.lang.reflect.Type] */
    /* JADX WARN: Type inference failed for: r11v2, types: [java.lang.reflect.WildcardType] */
    /* JADX WARN: Type inference failed for: r11v3, types: [java.lang.reflect.WildcardType] */
    /* JADX WARN: Type inference failed for: r11v4, types: [java.lang.reflect.ParameterizedType] */
    /* JADX WARN: Type inference failed for: r11v5, types: [java.lang.reflect.GenericArrayType] */
    /* JADX WARN: Type inference failed for: r11v6 */
    /* JADX WARN: Type inference failed for: r11v8 */
    /* JADX WARN: Type inference failed for: r11v9, types: [java.lang.Object, java.lang.reflect.Type] */
    /* JADX WARN: Type inference failed for: r12v0, types: [java.util.Map, java.util.Map<java.lang.reflect.TypeVariable<?>, java.lang.reflect.Type>] */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v20 */
    /* JADX WARN: Type inference failed for: r1v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.reflect.Type resolve(java.lang.reflect.Type r9, java.lang.Class<?> r10, java.lang.reflect.Type r11, java.util.Map<java.lang.reflect.TypeVariable<?>, java.lang.reflect.Type> r12) {
        /*
            Method dump skipped, instruction units count: 229
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.BeanUtils.resolve(java.lang.reflect.Type, java.lang.Class, java.lang.reflect.Type, java.util.Map):java.lang.reflect.Type");
    }

    static Type resolveTypeVariable(Type type, Class<?> cls, TypeVariable<?> typeVariable) {
        Class<?> clsDeclaringClassOf = declaringClassOf(typeVariable);
        if (clsDeclaringClassOf != null) {
            Type genericSupertype = getGenericSupertype(type, cls, clsDeclaringClassOf);
            if (genericSupertype instanceof ParameterizedType) {
                return ((ParameterizedType) genericSupertype).getActualTypeArguments()[indexOf(clsDeclaringClassOf.getTypeParameters(), typeVariable)];
            }
        }
        return typeVariable;
    }

    private static int indexOf(Object[] objArr, Object obj) {
        int length = objArr.length;
        for (int i = 0; i < length; i++) {
            if (obj.equals(objArr[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        if (genericDeclaration instanceof Class) {
            return (Class) genericDeclaration;
        }
        return null;
    }

    static void checkNotPrimitive(Type type) {
        checkArgument(((type instanceof Class) && ((Class) type).isPrimitive()) ? false : true);
    }

    public static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, Class<A> cls) {
        if (cls == null) {
            throw new NullPointerException("annotationType must not be null");
        }
        return (A) findAnnotation(annotatedElement, cls, cls.isAnnotationPresent(Inherited.class), new HashSet());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <A extends Annotation> A findAnnotation(Annotation annotation, Class<A> cls) {
        if (annotation == 0) {
            throw new NullPointerException("annotation must not be null");
        }
        if (cls == null) {
            throw new NullPointerException("annotationType must not be null");
        }
        Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
        return clsAnnotationType == cls ? annotation : (A) findAnnotation(clsAnnotationType, cls, cls.isAnnotationPresent(Inherited.class), new HashSet());
    }

    private static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, Class<A> cls, boolean z, Set<Annotation> set) {
        Class superclass;
        A a;
        A a2;
        if (annotatedElement == null || cls == null) {
            return null;
        }
        A a3 = (A) annotatedElement.getDeclaredAnnotation(cls);
        if (a3 != null) {
            return a3;
        }
        A a4 = (A) findMetaAnnotation(cls, annotatedElement.getDeclaredAnnotations(), z, set);
        if (a4 != null) {
            return a4;
        }
        if (annotatedElement instanceof Class) {
            Class cls2 = (Class) annotatedElement;
            for (Class<?> cls3 : cls2.getInterfaces()) {
                if (cls3 != Annotation.class && (a2 = (A) findAnnotation(cls3, cls, z, set)) != null) {
                    return a2;
                }
            }
            if (z && (superclass = cls2.getSuperclass()) != null && superclass != Object.class && (a = (A) findAnnotation(superclass, cls, true, set)) != null) {
                return a;
            }
        }
        return (A) findMetaAnnotation(cls, getAnnotations(annotatedElement), z, set);
    }

    private static <A extends Annotation> A findMetaAnnotation(Class<A> cls, Annotation[] annotationArr, boolean z, Set<Annotation> set) {
        A a;
        for (Annotation annotation : annotationArr) {
            Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
            String name = clsAnnotationType.getName();
            if (!name.startsWith("java.lang.annotation") && !name.startsWith("kotlin.") && set.add(annotation) && (a = (A) findAnnotation(clsAnnotationType, cls, z, set)) != null) {
                return a;
            }
        }
        return null;
    }

    public static Annotation[] getAnnotations(AnnotatedElement annotatedElement) {
        try {
            return annotatedElement.getDeclaredAnnotations();
        } catch (Throwable unused) {
            return new Annotation[0];
        }
    }

    static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;

        public ParameterizedTypeImpl(Type type, Type type2, Type... typeArr) {
            if (type2 instanceof Class) {
                Class cls = (Class) type2;
                boolean z = true;
                boolean z2 = Modifier.isStatic(cls.getModifiers()) || cls.getEnclosingClass() == null;
                if (type == null && !z2) {
                    z = false;
                }
                BeanUtils.checkArgument(z);
            }
            this.ownerType = type == null ? null : BeanUtils.canonicalize(type);
            this.rawType = BeanUtils.canonicalize(type2);
            Type[] typeArr2 = (Type[]) typeArr.clone();
            this.typeArguments = typeArr2;
            int length = typeArr2.length;
            for (int i = 0; i < length; i++) {
                BeanUtils.checkNotPrimitive(this.typeArguments[i]);
                Type[] typeArr3 = this.typeArguments;
                typeArr3[i] = BeanUtils.canonicalize(typeArr3[i]);
            }
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            return (Type[]) this.typeArguments.clone();
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.rawType;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getOwnerType() {
            return this.ownerType;
        }

        public boolean equals(Object obj) {
            return (obj instanceof ParameterizedType) && BeanUtils.equals(this, (ParameterizedType) obj);
        }

        public int hashCode() {
            return (Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode()) ^ BeanUtils.hashCodeOrZero(this.ownerType);
        }

        public String toString() {
            int length = this.typeArguments.length;
            if (length == 0) {
                return BeanUtils.typeToString(this.rawType);
            }
            StringBuilder sb = new StringBuilder((length + 1) * 30);
            sb.append(BeanUtils.typeToString(this.rawType)).append("<").append(BeanUtils.typeToString(this.typeArguments[0]));
            for (int i = 1; i < length; i++) {
                sb.append(", ").append(BeanUtils.typeToString(this.typeArguments[i]));
            }
            return sb.append(">").toString();
        }
    }

    public static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type componentType;

        public GenericArrayTypeImpl(Type type) {
            this.componentType = BeanUtils.canonicalize(type);
        }

        @Override // java.lang.reflect.GenericArrayType
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public boolean equals(Object obj) {
            return (obj instanceof GenericArrayType) && BeanUtils.equals(this, (GenericArrayType) obj);
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return BeanUtils.typeToString(this.componentType) + HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
    }

    static final class WildcardTypeImpl implements WildcardType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type lowerBound;
        private final Type upperBound;

        public WildcardTypeImpl(Type[] typeArr, Type[] typeArr2) {
            BeanUtils.checkArgument(typeArr2.length <= 1);
            BeanUtils.checkArgument(typeArr.length == 1);
            if (typeArr2.length == 1) {
                BeanUtils.checkNotPrimitive(typeArr2[0]);
                BeanUtils.checkArgument(typeArr[0] == Object.class);
                this.lowerBound = BeanUtils.canonicalize(typeArr2[0]);
                this.upperBound = Object.class;
                return;
            }
            BeanUtils.checkNotPrimitive(typeArr[0]);
            this.lowerBound = null;
            this.upperBound = BeanUtils.canonicalize(typeArr[0]);
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getUpperBounds() {
            return new Type[]{this.upperBound};
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getLowerBounds() {
            Type type = this.lowerBound;
            return type != null ? new Type[]{type} : BeanUtils.EMPTY_TYPE_ARRAY;
        }

        public boolean equals(Object obj) {
            return (obj instanceof WildcardType) && BeanUtils.equals(this, (WildcardType) obj);
        }

        public int hashCode() {
            Type type = this.lowerBound;
            return (type != null ? type.hashCode() + 31 : 1) ^ (this.upperBound.hashCode() + 31);
        }

        public String toString() {
            if (this.lowerBound != null) {
                return "? super " + BeanUtils.typeToString(this.lowerBound);
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            return "? extends " + BeanUtils.typeToString(this.upperBound);
        }
    }

    static void checkArgument(boolean z) {
        if (!z) {
            throw new IllegalArgumentException();
        }
    }

    public static void processJacksonJsonIgnore(final FieldInfo fieldInfo, final Annotation annotation) {
        fieldInfo.ignore = true;
        annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$processJacksonJsonIgnore$11(annotation, fieldInfo, (Method) obj);
            }
        });
    }

    static /* synthetic */ void lambda$processJacksonJsonIgnore$11(Annotation annotation, FieldInfo fieldInfo, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            if ("value".equals(name)) {
                fieldInfo.ignore = ((Boolean) objInvoke).booleanValue();
            }
        } catch (Throwable unused) {
        }
    }

    public static boolean isNoneStaticMemberClass(Class cls, Class cls2) {
        Class<?> enclosingClass;
        if (cls2 == null || cls2.isPrimitive() || cls2 == String.class || cls2 == List.class || (enclosingClass = cls2.getEnclosingClass()) == null) {
            return false;
        }
        if (cls != null && !cls.equals(enclosingClass)) {
            return false;
        }
        ConcurrentMap<Class, Constructor[]> concurrentMap = constructorCache;
        Constructor[] declaredConstructors = concurrentMap.get(cls2);
        if (declaredConstructors == null) {
            declaredConstructors = cls2.getDeclaredConstructors();
            concurrentMap.putIfAbsent(cls2, declaredConstructors);
        }
        if (declaredConstructors.length == 0) {
            return false;
        }
        Constructor<?> constructor = declaredConstructors[0];
        if (constructor.getParameterCount() == 0) {
            return false;
        }
        return enclosingClass.equals(constructor.getParameterTypes()[0]);
    }

    public static void setNoneStaticMemberClassParent(Object obj, Object obj2) {
        Class<?> cls = obj.getClass();
        Field[] declaredFields = declaredFieldCache.get(cls);
        if (declaredFields == null) {
            declaredFields = cls.getDeclaredFields();
            int length = declaredFields.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (Modifier.isStatic(declaredFields[i].getModifiers())) {
                    ArrayList arrayList = new ArrayList(declaredFields.length);
                    for (Field field : declaredFields) {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            arrayList.add(field);
                        }
                    }
                    declaredFields = (Field[]) arrayList.toArray(new Field[arrayList.size()]);
                } else {
                    i++;
                }
            }
            fieldCache.putIfAbsent(cls, declaredFields);
        }
        Field field2 = null;
        for (Field field3 : declaredFields) {
            if ("this$0".equals(field3.getName())) {
                field2 = field3;
            }
        }
        if (field2 != null) {
            field2.setAccessible(true);
            try {
                field2.set(obj, obj2);
            } catch (IllegalAccessException unused) {
                throw new JSONException("setNoneStaticMemberClassParent error, class " + cls);
            }
        }
    }

    public static void cleanupCache(Class cls) {
        if (cls == null) {
            return;
        }
        fieldCache.remove(cls);
        fieldMapCache.remove(cls);
        declaredFieldCache.remove(cls);
        methodCache.remove(cls);
        constructorCache.remove(cls);
    }

    public static void cleanupCache(ClassLoader classLoader) {
        Iterator<Map.Entry<Class, Field[]>> it = fieldCache.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getKey().getClassLoader() == classLoader) {
                it.remove();
            }
        }
        Iterator<Map.Entry<Class, Map<String, Field>>> it2 = fieldMapCache.entrySet().iterator();
        while (it2.hasNext()) {
            if (it2.next().getKey().getClassLoader() == classLoader) {
                it2.remove();
            }
        }
        Iterator<Map.Entry<Class, Field[]>> it3 = declaredFieldCache.entrySet().iterator();
        while (it3.hasNext()) {
            if (it3.next().getKey().getClassLoader() == classLoader) {
                it3.remove();
            }
        }
        Iterator<Map.Entry<Class, Method[]>> it4 = methodCache.entrySet().iterator();
        while (it4.hasNext()) {
            if (it4.next().getKey().getClassLoader() == classLoader) {
                it4.remove();
            }
        }
        Iterator<Map.Entry<Class, Constructor[]>> it5 = constructorCache.entrySet().iterator();
        while (it5.hasNext()) {
            if (it5.next().getKey().getClassLoader() == classLoader) {
                it5.remove();
            }
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static void processJSONType1x(BeanInfo beanInfo, Annotation annotation, Method method) {
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            String name = method.getName();
            switch (name.hashCode()) {
                case -1678076717:
                    if (name.equals("deserializer")) {
                        Class cls = (Class) objInvoke;
                        if (ObjectReader.class.isAssignableFrom(cls)) {
                            beanInfo.deserializer = cls;
                            return;
                        }
                        return;
                    }
                    return;
                case -1315832283:
                    if (name.equals("serializeEnumAsJavaBean") && ((Boolean) objInvoke).booleanValue()) {
                        beanInfo.writeEnumAsJavaBean = true;
                        return;
                    }
                    return;
                case -1210506547:
                    if (!name.equals("alphabetic") || ((Boolean) objInvoke).booleanValue()) {
                        return;
                    }
                    beanInfo.alphabetic = false;
                    return;
                case -1052827512:
                    if (name.equals("naming")) {
                        beanInfo.namingStrategy = ((Enum) objInvoke).name();
                        return;
                    }
                    return;
                case -1008770331:
                    if (name.equals("orders")) {
                        String[] strArr = (String[]) objInvoke;
                        if (strArr.length != 0) {
                            beanInfo.orders = strArr;
                            return;
                        }
                        return;
                    }
                    return;
                case -940893828:
                    if (!name.equals("serialzeFeatures")) {
                        return;
                    }
                    break;
                case -853109563:
                    if (name.equals("typeKey")) {
                        String str = (String) objInvoke;
                        if (str.isEmpty()) {
                            return;
                        }
                        beanInfo.typeKey = str;
                        return;
                    }
                    return;
                case -676507419:
                    if (name.equals("typeName")) {
                        String str2 = (String) objInvoke;
                        if (str2.isEmpty()) {
                            return;
                        }
                        beanInfo.typeName = str2;
                        return;
                    }
                    return;
                case -597985902:
                    if (name.equals("serializer")) {
                        Class cls2 = (Class) objInvoke;
                        if (ObjectWriter.class.isAssignableFrom(cls2)) {
                            beanInfo.writeEnumAsJavaBean = true;
                            beanInfo.serializer = cls2;
                            return;
                        }
                        return;
                    }
                    return;
                case -167039347:
                    if (name.equals("rootName")) {
                        String str3 = (String) objInvoke;
                        if (str3.isEmpty()) {
                            return;
                        }
                        beanInfo.rootName = str3;
                        return;
                    }
                    return;
                case 90259659:
                    if (name.equals("includes")) {
                        String[] strArr2 = (String[]) objInvoke;
                        if (strArr2.length != 0) {
                            beanInfo.includes = strArr2;
                            return;
                        }
                        return;
                    }
                    return;
                case 1752415457:
                    if (name.equals("ignores")) {
                        String[] strArr3 = (String[]) objInvoke;
                        if (strArr3.length != 0) {
                            if (beanInfo.ignores == null) {
                                beanInfo.ignores = strArr3;
                                return;
                            }
                            LinkedHashSet linkedHashSet = new LinkedHashSet();
                            linkedHashSet.addAll(Arrays.asList(beanInfo.ignores));
                            linkedHashSet.addAll(Arrays.asList(strArr3));
                            beanInfo.ignores = (String[]) linkedHashSet.toArray(new String[linkedHashSet.size()]);
                            return;
                        }
                        return;
                    }
                    return;
                case 1869860669:
                    if (!name.equals("serializeFeatures")) {
                        return;
                    }
                    break;
                case 1970571962:
                    if (name.equals("seeAlso")) {
                        Class[] clsArr = (Class[]) objInvoke;
                        if (clsArr.length != 0) {
                            beanInfo.seeAlso = clsArr;
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
            for (Enum r0 : (Enum[]) objInvoke) {
                String strName = r0.name();
                switch (strName.hashCode()) {
                    case -1937516631:
                        if (strName.equals("WriteNullNumberAsZero")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.WriteNullNumberAsZero.mask;
                        }
                        break;
                    case -1779797023:
                        if (strName.equals("IgnoreErrorGetter")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.IgnoreErrorGetter.mask;
                        }
                        break;
                    case -335314544:
                        if (strName.equals("WriteEnumUsingToString")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.WriteEnumUsingToString.mask;
                        }
                        break;
                    case -211922948:
                        if (strName.equals("BrowserCompatible")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.BrowserCompatible.mask;
                        }
                        break;
                    case -102443356:
                        if (strName.equals("WriteNullStringAsEmpty")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.WriteNullStringAsEmpty.mask;
                        }
                        break;
                    case -62964779:
                        if (strName.equals("NotWriteRootClassName")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.NotWriteRootClassName.mask;
                        }
                        break;
                    case 1009181687:
                        if (strName.equals("WriteNullListAsEmpty")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.WriteNullListAsEmpty.mask;
                        }
                        break;
                    case 1519175029:
                        if (strName.equals("WriteNonStringValueAsString")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.WriteNonStringValueAsString.mask;
                        }
                        break;
                    case 1808123471:
                        if (strName.equals("WriteNullBooleanAsFalse")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.WriteNullBooleanAsFalse.mask;
                        }
                        break;
                    case 1879776036:
                        if (strName.equals("WriteClassName")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.WriteClassName.mask;
                        }
                        break;
                    case 2049970061:
                        if (strName.equals("WriteMapNullValue")) {
                            beanInfo.writerFeatures |= JSONWriter.Feature.WriteNulls.mask;
                        }
                        break;
                }
            }
        } catch (Throwable unused) {
        }
    }

    public static void processJacksonJsonFormat(final FieldInfo fieldInfo, final Annotation annotation) {
        annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$processJacksonJsonFormat$12(annotation, fieldInfo, (Method) obj);
            }
        });
    }

    static /* synthetic */ void lambda$processJacksonJsonFormat$12(Annotation annotation, FieldInfo fieldInfo, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            int iHashCode = name.hashCode();
            if (iHashCode == -1097462182) {
                if (name.equals("locale")) {
                    String str = (String) objInvoke;
                    if (str.isEmpty() || "##default".equals(str)) {
                        return;
                    }
                    fieldInfo.locale = Locale.forLanguageTag(str);
                    return;
                }
                return;
            }
            if (iHashCode == -791090288) {
                if (name.equals("pattern")) {
                    String str2 = (String) objInvoke;
                    if (str2.length() != 0) {
                        fieldInfo.format = str2;
                        return;
                    }
                    return;
                }
                return;
            }
            if (iHashCode == 109399969 && name.equals("shape")) {
                String strName = ((Enum) objInvoke).name();
                if ("STRING".equals(strName)) {
                    fieldInfo.features |= JSONWriter.Feature.WriteNonStringValueAsString.mask;
                } else if ("NUMBER".equals(strName)) {
                    fieldInfo.format = "millis";
                }
            }
        } catch (Throwable unused) {
        }
    }

    public static void processJacksonJsonFormat(final BeanInfo beanInfo, final Annotation annotation) {
        annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$processJacksonJsonFormat$13(annotation, beanInfo, (Method) obj);
            }
        });
    }

    static /* synthetic */ void lambda$processJacksonJsonFormat$13(Annotation annotation, BeanInfo beanInfo, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            int iHashCode = name.hashCode();
            if (iHashCode == -1097462182) {
                if (name.equals("locale")) {
                    String str = (String) objInvoke;
                    if (str.isEmpty() || "##default".equals(str)) {
                        return;
                    }
                    beanInfo.locale = Locale.forLanguageTag(str);
                    return;
                }
                return;
            }
            if (iHashCode == -791090288) {
                if (name.equals("pattern")) {
                    String str2 = (String) objInvoke;
                    if (str2.isEmpty()) {
                        return;
                    }
                    beanInfo.format = str2;
                    return;
                }
                return;
            }
            if (iHashCode == 109399969 && name.equals("shape")) {
                String strName = ((Enum) objInvoke).name();
                if ("NUMBER".equals(strName)) {
                    beanInfo.format = "millis";
                } else if ("OBJECT".equals(strName)) {
                    beanInfo.writeEnumAsJavaBean = true;
                }
            }
        } catch (Throwable unused) {
        }
    }

    public static void processJacksonJsonInclude(final BeanInfo beanInfo, final Annotation annotation) {
        annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$processJacksonJsonInclude$14(annotation, beanInfo, (Method) obj);
            }
        });
    }

    static /* synthetic */ void lambda$processJacksonJsonInclude$14(Annotation annotation, BeanInfo beanInfo, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            if ("value".equals(name)) {
                String strName = ((Enum) objInvoke).name();
                int iHashCode = strName.hashCode();
                if (iHashCode == -7755493) {
                    if (strName.equals("NON_EMPTY")) {
                        beanInfo.writerFeatures |= JSONWriter.Feature.NotWriteEmptyArray.mask;
                    }
                } else if (iHashCode == 10566287) {
                    if (strName.equals("NON_DEFAULT")) {
                        beanInfo.writerFeatures |= JSONWriter.Feature.NotWriteDefaultValue.mask;
                    }
                } else if (iHashCode == 1933739535 && strName.equals("ALWAYS")) {
                    beanInfo.writerFeatures |= JSONWriter.Feature.WriteNulls.mask;
                }
            }
        } catch (Throwable unused) {
        }
    }

    public static void processJacksonJsonInclude(final FieldInfo fieldInfo, final Annotation annotation) {
        annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$processJacksonJsonInclude$15(annotation, fieldInfo, (Method) obj);
            }
        });
    }

    static /* synthetic */ void lambda$processJacksonJsonInclude$15(Annotation annotation, FieldInfo fieldInfo, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            if ("value".equals(name)) {
                String strName = ((Enum) objInvoke).name();
                int iHashCode = strName.hashCode();
                if (iHashCode == -7755493) {
                    if (strName.equals("NON_EMPTY")) {
                        fieldInfo.features |= JSONWriter.Feature.NotWriteEmptyArray.mask;
                        fieldInfo.features |= JSONWriter.Feature.IgnoreEmpty.mask;
                        return;
                    }
                    return;
                }
                if (iHashCode == 10566287) {
                    if (strName.equals("NON_DEFAULT")) {
                        fieldInfo.features |= JSONWriter.Feature.NotWriteDefaultValue.mask;
                    }
                } else if (iHashCode == 1933739535 && strName.equals("ALWAYS")) {
                    fieldInfo.features |= JSONWriter.Feature.WriteNulls.mask;
                }
            }
        } catch (Throwable unused) {
        }
    }

    public static void processJacksonJsonUnwrapped(final FieldInfo fieldInfo, final Annotation annotation) {
        annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$processJacksonJsonUnwrapped$16(annotation, fieldInfo, (Method) obj);
            }
        });
    }

    static /* synthetic */ void lambda$processJacksonJsonUnwrapped$16(Annotation annotation, FieldInfo fieldInfo, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            if ("enabled".equals(name) && ((Boolean) objInvoke).booleanValue()) {
                fieldInfo.features = FieldInfo.UNWRAPPED_MASK;
            }
        } catch (Throwable unused) {
        }
    }

    public static void processJacksonJsonTypeName(final BeanInfo beanInfo, final Annotation annotation) {
        annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$processJacksonJsonTypeName$17(annotation, beanInfo, (Method) obj);
            }
        });
    }

    static /* synthetic */ void lambda$processJacksonJsonTypeName$17(Annotation annotation, BeanInfo beanInfo, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            if ("value".equals(name)) {
                String str = (String) objInvoke;
                if (str.isEmpty()) {
                    return;
                }
                beanInfo.typeName = str;
            }
        } catch (Throwable unused) {
        }
    }

    public static void processJacksonJsonSubTypesType(final BeanInfo beanInfo, final int i, final Annotation annotation) {
        annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$processJacksonJsonSubTypesType$18(annotation, beanInfo, i, (Method) obj);
            }
        });
    }

    static /* synthetic */ void lambda$processJacksonJsonSubTypesType$18(Annotation annotation, BeanInfo beanInfo, int i, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            int iHashCode = name.hashCode();
            if (iHashCode == 3373707) {
                if (name.equals("name")) {
                    beanInfo.seeAlsoNames[i] = (String) objInvoke;
                    return;
                }
                return;
            }
            if (iHashCode == 111972721 && name.equals("value")) {
                beanInfo.seeAlso[i] = (Class) objInvoke;
            }
        } catch (Throwable unused) {
        }
    }

    public static void processGsonSerializedName(final FieldInfo fieldInfo, final Annotation annotation) {
        annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$processGsonSerializedName$19(annotation, fieldInfo, (Method) obj);
            }
        });
    }

    static /* synthetic */ void lambda$processGsonSerializedName$19(Annotation annotation, FieldInfo fieldInfo, Method method) {
        String name = method.getName();
        try {
            Object objInvoke = method.invoke(annotation, new Object[0]);
            int iHashCode = name.hashCode();
            if (iHashCode == -1408024454) {
                if (name.equals("alternate")) {
                    String[] strArr = (String[]) objInvoke;
                    if (strArr.length != 0) {
                        fieldInfo.alternateNames = strArr;
                        return;
                    }
                    return;
                }
                return;
            }
            if (iHashCode == 111972721 && name.equals("value")) {
                String str = (String) objInvoke;
                if (str.isEmpty()) {
                    return;
                }
                fieldInfo.fieldName = str;
            }
        } catch (Throwable unused) {
        }
    }

    public static boolean isExtendedMap(Class cls) {
        if (cls == HashMap.class || cls == LinkedHashMap.class || cls == TreeMap.class || cls.getSimpleName().isEmpty()) {
            return false;
        }
        final Class superclass = cls.getSuperclass();
        if ((superclass != HashMap.class && superclass != LinkedHashMap.class && superclass != TreeMap.class) || getDefaultConstructor(cls, false) != null) {
            return false;
        }
        final ArrayList arrayList = new ArrayList();
        declaredFields(cls, new Consumer() { // from class: com.alibaba.fastjson2.util.BeanUtils$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanUtils.lambda$isExtendedMap$20(superclass, arrayList, (Field) obj);
            }
        });
        return !arrayList.isEmpty();
    }

    static /* synthetic */ void lambda$isExtendedMap$20(Class cls, List list, Field field) {
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || field.getDeclaringClass().isAssignableFrom(cls) || "this$0".equals(field.getName())) {
            return;
        }
        list.add(field);
    }
}
