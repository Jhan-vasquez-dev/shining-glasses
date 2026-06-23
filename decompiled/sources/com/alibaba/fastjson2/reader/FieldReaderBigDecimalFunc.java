package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderBigDecimalFunc<T, V> extends FieldReader<T> {
    final BiConsumer<T, V> function;

    public FieldReaderBigDecimalFunc(String str, Class<V> cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Method method, BiConsumer<T, V> biConsumer) {
        super(str, cls, cls, i, j, str2, locale, obj, jSONSchema, method, null);
        this.function = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        BigDecimal bigDecimal = TypeUtils.toBigDecimal(obj);
        if (this.schema != null) {
            this.schema.assertValidate(bigDecimal);
        }
        try {
            this.function.accept(t, bigDecimal);
        } catch (Exception e) {
            throw new JSONException("set " + super.toString() + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, int i) {
        if (this.schema != null) {
            this.schema.assertValidate(i);
        }
        try {
            this.function.accept(t, (V) BigDecimal.valueOf(i));
        } catch (Exception e) {
            throw new JSONException("set " + super.toString() + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        if (this.schema != null) {
            this.schema.assertValidate(j);
        }
        try {
            this.function.accept(t, (V) BigDecimal.valueOf(j));
        } catch (Exception e) {
            throw new JSONException("set " + super.toString() + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) throws Exception {
        BigDecimal bigDecimal;
        try {
            bigDecimal = jSONReader.readBigDecimal();
        } catch (Exception e) {
            if ((jSONReader.features(this.features) & JSONReader.Feature.NullOnError.mask) == 0) {
                throw e;
            }
            bigDecimal = null;
        }
        if (this.schema != null) {
            this.schema.assertValidate(bigDecimal);
        }
        this.function.accept(t, bigDecimal);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readBigDecimal();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public BiConsumer getFunction() {
        return this.function;
    }
}
