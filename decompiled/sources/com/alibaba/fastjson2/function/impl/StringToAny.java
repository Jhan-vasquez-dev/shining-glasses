package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.IOUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class StringToAny implements Function {
    final Object defaultValue;
    final Class targetClass;

    public StringToAny(Class cls, Object obj) {
        this.targetClass = cls;
        this.defaultValue = obj;
    }

    @Override // java.util.function.Function
    public Object apply(Object obj) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        String str = (String) obj;
        if (str == null || "null".equals(str) || str.isEmpty()) {
            return this.defaultValue;
        }
        if (this.targetClass == Byte.TYPE || (cls = this.targetClass) == Byte.class) {
            return Byte.valueOf(Byte.parseByte(str));
        }
        if (cls == Short.TYPE || (cls2 = this.targetClass) == Short.class) {
            return Short.valueOf(Short.parseShort(str));
        }
        if (cls2 == Integer.TYPE || (cls3 = this.targetClass) == Integer.class) {
            return Integer.valueOf(Integer.parseInt(str));
        }
        if (cls3 == Long.TYPE || (cls4 = this.targetClass) == Long.class) {
            if (!IOUtils.isNumber(str) && str.length() == 19) {
                return Long.valueOf(DateUtils.parseMillis(str, DateUtils.DEFAULT_ZONE_ID));
            }
            return Long.valueOf(Long.parseLong(str));
        }
        if (cls4 == Float.TYPE || (cls5 = this.targetClass) == Float.class) {
            return Float.valueOf(Float.parseFloat(str));
        }
        if (cls5 == Double.TYPE || (cls6 = this.targetClass) == Double.class) {
            return Double.valueOf(Double.parseDouble(str));
        }
        if (cls6 == Character.TYPE || (cls7 = this.targetClass) == Character.class) {
            return Character.valueOf(str.charAt(0));
        }
        if (cls7 == Boolean.TYPE || (cls8 = this.targetClass) == Boolean.class) {
            return Boolean.valueOf("true".equals(str));
        }
        if (cls8 == BigDecimal.class) {
            return new BigDecimal(str);
        }
        if (cls8 == BigInteger.class) {
            return new BigInteger(str);
        }
        if (cls8 == Collections.class || cls8 == List.class || cls8 == JSONArray.class) {
            if (str.charAt(0) == '[') {
                return JSON.parseObject(str, this.targetClass);
            }
            if (str.indexOf(44) != -1) {
                return Arrays.asList(str.split(","));
            }
        }
        throw new JSONException("can not convert to " + this.targetClass + ", value : " + str);
    }
}
