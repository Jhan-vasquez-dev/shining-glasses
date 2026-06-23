package com.alibaba.fastjson.parser;

import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
final class ObjectDeserializerWrapper implements com.alibaba.fastjson.parser.deserializer.ObjectDeserializer {
    private final ObjectReader raw;

    ObjectDeserializerWrapper(ObjectReader objectReader) {
        this.raw = objectReader;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    @Deprecated
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return (T) this.raw.readObject(defaultJSONParser.getRawReader(), type, obj, 0L);
    }
}
