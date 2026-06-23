package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterCalendarFunc<T> extends FieldWriterDate<T> {
    final Function<T, Calendar> function;

    FieldWriterCalendarFunc(String str, int i, long j, String str2, String str3, Field field, Method method, Function<T, Calendar> function) {
        super(str, i, j, str2, str3, Calendar.class, Calendar.class, field, method);
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return this.function.apply(t);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public void writeValue(JSONWriter jSONWriter, T t) {
        Calendar calendarApply = this.function.apply(t);
        if (calendarApply == null) {
            jSONWriter.writeNull();
        } else {
            writeDate(jSONWriter, false, calendarApply.getTimeInMillis());
        }
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean write(JSONWriter jSONWriter, T t) {
        Calendar calendarApply = this.function.apply(t);
        if (calendarApply != null) {
            writeDate(jSONWriter, calendarApply.getTimeInMillis());
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
