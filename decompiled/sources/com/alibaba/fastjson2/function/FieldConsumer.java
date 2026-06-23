package com.alibaba.fastjson2.function;

/* JADX INFO: loaded from: classes.dex */
@FunctionalInterface
public interface FieldConsumer<T> {
    void accept(T t, int i, Object obj);
}
