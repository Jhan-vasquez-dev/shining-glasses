package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class JavaBeanDeserializer implements ObjectDeserializer {
    final ObjectReader objectReader;

    public JavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls, Type type) {
        this.objectReader = (parserConfig == null ? ParserConfig.global : parserConfig).getProvider().getObjectReader(type != null ? type : cls);
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return (T) this.objectReader.readObject(defaultJSONParser.getRawReader(), type, obj, 0L);
    }
}
