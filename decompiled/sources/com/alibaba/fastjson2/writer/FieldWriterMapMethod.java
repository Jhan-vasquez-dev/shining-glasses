package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterMapMethod extends FieldWriterMap {
    FieldWriterMapMethod(String str, int i, long j, String str2, Locale locale, String str3, Type type, Class cls, Field field, Method method, Class<?> cls2) {
        super(str, i, j, str2, locale, str3, type, cls, field, method, cls2);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        try {
            return this.method.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new JSONException("invoke getter method error, " + this.fieldName, e);
        }
    }
}
