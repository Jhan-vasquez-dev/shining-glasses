package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriterBoolValField extends FieldWriterBoolVal {
    FieldWriterBoolValField(String str, int i, long j, String str2, String str3, Field field, Class cls) {
        super(str, i, j, str2, str3, cls, cls, field, null);
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public Object getFieldValue(Object obj) {
        return Boolean.valueOf(getFieldValueBoolean(obj));
    }

    public boolean getFieldValueBoolean(Object obj) {
        if (obj == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            if (this.fieldOffset != -1) {
                return JDKUtils.UNSAFE.getBoolean(obj, this.fieldOffset);
            }
            return this.field.getBoolean(obj);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }
}
