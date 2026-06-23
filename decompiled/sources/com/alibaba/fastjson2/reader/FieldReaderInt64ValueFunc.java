package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.util.function.ObjLongConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt64ValueFunc<T> extends FieldReader<T> {
    final ObjLongConsumer<T> function;

    public FieldReaderInt64ValueFunc(String str, int i, Long l, JSONSchema jSONSchema, Method method, ObjLongConsumer<T> objLongConsumer) {
        super(str, Long.TYPE, Long.TYPE, i, 0L, null, null, l, jSONSchema, method, null);
        this.function = objLongConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        if (this.schema != null) {
            this.schema.assertValidate(j);
        }
        this.function.accept(t, j);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        long longValue = TypeUtils.toLongValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(longValue);
        }
        this.function.accept(t, longValue);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, int i) {
        if (this.schema != null) {
            this.schema.assertValidate(i);
        }
        this.function.accept(t, i);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        long int64Value = jSONReader.readInt64Value();
        if (this.schema != null) {
            this.schema.assertValidate(int64Value);
        }
        this.function.accept(t, int64Value);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return Long.valueOf(jSONReader.readInt64Value());
    }
}
