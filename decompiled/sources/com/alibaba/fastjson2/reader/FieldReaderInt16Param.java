package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Parameter;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class FieldReaderInt16Param<T> extends FieldReaderObjectParam<T> {
    FieldReaderInt16Param(String str, Class cls, String str2, Parameter parameter, int i, long j, String str3, Locale locale, Object obj, JSONSchema jSONSchema) {
        super(str, cls, cls, str2, parameter, i, j, str3, locale, obj, jSONSchema);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        Integer int32 = jSONReader.readInt32();
        if (int32 == null && this.fieldClass == Byte.TYPE && (jSONReader.features(this.features) & JSONReader.Feature.ErrorOnNullForPrimitives.mask) != 0) {
            throw new JSONException(jSONReader.info("short value not support input null"));
        }
        if (int32 == null) {
            return null;
        }
        return Short.valueOf(int32.shortValue());
    }
}
