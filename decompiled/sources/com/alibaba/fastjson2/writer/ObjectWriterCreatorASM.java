package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.SymbolTable;
import com.alibaba.fastjson2.codec.BeanInfo;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.internal.asm.ASMUtils;
import com.alibaba.fastjson2.internal.asm.ClassWriter;
import com.alibaba.fastjson2.internal.asm.Label;
import com.alibaba.fastjson2.internal.asm.MethodWriter;
import com.alibaba.fastjson2.modules.ObjectWriterModule;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.DynamicClassLoader;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class ObjectWriterCreatorASM extends ObjectWriterCreator {
    static final String CONTEXT_FEATURES = "CONTEXT_FEATURES";
    static final String DESC_SYMBOL;
    static final int JSON_WRITER = 1;
    static final String METHOD_DESC_FIELD_WRITE_OBJECT;
    static final String METHOD_DESC_GET_ITEM_WRITER;
    static final String METHOD_DESC_GET_OBJECT_WRITER;
    static final String METHOD_DESC_HAS_FILTER;
    static final String METHOD_DESC_IO_WRITE_REFERENCE;
    static final String METHOD_DESC_SET_PATH2;
    static final String METHOD_DESC_WRITE;
    static final String METHOD_DESC_WRITE_BArray;
    static final String METHOD_DESC_WRITE_CArray;
    static final String METHOD_DESC_WRITE_CLASS_INFO;
    static final String METHOD_DESC_WRITE_D;
    static final String METHOD_DESC_WRITE_DARRAY;
    static final String METHOD_DESC_WRITE_DATE_WITH_FIELD_NAME;
    static final String METHOD_DESC_WRITE_ENUM;
    static final String METHOD_DESC_WRITE_F;
    static final String METHOD_DESC_WRITE_FARRAY;
    static final String METHOD_DESC_WRITE_FIELD_NAME;
    static final String METHOD_DESC_WRITE_FIELD_NAME_JSONB;
    static final String METHOD_DESC_WRITE_I;
    static final String METHOD_DESC_WRITE_J;
    static final String METHOD_DESC_WRITE_LIST;
    static final String METHOD_DESC_WRITE_LIST_VALUE_JSONB;
    static final String METHOD_DESC_WRITE_NAME_SYMBOL;
    static final String METHOD_DESC_WRITE_OBJECT;
    static final String METHOD_DESC_WRITE_REFERENCE = "(Ljava/lang/String;)V";
    static final String METHOD_DESC_WRITE_SArray;
    static final String METHOD_DESC_WRITE_TYPE_INFO;
    static final String METHOD_DESC_WRITE_VALUE;
    static final String METHOD_DESC_WRITE_Z;
    static final String METHOD_DESC_WRITE_ZARRAY;
    static final String NAME_DIRECT = "NAME_DIRECT";
    static final String NOT_WRITE_DEFAULT_VALUE = "WRITE_DEFAULT_VALUE";
    static final int THIS = 0;
    static final String WRITE_NULLS = "WRITE_NULLS";
    protected final DynamicClassLoader classLoader;
    public static final ObjectWriterCreatorASM INSTANCE = new ObjectWriterCreatorASM(DynamicClassLoader.getInstance());
    protected static final AtomicLong seed = new AtomicLong();
    static final String[] INTERFACES = {ASMUtils.TYPE_OBJECT_WRITER};

    static {
        String strDesc = ASMUtils.desc(SymbolTable.class);
        DESC_SYMBOL = strDesc;
        METHOD_DESC_WRITE_VALUE = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Object;)V";
        METHOD_DESC_WRITE = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;J)V";
        METHOD_DESC_WRITE_FIELD_NAME = "(" + ASMUtils.DESC_JSON_WRITER + ")V";
        METHOD_DESC_WRITE_OBJECT = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;J)V";
        METHOD_DESC_WRITE_J = "(" + ASMUtils.DESC_JSON_WRITER + "J)V";
        METHOD_DESC_WRITE_D = "(" + ASMUtils.DESC_JSON_WRITER + "D)V";
        METHOD_DESC_WRITE_F = "(" + ASMUtils.DESC_JSON_WRITER + "F)V";
        METHOD_DESC_WRITE_DATE_WITH_FIELD_NAME = "(" + ASMUtils.DESC_JSON_WRITER + "ZLjava/util/Date;)V";
        METHOD_DESC_WRITE_Z = "(" + ASMUtils.DESC_JSON_WRITER + "Z)V";
        METHOD_DESC_WRITE_ZARRAY = "(" + ASMUtils.DESC_JSON_WRITER + "[Z)V";
        METHOD_DESC_WRITE_FARRAY = "(" + ASMUtils.DESC_JSON_WRITER + "[F)V";
        METHOD_DESC_WRITE_DARRAY = "(" + ASMUtils.DESC_JSON_WRITER + "[D)V";
        METHOD_DESC_WRITE_I = "(" + ASMUtils.DESC_JSON_WRITER + "I)V";
        METHOD_DESC_WRITE_SArray = "(" + ASMUtils.DESC_JSON_WRITER + "[S)V";
        METHOD_DESC_WRITE_BArray = "(" + ASMUtils.DESC_JSON_WRITER + "[B)V";
        METHOD_DESC_WRITE_CArray = "(" + ASMUtils.DESC_JSON_WRITER + "[C)V";
        METHOD_DESC_WRITE_ENUM = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Enum;)V";
        METHOD_DESC_WRITE_LIST = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/util/List;)V";
        METHOD_DESC_FIELD_WRITE_OBJECT = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Object;)Z";
        METHOD_DESC_GET_OBJECT_WRITER = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Class;)" + ASMUtils.DESC_OBJECT_WRITER;
        METHOD_DESC_GET_ITEM_WRITER = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/reflect/Type;)" + ASMUtils.DESC_OBJECT_WRITER;
        METHOD_DESC_WRITE_TYPE_INFO = "(" + ASMUtils.DESC_JSON_WRITER + ")Z";
        METHOD_DESC_HAS_FILTER = "(" + ASMUtils.DESC_JSON_WRITER + ")Z";
        METHOD_DESC_SET_PATH2 = "(" + ASMUtils.DESC_FIELD_WRITER + "Ljava/lang/Object;)Ljava/lang/String;";
        METHOD_DESC_IO_WRITE_REFERENCE = "([BILjava/lang/String;" + ASMUtils.DESC_JSON_WRITER + ")I";
        METHOD_DESC_WRITE_CLASS_INFO = "(" + ASMUtils.DESC_JSON_WRITER + ")V";
        METHOD_DESC_WRITE_FIELD_NAME_JSONB = "([BI" + ASMUtils.DESC_JSON_WRITER + ")I";
        METHOD_DESC_WRITE_NAME_SYMBOL = "(" + strDesc + ")I";
        METHOD_DESC_WRITE_LIST_VALUE_JSONB = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/util/List;)V";
    }

    static String fieldWriter(int i) {
        switch (i) {
            case 0:
                return "fieldWriter0";
            case 1:
                return "fieldWriter1";
            case 2:
                return "fieldWriter2";
            case 3:
                return "fieldWriter3";
            case 4:
                return "fieldWriter4";
            case 5:
                return "fieldWriter5";
            case 6:
                return "fieldWriter6";
            case 7:
                return "fieldWriter7";
            case 8:
                return "fieldWriter8";
            case 9:
                return "fieldWriter9";
            case 10:
                return "fieldWriter10";
            case 11:
                return "fieldWriter11";
            case 12:
                return "fieldWriter12";
            case 13:
                return "fieldWriter13";
            case 14:
                return "fieldWriter14";
            case 15:
                return "fieldWriter15";
            default:
                int length = "fieldWriter".length() + IOUtils.stringSize(i);
                char[] cArr = new char[length];
                "fieldWriter".getChars(0, "fieldWriter".length(), cArr, 0);
                IOUtils.getChars(i, length, cArr);
                return new String(cArr);
        }
    }

    public ObjectWriterCreatorASM() {
        this.classLoader = new DynamicClassLoader();
    }

    public ObjectWriterCreatorASM(ClassLoader classLoader) {
        DynamicClassLoader dynamicClassLoader;
        if (classLoader instanceof DynamicClassLoader) {
            dynamicClassLoader = (DynamicClassLoader) classLoader;
        } else {
            dynamicClassLoader = new DynamicClassLoader(classLoader);
        }
        this.classLoader = dynamicClassLoader;
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriterCreator
    public ObjectWriter createObjectWriter(List<FieldWriter> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFunction() == null) {
                return super.createObjectWriter(list);
            }
        }
        ObjectWriterProvider defaultObjectWriterProvider = JSONFactory.getDefaultObjectWriterProvider();
        return jitWriter(null, defaultObjectWriterProvider, defaultObjectWriterProvider.createBeanInfo(), list, 0L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.writer.ObjectWriterCreator
    public ObjectWriter createObjectWriter(Class cls, long j, final ObjectWriterProvider objectWriterProvider) {
        final ObjectWriterCreatorASM objectWriterCreatorASM = this;
        final Class cls2 = cls;
        int modifiers = cls2.getModifiers();
        boolean zIsExternalClass = objectWriterCreatorASM.classLoader.isExternalClass(cls2);
        boolean zIsPublic = Modifier.isPublic(modifiers);
        final BeanInfo beanInfoCreateBeanInfo = objectWriterProvider.createBeanInfo();
        objectWriterProvider.getBeanInfo(beanInfoCreateBeanInfo, cls2);
        if (beanInfoCreateBeanInfo.serializer != null && ObjectWriter.class.isAssignableFrom(beanInfoCreateBeanInfo.serializer)) {
            try {
                Constructor declaredConstructor = beanInfoCreateBeanInfo.serializer.getDeclaredConstructor(new Class[0]);
                declaredConstructor.setAccessible(true);
                return (ObjectWriter) declaredConstructor.newInstance(new Object[0]);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw new JSONException("create serializer error", e);
            }
        }
        long j2 = beanInfoCreateBeanInfo.writerFeatures;
        if (beanInfoCreateBeanInfo.seeAlso != null) {
            j2 &= ~JSONWriter.Feature.WriteClassName.mask;
        }
        final boolean zIsRecord = BeanUtils.isRecord(cls2);
        final long j3 = j | j2 | (zIsRecord ? FieldInfo.RECORD : 0L);
        boolean z = (((JSONWriter.Feature.FieldBased.mask & j3) == 0 || cls2.isInterface()) && beanInfoCreateBeanInfo.alphabetic) ? false : true;
        if (Throwable.class.isAssignableFrom(cls2) || BeanUtils.isExtendedMap(cls2) || beanInfoCreateBeanInfo.rootName != null) {
            return super.createObjectWriter(cls, j, objectWriterProvider);
        }
        final LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (!z || zIsRecord) {
            ArrayList<FieldWriter> arrayList = new ArrayList();
            Iterator<ObjectWriterModule> it = objectWriterProvider.modules.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().createFieldWriters(objectWriterCreatorASM, cls2, arrayList)) {
                        for (FieldWriter fieldWriter : arrayList) {
                            if (fieldWriter.method == null) {
                                return super.createObjectWriter(cls2, j3, objectWriterProvider);
                            }
                            linkedHashMap.putIfAbsent(fieldWriter.fieldName, fieldWriter);
                        }
                    }
                } else {
                    final FieldInfo fieldInfo = new FieldInfo();
                    if (!zIsRecord) {
                        BeanUtils.declaredFields(cls2, new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                this.f$0.m68x56930f12(fieldInfo, cls2, j3, objectWriterProvider, beanInfoCreateBeanInfo, linkedHashMap, (Field) obj);
                            }
                        });
                    }
                    Class mixIn = objectWriterProvider.getMixIn(cls2);
                    boolean z2 = beanInfoCreateBeanInfo.f4kotlin;
                    objectWriterCreatorASM = this;
                    Consumer consumer = new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            this.f$0.m69xe3cdc093(fieldInfo, j3, beanInfoCreateBeanInfo, objectWriterProvider, cls2, zIsRecord, linkedHashMap, (Method) obj);
                        }
                    };
                    cls2 = cls2;
                    linkedHashMap = linkedHashMap;
                    beanInfoCreateBeanInfo = beanInfoCreateBeanInfo;
                    BeanUtils.getters(cls2, mixIn, z2, consumer);
                }
            }
        } else {
            final FieldInfo fieldInfo2 = new FieldInfo();
            BeanUtils.declaredFields(cls2, new Consumer() { // from class: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m70x71087214(fieldInfo2, cls2, j3, objectWriterProvider, beanInfoCreateBeanInfo, linkedHashMap, (Field) obj);
                }
            });
        }
        ArrayList arrayList2 = new ArrayList(linkedHashMap.values());
        objectWriterCreatorASM.handleIgnores(beanInfoCreateBeanInfo, arrayList2);
        if (beanInfoCreateBeanInfo.alphabetic) {
            try {
                Collections.sort(arrayList2);
            } catch (Exception e2) {
                StringBuilder sbAppend = new StringBuilder("fieldWriters sort error, objectClass ").append(cls2.getName()).append(", fields ");
                JSONArray jSONArray = new JSONArray();
                for (FieldWriter fieldWriter2 : arrayList2) {
                    jSONArray.add(JSONObject.of("name", (Object) fieldWriter2.fieldName, "type", (Object) fieldWriter2.fieldClass, "ordinal", (Object) Integer.valueOf(fieldWriter2.ordinal), "field", (Object) fieldWriter2.field, "method", (Object) fieldWriter2.method));
                }
                sbAppend.append(jSONArray);
                throw new JSONException(sbAppend.toString(), e2);
            }
        }
        boolean z3 = arrayList2.size() < 100 && !Throwable.class.isAssignableFrom(cls2);
        if (!zIsPublic || zIsExternalClass) {
            Iterator<FieldWriter> it2 = arrayList2.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                if (it2.next().method != null) {
                    z3 = false;
                    break;
                }
            }
        }
        for (FieldWriter fieldWriter3 : arrayList2) {
            if (fieldWriter3.getInitWriter() != null || (fieldWriter3.features & FieldInfo.VALUE_MASK) != 0 || (fieldWriter3.features & FieldInfo.RAW_VALUE_MASK) != 0) {
                z3 = false;
                break;
            }
        }
        if (cls2.getSuperclass() == Object.class) {
            String simpleName = cls2.getSimpleName();
            if (simpleName.indexOf(36) != -1 && simpleName.contains("$$")) {
                z3 = false;
            }
        }
        boolean z4 = arrayList2.size() > 64 ? false : z3;
        long j4 = j | beanInfoCreateBeanInfo.writerFeatures;
        if (!z4) {
            return super.createObjectWriter(cls, j, objectWriterProvider);
        }
        objectWriterCreatorASM.setDefaultValue(arrayList2, cls2);
        return objectWriterCreatorASM.jitWriter(cls2, objectWriterProvider, beanInfoCreateBeanInfo, arrayList2, j4);
    }

    /* JADX INFO: renamed from: lambda$createObjectWriter$0$com-alibaba-fastjson2-writer-ObjectWriterCreatorASM, reason: not valid java name */
    /* synthetic */ void m68x56930f12(FieldInfo fieldInfo, Class cls, long j, ObjectWriterProvider objectWriterProvider, BeanInfo beanInfo, Map map, Field field) {
        fieldInfo.init();
        boolean z = (field.getModifiers() & 1) == 0;
        fieldInfo.isPrivate = z;
        fieldInfo.ignore = z;
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
    /* JADX WARN: Removed duplicated region for block: B:109:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0157  */
    /* JADX WARN: Type inference failed for: r0v42, types: [java.lang.String[]] */
    /* JADX WARN: Type inference failed for: r14v3, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r14v7 */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v15 */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.alibaba.fastjson2.writer.ObjectWriterCreatorASM] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.alibaba.fastjson2.writer.ObjectWriterCreatorASM] */
    /* JADX WARN: Type inference failed for: r1v6, types: [com.alibaba.fastjson2.writer.ObjectWriterCreatorASM] */
    /* JADX WARN: Type inference failed for: r4v1, types: [java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v3, types: [java.lang.String] */
    /* JADX INFO: renamed from: lambda$createObjectWriter$1$com-alibaba-fastjson2-writer-ObjectWriterCreatorASM, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    /* synthetic */ void m69xe3cdc093(com.alibaba.fastjson2.codec.FieldInfo r18, long r19, com.alibaba.fastjson2.codec.BeanInfo r21, com.alibaba.fastjson2.writer.ObjectWriterProvider r22, java.lang.Class r23, boolean r24, java.util.Map r25, java.lang.reflect.Method r26) {
        /*
            Method dump skipped, instruction units count: 441
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.m69xe3cdc093(com.alibaba.fastjson2.codec.FieldInfo, long, com.alibaba.fastjson2.codec.BeanInfo, com.alibaba.fastjson2.writer.ObjectWriterProvider, java.lang.Class, boolean, java.util.Map, java.lang.reflect.Method):void");
    }

    /* JADX INFO: renamed from: lambda$createObjectWriter$2$com-alibaba-fastjson2-writer-ObjectWriterCreatorASM, reason: not valid java name */
    /* synthetic */ void m70x71087214(FieldInfo fieldInfo, Class cls, long j, ObjectWriterProvider objectWriterProvider, BeanInfo beanInfo, Map map, Field field) {
        fieldInfo.init();
        FieldWriter fieldWriterCreateFieldWriter = createFieldWriter(cls, j, objectWriterProvider, beanInfo, fieldInfo, field);
        if (fieldWriterCreateFieldWriter != null) {
            if (fieldInfo.writeUsing != null && (fieldWriterCreateFieldWriter instanceof FieldWriterObject)) {
                ((FieldWriterObject) fieldWriterCreateFieldWriter).writeUsing = true;
            }
            map.put(fieldWriterCreateFieldWriter.fieldName, fieldWriterCreateFieldWriter);
        }
    }

    private ObjectWriterAdapter jitWriter(Class cls, ObjectWriterProvider objectWriterProvider, BeanInfo beanInfo, List<FieldWriter> list, long j) {
        String str;
        String str2;
        String str3;
        ObjectWriterCreatorASM objectWriterCreatorASM;
        Class cls2;
        List<FieldWriter> list2;
        List<FieldWriterGroup> listBuildGroups = buildGroups(beanInfo.writerFeatures, list);
        ClassWriter classWriter = new ClassWriter(null);
        String str4 = "OWG_" + seed.incrementAndGet() + "_" + list.size() + (cls == null ? "" : "_" + cls.getSimpleName());
        Package r3 = ObjectWriterCreatorASM.class.getPackage();
        if (r3 != null) {
            String name = r3.getName();
            int length = name.length();
            int i = length + 1;
            char[] cArr = new char[str4.length() + i];
            name.getChars(0, name.length(), cArr, 0);
            cArr[length] = '.';
            str4.getChars(0, str4.length(), cArr, i);
            String str5 = new String(cArr);
            cArr[length] = '/';
            for (int i2 = 0; i2 < length; i2++) {
                if (cArr[i2] == '.') {
                    cArr[i2] = '/';
                }
            }
            str2 = str5;
            str = new String(cArr);
        } else {
            str = str4;
            str2 = str;
        }
        switch (list.size()) {
            case 1:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_1;
                break;
            case 2:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_2;
                break;
            case 3:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_3;
                break;
            case 4:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_4;
                break;
            case 5:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_5;
                break;
            case 6:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_6;
                break;
            case 7:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_7;
                break;
            case 8:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_8;
                break;
            case 9:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_9;
                break;
            case 10:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_10;
                break;
            case 11:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_11;
                break;
            case 12:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_12;
                break;
            default:
                str3 = ASMUtils.TYPE_OBJECT_WRITER_ADAPTER;
                break;
        }
        String str6 = str3;
        String str7 = str;
        classWriter.visit(52, 49, str7, str6, INTERFACES);
        String str8 = str7;
        genFields(list, classWriter, str6);
        genMethodInit(list, classWriter, str8, str6);
        boolean z = (j & FieldInfo.DISABLE_JSONB) != 0;
        boolean z2 = (j & FieldInfo.DISABLE_ARRAY_MAPPING) != 0;
        if (!z) {
            genMethodWriteJSONB(objectWriterProvider, cls, listBuildGroups, list, classWriter, str8, j);
            classWriter = classWriter;
            str8 = str8;
        }
        if ((j & JSONWriter.Feature.BeanToArray.mask) != 0 && !z) {
            genMethodWriteArrayMapping(objectWriterProvider, "write", cls, j, list, classWriter, str8);
        } else {
            ClassWriter classWriter2 = classWriter;
            String str9 = str8;
            genMethodWrite(objectWriterProvider, cls, list, classWriter2, str9, j);
            classWriter = classWriter2;
            str8 = str9;
        }
        if (!z) {
            genMethodWriteArrayMappingJSONB(objectWriterProvider, cls, j, listBuildGroups, list, classWriter, str8, j);
        }
        if (z2) {
            objectWriterCreatorASM = this;
            cls2 = cls;
            list2 = list;
        } else {
            objectWriterCreatorASM = this;
            list2 = list;
            objectWriterCreatorASM.genMethodWriteArrayMapping(objectWriterProvider, "writeArrayMapping", cls, j, list2, classWriter, str8);
            cls2 = cls;
        }
        byte[] byteArray = classWriter.toByteArray();
        try {
            ObjectWriterAdapter objectWriterAdapter = (ObjectWriterAdapter) objectWriterCreatorASM.classLoader.defineClassPublic(str2, byteArray, 0, byteArray.length).getConstructor(Class.class, String.class, String.class, Long.TYPE, List.class).newInstance(cls2, beanInfo.typeKey, beanInfo.typeName, Long.valueOf(j), list2);
            if (beanInfo.serializeFilters != null) {
                configSerializeFilters(beanInfo, objectWriterAdapter);
            }
            return objectWriterAdapter;
        } catch (Throwable th) {
            throw new JSONException("create objectWriter error, objectType " + cls2, th);
        }
    }

    private void genMethodWrite(ObjectWriterProvider objectWriterProvider, Class cls, List<FieldWriter> list, ClassWriter classWriter, String str, long j) {
        String str2;
        boolean z = (j & FieldInfo.DISABLE_JSONB) != 0;
        boolean z2 = (j & FieldInfo.DISABLE_ARRAY_MAPPING) != 0;
        boolean z3 = (j & FieldInfo.DISABLE_AUTO_TYPE) != 0;
        MethodWriter methodWriterVisitMethod = classWriter.visitMethod(1, "write", METHOD_DESC_WRITE, list.size() < 6 ? 512 : 1024);
        Label label = new Label();
        MethodWriterContext methodWriterContext = new MethodWriterContext(objectWriterProvider, cls, j, str, methodWriterVisitMethod, 8, false);
        methodWriterContext.genVariantsMethodBefore(false);
        methodWriterContext.genIsEnabled(JSONWriter.Feature.IgnoreErrorGetter.mask | JSONWriter.Feature.UnquoteFieldName.mask, label);
        methodWriterVisitMethod.aload(0);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.aload(2);
        methodWriterVisitMethod.aload(3);
        methodWriterVisitMethod.aload(4);
        methodWriterVisitMethod.lload(5);
        String str3 = ASMUtils.TYPE_OBJECT_WRITER_ADAPTER;
        String str4 = METHOD_DESC_WRITE_OBJECT;
        methodWriterVisitMethod.invokespecial(str3, "write", str4);
        methodWriterVisitMethod.return_();
        methodWriterVisitMethod.visitLabel(label);
        if (z) {
            str2 = str4;
        } else {
            Label label2 = new Label();
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.getfield(ASMUtils.TYPE_JSON_WRITER, "jsonb", "Z");
            methodWriterVisitMethod.ifeq(label2);
            if (z2) {
                str2 = str4;
            } else {
                Label label3 = new Label();
                str2 = str4;
                methodWriterContext.genIsEnabled(JSONWriter.Feature.BeanToArray.mask, label3);
                methodWriterVisitMethod.aload(0);
                methodWriterVisitMethod.aload(1);
                methodWriterVisitMethod.aload(2);
                methodWriterVisitMethod.aload(3);
                methodWriterVisitMethod.aload(4);
                methodWriterVisitMethod.lload(5);
                methodWriterVisitMethod.invokevirtual(str, "writeArrayMappingJSONB", str2);
                methodWriterVisitMethod.return_();
                methodWriterVisitMethod.visitLabel(label3);
            }
            methodWriterVisitMethod.aload(0);
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.aload(2);
            methodWriterVisitMethod.aload(3);
            methodWriterVisitMethod.aload(4);
            methodWriterVisitMethod.lload(5);
            methodWriterVisitMethod.invokevirtual(str, "writeJSONB", str2);
            methodWriterVisitMethod.return_();
            methodWriterVisitMethod.visitLabel(label2);
        }
        if (!z2) {
            Label label4 = new Label();
            methodWriterContext.genIsEnabled(JSONWriter.Feature.BeanToArray.mask, label4);
            methodWriterVisitMethod.aload(0);
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.aload(2);
            methodWriterVisitMethod.aload(3);
            methodWriterVisitMethod.aload(4);
            methodWriterVisitMethod.lload(5);
            methodWriterVisitMethod.invokevirtual(str, "writeArrayMapping", str2);
            methodWriterVisitMethod.return_();
            methodWriterVisitMethod.visitLabel(label4);
        }
        Label label5 = new Label();
        hashFilter(methodWriterVisitMethod, list, label5);
        methodWriterVisitMethod.aload(0);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.aload(2);
        methodWriterVisitMethod.aload(3);
        methodWriterVisitMethod.aload(4);
        methodWriterVisitMethod.lload(5);
        methodWriterVisitMethod.invokevirtual(str, "writeWithFilter", str2);
        methodWriterVisitMethod.return_();
        methodWriterVisitMethod.visitLabel(label5);
        Label label6 = new Label();
        if (cls == null || !Serializable.class.isAssignableFrom(cls)) {
            Label label7 = new Label();
            methodWriterContext.genIsEnabled(JSONWriter.Feature.IgnoreNoneSerializable.mask, label7);
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V");
            methodWriterVisitMethod.goto_(label6);
            methodWriterVisitMethod.visitLabel(label7);
            Label label8 = new Label();
            methodWriterContext.genIsEnabled(JSONWriter.Feature.ErrorOnNoneSerializable.mask, label8);
            methodWriterVisitMethod.aload(0);
            methodWriterVisitMethod.invokevirtual(methodWriterContext.classNameType, "errorOnNoneSerializable", "()V");
            methodWriterVisitMethod.goto_(label6);
            methodWriterVisitMethod.visitLabel(label8);
        }
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "startObject", "()V");
        if (!z3) {
            methodWriterVisitMethod.iconst_1();
            methodWriterVisitMethod.istore(7);
            Label label9 = new Label();
            isWriteTypeInfo(j, methodWriterVisitMethod, 2, 4, 5, label9);
            methodWriterVisitMethod.aload(0);
            methodWriterVisitMethod.aload(1);
            methodWriterVisitMethod.invokeinterface(ASMUtils.TYPE_OBJECT_WRITER, "writeTypeInfo", METHOD_DESC_WRITE_TYPE_INFO);
            methodWriterVisitMethod.iconst_1();
            methodWriterVisitMethod.ixor();
            methodWriterVisitMethod.istore(7);
            methodWriterVisitMethod.visitLabel(label9);
        }
        for (int i = 0; i < list.size(); i++) {
            gwFieldValue(methodWriterContext, list.get(i), 2, i);
        }
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "endObject", "()V");
        methodWriterVisitMethod.visitLabel(label6);
        methodWriterVisitMethod.return_();
        methodWriterVisitMethod.visitMaxs(methodWriterContext.maxVariant + 1, methodWriterContext.maxVariant + 1);
    }

    private static void isWriteTypeInfo(long j, MethodWriter methodWriter, int i, int i2, int i3, Label label) {
        if ((JSONWriter.Feature.WriteClassName.mask & j) == 0 || (j & JSONWriter.Feature.NotWriteRootClassName.mask) != 0) {
            methodWriter.aload(i);
            methodWriter.ifnull(label);
            methodWriter.aload(i);
            methodWriter.invokevirtual(ASMUtils.TYPE_OBJECT, "getClass", "()Ljava/lang/Class;");
            methodWriter.aload(i2);
            methodWriter.if_acmpeq(label);
            methodWriter.aload(1);
            methodWriter.aload(i);
            methodWriter.aload(i2);
            methodWriter.lload(i3);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "isWriteTypeInfo", "(Ljava/lang/Object;Ljava/lang/reflect/Type;J)Z");
            methodWriter.ifeq(label);
        }
    }

    private void genMethodWriteJSONB(ObjectWriterProvider objectWriterProvider, Class cls, List<FieldWriterGroup> list, List<FieldWriter> list2, ClassWriter classWriter, String str, long j) {
        MethodWriterContext methodWriterContext;
        Label label;
        String str2;
        int i;
        Integer num;
        MethodWriterContext methodWriterContext2;
        int i2 = 1;
        MethodWriter methodWriterVisitMethod = classWriter.visitMethod(1, "writeJSONB", METHOD_DESC_WRITE, list2.size() < 6 ? 512 : 1024);
        MethodWriterContext methodWriterContext3 = new MethodWriterContext(objectWriterProvider, cls, j, str, methodWriterVisitMethod, 7, true);
        MethodWriter methodWriter = methodWriterVisitMethod;
        methodWriterContext3.genVariantsMethodBefore(true);
        Label label2 = new Label();
        String str3 = "()V";
        if (cls == null || !Serializable.class.isAssignableFrom(cls)) {
            Label label3 = new Label();
            methodWriterContext3.genIsEnabled(JSONWriter.Feature.IgnoreNoneSerializable.mask, label3);
            methodWriter.aload(1);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V");
            methodWriter.goto_(label2);
            methodWriter.visitLabel(label3);
            Label label4 = new Label();
            methodWriterContext3.genIsEnabled(JSONWriter.Feature.ErrorOnNoneSerializable.mask, label4);
            methodWriter.aload(0);
            methodWriter.invokevirtual(methodWriterContext3.classNameType, "errorOnNoneSerializable", "()V");
            methodWriter.goto_(label2);
            methodWriter.visitLabel(label4);
        }
        if ((j & FieldInfo.DISABLE_AUTO_TYPE) == 0) {
            Label label5 = new Label();
            isWriteTypeInfo(j, methodWriter, 2, 4, 5, label5);
            methodWriter.aload(0);
            methodWriter.aload(1);
            methodWriter.invokevirtual(str, "writeClassInfo", METHOD_DESC_WRITE_CLASS_INFO);
            methodWriter.visitLabel(label5);
        }
        Iterator<FieldWriterGroup> it = list.iterator();
        Integer num2 = null;
        while (it.hasNext()) {
            FieldWriterGroup next = it.next();
            if (next.direct) {
                int iVar = methodWriterContext3.var("offset");
                int iVar2 = methodWriterContext3.var("bytes");
                int iVar22 = methodWriterContext3.var2(CONTEXT_FEATURES);
                methodWriter.aload(i2);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "getOffset", "()I");
                methodWriter.istore(iVar);
                if (num2 == null) {
                    Integer numValueOf = Integer.valueOf(methodWriterContext3.var("symbolTable"));
                    methodWriter.aload(i2);
                    methodWriter.getfield(ASMUtils.TYPE_JSON_WRITER, "symbolTable", DESC_SYMBOL);
                    methodWriter.astore(numValueOf.intValue());
                    num = numValueOf;
                } else {
                    num = num2;
                }
                int i3 = ((next.start ? 1 : 0) ^ i2) + ((next.end ? 1 : 0) ^ i2);
                for (FieldWriterRecord fieldWriterRecord : next.fieldWriters) {
                    int length = i3 + fieldWriterRecord.fieldWriter.nameJSONB.length;
                    FieldWriter fieldWriter = fieldWriterRecord.fieldWriter;
                    Class<?> cls2 = fieldWriter.fieldClass;
                    if (isFieldVarIndex(methodWriterContext3, fieldWriter)) {
                        MethodWriterContext methodWriterContext4 = methodWriterContext3;
                        sotreFieldValueToLocalVar(methodWriterContext4, fieldWriterRecord.ordinal, fieldWriter, 2, methodWriter);
                        methodWriterContext2 = methodWriterContext4;
                    } else {
                        methodWriterContext2 = methodWriterContext3;
                        length += fieldCapacity(cls2);
                    }
                    i3 = length;
                    methodWriterContext3 = methodWriterContext2;
                }
                MethodWriterContext methodWriterContext5 = methodWriterContext3;
                methodWriter.aload(i2);
                methodWriter.iload(iVar);
                methodWriter.visitLdcInsn(i3);
                methodWriter.iadd();
                MethodWriter methodWriter2 = methodWriter;
                fieldValueCapacity(j, next.fieldWriters, methodWriterContext5, methodWriter2, iVar22);
                methodWriterContext = methodWriterContext5;
                methodWriter = methodWriter2;
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "ensureCapacity", "(I)Ljava/lang/Object;");
                methodWriter.checkcast("[B");
                methodWriter.astore(iVar2);
                if (next.start) {
                    gwWriteByte(methodWriter, iVar2, iVar, JSONB.Constants.BC_OBJECT);
                    methodWriter.visitIincInsn(iVar, i2);
                }
                for (FieldWriterRecord fieldWriterRecord2 : next.fieldWriters) {
                    MethodWriter methodWriter3 = methodWriter;
                    FieldWriterGroup fieldWriterGroup = next;
                    int i4 = iVar;
                    int i5 = iVar22;
                    int i6 = iVar2;
                    writeFieldValueDirectJSONB(j, str, methodWriterContext, fieldWriterRecord2.fieldWriter, fieldWriterRecord2.ordinal, methodWriter3, i6, i4, 2, i5, num.intValue(), true);
                    methodWriter = methodWriter3;
                    iVar2 = i6;
                    iVar = i4;
                    iVar22 = i5;
                    next = fieldWriterGroup;
                    str3 = str3;
                    label2 = label2;
                }
                FieldWriterGroup fieldWriterGroup2 = next;
                int i7 = iVar;
                int i8 = iVar2;
                label = label2;
                str2 = str3;
                if (fieldWriterGroup2.end) {
                    gwWriteByte(methodWriter, i8, i7, JSONB.Constants.BC_OBJECT_END);
                }
                methodWriter.aload(1);
                methodWriter.iload(i7);
                if (fieldWriterGroup2.end) {
                    methodWriter.iconst_1();
                    methodWriter.iadd();
                }
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "setOffset", "(I)V");
                num2 = num;
            } else {
                methodWriterContext = methodWriterContext3;
                label = label2;
                str2 = str3;
                if (next.start) {
                    methodWriter.aload(1);
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "startObject", str2);
                }
                for (FieldWriterRecord fieldWriterRecord3 : next.fieldWriters) {
                    gwFieldValueJSONB(methodWriterContext, fieldWriterRecord3.fieldWriter, 2, fieldWriterRecord3.ordinal);
                }
                if (next.end) {
                    i = 1;
                    methodWriter.aload(1);
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "endObject", str2);
                }
                i2 = i;
                methodWriterContext3 = methodWriterContext;
                str3 = str2;
                label2 = label;
            }
            i = 1;
            i2 = i;
            methodWriterContext3 = methodWriterContext;
            str3 = str2;
            label2 = label;
        }
        MethodWriterContext methodWriterContext6 = methodWriterContext3;
        int i9 = i2;
        methodWriter.visitLabel(label2);
        methodWriter.return_();
        methodWriter.visitMaxs(methodWriterContext6.maxVariant + i9, methodWriterContext6.maxVariant + i9);
    }

    private static void gwFieldNameDirectJSONB(String str, FieldWriter fieldWriter, int i, MethodWriterContext methodWriterContext, int i2, int i3) {
        Label label = new Label();
        Label label2 = new Label();
        MethodWriter methodWriter = methodWriterContext.mw;
        methodWriterContext.genIsDisabled(JSONWriter.Feature.WriteNameAsSymbol.mask, label);
        int iVar = methodWriterContext.var("symbolTable");
        Label label3 = new Label();
        methodWriter.aload(iVar);
        methodWriter.ifnull(label3);
        int iVar2 = methodWriterContext.var("symbol");
        methodWriter.aload(0);
        methodWriter.getfield(str, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        methodWriter.aload(iVar);
        methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeFieldNameSymbol", METHOD_DESC_WRITE_NAME_SYMBOL);
        methodWriter.istore(iVar2);
        methodWriter.iload(iVar2);
        methodWriter.visitLdcInsn(-1);
        methodWriter.if_icmpeq(label3);
        methodWriter.aload(i2);
        methodWriter.iload(i3);
        methodWriter.iload(iVar2);
        methodWriter.ineg();
        methodWriter.invokestatic(ASMUtils.TYPE_JSONB_IO, "writeSymbol", "([BII)I", true);
        methodWriter.istore(i3);
        methodWriter.goto_(label2);
        methodWriter.visitLabel(label3);
        byte[] bArr = fieldWriter.nameJSONB;
        int i4 = 0;
        while (true) {
            int i5 = i4 + 8;
            if (i5 > bArr.length) {
                break;
            }
            gwWriteLong(methodWriter, i2, i3, bArr, i4);
            i4 = i5;
        }
        int i6 = i4 + 4;
        if (i6 <= bArr.length) {
            gwWriteInt(methodWriter, i2, i3, bArr, i4);
            i4 = i6;
        }
        int i7 = i4 + 2;
        if (i7 <= bArr.length) {
            gwWriteShort(methodWriter, i2, i3, bArr, i4);
            i4 = i7;
        }
        if (i4 + 1 <= bArr.length) {
            gwWriteByte(methodWriter, i2, i3, bArr, i4);
        }
        methodWriter.visitIincInsn(i3, bArr.length);
        methodWriter.goto_(label2);
        methodWriter.visitLabel(label);
        methodWriter.aload(0);
        methodWriter.getfield(str, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        methodWriter.aload(i2);
        methodWriter.iload(i3);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeFieldNameJSONB", METHOD_DESC_WRITE_FIELD_NAME_JSONB);
        methodWriter.istore(i3);
        methodWriter.visitLabel(label2);
    }

    private static void gwWriteByte(MethodWriter methodWriter, int i, int i2, byte b) {
        methodWriter.aload(i);
        methodWriter.iload(i2);
        methodWriter.iconst_n(b);
        methodWriter.bastore();
    }

    private static void gwWriteByte(MethodWriter methodWriter, int i, int i2, byte[] bArr, int i3) {
        methodWriter.aload(i);
        methodWriter.iload(i2);
        if (i3 != 0) {
            methodWriter.iconst_n(i3);
            methodWriter.iadd();
        }
        methodWriter.iconst_n(bArr[i3]);
        methodWriter.bastore();
    }

    private static void gwWriteShort(MethodWriter methodWriter, int i, int i2, byte[] bArr, int i3) {
        short shortUnaligned = IOUtils.getShortUnaligned(bArr, i3);
        methodWriter.aload(i);
        methodWriter.iload(i2);
        if (i3 != 0) {
            methodWriter.iconst_n(i3);
            methodWriter.iadd();
        }
        methodWriter.visitLdcInsn((int) shortUnaligned);
        methodWriter.invokestatic(ASMUtils.TYPE_IO_UTILS, "putShortUnaligned", "([BIS)V");
    }

    private static void gwWriteInt(MethodWriter methodWriter, int i, int i2, byte[] bArr, int i3) {
        int intUnaligned = IOUtils.getIntUnaligned(bArr, i3);
        methodWriter.aload(i);
        methodWriter.iload(i2);
        if (i3 != 0) {
            methodWriter.iconst_n(i3);
            methodWriter.iadd();
        }
        methodWriter.visitLdcInsn(intUnaligned);
        methodWriter.invokestatic(ASMUtils.TYPE_IO_UTILS, "putIntUnaligned", "([BII)V");
    }

    private static void gwWriteLong(MethodWriter methodWriter, int i, int i2, byte[] bArr, int i3) {
        long longUnaligned = IOUtils.getLongUnaligned(bArr, i3);
        methodWriter.aload(i);
        methodWriter.iload(i2);
        if (i3 != 0) {
            methodWriter.iconst_n(i3);
            methodWriter.iadd();
        }
        methodWriter.visitLdcInsn(longUnaligned);
        methodWriter.invokestatic(ASMUtils.TYPE_IO_UTILS, "putLongUnaligned", "([BIJ)V");
    }

    private void writeFieldValueDirectJSONB(long j, String str, MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, MethodWriter methodWriter, int i2, int i3, int i4, int i5, int i6, boolean z) {
        Integer numValueOf;
        Integer num;
        Label label;
        Label label2;
        Integer num2;
        int i7;
        int i8;
        Label label3;
        boolean z2;
        String str2;
        FieldWriter fieldWriter2;
        int i9;
        boolean z3;
        String str3;
        String str4;
        Class<?> cls = fieldWriter.fieldClass;
        boolean zIsFieldVarIndex = isFieldVarIndex(methodWriterContext, fieldWriter);
        if (!cls.isPrimitive() || z) {
            Label label4 = new Label();
            if (zIsFieldVarIndex) {
                numValueOf = Integer.valueOf(methodWriterContext.var("field_" + i));
            } else {
                numValueOf = Integer.valueOf(methodWriterContext.var(cls));
                genGetObject(methodWriterContext, fieldWriter, i, i4);
                methodWriter.storeLocal(cls, numValueOf.intValue());
            }
            num = numValueOf;
            label = label4;
        } else {
            label = null;
            num = null;
        }
        if ((Collection.class.isAssignableFrom(cls) || cls.isArray()) && !methodWriterContext.disableReferenceDetect()) {
            int iVar = methodWriterContext.var("REF_PATH");
            if (label == null) {
                label = new Label();
            }
            Label label5 = new Label();
            methodWriter.aload(num.intValue());
            methodWriter.ifnull(label5);
            methodWriterContext.genIsEnabled(JSONWriter.Feature.ReferenceDetection.mask, label5);
            methodWriter.aload(1);
            methodWriter.aload(0);
            methodWriter.getfield(str, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            methodWriter.aload(num.intValue());
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "setPath0", METHOD_DESC_SET_PATH2);
            methodWriter.dup();
            methodWriter.astore(iVar);
            methodWriter.ifnull(label5);
            if (z) {
                label2 = label;
                num2 = num;
                i7 = iVar;
                i8 = i3;
                gwFieldNameDirectJSONB(str, fieldWriter, i, methodWriterContext, i2, i8);
            } else {
                label2 = label;
                num2 = num;
                i7 = iVar;
                i8 = i3;
            }
            methodWriter.aload(i2);
            methodWriter.iload(i8);
            methodWriter.aload(i7);
            methodWriter.aload(1);
            methodWriter.invokestatic(ASMUtils.TYPE_JSONB_IO, "writeReference", METHOD_DESC_IO_WRITE_REFERENCE, true);
            methodWriter.istore(i8);
            methodWriter.aload(1);
            methodWriter.aload(num2.intValue());
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "popPath0", "(Ljava/lang/Object;)V");
            methodWriter.goto_(label2);
            methodWriter.visitLabel(label5);
            label3 = label2;
            z2 = true;
        } else {
            i8 = i3;
            num2 = num;
            label3 = label;
            z2 = false;
        }
        if (z) {
            if (!cls.isPrimitive()) {
                Label label6 = new Label();
                methodWriter.iload(methodWriterContext.var(WRITE_NULLS));
                methodWriter.ifne(label6);
                methodWriter.aload(num2.intValue());
                methodWriter.ifnull(label3);
                methodWriter.visitLabel(label6);
            } else {
                int iVar2 = methodWriterContext.var(NOT_WRITE_DEFAULT_VALUE);
                Label label7 = new Label();
                if (cls == Byte.TYPE || cls == Short.TYPE || cls == Integer.TYPE || cls == Boolean.TYPE) {
                    methodWriter.iload(num2.intValue());
                    methodWriter.ifne(label7);
                    methodWriter.iload(iVar2);
                    methodWriter.ifne(label3);
                    methodWriter.visitLabel(label7);
                } else if (cls == Long.TYPE) {
                    methodWriter.lload(num2.intValue());
                    methodWriter.lconst_0();
                    methodWriter.lcmp();
                    methodWriter.ifne(label7);
                    methodWriter.iload(iVar2);
                    methodWriter.ifne(label3);
                    methodWriter.visitLabel(label7);
                }
            }
            str2 = str;
            fieldWriter2 = fieldWriter;
            i9 = i;
            gwFieldNameDirectJSONB(str2, fieldWriter2, i9, methodWriterContext, i2, i8);
        } else {
            str2 = str;
            fieldWriter2 = fieldWriter;
            i9 = i;
        }
        if (Collection.class.isAssignableFrom(cls)) {
            methodWriter.aload(i2);
            methodWriter.iload(i8);
            methodWriter.aload(num2.intValue());
            methodWriter.aload(0);
            methodWriter.getfield(str2, fieldWriter(i9), ASMUtils.DESC_FIELD_WRITER);
            z3 = z2;
            methodWriter.getfield(ASMUtils.TYPE_FIELD_WRITER, "fieldClass", "Ljava/lang/Class;");
            methodWriter.aload(1);
            str3 = "(Ljava/lang/Object;)V";
            methodWriter.invokestatic(ASMUtils.TYPE_JSONB_IO, "checkAndWriteTypeName", "([BILjava/lang/Object;Ljava/lang/Class;" + ASMUtils.DESC_JSON_WRITER + ")I", true);
            methodWriter.istore(i8);
        } else {
            z3 = z2;
            str3 = "(Ljava/lang/Object;)V";
        }
        boolean z4 = fieldWriter2 instanceof FieldWriterEnum;
        if (z4) {
            Label label8 = new Label();
            methodWriter.aload(num2.intValue());
            methodWriter.ifnull(label8);
            methodWriter.aload(i6);
            methodWriter.ifnull(label8);
            methodWriter.aload(0);
            methodWriter.getfield(str2, fieldWriter(i9), ASMUtils.DESC_FIELD_WRITER);
            methodWriter.aload(i2);
            methodWriter.iload(i8);
            methodWriter.aload(num2.intValue());
            methodWriter.aload(i6);
            methodWriter.lload(i5);
            methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeEnumValueJSONB", "([BILjava/lang/Enum;" + DESC_SYMBOL + "J)I");
            methodWriter.istore(i8);
            methodWriter.goto_(label3);
            methodWriter.visitLabel(label8);
        }
        methodWriter.aload(i2);
        methodWriter.iload(i8);
        if (num2 != null) {
            methodWriter.loadLocal(cls, num2.intValue());
        } else {
            genGetObject(methodWriterContext, fieldWriter2, i9, i4);
        }
        String str5 = "writeBoolean";
        if (cls == Boolean.TYPE) {
            str4 = "([BIZ)I";
        } else {
            if (cls == Byte.TYPE) {
                str4 = "([BIB)I";
            } else {
                if (cls == Short.TYPE) {
                    str4 = "([BIS)I";
                } else {
                    if (cls == Integer.TYPE) {
                        str4 = "([BII)I";
                    } else {
                        if (cls == Long.TYPE) {
                            str4 = "([BIJ)I";
                        } else {
                            if (cls == Float.TYPE) {
                                str4 = "([BIF)I";
                            } else {
                                if (cls == Double.TYPE) {
                                    str4 = "([BID)I";
                                } else if (cls == Boolean.class) {
                                    str4 = "([BILjava/lang/Boolean;)I";
                                } else if (cls == Byte.class) {
                                    str4 = "([BILjava/lang/Byte;J)I";
                                } else if (cls == Short.class) {
                                    str4 = "([BILjava/lang/Short;J)I";
                                } else if (cls == Integer.class) {
                                    str4 = "([BILjava/lang/Integer;J)I";
                                } else if (cls == Long.class) {
                                    str4 = "([BILjava/lang/Long;J)I";
                                } else if (cls == Float.class) {
                                    str4 = "([BILjava/lang/Float;J)I";
                                } else if (cls == Double.class) {
                                    str4 = "([BILjava/lang/Double;J)I";
                                } else {
                                    str5 = "writeString";
                                    if (cls == String.class) {
                                        str4 = "([BILjava/lang/String;)I";
                                    } else if (z4) {
                                        str5 = "writeEnum";
                                        str4 = "([BILjava/lang/Enum;J)I";
                                    } else if (cls == UUID.class) {
                                        str5 = "writeUUID";
                                        str4 = "([BILjava/util/UUID;)I";
                                    } else if (cls == LocalDate.class) {
                                        str5 = "writeLocalDate";
                                        str4 = "([BILjava/time/LocalDate;)I";
                                    } else if (cls == LocalTime.class) {
                                        str5 = "writeLocalTime";
                                        str4 = "([BILjava/time/LocalTime;)I";
                                    } else if (cls == LocalDateTime.class) {
                                        str5 = "writeLocalDateTime";
                                        str4 = "([BILjava/time/LocalDateTime;)I";
                                    } else if (cls == OffsetDateTime.class) {
                                        str5 = "writeOffsetDateTime";
                                        str4 = "([BILjava/time/OffsetDateTime;)I";
                                    } else if (cls == OffsetTime.class) {
                                        str5 = "writeOffsetTime";
                                        str4 = "([BILjava/time/OffsetTime;)I";
                                    } else if (cls == Instant.class) {
                                        str5 = "writeInstant";
                                        str4 = "([BILjava/time/Instant;)I";
                                    } else if (cls == String[].class) {
                                        str4 = "([BI[Ljava/lang/String;J)I";
                                    } else if (Collection.class.isAssignableFrom(cls)) {
                                        Class itemClass = fieldWriter2.getItemClass();
                                        if (itemClass != String.class) {
                                            if (itemClass != Long.class) {
                                                throw new JSONException("assert error " + cls.getName());
                                            }
                                            str5 = "writeInt64";
                                        }
                                        str4 = "([BILjava/util/Collection;J)I";
                                    } else {
                                        throw new JSONException("assert error " + cls.getName());
                                    }
                                }
                                str5 = "writeDouble";
                            }
                            str5 = "writeFloat";
                        }
                        str5 = "writeInt64";
                    }
                    str5 = "writeInt32";
                }
                str5 = "writeInt16";
            }
            str5 = "writeInt8";
        }
        if (cls == Float.class || cls == Double.class || cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class || cls == String[].class || Collection.class.isAssignableFrom(cls) || z4) {
            methodWriter.lload(i5);
            long j2 = j | fieldWriter2.features;
            if (j2 != 0) {
                methodWriter.visitLdcInsn(j2);
                methodWriter.lor();
            }
        }
        methodWriter.invokestatic(ASMUtils.TYPE_JSONB_IO, str5, str4, true);
        methodWriter.istore(i8);
        if (label3 != null) {
            if (z3) {
                methodWriter.aload(1);
                methodWriter.aload(num2.intValue());
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "popPath0", str3);
            }
            methodWriter.visitLabel(label3);
        }
    }

    private void sotreFieldValueToLocalVar(MethodWriterContext methodWriterContext, int i, FieldWriter fieldWriter, int i2, MethodWriter methodWriter) {
        int iVar = methodWriterContext.var("field_" + i);
        genGetObject(methodWriterContext, fieldWriter, i, i2);
        methodWriter.astore(iVar);
    }

    private static boolean isFieldVarIndex(MethodWriterContext methodWriterContext, FieldWriter fieldWriter) {
        Class cls = fieldWriter.fieldClass;
        if (cls == String.class || Collection.class.isAssignableFrom(cls) || cls == String[].class || (fieldWriter instanceof FieldWriterEnum)) {
            return true;
        }
        return Collection.class.isAssignableFrom(cls) && !methodWriterContext.disableReferenceDetect();
    }

    private static void fieldValueCapacity(long j, List<FieldWriterRecord> list, MethodWriterContext methodWriterContext, MethodWriter methodWriter, int i) {
        for (FieldWriterRecord fieldWriterRecord : list) {
            FieldWriter fieldWriter = fieldWriterRecord.fieldWriter;
            if (fieldWriter.fieldClass == String.class) {
                methodWriter.aload(methodWriterContext.var("field_" + fieldWriterRecord.ordinal));
                methodWriter.invokestatic(ASMUtils.TYPE_JSONB_IO, "stringCapacity", "(Ljava/lang/String;)I", true);
                methodWriter.iadd();
            } else if (fieldWriter.fieldClass == String[].class) {
                methodWriter.aload(methodWriterContext.var("field_" + fieldWriterRecord.ordinal));
                methodWriter.invokestatic(ASMUtils.TYPE_JSONB_IO, "stringCapacity", "([Ljava/lang/String;)I", true);
                methodWriter.iadd();
            } else if (fieldWriter instanceof FieldWriterEnum) {
                methodWriter.aload(methodWriterContext.var("field_" + fieldWriterRecord.ordinal));
                methodWriter.lload(i);
                long j2 = fieldWriter.features | j;
                if (j2 != 0) {
                    methodWriter.visitLdcInsn(j2);
                    methodWriter.lor();
                }
                methodWriter.invokestatic(ASMUtils.TYPE_JSONB_IO, "enumCapacity", "(Ljava/lang/Enum;J)I", true);
                methodWriter.iadd();
            } else if (Collection.class.isAssignableFrom(fieldWriter.fieldClass)) {
                methodWriter.aload(methodWriterContext.var("field_" + fieldWriterRecord.ordinal));
                Class itemClass = fieldWriter.getItemClass();
                if (itemClass == String.class) {
                    methodWriter.invokestatic(ASMUtils.TYPE_JSONB_IO, "stringCapacity", "(Ljava/util/Collection;)I", true);
                    methodWriter.iadd();
                } else if (itemClass == Long.class) {
                    methodWriter.invokestatic(ASMUtils.TYPE_JSONB_IO, "int64Capacity", "(Ljava/util/Collection;)I", true);
                    methodWriter.iadd();
                } else {
                    throw new JSONException("assert error itemClass " + itemClass.getName());
                }
            } else {
                continue;
            }
        }
    }

    private void genMethodWriteArrayMappingJSONB(ObjectWriterProvider objectWriterProvider, Class cls, long j, List<FieldWriterGroup> list, List<FieldWriter> list2, ClassWriter classWriter, String str, long j2) {
        int i;
        int i2;
        int i3;
        int i4;
        int iFieldCapacity;
        int i5 = 1;
        MethodWriter methodWriterVisitMethod = classWriter.visitMethod(1, "writeArrayMappingJSONB", METHOD_DESC_WRITE, 512);
        MethodWriterContext methodWriterContext = new MethodWriterContext(objectWriterProvider, cls, j, str, methodWriterVisitMethod, 7, true);
        MethodWriter methodWriter = methodWriterVisitMethod;
        int iVar = methodWriterContext.var("offset");
        int iVar2 = methodWriterContext.var("bytes");
        int iVar22 = methodWriterContext.var2(CONTEXT_FEATURES);
        if ((j2 & FieldInfo.DISABLE_AUTO_TYPE) == 0) {
            Label label = new Label();
            isWriteTypeInfo(j, methodWriter, 2, 4, 5, label);
            methodWriter.aload(0);
            methodWriter.aload(1);
            methodWriter.invokevirtual(str, "writeClassInfo", METHOD_DESC_WRITE_CLASS_INFO);
            methodWriter.visitLabel(label);
        }
        int size = list2.size();
        methodWriterContext.genVariantsMethodBefore(true);
        for (FieldWriterGroup fieldWriterGroup : list) {
            if (fieldWriterGroup.direct) {
                methodWriter.aload(i5);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "getOffset", "()I");
                methodWriter.istore(iVar);
                int i6 = 6;
                for (FieldWriterRecord fieldWriterRecord : fieldWriterGroup.fieldWriters) {
                    FieldWriter fieldWriter = fieldWriterRecord.fieldWriter;
                    Class<?> cls2 = fieldWriter.fieldClass;
                    if (isFieldVarIndex(methodWriterContext, fieldWriter)) {
                        iFieldCapacity = i6;
                        sotreFieldValueToLocalVar(methodWriterContext, fieldWriterRecord.ordinal, fieldWriter, 2, methodWriter);
                    } else {
                        iFieldCapacity = fieldCapacity(cls2) + i6;
                    }
                    i6 = iFieldCapacity;
                    i5 = 1;
                }
                methodWriter.aload(i5);
                methodWriter.iload(iVar);
                methodWriter.visitLdcInsn(i6);
                methodWriter.iadd();
                MethodWriter methodWriter2 = methodWriter;
                int i7 = iVar22;
                fieldValueCapacity(j, fieldWriterGroup.fieldWriters, methodWriterContext, methodWriter2, i7);
                methodWriter = methodWriter2;
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "ensureCapacity", "(I)Ljava/lang/Object;");
                methodWriter.checkcast("[B");
                methodWriter.astore(iVar2);
                if (fieldWriterGroup.start) {
                    methodWriter.aload(iVar2);
                    methodWriter.iload(iVar);
                    methodWriter.visitLdcInsn(list2.size());
                    i4 = 1;
                    methodWriter.invokestatic(ASMUtils.TYPE_JSONB_IO, "startArray", "([BII)I", true);
                    methodWriter.istore(iVar);
                } else {
                    i4 = 1;
                }
                int iVar3 = methodWriterContext.var("symbolTable");
                methodWriter.aload(i4);
                methodWriter.getfield(ASMUtils.TYPE_JSON_WRITER, "symbolTable", DESC_SYMBOL);
                methodWriter.astore(iVar3);
                for (FieldWriterRecord fieldWriterRecord2 : fieldWriterGroup.fieldWriters) {
                    MethodWriter methodWriter3 = methodWriter;
                    FieldWriter fieldWriter2 = fieldWriterRecord2.fieldWriter;
                    int i8 = fieldWriterRecord2.ordinal;
                    int i9 = iVar;
                    int i10 = iVar2;
                    int i11 = i7;
                    MethodWriterContext methodWriterContext2 = methodWriterContext;
                    writeFieldValueDirectJSONB(j, str, methodWriterContext2, fieldWriter2, i8, methodWriter3, i10, i9, 2, i11, iVar3, false);
                    iVar2 = i10;
                    i7 = i11;
                    methodWriterContext = methodWriterContext2;
                    methodWriter = methodWriter3;
                    iVar = i9;
                }
                int i12 = iVar2;
                i3 = i7;
                i2 = i12;
                i = iVar;
                methodWriter.aload(1);
                methodWriter.iload(i);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "setOffset", "(I)V");
            } else {
                int i13 = i5;
                i = iVar;
                i2 = iVar2;
                i3 = iVar22;
                if (fieldWriterGroup.start) {
                    methodWriter.aload(i13);
                    if (size <= 15) {
                        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "startArray" + size, "()V");
                    } else {
                        methodWriter.iconst_n(size);
                        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "startArray", "(I)V");
                    }
                }
                for (FieldWriterRecord fieldWriterRecord3 : fieldWriterGroup.fieldWriters) {
                    gwValueJSONB(methodWriterContext, fieldWriterRecord3.fieldWriter, 2, fieldWriterRecord3.ordinal);
                }
            }
            iVar = i;
            iVar22 = i3;
            i5 = 1;
            iVar2 = i2;
        }
        methodWriter.return_();
        methodWriter.visitMaxs(methodWriterContext.maxVariant + 1, methodWriterContext.maxVariant + 1);
    }

    private int fieldCapacity(Class<?> cls) {
        if (cls == Boolean.TYPE || cls == Boolean.class) {
            return 1;
        }
        if (cls == Byte.TYPE || cls == Byte.class) {
            return 2;
        }
        if (cls == Short.TYPE || cls == Short.class) {
            return 3;
        }
        if (cls == Integer.TYPE || cls == Integer.class || cls == Float.TYPE || cls == Float.class || cls == LocalDate.class) {
            return 5;
        }
        if (cls == Long.TYPE || cls == Long.class || cls == Double.TYPE || cls == Double.class || cls == LocalTime.class) {
            return 9;
        }
        if (cls == LocalDateTime.class) {
            return 13;
        }
        if (cls == Instant.class) {
            return 15;
        }
        if (cls == UUID.class) {
            return 18;
        }
        if (cls == OffsetDateTime.class || cls == OffsetTime.class) {
            return 21;
        }
        throw new JSONException("assert error " + cls.getName());
    }

    private void gwValueJSONB(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        long j = fieldWriter.features | methodWriterContext.objectFeatures;
        Class cls = fieldWriter.fieldClass;
        boolean z = false;
        boolean z2 = (j & JSONWriter.Feature.BeanToArray.mask) != 0;
        if ((cls == Long.TYPE || cls == Long.class || cls == long[].class) && (methodWriterContext.provider.userDefineMask & 4) != 0) {
            z = methodWriterContext.provider.getObjectWriter(Long.class) != ObjectWriterImplInt64.INSTANCE;
        }
        if (cls == Boolean.TYPE || cls == boolean[].class || cls == Character.TYPE || cls == char[].class || cls == Byte.TYPE || cls == byte[].class || cls == Short.TYPE || cls == short[].class || cls == Integer.TYPE || cls == int[].class || cls == Long.TYPE || ((cls == long[].class && !z) || cls == Float.TYPE || cls == float[].class || cls == Double.TYPE || cls == double[].class || cls == String.class || cls == Integer.class || cls == Long.class || cls == BigDecimal.class || cls.isEnum())) {
            gwValue(methodWriterContext, fieldWriter, i, i2, null);
            return;
        }
        if (cls == Date.class) {
            gwDate(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (fieldWriter instanceof FieldWriterList) {
            gwListJSONB(methodWriterContext, fieldWriter, i, i2);
        } else if (cls.isArray()) {
            gwObjectA(methodWriterContext, fieldWriter, i, i2);
        } else {
            gwObjectJSONB(fieldWriter, i, methodWriterContext, i2, z2);
        }
    }

    private void gwObjectJSONB(FieldWriter fieldWriter, int i, MethodWriterContext methodWriterContext, int i2, boolean z) {
        boolean z2;
        String str;
        String str2;
        int i3;
        Class cls = fieldWriter.fieldClass;
        String str3 = fieldWriter.fieldName;
        String str4 = methodWriterContext.classNameType;
        MethodWriter methodWriter = methodWriterContext.mw;
        int iVar = methodWriterContext.var(cls);
        Label label = new Label();
        Label label2 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnonnull(label2);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V");
        methodWriter.goto_(label);
        methodWriter.visitLabel(label2);
        boolean z3 = (methodWriterContext.disableSupportArrayMapping() || ObjectWriterProvider.isNotReferenceDetect(cls)) ? false : true;
        if (!z3) {
            z2 = z3;
            str = "(Ljava/lang/Object;)V";
            str2 = "popPath0";
        } else {
            int iVar2 = methodWriterContext.var("REF_PATH");
            Label label3 = new Label();
            z2 = z3;
            Label label4 = new Label();
            methodWriterContext.genIsEnabled(JSONWriter.Feature.ReferenceDetection.mask, label3);
            if (cls.isAssignableFrom(methodWriterContext.objectClass)) {
                methodWriter.aload(i);
                methodWriter.aload(iVar);
                methodWriter.if_acmpne(label4);
                i3 = 1;
                methodWriter.aload(1);
                methodWriter.visitLdcInsn("..");
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeReference", METHOD_DESC_WRITE_REFERENCE);
                methodWriter.goto_(label);
                methodWriter.visitLabel(label4);
            } else {
                i3 = 1;
            }
            methodWriter.aload(i3);
            methodWriter.aload(0);
            methodWriter.getfield(str4, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
            methodWriter.aload(iVar);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "setPath0", METHOD_DESC_SET_PATH2);
            methodWriter.dup();
            methodWriter.astore(iVar2);
            methodWriter.ifnull(label3);
            methodWriter.aload(1);
            methodWriter.aload(iVar2);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeReference", METHOD_DESC_WRITE_REFERENCE);
            methodWriter.aload(1);
            methodWriter.aload(iVar);
            str = "(Ljava/lang/Object;)V";
            str2 = "popPath0";
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, str2, str);
            methodWriter.goto_(label);
            methodWriter.visitLabel(label3);
        }
        methodWriter.aload(0);
        methodWriter.getfield(str4, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
        methodWriter.aload(1);
        methodWriter.aload(iVar);
        methodWriter.invokevirtual(ASMUtils.TYPE_OBJECT, "getClass", "()Ljava/lang/Class;");
        methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "getObjectWriter", METHOD_DESC_GET_OBJECT_WRITER);
        methodWriter.aload(1);
        methodWriter.aload(iVar);
        methodWriter.visitLdcInsn(str3);
        methodWriterContext.loadFieldType(i2, fieldWriter.fieldType);
        methodWriter.visitLdcInsn(fieldWriter.features);
        methodWriter.invokeinterface(ASMUtils.TYPE_OBJECT_WRITER, z ? "writeJSONB" : "writeArrayMappingJSONB", METHOD_DESC_WRITE_OBJECT);
        if (z2) {
            methodWriter.aload(1);
            methodWriter.aload(iVar);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, str2, str);
        }
        methodWriter.visitLabel(label);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void gwListJSONB(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.MethodWriterContext r20, com.alibaba.fastjson2.writer.FieldWriter r21, int r22, int r23) {
        /*
            Method dump skipped, instruction units count: 378
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.gwListJSONB(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM$MethodWriterContext, com.alibaba.fastjson2.writer.FieldWriter, int, int):void");
    }

    private void gwDate(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        MethodWriter methodWriter = methodWriterContext.mw;
        methodWriter.aload(0);
        methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
        methodWriter.aload(1);
        methodWriter.iconst_0();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeDate", METHOD_DESC_WRITE_DATE_WITH_FIELD_NAME);
    }

    private void gwValue(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2, Integer num) {
        MethodWriter methodWriter = methodWriterContext.mw;
        Class<?> cls = fieldWriter.fieldClass;
        if (cls == String.class) {
            genGetObject(methodWriterContext, fieldWriter, i2, i);
            methodWriter.checkcast("java/lang/String");
            int iVar = methodWriterContext.var("FIELD_VALUE_" + fieldWriter.fieldClass.getName());
            methodWriter.astore(iVar);
            gwString(methodWriterContext, false, true, iVar);
            return;
        }
        methodWriter.aload(1);
        if (num != null) {
            methodWriter.loadLocal(cls, num.intValue());
        } else {
            genGetObject(methodWriterContext, fieldWriter, i2, i);
        }
        String str = "(Ljava/math/BigDecimal;JLjava/text/DecimalFormat;)V";
        String str2 = "writeDecimal";
        if (fieldWriter.decimalFormat != null) {
            if (cls == Double.TYPE) {
                methodWriter.aload(0);
                methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
                methodWriter.getfield(ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeDouble", "(DLjava/text/DecimalFormat;)V");
                return;
            }
            if (cls == Float.TYPE) {
                methodWriter.aload(0);
                methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
                methodWriter.getfield(ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeFloat", "(FLjava/text/DecimalFormat;)V");
                return;
            }
            if (cls == BigDecimal.class) {
                methodWriter.visitLdcInsn(fieldWriter.features);
                methodWriter.aload(0);
                methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
                methodWriter.getfield(ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeDecimal", "(Ljava/math/BigDecimal;JLjava/text/DecimalFormat;)V");
                return;
            }
            throw new UnsupportedOperationException();
        }
        boolean z = (fieldWriter.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        if (cls == Integer.TYPE && !z) {
            String str3 = fieldWriter.format;
            if ("string".equals(str3)) {
                methodWriter.invokestatic("java/lang/Integer", "toString", "(I)Ljava/lang/String;");
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeString", METHOD_DESC_WRITE_REFERENCE);
                return;
            } else if (str3 != null) {
                methodWriter.visitLdcInsn(str3);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeInt32", "(ILjava/lang/String;)V");
                return;
            } else {
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeInt32", "(I)V");
                return;
            }
        }
        if (cls == Boolean.TYPE) {
            str = "(Z)V";
        } else {
            if (cls == Character.TYPE) {
                str2 = "writeChar";
                str = "(C)V";
            } else if (cls == Byte.TYPE) {
                str2 = z ? "writeString" : "writeInt8";
                str = "(B)V";
            } else if (cls == Short.TYPE) {
                str2 = z ? "writeString" : "writeInt16";
                str = "(S)V";
            } else if (cls == Integer.TYPE) {
                str2 = z ? "writeString" : "writeInt32";
                str = "(I)V";
            } else {
                if (cls == Integer.class) {
                    str = "(Ljava/lang/Integer;)V";
                } else if (cls == Long.TYPE) {
                    str2 = z ? "writeString" : "writeInt64";
                    str = "(J)V";
                } else {
                    if (cls == Long.class) {
                        str = "(Ljava/lang/Long;)V";
                    } else if (cls == Float.TYPE) {
                        str2 = z ? "writeString" : "writeFloat";
                        str = "(F)V";
                    } else if (cls == Double.TYPE) {
                        str2 = z ? "writeString" : "writeDouble";
                        str = "(D)V";
                    } else if (cls == boolean[].class) {
                        str = "([Z)V";
                    } else if (cls == char[].class) {
                        str = "([C)V";
                        str2 = "writeString";
                    } else if (cls == byte[].class) {
                        str2 = "writeBinary";
                        str = "([B)V";
                    } else if (cls == short[].class) {
                        str = "([S)V";
                        str2 = "writeInt16";
                    } else if (cls == int[].class) {
                        str = "([I)V";
                    } else if (cls == long[].class && methodWriterContext.provider.getObjectWriter(Long.class) == ObjectWriterImplInt64.INSTANCE) {
                        str = "([J)V";
                    } else if (cls == float[].class) {
                        str = "([F)V";
                        str2 = "writeFloat";
                    } else if (cls == double[].class) {
                        str = "([D)V";
                        str2 = "writeDouble";
                    } else if (cls == BigDecimal.class) {
                        methodWriter.visitLdcInsn(fieldWriter.features);
                        methodWriter.aconst_null();
                    } else if (Enum.class.isAssignableFrom(cls)) {
                        str2 = "writeEnum";
                        str = "(Ljava/lang/Enum;)V";
                    } else {
                        throw new UnsupportedOperationException();
                    }
                    str2 = "writeInt64";
                }
                str2 = "writeInt32";
            }
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, str2, str);
        }
        str2 = "writeBool";
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, str2, str);
    }

    private void gwObjectA(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        MethodWriter methodWriter = methodWriterContext.mw;
        if (fieldWriter.fieldClass == String[].class) {
            methodWriter.aload(1);
            genGetObject(methodWriterContext, fieldWriter, i2, i);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeString", "([Ljava/lang/String;)V");
        } else {
            methodWriter.aload(0);
            methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
            methodWriter.aload(1);
            methodWriter.aload(i);
            methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeValue", METHOD_DESC_WRITE_VALUE);
        }
    }

    private void genMethodWriteArrayMapping(ObjectWriterProvider objectWriterProvider, String str, Class cls, long j, List<FieldWriter> list, ClassWriter classWriter, String str2) {
        String str3 = METHOD_DESC_WRITE;
        MethodWriter methodWriterVisitMethod = classWriter.visitMethod(1, str, str3, 512);
        Label label = new Label();
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.getfield(ASMUtils.TYPE_JSON_WRITER, "jsonb", "Z");
        methodWriterVisitMethod.ifeq(label);
        methodWriterVisitMethod.aload(0);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.aload(2);
        methodWriterVisitMethod.aload(3);
        methodWriterVisitMethod.aload(4);
        methodWriterVisitMethod.lload(5);
        methodWriterVisitMethod.invokevirtual(str2, "writeArrayMappingJSONB", METHOD_DESC_WRITE_OBJECT);
        methodWriterVisitMethod.return_();
        methodWriterVisitMethod.visitLabel(label);
        Label label2 = new Label();
        hashFilter(methodWriterVisitMethod, list, label2);
        methodWriterVisitMethod.aload(0);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.aload(2);
        methodWriterVisitMethod.aload(3);
        methodWriterVisitMethod.aload(4);
        methodWriterVisitMethod.lload(5);
        methodWriterVisitMethod.invokespecial(ASMUtils.TYPE_OBJECT_WRITER_ADAPTER, str, str3);
        methodWriterVisitMethod.return_();
        methodWriterVisitMethod.visitLabel(label2);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "startArray", "()V");
        MethodWriterContext methodWriterContext = new MethodWriterContext(objectWriterProvider, cls, j, str2, methodWriterVisitMethod, 7, false);
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                methodWriterVisitMethod.aload(1);
                methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeComma", "()V");
            }
            gwFieldValueArrayMapping(list.get(i), methodWriterContext, 2, i);
        }
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "endArray", "()V");
        methodWriterVisitMethod.return_();
        methodWriterVisitMethod.visitMaxs(methodWriterContext.maxVariant + 1, methodWriterContext.maxVariant + 1);
    }

    private static void hashFilter(MethodWriter methodWriter, List<FieldWriter> list, Label label) {
        boolean z;
        Iterator<FieldWriter> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            FieldWriter next = it.next();
            if (next.method != null && (next.features & FieldInfo.FIELD_MASK) == 0) {
                z = true;
                break;
            }
        }
        methodWriter.aload(0);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_OBJECT_WRITER_ADAPTER, z ? "hasFilter" : "hasFilter0", METHOD_DESC_HAS_FILTER);
        methodWriter.ifeq(label);
    }

    private void gwFieldValueArrayMapping(FieldWriter fieldWriter, MethodWriterContext methodWriterContext, int i, int i2) {
        Class cls = methodWriterContext.objectClass;
        Class cls2 = fieldWriter.fieldClass;
        String strType = cls == null ? ASMUtils.TYPE_OBJECT : ASMUtils.type(cls);
        boolean z = false;
        if ((cls2 == Long.TYPE || cls2 == Long.class || cls2 == long[].class) && (methodWriterContext.provider.userDefineMask & 4) != 0 && methodWriterContext.provider.getObjectWriter(Long.class) != ObjectWriterImplInt64.INSTANCE) {
            z = true;
        }
        if (cls2 == Boolean.TYPE || cls2 == boolean[].class || cls2 == Character.TYPE || cls2 == char[].class || cls2 == Byte.TYPE || cls2 == byte[].class || cls2 == Short.TYPE || cls2 == short[].class || cls2 == Integer.TYPE || cls2 == int[].class || cls2 == Long.TYPE || ((cls2 == long[].class && !z) || cls2 == Float.TYPE || cls2 == float[].class || cls2 == Double.TYPE || cls2 == double[].class || cls2 == String.class || cls2 == Integer.class || cls2 == Long.class || cls2 == BigDecimal.class || cls2.isEnum())) {
            gwValue(methodWriterContext, fieldWriter, i, i2, null);
            return;
        }
        if (cls2 == Date.class) {
            gwDate(methodWriterContext, fieldWriter, i, i2);
        } else if (fieldWriter instanceof FieldWriterList) {
            gwList(methodWriterContext, i, i2, fieldWriter);
        } else {
            gwObject(methodWriterContext, i, i2, fieldWriter, strType);
        }
    }

    private void gwObject(MethodWriterContext methodWriterContext, int i, int i2, FieldWriter fieldWriter, String str) {
        Label label;
        Label label2;
        boolean z;
        int i3;
        int i4;
        Class cls = fieldWriter.fieldClass;
        String str2 = fieldWriter.fieldName;
        MethodWriter methodWriter = methodWriterContext.mw;
        int iVar = methodWriterContext.var(cls);
        int iVar2 = methodWriterContext.var("REF_PATH");
        Label label3 = new Label();
        Label label4 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnonnull(label4);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V");
        methodWriter.goto_(label3);
        methodWriter.visitLabel(label4);
        if (cls == Double.class || cls == Float.class || cls == BigDecimal.class) {
            methodWriter.aload(1);
            if (fieldWriter.decimalFormat != null) {
                methodWriter.aload(iVar);
                label = label3;
                if (cls == Double.class) {
                    methodWriter.invokevirtual("java/lang/Double", "doubleValue", "()D");
                    methodWriter.aload(0);
                    methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
                    methodWriter.getfield(ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeDouble", "(DLjava/text/DecimalFormat;)V");
                } else if (cls == Float.class) {
                    methodWriter.invokevirtual("java/lang/Float", "floatValue", "()F");
                    methodWriter.aload(0);
                    methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
                    methodWriter.getfield(ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeFloat", "(FLjava/text/DecimalFormat;)V");
                } else {
                    methodWriter.visitLdcInsn(fieldWriter.features);
                    methodWriter.aload(0);
                    methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
                    methodWriter.getfield(ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeDecimal", "(Ljava/math/BigDecimal;JLjava/text/DecimalFormat;)V");
                }
            } else {
                label = label3;
                methodWriter.aload(iVar);
                if (cls == Double.class) {
                    methodWriter.invokevirtual("java/lang/Double", "doubleValue", "()D");
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeDouble", "(D)V");
                } else if (cls == Float.class) {
                    methodWriter.invokevirtual("java/lang/Float", "floatValue", "()F");
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeFloat", "(F)V");
                } else {
                    methodWriter.visitLdcInsn(fieldWriter.features);
                    methodWriter.aconst_null();
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeDecimal", "(Ljava/math/BigDecimal;JLjava/text/DecimalFormat;)V");
                }
            }
            label2 = label;
        } else {
            boolean zIsNotReferenceDetect = ObjectWriterProvider.isNotReferenceDetect(cls);
            if (zIsNotReferenceDetect) {
                z = zIsNotReferenceDetect;
                i3 = 1;
            } else {
                Label label5 = new Label();
                Label label6 = new Label();
                methodWriter.aload(1);
                z = zIsNotReferenceDetect;
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "isRefDetect", "()Z");
                methodWriter.ifeq(label5);
                if (cls.isAssignableFrom(methodWriterContext.objectClass)) {
                    methodWriter.aload(i);
                    methodWriter.aload(iVar);
                    methodWriter.if_acmpne(label6);
                    i4 = 1;
                    methodWriter.aload(1);
                    methodWriter.visitLdcInsn("..");
                    methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeReference", METHOD_DESC_WRITE_REFERENCE);
                    methodWriter.goto_(label3);
                    methodWriter.visitLabel(label6);
                } else {
                    i4 = 1;
                }
                methodWriter.aload(i4);
                methodWriter.aload(0);
                methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
                methodWriter.aload(iVar);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "setPath0", METHOD_DESC_SET_PATH2);
                methodWriter.dup();
                methodWriter.astore(iVar2);
                methodWriter.ifnull(label5);
                i3 = 1;
                methodWriter.aload(1);
                methodWriter.aload(iVar2);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeReference", METHOD_DESC_WRITE_REFERENCE);
                methodWriter.aload(1);
                methodWriter.aload(iVar);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "popPath0", "(Ljava/lang/Object;)V");
                methodWriter.goto_(label3);
                methodWriter.visitLabel(label5);
            }
            if (cls == String[].class) {
                methodWriter.aload(i3);
                methodWriter.aload(iVar);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeString", "([Ljava/lang/String;)V");
            } else {
                methodWriter.aload(0);
                methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
                methodWriter.aload(1);
                methodWriter.aload(iVar);
                methodWriter.invokevirtual(ASMUtils.TYPE_OBJECT, "getClass", "()Ljava/lang/Class;");
                methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "getObjectWriter", METHOD_DESC_GET_OBJECT_WRITER);
                methodWriter.aload(1);
                methodWriter.aload(iVar);
                methodWriter.visitLdcInsn(fieldWriter.fieldName);
                methodWriterContext.loadFieldType(i2, fieldWriter.fieldType);
                methodWriter.visitLdcInsn(fieldWriter.features);
                methodWriter.invokeinterface(ASMUtils.TYPE_OBJECT_WRITER, "write", METHOD_DESC_WRITE_OBJECT);
            }
            if (!z) {
                methodWriter.aload(1);
                methodWriter.aload(iVar);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "popPath0", "(Ljava/lang/Object;)V");
            }
            label2 = label3;
        }
        methodWriter.visitLabel(label2);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0039  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void gwList(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.MethodWriterContext r19, int r20, int r21, com.alibaba.fastjson2.writer.FieldWriter r22) {
        /*
            Method dump skipped, instruction units count: 443
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.gwList(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM$MethodWriterContext, int, int, com.alibaba.fastjson2.writer.FieldWriter):void");
    }

    private void gwFieldValue(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        Class cls = fieldWriter.fieldClass;
        if (cls == Boolean.TYPE) {
            gwFieldValueBooleanV(methodWriterContext, fieldWriter, i, i2, false);
            return;
        }
        if (cls == boolean[].class || cls == byte[].class || cls == char[].class || cls == short[].class || cls == float[].class || cls == double[].class) {
            gwFieldValueArray(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls == Character.TYPE || cls == Byte.TYPE || cls == Integer.TYPE || cls == Short.TYPE || cls == Float.TYPE) {
            gwFieldValueInt32V(methodWriterContext, fieldWriter, i, i2, false);
            return;
        }
        if (cls == int[].class) {
            gwFieldValueIntVA(methodWriterContext, fieldWriter, i, i2, false);
            return;
        }
        if (cls == Long.TYPE || cls == Double.TYPE) {
            gwFieldValueInt64V(methodWriterContext, fieldWriter, i, i2, true);
            return;
        }
        if (cls == long[].class && methodWriterContext.provider.getObjectWriter(Long.class) == ObjectWriterImplInt64.INSTANCE) {
            gwFieldValueInt64VA(methodWriterContext, fieldWriter, i, i2, false);
            return;
        }
        if (cls == Integer.class) {
            gwInt32(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls == Long.class) {
            gwInt64(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls == Float.class) {
            gwFloat(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls == Double.class) {
            gwDouble(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls == String.class) {
            gwFieldValueString(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls.isEnum() && BeanUtils.getEnumValueField(cls, methodWriterContext.provider) == null && !(fieldWriter instanceof FieldWriterObject)) {
            gwFieldValueEnum(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls == Date.class) {
            gwFieldValueDate(methodWriterContext, fieldWriter, i, i2);
        } else if (cls == List.class) {
            gwFieldValueList(methodWriterContext, fieldWriter, i, i2);
        } else {
            gwFieldValueObject(methodWriterContext, fieldWriter, i, i2, false);
        }
    }

    private void gwFieldValueEnum(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        Class cls = fieldWriter.fieldClass;
        MethodWriter methodWriter = methodWriterContext.mw;
        int iVar = methodWriterContext.var(cls);
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        Label label = new Label();
        Label label2 = new Label();
        methodWriter.ifnull(label);
        methodWriter.aload(0);
        methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
        methodWriter.aload(1);
        methodWriter.aload(iVar);
        methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeEnum", METHOD_DESC_WRITE_ENUM);
        methodWriter.goto_(label2);
        methodWriter.visitLabel(label);
        methodWriter.iload(methodWriterContext.var(WRITE_NULLS));
        methodWriter.ifeq(label2);
        gwFieldName(methodWriterContext, fieldWriter, i2);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V");
        methodWriter.visitLabel(label2);
    }

    /* JADX WARN: Removed duplicated region for block: B:125:0x03ee  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x041c  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0488  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x04d9  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0208  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0241  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0284  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02ba  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void gwFieldValueObject(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.MethodWriterContext r31, com.alibaba.fastjson2.writer.FieldWriter r32, int r33, int r34, boolean r35) {
        /*
            Method dump skipped, instruction units count: 1252
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.gwFieldValueObject(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM$MethodWriterContext, com.alibaba.fastjson2.writer.FieldWriter, int, int, boolean):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0057  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void gwFieldValueList(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.MethodWriterContext r26, com.alibaba.fastjson2.writer.FieldWriter r27, int r28, int r29) {
        /*
            Method dump skipped, instruction units count: 546
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.gwFieldValueList(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM$MethodWriterContext, com.alibaba.fastjson2.writer.FieldWriter, int, int):void");
    }

    private void gwFieldValueJSONB(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        Class cls = fieldWriter.fieldClass;
        long j = fieldWriter.features;
        long j2 = JSONWriter.Feature.WriteNonStringValueAsString.mask;
        if (cls == Boolean.TYPE) {
            gwFieldValueBooleanV(methodWriterContext, fieldWriter, i, i2, true);
            return;
        }
        if (cls == boolean[].class || cls == byte[].class || cls == char[].class || cls == short[].class || cls == float[].class || cls == double[].class) {
            gwFieldValueArray(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls == Character.TYPE || cls == Byte.TYPE || cls == Short.TYPE || cls == Integer.TYPE || cls == Float.TYPE) {
            gwFieldValueInt32V(methodWriterContext, fieldWriter, i, i2, true);
            return;
        }
        if (cls == int[].class) {
            gwFieldValueIntVA(methodWriterContext, fieldWriter, i, i2, true);
            return;
        }
        if (cls == Long.TYPE || cls == Double.TYPE) {
            gwFieldValueInt64V(methodWriterContext, fieldWriter, i, i2, true);
            return;
        }
        if (cls == long[].class && methodWriterContext.provider.getObjectWriter(Long.class) == ObjectWriterImplInt64.INSTANCE) {
            gwFieldValueInt64VA(methodWriterContext, fieldWriter, i, i2, true);
            return;
        }
        if (cls == Integer.class) {
            gwInt32(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls == Long.class) {
            gwInt64(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls == String.class) {
            gwFieldValueString(methodWriterContext, fieldWriter, i, i2);
            return;
        }
        if (cls.isEnum()) {
            gwFieldValueArray(methodWriterContext, fieldWriter, i, i2);
        } else if (cls == Date.class) {
            gwFieldValueDate(methodWriterContext, fieldWriter, i, i2);
        } else {
            gwFieldValueObject(methodWriterContext, fieldWriter, i, i2, true);
        }
    }

    private void gwInt32(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        boolean z;
        String str = methodWriterContext.classNameType;
        MethodWriter methodWriter = methodWriterContext.mw;
        int iVar = methodWriterContext.var(fieldWriter.fieldClass);
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnonnull(label2);
        boolean z2 = (fieldWriter.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        if ((fieldWriter.features & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask)) == 0) {
            z = z2;
            methodWriterContext.genIsEnabled(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask, label3, label);
            methodWriter.visitLabel(label3);
            gwFieldName(methodWriterContext, fieldWriter, i2);
            methodWriter.aload(1);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeNumberNull", "()V");
        } else {
            z = z2;
            if ((fieldWriter.features & (JSONWriter.Feature.WriteNullNumberAsZero.mask | JSONWriter.Feature.NullAsDefaultValue.mask)) != 0) {
                gwFieldName(methodWriterContext, fieldWriter, i2);
                methodWriter.aload(1);
                methodWriter.visitLdcInsn(0);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeInt32", "(I)V");
            } else {
                gwFieldName(methodWriterContext, fieldWriter, i2);
                methodWriter.aload(1);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V");
            }
        }
        methodWriter.goto_(label);
        methodWriter.visitLabel(label2);
        if (z) {
            methodWriter.aload(0);
            methodWriter.getfield(str, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
            methodWriter.aload(1);
            methodWriter.aload(iVar);
            methodWriter.invokevirtual("java/lang/Integer", "intValue", "()I");
            methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeInt32", METHOD_DESC_WRITE_I);
        } else {
            gwFieldName(methodWriterContext, fieldWriter, i2);
            methodWriter.aload(1);
            methodWriter.aload(iVar);
            methodWriter.invokevirtual("java/lang/Integer", "intValue", "()I");
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeInt32", "(I)V");
        }
        methodWriter.visitLabel(label);
    }

    private void gwInt64(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        MethodWriter methodWriter = methodWriterContext.mw;
        Class cls = fieldWriter.fieldClass;
        String str = methodWriterContext.classNameType;
        int iVar = methodWriterContext.var(cls);
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnonnull(label2);
        if ((fieldWriter.features & JSONWriter.Feature.WriteNulls.mask) == 0) {
            methodWriterContext.genIsEnabled(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask, label3, label);
        }
        methodWriter.visitLabel(label3);
        gwFieldName(methodWriterContext, fieldWriter, i2);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeInt64Null", "()V");
        methodWriter.goto_(label);
        methodWriter.visitLabel(label2);
        if ((fieldWriter.features & (JSONWriter.Feature.WriteLongAsString.mask | JSONWriter.Feature.WriteNonStringValueAsString.mask | JSONWriter.Feature.BrowserCompatible.mask)) == 0) {
            gwFieldName(methodWriterContext, fieldWriter, i2);
            methodWriter.aload(1);
            methodWriter.aload(iVar);
            methodWriter.invokevirtual("java/lang/Long", "longValue", "()J");
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeInt64", "(J)V");
        } else {
            methodWriter.aload(0);
            methodWriter.getfield(str, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
            methodWriter.aload(1);
            methodWriter.aload(iVar);
            methodWriter.invokevirtual("java/lang/Long", "longValue", "()J");
            methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeInt64", METHOD_DESC_WRITE_J);
        }
        methodWriter.visitLabel(label);
    }

    private void gwDouble(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        long j;
        String str;
        boolean z = methodWriterContext.jsonb;
        MethodWriter methodWriter = methodWriterContext.mw;
        Class cls = fieldWriter.fieldClass;
        String str2 = methodWriterContext.classNameType;
        int iVar = methodWriterContext.var(cls);
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnonnull(label2);
        if ((fieldWriter.features & JSONWriter.Feature.WriteNulls.mask) == 0) {
            j = 0;
            methodWriterContext.genIsEnabled(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask, label3, label);
        } else {
            j = 0;
        }
        methodWriter.visitLabel(label3);
        gwFieldName(methodWriterContext, fieldWriter, i2);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeDecimalNull", "()V");
        methodWriter.goto_(label);
        methodWriter.visitLabel(label2);
        if (z) {
            if ((JSONWriter.Feature.WriteNonStringValueAsString.mask & fieldWriter.features) == j) {
                gwFieldName(methodWriterContext, fieldWriter, i2);
                methodWriter.aload(1);
                methodWriter.aload(iVar);
                methodWriter.invokevirtual("java/lang/Double", "doubleValue", "()D");
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeDouble", "(D)V");
                methodWriter.visitLabel(label);
            }
            str = "()D";
        } else {
            str = "()D";
        }
        methodWriter.aload(0);
        methodWriter.getfield(str2, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
        methodWriter.aload(1);
        methodWriter.aload(iVar);
        methodWriter.invokevirtual("java/lang/Double", "doubleValue", str);
        methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeDouble", METHOD_DESC_WRITE_D);
        methodWriter.visitLabel(label);
    }

    private void gwFloat(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        boolean z = methodWriterContext.jsonb;
        MethodWriter methodWriter = methodWriterContext.mw;
        Class cls = fieldWriter.fieldClass;
        String str = methodWriterContext.classNameType;
        int iVar = methodWriterContext.var(cls);
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnonnull(label2);
        methodWriterContext.genIsEnabled(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask, label3, label);
        methodWriter.visitLabel(label3);
        gwFieldName(methodWriterContext, fieldWriter, i2);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeDecimalNull", "()V");
        methodWriter.goto_(label);
        methodWriter.visitLabel(label2);
        if (z) {
            gwFieldName(methodWriterContext, fieldWriter, i2);
            methodWriter.aload(1);
            methodWriter.aload(iVar);
            methodWriter.invokevirtual("java/lang/Float", "floatValue", "()F");
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeFloat", "(D)V");
        } else {
            methodWriter.aload(0);
            methodWriter.getfield(str, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
            methodWriter.aload(1);
            methodWriter.aload(iVar);
            methodWriter.invokevirtual("java/lang/Float", "floatValue", "()F");
            methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeFloat", METHOD_DESC_WRITE_F);
        }
        methodWriter.visitLabel(label);
    }

    private static void gwListSimpleType(MethodWriterContext methodWriterContext, int i, MethodWriter methodWriter, Class<?> cls, Class cls2, int i2) {
        if (methodWriterContext.jsonb) {
            methodWriter.aload(1);
            methodWriter.aload(i2);
            methodWriterContext.loadFieldClass(i, cls);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "checkAndWriteTypeName", "(Ljava/lang/Object;Ljava/lang/Class;)V");
        }
        if (cls2 == Integer.class) {
            methodWriter.aload(1);
            methodWriter.aload(i2);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeListInt32", "(Ljava/util/List;)V");
        } else if (cls2 == Long.class) {
            methodWriter.aload(1);
            methodWriter.aload(i2);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeListInt64", "(Ljava/util/List;)V");
        } else {
            if (cls2 == String.class) {
                methodWriter.aload(1);
                methodWriter.aload(i2);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeString", "(Ljava/util/List;)V");
                return;
            }
            throw new JSONException("TOOD " + cls2.getName());
        }
    }

    static void gwString(MethodWriterContext methodWriterContext, boolean z, boolean z2, int i) {
        MethodWriter methodWriter = methodWriterContext.mw;
        Label label = new Label();
        Label label2 = new Label();
        if (z2) {
            methodWriter.aload(i);
            methodWriter.ifnonnull(label);
            methodWriter.aload(1);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeStringNull", "()V");
            methodWriter.goto_(label2);
            methodWriter.visitLabel(label);
        }
        if (JDKUtils.JVM_VERSION == 8 && !JDKUtils.OPENJ9 && !JDKUtils.FIELD_STRING_VALUE_ERROR && !z) {
            methodWriter.aload(1);
            methodWriter.getstatic(ObjectWriterCreatorASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
            methodWriter.aload(i);
            methodWriter.visitLdcInsn(JDKUtils.FIELD_STRING_VALUE_OFFSET);
            methodWriter.invokevirtual("sun/misc/Unsafe", "getObject", "(Ljava/lang/Object;J)Ljava/lang/Object;");
            methodWriter.checkcast("[C");
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeString", "([C)V");
        } else if (JDKUtils.JVM_VERSION > 8 && !JDKUtils.OPENJ9 && JDKUtils.FIELD_STRING_CODER_OFFSET != -1 && JDKUtils.FIELD_STRING_VALUE_OFFSET != -1 && !z) {
            Label label3 = new Label();
            Label label4 = new Label();
            methodWriter.aload(1);
            methodWriter.getstatic(ObjectWriterCreatorASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
            methodWriter.aload(i);
            methodWriter.visitLdcInsn(JDKUtils.FIELD_STRING_VALUE_OFFSET);
            methodWriter.invokevirtual("sun/misc/Unsafe", "getObject", "(Ljava/lang/Object;J)Ljava/lang/Object;");
            methodWriter.checkcast("[B");
            methodWriter.getstatic(ObjectWriterCreatorASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
            methodWriter.aload(i);
            methodWriter.visitLdcInsn(JDKUtils.FIELD_STRING_CODER_OFFSET);
            methodWriter.invokevirtual("sun/misc/Unsafe", "getByte", "(Ljava/lang/Object;J)B");
            methodWriter.ifne(label3);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeStringLatin1", "([B)V");
            methodWriter.goto_(label4);
            methodWriter.visitLabel(label3);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeStringUTF16", "([B)V");
            methodWriter.visitLabel(label4);
        } else {
            methodWriter.aload(1);
            methodWriter.aload(i);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, z ? "writeSymbol" : "writeString", METHOD_DESC_WRITE_REFERENCE);
        }
        if (z2) {
            methodWriter.visitLabel(label2);
        }
    }

    private void gwFieldValueDate(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        MethodWriter methodWriter = methodWriterContext.mw;
        Class cls = fieldWriter.fieldClass;
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        int iVar = methodWriterContext.var(cls);
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnull(label);
        methodWriter.aload(0);
        methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
        methodWriter.aload(1);
        methodWriter.aload(iVar);
        methodWriter.invokevirtual("java/util/Date", "getTime", "()J");
        methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeDate", METHOD_DESC_WRITE_J);
        methodWriter.goto_(label3);
        methodWriter.visitLabel(label);
        if ((fieldWriter.features & JSONWriter.Feature.WriteNulls.mask) == 0) {
            methodWriter.iload(methodWriterContext.var(WRITE_NULLS));
            methodWriter.ifne(label2);
            methodWriter.goto_(label3);
        }
        methodWriter.visitLabel(label2);
        gwFieldName(methodWriterContext, fieldWriter, i2);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V");
        methodWriter.visitLabel(label3);
    }

    private void gwFieldValueArray(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        String str;
        String str2;
        MethodWriter methodWriter = methodWriterContext.mw;
        Class cls = fieldWriter.fieldClass;
        if (cls == char[].class) {
            str = METHOD_DESC_WRITE_CArray;
            str2 = "writeString";
        } else if (cls == boolean[].class) {
            str = METHOD_DESC_WRITE_ZARRAY;
            str2 = "writeBool";
        } else if (cls == byte[].class) {
            str = METHOD_DESC_WRITE_BArray;
            str2 = "writeBinary";
        } else if (cls == short[].class) {
            str = METHOD_DESC_WRITE_SArray;
            str2 = "writeInt16";
        } else if (cls == float[].class) {
            str = METHOD_DESC_WRITE_FARRAY;
            str2 = "writeFloat";
        } else if (cls == double[].class) {
            str = METHOD_DESC_WRITE_DARRAY;
            str2 = "writeDouble";
        } else if (cls.isEnum()) {
            str = METHOD_DESC_WRITE_ENUM;
            str2 = "writeEnumJSONB";
        } else {
            throw new UnsupportedOperationException();
        }
        methodWriter.aload(0);
        methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
        methodWriter.aload(1);
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, str2, str);
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x02a4  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x02d0  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x02d6  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0369  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0383  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0386  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x038f  */
    /* JADX WARN: Removed duplicated region for block: B:96:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void gwFieldName(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.MethodWriterContext r26, com.alibaba.fastjson2.writer.FieldWriter r27, int r28) {
        /*
            Method dump skipped, instruction units count: 984
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.gwFieldName(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM$MethodWriterContext, com.alibaba.fastjson2.writer.FieldWriter, int):void");
    }

    private void gwFieldValueInt64VA(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2, boolean z) {
        MethodWriter methodWriter = methodWriterContext.mw;
        int iVar = methodWriterContext.var(fieldWriter.fieldClass);
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnonnull(label2);
        methodWriter.iload(methodWriterContext.var(WRITE_NULLS));
        methodWriter.ifne(label3);
        methodWriter.goto_(label);
        methodWriter.visitLabel(label3);
        gwFieldName(methodWriterContext, fieldWriter, i2);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeArrayNull", "()V");
        methodWriter.goto_(label);
        methodWriter.visitLabel(label2);
        gwFieldName(methodWriterContext, fieldWriter, i2);
        boolean z2 = (fieldWriter.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        methodWriter.aload(1);
        methodWriter.aload(iVar);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, z2 ? "writeString" : "writeInt64", "([J)V");
        methodWriter.visitLabel(label);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x007d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void gwFieldValueInt64V(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.MethodWriterContext r17, com.alibaba.fastjson2.writer.FieldWriter r18, int r19, int r20, boolean r21) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r2 = r18
            r4 = r20
            com.alibaba.fastjson2.internal.asm.MethodWriter r6 = r1.mw
            java.lang.String r3 = r2.format
            java.lang.String r5 = r1.classNameType
            java.lang.Class r7 = r2.fieldClass
            int r8 = r1.var(r7)
            java.lang.String r9 = "WRITE_DEFAULT_VALUE"
            int r9 = r1.var(r9)
            com.alibaba.fastjson2.internal.asm.Label r10 = new com.alibaba.fastjson2.internal.asm.Label
            r10.<init>()
            com.alibaba.fastjson2.internal.asm.Label r11 = new com.alibaba.fastjson2.internal.asm.Label
            r11.<init>()
            r12 = r19
            r0.genGetObject(r1, r2, r4, r12)
            r6.dup2()
            r6.storeLocal(r7, r8)
            r6.cmpWithZero(r7)
            r6.ifne(r10)
            java.lang.Object r13 = r2.defaultValue
            if (r13 != 0) goto L42
            r6.iload(r9)
            r6.ifeq(r10)
            r6.goto_(r11)
        L42:
            r6.visitLabel(r10)
            java.lang.Class r9 = java.lang.Long.TYPE
            if (r7 != r9) goto L9d
            java.lang.String r7 = "iso8601"
            boolean r3 = r7.equals(r3)
            java.lang.String r7 = "writeInt64"
            if (r3 != 0) goto L7d
            long r12 = r2.features
            com.alibaba.fastjson2.JSONWriter$Feature r10 = com.alibaba.fastjson2.JSONWriter.Feature.WriteNonStringValueAsString
            long r14 = r10.mask
            com.alibaba.fastjson2.JSONWriter$Feature r10 = com.alibaba.fastjson2.JSONWriter.Feature.WriteLongAsString
            long r9 = r10.mask
            long r9 = r9 | r14
            com.alibaba.fastjson2.JSONWriter$Feature r14 = com.alibaba.fastjson2.JSONWriter.Feature.BrowserCompatible
            long r14 = r14.mask
            long r9 = r9 | r14
            long r9 = r9 & r12
            r12 = 0
            int r9 = (r9 > r12 ? 1 : (r9 == r12 ? 0 : -1))
            if (r9 == 0) goto L6b
            goto L7d
        L6b:
            r0.gwFieldName(r1, r2, r4)
            r1 = 1
            r6.aload(r1)
            r6.lload(r8)
            java.lang.String r1 = com.alibaba.fastjson2.internal.asm.ASMUtils.TYPE_JSON_WRITER
            java.lang.String r2 = "(J)V"
            r6.invokevirtual(r1, r7, r2)
            goto Lac
        L7d:
            r1 = 0
            r6.aload(r1)
            java.lang.String r1 = fieldWriter(r4)
            java.lang.String r2 = com.alibaba.fastjson2.internal.asm.ASMUtils.DESC_FIELD_WRITER
            r6.getfield(r5, r1, r2)
            r1 = 1
            r6.aload(r1)
            r6.lload(r8)
            java.lang.String r1 = com.alibaba.fastjson2.internal.asm.ASMUtils.TYPE_FIELD_WRITER
            if (r3 == 0) goto L97
            java.lang.String r7 = "writeDate"
        L97:
            java.lang.String r2 = com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.METHOD_DESC_WRITE_J
            r6.invokevirtual(r1, r7, r2)
            goto Lac
        L9d:
            java.lang.Class r3 = java.lang.Double.TYPE
            if (r7 != r3) goto Lb0
            r0.gwFieldName(r1, r2, r4)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r8)
            r3 = r12
            r0.gwValue(r1, r2, r3, r4, r5)
        Lac:
            r6.visitLabel(r11)
            return
        Lb0:
            java.lang.UnsupportedOperationException r0 = new java.lang.UnsupportedOperationException
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.gwFieldValueInt64V(com.alibaba.fastjson2.writer.ObjectWriterCreatorASM$MethodWriterContext, com.alibaba.fastjson2.writer.FieldWriter, int, int, boolean):void");
    }

    void gwFieldValueIntVA(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2, boolean z) {
        MethodWriter methodWriter = methodWriterContext.mw;
        int iVar = methodWriterContext.var(fieldWriter.fieldClass);
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnonnull(label2);
        methodWriter.iload(methodWriterContext.var(WRITE_NULLS));
        methodWriter.ifne(label3);
        methodWriter.goto_(label);
        methodWriter.visitLabel(label3);
        gwFieldName(methodWriterContext, fieldWriter, i2);
        methodWriter.aload(1);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeArrayNull", "()V");
        methodWriter.goto_(label);
        methodWriter.visitLabel(label2);
        gwFieldName(methodWriterContext, fieldWriter, i2);
        boolean z2 = (fieldWriter.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        methodWriter.aload(1);
        methodWriter.aload(iVar);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, z2 ? "writeString" : "writeInt32", "([I)V");
        methodWriter.visitLabel(label);
    }

    private void gwFieldValueInt32V(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2, boolean z) {
        MethodWriter methodWriter = methodWriterContext.mw;
        String str = fieldWriter.format;
        String str2 = methodWriterContext.classNameType;
        Class<?> cls = fieldWriter.fieldClass;
        int iVar = methodWriterContext.var(cls);
        int iVar2 = methodWriterContext.var(NOT_WRITE_DEFAULT_VALUE);
        Label label = new Label();
        Label label2 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup(cls);
        methodWriter.storeLocal(cls, iVar);
        methodWriter.cmpWithZero(cls);
        methodWriter.ifne(label);
        if (fieldWriter.defaultValue == null) {
            methodWriter.iload(iVar2);
            methodWriter.ifeq(label);
            methodWriter.goto_(label2);
        }
        methodWriter.visitLabel(label);
        gwFieldName(methodWriterContext, fieldWriter, i2);
        gwValue(methodWriterContext, fieldWriter, i, i2, Integer.valueOf(iVar));
        methodWriter.visitLabel(label2);
    }

    private void gwFieldValueBooleanV(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2, boolean z) {
        MethodWriter methodWriter = methodWriterContext.mw;
        String str = methodWriterContext.classNameType;
        int iVar = methodWriterContext.var(Boolean.TYPE);
        int iVar2 = methodWriterContext.var(NOT_WRITE_DEFAULT_VALUE);
        Label label = new Label();
        Label label2 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.istore(iVar);
        methodWriter.ifne(label);
        if (fieldWriter.defaultValue == null) {
            methodWriter.iload(iVar2);
            methodWriter.ifeq(label);
            methodWriter.goto_(label2);
        }
        methodWriter.visitLabel(label);
        methodWriter.aload(0);
        methodWriter.getfield(str, fieldWriter(i2), ASMUtils.DESC_FIELD_WRITER);
        methodWriter.aload(1);
        methodWriter.iload(iVar);
        methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "writeBool", METHOD_DESC_WRITE_Z);
        methodWriter.visitLabel(label2);
    }

    private void gwFieldValueString(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        boolean z;
        long j;
        Label label;
        Label label2;
        int i3;
        long j2;
        boolean z2 = methodWriterContext.jsonb;
        long j3 = fieldWriter.features | methodWriterContext.objectFeatures;
        MethodWriter methodWriter = methodWriterContext.mw;
        Class cls = fieldWriter.fieldClass;
        String str = fieldWriter.format;
        int iVar = methodWriterContext.var(cls);
        Label label3 = new Label();
        Label label4 = new Label();
        genGetObject(methodWriterContext, fieldWriter, i2, i);
        methodWriter.dup();
        methodWriter.astore(iVar);
        methodWriter.ifnull(label3);
        if ("trim".equals(str)) {
            methodWriter.aload(iVar);
            methodWriter.invokevirtual("java/lang/String", "trim", "()Ljava/lang/String;");
            methodWriter.astore(iVar);
        }
        if ((JSONWriter.Feature.IgnoreEmpty.mask & j3) == 0) {
            label = new Label();
            z = z2;
            j = j3;
            methodWriterContext.genIsEnabled(JSONWriter.Feature.IgnoreEmpty.mask, label);
        } else {
            z = z2;
            j = j3;
            label = null;
        }
        methodWriter.aload(iVar);
        methodWriter.invokevirtual("java/lang/String", "isEmpty", "()Z");
        methodWriter.ifne(label4);
        if (label != null) {
            methodWriter.visitLabel(label);
        }
        gwFieldName(methodWriterContext, fieldWriter, i2);
        gwString(methodWriterContext, z && "symbol".equals(str), false, iVar);
        methodWriter.goto_(label4);
        methodWriter.visitLabel(label3);
        Label label5 = new Label();
        Label label6 = new Label();
        long j4 = JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask | JSONWriter.Feature.WriteNullBooleanAsFalse.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask;
        if ((j & (JSONWriter.Feature.WriteNulls.mask | j4)) == 0) {
            label2 = label6;
            methodWriterContext.genIsEnabled(JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask, label2, label4);
        } else {
            label2 = label6;
        }
        methodWriter.visitLabel(label2);
        if (fieldWriter.defaultValue == null) {
            methodWriterContext.genIsDisabled(JSONWriter.Feature.NotWriteDefaultValue.mask, label4);
        }
        gwFieldName(methodWriterContext, fieldWriter, i2);
        if ((j & j4) == 0) {
            long j5 = JSONWriter.Feature.NullAsDefaultValue.mask;
            if (cls == String.class) {
                j2 = JSONWriter.Feature.WriteNullStringAsEmpty.mask;
            } else if (cls == Boolean.class) {
                j2 = JSONWriter.Feature.WriteNullBooleanAsFalse.mask;
            } else if (Number.class.isAssignableFrom(cls)) {
                j2 = JSONWriter.Feature.WriteNullNumberAsZero.mask;
            } else {
                if (Collection.class.isAssignableFrom(cls)) {
                    j2 = JSONWriter.Feature.WriteNullListAsEmpty.mask;
                }
                i3 = 1;
                methodWriter.aload(1);
                methodWriter.visitLdcInsn(j5);
                methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "isEnabled", "(J)Z");
                methodWriter.ifeq(label5);
            }
            j5 |= j2;
            i3 = 1;
            methodWriter.aload(1);
            methodWriter.visitLdcInsn(j5);
            methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "isEnabled", "(J)Z");
            methodWriter.ifeq(label5);
        } else {
            i3 = 1;
        }
        methodWriter.aload(i3);
        methodWriter.visitLdcInsn("");
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeString", METHOD_DESC_WRITE_REFERENCE);
        methodWriter.goto_(label4);
        methodWriter.visitLabel(label5);
        methodWriter.aload(i3);
        methodWriter.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "writeStringNull", "()V");
        methodWriter.visitLabel(label4);
    }

    private void genMethodInit(List<FieldWriter> list, ClassWriter classWriter, String str, String str2) {
        MethodWriter methodWriterVisitMethod = classWriter.visitMethod(1, "<init>", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;JLjava/util/List;)V", 64);
        methodWriterVisitMethod.aload(0);
        methodWriterVisitMethod.aload(1);
        methodWriterVisitMethod.aload(2);
        methodWriterVisitMethod.aload(3);
        methodWriterVisitMethod.lload(4);
        methodWriterVisitMethod.aload(6);
        methodWriterVisitMethod.invokespecial(str2, "<init>", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;JLjava/util/List;)V");
        if (str2 == ASMUtils.TYPE_OBJECT_WRITER_ADAPTER) {
            for (int i = 0; i < list.size(); i++) {
                methodWriterVisitMethod.aload(0);
                methodWriterVisitMethod.dup();
                methodWriterVisitMethod.getfield(ASMUtils.TYPE_OBJECT_WRITER_ADAPTER, "fieldWriterArray", ASMUtils.DESC_FIELD_WRITER_ARRAY);
                methodWriterVisitMethod.iconst_n(i);
                methodWriterVisitMethod.aaload();
                methodWriterVisitMethod.checkcast(ASMUtils.TYPE_FIELD_WRITER);
                methodWriterVisitMethod.putfield(str, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            }
        }
        methodWriterVisitMethod.return_();
        methodWriterVisitMethod.visitMaxs(7, 7);
    }

    private void genFields(List<FieldWriter> list, ClassWriter classWriter, String str) {
        if (str != ASMUtils.TYPE_OBJECT_WRITER_ADAPTER) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            classWriter.visitField(1, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0070  */
    @Override // com.alibaba.fastjson2.writer.ObjectWriterCreator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public <T> com.alibaba.fastjson2.writer.FieldWriter<T> createFieldWriter(com.alibaba.fastjson2.writer.ObjectWriterProvider r26, java.lang.String r27, int r28, long r29, java.lang.String r31, java.util.Locale r32, java.lang.String r33, java.lang.reflect.Field r34, com.alibaba.fastjson2.writer.ObjectWriter r35, java.lang.Class<?> r36) {
        /*
            Method dump skipped, instruction units count: 787
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.createFieldWriter(com.alibaba.fastjson2.writer.ObjectWriterProvider, java.lang.String, int, long, java.lang.String, java.util.Locale, java.lang.String, java.lang.reflect.Field, com.alibaba.fastjson2.writer.ObjectWriter, java.lang.Class):com.alibaba.fastjson2.writer.FieldWriter");
    }

    private FieldWriter createFieldWriterList(ObjectWriterProvider objectWriterProvider, String str, int i, long j, String str2, String str3, Field field, Class<?> cls, Type type, Type type2, Class<?> cls2) {
        FieldWriter fieldWriterJitFieldWriterList = jitFieldWriterList(objectWriterProvider, str, i, j, str2, str3, field, cls, type, type2, cls2);
        return fieldWriterJitFieldWriterList == null ? new FieldWriterListField(str, type, i, j, str2, str3, type2, cls2, field, cls) : fieldWriterJitFieldWriterList;
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x0454 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x00c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.alibaba.fastjson2.writer.FieldWriter jitFieldWriterList(com.alibaba.fastjson2.writer.ObjectWriterProvider r33, java.lang.String r34, int r35, long r36, java.lang.String r38, java.lang.String r39, java.lang.reflect.Field r40, java.lang.Class<?> r41, java.lang.reflect.Type r42, java.lang.reflect.Type r43, java.lang.Class<?> r44) {
        /*
            Method dump skipped, instruction units count: 1109
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterCreatorASM.jitFieldWriterList(com.alibaba.fastjson2.writer.ObjectWriterProvider, java.lang.String, int, long, java.lang.String, java.lang.String, java.lang.reflect.Field, java.lang.Class, java.lang.reflect.Type, java.lang.reflect.Type, java.lang.Class):com.alibaba.fastjson2.writer.FieldWriter");
    }

    void genGetObject(MethodWriterContext methodWriterContext, FieldWriter fieldWriter, int i, int i2) {
        String str;
        String str2;
        MethodWriter methodWriter = methodWriterContext.mw;
        Class<?> cls = methodWriterContext.objectClass;
        String strType = cls == null ? ASMUtils.TYPE_OBJECT : ASMUtils.type(cls);
        Class cls2 = fieldWriter.fieldClass;
        Member member = fieldWriter.method != null ? fieldWriter.method : fieldWriter.field;
        Function function = fieldWriter.getFunction();
        if (member == null && function != null) {
            methodWriter.aload(0);
            methodWriter.getfield(methodWriterContext.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            methodWriter.invokevirtual(ASMUtils.TYPE_FIELD_WRITER, "getFunction", "()Ljava/util/function/Function;");
            methodWriter.aload(i2);
            methodWriter.invokeinterface(ASMUtils.type(Function.class), "apply", "(Ljava/lang/Object;)Ljava/lang/Object;");
            methodWriter.checkcast(ASMUtils.type(cls2));
            return;
        }
        if (member instanceof Method) {
            methodWriter.aload(i2);
            methodWriter.checkcast(strType);
            if (cls.isInterface()) {
                methodWriter.invokeinterface(strType, member.getName(), "()" + ASMUtils.desc(cls2));
                return;
            } else {
                methodWriter.invokevirtual(strType, member.getName(), "()" + ASMUtils.desc(cls2));
                return;
            }
        }
        if (Modifier.isPublic(cls.getModifiers()) && Modifier.isPublic(member.getModifiers()) && !this.classLoader.isExternalClass(cls)) {
            methodWriter.aload(i2);
            methodWriter.checkcast(strType);
            methodWriter.getfield(strType, member.getName(), ASMUtils.desc(cls2));
            return;
        }
        Field field = (Field) member;
        String strType2 = null;
        if (cls2 == Integer.TYPE) {
            str = "getInt";
            str2 = "(Ljava/lang/Object;J)I";
        } else if (cls2 == Long.TYPE) {
            str = "getLong";
            str2 = "(Ljava/lang/Object;J)J";
        } else if (cls2 == Float.TYPE) {
            str = "getFloat";
            str2 = "(Ljava/lang/Object;J)F";
        } else if (cls2 == Double.TYPE) {
            str = "getDouble";
            str2 = "(Ljava/lang/Object;J)D";
        } else if (cls2 == Character.TYPE) {
            str = "getChar";
            str2 = "(Ljava/lang/Object;J)C";
        } else if (cls2 == Byte.TYPE) {
            str = "getByte";
            str2 = "(Ljava/lang/Object;J)B";
        } else if (cls2 == Short.TYPE) {
            str = "getShort";
            str2 = "(Ljava/lang/Object;J)S";
        } else if (cls2 == Boolean.TYPE) {
            str = "getBoolean";
            str2 = "(Ljava/lang/Object;J)Z";
        } else {
            if (cls2.isEnum()) {
                strType2 = "java/lang/Enum";
            } else if (ObjectWriterProvider.isPrimitiveOrEnum(cls2)) {
                strType2 = ASMUtils.type(cls2);
            } else if (cls2.isArray() && ObjectWriterProvider.isPrimitiveOrEnum(cls2.getComponentType())) {
                strType2 = ASMUtils.type(cls2);
            } else if (Map.class.isAssignableFrom(cls2)) {
                strType2 = "java/util/Map";
            } else if (List.class.isAssignableFrom(cls2)) {
                strType2 = "java/util/List";
            } else if (Collection.class.isAssignableFrom(cls2)) {
                strType2 = "java/util/Collection";
            }
            str = "getObject";
            str2 = "(Ljava/lang/Object;J)Ljava/lang/Object;";
        }
        methodWriter.getstatic(ObjectWriterCreatorASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
        methodWriter.aload(i2);
        methodWriter.visitLdcInsn(JDKUtils.UNSAFE.objectFieldOffset(field));
        methodWriter.invokevirtual("sun/misc/Unsafe", str, str2);
        if (strType2 != null) {
            methodWriter.checkcast(strType2);
        }
    }

    static class MethodWriterContext {
        final String classNameType;
        final boolean jsonb;
        int maxVariant;
        final MethodWriter mw;
        final Class objectClass;
        final long objectFeatures;
        final ObjectWriterProvider provider;
        final Map<Object, Integer> variants = new LinkedHashMap();

        public MethodWriterContext(ObjectWriterProvider objectWriterProvider, Class cls, long j, String str, MethodWriter methodWriter, int i, boolean z) {
            this.provider = objectWriterProvider;
            this.objectClass = cls;
            this.objectFeatures = j;
            this.classNameType = str;
            this.mw = methodWriter;
            this.jsonb = z;
            this.maxVariant = i;
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

        int var2(Object obj) {
            Integer numValueOf = this.variants.get(obj);
            if (numValueOf == null) {
                numValueOf = Integer.valueOf(this.maxVariant);
                this.variants.put(obj, numValueOf);
                this.maxVariant += 2;
            }
            return numValueOf.intValue();
        }

        void genVariantsMethodBefore(boolean z) {
            Label label = new Label();
            Label label2 = new Label();
            this.mw.aload(1);
            this.mw.invokevirtual(ASMUtils.TYPE_JSON_WRITER, "getFeatures", "()J");
            this.mw.lstore(var2(ObjectWriterCreatorASM.CONTEXT_FEATURES));
            if (!z) {
                Label label3 = new Label();
                Label label4 = new Label();
                this.mw.aload(1);
                this.mw.getfield(ASMUtils.TYPE_JSON_WRITER, "useSingleQuote", "Z");
                this.mw.ifne(label3);
                this.mw.lload(var2(ObjectWriterCreatorASM.CONTEXT_FEATURES));
                this.mw.visitLdcInsn(JSONWriter.Feature.UnquoteFieldName.mask | JSONWriter.Feature.UseSingleQuotes.mask);
                this.mw.land();
                this.mw.lconst_0();
                this.mw.lcmp();
                this.mw.ifne(label3);
                this.mw.iconst_1();
                this.mw.goto_(label4);
                this.mw.visitLabel(label3);
                this.mw.iconst_0();
                this.mw.visitLabel(label4);
                this.mw.istore(var2(ObjectWriterCreatorASM.NAME_DIRECT));
            } else {
                Label label5 = new Label();
                Label label6 = new Label();
                this.mw.aload(1);
                this.mw.getfield(ASMUtils.TYPE_JSON_WRITER, "symbolTable", ObjectWriterCreatorASM.DESC_SYMBOL);
                this.mw.ifnonnull(label5);
                this.mw.lload(var2(ObjectWriterCreatorASM.CONTEXT_FEATURES));
                this.mw.visitLdcInsn(JSONWriter.Feature.WriteNameAsSymbol.mask);
                this.mw.land();
                this.mw.lconst_0();
                this.mw.lcmp();
                this.mw.ifne(label5);
                this.mw.iconst_1();
                this.mw.goto_(label6);
                this.mw.visitLabel(label5);
                this.mw.iconst_0();
                this.mw.visitLabel(label6);
                this.mw.istore(var2(ObjectWriterCreatorASM.NAME_DIRECT));
            }
            genIsEnabledAndAssign(JSONWriter.Feature.NotWriteDefaultValue.mask, var(ObjectWriterCreatorASM.NOT_WRITE_DEFAULT_VALUE));
            this.mw.iload(var(ObjectWriterCreatorASM.NOT_WRITE_DEFAULT_VALUE));
            this.mw.ifeq(label);
            this.mw.iconst_0();
            this.mw.istore(var(ObjectWriterCreatorASM.WRITE_NULLS));
            this.mw.goto_(label2);
            this.mw.visitLabel(label);
            genIsEnabledAndAssign(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask, var(ObjectWriterCreatorASM.WRITE_NULLS));
            this.mw.visitLabel(label2);
        }

        void genIsEnabled(long j, Label label) {
            this.mw.lload(var2(ObjectWriterCreatorASM.CONTEXT_FEATURES));
            this.mw.visitLdcInsn(j);
            this.mw.land();
            this.mw.lconst_0();
            this.mw.lcmp();
            if (label != null) {
                this.mw.ifeq(label);
            }
        }

        void genIsEnabled(long j, long j2, Label label) {
            this.mw.lload(var2(ObjectWriterCreatorASM.CONTEXT_FEATURES));
            this.mw.visitLdcInsn(j);
            this.mw.lor();
            this.mw.visitLdcInsn(j2);
            this.mw.land();
            this.mw.lconst_0();
            this.mw.lcmp();
            if (label != null) {
                this.mw.ifeq(label);
            }
        }

        void genIsDisabled(long j, Label label) {
            this.mw.lload(var2(ObjectWriterCreatorASM.CONTEXT_FEATURES));
            this.mw.visitLdcInsn(j);
            this.mw.land();
            this.mw.lconst_0();
            this.mw.lcmp();
            this.mw.ifne(label);
        }

        void genIsEnabled(long j, Label label, Label label2) {
            this.mw.lload(var2(ObjectWriterCreatorASM.CONTEXT_FEATURES));
            this.mw.visitLdcInsn(j);
            this.mw.land();
            this.mw.lconst_0();
            this.mw.lcmp();
            this.mw.ifeq(label2);
            this.mw.goto_(label);
        }

        void genIsEnabledAndAssign(long j, int i) {
            this.mw.lload(var2(ObjectWriterCreatorASM.CONTEXT_FEATURES));
            this.mw.visitLdcInsn(j);
            this.mw.land();
            this.mw.lconst_0();
            this.mw.lcmp();
            this.mw.istore(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void loadFieldType(int i, Type type) {
            if ((type instanceof Class) && type.getTypeName().startsWith("java")) {
                this.mw.visitLdcInsn((Class) type);
                return;
            }
            this.mw.aload(0);
            this.mw.getfield(this.classNameType, ObjectWriterCreatorASM.fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            this.mw.getfield(ASMUtils.TYPE_FIELD_WRITER, "fieldType", "Ljava/lang/reflect/Type;");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void loadFieldClass(int i, Class cls) {
            if (cls.getName().startsWith("java")) {
                this.mw.visitLdcInsn(cls);
                return;
            }
            this.mw.aload(0);
            this.mw.getfield(this.classNameType, ObjectWriterCreatorASM.fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            this.mw.getfield(ASMUtils.TYPE_FIELD_WRITER, "fieldClass", "Ljava/lang/Class;");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void ldcIFEQ(String str, Number number, Number number2) {
            if (number.longValue() == number2.longValue()) {
                this.mw.visitLdcInsn(number);
                return;
            }
            Label label = new Label();
            Label label2 = new Label();
            this.mw.iload(var(str));
            this.mw.ifeq(label);
            this.mw.visitLdcInsn(number);
            this.mw.goto_(label2);
            this.mw.visitLabel(label);
            this.mw.visitLdcInsn(number2);
            this.mw.visitLabel(label2);
        }

        public boolean disableSupportArrayMapping() {
            return (this.objectFeatures & FieldInfo.DISABLE_ARRAY_MAPPING) != 0;
        }

        public boolean disableReferenceDetect() {
            return (this.objectFeatures & FieldInfo.DISABLE_REFERENCE_DETECT) != 0;
        }

        public boolean disableSmartMatch() {
            return (this.objectFeatures & FieldInfo.DISABLE_ARRAY_MAPPING) != 0;
        }

        public boolean disableAutoType() {
            return (this.objectFeatures & FieldInfo.DISABLE_AUTO_TYPE) != 0;
        }

        public boolean disableJSONB() {
            return (this.objectFeatures & FieldInfo.DISABLE_JSONB) != 0;
        }
    }

    static class FieldWriterGroup {
        final boolean direct;
        boolean end;
        final List<FieldWriterRecord> fieldWriters = new ArrayList();
        final boolean start;

        public FieldWriterGroup(boolean z, boolean z2) {
            this.start = z;
            this.direct = z2;
        }
    }

    static final class FieldWriterRecord {
        final FieldWriter fieldWriter;
        final int ordinal;

        public FieldWriterRecord(FieldWriter fieldWriter, int i) {
            this.fieldWriter = fieldWriter;
            this.ordinal = i;
        }

        static List<FieldWriterRecord> of(List<FieldWriter> list) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                arrayList.add(new FieldWriterRecord(list.get(i), i));
            }
            return arrayList;
        }
    }

    static List<FieldWriterGroup> buildGroups(long j, List<FieldWriter> list) {
        ArrayList arrayList = new ArrayList();
        if (list.isEmpty()) {
            FieldWriterGroup fieldWriterGroup = new FieldWriterGroup(true, false);
            fieldWriterGroup.end = true;
            arrayList.add(fieldWriterGroup);
            return arrayList;
        }
        FieldWriterGroup fieldWriterGroup2 = null;
        int i = 0;
        while (i < list.size()) {
            FieldWriter fieldWriter = list.get(i);
            boolean zSupportDirectWrite = supportDirectWrite(j, fieldWriter);
            if (fieldWriterGroup2 == null || fieldWriterGroup2.direct != zSupportDirectWrite) {
                fieldWriterGroup2 = new FieldWriterGroup(i == 0, zSupportDirectWrite);
                arrayList.add(fieldWriterGroup2);
            }
            fieldWriterGroup2.fieldWriters.add(new FieldWriterRecord(fieldWriter, i));
            if (i == list.size() - 1) {
                fieldWriterGroup2.end = true;
            }
            i++;
        }
        return arrayList;
    }

    static boolean supportDirectWrite(long j, FieldWriter fieldWriter) {
        if (JSONWriter.Feature.WriteNonStringValueAsString.mask == (j & JSONWriter.Feature.WriteNonStringValueAsString.mask) || fieldWriter.format != null) {
            return false;
        }
        Class cls = fieldWriter.fieldClass;
        if (!Collection.class.isAssignableFrom(cls)) {
            return cls == Byte.TYPE || cls == Byte.class || cls == Short.TYPE || cls == Short.class || cls == Integer.TYPE || cls == Integer.class || cls == Long.TYPE || cls == Long.class || cls == Float.TYPE || cls == Float.class || cls == Double.TYPE || cls == Double.class || cls == Boolean.TYPE || cls == Boolean.class || cls == String.class || cls == String[].class || cls == UUID.class || cls == LocalDate.class || cls == LocalDateTime.class || cls == LocalTime.class || cls == OffsetDateTime.class || cls == OffsetTime.class || cls == Instant.class || (fieldWriter instanceof FieldWriterEnum);
        }
        Class itemClass = fieldWriter.getItemClass();
        return itemClass == String.class || itemClass == Long.class;
    }
}
