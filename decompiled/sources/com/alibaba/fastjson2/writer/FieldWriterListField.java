package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class FieldWriterListField<T> extends FieldWriterList<T> {
    public FieldWriterListField(String str, Type type, int i, long j, String str2, String str3, Type type2, Class cls, Field field, Class<?> cls2) {
        super(str, type, i, j, str2, str3, type2, cls, field, null, cls2);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        List<String> list = (List) getFieldValue(t);
        JSONWriter.Context context = jSONWriter.context;
        if (list == null) {
            long features = this.features | context.getFeatures();
            if (((JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask) & features) == 0) {
                return false;
            }
            writeFieldName(jSONWriter);
            jSONWriter.writeArrayNull(features);
            return true;
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
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        String path;
        List list = (List) getFieldValue(t);
        if (list == null) {
            jSONWriter.writeNull();
            return;
        }
        boolean zIsRefDetect = jSONWriter.isRefDetect();
        if (zIsRefDetect && (path = jSONWriter.setPath(this.fieldName, list)) != null) {
            jSONWriter.writeReference(path);
            jSONWriter.popPath(list);
        } else {
            writeListValue(jSONWriter, list);
            if (zIsRefDetect) {
                jSONWriter.popPath(list);
            }
        }
    }
}
