package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
abstract class FieldWriterBoolVal extends FieldWriterBoolean {
    FieldWriterBoolVal(String str, int i, long j, String str2, String str3, Type type, Class cls, Field field, Method method) {
        super(str, i, j, str2, str3, type, cls, field, method);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterBoolean, com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, Object obj) {
        try {
            boolean zBooleanValue = ((Boolean) getFieldValue(obj)).booleanValue();
            if (!zBooleanValue) {
                long features = this.features | jSONWriter.getFeatures();
                if (this.defaultValue == null && (features & JSONWriter.Feature.NotWriteDefaultValue.mask) != 0) {
                    return false;
                }
            }
            writeBool(jSONWriter, zBooleanValue);
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }
}
