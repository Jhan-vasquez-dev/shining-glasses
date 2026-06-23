package com.alibaba.fastjson2.writer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterInt8Func<T> extends FieldWriterInt8<T> {
    final Function<T, Byte> function;

    FieldWriterInt8Func(String str, int i, long j, String str2, String str3, Field field, Method method, Function<T, Byte> function) {
        super(str, i, j, str2, str3, Byte.class, field, method);
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(T t) {
        return this.function.apply(t);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Function getFunction() {
        return this.function;
    }
}
