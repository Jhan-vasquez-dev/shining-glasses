package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterListStrFunc<T> extends FieldWriter<T> {
    final Function<T, List> function;

    FieldWriterListStrFunc(String str, int i, long j, String str2, String str3, Field field, Method method, Function<T, List> function, Type type, Class cls) {
        super(str, i, j, str2, null, str3, type, cls, field, method);
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return this.function.apply(t);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        int i = 0;
        try {
            List listApply = this.function.apply(t);
            long features = this.features | jSONWriter.getFeatures();
            if (listApply == null) {
                if (((JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask) & features) == 0) {
                    return false;
                }
                writeFieldName(jSONWriter);
                jSONWriter.writeArrayNull(features);
                return true;
            }
            if ((features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0 && listApply.isEmpty()) {
                return false;
            }
            writeFieldName(jSONWriter);
            if (jSONWriter.jsonb) {
                int size = listApply.size();
                jSONWriter.startArray(size);
                while (i < size) {
                    String str = (String) listApply.get(i);
                    if (str == null) {
                        jSONWriter.writeNull();
                    } else {
                        jSONWriter.writeString(str);
                    }
                    i++;
                }
                return true;
            }
            jSONWriter.startArray();
            while (i < listApply.size()) {
                if (i != 0) {
                    jSONWriter.writeComma();
                }
                String str2 = (String) listApply.get(i);
                if (str2 == null) {
                    jSONWriter.writeNull();
                } else {
                    jSONWriter.writeString(str2);
                }
                i++;
            }
            jSONWriter.endArray();
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
        List listApply = this.function.apply(t);
        if (listApply == null) {
            jSONWriter.writeNull();
            return;
        }
        int i = 0;
        if (jSONWriter.jsonb) {
            int size = listApply.size();
            jSONWriter.startArray(size);
            while (i < size) {
                String str = (String) listApply.get(i);
                if (str == null) {
                    jSONWriter.writeNull();
                } else {
                    jSONWriter.writeString(str);
                }
                i++;
            }
            return;
        }
        jSONWriter.startArray();
        while (i < listApply.size()) {
            if (i != 0) {
                jSONWriter.writeComma();
            }
            String str2 = (String) listApply.get(i);
            if (str2 == null) {
                jSONWriter.writeNull();
            } else {
                jSONWriter.writeString(str2);
            }
            i++;
        }
        jSONWriter.endArray();
    }
}
