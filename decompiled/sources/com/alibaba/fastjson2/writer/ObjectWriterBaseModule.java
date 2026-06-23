package com.alibaba.fastjson2.writer;

import androidx.exifinterface.media.ExifInterface;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONPObject;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONCompiler;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.annotation.JSONType;
import com.alibaba.fastjson2.codec.BeanInfo;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.modules.ObjectWriterAnnotationProcessor;
import com.alibaba.fastjson2.modules.ObjectWriterModule;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import com.alibaba.fastjson2.support.money.MoneySupport;
import com.alibaba.fastjson2.util.ApacheLang3Support;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.JdbcSupport;
import com.alibaba.fastjson2.util.JodaSupport;
import com.alibaba.fastjson2.util.KotlinUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.ObjectWriterBaseModule;
import java.io.File;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/* JADX INFO: loaded from: classes.dex */
public class ObjectWriterBaseModule implements ObjectWriterModule {
    static ObjectWriterAdapter STACK_TRACE_ELEMENT_WRITER;
    final WriterAnnotationProcessor annotationProcessor = new WriterAnnotationProcessor();
    final ObjectWriterProvider provider;

    public ObjectWriterBaseModule(ObjectWriterProvider objectWriterProvider) {
        this.provider = objectWriterProvider;
    }

    @Override // com.alibaba.fastjson2.modules.ObjectWriterModule
    public ObjectWriterProvider getProvider() {
        return this.provider;
    }

    @Override // com.alibaba.fastjson2.modules.ObjectWriterModule
    public ObjectWriterAnnotationProcessor getAnnotationProcessor() {
        return this.annotationProcessor;
    }

    public class WriterAnnotationProcessor implements ObjectWriterAnnotationProcessor {
        public WriterAnnotationProcessor() {
        }

        @Override // com.alibaba.fastjson2.modules.ObjectWriterAnnotationProcessor
        public void getBeanInfo(final BeanInfo beanInfo, Class cls) {
            Class cls2;
            boolean zIsUseJacksonAnnotation;
            String str;
            if (cls != null) {
                Class superclass = cls.getSuperclass();
                if (superclass != Object.class && superclass != null && superclass != Enum.class) {
                    getBeanInfo(beanInfo, superclass);
                }
                for (Class<?> cls3 : cls.getInterfaces()) {
                    if (cls3 != Serializable.class) {
                        getBeanInfo(beanInfo, cls3);
                    }
                }
                if (beanInfo.seeAlso != null && beanInfo.seeAlsoNames != null) {
                    int i = 0;
                    while (true) {
                        if (i >= beanInfo.seeAlso.length) {
                            break;
                        }
                        if (beanInfo.seeAlso[i] == cls && i < beanInfo.seeAlsoNames.length && (str = beanInfo.seeAlsoNames[i]) != null && str.length() != 0) {
                            beanInfo.typeName = str;
                            break;
                        }
                        i++;
                    }
                }
            }
            Annotation[] annotations = BeanUtils.getAnnotations(cls);
            JSONType jSONType = null;
            int i2 = 0;
            final Annotation annotation = null;
            while (true) {
                if (i2 < annotations.length) {
                    Annotation annotation2 = annotations[i2];
                    Class<? extends Annotation> clsAnnotationType = annotation2.annotationType();
                    if (jSONType == null) {
                        jSONType = (JSONType) BeanUtils.findAnnotation(annotation2, JSONType.class);
                    }
                    if (jSONType != annotation2) {
                        if (clsAnnotationType == JSONCompiler.class && ((JSONCompiler) annotation2).value() == JSONCompiler.CompilerOption.LAMBDA) {
                            beanInfo.writerFeatures |= FieldInfo.JIT;
                        }
                        zIsUseJacksonAnnotation = JSONFactory.isUseJacksonAnnotation();
                        String name = clsAnnotationType.getName();
                        name.hashCode();
                        switch (name) {
                            case "com.fasterxml.jackson.annotation.JsonInclude":
                                if (!zIsUseJacksonAnnotation) {
                                    break;
                                } else {
                                    BeanUtils.processJacksonJsonInclude(beanInfo, annotation2);
                                    break;
                                }
                                break;
                            case "com.fasterxml.jackson.databind.annotation.JsonSerialize":
                                if (!zIsUseJacksonAnnotation) {
                                    break;
                                } else {
                                    processJacksonJsonSerialize(beanInfo, annotation2);
                                    if (beanInfo.serializer != null && Enum.class.isAssignableFrom(cls)) {
                                        beanInfo.writeEnumAsJavaBean = true;
                                    }
                                    break;
                                }
                                break;
                            case "com.fasterxml.jackson.annotation.JsonFormat":
                                if (!zIsUseJacksonAnnotation) {
                                    break;
                                } else {
                                    BeanUtils.processJacksonJsonFormat(beanInfo, annotation2);
                                    break;
                                }
                                break;
                            case "com.fasterxml.jackson.annotation.JsonSubTypes":
                                if (!zIsUseJacksonAnnotation) {
                                    break;
                                } else {
                                    processJacksonJsonSubTypes(beanInfo, annotation2);
                                    break;
                                }
                                break;
                            case "com.fasterxml.jackson.annotation.JsonPropertyOrder":
                                if (!zIsUseJacksonAnnotation) {
                                    break;
                                } else {
                                    processJacksonJsonPropertyOrder(beanInfo, annotation2);
                                    break;
                                }
                                break;
                            case "com.fasterxml.jackson.annotation.JsonTypeInfo":
                                if (!zIsUseJacksonAnnotation) {
                                    break;
                                } else {
                                    processJacksonJsonTypeInfo(beanInfo, annotation2);
                                    break;
                                }
                                break;
                            case "com.fasterxml.jackson.annotation.JsonTypeName":
                                if (!zIsUseJacksonAnnotation) {
                                    break;
                                } else {
                                    BeanUtils.processJacksonJsonTypeName(beanInfo, annotation2);
                                    break;
                                }
                                break;
                            case "com.fasterxml.jackson.annotation.JsonIgnoreProperties":
                                if (!zIsUseJacksonAnnotation) {
                                    break;
                                } else {
                                    processJacksonJsonIgnoreProperties(beanInfo, annotation2);
                                    break;
                                }
                                break;
                            case "kotlin.Metadata":
                                beanInfo.f4kotlin = true;
                                KotlinUtils.getConstructor(cls, beanInfo);
                                break;
                            case "com.alibaba.fastjson.annotation.JSONType":
                                annotation = annotation2;
                                break;
                        }
                    }
                    i2++;
                } else {
                    if (jSONType == null && (cls2 = ObjectWriterBaseModule.this.provider.mixInCache.get(cls)) != null) {
                        beanInfo.mixIn = true;
                        Annotation[] annotations2 = BeanUtils.getAnnotations(cls2);
                        int i3 = 0;
                        while (i3 < annotations2.length) {
                            Annotation annotation3 = annotations2[i3];
                            Class<? extends Annotation> clsAnnotationType2 = annotation3.annotationType();
                            JSONType jSONType2 = (JSONType) BeanUtils.findAnnotation(annotation3, JSONType.class);
                            if (jSONType2 != annotation3 && "com.alibaba.fastjson.annotation.JSONType".equals(clsAnnotationType2.getName())) {
                                annotation = annotation3;
                            }
                            i3++;
                            jSONType = jSONType2;
                        }
                    }
                    if (jSONType != null) {
                        Class<?>[] clsArrSeeAlso = jSONType.seeAlso();
                        if (clsArrSeeAlso.length != 0) {
                            beanInfo.seeAlso = clsArrSeeAlso;
                        }
                        String strTypeKey = jSONType.typeKey();
                        if (!strTypeKey.isEmpty()) {
                            beanInfo.typeKey = strTypeKey;
                        }
                        String strTypeName = jSONType.typeName();
                        if (!strTypeName.isEmpty()) {
                            beanInfo.typeName = strTypeName;
                        }
                        for (JSONWriter.Feature feature : jSONType.serializeFeatures()) {
                            beanInfo.writerFeatures = feature.mask | beanInfo.writerFeatures;
                        }
                        beanInfo.namingStrategy = jSONType.naming().name();
                        String[] strArrIgnores = jSONType.ignores();
                        if (strArrIgnores.length > 0) {
                            beanInfo.ignores = strArrIgnores;
                        }
                        String[] strArrIncludes = jSONType.includes();
                        if (strArrIncludes.length > 0) {
                            beanInfo.includes = strArrIncludes;
                        }
                        String[] strArrOrders = jSONType.orders();
                        if (strArrOrders.length > 0) {
                            beanInfo.orders = strArrOrders;
                        }
                        Class<?> clsSerializer = jSONType.serializer();
                        if (ObjectWriter.class.isAssignableFrom(clsSerializer)) {
                            beanInfo.serializer = clsSerializer;
                            beanInfo.writeEnumAsJavaBean = true;
                        }
                        Class<? extends Filter>[] clsArrSerializeFilters = jSONType.serializeFilters();
                        if (clsArrSerializeFilters.length != 0) {
                            beanInfo.serializeFilters = clsArrSerializeFilters;
                        }
                        String str2 = jSONType.format();
                        if (!str2.isEmpty()) {
                            beanInfo.format = str2;
                        }
                        String strLocale = jSONType.locale();
                        if (!strLocale.isEmpty()) {
                            String[] strArrSplit = strLocale.split("_");
                            if (strArrSplit.length == 2) {
                                beanInfo.locale = new Locale(strArrSplit[0], strArrSplit[1]);
                            }
                        }
                        if (!jSONType.alphabetic()) {
                            beanInfo.alphabetic = false;
                        }
                        if (jSONType.writeEnumAsJavaBean()) {
                            beanInfo.writeEnumAsJavaBean = true;
                        }
                        String strRootName = jSONType.rootName();
                        if (!strRootName.isEmpty()) {
                            beanInfo.rootName = strRootName;
                        }
                        if (beanInfo.skipTransient) {
                            beanInfo.skipTransient = jSONType.skipTransient();
                        }
                    } else if (annotation != null) {
                        BeanUtils.annotationMethods(annotation.annotationType(), new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor$$ExternalSyntheticLambda4
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                BeanUtils.processJSONType1x(beanInfo, annotation, (Method) obj);
                            }
                        });
                    }
                    if (beanInfo.seeAlso == null || beanInfo.seeAlso.length == 0) {
                        return;
                    }
                    if (beanInfo.typeName == null || beanInfo.typeName.length() == 0) {
                        for (Class cls4 : beanInfo.seeAlso) {
                            if (cls4 == cls) {
                                beanInfo.typeName = cls.getSimpleName();
                                return;
                            }
                        }
                        return;
                    }
                    return;
                }
            }
        }

        @Override // com.alibaba.fastjson2.modules.ObjectWriterAnnotationProcessor
        public void getFieldInfo(BeanInfo beanInfo, FieldInfo fieldInfo, Class cls, Field field) {
            boolean zIsUseJacksonAnnotation;
            Class cls2;
            Field declaredField;
            JSONField jSONField = null;
            if (cls != null && (cls2 = ObjectWriterBaseModule.this.provider.mixInCache.get(cls)) != null && cls2 != cls) {
                try {
                    declaredField = cls2.getDeclaredField(field.getName());
                } catch (Exception unused) {
                    declaredField = null;
                }
                if (declaredField != null) {
                    getFieldInfo(beanInfo, fieldInfo, cls2, declaredField);
                }
            }
            if (ObjectWriterBaseModule.this.provider.mixInCache.get(field.getType()) != null) {
                fieldInfo.fieldClassMixIn = true;
            }
            if (Modifier.isTransient(field.getModifiers())) {
                fieldInfo.isTransient = true;
                if (fieldInfo.skipTransient && beanInfo.skipTransient) {
                    fieldInfo.ignore = true;
                }
            }
            Annotation[] annotations = BeanUtils.getAnnotations(field);
            if (annotations.length == 0 && KotlinUtils.isKotlin(cls)) {
                annotations = BeanUtils.getAnnotations(field.getType());
                Constructor kotlinConstructor = KotlinUtils.getKotlinConstructor(BeanUtils.getConstructor(cls));
                if (kotlinConstructor != null) {
                    String[] koltinConstructorParameters = KotlinUtils.getKoltinConstructorParameters(cls);
                    int i = 0;
                    while (true) {
                        if (i >= koltinConstructorParameters.length) {
                            break;
                        }
                        if (koltinConstructorParameters[i].equals(field.getName())) {
                            annotations = kotlinConstructor.getParameterAnnotations()[i];
                            break;
                        }
                        i++;
                    }
                    if (fieldInfo.ignore) {
                        for (Annotation annotation : annotations) {
                            if (annotation.annotationType() == JSONField.class) {
                                fieldInfo.ignore = !((JSONField) r9).serialize();
                            }
                        }
                    }
                }
            }
            for (Annotation annotation2 : annotations) {
                Class<? extends Annotation> clsAnnotationType = annotation2.annotationType();
                if (jSONField != null || (jSONField = (JSONField) BeanUtils.findAnnotation(annotation2, JSONField.class)) != annotation2) {
                    String name = clsAnnotationType.getName();
                    zIsUseJacksonAnnotation = JSONFactory.isUseJacksonAnnotation();
                    name.hashCode();
                    switch (name) {
                        case "com.google.gson.annotations.SerializedName":
                            if (JSONFactory.isUseGsonAnnotation()) {
                                BeanUtils.processGsonSerializedName(fieldInfo, annotation2);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonInclude":
                            if (zIsUseJacksonAnnotation) {
                                BeanUtils.processJacksonJsonInclude(beanInfo, annotation2);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonManagedReference":
                            if (zIsUseJacksonAnnotation) {
                                fieldInfo.features |= JSONWriter.Feature.ReferenceDetection.mask;
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.databind.annotation.JsonSerialize":
                            if (zIsUseJacksonAnnotation) {
                                processJacksonJsonSerialize(fieldInfo, annotation2);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonFormat":
                            if (zIsUseJacksonAnnotation) {
                                BeanUtils.processJacksonJsonFormat(fieldInfo, annotation2);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonIgnore":
                            if (zIsUseJacksonAnnotation) {
                                BeanUtils.processJacksonJsonIgnore(fieldInfo, annotation2);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonValue":
                            if (zIsUseJacksonAnnotation) {
                                fieldInfo.features |= FieldInfo.VALUE_MASK;
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonAnyGetter":
                            if (zIsUseJacksonAnnotation) {
                                fieldInfo.features |= FieldInfo.UNWRAPPED_MASK;
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonProperty":
                            if (zIsUseJacksonAnnotation) {
                                processJacksonJsonProperty(fieldInfo, annotation2);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.alibaba.fastjson.annotation.JSONField":
                            processJSONField1x(fieldInfo, annotation2);
                            break;
                        case "com.fasterxml.jackson.annotation.JsonBackReference":
                            if (zIsUseJacksonAnnotation) {
                                fieldInfo.features |= FieldInfo.BACKR_EFERENCE;
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonRawValue":
                            if (zIsUseJacksonAnnotation) {
                                fieldInfo.features = FieldInfo.RAW_VALUE_MASK | fieldInfo.features;
                                break;
                            } else {
                                break;
                            }
                            break;
                    }
                }
            }
            if (jSONField == null) {
                return;
            }
            loadFieldInfo(fieldInfo, jSONField);
            Class<?> clsWriteUsing = jSONField.writeUsing();
            if (ObjectWriter.class.isAssignableFrom(clsWriteUsing)) {
                fieldInfo.writeUsing = clsWriteUsing;
            }
            Class<?> clsSerializeUsing = jSONField.serializeUsing();
            if (ObjectWriter.class.isAssignableFrom(clsSerializeUsing)) {
                fieldInfo.writeUsing = clsSerializeUsing;
            }
            if (jSONField.jsonDirect()) {
                fieldInfo.features |= FieldInfo.RAW_VALUE_MASK;
            }
            if ((fieldInfo.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) == 0 || String.class.equals(field.getType()) || fieldInfo.writeUsing != null) {
                return;
            }
            fieldInfo.writeUsing = ObjectWriterImplToString.class;
        }

        private void processJacksonJsonSubTypes(final BeanInfo beanInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectWriterBaseModule.WriterAnnotationProcessor.lambda$processJacksonJsonSubTypes$1(annotation, beanInfo, (Method) obj);
                }
            });
        }

        static /* synthetic */ void lambda$processJacksonJsonSubTypes$1(Annotation annotation, BeanInfo beanInfo, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                if ("value".equals(name)) {
                    Annotation[] annotationArr = (Annotation[]) objInvoke;
                    if (annotationArr.length != 0) {
                        beanInfo.seeAlso = new Class[annotationArr.length];
                        beanInfo.seeAlsoNames = new String[annotationArr.length];
                        for (int i = 0; i < annotationArr.length; i++) {
                            BeanUtils.processJacksonJsonSubTypesType(beanInfo, i, annotationArr[i]);
                        }
                    }
                }
            } catch (Throwable unused) {
            }
        }

        private void processJacksonJsonSerialize(final BeanInfo beanInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m63x32eb22ba(annotation, beanInfo, (Method) obj);
                }
            });
        }

        /* JADX INFO: renamed from: lambda$processJacksonJsonSerialize$2$com-alibaba-fastjson2-writer-ObjectWriterBaseModule$WriterAnnotationProcessor, reason: not valid java name */
        /* synthetic */ void m63x32eb22ba(Annotation annotation, BeanInfo beanInfo, Method method) {
            Class clsProcessUsing;
            Class clsProcessUsing2;
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                int iHashCode = name.hashCode();
                if (iHashCode == 111582340) {
                    if (!name.equals("using") || (clsProcessUsing = processUsing((Class) objInvoke)) == null) {
                        return;
                    }
                    beanInfo.serializer = clsProcessUsing;
                    return;
                }
                if (iHashCode == 491860325 && name.equals("keyUsing") && (clsProcessUsing2 = processUsing((Class) objInvoke)) != null) {
                    beanInfo.serializer = clsProcessUsing2;
                }
            } catch (Throwable unused) {
            }
        }

        private Class processUsing(Class cls) {
            String name = cls.getName();
            if (!"com.fasterxml.jackson.databind.JsonSerializer$None".equals(name) && ObjectWriter.class.isAssignableFrom(cls)) {
                return cls;
            }
            if ("com.fasterxml.jackson.databind.ser.std.ToStringSerializer".equals(name)) {
                return ObjectWriterImplToString.class;
            }
            return null;
        }

        private void processJacksonJsonTypeInfo(final BeanInfo beanInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectWriterBaseModule.WriterAnnotationProcessor.lambda$processJacksonJsonTypeInfo$3(annotation, beanInfo, (Method) obj);
                }
            });
        }

        static /* synthetic */ void lambda$processJacksonJsonTypeInfo$3(Annotation annotation, BeanInfo beanInfo, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                if ("property".equals(name)) {
                    String str = (String) objInvoke;
                    if (str.isEmpty()) {
                        return;
                    }
                    beanInfo.typeKey = str;
                    beanInfo.writerFeatures |= JSONWriter.Feature.WriteClassName.mask;
                }
            } catch (Throwable unused) {
            }
        }

        private void processJacksonJsonPropertyOrder(final BeanInfo beanInfo, final Annotation annotation) {
            Class<?> cls = annotation.getClass();
            final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            BeanUtils.annotationMethods(cls, new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectWriterBaseModule.WriterAnnotationProcessor.lambda$processJacksonJsonPropertyOrder$4(annotation, beanInfo, atomicBoolean, (Method) obj);
                }
            });
            if (beanInfo.orders == null || beanInfo.orders.length == 0) {
                beanInfo.alphabetic = atomicBoolean.get();
            }
        }

        static /* synthetic */ void lambda$processJacksonJsonPropertyOrder$4(Annotation annotation, BeanInfo beanInfo, AtomicBoolean atomicBoolean, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                if ("value".equals(name)) {
                    String[] strArr = (String[]) objInvoke;
                    if (strArr.length != 0) {
                        beanInfo.orders = strArr;
                        return;
                    }
                    return;
                }
                if ("alphabetic".equals(name)) {
                    atomicBoolean.set(((Boolean) objInvoke).booleanValue());
                }
            } catch (Throwable unused) {
            }
        }

        private void processJacksonJsonSerialize(final FieldInfo fieldInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m64x4865c97d(annotation, fieldInfo, (Method) obj);
                }
            });
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        /* JADX INFO: renamed from: lambda$processJacksonJsonSerialize$5$com-alibaba-fastjson2-writer-ObjectWriterBaseModule$WriterAnnotationProcessor, reason: not valid java name */
        /* synthetic */ void m64x4865c97d(Annotation annotation, FieldInfo fieldInfo, Method method) {
            Class<?> cls;
            Class<?> clsProcessUsing;
            Class<?> clsProcessUsing2;
            Class<?> clsProcessUsing3;
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                switch (name.hashCode()) {
                    case -407108981:
                        if (name.equals("contentAs") && (cls = (Class) objInvoke) != Void.class) {
                            fieldInfo.contentAs = cls;
                            break;
                        }
                        break;
                    case 111582340:
                        if (name.equals("using") && (clsProcessUsing = processUsing((Class) objInvoke)) != null) {
                            fieldInfo.writeUsing = clsProcessUsing;
                            break;
                        }
                        break;
                    case 491860325:
                        if (name.equals("keyUsing") && (clsProcessUsing2 = processUsing((Class) objInvoke)) != null) {
                            fieldInfo.keyUsing = clsProcessUsing2;
                            break;
                        }
                        break;
                    case 2034063763:
                        if (name.equals("valueUsing") && (clsProcessUsing3 = processUsing((Class) objInvoke)) != null) {
                            fieldInfo.valueUsing = clsProcessUsing3;
                            break;
                        }
                        break;
                }
            } catch (Throwable unused) {
            }
        }

        private void processJacksonJsonProperty(final FieldInfo fieldInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectWriterBaseModule.WriterAnnotationProcessor.lambda$processJacksonJsonProperty$6(annotation, fieldInfo, (Method) obj);
                }
            });
        }

        static /* synthetic */ void lambda$processJacksonJsonProperty$6(Annotation annotation, FieldInfo fieldInfo, Method method) {
            int iIntValue;
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                int iHashCode = name.hashCode();
                if (iHashCode == -1423461020) {
                    if (name.equals("access")) {
                        fieldInfo.ignore = "WRITE_ONLY".equals(((Enum) objInvoke).name());
                        return;
                    }
                    return;
                }
                if (iHashCode == 100346066) {
                    if (!name.equals("index") || (iIntValue = ((Integer) objInvoke).intValue()) == -1) {
                        return;
                    }
                    fieldInfo.ordinal = iIntValue;
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

        private void processJacksonJsonIgnoreProperties(final BeanInfo beanInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectWriterBaseModule.WriterAnnotationProcessor.lambda$processJacksonJsonIgnoreProperties$7(annotation, beanInfo, (Method) obj);
                }
            });
        }

        static /* synthetic */ void lambda$processJacksonJsonIgnoreProperties$7(Annotation annotation, BeanInfo beanInfo, Method method) {
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                if ("value".equals(name)) {
                    String[] strArr = (String[]) objInvoke;
                    if (strArr.length != 0) {
                        beanInfo.ignores = strArr;
                    }
                }
            } catch (Throwable unused) {
            }
        }

        private void processJSONField1x(final FieldInfo fieldInfo, final Annotation annotation) {
            BeanUtils.annotationMethods(annotation.getClass(), new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m62xed6a8b50(annotation, fieldInfo, (Method) obj);
                }
            });
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        /* JADX INFO: renamed from: lambda$processJSONField1x$8$com-alibaba-fastjson2-writer-ObjectWriterBaseModule$WriterAnnotationProcessor, reason: not valid java name */
        /* synthetic */ void m62xed6a8b50(Annotation annotation, FieldInfo fieldInfo, Method method) {
            int iIntValue;
            String name = method.getName();
            try {
                Object objInvoke = method.invoke(annotation, new Object[0]);
                switch (name.hashCode()) {
                    case -1371565692:
                        if (name.equals("serializeUsing")) {
                            Class<?> cls = (Class) objInvoke;
                            if (ObjectWriter.class.isAssignableFrom(cls)) {
                                fieldInfo.writeUsing = cls;
                            }
                        }
                        break;
                    case -1268779017:
                        if (name.equals("format")) {
                            loadJsonFieldFormat(fieldInfo, (String) objInvoke);
                        }
                        break;
                    case -1206994319:
                        if (name.equals("ordinal") && (iIntValue = ((Integer) objInvoke).intValue()) != 0) {
                            fieldInfo.ordinal = iIntValue;
                            break;
                        }
                        break;
                    case -987658292:
                        if (name.equals("unwrapped") && ((Boolean) objInvoke).booleanValue()) {
                            fieldInfo.features |= FieldInfo.UNWRAPPED_MASK;
                            break;
                        }
                        break;
                    case -940893828:
                        if (name.equals("serialzeFeatures")) {
                            applyFeatures(fieldInfo, (Enum[]) objInvoke);
                        }
                        break;
                    case -659125328:
                        if (name.equals("defaultValue")) {
                            String str = (String) objInvoke;
                            if (!str.isEmpty()) {
                                fieldInfo.defaultValue = str;
                            }
                        }
                        break;
                    case -573479200:
                        if (name.equals("serialize") && !((Boolean) objInvoke).booleanValue()) {
                            fieldInfo.ignore = true;
                            break;
                        }
                        break;
                    case 3373707:
                        if (name.equals("name")) {
                            String str2 = (String) objInvoke;
                            if (!str2.isEmpty()) {
                                fieldInfo.fieldName = str2;
                            }
                        }
                        break;
                    case 12396273:
                        if (name.equals("jsonDirect") && ((Boolean) objInvoke).booleanValue()) {
                            fieldInfo.features |= FieldInfo.RAW_VALUE_MASK;
                            break;
                        }
                        break;
                    case 102727412:
                        if (name.equals("label")) {
                            String str3 = (String) objInvoke;
                            if (!str3.isEmpty()) {
                                fieldInfo.label = str3;
                            }
                        }
                        break;
                }
            } catch (Throwable unused) {
            }
        }

        private void applyFeatures(FieldInfo fieldInfo, Enum[] enumArr) {
            for (Enum r0 : enumArr) {
                String strName = r0.name();
                strName.hashCode();
                switch (strName) {
                    case "DisableCircularReferenceDetect":
                        fieldInfo.features |= FieldInfo.DISABLE_REFERENCE_DETECT;
                        break;
                    case "WriteNullNumberAsZero":
                        fieldInfo.features |= JSONWriter.Feature.WriteNullNumberAsZero.mask;
                        break;
                    case "IgnoreErrorGetter":
                        fieldInfo.features |= JSONWriter.Feature.IgnoreErrorGetter.mask;
                        break;
                    case "UseISO8601DateFormat":
                        fieldInfo.format = "iso8601";
                        break;
                    case "WriteBigDecimalAsPlain":
                        fieldInfo.features |= JSONWriter.Feature.WriteBigDecimalAsPlain.mask;
                        break;
                    case "WriteEnumUsingToString":
                        fieldInfo.features |= JSONWriter.Feature.WriteEnumUsingToString.mask;
                        break;
                    case "BrowserCompatible":
                        fieldInfo.features |= JSONWriter.Feature.BrowserCompatible.mask;
                        break;
                    case "WriteNullStringAsEmpty":
                        fieldInfo.features |= JSONWriter.Feature.WriteNullStringAsEmpty.mask;
                        break;
                    case "NotWriteRootClassName":
                        fieldInfo.features |= JSONWriter.Feature.NotWriteRootClassName.mask;
                        break;
                    case "WriteNullListAsEmpty":
                        fieldInfo.features |= JSONWriter.Feature.WriteNullListAsEmpty.mask;
                        break;
                    case "WriteNonStringValueAsString":
                        fieldInfo.features |= JSONWriter.Feature.WriteNonStringValueAsString.mask;
                        break;
                    case "WriteNullBooleanAsFalse":
                        fieldInfo.features |= JSONWriter.Feature.WriteNullBooleanAsFalse.mask;
                        break;
                    case "WriteClassName":
                        fieldInfo.features |= JSONWriter.Feature.WriteClassName.mask;
                        break;
                    case "WriteMapNullValue":
                        fieldInfo.features |= JSONWriter.Feature.WriteNulls.mask;
                        break;
                }
            }
        }

        @Override // com.alibaba.fastjson2.modules.ObjectWriterAnnotationProcessor
        public void getFieldInfo(BeanInfo beanInfo, FieldInfo fieldInfo, Class cls, Method method) {
            Field field;
            Method declaredMethod;
            Class cls2 = ObjectWriterBaseModule.this.provider.mixInCache.get(cls);
            String name = method.getName();
            if ("getTargetSql".equals(name) && cls != null && cls.getName().startsWith("com.baomidou.mybatisplus.")) {
                fieldInfo.features |= JSONWriter.Feature.IgnoreErrorGetter.mask;
            }
            if (cls2 != null && cls2 != cls) {
                try {
                    declaredMethod = cls2.getDeclaredMethod(name, method.getParameterTypes());
                } catch (Exception unused) {
                    declaredMethod = null;
                }
                if (declaredMethod != null) {
                    getFieldInfo(beanInfo, fieldInfo, cls2, declaredMethod);
                }
            }
            if (ObjectWriterBaseModule.this.provider.mixInCache.get(method.getReturnType()) != null) {
                fieldInfo.fieldClassMixIn = true;
            }
            if (JDKUtils.CLASS_TRANSIENT != null && method.getAnnotation(JDKUtils.CLASS_TRANSIENT) != null) {
                fieldInfo.ignore = true;
                fieldInfo.isTransient = true;
                if (!beanInfo.skipTransient) {
                    fieldInfo.skipTransient = false;
                    fieldInfo.ignore = false;
                }
            }
            if (cls != null) {
                Class superclass = cls.getSuperclass();
                Method method2 = BeanUtils.getMethod(superclass, method);
                boolean z = fieldInfo.ignore;
                if (method2 != null) {
                    getFieldInfo(beanInfo, fieldInfo, superclass, method2);
                    Field field2 = BeanUtils.getField(cls, method);
                    int modifiers = method2.getModifiers();
                    if (field2 != null && z != fieldInfo.ignore && !Modifier.isAbstract(modifiers) && !method2.equals(method)) {
                        fieldInfo.ignore = z;
                    }
                }
                for (Class<?> cls3 : cls.getInterfaces()) {
                    Method method3 = BeanUtils.getMethod(cls3, method);
                    if (superclass != null && method3 != null) {
                        getFieldInfo(beanInfo, fieldInfo, superclass, method3);
                    }
                }
            }
            fieldInfo.isPrivate = false;
            processAnnotations(fieldInfo, BeanUtils.getAnnotations(method));
            if (!cls.getName().startsWith("java.lang") && !BeanUtils.isRecord(cls) && (field = BeanUtils.getField(cls, method)) != null) {
                fieldInfo.features |= FieldInfo.FIELD_MASK;
                getFieldInfo(beanInfo, fieldInfo, cls, field);
            }
            if (!beanInfo.f4kotlin || beanInfo.creatorConstructor == null || beanInfo.createParameterNames == null) {
                return;
            }
            String str = BeanUtils.getterName(method, beanInfo.f4kotlin, null);
            for (int i = 0; i < beanInfo.createParameterNames.length; i++) {
                if (str.equals(beanInfo.createParameterNames[i])) {
                    Annotation[][] parameterAnnotations = beanInfo.creatorConstructor.getParameterAnnotations();
                    if (i < parameterAnnotations.length) {
                        processAnnotations(fieldInfo, parameterAnnotations[i]);
                        return;
                    }
                }
            }
        }

        private void processAnnotations(FieldInfo fieldInfo, Annotation[] annotationArr) {
            boolean zIsUseJacksonAnnotation;
            for (Annotation annotation : annotationArr) {
                Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
                JSONField jSONField = (JSONField) BeanUtils.findAnnotation(annotation, JSONField.class);
                Objects.nonNull(jSONField);
                if (jSONField != null) {
                    loadFieldInfo(fieldInfo, jSONField);
                } else {
                    if (clsAnnotationType == JSONCompiler.class && ((JSONCompiler) annotation).value() == JSONCompiler.CompilerOption.LAMBDA) {
                        fieldInfo.features |= FieldInfo.JIT;
                    }
                    zIsUseJacksonAnnotation = JSONFactory.isUseJacksonAnnotation();
                    String name = clsAnnotationType.getName();
                    name.hashCode();
                    switch (name) {
                        case "com.fasterxml.jackson.annotation.JsonInclude":
                            if (zIsUseJacksonAnnotation) {
                                BeanUtils.processJacksonJsonInclude(fieldInfo, annotation);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.databind.annotation.JsonSerialize":
                            if (zIsUseJacksonAnnotation) {
                                processJacksonJsonSerialize(fieldInfo, annotation);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonFormat":
                            if (zIsUseJacksonAnnotation) {
                                BeanUtils.processJacksonJsonFormat(fieldInfo, annotation);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonIgnore":
                            if (zIsUseJacksonAnnotation) {
                                BeanUtils.processJacksonJsonIgnore(fieldInfo, annotation);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonValue":
                            if (zIsUseJacksonAnnotation) {
                                fieldInfo.features |= FieldInfo.VALUE_MASK;
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonAnyGetter":
                            if (zIsUseJacksonAnnotation) {
                                fieldInfo.features |= FieldInfo.UNWRAPPED_MASK;
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonProperty":
                            if (zIsUseJacksonAnnotation) {
                                processJacksonJsonProperty(fieldInfo, annotation);
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "java.beans.Transient":
                            if (fieldInfo.skipTransient) {
                                fieldInfo.ignore = true;
                            }
                            fieldInfo.isTransient = true;
                            break;
                        case "com.alibaba.fastjson.annotation.JSONField":
                            processJSONField1x(fieldInfo, annotation);
                            break;
                        case "com.fasterxml.jackson.annotation.JsonRawValue":
                            if (zIsUseJacksonAnnotation) {
                                fieldInfo.features |= FieldInfo.RAW_VALUE_MASK;
                                break;
                            } else {
                                break;
                            }
                            break;
                        case "com.fasterxml.jackson.annotation.JsonUnwrapped":
                            if (zIsUseJacksonAnnotation) {
                                BeanUtils.processJacksonJsonUnwrapped(fieldInfo, annotation);
                                break;
                            } else {
                                break;
                            }
                            break;
                    }
                }
            }
        }

        private void loadFieldInfo(FieldInfo fieldInfo, JSONField jSONField) {
            String strName = jSONField.name();
            if (!strName.isEmpty()) {
                fieldInfo.fieldName = strName;
            }
            String strDefaultValue = jSONField.defaultValue();
            if (!strDefaultValue.isEmpty()) {
                fieldInfo.defaultValue = strDefaultValue;
            }
            loadJsonFieldFormat(fieldInfo, jSONField.format());
            String strLabel = jSONField.label();
            if (!strLabel.isEmpty()) {
                fieldInfo.label = strLabel;
            }
            String strLocale = jSONField.locale();
            if (!strLocale.isEmpty()) {
                String[] strArrSplit = strLocale.split("_");
                if (strArrSplit.length == 2) {
                    fieldInfo.locale = new Locale(strArrSplit[0], strArrSplit[1]);
                }
            }
            boolean zSerialize = jSONField.serialize();
            boolean z = !zSerialize;
            if (!fieldInfo.ignore) {
                fieldInfo.ignore = z;
            }
            if (!jSONField.skipTransient()) {
                fieldInfo.skipTransient = false;
                if (fieldInfo.isTransient && !fieldInfo.isPrivate) {
                    fieldInfo.ignore = false;
                }
            }
            if (jSONField.unwrapped()) {
                fieldInfo.features |= FieldInfo.UNWRAPPED_MASK;
            }
            for (JSONWriter.Feature feature : jSONField.serializeFeatures()) {
                fieldInfo.features |= feature.mask;
                if (fieldInfo.ignore && !fieldInfo.isTransient && zSerialize && feature == JSONWriter.Feature.FieldBased) {
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
            if (jSONField.jsonDirect()) {
                fieldInfo.features |= FieldInfo.RAW_VALUE_MASK;
            }
            Class<?> clsSerializeUsing = jSONField.serializeUsing();
            if (ObjectWriter.class.isAssignableFrom(clsSerializeUsing)) {
                fieldInfo.writeUsing = clsSerializeUsing;
            }
            Class<?> clsContentAs = jSONField.contentAs();
            if (clsContentAs != Void.class) {
                fieldInfo.contentAs = clsContentAs;
            }
        }

        private void loadJsonFieldFormat(FieldInfo fieldInfo, String str) {
            if (str.isEmpty()) {
                return;
            }
            String strTrim = str.trim();
            if (strTrim.indexOf(84) != -1 && !strTrim.contains("'T'")) {
                strTrim = strTrim.replace(ExifInterface.GPS_DIRECTION_TRUE, "'T'");
            }
            if (strTrim.isEmpty()) {
                return;
            }
            fieldInfo.format = strTrim;
        }
    }

    ObjectWriter getExternalObjectWriter(String str, Class cls) {
        str.hashCode();
        switch (str) {
            case "org.joda.time.LocalDate":
                return JodaSupport.createLocalDateWriter(cls, null);
            case "org.joda.time.chrono.GregorianChronology":
                return JodaSupport.createGregorianChronologyWriter(cls);
            case "java.sql.Time":
                return JdbcSupport.createTimeWriter(null);
            case "org.joda.time.chrono.ISOChronology":
                return JodaSupport.createISOChronologyWriter(cls);
            case "org.joda.time.DateTime":
                return new ObjectWriterImplZonedDateTime(null, null, new JodaSupport.DateTime2ZDT());
            case "java.sql.Timestamp":
                return JdbcSupport.createTimestampWriter(cls, null);
            case "org.joda.time.LocalDateTime":
                return JodaSupport.createLocalDateTimeWriter(cls, null);
            default:
                if (JdbcSupport.isClob(cls)) {
                    return JdbcSupport.createClobWriter(cls);
                }
                return null;
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.alibaba.fastjson2.modules.ObjectWriterModule
    public ObjectWriter getObjectWriter(Type type, Class cls) {
        Class cls2;
        ObjectWriter objectWriterCreateEnumWriter;
        Class<?> mapping;
        Type type2 = type;
        if (type2 == String.class) {
            return ObjectWriterImplString.INSTANCE;
        }
        if (cls == null) {
            if (type2 instanceof Class) {
                mapping = (Class) type2;
            } else {
                mapping = TypeUtils.getMapping(type2);
            }
            cls2 = mapping;
        } else {
            cls2 = cls;
        }
        String name = cls2.getName();
        ObjectWriter externalObjectWriter = getExternalObjectWriter(name, cls2);
        if (externalObjectWriter != null) {
            return externalObjectWriter;
        }
        name.hashCode();
        byte b = -1;
        switch (name.hashCode()) {
            case -2088293497:
                if (name.equals("java.awt.Color")) {
                    b = 0;
                }
                break;
            case -2037224663:
                if (name.equals("java.util.regex.Pattern")) {
                    b = 1;
                }
                break;
            case -1786540538:
                if (name.equals("com.google.common.collect.AbstractMapBasedMultimap$WrappedSet")) {
                    b = 2;
                }
                break;
            case -1757049669:
                if (name.equals("com.carrotsearch.hppc.LongHashSet")) {
                    b = 3;
                }
                break;
            case -1682705914:
                if (name.equals("gnu.trove.set.hash.TShortHashSet")) {
                    b = 4;
                }
                break;
            case -1670613343:
                if (name.equals("com.carrotsearch.hppc.CharHashSet")) {
                    b = 5;
                }
                break;
            case -1598338761:
                if (name.equals("java.nio.DirectByteBuffer")) {
                    b = 6;
                }
                break;
            case -1369392210:
                if (name.equals("com.fasterxml.jackson.databind.node.ObjectNode")) {
                    b = 7;
                }
                break;
            case -942806480:
                if (name.equals("org.javamoney.moneta.internal.JDKCurrencyAdapter")) {
                    b = 8;
                }
                break;
            case -864935548:
                if (name.equals("com.carrotsearch.hppc.CharArrayList")) {
                    b = 9;
                }
                break;
            case -848095899:
                if (name.equals("com.carrotsearch.hppc.IntArrayList")) {
                    b = 10;
                }
                break;
            case -808573634:
                if (name.equals("gnu.trove.list.array.TLongArrayList")) {
                    b = 11;
                }
                break;
            case -702521390:
                if (name.equals("com.carrotsearch.hppc.BitSet")) {
                    b = 12;
                }
                break;
            case -561799942:
                if (name.equals("java.nio.HeapByteBuffer")) {
                    b = 13;
                }
                break;
            case -448666600:
                if (name.equals("gnu.trove.list.array.TShortArrayList")) {
                    b = 14;
                }
                break;
            case -342082893:
                if (name.equals("gnu.trove.set.hash.TIntHashSet")) {
                    b = 15;
                }
                break;
            case -314457643:
                if (name.equals("org.apache.commons.lang3.tuple.MutablePair")) {
                    b = JSONB.Constants.BC_INT32_NUM_16;
                }
                break;
            case -240096200:
                if (name.equals("com.carrotsearch.hppc.ShortArrayList")) {
                    b = 17;
                }
                break;
            case -172623342:
                if (name.equals("org.javamoney.moneta.Money")) {
                    b = 18;
                }
                break;
            case -137241147:
                if (name.equals("org.apache.commons.lang3.tuple.Pair")) {
                    b = 19;
                }
                break;
            case -127813975:
                if (name.equals("com.carrotsearch.hppc.DoubleArrayList")) {
                    b = 20;
                }
                break;
            case 100244498:
                if (name.equals("com.carrotsearch.hppc.ByteArrayList")) {
                    b = 21;
                }
                break;
            case 217956074:
                if (name.equals("gnu.trove.set.hash.TLongHashSet")) {
                    b = 22;
                }
                break;
            case 255703211:
                if (name.equals("net.sf.json.JSONNull")) {
                    b = 23;
                }
                break;
            case 348125975:
                if (name.equals("org.javamoney.moneta.spi.DefaultNumberValue")) {
                    b = 24;
                }
                break;
            case 444521103:
                if (name.equals("java.net.Inet6Address")) {
                    b = 25;
                }
                break;
            case 574530702:
                if (name.equals("com.fasterxml.jackson.databind.node.ArrayNode")) {
                    b = 26;
                }
                break;
            case 652357028:
                if (name.equals("gnu.trove.list.array.TCharArrayList")) {
                    b = 27;
                }
                break;
            case 924843249:
                if (name.equals("org.apache.commons.lang3.tuple.ImmutablePair")) {
                    b = 28;
                }
                break;
            case 1138418232:
                if (name.equals("gnu.trove.list.array.TFloatArrayList")) {
                    b = 29;
                }
                break;
            case 1195384194:
                if (name.equals("gnu.trove.stack.array.TByteArrayStack")) {
                    b = 30;
                }
                break;
            case 1253867729:
                if (name.equals("java.net.Inet4Address")) {
                    b = 31;
                }
                break;
            case 1346988632:
                if (name.equals("com.carrotsearch.hppc.FloatArrayList")) {
                    b = 32;
                }
                break;
            case 1395322562:
                if (name.equals("com.carrotsearch.hppc.IntHashSet")) {
                    b = 33;
                }
                break;
            case 1527725683:
                if (name.equals("com.google.common.collect.AbstractMapBasedMultimap$RandomAccessWrappedList")) {
                    b = 34;
                }
                break;
            case 1539653772:
                if (name.equals("java.text.SimpleDateFormat")) {
                    b = 35;
                }
                break;
            case 1556153669:
                if (name.equals("gnu.trove.list.array.TIntArrayList")) {
                    b = 36;
                }
                break;
            case 1585284048:
                if (name.equals("java.net.InetSocketAddress")) {
                    b = 37;
                }
                break;
            case 1617537074:
                if (name.equals("gnu.trove.list.array.TByteArrayList")) {
                    b = 38;
                }
                break;
            case 1643140783:
                if (name.equals("org.bson.types.Decimal128")) {
                    b = 39;
                }
                break;
            case 1891987166:
                if (name.equals("gnu.trove.set.hash.TByteHashSet")) {
                    b = 40;
                }
                break;
            case 1969101086:
                if (name.equals("com.carrotsearch.hppc.LongArrayList")) {
                    b = 41;
                }
                break;
            case 1996438217:
                if (name.equals("gnu.trove.list.array.TDoubleArrayList")) {
                    b = 42;
                }
                break;
        }
        switch (b) {
            case 0:
                try {
                    return new ObjectWriter4(cls2, null, null, 0L, Arrays.asList(ObjectWriters.fieldWriter("r", cls2.getMethod("getRed", new Class[0])), ObjectWriters.fieldWriter("g", cls2.getMethod("getGreen", new Class[0])), ObjectWriters.fieldWriter("b", cls2.getMethod("getBlue", new Class[0])), ObjectWriters.fieldWriter("alpha", cls2.getMethod("getAlpha", new Class[0]))));
                } catch (NoSuchMethodException unused) {
                }
                break;
            case 1:
            case 23:
            case 25:
            case 26:
            case 31:
            case 35:
            case 37:
                return ObjectWriterMisc.INSTANCE;
            case 2:
            case 34:
                return null;
            case 3:
            case 4:
            case 5:
            case 9:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            case 17:
            case 20:
            case 21:
            case 22:
            case 27:
            case 29:
            case 30:
            case 32:
            case 33:
            case 36:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
                return LambdaMiscCodec.getObjectWriter(type2, cls2);
            case 6:
            case 13:
                return new ObjectWriterImplInt8ValueArray(new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((ByteBuffer) obj).array();
                    }
                });
            case 7:
                return ObjectWriterImplToString.DIRECT;
            case 8:
                return ObjectWriterImplToString.INSTANCE;
            case 16:
            case 19:
            case 28:
                return new ApacheLang3Support.PairWriter(cls2);
            case 18:
                return MoneySupport.createMonetaryAmountWriter();
            case 24:
                return MoneySupport.createNumberValueWriter();
        }
        if (type2 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type2;
            Type rawType = parameterizedType.getRawType();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (rawType == List.class || rawType == ArrayList.class) {
                if (actualTypeArguments.length == 1 && actualTypeArguments[0] == String.class) {
                    return ObjectWriterImplListStr.INSTANCE;
                }
                type2 = rawType;
            }
            if (Map.class.isAssignableFrom(cls2)) {
                return ObjectWriterImplMap.of(type2, cls2);
            }
            if (cls2 == Optional.class && actualTypeArguments.length == 1) {
                return new ObjectWriterImplOptional(actualTypeArguments[0], null, null);
            }
        }
        if (type2 == LinkedList.class) {
            return ObjectWriterImplList.INSTANCE;
        }
        if (type2 == ArrayList.class || type2 == List.class || List.class.isAssignableFrom(cls2)) {
            return ObjectWriterImplList.INSTANCE;
        }
        if (Collection.class.isAssignableFrom(cls2)) {
            return ObjectWriterImplCollection.INSTANCE;
        }
        if (BeanUtils.isExtendedMap(cls2)) {
            return null;
        }
        if (Map.class.isAssignableFrom(cls2)) {
            return ObjectWriterImplMap.of(cls2);
        }
        if (Map.Entry.class.isAssignableFrom(cls2)) {
            return ObjectWriterImplMapEntry.INSTANCE;
        }
        if (Path.class.isAssignableFrom(cls2)) {
            return ObjectWriterImplToString.INSTANCE;
        }
        if (type2 == Integer.class) {
            return ObjectWriterImplInt32.INSTANCE;
        }
        if (type2 == AtomicInteger.class) {
            return ObjectWriterImplAtomicInteger.INSTANCE;
        }
        if (type2 == Byte.class) {
            return ObjectWriterImplInt8.INSTANCE;
        }
        if (type2 == Short.class) {
            return ObjectWriterImplInt16.INSTANCE;
        }
        if (type2 == Long.class) {
            return ObjectWriterImplInt64.INSTANCE;
        }
        if (type2 == AtomicLong.class) {
            return ObjectWriterImplAtomicLong.INSTANCE;
        }
        if (type2 == AtomicReference.class) {
            return ObjectWriterImplAtomicReference.INSTANCE;
        }
        if (type2 == Float.class) {
            return ObjectWriterImplFloat.INSTANCE;
        }
        if (type2 == Double.class) {
            return ObjectWriterImplDouble.INSTANCE;
        }
        if (type2 == BigInteger.class) {
            return ObjectWriterBigInteger.INSTANCE;
        }
        if (type2 == BigDecimal.class) {
            return ObjectWriterImplBigDecimal.INSTANCE;
        }
        if (type2 == BitSet.class) {
            return ObjectWriterImplBitSet.INSTANCE;
        }
        if (type2 == OptionalInt.class) {
            return ObjectWriterImplOptionalInt.INSTANCE;
        }
        if (type2 == OptionalLong.class) {
            return ObjectWriterImplOptionalLong.INSTANCE;
        }
        if (type2 == OptionalDouble.class) {
            return ObjectWriterImplOptionalDouble.INSTANCE;
        }
        if (type2 == Optional.class) {
            return ObjectWriterImplOptional.INSTANCE;
        }
        if (type2 == Boolean.class) {
            return ObjectWriterImplBoolean.INSTANCE;
        }
        if (type2 == AtomicBoolean.class) {
            return ObjectWriterImplAtomicBoolean.INSTANCE;
        }
        if (type2 == AtomicIntegerArray.class) {
            return ObjectWriterImplAtomicIntegerArray.INSTANCE;
        }
        if (type2 == AtomicLongArray.class) {
            return ObjectWriterImplAtomicLongArray.INSTANCE;
        }
        if (type2 == Character.class) {
            return ObjectWriterImplCharacter.INSTANCE;
        }
        if (type2 instanceof Class) {
            Class cls3 = (Class) type2;
            if (TimeUnit.class.isAssignableFrom(cls3)) {
                return new ObjectWriterImplEnum(null, TimeUnit.class, null, null, 0L);
            }
            if (Enum.class.isAssignableFrom(cls3) && (objectWriterCreateEnumWriter = createEnumWriter(cls3)) != null) {
                return objectWriterCreateEnumWriter;
            }
            if (JSONPath.class.isAssignableFrom(cls3)) {
                return ObjectWriterImplToString.INSTANCE;
            }
            if (cls3 == boolean[].class) {
                return ObjectWriterImplBoolValueArray.INSTANCE;
            }
            if (cls3 == char[].class) {
                return ObjectWriterImplCharValueArray.INSTANCE;
            }
            if (cls3 == StringBuffer.class || cls3 == StringBuilder.class) {
                return ObjectWriterImplToString.INSTANCE;
            }
            if (cls3 == byte[].class) {
                return ObjectWriterImplInt8ValueArray.INSTANCE;
            }
            if (cls3 == short[].class) {
                return ObjectWriterImplInt16ValueArray.INSTANCE;
            }
            if (cls3 == int[].class) {
                return ObjectWriterImplInt32ValueArray.INSTANCE;
            }
            if (cls3 == long[].class) {
                return ObjectWriterImplInt64ValueArray.INSTANCE;
            }
            if (cls3 == float[].class) {
                return ObjectWriterImplFloatValueArray.INSTANCE;
            }
            if (cls3 == double[].class) {
                return ObjectWriterImplDoubleValueArray.INSTANCE;
            }
            if (cls3 == Byte[].class) {
                return ObjectWriterImplInt8Array.INSTANCE;
            }
            if (cls3 == Integer[].class) {
                return ObjectWriterImplInt32Array.INSTANCE;
            }
            if (cls3 == Long[].class) {
                return ObjectWriterImplInt64Array.INSTANCE;
            }
            if (String[].class == cls3) {
                return ObjectWriterImplStringArray.INSTANCE;
            }
            if (BigDecimal[].class == cls3) {
                return ObjectWriterImpDecimalArray.INSTANCE;
            }
            if (Object[].class.isAssignableFrom(cls3)) {
                if (cls3 == Object[].class) {
                    return ObjectWriterArray.INSTANCE;
                }
                Class<?> componentType = cls3.getComponentType();
                if (Modifier.isFinal(componentType.getModifiers())) {
                    return new ObjectWriterArrayFinal(componentType, null);
                }
                return new ObjectWriterArray(componentType);
            }
            if (cls3 == UUID.class) {
                return ObjectWriterImplUUID.INSTANCE;
            }
            if (cls3 == Locale.class) {
                return ObjectWriterImplLocale.INSTANCE;
            }
            if (cls3 == Currency.class) {
                return ObjectWriterImplCurrency.INSTANCE;
            }
            if (TimeZone.class.isAssignableFrom(cls3)) {
                return ObjectWriterImplTimeZone.INSTANCE;
            }
            if (JSONPObject.class.isAssignableFrom(cls3)) {
                return new ObjectWriterImplJSONP();
            }
            if (cls3 == URI.class || cls3 == URL.class || cls3 == File.class || ZoneId.class.isAssignableFrom(cls3) || Charset.class.isAssignableFrom(cls3)) {
                return ObjectWriterImplToString.INSTANCE;
            }
            ObjectWriter externalObjectWriter2 = getExternalObjectWriter(cls3.getName(), cls3);
            if (externalObjectWriter2 != null) {
                return externalObjectWriter2;
            }
            BeanInfo beanInfoCreateBeanInfo = this.provider.createBeanInfo();
            Class mixIn = this.provider.getMixIn(cls3);
            if (mixIn != null) {
                this.annotationProcessor.getBeanInfo(beanInfoCreateBeanInfo, mixIn);
            }
            if (Date.class.isAssignableFrom(cls3)) {
                if (beanInfoCreateBeanInfo.format != null || beanInfoCreateBeanInfo.locale != null) {
                    return new ObjectWriterImplDate(beanInfoCreateBeanInfo.format, beanInfoCreateBeanInfo.locale);
                }
                return ObjectWriterImplDate.INSTANCE;
            }
            if (Calendar.class.isAssignableFrom(cls3)) {
                if (beanInfoCreateBeanInfo.format != null || beanInfoCreateBeanInfo.locale != null) {
                    return new ObjectWriterImplCalendar(beanInfoCreateBeanInfo.format, beanInfoCreateBeanInfo.locale);
                }
                return ObjectWriterImplCalendar.INSTANCE;
            }
            if (ZonedDateTime.class == cls3) {
                if (beanInfoCreateBeanInfo.format != null || beanInfoCreateBeanInfo.locale != null) {
                    return new ObjectWriterImplZonedDateTime(beanInfoCreateBeanInfo.format, beanInfoCreateBeanInfo.locale);
                }
                return ObjectWriterImplZonedDateTime.INSTANCE;
            }
            if (OffsetDateTime.class == cls3) {
                return ObjectWriterImplOffsetDateTime.of(beanInfoCreateBeanInfo.format, beanInfoCreateBeanInfo.locale);
            }
            if (LocalDateTime.class == cls3) {
                if (beanInfoCreateBeanInfo.format != null || beanInfoCreateBeanInfo.locale != null) {
                    return new ObjectWriterImplLocalDateTime(beanInfoCreateBeanInfo.format, beanInfoCreateBeanInfo.locale);
                }
                return ObjectWriterImplLocalDateTime.INSTANCE;
            }
            if (LocalDate.class == cls3) {
                return ObjectWriterImplLocalDate.of(beanInfoCreateBeanInfo.format, beanInfoCreateBeanInfo.locale);
            }
            if (LocalTime.class == cls3) {
                if (beanInfoCreateBeanInfo.format != null || beanInfoCreateBeanInfo.locale != null) {
                    return new ObjectWriterImplLocalTime(beanInfoCreateBeanInfo.format, beanInfoCreateBeanInfo.locale);
                }
                return ObjectWriterImplLocalTime.INSTANCE;
            }
            if (OffsetTime.class == cls3) {
                if (beanInfoCreateBeanInfo.format != null || beanInfoCreateBeanInfo.locale != null) {
                    return new ObjectWriterImplOffsetTime(beanInfoCreateBeanInfo.format, beanInfoCreateBeanInfo.locale);
                }
                return ObjectWriterImplOffsetTime.INSTANCE;
            }
            if (Instant.class == cls3) {
                if (beanInfoCreateBeanInfo.format != null || beanInfoCreateBeanInfo.locale != null) {
                    return new ObjectWriterImplInstant(beanInfoCreateBeanInfo.format, beanInfoCreateBeanInfo.locale);
                }
                return ObjectWriterImplInstant.INSTANCE;
            }
            if (Duration.class == cls3 || Period.class == cls3) {
                return ObjectWriterImplToString.INSTANCE;
            }
            if (StackTraceElement.class == cls3) {
                if (STACK_TRACE_ELEMENT_WRITER == null) {
                    ObjectWriterCreator creator = this.provider.getCreator();
                    STACK_TRACE_ELEMENT_WRITER = new ObjectWriterAdapter(StackTraceElement.class, null, null, 0L, Arrays.asList(creator.createFieldWriter("fileName", String.class, BeanUtils.getDeclaredField(StackTraceElement.class, "fileName"), BeanUtils.getMethod(StackTraceElement.class, "getFileName"), new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda7
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return ((StackTraceElement) obj).getFileName();
                        }
                    }), creator.createFieldWriter("lineNumber", BeanUtils.getDeclaredField(StackTraceElement.class, "lineNumber"), BeanUtils.getMethod(StackTraceElement.class, "getLineNumber"), new ToIntFunction() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda8
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(Object obj) {
                            return ((StackTraceElement) obj).getLineNumber();
                        }
                    }), creator.createFieldWriter("className", String.class, BeanUtils.getDeclaredField(StackTraceElement.class, "declaringClass"), BeanUtils.getMethod(StackTraceElement.class, "getClassName"), new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda9
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return ((StackTraceElement) obj).getClassName();
                        }
                    }), creator.createFieldWriter("methodName", String.class, BeanUtils.getDeclaredField(StackTraceElement.class, "methodName"), BeanUtils.getMethod(StackTraceElement.class, "getMethodName"), new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda10
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return ((StackTraceElement) obj).getMethodName();
                        }
                    })));
                }
                return STACK_TRACE_ELEMENT_WRITER;
            }
            if (Class.class == cls3) {
                return ObjectWriterImplClass.INSTANCE;
            }
            if (Method.class == cls3) {
                return new ObjectWriterAdapter(Method.class, null, null, 0L, Arrays.asList(ObjectWriters.fieldWriter("declaringClass", Class.class, new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda11
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((Method) obj).getDeclaringClass();
                    }
                }), ObjectWriters.fieldWriter("name", String.class, new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda12
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((Method) obj).getName();
                    }
                }), ObjectWriters.fieldWriter("parameterTypes", Class[].class, new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda1
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((Method) obj).getParameterTypes();
                    }
                })));
            }
            if (Field.class == cls3) {
                return new ObjectWriterAdapter(Method.class, null, null, 0L, Arrays.asList(ObjectWriters.fieldWriter("declaringClass", Class.class, new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((Field) obj).getDeclaringClass();
                    }
                }), ObjectWriters.fieldWriter("name", String.class, new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda3
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((Field) obj).getName();
                    }
                })));
            }
            if (ParameterizedType.class.isAssignableFrom(cls3)) {
                return ObjectWriters.objectWriter(ParameterizedType.class, ObjectWriters.fieldWriter("actualTypeArguments", Type[].class, new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda4
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((ParameterizedType) obj).getActualTypeArguments();
                    }
                }), ObjectWriters.fieldWriter("ownerType", Type.class, new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda5
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((ParameterizedType) obj).getOwnerType();
                    }
                }), ObjectWriters.fieldWriter("rawType", Type.class, new Function() { // from class: com.alibaba.fastjson2.writer.ObjectWriterBaseModule$$ExternalSyntheticLambda6
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((ParameterizedType) obj).getRawType();
                    }
                }));
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0012  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.alibaba.fastjson2.writer.ObjectWriter createEnumWriter(java.lang.Class r10) throws java.lang.NoSuchMethodException {
        /*
            r9 = this;
            boolean r0 = r10.isEnum()
            if (r0 != 0) goto L12
            java.lang.Class r0 = r10.getSuperclass()
            boolean r1 = r0.isEnum()
            if (r1 == 0) goto L12
            r4 = r0
            goto L13
        L12:
            r4 = r10
        L13:
            com.alibaba.fastjson2.writer.ObjectWriterProvider r10 = r9.provider
            java.lang.reflect.Member r10 = com.alibaba.fastjson2.util.BeanUtils.getEnumValueField(r4, r10)
            r0 = 0
            if (r10 != 0) goto L47
            com.alibaba.fastjson2.writer.ObjectWriterProvider r1 = r9.provider
            java.util.concurrent.ConcurrentMap<java.lang.Class, java.lang.Class> r1 = r1.mixInCache
            java.lang.Object r1 = r1.get(r4)
            java.lang.Class r1 = (java.lang.Class) r1
            com.alibaba.fastjson2.writer.ObjectWriterProvider r2 = r9.provider
            java.lang.reflect.Member r1 = com.alibaba.fastjson2.util.BeanUtils.getEnumValueField(r1, r2)
            boolean r2 = r1 instanceof java.lang.reflect.Field
            if (r2 == 0) goto L39
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Throwable -> L47
            java.lang.reflect.Field r10 = r4.getField(r1)     // Catch: java.lang.Throwable -> L47
            goto L47
        L39:
            boolean r2 = r1 instanceof java.lang.reflect.Method
            if (r2 == 0) goto L47
            java.lang.String r1 = r1.getName()
            java.lang.Class[] r2 = new java.lang.Class[r0]
            java.lang.reflect.Method r10 = r4.getMethod(r1, r2)
        L47:
            r5 = r10
            com.alibaba.fastjson2.writer.ObjectWriterProvider r10 = r9.provider
            com.alibaba.fastjson2.codec.BeanInfo r10 = r10.createBeanInfo()
            java.lang.Class[] r1 = r4.getInterfaces()
        L52:
            int r2 = r1.length
            if (r0 >= r2) goto L5f
            com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor r2 = r9.annotationProcessor
            r3 = r1[r0]
            r2.getBeanInfo(r10, r3)
            int r0 = r0 + 1
            goto L52
        L5f:
            com.alibaba.fastjson2.writer.ObjectWriterBaseModule$WriterAnnotationProcessor r0 = r9.annotationProcessor
            r0.getBeanInfo(r10, r4)
            boolean r10 = r10.writeEnumAsJavaBean
            if (r10 == 0) goto L6a
            r10 = 0
            return r10
        L6a:
            java.lang.String[] r6 = com.alibaba.fastjson2.util.BeanUtils.getEnumAnnotationNames(r4)
            com.alibaba.fastjson2.writer.ObjectWriterImplEnum r2 = new com.alibaba.fastjson2.writer.ObjectWriterImplEnum
            r3 = 0
            r7 = 0
            r2.<init>(r3, r4, r5, r6, r7)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterBaseModule.createEnumWriter(java.lang.Class):com.alibaba.fastjson2.writer.ObjectWriter");
    }

    static class VoidObjectWriter implements ObjectWriter {
        public static final VoidObjectWriter INSTANCE = new VoidObjectWriter();

        @Override // com.alibaba.fastjson2.writer.ObjectWriter
        public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        }

        VoidObjectWriter() {
        }
    }
}
