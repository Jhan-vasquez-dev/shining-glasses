package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderFloatFunc<T> extends FieldReader<T> {
    final BiConsumer<T, Float> function;

    public FieldReaderFloatFunc(String str, Class cls, int i, long j, String str2, Locale locale, Float f, JSONSchema jSONSchema, Method method, BiConsumer<T, Float> biConsumer) {
        super(str, cls, cls, i, j, str2, locale, f, jSONSchema, method, null);
        this.function = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Float f = TypeUtils.toFloat(obj);
        if (this.schema != null) {
            this.schema.assertValidate(f);
        }
        this.function.accept(t, f);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) throws Exception {
        Float f;
        try {
            f = jSONReader.readFloat();
        } catch (Exception e) {
            if ((jSONReader.features(this.features) & JSONReader.Feature.NullOnError.mask) == 0) {
                throw e;
            }
            f = null;
        }
        if (this.schema != null) {
            this.schema.assertValidate(f);
        }
        this.function.accept(t, f);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readFloat();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public BiConsumer getFunction() {
        return this.function;
    }
}
