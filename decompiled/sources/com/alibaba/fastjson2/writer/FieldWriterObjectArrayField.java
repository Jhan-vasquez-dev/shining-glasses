package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterObjectArrayField<T> extends FieldWriter<T> {
    final Class itemClass;
    ObjectWriter itemObjectWriter;
    final Type itemType;

    FieldWriterObjectArrayField(String str, Type type, int i, long j, String str2, String str3, Type type2, Class cls, Field field) {
        super(str, i, j, str2, null, str3, type2, cls, field, null);
        this.itemType = type;
        if (type instanceof Class) {
            this.itemClass = (Class) type;
        } else {
            this.itemClass = TypeUtils.getMapping(type);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public ObjectWriter getItemWriter(JSONWriter jSONWriter, Type type) {
        if (type == null || type == this.itemType) {
            ObjectWriter objectWriter = this.itemObjectWriter;
            if (objectWriter != null) {
                return objectWriter;
            }
            if (type == Double.class) {
                this.itemObjectWriter = new ObjectWriterImplDouble(new DecimalFormat(this.format));
            } else if (type == Float.class) {
                this.itemObjectWriter = new ObjectWriterImplFloat(new DecimalFormat(this.format));
            } else if (type == BigDecimal.class && this.decimalFormat != null) {
                this.itemObjectWriter = new ObjectWriterImplBigDecimal(this.decimalFormat, null);
            } else {
                this.itemObjectWriter = jSONWriter.getObjectWriter(this.itemType, this.itemClass);
            }
            return this.itemObjectWriter;
        }
        return jSONWriter.getObjectWriter(type, TypeUtils.getClass(type));
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
        Class<?> cls;
        String path;
        String path2;
        long features = jSONWriter.getFeatures();
        boolean z2 = (JSONWriter.Feature.ReferenceDetection.mask & features) != 0;
        if (z) {
            if (objArr.length == 0 && (features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0) {
                return;
            } else {
                writeFieldName(jSONWriter);
            }
        }
        if (z2 && (path2 = jSONWriter.setPath(this.fieldName, objArr)) != null) {
            jSONWriter.writeReference(path2);
            return;
        }
        Class<?> cls2 = null;
        if (jSONWriter.jsonb) {
            Class<?> cls3 = objArr.getClass();
            if (cls3 != this.fieldClass) {
                jSONWriter.writeTypeName(TypeUtils.getTypeName(cls3));
            }
            int length = objArr.length;
            jSONWriter.startArray(length);
            ObjectWriter objectWriter = null;
            boolean z3 = z2;
            for (int i = 0; i < length; i++) {
                Object obj = objArr[i];
                if (obj == null) {
                    jSONWriter.writeNull();
                } else {
                    Class<?> cls4 = obj.getClass();
                    if (cls4 != cls2) {
                        boolean zIsRefDetect = jSONWriter.isRefDetect();
                        ObjectWriter itemWriter = getItemWriter(jSONWriter, cls4);
                        if (zIsRefDetect) {
                            zIsRefDetect = !ObjectWriterProvider.isNotReferenceDetect(cls4);
                        }
                        z3 = zIsRefDetect;
                        objectWriter = itemWriter;
                        cls = cls4;
                    } else {
                        cls = cls2;
                    }
                    boolean z4 = z3;
                    if (z4 && (path = jSONWriter.setPath(i, obj)) != null) {
                        jSONWriter.writeReference(path);
                        jSONWriter.popPath(obj);
                    } else {
                        objectWriter.writeJSONB(jSONWriter, obj, Integer.valueOf(i), this.itemType, this.features);
                        if (z4) {
                            jSONWriter.popPath(obj);
                        }
                    }
                    cls2 = cls;
                    z3 = z4;
                }
            }
            if (z2) {
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

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public ObjectWriter getObjectWriter(JSONWriter jSONWriter, Class cls) {
        if (cls == String[].class) {
            return ObjectWriterImplStringArray.INSTANCE;
        }
        if (cls == Float[].class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(Float.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.FLOAT_ARRAY;
        }
        if (cls == Double[].class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(Double.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.DOUBLE_ARRAY;
        }
        if (cls == BigDecimal[].class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(BigDecimal.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.DECIMAL_ARRAY;
        }
        return jSONWriter.getObjectWriter(cls);
    }
}
