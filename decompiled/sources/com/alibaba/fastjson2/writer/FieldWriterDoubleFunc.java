package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterDoubleFunc<T> extends FieldWriter<T> {
    final Function<T, Double> function;

    FieldWriterDoubleFunc(String str, int i, long j, String str2, String str3, Field field, Method method, Function<T, Double> function) {
        super(str, i, j, str2, null, str3, Double.class, Double.class, field, method);
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return this.function.apply(t);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        Double dApply = this.function.apply(t);
        if (dApply == null) {
            jSONWriter.writeNumberNull();
            return;
        }
        double dDoubleValue = dApply.doubleValue();
        if (this.decimalFormat != null) {
            jSONWriter.writeDouble(dDoubleValue, this.decimalFormat);
        } else {
            jSONWriter.writeDouble(dDoubleValue);
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        try {
            Double dApply = this.function.apply(t);
            if (dApply == null) {
                long features = jSONWriter.getFeatures(this.features);
                if (((JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask) & features) == 0 || (features & JSONWriter.Feature.NotWriteDefaultValue.mask) != 0) {
                    return false;
                }
                writeFieldName(jSONWriter);
                jSONWriter.writeDecimalNull();
                return true;
            }
            writeFieldName(jSONWriter);
            double dDoubleValue = dApply.doubleValue();
            if (this.decimalFormat != null) {
                jSONWriter.writeDouble(dDoubleValue, this.decimalFormat);
            } else {
                jSONWriter.writeDouble(dDoubleValue);
            }
            return true;
        } catch (RuntimeException e) {
            if (jSONWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw e;
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Function getFunction() {
        return this.function;
    }
}
