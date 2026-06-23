package com.alibaba.fastjson.support.hsf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class HSFJSONUtils {
    static final long HASH_ARGS_TYPES = Fnv.hashCode64("argsTypes");
    static final long HASH_ARGS_OBJS = Fnv.hashCode64("argsObjs");

    public static Object[] parseInvocationArguments(String str, MethodLocator methodLocator) {
        String[] strArr;
        Method methodFindMethod;
        String[] strArr2;
        JSONReader jSONReaderOf = JSONReader.of(str);
        String str2 = null;
        if (!jSONReaderOf.nextIfObjectStart()) {
            if (!jSONReaderOf.nextIfArrayStart()) {
                return null;
            }
            if (jSONReaderOf.nextIfArrayStart()) {
                int i = 0;
                ArrayList arrayList = null;
                String str3 = null;
                while (!jSONReaderOf.nextIfArrayEnd()) {
                    if (jSONReaderOf.isEnd()) {
                        throw new JSONException("illegal format");
                    }
                    String string = jSONReaderOf.readString();
                    if (i == 0) {
                        str2 = string;
                    } else if (i == 1) {
                        str3 = string;
                    } else if (i == 2) {
                        arrayList = new ArrayList();
                        arrayList.add(str2);
                        arrayList.add(str3);
                        arrayList.add(string);
                    } else {
                        arrayList.add(string);
                    }
                    i++;
                }
                jSONReaderOf.nextIfComma();
                if (i == 0) {
                    strArr = new String[0];
                } else if (i == 1) {
                    strArr = new String[]{str2};
                } else if (i == 2) {
                    strArr = new String[]{str2, str3};
                } else {
                    String[] strArr3 = new String[arrayList.size()];
                    arrayList.toArray(strArr3);
                    strArr = strArr3;
                }
                return jSONReaderOf.readArray(methodLocator.findMethod(strArr).getGenericParameterTypes());
            }
            throw new JSONException("illegal format");
        }
        if (jSONReaderOf.readFieldNameHashCode() != HASH_ARGS_TYPES) {
            methodFindMethod = null;
        } else if (jSONReaderOf.nextIfArrayStart()) {
            int i2 = 0;
            String str4 = null;
            ArrayList arrayList2 = null;
            String str5 = null;
            while (!jSONReaderOf.nextIfArrayEnd()) {
                if (jSONReaderOf.isEnd()) {
                    throw new JSONException("illegal format");
                }
                String string2 = jSONReaderOf.readString();
                if (i2 == 0) {
                    str4 = string2;
                } else if (i2 == 1) {
                    str5 = string2;
                } else if (i2 == 2) {
                    arrayList2 = new ArrayList();
                    arrayList2.add(str4);
                    arrayList2.add(str5);
                    arrayList2.add(string2);
                } else {
                    arrayList2.add(string2);
                }
                i2++;
            }
            jSONReaderOf.nextIfComma();
            if (i2 == 0) {
                strArr2 = new String[0];
            } else if (i2 == 1) {
                strArr2 = new String[]{str4};
            } else if (i2 == 2) {
                strArr2 = new String[]{str4, str5};
            } else {
                strArr2 = new String[arrayList2.size()];
                arrayList2.toArray(strArr2);
            }
            methodFindMethod = methodLocator.findMethod(strArr2);
        } else {
            throw new JSONException("illegal format");
        }
        if (methodFindMethod != null) {
            Type[] genericParameterTypes = methodFindMethod.getGenericParameterTypes();
            if (jSONReaderOf.readFieldNameHashCode() == HASH_ARGS_OBJS) {
                return jSONReaderOf.readArray(genericParameterTypes);
            }
            throw new JSONException("illegal format");
        }
        JSONObject object = JSON.parseObject(str);
        Method methodFindMethod2 = methodLocator.findMethod((String[]) object.getObject("argsTypes", String[].class));
        JSONArray jSONArray = object.getJSONArray("argsObjs");
        if (jSONArray == null) {
            return null;
        }
        Type[] genericParameterTypes2 = methodFindMethod2.getGenericParameterTypes();
        Object[] objArr = new Object[genericParameterTypes2.length];
        for (int i3 = 0; i3 < genericParameterTypes2.length; i3++) {
            objArr[i3] = jSONArray.getObject(i3, genericParameterTypes2[i3]);
        }
        return objArr;
    }
}
