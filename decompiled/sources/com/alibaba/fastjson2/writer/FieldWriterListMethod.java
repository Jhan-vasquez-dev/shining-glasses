package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterListMethod<T> extends FieldWriterList<T> {
    FieldWriterListMethod(String str, Type type, int i, long j, String str2, String str3, Field field, Method method, Type type2, Class cls, Class<?> cls2) {
        super(str, type, i, j, str2, str3, type2, cls, field, method, cls2);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        try {
            return this.method.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new JSONException("invoke getter method error, " + this.fieldName, e);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            List<String> list = (List) getFieldValue(t);
            long features = this.features | jSONWriter.getFeatures();
            if (list == null) {
                if (((JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask) & features) == 0) {
                    return false;
                }
                writeFieldName(jSONWriter);
                jSONWriter.writeArrayNull(features);
                return true;
            }
            if ((features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0 && list.isEmpty()) {
                return false;
            }
            String path = jSONWriter.setPath(this, list);
            if (path != null) {
                writeFieldName(jSONWriter);
                jSONWriter.writeReference(path);
                jSONWriter.popPath(list);
                return true;
            }
            if (this.itemType == String.class) {
                writeListStr(jSONWriter, true, list);
            } else {
                writeList(jSONWriter, list);
            }
            jSONWriter.popPath(list);
            return true;
        } catch (JSONException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        List list = (List) getFieldValue(t);
        if (list == null) {
            jSONWriter.writeNull();
        } else {
            writeListValue(jSONWriter, list);
        }
    }
}
