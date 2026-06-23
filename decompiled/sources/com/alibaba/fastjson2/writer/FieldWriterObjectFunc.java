package com.alibaba.fastjson2.writer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterObjectFunc<T> extends FieldWriterObject<T> {
    final Function function;
    final boolean isArray;

    FieldWriterObjectFunc(String str, int i, long j, String str2, Locale locale, String str3, Type type, Class cls, Field field, Method method, Function function) {
        super(str, i, j, str2, locale, str3, type, cls, field, method);
        this.function = function;
        this.isArray = cls == AtomicIntegerArray.class || cls == AtomicLongArray.class || cls == AtomicReferenceArray.class || cls.isArray();
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
