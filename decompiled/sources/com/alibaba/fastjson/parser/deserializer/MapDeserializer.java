package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.ParameterizedTypeImpl;
import java.lang.reflect.Type;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class MapDeserializer implements ObjectDeserializer {
    public static final MapDeserializer instance = new MapDeserializer();

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return (T) defaultJSONParser.getRawReader().read((Class) Map.class);
    }

    public static Map parseMap(DefaultJSONParser defaultJSONParser, Map<String, Object> map, Type type, Object obj, int i) {
        ParameterizedTypeImpl parameterizedTypeImpl = new ParameterizedTypeImpl(Map.class, String.class, type);
        JSONReader reader = defaultJSONParser.getLexer().getReader();
        map.putAll((Map) reader.getObjectReader(parameterizedTypeImpl).readObject(reader, parameterizedTypeImpl, obj, 0L));
        return map;
    }

    public static Map parseMap(DefaultJSONParser defaultJSONParser, Map<String, Object> map, Type type, Object obj) {
        return parseMap(defaultJSONParser, map, type, obj, 0);
    }

    public static Object parseMap(DefaultJSONParser defaultJSONParser, Map<Object, Object> map, Type type, Type type2, Object obj) {
        defaultJSONParser.getRawReader().read(map, type, type2, 0L);
        return map;
    }
}
