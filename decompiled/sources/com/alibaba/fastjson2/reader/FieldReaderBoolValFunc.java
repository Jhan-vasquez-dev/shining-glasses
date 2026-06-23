package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.function.ObjBoolConsumer;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderBoolValFunc<T> extends FieldReader<T> {
    final ObjBoolConsumer<T> function;

    public FieldReaderBoolValFunc(String str, int i, JSONSchema jSONSchema, Method method, ObjBoolConsumer<T> objBoolConsumer) {
        super(str, Boolean.TYPE, Boolean.TYPE, i, 0L, null, null, null, jSONSchema, method, null);
        this.function = objBoolConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        this.function.accept(t, TypeUtils.toBooleanValue(obj));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, boolean z) {
        this.function.accept(t, z);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        this.function.accept(t, jSONReader.readBoolValue());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readBool();
    }
}
