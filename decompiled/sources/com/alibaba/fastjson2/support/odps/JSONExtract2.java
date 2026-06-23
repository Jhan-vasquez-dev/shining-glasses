package com.alibaba.fastjson2.support.odps;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.aliyun.odps.udf.UDF;

/* JADX INFO: loaded from: classes.dex */
public class JSONExtract2 extends UDF {
    public String evaluate(String str, String str2) {
        Object objExtract;
        JSONReader jSONReaderOf;
        if (str != null && !str.isEmpty()) {
            try {
                jSONReaderOf = JSONReader.of(str);
            } catch (Throwable unused) {
                objExtract = null;
            }
            try {
                objExtract = JSONPath.of(str2).extract(jSONReaderOf);
                if (jSONReaderOf != null) {
                    try {
                        jSONReaderOf.close();
                    } catch (Throwable unused2) {
                    }
                }
                if (objExtract == null) {
                    return null;
                }
                try {
                    return JSON.toJSONString(objExtract);
                } catch (Exception unused3) {
                }
            } finally {
            }
        }
        return null;
    }
}
