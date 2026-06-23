package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
class FieldReaderObjectParam<T> extends FieldReaderObject<T> {
    final String paramName;
    final long paramNameHash;
    final Parameter parameter;

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
    }

    FieldReaderObjectParam(String str, Type type, Class cls, String str2, Parameter parameter, int i, long j, String str3, Locale locale, Object obj, JSONSchema jSONSchema) {
        super(str, type, cls, i, j, str3, locale, obj, jSONSchema, null, null, null);
        this.paramName = str2;
        this.paramNameHash = Fnv.hashCode64(str2);
        this.parameter = parameter;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        throw new JSONException("UnsupportedOperationException");
    }
}
