package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.codec.DateTimeCodec;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ObjectReaderImplDate extends DateTimeCodec implements ObjectReader {
    public static final ObjectReaderImplDate INSTANCE = new ObjectReaderImplDate(null, null);

    public static ObjectReaderImplDate of(String str, Locale locale) {
        if (str == null) {
            return INSTANCE;
        }
        return new ObjectReaderImplDate(str, locale);
    }

    public ObjectReaderImplDate(String str, Locale locale) {
        super(str, locale);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Class getObjectClass() {
        return Date.class;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        return readDate(jSONReader);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object readObject(JSONReader jSONReader, Type type, Object obj, long j) {
        return readDate(jSONReader);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:120:0x01c6  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x01c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.Object readDate(com.alibaba.fastjson2.JSONReader r14) {
        /*
            Method dump skipped, instruction units count: 521
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderImplDate.readDate(com.alibaba.fastjson2.JSONReader):java.lang.Object");
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Date createInstance(Map map, long j) {
        return TypeUtils.toDate(map);
    }
}
