package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderObjectFunc<T, V> extends FieldReaderObject<T> {
    FieldReaderObjectFunc(String str, Type type, Class<V> cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Method method, BiConsumer<T, V> biConsumer, ObjectReader objectReader) {
        super(str, type, cls, i, j, str2, locale, obj, jSONSchema, method, null, biConsumer);
        this.initReader = objectReader;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (this.fieldType == Float.class) {
            obj = TypeUtils.toFloat(obj);
        } else if (this.fieldType == Double.class) {
            obj = TypeUtils.toDouble(obj);
        }
        if (obj == null && this.fieldClass == StackTraceElement[].class) {
            return;
        }
        if (this.schema != null) {
            this.schema.assertValidate(obj);
        }
        this.function.accept(t, obj);
    }
}
