package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson2.JSONWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public final class ToStringSerializer implements ObjectSerializer {
    public static final ToStringSerializer instance = new ToStringSerializer();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer, com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (obj == null) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeString(obj.toString());
        }
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        JSONWriter jSONWriter = jSONSerializer.out.raw;
        if (obj == null) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeString(obj.toString());
        }
    }
}
