package com.alibaba.fastjson2.function;

/* JADX INFO: loaded from: classes.dex */
@FunctionalInterface
public interface FieldSupplier<T> {
    Object get(T t, int i);
}
