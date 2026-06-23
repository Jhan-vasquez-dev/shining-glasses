package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSONException;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class ToShort implements Function {
    final Short defaultValue;

    public ToShort(Short sh) {
        this.defaultValue = sh;
    }

    @Override // java.util.function.Function
    public Object apply(Object obj) {
        if (obj == null) {
            return this.defaultValue;
        }
        if (obj instanceof Boolean) {
            return Short.valueOf(((Boolean) obj).booleanValue() ? (short) 1 : (short) 0);
        }
        if (obj instanceof Number) {
            return Short.valueOf(((Number) obj).shortValue());
        }
        throw new JSONException("can not cast to Short " + obj.getClass());
    }
}
