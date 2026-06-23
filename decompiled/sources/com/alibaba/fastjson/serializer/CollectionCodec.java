package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class CollectionCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CollectionCodec instance = new CollectionCodec();

    @Override // com.alibaba.fastjson2.writer.ObjectWriter, com.alibaba.fastjson2.reader.ObjectReader
    public long getFeatures() {
        return 0L;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public Object deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        JSONReader reader = defaultJSONParser.getLexer().getReader();
        return reader.getContext().getObjectReader(type).readObject(reader, type, obj, 0L);
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        JSONWriter jSONWriter = jSONSerializer.out.raw;
        jSONWriter.getContext().getObjectWriter(obj.getClass()).write(jSONWriter, obj, obj2, type, 0L);
    }
}
