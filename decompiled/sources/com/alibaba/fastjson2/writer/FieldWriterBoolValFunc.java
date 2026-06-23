package com.alibaba.fastjson2.writer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterBoolValFunc extends FieldWriterBoolVal {
    final Predicate function;

    FieldWriterBoolValFunc(String str, int i, long j, String str2, String str3, Field field, Method method, Predicate predicate) {
        super(str, i, j, str2, str3, Boolean.class, Boolean.class, field, method);
        this.function = predicate;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return Boolean.valueOf(this.function.test(obj));
    }
}
