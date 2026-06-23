package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt16Func<T, V> extends FieldReader<T> {
    final BiConsumer<T, V> function;

    public FieldReaderInt16Func(String str, Class<V> cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Method method, BiConsumer<T, V> biConsumer) {
        super(str, cls, cls, i, j, str2, locale, obj, jSONSchema, method, null);
        this.function = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        Short sh = TypeUtils.toShort(obj);
        if (this.schema != null) {
            this.schema.assertValidate(sh);
        }
        this.function.accept(t, sh);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) throws Exception {
        Short shValueOf = null;
        try {
            Integer int32 = jSONReader.readInt32();
            if (int32 != null) {
                shValueOf = Short.valueOf(int32.shortValue());
            }
        } catch (Exception e) {
            if ((jSONReader.features(this.features) & JSONReader.Feature.NullOnError.mask) == 0) {
                throw e;
            }
        }
        if (this.schema != null) {
            this.schema.assertValidate(shValueOf);
        }
        this.function.accept(t, shValueOf);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return Short.valueOf((short) jSONReader.readInt32Value());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public BiConsumer getFunction() {
        return this.function;
    }
}
