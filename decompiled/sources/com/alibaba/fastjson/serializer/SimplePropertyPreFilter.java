package com.alibaba.fastjson.serializer;

import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class SimplePropertyPreFilter extends com.alibaba.fastjson2.filter.SimplePropertyPreFilter implements SerializeFilter {
    public SimplePropertyPreFilter(String... strArr) {
        super(strArr);
    }

    public SimplePropertyPreFilter(Class<?> cls, String... strArr) {
        super(cls, strArr);
    }

    @Override // com.alibaba.fastjson2.filter.SimplePropertyPreFilter
    public Set<String> getIncludes() {
        return super.getIncludes();
    }

    @Override // com.alibaba.fastjson2.filter.SimplePropertyPreFilter
    public Set<String> getExcludes() {
        return super.getExcludes();
    }
}
