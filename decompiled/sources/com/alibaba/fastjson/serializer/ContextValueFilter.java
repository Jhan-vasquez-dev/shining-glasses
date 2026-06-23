package com.alibaba.fastjson.serializer;

/* JADX INFO: loaded from: classes.dex */
public interface ContextValueFilter extends com.alibaba.fastjson2.filter.ContextValueFilter, SerializeFilter {
    Object process(BeanContext beanContext, Object obj, String str, Object obj2);

    @Override // com.alibaba.fastjson2.filter.ContextValueFilter
    default Object process(com.alibaba.fastjson2.filter.BeanContext beanContext, Object obj, String str, Object obj2) {
        return process(new BeanContext(beanContext), obj, str, obj2);
    }
}
