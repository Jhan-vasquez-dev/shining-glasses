package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.codec.BeanInfo;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.function.FieldSupplier;
import com.alibaba.fastjson2.function.FieldSupplierFunction;
import com.alibaba.fastjson2.function.ToByteFunction;
import com.alibaba.fastjson2.function.ToCharFunction;
import com.alibaba.fastjson2.function.ToFloatFunction;
import com.alibaba.fastjson2.function.ToShortFunction;
import com.alibaba.fastjson2.modules.ObjectWriterModule;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.ObjectWriterBaseModule;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
public class ObjectWriterCreator {
    public static final ObjectWriterCreator INSTANCE = new ObjectWriterCreator();
    static final Map<Class, LambdaInfo> lambdaMapping;
    protected final AtomicInteger jitErrorCount = new AtomicInteger();
    protected volatile Throwable jitErrorLast;

    static {
        HashMap map = new HashMap();
        lambdaMapping = map;
        map.put(Boolean.TYPE, new LambdaInfo(Boolean.TYPE, Predicate.class, "test"));
        map.put(Character.TYPE, new LambdaInfo(Character.TYPE, ToCharFunction.class, "applyAsChar"));
        map.put(Byte.TYPE, new LambdaInfo(Byte.TYPE, ToByteFunction.class, "applyAsByte"));
        map.put(Short.TYPE, new LambdaInfo(Short.TYPE, ToShortFunction.class, "applyAsShort"));
        map.put(Integer.TYPE, new LambdaInfo(Integer.TYPE, ToIntFunction.class, "applyAsInt"));
        map.put(Long.TYPE, new LambdaInfo(Long.TYPE, ToLongFunction.class, "applyAsLong"));
        map.put(Float.TYPE, new LambdaInfo(Float.TYPE, ToFloatFunction.class, "applyAsFloat"));
        map.put(Double.TYPE, new LambdaInfo(Double.TYPE, ToDoubleFunction.class, "applyAsDouble"));
    }

    public ObjectWriter createObjectWriter(List<FieldWriter> list) {
        return new ObjectWriterAdapter(null, null, null, 0L, list);
    }

    public ObjectWriter createObjectWriter(FieldWriter... fieldWriterArr) {
        return createObjectWriter(Arrays.asList(fieldWriterArr));
    }

    public <T> ObjectWriter<T> createObjectWriter(String[] strArr, Type[] typeArr, FieldSupplier<T> fieldSupplier) {
        FieldWriter[] fieldWriterArr = new FieldWriter[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            Type type = typeArr[i];
            fieldWriterArr[i] = createFieldWriter(str, type, TypeUtils.getClass(type), new FieldSupplierFunction(fieldSupplier, i));
        }
        return createObjectWriter(fieldWriterArr);
    }

    public ObjectWriter createObjectWriter(Class cls) {
        return createObjectWriter(cls, 0L, JSONFactory.getDefaultObjectWriterProvider());
    }

    public ObjectWriter createObjectWriter(Class cls, FieldWriter... fieldWriterArr) {
        return createObjectWriter(cls, 0L, fieldWriterArr);
    }

    public ObjectWriter createObjectWriter(Class cls, long j, FieldWriter... fieldWriterArr) {
        if (fieldWriterArr.length == 0) {
            return createObjectWriter(cls, j, JSONFactory.getDefaultObjectWriterProvider());
        }
        if (cls != null) {
            String name = cls.getName();
            if ("com.google.common.collect.AbstractMapBasedMultimap$RandomAccessWrappedList".equals(name) || "com.google.common.collect.AbstractMapBasedMultimap$WrappedSet".equals(name)) {
                return new ObjectWriterAdapter(cls, null, null, j, Arrays.asList(fieldWriterArr));
            }
        }
        switch (fieldWriterArr.length) {
            case 1:
                if ((fieldWriterArr[0].features & FieldInfo.VALUE_MASK) == 0) {
                    return new ObjectWriter1(cls, null, null, j, Arrays.asList(fieldWriterArr));
                }
                return new ObjectWriterAdapter(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 2:
                return new ObjectWriter2(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 3:
                return new ObjectWriter3(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 4:
                return new ObjectWriter4(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 5:
                return new ObjectWriter5(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 6:
                return new ObjectWriter6(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 7:
                return new ObjectWriter7(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 8:
                return new ObjectWriter8(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 9:
                return new ObjectWriter9(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 10:
                return new ObjectWriter10(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 11:
                return new ObjectWriter11(cls, null, null, j, Arrays.asList(fieldWriterArr));
            case 12:
                return new ObjectWriter12(cls, null, null, j, Arrays.asList(fieldWriterArr));
            default:
                return new ObjectWriterAdapter(cls, null, null, j, Arrays.asList(fieldWriterArr));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0121  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0109 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r11v0, types: [com.alibaba.fastjson2.writer.ObjectWriter] */
    /* JADX WARN: Type inference failed for: r13v0, types: [com.alibaba.fastjson2.writer.ObjectWriterCreator] */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v12 */
    /* JADX WARN: Type inference failed for: r4v16, types: [com.alibaba.fastjson2.writer.ObjectWriterImplMap] */
    /* JADX WARN: Type inference failed for: r4v17 */
    /* JADX WARN: Type inference failed for: r4v18, types: [com.alibaba.fastjson2.writer.ObjectWriterBaseModule$VoidObjectWriter] */
    /* JADX WARN: Type inference failed for: r4v19 */
    /* JADX WARN: Type inference failed for: r4v32 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected com.alibaba.fastjson2.writer.FieldWriter createFieldWriter(java.lang.Class r14, long r15, com.alibaba.fastjson2.writer.ObjectWriterProvider r17, com.alibaba.fastjson2.codec.BeanInfo r18, com.alibaba.fastjson2.codec.FieldInfo r19, java.lang.reflect.Field r20) {
        /*
            Method dump skipped, instruction units count: 331
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreator.createFieldWriter(java.lang.Class, long, com.alibaba.fastjson2.writer.ObjectWriterProvider, com.alibaba.fastjson2.codec.BeanInfo, com.alibaba.fastjson2.codec.FieldInfo, java.lang.reflect.Field):com.alibaba.fastjson2.writer.FieldWriter");
    }

    public ObjectWriter createObjectWriter(Class cls, long j, List<ObjectWriterModule> list) {
        ObjectWriterProvider provider = null;
        for (ObjectWriterModule objectWriterModule : list) {
            if (provider == null) {
                provider = objectWriterModule.getProvider();
            }
        }
        return createObjectWriter(cls, j, provider);
    }

    protected void setDefaultValue(List<FieldWriter> list, Class cls) {
        Object objNewInstance;
        Constructor defaultConstructor = BeanUtils.getDefaultConstructor(cls, true);
        if (defaultConstructor == null) {
            return;
        }
        int parameterCount = defaultConstructor.getParameterCount();
        try {
            defaultConstructor.setAccessible(true);
            if (parameterCount == 0) {
                objNewInstance = defaultConstructor.newInstance(new Object[0]);
            } else if (parameterCount != 1) {
                return;
            } else {
                objNewInstance = defaultConstructor.newInstance(true);
            }
            Iterator<FieldWriter> it = list.iterator();
            while (it.hasNext()) {
                it.next().setDefaultValue(objNewInstance);
            }
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0206  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.alibaba.fastjson2.writer.ObjectWriter createObjectWriter(java.lang.Class r17, long r18, final com.alibaba.fastjson2.writer.ObjectWriterProvider r20) {
        /*
            Method dump skipped, instruction units count: 550
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreator.createObjectWriter(java.lang.Class, long, com.alibaba.fastjson2.writer.ObjectWriterProvider):com.alibaba.fastjson2.writer.ObjectWriter");
    }

    /* JADX INFO: renamed from: lambda$createObjectWriter$0$com-alibaba-fastjson2-writer-ObjectWriterCreator, reason: not valid java name */
    /* synthetic */ void m65x7bf382a9(FieldInfo fieldInfo, Class cls, long j, ObjectWriterProvider objectWriterProvider, BeanInfo beanInfo, Map map, Field field) {
        fieldInfo.init();
        FieldWriter fieldWriterCreateFieldWriter = createFieldWriter(cls, j, objectWriterProvider, beanInfo, fieldInfo, field);
        if (fieldWriterCreateFieldWriter != null) {
            if (fieldInfo.writeUsing != null && (fieldWriterCreateFieldWriter instanceof FieldWriterObject)) {
                ((FieldWriterObject) fieldWriterCreateFieldWriter).writeUsing = true;
            }
            map.put(fieldWriterCreateFieldWriter.fieldName, fieldWriterCreateFieldWriter);
        }
    }

    /* JADX INFO: renamed from: lambda$createObjectWriter$1$com-alibaba-fastjson2-writer-ObjectWriterCreator, reason: not valid java name */
    /* synthetic */ void m66x6d9d28c8(FieldInfo fieldInfo, Class cls, long j, ObjectWriterProvider objectWriterProvider, BeanInfo beanInfo, Map map, Field field) {
        fieldInfo.init();
        fieldInfo.ignore = (field.getModifiers() & 1) == 0;
        FieldWriter fieldWriterCreateFieldWriter = createFieldWriter(cls, j, objectWriterProvider, beanInfo, fieldInfo, field);
        if (fieldWriterCreateFieldWriter != null) {
            if (fieldInfo.writeUsing != null && (fieldWriterCreateFieldWriter instanceof FieldWriterObject)) {
                ((FieldWriterObject) fieldWriterCreateFieldWriter).writeUsing = true;
            }
            FieldWriter fieldWriter = (FieldWriter) map.putIfAbsent(fieldWriterCreateFieldWriter.fieldName, fieldWriterCreateFieldWriter);
            if (fieldWriter == null || fieldWriter.compareTo(fieldWriterCreateFieldWriter) <= 0) {
                return;
            }
            map.put(fieldWriterCreateFieldWriter.fieldName, fieldWriterCreateFieldWriter);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:112:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0135  */
    /* JADX WARN: Type inference failed for: r0v49, types: [java.lang.String[]] */
    /* JADX WARN: Type inference failed for: r14v3, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r14v7 */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* JADX WARN: Type inference failed for: r1v27 */
    /* JADX WARN: Type inference failed for: r1v28 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v30 */
    /* JADX WARN: Type inference failed for: r1v31 */
    /* JADX WARN: Type inference failed for: r1v32 */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.alibaba.fastjson2.writer.ObjectWriterCreator] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.alibaba.fastjson2.writer.ObjectWriterCreator] */
    /* JADX WARN: Type inference failed for: r1v6, types: [com.alibaba.fastjson2.writer.ObjectWriterCreator] */
    /* JADX WARN: Type inference failed for: r4v1, types: [java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v5, types: [java.lang.String] */
    /* JADX INFO: renamed from: lambda$createObjectWriter$2$com-alibaba-fastjson2-writer-ObjectWriterCreator, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    /* synthetic */ void m67x5f46cee7(com.alibaba.fastjson2.codec.FieldInfo r18, long r19, com.alibaba.fastjson2.codec.BeanInfo r21, com.alibaba.fastjson2.writer.ObjectWriterProvider r22, java.lang.Class r23, boolean r24, java.util.Map r25, java.lang.reflect.Method r26) {
        /*
            Method dump skipped, instruction units count: 407
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreator.m67x5f46cee7(com.alibaba.fastjson2.codec.FieldInfo, long, com.alibaba.fastjson2.codec.BeanInfo, com.alibaba.fastjson2.writer.ObjectWriterProvider, java.lang.Class, boolean, java.util.Map, java.lang.reflect.Method):void");
    }

    protected static String getFieldName(Class cls, ObjectWriterProvider objectWriterProvider, BeanInfo beanInfo, boolean z, FieldInfo fieldInfo, Method method) {
        char cCharAt;
        Field field;
        if (fieldInfo.fieldName != null && !fieldInfo.fieldName.isEmpty()) {
            return fieldInfo.fieldName;
        }
        if (z) {
            return method.getName();
        }
        String str = BeanUtils.getterName(method, beanInfo.f4kotlin, beanInfo.namingStrategy);
        if ((objectWriterProvider.userDefineMask & 64) != 0 && (field = BeanUtils.getField(cls, method)) != null) {
            return field.getName();
        }
        int length = str.length();
        char cCharAt2 = length > 0 ? str.charAt(0) : (char) 0;
        if ((length == 1 && cCharAt2 >= 'a' && cCharAt2 <= 'z') || (length > 2 && cCharAt2 >= 'A' && cCharAt2 <= 'Z' && (cCharAt = str.charAt(1)) >= 'A' && cCharAt <= 'Z')) {
            char[] charArray = str.toCharArray();
            if (cCharAt2 >= 'a') {
                charArray[0] = (char) (charArray[0] - ' ');
            } else {
                charArray[0] = (char) (charArray[0] + TokenParser.SP);
            }
            Field declaredField = BeanUtils.getDeclaredField(cls, new String(charArray));
            if (declaredField != null && (length == 1 || Modifier.isPublic(declaredField.getModifiers()))) {
                return declaredField.getName();
            }
        }
        return str;
    }

    protected static void configSerializeFilters(BeanInfo beanInfo, ObjectWriterAdapter objectWriterAdapter) {
        for (Class<? extends Filter> cls : beanInfo.serializeFilters) {
            if (Filter.class.isAssignableFrom(cls)) {
                try {
                    objectWriterAdapter.setFilter(cls.newInstance());
                } catch (IllegalAccessException | InstantiationException unused) {
                }
            }
        }
    }

    protected void handleIgnores(BeanInfo beanInfo, List<FieldWriter> list) {
        if (beanInfo.ignores == null || beanInfo.ignores.length == 0) {
            return;
        }
        for (int size = list.size() - 1; size >= 0; size--) {
            FieldWriter fieldWriter = list.get(size);
            String[] strArr = beanInfo.ignores;
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (strArr[i].equals(fieldWriter.fieldName)) {
                    list.remove(size);
                    break;
                }
                i++;
            }
        }
    }

    public <T> FieldWriter<T> createFieldWriter(String str, String str2, Field field) {
        return createFieldWriter(JSONFactory.getDefaultObjectWriterProvider(), str, 0, 0L, str2, null, field, null);
    }

    public <T> FieldWriter<T> createFieldWriter(String str, int i, long j, String str2, Field field) {
        return createFieldWriter(JSONFactory.getDefaultObjectWriterProvider(), str, i, j, str2, null, field, null);
    }

    public <T> FieldWriter<T> createFieldWriter(String str, int i, long j, String str2, String str3, Field field, ObjectWriter objectWriter) {
        return createFieldWriter(JSONFactory.getDefaultObjectWriterProvider(), str, i, j, str2, str3, field, objectWriter);
    }

    public final <T> FieldWriter<T> createFieldWriter(ObjectWriterProvider objectWriterProvider, String str, int i, long j, String str2, String str3, Field field, ObjectWriter objectWriter) {
        return createFieldWriter(objectWriterProvider, str, i, j, str2, (Locale) null, str3, field, objectWriter);
    }

    public <T> FieldWriter<T> createFieldWriter(ObjectWriterProvider objectWriterProvider, String str, int i, long j, String str2, Locale locale, String str3, Field field, ObjectWriter objectWriter) {
        return createFieldWriter(objectWriterProvider, str, i, j, str2, locale, str3, field, objectWriter, (Class<?>) null);
    }

    public <T> FieldWriter<T> createFieldWriter(ObjectWriterProvider objectWriterProvider, String str, int i, long j, String str2, Locale locale, String str3, Field field, ObjectWriter objectWriter, Class<?> cls) {
        ObjectWriter objectWriter2;
        Method method = (field.getDeclaringClass() == Throwable.class && "stackTrace".equals(field.getName())) ? BeanUtils.getMethod(Throwable.class, "getStackTrace") : null;
        if (method != null) {
            return createFieldWriter(objectWriterProvider, Throwable.class, str, i, j, str2, str3, method, objectWriter);
        }
        Class<?> type = field.getType();
        Type genericType = field.getGenericType();
        if (objectWriter != null) {
            FieldWriterObject fieldWriterObject = new FieldWriterObject(str, i, j, str2, null, str3, genericType, type, field, null);
            fieldWriterObject.initValueClass = type;
            if (objectWriter != ObjectWriterBaseModule.VoidObjectWriter.INSTANCE) {
                fieldWriterObject.initObjectWriter = objectWriter;
            }
            return fieldWriterObject;
        }
        if (type == Boolean.TYPE) {
            return new FieldWriterBoolValField(str, i, j, str2, str3, field, type);
        }
        if (type == Byte.TYPE) {
            return new FieldWriterInt8ValField(str, i, j, str2, str3, field);
        }
        if (type == Short.TYPE) {
            return new FieldWriterInt16ValField(str, i, j, str2, str3, field);
        }
        if (type == Integer.TYPE) {
            return new FieldWriterInt32Val(str, i, j, str2, str3, field);
        }
        if (type == Long.TYPE) {
            if (str2 == null || str2.isEmpty() || "string".equals(str2)) {
                return new FieldWriterInt64ValField(str, i, j, str2, str3, field);
            }
            return new FieldWriterMillisField(str, i, j, str2, str3, field);
        }
        if (type == Float.TYPE) {
            return new FieldWriterFloatValField(str, i, j, str2, str3, field);
        }
        if (type == Float.class) {
            return new FieldWriterFloatField(str, i, j, str2, str3, field);
        }
        if (type == Double.TYPE) {
            return new FieldWriterDoubleValField(str, i, str2, str3, field);
        }
        if (type == Double.class) {
            return new FieldWriterDoubleField(str, i, j, str2, str3, field);
        }
        if (type == Character.TYPE) {
            return new FieldWriterCharValField(str, i, j, str2, str3, field);
        }
        if (type == BigInteger.class) {
            return new FieldWriterBigIntField(str, i, j, str2, str3, field);
        }
        if (type == BigDecimal.class) {
            return new FieldWriterBigDecimalField(str, i, j, str2, str3, field);
        }
        if (type == Date.class) {
            return new FieldWriterDateField(str, i, j, str2, str3, field);
        }
        if (type == String.class) {
            return new FieldWriterStringField(str, i, j, str2, str3, field);
        }
        if (type.isEnum()) {
            BeanInfo beanInfoCreateBeanInfo = objectWriterProvider.createBeanInfo();
            objectWriterProvider.getBeanInfo(beanInfoCreateBeanInfo, type);
            boolean z = beanInfoCreateBeanInfo.writeEnumAsJavaBean;
            if (!z && (objectWriter2 = objectWriterProvider.cache.get(type)) != null && !(objectWriter2 instanceof ObjectWriterImplEnum)) {
                z = true;
            }
            if (BeanUtils.getEnumValueField(type, objectWriterProvider) == null && !z && BeanUtils.getEnumAnnotationNames(type) == null) {
                return new FieldWriterEnum(str, i, j, str2, str3, genericType, type, field, null);
            }
        }
        if (type == List.class || type == ArrayList.class) {
            return new FieldWriterListField(str, genericType instanceof ParameterizedType ? ((ParameterizedType) genericType).getActualTypeArguments()[0] : null, i, j, str2, str3, genericType, type, field, cls);
        }
        if (Map.class.isAssignableFrom(type)) {
            return new FieldWriterMapField(str, i, j, str2, locale, str3, field.getGenericType(), type, field, null, cls);
        }
        if (type.isArray() && !type.getComponentType().isPrimitive()) {
            Class<?> componentType = type.getComponentType();
            return new FieldWriterObjectArrayField(str, componentType, i, j, str2, str3, componentType, type, field);
        }
        return new FieldWriterObject(str, i, j, str2, locale, str3, field.getGenericType(), type, field, null);
    }

    public <T> FieldWriter<T> createFieldWriter(Class<T> cls, String str, String str2, Method method) {
        return createFieldWriter(cls, str, 0, 0L, str2, method);
    }

    public <T> FieldWriter<T> createFieldWriter(Class<T> cls, String str, int i, long j, String str2, Method method) {
        return createFieldWriter((ObjectWriterProvider) null, cls, str, i, j, str2, (String) null, method, (ObjectWriter) null);
    }

    public <T> FieldWriter<T> createFieldWriter(ObjectWriterProvider objectWriterProvider, Class<T> cls, String str, int i, long j, String str2, String str3, Method method, ObjectWriter objectWriter) {
        return createFieldWriter(objectWriterProvider, cls, str, i, j, str2, (Locale) null, str3, method, objectWriter);
    }

    public <T> FieldWriter<T> createFieldWriter(ObjectWriterProvider objectWriterProvider, Class<T> cls, String str, int i, long j, String str2, Locale locale, String str3, Method method, ObjectWriter objectWriter) {
        return createFieldWriter(objectWriterProvider, cls, str, i, j, str2, locale, str3, method, objectWriter, (Class<?>) null);
    }

    public <T> FieldWriter<T> createFieldWriter(ObjectWriterProvider objectWriterProvider, Class<T> cls, String str, int i, long j, String str2, Locale locale, String str3, Method method, ObjectWriter objectWriter, Class<?> cls2) {
        Type type;
        String str4;
        method.setAccessible(true);
        Class<?> returnType = method.getReturnType();
        Type genericReturnType = method.getGenericReturnType();
        ObjectWriter initWriter = (objectWriter != null || objectWriterProvider == null) ? objectWriter : getInitWriter(objectWriterProvider, returnType);
        if (initWriter != null) {
            FieldWriterObjectMethod fieldWriterObjectMethod = new FieldWriterObjectMethod(str, i, j, str2, locale, str3, genericReturnType, returnType, null, method);
            fieldWriterObjectMethod.initValueClass = returnType;
            if (initWriter != ObjectWriterBaseModule.VoidObjectWriter.INSTANCE) {
                fieldWriterObjectMethod.initObjectWriter = initWriter;
            }
            return fieldWriterObjectMethod;
        }
        String str5 = str == null ? BeanUtils.getterName(method, false, null) : str;
        Field field = (j & FieldInfo.RECORD) != 0 ? null : BeanUtils.getField(cls, method);
        if (returnType == Boolean.TYPE || returnType == Boolean.class) {
            return new FieldWriterBoolMethod(str5, i, j, str2, str3, field, method, returnType);
        }
        if (returnType == Integer.TYPE || returnType == Integer.class) {
            return new FieldWriterInt32Method(str5, i, j, str2, str3, field, method, returnType);
        }
        if (returnType == Float.TYPE || returnType == Float.class) {
            return new FieldWriterFloatMethod(str5, i, j, str2, str3, returnType, returnType, field, method);
        }
        if (returnType == Double.TYPE || returnType == Double.class) {
            return new FieldWriterDoubleMethod(str5, i, j, str2, str3, returnType, returnType, field, method);
        }
        if (returnType == Long.TYPE || returnType == Long.class) {
            String str6 = str5;
            if (str2 == null || str2.isEmpty() || "string".equals(str2)) {
                return new FieldWriterInt64Method(str6, i, j, str2, str3, field, method, returnType);
            }
            return new FieldWriterMillisMethod(str6, i, j, str2, str3, returnType, field, method);
        }
        if (returnType == Short.TYPE || returnType == Short.class) {
            return new FieldWriterInt16Method(str5, i, j, str2, str3, field, method, returnType);
        }
        if (returnType == Byte.TYPE || returnType == Byte.class) {
            return new FieldWriterInt8Method(str5, i, j, str2, str3, field, method, returnType);
        }
        if (returnType == Character.TYPE || returnType == Character.class) {
            return new FieldWriterCharMethod(str5, i, j, str2, str3, field, method, returnType);
        }
        if (returnType == BigDecimal.class) {
            return new FieldWriterBigDecimalMethod(str5, i, j, str2, str3, field, method);
        }
        String str7 = str5;
        if (returnType.isEnum() && BeanUtils.getEnumValueField(returnType, objectWriterProvider) == null && !BeanUtils.isWriteEnumAsJavaBean(returnType) && BeanUtils.getEnumAnnotationNames(returnType) == null) {
            return new FieldWriterEnumMethod(str7, i, j, str2, str3, returnType, field, method);
        }
        if (returnType == Date.class) {
            if (str2 != null) {
                String strTrim = str2.trim();
                str4 = strTrim.isEmpty() ? null : strTrim;
            } else {
                str4 = str2;
            }
            return new FieldWriterDateMethod(str7, i, j, str4, str3, returnType, field, method);
        }
        if (returnType == String.class) {
            return new FieldWriterStringMethod(str7, i, str2, str3, j, field, method);
        }
        if (returnType == List.class || returnType == Iterable.class) {
            if (genericReturnType instanceof ParameterizedType) {
                type = ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
            } else {
                type = Object.class;
            }
            return new FieldWriterListMethod(str7, type, i, j, str2, str3, null, method, genericReturnType, returnType, cls2);
        }
        if (Map.class.isAssignableFrom(returnType)) {
            return new FieldWriterMapMethod(str7, i, j, str2, locale, str3, genericReturnType, returnType, null, method, cls2);
        }
        if (returnType == Float[].class || returnType == Double[].class || returnType == BigDecimal[].class) {
            return new FieldWriterObjectArrayMethod(str7, returnType.getComponentType(), i, j, str2, str3, genericReturnType, returnType, field, method);
        }
        return new FieldWriterObjectMethod(str7, i, j, str2, locale, str3, genericReturnType, returnType, null, method);
    }

    public <T> FieldWriter createFieldWriter(String str, ToLongFunction<T> toLongFunction) {
        return new FieldWriterInt64ValFunc(str, 0, 0L, null, null, null, null, toLongFunction);
    }

    public <T> FieldWriter createFieldWriter(String str, ToIntFunction<T> toIntFunction) {
        return new FieldWriterInt32ValFunc(str, 0, 0L, null, null, null, null, toIntFunction);
    }

    public <T> FieldWriter createFieldWriter(String str, Field field, Method method, ToIntFunction<T> toIntFunction) {
        return new FieldWriterInt32ValFunc(str, 0, 0L, null, null, field, method, toIntFunction);
    }

    public <T> FieldWriter createFieldWriter(String str, ToShortFunction<T> toShortFunction) {
        return new FieldWriterInt16ValFunc(str, 0, 0L, null, null, null, null, toShortFunction);
    }

    public <T> FieldWriter createFieldWriter(String str, ToByteFunction<T> toByteFunction) {
        return new FieldWriterInt8ValFunc(str, 0, 0L, null, null, null, null, toByteFunction);
    }

    public <T> FieldWriter createFieldWriter(String str, ToFloatFunction<T> toFloatFunction) {
        return new FieldWriterFloatValueFunc(str, 0, 0L, null, null, null, null, toFloatFunction);
    }

    public <T> FieldWriter createFieldWriter(String str, ToDoubleFunction<T> toDoubleFunction) {
        return new FieldWriterDoubleValueFunc(str, 0, 0L, null, null, null, null, toDoubleFunction);
    }

    public <T> FieldWriter createFieldWriter(String str, ToCharFunction<T> toCharFunction) {
        return new FieldWriterCharValFunc(str, 0, 0L, null, null, null, null, toCharFunction);
    }

    public <T> FieldWriter createFieldWriter(String str, Predicate<T> predicate) {
        return new FieldWriterBoolValFunc(str, 0, 0L, null, null, null, null, predicate);
    }

    public <T, V> FieldWriter createFieldWriter(String str, Class cls, Function<T, V> function) {
        return createFieldWriter((ObjectWriterProvider) null, (Class) null, str, 0, 0L, (String) null, (String) null, cls, cls, (Method) null, function);
    }

    public <T, V> FieldWriter createFieldWriter(String str, Class cls, Field field, Method method, Function<T, V> function) {
        return createFieldWriter(null, null, str, 0, 0L, null, null, cls, cls, field, method, function);
    }

    public <T, V> FieldWriter createFieldWriter(String str, Type type, Class cls, Function<T, V> function) {
        return createFieldWriter((ObjectWriterProvider) null, (Class) null, str, 0, 0L, (String) null, (String) null, type, cls, (Method) null, function);
    }

    public <T, V> FieldWriter createFieldWriter(String str, long j, String str2, Class cls, Function<T, V> function) {
        return createFieldWriter((ObjectWriterProvider) null, (Class) null, str, 0, j, str2, (String) null, cls, cls, (Method) null, function);
    }

    public <T, V> FieldWriter<T> createFieldWriter(ObjectWriterProvider objectWriterProvider, Class<T> cls, String str, int i, long j, String str2, String str3, Type type, Class<V> cls2, Method method, Function<T, V> function) {
        return createFieldWriter(objectWriterProvider, cls, str, i, j, str2, null, str3, type, cls2, null, method, function);
    }

    public <T, V> FieldWriter<T> createFieldWriter(ObjectWriterProvider objectWriterProvider, Class<T> cls, String str, int i, long j, String str2, String str3, Type type, Class<V> cls2, Field field, Method method, Function<T, V> function) {
        return createFieldWriter(objectWriterProvider, cls, str, i, j, str2, null, str3, type, cls2, field, method, function);
    }

    public <T, V> FieldWriter<T> createFieldWriter(ObjectWriterProvider objectWriterProvider, Class<T> cls, String str, int i, long j, String str2, Locale locale, String str3, Type type, Class<V> cls2, Field field, Method method, Function<T, V> function) {
        return createFieldWriter(objectWriterProvider, cls, str, i, j, str2, locale, str3, type, cls2, field, method, function, null);
    }

    public <T, V> FieldWriter<T> createFieldWriter(ObjectWriterProvider objectWriterProvider, Class<T> cls, String str, int i, long j, String str2, Locale locale, String str3, Type type, Class<V> cls2, Field field, Method method, Function<T, V> function, Class<?> cls3) {
        ObjectWriter objectWriter;
        if (cls2 == Byte.class) {
            return new FieldWriterInt8Func(str, i, j, str2, str3, field, method, function);
        }
        if (cls2 == Short.class) {
            return new FieldWriterInt16Func(str, i, j, str2, str3, field, method, function);
        }
        if (cls2 == Integer.class) {
            return new FieldWriterInt32Func(str, i, j, str2, str3, field, method, function);
        }
        if (cls2 == Long.class) {
            return new FieldWriterInt64Func(str, i, j, str2, str3, field, method, function);
        }
        if (cls2 == BigInteger.class) {
            return new FieldWriterBigIntFunc(str, i, j, str2, str3, field, method, function);
        }
        if (cls2 == BigDecimal.class) {
            return new FieldWriterBigDecimalFunc(str, i, j, str2, str3, field, method, function);
        }
        if (cls2 == String.class) {
            return new FieldWriterStringFunc(str, i, j, str2, str3, field, method, function);
        }
        if (cls2 == Date.class) {
            return new FieldWriterDateFunc(str, i, j, str2, str3, field, method, function);
        }
        if (cls2 == LocalDate.class) {
            return new FieldWriterLocalDateFunc(str, i, j, str2, str3, type, cls2, field, method, function);
        }
        if (cls2 == OffsetDateTime.class) {
            return new FieldWriterOffsetDateTimeFunc(str, i, j, str2, str3, type, cls2, field, method, function);
        }
        if (cls2 == UUID.class) {
            return new FieldWriterUUIDFunc(str, i, j, str2, str3, type, cls2, field, method, function);
        }
        if (Calendar.class.isAssignableFrom(cls2)) {
            return new FieldWriterCalendarFunc(str, i, j, str2, str3, field, method, function);
        }
        if (cls2.isEnum()) {
            if (objectWriterProvider == null) {
                objectWriterProvider = JSONFactory.getDefaultObjectWriterProvider();
            }
            BeanInfo beanInfoCreateBeanInfo = objectWriterProvider.createBeanInfo();
            objectWriterProvider.getBeanInfo(beanInfoCreateBeanInfo, cls2);
            boolean z = beanInfoCreateBeanInfo.writeEnumAsJavaBean;
            if (!z && (objectWriter = objectWriterProvider.cache.get(cls2)) != null && !(objectWriter instanceof ObjectWriterImplEnum)) {
                z = true;
            }
            if (!z && BeanUtils.getEnumValueField(cls2, objectWriterProvider) == null && BeanUtils.getEnumAnnotationNames(cls2) == null) {
                return new FieldWriterEnumFunc(str, i, j, str2, str3, type, cls2, field, method, function);
            }
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if ((rawType == List.class || rawType == ArrayList.class) && actualTypeArguments.length == 1) {
                Type type2 = actualTypeArguments[0];
                if (type2 == String.class) {
                    return new FieldWriterListStrFunc(str, i, j, str2, str3, field, method, function, type, cls2);
                }
                return new FieldWriterListFunc(str, i, j, str2, str3, type2, field, method, function, type, cls2, cls3);
            }
            if ((rawType instanceof Class) && Map.class.isAssignableFrom((Class) rawType)) {
                return new FieldWriterMapFunction(str, i, j, str2, locale, str3, type, cls2, field, method, function, cls3);
            }
        }
        if (Modifier.isFinal(cls2.getModifiers())) {
            return new FieldWriterObjectFuncFinal(str, i, j, str2, str3, type, cls2, field, method, function);
        }
        return new FieldWriterObjectFunc(str, i, j, str2, locale, str3, type, cls2, field, method, function);
    }

    static class LambdaInfo {
        final Class fieldClass;
        final MethodType invokedType;
        final String methodName;
        final MethodType methodType;
        final MethodType samMethodType;
        final Class supplierClass;

        LambdaInfo(Class cls, Class cls2, String str) {
            this.fieldClass = cls;
            this.supplierClass = cls2;
            this.methodName = str;
            this.methodType = MethodType.methodType(cls);
            this.invokedType = MethodType.methodType(cls2);
            this.samMethodType = MethodType.methodType((Class<?>) cls, (Class<?>) Object.class);
        }
    }

    Object lambdaGetter(Class cls, Class cls2, Method method) {
        MethodType methodType;
        MethodType methodType2;
        MethodType methodType3;
        String str;
        MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(cls);
        LambdaInfo lambdaInfo = lambdaMapping.get(cls2);
        if (lambdaInfo != null) {
            methodType = lambdaInfo.methodType;
            methodType2 = lambdaInfo.invokedType;
            str = lambdaInfo.methodName;
            methodType3 = lambdaInfo.samMethodType;
        } else {
            methodType = MethodType.methodType(cls2);
            methodType2 = TypeUtils.METHOD_TYPE_FUNCTION;
            methodType3 = TypeUtils.METHOD_TYPE_OBJECT_OBJECT;
            str = "apply";
        }
        String str2 = str;
        MethodType methodType4 = methodType3;
        try {
            MethodHandle methodHandleFindVirtual = lookupTrustedLookup.findVirtual(cls, method.getName(), methodType);
            return (Object) LambdaMetafactory.metafactory(lookupTrustedLookup, str2, methodType2, methodType4, methodHandleFindVirtual, methodHandleFindVirtual.type()).getTarget().invoke();
        } catch (Throwable th) {
            throw new JSONException("create fieldLambdaGetter error, method : " + method, th);
        }
    }

    protected ObjectWriter getInitWriter(ObjectWriterProvider objectWriterProvider, Class cls) {
        ObjectWriter objectWriter;
        ObjectWriter objectWriter2;
        ObjectWriter objectWriter3;
        ObjectWriter objectWriter4;
        if (cls == Date.class) {
            if ((objectWriterProvider.userDefineMask & 16) == 0 || (objectWriter4 = objectWriterProvider.cache.get(cls)) == ObjectWriterImplDate.INSTANCE) {
                return null;
            }
            return objectWriter4;
        }
        if (cls == Integer.TYPE || cls == Integer.class) {
            if ((objectWriterProvider.userDefineMask & 2) == 0 || (objectWriter = objectWriterProvider.cache.get(Integer.class)) == ObjectWriterImplInt32.INSTANCE) {
                return null;
            }
            return objectWriter;
        }
        if (cls == Long.TYPE || cls == Long.class) {
            if ((objectWriterProvider.userDefineMask & 4) == 0 || (objectWriter2 = objectWriterProvider.cache.get(Long.class)) == ObjectWriterImplInt64.INSTANCE) {
                return null;
            }
            return objectWriter2;
        }
        if (cls == BigDecimal.class) {
            if ((objectWriterProvider.userDefineMask & 8) == 0 || (objectWriter3 = objectWriterProvider.cache.get(cls)) == ObjectWriterImplBigDecimal.INSTANCE) {
                return null;
            }
            return objectWriter3;
        }
        if (!Enum.class.isAssignableFrom(cls)) {
            return null;
        }
        ObjectWriter objectWriter5 = objectWriterProvider.cache.get(cls);
        if (objectWriter5 instanceof ObjectWriterImplEnum) {
            return null;
        }
        return objectWriter5;
    }

    <T> FieldWriter<T> createFieldWriterLambda(ObjectWriterProvider objectWriterProvider, Class<T> cls, String str, int i, long j, String str2, String str3, Method method, ObjectWriter objectWriter, Class<?> cls2) {
        Class<?> returnType = method.getReturnType();
        Type genericReturnType = method.getGenericReturnType();
        if (((objectWriter != null || objectWriterProvider == null) ? objectWriter : getInitWriter(objectWriterProvider, returnType)) != null) {
            return null;
        }
        String name = cls.getName();
        if (name.indexOf(36) != -1 && name.contains("$$")) {
            return null;
        }
        Object objLambdaGetter = lambdaGetter(cls, returnType, method);
        Field field = BeanUtils.getField(cls, method);
        if (returnType == Integer.TYPE) {
            return new FieldWriterInt32ValFunc(str, i, j, str2, str3, null, method, (ToIntFunction) objLambdaGetter);
        }
        if (returnType == Long.TYPE) {
            if (str2 == null || str2.isEmpty() || "string".equals(str2)) {
                return new FieldWriterInt64ValFunc(str, i, j, str2, str3, field, method, (ToLongFunction) objLambdaGetter);
            }
            return new FieldWriterMillisFunc(str, i, j, str2, str3, field, method, (ToLongFunction) objLambdaGetter);
        }
        if (returnType == Boolean.TYPE) {
            return new FieldWriterBoolValFunc(str, i, j, str2, str3, field, method, (Predicate) objLambdaGetter);
        }
        if (returnType == Boolean.class) {
            return new FieldWriterBooleanFunc(str, i, j, str2, str3, field, method, (Function) objLambdaGetter);
        }
        if (returnType == Short.TYPE) {
            return new FieldWriterInt16ValFunc(str, i, j, str2, str3, field, method, (ToShortFunction) objLambdaGetter);
        }
        if (returnType == Byte.TYPE) {
            return new FieldWriterInt8ValFunc(str, i, j, str2, str3, field, method, (ToByteFunction) objLambdaGetter);
        }
        if (returnType == Float.TYPE) {
            return new FieldWriterFloatValueFunc(str, i, j, str2, str3, field, method, (ToFloatFunction) objLambdaGetter);
        }
        if (returnType == Float.class) {
            return new FieldWriterFloatFunc(str, i, j, str2, str3, field, method, (Function) objLambdaGetter);
        }
        if (returnType == Double.TYPE) {
            return new FieldWriterDoubleValueFunc(str, i, j, str2, str3, field, method, (ToDoubleFunction) objLambdaGetter);
        }
        if (returnType == Double.class) {
            return new FieldWriterDoubleFunc(str, i, j, str2, str3, field, method, (Function) objLambdaGetter);
        }
        if (returnType == Character.TYPE) {
            return new FieldWriterCharValFunc(str, i, j, str2, str3, field, method, (ToCharFunction) objLambdaGetter);
        }
        return createFieldWriter(objectWriterProvider, cls, str, i, j, str2, null, str3, genericReturnType, returnType, field, method, (Function) objLambdaGetter, cls2);
    }
}
