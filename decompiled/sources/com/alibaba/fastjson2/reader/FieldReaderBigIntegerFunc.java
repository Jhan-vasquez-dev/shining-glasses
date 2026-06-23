package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Locale;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderBigIntegerFunc<T, V> extends FieldReader<T> {
    final BiConsumer<T, V> function;

    public FieldReaderBigIntegerFunc(String str, Class<V> cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Method method, BiConsumer<T, V> biConsumer) {
        super(str, cls, cls, i, j, str2, locale, obj, jSONSchema, method, null);
        this.function = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        BigInteger bigInteger = TypeUtils.toBigInteger(obj);
        if (this.schema != null) {
            this.schema.assertValidate(bigInteger);
        }
        try {
            this.function.accept(t, bigInteger);
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
            this.function.accept(t, (V) BigInteger.valueOf(i));
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
            this.function.accept(t, (V) BigInteger.valueOf(j));
        } catch (Exception e) {
            throw new JSONException("set " + super.toString() + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) throws Exception {
        BigInteger bigInteger;
        try {
            bigInteger = jSONReader.readBigInteger();
        } catch (Exception e) {
            if ((jSONReader.features(this.features) & JSONReader.Feature.NullOnError.mask) == 0) {
                throw e;
            }
            bigInteger = null;
        }
        if (this.schema != null) {
            this.schema.assertValidate(bigInteger);
        }
        this.function.accept(t, bigInteger);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readBigInteger();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public BiConsumer getFunction() {
        return this.function;
    }
}
