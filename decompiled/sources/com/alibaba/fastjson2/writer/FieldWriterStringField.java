package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterStringField<T> extends FieldWriter<T> {
    FieldWriterStringField(String str, int i, long j, String str2, String str3, Field field) {
        super(str, i, j, str2, null, str3, String.class, String.class, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        String strTrim = (String) getFieldValue(t);
        long features = this.features | jSONWriter.getFeatures();
        if (strTrim == null) {
            if (((JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask) & features) == 0 || (JSONWriter.Feature.NotWriteDefaultValue.mask & features) != 0) {
                return false;
            }
            writeFieldName(jSONWriter);
            if ((features & (JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask)) != 0) {
                jSONWriter.writeString("");
            } else {
                jSONWriter.writeNull();
            }
            return true;
        }
        if (this.trim) {
            strTrim = strTrim.trim();
        }
        if (strTrim.isEmpty() && (features & JSONWriter.Feature.IgnoreEmpty.mask) != 0) {
            return false;
        }
        writeFieldName(jSONWriter);
        if (this.symbol && jSONWriter.jsonb) {
            jSONWriter.writeSymbol(strTrim);
        } else if (this.raw) {
            jSONWriter.writeRaw(strTrim);
        } else {
            jSONWriter.writeString(strTrim);
        }
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        String strTrim = (String) getFieldValue(t);
        if (strTrim == null) {
            jSONWriter.writeNull();
            return;
        }
        if (this.trim) {
            strTrim = strTrim.trim();
        }
        if (this.raw) {
            jSONWriter.writeRaw(strTrim);
        } else {
            jSONWriter.writeString(strTrim);
        }
    }
}
