package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderDoubleMethod<T> extends FieldReaderObject<T> {
    FieldReaderDoubleMethod(String str, int i, long j, String str2, Double d, JSONSchema jSONSchema, Method method) {
        super(str, Double.class, Double.class, i, j, str2, null, d, jSONSchema, method, null, null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        Double d = jSONReader.readDouble();
        if (this.schema != null) {
            this.schema.assertValidate(d);
        }
        if (d != null || this.defaultValue == null) {
            try {
                this.method.invoke(t, d);
            } catch (Exception e) {
                throw new JSONException(jSONReader.info("set " + this.fieldName + " error"), e);
            }
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Double d = TypeUtils.toDouble(obj);
        if (this.schema != null) {
            this.schema.assertValidate(d);
        }
        try {
            this.method.invoke(t, d);
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
