package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public interface ObjectSerializer extends ObjectWriter {
    void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException;

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    default void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        try {
            write(new JSONSerializer(jSONWriter), obj, obj2, type, 0);
        } catch (IOException e) {
            throw new JSONException("write error", e);
        }
    }
}
