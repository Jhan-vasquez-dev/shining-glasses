package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson2.JSONWriter;

/* JADX INFO: loaded from: classes.dex */
public interface PropertyPreFilter extends com.alibaba.fastjson2.filter.PropertyPreFilter, SerializeFilter {
    boolean apply(JSONSerializer jSONSerializer, Object obj, String str);

    @Override // com.alibaba.fastjson2.filter.PropertyPreFilter
    default boolean process(JSONWriter jSONWriter, Object obj, String str) {
        JSONSerializer jSONSerializer = JSONSerializer.getJSONSerializer(jSONWriter);
        jSONSerializer.setContext(new SerialContext(jSONWriter, jSONSerializer.context, obj, str, 0, 0));
        return apply(jSONSerializer, obj, str);
    }
}
