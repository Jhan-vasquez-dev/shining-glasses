package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.GuavaSupport;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public final class ObjectReaderImplListStr implements ObjectReader {
    final Class instanceType;
    final Class listType;

    public ObjectReaderImplListStr(Class cls, Class cls2) {
        this.listType = cls;
        this.instanceType = cls2;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(long j) {
        Class cls = this.instanceType;
        if (cls == ArrayList.class) {
            return new ArrayList();
        }
        if (cls == LinkedList.class) {
            return new LinkedList();
        }
        try {
            return cls.newInstance();
        } catch (IllegalAccessException | InstantiationException unused) {
            throw new JSONException("create list error, type " + this.instanceType);
        }
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(Collection collection, long j) {
        if (this.listType.isInstance(collection)) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                if (!(it.next() instanceof String)) {
                }
            }
            return collection;
        }
        Collection collection2 = (Collection) createInstance(0L);
        for (Object obj : collection) {
            if (obj == null || (obj instanceof String)) {
                collection2.add(obj);
            } else {
                collection2.add(JSON.toJSONString(obj));
            }
        }
        return collection2;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Class getObjectClass() {
        return this.listType;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        Collection kotlinEmptyList;
        Function functionImmutableListConverter;
        ArrayList arrayList;
        JSONArray jSONArray;
        ArrayList arrayList2;
        Class objectClass = this.instanceType;
        Function function = null;
        if (jSONReader.nextIfNull()) {
            return null;
        }
        ObjectReader objectReaderCheckAutoType = jSONReader.checkAutoType(this.listType, 0L, j);
        if (objectReaderCheckAutoType != null) {
            objectClass = objectReaderCheckAutoType.getObjectClass();
        }
        int i = 0;
        if (objectClass == ObjectReaderImplList.CLASS_ARRAYS_LIST) {
            int iStartArray = jSONReader.startArray();
            String[] strArr = new String[iStartArray];
            while (i < iStartArray) {
                strArr[i] = jSONReader.readString();
                i++;
            }
            return Arrays.asList(strArr);
        }
        int iStartArray2 = jSONReader.startArray();
        if (objectClass != ArrayList.class) {
            if (objectClass != JSONArray.class) {
                if (objectClass == ObjectReaderImplList.CLASS_UNMODIFIABLE_COLLECTION) {
                    ArrayList arrayList3 = new ArrayList();
                    function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplListStr$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            return Collections.unmodifiableCollection((Collection) obj2);
                        }
                    };
                    kotlinEmptyList = arrayList3;
                } else if (objectClass == ObjectReaderImplList.CLASS_UNMODIFIABLE_LIST) {
                    ArrayList arrayList4 = new ArrayList();
                    function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplListStr$$ExternalSyntheticLambda1
                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            return Collections.unmodifiableList((List) obj2);
                        }
                    };
                    kotlinEmptyList = arrayList4;
                } else if (objectClass == ObjectReaderImplList.CLASS_UNMODIFIABLE_SET) {
                    LinkedHashSet linkedHashSet = new LinkedHashSet();
                    function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplListStr$$ExternalSyntheticLambda2
                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            return Collections.unmodifiableSet((Set) obj2);
                        }
                    };
                    kotlinEmptyList = linkedHashSet;
                } else if (objectClass == ObjectReaderImplList.CLASS_UNMODIFIABLE_SORTED_SET) {
                    TreeSet treeSet = new TreeSet();
                    function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplListStr$$ExternalSyntheticLambda3
                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            return Collections.unmodifiableSortedSet((SortedSet) obj2);
                        }
                    };
                    kotlinEmptyList = treeSet;
                } else if (objectClass == ObjectReaderImplList.CLASS_UNMODIFIABLE_NAVIGABLE_SET) {
                    TreeSet treeSet2 = new TreeSet();
                    function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplListStr$$ExternalSyntheticLambda4
                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            return Collections.unmodifiableNavigableSet((NavigableSet) obj2);
                        }
                    };
                    kotlinEmptyList = treeSet2;
                } else if (objectClass == ObjectReaderImplList.CLASS_SINGLETON) {
                    ArrayList arrayList5 = new ArrayList();
                    function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplListStr$$ExternalSyntheticLambda5
                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            return Collections.singleton(((Collection) obj2).iterator().next());
                        }
                    };
                    kotlinEmptyList = arrayList5;
                } else if (objectClass == ObjectReaderImplList.CLASS_SINGLETON_LIST) {
                    ArrayList arrayList6 = new ArrayList();
                    function = new Function() { // from class: com.alibaba.fastjson2.reader.ObjectReaderImplListStr$$ExternalSyntheticLambda6
                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            return Collections.singletonList(((Collection) obj2).iterator().next());
                        }
                    };
                    kotlinEmptyList = arrayList6;
                } else if (objectClass != null && objectClass != this.listType) {
                    String typeName = objectClass.getTypeName();
                    typeName.hashCode();
                    switch (typeName) {
                        case "com.google.common.collect.ImmutableList":
                            ArrayList arrayList7 = new ArrayList();
                            functionImmutableListConverter = GuavaSupport.immutableListConverter();
                            arrayList = arrayList7;
                            function = functionImmutableListConverter;
                            kotlinEmptyList = arrayList;
                            break;
                        case "kotlin.collections.EmptyList":
                            kotlinEmptyList = ObjectReaderImplList.getKotlinEmptyList(objectClass);
                            break;
                        case "kotlin.collections.EmptySet":
                            kotlinEmptyList = ObjectReaderImplList.getKotlinEmptySet(objectClass);
                            break;
                        case "com.google.common.collect.ImmutableSet":
                            ArrayList arrayList8 = new ArrayList();
                            functionImmutableListConverter = GuavaSupport.immutableSetConverter();
                            arrayList = arrayList8;
                            function = functionImmutableListConverter;
                            kotlinEmptyList = arrayList;
                            break;
                        case "com.google.common.collect.Lists$TransformingRandomAccessList":
                            kotlinEmptyList = new ArrayList();
                            break;
                        case "com.google.common.collect.Lists.TransformingSequentialList":
                            kotlinEmptyList = new LinkedList();
                            break;
                        default:
                            try {
                                kotlinEmptyList = (Collection) objectClass.newInstance();
                                break;
                            } catch (IllegalAccessException | InstantiationException e) {
                                throw new JSONException(jSONReader.info("create instance error " + objectClass), e);
                            }
                            break;
                    }
                } else {
                    kotlinEmptyList = (Collection) createInstance(jSONReader.getContext().getFeatures() | j);
                }
            } else if (iStartArray2 > 0) {
                kotlinEmptyList = jSONArray;
                jSONArray = new JSONArray(iStartArray2);
            } else {
                kotlinEmptyList = jSONArray;
                jSONArray = new JSONArray();
            }
        } else if (iStartArray2 > 0) {
            kotlinEmptyList = arrayList2;
            arrayList2 = new ArrayList(iStartArray2);
        } else {
            kotlinEmptyList = arrayList2;
            arrayList2 = new ArrayList();
        }
        while (i < iStartArray2) {
            kotlinEmptyList.add(jSONReader.readString());
            i++;
        }
        return function != null ? (Collection) function.apply(kotlinEmptyList) : kotlinEmptyList;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object readObject(JSONReader jSONReader, Type type, Object obj, long j) {
        Collection hashSet;
        if (jSONReader.jsonb) {
            return readJSONBObject(jSONReader, type, obj, 0L);
        }
        if (jSONReader.readIfNull()) {
            return null;
        }
        if (jSONReader.nextIfSet()) {
            hashSet = new HashSet();
        } else {
            hashSet = (Collection) createInstance(jSONReader.getContext().getFeatures() | j);
        }
        char cCurrent = jSONReader.current();
        if (cCurrent == '[') {
            jSONReader.next();
            while (!jSONReader.nextIfArrayEnd()) {
                String string = jSONReader.readString();
                if (string != null || !(hashSet instanceof SortedSet)) {
                    hashSet.add(string);
                }
            }
        } else if (cCurrent == '\"' || cCurrent == '\'' || cCurrent == '{') {
            String string2 = jSONReader.readString();
            if (string2 != null && !string2.isEmpty()) {
                hashSet.add(string2);
            }
        } else {
            throw new JSONException(jSONReader.info());
        }
        jSONReader.nextIfComma();
        return hashSet;
    }
}
