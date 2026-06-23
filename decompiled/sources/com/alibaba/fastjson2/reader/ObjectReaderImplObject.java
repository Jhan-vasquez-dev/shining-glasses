package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class ObjectReaderImplObject extends ObjectReaderPrimitive {
    public static final ObjectReaderImplObject INSTANCE = new ObjectReaderImplObject();

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(Collection collection, long j) {
        return collection;
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public /* bridge */ /* synthetic */ Class getObjectClass() {
        return super.getObjectClass();
    }

    public ObjectReaderImplObject() {
        super(Object.class);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(long j) {
        return new JSONObject();
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object createInstance(Map map, long j) {
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        Object obj = map.get(getTypeKey());
        if (!(obj instanceof String)) {
            return map;
        }
        String str = (String) obj;
        ObjectReader objectReaderAutoType = (JSONReader.Feature.SupportAutoType.mask & j) != 0 ? autoType(defaultObjectReaderProvider, Fnv.hashCode64(str)) : null;
        if (objectReaderAutoType == null && (objectReaderAutoType = defaultObjectReaderProvider.getObjectReader(str, getObjectClass(), getFeatures() | j)) == null) {
            throw new JSONException("No suitable ObjectReader found for" + str);
        }
        return objectReaderAutoType != this ? objectReaderAutoType.createInstance(map, j) : map;
    }

    /* JADX WARN: Removed duplicated region for block: B:117:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0085  */
    @Override // com.alibaba.fastjson2.reader.ObjectReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object readObject(com.alibaba.fastjson2.JSONReader r24, java.lang.reflect.Type r25, java.lang.Object r26, long r27) {
        /*
            Method dump skipped, instruction units count: 748
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderImplObject.readObject(com.alibaba.fastjson2.JSONReader, java.lang.reflect.Type, java.lang.Object, long):java.lang.Object");
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        ObjectReader objectReaderCheckAutoType;
        byte type2 = jSONReader.getType();
        if (type2 >= 73 && type2 <= 125) {
            return jSONReader.readString();
        }
        if (type2 == -110 && (objectReaderCheckAutoType = jSONReader.checkAutoType(Object.class, 0L, j)) != null) {
            return objectReaderCheckAutoType.readJSONBObject(jSONReader, type, obj, j);
        }
        if (type2 == -81) {
            jSONReader.next();
            return null;
        }
        return jSONReader.readAny();
    }
}
