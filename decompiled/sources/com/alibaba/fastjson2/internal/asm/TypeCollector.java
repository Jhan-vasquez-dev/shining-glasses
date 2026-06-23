package com.alibaba.fastjson2.internal.asm;

import androidx.exifinterface.media.ExifInterface;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import okhttp3.HttpUrl;

/* JADX INFO: loaded from: classes.dex */
public class TypeCollector {
    static final Map<String, String> PRIMITIVES;
    protected MethodCollector collector = null;
    final String methodName;
    final Class<?>[] parameterTypes;

    static {
        HashMap map = new HashMap();
        map.put("int", "I");
        map.put("boolean", "Z");
        map.put("byte", "B");
        map.put("char", "C");
        map.put("short", ExifInterface.LATITUDE_SOUTH);
        map.put("float", "F");
        map.put("long", "J");
        map.put("double", "D");
        PRIMITIVES = map;
    }

    public TypeCollector(String str, Class<?>[] clsArr) {
        this.methodName = str;
        this.parameterTypes = clsArr;
    }

    protected MethodCollector visitMethod(int i, String str, String str2) {
        if (this.collector != null || !str.equals(this.methodName)) {
            return null;
        }
        Type[] argumentTypes = Type.getArgumentTypes(str2);
        int i2 = 0;
        for (Type type : argumentTypes) {
            String className = type.getClassName();
            if ("long".equals(className) || "double".equals(className)) {
                i2++;
            }
        }
        if (argumentTypes.length != this.parameterTypes.length) {
            return null;
        }
        for (int i3 = 0; i3 < argumentTypes.length; i3++) {
            if (!correctTypeName(argumentTypes[i3], this.parameterTypes[i3].getName())) {
                return null;
            }
        }
        MethodCollector methodCollector = new MethodCollector(!Modifier.isStatic(i) ? 1 : 0, argumentTypes.length + i2);
        this.collector = methodCollector;
        return methodCollector;
    }

    private boolean correctTypeName(Type type, String str) {
        String className = type.getClassName();
        StringBuilder sb = new StringBuilder();
        while (className.endsWith(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI)) {
            sb.append('[');
            className = className.substring(0, className.length() - 2);
        }
        if (sb.length() != 0) {
            String str2 = PRIMITIVES.get(className);
            if (str2 != null) {
                className = sb.append(str2).toString();
            } else {
                className = sb.append('L').append(className).append(';').toString();
            }
        }
        return className.equals(str);
    }

    public String[] getParameterNamesForMethod() {
        MethodCollector methodCollector = this.collector;
        if (methodCollector == null || !methodCollector.debugInfoPresent) {
            return new String[0];
        }
        return this.collector.getResult().split(",");
    }
}
