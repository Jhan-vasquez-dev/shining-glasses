package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderNumberFunc<T, V> extends FieldReader<T> {
    final BiConsumer<T, V> function;

    public FieldReaderNumberFunc(String str, Class<V> cls, int i, long j, String str2, Locale locale, Number number, JSONSchema jSONSchema, Method method, BiConsumer<T, V> biConsumer) {
        super(str, cls, cls, i, j, str2, locale, number, jSONSchema, method, null);
        this.function = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (this.schema != null) {
            this.schema.assertValidate(obj);
        }
        if (obj instanceof Boolean) {
            obj = Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        }
        this.function.accept(t, (V) obj);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, int i) {
        if (this.schema != null) {
            this.schema.assertValidate(i);
        }
        this.function.accept(t, (V) Integer.valueOf(i));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        if (this.schema != null) {
            this.schema.assertValidate(j);
        }
        this.function.accept(t, (V) Long.valueOf(j));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) throws Exception {
        Number number;
        try {
            number = jSONReader.readNumber();
        } catch (Exception e) {
            if ((jSONReader.features(this.features) & JSONReader.Feature.NullOnError.mask) == 0) {
                throw e;
            }
            number = null;
        }
        if (this.schema != null) {
            this.schema.assertValidate(number);
        }
        this.function.accept(t, number);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readNumber();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public BiConsumer getFunction() {
        return this.function;
    }
}
