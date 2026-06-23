package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.codec.BeanInfo;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.function.ObjBoolConsumer;
import com.alibaba.fastjson2.function.ObjByteConsumer;
import com.alibaba.fastjson2.function.ObjCharConsumer;
import com.alibaba.fastjson2.function.ObjFloatConsumer;
import com.alibaba.fastjson2.function.ObjShortConsumer;
import com.alibaba.fastjson2.modules.ObjectReaderAnnotationProcessor;
import com.alibaba.fastjson2.modules.ObjectReaderModule;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class ObjectReaderCreator {
    public static final ObjectReaderCreator INSTANCE;
    public static final boolean JIT;
    protected static final Map<Class, LambdaSetterInfo> methodTypeMapping;
    protected final AtomicInteger jitErrorCount = new AtomicInteger();
    protected volatile Throwable jitErrorLast;

    public Function<Consumer, ByteArrayValueConsumer> createByteArrayValueConsumerCreator(Class cls, FieldReader[] fieldReaderArr) {
        return null;
    }

    public Function<Consumer, CharArrayValueConsumer> createCharArrayValueConsumerCreator(Class cls, FieldReader[] fieldReaderArr) {
        return null;
    }

    static {
        JIT = (JDKUtils.ANDROID || JDKUtils.GRAAL) ? false : true;
        INSTANCE = new ObjectReaderCreator();
        HashMap map = new HashMap();
        methodTypeMapping = map;
        map.put(Boolean.TYPE, new LambdaSetterInfo(Boolean.TYPE, ObjBoolConsumer.class));
        map.put(Byte.TYPE, new LambdaSetterInfo(Byte.TYPE, ObjByteConsumer.class));
        map.put(Short.TYPE, new LambdaSetterInfo(Short.TYPE, ObjShortConsumer.class));
        map.put(Integer.TYPE, new LambdaSetterInfo(Integer.TYPE, ObjIntConsumer.class));
        map.put(Long.TYPE, new LambdaSetterInfo(Long.TYPE, ObjLongConsumer.class));
        map.put(Character.TYPE, new LambdaSetterInfo(Character.TYPE, ObjCharConsumer.class));
        map.put(Float.TYPE, new LambdaSetterInfo(Float.TYPE, ObjFloatConsumer.class));
        map.put(Double.TYPE, new LambdaSetterInfo(Double.TYPE, ObjDoubleConsumer.class));
    }

    static class LambdaSetterInfo {
        final Class fieldClass;
        final MethodType invokedType;
        final MethodType methodType;
        final MethodType sameMethodMethod;

        LambdaSetterInfo(Class cls, Class cls2) {
            this.fieldClass = cls;
            this.sameMethodMethod = MethodType.methodType(Void.TYPE, Object.class, cls);
            this.methodType = MethodType.methodType((Class<?>) Void.TYPE, (Class<?>) cls);
            this.invokedType = MethodType.methodType(cls2);
        }
    }

    public <T> ObjectReader<T> createObjectReaderNoneDefaultConstructor(Constructor constructor, String... strArr) {
        Function<Map<Long, Object>, T> functionCreateFunction = createFunction(constructor, strArr);
        Class<T> declaringClass = constructor.getDeclaringClass();
        return createObjectReaderNoneDefaultConstructor(declaringClass, functionCreateFunction, createFieldReaders(JSONFactory.getDefaultObjectReaderProvider(), declaringClass, declaringClass, constructor, constructor.getParameters(), strArr));
    }

    public <T> ObjectReader<T> createObjectReaderNoneDefaultConstructor(Class cls, Function<Map<Long, Object>, T> function, FieldReader... fieldReaderArr) {
        return new ObjectReaderNoneDefaultConstructor(cls, null, null, 0L, function, null, null, fieldReaderArr, null, null, null);
    }

    public <T> ObjectReader<T> createObjectReaderFactoryMethod(Method method, String... strArr) {
        return new ObjectReaderNoneDefaultConstructor(null, null, null, 0L, createFactoryFunction(method, strArr), null, strArr, createFieldReaders(JSONFactory.getDefaultObjectReaderProvider(), null, null, method, method.getParameters(), strArr), null, null, null);
    }

    public FieldReader[] createFieldReaders(ObjectReaderProvider objectReaderProvider, Class cls, Type type, Executable executable, Parameter[] parameterArr, String... strArr) {
        String name;
        ObjectReaderProvider objectReaderProvider2;
        Class<?> cls2;
        int i;
        int i2;
        String[] strArr2;
        String str;
        Field declaredField;
        Class<?> declaringClass = executable != null ? executable.getDeclaringClass() : null;
        ArrayList arrayList = new ArrayList(parameterArr.length);
        int i3 = 0;
        while (i3 < parameterArr.length) {
            FieldInfo fieldInfo = new FieldInfo();
            Parameter parameter = parameterArr[i3];
            if (i3 < strArr.length) {
                name = strArr[i3];
            } else {
                name = parameter.getName();
            }
            String str2 = name;
            boolean z = executable instanceof Constructor;
            if (z) {
                objectReaderProvider2 = objectReaderProvider;
                cls2 = declaringClass;
                objectReaderProvider2.getFieldInfo(fieldInfo, cls2, (Constructor) executable, i3, parameter);
            } else {
                objectReaderProvider2 = objectReaderProvider;
                cls2 = declaringClass;
            }
            if (z && (declaredField = BeanUtils.getDeclaredField(cls2, str2)) != null) {
                objectReaderProvider2.getFieldInfo(fieldInfo, cls2, declaredField);
            }
            String str3 = (fieldInfo.fieldName == null || fieldInfo.fieldName.isEmpty()) ? str2 : fieldInfo.fieldName;
            if (str3 == null) {
                str3 = "arg" + i3;
            }
            String str4 = str3;
            if (str2 == null) {
                str2 = "arg" + i3;
            }
            String str5 = str2;
            ObjectReader initReader = getInitReader(objectReaderProvider2, parameter.getParameterizedType(), parameter.getType(), fieldInfo);
            Type parameterizedType = parameter.getParameterizedType();
            Type typeResolve = BeanUtils.resolve(type, cls, parameterizedType);
            Type type2 = typeResolve != null ? typeResolve : parameterizedType;
            declaringClass = cls2;
            ArrayList arrayList2 = arrayList;
            String str6 = str4;
            arrayList2.add(createFieldReaderParam(null, null, str4, i3, fieldInfo.features, fieldInfo.format, fieldInfo.locale, fieldInfo.defaultValue, type2, parameter.getType(), str5, declaringClass, parameter, null, initReader));
            if (fieldInfo.alternateNames != null) {
                String[] strArr3 = fieldInfo.alternateNames;
                int length = strArr3.length;
                int i4 = 0;
                while (i4 < length) {
                    int i5 = i4;
                    String str7 = strArr3[i5];
                    if (str6.equals(str7)) {
                        str = str6;
                        strArr2 = strArr3;
                        i = length;
                        i2 = i5;
                    } else {
                        i = length;
                        i2 = i5;
                        strArr2 = strArr3;
                        str = str6;
                        arrayList2.add(createFieldReaderParam(null, null, str7, i3, fieldInfo.features, fieldInfo.format, fieldInfo.locale, fieldInfo.defaultValue, type2, parameter.getType(), str5, declaringClass, parameter, null, initReader));
                    }
                    i4 = i2 + 1;
                    str6 = str;
                    strArr3 = strArr2;
                    length = i;
                }
            }
            i3++;
            arrayList = arrayList2;
        }
        return (FieldReader[]) arrayList.toArray(new FieldReader[0]);
    }

    public <T> Function<Map<Long, Object>, T> createFactoryFunction(Method method, String... strArr) {
        method.setAccessible(true);
        return new FactoryFunction(method, strArr);
    }

    public <T> Function<Map<Long, Object>, T> createFunction(Constructor constructor, String... strArr) {
        constructor.setAccessible(true);
        return new ConstructorFunction(null, constructor, null, null, null, strArr);
    }

    public <T> Function<Map<Long, Object>, T> createFunction(Constructor constructor, Constructor constructor2, String... strArr) {
        if (constructor2 == null) {
            constructor.setAccessible(true);
        } else {
            constructor2.setAccessible(true);
        }
        return new ConstructorFunction(null, constructor, null, null, constructor2, strArr);
    }

    public <T> ObjectReader<T> createObjectReader(Class<T> cls, FieldReader... fieldReaderArr) {
        return createObjectReader(cls, null, 0L, null, createSupplier(cls), null, fieldReaderArr);
    }

    public <T> ObjectReader<T> createObjectReader(Class<T> cls, Supplier<T> supplier, FieldReader... fieldReaderArr) {
        return createObjectReader(cls, null, 0L, null, supplier, null, fieldReaderArr);
    }

    public <T> ObjectReader<T> createObjectReaderSeeAlso(Class<T> cls, Class[] clsArr, FieldReader... fieldReaderArr) {
        return new ObjectReaderSeeAlso(cls, createSupplier(cls), "@type", clsArr, null, null, fieldReaderArr);
    }

    public <T> ObjectReader<T> createObjectReaderSeeAlso(Class<T> cls, String str, Class[] clsArr, String[] strArr, FieldReader... fieldReaderArr) {
        return new ObjectReaderSeeAlso(cls, createSupplier(cls), str, clsArr, strArr, null, fieldReaderArr);
    }

    public <T> ObjectReader<T> createObjectReaderSeeAlso(Class<T> cls, String str, Class[] clsArr, String[] strArr, Class cls2, FieldReader... fieldReaderArr) {
        return new ObjectReaderSeeAlso(cls, createSupplier(cls), str, clsArr, strArr, cls2, fieldReaderArr);
    }

    public <T> ObjectReader<T> createObjectReaderSeeAlso(Class<T> cls, Supplier<T> supplier, String str, Class[] clsArr, String[] strArr, FieldReader... fieldReaderArr) {
        return new ObjectReaderSeeAlso(cls, supplier, str, clsArr, strArr, null, fieldReaderArr);
    }

    protected <T> ObjectReader<T> createObjectReaderWithBuilder(final Class<T> cls, final Type type, final ObjectReaderProvider objectReaderProvider, final BeanInfo beanInfo) {
        Function functionCreateBuildFunction = beanInfo.buildMethod != null ? createBuildFunction(beanInfo.buildMethod) : null;
        final Class<T> cls2 = beanInfo.builder;
        String str = beanInfo.builderWithPrefix;
        if (str == null || str.isEmpty()) {
            str = "with";
        }
        final String str2 = str;
        final int length = str2.length();
        final LinkedHashMap linkedHashMap = new LinkedHashMap();
        final FieldInfo fieldInfo = new FieldInfo();
        BeanUtils.setters((Class) cls2, false, (Consumer<Method>) new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreator$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.m45x914526fa(fieldInfo, objectReaderProvider, cls, str2, cls2, beanInfo, length, linkedHashMap, type, (Method) obj);
            }
        });
        return createObjectReader(cls2, 0L, createSupplier(cls2), functionCreateBuildFunction, toFieldReaderArray(linkedHashMap));
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x006b, code lost:
    
        if ((r26.readerFeatures & com.alibaba.fastjson2.JSONReader.Feature.SupportSmartMatch.mask) != 0) goto L26;
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x009a  */
    /* JADX INFO: renamed from: lambda$createObjectReaderWithBuilder$0$com-alibaba-fastjson2-reader-ObjectReaderCreator, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    /* synthetic */ void m45x914526fa(com.alibaba.fastjson2.codec.FieldInfo r21, com.alibaba.fastjson2.reader.ObjectReaderProvider r22, java.lang.Class r23, java.lang.String r24, java.lang.Class r25, com.alibaba.fastjson2.codec.BeanInfo r26, int r27, java.util.Map r28, java.lang.reflect.Type r29, java.lang.reflect.Method r30) {
        /*
            Method dump skipped, instruction units count: 296
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreator.m45x914526fa(com.alibaba.fastjson2.codec.FieldInfo, com.alibaba.fastjson2.reader.ObjectReaderProvider, java.lang.Class, java.lang.String, java.lang.Class, com.alibaba.fastjson2.codec.BeanInfo, int, java.util.Map, java.lang.reflect.Type, java.lang.reflect.Method):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01cd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected <T> com.alibaba.fastjson2.reader.ObjectReader<T> createObjectReaderWithCreator(final java.lang.Class<T> r23, java.lang.reflect.Type r24, final com.alibaba.fastjson2.reader.ObjectReaderProvider r25, final com.alibaba.fastjson2.codec.BeanInfo r26) {
        /*
            Method dump skipped, instruction units count: 779
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreator.createObjectReaderWithCreator(java.lang.Class, java.lang.reflect.Type, com.alibaba.fastjson2.reader.ObjectReaderProvider, com.alibaba.fastjson2.codec.BeanInfo):com.alibaba.fastjson2.reader.ObjectReader");
    }

    static /* synthetic */ void lambda$createObjectReaderWithCreator$1(Class cls, ObjectReaderProvider objectReaderProvider, Class cls2, BeanInfo beanInfo, String str, FieldInfo fieldInfo, Method method) {
        if (method.getReturnType() != cls) {
            return;
        }
        FieldInfo fieldInfo2 = new FieldInfo();
        objectReaderProvider.getFieldInfo(fieldInfo2, cls2, method);
        String str2 = fieldInfo2.fieldName;
        if (str2 == null) {
            str2 = BeanUtils.getterName(method, beanInfo.f4kotlin, PropertyNamingStrategy.CamelCase.name());
        }
        if (fieldInfo2.readUsing == null || !str.equals(str2)) {
            return;
        }
        fieldInfo.readUsing = fieldInfo2.readUsing;
    }

    public <T> ObjectReader<T> createObjectReader(Class<T> cls, long j, Supplier<T> supplier, Function function, FieldReader... fieldReaderArr) {
        return createObjectReader(cls, null, j, null, supplier, function, fieldReaderArr);
    }

    public <T> ObjectReader<T> createObjectReader(Class<T> cls, String str, long j, JSONSchema jSONSchema, Supplier<T> supplier, Function function, FieldReader... fieldReaderArr) {
        return createObjectReader(cls, str, null, j, jSONSchema, supplier, function, fieldReaderArr);
    }

    public <T> ObjectReader<T> createObjectReader(Class<T> cls, String str, String str2, long j, JSONSchema jSONSchema, Supplier<T> supplier, Function function, FieldReader... fieldReaderArr) {
        if (cls != null) {
            int modifiers = cls.getModifiers();
            if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
                return new ObjectReaderAdapter(cls, str, null, j, jSONSchema, supplier, function, fieldReaderArr);
            }
        }
        if (str2 != null) {
            return new ObjectReaderRootName(cls, str, null, str2, j, jSONSchema, supplier, function, null, null, null, fieldReaderArr);
        }
        switch (fieldReaderArr.length) {
            case 1:
                return new ObjectReader1(cls, null, null, j, jSONSchema, supplier, function, fieldReaderArr[0]);
            case 2:
                return new ObjectReader2(cls, j, jSONSchema, supplier, function, fieldReaderArr[0], fieldReaderArr[1]);
            case 3:
                return new ObjectReader3(cls, supplier, j, jSONSchema, function, fieldReaderArr[0], fieldReaderArr[1], fieldReaderArr[2]);
            case 4:
                return new ObjectReader4(cls, j, jSONSchema, supplier, function, fieldReaderArr[0], fieldReaderArr[1], fieldReaderArr[2], fieldReaderArr[3]);
            case 5:
                return new ObjectReader5(cls, supplier, j, jSONSchema, function, fieldReaderArr[0], fieldReaderArr[1], fieldReaderArr[2], fieldReaderArr[3], fieldReaderArr[4]);
            case 6:
                return new ObjectReader6(cls, supplier, j, jSONSchema, function, fieldReaderArr[0], fieldReaderArr[1], fieldReaderArr[2], fieldReaderArr[3], fieldReaderArr[4], fieldReaderArr[5]);
            case 7:
                return new ObjectReader7(cls, null, null, j, jSONSchema, supplier, function, fieldReaderArr);
            case 8:
                return new ObjectReader8(cls, null, null, j, jSONSchema, supplier, function, fieldReaderArr);
            case 9:
                return new ObjectReader9(cls, null, null, j, jSONSchema, supplier, function, fieldReaderArr);
            case 10:
                return new ObjectReader10(cls, null, null, j, jSONSchema, supplier, function, fieldReaderArr);
            case 11:
                return new ObjectReader11(cls, null, null, j, jSONSchema, supplier, function, fieldReaderArr);
            case 12:
                return new ObjectReader12(cls, null, null, j, jSONSchema, supplier, function, fieldReaderArr);
            default:
                return new ObjectReaderAdapter(cls, str, null, j, jSONSchema, supplier, function, fieldReaderArr);
        }
    }

    public <T> ObjectReader<T> createObjectReader(Type type) {
        if (type instanceof Class) {
            return createObjectReader((Class) type);
        }
        Class<?> mapping = TypeUtils.getMapping(type);
        return createObjectReader(mapping, createSupplier(mapping), createFieldReaders(mapping, type));
    }

    public <T> ObjectReader<T> createObjectReader(Class<T> cls) {
        return createObjectReader(cls, cls, false, JSONFactory.getDefaultObjectReaderProvider());
    }

    public <T> ObjectReader<T> createObjectReader(Class<T> cls, boolean z) {
        return createObjectReader(cls, cls, z, JSONFactory.getDefaultObjectReaderProvider());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0245  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0398  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public <T> com.alibaba.fastjson2.reader.ObjectReader<T> createObjectReader(java.lang.Class<T> r22, java.lang.reflect.Type r23, boolean r24, com.alibaba.fastjson2.reader.ObjectReaderProvider r25) {
        /*
            Method dump skipped, instruction units count: 936
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreator.createObjectReader(java.lang.Class, java.lang.reflect.Type, boolean, com.alibaba.fastjson2.reader.ObjectReaderProvider):com.alibaba.fastjson2.reader.ObjectReader");
    }

    protected <T> ObjectReaderNoneDefaultConstructor createNoneDefaultConstructorObjectReader(Class cls, BeanInfo beanInfo, Function<Map<Long, Object>, T> function, List<Constructor> list, String[] strArr, FieldReader[] fieldReaderArr, FieldReader[] fieldReaderArr2) {
        return new ObjectReaderNoneDefaultConstructor(cls, beanInfo.typeKey, beanInfo.typeName, beanInfo.readerFeatures, function, list, strArr, fieldReaderArr, fieldReaderArr2, beanInfo.seeAlso, beanInfo.seeAlsoNames);
    }

    public <T> FieldReader[] createFieldReaders(Class<T> cls) {
        return createFieldReaders(cls, cls, null, false, JSONFactory.getDefaultObjectReaderProvider());
    }

    public <T> FieldReader[] createFieldReaders(Class<T> cls, Type type) {
        return createFieldReaders(cls, type, null, false, JSONFactory.getDefaultObjectReaderProvider());
    }

    protected void createFieldReader(Class cls, Type type, String str, String[] strArr, FieldInfo fieldInfo, Field field, Map<String, List<FieldReader>> map, ObjectReaderProvider objectReaderProvider) {
        String strFieldName;
        String[] strArr2;
        int i;
        int i2;
        String str2;
        Map<String, List<FieldReader>> map2;
        objectReaderProvider.getFieldInfo(fieldInfo, cls, field);
        if (!fieldInfo.ignore || ((fieldInfo.features & FieldInfo.UNWRAPPED_MASK) != 0 && Map.class.isAssignableFrom(field.getType()))) {
            if (fieldInfo.fieldName == null || fieldInfo.fieldName.isEmpty()) {
                String name = field.getName();
                strFieldName = str != null ? BeanUtils.fieldName(name, str) : name;
            } else {
                strFieldName = fieldInfo.fieldName;
            }
            if (strArr != null && strArr.length > 0) {
                int i3 = 0;
                while (true) {
                    if (i3 < strArr.length) {
                        if (strFieldName.equals(strArr[i3])) {
                            fieldInfo.ordinal = i3;
                            break;
                        }
                        i3++;
                    } else if (fieldInfo.ordinal == 0) {
                        fieldInfo.ordinal = strArr.length;
                    }
                }
            }
            Type genericType = field.getGenericType();
            Class<?> type2 = field.getType();
            ObjectReader initReader = getInitReader(objectReaderProvider, genericType, type2, fieldInfo);
            String str3 = fieldInfo.schema;
            if (fieldInfo.required && str3 == null) {
                str3 = "{\"required\":true}";
            }
            Class<?> cls2 = type2;
            Type type3 = genericType;
            String str4 = str3;
            String str5 = strFieldName;
            Map<String, List<FieldReader>> map3 = map;
            Class cls3 = cls;
            ObjectReaderCreator objectReaderCreator = this;
            FieldReader fieldReaderCreateFieldReader = objectReaderCreator.createFieldReader(cls3, type, str5, fieldInfo.ordinal, fieldInfo.features, fieldInfo.format, fieldInfo.locale, fieldInfo.defaultValue, str4, type3, cls2, field, initReader, fieldInfo.arrayToMapKey, fieldInfo.getInitArrayToMapDuplicateHandler());
            String str6 = str5;
            objectReaderCreator.putIfAbsent(map3, str6, fieldReaderCreateFieldReader, cls3);
            FieldInfo fieldInfo2 = fieldInfo;
            if (fieldInfo2.alternateNames != null) {
                String[] strArr3 = fieldInfo2.alternateNames;
                int length = strArr3.length;
                int i4 = 0;
                while (i4 < length) {
                    String str7 = strArr3[i4];
                    if (str6.equals(str7)) {
                        strArr2 = strArr3;
                        i2 = length;
                        i = i4;
                        str2 = str6;
                        map2 = map3;
                    } else {
                        strArr2 = strArr3;
                        String str8 = str4;
                        Type type4 = type3;
                        Class<?> cls4 = cls2;
                        i = i4;
                        i2 = length;
                        str2 = str6;
                        map2 = map3;
                        ObjectReaderCreator objectReaderCreator2 = objectReaderCreator;
                        Class cls5 = cls3;
                        FieldReader fieldReaderCreateFieldReader2 = objectReaderCreator2.createFieldReader(cls5, type, str7, fieldInfo2.ordinal, fieldInfo2.features, null, fieldInfo2.locale, fieldInfo2.defaultValue, str8, type4, cls4, field, null);
                        cls3 = cls5;
                        cls2 = cls4;
                        objectReaderCreator = objectReaderCreator2;
                        type3 = type4;
                        str4 = str8;
                        objectReaderCreator.putIfAbsent(map2, str7, fieldReaderCreateFieldReader2, cls3);
                    }
                    i4 = i + 1;
                    fieldInfo2 = fieldInfo;
                    map3 = map2;
                    str6 = str2;
                    strArr3 = strArr2;
                    length = i2;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01ff  */
    /* JADX WARN: Type update failed for variable: r20v0 'this'  ??, new type: com.alibaba.fastjson2.reader.ObjectReaderCreator
    jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 5901. Try increasing type updates limit count.
    	at jadx.core.dex.visitors.typeinference.TypeUpdateInfo.requestUpdate(TypeUpdateInfo.java:37)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:224)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyInvokeTypes(TypeUpdate.java:399)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.invokeListener(TypeUpdate.java:364)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:86)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:58)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void createFieldReader(java.lang.Class r21, java.lang.reflect.Type r22, java.lang.String r23, java.lang.String[] r24, com.alibaba.fastjson2.codec.BeanInfo r25, com.alibaba.fastjson2.codec.FieldInfo r26, java.lang.reflect.Method r27, java.util.Map<java.lang.String, java.util.List<com.alibaba.fastjson2.reader.FieldReader>> r28, com.alibaba.fastjson2.reader.ObjectReaderProvider r29) {
        /*
            Method dump skipped, instruction units count: 590
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreator.createFieldReader(java.lang.Class, java.lang.reflect.Type, java.lang.String, java.lang.String[], com.alibaba.fastjson2.codec.BeanInfo, com.alibaba.fastjson2.codec.FieldInfo, java.lang.reflect.Method, java.util.Map, com.alibaba.fastjson2.reader.ObjectReaderProvider):void");
    }

    protected <T> FieldReader[] createFieldReaders(final Class<T> cls, final Type type, BeanInfo beanInfo, boolean z, final ObjectReaderProvider objectReaderProvider) {
        final BeanInfo beanInfo2;
        LinkedHashMap linkedHashMap;
        final ObjectReaderProvider objectReaderProvider2;
        final LinkedHashMap linkedHashMap2;
        if (beanInfo == null) {
            BeanInfo beanInfo3 = new BeanInfo(objectReaderProvider);
            Iterator<ObjectReaderModule> it = objectReaderProvider.modules.iterator();
            while (it.hasNext()) {
                ObjectReaderAnnotationProcessor annotationProcessor = it.next().getAnnotationProcessor();
                if (annotationProcessor != null) {
                    annotationProcessor.getBeanInfo(beanInfo3, cls);
                }
            }
            beanInfo2 = beanInfo3;
        } else {
            beanInfo2 = beanInfo;
        }
        boolean zIsRecord = BeanUtils.isRecord(cls);
        final String str = beanInfo2.namingStrategy;
        final LinkedHashMap linkedHashMap3 = new LinkedHashMap();
        final long j = beanInfo2.readerFeatures;
        final String str2 = beanInfo2.format;
        final FieldInfo fieldInfo = new FieldInfo();
        final String[] strArr = beanInfo2.orders;
        if (z) {
            linkedHashMap2 = linkedHashMap3;
            BeanUtils.declaredFields(cls, new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreator$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m41x7ddc2143(fieldInfo, j, str2, cls, type, str, strArr, linkedHashMap3, objectReaderProvider, (Field) obj);
                }
            });
        } else {
            if (zIsRecord) {
                linkedHashMap = linkedHashMap3;
                objectReaderProvider2 = objectReaderProvider;
            } else {
                final BeanInfo beanInfo4 = beanInfo2;
                objectReaderProvider2 = objectReaderProvider;
                linkedHashMap = linkedHashMap3;
                beanInfo2 = beanInfo4;
                BeanUtils.declaredFields(cls, new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreator$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.m42x6f85c762(fieldInfo, j, str2, cls, type, str, strArr, linkedHashMap3, objectReaderProvider2, beanInfo4, (Field) obj);
                    }
                });
            }
            Class mixIn = objectReaderProvider2.getMixIn(cls);
            linkedHashMap2 = linkedHashMap;
            BeanUtils.setters(cls, beanInfo2, mixIn, new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreator$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m43x612f6d81(fieldInfo, j, str2, cls, type, str, strArr, beanInfo2, linkedHashMap2, objectReaderProvider, (Method) obj);
                }
            });
            if (cls.isInterface()) {
                final BeanInfo beanInfo5 = beanInfo2;
                Consumer consumer = new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreator$$ExternalSyntheticLambda5
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.m44x52d913a0(fieldInfo, j, cls, type, str, strArr, beanInfo5, linkedHashMap2, objectReaderProvider, (Method) obj);
                    }
                };
                linkedHashMap2 = linkedHashMap2;
                BeanUtils.getters(cls, consumer);
            }
        }
        Class<? super T> superclass = cls.getSuperclass();
        if (BeanUtils.isExtendedMap(cls)) {
            linkedHashMap2.put(BeanUtils.SUPER, listOf(ObjectReaders.fieldReader(BeanUtils.SUPER, cls.getGenericSuperclass(), superclass, new BiConsumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreator$$ExternalSyntheticLambda6
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ObjectReaderCreator.lambda$createFieldReaders$6(obj, obj2);
                }
            })));
        }
        return toFieldReaderArray(linkedHashMap2);
    }

    /* JADX INFO: renamed from: lambda$createFieldReaders$2$com-alibaba-fastjson2-reader-ObjectReaderCreator, reason: not valid java name */
    /* synthetic */ void m41x7ddc2143(FieldInfo fieldInfo, long j, String str, Class cls, Type type, String str2, String[] strArr, Map map, ObjectReaderProvider objectReaderProvider, Field field) {
        fieldInfo.init();
        fieldInfo.features |= JSONReader.Feature.FieldBased.mask;
        fieldInfo.features = j | fieldInfo.features;
        fieldInfo.format = str;
        createFieldReader(cls, type, str2, strArr, fieldInfo, field, (Map<String, List<FieldReader>>) map, objectReaderProvider);
    }

    /* JADX INFO: renamed from: lambda$createFieldReaders$3$com-alibaba-fastjson2-reader-ObjectReaderCreator, reason: not valid java name */
    /* synthetic */ void m42x6f85c762(FieldInfo fieldInfo, long j, String str, Class cls, Type type, String str2, String[] strArr, Map map, ObjectReaderProvider objectReaderProvider, BeanInfo beanInfo, Field field) {
        fieldInfo.init();
        fieldInfo.ignore = (field.getModifiers() & 1) == 0 && (JSONReader.Feature.FieldBased.mask & j) == 0;
        fieldInfo.features = j | fieldInfo.features;
        fieldInfo.format = str;
        createFieldReader(cls, type, str2, strArr, fieldInfo, field, (Map<String, List<FieldReader>>) map, objectReaderProvider);
        if (fieldInfo.required) {
            String name = fieldInfo.fieldName;
            if (name == null || name.isEmpty()) {
                name = field.getName();
            }
            beanInfo.required(name);
        }
    }

    /* JADX INFO: renamed from: lambda$createFieldReaders$4$com-alibaba-fastjson2-reader-ObjectReaderCreator, reason: not valid java name */
    /* synthetic */ void m43x612f6d81(FieldInfo fieldInfo, long j, String str, Class cls, Type type, String str2, String[] strArr, BeanInfo beanInfo, Map map, ObjectReaderProvider objectReaderProvider, Method method) {
        fieldInfo.init();
        fieldInfo.features = j | fieldInfo.features;
        fieldInfo.format = str;
        createFieldReader(cls, type, str2, strArr, beanInfo, fieldInfo, method, map, objectReaderProvider);
    }

    /* JADX INFO: renamed from: lambda$createFieldReaders$5$com-alibaba-fastjson2-reader-ObjectReaderCreator, reason: not valid java name */
    /* synthetic */ void m44x52d913a0(FieldInfo fieldInfo, long j, Class cls, Type type, String str, String[] strArr, BeanInfo beanInfo, Map map, ObjectReaderProvider objectReaderProvider, Method method) {
        fieldInfo.init();
        fieldInfo.features = j | fieldInfo.features;
        createFieldReader(cls, type, str, strArr, beanInfo, fieldInfo, method, map, objectReaderProvider);
    }

    static /* synthetic */ void lambda$createFieldReaders$6(Object obj, Object obj2) {
        Map map = (Map) obj;
        for (Map.Entry entry : ((Map) obj2).entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    public <T> Supplier<T> createSupplier(Class<T> cls) {
        if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
            return null;
        }
        try {
            Constructor<T> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            declaredConstructor.setAccessible(true);
            return createSupplier(declaredConstructor, true);
        } catch (NoSuchMethodException unused) {
            return null;
        } catch (Throwable th) {
            throw new JSONException("get constructor error, class " + cls.getName(), th);
        }
    }

    public <T> Supplier<T> createSupplier(Constructor constructor, boolean z) {
        if (z & JIT) {
            Class<T> declaringClass = constructor.getDeclaringClass();
            MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
            try {
                if (constructor.getParameterCount() == 0) {
                    return (Supplier) LambdaMetafactory.metafactory(lookupTrustedLookup, "get", TypeUtils.METHOD_TYPE_SUPPLIER, TypeUtils.METHOD_TYPE_OBJECT, lookupTrustedLookup.findConstructor(declaringClass, TypeUtils.METHOD_TYPE_VOID), TypeUtils.METHOD_TYPE_OBJECT).getTarget().invokeExact();
                }
            } catch (Throwable th) {
                this.jitErrorCount.incrementAndGet();
                this.jitErrorLast = th;
            }
        }
        return new ConstructorSupplier(constructor);
    }

    protected <T> IntFunction<T> createIntFunction(Constructor constructor) {
        Class<T> declaringClass = constructor.getDeclaringClass();
        MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
        try {
            return (IntFunction) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_INT_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_INT, lookupTrustedLookup.findConstructor(declaringClass, TypeUtils.METHOD_TYPE_VOID_INT), MethodType.methodType((Class<?>) declaringClass, (Class<?>) Integer.TYPE)).getTarget().invokeExact();
        } catch (Throwable th) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = th;
            return null;
        }
    }

    protected <T> IntFunction<T> createIntFunction(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
        try {
            MethodType methodType = MethodType.methodType(method.getReturnType(), (Class<?>) Integer.TYPE);
            return (IntFunction) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_INT_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_INT, lookupTrustedLookup.findStatic(declaringClass, method.getName(), methodType), methodType).getTarget().invokeExact();
        } catch (Throwable th) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = th;
            return null;
        }
    }

    protected <T> Function<String, T> createStringFunction(Constructor constructor) {
        Class<T> declaringClass = constructor.getDeclaringClass();
        MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
        try {
            return (Function) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, lookupTrustedLookup.findConstructor(declaringClass, TypeUtils.METHOD_TYPE_VOID_STRING), MethodType.methodType((Class<?>) declaringClass, (Class<?>) String.class)).getTarget().invokeExact();
        } catch (Throwable th) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = th;
            return null;
        }
    }

    protected <T> Function<String, T> createStringFunction(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
        try {
            MethodType methodType = MethodType.methodType(method.getReturnType(), (Class<?>) String.class);
            return (Function) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, lookupTrustedLookup.findStatic(declaringClass, method.getName(), methodType), methodType).getTarget().invokeExact();
        } catch (Throwable th) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = th;
            return null;
        }
    }

    protected <I, T> Function<I, T> createValueFunction(Constructor<T> constructor, Class<I> cls) {
        Class<T> declaringClass = constructor.getDeclaringClass();
        MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
        try {
            return (Function) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, lookupTrustedLookup.findConstructor(declaringClass, MethodType.methodType((Class<?>) Void.TYPE, (Class<?>) cls)), MethodType.methodType((Class<?>) declaringClass, (Class<?>) cls)).getTarget().invokeExact();
        } catch (Throwable th) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = th;
            return null;
        }
    }

    protected <I, T> Function<I, T> createValueFunction(Method method, Class cls) {
        Class<?> declaringClass = method.getDeclaringClass();
        MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(declaringClass);
        try {
            MethodType methodType = MethodType.methodType(method.getReturnType(), (Class<?>) cls);
            return (Function) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, lookupTrustedLookup.findStatic(declaringClass, method.getName(), methodType), methodType).getTarget().invokeExact();
        } catch (Throwable th) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = th;
            return null;
        }
    }

    public <T, R> Function<T, R> createBuildFunction(final Method method) {
        try {
            return createBuildFunctionLambda(method);
        } catch (Throwable th) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = th;
            method.setAccessible(true);
            return new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreator$$ExternalSyntheticLambda10
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ObjectReaderCreator.lambda$createBuildFunction$7(method, obj);
                }
            };
        }
    }

    static /* synthetic */ Object lambda$createBuildFunction$7(Method method, Object obj) {
        try {
            return method.invoke(obj, new Object[0]);
        } catch (Throwable th) {
            throw new JSONException("create instance error", th);
        }
    }

    <T, R> Function<T, R> createBuildFunctionLambda(Method method) {
        MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(method.getDeclaringClass());
        try {
            MethodHandle methodHandleFindVirtual = lookupTrustedLookup.findVirtual(method.getDeclaringClass(), method.getName(), MethodType.methodType(method.getReturnType()));
            MethodType methodTypeType = methodHandleFindVirtual.type();
            return (Function) (Object) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, methodTypeType.erase(), methodHandleFindVirtual, methodTypeType).getTarget().invoke();
        } catch (Throwable th) {
            throw new JSONException("create fieldReader error", th);
        }
    }

    public <T> FieldReader createFieldReader(Class<T> cls, String str, Type type, Class cls2, Method method) {
        return createFieldReaderMethod(cls, cls, str, 0, 0L, null, null, null, null, type, cls2, method, null);
    }

    public <T> FieldReader createFieldReader(Class<T> cls, String str, String str2, Type type, Class cls2, Method method) {
        return createFieldReaderMethod(cls, str, str2, type, cls2, method);
    }

    public <T> FieldReader createFieldReaderMethod(Class<T> cls, String str, String str2, Type type, Class cls2, Method method) {
        return createFieldReaderMethod(cls, cls, str, 0, 0L, str2, null, null, null, type, cls2, method, null);
    }

    public <T> FieldReader createFieldReaderParam(Class<T> cls, Type type, String str, int i, long j, String str2, Type type2, Class cls2, String str3, Class cls3, Parameter parameter, JSONSchema jSONSchema) {
        return createFieldReaderParam(cls, type, str, i, j, str2, type2, cls2, str3, cls3, parameter, jSONSchema, null);
    }

    public <T> FieldReader createFieldReaderParam(Class<T> cls, Type type, String str, int i, long j, String str2, Type type2, Class cls2, String str3, Class cls3, Parameter parameter, JSONSchema jSONSchema, ObjectReader objectReader) {
        return createFieldReaderParam(cls, type, str, i, j, str2, null, null, type2, cls2, str3, cls3, parameter, jSONSchema, objectReader);
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public <T> com.alibaba.fastjson2.reader.FieldReader createFieldReaderParam(java.lang.Class<T> r17, java.lang.reflect.Type r18, java.lang.String r19, int r20, long r21, java.lang.String r23, java.util.Locale r24, java.lang.Object r25, java.lang.reflect.Type r26, java.lang.Class r27, java.lang.String r28, java.lang.Class r29, java.lang.reflect.Parameter r30, com.alibaba.fastjson2.schema.JSONSchema r31, com.alibaba.fastjson2.reader.ObjectReader r32) {
        /*
            Method dump skipped, instruction units count: 321
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreator.createFieldReaderParam(java.lang.Class, java.lang.reflect.Type, java.lang.String, int, long, java.lang.String, java.util.Locale, java.lang.Object, java.lang.reflect.Type, java.lang.Class, java.lang.String, java.lang.Class, java.lang.reflect.Parameter, com.alibaba.fastjson2.schema.JSONSchema, com.alibaba.fastjson2.reader.ObjectReader):com.alibaba.fastjson2.reader.FieldReader");
    }

    public <T> FieldReader createFieldReaderMethod(Class<T> cls, Type type, String str, int i, long j, String str2, Locale locale, Object obj, String str3, Type type2, Class cls2, Method method, ObjectReader objectReader) {
        return createFieldReaderMethod(cls, type, str, i, j, str2, locale, obj, str3, type2, cls2, method, objectReader, null, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:130:0x02ee  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x04c3  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x008b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public <T> com.alibaba.fastjson2.reader.FieldReader createFieldReaderMethod(java.lang.Class<T> r17, java.lang.reflect.Type r18, java.lang.String r19, int r20, long r21, java.lang.String r23, java.util.Locale r24, java.lang.Object r25, java.lang.String r26, java.lang.reflect.Type r27, java.lang.Class r28, java.lang.reflect.Method r29, com.alibaba.fastjson2.reader.ObjectReader r30, java.lang.String r31, java.util.function.BiConsumer r32) {
        /*
            Method dump skipped, instruction units count: 1324
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreator.createFieldReaderMethod(java.lang.Class, java.lang.reflect.Type, java.lang.String, int, long, java.lang.String, java.util.Locale, java.lang.Object, java.lang.String, java.lang.reflect.Type, java.lang.Class, java.lang.reflect.Method, com.alibaba.fastjson2.reader.ObjectReader, java.lang.String, java.util.function.BiConsumer):com.alibaba.fastjson2.reader.FieldReader");
    }

    public <T> FieldReader<T> createFieldReader(String str, Type type, Field field) {
        return createFieldReader(str, null, type, field);
    }

    public <T> FieldReader<T> createFieldReader(String str, Field field) {
        return createFieldReader(str, null, field.getGenericType(), field);
    }

    public <T> FieldReader createFieldReader(String str, Method method) {
        Class<?> returnType;
        Type genericReturnType;
        Class<?> declaringClass = method.getDeclaringClass();
        int parameterCount = method.getParameterCount();
        if (parameterCount == 0) {
            returnType = method.getReturnType();
            genericReturnType = method.getGenericReturnType();
        } else if (parameterCount == 1) {
            returnType = method.getParameterTypes()[0];
            genericReturnType = method.getGenericParameterTypes()[0];
        } else {
            throw new JSONException("illegal setter method " + method);
        }
        return createFieldReaderMethod(declaringClass, declaringClass, str, 0, 0L, null, null, null, null, genericReturnType, returnType, method, null);
    }

    public <T> FieldReader<T> createFieldReader(String str, String str2, Type type, Field field) {
        Class<?> declaringClass = field.getDeclaringClass();
        return createFieldReader(declaringClass, declaringClass, str, 0L, str2, type, field.getType(), field);
    }

    public <T> FieldReader<T> createFieldReader(Class cls, Type type, String str, long j, String str2, Type type2, Class cls2, Field field) {
        return createFieldReader(cls, type, str, 0, j, str2, null, null, null, type2, field.getType(), field, null, null, null);
    }

    public <T> FieldReader<T> createFieldReader(Class cls, Type type, String str, int i, long j, String str2, Locale locale, Object obj, String str3, Type type2, Class cls2, Field field, ObjectReader objectReader) {
        return createFieldReader(cls, type, str, 0, j, str2, locale, obj, str3, type2, field.getType(), field, objectReader, null, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0071  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public <T> com.alibaba.fastjson2.reader.FieldReader<T> createFieldReader(java.lang.Class r16, java.lang.reflect.Type r17, java.lang.String r18, int r19, long r20, java.lang.String r22, java.util.Locale r23, java.lang.Object r24, java.lang.String r25, java.lang.reflect.Type r26, java.lang.Class r27, java.lang.reflect.Field r28, com.alibaba.fastjson2.reader.ObjectReader r29, java.lang.String r30, java.util.function.BiConsumer r31) {
        /*
            Method dump skipped, instruction units count: 1276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreator.createFieldReader(java.lang.Class, java.lang.reflect.Type, java.lang.String, int, long, java.lang.String, java.util.Locale, java.lang.Object, java.lang.String, java.lang.reflect.Type, java.lang.Class, java.lang.reflect.Field, com.alibaba.fastjson2.reader.ObjectReader, java.lang.String, java.util.function.BiConsumer):com.alibaba.fastjson2.reader.FieldReader");
    }

    public <T, V> FieldReader createFieldReader(String str, Type type, Class<V> cls, long j, BiConsumer<T, V> biConsumer) {
        return createFieldReader(null, null, str, type, cls, 0, j, null, null, null, null, null, biConsumer, null);
    }

    public <T, V> FieldReader createFieldReader(String str, Type type, Class<V> cls, Method method, BiConsumer<T, V> biConsumer) {
        return createFieldReader(null, null, str, type, cls, 0, 0L, null, null, null, null, method, biConsumer, null);
    }

    public <T, V> FieldReader createFieldReader(Class cls, Type type, String str, Type type2, Class<V> cls2, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Method method, BiConsumer<T, V> biConsumer, ObjectReader objectReader) {
        Method method2;
        Class<?> mapping;
        Class<?> cls3;
        Type type3;
        if (objectReader != null) {
            FieldReaderObject fieldReaderObject = new FieldReaderObject(str, type2, cls2, i, j | FieldInfo.READ_USING_MASK, str2, locale, obj, jSONSchema, method, null, biConsumer);
            fieldReaderObject.initReader = objectReader;
            return fieldReaderObject;
        }
        if (cls2 == Integer.class) {
            return new FieldReaderInt32Func(str, cls2, i, j, str2, locale, obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == Long.class) {
            return new FieldReaderInt64Func(str, cls2, i, j, str2, locale, obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == String.class) {
            return new FieldReaderStringFunc(str, cls2, i, j, str2, locale, obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == Boolean.class) {
            return new FieldReaderBoolFunc(str, cls2, i, j, str2, locale, obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == Short.class) {
            return new FieldReaderInt16Func(str, cls2, i, j, str2, locale, obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == Byte.class) {
            return new FieldReaderInt8Func(str, cls2, i, j, str2, locale, obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == BigDecimal.class) {
            return new FieldReaderBigDecimalFunc(str, cls2, i, j, str2, locale, obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == BigInteger.class) {
            return new FieldReaderBigIntegerFunc(str, cls2, i, j, str2, locale, obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == Float.class) {
            return new FieldReaderFloatFunc(str, cls2, i, j, str2, locale, (Float) obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == Double.class) {
            return new FieldReaderDoubleFunc(str, cls2, i, j, str2, locale, (Double) obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == Number.class) {
            return new FieldReaderNumberFunc(str, cls2, i, j, str2, locale, (Number) obj, jSONSchema, method, biConsumer);
        }
        if (cls2 == Date.class) {
            return new FieldReaderDate(str, type2, cls2, i, j, str2, locale, obj, jSONSchema, null, method, biConsumer);
        }
        Type type4 = type2;
        Type fieldType = null;
        if (type4 instanceof Class) {
            method2 = method;
            mapping = null;
        } else {
            method2 = method;
            fieldType = BeanUtils.getFieldType(type != null ? TypeReference.get(type) : null, cls, method2, type4);
            mapping = TypeUtils.getMapping(fieldType);
        }
        if (cls2 == List.class || cls2 == ArrayList.class) {
            Type type5 = Object.class;
            Class<?> mapping2 = Object.class;
            if (fieldType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) fieldType).getActualTypeArguments();
                if (actualTypeArguments.length == 1 && (mapping2 = TypeUtils.getMapping((type5 = actualTypeArguments[0]))) == String.class) {
                    return new FieldReaderList(str, fieldType, mapping, String.class, String.class, i, j, str2, locale, obj, jSONSchema, method, null, biConsumer);
                }
                cls3 = mapping;
                type3 = fieldType;
            } else {
                cls3 = mapping;
                type3 = fieldType;
            }
            boolean z = type3 == null;
            if (!z) {
                type4 = type3;
            }
            return new FieldReaderList(str, type4, z ? cls2 : cls3, type5, mapping2, i, j, str2, locale, obj, jSONSchema, method, null, biConsumer);
        }
        if (fieldType != null) {
            type4 = fieldType;
        }
        return new FieldReaderObjectFunc(str, type4, cls2, i, j, str2, locale, obj, jSONSchema, method2, biConsumer, null);
    }

    protected ObjectReader createEnumReader(Class cls, Method method, ObjectReaderProvider objectReaderProvider) throws NoSuchMethodException {
        Class mixIn;
        FieldInfo fieldInfo = new FieldInfo();
        Enum[] enumArr = (Enum[]) cls.getEnumConstants();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (int i = 0; enumArr != null && i < enumArr.length; i++) {
            Enum r4 = enumArr[i];
            String strName = r4.name();
            linkedHashMap.put(Long.valueOf(Fnv.hashCode64(strName)), r4);
            try {
                fieldInfo.init();
                objectReaderProvider.getFieldInfo(fieldInfo, cls, cls.getField(strName));
                String str = fieldInfo.fieldName;
                if (str != null && !str.isEmpty() && !str.equals(strName)) {
                    linkedHashMap.putIfAbsent(Long.valueOf(Fnv.hashCode64(str)), r4);
                }
                if (fieldInfo.alternateNames != null) {
                    for (String str2 : fieldInfo.alternateNames) {
                        if (str2 != null && !str2.isEmpty()) {
                            linkedHashMap.putIfAbsent(Long.valueOf(Fnv.hashCode64(str2)), r4);
                        }
                    }
                }
            } catch (Exception unused) {
            }
        }
        for (int i2 = 0; enumArr != null && i2 < enumArr.length; i2++) {
            Enum r3 = enumArr[i2];
            String strName2 = r3.name();
            linkedHashMap.putIfAbsent(Long.valueOf(Fnv.hashCode64LCase(strName2)), r3);
            String string = r3.toString();
            if (!strName2.equals(string)) {
                linkedHashMap.putIfAbsent(Long.valueOf(Fnv.hashCode64LCase(string)), r3);
            }
        }
        int size = linkedHashMap.size();
        long[] jArr = new long[size];
        Iterator it = linkedHashMap.keySet().iterator();
        int i3 = 0;
        while (it.hasNext()) {
            jArr[i3] = ((Long) it.next()).longValue();
            i3++;
        }
        Arrays.sort(jArr);
        Member enumValueField = BeanUtils.getEnumValueField(cls, objectReaderProvider);
        if (enumValueField == null && objectReaderProvider.modules.size() > 0 && (mixIn = objectReaderProvider.getMixIn(cls)) != null) {
            Member enumValueField2 = BeanUtils.getEnumValueField(mixIn, objectReaderProvider);
            if (enumValueField2 instanceof Field) {
                try {
                    enumValueField = cls.getField(enumValueField2.getName());
                } catch (NoSuchFieldException | NoSuchMethodException unused2) {
                }
            } else if (enumValueField2 instanceof Method) {
                enumValueField = cls.getMethod(enumValueField2.getName(), new Class[0]);
            }
        }
        Member member = enumValueField;
        Enum[] enumArr2 = new Enum[size];
        for (int i4 = 0; i4 < size; i4++) {
            enumArr2[i4] = (Enum) linkedHashMap.get(Long.valueOf(jArr[i4]));
        }
        return new ObjectReaderImplEnum(cls, method, member, enumArr2, enumArr, jArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0058  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0036 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static com.alibaba.fastjson2.reader.ObjectReader getInitReader(com.alibaba.fastjson2.reader.ObjectReaderProvider r7, java.lang.reflect.Type r8, java.lang.Class r9, com.alibaba.fastjson2.codec.FieldInfo r10) {
        /*
            com.alibaba.fastjson2.reader.ObjectReader r0 = r10.getInitReader()
            if (r0 != 0) goto L63
            java.lang.Class<?> r1 = r10.keyUsing
            if (r1 != 0) goto Le
            java.lang.Class<?> r1 = r10.valueUsing
            if (r1 == 0) goto L63
        Le:
            java.lang.Class<java.util.Map> r1 = java.util.Map.class
            boolean r1 = r1.isAssignableFrom(r9)
            if (r1 == 0) goto L63
            java.lang.Class<?> r1 = r10.keyUsing
            r2 = 1
            r3 = 0
            r4 = 0
            if (r1 == 0) goto L31
            java.lang.Class<?> r1 = r10.keyUsing     // Catch: java.lang.Exception -> L31
            java.lang.Class[] r5 = new java.lang.Class[r4]     // Catch: java.lang.Exception -> L31
            java.lang.reflect.Constructor r1 = r1.getDeclaredConstructor(r5)     // Catch: java.lang.Exception -> L31
            r1.setAccessible(r2)     // Catch: java.lang.Exception -> L31
            java.lang.Object[] r5 = new java.lang.Object[r4]     // Catch: java.lang.Exception -> L31
            java.lang.Object r1 = r1.newInstance(r5)     // Catch: java.lang.Exception -> L31
            com.alibaba.fastjson2.reader.ObjectReader r1 = (com.alibaba.fastjson2.reader.ObjectReader) r1     // Catch: java.lang.Exception -> L31
            goto L32
        L31:
            r1 = r3
        L32:
            java.lang.Class<?> r5 = r10.valueUsing
            if (r5 == 0) goto L4a
            java.lang.Class<?> r5 = r10.valueUsing     // Catch: java.lang.Exception -> L4a
            java.lang.Class[] r6 = new java.lang.Class[r4]     // Catch: java.lang.Exception -> L4a
            java.lang.reflect.Constructor r5 = r5.getDeclaredConstructor(r6)     // Catch: java.lang.Exception -> L4a
            r5.setAccessible(r2)     // Catch: java.lang.Exception -> L4a
            java.lang.Object[] r2 = new java.lang.Object[r4]     // Catch: java.lang.Exception -> L4a
            java.lang.Object r2 = r5.newInstance(r2)     // Catch: java.lang.Exception -> L4a
            com.alibaba.fastjson2.reader.ObjectReader r2 = (com.alibaba.fastjson2.reader.ObjectReader) r2     // Catch: java.lang.Exception -> L4a
            r3 = r2
        L4a:
            if (r1 != 0) goto L4e
            if (r3 == 0) goto L63
        L4e:
            long r4 = r10.features
            com.alibaba.fastjson2.reader.ObjectReader r8 = com.alibaba.fastjson2.reader.ObjectReaderImplMap.of(r8, r9, r4)
            boolean r10 = r8 instanceof com.alibaba.fastjson2.reader.ObjectReaderImplMapTyped
            if (r10 == 0) goto L63
            com.alibaba.fastjson2.reader.ObjectReaderImplMapTyped r8 = (com.alibaba.fastjson2.reader.ObjectReaderImplMapTyped) r8
            if (r1 == 0) goto L5e
            r8.keyObjectReader = r1
        L5e:
            if (r3 == 0) goto L62
            r8.valueObjectReader = r3
        L62:
            return r8
        L63:
            if (r0 != 0) goto La7
            java.lang.Class r8 = java.lang.Long.TYPE
            if (r9 == r8) goto L9c
            java.lang.Class<java.lang.Long> r8 = java.lang.Long.class
            if (r9 != r8) goto L6e
            goto L9c
        L6e:
            java.lang.Class<java.math.BigDecimal> r8 = java.math.BigDecimal.class
            if (r9 != r8) goto L7d
            java.lang.Class<java.math.BigDecimal> r8 = java.math.BigDecimal.class
            com.alibaba.fastjson2.reader.ObjectReader r7 = r7.getObjectReader(r8)
            com.alibaba.fastjson2.reader.ObjectReaderImplBigDecimal r8 = com.alibaba.fastjson2.reader.ObjectReaderImplBigDecimal.INSTANCE
            if (r7 == r8) goto La7
            goto L9a
        L7d:
            java.lang.Class<java.math.BigInteger> r8 = java.math.BigInteger.class
            if (r9 != r8) goto L8c
            java.lang.Class<java.math.BigInteger> r8 = java.math.BigInteger.class
            com.alibaba.fastjson2.reader.ObjectReader r7 = r7.getObjectReader(r8)
            com.alibaba.fastjson2.reader.ObjectReaderImplBigInteger r8 = com.alibaba.fastjson2.reader.ObjectReaderImplBigInteger.INSTANCE
            if (r7 == r8) goto La7
            goto L9a
        L8c:
            java.lang.Class<java.util.Date> r8 = java.util.Date.class
            if (r9 != r8) goto La7
            java.lang.Class<java.util.Date> r8 = java.util.Date.class
            com.alibaba.fastjson2.reader.ObjectReader r7 = r7.getObjectReader(r8)
            com.alibaba.fastjson2.reader.ObjectReaderImplDate r8 = com.alibaba.fastjson2.reader.ObjectReaderImplDate.INSTANCE
            if (r7 == r8) goto La7
        L9a:
            r0 = r7
            goto La7
        L9c:
            java.lang.Class<java.lang.Long> r8 = java.lang.Long.class
            com.alibaba.fastjson2.reader.ObjectReader r7 = r7.getObjectReader(r8)
            com.alibaba.fastjson2.reader.ObjectReaderImplInt64 r8 = com.alibaba.fastjson2.reader.ObjectReaderImplInt64.INSTANCE
            if (r7 == r8) goto La7
            goto L9a
        La7:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreator.getInitReader(com.alibaba.fastjson2.reader.ObjectReaderProvider, java.lang.reflect.Type, java.lang.Class, com.alibaba.fastjson2.codec.FieldInfo):com.alibaba.fastjson2.reader.ObjectReader");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected <T> com.alibaba.fastjson2.reader.FieldReader createFieldReaderLambda(java.lang.Class<T> r18, java.lang.reflect.Type r19, java.lang.String r20, int r21, long r22, java.lang.String r24, java.util.Locale r25, java.lang.Object r26, java.lang.String r27, java.lang.reflect.Type r28, java.lang.Class r29, java.lang.reflect.Method r30, com.alibaba.fastjson2.reader.ObjectReader r31) {
        /*
            Method dump skipped, instruction units count: 413
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreator.createFieldReaderLambda(java.lang.Class, java.lang.reflect.Type, java.lang.String, int, long, java.lang.String, java.util.Locale, java.lang.Object, java.lang.String, java.lang.reflect.Type, java.lang.Class, java.lang.reflect.Method, com.alibaba.fastjson2.reader.ObjectReader):com.alibaba.fastjson2.reader.FieldReader");
    }

    protected Object lambdaSetter(Class cls, Class cls2, Method method) {
        MethodType methodType;
        MethodType methodType2;
        MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(cls);
        Class<?> returnType = method.getReturnType();
        LambdaSetterInfo lambdaSetterInfo = methodTypeMapping.get(cls2);
        MethodType methodType3 = null;
        if (lambdaSetterInfo != null) {
            methodType = lambdaSetterInfo.sameMethodMethod;
            methodType2 = lambdaSetterInfo.invokedType;
            if (returnType == Void.TYPE) {
                methodType3 = lambdaSetterInfo.methodType;
            }
        } else {
            methodType = TypeUtils.METHOD_TYPE_VOO;
            methodType2 = TypeUtils.METHOD_TYPE_BI_CONSUMER;
        }
        MethodType methodType4 = methodType2;
        if (methodType3 == null) {
            methodType3 = MethodType.methodType(returnType, (Class<?>) cls2);
        }
        try {
            return (Object) LambdaMetafactory.metafactory(lookupTrustedLookup, "accept", methodType4, methodType, lookupTrustedLookup.findVirtual(cls, method.getName(), methodType3), MethodType.methodType(Void.TYPE, cls, cls2)).getTarget().invoke();
        } catch (Throwable th) {
            throw new JSONException("create fieldReader error", th);
        }
    }

    private List<FieldReader> listOf(FieldReader fieldReader) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(fieldReader);
        return arrayList;
    }

    private void putIfAbsent(Map<String, List<FieldReader>> map, String str, FieldReader fieldReader, Class cls) {
        FieldReader fieldReader2;
        List<FieldReader> list = map.get(str);
        if (list == null) {
            map.put(str, listOf(fieldReader));
            return;
        }
        if (fieldReader.isReadOnly()) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= list.size()) {
                fieldReader2 = null;
                break;
            }
            fieldReader2 = list.get(i);
            if (fieldReader2.sameTo(fieldReader)) {
                break;
            } else {
                i++;
            }
        }
        if (fieldReader2 != null) {
            if (fieldReader2.compareTo(fieldReader) > 0 || !fieldReader2.belongTo(cls)) {
                list.set(list.indexOf(fieldReader2), fieldReader);
                return;
            }
            return;
        }
        list.add(fieldReader);
    }

    private FieldReader[] toFieldReaderArray(Map<String, List<FieldReader>> map) {
        FieldReader[] fieldReaderArr = new FieldReader[map.values().stream().mapToInt(new ToIntFunction() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreator$$ExternalSyntheticLambda11
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((List) obj).size();
            }
        }).sum()];
        ((List) map.values().stream().flatMap(new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreator$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((List) obj).stream();
            }
        }).collect(Collectors.toList())).toArray(fieldReaderArr);
        Arrays.sort(fieldReaderArr);
        return fieldReaderArr;
    }
}
