package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderDoubleFunc<T> extends FieldReader<T> {
    final BiConsumer<T, Double> function;

    public FieldReaderDoubleFunc(String str, Class cls, int i, long j, String str2, Locale locale, Double d, JSONSchema jSONSchema, Method method, BiConsumer<T, Double> biConsumer) {
        super(str, cls, cls, i, j, str2, locale, d, jSONSchema, method, null);
        this.function = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Double d = TypeUtils.toDouble(obj);
        if (this.schema != null) {
            this.schema.assertValidate(d);
        }
        this.function.accept(t, d);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) throws Exception {
        Double d;
        try {
            d = jSONReader.readDouble();
        } catch (Exception e) {
            if ((jSONReader.features(this.features) & JSONReader.Feature.NullOnError.mask) == 0) {
                throw e;
            }
            d = null;
        }
        if (d != null || this.defaultValue == null) {
            if (this.schema != null) {
                this.schema.assertValidate(d);
            }
            this.function.accept(t, d);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readDouble();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public BiConsumer getFunction() {
        return this.function;
    }
}
