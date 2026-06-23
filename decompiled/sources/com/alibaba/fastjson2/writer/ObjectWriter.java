package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ObjectWriter<T> {
    default long getFeatures() {
        return 0L;
    }

    default FieldWriter getFieldWriter(long j) {
        return null;
    }

    default void setNameFilter(NameFilter nameFilter) {
    }

    default void setPropertyFilter(PropertyFilter propertyFilter) {
    }

    default void setPropertyPreFilter(PropertyPreFilter propertyPreFilter) {
    }

    default void setValueFilter(ValueFilter valueFilter) {
    }

    void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j);

    default boolean writeTypeInfo(JSONWriter jSONWriter) {
        return false;
    }

    default List<FieldWriter> getFieldWriters() {
        return Collections.emptyList();
    }

    default Object getFieldValue(Object obj, String str) {
        FieldWriter fieldWriter = getFieldWriter(str);
        if (fieldWriter == null) {
            return null;
        }
        return fieldWriter.getFieldValue(obj);
    }

    default FieldWriter getFieldWriter(String str) {
        long jHashCode64 = Fnv.hashCode64(str);
        FieldWriter fieldWriter = getFieldWriter(jHashCode64);
        if (fieldWriter == null) {
            long jHashCode64LCase = Fnv.hashCode64LCase(str);
            if (jHashCode64LCase != jHashCode64) {
                return getFieldWriter(jHashCode64LCase);
            }
        }
        return fieldWriter;
    }

    default void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        write(jSONWriter, obj, obj2, type, j);
    }

    default void writeArrayMappingJSONB(JSONWriter jSONWriter, Object obj) {
        writeArrayMappingJSONB(jSONWriter, obj, null, null, 0L);
    }

    default void writeArrayMappingJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        List<FieldWriter> fieldWriters = getFieldWriters();
        int size = fieldWriters.size();
        jSONWriter.startArray(size);
        for (int i = 0; i < size; i++) {
            fieldWriters.get(i).writeValue(jSONWriter, obj);
        }
    }

    default void writeArrayMapping(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (jSONWriter.jsonb) {
            writeArrayMappingJSONB(jSONWriter, obj, obj2, type, j);
            return;
        }
        List<FieldWriter> fieldWriters = getFieldWriters();
        jSONWriter.startArray();
        int i = 0;
        if (!hasFilter(jSONWriter)) {
            int size = fieldWriters.size();
            while (i < size) {
                if (i != 0) {
                    jSONWriter.writeComma();
                }
                fieldWriters.get(i).writeValue(jSONWriter, obj);
                i++;
            }
        } else {
            JSONWriter.Context context = jSONWriter.context;
            PropertyPreFilter propertyPreFilter = context.getPropertyPreFilter();
            ValueFilter valueFilter = context.getValueFilter();
            PropertyFilter propertyFilter = context.getPropertyFilter();
            int size2 = fieldWriters.size();
            while (i < size2) {
                if (i != 0) {
                    jSONWriter.writeComma();
                }
                FieldWriter fieldWriter = fieldWriters.get(i);
                if (propertyPreFilter != null && !propertyPreFilter.process(jSONWriter, obj, fieldWriter.fieldName)) {
                    jSONWriter.writeNull();
                } else {
                    Object fieldValue = fieldWriter.getFieldValue(obj);
                    if (propertyFilter != null && !propertyFilter.apply(obj, fieldWriter.fieldName, fieldValue)) {
                        jSONWriter.writeNull();
                    } else if (valueFilter != null) {
                        Object objApply = valueFilter.apply(obj, fieldWriter.fieldName, fieldValue);
                        if (objApply == null) {
                            jSONWriter.writeNull();
                        } else {
                            fieldWriter.getObjectWriter(jSONWriter, objApply.getClass()).write(jSONWriter, fieldValue);
                        }
                    } else if (fieldValue == null) {
                        jSONWriter.writeNull();
                    } else {
                        fieldWriter.getObjectWriter(jSONWriter, fieldValue.getClass()).write(jSONWriter, fieldValue);
                    }
                }
                i++;
            }
        }
        jSONWriter.endArray();
    }

    default boolean hasFilter(JSONWriter jSONWriter) {
        return jSONWriter.hasFilter(JSONWriter.Feature.IgnoreNonFieldGetter.mask);
    }

    default void write(JSONWriter jSONWriter, Object obj) {
        write(jSONWriter, obj, null, null, 0L);
    }

    default String toJSONString(T t, JSONWriter.Feature... featureArr) {
        JSONWriter jSONWriterOf = JSONWriter.of(featureArr);
        try {
            write(jSONWriterOf, t, null, null, 0L);
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } catch (Throwable th) {
            if (jSONWriterOf == null) {
                throw th;
            }
            try {
                jSONWriterOf.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    default void writeWithFilter(JSONWriter jSONWriter, Object obj) {
        writeWithFilter(jSONWriter, obj, null, null, 0L);
    }

    default void writeWithFilter(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        throw new UnsupportedOperationException();
    }

    default void setFilter(Filter filter) {
        if (filter instanceof PropertyFilter) {
            setPropertyFilter((PropertyFilter) filter);
        }
        if (filter instanceof ValueFilter) {
            setValueFilter((ValueFilter) filter);
        }
        if (filter instanceof NameFilter) {
            setNameFilter((NameFilter) filter);
        }
        if (filter instanceof PropertyPreFilter) {
            setPropertyPreFilter((PropertyPreFilter) filter);
        }
    }
}
