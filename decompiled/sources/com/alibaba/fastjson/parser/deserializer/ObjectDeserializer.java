package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public interface ObjectDeserializer extends ObjectReader {
    <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj);

    default int getFastMatchToken() {
        return 0;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    default Object readObject(JSONReader jSONReader, Type type, Object obj, long j) {
        return deserialze(new DefaultJSONParser(jSONReader, ParserConfig.global), type, obj);
    }
}
