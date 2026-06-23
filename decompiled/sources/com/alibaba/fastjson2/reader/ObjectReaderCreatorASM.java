package com.alibaba.fastjson2.reader;

import androidx.exifinterface.media.ExifInterface;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.codec.BeanInfo;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.function.ObjBoolConsumer;
import com.alibaba.fastjson2.function.ObjByteConsumer;
import com.alibaba.fastjson2.function.ObjCharConsumer;
import com.alibaba.fastjson2.function.ObjFloatConsumer;
import com.alibaba.fastjson2.function.ObjShortConsumer;
import com.alibaba.fastjson2.internal.CodeGenUtils;
import com.alibaba.fastjson2.internal.asm.ASMUtils;
import com.alibaba.fastjson2.internal.asm.ClassWriter;
import com.alibaba.fastjson2.internal.asm.Label;
import com.alibaba.fastjson2.internal.asm.MethodWriter;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.DynamicClassLoader;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public class ObjectReaderCreatorASM extends ObjectReaderCreator {
    static final int THIS = 0;
    static final String[] fieldItemObjectReader;
    static final Map<Class, FieldReaderInfo> infos;
    static final String packageName;
    protected final DynamicClassLoader classLoader;
    public static final ObjectReaderCreatorASM INSTANCE = new ObjectReaderCreatorASM(DynamicClassLoader.getInstance());
    protected static final AtomicLong seed = new AtomicLong();
    static final String METHOD_DESC_GET_ITEM_OBJECT_READER = "(" + ASMUtils.DESC_JSON_READER + ")" + ASMUtils.DESC_OBJECT_READER;
    static final String METHOD_DESC_GET_OBJECT_READER_1 = "(" + ASMUtils.DESC_JSON_READER + ")" + ASMUtils.DESC_OBJECT_READER;
    static final String METHOD_DESC_INIT = "(Ljava/lang/Class;Ljava/util/function/Supplier;" + ASMUtils.DESC_FIELD_READER_ARRAY + ")V";
    static final String METHOD_DESC_ADAPTER_INIT = "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;J" + ASMUtils.DESC_JSONSCHEMA + "Ljava/util/function/Supplier;Ljava/util/function/Function;" + ASMUtils.DESC_FIELD_READER_ARRAY + ")V";
    static final String METHOD_DESC_READ_OBJECT = "(" + ASMUtils.DESC_JSON_READER + "Ljava/lang/reflect/Type;Ljava/lang/Object;J)Ljava/lang/Object;";
    static final String METHOD_DESC_GET_FIELD_READER = "(J)" + ASMUtils.DESC_FIELD_READER;
    static final String METHOD_DESC_READ_FIELD_VALUE = "(" + ASMUtils.DESC_JSON_READER + "Ljava/lang/Object;)V";
    static final String READ_FIELD_READER_UL = "(J" + ASMUtils.DESC_JSON_READER + "JLjava/lang/Object;)V";
    static final String METHOD_DESC_ADD_RESOLVE_TASK = "(" + ASMUtils.DESC_JSON_READER + "Ljava/lang/Object;Ljava/lang/String;)V";
    static final String METHOD_DESC_CHECK_ARRAY_AUTO_TYPE = "(" + ASMUtils.DESC_JSON_READER + ")" + ASMUtils.DESC_OBJECT_READER;
    static final String METHOD_DESC_PROCESS_EXTRA = "(" + ASMUtils.DESC_JSON_READER + "Ljava/lang/Object;J)V";
    static final String METHOD_DESC_JSON_READER_CHECK_ARRAY_AUTO_TYPE = "(" + ASMUtils.DESC_JSON_READER + "J)" + ASMUtils.DESC_OBJECT_READER;
    static final String METHOD_DESC_READ_ARRAY_MAPPING_JSONB_OBJECT0 = "(" + ASMUtils.DESC_JSON_READER + "Ljava/lang/Object;I)V";

    static {
        HashMap map = new HashMap();
        infos = map;
        Package r1 = ObjectReaderCreatorASM.class.getPackage();
        packageName = r1 != null ? r1.getName() : "";
        map.put(Boolean.TYPE, new FieldReaderInfo(ASMUtils.type(ObjBoolConsumer.class), "(Ljava/lang/Object;Z)V", "(Z)V", 21, "readFieldBoolValue", "()Z", 54));
        map.put(Character.TYPE, new FieldReaderInfo(ASMUtils.type(ObjCharConsumer.class), "(Ljava/lang/Object;C)V", "(C)V", 21, "readInt32Value", "()C", 54));
        map.put(Byte.TYPE, new FieldReaderInfo(ASMUtils.type(ObjByteConsumer.class), "(Ljava/lang/Object;B)V", "(B)V", 21, "readInt32Value", "()B", 54));
        map.put(Short.TYPE, new FieldReaderInfo(ASMUtils.type(ObjShortConsumer.class), "(Ljava/lang/Object;S)V", "(S)V", 21, "readInt32Value", "()S", 54));
        map.put(Integer.TYPE, new FieldReaderInfo(ASMUtils.type(ObjIntConsumer.class), "(Ljava/lang/Object;I)V", "(I)V", 21, "readInt32Value", "()I", 54));
        map.put(Long.TYPE, new FieldReaderInfo(ASMUtils.type(ObjLongConsumer.class), "(Ljava/lang/Object;J)V", "(J)V", 22, "readInt64Value", "()V", 55));
        map.put(Float.TYPE, new FieldReaderInfo(ASMUtils.type(ObjFloatConsumer.class), "(Ljava/lang/Object;F)V", "(F)V", 23, "readFieldFloatValue", "()F", 56));
        map.put(Double.TYPE, new FieldReaderInfo(ASMUtils.type(ObjDoubleConsumer.class), "(Ljava/lang/Object;D)V", "(D)V", 24, "readFloatDoubleValue", "()D", 57));
        map.put(String.class, new FieldReaderInfo(ASMUtils.type(BiConsumer.class), "(Ljava/lang/Object;Ljava/lang/Object;)V", "(Ljava/lang/String;)V", 25, "readString", "()Ljava/lang/String;", 58));
        map.put(Integer.class, new FieldReaderInfo(ASMUtils.type(BiConsumer.class), "(Ljava/lang/Object;Ljava/lang/Integer;)V", "(Ljava/lang/Integer;)V", 25, "readInt32", "()Ljava/lang/Integer;", 58));
        fieldItemObjectReader = new String[1024];
    }

    static String fieldObjectReader(int i) {
        switch (i) {
            case 0:
                return "objectReader0";
            case 1:
                return "objectReader1";
            case 2:
                return "objectReader2";
            case 3:
                return "objectReader3";
            case 4:
                return "objectReader4";
            case 5:
                return "objectReader5";
            case 6:
                return "objectReader6";
            case 7:
                return "objectReader7";
            case 8:
                return "objectReader8";
            case 9:
                return "objectReader9";
            case 10:
                return "objectReader10";
            case 11:
                return "objectReader11";
            case 12:
                return "objectReader12";
            case 13:
                return "objectReader13";
            case 14:
                return "objectReader14";
            case 15:
                return "objectReader15";
            default:
                int length = "objectReader".length() + IOUtils.stringSize(i);
                char[] cArr = new char[length];
                "objectReader".getChars(0, "objectReader".length(), cArr, 0);
                IOUtils.getChars(i, length, cArr);
                return new String(cArr);
        }
    }

    static String fieldItemObjectReader(int i) {
        String[] strArr = fieldItemObjectReader;
        String str = strArr[i];
        if (str != null) {
            return str;
        }
        int length = "itemReader".length() + IOUtils.stringSize(i);
        char[] cArr = new char[length];
        "itemReader".getChars(0, "itemReader".length(), cArr, 0);
        IOUtils.getChars(i, length, cArr);
        String str2 = new String(cArr);
        strArr[i] = str2;
        return str2;
    }

    private static class FieldReaderInfo {
        final String acceptDesc;
        final String interfaceDesc;
        final int loadCode;
        final String readMethodDesc;
        final String readMethodName;
        final String setterDesc;
        final int storeCode;

        FieldReaderInfo(String str, String str2, String str3, int i, String str4, String str5, int i2) {
            this.interfaceDesc = str;
            this.acceptDesc = str2;
            this.setterDesc = str3;
            this.loadCode = i;
            this.readMethodName = str4;
            this.readMethodDesc = str5;
            this.storeCode = i2;
        }
    }

    public ObjectReaderCreatorASM(ClassLoader classLoader) {
        DynamicClassLoader dynamicClassLoader;
        if (classLoader instanceof DynamicClassLoader) {
            dynamicClassLoader = (DynamicClassLoader) classLoader;
        } else {
            dynamicClassLoader = new DynamicClassLoader(classLoader);
        }
        this.classLoader = dynamicClassLoader;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderCreator
    public <T> ObjectReader<T> createObjectReader(Class<T> cls, Type type, boolean z, ObjectReaderProvider objectReaderProvider) {
        Constructor defaultConstructor;
        Class cls2;
        boolean z2 = cls != null && this.classLoader.isExternalClass(cls);
        int modifiers = cls.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
            return super.createObjectReader(cls, type, z, objectReaderProvider);
        }
        BeanInfo beanInfo = new BeanInfo(objectReaderProvider);
        objectReaderProvider.getBeanInfo(beanInfo, cls);
        if (z2 || !Modifier.isPublic(modifiers)) {
            beanInfo.readerFeatures |= FieldInfo.JIT;
        }
        if (beanInfo.deserializer != null && ObjectReader.class.isAssignableFrom(beanInfo.deserializer)) {
            try {
                Constructor<T> declaredConstructor = beanInfo.deserializer.getDeclaredConstructor(new Class[0]);
                declaredConstructor.setAccessible(true);
                return (ObjectReader) declaredConstructor.newInstance(new Object[0]);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw new JSONException("create deserializer error", e);
            }
        }
        boolean z3 = (z && (cls.isInterface() || BeanUtils.isRecord(cls))) ? false : z;
        if (Enum.class.isAssignableFrom(cls) && (beanInfo.createMethod == null || beanInfo.createMethod.getParameterCount() == 1)) {
            return createEnumReader(cls, beanInfo.createMethod, objectReaderProvider);
        }
        if (beanInfo.creatorConstructor != null || beanInfo.createMethod != null) {
            return createObjectReaderWithCreator(cls, type, objectReaderProvider, beanInfo);
        }
        if (beanInfo.builder != null) {
            return createObjectReaderWithBuilder(cls, type, objectReaderProvider, beanInfo);
        }
        if (Throwable.class.isAssignableFrom(cls) || BeanUtils.isExtendedMap(cls)) {
            return super.createObjectReader(cls, type, z3, objectReaderProvider);
        }
        if (cls == Class.class) {
            return ObjectReaderImplClass.INSTANCE;
        }
        FieldReader[] fieldReaderArrCreateFieldReaders = createFieldReaders(cls, type, beanInfo, z3, objectReaderProvider);
        boolean z4 = fieldReaderArrCreateFieldReaders.length <= 96;
        if (!z3) {
            if (JDKUtils.JVM_VERSION >= 9 && cls == StackTraceElement.class) {
                try {
                    return createObjectReaderNoneDefaultConstructor(StackTraceElement.class.getConstructor(String.class, String.class, String.class, String.class, String.class, String.class, Integer.TYPE), "", "classLoaderName", "moduleName", "moduleVersion", "declaringClass", "methodName", "fileName", "lineNumber");
                } catch (NoSuchMethodException | SecurityException unused) {
                }
            }
            for (FieldReader fieldReader : fieldReaderArrCreateFieldReaders) {
                if (fieldReader.isReadOnly() || fieldReader.isUnwrapped() || (fieldReader.features & FieldInfo.READ_USING_MASK) != 0) {
                    z4 = false;
                    break;
                }
            }
        }
        if (beanInfo.autoTypeBeforeHandler != null) {
            z4 = false;
        }
        if (z4) {
            for (FieldReader fieldReader2 : fieldReaderArrCreateFieldReaders) {
                if (fieldReader2.defaultValue != null || fieldReader2.schema != null || (((cls2 = fieldReader2.fieldClass) != null && !Modifier.isPublic(cls2.getModifiers())) || (((fieldReader2 instanceof FieldReaderMapField) && ((FieldReaderMapField) fieldReader2).arrayToMapKey != null) || ((fieldReader2 instanceof FieldReaderMapMethod) && ((FieldReaderMapMethod) fieldReader2).arrayToMapKey != null)))) {
                    z4 = false;
                    break;
                }
            }
        }
        if (z4 && (beanInfo.rootName != null || (beanInfo.schema != null && !beanInfo.schema.isEmpty()))) {
            z4 = false;
        }
        if (!z4) {
            return super.createObjectReader(cls, type, z3, objectReaderProvider);
        }
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers) || (defaultConstructor = BeanUtils.getDefaultConstructor(cls, true)) == null) {
            defaultConstructor = null;
        } else {
            try {
                defaultConstructor.setAccessible(true);
            } catch (SecurityException unused2) {
            }
        }
        if (beanInfo.seeAlso != null && beanInfo.seeAlso.length != 0) {
            return createObjectReaderSeeAlso(cls, beanInfo.typeKey, beanInfo.seeAlso, beanInfo.seeAlsoNames, beanInfo.seeAlsoDefault, fieldReaderArrCreateFieldReaders);
        }
        if (!z3 && defaultConstructor == null) {
            return super.createObjectReader(cls, type, false, objectReaderProvider);
        }
        return jitObjectReader(cls, type, z3, z2, modifiers, beanInfo, null, fieldReaderArrCreateFieldReaders, defaultConstructor);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderCreator
    protected <T> ObjectReaderNoneDefaultConstructor createNoneDefaultConstructorObjectReader(final Class cls, BeanInfo beanInfo, Function<Map<Long, Object>, T> function, List<Constructor> list, String[] strArr, FieldReader[] fieldReaderArr, FieldReader[] fieldReaderArr2) {
        Class<?> cls2;
        ObjectReaderNoneDefaultConstructor objectReaderNoneDefaultConstructor = new ObjectReaderNoneDefaultConstructor(cls, beanInfo.typeKey, beanInfo.typeName, beanInfo.readerFeatures, function, list, strArr, fieldReaderArr, fieldReaderArr2, beanInfo.seeAlso, beanInfo.seeAlsoNames);
        boolean z = beanInfo.autoTypeBeforeHandler == null && fieldReaderArr2.length == 0 && ((function instanceof ConstructorFunction) || (function instanceof FactoryFunction)) && ((list == null || list.isEmpty()) && !this.classLoader.isExternalClass(cls) && (beanInfo.readerFeatures & JSONReader.Feature.SupportAutoType.mask) == 0 && ((objectReaderNoneDefaultConstructor.noneDefaultConstructor == null || objectReaderNoneDefaultConstructor.noneDefaultConstructor.getParameterCount() == fieldReaderArr.length) && ((!(function instanceof FactoryFunction) || ((FactoryFunction) function).paramNames.length == fieldReaderArr.length) && fieldReaderArr.length <= 64)));
        if (z) {
            for (FieldReader fieldReader : fieldReaderArr) {
                if (fieldReader.getInitReader() != null || fieldReader.defaultValue != null || fieldReader.schema != null || (((cls2 = fieldReader.fieldClass) != null && (!Modifier.isPublic(cls2.getModifiers()) || this.classLoader.isExternalClass(cls2))) || (((fieldReader instanceof FieldReaderMapField) && ((FieldReaderMapField) fieldReader).arrayToMapKey != null) || ((fieldReader instanceof FieldReaderMapMethod) && ((FieldReaderMapMethod) fieldReader).arrayToMapKey != null)))) {
                    z = false;
                    break;
                }
            }
        }
        if (!z) {
            return objectReaderNoneDefaultConstructor;
        }
        boolean z2 = cls != null && this.classLoader.isExternalClass(cls);
        ClassWriter classWriter = new ClassWriter(new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ObjectReaderCreatorASM.lambda$createNoneDefaultConstructorObjectReader$0(cls, (String) obj);
            }
        });
        beanInfo.readerFeatures |= FieldInfo.DISABLE_REFERENCE_DETECT;
        ObjectReadContext objectReadContext = new ObjectReadContext(beanInfo, cls, classWriter, z2, fieldReaderArr, null);
        objectReadContext.objectReaderAdapter = objectReaderNoneDefaultConstructor;
        genFields(fieldReaderArr, classWriter, ASMUtils.TYPE_OBJECT_READER_NONE_DEFAULT_CONSTRUCTOR);
        classWriter.visit(52, 49, objectReadContext.classNameType, ASMUtils.TYPE_OBJECT_READER_NONE_DEFAULT_CONSTRUCTOR, new String[0]);
        MethodWriter methodWriterVisitMethod = classWriter.visitMethod(1, "<init>", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;JLjava/util/function/Function;Ljava/util/List;[Ljava/lang/String;[Lcom/alibaba/fastjson2/reader/FieldReader;[Lcom/alibaba/fastjson2/reader/FieldReader;[Ljava/lang/Class;[Ljava/lang/String;)V", fieldReaderArr2.length <= 12 ? 32 : 128);
        methodWriterVisitMethod.aload(0);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.aload(2);
        methodWriterVisitMethod.aload(3);
        methodWriterVisitMethod.lload(4);
        methodWriterVisitMethod.aload(6);
        methodWriterVisitMethod.aload(7);
        methodWriterVisitMethod.aload(8);
        methodWriterVisitMethod.aload(9);
        methodWriterVisitMethod.aload(10);
        methodWriterVisitMethod.aload(11);
        methodWriterVisitMethod.aload(12);
        methodWriterVisitMethod.invokespecial(ASMUtils.TYPE_OBJECT_READER_NONE_DEFAULT_CONSTRUCTOR, "<init>", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;JLjava/util/function/Function;Ljava/util/List;[Ljava/lang/String;[Lcom/alibaba/fastjson2/reader/FieldReader;[Lcom/alibaba/fastjson2/reader/FieldReader;[Ljava/lang/Class;[Ljava/lang/String;)V");
        genInitFields(fieldReaderArr, objectReadContext.classNameType, true, 9, methodWriterVisitMethod, ASMUtils.TYPE_OBJECT_READER_NONE_DEFAULT_CONSTRUCTOR);
        methodWriterVisitMethod.return_();
        methodWriterVisitMethod.visitMaxs(3, 3);
        genMethodReadObject(objectReadContext, beanInfo.readerFeatures);
        if (!objectReadContext.disableJSONB()) {
            genMethodReadJSONBObject(objectReadContext, beanInfo.readerFeatures);
        }
        byte[] byteArray = classWriter.toByteArray();
        try {
            return (ObjectReaderNoneDefaultConstructor) this.classLoader.defineClassPublic(objectReadContext.classNameFull, byteArray, 0, byteArray.length).getConstructors()[0].newInstance(cls, beanInfo.typeKey, beanInfo.typeName, Long.valueOf(beanInfo.readerFeatures), function, list, strArr, fieldReaderArr, fieldReaderArr2, null, null);
        } catch (Throwable th) {
            throw new JSONException("create objectReader error" + (cls == null ? "" : ", objectType " + cls.getTypeName()), th);
        }
    }

    static /* synthetic */ Class lambda$createNoneDefaultConstructorObjectReader$0(Class cls, String str) {
        if (cls.getName().equals(str)) {
            return cls;
        }
        return null;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderCreator
    public <T> ObjectReader<T> createObjectReader(Class<T> cls, String str, String str2, long j, JSONSchema jSONSchema, Supplier<T> supplier, Function function, FieldReader... fieldReaderArr) {
        if (cls == null && supplier != null && function == null) {
            for (FieldReader fieldReader : fieldReaderArr) {
                if (fieldReader.getFunction() != null) {
                }
            }
            return jitObjectReader(cls, cls, false, false, 0, new BeanInfo(JSONFactory.getDefaultObjectReaderProvider()), supplier, fieldReaderArr, null);
        }
        return super.createObjectReader(cls, str, str2, j, jSONSchema, supplier, function, fieldReaderArr);
    }

    private <T> ObjectReaderBean jitObjectReader(final Class<T> cls, Type type, boolean z, boolean z2, int i, BeanInfo beanInfo, Supplier<T> supplier, FieldReader[] fieldReaderArr, Constructor constructor) {
        String str;
        Supplier<T> supplierCreateSupplier;
        FieldReader[] fieldReaderArr2;
        Class<T> cls2;
        ClassWriter classWriter = new ClassWriter(new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ObjectReaderCreatorASM.lambda$jitObjectReader$1(cls, (String) obj);
            }
        });
        ObjectReadContext objectReadContext = new ObjectReadContext(beanInfo, cls, classWriter, z2, fieldReaderArr, constructor);
        boolean z3 = true;
        boolean z4 = fieldReaderArr.length <= 96;
        switch (fieldReaderArr.length) {
            case 1:
                str = ASMUtils.TYPE_OBJECT_READER_1;
                break;
            case 2:
                str = ASMUtils.TYPE_OBJECT_READER_2;
                break;
            case 3:
                str = ASMUtils.TYPE_OBJECT_READER_3;
                break;
            case 4:
                str = ASMUtils.TYPE_OBJECT_READER_4;
                break;
            case 5:
                str = ASMUtils.TYPE_OBJECT_READER_5;
                break;
            case 6:
                str = ASMUtils.TYPE_OBJECT_READER_6;
                break;
            case 7:
                str = ASMUtils.TYPE_OBJECT_READER_7;
                break;
            case 8:
                str = ASMUtils.TYPE_OBJECT_READER_8;
                break;
            case 9:
                str = ASMUtils.TYPE_OBJECT_READER_9;
                break;
            case 10:
                str = ASMUtils.TYPE_OBJECT_READER_10;
                break;
            case 11:
                str = ASMUtils.TYPE_OBJECT_READER_11;
                break;
            case 12:
                str = ASMUtils.TYPE_OBJECT_READER_12;
                break;
            default:
                str = ASMUtils.TYPE_OBJECT_READER_ADAPTER;
                break;
        }
        String str2 = str;
        if (z4) {
            genFields(fieldReaderArr, classWriter, str2);
        }
        classWriter.visit(52, 49, objectReadContext.classNameType, str2, new String[0]);
        MethodWriter methodWriterVisitMethod = classWriter.visitMethod(1, "<init>", METHOD_DESC_INIT, fieldReaderArr.length <= 12 ? 32 : 128);
        methodWriterVisitMethod.aload(0);
        methodWriterVisitMethod.aload(1);
        if (beanInfo.typeKey != null) {
            methodWriterVisitMethod.visitLdcInsn(beanInfo.typeKey);
        } else {
            methodWriterVisitMethod.aconst_null();
        }
        methodWriterVisitMethod.aconst_null();
        boolean z5 = z4;
        methodWriterVisitMethod.visitLdcInsn(beanInfo.readerFeatures);
        methodWriterVisitMethod.aconst_null();
        methodWriterVisitMethod.aload(2);
        methodWriterVisitMethod.aconst_null();
        methodWriterVisitMethod.aload(3);
        methodWriterVisitMethod.invokespecial(str2, "<init>", METHOD_DESC_ADAPTER_INIT);
        genInitFields(fieldReaderArr, objectReadContext.classNameType, z5, 3, methodWriterVisitMethod, str2);
        methodWriterVisitMethod.return_();
        methodWriterVisitMethod.visitMaxs(3, 3);
        String str3 = (z && constructor == null) ? "createInstance0" : "createInstance";
        if ((z2 && constructor != null) || (z && (constructor == null || !Modifier.isPublic(constructor.getModifiers()) || !Modifier.isPublic(cls.getModifiers())))) {
            MethodWriter methodWriterVisitMethod2 = classWriter.visitMethod(1, str3, "(J)Ljava/lang/Object;", 32);
            methodWriterVisitMethod2.getstatic(ASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
            methodWriterVisitMethod2.aload(0);
            methodWriterVisitMethod2.getfield(ASMUtils.TYPE_OBJECT_READER_ADAPTER, "objectClass", "Ljava/lang/Class;");
            methodWriterVisitMethod2.invokevirtual("sun/misc/Unsafe", "allocateInstance", "(Ljava/lang/Class;)Ljava/lang/Object;");
            methodWriterVisitMethod2.areturn();
            methodWriterVisitMethod2.visitMaxs(3, 3);
        } else if (constructor != null && Modifier.isPublic(constructor.getModifiers()) && Modifier.isPublic(cls.getModifiers())) {
            MethodWriter methodWriterVisitMethod3 = classWriter.visitMethod(1, str3, "(J)Ljava/lang/Object;", 32);
            newObject(methodWriterVisitMethod3, objectReadContext.objectType, constructor);
            methodWriterVisitMethod3.areturn();
            methodWriterVisitMethod3.visitMaxs(3, 3);
        }
        if (constructor != null) {
            if (Modifier.isPublic(i) && !this.classLoader.isExternalClass(cls) && Modifier.isPublic(constructor.getModifiers())) {
                z3 = false;
            }
            supplierCreateSupplier = createSupplier(constructor, z3);
        } else {
            supplierCreateSupplier = supplier;
        }
        if (z5) {
            long j = beanInfo.readerFeatures;
            if (z) {
                j |= JSONReader.Feature.FieldBased.mask;
            }
            long j2 = j;
            boolean zDisableSupportArrayMapping = objectReadContext.disableSupportArrayMapping();
            boolean zDisableJSONB = objectReadContext.disableJSONB();
            fieldReaderArr2 = fieldReaderArr;
            ObjectReaderAdapter objectReaderAdapter = new ObjectReaderAdapter(cls, beanInfo.typeKey, beanInfo.typeName, j2, null, supplierCreateSupplier, null, fieldReaderArr2);
            cls2 = cls;
            objectReadContext.objectReaderAdapter = objectReaderAdapter;
            if (!zDisableJSONB) {
                genMethodReadJSONBObject(objectReadContext, j2);
                if (!zDisableSupportArrayMapping) {
                    genMethodReadJSONBObjectArrayMapping(objectReadContext, j2);
                }
            }
            genMethodReadObject(objectReadContext, j2);
            if (str2 == ASMUtils.TYPE_OBJECT_READER_ADAPTER || str2 == ASMUtils.TYPE_OBJECT_READER_1 || str2 == ASMUtils.TYPE_OBJECT_READER_2 || str2 == ASMUtils.TYPE_OBJECT_READER_3 || str2 == ASMUtils.TYPE_OBJECT_READER_4 || str2 == ASMUtils.TYPE_OBJECT_READER_5 || str2 == ASMUtils.TYPE_OBJECT_READER_6 || str2 == ASMUtils.TYPE_OBJECT_READER_7 || str2 == ASMUtils.TYPE_OBJECT_READER_8 || str2 == ASMUtils.TYPE_OBJECT_READER_9 || str2 == ASMUtils.TYPE_OBJECT_READER_10 || str2 == ASMUtils.TYPE_OBJECT_READER_11 || str2 == ASMUtils.TYPE_OBJECT_READER_12) {
                genMethodGetFieldReader(objectReadContext);
                genMethodGetFieldReaderLCase(objectReadContext);
            }
        } else {
            fieldReaderArr2 = fieldReaderArr;
            cls2 = cls;
        }
        byte[] byteArray = classWriter.toByteArray();
        try {
            return (ObjectReaderBean) this.classLoader.defineClassPublic(objectReadContext.classNameFull, byteArray, 0, byteArray.length).getConstructors()[0].newInstance(cls2, supplierCreateSupplier, fieldReaderArr2);
        } catch (Throwable th) {
            throw new JSONException("create objectReader error" + (type == null ? "" : ", objectType " + type.getTypeName()), th);
        }
    }

    static /* synthetic */ Class lambda$jitObjectReader$1(Class cls, String str) {
        if (cls.getName().equals(str)) {
            return cls;
        }
        return null;
    }

    private static void newObject(MethodWriter methodWriter, String str, Constructor constructor) {
        methodWriter.new_(str);
        methodWriter.dup();
        if (constructor.getParameterCount() == 0) {
            methodWriter.invokespecial(str, "<init>", "()V");
            return;
        }
        Class<?> cls = constructor.getParameterTypes()[0];
        methodWriter.aconst_null();
        methodWriter.invokespecial(str, "<init>", "(" + ASMUtils.desc(cls) + ")V");
    }

    private void genMethodGetFieldReader(ObjectReadContext objectReadContext) {
        ObjectReaderAdapter objectReaderAdapter = objectReadContext.objectReaderAdapter;
        FieldReader[] fieldReaderArr = objectReadContext.fieldReaders;
        int i = 1;
        MethodWriter methodWriterVisitMethod = objectReadContext.cw.visitMethod(1, "getFieldReader", "(J)" + ASMUtils.DESC_FIELD_READER, 512);
        Label label = new Label();
        if (fieldReaderArr.length > 6) {
            TreeMap treeMap = new TreeMap();
            for (int i2 = 0; i2 < objectReaderAdapter.hashCodes.length; i2++) {
                long j = objectReaderAdapter.hashCodes[i2];
                ((List) treeMap.computeIfAbsent(Integer.valueOf((int) ((j >>> 32) ^ j)), new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$$ExternalSyntheticLambda3
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ObjectReaderCreatorASM.lambda$genMethodGetFieldReader$2((Integer) obj);
                    }
                })).add(Long.valueOf(j));
            }
            int size = treeMap.size();
            int[] iArr = new int[size];
            Iterator it = treeMap.keySet().iterator();
            int i3 = 0;
            while (it.hasNext()) {
                iArr[i3] = ((Integer) it.next()).intValue();
                i3++;
            }
            Arrays.sort(iArr);
            methodWriterVisitMethod.lload(1);
            methodWriterVisitMethod.lload(1);
            methodWriterVisitMethod.bipush(32);
            methodWriterVisitMethod.lushr();
            methodWriterVisitMethod.lxor();
            methodWriterVisitMethod.l2i();
            methodWriterVisitMethod.istore(3);
            Label label2 = new Label();
            Label[] labelArr = new Label[size];
            for (int i4 = 0; i4 < size; i4++) {
                labelArr[i4] = new Label();
            }
            methodWriterVisitMethod.iload(3);
            methodWriterVisitMethod.visitLookupSwitchInsn(label2, iArr, labelArr);
            int i5 = 0;
            while (i5 < size) {
                methodWriterVisitMethod.visitLabel(labelArr[i5]);
                List list = (List) treeMap.get(Integer.valueOf(iArr[i5]));
                int size2 = list.size();
                int i6 = 0;
                while (i6 < size2) {
                    int i7 = i5;
                    long jLongValue = ((Long) list.get(i6)).longValue();
                    Label label3 = size2 > i ? new Label() : label2;
                    methodWriterVisitMethod.lload(i);
                    methodWriterVisitMethod.visitLdcInsn(jLongValue);
                    methodWriterVisitMethod.lcmp();
                    methodWriterVisitMethod.ifne(label3);
                    short s = objectReaderAdapter.mapping[Arrays.binarySearch(objectReaderAdapter.hashCodes, jLongValue)];
                    methodWriterVisitMethod.aload(0);
                    methodWriterVisitMethod.getfield(objectReadContext.classNameType, CodeGenUtils.fieldReader(s), ASMUtils.DESC_FIELD_READER);
                    methodWriterVisitMethod.goto_(label);
                    if (label3 != label2) {
                        methodWriterVisitMethod.visitLabel(label3);
                    }
                    i6++;
                    i5 = i7;
                    i = 1;
                }
                methodWriterVisitMethod.goto_(label2);
                i5++;
                i = 1;
            }
            methodWriterVisitMethod.visitLabel(label2);
        } else {
            for (int i8 = 0; i8 < fieldReaderArr.length; i8++) {
                Label label4 = new Label();
                Label label5 = new Label();
                String str = fieldReaderArr[i8].fieldName;
                long j2 = fieldReaderArr[i8].fieldNameHash;
                methodWriterVisitMethod.lload(1);
                methodWriterVisitMethod.visitLdcInsn(j2);
                methodWriterVisitMethod.lcmp();
                methodWriterVisitMethod.ifne(label4);
                methodWriterVisitMethod.visitLabel(label5);
                methodWriterVisitMethod.aload(0);
                methodWriterVisitMethod.getfield(objectReadContext.classNameType, CodeGenUtils.fieldReader(i8), ASMUtils.DESC_FIELD_READER);
                methodWriterVisitMethod.goto_(label);
                methodWriterVisitMethod.visitLabel(label4);
            }
        }
        methodWriterVisitMethod.aconst_null();
        methodWriterVisitMethod.areturn();
        methodWriterVisitMethod.visitLabel(label);
        methodWriterVisitMethod.areturn();
        methodWriterVisitMethod.visitMaxs(5, 5);
    }

    static /* synthetic */ List lambda$genMethodGetFieldReader$2(Integer num) {
        return new ArrayList();
    }

    private void genMethodGetFieldReaderLCase(ObjectReadContext objectReadContext) {
        ObjectReaderAdapter objectReaderAdapter = objectReadContext.objectReaderAdapter;
        FieldReader[] fieldReaderArr = objectReadContext.fieldReaders;
        MethodWriter methodWriterVisitMethod = objectReadContext.cw.visitMethod(1, "getFieldReaderLCase", "(J)" + ASMUtils.DESC_FIELD_READER, 512);
        Label label = new Label();
        if (fieldReaderArr.length > 6) {
            TreeMap treeMap = new TreeMap();
            for (int i = 0; i < objectReaderAdapter.hashCodesLCase.length; i++) {
                long j = objectReaderAdapter.hashCodesLCase[i];
                ((List) treeMap.computeIfAbsent(Integer.valueOf((int) ((j >>> 32) ^ j)), new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$$ExternalSyntheticLambda5
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ObjectReaderCreatorASM.lambda$genMethodGetFieldReaderLCase$3((Integer) obj);
                    }
                })).add(Long.valueOf(j));
            }
            int size = treeMap.size();
            int[] iArr = new int[size];
            Iterator it = treeMap.keySet().iterator();
            int i2 = 0;
            while (it.hasNext()) {
                iArr[i2] = ((Integer) it.next()).intValue();
                i2++;
            }
            Arrays.sort(iArr);
            methodWriterVisitMethod.lload(1);
            methodWriterVisitMethod.lload(1);
            methodWriterVisitMethod.bipush(32);
            methodWriterVisitMethod.lushr();
            methodWriterVisitMethod.lxor();
            methodWriterVisitMethod.l2i();
            methodWriterVisitMethod.istore(3);
            Label label2 = new Label();
            Label[] labelArr = new Label[size];
            for (int i3 = 0; i3 < size; i3++) {
                labelArr[i3] = new Label();
            }
            methodWriterVisitMethod.iload(3);
            methodWriterVisitMethod.visitLookupSwitchInsn(label2, iArr, labelArr);
            for (int i4 = 0; i4 < size; i4++) {
                methodWriterVisitMethod.visitLabel(labelArr[i4]);
                Iterator it2 = ((List) treeMap.get(Integer.valueOf(iArr[i4]))).iterator();
                while (it2.hasNext()) {
                    long jLongValue = ((Long) it2.next()).longValue();
                    methodWriterVisitMethod.lload(1);
                    methodWriterVisitMethod.visitLdcInsn(jLongValue);
                    methodWriterVisitMethod.lcmp();
                    methodWriterVisitMethod.ifne(label2);
                    short s = objectReaderAdapter.mappingLCase[Arrays.binarySearch(objectReaderAdapter.hashCodesLCase, jLongValue)];
                    methodWriterVisitMethod.aload(0);
                    methodWriterVisitMethod.getfield(objectReadContext.classNameType, CodeGenUtils.fieldReader(s), ASMUtils.DESC_FIELD_READER);
                    methodWriterVisitMethod.goto_(label);
                }
                methodWriterVisitMethod.goto_(label2);
            }
            methodWriterVisitMethod.visitLabel(label2);
        } else {
            for (int i5 = 0; i5 < fieldReaderArr.length; i5++) {
                Label label3 = new Label();
                Label label4 = new Label();
                String str = fieldReaderArr[i5].fieldName;
                long j2 = fieldReaderArr[i5].fieldNameHashLCase;
                methodWriterVisitMethod.lload(1);
                methodWriterVisitMethod.visitLdcInsn(j2);
                methodWriterVisitMethod.lcmp();
                methodWriterVisitMethod.ifne(label3);
                methodWriterVisitMethod.visitLabel(label4);
                methodWriterVisitMethod.aload(0);
                methodWriterVisitMethod.getfield(objectReadContext.classNameType, CodeGenUtils.fieldReader(i5), ASMUtils.DESC_FIELD_READER);
                methodWriterVisitMethod.goto_(label);
                methodWriterVisitMethod.visitLabel(label3);
            }
        }
        methodWriterVisitMethod.aconst_null();
        methodWriterVisitMethod.areturn();
        methodWriterVisitMethod.visitLabel(label);
        methodWriterVisitMethod.areturn();
        methodWriterVisitMethod.visitMaxs(5, 5);
    }

    static /* synthetic */ List lambda$genMethodGetFieldReaderLCase$3(Integer num) {
        return new ArrayList();
    }

    private void genInitFields(FieldReader[] fieldReaderArr, String str, boolean z, int i, MethodWriter methodWriter, String str2) {
        if ((str2 == ASMUtils.TYPE_OBJECT_READER_ADAPTER || str2 == ASMUtils.TYPE_OBJECT_READER_NONE_DEFAULT_CONSTRUCTOR) && z) {
            for (int i2 = 0; i2 < fieldReaderArr.length; i2++) {
                methodWriter.aload(0);
                methodWriter.aload(i);
                methodWriter.iconst_n(i2);
                methodWriter.aaload();
                methodWriter.putfield(str, CodeGenUtils.fieldReader(i2), ASMUtils.DESC_FIELD_READER);
            }
        }
    }

    private void genFields(FieldReader[] fieldReaderArr, ClassWriter classWriter, String str) {
        if (str == ASMUtils.TYPE_OBJECT_READER_ADAPTER || str == ASMUtils.TYPE_OBJECT_READER_NONE_DEFAULT_CONSTRUCTOR) {
            for (int i = 0; i < fieldReaderArr.length; i++) {
                classWriter.visitField(1, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
            }
            for (int i2 = 0; i2 < fieldReaderArr.length; i2++) {
                classWriter.visitField(1, fieldObjectReader(i2), ASMUtils.DESC_OBJECT_READER);
            }
        }
        for (int i3 = 0; i3 < fieldReaderArr.length; i3++) {
            if (List.class.isAssignableFrom(fieldReaderArr[i3].fieldClass)) {
                classWriter.visitField(1, fieldItemObjectReader(i3), ASMUtils.DESC_OBJECT_READER);
            }
        }
    }

    private <T> void genMethodReadJSONBObject(ObjectReadContext objectReadContext, long j) {
        int i;
        int i2;
        MethodWriterContext methodWriterContext;
        Label label;
        boolean z;
        Label label2;
        int i3;
        int i4;
        boolean z2;
        boolean z3;
        Label label3;
        Label label4;
        int i5;
        ObjectReaderCreatorASM objectReaderCreatorASM;
        ObjectReadContext objectReadContext2;
        String str;
        int i6;
        int i7;
        String str2 = objectReadContext.classNameType;
        FieldReader[] fieldReaderArr = objectReadContext.fieldReaders;
        Class cls = objectReadContext.objectClass;
        boolean z4 = (j & JSONReader.Feature.FieldBased.mask) != 0;
        ObjectReaderAdapter objectReaderAdapter = objectReadContext.objectReaderAdapter;
        ClassWriter classWriter = objectReadContext.cw;
        String str3 = METHOD_DESC_READ_OBJECT;
        MethodWriter methodWriterVisitMethod = classWriter.visitMethod(1, "readJSONBObject", str3, 2048);
        boolean zDisableSupportArrayMapping = objectReadContext.disableSupportArrayMapping();
        boolean zDisableAutoType = objectReadContext.disableAutoType();
        boolean z5 = z4;
        MethodWriterContext methodWriterContext2 = new MethodWriterContext(methodWriterVisitMethod, 6, true);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.lload(4);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "features", "(J)J");
        methodWriterVisitMethod.lstore(4);
        int iVar = methodWriterContext2.var("object");
        int iVar2 = methodWriterContext2.var("I");
        int iVar22 = methodWriterContext2.var2("hashCode64");
        int iVar3 = methodWriterContext2.var("hashCode32");
        int iVar4 = methodWriterContext2.var("fieldReader");
        if (!zDisableAutoType) {
            genCheckAutoType(str2, methodWriterContext2);
        }
        Label label5 = new Label();
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "nextIfNull", "()Z");
        methodWriterVisitMethod.ifeq(label5);
        methodWriterVisitMethod.aconst_null();
        methodWriterVisitMethod.areturn();
        methodWriterVisitMethod.visitLabel(label5);
        if (cls != null && !Serializable.class.isAssignableFrom(cls)) {
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.aload(0);
            methodWriterVisitMethod.getfield(str2, "objectClass", "Ljava/lang/Class;");
            methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "errorOnNoneSerializable", "(Ljava/lang/Class;)V");
        }
        if (!zDisableSupportArrayMapping) {
            Label label6 = new Label();
            new Label();
            Label label7 = new Label();
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "isArray", "()Z");
            methodWriterVisitMethod.ifeq(label6);
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "isSupportBeanArray", "()Z");
            methodWriterVisitMethod.ifeq(label7);
            methodWriterVisitMethod.aload(0);
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.aload(2);
            methodWriterVisitMethod.aload(3);
            methodWriterVisitMethod.lload(4);
            methodWriterVisitMethod.invokevirtual(str2, "readArrayMappingObject", str3);
            methodWriterVisitMethod.areturn();
            methodWriterVisitMethod.visitLabel(label7);
            methodWriterVisitMethod.visitLabel(label6);
        }
        if (objectReadContext.objectReaderAdapter instanceof ObjectReaderNoneDefaultConstructor) {
            Label label8 = new Label();
            Label label9 = new Label();
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "hasAutoTypeBeforeHandler", "()Z");
            methodWriterVisitMethod.ifne(label8);
            methodWriterVisitMethod.lload(4);
            i = iVar2;
            methodWriterVisitMethod.visitLdcInsn(JSONReader.Feature.SupportSmartMatch.mask | JSONReader.Feature.SupportAutoType.mask);
            methodWriterVisitMethod.land();
            methodWriterVisitMethod.lconst_0();
            methodWriterVisitMethod.lcmp();
            methodWriterVisitMethod.ifeq(label9);
            methodWriterVisitMethod.visitLabel(label8);
            methodWriterVisitMethod.aload(0);
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.aload(2);
            methodWriterVisitMethod.aload(3);
            methodWriterVisitMethod.lload(4);
            methodWriterVisitMethod.invokespecial(ASMUtils.TYPE_OBJECT_READER_NONE_DEFAULT_CONSTRUCTOR, "readJSONBObject", str3);
            methodWriterVisitMethod.areturn();
            methodWriterVisitMethod.visitLabel(label9);
            genInitForNonDefaultConstructor(fieldReaderArr, methodWriterContext2);
        } else {
            i = iVar2;
            genCreateObject(methodWriterVisitMethod, objectReadContext, str2);
            methodWriterVisitMethod.astore(iVar);
        }
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "nextIfObjectStart", "()Z");
        methodWriterVisitMethod.pop();
        Label label10 = new Label();
        Label label11 = new Label();
        Label label12 = new Label();
        if (zDisableAutoType) {
            i2 = i;
        } else {
            methodWriterVisitMethod.iconst_0();
            i2 = i;
            methodWriterVisitMethod.istore(i2);
        }
        methodWriterVisitMethod.visitLabel(label10);
        Label label13 = new Label();
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "nextIfObjectEnd", "()Z");
        methodWriterVisitMethod.ifne(label11);
        if (objectReadContext.fieldNameLengthMin < 2 || objectReadContext.fieldNameLengthMax > 43) {
            methodWriterContext = methodWriterContext2;
            label = label13;
            z = z5;
            label2 = label12;
            i3 = iVar;
            i4 = iVar4;
            z2 = false;
        } else {
            i4 = iVar4;
            genRead243(objectReadContext, z5, methodWriterContext2, iVar, label12, label13);
            label = label13;
            label2 = label12;
            i3 = iVar;
            methodWriterContext = methodWriterContext2;
            z = z5;
            z2 = true;
        }
        methodWriterVisitMethod.visitLabel(label);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "readFieldNameHashCode", "()J");
        methodWriterVisitMethod.dup2();
        methodWriterVisitMethod.lstore(iVar22);
        methodWriterVisitMethod.lconst_0();
        methodWriterVisitMethod.lcmp();
        methodWriterVisitMethod.ifeq(label2);
        if (zDisableAutoType || (objectReadContext.objectReaderAdapter instanceof ObjectReaderNoneDefaultConstructor)) {
            z3 = z2;
        } else {
            Label label14 = new Label();
            methodWriterVisitMethod.lload(iVar22);
            methodWriterVisitMethod.aload(0);
            z3 = z2;
            methodWriterVisitMethod.getfield(str2, "typeKeyHashCode", "J");
            methodWriterVisitMethod.lcmp();
            methodWriterVisitMethod.ifne(label14);
            methodWriterVisitMethod.lload(iVar22);
            methodWriterVisitMethod.lconst_0();
            methodWriterVisitMethod.lcmp();
            methodWriterVisitMethod.ifeq(label14);
            methodWriterVisitMethod.aload(0);
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.invokevirtual(str2, "autoType", "(" + ASMUtils.DESC_JSON_READER + ")Ljava/lang/Object;");
            methodWriterVisitMethod.astore(i3);
            methodWriterVisitMethod.goto_(label11);
            methodWriterVisitMethod.visitLabel(label14);
        }
        if (z3) {
            if (objectReadContext.objectReaderAdapter instanceof ObjectReaderNoneDefaultConstructor) {
                MethodWriterContext methodWriterContext3 = methodWriterContext;
                genReadHashCode64ValueForNonDefaultConstructor(objectReadContext, methodWriterContext3, iVar22, z, i3, label2);
                methodWriterContext = methodWriterContext3;
                label3 = label2;
            } else {
                label3 = label2;
                methodWriterVisitMethod.aload(0);
                methodWriterVisitMethod.lload(iVar22);
                methodWriterVisitMethod.aload(1);
                methodWriterVisitMethod.lload(4);
                methodWriterVisitMethod.aload(i3);
                methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_OBJECT_READER_ADAPTER, "readFieldValue", READ_FIELD_READER_UL);
            }
            methodWriterVisitMethod.goto_(label3);
            objectReaderCreatorASM = this;
            objectReadContext2 = objectReadContext;
            label4 = label11;
            i5 = i2;
        } else {
            int i8 = iVar22;
            label3 = label2;
            String str4 = "isSupportSmartMatch";
            String str5 = "getNameHashCodeLCase";
            if (fieldReaderArr.length > 6) {
                TreeMap treeMap = new TreeMap();
                String str6 = "(J)Z";
                label4 = label11;
                ObjectReaderAdapter objectReaderAdapter2 = objectReaderAdapter;
                int i9 = 0;
                while (true) {
                    str = str4;
                    if (i9 >= objectReaderAdapter2.hashCodes.length) {
                        break;
                    }
                    long j2 = objectReaderAdapter2.hashCodes[i9];
                    ((List) treeMap.computeIfAbsent(Integer.valueOf((int) (j2 ^ (j2 >>> 32))), new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$$ExternalSyntheticLambda2
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return ObjectReaderCreatorASM.lambda$genMethodReadJSONBObject$4((Integer) obj);
                        }
                    })).add(Long.valueOf(j2));
                    i9++;
                    str4 = str;
                }
                int size = treeMap.size();
                int[] iArr = new int[size];
                Iterator it = treeMap.keySet().iterator();
                int i10 = 0;
                while (it.hasNext()) {
                    iArr[i10] = ((Integer) it.next()).intValue();
                    i10++;
                }
                Arrays.sort(iArr);
                methodWriterVisitMethod.lload(i8);
                methodWriterVisitMethod.lload(i8);
                methodWriterVisitMethod.bipush(32);
                methodWriterVisitMethod.lushr();
                methodWriterVisitMethod.lxor();
                methodWriterVisitMethod.l2i();
                methodWriterVisitMethod.istore(iVar3);
                i5 = i2;
                Label label15 = new Label();
                Label[] labelArr = new Label[size];
                boolean z6 = z;
                for (int i11 = 0; i11 < size; i11++) {
                    labelArr[i11] = new Label();
                }
                methodWriterVisitMethod.iload(iVar3);
                methodWriterVisitMethod.visitLookupSwitchInsn(label15, iArr, labelArr);
                int i12 = 0;
                while (i12 < size) {
                    methodWriterVisitMethod.visitLabel(labelArr[i12]);
                    List list = (List) treeMap.get(Integer.valueOf(iArr[i12]));
                    int[] iArr2 = iArr;
                    int size2 = list.size();
                    Label[] labelArr2 = labelArr;
                    int i13 = 0;
                    while (i13 < size2) {
                        TreeMap treeMap2 = treeMap;
                        int i14 = size;
                        long jLongValue = ((Long) list.get(i13)).longValue();
                        int i15 = i12;
                        Label label16 = size2 > 1 ? new Label() : label15;
                        methodWriterVisitMethod.lload(i8);
                        methodWriterVisitMethod.visitLdcInsn(jLongValue);
                        methodWriterVisitMethod.lcmp();
                        methodWriterVisitMethod.ifne(label16);
                        Label label17 = label16;
                        short s = objectReaderAdapter2.mapping[Arrays.binarySearch(objectReaderAdapter2.hashCodes, jLongValue)];
                        int i16 = size2;
                        String str7 = str6;
                        List list2 = list;
                        boolean z7 = z6;
                        int i17 = i13;
                        ObjectReaderAdapter objectReaderAdapter3 = objectReaderAdapter2;
                        int i18 = i4;
                        int i19 = i8;
                        String str8 = str;
                        String str9 = str5;
                        genReadFieldValue(objectReadContext, fieldReaderArr[s], z7, methodWriterContext, i3, s, true);
                        methodWriterVisitMethod.goto_(label3);
                        if (label17 != label15) {
                            methodWriterVisitMethod.visitLabel(label17);
                        }
                        i13 = i17 + 1;
                        z6 = z7;
                        str5 = str9;
                        list = list2;
                        size2 = i16;
                        i12 = i15;
                        treeMap = treeMap2;
                        size = i14;
                        i4 = i18;
                        str6 = str7;
                        str = str8;
                        objectReaderAdapter2 = objectReaderAdapter3;
                        i8 = i19;
                    }
                    methodWriterVisitMethod.goto_(label3);
                    i12++;
                    iArr = iArr2;
                    labelArr = labelArr2;
                    size = size;
                    objectReaderAdapter2 = objectReaderAdapter2;
                    i8 = i8;
                    i4 = i4;
                }
                objectReadContext2 = objectReadContext;
                int i20 = i4;
                String str10 = str6;
                String str11 = str;
                String str12 = str5;
                methodWriterVisitMethod.visitLabel(label15);
                if (!objectReadContext2.disableSmartMatch() && !(objectReadContext2.objectReaderAdapter instanceof ObjectReaderNoneDefaultConstructor)) {
                    Label label18 = new Label();
                    if ((j & JSONReader.Feature.SupportSmartMatch.mask) == 0) {
                        i6 = 1;
                        methodWriterVisitMethod.aload(1);
                        methodWriterVisitMethod.lload(4);
                        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, str11, str10);
                        methodWriterVisitMethod.ifeq(label18);
                    } else {
                        i6 = 1;
                    }
                    methodWriterVisitMethod.aload(0);
                    methodWriterVisitMethod.aload(i6);
                    methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, str12, "()J");
                    methodWriterVisitMethod.invokeinterface(ASMUtils.TYPE_OBJECT_READER, "getFieldReaderLCase", METHOD_DESC_GET_FIELD_READER);
                    methodWriterVisitMethod.dup();
                    methodWriterVisitMethod.astore(i20);
                    methodWriterVisitMethod.ifnull(label18);
                    methodWriterVisitMethod.aload(i20);
                    methodWriterVisitMethod.aload(i6);
                    methodWriterVisitMethod.aload(i3);
                    methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_FIELD_READE, "readFieldValueJSONB", METHOD_DESC_READ_FIELD_VALUE);
                    methodWriterVisitMethod.goto_(label3);
                    methodWriterVisitMethod.visitLabel(label18);
                }
                objectReaderCreatorASM = this;
            } else {
                label4 = label11;
                i5 = i2;
                int i21 = 0;
                while (i21 < fieldReaderArr.length) {
                    Label label19 = new Label();
                    FieldReader fieldReader = fieldReaderArr[i21];
                    long jHashCode64 = Fnv.hashCode64(fieldReader.fieldName);
                    methodWriterVisitMethod.lload(i8);
                    methodWriterVisitMethod.visitLdcInsn(jHashCode64);
                    methodWriterVisitMethod.lcmp();
                    methodWriterVisitMethod.ifne(label19);
                    genReadFieldValue(objectReadContext, fieldReader, z, methodWriterContext, i3, i21, false);
                    methodWriterVisitMethod.goto_(label3);
                    methodWriterVisitMethod.visitLabel(label19);
                    i21++;
                    fieldReaderArr = fieldReaderArr;
                }
                FieldReader[] fieldReaderArr2 = fieldReaderArr;
                Label label20 = new Label();
                if ((j & JSONReader.Feature.SupportSmartMatch.mask) == 0) {
                    methodWriterVisitMethod.aload(1);
                    methodWriterVisitMethod.lload(4);
                    methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "isSupportSmartMatch", "(J)Z");
                    methodWriterVisitMethod.ifeq(label20);
                }
                methodWriterVisitMethod.aload(1);
                methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "getNameHashCodeLCase", "()J");
                methodWriterVisitMethod.lstore(i8);
                for (int i22 = 0; i22 < fieldReaderArr2.length; i22++) {
                    Label label21 = new Label();
                    FieldReader fieldReader2 = fieldReaderArr2[i22];
                    long jHashCode642 = Fnv.hashCode64(fieldReader2.fieldName);
                    methodWriterVisitMethod.lload(i8);
                    methodWriterVisitMethod.visitLdcInsn(jHashCode642);
                    methodWriterVisitMethod.lcmp();
                    methodWriterVisitMethod.ifne(label21);
                    genReadFieldValue(objectReadContext, fieldReader2, z, methodWriterContext, i3, i22, false);
                    methodWriterVisitMethod.goto_(label3);
                    methodWriterVisitMethod.visitLabel(label21);
                }
                objectReaderCreatorASM = this;
                objectReadContext2 = objectReadContext;
                methodWriterVisitMethod.visitLabel(label20);
            }
        }
        if (objectReadContext2.objectReaderAdapter instanceof ObjectReaderNoneDefaultConstructor) {
            i7 = 1;
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "skipValue", "()V");
        } else {
            i7 = 1;
            methodWriterVisitMethod.aload(0);
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.aload(i3);
            methodWriterVisitMethod.lload(4);
            methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_OBJECT_READER_ADAPTER, "processExtra", METHOD_DESC_PROCESS_EXTRA);
        }
        methodWriterVisitMethod.goto_(label3);
        methodWriterVisitMethod.visitLabel(label3);
        if (!zDisableAutoType) {
            methodWriterVisitMethod.visitIincInsn(i5, i7);
        }
        methodWriterVisitMethod.goto_(label10);
        methodWriterVisitMethod.visitLabel(label4);
        if (objectReadContext2.objectReaderAdapter instanceof ObjectReaderNoneDefaultConstructor) {
            objectReaderCreatorASM.createObjectForNonConstructor(objectReadContext2, methodWriterContext);
        } else {
            methodWriterVisitMethod.aload(i3);
        }
        methodWriterVisitMethod.areturn();
        methodWriterVisitMethod.visitMaxs(5, 10);
    }

    static /* synthetic */ List lambda$genMethodReadJSONBObject$4(Integer num) {
        return new ArrayList();
    }

    private void genReadHashCode64ValueForNonDefaultConstructor(ObjectReadContext objectReadContext, MethodWriterContext methodWriterContext, int i, boolean z, int i2, Label label) {
        FieldReader[] fieldReaderArr = objectReadContext.fieldReaders;
        MethodWriter methodWriter = methodWriterContext.mw;
        methodWriter.aload(0);
        methodWriter.lload(i);
        methodWriter.invokevirtual(ASMUtils.TYPE_OBJECT_READER_ADAPTER, "getFieldOrdinal", "(J)I");
        Label label2 = new Label();
        Label[] labelArr = new Label[fieldReaderArr.length];
        int[] iArr = new int[fieldReaderArr.length];
        for (int i3 = 0; i3 < fieldReaderArr.length; i3++) {
            labelArr[i3] = new Label();
            iArr[i3] = i3;
        }
        methodWriter.visitLookupSwitchInsn(label2, iArr, labelArr);
        for (int i4 = 0; i4 < fieldReaderArr.length; i4++) {
            methodWriter.visitLabel(labelArr[i4]);
            genReadFieldValue(objectReadContext, fieldReaderArr[i4], z, methodWriterContext, i2, i4, false);
            methodWriter.goto_(label);
        }
        methodWriter.visitLabel(label2);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "skipValue", "()V");
    }

    private <T> void genMethodReadJSONBObjectArrayMapping(ObjectReadContext objectReadContext, long j) {
        ObjectReadContext objectReadContext2 = objectReadContext;
        FieldReader[] fieldReaderArr = objectReadContext2.fieldReaders;
        String str = objectReadContext2.classNameType;
        boolean z = (j & JSONReader.Feature.FieldBased.mask) != 0;
        MethodWriter methodWriterVisitMethod = objectReadContext2.cw.visitMethod(1, "readArrayMappingJSONBObject", METHOD_DESC_READ_OBJECT, 512);
        MethodWriterContext methodWriterContext = new MethodWriterContext(methodWriterVisitMethod, 6, true);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.lload(4);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "features", "(J)J");
        methodWriterVisitMethod.lstore(4);
        int iVar = methodWriterContext.var("object");
        int iVar2 = methodWriterContext.var("entryCnt");
        if (!objectReadContext2.disableAutoType()) {
            genCheckAutoType(str, methodWriterContext);
        }
        Label label = new Label();
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "nextIfNull", "()Z");
        methodWriterVisitMethod.ifeq(label);
        methodWriterVisitMethod.aconst_null();
        methodWriterVisitMethod.areturn();
        methodWriterVisitMethod.visitLabel(label);
        genCreateObject(methodWriterVisitMethod, objectReadContext2, str);
        methodWriterVisitMethod.astore(iVar);
        Label label2 = new Label();
        Label label3 = new Label();
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_READER, "startArray", "()I");
        methodWriterVisitMethod.dup();
        methodWriterVisitMethod.istore(iVar2);
        methodWriterVisitMethod.visitLdcInsn(fieldReaderArr.length);
        methodWriterVisitMethod.if_icmpne(label3);
        int i = 0;
        while (i < fieldReaderArr.length) {
            genReadFieldValue(objectReadContext2, fieldReaderArr[i], z, methodWriterContext, iVar, i, true);
            i++;
            objectReadContext2 = objectReadContext;
        }
        methodWriterVisitMethod.goto_(label2);
        methodWriterVisitMethod.visitLabel(label3);
        methodWriterVisitMethod.aload(0);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.aload(iVar);
        methodWriterVisitMethod.iload(iVar2);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_OBJECT_READER_ADAPTER, "readArrayMappingJSONBObject0", METHOD_DESC_READ_ARRAY_MAPPING_JSONB_OBJECT0);
        methodWriterVisitMethod.visitLabel(label2);
        methodWriterVisitMethod.aload(iVar);
        methodWriterVisitMethod.areturn();
        methodWriterVisitMethod.visitMaxs(5, 10);
    }

    private void genCheckAutoType(String str, MethodWriterContext methodWriterContext) {
        MethodWriter methodWriter = methodWriterContext.mw;
        int iVar = methodWriterContext.var("autoTypeObjectReader");
        Label label = new Label();
        methodWriter.aload(0);
        methodWriter.aload(1);
        methodWriter.lload(4);
        methodWriter.invokevirtual(str, "checkAutoType", METHOD_DESC_JSON_READER_CHECK_ARRAY_AUTO_TYPE);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnull(label);
        methodWriter.aload(iVar);
        methodWriter.aload(1);
        methodWriter.aload(2);
        methodWriter.aload(3);
        methodWriter.lload(4);
        methodWriter.invokeinterface(ASMUtils.TYPE_OBJECT_READER, "readJSONBObject", METHOD_DESC_READ_OBJECT);
        methodWriter.areturn();
        methodWriter.visitLabel(label);
    }

    /* JADX WARN: Removed duplicated region for block: B:112:0x05c3  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x05f7  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0612  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0616  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x028f  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x02e3  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x031d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private <T> void genMethodReadObject(com.alibaba.fastjson2.reader.ObjectReaderCreatorASM.ObjectReadContext r38, long r39) {
        /*
            Method dump skipped, instruction units count: 1571
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM.genMethodReadObject(com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$ObjectReadContext, long):void");
    }

    static /* synthetic */ List lambda$genMethodReadObject$5(Integer num) {
        return new ArrayList();
    }

    private void createObjectForNonConstructor(ObjectReadContext objectReadContext, MethodWriterContext methodWriterContext) {
        FieldReader[] fieldReaderArr = objectReadContext.fieldReaders;
        MethodWriter methodWriter = methodWriterContext.mw;
        ObjectReaderNoneDefaultConstructor objectReaderNoneDefaultConstructor = (ObjectReaderNoneDefaultConstructor) objectReadContext.objectReaderAdapter;
        int i = 0;
        if (!this.classLoader.isExternalClass(objectReadContext.objectClass) && objectReadContext.objectClass.getTypeParameters().length == 0 && ((objectReaderNoneDefaultConstructor.constructor == null || Modifier.isPublic(objectReaderNoneDefaultConstructor.constructor.getModifiers())) && ((objectReadContext.objectClass == null || Modifier.isPublic(objectReadContext.objectClass.getModifiers())) && objectReaderNoneDefaultConstructor.factoryFunction == null && (objectReaderNoneDefaultConstructor.noneDefaultConstructor == null || Modifier.isPublic(objectReaderNoneDefaultConstructor.noneDefaultConstructor.getModifiers()))))) {
            methodWriter.new_(objectReadContext.objectType);
            methodWriter.dup();
            StringBuilder sb = new StringBuilder("(");
            int length = fieldReaderArr.length;
            while (i < length) {
                FieldReader fieldReader = fieldReaderArr[i];
                methodWriter.loadLocal(fieldReader.fieldClass, methodWriterContext.var(fieldReader));
                sb.append(ASMUtils.desc(fieldReader.fieldClass));
                i++;
            }
            sb.append(")V");
            methodWriter.invokespecial(objectReadContext.objectType, "<init>", sb.toString());
            return;
        }
        methodWriter.aload(0);
        methodWriter.iconst_n(fieldReaderArr.length);
        methodWriter.anewArray(ASMUtils.TYPE_OBJECT);
        while (i < fieldReaderArr.length) {
            FieldReader fieldReader2 = fieldReaderArr[i];
            methodWriter.dup();
            methodWriter.iconst_n(i);
            methodWriter.loadLocal(fieldReader2.fieldClass, methodWriterContext.var(fieldReader2));
            if (fieldReader2.fieldClass == Integer.TYPE) {
                methodWriter.invokestatic("java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            } else if (fieldReader2.fieldClass == Long.TYPE) {
                methodWriter.invokestatic("java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
            } else if (fieldReader2.fieldClass == Float.TYPE) {
                methodWriter.invokestatic("java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
            } else if (fieldReader2.fieldClass == Double.TYPE) {
                methodWriter.invokestatic("java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            } else if (fieldReader2.fieldClass == Boolean.TYPE) {
                methodWriter.invokestatic("java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
            } else if (fieldReader2.fieldClass == Short.TYPE) {
                methodWriter.invokestatic("java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
            } else if (fieldReader2.fieldClass == Byte.TYPE) {
                methodWriter.invokestatic("java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
            } else if (fieldReader2.fieldClass == Character.TYPE) {
                methodWriter.invokestatic("java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
            }
            methodWriter.aastore();
            i++;
        }
        methodWriter.invokevirtual(ASMUtils.TYPE_OBJECT_READER_NONE_DEFAULT_CONSTRUCTOR, "createInstance", "([Ljava/lang/Object;)Ljava/lang/Object;");
    }

    private static void genInitForNonDefaultConstructor(FieldReader[] fieldReaderArr, MethodWriterContext methodWriterContext) {
        MethodWriter methodWriter = methodWriterContext.mw;
        for (FieldReader fieldReader : fieldReaderArr) {
            Class cls = fieldReader.fieldClass;
            int iVar = methodWriterContext.var(fieldReader);
            if (cls == Byte.TYPE || cls == Short.TYPE || cls == Integer.TYPE || cls == Boolean.TYPE || cls == Character.TYPE) {
                methodWriter.iconst_0();
                methodWriter.istore(iVar);
            } else if (cls == Long.TYPE) {
                methodWriter.lconst_0();
                methodWriter.lstore(iVar);
            } else if (cls == Float.TYPE) {
                methodWriter.iconst_0();
                methodWriter.i2f();
                methodWriter.fstore(iVar);
            } else if (cls == Double.TYPE) {
                methodWriter.iconst_0();
                methodWriter.i2d();
                methodWriter.dstore(iVar);
            } else {
                methodWriter.aconst_null();
                methodWriter.astore(iVar);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:139:0x0e96  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0e98  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0eba  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0ebd A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void genRead243(com.alibaba.fastjson2.reader.ObjectReaderCreatorASM.ObjectReadContext r52, boolean r53, com.alibaba.fastjson2.reader.ObjectReaderCreatorASM.MethodWriterContext r54, int r55, com.alibaba.fastjson2.internal.asm.Label r56, com.alibaba.fastjson2.internal.asm.Label r57) {
        /*
            Method dump skipped, instruction units count: 3930
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM.genRead243(com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$ObjectReadContext, boolean, com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$MethodWriterContext, int, com.alibaba.fastjson2.internal.asm.Label, com.alibaba.fastjson2.internal.asm.Label):void");
    }

    private void genRead57(ObjectReadContext objectReadContext, boolean z, MethodWriterContext methodWriterContext, int i, Label label, Label label2) {
        String str;
        MethodWriterContext methodWriterContext2 = methodWriterContext;
        ObjectReadContext objectReadContext2 = objectReadContext;
        FieldReader[] fieldReaderArr = objectReadContext2.fieldReaders;
        int iVar2 = methodWriterContext2.var2("RAW_LONG");
        MethodWriter methodWriter = methodWriterContext2.mw;
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "getRawLong", "()J");
        methodWriter.dup2();
        methodWriter.lstore(iVar2);
        methodWriter.lconst_0();
        methodWriter.lcmp();
        methodWriter.ifeq(label2);
        int i2 = 0;
        int i3 = 0;
        while (i3 < fieldReaderArr.length) {
            Label label3 = new Label();
            FieldReader fieldReader = fieldReaderArr[i3];
            byte[] bytes = fieldReader.fieldName.getBytes(StandardCharsets.UTF_8);
            int length = bytes.length;
            byte[] bArr = new byte[8];
            if (length == 5) {
                bArr[i2] = 34;
                System.arraycopy(bytes, i2, bArr, 1, 5);
                bArr[6] = 34;
                bArr[7] = 58;
                str = "nextIfName8Match0";
            } else if (length == 6) {
                bArr[i2] = 34;
                System.arraycopy(bytes, i2, bArr, 1, 6);
                bArr[7] = 34;
                str = "nextIfName8Match1";
            } else if (length == 7) {
                bArr[i2] = 34;
                System.arraycopy(bytes, i2, bArr, 1, 7);
                str = "nextIfName8Match2";
            } else {
                throw new IllegalStateException("length " + length);
            }
            long j = JDKUtils.UNSAFE.getLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
            methodWriter.lload(iVar2);
            methodWriter.visitLdcInsn(j);
            methodWriter.lcmp();
            methodWriter.ifne(label3);
            methodWriter.aload(1);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, str, "()Z");
            methodWriter.ifeq(label2);
            genReadFieldValue(objectReadContext2, fieldReader, z, methodWriterContext2, i, i3, false);
            methodWriter.goto_(label);
            methodWriter.visitLabel(label3);
            i3++;
            objectReadContext2 = objectReadContext;
            methodWriterContext2 = methodWriterContext;
            i2 = 0;
        }
    }

    private <T> void genCreateObject(MethodWriter methodWriter, ObjectReadContext objectReadContext, String str) {
        Constructor constructor = objectReadContext.defaultConstructor;
        Supplier<T> supplier = objectReadContext.objectReaderAdapter.creator;
        Class<?> cls = objectReadContext.objectClass;
        boolean z = Modifier.isPublic(cls == null ? 1 : cls.getModifiers()) && (cls == null || !this.classLoader.isExternalClass(cls));
        if (constructor != null && z && Modifier.isPublic(constructor.getModifiers())) {
            newObject(methodWriter, objectReadContext.objectType, objectReadContext.defaultConstructor);
        } else {
            if (supplier != null) {
                methodWriter.aload(0);
                methodWriter.getfield(str, "creator", ASMUtils.DESC_SUPPLIER);
                methodWriter.invokeinterface("java/util/function/Supplier", "get", "()Ljava/lang/Object;");
            } else {
                methodWriter.aload(0);
                methodWriter.aload(1);
                methodWriter.lload(4);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "features", "(J)J");
                methodWriter.invokevirtual(str, "createInstance", "(J)Ljava/lang/Object;");
            }
            if (z) {
                methodWriter.checkcast(objectReadContext.objectType);
            }
        }
        if (objectReadContext.hasStringField) {
            Label label = new Label();
            methodWriter.lload(4);
            methodWriter.visitLdcInsn(JSONReader.Feature.InitStringFieldAsEmpty.mask);
            methodWriter.land();
            methodWriter.lconst_0();
            methodWriter.lcmp();
            methodWriter.ifeq(label);
            methodWriter.dup();
            methodWriter.aload(0);
            methodWriter.swap();
            methodWriter.invokevirtual(str, "initStringFieldAsEmpty", "(Ljava/lang/Object;)V");
            methodWriter.visitLabel(label);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:364:0x07fd  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x083a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private <T> void genReadFieldValue(com.alibaba.fastjson2.reader.ObjectReaderCreatorASM.ObjectReadContext r47, com.alibaba.fastjson2.reader.FieldReader r48, boolean r49, com.alibaba.fastjson2.reader.ObjectReaderCreatorASM.MethodWriterContext r50, int r51, int r52, boolean r53) {
        /*
            Method dump skipped, instruction units count: 2123
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM.genReadFieldValue(com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$ObjectReadContext, com.alibaba.fastjson2.reader.FieldReader, boolean, com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$MethodWriterContext, int, int, boolean):void");
    }

    private void genReadObject(FieldReader fieldReader, String str, MethodWriterContext methodWriterContext, int i, Type type, long j, String str2) {
        Label label = new Label();
        MethodWriter methodWriter = methodWriterContext.mw;
        boolean z = methodWriterContext.jsonb;
        methodWriter.aload(0);
        methodWriter.getfield(str, str2, ASMUtils.DESC_OBJECT_READER);
        methodWriter.ifnonnull(label);
        methodWriter.aload(0);
        methodWriter.aload(0);
        methodWriter.getfield(str, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_READE, "getObjectReader", METHOD_DESC_GET_OBJECT_READER_1);
        methodWriter.putfield(str, str2, ASMUtils.DESC_OBJECT_READER);
        methodWriter.visitLabel(label);
        methodWriter.aload(0);
        methodWriter.getfield(str, str2, ASMUtils.DESC_OBJECT_READER);
        methodWriter.aload(1);
        gwGetFieldType(str, methodWriter, i, type);
        methodWriter.visitLdcInsn(fieldReader.fieldName);
        methodWriter.visitLdcInsn(j);
        methodWriter.invokeinterface(ASMUtils.TYPE_OBJECT_READER, z ? "readJSONBObject" : "readObject", METHOD_DESC_READ_OBJECT);
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x02a7  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x02a9  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x02d8  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x02db A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void genReadEnumValueRaw(com.alibaba.fastjson2.reader.FieldReader r32, java.lang.String r33, com.alibaba.fastjson2.reader.ObjectReaderCreatorASM.MethodWriterContext r34, int r35, java.lang.reflect.Type r36, java.lang.Class r37, long r38, java.lang.String r40) {
        /*
            Method dump skipped, instruction units count: 852
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM.genReadEnumValueRaw(com.alibaba.fastjson2.reader.FieldReader, java.lang.String, com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$MethodWriterContext, int, java.lang.reflect.Type, java.lang.Class, long, java.lang.String):void");
    }

    private void genReadFieldValueList(FieldReader fieldReader, String str, MethodWriterContext methodWriterContext, int i, int i2, boolean z, Class cls, Class cls2, Type type, long j, Type type2, String str2, ObjectReadContext objectReadContext, boolean z2) {
        Class cls3;
        int iVar;
        String str3;
        String str4;
        Type type3;
        String str5;
        String str6;
        int i3;
        ObjectReaderCreatorASM objectReaderCreatorASM;
        FieldReader fieldReader2;
        int i4;
        Type type4;
        Label label;
        int i5;
        String str7;
        String str8;
        String str9;
        String str10;
        Label label2;
        boolean z3 = methodWriterContext.jsonb;
        Type type5 = type2 == null ? Object.class : type2;
        Class<?> mapping = TypeUtils.getMapping(type5);
        String strFieldItemObjectReader = fieldItemObjectReader(i2);
        MethodWriter methodWriter = methodWriterContext.mw;
        if (objectReadContext.objectReaderAdapter instanceof ObjectReaderNoneDefaultConstructor) {
            iVar = methodWriterContext.var(fieldReader);
            cls3 = cls2;
        } else {
            cls3 = cls2;
            iVar = methodWriterContext.var(cls3);
        }
        Integer numValueOf = Integer.valueOf(methodWriterContext.var(ObjectReader.class));
        String str11 = cls3.isInterface() ? "java/util/ArrayList" : str2;
        Label label3 = new Label();
        Label label4 = new Label();
        Label label5 = new Label();
        Type type6 = type5;
        boolean z4 = JDKUtils.JVM_VERSION == 8 && "java/util/ArrayList".equals(str11);
        int iVar2 = methodWriterContext.var("ITEM_CNT");
        boolean z5 = z4;
        String str12 = "java/util/List";
        String str13 = "(Ljava/lang/Object;)Z";
        String str14 = "readString";
        if (z3) {
            if (objectReadContext.disableAutoType()) {
                str7 = "(I)V";
                str8 = "()V";
                str9 = "<init>";
                str10 = str11;
            } else {
                str8 = "()V";
                Label label6 = new Label();
                str7 = "(I)V";
                methodWriter.aload(0);
                str9 = "<init>";
                methodWriter.getfield(str, CodeGenUtils.fieldReader(i2), ASMUtils.DESC_FIELD_READER);
                methodWriter.aload(1);
                str10 = str11;
                methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_READE, "checkObjectAutoType", METHOD_DESC_CHECK_ARRAY_AUTO_TYPE);
                methodWriter.dup();
                methodWriter.astore(numValueOf.intValue());
                methodWriter.ifnull(label6);
                methodWriter.aload(numValueOf.intValue());
                methodWriter.aload(1);
                gwGetFieldType(str, methodWriter, i2, type);
                methodWriter.visitLdcInsn(fieldReader.fieldName);
                methodWriter.visitLdcInsn(j);
                methodWriter.invokeinterface(ASMUtils.TYPE_OBJECT_READER, "readJSONBObject", METHOD_DESC_READ_OBJECT);
                methodWriter.checkcast(str2);
                methodWriter.astore(iVar);
                methodWriter.goto_(label3);
                methodWriter.visitLabel(label6);
            }
            methodWriter.aload(1);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "startArray", "()I");
            methodWriter.dup();
            methodWriter.istore(iVar2);
            methodWriter.visitLdcInsn(-1);
            methodWriter.if_icmpne(label4);
            methodWriter.aconst_null();
            methodWriter.astore(iVar);
            methodWriter.goto_(label3);
            methodWriter.visitLabel(label4);
            if (fieldReader.method != null || fieldReader.field == null) {
                label2 = label5;
            } else {
                long jObjectFieldOffset = JDKUtils.UNSAFE.objectFieldOffset(fieldReader.field);
                methodWriter.getstatic(ASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
                methodWriter.aload(i);
                methodWriter.visitLdcInsn(jObjectFieldOffset);
                methodWriter.invokevirtual("sun/misc/Unsafe", "getObject", "(Ljava/lang/Object;J)Ljava/lang/Object;");
                methodWriter.dup();
                methodWriter.checkcast(str2);
                methodWriter.astore(iVar);
                Label label7 = new Label();
                methodWriter.ifnull(label7);
                methodWriter.aload(iVar);
                methodWriter.invokevirtual(ASMUtils.TYPE_OBJECT, "getClass", "()Ljava/lang/Class;");
                methodWriter.getstatic("java/util/Collections", "EMPTY_LIST", "Ljava/util/List;");
                methodWriter.invokevirtual(ASMUtils.TYPE_OBJECT, "getClass", "()Ljava/lang/Class;");
                label2 = label5;
                methodWriter.if_acmpne(label2);
                methodWriter.visitLabel(label7);
            }
            String str15 = str10;
            methodWriter.new_(str15);
            methodWriter.dup();
            if (z5) {
                methodWriter.iload(iVar2);
                methodWriter.invokespecial(str15, str9, str7);
            } else {
                methodWriter.invokespecial(str15, str9, str8);
            }
            methodWriter.astore(iVar);
            methodWriter.visitLabel(label2);
            str3 = "readJSONBObject";
            type3 = type6;
            str5 = "add";
            str4 = "()Z";
        } else {
            String str16 = str11;
            Label label8 = new Label();
            Label label9 = new Label();
            Label label10 = new Label();
            methodWriter.aload(1);
            str3 = "readJSONBObject";
            str4 = "()Z";
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "nextIfNull", str4);
            methodWriter.ifne(label10);
            methodWriter.aload(1);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "nextIfArrayStart", str4);
            methodWriter.ifne(label8);
            if (mapping == String.class) {
                methodWriter.aload(1);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "isString", str4);
                methodWriter.ifeq(label9);
                methodWriter.new_(str16);
                methodWriter.dup();
                if (z5) {
                    methodWriter.visitLdcInsn(10);
                    methodWriter.invokespecial(str16, "<init>", "(I)V");
                } else {
                    methodWriter.invokespecial(str16, "<init>", "()V");
                }
                methodWriter.astore(iVar);
                methodWriter.aload(1);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "nextIfNullOrEmptyString", str4);
                methodWriter.ifne(label3);
                methodWriter.aload(iVar);
                methodWriter.aload(1);
                if (mapping == String.class) {
                    str6 = str14;
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, str6, "()Ljava/lang/String;");
                } else {
                    str6 = str14;
                }
                methodWriter.invokeinterface(str12, "add", str13);
                methodWriter.pop();
                methodWriter.goto_(label3);
                str13 = str13;
                str12 = str12;
                str14 = str6;
                str5 = "add";
                type3 = type6;
            } else {
                type3 = type6;
                str5 = "add";
                if (type3 instanceof Class) {
                    methodWriter.aload(1);
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "nextIfNullOrEmptyString", str4);
                    methodWriter.ifne(label10);
                    methodWriter.new_(str16);
                    methodWriter.dup();
                    if (z5) {
                        methodWriter.visitLdcInsn(10);
                        methodWriter.invokespecial(str16, "<init>", "(I)V");
                    } else {
                        methodWriter.invokespecial(str16, "<init>", "()V");
                    }
                    methodWriter.astore(iVar);
                    methodWriter.aload(1);
                    methodWriter.aload(iVar);
                    methodWriter.visitLdcInsn((Class) type3);
                    str14 = str14;
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "readArray", "(Ljava/util/List;Ljava/lang/reflect/Type;)V");
                    methodWriter.goto_(label3);
                } else {
                    str14 = str14;
                }
            }
            methodWriter.visitLabel(label9);
            methodWriter.aload(1);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "skipValue", "()V");
            methodWriter.visitLabel(label10);
            methodWriter.aconst_null();
            methodWriter.astore(iVar);
            methodWriter.goto_(label3);
            methodWriter.visitLabel(label8);
            methodWriter.new_(str16);
            methodWriter.dup();
            if (z5) {
                methodWriter.visitLdcInsn(10);
                methodWriter.invokespecial(str16, "<init>", "(I)V");
            } else {
                methodWriter.invokespecial(str16, "<init>", "()V");
            }
            methodWriter.astore(iVar);
        }
        int iVar3 = methodWriterContext.var("J");
        Label label11 = new Label();
        Label label12 = new Label();
        Label label13 = new Label();
        methodWriter.iconst_0();
        methodWriter.istore(iVar3);
        methodWriter.visitLabel(label11);
        if (z3) {
            methodWriter.iload(iVar3);
            methodWriter.iload(iVar2);
            methodWriter.if_icmpge(label12);
            i3 = 1;
        } else {
            i3 = 1;
            methodWriter.aload(1);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "nextIfArrayEnd", str4);
            methodWriter.ifne(label12);
        }
        if (type3 == String.class) {
            methodWriter.aload(iVar);
            methodWriter.aload(i3);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, str14, "()Ljava/lang/String;");
        } else if (type3 == Integer.class) {
            methodWriter.aload(iVar);
            methodWriter.aload(i3);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "readInt32", "()Ljava/lang/Integer;");
        } else if (type3 == Long.class) {
            methodWriter.aload(iVar);
            methodWriter.aload(i3);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "readInt64", "()Ljava/lang/Long;");
        } else {
            Label label14 = new Label();
            methodWriter.aload(0);
            methodWriter.getfield(str, strFieldItemObjectReader, ASMUtils.DESC_OBJECT_READER);
            methodWriter.ifnonnull(label14);
            methodWriter.aload(0);
            methodWriter.aload(0);
            methodWriter.getfield(str, CodeGenUtils.fieldReader(i2), ASMUtils.DESC_FIELD_READER);
            methodWriter.aload(1);
            methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_READE, "getItemObjectReader", METHOD_DESC_GET_ITEM_OBJECT_READER);
            methodWriter.putfield(str, strFieldItemObjectReader, ASMUtils.DESC_OBJECT_READER);
            methodWriter.visitLabel(label14);
            if (!objectReadContext.disableReferenceDetect()) {
                methodWriter.aload(1);
                methodWriter.aload(iVar);
                methodWriter.iload(iVar3);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "readReference", "(Ljava/util/List;I)Z");
                methodWriter.ifne(label13);
            }
            methodWriter.aload(iVar);
            Label label15 = new Label();
            Label label16 = new Label();
            if (z) {
                methodWriter.aload(1);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_READER, "isArray", str4);
                methodWriter.ifeq(label15);
                methodWriter.aload(0);
                methodWriter.getfield(str, strFieldItemObjectReader, ASMUtils.DESC_OBJECT_READER);
                methodWriter.aload(1);
                objectReaderCreatorASM = this;
                i4 = i2;
                type4 = type;
                objectReaderCreatorASM.gwGetFieldType(str, methodWriter, i4, type4);
                fieldReader2 = fieldReader;
                i5 = iVar;
                methodWriter.visitLdcInsn(fieldReader2.fieldName);
                methodWriter.lload(4);
                label = label3;
                methodWriter.invokeinterface(ASMUtils.TYPE_OBJECT_READER, z3 ? "readArrayMappingJSONBObject" : "readArrayMappingObject", METHOD_DESC_READ_OBJECT);
                methodWriter.goto_(label16);
                methodWriter.visitLabel(label15);
            } else {
                objectReaderCreatorASM = this;
                fieldReader2 = fieldReader;
                i4 = i2;
                type4 = type;
                label = label3;
                i5 = iVar;
            }
            methodWriter.aload(0);
            methodWriter.getfield(str, strFieldItemObjectReader, ASMUtils.DESC_OBJECT_READER);
            methodWriter.aload(1);
            objectReaderCreatorASM.gwGetFieldType(str, methodWriter, i4, type4);
            methodWriter.visitLdcInsn(fieldReader2.fieldName);
            methodWriter.lload(4);
            methodWriter.invokeinterface(ASMUtils.TYPE_OBJECT_READER, z3 ? str3 : "readObject", METHOD_DESC_READ_OBJECT);
            if (z) {
                methodWriter.visitLabel(label16);
            }
            methodWriter.invokeinterface(str12, str5, str13);
            methodWriter.pop();
            methodWriter.visitLabel(label13);
            methodWriter.visitIincInsn(iVar3, 1);
            methodWriter.goto_(label11);
            methodWriter.visitLabel(label12);
            methodWriter.visitLabel(label);
            methodWriter.aload(i5);
        }
        label = label3;
        i5 = iVar;
        methodWriter.invokeinterface(str12, str5, str13);
        methodWriter.pop();
        methodWriter.visitLabel(label13);
        methodWriter.visitIincInsn(iVar3, 1);
        methodWriter.goto_(label11);
        methodWriter.visitLabel(label12);
        methodWriter.visitLabel(label);
        methodWriter.aload(i5);
    }

    private void gwGetFieldType(String str, MethodWriter methodWriter, int i, Type type) {
        if (type instanceof Class) {
            Class cls = (Class) type;
            String name = cls.getName();
            boolean zIsPublic = Modifier.isPublic(cls.getModifiers());
            boolean z = name.startsWith("java.") || cls == JSONArray.class || cls == JSONObject.class;
            if (zIsPublic && z) {
                methodWriter.visitLdcInsn(cls);
                return;
            }
        }
        methodWriter.aload(0);
        methodWriter.getfield(str, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
        methodWriter.getfield(ASMUtils.TYPE_FIELD_READE, "fieldType", "Ljava/lang/reflect/Type;");
    }

    static class ObjectReadContext {
        final BeanInfo beanInfo;
        final String classNameFull;
        final String classNameType;
        final ClassWriter cw;
        final Constructor defaultConstructor;
        final boolean externalClass;
        final int fieldNameLengthMax;
        final int fieldNameLengthMin;
        final FieldReader[] fieldReaders;
        final boolean hasStringField;
        final Class objectClass;
        ObjectReaderAdapter objectReaderAdapter;
        final String objectType;
        final boolean publicClass;

        public ObjectReadContext(BeanInfo beanInfo, Class cls, ClassWriter classWriter, boolean z, FieldReader[] fieldReaderArr, Constructor constructor) {
            this.beanInfo = beanInfo;
            this.objectClass = cls;
            this.cw = classWriter;
            this.publicClass = cls == null || Modifier.isPublic(cls.getModifiers());
            this.externalClass = z;
            this.fieldReaders = fieldReaderArr;
            this.defaultConstructor = constructor;
            this.objectType = cls == null ? ASMUtils.TYPE_OBJECT : ASMUtils.type(cls);
            boolean z2 = false;
            int iMin = 0;
            int iMax = 0;
            for (int i = 0; i < fieldReaderArr.length; i++) {
                FieldReader fieldReader = fieldReaderArr[i];
                z2 = fieldReader.fieldClass == String.class ? true : z2;
                byte[] bytes = fieldReader.fieldName.getBytes(StandardCharsets.UTF_8);
                int length = bytes.length;
                int length2 = bytes.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length2) {
                        break;
                    }
                    if (bytes[i2] <= 0) {
                        length = -1;
                        break;
                    }
                    i2++;
                }
                if (i == 0) {
                    iMin = length;
                    iMax = iMin;
                } else {
                    iMin = Math.min(length, iMin);
                    iMax = Math.max(length, iMax);
                }
            }
            this.hasStringField = z2;
            this.fieldNameLengthMin = iMin;
            this.fieldNameLengthMax = iMax;
            String str = "ORG_" + ObjectReaderCreatorASM.seed.incrementAndGet() + "_" + fieldReaderArr.length + (cls == null ? "" : "_" + cls.getSimpleName());
            if (ObjectReaderCreatorASM.class.getPackage() != null) {
                String str2 = ObjectReaderCreatorASM.packageName + '.' + str;
                this.classNameFull = str2;
                this.classNameType = str2.replace('.', '/');
            } else {
                this.classNameType = str;
                this.classNameFull = str;
            }
        }

        public boolean disableSupportArrayMapping() {
            return (this.beanInfo.readerFeatures & FieldInfo.DISABLE_ARRAY_MAPPING) != 0;
        }

        public boolean disableReferenceDetect() {
            return (this.beanInfo.readerFeatures & FieldInfo.DISABLE_REFERENCE_DETECT) != 0;
        }

        public boolean disableAutoType() {
            return (this.beanInfo.readerFeatures & FieldInfo.DISABLE_AUTO_TYPE) != 0;
        }

        public boolean disableJSONB() {
            return (this.beanInfo.readerFeatures & FieldInfo.DISABLE_JSONB) != 0;
        }

        public boolean disableSmartMatch() {
            return (this.beanInfo.readerFeatures & FieldInfo.DISABLE_SMART_MATCH) != 0;
        }
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderCreator
    public Function<Consumer, ByteArrayValueConsumer> createByteArrayValueConsumerCreator(Class cls, FieldReader[] fieldReaderArr) {
        return createValueConsumer0(cls, fieldReaderArr, true);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderCreator
    public Function<Consumer, CharArrayValueConsumer> createCharArrayValueConsumerCreator(Class cls, FieldReader[] fieldReaderArr) {
        return createValueConsumer0(cls, fieldReaderArr, false);
    }

    private Function createValueConsumer0(final Class cls, FieldReader[] fieldReaderArr, boolean z) {
        String strReplace;
        String str;
        String str2;
        String strDesc;
        String str3;
        String str4;
        FieldReader[] fieldReaderArr2 = fieldReaderArr;
        Constructor defaultConstructor = BeanUtils.getDefaultConstructor(cls, false);
        if (defaultConstructor == null || !Modifier.isPublic(cls.getModifiers())) {
            return null;
        }
        ClassWriter classWriter = new ClassWriter(new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ObjectReaderCreatorASM.lambda$createValueConsumer0$6(cls, (String) obj);
            }
        });
        String str5 = (z ? "VBACG_" : "VCACG_") + seed.incrementAndGet() + "_" + fieldReaderArr2.length + "_" + cls.getSimpleName();
        if (ObjectReaderCreatorASM.class.getPackage() != null) {
            str5 = packageName + '.' + str5;
            strReplace = str5.replace('.', '/');
        } else {
            strReplace = str5;
        }
        String strType = ASMUtils.type(cls);
        String strDesc2 = ASMUtils.desc(cls);
        classWriter.visitField(17, "consumer", "Ljava/util/function/Consumer;");
        classWriter.visitField(1, "object", strDesc2);
        String[] strArr = new String[1];
        strArr[0] = z ? ASMUtils.TYPE_BYTE_ARRAY_VALUE_CONSUMER : ASMUtils.TYPE_CHAR_ARRAY_VALUE_CONSUMER;
        String str6 = "object";
        classWriter.visit(52, 49, strReplace, ASMUtils.TYPE_OBJECT, strArr);
        MethodWriter methodWriterVisitMethod = classWriter.visitMethod(1, "<init>", "(Ljava/util/function/Consumer;)V", 32);
        methodWriterVisitMethod.aload(0);
        methodWriterVisitMethod.invokespecial(ASMUtils.TYPE_OBJECT, "<init>", "()V");
        methodWriterVisitMethod.aload(0);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.putfield(strReplace, "consumer", "Ljava/util/function/Consumer;");
        methodWriterVisitMethod.return_();
        methodWriterVisitMethod.visitMaxs(3, 3);
        MethodWriter methodWriterVisitMethod2 = classWriter.visitMethod(1, "beforeRow", "(I)V", 32);
        methodWriterVisitMethod2.aload(0);
        newObject(methodWriterVisitMethod2, strType, defaultConstructor);
        methodWriterVisitMethod2.putfield(strReplace, str6, strDesc2);
        methodWriterVisitMethod2.return_();
        methodWriterVisitMethod2.visitMaxs(3, 3);
        MethodWriter methodWriterVisitMethod3 = classWriter.visitMethod(1, "afterRow", "(I)V", 32);
        methodWriterVisitMethod3.aload(0);
        methodWriterVisitMethod3.getfield(strReplace, "consumer", "Ljava/util/function/Consumer;");
        methodWriterVisitMethod3.aload(0);
        methodWriterVisitMethod3.getfield(strReplace, str6, strDesc2);
        methodWriterVisitMethod3.invokeinterface("java/util/function/Consumer", "accept", "(Ljava/lang/Object;)V");
        methodWriterVisitMethod3.aload(0);
        methodWriterVisitMethod3.aconst_null();
        methodWriterVisitMethod3.putfield(strReplace, str6, strDesc2);
        methodWriterVisitMethod3.return_();
        methodWriterVisitMethod3.visitMaxs(3, 3);
        if (z) {
            str = "(II[BIILjava/nio/charset/Charset;)V";
        } else {
            str = "(II[CII)V";
        }
        MethodWriter methodWriterVisitMethod4 = classWriter.visitMethod(1, "accept", str, 32);
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        methodWriterVisitMethod4.iload(5);
        methodWriterVisitMethod4.ifne(label2);
        methodWriterVisitMethod4.return_();
        methodWriterVisitMethod4.visitLabel(label2);
        methodWriterVisitMethod4.iload(2);
        methodWriterVisitMethod4.ifge(label3);
        methodWriterVisitMethod4.return_();
        methodWriterVisitMethod4.visitLabel(label3);
        methodWriterVisitMethod4.iload(2);
        methodWriterVisitMethod4.visitLdcInsn(fieldReaderArr2.length);
        methodWriterVisitMethod4.if_icmple(label);
        methodWriterVisitMethod4.return_();
        methodWriterVisitMethod4.visitLabel(label);
        Label label4 = new Label();
        int length = fieldReaderArr2.length;
        Label[] labelArr = new Label[length];
        int length2 = fieldReaderArr2.length;
        int[] iArr = new int[length2];
        for (int i = 0; i < length2; i++) {
            iArr[i] = i;
            labelArr[i] = new Label();
        }
        methodWriterVisitMethod4.iload(2);
        methodWriterVisitMethod4.visitLookupSwitchInsn(label4, iArr, labelArr);
        int i2 = 0;
        while (i2 < length) {
            methodWriterVisitMethod4.visitLabel(labelArr[i2]);
            FieldReader fieldReader = fieldReaderArr2[i2];
            Field field = fieldReader.field;
            Class cls2 = fieldReader.fieldClass;
            int i3 = i2;
            Type type = fieldReader.fieldType;
            ClassWriter classWriter2 = classWriter;
            methodWriterVisitMethod4.aload(0);
            methodWriterVisitMethod4.getfield(strReplace, str6, strDesc2);
            String str7 = strDesc2;
            String str8 = str6;
            if (type == Integer.class || type == Integer.TYPE || type == Short.class || type == Short.TYPE || type == Byte.class || type == Byte.TYPE) {
                str2 = strReplace;
                methodWriterVisitMethod4.aload(3);
                methodWriterVisitMethod4.iload(4);
                methodWriterVisitMethod4.iload(5);
                methodWriterVisitMethod4.invokestatic(ASMUtils.TYPE_TYPE_UTILS, "parseInt", z ? "([BII)I" : "([CII)I");
                if (type == Short.TYPE) {
                    strDesc = ExifInterface.LATITUDE_SOUTH;
                    str3 = "(S)V";
                } else if (type == Short.class) {
                    methodWriterVisitMethod4.invokestatic("java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
                    strDesc = "Ljava/lang/Short;";
                    str3 = "(Ljava/lang/Short;)V";
                } else if (type == Byte.TYPE) {
                    strDesc = "B";
                    str3 = "(B)V";
                } else if (type == Byte.class) {
                    methodWriterVisitMethod4.invokestatic("java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
                    strDesc = "Ljava/lang/Byte;";
                    str3 = "(Ljava/lang/Byte;)V";
                } else if (type == Integer.TYPE) {
                    strDesc = "I";
                    str3 = "(I)V";
                } else {
                    methodWriterVisitMethod4.invokestatic("java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                    strDesc = "Ljava/lang/Integer;";
                    str3 = "(Ljava/lang/Integer;)V";
                }
            } else {
                str3 = "(J)V";
                str2 = strReplace;
                if (type == Long.class || type == Long.TYPE) {
                    methodWriterVisitMethod4.aload(3);
                    methodWriterVisitMethod4.iload(4);
                    methodWriterVisitMethod4.iload(5);
                    methodWriterVisitMethod4.invokestatic(ASMUtils.TYPE_TYPE_UTILS, "parseLong", z ? "([BII)J" : "([CII)J");
                    if (type == Long.TYPE) {
                        strDesc = "J";
                    } else {
                        methodWriterVisitMethod4.invokestatic("java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
                        strDesc = "Ljava/lang/Long;";
                        str3 = "(Ljava/lang/Long;)V";
                    }
                } else if (type == Float.class || type == Float.TYPE) {
                    methodWriterVisitMethod4.aload(3);
                    methodWriterVisitMethod4.iload(4);
                    methodWriterVisitMethod4.iload(5);
                    methodWriterVisitMethod4.invokestatic(ASMUtils.TYPE_TYPE_UTILS, "parseFloat", z ? "([BII)F" : "([CII)F");
                    if (type == Float.TYPE) {
                        strDesc = "F";
                        str3 = "(F)V";
                    } else {
                        methodWriterVisitMethod4.invokestatic("java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
                        strDesc = "Ljava/lang/Float;";
                        str3 = "(Ljava/lang/Float;)V";
                    }
                } else if (type == Double.class || type == Double.TYPE) {
                    methodWriterVisitMethod4.aload(3);
                    methodWriterVisitMethod4.iload(4);
                    methodWriterVisitMethod4.iload(5);
                    methodWriterVisitMethod4.invokestatic(ASMUtils.TYPE_TYPE_UTILS, "parseDouble", z ? "([BII)D" : "([CII)D");
                    if (type == Double.TYPE) {
                        strDesc = "D";
                        str3 = "(D)V";
                    } else {
                        methodWriterVisitMethod4.invokestatic("java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
                        strDesc = "Ljava/lang/Double;";
                        str3 = "(Ljava/lang/Double;)V";
                    }
                } else if (type == Boolean.class || type == Boolean.TYPE) {
                    methodWriterVisitMethod4.aload(3);
                    methodWriterVisitMethod4.iload(4);
                    methodWriterVisitMethod4.iload(5);
                    methodWriterVisitMethod4.invokestatic(ASMUtils.TYPE_TYPE_UTILS, "parseBoolean", z ? "([BII)Ljava/lang/Boolean;" : "([CII)Ljava/lang/Boolean;");
                    if (type == Boolean.TYPE) {
                        methodWriterVisitMethod4.invokevirtual("java/lang/Boolean", "booleanValue", "()Z");
                        strDesc = "Z";
                        str3 = "(Z)V";
                    } else {
                        strDesc = "Ljava/lang/Boolean;";
                        str3 = "(Ljava/lang/Boolean;)V";
                    }
                } else if (type == Date.class) {
                    methodWriterVisitMethod4.new_("java/util/Date");
                    methodWriterVisitMethod4.dup();
                    methodWriterVisitMethod4.aload(3);
                    methodWriterVisitMethod4.iload(4);
                    methodWriterVisitMethod4.iload(5);
                    if (!z) {
                        methodWriterVisitMethod4.invokestatic(ASMUtils.TYPE_DATE_UTILS, "parseMillis", "([CII)J");
                    } else {
                        methodWriterVisitMethod4.aload(6);
                        methodWriterVisitMethod4.invokestatic(ASMUtils.TYPE_DATE_UTILS, "parseMillis", "([BIILjava/nio/charset/Charset;)J");
                    }
                    methodWriterVisitMethod4.invokespecial("java/util/Date", "<init>", "(J)V");
                    strDesc = "Ljava/util/Date;";
                    str3 = "(Ljava/util/Date;)V";
                } else if (type == BigDecimal.class) {
                    methodWriterVisitMethod4.aload(3);
                    methodWriterVisitMethod4.iload(4);
                    methodWriterVisitMethod4.iload(5);
                    methodWriterVisitMethod4.invokestatic(ASMUtils.TYPE_TYPE_UTILS, "parseBigDecimal", z ? "([BII)Ljava/math/BigDecimal;" : "([CII)Ljava/math/BigDecimal;");
                    strDesc = "Ljava/math/BigDecimal;";
                    str3 = "(Ljava/math/BigDecimal;)V";
                } else {
                    methodWriterVisitMethod4.new_("java/lang/String");
                    methodWriterVisitMethod4.dup();
                    methodWriterVisitMethod4.aload(3);
                    methodWriterVisitMethod4.iload(4);
                    methodWriterVisitMethod4.iload(5);
                    if (!z) {
                        methodWriterVisitMethod4.invokespecial("java/lang/String", "<init>", "([CII)V");
                    } else {
                        methodWriterVisitMethod4.aload(6);
                        methodWriterVisitMethod4.invokespecial("java/lang/String", "<init>", "([BIILjava/nio/charset/Charset;)V");
                    }
                    if (type == String.class) {
                        strDesc = "Ljava/lang/String;";
                        str3 = "(Ljava/lang/String;)V";
                    } else {
                        strDesc = ASMUtils.desc(cls2);
                        if (cls2 == Character.TYPE) {
                            str4 = "(C)V";
                        } else {
                            str4 = "(" + strDesc + ")V";
                        }
                        str3 = str4;
                        methodWriterVisitMethod4.visitLdcInsn(cls2);
                        methodWriterVisitMethod4.invokestatic(ASMUtils.TYPE_TYPE_UTILS, "cast", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
                        methodWriterVisitMethod4.checkcast(ASMUtils.type(cls2));
                    }
                }
            }
            if (fieldReader.method != null) {
                if (fieldReader.method.getReturnType() != Void.TYPE) {
                    return null;
                }
                methodWriterVisitMethod4.invokevirtual(strType, fieldReader.method.getName(), str3);
            } else {
                if (field == null) {
                    return null;
                }
                methodWriterVisitMethod4.putfield(strType, field.getName(), strDesc);
            }
            methodWriterVisitMethod4.goto_(label4);
            i2 = i3 + 1;
            fieldReaderArr2 = fieldReaderArr;
            classWriter = classWriter2;
            strDesc2 = str7;
            str6 = str8;
            strReplace = str2;
        }
        methodWriterVisitMethod4.visitLabel(label4);
        methodWriterVisitMethod4.return_();
        methodWriterVisitMethod4.visitMaxs(3, 3);
        byte[] byteArray = classWriter.toByteArray();
        try {
            final Constructor<?> constructor = this.classLoader.defineClassPublic(str5, byteArray, 0, byteArray.length).getConstructor(Consumer.class);
            return new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderCreatorASM$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ObjectReaderCreatorASM.lambda$createValueConsumer0$7(constructor, obj);
                }
            };
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    static /* synthetic */ Class lambda$createValueConsumer0$6(Class cls, String str) {
        if (cls.getName().equals(str)) {
            return cls;
        }
        return null;
    }

    static /* synthetic */ Object lambda$createValueConsumer0$7(Constructor constructor, Object obj) {
        try {
            return constructor.newInstance(obj);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new JSONException("create ByteArrayValueConsumer error", e);
        }
    }

    static class MethodWriterContext {
        static final int FEATURES = 4;
        static final int FIELD_NAME = 3;
        static final int FIELD_TYPE = 2;
        static final int JSON_READER = 1;
        final boolean jsonb;
        int maxVariant;
        final MethodWriter mw;
        final Map<Object, Integer> variants = new LinkedHashMap();

        public MethodWriterContext(MethodWriter methodWriter, int i, boolean z) {
            this.mw = methodWriter;
            this.maxVariant = i;
            this.jsonb = z;
        }

        int var(Object obj) {
            Integer numValueOf = this.variants.get(obj);
            if (numValueOf == null) {
                numValueOf = Integer.valueOf(this.maxVariant);
                this.variants.put(obj, numValueOf);
                if (obj == Long.TYPE || obj == Double.TYPE) {
                    this.maxVariant += 2;
                } else {
                    this.maxVariant++;
                }
            }
            return numValueOf.intValue();
        }

        int var(FieldReader fieldReader) {
            return var("_param_" + fieldReader.fieldName, fieldReader.fieldClass);
        }

        int var(String str, Class cls) {
            Integer numValueOf = this.variants.get(str);
            if (numValueOf == null) {
                numValueOf = Integer.valueOf(this.maxVariant);
                this.variants.put(str, numValueOf);
                if (cls == Long.TYPE || cls == Double.TYPE) {
                    this.maxVariant += 2;
                } else {
                    this.maxVariant++;
                }
            }
            return numValueOf.intValue();
        }

        int var2(Object obj) {
            Integer numValueOf = this.variants.get(obj);
            if (numValueOf == null) {
                numValueOf = Integer.valueOf(this.maxVariant);
                this.variants.put(obj, numValueOf);
                this.maxVariant += 2;
            }
            return numValueOf.intValue();
        }
    }
}
