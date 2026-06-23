package com.alibaba.fastjson;

import com.alibaba.fastjson2.modules.ObjectWriterModule;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class Fastjson1xWriterModule implements ObjectWriterModule {
    final ObjectWriterProvider provider;

    public Fastjson1xWriterModule(ObjectWriterProvider objectWriterProvider) {
        this.provider = objectWriterProvider;
    }

    @Override // com.alibaba.fastjson2.modules.ObjectWriterModule
    public ObjectWriter getObjectWriter(Type type, Class cls) {
        if (cls == null || !JSONAware.class.isAssignableFrom(cls) || JSONArray.class == cls || JSONObject.class == cls) {
            return null;
        }
        return JSONAwareWriter.INSTANCE;
    }

    static class JSONAwareWriter implements ObjectWriter {
        static final JSONAwareWriter INSTANCE = new JSONAwareWriter();

        JSONAwareWriter() {
        }

        @Override // com.alibaba.fastjson2.writer.ObjectWriter
        public void write(com.alibaba.fastjson2.JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
            if (obj == null) {
                jSONWriter.writeNull();
            } else {
                jSONWriter.writeRaw(((JSONAware) obj).toJSONString());
            }
        }
    }
}
