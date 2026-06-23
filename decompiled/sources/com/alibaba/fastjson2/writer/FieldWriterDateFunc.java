package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterDateFunc<T> extends FieldWriterDate<T> {
    final Function<T, Date> function;

    FieldWriterDateFunc(String str, int i, long j, String str2, String str3, Field field, Method method, Function<T, Date> function) {
        super(str, i, j, str2, str3, Date.class, Date.class, field, method);
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return this.function.apply(t);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        Date dateApply = this.function.apply(t);
        if (dateApply == null) {
            jSONWriter.writeNull();
        } else {
            writeDate(jSONWriter, false, dateApply.getTime());
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        Date dateApply = this.function.apply(t);
        if (dateApply != null) {
            writeDate(jSONWriter, dateApply.getTime());
            return true;
        }
        if (((this.features | jSONWriter.getFeatures()) & JSONWriter.Feature.WriteNulls.mask) == 0) {
            return false;
        }
        writeFieldName(jSONWriter);
        jSONWriter.writeNull();
        return true;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Function getFunction() {
        return this.function;
    }
}
