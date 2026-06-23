package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson2.util.BeanUtils;

/* JADX INFO: loaded from: classes.dex */
public interface NameFilter extends SerializeFilter, com.alibaba.fastjson2.filter.NameFilter {
    static NameFilter of(final PropertyNamingStrategy propertyNamingStrategy) {
        return new NameFilter() { // from class: com.alibaba.fastjson.serializer.NameFilter$$ExternalSyntheticLambda1
            @Override // com.alibaba.fastjson2.filter.NameFilter
            public final String process(Object obj, String str, Object obj2) {
                return BeanUtils.fieldName(str, propertyNamingStrategy.name());
            }
        };
    }

    static NameFilter compose(final NameFilter nameFilter, final NameFilter nameFilter2) {
        return new NameFilter() { // from class: com.alibaba.fastjson.serializer.NameFilter$$ExternalSyntheticLambda0
            @Override // com.alibaba.fastjson2.filter.NameFilter
            public final String process(Object obj, String str, Object obj2) {
                return this.f$0.process(obj, nameFilter.process(obj, str, obj2), obj2);
            }
        };
    }
}
