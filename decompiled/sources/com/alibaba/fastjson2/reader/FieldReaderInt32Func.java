package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt32Func<T, V> extends FieldReader<T> {
    final BiConsumer<T, V> function;

    FieldReaderInt32Func(String str, Class<V> cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Method method, BiConsumer<T, V> biConsumer) {
        super(str, cls, cls, i, j, str2, locale, obj, jSONSchema, method, null);
        this.function = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Integer integer = TypeUtils.toInteger(obj);
        if (this.schema != null) {
            this.schema.assertValidate(integer);
        }
        this.function.accept(t, integer);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) throws Exception {
        Integer int32;
        try {
            int32 = jSONReader.readInt32();
        } catch (Exception e) {
            if ((jSONReader.features(this.features) & JSONReader.Feature.NullOnError.mask) == 0) {
                throw e;
            }
            int32 = null;
        }
        if (this.schema != null) {
            this.schema.assertValidate(int32);
        }
        this.function.accept(t, int32);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readInt32();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public BiConsumer getFunction() {
        return this.function;
    }
}
