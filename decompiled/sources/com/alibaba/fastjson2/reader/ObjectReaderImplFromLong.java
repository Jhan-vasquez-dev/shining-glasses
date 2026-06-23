package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;
import java.util.function.LongFunction;

/* JADX INFO: loaded from: classes.dex */
public final class ObjectReaderImplFromLong<T> extends ObjectReaderPrimitive<T> {
    final LongFunction<T> creator;

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public /* bridge */ /* synthetic */ Object createInstance(long j) {
        return super.createInstance(j);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public /* bridge */ /* synthetic */ Class getObjectClass() {
        return super.getObjectClass();
    }

    public ObjectReaderImplFromLong(Class<T> cls, LongFunction longFunction) {
        super(cls);
        this.creator = longFunction;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public T readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        if (jSONReader.nextIfNull()) {
            return null;
        }
        return this.creator.apply(jSONReader.readInt64Value());
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public T readObject(JSONReader jSONReader, Type type, Object obj, long j) {
        if (jSONReader.nextIfNull()) {
            return null;
        }
        return this.creator.apply(jSONReader.readInt64Value());
    }
}
