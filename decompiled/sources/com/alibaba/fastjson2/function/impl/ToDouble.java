package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import java.util.List;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class ToDouble implements Function {
    final Double defaultValue;

    public ToDouble(Double d) {
        this.defaultValue = d;
    }

    @Override // java.util.function.Function
    public Object apply(Object obj) {
        if (obj == null) {
            return this.defaultValue;
        }
        if (obj instanceof Boolean) {
            return Double.valueOf(((Boolean) obj).booleanValue() ? 1.0d : 0.0d);
        }
        if (obj instanceof Number) {
            return Double.valueOf(((Number) obj).doubleValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty()) {
                return this.defaultValue;
            }
            return Double.valueOf(Double.parseDouble(str));
        }
        if (obj instanceof List) {
            List list = (List) obj;
            JSONArray jSONArray = new JSONArray(list.size());
            for (int i = 0; i < list.size(); i++) {
                jSONArray.add(apply(list.get(i)));
            }
            return jSONArray;
        }
        throw new JSONException("can not cast to Double " + obj.getClass());
    }
}
