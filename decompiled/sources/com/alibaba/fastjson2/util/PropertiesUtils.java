package com.alibaba.fastjson2.util;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Properties;

/* JADX INFO: loaded from: classes.dex */
public class PropertiesUtils {
    public static <T> T toJavaObject(Properties properties, Class<T> cls) {
        return (T) toJavaObject(properties, JSONFactory.getDefaultObjectReaderProvider(), cls, new JSONReader.Feature[0]);
    }

    public static <T> T toJavaObject(Properties properties, ObjectReaderProvider objectReaderProvider, Class<T> cls, JSONReader.Feature... featureArr) {
        ObjectReader objectReader = objectReaderProvider.getObjectReader(cls);
        T t = (T) objectReader.createInstance(JSONReader.Feature.of(featureArr));
        for (Map.Entry entry : properties.entrySet()) {
            String str = (String) entry.getKey();
            String str2 = (String) entry.getValue();
            if (str.lastIndexOf(".") == -1) {
                FieldReader fieldReader = objectReader.getFieldReader(str);
                if (fieldReader != null) {
                    fieldReader.accept(t, TypeUtils.cast(str2, fieldReader.fieldType));
                }
            } else {
                JSONPath.set(t, str, str2);
            }
        }
        return t;
    }

    public static Properties toProperties(Object obj) {
        return toProperties(JSONFactory.getDefaultObjectWriterProvider(), obj, new JSONWriter.Feature[0]);
    }

    public static Properties toProperties(ObjectWriterProvider objectWriterProvider, Object obj, JSONWriter.Feature... featureArr) {
        IdentityHashMap identityHashMap = new IdentityHashMap();
        Properties properties = new Properties();
        paths(objectWriterProvider, identityHashMap, properties, null, obj);
        return properties;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00ab A[PHI: r10
      0x00ab: PHI (r10v8 ??) = (r10v7 ??), (r10v13 ??) binds: [B:31:0x0063, B:40:0x0080] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00d8  */
    /* JADX WARN: Type inference failed for: r10v12 */
    /* JADX WARN: Type inference failed for: r10v13 */
    /* JADX WARN: Type inference failed for: r10v14 */
    /* JADX WARN: Type inference failed for: r10v16 */
    /* JADX WARN: Type inference failed for: r10v18 */
    /* JADX WARN: Type inference failed for: r10v19 */
    /* JADX WARN: Type inference failed for: r10v20 */
    /* JADX WARN: Type inference failed for: r10v21 */
    /* JADX WARN: Type inference failed for: r10v22 */
    /* JADX WARN: Type inference failed for: r10v7, types: [boolean] */
    /* JADX WARN: Type inference failed for: r10v8 */
    /* JADX WARN: Type inference failed for: r10v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static void paths(com.alibaba.fastjson2.writer.ObjectWriterProvider r18, java.util.Map<java.lang.Object, java.lang.String> r19, java.util.Map r20, java.lang.String r21, java.lang.Object r22) {
        /*
            Method dump skipped, instruction units count: 470
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.PropertiesUtils.paths(com.alibaba.fastjson2.writer.ObjectWriterProvider, java.util.Map, java.util.Map, java.lang.String, java.lang.Object):void");
    }
}
