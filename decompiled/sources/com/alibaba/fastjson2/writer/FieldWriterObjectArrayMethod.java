package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterObjectArrayMethod<T> extends FieldWriter<T> {
    final Class itemClass;
    ObjectWriter itemObjectWriter;
    final Type itemType;

    FieldWriterObjectArrayMethod(String str, Type type, int i, long j, String str2, String str3, Type type2, Class cls, Field field, Method method) {
        super(str, i, j, str2, null, str3, type2, cls, field, method);
        this.itemType = type;
        if (type instanceof Class) {
            this.itemClass = (Class) type;
        } else {
            this.itemClass = TypeUtils.getMapping(type);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        try {
            return this.method.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public ObjectWriter getItemWriter(JSONWriter jSONWriter, Type type) {
        if (type == null || type == this.itemType) {
            ObjectWriter objectWriter = this.itemObjectWriter;
            if (objectWriter != null) {
                return objectWriter;
            }
            if (type == Float[].class) {
                if (this.decimalFormat != null) {
                    return new ObjectWriterArrayFinal(Float.class, this.decimalFormat);
                }
                return ObjectWriterArrayFinal.FLOAT_ARRAY;
            }
            if (type == Double[].class) {
                if (this.decimalFormat != null) {
                    return new ObjectWriterArrayFinal(Double.class, this.decimalFormat);
                }
                return ObjectWriterArrayFinal.DOUBLE_ARRAY;
            }
            if (type == BigDecimal[].class) {
                if (this.decimalFormat != null) {
                    return new ObjectWriterArrayFinal(BigDecimal.class, this.decimalFormat);
                }
                return ObjectWriterArrayFinal.DECIMAL_ARRAY;
            }
            if (type == Float.class) {
                if (this.decimalFormat != null) {
                    return new ObjectWriterImplFloat(this.decimalFormat);
                }
                return ObjectWriterImplFloat.INSTANCE;
            }
            if (type == Double.class) {
                if (this.decimalFormat != null) {
                    return new ObjectWriterImplDouble(this.decimalFormat);
                }
                return ObjectWriterImplDouble.INSTANCE;
            }
            if (type == BigDecimal.class) {
                if (this.decimalFormat != null) {
                    return new ObjectWriterImplBigDecimal(this.decimalFormat, null);
                }
                return ObjectWriterImplBigDecimal.INSTANCE;
            }
            ObjectWriter objectWriter2 = jSONWriter.getObjectWriter(this.itemType, this.itemClass);
            this.itemObjectWriter = objectWriter2;
            return objectWriter2;
        }
        return jSONWriter.getObjectWriter(type, (Class) null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        Object[] objArr = (Object[]) getFieldValue(t);
        if (objArr != null) {
            writeArray(jSONWriter, true, objArr);
            return true;
        }
        if (((this.features | jSONWriter.getFeatures()) & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask)) == 0) {
            return false;
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeArrayNull();
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        Object[] objArr = (Object[]) getFieldValue(t);
        if (objArr == null) {
            jSONWriter.writeNull();
        } else {
            writeArray(jSONWriter, false, objArr);
        }
    }

    public void writeArray(JSONWriter jSONWriter, boolean z, Object[] objArr) {
        boolean z2;
        Class<?> cls;
        String path;
        String path2;
        if (z) {
            writeFieldName(jSONWriter);
        }
        boolean zIsRefDetect = jSONWriter.isRefDetect();
        if (zIsRefDetect && (path2 = jSONWriter.setPath(this.fieldName, objArr)) != null) {
            jSONWriter.writeReference(path2);
            return;
        }
        boolean z3 = (this.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        Class<?> cls2 = null;
        if (jSONWriter.jsonb) {
            Class<?> cls3 = objArr.getClass();
            if (cls3 != this.fieldClass) {
                jSONWriter.writeTypeName(TypeUtils.getTypeName(cls3));
            }
            int length = objArr.length;
            jSONWriter.startArray(length);
            ObjectWriter objectWriter = null;
            boolean z4 = zIsRefDetect;
            for (int i = 0; i < length; i++) {
                boolean z5 = z4;
                Object obj = objArr[i];
                if (obj == null) {
                    jSONWriter.writeNull();
                    z4 = z5;
                } else {
                    Class<?> cls4 = obj.getClass();
                    if (cls4 != cls2) {
                        boolean zIsRefDetect2 = jSONWriter.isRefDetect();
                        ObjectWriter itemWriter = getItemWriter(jSONWriter, cls4);
                        if (zIsRefDetect2) {
                            zIsRefDetect2 = !ObjectWriterProvider.isNotReferenceDetect(cls4);
                        }
                        z2 = zIsRefDetect2;
                        objectWriter = itemWriter;
                        cls = cls4;
                    } else {
                        z2 = z5;
                        cls = cls2;
                    }
                    if (z2 && (path = jSONWriter.setPath(i, obj)) != null) {
                        jSONWriter.writeReference(path);
                        jSONWriter.popPath(obj);
                    } else {
                        objectWriter.writeJSONB(jSONWriter, obj, Integer.valueOf(i), this.itemType, this.features);
                        if (z2) {
                            jSONWriter.popPath(obj);
                        }
                    }
                    z4 = z2;
                    cls2 = cls;
                }
            }
            if (zIsRefDetect) {
                jSONWriter.popPath(objArr);
                return;
            }
            return;
        }
        jSONWriter.startArray();
        ObjectWriter itemWriter2 = null;
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (i2 != 0) {
                jSONWriter.writeComma();
            }
            Object obj2 = objArr[i2];
            if (obj2 == null) {
                jSONWriter.writeNull();
            } else if (z3) {
                jSONWriter.writeString(obj2.toString());
            } else {
                Class<?> cls5 = obj2.getClass();
                if (cls5 != cls2) {
                    itemWriter2 = getItemWriter(jSONWriter, cls5);
                    cls2 = cls5;
                }
                itemWriter2.write(jSONWriter, obj2);
            }
        }
        jSONWriter.endArray();
    }
}
