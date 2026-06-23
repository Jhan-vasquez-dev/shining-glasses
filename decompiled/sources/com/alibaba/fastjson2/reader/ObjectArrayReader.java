package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class ObjectArrayReader extends ObjectReaderPrimitive {
    public static final ObjectArrayReader INSTANCE = new ObjectArrayReader();
    public static final long TYPE_HASH_CODE = Fnv.hashCode64("[O");

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public /* bridge */ /* synthetic */ Object createInstance(long j) {
        return super.createInstance(j);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public /* bridge */ /* synthetic */ Class getObjectClass() {
        return super.getObjectClass();
    }

    public ObjectArrayReader() {
        super(Object[].class);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object[] createInstance(Collection collection, long j) {
        Object[] objArr = new Object[collection.size()];
        Iterator it = collection.iterator();
        int i = 0;
        while (it.hasNext()) {
            objArr[i] = it.next();
            i++;
        }
        return objArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x006e  */
    @Override // com.alibaba.fastjson2.reader.ObjectReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object readObject(com.alibaba.fastjson2.JSONReader r7, java.lang.reflect.Type r8, java.lang.Object r9, long r10) {
        /*
            Method dump skipped, instruction units count: 242
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectArrayReader.readObject(com.alibaba.fastjson2.JSONReader, java.lang.reflect.Type, java.lang.Object, long):java.lang.Object");
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        Object any;
        ObjectReader objectReaderCheckAutoType;
        if (jSONReader.getType() == -110 && (objectReaderCheckAutoType = jSONReader.checkAutoType(Object[].class, TYPE_HASH_CODE, j)) != this) {
            return objectReaderCheckAutoType.readJSONBObject(jSONReader, type, obj, j);
        }
        int iStartArray = jSONReader.startArray();
        if (iStartArray == -1) {
            return null;
        }
        Object[] objArr = new Object[iStartArray];
        for (int i = 0; i < iStartArray; i++) {
            byte type2 = jSONReader.getType();
            if (type2 >= 73 && type2 <= 125) {
                any = jSONReader.readString();
            } else if (type2 == -110) {
                ObjectReader objectReaderCheckAutoType2 = jSONReader.checkAutoType(Object.class, 0L, j);
                if (objectReaderCheckAutoType2 != null) {
                    any = objectReaderCheckAutoType2.readJSONBObject(jSONReader, null, null, j);
                } else {
                    any = jSONReader.readAny();
                }
            } else if (type2 == -81) {
                jSONReader.next();
                any = null;
            } else if (type2 == -79) {
                jSONReader.next();
                any = Boolean.TRUE;
            } else if (type2 == -80) {
                jSONReader.next();
                any = Boolean.FALSE;
            } else if (type2 == -66) {
                any = Long.valueOf(jSONReader.readInt64Value());
            } else {
                any = jSONReader.readAny();
            }
            objArr[i] = any;
        }
        return objArr;
    }
}
