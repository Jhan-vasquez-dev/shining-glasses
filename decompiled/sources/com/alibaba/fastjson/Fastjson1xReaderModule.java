package com.alibaba.fastjson;

import com.alibaba.fastjson2.modules.ObjectReaderModule;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public class Fastjson1xReaderModule implements ObjectReaderModule {
    final ObjectReaderProvider provider;

    public Fastjson1xReaderModule(ObjectReaderProvider objectReaderProvider) {
        this.provider = objectReaderProvider;
    }

    @Override // com.alibaba.fastjson2.modules.ObjectReaderModule
    public ObjectReader getObjectReader(ObjectReaderProvider objectReaderProvider, Type type) {
        if (type == JSON.class) {
            return new JSONImpl();
        }
        return null;
    }

    static class JSONImpl implements ObjectReader {
        JSONImpl() {
        }

        @Override // com.alibaba.fastjson2.reader.ObjectReader
        public Object readObject(com.alibaba.fastjson2.JSONReader jSONReader, Type type, Object obj, long j) {
            if (jSONReader.isObject()) {
                return jSONReader.read(JSONObject.class);
            }
            if (jSONReader.isArray()) {
                return jSONReader.read(JSONArray.class);
            }
            throw new JSONException("read json error");
        }

        @Override // com.alibaba.fastjson2.reader.ObjectReader
        public Object createInstance(Collection collection, long j) {
            return Collections.emptyList();
        }
    }
}
