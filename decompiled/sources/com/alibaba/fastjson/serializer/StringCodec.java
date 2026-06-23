package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.io.IOException;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class StringCodec implements ObjectSerializer {
    public static final StringCodec instance = new StringCodec();

    public static <T> T deserialze(DefaultJSONParser defaultJSONParser) {
        return (T) defaultJSONParser.getRawReader().readString();
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [T, java.lang.String] */
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        ?? r1 = (T) defaultJSONParser.getRawReader().readString();
        if (type == StringBuffer.class) {
            return (T) new StringBuffer((String) r1);
        }
        return type == StringBuilder.class ? (T) new StringBuilder((String) r1) : r1;
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        jSONSerializer.out.writeString((String) obj);
    }
}
