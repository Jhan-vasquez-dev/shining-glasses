package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class JavaBeanSerializer implements ObjectSerializer {
    private final ObjectWriter raw;

    public JavaBeanSerializer(ObjectWriter objectWriter) {
        this.raw = objectWriter;
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        this.raw.write(jSONSerializer.out.raw, obj, obj2, type, 0L);
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public Object getFieldValue(Object obj, String str) {
        FieldWriter fieldWriter = this.raw.getFieldWriter(str);
        if (fieldWriter == null) {
            return null;
        }
        return fieldWriter.getFieldValue(obj);
    }
}
