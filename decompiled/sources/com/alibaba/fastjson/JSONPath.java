package com.alibaba.fastjson;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class JSONPath {
    private final com.alibaba.fastjson2.JSONPath path;

    private JSONPath(com.alibaba.fastjson2.JSONPath jSONPath) {
        this.path = jSONPath;
    }

    public static JSONPath compile(String str) {
        if (str == null) {
            throw new JSONException("jsonpath can not be null");
        }
        return new JSONPath(com.alibaba.fastjson2.JSONPath.of(str));
    }

    public Object eval(Object obj) {
        return this.path.eval(obj);
    }

    public boolean set(Object obj, Object obj2) {
        this.path.set(obj, obj2);
        return true;
    }

    public String getPath() {
        return this.path.toString();
    }

    public static <T> T read(String str, String str2, Type type, ParserConfig parserConfig) {
        return (T) TypeUtils.cast(com.alibaba.fastjson2.JSONPath.of(str2).extract(com.alibaba.fastjson2.JSONReader.of(str, JSON.createReadContext(JSON.DEFAULT_PARSER_FEATURE, new Feature[0]))), type, parserConfig);
    }

    public static <T> T read(String str, String str2, Type type) {
        return (T) TypeUtils.cast(com.alibaba.fastjson2.JSONPath.of(str2).extract(com.alibaba.fastjson2.JSONReader.of(str)), type, ParserConfig.global);
    }

    public static Object eval(String str, String str2) {
        return JSON.adaptResult(com.alibaba.fastjson2.JSONPath.eval(str, str2));
    }

    public static Object eval(Object obj, String str) {
        return JSON.adaptResult(com.alibaba.fastjson2.JSONPath.of(str).eval(obj));
    }

    public static boolean set(Object obj, String str, Object obj2) {
        com.alibaba.fastjson2.JSONPath jSONPathOf = com.alibaba.fastjson2.JSONPath.of(str);
        jSONPathOf.setReaderContext(JSON.createReadContext(JSON.DEFAULT_PARSER_FEATURE, new Feature[0]));
        jSONPathOf.set(obj, obj2);
        return true;
    }

    public static Map<String, Object> paths(Object obj) {
        return com.alibaba.fastjson2.JSONPath.paths(obj);
    }

    public static void arrayAdd(Object obj, String str, Object... objArr) {
        com.alibaba.fastjson2.JSONPath.of(str).arrayAdd(obj, objArr);
    }

    public static Object extract(String str, String str2) {
        return JSON.adaptResult(com.alibaba.fastjson2.JSONPath.of(str2).extract(com.alibaba.fastjson2.JSONReader.of(str, JSON.createReadContext(JSON.DEFAULT_PARSER_FEATURE, new Feature[0]))));
    }

    public static boolean remove(Object obj, String str) {
        return com.alibaba.fastjson2.JSONPath.of(str).remove(obj);
    }

    public static boolean contains(Object obj, String str) {
        if (obj == null) {
            return false;
        }
        return com.alibaba.fastjson2.JSONPath.of(str).contains(obj);
    }

    public static Object read(String str, String str2) {
        return com.alibaba.fastjson2.JSONPath.of(str2).extract(com.alibaba.fastjson2.JSONReader.of(str, JSON.createReadContext(JSON.DEFAULT_PARSER_FEATURE, new Feature[0])));
    }
}
