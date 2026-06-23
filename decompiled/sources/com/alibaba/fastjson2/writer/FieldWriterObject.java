package com.alibaba.fastjson2.writer;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.writer.ObjectWriterBaseModule;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* JADX INFO: loaded from: classes.dex */
public class FieldWriterObject<T> extends FieldWriter<T> {
    static final AtomicReferenceFieldUpdater<FieldWriterObject, Class> initValueClassUpdater = AtomicReferenceFieldUpdater.newUpdater(FieldWriterObject.class, Class.class, "initValueClass");
    final boolean array;
    volatile Class initValueClass;
    final boolean number;
    final boolean unwrapped;
    protected boolean writeUsing;

    protected FieldWriterObject(String str, int i, long j, String str2, Locale locale, String str3, Type type, Class cls, Field field, Method method) {
        super(str, i, j, str2, locale, str3, type, cls, field, method);
        long j2 = j & FieldInfo.UNWRAPPED_MASK;
        boolean z = true;
        this.unwrapped = j2 != 0;
        if (cls == Currency.class) {
            this.initValueClass = cls;
            this.initObjectWriter = ObjectWriterImplCurrency.INSTANCE_FOR_FIELD;
        }
        if (!cls.isArray() && !Collection.class.isAssignableFrom(cls) && cls != AtomicLongArray.class && cls != AtomicIntegerArray.class) {
            z = false;
        }
        this.array = z;
        this.number = Number.class.isAssignableFrom(cls);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public ObjectWriter getInitWriter() {
        return this.initObjectWriter;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean unwrapped() {
        return this.unwrapped;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public ObjectWriter getObjectWriter(JSONWriter jSONWriter, Class cls) {
        Class cls2 = this.initValueClass;
        if (cls2 == null || this.initObjectWriter == ObjectWriterBaseModule.VoidObjectWriter.INSTANCE) {
            return getObjectWriterVoid(jSONWriter, cls);
        }
        boolean zTypeMatch = cls2 == cls || (this.writeUsing && cls2.isAssignableFrom(cls)) || ((cls2 == Map.class && cls2.isAssignableFrom(cls)) || (cls2 == List.class && cls2.isAssignableFrom(cls)));
        if (!zTypeMatch && cls2.isPrimitive()) {
            zTypeMatch = typeMatch(cls2, cls);
        }
        if (zTypeMatch) {
            if (this.initObjectWriter == null) {
                return getObjectWriterTypeMatch(jSONWriter, cls);
            }
            return this.initObjectWriter;
        }
        return getObjectWriterTypeNotMatch(jSONWriter, cls);
    }

    protected final ObjectWriter getObjectWriterVoid(JSONWriter jSONWriter, Class cls) {
        ObjectWriter objectWriterImplDoubleValueArray;
        if (BeanUtils.isExtendedMap(cls) && BeanUtils.SUPER.equals(this.fieldName)) {
            JSONWriter.Context context = jSONWriter.context;
            ObjectWriter objectWriter = context.provider.getObjectWriter(this.fieldType, this.fieldClass, ((this.features | context.getFeatures()) & JSONWriter.Feature.FieldBased.mask) != 0);
            if (this.initObjectWriter == null && AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(initValueClassUpdater, this, null, cls)) {
                AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(initObjectWriterUpdater, this, null, objectWriter);
            }
            return objectWriter;
        }
        if (this.format == null) {
            JSONWriter.Context context2 = jSONWriter.context;
            objectWriterImplDoubleValueArray = context2.provider.getObjectWriterFromCache(cls, cls, ((this.features | context2.getFeatures()) & JSONWriter.Feature.FieldBased.mask) != 0);
        } else {
            objectWriterImplDoubleValueArray = null;
        }
        DecimalFormat decimalFormat = this.decimalFormat;
        if (cls == Float[].class) {
            if (decimalFormat != null) {
                objectWriterImplDoubleValueArray = new ObjectWriterArrayFinal(Float.class, decimalFormat);
            } else {
                objectWriterImplDoubleValueArray = ObjectWriterArrayFinal.FLOAT_ARRAY;
            }
        } else if (cls == Double[].class) {
            if (decimalFormat != null) {
                objectWriterImplDoubleValueArray = new ObjectWriterArrayFinal(Double.class, decimalFormat);
            } else {
                objectWriterImplDoubleValueArray = ObjectWriterArrayFinal.DOUBLE_ARRAY;
            }
        } else if (cls == float[].class) {
            if (decimalFormat != null) {
                objectWriterImplDoubleValueArray = new ObjectWriterImplFloatValueArray(decimalFormat);
            } else {
                objectWriterImplDoubleValueArray = ObjectWriterImplFloatValueArray.INSTANCE;
            }
        } else if (cls == double[].class) {
            if (decimalFormat != null) {
                objectWriterImplDoubleValueArray = new ObjectWriterImplDoubleValueArray(decimalFormat);
            } else {
                objectWriterImplDoubleValueArray = ObjectWriterImplDoubleValueArray.INSTANCE;
            }
        }
        if (objectWriterImplDoubleValueArray == null) {
            objectWriterImplDoubleValueArray = FieldWriter.getObjectWriter(this.fieldType, this.fieldClass, this.format, this.locale, cls);
        }
        if (objectWriterImplDoubleValueArray == null) {
            boolean zM = AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(initValueClassUpdater, this, null, cls);
            ObjectWriter objectWriter2 = jSONWriter.getObjectWriter(cls);
            if (zM) {
                AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(initObjectWriterUpdater, this, null, objectWriter2);
            }
            return objectWriter2;
        }
        if (this.initObjectWriter == null && AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(initValueClassUpdater, this, null, cls)) {
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(initObjectWriterUpdater, this, null, objectWriterImplDoubleValueArray);
        }
        return objectWriterImplDoubleValueArray;
    }

    static boolean typeMatch(Class cls, Class cls2) {
        if (cls == Integer.TYPE && cls2 == Integer.class) {
            return true;
        }
        if (cls == Long.TYPE && cls2 == Long.class) {
            return true;
        }
        if (cls == Boolean.TYPE && cls2 == Boolean.class) {
            return true;
        }
        if (cls == Short.TYPE && cls2 == Short.class) {
            return true;
        }
        if (cls == Byte.TYPE && cls2 == Byte.class) {
            return true;
        }
        if (cls == Float.TYPE && cls2 == Float.class) {
            return true;
        }
        if (cls == Double.TYPE && cls2 == Double.class) {
            return true;
        }
        return cls == Character.TYPE && cls2 == Character.class;
    }

    private ObjectWriter getObjectWriterTypeNotMatch(JSONWriter jSONWriter, Class cls) {
        if (Map.class.isAssignableFrom(cls)) {
            if (this.fieldClass.isAssignableFrom(cls)) {
                return ObjectWriterImplMap.of(this.fieldType, cls);
            }
            return ObjectWriterImplMap.of(cls);
        }
        ObjectWriter objectWriter = this.format != null ? FieldWriter.getObjectWriter(this.fieldType, this.fieldClass, this.format, null, cls) : null;
        return objectWriter == null ? jSONWriter.getObjectWriter(cls) : objectWriter;
    }

    private ObjectWriter getObjectWriterTypeMatch(JSONWriter jSONWriter, Class cls) {
        ObjectWriter objectWriter;
        if (Map.class.isAssignableFrom(cls)) {
            if (this.fieldClass.isAssignableFrom(cls)) {
                objectWriter = ObjectWriterImplMap.of(this.fieldType, cls);
            } else {
                objectWriter = ObjectWriterImplMap.of(cls);
            }
        } else {
            objectWriter = jSONWriter.getObjectWriter(cls);
        }
        AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(initObjectWriterUpdater, this, null, objectWriter);
        return objectWriter;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeEnumJSONB(JSONWriter jSONWriter, Enum r11) {
        ObjectWriter objectWriter;
        if (r11 == null) {
            return;
        }
        writeFieldName(jSONWriter);
        Class<?> cls = r11.getClass();
        if (this.initValueClass == null) {
            this.initValueClass = cls;
            objectWriter = jSONWriter.getObjectWriter(cls);
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(initObjectWriterUpdater, this, null, objectWriter);
        } else if (this.initValueClass == cls) {
            objectWriter = this.initObjectWriter;
        } else {
            objectWriter = jSONWriter.getObjectWriter(cls);
        }
        ObjectWriter objectWriter2 = objectWriter;
        if (objectWriter2 == null) {
            throw new JSONException("get value writer error, valueType : " + cls);
        }
        objectWriter2.writeJSONB(jSONWriter, r11, this.fieldName, this.fieldType, this.features);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        JSONWriter.Context context = jSONWriter.context;
        long features = context.getFeatures();
        context.setFeatures(this.features | features);
        boolean zWriteInternal = writeInternal(jSONWriter, t);
        context.setFeatures(features);
        return zWriteInternal;
    }

    private boolean writeInternal(JSONWriter jSONWriter, T t) {
        boolean z;
        ObjectWriter objectWriter;
        long features = jSONWriter.getFeatures();
        if (!this.fieldClassSerializable && (JSONWriter.Feature.IgnoreNoneSerializable.mask & features) != 0) {
            return false;
        }
        if (this.backReference && jSONWriter.containsReference(t)) {
            return false;
        }
        try {
            Object fieldValue = getFieldValue(t);
            if (fieldValue == null) {
                if (((JSONWriter.Feature.WriteNulls.mask & features) == 0 && ((JSONWriter.Feature.NullAsDefaultValue.mask & features) == 0 || this.number)) || (JSONWriter.Feature.NotWriteDefaultValue.mask & features) != 0) {
                    if (((JSONWriter.Feature.WriteNullNumberAsZero.mask | JSONWriter.Feature.NullAsDefaultValue.mask) & features) != 0 && this.number) {
                        writeFieldName(jSONWriter);
                        jSONWriter.writeInt32(0);
                        return true;
                    }
                    if ((features & (JSONWriter.Feature.WriteNullBooleanAsFalse.mask | JSONWriter.Feature.NullAsDefaultValue.mask)) == 0 || !(this.fieldClass == Boolean.class || this.fieldClass == AtomicBoolean.class)) {
                        return false;
                    }
                    writeFieldName(jSONWriter);
                    jSONWriter.writeBool(false);
                    return true;
                }
                writeFieldName(jSONWriter);
                if (this.array) {
                    jSONWriter.writeArrayNull();
                } else if (this.number) {
                    jSONWriter.writeNumberNull();
                } else if (this.fieldClass == Appendable.class || this.fieldClass == StringBuffer.class || this.fieldClass == StringBuilder.class) {
                    jSONWriter.writeStringNull();
                } else if (this.fieldClass == Boolean.class) {
                    jSONWriter.writeBooleanNull();
                } else {
                    jSONWriter.writeObjectNull(this.fieldClass);
                }
                return true;
            }
            if (fieldValue == t && this.fieldClass == Throwable.class && this.field != null && this.field.getDeclaringClass() == Throwable.class) {
                return false;
            }
            if ((JSONWriter.Feature.IgnoreNoneSerializable.mask & features) != 0 && !(fieldValue instanceof Serializable)) {
                return false;
            }
            if ((JSONWriter.Feature.IgnoreEmpty.mask & features) != 0) {
                if ((fieldValue instanceof Collection) && ((Collection) fieldValue).isEmpty()) {
                    return false;
                }
                if ((fieldValue instanceof Map) && ((Map) fieldValue).isEmpty()) {
                    return false;
                }
            }
            boolean zIsRefDetect = jSONWriter.isRefDetect(fieldValue);
            if (zIsRefDetect) {
                if (fieldValue == t) {
                    writeFieldName(jSONWriter);
                    jSONWriter.writeReference("..");
                    return true;
                }
                String path = jSONWriter.setPath(this, fieldValue);
                if (path != null) {
                    writeFieldName(jSONWriter);
                    jSONWriter.writeReference(path);
                    jSONWriter.popPath(fieldValue);
                    return true;
                }
            }
            Class<?> cls = fieldValue.getClass();
            if (cls == byte[].class) {
                writeBinary(jSONWriter, (byte[]) fieldValue);
                return true;
            }
            ObjectWriter objectWriter2 = getObjectWriter(jSONWriter, cls);
            if (objectWriter2 == null) {
                throw new JSONException("get objectWriter error : " + cls);
            }
            if (this.unwrapped) {
                boolean zWriteWithUnwrapped = writeWithUnwrapped(jSONWriter, fieldValue, features, zIsRefDetect, objectWriter2);
                fieldValue = fieldValue;
                z = zIsRefDetect;
                objectWriter = objectWriter2;
                if (zWriteWithUnwrapped) {
                    return true;
                }
            } else {
                z = zIsRefDetect;
                objectWriter = objectWriter2;
            }
            writeFieldName(jSONWriter);
            boolean z2 = jSONWriter.jsonb;
            if ((this.features & JSONWriter.Feature.BeanToArray.mask) != 0) {
                if (z2) {
                    objectWriter.writeArrayMappingJSONB(jSONWriter, fieldValue, this.fieldName, this.fieldType, this.features);
                } else {
                    objectWriter.writeArrayMapping(jSONWriter, fieldValue, this.fieldName, this.fieldType, this.features);
                }
            } else if (z2) {
                objectWriter.writeJSONB(jSONWriter, fieldValue, this.fieldName, this.fieldType, this.features);
            } else {
                objectWriter.write(jSONWriter, fieldValue, this.fieldName, this.fieldType, this.features);
            }
            if (z) {
                jSONWriter.popPath(fieldValue);
            }
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }

    protected final boolean writeWithUnwrapped(JSONWriter jSONWriter, Object obj, long j, boolean z, ObjectWriter objectWriter) {
        if (obj instanceof Map) {
            boolean z2 = jSONWriter.jsonb;
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                String string = entry.getKey().toString();
                Object value = entry.getValue();
                if (value != null || (JSONWriter.Feature.WriteNulls.mask & j) != 0) {
                    jSONWriter.writeName(string);
                    if (!z2) {
                        jSONWriter.writeColon();
                    }
                    if (value == null) {
                        jSONWriter.writeNull();
                    } else {
                        jSONWriter.getObjectWriter(value.getClass()).write(jSONWriter, value);
                    }
                }
            }
            if (z) {
                jSONWriter.popPath(obj);
            }
            return true;
        }
        if (!(objectWriter instanceof ObjectWriterAdapter)) {
            return false;
        }
        Iterator<FieldWriter> it = ((ObjectWriterAdapter) objectWriter).fieldWriters.iterator();
        while (it.hasNext()) {
            it.next().write(jSONWriter, obj);
        }
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        ObjectWriter objectWriter;
        JSONWriter jSONWriter2;
        Object fieldValue = getFieldValue(t);
        if (fieldValue == null) {
            jSONWriter.writeNull();
            return;
        }
        Class<?> cls = fieldValue.getClass();
        if (this.initValueClass == null) {
            this.initValueClass = cls;
            objectWriter = jSONWriter.getObjectWriter(cls);
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(initObjectWriterUpdater, this, null, objectWriter);
        } else if (this.initValueClass == cls) {
            objectWriter = this.initObjectWriter;
        } else {
            objectWriter = jSONWriter.getObjectWriter(cls);
        }
        if (objectWriter == null) {
            throw new JSONException("get value writer error, valueType : " + cls);
        }
        boolean z = jSONWriter.isRefDetect() && !ObjectWriterProvider.isNotReferenceDetect(cls);
        if (z) {
            if (fieldValue == t) {
                jSONWriter.writeReference("..");
                return;
            }
            String path = jSONWriter.setPath(this.fieldName, fieldValue);
            if (path != null) {
                jSONWriter.writeReference(path);
                jSONWriter.popPath(fieldValue);
                return;
            }
        }
        if (jSONWriter.jsonb) {
            if (jSONWriter.isBeanToArray()) {
                ObjectWriter objectWriter2 = objectWriter;
                jSONWriter2 = jSONWriter;
                objectWriter2.writeArrayMappingJSONB(jSONWriter2, fieldValue, this.fieldName, this.fieldClass, this.features);
            } else {
                ObjectWriter objectWriter3 = objectWriter;
                jSONWriter2 = jSONWriter;
                objectWriter3.writeJSONB(jSONWriter2, fieldValue, this.fieldName, this.fieldClass, this.features);
            }
        } else {
            ObjectWriter objectWriter4 = objectWriter;
            jSONWriter2 = jSONWriter;
            objectWriter4.write(jSONWriter2, fieldValue, this.fieldName, this.fieldClass, this.features);
        }
        if (z) {
            jSONWriter2.popPath(fieldValue);
        }
    }
}
