package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterBigIntFunc<T> extends FieldWriter<T> {
    final Function<T, BigInteger> function;

    FieldWriterBigIntFunc(String str, int i, long j, String str2, String str3, Field field, Method method, Function<T, BigInteger> function) {
        super(str, i, j, str2, null, str3, BigInteger.class, BigInteger.class, null, method);
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return this.function.apply(t);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        jSONWriter.writeBigInt((BigInteger) getFieldValue(t), this.features);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        BigInteger bigIntegerApply = this.function.apply(t);
        if (bigIntegerApply == null && ((this.features | jSONWriter.getFeatures()) & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask)) == 0) {
            return false;
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeBigInt(bigIntegerApply, this.features);
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Function getFunction() {
        return this.function;
    }
}
