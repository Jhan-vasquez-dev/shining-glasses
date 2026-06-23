package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
abstract class FieldWriterInt32<T> extends FieldWriter<T> {
    final boolean toString;

    protected FieldWriterInt32(String str, int i, long j, String str2, String str3, Type type, Class cls, Field field, Method method) {
        super(str, i, j, str2, null, str3, type, cls, field, method);
        this.toString = (JSONWriter.Feature.WriteNonStringValueAsString.mask & j) != 0 || "string".equals(str2);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public final void writeInt32(JSONWriter jSONWriter, int i) {
        if (this.toString) {
            writeFieldName(jSONWriter);
            jSONWriter.writeString(Integer.toString(i));
            return;
        }
        writeFieldName(jSONWriter);
        if (this.format != null) {
            jSONWriter.writeInt32(i, this.format);
        } else {
            jSONWriter.writeInt32(i);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            Integer num = (Integer) getFieldValue(t);
            if (num != null) {
                writeInt32(jSONWriter, num.intValue());
                return true;
            }
            if (((this.features | jSONWriter.getFeatures()) & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask)) == 0) {
                return false;
            }
            writeFieldName(jSONWriter);
            jSONWriter.writeNumberNull();
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        Integer num = (Integer) getFieldValue(t);
        if (num == null) {
            jSONWriter.writeNumberNull();
        } else {
            jSONWriter.writeInt32(num);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public ObjectWriter getObjectWriter(JSONWriter jSONWriter, Class cls) {
        if (cls == this.fieldClass) {
            return ObjectWriterImplInt32.INSTANCE;
        }
        return jSONWriter.getObjectWriter(cls);
    }
}
