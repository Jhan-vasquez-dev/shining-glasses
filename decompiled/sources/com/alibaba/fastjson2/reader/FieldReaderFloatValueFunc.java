package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.function.ObjFloatConsumer;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderFloatValueFunc<T> extends FieldReader<T> {
    final ObjFloatConsumer<T> function;

    public FieldReaderFloatValueFunc(String str, int i, Float f, JSONSchema jSONSchema, Method method, ObjFloatConsumer<T> objFloatConsumer) {
        super(str, Float.TYPE, Float.TYPE, i, 0L, null, null, f, jSONSchema, method, null);
        this.function = objFloatConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, float f) {
        if (this.schema != null) {
            this.schema.assertValidate(f);
        }
        this.function.accept(t, f);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        float floatValue = TypeUtils.toFloatValue(obj);
        if (this.schema != null) {
            this.schema.assertValidate(floatValue);
        }
        this.function.accept(t, floatValue);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        float floatValue = jSONReader.readFloatValue();
        if (this.schema != null) {
            this.schema.assertValidate(floatValue);
        }
        this.function.accept(t, floatValue);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return Float.valueOf(jSONReader.readFloatValue());
    }
}
