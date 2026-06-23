package com.alibaba.fastjson2.writer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterMapFunction extends FieldWriterMap {
    final Function function;

    FieldWriterMapFunction(String str, int i, long j, String str2, Locale locale, String str3, Type type, Class cls, Field field, Method method, Function function, Class<?> cls2) {
        super(str, i, j, str2, locale, str3, type, cls, field, method, cls2);
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return this.function.apply(obj);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Function getFunction() {
        return this.function;
    }
}
