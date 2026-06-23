package com.alibaba.fastjson2.filter;

import com.alibaba.fastjson2.JSONWriter;

/* JADX INFO: loaded from: classes.dex */
public abstract class BeforeFilter implements Filter {
    private static final ThreadLocal<JSONWriter> serializerLocal = new ThreadLocal<>();

    public abstract void writeBefore(Object obj);

    public void writeBefore(JSONWriter jSONWriter, Object obj) {
        ThreadLocal<JSONWriter> threadLocal = serializerLocal;
        JSONWriter jSONWriter2 = threadLocal.get();
        threadLocal.set(jSONWriter);
        writeBefore(obj);
        threadLocal.set(jSONWriter2);
    }

    protected final void writeKeyValue(String str, Object obj) {
        JSONWriter jSONWriter = serializerLocal.get();
        boolean zContainsReference = jSONWriter.containsReference(obj);
        jSONWriter.writeName(str);
        jSONWriter.writeColon();
        jSONWriter.writeAny(obj);
        if (zContainsReference) {
            return;
        }
        jSONWriter.removeReference(obj);
    }
}
