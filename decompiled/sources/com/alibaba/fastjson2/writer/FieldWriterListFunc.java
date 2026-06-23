package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterListFunc<T> extends FieldWriterList<T> {
    final Function<T, List> function;

    FieldWriterListFunc(String str, int i, long j, String str2, String str3, Type type, Field field, Method method, Function<T, List> function, Type type2, Class cls, Class<?> cls2) {
        super(str, type, i, j, str2, str3, type2, cls, field, method, cls2);
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return this.function.apply(t);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            List listApply = this.function.apply(t);
            if (listApply == null) {
                long features = this.features | jSONWriter.getFeatures();
                if (((JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask) & features) == 0) {
                    return false;
                }
                writeFieldName(jSONWriter);
                jSONWriter.writeArrayNull(features);
                return true;
            }
            if ((this.features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0 && listApply.isEmpty()) {
                return false;
            }
            String path = jSONWriter.setPath(this, listApply);
            if (path != null) {
                writeFieldName(jSONWriter);
                jSONWriter.writeReference(path);
                jSONWriter.popPath(listApply);
                return true;
            }
            if (this.itemType == String.class) {
                writeListStr(jSONWriter, true, listApply);
            } else {
                writeList(jSONWriter, listApply);
            }
            jSONWriter.popPath(listApply);
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
        Class<?> cls = null;
        if (jSONWriter.jsonb) {
            int size = listApply.size();
            jSONWriter.startArray(size);
            ObjectWriter itemWriter = null;
            while (i < size) {
                Object obj = listApply.get(i);
                if (obj == null) {
                    jSONWriter.writeNull();
                } else {
                    Class<?> cls2 = obj.getClass();
                    if (cls2 != cls) {
                        itemWriter = getItemWriter(jSONWriter, cls2);
                        cls = cls2;
                    }
                    itemWriter.write(jSONWriter, obj);
                }
                i++;
            }
            return;
        }
        jSONWriter.startArray();
        ObjectWriter itemWriter2 = null;
        while (i < listApply.size()) {
            if (i != 0) {
                jSONWriter.writeComma();
            }
            Object obj2 = listApply.get(i);
            if (obj2 == null) {
                jSONWriter.writeNull();
            } else {
                Class<?> cls3 = obj2.getClass();
                if (cls3 != cls) {
                    itemWriter2 = getItemWriter(jSONWriter, cls3);
                    cls = cls3;
                }
                itemWriter2.write(jSONWriter, obj2);
            }
            i++;
        }
        jSONWriter.endArray();
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Function getFunction() {
        return this.function;
    }
}
