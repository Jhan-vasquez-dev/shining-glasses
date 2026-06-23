package com.alibaba.fastjson2.reader;

import androidx.exifinterface.media.ExifInterface;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.annotation.JSONBuilder;
import com.alibaba.fastjson2.annotation.JSONCreator;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.annotation.JSONType;
import com.alibaba.fastjson2.codec.BeanInfo;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.function.impl.StringToAny;
import com.alibaba.fastjson2.function.impl.ToBigDecimal;
import com.alibaba.fastjson2.function.impl.ToBigInteger;
import com.alibaba.fastjson2.function.impl.ToBoolean;
import com.alibaba.fastjson2.function.impl.ToByte;
import com.alibaba.fastjson2.function.impl.ToDouble;
import com.alibaba.fastjson2.function.impl.ToFloat;
import com.alibaba.fastjson2.function.impl.ToInteger;
import com.alibaba.fastjson2.function.impl.ToLong;
import com.alibaba.fastjson2.function.impl.ToNumber;
import com.alibaba.fastjson2.function.impl.ToShort;
import com.alibaba.fastjson2.function.impl.ToString;
import com.alibaba.fastjson2.modules.ObjectReaderAnnotationProcessor;
import com.alibaba.fastjson2.modules.ObjectReaderModule;
import com.alibaba.fastjson2.reader.ObjectReaderBaseModule;
import com.alibaba.fastjson2.util.ApacheLang3Support;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.KotlinUtils;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class ObjectReaderBaseModule implements ObjectReaderModule {
    static Method METHOD_getPermittedSubclasses;
    final ReaderAnnotationProcessor annotationProcessor = new ReaderAnnotationProcessor();
    final ObjectReaderProvider provider;

    public static /* synthetic */ File $r8$lambda$3UDCRuryPMpPdeZ_mA8WA5301tk(String str) {
        return new File(str);
    }

    public static /* synthetic */ AtomicIntegerArray $r8$lambda$3qZHSL4C1HvTalVoO96JJ_FVv6I(int[] iArr) {
        return new AtomicIntegerArray(iArr);
    }

    public static /* synthetic */ AtomicLong $r8$lambda$8ZqB1iVOXrPrsVyf72if6iEU5hw(long j) {
        return new AtomicLong(j);
    }

    public static /* synthetic */ AtomicLongArray $r8$lambda$U0lGPadskxz3pdM5U9NMWRlHrRE(long[] jArr) {
        return new AtomicLongArray(jArr);
    }

    public static /* synthetic */ SimpleDateFormat $r8$lambda$b3Lmh_nz6Ow94jVYodnZkoZlIXY(String str) {
        return new SimpleDateFormat(str);
    }

    public static /* synthetic */ AtomicInteger $r8$lambda$fgjolKahIJ8DIhpC69R6doXRWuY(int i) {
        return new AtomicInteger(i);
    }

    /* JADX INFO: renamed from: $r8$lambda$p90uMnuBXujNSd9h8icGqEE8-mU, reason: not valid java name */
    public static /* synthetic */ AtomicBoolean m30$r8$lambda$p90uMnuBXujNSd9h8icGqEE8mU(boolean z) {
        return new AtomicBoolean(z);
    }

    static /* synthetic */ Object lambda$init$0(Object obj) {
        return obj;
    }

    static /* synthetic */ Object lambda$init$1(Object obj) {
        return obj;
    }

    public ObjectReaderBaseModule(ObjectReaderProvider objectReaderProvider) {
        this.provider = objectReaderProvider;
    }

    @Override // com.alibaba.fastjson2.modules.ObjectReaderModule
    public ObjectReaderProvider getProvider() {
        return this.provider;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v30 */
    /* JADX WARN: Type inference failed for: r4v4, types: [int] */
    @Override // com.alibaba.fastjson2.modules.ObjectReaderModule
    public void init(ObjectReaderProvider objectReaderProvider) {
        objectReaderProvider.registerTypeConvert(Character.class, Character.TYPE, new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$$ExternalSyntheticLambda19
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ObjectReaderBaseModule.lambda$init$0(obj);
            }
        });
        boolean z = false;
        Class[] clsArr = {Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Number.class, Float.class, Double.class, BigInteger.class, BigDecimal.class, AtomicInteger.class, AtomicLong.class};
        ToBoolean toBoolean = new ToBoolean(null);
        for (int i = 0; i < 12; i++) {
            objectReaderProvider.registerTypeConvert(clsArr[i], Boolean.class, toBoolean);
        }
        ToBoolean toBoolean2 = new ToBoolean(Boolean.FALSE);
        for (int i2 = 0; i2 < 12; i2++) {
            objectReaderProvider.registerTypeConvert(clsArr[i2], Boolean.TYPE, toBoolean2);
        }
        ToString toString = new ToString();
        for (int i3 = 0; i3 < 12; i3++) {
            objectReaderProvider.registerTypeConvert(clsArr[i3], String.class, toString);
        }
        ToBigDecimal toBigDecimal = new ToBigDecimal();
        for (int i4 = 0; i4 < 12; i4++) {
            objectReaderProvider.registerTypeConvert(clsArr[i4], BigDecimal.class, toBigDecimal);
        }
        ToBigInteger toBigInteger = new ToBigInteger();
        for (int i5 = 0; i5 < 12; i5++) {
            objectReaderProvider.registerTypeConvert(clsArr[i5], BigInteger.class, toBigInteger);
        }
        ToByte toByte = new ToByte(null);
        for (int i6 = 0; i6 < 12; i6++) {
            objectReaderProvider.registerTypeConvert(clsArr[i6], Byte.class, toByte);
        }
        ToByte toByte2 = new ToByte((byte) 0);
        for (int i7 = 0; i7 < 12; i7++) {
            objectReaderProvider.registerTypeConvert(clsArr[i7], Byte.TYPE, toByte2);
        }
        ToShort toShort = new ToShort(null);
        for (int i8 = 0; i8 < 12; i8++) {
            objectReaderProvider.registerTypeConvert(clsArr[i8], Short.class, toShort);
        }
        ToShort toShort2 = new ToShort((short) 0);
        for (int i9 = 0; i9 < 12; i9++) {
            objectReaderProvider.registerTypeConvert(clsArr[i9], Short.TYPE, toShort2);
        }
        ToInteger toInteger = new ToInteger(null);
        for (int i10 = 0; i10 < 12; i10++) {
            objectReaderProvider.registerTypeConvert(clsArr[i10], Integer.class, toInteger);
        }
        ToInteger toInteger2 = new ToInteger(0);
        for (int i11 = 0; i11 < 12; i11++) {
            objectReaderProvider.registerTypeConvert(clsArr[i11], Integer.TYPE, toInteger2);
        }
        ToLong toLong = new ToLong(null);
        for (int i12 = 0; i12 < 12; i12++) {
            objectReaderProvider.registerTypeConvert(clsArr[i12], Long.class, toLong);
        }
        ToLong toLong2 = new ToLong(0L);
        for (int i13 = 0; i13 < 12; i13++) {
            objectReaderProvider.registerTypeConvert(clsArr[i13], Long.TYPE, toLong2);
        }
        ToFloat toFloat = new ToFloat(null);
        for (int i14 = 0; i14 < 12; i14++) {
            objectReaderProvider.registerTypeConvert(clsArr[i14], Float.class, toFloat);
        }
        ToFloat toFloat2 = new ToFloat(Float.valueOf(0.0f));
        for (int i15 = 0; i15 < 12; i15++) {
            objectReaderProvider.registerTypeConvert(clsArr[i15], Float.TYPE, toFloat2);
        }
        ToDouble toDouble = new ToDouble(null);
        for (int i16 = 0; i16 < 12; i16++) {
            objectReaderProvider.registerTypeConvert(clsArr[i16], Double.class, toDouble);
        }
        ToDouble toDouble2 = new ToDouble(Double.valueOf(0.0d));
        int i17 = 0;
        while (i17 < 12) {
            objectReaderProvider.registerTypeConvert(clsArr[i17], Double.TYPE, toDouble2);
            i17++;
            z = z;
        }
        boolean z2 = z;
        ToNumber toNumber = new ToNumber(Double.valueOf(0.0d));
        for (?? r4 = z2; r4 < 12; r4++) {
            objectReaderProvider.registerTypeConvert(clsArr[r4], Number.class, toNumber);
        }
        objectReaderProvider.registerTypeConvert(String.class, Character.TYPE, new StringToAny(Character.TYPE, '0'));
        objectReaderProvider.registerTypeConvert(String.class, Boolean.TYPE, new StringToAny(Boolean.TYPE, Boolean.valueOf(z2)));
        objectReaderProvider.registerTypeConvert(String.class, Float.TYPE, new StringToAny(Float.TYPE, Float.valueOf(0.0f)));
        objectReaderProvider.registerTypeConvert(String.class, Double.TYPE, new StringToAny(Double.TYPE, Double.valueOf(0.0d)));
        objectReaderProvider.registerTypeConvert(String.class, Byte.TYPE, new StringToAny(Byte.TYPE, (byte) 0));
        objectReaderProvider.registerTypeConvert(String.class, Short.TYPE, new StringToAny(Short.TYPE, (short) 0));
        objectReaderProvider.registerTypeConvert(String.class, Integer.TYPE, new StringToAny(Integer.TYPE, 0));
        objectReaderProvider.registerTypeConvert(String.class, Long.TYPE, new StringToAny(Long.TYPE, 0L));
        objectReaderProvider.registerTypeConvert(String.class, Character.class, new StringToAny(Character.class, null));
        objectReaderProvider.registerTypeConvert(String.class, Boolean.class, new StringToAny(Boolean.class, null));
        objectReaderProvider.registerTypeConvert(String.class, Double.class, new StringToAny(Double.class, null));
        objectReaderProvider.registerTypeConvert(String.class, Float.class, new StringToAny(Float.class, null));
        objectReaderProvider.registerTypeConvert(String.class, Byte.class, new StringToAny(Byte.class, null));
        objectReaderProvider.registerTypeConvert(String.class, Short.class, new StringToAny(Short.class, null));
        objectReaderProvider.registerTypeConvert(String.class, Integer.class, new StringToAny(Integer.class, null));
        objectReaderProvider.registerTypeConvert(String.class, Long.class, new StringToAny(Long.class, null));
        objectReaderProvider.registerTypeConvert(String.class, BigDecimal.class, new StringToAny(BigDecimal.class, null));
        objectReaderProvider.registerTypeConvert(String.class, BigInteger.class, new StringToAny(BigInteger.class, null));
        objectReaderProvider.registerTypeConvert(String.class, Number.class, new StringToAny(BigDecimal.class, null));
        objectReaderProvider.registerTypeConvert(String.class, Collection.class, new StringToAny(Collection.class, null));
        objectReaderProvider.registerTypeConvert(String.class, List.class, new StringToAny(List.class, null));
        objectReaderProvider.registerTypeConvert(String.class, JSONArray.class, new StringToAny(JSONArray.class, null));
        objectReaderProvider.registerTypeConvert(Boolean.class, Boolean.TYPE, new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$$ExternalSyntheticLambda20
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ObjectReaderBaseModule.lambda$init$1(obj);
            }
        });
        objectReaderProvider.registerTypeConvert(Long.class, LocalDateTime.class, new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$$ExternalSyntheticLambda21
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ObjectReaderBaseModule.lambda$init$2(obj);
            }
        });
        objectReaderProvider.registerTypeConvert(String.class, UUID.class, new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$$ExternalSyntheticLambda22
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ObjectReaderBaseModule.lambda$init$3(obj);
            }
        });
    }

    static /* synthetic */ Object lambda$init$2(Object obj) {
        if (obj == null || "null".equals(obj) || obj.equals(0L)) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(((Long) obj).longValue()), ZoneId.systemDefault());
    }

    static /* synthetic */ Object lambda$init$3(Object obj) {
        if (obj == null || "null".equals(obj) || "".equals(obj)) {
            return null;
        }
        return UUID.fromString((String) obj);
    }

    public class ReaderAnnotationProcessor implements ObjectReaderAnnotationProcessor {
        public ReaderAnnotationProcessor() {
        }

        @Override // com.alibaba.fastjson2.modules.ObjectReaderAnnotationProcessor
        public void getBeanInfo(final BeanInfo beanInfo, final Class<?> cls) {
            int i;
            boolean zIsUseJacksonAnnotation;
            Class cls2 = ObjectReaderBaseModule.this.provider.mixInCache.get(cls);
            if (cls2 == null && "org.apache.commons.lang3.tuple.Triple".equals(cls.getName())) {
                ObjectReaderBaseModule.this.provider.mixIn(cls, ApacheLang3Support.TripleMixIn.class);
                cls2 = ApacheLang3Support.TripleMixIn.class;
            }
            if (cls2 != null && cls2 != cls) {
                beanInfo.mixIn = true;
                getBeanInfo(beanInfo, BeanUtils.getAnnotations(cls2));
                BeanUtils.staticMethod(cls2, new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda8
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.m33xb767e488(beanInfo, cls, (Method) obj);
                    }
                });
                BeanUtils.constructor(cls2, new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda9
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.m34xbe90c6c9(beanInfo, cls, (Constructor) obj);
                    }
                });
            }
            Class<? super Object> superclass = cls.getSuperclass();
            Class<? super Object> cls3 = null;
            while (true) {
                i = 0;
                if (superclass == null || superclass == Object.class || superclass == Enum.class) {
                    break;
                }
                BeanInfo beanInfo2 = new BeanInfo(JSONFactory.getDefaultObjectReaderProvider());
                getBeanInfo(beanInfo2, superclass);
                if (beanInfo2.seeAlso != null) {
                    Class<?>[] clsArr = beanInfo2.seeAlso;
                    int length = clsArr.length;
                    while (true) {
                        if (i >= length) {
                            cls3 = superclass;
                            break;
                        } else if (clsArr[i] == cls) {
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                superclass = superclass.getSuperclass();
            }
            if (cls3 != null) {
                getBeanInfo(beanInfo, cls3);
            }
            Annotation[] annotations = BeanUtils.getAnnotations(cls);
            getBeanInfo(beanInfo, annotations);
            for (Annotation annotation : annotations) {
                zIsUseJacksonAnnotation = JSONFactory.isUseJacksonAnnotation();
                String name = annotation.annotationType().getName();
                name.hashCode();
                switch (name) {
                    case "com.fasterxml.jackson.databind.annotation.JsonDeserialize":
                        if (zIsUseJacksonAnnotation) {
                            processJacksonJsonDeserializer(beanInfo, annotation);
                            break;
                        } else {
                            break;
                        }
                        break;
                    case "com.fasterxml.jackson.annotation.JsonFormat":
                        if (zIsUseJacksonAnnotation) {
                            BeanUtils.processJacksonJsonFormat(beanInfo, annotation);
                            break;
                        } else {
                            break;
                        }
                        break;
                    case "com.fasterxml.jackson.annotation.JsonSubTypes":
                        if (zIsUseJacksonAnnotation) {
                            processJacksonJsonSubTypes(beanInfo, annotation);
                            break;
                        } else {
                            break;
                        }
                        break;
                    case "com.fasterxml.jackson.annotation.JsonTypeInfo":
                        if (zIsUseJacksonAnnotation) {
                            processJacksonJsonTypeInfo(beanInfo, annotation);
                            break;
                        } else {
                            break;
                        }
                        break;
                    case "com.fasterxml.jackson.annotation.JsonTypeName":
                        if (zIsUseJacksonAnnotation) {
                            BeanUtils.processJacksonJsonTypeName(beanInfo, annotation);
                            break;
                        } else {
                            break;
                        }
                        break;
                    case "kotlin.Metadata":
                        beanInfo.f4kotlin = true;
                        break;
                    case "com.alibaba.fastjson.annotation.JSONType":
                        getBeanInfo1x(beanInfo, annotation);
                        break;
                }
            }
            if (JDKUtils.JVM_VERSION >= 17 && beanInfo.seeAlso == null && cls.isAnnotationPresent(JSONType.class)) {
                try {
                    Method method = ObjectReaderBaseModule.METHOD_getPermittedSubclasses;
                    if (method == null) {
                        method = Class.class.getMethod("getPermittedSubclasses", new Class[0]);
                        ObjectReaderBaseModule.METHOD_getPermittedSubclasses = method;
                    }
                    Class<?>[] clsArr2 = (Class[]) method.invoke(cls, new Object[0]);
                    beanInfo.seeAlso = clsArr2;
                    beanInfo.seeAlsoNames = new String[clsArr2.length];
                    while (i < clsArr2.length) {
                        Class<?> cls4 = clsArr2[i];
                        BeanInfo beanInfo3 = new BeanInfo(ObjectReaderBaseModule.this.provider);
                        processSeeAlsoAnnotation(beanInfo3, cls4);
                        String simpleName = beanInfo3.typeName;
                        if (simpleName == null || simpleName.isEmpty()) {
                            simpleName = cls4.getSimpleName();
                        }
                        beanInfo.seeAlsoNames[i] = simpleName;
                        i++;
                    }
                    beanInfo.readerFeatures |= JSONReader.Feature.SupportAutoType.mask;
                } catch (Throwable unused) {
                }
            }
            BeanUtils.staticMethod(cls, new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda10
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m35xc5b9a90a(beanInfo, cls, (Method) obj);
                }
            });
            BeanUtils.constructor(cls, new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda11
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m36xcce28b4b(beanInfo, cls, (Constructor) obj);
                }
            });
            if (beanInfo.creatorConstructor == null && (beanInfo.readerFeatures & JSONReader.Feature.FieldBased.mask) == 0 && beanInfo.f4kotlin) {
                KotlinUtils.getConstructor(cls, beanInfo);
            }
        }

        /* JADX INFO: renamed from: lambda$getBeanInfo$0$com-alibaba-fastjson2-reader-ObjectReaderBaseModule$ReaderAnnotationProcessor, reason: not valid java name */
        /* synthetic */ void m33xb767e488(BeanInfo beanInfo, Class cls, Method method) {
            ObjectReaderBaseModule.this.getCreator(beanInfo, (Class<?>) cls, method);
        }

        /* JADX INFO: renamed from: lambda$getBeanInfo$1$com-alibaba-fastjson2-reader-ObjectReaderBaseModule$ReaderAnnotationProcessor, reason: not valid java name */
        /* synthetic */ void m34xbe90c6c9(BeanInfo beanInfo, Class cls, Constructor constructor) {
            ObjectReaderBaseModule.this.getCreator(beanInfo, (Class<?>) cls, constructor);
        }

        /* JADX INFO: renamed from: lambda$getBeanInfo$2$com-alibaba-fastjson2-reader-ObjectReaderBaseModule$ReaderAnnotationProcessor, reason: not valid java name */
        /* synthetic */ void m35xc5b9a90a(BeanInfo beanInfo, Class cls, Method method) {
            ObjectReaderBaseModule.this.getCreator(beanInfo, (Class<?>) cls, method);
        }

        /* JADX INFO: renamed from: lambda$getBeanInfo$3$com-alibaba-fastjson2-reader-ObjectReaderBaseModule$ReaderAnnotationProcessor, reason: not valid java name */
        /* synthetic */ void m36xcce28b4b(BeanInfo beanInfo, Class cls, Constructor constructor) {
            ObjectReaderBaseModule.this.getCreator(beanInfo, (Class<?>) cls, constructor);
        }

        private void processJacksonJsonSubTypes(final BeanInfo beanInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda12
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectReaderBaseModule.ReaderAnnotationProcessor.lambda$processJacksonJsonSubTypes$4(annotation, beanInfo, (Method) obj);
                }
            });
        }

        static /* synthetic */ void lambda$processJacksonJsonSubTypes$4(Annotation annotation, BeanInfo beanInfo, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                if ("value".equals(name)) {
                    Object[] objArr = (Object[]) objInvoke;
                    if (objArr.length != 0) {
                        beanInfo.seeAlso = new Class[objArr.length];
                        beanInfo.seeAlsoNames = new String[objArr.length];
                        for (int i = 0; i < objArr.length; i++) {
                            BeanUtils.processJacksonJsonSubTypesType(beanInfo, i, (Annotation) objArr[i]);
                        }
                    }
                }
            } catch (Throwable unused) {
            }
        }

        private void processJacksonJsonDeserializer(final BeanInfo beanInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m40x47139f92(annotation, beanInfo, (Method) obj);
                }
            });
        }

        /* JADX INFO: renamed from: lambda$processJacksonJsonDeserializer$5$com-alibaba-fastjson2-reader-ObjectReaderBaseModule$ReaderAnnotationProcessor, reason: not valid java name */
        /* synthetic */ void m40x47139f92(Annotation annotation, BeanInfo beanInfo, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                if (!"using".equals(name) && !"contentUsing".equals(name)) {
                    if ("builder".equals(name)) {
                        processBuilder(beanInfo, (Class) objInvoke);
                        return;
                    }
                    return;
                }
                Class clsProcessUsing = processUsing((Class) objInvoke);
                if (clsProcessUsing != null) {
                    beanInfo.deserializer = clsProcessUsing;
                }
            } catch (Throwable unused) {
            }
        }

        private void processJacksonJsonTypeInfo(final BeanInfo beanInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectReaderBaseModule.ReaderAnnotationProcessor.lambda$processJacksonJsonTypeInfo$6(annotation, beanInfo, (Method) obj);
                }
            });
        }

        static /* synthetic */ void lambda$processJacksonJsonTypeInfo$6(Annotation annotation, BeanInfo beanInfo, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                if ("property".equals(name)) {
                    String str = (String) objInvoke;
                    if (str.isEmpty()) {
                        return;
                    }
                    beanInfo.typeKey = str;
                    beanInfo.readerFeatures |= JSONReader.Feature.SupportAutoType.mask;
                }
            } catch (Throwable unused) {
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x001a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void getBeanInfo(com.alibaba.fastjson2.codec.BeanInfo r7, java.lang.annotation.Annotation[] r8) {
            /*
                r6 = this;
                int r0 = r8.length
                r1 = 0
            L2:
                if (r1 >= r0) goto L32
                r2 = r8[r1]
                java.lang.Class r3 = r2.annotationType()
                java.lang.Class<com.alibaba.fastjson2.annotation.JSONType> r4 = com.alibaba.fastjson2.annotation.JSONType.class
                java.lang.annotation.Annotation r4 = com.alibaba.fastjson2.util.BeanUtils.findAnnotation(r2, r4)
                com.alibaba.fastjson2.annotation.JSONType r4 = (com.alibaba.fastjson2.annotation.JSONType) r4
                if (r4 == 0) goto L1a
                r6.getBeanInfo1x(r7, r2)
                if (r4 != r2) goto L1a
                goto L2f
            L1a:
                java.lang.Class<com.alibaba.fastjson2.annotation.JSONCompiler> r4 = com.alibaba.fastjson2.annotation.JSONCompiler.class
                if (r3 != r4) goto L2f
                com.alibaba.fastjson2.annotation.JSONCompiler r2 = (com.alibaba.fastjson2.annotation.JSONCompiler) r2
                com.alibaba.fastjson2.annotation.JSONCompiler$CompilerOption r2 = r2.value()
                com.alibaba.fastjson2.annotation.JSONCompiler$CompilerOption r3 = com.alibaba.fastjson2.annotation.JSONCompiler.CompilerOption.LAMBDA
                if (r2 != r3) goto L2f
                long r2 = r7.readerFeatures
                r4 = 18014398509481984(0x40000000000000, double:1.7800590868057611E-307)
                long r2 = r2 | r4
                r7.readerFeatures = r2
            L2f:
                int r1 = r1 + 1
                goto L2
            L32:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderBaseModule.ReaderAnnotationProcessor.getBeanInfo(com.alibaba.fastjson2.codec.BeanInfo, java.lang.annotation.Annotation[]):void");
        }

        void getBeanInfo1x(final BeanInfo beanInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m37x9eaa6af6(annotation, beanInfo, (Method) obj);
                }
            });
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        /* JADX WARN: Removed duplicated region for block: B:86:0x013e A[Catch: all -> 0x0217, TryCatch #0 {all -> 0x0217, blocks: (B:3:0x0005, B:4:0x000f, B:6:0x0014, B:8:0x001c, B:10:0x0021, B:11:0x0028, B:13:0x002b, B:15:0x003d, B:18:0x0047, B:17:0x0043, B:19:0x004e, B:21:0x0058, B:23:0x0060, B:25:0x0065, B:27:0x0068, B:59:0x00e0, B:61:0x00e6, B:63:0x00ee, B:30:0x0071, B:32:0x0079, B:34:0x0081, B:36:0x0089, B:38:0x0091, B:40:0x0096, B:41:0x00a2, B:43:0x00aa, B:45:0x00b4, B:47:0x00b7, B:49:0x00bf, B:51:0x00c5, B:53:0x00cd, B:55:0x00d5, B:57:0x00d8, B:65:0x00f1, B:67:0x00f9, B:69:0x0100, B:84:0x0136, B:86:0x013e, B:70:0x0103, B:72:0x010b, B:74:0x0113, B:76:0x0116, B:78:0x011e, B:80:0x0126, B:82:0x012e, B:88:0x0141, B:90:0x0149, B:92:0x0155, B:94:0x0158, B:96:0x0160, B:98:0x0168, B:100:0x0170, B:102:0x0178, B:104:0x017d, B:106:0x0180, B:108:0x0188, B:110:0x0191, B:112:0x0199, B:114:0x019e, B:115:0x01a8, B:127:0x01e8, B:117:0x01ac, B:119:0x01b4, B:120:0x01be, B:122:0x01c6, B:123:0x01d0, B:125:0x01d8, B:126:0x01e2, B:128:0x01eb, B:130:0x01f3, B:132:0x01fb, B:134:0x0203, B:136:0x020b, B:138:0x0215), top: B:141:0x0005 }] */
        /* JADX INFO: renamed from: lambda$getBeanInfo1x$7$com-alibaba-fastjson2-reader-ObjectReaderBaseModule$ReaderAnnotationProcessor, reason: not valid java name */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        /* synthetic */ void m37x9eaa6af6(java.lang.annotation.Annotation r7, com.alibaba.fastjson2.codec.BeanInfo r8, java.lang.reflect.Method r9) {
            /*
                Method dump skipped, instruction units count: 636
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderBaseModule.ReaderAnnotationProcessor.m37x9eaa6af6(java.lang.annotation.Annotation, com.alibaba.fastjson2.codec.BeanInfo, java.lang.reflect.Method):void");
        }

        private void processBuilder(BeanInfo beanInfo, Class cls) {
            if (cls == Void.TYPE || cls == Void.class) {
                return;
            }
            beanInfo.builder = cls;
            for (Annotation annotation : BeanUtils.getAnnotations(cls)) {
                Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
                String name = clsAnnotationType.getName();
                if ("com.alibaba.fastjson.annotation.JSONPOJOBuilder".equals(name) || "com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder".equals(name)) {
                    ObjectReaderBaseModule.this.getBeanInfo1xJSONPOJOBuilder(beanInfo, cls, annotation, clsAnnotationType);
                } else {
                    JSONBuilder jSONBuilder = (JSONBuilder) BeanUtils.findAnnotation(cls, JSONBuilder.class);
                    if (jSONBuilder != null) {
                        beanInfo.buildMethod = BeanUtils.buildMethod(cls, jSONBuilder.buildMethod());
                        String strWithPrefix = jSONBuilder.withPrefix();
                        if (!strWithPrefix.isEmpty()) {
                            beanInfo.builderWithPrefix = strWithPrefix;
                        }
                    }
                }
            }
            if (beanInfo.buildMethod == null) {
                beanInfo.buildMethod = BeanUtils.buildMethod(cls, "build");
            }
            if (beanInfo.buildMethod == null) {
                beanInfo.buildMethod = BeanUtils.buildMethod(cls, "create");
            }
        }

        private void processSeeAlsoAnnotation(BeanInfo beanInfo, Class<?> cls) {
            Class cls2 = ObjectReaderBaseModule.this.provider.mixInCache.get(cls);
            if (cls2 == null && "org.apache.commons.lang3.tuple.Triple".equals(cls.getName())) {
                ObjectReaderBaseModule.this.provider.mixIn(cls, ApacheLang3Support.TripleMixIn.class);
                cls2 = ApacheLang3Support.TripleMixIn.class;
            }
            if (cls2 != null && cls2 != cls) {
                beanInfo.mixIn = true;
                processSeeAlsoAnnotation(beanInfo, BeanUtils.getAnnotations(cls2));
            }
            processSeeAlsoAnnotation(beanInfo, BeanUtils.getAnnotations(cls));
        }

        private void processSeeAlsoAnnotation(final BeanInfo beanInfo, Annotation[] annotationArr) {
            for (final Annotation annotation : annotationArr) {
                BeanUtils.annotationMethods(annotation.annotationType(), new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda4
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ObjectReaderBaseModule.ReaderAnnotationProcessor.lambda$processSeeAlsoAnnotation$8(annotation, beanInfo, (Method) obj);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$processSeeAlsoAnnotation$8(Annotation annotation, BeanInfo beanInfo, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                if ("typeName".equals(name)) {
                    String str = (String) objInvoke;
                    if (str.isEmpty()) {
                        return;
                    }
                    beanInfo.typeName = str;
                }
            } catch (Throwable unused) {
            }
        }

        @Override // com.alibaba.fastjson2.modules.ObjectReaderAnnotationProcessor
        public void getFieldInfo(FieldInfo fieldInfo, Class cls, Constructor constructor, int i, Parameter parameter) {
            Class cls2;
            Constructor declaredConstructor;
            Annotation[] annotations = null;
            if (cls != null && (cls2 = ObjectReaderBaseModule.this.provider.mixInCache.get(cls)) != null && cls2 != cls) {
                try {
                    declaredConstructor = cls2.getDeclaredConstructor(constructor.getParameterTypes());
                } catch (NoSuchMethodException unused) {
                    declaredConstructor = null;
                }
                if (declaredConstructor != null) {
                    processAnnotation(fieldInfo, BeanUtils.getAnnotations(declaredConstructor.getParameters()[i]));
                }
            }
            if (Modifier.isStatic(constructor.getDeclaringClass().getModifiers())) {
                try {
                    annotations = BeanUtils.getAnnotations(parameter);
                } catch (ArrayIndexOutOfBoundsException unused2) {
                }
            } else {
                Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
                if (parameterAnnotations.length != constructor.getParameterCount()) {
                    i--;
                }
                if (i >= 0 && i < parameterAnnotations.length) {
                    annotations = parameterAnnotations[i];
                }
            }
            if (annotations == null || annotations.length <= 0) {
                return;
            }
            processAnnotation(fieldInfo, annotations);
        }

        @Override // com.alibaba.fastjson2.modules.ObjectReaderAnnotationProcessor
        public void getFieldInfo(FieldInfo fieldInfo, Class cls, Method method, int i, Parameter parameter) {
            Class cls2;
            Method method2;
            if (cls != null && (cls2 = ObjectReaderBaseModule.this.provider.mixInCache.get(cls)) != null && cls2 != cls) {
                try {
                    method2 = cls2.getMethod(method.getName(), method.getParameterTypes());
                } catch (NoSuchMethodException unused) {
                    method2 = null;
                }
                if (method2 != null) {
                    processAnnotation(fieldInfo, BeanUtils.getAnnotations(method2.getParameters()[i]));
                }
            }
            processAnnotation(fieldInfo, BeanUtils.getAnnotations(parameter));
        }

        @Override // com.alibaba.fastjson2.modules.ObjectReaderAnnotationProcessor
        public void getFieldInfo(FieldInfo fieldInfo, Class cls, Field field) {
            Class cls2;
            Field declaredField;
            if (cls != null && (cls2 = ObjectReaderBaseModule.this.provider.mixInCache.get(cls)) != null && cls2 != cls) {
                try {
                    declaredField = cls2.getDeclaredField(field.getName());
                } catch (Exception unused) {
                    declaredField = null;
                }
                if (declaredField != null) {
                    getFieldInfo(fieldInfo, cls2, declaredField);
                }
            }
            processAnnotation(fieldInfo, BeanUtils.getAnnotations(field));
        }

        /* JADX WARN: Removed duplicated region for block: B:32:0x007d A[PHI: r9
          0x007d: PHI (r9v2 java.lang.String) = (r9v1 java.lang.String), (r9v4 java.lang.String) binds: [B:28:0x0070, B:30:0x0079] A[DONT_GENERATE, DONT_INLINE]] */
        @Override // com.alibaba.fastjson2.modules.ObjectReaderAnnotationProcessor
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void getFieldInfo(final com.alibaba.fastjson2.codec.FieldInfo r19, final java.lang.Class r20, java.lang.reflect.Method r21) {
            /*
                Method dump skipped, instruction units count: 482
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderBaseModule.ReaderAnnotationProcessor.getFieldInfo(com.alibaba.fastjson2.codec.FieldInfo, java.lang.Class, java.lang.reflect.Method):void");
        }

        /* JADX INFO: renamed from: lambda$getFieldInfo$9$com-alibaba-fastjson2-reader-ObjectReaderBaseModule$ReaderAnnotationProcessor, reason: not valid java name */
        /* synthetic */ void m38x8ae4c409(String str, FieldInfo fieldInfo, Class cls, String str2, String str3, Field field) {
            if (field.getName().equals(str)) {
                int modifiers = field.getModifiers();
                if (!Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                    getFieldInfo(fieldInfo, cls, field);
                }
                fieldInfo.features |= FieldInfo.FIELD_MASK;
                return;
            }
            if (field.getName().equals(str2)) {
                int modifiers2 = field.getModifiers();
                if (!Modifier.isPublic(modifiers2) && !Modifier.isStatic(modifiers2)) {
                    getFieldInfo(fieldInfo, cls, field);
                }
                fieldInfo.features |= FieldInfo.FIELD_MASK;
                return;
            }
            if (field.getName().equals(str3)) {
                int modifiers3 = field.getModifiers();
                if (!Modifier.isPublic(modifiers3) && !Modifier.isStatic(modifiers3)) {
                    getFieldInfo(fieldInfo, cls, field);
                }
                fieldInfo.features |= FieldInfo.FIELD_MASK;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x001c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void processAnnotation(com.alibaba.fastjson2.codec.FieldInfo r10, java.lang.annotation.Annotation[] r11) {
            /*
                Method dump skipped, instruction units count: 362
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderBaseModule.ReaderAnnotationProcessor.processAnnotation(com.alibaba.fastjson2.codec.FieldInfo, java.lang.annotation.Annotation[]):void");
        }

        private void processJacksonJsonDeserialize(final FieldInfo fieldInfo, final Annotation annotation) {
            if (JSONFactory.isUseJacksonAnnotation()) {
                BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.m39x3ab2c52(annotation, fieldInfo, (Method) obj);
                    }
                });
            }
        }

        /* JADX INFO: renamed from: lambda$processJacksonJsonDeserialize$10$com-alibaba-fastjson2-reader-ObjectReaderBaseModule$ReaderAnnotationProcessor, reason: not valid java name */
        /* synthetic */ void m39x3ab2c52(Annotation annotation, FieldInfo fieldInfo, Method method) {
            Class<?> clsProcessUsing;
            Class<?> clsProcessUsing2;
            Class<?> clsProcessUsing3;
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                int iHashCode = name.hashCode();
                if (iHashCode == 111582340) {
                    if (!name.equals("using") || (clsProcessUsing = processUsing((Class) objInvoke)) == null) {
                        return;
                    }
                    fieldInfo.readUsing = clsProcessUsing;
                    return;
                }
                if (iHashCode == 491860325) {
                    if (!name.equals("keyUsing") || (clsProcessUsing2 = processUsing((Class) objInvoke)) == null) {
                        return;
                    }
                    fieldInfo.keyUsing = clsProcessUsing2;
                    return;
                }
                if (iHashCode == 2034063763 && name.equals("valueUsing") && (clsProcessUsing3 = processUsing((Class) objInvoke)) != null) {
                    fieldInfo.keyUsing = clsProcessUsing3;
                }
            } catch (Throwable unused) {
            }
        }

        private Class processUsing(Class cls) {
            if ("com.fasterxml.jackson.databind.JsonDeserializer$None".equals(cls.getName()) || !ObjectReader.class.isAssignableFrom(cls)) {
                return null;
            }
            return cls;
        }

        private void processJacksonJsonProperty(final FieldInfo fieldInfo, final Annotation annotation) {
            if (JSONFactory.isUseJacksonAnnotation()) {
                BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda14
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ObjectReaderBaseModule.ReaderAnnotationProcessor.lambda$processJacksonJsonProperty$11(annotation, fieldInfo, (Method) obj);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$processJacksonJsonProperty$11(Annotation annotation, FieldInfo fieldInfo, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                int iHashCode = name.hashCode();
                if (iHashCode == -1423461020) {
                    if (name.equals("access")) {
                        fieldInfo.ignore = "READ_ONLY".equals(((Enum) objInvoke).name());
                        return;
                    }
                    return;
                }
                if (iHashCode == -393139297) {
                    if (name.equals("required") && ((Boolean) objInvoke).booleanValue()) {
                        fieldInfo.required = true;
                        return;
                    }
                    return;
                }
                if (iHashCode == 111972721 && name.equals("value")) {
                    String str = (String) objInvoke;
                    if (str.isEmpty()) {
                        return;
                    }
                    if (fieldInfo.fieldName == null || fieldInfo.fieldName.isEmpty()) {
                        fieldInfo.fieldName = str;
                    }
                }
            } catch (Throwable unused) {
            }
        }

        private void processJacksonJsonSetter(final FieldInfo fieldInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectReaderBaseModule.ReaderAnnotationProcessor.lambda$processJacksonJsonSetter$12(annotation, fieldInfo, (Method) obj);
                }
            });
        }

        static /* synthetic */ void lambda$processJacksonJsonSetter$12(Annotation annotation, FieldInfo fieldInfo, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                if (name.hashCode() == 111972721 && name.equals("value")) {
                    String str = (String) objInvoke;
                    if (str.isEmpty()) {
                        return;
                    }
                    fieldInfo.fieldName = str;
                }
            } catch (Throwable unused) {
            }
        }

        private void processJacksonJsonAlias(final FieldInfo fieldInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectReaderBaseModule.ReaderAnnotationProcessor.lambda$processJacksonJsonAlias$13(annotation, fieldInfo, (Method) obj);
                }
            });
        }

        static /* synthetic */ void lambda$processJacksonJsonAlias$13(Annotation annotation, FieldInfo fieldInfo, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                if ("value".equals(name)) {
                    String[] strArr = (String[]) objInvoke;
                    if (strArr.length != 0) {
                        fieldInfo.alternateNames = strArr;
                    }
                }
            } catch (Throwable unused) {
            }
        }

        private void processJSONField1x(final FieldInfo fieldInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$ReaderAnnotationProcessor$$ExternalSyntheticLambda13
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectReaderBaseModule.ReaderAnnotationProcessor.lambda$processJSONField1x$14(annotation, fieldInfo, (Method) obj);
                }
            });
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        static /* synthetic */ void lambda$processJSONField1x$14(Annotation annotation, FieldInfo fieldInfo, Method method) {
            int iIntValue;
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                switch (name.hashCode()) {
                    case -1268779017:
                        if (name.equals("format")) {
                            String str = (String) objInvoke;
                            if (!str.isEmpty()) {
                                String strTrim = str.trim();
                                if (strTrim.indexOf(84) != -1 && !strTrim.contains("'T'")) {
                                    strTrim = strTrim.replace(ExifInterface.GPS_DIRECTION_TRUE, "'T'");
                                }
                                fieldInfo.format = strTrim;
                            }
                        }
                        break;
                    case -1206994319:
                        if (name.equals("ordinal") && (iIntValue = ((Integer) objInvoke).intValue()) != 0) {
                            fieldInfo.ordinal = iIntValue;
                            break;
                        }
                        break;
                    case -1073807344:
                        if (name.equals("parseFeatures")) {
                            for (Enum r0 : (Enum[]) objInvoke) {
                                String strName = r0.name();
                                int iHashCode = strName.hashCode();
                                if (iHashCode != -894003883) {
                                    if (iHashCode != -200815016) {
                                        if (iHashCode == 2005790178 && strName.equals("InitStringFieldAsEmpty")) {
                                            fieldInfo.features |= JSONReader.Feature.InitStringFieldAsEmpty.mask;
                                        }
                                    } else if (strName.equals("SupportAutoType")) {
                                        fieldInfo.features |= JSONReader.Feature.SupportAutoType.mask;
                                    }
                                } else if (strName.equals("SupportArrayToBean")) {
                                    fieldInfo.features |= JSONReader.Feature.SupportArrayToBean.mask;
                                }
                            }
                        }
                        break;
                    case -987658292:
                        if (name.equals("unwrapped") && ((Boolean) objInvoke).booleanValue()) {
                            fieldInfo.features |= FieldInfo.UNWRAPPED_MASK;
                            break;
                        }
                        break;
                    case -659125328:
                        if (name.equals("defaultValue")) {
                            String str2 = (String) objInvoke;
                            if (!str2.isEmpty()) {
                                fieldInfo.defaultValue = str2;
                            }
                        }
                        break;
                    case -224599314:
                        if (name.equals("alternateNames")) {
                            String[] strArr = (String[]) objInvoke;
                            if (strArr.length != 0) {
                                if (fieldInfo.alternateNames == null) {
                                    fieldInfo.alternateNames = strArr;
                                } else {
                                    LinkedHashSet linkedHashSet = new LinkedHashSet();
                                    linkedHashSet.addAll(Arrays.asList(strArr));
                                    linkedHashSet.addAll(Arrays.asList(fieldInfo.alternateNames));
                                    fieldInfo.alternateNames = (String[]) linkedHashSet.toArray(new String[linkedHashSet.size()]);
                                }
                            }
                        }
                        break;
                    case 3373707:
                        if (name.equals("name")) {
                            String str3 = (String) objInvoke;
                            if (!str3.isEmpty()) {
                                fieldInfo.fieldName = str3;
                            }
                        }
                        break;
                    case 102727412:
                        if (name.equals("label")) {
                            String str4 = (String) objInvoke;
                            if (!str4.isEmpty()) {
                                fieldInfo.label = str4;
                            }
                        }
                        break;
                    case 1053501509:
                        if (name.equals("deserializeUsing")) {
                            Class<?> cls = (Class) objInvoke;
                            if (ObjectReader.class.isAssignableFrom(cls)) {
                                fieldInfo.readUsing = cls;
                            }
                        }
                        break;
                    case 1746983807:
                        if (name.equals("deserialize") && !((Boolean) objInvoke).booleanValue()) {
                            fieldInfo.ignore = true;
                            break;
                        }
                        break;
                }
            } catch (Throwable unused) {
            }
        }

        private void getFieldInfo(FieldInfo fieldInfo, JSONField jSONField) {
            if (jSONField == null) {
                return;
            }
            String strName = jSONField.name();
            if (!strName.isEmpty()) {
                fieldInfo.fieldName = strName;
            }
            String str = jSONField.format();
            if (!str.isEmpty()) {
                String strTrim = str.trim();
                if (strTrim.indexOf(84) != -1 && !strTrim.contains("'T'")) {
                    strTrim = strTrim.replace(ExifInterface.GPS_DIRECTION_TRUE, "'T'");
                }
                fieldInfo.format = strTrim;
            }
            String strLabel = jSONField.label();
            if (!strLabel.isEmpty()) {
                fieldInfo.label = strLabel.trim();
            }
            String strDefaultValue = jSONField.defaultValue();
            if (!strDefaultValue.isEmpty()) {
                fieldInfo.defaultValue = strDefaultValue;
            }
            String strLocale = jSONField.locale();
            if (!strLocale.isEmpty()) {
                String[] strArrSplit = strLocale.split("_");
                if (strArrSplit.length == 2) {
                    fieldInfo.locale = new Locale(strArrSplit[0], strArrSplit[1]);
                }
            }
            String[] strArrAlternateNames = jSONField.alternateNames();
            if (strArrAlternateNames.length != 0) {
                if (fieldInfo.alternateNames == null) {
                    fieldInfo.alternateNames = strArrAlternateNames;
                } else {
                    LinkedHashSet linkedHashSet = new LinkedHashSet(strArrAlternateNames.length + fieldInfo.alternateNames.length, 1.0f);
                    Collections.addAll(linkedHashSet, strArrAlternateNames);
                    Collections.addAll(linkedHashSet, fieldInfo.alternateNames);
                    fieldInfo.alternateNames = (String[]) linkedHashSet.toArray(new String[linkedHashSet.size()]);
                }
            }
            boolean zDeserialize = jSONField.deserialize();
            boolean z = !zDeserialize;
            if (!fieldInfo.ignore) {
                fieldInfo.ignore = z;
            }
            for (JSONReader.Feature feature : jSONField.deserializeFeatures()) {
                fieldInfo.features |= feature.mask;
                if (fieldInfo.ignore && zDeserialize && feature == JSONReader.Feature.FieldBased) {
                    fieldInfo.ignore = false;
                }
            }
            int iOrdinal = jSONField.ordinal();
            if (iOrdinal != 0) {
                fieldInfo.ordinal = iOrdinal;
            }
            if (jSONField.value()) {
                fieldInfo.features |= FieldInfo.VALUE_MASK;
            }
            if (jSONField.unwrapped()) {
                fieldInfo.features |= FieldInfo.UNWRAPPED_MASK;
            }
            if (jSONField.required()) {
                fieldInfo.required = true;
            }
            String strTrim2 = jSONField.schema().trim();
            if (!strTrim2.isEmpty()) {
                fieldInfo.schema = strTrim2;
            }
            Class<?> clsDeserializeUsing = jSONField.deserializeUsing();
            if (ObjectReader.class.isAssignableFrom(clsDeserializeUsing)) {
                fieldInfo.readUsing = clsDeserializeUsing;
            }
            String strTrim3 = jSONField.arrayToMapKey().trim();
            if (!strTrim3.isEmpty()) {
                fieldInfo.arrayToMapKey = strTrim3;
            }
            Class<?> clsArrayToMapDuplicateHandler = jSONField.arrayToMapDuplicateHandler();
            if (clsArrayToMapDuplicateHandler != Void.class) {
                fieldInfo.arrayToMapDuplicateHandler = clsArrayToMapDuplicateHandler;
            }
            Class<?> clsContentAs = jSONField.contentAs();
            if (clsContentAs != Void.class) {
                fieldInfo.contentAs = clsContentAs;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getBeanInfo1xJSONPOJOBuilder(final BeanInfo beanInfo, final Class<?> cls, final Annotation annotation, Class<? extends Annotation> cls2) {
        BeanUtils.annotationMethods(cls2, new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ObjectReaderBaseModule.lambda$getBeanInfo1xJSONPOJOBuilder$4(annotation, beanInfo, cls, (Method) obj);
            }
        });
    }

    static /* synthetic */ void lambda$getBeanInfo1xJSONPOJOBuilder$4(Annotation annotation, BeanInfo beanInfo, Class cls, Method method) {
        try {
            String name = method.getName();
            int iHashCode = name.hashCode();
            if (iHashCode != 672684058) {
                if (iHashCode != 2068281583) {
                    if (iHashCode == 2092901112 && name.equals("withPrefix")) {
                        String str = (String) method.invoke(annotation, new Object[0]);
                        if (str.isEmpty()) {
                            return;
                        }
                        beanInfo.builderWithPrefix = str;
                        return;
                    }
                    return;
                }
                if (!name.equals("buildMethod")) {
                    return;
                }
            } else if (!name.equals("buildMethodName")) {
                return;
            }
            beanInfo.buildMethod = BeanUtils.buildMethod(cls, (String) method.invoke(annotation, new Object[0]));
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0075 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void getCreator(final com.alibaba.fastjson2.codec.BeanInfo r12, java.lang.Class<?> r13, java.lang.reflect.Constructor r14) {
        /*
            r11 = this;
            boolean r0 = r13.isEnum()
            if (r0 == 0) goto L8
            goto L89
        L8:
            java.lang.annotation.Annotation[] r0 = com.alibaba.fastjson2.util.BeanUtils.getAnnotations(r14)
            int r1 = r0.length
            r2 = 0
            r3 = r2
            r4 = r3
        L10:
            if (r3 >= r1) goto L78
            r5 = r0[r3]
            java.lang.Class r6 = r5.annotationType()
            java.lang.Class<com.alibaba.fastjson2.annotation.JSONCreator> r7 = com.alibaba.fastjson2.annotation.JSONCreator.class
            java.lang.annotation.Annotation r7 = com.alibaba.fastjson2.util.BeanUtils.findAnnotation(r5, r7)
            com.alibaba.fastjson2.annotation.JSONCreator r7 = (com.alibaba.fastjson2.annotation.JSONCreator) r7
            r8 = 1
            if (r7 == 0) goto L30
            java.lang.String[] r4 = r7.parameterNames()
            int r9 = r4.length
            if (r9 == 0) goto L2c
            r12.createParameterNames = r4
        L2c:
            if (r7 != r5) goto L2f
        L2e:
            goto L6c
        L2f:
            r4 = r8
        L30:
            java.lang.String r7 = r6.getName()
            r7.hashCode()
            int r9 = r7.hashCode()
            r10 = -1
            switch(r9) {
                case -2114585843: goto L56;
                case 1394939344: goto L4b;
                case 1418891396: goto L40;
                default: goto L3f;
            }
        L3f:
            goto L60
        L40:
            java.lang.String r9 = "com.alibaba.fastjson2.annotation.JSONCreator"
            boolean r7 = r7.equals(r9)
            if (r7 != 0) goto L49
            goto L60
        L49:
            r10 = 2
            goto L60
        L4b:
            java.lang.String r9 = "com.alibaba.fastjson.annotation.JSONCreator"
            boolean r7 = r7.equals(r9)
            if (r7 != 0) goto L54
            goto L60
        L54:
            r10 = r8
            goto L60
        L56:
            java.lang.String r9 = "com.fasterxml.jackson.annotation.JsonCreator"
            boolean r7 = r7.equals(r9)
            if (r7 != 0) goto L5f
            goto L60
        L5f:
            r10 = r2
        L60:
            switch(r10) {
                case 0: goto L6e;
                case 1: goto L64;
                case 2: goto L64;
                default: goto L63;
            }
        L63:
            goto L75
        L64:
            com.alibaba.fastjson2.reader.ObjectReaderBaseModule$$ExternalSyntheticLambda17 r4 = new com.alibaba.fastjson2.reader.ObjectReaderBaseModule$$ExternalSyntheticLambda17
            r4.<init>()
            com.alibaba.fastjson2.util.BeanUtils.annotationMethods(r6, r4)
        L6c:
            r4 = r8
            goto L75
        L6e:
            boolean r5 = com.alibaba.fastjson2.JSONFactory.isUseJacksonAnnotation()
            if (r5 == 0) goto L75
            goto L2e
        L75:
            int r3 = r3 + 1
            goto L10
        L78:
            if (r4 != 0) goto L7b
            goto L89
        L7b:
            java.lang.Class[] r14 = r14.getParameterTypes()     // Catch: java.lang.NoSuchMethodException -> L84
            java.lang.reflect.Constructor r13 = r13.getDeclaredConstructor(r14)     // Catch: java.lang.NoSuchMethodException -> L84
            goto L85
        L84:
            r13 = 0
        L85:
            if (r13 == 0) goto L89
            r12.creatorConstructor = r13
        L89:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderBaseModule.getCreator(com.alibaba.fastjson2.codec.BeanInfo, java.lang.Class, java.lang.reflect.Constructor):void");
    }

    static /* synthetic */ void lambda$getCreator$5(Annotation annotation, BeanInfo beanInfo, Method method) {
        try {
            if ("parameterNames".equals(method.getName())) {
                String[] strArr = (String[]) method.invoke(annotation, new Object[0]);
                if (strArr.length != 0) {
                    beanInfo.createParameterNames = strArr;
                }
            }
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCreator(final BeanInfo beanInfo, Class<?> cls, Method method) {
        if (method.getDeclaringClass() == Enum.class) {
            return;
        }
        String name = method.getName();
        if (cls.isEnum() && "values".equals(name)) {
            return;
        }
        Annotation[] annotations = BeanUtils.getAnnotations(method);
        int length = annotations.length;
        Method declaredMethod = null;
        int i = 0;
        JSONCreator jSONCreator = null;
        boolean z = false;
        while (i < length) {
            final Annotation annotation = annotations[i];
            Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
            JSONCreator jSONCreator2 = (JSONCreator) BeanUtils.findAnnotation(annotation, JSONCreator.class);
            if (jSONCreator2 != annotation) {
                String name2 = clsAnnotationType.getName();
                name2.hashCode();
                if (name2.equals("com.fasterxml.jackson.annotation.JsonCreator")) {
                    if (JSONFactory.isUseJacksonAnnotation()) {
                        BeanUtils.annotationMethods(clsAnnotationType, new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$$ExternalSyntheticLambda11
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ObjectReaderBaseModule.lambda$getCreator$7(annotation, beanInfo, (Method) obj);
                            }
                        });
                        z = true;
                    }
                } else if (name2.equals("com.alibaba.fastjson.annotation.JSONCreator")) {
                    BeanUtils.annotationMethods(clsAnnotationType, new Consumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderBaseModule$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ObjectReaderBaseModule.lambda$getCreator$6(annotation, beanInfo, (Method) obj);
                        }
                    });
                    z = true;
                }
            }
            i++;
            jSONCreator = jSONCreator2;
        }
        if (jSONCreator != null) {
            String[] strArrParameterNames = jSONCreator.parameterNames();
            if (strArrParameterNames.length != 0) {
                beanInfo.createParameterNames = strArrParameterNames;
            }
            z = true;
        }
        if (z) {
            try {
                declaredMethod = cls.getDeclaredMethod(name, method.getParameterTypes());
            } catch (NoSuchMethodException unused) {
            }
            if (declaredMethod != null) {
                beanInfo.createMethod = declaredMethod;
            }
        }
    }

    static /* synthetic */ void lambda$getCreator$6(Annotation annotation, BeanInfo beanInfo, Method method) {
        try {
            if ("parameterNames".equals(method.getName())) {
                String[] strArr = (String[]) method.invoke(annotation, new Object[0]);
                if (strArr.length != 0) {
                    beanInfo.createParameterNames = strArr;
                }
            }
        } catch (Throwable unused) {
        }
    }

    static /* synthetic */ void lambda$getCreator$7(Annotation annotation, BeanInfo beanInfo, Method method) {
        try {
            if ("parameterNames".equals(method.getName())) {
                String[] strArr = (String[]) method.invoke(annotation, new Object[0]);
                if (strArr.length != 0) {
                    beanInfo.createParameterNames = strArr;
                }
            }
        } catch (Throwable unused) {
        }
    }

    @Override // com.alibaba.fastjson2.modules.ObjectReaderModule
    public ReaderAnnotationProcessor getAnnotationProcessor() {
        return this.annotationProcessor;
    }

    @Override // com.alibaba.fastjson2.modules.ObjectReaderModule
    public void getBeanInfo(BeanInfo beanInfo, Class<?> cls) {
        ReaderAnnotationProcessor readerAnnotationProcessor = this.annotationProcessor;
        if (readerAnnotationProcessor != null) {
            readerAnnotationProcessor.getBeanInfo(beanInfo, cls);
        }
    }

    @Override // com.alibaba.fastjson2.modules.ObjectReaderModule
    public void getFieldInfo(FieldInfo fieldInfo, Class cls, Field field) {
        ReaderAnnotationProcessor readerAnnotationProcessor = this.annotationProcessor;
        if (readerAnnotationProcessor != null) {
            readerAnnotationProcessor.getFieldInfo(fieldInfo, cls, field);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0170  */
    /* JADX WARN: Removed duplicated region for block: B:535:0x0579  */
    /* JADX WARN: Removed duplicated region for block: B:631:0x0699  */
    /* JADX WARN: Removed duplicated region for block: B:733:0x07ce  */
    @Override // com.alibaba.fastjson2.modules.ObjectReaderModule
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.alibaba.fastjson2.reader.ObjectReader getObjectReader(com.alibaba.fastjson2.reader.ObjectReaderProvider r29, java.lang.reflect.Type r30) {
        /*
            Method dump skipped, instruction units count: 3612
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderBaseModule.getObjectReader(com.alibaba.fastjson2.reader.ObjectReaderProvider, java.lang.reflect.Type):com.alibaba.fastjson2.reader.ObjectReader");
    }

    static /* synthetic */ URL lambda$getObjectReader$9(String str) {
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            throw new JSONException("read URL error", e);
        }
    }

    static /* synthetic */ InetAddress lambda$getObjectReader$10(String str) {
        try {
            return InetAddress.getByName(str);
        } catch (UnknownHostException e) {
            throw new JSONException("create address error", e);
        }
    }

    public static ObjectReader typedMap(Class cls, Class cls2, Type type, Type type2) {
        if ((type == null || type == String.class) && type2 == String.class) {
            return new ObjectReaderImplMapString(cls, cls2, 0L);
        }
        return new ObjectReaderImplMapTyped(cls, cls2, type, type2, 0L, null);
    }
}
