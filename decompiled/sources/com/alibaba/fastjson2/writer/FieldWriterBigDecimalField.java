package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterBigDecimalField<T> extends FieldWriter<T> {
    FieldWriterBigDecimalField(String str, int i, long j, String str2, String str3, Field field) {
        super(str, i, j, str2, null, str3, BigDecimal.class, BigDecimal.class, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        BigDecimal bigDecimal = (BigDecimal) getFieldValue(t);
        if (bigDecimal == null && ((this.features | jSONWriter.getFeatures()) & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask)) == 0) {
            return false;
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeDecimal(bigDecimal, this.features, this.decimalFormat);
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        jSONWriter.writeDecimal((BigDecimal) getFieldValue(t), this.features, this.decimalFormat);
    }
}
