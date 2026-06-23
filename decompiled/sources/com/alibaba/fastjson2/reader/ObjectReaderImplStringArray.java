package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Type;
import java.util.Collection;

/* JADX INFO: loaded from: classes.dex */
public final class ObjectReaderImplStringArray extends ObjectReaderPrimitive {
    static final ObjectReaderImplStringArray INSTANCE = new ObjectReaderImplStringArray();
    public static final long HASH_TYPE = Fnv.hashCode64("[String");

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public /* bridge */ /* synthetic */ Object createInstance(long j) {
        return super.createInstance(j);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public /* bridge */ /* synthetic */ Class getObjectClass() {
        return super.getObjectClass();
    }

    ObjectReaderImplStringArray() {
        super(Long[].class);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(Collection collection, long j) {
        String string;
        String[] strArr = new String[collection.size()];
        int i = 0;
        for (Object obj : collection) {
            if (obj == null) {
                string = null;
            } else if (obj instanceof String) {
                string = (String) obj;
            } else {
                string = obj.toString();
            }
            strArr[i] = string;
            i++;
        }
        return strArr;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object readObject(JSONReader jSONReader, Type type, Object obj, long j) {
        return jSONReader.readStringArray();
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        return jSONReader.readStringArray();
    }
}
