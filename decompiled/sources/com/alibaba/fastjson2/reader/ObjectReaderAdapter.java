package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public class ObjectReaderAdapter<T> extends ObjectReaderBean<T> {
    final Constructor constructor;
    protected final FieldReader[] fieldReaders;
    final long[] hashCodes;
    final long[] hashCodesLCase;
    volatile boolean instantiationError;
    final short[] mapping;
    final short[] mappingLCase;
    final Class[] seeAlso;
    final Class seeAlsoDefault;
    final Map<Long, Class> seeAlsoMapping;
    final String[] seeAlsoNames;
    protected final String typeKey;
    protected final long typeKeyHashCode;

    public ObjectReaderAdapter(Class cls, Supplier<T> supplier, FieldReader... fieldReaderArr) {
        this(cls, null, null, 0L, null, supplier, null, fieldReaderArr);
    }

    public ObjectReaderAdapter(Class cls, String str, String str2, long j, JSONSchema jSONSchema, Supplier<T> supplier, Function function, FieldReader... fieldReaderArr) {
        this(cls, str, str2, j, jSONSchema, supplier, function, null, null, null, fieldReaderArr);
    }

    public ObjectReaderAdapter(Class cls, String str, String str2, long j, Supplier<T> supplier, Function function, FieldReader... fieldReaderArr) {
        this(cls, str, str2, j, null, supplier, function, fieldReaderArr);
    }

    public ObjectReaderAdapter(Class cls, String str, String str2, long j, JSONSchema jSONSchema, Supplier<T> supplier, Function function, Class[] clsArr, String[] strArr, FieldReader... fieldReaderArr) {
        this(cls, str, str2, j, jSONSchema, supplier, function, clsArr, strArr, null, fieldReaderArr);
    }

    public ObjectReaderAdapter(Class cls, String str, String str2, long j, JSONSchema jSONSchema, Supplier<T> supplier, Function function, Class[] clsArr, String[] strArr, Class cls2, FieldReader... fieldReaderArr) {
        super(cls, supplier, str2, j, jSONSchema, function);
        Constructor defaultConstructor = cls == null ? null : BeanUtils.getDefaultConstructor(cls, true);
        this.constructor = defaultConstructor;
        if (defaultConstructor != null) {
            defaultConstructor.setAccessible(true);
        }
        if (str == null || str.isEmpty()) {
            this.typeKey = "@type";
            this.typeKeyHashCode = HASH_TYPE;
        } else {
            this.typeKey = str;
            this.typeKeyHashCode = Fnv.hashCode64(str);
        }
        this.fieldReaders = fieldReaderArr;
        int length = fieldReaderArr.length;
        long[] jArr = new long[length];
        int length2 = fieldReaderArr.length;
        long[] jArr2 = new long[length2];
        for (int i = 0; i < fieldReaderArr.length; i++) {
            FieldReader fieldReader = fieldReaderArr[i];
            jArr[i] = fieldReader.fieldNameHash;
            jArr2[i] = fieldReader.fieldNameHashLCase;
            if (fieldReader.isUnwrapped() && (this.extraFieldReader == null || !(this.extraFieldReader instanceof FieldReaderAnySetter))) {
                this.extraFieldReader = fieldReader;
            }
            if (fieldReader.defaultValue != null) {
                this.hasDefaultValue = true;
            }
        }
        long[] jArrCopyOf = Arrays.copyOf(jArr, length);
        this.hashCodes = jArrCopyOf;
        Arrays.sort(jArrCopyOf);
        this.mapping = new short[jArrCopyOf.length];
        for (int i2 = 0; i2 < length; i2++) {
            this.mapping[Arrays.binarySearch(this.hashCodes, jArr[i2])] = (short) i2;
        }
        long[] jArrCopyOf2 = Arrays.copyOf(jArr2, length2);
        this.hashCodesLCase = jArrCopyOf2;
        Arrays.sort(jArrCopyOf2);
        this.mappingLCase = new short[jArrCopyOf2.length];
        for (int i3 = 0; i3 < length2; i3++) {
            this.mappingLCase[Arrays.binarySearch(this.hashCodesLCase, jArr2[i3])] = (short) i3;
        }
        this.seeAlso = clsArr;
        if (clsArr != null) {
            this.seeAlsoMapping = new HashMap(clsArr.length, 1.0f);
            this.seeAlsoNames = new String[clsArr.length];
            for (int i4 = 0; i4 < clsArr.length; i4++) {
                Class cls3 = clsArr[i4];
                String simpleName = (strArr == null || strArr.length < i4 + 1) ? null : strArr[i4];
                if (simpleName == null || simpleName.isEmpty()) {
                    simpleName = cls3.getSimpleName();
                }
                this.seeAlsoMapping.put(Long.valueOf(Fnv.hashCode64(simpleName)), cls3);
                this.seeAlsoNames[i4] = simpleName;
            }
        } else {
            this.seeAlsoMapping = null;
            this.seeAlsoNames = null;
        }
        this.seeAlsoDefault = cls2;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public final String getTypeKey() {
        return this.typeKey;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public final long getTypeKeyHash() {
        return this.typeKeyHashCode;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public final long getFeatures() {
        return this.features;
    }

    public FieldReader[] getFieldReaders() {
        FieldReader[] fieldReaderArr = this.fieldReaders;
        return (FieldReader[]) Arrays.copyOf(fieldReaderArr, fieldReaderArr.length);
    }

    public void apply(Consumer<FieldReader> consumer) {
        for (FieldReader fieldReader : this.fieldReaders) {
            try {
                consumer.accept(fieldReader);
            } catch (RuntimeException e) {
                if (!ignoreError(fieldReader)) {
                    throw e;
                }
            }
        }
    }

    public Object autoType(JSONReader jSONReader, Class cls, long j) {
        long typeHashCode = jSONReader.readTypeHashCode();
        JSONReader.Context context = jSONReader.getContext();
        ObjectReader objectReaderAutoType = jSONReader.isSupportAutoTypeOrHandler(j) ? context.getObjectReaderAutoType(typeHashCode) : null;
        if (objectReaderAutoType == null) {
            String string = jSONReader.getString();
            ObjectReader objectReaderAutoType2 = context.getObjectReaderAutoType(string, cls, this.features | j | context.getFeatures());
            if (objectReaderAutoType2 != null) {
                objectReaderAutoType = objectReaderAutoType2;
            } else {
                if (cls != this.objectClass) {
                    throw new JSONException(jSONReader.info("autoType not support : " + string));
                }
                objectReaderAutoType = this;
            }
        }
        return objectReaderAutoType.readObject(jSONReader, null, null, j);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public final Function getBuildFunction() {
        return this.buildFunction;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public T readArrayMappingObject(JSONReader jSONReader, Type type, Object obj, long j) {
        if (jSONReader.jsonb) {
            return readArrayMappingJSONBObject(jSONReader, type, obj, j);
        }
        if (!this.serializable) {
            jSONReader.errorOnNoneSerializable(this.objectClass);
        }
        jSONReader.nextIfArrayStart();
        T t = this.creator.get();
        int i = 0;
        while (true) {
            FieldReader[] fieldReaderArr = this.fieldReaders;
            if (i < fieldReaderArr.length) {
                FieldReader fieldReader = fieldReaderArr[i];
                try {
                    fieldReader.readFieldValue(jSONReader, t);
                } catch (RuntimeException e) {
                    if (!ignoreError(fieldReader)) {
                        throw e;
                    }
                }
                i++;
            } else {
                if (!jSONReader.nextIfArrayEnd()) {
                    throw new JSONException(jSONReader.info("array to bean end error"));
                }
                jSONReader.nextIfComma();
                return this.buildFunction != null ? (T) this.buildFunction.apply(t) : t;
            }
        }
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public T readArrayMappingJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        if (!this.serializable) {
            jSONReader.errorOnNoneSerializable(this.objectClass);
        }
        ObjectReader objectReaderCheckAutoType = checkAutoType(jSONReader, j);
        if (objectReaderCheckAutoType != null) {
            return (T) objectReaderCheckAutoType.readArrayMappingJSONBObject(jSONReader, type, obj, j);
        }
        T tCreateInstance = createInstance(0L);
        int iStartArray = jSONReader.startArray();
        if (iStartArray == this.fieldReaders.length) {
            int i = 0;
            while (true) {
                FieldReader[] fieldReaderArr = this.fieldReaders;
                if (i >= fieldReaderArr.length) {
                    break;
                }
                FieldReader fieldReader = fieldReaderArr[i];
                try {
                    fieldReader.readFieldValue(jSONReader, tCreateInstance);
                } catch (RuntimeException e) {
                    if (!ignoreError(fieldReader)) {
                        throw e;
                    }
                }
                i++;
            }
        } else {
            readArrayMappingJSONBObject0(jSONReader, tCreateInstance, iStartArray);
        }
        return this.buildFunction != null ? (T) this.buildFunction.apply(tCreateInstance) : tCreateInstance;
    }

    protected void readArrayMappingJSONBObject0(JSONReader jSONReader, Object obj, int i) {
        int i2 = 0;
        while (true) {
            FieldReader[] fieldReaderArr = this.fieldReaders;
            if (i2 >= fieldReaderArr.length) {
                for (int length = fieldReaderArr.length; length < i; length++) {
                    jSONReader.skipValue();
                }
                return;
            }
            if (i2 < i) {
                FieldReader fieldReader = fieldReaderArr[i2];
                try {
                    fieldReader.readFieldValue(jSONReader, obj);
                } catch (RuntimeException e) {
                    if (!ignoreError(fieldReader)) {
                        throw e;
                    }
                }
            }
            i2++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected Object createInstance0(long j) {
        Constructor constructor;
        if ((j & JSONReader.Feature.UseDefaultConstructorAsPossible.mask) != 0 && (constructor = this.constructor) != null && constructor.getParameterCount() == 0) {
            try {
                Object objNewInstance = this.constructor.newInstance(new Object[0]);
                if (this.hasDefaultValue) {
                    initDefaultValue(objNewInstance);
                }
                return objNewInstance;
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new JSONException("create instance error, " + this.objectClass, e);
            }
        }
        if (this.creator == null) {
            throw new JSONException("create instance error, " + this.objectClass);
        }
        return this.creator.get();
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderBean
    protected void initDefaultValue(T t) {
        int i = 0;
        while (true) {
            FieldReader[] fieldReaderArr = this.fieldReaders;
            if (i >= fieldReaderArr.length) {
                return;
            }
            FieldReader fieldReader = fieldReaderArr[i];
            Object obj = fieldReader.defaultValue;
            if (obj != null) {
                fieldReader.accept(t, obj);
            }
            i++;
        }
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public T createInstance(Collection collection, long j) {
        T tCreateInstance = createInstance(0L);
        int i = 0;
        for (Object obj : collection) {
            FieldReader[] fieldReaderArr = this.fieldReaders;
            if (i >= fieldReaderArr.length) {
                break;
            }
            fieldReaderArr[i].accept(tCreateInstance, obj);
            i++;
        }
        return tCreateInstance;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public T createInstance(long j) {
        Constructor constructor;
        if (this.instantiationError && (constructor = this.constructor) != null) {
            try {
                T t = (T) constructor.newInstance(new Object[0]);
                if (this.hasDefaultValue) {
                    initDefaultValue(t);
                }
                return t;
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new JSONException("create instance error, " + this.objectClass, e);
            }
        }
        try {
            T t2 = (T) createInstance0(j);
            if (this.hasDefaultValue) {
                initDefaultValue(t2);
            }
            return t2;
        } catch (Exception e2) {
            this.instantiationError = true;
            Constructor constructor2 = this.constructor;
            if (constructor2 != null) {
                try {
                    T t3 = (T) constructor2.newInstance(new Object[0]);
                    if (this.hasDefaultValue) {
                        initDefaultValue(t3);
                    }
                    return t3;
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e3) {
                    throw new JSONException("create instance error, " + this.objectClass, e3);
                }
            }
            throw new JSONException("create instance error, " + this.objectClass, e2);
        }
    }

    protected final boolean ignoreError(FieldReader fieldReader) {
        return (fieldReader.features & JSONReader.Feature.NullOnError.mask) != 0;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public FieldReader getFieldReader(long j) {
        int iBinarySearch = Arrays.binarySearch(this.hashCodes, j);
        if (iBinarySearch < 0) {
            return null;
        }
        return this.fieldReaders[this.mapping[iBinarySearch]];
    }

    public int getFieldOrdinal(long j) {
        int iBinarySearch = Arrays.binarySearch(this.hashCodes, j);
        if (iBinarySearch < 0) {
            return -1;
        }
        return this.mapping[iBinarySearch];
    }

    protected final FieldReader getFieldReaderUL(long j, JSONReader jSONReader, long j2) {
        FieldReader fieldReader = getFieldReader(j);
        return (fieldReader == null && jSONReader.isSupportSmartMatch(j2 | this.features)) ? getFieldReaderLCase(jSONReader.getNameHashCodeLCase()) : fieldReader;
    }

    protected final Map<Long, Object> readFieldValue(long j, JSONReader jSONReader, long j2, Map<Long, Object> map) {
        FieldReader fieldReader = getFieldReader(j);
        if (fieldReader == null && jSONReader.isSupportSmartMatch(j2 | this.features)) {
            fieldReader = getFieldReaderLCase(jSONReader.getNameHashCodeLCase());
        }
        if (fieldReader != null) {
            if (map == null) {
                map = new LinkedHashMap<>();
            }
            map.put(Long.valueOf(fieldReader.fieldNameHash), fieldReader.readFieldValue(jSONReader));
            return map;
        }
        jSONReader.skipValue();
        return map;
    }

    protected final void readFieldValue(long j, JSONReader jSONReader, long j2, Object obj) {
        FieldReader fieldReader = getFieldReader(j);
        if (fieldReader == null && jSONReader.isSupportSmartMatch(j2 | this.features)) {
            fieldReader = getFieldReaderLCase(jSONReader.getNameHashCodeLCase());
        }
        if (fieldReader != null) {
            if (jSONReader.jsonb) {
                fieldReader.readFieldValueJSONB(jSONReader, obj);
                return;
            } else {
                fieldReader.readFieldValue(jSONReader, obj);
                return;
            }
        }
        processExtra(jSONReader, obj);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public FieldReader getFieldReaderLCase(long j) {
        int iBinarySearch = Arrays.binarySearch(this.hashCodesLCase, j);
        if (iBinarySearch < 0) {
            return null;
        }
        return this.fieldReaders[this.mappingLCase[iBinarySearch]];
    }

    protected T autoType(JSONReader jSONReader) {
        ObjectReader objectReader;
        long typeHashCode = jSONReader.readTypeHashCode();
        JSONReader.Context context = jSONReader.getContext();
        ObjectReader objectReaderAutoType = autoType(context, typeHashCode);
        if (objectReaderAutoType == null) {
            String string = jSONReader.getString();
            ObjectReader objectReaderAutoType2 = context.getObjectReaderAutoType(string, null);
            if (objectReaderAutoType2 == null) {
                throw new JSONException(jSONReader.info("autoType not support : " + string));
            }
            objectReader = objectReaderAutoType2;
        } else {
            objectReader = objectReaderAutoType;
        }
        return (T) objectReader.readJSONBObject(jSONReader, null, null, this.features);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public T readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        if (jSONReader.nextIfNull()) {
            return null;
        }
        ObjectReader objectReaderCheckAutoType = jSONReader.checkAutoType(this.objectClass, this.typeNameHash, this.features | j);
        if (objectReaderCheckAutoType != null && objectReaderCheckAutoType.getObjectClass() != this.objectClass) {
            return (T) objectReaderCheckAutoType.readJSONBObject(jSONReader, type, obj, j);
        }
        if (!this.serializable) {
            jSONReader.errorOnNoneSerializable(this.objectClass);
        }
        if (jSONReader.isArray()) {
            if (jSONReader.isSupportBeanArray()) {
                return readArrayMappingJSONBObject(jSONReader, type, obj, j);
            }
            throw new JSONException(jSONReader.info("expect object, but " + JSONB.typeName(jSONReader.getType())));
        }
        jSONReader.nextIfObjectStart();
        int i = 0;
        T tCreateInstance = null;
        while (!jSONReader.nextIfObjectEnd()) {
            long fieldNameHashCode = jSONReader.readFieldNameHashCode();
            if (fieldNameHashCode == this.typeKeyHashCode && i == 0) {
                long valueHashCode = jSONReader.readValueHashCode();
                JSONReader.Context context = jSONReader.getContext();
                ObjectReader objectReaderAutoType = autoType(context, valueHashCode);
                if (objectReaderAutoType == null) {
                    String string = jSONReader.getString();
                    ObjectReader objectReaderAutoType2 = context.getObjectReaderAutoType(string, null);
                    if (objectReaderAutoType2 == null) {
                        throw new JSONException(jSONReader.info("autoType not support : " + string));
                    }
                    objectReaderAutoType = objectReaderAutoType2;
                }
                if (objectReaderAutoType != this) {
                    jSONReader.setTypeRedirect(true);
                    return (T) objectReaderAutoType.readJSONBObject(jSONReader, type, obj, j);
                }
            } else if (fieldNameHashCode != 0) {
                FieldReader fieldReader = getFieldReader(fieldNameHashCode);
                if (fieldReader == null && jSONReader.isSupportSmartMatch(this.features | j)) {
                    fieldReader = getFieldReaderLCase(jSONReader.getNameHashCodeLCase());
                }
                if (fieldReader == null) {
                    processExtra(jSONReader, tCreateInstance);
                } else {
                    if (tCreateInstance == null) {
                        tCreateInstance = createInstance(jSONReader.getContext().getFeatures() | j);
                    }
                    fieldReader.readFieldValue(jSONReader, tCreateInstance);
                }
            }
            i++;
        }
        if (tCreateInstance == null) {
            tCreateInstance = createInstance(jSONReader.getContext().getFeatures() | j);
        }
        if (this.schema != null) {
            this.schema.assertValidate(tCreateInstance);
        }
        return tCreateInstance;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public ObjectReader autoType(ObjectReaderProvider objectReaderProvider, long j) {
        Map<Long, Class> map = this.seeAlsoMapping;
        if (map != null && map.size() > 0) {
            Class cls = this.seeAlsoMapping.get(Long.valueOf(j));
            if (cls == null) {
                return null;
            }
            return objectReaderProvider.getObjectReader(cls);
        }
        return objectReaderProvider.getObjectReader(j);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public ObjectReader autoType(JSONReader.Context context, long j) {
        Map<Long, Class> map = this.seeAlsoMapping;
        if (map != null && map.size() > 0) {
            Class cls = this.seeAlsoMapping.get(Long.valueOf(j));
            if (cls == null) {
                return null;
            }
            return context.getObjectReader(cls);
        }
        return context.getObjectReaderAutoType(j);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderBean
    protected void initStringFieldAsEmpty(Object obj) {
        int i = 0;
        while (true) {
            FieldReader[] fieldReaderArr = this.fieldReaders;
            if (i >= fieldReaderArr.length) {
                return;
            }
            FieldReader fieldReader = fieldReaderArr[i];
            if (fieldReader.fieldClass == String.class) {
                fieldReader.accept(obj, "");
            }
            i++;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:80:0x009c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.alibaba.fastjson2.reader.ObjectReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public T createInstance(java.util.Map r12, long r13) {
        /*
            Method dump skipped, instruction units count: 313
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderAdapter.createInstance(java.util.Map, long):java.lang.Object");
    }
}
