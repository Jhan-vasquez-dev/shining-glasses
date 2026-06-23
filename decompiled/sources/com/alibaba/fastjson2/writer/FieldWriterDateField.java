package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterDateField<T> extends FieldWriterDate<T> {
    FieldWriterDateField(String str, int i, long j, String str2, String str3, Field field) {
        super(str, i, j, str2, str3, Date.class, Date.class, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        Date date = (Date) getFieldValue(t);
        if (date == null) {
            jSONWriter.writeNull();
        } else {
            writeDate(jSONWriter, false, date.getTime());
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        Date date = (Date) getFieldValue(t);
        if (date != null) {
            writeDate(jSONWriter, date.getTime());
            return true;
        }
        if (((this.features | jSONWriter.getFeatures()) & JSONWriter.Feature.WriteNulls.mask) == 0) {
            return false;
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeNull();
        return true;
    }
}
