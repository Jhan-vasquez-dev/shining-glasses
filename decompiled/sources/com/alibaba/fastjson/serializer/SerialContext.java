package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson2.JSONWriter;

/* JADX INFO: loaded from: classes.dex */
public class SerialContext {
    public final int features;
    public final Object fieldName;
    JSONWriter jsonWriter;
    public final Object object;
    public final SerialContext parent;

    SerialContext(JSONWriter jSONWriter, SerialContext serialContext, Object obj, Object obj2, int i, int i2) {
        this.parent = serialContext;
        this.jsonWriter = jSONWriter;
        this.object = obj;
        this.fieldName = obj2;
        this.features = i;
    }

    public SerialContext(SerialContext serialContext, Object obj, Object obj2, int i, int i2) {
        this.parent = serialContext;
        this.object = obj;
        this.fieldName = obj2;
        this.features = i;
    }

    public String getPath() {
        return toString();
    }

    public String toString() {
        JSONWriter jSONWriter = this.jsonWriter;
        if (jSONWriter != null) {
            return jSONWriter.getPath();
        }
        return null;
    }
}
