package com.alibaba.fastjson2;

/* JADX INFO: loaded from: classes.dex */
final class JSONPathSingleNameString extends JSONPathTyped {
    final String name;
    final long nameHashCode;

    public JSONPathSingleNameString(JSONPathSingleName jSONPathSingleName) {
        super(jSONPathSingleName, String.class);
        this.nameHashCode = jSONPathSingleName.nameHashCode;
        this.name = jSONPathSingleName.name;
    }

    @Override // com.alibaba.fastjson2.JSONPathTyped, com.alibaba.fastjson2.JSONPath
    public Object extract(JSONReader jSONReader) {
        if (jSONReader.jsonb) {
            if (!jSONReader.isObject()) {
                return null;
            }
            jSONReader.nextIfObjectStart();
            while (!jSONReader.nextIfObjectEnd()) {
                long fieldNameHashCode = jSONReader.readFieldNameHashCode();
                if (fieldNameHashCode != 0) {
                    if (fieldNameHashCode != this.nameHashCode && !jSONReader.isObject() && !jSONReader.isArray()) {
                        jSONReader.skipValue();
                    } else {
                        return jSONReader.readString();
                    }
                }
            }
            return null;
        }
        if (!jSONReader.nextIfObjectStart()) {
            return null;
        }
        while (!jSONReader.nextIfObjectEnd()) {
            if (jSONReader.readFieldNameHashCode() != this.nameHashCode) {
                jSONReader.skipValue();
            } else {
                return jSONReader.readString();
            }
        }
        return null;
    }
}
