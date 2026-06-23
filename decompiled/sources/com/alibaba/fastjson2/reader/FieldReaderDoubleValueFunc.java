package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.util.function.ObjDoubleConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderDoubleValueFunc<T> extends FieldReader<T> {
    final ObjDoubleConsumer<T> function;

    public FieldReaderDoubleValueFunc(String str, int i, Double d, JSONSchema jSONSchema, Method method, ObjDoubleConsumer<T> objDoubleConsumer) {
        super(str, Double.TYPE, Double.TYPE, i, 0L, null, null, d, jSONSchema, method, null);
        this.function = objDoubleConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, double d) {
        if (this.schema != null) {
            this.schema.assertValidate(d);
        }
        this.function.accept(t, d);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        double doubleValue = TypeUtils.toDoubleValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        this.function.accept(t, doubleValue);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        double doubleValue = jSONReader.readDoubleValue();
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        this.function.accept(t, doubleValue);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return Double.valueOf(jSONReader.readDoubleValue());
    }
}
