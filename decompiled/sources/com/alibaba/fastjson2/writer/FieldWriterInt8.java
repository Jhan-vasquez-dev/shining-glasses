package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
abstract class FieldWriterInt8<T> extends FieldWriter<T> {
    FieldWriterInt8(String str, int i, long j, String str2, String str3, Class cls, Field field, Method method) {
        super(str, i, j, str2, null, str3, cls, cls, field, method);
    }

    protected final void writeInt8(JSONWriter jSONWriter, byte b) {
        if ((this.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0) {
            writeFieldName(jSONWriter);
            jSONWriter.writeString(Byte.toString(b));
        } else {
            writeFieldName(jSONWriter);
            jSONWriter.writeInt8(b);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            Byte b = (Byte) getFieldValue(t);
            if (b != null) {
                writeInt8(jSONWriter, b.byteValue());
                return true;
            }
            if (((this.features | jSONWriter.getFeatures()) & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask)) == 0) {
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
        Byte b = (Byte) getFieldValue(t);
        if (b == null) {
            jSONWriter.writeNumberNull();
        } else {
            jSONWriter.writeInt32(b.byteValue());
        }
    }
}
