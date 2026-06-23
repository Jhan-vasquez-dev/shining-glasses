package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSONException;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class ToInteger implements Function {
    final Integer defaultValue;

    public ToInteger(Integer num) {
        this.defaultValue = num;
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
            return Integer.valueOf(((Number) obj).intValue());
        }
        throw new JSONException("can not cast to Integer " + obj.getClass());
    }
}
