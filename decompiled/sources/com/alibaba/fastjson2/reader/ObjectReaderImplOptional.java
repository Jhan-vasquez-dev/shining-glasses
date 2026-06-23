package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Optional;

/* JADX INFO: loaded from: classes.dex */
class ObjectReaderImplOptional extends ObjectReaderPrimitive {
    static final ObjectReaderImplOptional INSTANCE = new ObjectReaderImplOptional(null, null, null);
    final String format;
    final Class itemClass;
    ObjectReader itemObjectReader;
    final Type itemType;
    final Locale locale;

    static ObjectReaderImplOptional of(Type type, String str, Locale locale) {
        if (type == null) {
            return INSTANCE;
        }
        return new ObjectReaderImplOptional(type, str, locale);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0017  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ObjectReaderImplOptional(java.lang.reflect.Type r3, java.lang.String r4, java.util.Locale r5) {
        /*
            r2 = this;
            java.lang.Class<java.util.Optional> r0 = java.util.Optional.class
            r2.<init>(r0)
            boolean r0 = r3 instanceof java.lang.reflect.ParameterizedType
            if (r0 == 0) goto L17
            java.lang.reflect.ParameterizedType r3 = (java.lang.reflect.ParameterizedType) r3
            java.lang.reflect.Type[] r3 = r3.getActualTypeArguments()
            int r0 = r3.length
            r1 = 1
            if (r0 != r1) goto L17
            r0 = 0
            r3 = r3[r0]
            goto L18
        L17:
            r3 = 0
        L18:
            r2.itemType = r3
            java.lang.Class r3 = com.alibaba.fastjson2.util.TypeUtils.getClass(r3)
            r2.itemClass = r3
            r2.format = r4
            r2.locale = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderImplOptional.<init>(java.lang.reflect.Type, java.lang.String, java.util.Locale):void");
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderPrimitive, com.alibaba.fastjson2.reader.ObjectReader
    public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        Object jSONBObject;
        Type type2 = this.itemType;
        if (type2 == null) {
            jSONBObject = jSONReader.readAny();
        } else {
            if (this.itemObjectReader == null) {
                String str = this.format;
                ObjectReader objectReaderCreateFormattedObjectReader = str != null ? FieldReader.createFormattedObjectReader(type2, this.itemClass, str, this.locale) : null;
                if (objectReaderCreateFormattedObjectReader == null) {
                    this.itemObjectReader = jSONReader.getObjectReader(this.itemType);
                } else {
                    this.itemObjectReader = objectReaderCreateFormattedObjectReader;
                }
            }
            jSONBObject = this.itemObjectReader.readJSONBObject(jSONReader, this.itemType, obj, 0L);
        }
        if (jSONBObject == null) {
            return Optional.empty();
        }
        return Optional.of(jSONBObject);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Object readObject(JSONReader jSONReader, Type type, Object obj, long j) {
        Object object;
        Type type2 = this.itemType;
        if (type2 == null) {
            object = jSONReader.readAny();
        } else {
            if (this.itemObjectReader == null) {
                String str = this.format;
                ObjectReader objectReaderCreateFormattedObjectReader = str != null ? FieldReader.createFormattedObjectReader(type2, this.itemClass, str, this.locale) : null;
                if (objectReaderCreateFormattedObjectReader == null) {
                    this.itemObjectReader = jSONReader.getObjectReader(this.itemType);
                } else {
                    this.itemObjectReader = objectReaderCreateFormattedObjectReader;
                }
            }
            object = this.itemObjectReader.readObject(jSONReader, this.itemType, obj, 0L);
        }
        if (object == null) {
            return Optional.empty();
        }
        return Optional.of(object);
    }
}
