package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
class FieldReaderStringField<T> extends FieldReaderObjectField<T> {
    final boolean emptyToNull;
    final boolean trim;
    final boolean upper;

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean supportAcceptType(Class cls) {
        return true;
    }

    FieldReaderStringField(String str, Class cls, int i, long j, String str2, String str3, JSONSchema jSONSchema, Field field) {
        super(str, cls, cls, i, j, str2, null, str3, jSONSchema, field);
        this.trim = "trim".equals(str2) || (j & JSONReader.Feature.TrimString.mask) != 0;
        this.upper = "upper".equals(str2);
        this.emptyToNull = (j & JSONReader.Feature.EmptyStringAsNull.mask) != 0;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        String string = jSONReader.readString();
        if (string != null) {
            if (this.trim) {
                string = string.trim();
            }
            if (this.upper) {
                string = string.toUpperCase();
            }
            if (this.emptyToNull && string.isEmpty()) {
                string = null;
            }
        }
        if (this.schema != null) {
            this.schema.assertValidate(string);
        }
        JDKUtils.UNSAFE.putObject(t, this.fieldOffset, string);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValueJSONB(JSONReader jSONReader, T t) {
        String string = jSONReader.readString();
        if (string != null) {
            if (this.trim) {
                string = string.trim();
            }
            if (this.upper) {
                string = string.toUpperCase();
            }
            if (this.emptyToNull && string.isEmpty()) {
                string = null;
            }
        }
        if (this.schema != null) {
            this.schema.assertValidate(string);
        }
        accept(t, string);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public String readFieldValue(JSONReader jSONReader) {
        String string = jSONReader.readString();
        return (!this.trim || string == null) ? string : string.trim();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        String upperCase;
        if (obj != null && !(obj instanceof String)) {
            upperCase = obj.toString();
        } else {
            upperCase = (String) obj;
        }
        if (upperCase != null) {
            if (this.trim) {
                upperCase = upperCase.trim();
            }
            if (this.upper) {
                upperCase = upperCase.toUpperCase();
            }
            if (this.emptyToNull && upperCase.isEmpty()) {
                upperCase = null;
            }
        }
        if (this.schema != null) {
            this.schema.assertValidate(upperCase);
        }
        JDKUtils.UNSAFE.putObject(t, this.fieldOffset, upperCase);
    }
}
