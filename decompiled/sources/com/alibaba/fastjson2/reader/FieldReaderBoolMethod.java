package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderBoolMethod<T> extends FieldReaderObject<T> {
    FieldReaderBoolMethod(String str, Type type, Class cls, int i, long j, String str2, Locale locale, Boolean bool, JSONSchema jSONSchema, Method method) {
        super(str, type, cls, i, j, str2, locale, bool, jSONSchema, method, null, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        Boolean bool = jSONReader.readBool();
        if (this.schema != null) {
            this.schema.assertValidate(bool);
        }
        try {
            this.method.invoke(t, bool);
        } catch (Exception e) {
            throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        try {
            this.method.invoke(t, TypeUtils.toBoolean(obj));
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
