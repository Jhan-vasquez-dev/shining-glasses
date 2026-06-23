package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.util.function.ObjIntConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt32ValueFunc<T> extends FieldReader<T> {
    final ObjIntConsumer<T> function;

    public FieldReaderInt32ValueFunc(String str, int i, Integer num, JSONSchema jSONSchema, Method method, ObjIntConsumer<T> objIntConsumer) {
        super(str, Integer.TYPE, Integer.TYPE, i, 0L, null, null, num, jSONSchema, method, null);
        this.function = objIntConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, int i) {
        if (this.schema != null) {
            this.schema.assertValidate(i);
        }
        this.function.accept(t, i);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        if (this.schema != null) {
            this.schema.assertValidate(j);
        }
        this.function.accept(t, (int) j);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        int intValue = TypeUtils.toIntValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(intValue);
        }
        this.function.accept(t, intValue);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        int int32Value = jSONReader.readInt32Value();
        if (this.schema != null) {
            this.schema.assertValidate(int32Value);
        }
        this.function.accept(t, int32Value);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return Integer.valueOf(jSONReader.readInt32Value());
    }
}
