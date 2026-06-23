package com.alibaba.fastjson2.writer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterBooleanFunc extends FieldWriterBoolean {
    final Function function;

    FieldWriterBooleanFunc(String str, int i, long j, String str2, String str3, Field field, Method method, Function function) {
        super(str, i, j, str2, str3, Boolean.class, Boolean.class, field, method);
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
