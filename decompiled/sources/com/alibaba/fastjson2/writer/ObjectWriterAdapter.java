package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.SymbolTable;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ObjectWriterAdapter<T> implements ObjectWriter<T> {
    static final String TYPE = "@type";
    final boolean containsNoneFieldGetter;
    final long features;
    protected final FieldWriter[] fieldWriterArray;
    final List<FieldWriter> fieldWriters;
    final boolean googleCollection;
    boolean hasFilter;
    final boolean hasValueField;
    final long[] hashCodes;
    byte[] jsonbClassInfo;
    final short[] mapping;
    NameFilter nameFilter;
    char[] nameWithColonUTF16;
    byte[] nameWithColonUTF8;
    final Class objectClass;
    PropertyFilter propertyFilter;
    PropertyPreFilter propertyPreFilter;
    final boolean serializable;
    final String typeKey;
    byte[] typeKeyJSONB;
    protected final String typeName;
    protected final long typeNameHash;
    protected final byte[] typeNameJSONB;
    protected long typeNameSymbolCache;
    ValueFilter valueFilter;

    public ObjectWriterAdapter(Class<T> cls, List<FieldWriter> list) {
        this(cls, null, null, 0L, list);
    }

    public ObjectWriterAdapter(Class<T> cls, String str, String str2, long j, List<FieldWriter> list) {
        if (str2 == null && cls != null) {
            if (Enum.class.isAssignableFrom(cls) && !cls.isEnum()) {
                str2 = cls.getSuperclass().getName();
            } else {
                str2 = TypeUtils.getTypeName(cls);
            }
        }
        this.objectClass = cls;
        this.typeKey = (str == null || str.isEmpty()) ? TYPE : str;
        this.typeName = str2;
        this.typeNameHash = str2 != null ? Fnv.hashCode64(str2) : 0L;
        this.typeNameJSONB = JSONB.toBytes(str2);
        this.features = j;
        this.fieldWriters = list;
        this.serializable = cls == null || Serializable.class.isAssignableFrom(cls);
        this.googleCollection = "com.google.common.collect.AbstractMapBasedMultimap$RandomAccessWrappedList".equals(str2) || "com.google.common.collect.AbstractMapBasedMultimap$WrappedSet".equals(str2);
        FieldWriter[] fieldWriterArr = new FieldWriter[list.size()];
        this.fieldWriterArray = fieldWriterArr;
        list.toArray(fieldWriterArr);
        this.hasValueField = fieldWriterArr.length == 1 && (fieldWriterArr[0].features & FieldInfo.VALUE_MASK) != 0;
        int length = fieldWriterArr.length;
        long[] jArr = new long[length];
        int i = 0;
        boolean z = false;
        while (true) {
            FieldWriter[] fieldWriterArr2 = this.fieldWriterArray;
            if (i >= fieldWriterArr2.length) {
                break;
            }
            FieldWriter fieldWriter = fieldWriterArr2[i];
            jArr[i] = Fnv.hashCode64(fieldWriter.fieldName);
            if (fieldWriter.method != null && (fieldWriter.features & FieldInfo.FIELD_MASK) == 0) {
                z = true;
            }
            i++;
        }
        this.containsNoneFieldGetter = z;
        long[] jArrCopyOf = Arrays.copyOf(jArr, length);
        this.hashCodes = jArrCopyOf;
        Arrays.sort(jArrCopyOf);
        this.mapping = new short[jArrCopyOf.length];
        for (int i2 = 0; i2 < length; i2++) {
            this.mapping[Arrays.binarySearch(this.hashCodes, jArr[i2])] = (short) i2;
        }
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter, com.alibaba.fastjson2.reader.ObjectReader
    public long getFeatures() {
        return this.features;
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public FieldWriter getFieldWriter(long j) {
        int iBinarySearch = Arrays.binarySearch(this.hashCodes, j);
        if (iBinarySearch < 0) {
            return null;
        }
        return this.fieldWriterArray[this.mapping[iBinarySearch]];
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public final boolean hasFilter(JSONWriter jSONWriter) {
        return jSONWriter.hasFilter(this.containsNoneFieldGetter) | this.hasFilter;
    }

    protected final boolean hasFilter0(JSONWriter jSONWriter) {
        return jSONWriter.hasFilter() | this.hasFilter;
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void setPropertyFilter(PropertyFilter propertyFilter) {
        this.propertyFilter = propertyFilter;
        if (propertyFilter != null) {
            this.hasFilter = true;
        }
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void setValueFilter(ValueFilter valueFilter) {
        this.valueFilter = valueFilter;
        if (valueFilter != null) {
            this.hasFilter = true;
        }
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void setNameFilter(NameFilter nameFilter) {
        this.nameFilter = nameFilter;
        if (nameFilter != null) {
            this.hasFilter = true;
        }
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void setPropertyPreFilter(PropertyPreFilter propertyPreFilter) {
        this.propertyPreFilter = propertyPreFilter;
        if (propertyPreFilter != null) {
            this.hasFilter = true;
        }
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeArrayMappingJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (jSONWriter.isWriteTypeInfo(obj, type, j)) {
            writeClassInfo(jSONWriter);
        }
        int size = this.fieldWriters.size();
        jSONWriter.startArray(size);
        for (int i = 0; i < size; i++) {
            this.fieldWriters.get(i).writeValue(jSONWriter, obj);
        }
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        long features = this.features | j | jSONWriter.getFeatures();
        if (!this.serializable) {
            if ((JSONWriter.Feature.ErrorOnNoneSerializable.mask & features) != 0) {
                errorOnNoneSerializable();
                return;
            } else if ((JSONWriter.Feature.IgnoreNoneSerializable.mask & features) != 0) {
                jSONWriter.writeNull();
                return;
            }
        }
        if ((features & JSONWriter.Feature.IgnoreNoneSerializable.mask) != 0) {
            writeWithFilter(jSONWriter, obj, obj2, type, j);
            return;
        }
        int length = this.fieldWriterArray.length;
        if (jSONWriter.isWriteTypeInfo(obj, type, j)) {
            writeClassInfo(jSONWriter);
        }
        jSONWriter.startObject();
        for (int i = 0; i < length; i++) {
            this.fieldWriters.get(i).write(jSONWriter, obj);
        }
        jSONWriter.endObject();
    }

    protected final void writeClassInfo(JSONWriter jSONWriter) {
        SymbolTable symbolTable = jSONWriter.symbolTable;
        if (symbolTable == null || !writeClassInfoSymbol(jSONWriter, symbolTable)) {
            jSONWriter.writeTypeName(this.typeNameJSONB, this.typeNameHash);
        }
    }

    private boolean writeClassInfoSymbol(JSONWriter jSONWriter, SymbolTable symbolTable) {
        int ordinalByHashCode;
        int iIdentityHashCode = System.identityHashCode(symbolTable);
        long j = this.typeNameSymbolCache;
        if (j == 0) {
            ordinalByHashCode = symbolTable.getOrdinalByHashCode(this.typeNameHash);
            if (ordinalByHashCode != -1) {
                this.typeNameSymbolCache = (((long) ordinalByHashCode) << 32) | ((long) iIdentityHashCode);
            }
        } else if (((int) j) == iIdentityHashCode) {
            ordinalByHashCode = (int) (j >> 32);
        } else {
            ordinalByHashCode = symbolTable.getOrdinalByHashCode(this.typeNameHash);
            if (ordinalByHashCode != -1) {
                this.typeNameSymbolCache = (((long) ordinalByHashCode) << 32) | ((long) iIdentityHashCode);
            }
        }
        if (ordinalByHashCode == -1) {
            return false;
        }
        jSONWriter.writeRaw(JSONB.Constants.BC_TYPED_ANY);
        jSONWriter.writeInt32(-ordinalByHashCode);
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (this.hasValueField) {
            this.fieldWriterArray[0].writeValue(jSONWriter, obj);
            return;
        }
        long features = j | this.features | jSONWriter.getFeatures();
        boolean z = (JSONWriter.Feature.BeanToArray.mask & features) != 0;
        if (jSONWriter.jsonb) {
            if (z) {
                writeArrayMappingJSONB(jSONWriter, obj, obj2, type, j);
                return;
            } else {
                writeJSONB(jSONWriter, obj, obj2, type, j);
                return;
            }
        }
        if (this.googleCollection) {
            ObjectWriterImplCollection.INSTANCE.write(jSONWriter, (Collection) obj, obj2, type, j);
            return;
        }
        if (z) {
            writeArrayMapping(jSONWriter, obj, obj2, type, j);
            return;
        }
        if (!this.serializable) {
            if ((JSONWriter.Feature.ErrorOnNoneSerializable.mask & features) != 0) {
                errorOnNoneSerializable();
                return;
            } else if ((features & JSONWriter.Feature.IgnoreNoneSerializable.mask) != 0) {
                jSONWriter.writeNull();
                return;
            }
        }
        if (hasFilter(jSONWriter)) {
            writeWithFilter(jSONWriter, obj, obj2, type, j);
            return;
        }
        jSONWriter.startObject();
        if (((this.features | j) & JSONWriter.Feature.WriteClassName.mask) != 0 || jSONWriter.isWriteTypeInfo(obj, j)) {
            writeTypeInfo(jSONWriter);
        }
        int size = this.fieldWriters.size();
        for (int i = 0; i < size; i++) {
            this.fieldWriters.get(i).write(jSONWriter, obj);
        }
        jSONWriter.endObject();
    }

    public Map<String, Object> toMap(Object obj) {
        int size = this.fieldWriters.size();
        JSONObject jSONObject = new JSONObject(size, 1.0f);
        for (int i = 0; i < size; i++) {
            FieldWriter fieldWriter = this.fieldWriters.get(i);
            jSONObject.put(fieldWriter.fieldName, fieldWriter.getFieldValue(obj));
        }
        return jSONObject;
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public List<FieldWriter> getFieldWriters() {
        return this.fieldWriters;
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public boolean writeTypeInfo(JSONWriter jSONWriter) {
        if (jSONWriter.utf8) {
            if (this.nameWithColonUTF8 == null) {
                int length = this.typeKey.length();
                int length2 = this.typeName.length();
                int i = length + length2;
                byte[] bArr = new byte[i + 5];
                bArr[0] = 34;
                this.typeKey.getBytes(0, length, bArr, 1);
                bArr[length + 1] = 34;
                bArr[length + 2] = 58;
                bArr[length + 3] = 34;
                this.typeName.getBytes(0, length2, bArr, length + 4);
                bArr[i + 4] = 34;
                this.nameWithColonUTF8 = bArr;
            }
            jSONWriter.writeNameRaw(this.nameWithColonUTF8);
            return true;
        }
        if (jSONWriter.utf16) {
            if (this.nameWithColonUTF16 == null) {
                int length3 = this.typeKey.length();
                int length4 = this.typeName.length();
                int i2 = length3 + length4;
                char[] cArr = new char[i2 + 5];
                cArr[0] = '\"';
                this.typeKey.getChars(0, length3, cArr, 1);
                cArr[length3 + 1] = '\"';
                cArr[length3 + 2] = ':';
                cArr[length3 + 3] = '\"';
                this.typeName.getChars(0, length4, cArr, length3 + 4);
                cArr[i2 + 4] = '\"';
                this.nameWithColonUTF16 = cArr;
            }
            jSONWriter.writeNameRaw(this.nameWithColonUTF16);
            return true;
        }
        if (jSONWriter.jsonb) {
            if (this.typeKeyJSONB == null) {
                this.typeKeyJSONB = JSONB.toBytes(this.typeKey);
            }
            jSONWriter.writeRaw(this.typeKeyJSONB);
            jSONWriter.writeRaw(this.typeNameJSONB);
            return true;
        }
        jSONWriter.writeString(this.typeKey);
        jSONWriter.writeColon();
        jSONWriter.writeString(this.typeName);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x00cd  */
    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void writeWithFilter(com.alibaba.fastjson2.JSONWriter r47, java.lang.Object r48, java.lang.Object r49, java.lang.reflect.Type r50, long r51) {
        /*
            Method dump skipped, instruction units count: 623
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterAdapter.writeWithFilter(com.alibaba.fastjson2.JSONWriter, java.lang.Object, java.lang.Object, java.lang.reflect.Type, long):void");
    }

    public JSONObject toJSONObject(T t) {
        return toJSONObject(t, 0L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JSONObject toJSONObject(T t, long j) {
        JSONObject jSONObject = new JSONObject();
        int size = this.fieldWriters.size();
        for (int i = 0; i < size; i++) {
            FieldWriter fieldWriter = this.fieldWriters.get(i);
            Object fieldValue = fieldWriter.getFieldValue(t);
            String str = fieldWriter.format;
            Class cls = fieldWriter.fieldClass;
            Object objValueOf = fieldValue;
            if (str != null) {
                if (cls == Date.class) {
                    if ("millis".equals(str)) {
                        objValueOf = Long.valueOf(((Date) fieldValue).getTime());
                    } else {
                        objValueOf = DateUtils.format((Date) fieldValue, str);
                    }
                } else if (cls == LocalDate.class) {
                    objValueOf = DateUtils.format((LocalDate) fieldValue, str);
                } else {
                    objValueOf = fieldValue;
                    if (cls == LocalDateTime.class) {
                        objValueOf = DateUtils.format((LocalDateTime) fieldValue, str);
                    }
                }
            }
            Object obj = objValueOf;
            if ((fieldWriter.features & FieldInfo.UNWRAPPED_MASK) == 0) {
                if (objValueOf != null) {
                    String name = objValueOf.getClass().getName();
                    obj = objValueOf;
                    if (Collection.class.isAssignableFrom(cls)) {
                        Class<?> cls2 = objValueOf.getClass();
                        obj = objValueOf;
                        if (cls2 != JSONObject.class) {
                            obj = objValueOf;
                            if (!name.equals("com.alibaba.fastjson.JSONObject")) {
                                Collection collection = (Collection) objValueOf;
                                JSONArray jSONArray = new JSONArray(collection.size());
                                Iterator it = collection.iterator();
                                while (it.hasNext()) {
                                    Object next = it.next();
                                    jSONArray.add(next == t ? jSONObject : JSON.toJSON(next));
                                }
                                obj = jSONArray;
                            }
                        }
                    }
                }
                if (obj != null || ((this.features | j) & JSONWriter.Feature.WriteNulls.mask) != 0) {
                    if (obj == t) {
                        obj = jSONObject;
                    }
                    boolean z = obj instanceof Enum;
                    Object objName = obj;
                    if (z) {
                        objName = obj;
                        if ((JSONWriter.Feature.WriteEnumsUsingName.mask & j) != 0) {
                            objName = ((Enum) obj).name();
                        }
                    }
                    Object json = objName;
                    if (fieldWriter instanceof FieldWriterObject) {
                        boolean z2 = objName instanceof Map;
                        json = objName;
                        if (!z2) {
                            ObjectWriter initWriter = fieldWriter.getInitWriter();
                            if (initWriter == null) {
                                initWriter = JSONFactory.getObjectWriter(fieldWriter.fieldType, this.features | j);
                            }
                            json = objName;
                            if (initWriter instanceof ObjectWriterAdapter) {
                                ObjectWriterAdapter objectWriterAdapter = (ObjectWriterAdapter) initWriter;
                                if (!objectWriterAdapter.getFieldWriters().isEmpty()) {
                                    json = objectWriterAdapter.toJSONObject(objName);
                                } else {
                                    json = JSON.toJSON(objName);
                                }
                            }
                        }
                    }
                    jSONObject.put(fieldWriter.fieldName, json);
                }
            } else if (objValueOf instanceof Map) {
                jSONObject.putAll((Map) objValueOf);
            } else {
                ObjectWriter initWriter2 = fieldWriter.getInitWriter();
                if (initWriter2 == null) {
                    initWriter2 = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(cls);
                }
                List<FieldWriter> fieldWriters = initWriter2.getFieldWriters();
                int size2 = fieldWriters.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    FieldWriter fieldWriter2 = fieldWriters.get(i2);
                    jSONObject.put(fieldWriter2.fieldName, fieldWriter2.getFieldValue(objValueOf));
                }
            }
        }
        return jSONObject;
    }

    public String toString() {
        return this.objectClass.getName();
    }

    protected void errorOnNoneSerializable() {
        throw new JSONException("not support none serializable class " + this.objectClass.getName());
    }
}
