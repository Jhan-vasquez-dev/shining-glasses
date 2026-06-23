package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderStringFunc<T, V> extends FieldReader<T> {
    final String format;
    final BiConsumer<T, V> function;
    final boolean trim;
    final boolean upper;

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean supportAcceptType(Class cls) {
        return true;
    }

    FieldReaderStringFunc(String str, Class<V> cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Method method, BiConsumer<T, V> biConsumer) {
        super(str, cls, cls, i, j, str2, locale, obj, jSONSchema, method, null);
        this.function = biConsumer;
        this.format = str2;
        this.trim = "trim".equals(str2) || (j & JSONReader.Feature.TrimString.mask) != 0;
        this.upper = "upper".equals(str2);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, int i) {
        accept(t, Integer.toString(i));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        accept(t, Long.toString(j));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        String upperCase;
        if ((obj instanceof String) || obj == null) {
            upperCase = (String) obj;
        } else {
            upperCase = obj.toString();
        }
        if (upperCase != null) {
            if (this.trim) {
                upperCase = upperCase.trim();
            }
            if (this.upper) {
                upperCase = upperCase.toUpperCase();
            }
        }
        if (this.schema != null) {
            this.schema.assertValidate(upperCase);
        }
        try {
            this.function.accept(t, upperCase);
        } catch (Exception e) {
            throw new JSONException("set " + super.toString() + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        String string = jSONReader.readString();
        if (string != null) {
            if (this.trim) {
                string = string.trim();
            }
            if (this.upper) {
                string = string.toUpperCase();
            }
        }
        if (this.schema != null) {
            this.schema.assertValidate(string);
        }
        this.function.accept(t, string);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.readString();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public BiConsumer getFunction() {
        return this.function;
    }
}
