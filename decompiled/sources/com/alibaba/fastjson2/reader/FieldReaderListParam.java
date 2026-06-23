package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class FieldReaderListParam extends FieldReaderList {
    final String paramName;
    final long paramNameHash;
    final Parameter parameter;

    public FieldReaderListParam(String str, Type type, String str2, Parameter parameter, Class cls, Type type2, Class cls2, int i, long j, String str3, Locale locale, Object obj, JSONSchema jSONSchema) {
        super(str, type, cls, type2, cls2, i, j, str3, locale, obj, jSONSchema, null, null, null);
        this.paramName = str2;
        this.paramNameHash = Fnv.hashCode64(str2);
        this.parameter = parameter;
    }
}
