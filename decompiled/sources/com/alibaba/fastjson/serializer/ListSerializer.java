package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson2.JSONWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ListSerializer implements ObjectSerializer {
    public static final ListSerializer instance = new ListSerializer();

    private ListSerializer() {
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public final void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        JSONWriter jSONWriter = jSONSerializer.out.raw;
        if (obj == null) {
            jSONWriter.writeArrayNull();
        } else {
            jSONWriter.write((List) obj);
        }
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) throws IOException {
        JSONWriter jSONWriter = jSONSerializer.out.raw;
        if (obj == null) {
            jSONWriter.writeArrayNull();
        } else {
            jSONWriter.write((List) obj);
        }
    }
}
