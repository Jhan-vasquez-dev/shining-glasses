package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
abstract class FieldWriterInt16<T> extends FieldWriter<T> {
    FieldWriterInt16(String str, int i, long j, String str2, String str3, Class cls, Field field, Method method) {
        super(str, i, j, str2, null, str3, cls, cls, field, method);
    }

    protected final void writeInt16(JSONWriter jSONWriter, short s) {
        if ((jSONWriter.getFeatures(this.features) & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0) {
            writeFieldName(jSONWriter);
            jSONWriter.writeString(Short.toString(s));
        } else {
            writeFieldName(jSONWriter);
            jSONWriter.writeInt16(s);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            Short sh = (Short) getFieldValue(t);
            if (sh != null) {
                writeInt16(jSONWriter, sh.shortValue());
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
        Short sh = (Short) getFieldValue(t);
        if (sh == null) {
            jSONWriter.writeNumberNull();
        } else {
            jSONWriter.writeInt32(sh.shortValue());
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public ObjectWriter getObjectWriter(JSONWriter jSONWriter, Class cls) {
        if (cls == this.fieldClass) {
            return ObjectWriterImplInt16.INSTANCE;
        }
        return jSONWriter.getObjectWriter(cls);
    }
}
