package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public final class ObjectReaderImplList implements ObjectReader {
    static List kotlinEmptyList;
    static Set kotlinEmptySet;
    final Function builder;
    volatile Constructor constructor;
    volatile boolean instanceError;
    final Class instanceType;
    final long instanceTypeHash;
    final Class itemClass;
    final String itemClassName;
    final long itemClassNameHash;
    ObjectReader itemObjectReader;
    final Type itemType;
    final Class listClass;
    Object listSingleton;
    final Type listType;
    static final Class CLASS_EMPTY_SET = Collections.emptySet().getClass();
    static final Class CLASS_EMPTY_LIST = Collections.emptyList().getClass();
    static final Class CLASS_SINGLETON = Collections.singleton(0).getClass();
    static final Class CLASS_SINGLETON_LIST = Collections.singletonList(0).getClass();
    static final Class CLASS_ARRAYS_LIST = Arrays.asList(0).getClass();
    static final Class CLASS_UNMODIFIABLE_COLLECTION = Collections.unmodifiableCollection(Collections.emptyList()).getClass();
    static final Class CLASS_UNMODIFIABLE_LIST = Collections.unmodifiableList(Collections.emptyList()).getClass();
    static final Class CLASS_UNMODIFIABLE_SET = Collections.unmodifiableSet(Collections.emptySet()).getClass();
    static final Class CLASS_UNMODIFIABLE_SORTED_SET = Collections.unmodifiableSortedSet(Collections.emptySortedSet()).getClass();
    static final Class CLASS_UNMODIFIABLE_NAVIGABLE_SET = Collections.unmodifiableNavigableSet(Collections.emptyNavigableSet()).getClass();
    public static ObjectReaderImplList INSTANCE = new ObjectReaderImplList(ArrayList.class, ArrayList.class, ArrayList.class, Object.class, null);

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:165:0x023e  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x024e  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0271  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x027d  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x029c  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x02a8  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x02b4  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x02c0  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x004d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.alibaba.fastjson2.reader.ObjectReader of(java.lang.reflect.Type r8, java.lang.Class r9, long r10) {
        /*
            Method dump skipped, instruction units count: 860
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderImplList.of(java.lang.reflect.Type, java.lang.Class, long):com.alibaba.fastjson2.reader.ObjectReader");
    }

    static /* synthetic */ Object lambda$of$0(Type type, Object obj) {
        Collection collection = (Collection) obj;
        if (collection.isEmpty() && (type instanceof Class)) {
            return EnumSet.noneOf((Class) type);
        }
        return EnumSet.copyOf(collection);
    }

    ObjectReaderImplList(Class cls, Object obj) {
        this(cls, cls, cls, Object.class, null);
        this.listSingleton = obj;
    }

    public ObjectReaderImplList(Type type, Class cls, Class cls2, Type type2, Function function) {
        this.listType = type;
        this.listClass = cls;
        this.instanceType = cls2;
        this.instanceTypeHash = Fnv.hashCode64(TypeUtils.getTypeName(cls2));
        this.itemType = type2;
        Class<?> cls3 = TypeUtils.getClass(type2);
        this.itemClass = cls3;
        this.builder = function;
        String typeName = cls3 != null ? TypeUtils.getTypeName(cls3) : null;
        this.itemClassName = typeName;
        this.itemClassNameHash = typeName != null ? Fnv.hashCode64(typeName) : 0L;
    }

    static Set getKotlinEmptySet(Class cls) {
        Set set = kotlinEmptySet;
        if (set != null) {
            return set;
        }
        try {
            Field field = cls.getField("INSTANCE");
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Set set2 = (Set) field.get(null);
            kotlinEmptySet = set2;
            return set2;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException("Failed to get singleton of " + cls, e);
        }
    }

    static List getKotlinEmptyList(Class cls) {
        List list = kotlinEmptyList;
        if (list != null) {
            return list;
        }
        try {
            Field field = cls.getField("INSTANCE");
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            List list2 = (List) field.get(null);
            kotlinEmptyList = list2;
            return list2;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException("Failed to get singleton of " + cls, e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Class getObjectClass() {
        return this.listClass;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Function getBuildFunction() {
        return this.builder;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(Collection collection, long j) {
        Collection arrayList;
        if (collection.size() == 0 && this.listClass == List.class) {
            ArrayList arrayList2 = new ArrayList();
            Function function = this.builder;
            return function != null ? function.apply(arrayList2) : arrayList2;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        if (this.instanceType == ArrayList.class) {
            arrayList = new ArrayList(collection.size());
        } else {
            arrayList = (Collection) createInstance(0L);
        }
        for (Object objCreateInstance : collection) {
            if (objCreateInstance == null) {
                arrayList.add(null);
            } else {
                Class<?> cls = objCreateInstance.getClass();
                if ((cls == JSONObject.class || cls == TypeUtils.CLASS_JSON_OBJECT_1x) && this.itemClass != cls) {
                    if (this.itemObjectReader == null) {
                        this.itemObjectReader = defaultObjectReaderProvider.getObjectReader(this.itemType);
                    }
                    objCreateInstance = this.itemObjectReader.createInstance((Map) objCreateInstance, j);
                } else {
                    Type type = this.itemType;
                    if (cls != type) {
                        Function typeConvert = defaultObjectReaderProvider.getTypeConvert(cls, type);
                        if (typeConvert != null) {
                            objCreateInstance = typeConvert.apply(objCreateInstance);
                        } else if (objCreateInstance instanceof Map) {
                            Map map = (Map) objCreateInstance;
                            if (this.itemObjectReader == null) {
                                this.itemObjectReader = defaultObjectReaderProvider.getObjectReader(this.itemType);
                            }
                            objCreateInstance = this.itemObjectReader.createInstance(map, 0L);
                        } else if (objCreateInstance instanceof Collection) {
                            if (this.itemObjectReader == null) {
                                this.itemObjectReader = defaultObjectReaderProvider.getObjectReader(this.itemType);
                            }
                            objCreateInstance = this.itemObjectReader.createInstance((Collection) objCreateInstance, j);
                        } else if (!this.itemClass.isInstance(objCreateInstance)) {
                            if (Enum.class.isAssignableFrom(this.itemClass)) {
                                if (this.itemObjectReader == null) {
                                    this.itemObjectReader = defaultObjectReaderProvider.getObjectReader(this.itemType);
                                }
                                ObjectReader objectReader = this.itemObjectReader;
                                if (objectReader instanceof ObjectReaderImplEnum) {
                                    objCreateInstance = ((ObjectReaderImplEnum) objectReader).getEnum((String) objCreateInstance);
                                } else {
                                    throw new JSONException("can not convert from " + cls + " to " + this.itemType);
                                }
                            } else {
                                throw new JSONException("can not convert from " + cls + " to " + this.itemType);
                            }
                        }
                    }
                }
                arrayList.add(objCreateInstance);
            }
        }
        Function function2 = this.builder;
        return function2 != null ? function2.apply(arrayList) : arrayList;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(long j) {
        JSONException jSONException;
        Class cls = this.instanceType;
        if (cls == ArrayList.class) {
            return JDKUtils.JVM_VERSION == 8 ? new ArrayList(10) : new ArrayList();
        }
        if (cls == LinkedList.class) {
            return new LinkedList();
        }
        if (cls == HashSet.class) {
            return new HashSet();
        }
        if (cls == LinkedHashSet.class) {
            return new LinkedHashSet();
        }
        if (cls == TreeSet.class) {
            return new TreeSet();
        }
        Object obj = this.listSingleton;
        if (obj != null) {
            return obj;
        }
        if (cls != null) {
            if (this.constructor == null && !BeanUtils.hasPublicDefaultConstructor(this.instanceType)) {
                this.constructor = BeanUtils.getDefaultConstructor(this.instanceType, false);
                this.constructor.setAccessible(true);
            }
            if (this.instanceError) {
                jSONException = null;
            } else {
                try {
                    if (this.constructor != null) {
                        return this.constructor.newInstance(new Object[0]);
                    }
                    return this.instanceType.newInstance();
                } catch (IllegalAccessException | InstantiationException | RuntimeException | InvocationTargetException unused) {
                    this.instanceError = true;
                    jSONException = new JSONException("create list error, type " + this.instanceType);
                }
            }
            if (this.instanceError && List.class.isAssignableFrom(this.instanceType.getSuperclass())) {
                try {
                    return this.instanceType.getSuperclass().newInstance();
                } catch (IllegalAccessException | InstantiationException unused2) {
                    this.instanceError = true;
                    jSONException = new JSONException("create list error, type " + this.instanceType);
                }
            }
            if (jSONException != null) {
                throw jSONException;
            }
        }
        return new ArrayList();
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        JSONArray jSONArray;
        Collection kotlinEmptyList2;
        Object jSONBObject;
        Collection collection;
        ArrayList arrayList;
        Object jSONBObject2;
        if (jSONReader.nextIfNull()) {
            return null;
        }
        ObjectReader objectReaderCheckAutoType = jSONReader.checkAutoType(this.listClass, 0L, j);
        Function function = this.builder;
        Class objectClass = this.instanceType;
        if (objectReaderCheckAutoType != null) {
            if (objectReaderCheckAutoType instanceof ObjectReaderImplList) {
                ObjectReaderImplList objectReaderImplList = (ObjectReaderImplList) objectReaderCheckAutoType;
                objectClass = objectReaderImplList.instanceType;
                function = objectReaderImplList.builder;
            } else {
                objectClass = objectReaderCheckAutoType.getObjectClass();
            }
            if (objectClass == CLASS_UNMODIFIABLE_COLLECTION) {
                objectClass = ArrayList.class;
                function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplList$$ExternalSyntheticLambda5
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        return Collections.unmodifiableCollection((Collection) obj2);
                    }
                };
            } else if (objectClass == CLASS_UNMODIFIABLE_LIST) {
                objectClass = ArrayList.class;
                function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplList$$ExternalSyntheticLambda6
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        return Collections.unmodifiableList((List) obj2);
                    }
                };
            } else if (objectClass == CLASS_UNMODIFIABLE_SET) {
                objectClass = LinkedHashSet.class;
                function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplList$$ExternalSyntheticLambda7
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        return Collections.unmodifiableSet((Set) obj2);
                    }
                };
            } else if (objectClass == CLASS_UNMODIFIABLE_SORTED_SET) {
                objectClass = TreeSet.class;
                function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplList$$ExternalSyntheticLambda8
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        return Collections.unmodifiableSortedSet((SortedSet) obj2);
                    }
                };
            } else if (objectClass == CLASS_UNMODIFIABLE_NAVIGABLE_SET) {
                objectClass = TreeSet.class;
                function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplList$$ExternalSyntheticLambda9
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        return Collections.unmodifiableNavigableSet((NavigableSet) obj2);
                    }
                };
            } else if (objectClass == CLASS_SINGLETON) {
                objectClass = ArrayList.class;
                function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplList$$ExternalSyntheticLambda10
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        return Collections.singleton(((Collection) obj2).iterator().next());
                    }
                };
            } else if (objectClass == CLASS_SINGLETON_LIST) {
                objectClass = ArrayList.class;
                function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplList$$ExternalSyntheticLambda12
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        return Collections.singletonList(((List) obj2).get(0));
                    }
                };
            } else {
                String typeName = objectClass.getTypeName();
                typeName.hashCode();
                if (typeName.equals("kotlin.collections.EmptyList") || typeName.equals("kotlin.collections.EmptySet")) {
                    return objectReaderCheckAutoType.readObject(jSONReader, type, obj, j);
                }
            }
        }
        Class cls = objectClass;
        JSONReader jSONReader2 = jSONReader;
        int iStartArray = jSONReader2.startArray();
        if (iStartArray > 0 && this.itemObjectReader == null) {
            this.itemObjectReader = jSONReader2.getContext().getObjectReader(this.itemType);
        }
        int i = 0;
        if (cls == CLASS_ARRAYS_LIST) {
            Object[] objArr = new Object[iStartArray];
            List listAsList = Arrays.asList(objArr);
            while (i < iStartArray) {
                if (jSONReader2.isReference()) {
                    String reference = jSONReader2.readReference();
                    if ("..".equals(reference)) {
                        jSONBObject2 = listAsList;
                    } else {
                        jSONReader2.addResolveTask(listAsList, i, JSONPath.of(reference));
                        jSONBObject2 = null;
                    }
                } else {
                    jSONBObject2 = this.itemObjectReader.readJSONBObject(jSONReader2, this.itemType, Integer.valueOf(i), j);
                }
                objArr[i] = jSONBObject2;
                i++;
            }
            return listAsList;
        }
        if (cls == ArrayList.class) {
            if (iStartArray > 0) {
                kotlinEmptyList2 = arrayList;
                arrayList = new ArrayList(iStartArray);
            } else {
                kotlinEmptyList2 = arrayList;
                arrayList = new ArrayList();
            }
        } else if (cls == JSONArray.class) {
            if (iStartArray > 0) {
                kotlinEmptyList2 = jSONArray;
                jSONArray = new JSONArray(iStartArray);
            } else {
                kotlinEmptyList2 = jSONArray;
                jSONArray = new JSONArray();
            }
        } else if (cls == HashSet.class) {
            kotlinEmptyList2 = new HashSet();
        } else if (cls == LinkedHashSet.class) {
            kotlinEmptyList2 = new LinkedHashSet();
        } else if (cls == TreeSet.class) {
            kotlinEmptyList2 = new TreeSet();
        } else if (cls == CLASS_EMPTY_SET) {
            kotlinEmptyList2 = Collections.emptySet();
        } else if (cls == CLASS_EMPTY_LIST) {
            kotlinEmptyList2 = Collections.emptyList();
        } else if (cls == CLASS_SINGLETON_LIST) {
            ArrayList arrayList2 = new ArrayList();
            function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplList$$ExternalSyntheticLambda13
                @Override // java.util.function.Function
                public final Object apply(Object obj2) {
                    return Collections.singletonList(((Collection) obj2).iterator().next());
                }
            };
            kotlinEmptyList2 = arrayList2;
        } else if (cls == CLASS_UNMODIFIABLE_LIST) {
            ArrayList arrayList3 = new ArrayList();
            function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplList$$ExternalSyntheticLambda6
                @Override // java.util.function.Function
                public final Object apply(Object obj2) {
                    return Collections.unmodifiableList((List) obj2);
                }
            };
            kotlinEmptyList2 = arrayList3;
        } else if (cls != null && EnumSet.class.isAssignableFrom(cls)) {
            HashSet hashSet = new HashSet();
            function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplList$$ExternalSyntheticLambda14
                @Override // java.util.function.Function
                public final Object apply(Object obj2) {
                    return this.f$0.m48x4f914009(obj2);
                }
            };
            kotlinEmptyList2 = hashSet;
        } else if (cls != null && cls != this.listType) {
            String name = cls.getName();
            name.hashCode();
            if (!name.equals("kotlin.collections.EmptyList")) {
                if (name.equals("kotlin.collections.EmptySet")) {
                    kotlinEmptyList2 = getKotlinEmptySet(cls);
                } else {
                    try {
                        kotlinEmptyList2 = (Collection) cls.newInstance();
                    } catch (IllegalAccessException | InstantiationException e) {
                        throw new JSONException(jSONReader2.info("create instance error " + cls), e);
                    }
                }
            } else {
                kotlinEmptyList2 = getKotlinEmptyList(cls);
            }
        } else {
            kotlinEmptyList2 = (Collection) createInstance(jSONReader2.getContext().getFeatures() | j);
        }
        Function function2 = function;
        Collection collection2 = kotlinEmptyList2;
        ObjectReader objectReader = this.itemObjectReader;
        Type type2 = this.itemType;
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (actualTypeArguments.length == 1 && (type2 = actualTypeArguments[0]) != this.itemType) {
                objectReader = jSONReader2.getObjectReader(type2);
            }
        }
        while (true) {
            ObjectReader objectReader2 = objectReader;
            Type type3 = type2;
            if (i >= iStartArray) {
                break;
            }
            if (jSONReader2.isReference()) {
                String reference2 = jSONReader2.readReference();
                if ("..".equals(reference2)) {
                    collection = collection2;
                } else {
                    jSONReader2.addResolveTask(collection2, i, JSONPath.of(reference2));
                    if (collection2 instanceof List) {
                        collection = null;
                    } else {
                        objectReader = objectReader2;
                        type2 = type3;
                        i++;
                        jSONReader2 = jSONReader;
                    }
                }
                Collection collection3 = collection;
                objectReader = objectReader2;
                jSONBObject = collection3;
                type2 = type3;
            } else {
                ObjectReader objectReaderCheckAutoType2 = jSONReader.checkAutoType(this.itemClass, this.itemClassNameHash, j);
                if (objectReaderCheckAutoType2 != null) {
                    type2 = type3;
                    objectReader = objectReader2;
                    jSONBObject = objectReaderCheckAutoType2.readJSONBObject(jSONReader, type2, Integer.valueOf(i), j);
                } else {
                    type2 = type3;
                    objectReader = objectReader2;
                    jSONBObject = objectReader.readJSONBObject(jSONReader, type2, Integer.valueOf(i), j);
                }
            }
            collection2.add(jSONBObject);
            i++;
            jSONReader2 = jSONReader;
        }
        return function2 != null ? function2.apply(collection2) : collection2;
    }

    /* JADX INFO: renamed from: lambda$readJSONBObject$12$com-alibaba-fastjson2-reader-ObjectReaderImplList, reason: not valid java name */
    /* synthetic */ Object m48x4f914009(Object obj) {
        Collection collection = (Collection) obj;
        if (collection.isEmpty()) {
            Type type = this.itemType;
            if (type instanceof Class) {
                return EnumSet.noneOf((Class) type);
            }
        }
        return EnumSet.copyOf(collection);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object readObject(JSONReader jSONReader, Type type, Object obj, long j) {
        Collection hashSet;
        Object object;
        JSONReader.Context context = jSONReader.getContext();
        if (this.itemObjectReader == null) {
            this.itemObjectReader = context.getObjectReader(this.itemType);
        }
        if (jSONReader.jsonb) {
            return readJSONBObject(jSONReader, type, obj, 0L);
        }
        if (jSONReader.readIfNull()) {
            return null;
        }
        if (jSONReader.nextIfSet()) {
            hashSet = new HashSet();
        } else {
            hashSet = (Collection) createInstance(context.getFeatures() | j);
        }
        char cCurrent = jSONReader.current();
        if (cCurrent == '\"') {
            String string = jSONReader.readString();
            if (this.itemClass == String.class) {
                jSONReader.nextIfComma();
                hashSet.add(string);
                return hashSet;
            }
            if (string.isEmpty()) {
                jSONReader.nextIfComma();
                return null;
            }
            ObjectReaderProvider provider = context.getProvider();
            if (this.itemClass.isEnum()) {
                ObjectReader objectReader = provider.getObjectReader(this.itemClass);
                if (objectReader instanceof ObjectReaderImplEnum) {
                    Enum r0 = ((ObjectReaderImplEnum) objectReader).getEnum(string);
                    if (r0 == null) {
                        if (JSONReader.Feature.ErrorOnEnumNotMatch.isEnabled(jSONReader.features(j))) {
                            throw new JSONException(jSONReader.info("enum not match : " + string));
                        }
                        return null;
                    }
                    hashSet.add(r0);
                    return hashSet;
                }
            }
            Function typeConvert = provider.getTypeConvert(String.class, this.itemType);
            if (typeConvert != null) {
                Object objApply = typeConvert.apply(string);
                jSONReader.nextIfComma();
                hashSet.add(objApply);
                return hashSet;
            }
            throw new JSONException(jSONReader.info());
        }
        int i = 0;
        if (cCurrent == '[') {
            jSONReader.next();
            ObjectReader objectReader2 = this.itemObjectReader;
            Type type2 = this.itemType;
            if (type != this.listType && (type instanceof ParameterizedType)) {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                if (actualTypeArguments.length == 1 && (type2 = actualTypeArguments[0]) != this.itemType) {
                    objectReader2 = jSONReader.getObjectReader(type2);
                }
            }
            ObjectReader objectReader3 = objectReader2;
            Type type3 = type2;
            while (!jSONReader.nextIfArrayEnd()) {
                if (type3 == String.class) {
                    object = jSONReader.readString();
                } else if (objectReader3 != null) {
                    if (jSONReader.isReference()) {
                        String reference = jSONReader.readReference();
                        if ("..".equals(reference)) {
                            object = this;
                        } else {
                            jSONReader.addResolveTask(hashSet, i, JSONPath.of(reference));
                            i++;
                        }
                    } else {
                        object = objectReader3.readObject(jSONReader, type3, Integer.valueOf(i), 0L);
                    }
                } else {
                    throw new JSONException(jSONReader.info("TODO : " + type3));
                }
                hashSet.add(object);
                i++;
            }
            jSONReader.nextIfComma();
            Function function = this.builder;
            if (function != null) {
                return function.apply(hashSet);
            }
        } else {
            Class cls = this.itemClass;
            if ((cls != Object.class && this.itemObjectReader != null) || (cls == Object.class && jSONReader.isObject())) {
                hashSet.add(this.itemObjectReader.readObject(jSONReader, this.itemType, 0, 0L));
                Function function2 = this.builder;
                if (function2 != null) {
                    return (Collection) function2.apply(hashSet);
                }
            } else {
                throw new JSONException(jSONReader.info());
            }
        }
        return hashSet;
    }
}
