package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSONException;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public final class ToNumber implements Function {
    final Number defaultValue;

    public ToNumber(Number number) {
        this.defaultValue = number;
    }

    @Override // java.util.function.Function
    public Object apply(Object obj) {
        if (obj == null) {
            return this.defaultValue;
        }
        if (obj instanceof Boolean) {
            return Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        }
        if (obj instanceof Number) {
            return obj;
        }
        throw new JSONException("can not cast to Number " + obj.getClass());
    }
}
