package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
abstract class FieldWriterInt64<T> extends FieldWriter<T> {
    final boolean browserCompatible;

    FieldWriterInt64(String str, int i, long j, String str2, String str3, Class cls, Field field, Method method) {
        super(str, i, j, str2, null, str3, cls, cls, field, method);
        this.browserCompatible = (JSONWriter.Feature.BrowserCompatible.mask & j) != 0;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public final void writeInt64(JSONWriter jSONWriter, long j) {
        boolean z = ((jSONWriter.getFeatures() | this.features) & (JSONWriter.Feature.WriteNonStringValueAsString.mask | JSONWriter.Feature.WriteLongAsString.mask)) != 0;
        writeFieldName(jSONWriter);
        if (!z) {
            z = (!this.browserCompatible || TypeUtils.isJavaScriptSupport(j) || jSONWriter.jsonb) ? false : true;
        }
        if (z) {
            jSONWriter.writeString(Long.toString(j));
        } else {
            jSONWriter.writeInt64(j);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            Long l = (Long) getFieldValue(t);
            if (l != null) {
                writeInt64(jSONWriter, l.longValue());
                return true;
            }
            if (((this.features | jSONWriter.getFeatures()) & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask)) == 0) {
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
        Long l = (Long) getFieldValue(t);
        if (l == null) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeInt64(l);
        }
    }
}
