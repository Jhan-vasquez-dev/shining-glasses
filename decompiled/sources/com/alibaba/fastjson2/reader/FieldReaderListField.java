package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderListField<T> extends FieldReaderList<T, Object> {
    FieldReaderListField(String str, Type type, Class cls, Type type2, Class cls2, int i, long j, String str2, Locale locale, Collection collection, JSONSchema jSONSchema, Field field) {
        super(str, type, cls, type2, cls2, i, j, str2, locale, collection, jSONSchema, null, field, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(Object obj, Object obj2) {
        if (this.schema != null) {
            this.schema.assertValidate(obj2);
        }
        JDKUtils.UNSAFE.putObject(obj, this.fieldOffset, obj2);
    }
}
