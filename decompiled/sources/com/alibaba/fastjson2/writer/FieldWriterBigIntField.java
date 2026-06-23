package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.math.BigInteger;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterBigIntField<T> extends FieldWriter<T> {
    FieldWriterBigIntField(String str, int i, long j, String str2, String str3, Field field) {
        super(str, i, j, str2, null, str3, BigInteger.class, BigInteger.class, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        BigInteger bigInteger = (BigInteger) getFieldValue(t);
        if (bigInteger == null && ((this.features | jSONWriter.getFeatures()) & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask)) == 0) {
            return false;
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeBigInt(bigInteger, this.features);
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        jSONWriter.writeBigInt((BigInteger) getFieldValue(t), this.features);
    }
}
