package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterUUIDFunc<T> extends FieldWriterObjectFinal<T> {
    final Function function;

    FieldWriterUUIDFunc(String str, int i, long j, String str2, String str3, Type type, Class cls, Field field, Method method, Function function) {
        super(str, i, j, str2, str3, type, cls, field, method);
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return this.function.apply(obj);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterObjectFinal, com.alibaba.fastjson2.writer.FieldWriterObject, com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        UUID uuid = (UUID) this.function.apply(t);
        if (uuid == null) {
            if (((this.features | jSONWriter.getFeatures()) & JSONWriter.Feature.WriteNulls.mask) == 0) {
                return false;
            }
            writeFieldName(jSONWriter);
            jSONWriter.writeNull();
            return true;
        }
        writeFieldName(jSONWriter);
        if (this.objectWriter == null) {
            this.objectWriter = getObjectWriter(jSONWriter, UUID.class);
        }
        if (this.objectWriter != ObjectWriterImplUUID.INSTANCE) {
            this.objectWriter.write(jSONWriter, uuid, this.fieldName, this.fieldClass, this.features);
        } else {
            jSONWriter.writeUUID(uuid);
        }
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Function getFunction() {
        return this.function;
    }
}
