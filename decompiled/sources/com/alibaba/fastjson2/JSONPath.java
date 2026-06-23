package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONPathFilter;
import com.alibaba.fastjson2.JSONPathFunction;
import com.alibaba.fastjson2.JSONPathSegment;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ValueConsumer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public abstract class JSONPath {
    static final JSONReader.Context PARSE_CONTEXT = JSONFactory.createReadContext();
    final long features;
    final String path;
    JSONReader.Context readerContext;
    JSONWriter.Context writerContext;

    public abstract boolean contains(Object obj);

    public boolean endsWithFilter() {
        return false;
    }

    public abstract Object eval(Object obj);

    public abstract Object extract(JSONReader jSONReader);

    public abstract String extractScalar(JSONReader jSONReader);

    public abstract JSONPath getParent();

    public boolean isPrevious() {
        return false;
    }

    public abstract boolean isRef();

    public abstract boolean remove(Object obj);

    public abstract void set(Object obj, Object obj2);

    public abstract void set(Object obj, Object obj2, JSONReader.Feature... featureArr);

    public abstract void setCallback(Object obj, BiFunction biFunction);

    public abstract void setInt(Object obj, int i);

    public abstract void setLong(Object obj, long j);

    protected JSONPath(String str, Feature... featureArr) {
        this.path = str;
        long j = 0;
        for (Feature feature : featureArr) {
            j |= feature.mask;
        }
        this.features = j;
    }

    protected JSONPath(String str, long j) {
        this.path = str;
        this.features = j;
    }

    public final String toString() {
        return this.path;
    }

    public static Object extract(String str, String str2) {
        return of(str2).extract(JSONReader.of(str));
    }

    public static Object extract(String str, String str2, Feature... featureArr) {
        return of(str2, featureArr).extract(JSONReader.of(str));
    }

    public static Object eval(String str, String str2) {
        return extract(str, str2);
    }

    public static Object eval(Object obj, String str) {
        return of(str).eval(obj);
    }

    public static String set(String str, String str2, Object obj) {
        Object obj2 = JSON.parse(str);
        of(str2).set(obj2, obj);
        return JSON.toJSONString(obj2);
    }

    public static boolean contains(Object obj, String str) {
        if (obj == null) {
            return false;
        }
        return of(str).contains(obj);
    }

    public static Object set(Object obj, String str, Object obj2) {
        of(str).set(obj, obj2);
        return obj;
    }

    public static Object setCallback(Object obj, String str, Function function) {
        of(str).setCallback(obj, function);
        return obj;
    }

    public static Object setCallback(Object obj, String str, BiFunction biFunction) {
        of(str).setCallback(obj, biFunction);
        return obj;
    }

    public static String remove(String str, String str2) {
        Object obj = JSON.parse(str);
        of(str2).remove(obj);
        return JSON.toJSONString(obj);
    }

    public static void remove(Object obj, String str) {
        of(str).remove(obj);
    }

    public static Map<String, Object> paths(Object obj) {
        IdentityHashMap identityHashMap = new IdentityHashMap();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        RootPath.INSTANCE.paths(identityHashMap, linkedHashMap, "$", obj);
        return linkedHashMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00ae A[PHI: r10
      0x00ae: PHI (r10v8 ??) = (r10v7 ??), (r10v13 ??) binds: [B:31:0x0066, B:40:0x0083] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00db  */
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
    void paths(java.util.Map<java.lang.Object, java.lang.String> r19, java.util.Map r20, java.lang.String r21, java.lang.Object r22) {
        /*
            Method dump skipped, instruction units count: 460
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPath.paths(java.util.Map, java.util.Map, java.lang.String, java.lang.Object):void");
    }

    public void arrayAdd(Object obj, Object... objArr) {
        Object objEval = eval(obj);
        if (objEval == null) {
            set(obj, JSONArray.of(objArr));
        } else if (objEval instanceof Collection) {
            ((Collection) objEval).addAll(Arrays.asList(objArr));
        }
    }

    protected JSONReader.Context createContext() {
        return JSONFactory.createReadContext();
    }

    public Object extract(String str) {
        if (str == null) {
            return null;
        }
        JSONReader jSONReaderOf = JSONReader.of(str, createContext());
        try {
            Object objExtract = extract(jSONReaderOf);
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return objExtract;
        } catch (Throwable th) {
            if (jSONReaderOf != null) {
                try {
                    jSONReaderOf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public Object extract(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        JSONReader jSONReaderOf = JSONReader.of(bArr, createContext());
        try {
            Object objExtract = extract(jSONReaderOf);
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return objExtract;
        } catch (Throwable th) {
            if (jSONReaderOf != null) {
                try {
                    jSONReaderOf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public Object extract(byte[] bArr, int i, int i2, Charset charset) {
        if (bArr == null) {
            return null;
        }
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, charset, createContext());
        try {
            Object objExtract = extract(jSONReaderOf);
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return objExtract;
        } catch (Throwable th) {
            if (jSONReaderOf != null) {
                try {
                    jSONReaderOf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public JSONReader.Context getReaderContext() {
        if (this.readerContext == null) {
            this.readerContext = JSONFactory.createReadContext();
        }
        return this.readerContext;
    }

    public JSONPath setReaderContext(JSONReader.Context context) {
        this.readerContext = context;
        return this;
    }

    public JSONWriter.Context getWriterContext() {
        if (this.writerContext == null) {
            this.writerContext = JSONFactory.createWriteContext();
        }
        return this.writerContext;
    }

    public JSONPath setWriterContext(JSONWriter.Context context) {
        this.writerContext = context;
        return this;
    }

    public void setCallback(Object obj, Function function) {
        setCallback(obj, new JSONPathFunction.BiFunctionAdapter(function));
    }

    public void extract(JSONReader jSONReader, ValueConsumer valueConsumer) {
        Object objExtract = extract(jSONReader);
        if (objExtract == null) {
            valueConsumer.acceptNull();
            return;
        }
        if (objExtract instanceof Number) {
            valueConsumer.accept((Number) objExtract);
            return;
        }
        if (objExtract instanceof String) {
            valueConsumer.accept((String) objExtract);
            return;
        }
        if (objExtract instanceof Boolean) {
            valueConsumer.accept(((Boolean) objExtract).booleanValue());
        } else if (objExtract instanceof Map) {
            valueConsumer.accept((Map) objExtract);
        } else {
            if (objExtract instanceof List) {
                valueConsumer.accept((List) objExtract);
                return;
            }
            throw new JSONException("TODO : " + objExtract.getClass());
        }
    }

    public void extractScalar(JSONReader jSONReader, ValueConsumer valueConsumer) {
        String strExtractScalar = extractScalar(jSONReader);
        if (strExtractScalar == null) {
            valueConsumer.acceptNull();
        } else {
            valueConsumer.accept(strExtractScalar.toString());
        }
    }

    public Long extractInt64(JSONReader jSONReader) {
        long jExtractInt64Value = extractInt64Value(jSONReader);
        if (jSONReader.wasNull) {
            return null;
        }
        return Long.valueOf(jExtractInt64Value);
    }

    public long extractInt64Value(JSONReader jSONReader) {
        Object objExtract = extract(jSONReader);
        if (objExtract == null) {
            jSONReader.wasNull = true;
            return 0L;
        }
        if (objExtract instanceof Number) {
            return ((Number) objExtract).longValue();
        }
        Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(objExtract.getClass(), Long.TYPE);
        if (typeConvert == null) {
            throw new JSONException("can not convert to long : " + objExtract);
        }
        return ((Long) typeConvert.apply(objExtract)).longValue();
    }

    public Integer extractInt32(JSONReader jSONReader) {
        int iExtractInt32Value = extractInt32Value(jSONReader);
        if (jSONReader.wasNull) {
            return null;
        }
        return Integer.valueOf(iExtractInt32Value);
    }

    public int extractInt32Value(JSONReader jSONReader) {
        Object objExtract = extract(jSONReader);
        if (objExtract == null) {
            jSONReader.wasNull = true;
            return 0;
        }
        if (objExtract instanceof Number) {
            return ((Number) objExtract).intValue();
        }
        Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(objExtract.getClass(), Integer.TYPE);
        if (typeConvert == null) {
            throw new JSONException("can not convert to int : " + objExtract);
        }
        return ((Integer) typeConvert.apply(objExtract)).intValue();
    }

    @Deprecated
    public static JSONPath compile(String str) {
        return of(str);
    }

    public static JSONPath compile(String str, Class cls) {
        return JSONFactory.getDefaultJSONPathCompiler().compile(cls, of(str));
    }

    static JSONPathSingle of(JSONPathSegment jSONPathSegment) {
        String str;
        if ((jSONPathSegment instanceof JSONPathSegment.MultiIndexSegment) || (jSONPathSegment instanceof JSONPathSegmentIndex)) {
            str = "$";
        } else {
            str = "$.";
        }
        String str2 = str + jSONPathSegment.toString();
        if (jSONPathSegment instanceof JSONPathSegmentName) {
            return new JSONPathSingleName(str2, (JSONPathSegmentName) jSONPathSegment, new Feature[0]);
        }
        return new JSONPathSingle(jSONPathSegment, str2, new Feature[0]);
    }

    public static JSONPath of(String str) {
        if ("#-1".equals(str)) {
            return PreviousPath.INSTANCE;
        }
        return new JSONPathParser(str).parse(new Feature[0]);
    }

    public static JSONPath of(String str, Type type) {
        return JSONPathTyped.of(of(str), type);
    }

    public static JSONPath of(String str, Type type, Feature... featureArr) {
        return JSONPathTyped.of(of(str, featureArr), type);
    }

    public static JSONPath of(String[] strArr, Type[] typeArr) {
        return of(strArr, typeArr, null, null, null, new JSONReader.Feature[0]);
    }

    public static JSONPath of(String[] strArr, Type[] typeArr, JSONReader.Feature... featureArr) {
        return of(strArr, typeArr, null, null, null, featureArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:133:0x02e7  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x02f5  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0311  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0354  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.alibaba.fastjson2.JSONPath of(java.lang.String[] r21, java.lang.reflect.Type[] r22, java.lang.String[] r23, long[] r24, java.time.ZoneId r25, com.alibaba.fastjson2.JSONReader.Feature... r26) {
        /*
            Method dump skipped, instruction units count: 1013
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPath.of(java.lang.String[], java.lang.reflect.Type[], java.lang.String[], long[], java.time.ZoneId, com.alibaba.fastjson2.JSONReader$Feature[]):com.alibaba.fastjson2.JSONPath");
    }

    public static JSONPath of(String str, Feature... featureArr) {
        if ("#-1".equals(str)) {
            return PreviousPath.INSTANCE;
        }
        return new JSONPathParser(str).parse(featureArr);
    }

    static JSONPathFilter.Operator parseOperator(JSONReader jSONReader) {
        switch (jSONReader.ch) {
            case '!':
                jSONReader.next();
                if (jSONReader.ch == '=') {
                    jSONReader.next();
                    return JSONPathFilter.Operator.NE;
                }
                throw new JSONException("not support operator : !" + jSONReader.ch);
            case '<':
                jSONReader.next();
                if (jSONReader.ch == '=') {
                    jSONReader.next();
                    return JSONPathFilter.Operator.LE;
                }
                if (jSONReader.ch == '>') {
                    jSONReader.next();
                    return JSONPathFilter.Operator.NE;
                }
                return JSONPathFilter.Operator.LT;
            case '=':
                jSONReader.next();
                if (jSONReader.ch == '~') {
                    jSONReader.nextWithoutComment();
                    return JSONPathFilter.Operator.REG_MATCH;
                }
                if (jSONReader.ch == '=') {
                    jSONReader.next();
                    return JSONPathFilter.Operator.EQ;
                }
                return JSONPathFilter.Operator.EQ;
            case '>':
                jSONReader.next();
                if (jSONReader.ch == '=') {
                    jSONReader.next();
                    return JSONPathFilter.Operator.GE;
                }
                return JSONPathFilter.Operator.GT;
            case 'B':
            case 'b':
                jSONReader.readFieldNameHashCodeUnquote();
                String fieldName = jSONReader.getFieldName();
                if ("between".equalsIgnoreCase(fieldName)) {
                    return JSONPathFilter.Operator.BETWEEN;
                }
                throw new JSONException("not support operator : " + fieldName);
            case 'E':
            case 'e':
                jSONReader.readFieldNameHashCodeUnquote();
                String fieldName2 = jSONReader.getFieldName();
                if ("ends".equalsIgnoreCase(fieldName2)) {
                    jSONReader.readFieldNameHashCodeUnquote();
                    String fieldName3 = jSONReader.getFieldName();
                    if (!"with".equalsIgnoreCase(fieldName3)) {
                        throw new JSONException("not support operator : " + fieldName3);
                    }
                } else if (!"endsWith".equalsIgnoreCase(fieldName2)) {
                    throw new JSONException("not support operator : " + fieldName2);
                }
                return JSONPathFilter.Operator.ENDS_WITH;
            case 'I':
            case 'i':
                jSONReader.readFieldNameHashCodeUnquote();
                String fieldName4 = jSONReader.getFieldName();
                if ("in".equalsIgnoreCase(fieldName4)) {
                    return JSONPathFilter.Operator.IN;
                }
                if ("is".equalsIgnoreCase(fieldName4)) {
                    return JSONPathFilter.Operator.EQ;
                }
                throw new JSONException("not support operator : " + fieldName4);
            case 'L':
            case 'l':
                jSONReader.readFieldNameHashCodeUnquote();
                String fieldName5 = jSONReader.getFieldName();
                if ("like".equalsIgnoreCase(fieldName5)) {
                    return JSONPathFilter.Operator.LIKE;
                }
                throw new JSONException("not support operator : " + fieldName5);
            case 'N':
            case 'n':
                jSONReader.readFieldNameHashCodeUnquote();
                String fieldName6 = jSONReader.getFieldName();
                if ("nin".equalsIgnoreCase(fieldName6)) {
                    return JSONPathFilter.Operator.NOT_IN;
                }
                if (!"not".equalsIgnoreCase(fieldName6)) {
                    throw new JSONException("not support operator : " + fieldName6);
                }
                jSONReader.readFieldNameHashCodeUnquote();
                String fieldName7 = jSONReader.getFieldName();
                if ("like".equalsIgnoreCase(fieldName7)) {
                    return JSONPathFilter.Operator.NOT_LIKE;
                }
                if ("rlike".equalsIgnoreCase(fieldName7)) {
                    return JSONPathFilter.Operator.NOT_RLIKE;
                }
                if ("in".equalsIgnoreCase(fieldName7)) {
                    return JSONPathFilter.Operator.NOT_IN;
                }
                if ("between".equalsIgnoreCase(fieldName7)) {
                    return JSONPathFilter.Operator.NOT_BETWEEN;
                }
                throw new JSONException("not support operator : " + fieldName7);
            case 'R':
            case 'r':
                jSONReader.readFieldNameHashCodeUnquote();
                String fieldName8 = jSONReader.getFieldName();
                if ("rlike".equalsIgnoreCase(fieldName8)) {
                    return JSONPathFilter.Operator.RLIKE;
                }
                throw new JSONException("not support operator : " + fieldName8);
            case 'S':
            case 's':
                jSONReader.readFieldNameHashCodeUnquote();
                String fieldName9 = jSONReader.getFieldName();
                if ("starts".equalsIgnoreCase(fieldName9)) {
                    jSONReader.readFieldNameHashCodeUnquote();
                    String fieldName10 = jSONReader.getFieldName();
                    if (!"with".equalsIgnoreCase(fieldName10)) {
                        throw new JSONException("not support operator : " + fieldName10);
                    }
                } else if (!"startsWith".equalsIgnoreCase(fieldName9)) {
                    throw new JSONException("not support operator : " + fieldName9);
                }
                return JSONPathFilter.Operator.STARTS_WITH;
            default:
                jSONReader.readFieldNameHashCodeUnquote();
                throw new JSONException("not support operator : " + jSONReader.getFieldName());
        }
    }

    static final class PreviousPath extends JSONPath {
        static final PreviousPath INSTANCE = new PreviousPath("#-1");

        @Override // com.alibaba.fastjson2.JSONPath
        public boolean isPrevious() {
            return true;
        }

        PreviousPath(String str) {
            super(str, new Feature[0]);
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public boolean isRef() {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public boolean contains(Object obj) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public Object eval(Object obj) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public Object extract(JSONReader jSONReader) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public String extractScalar(JSONReader jSONReader) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void set(Object obj, Object obj2) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void set(Object obj, Object obj2, JSONReader.Feature... featureArr) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void setCallback(Object obj, BiFunction biFunction) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public JSONPath getParent() {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void setInt(Object obj, int i) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void setLong(Object obj, long j) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public boolean remove(Object obj) {
            throw new JSONException("unsupported operation");
        }
    }

    static final class RootPath extends JSONPath {
        static final RootPath INSTANCE = new RootPath();

        @Override // com.alibaba.fastjson2.JSONPath
        public boolean contains(Object obj) {
            return false;
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public Object eval(Object obj) {
            return obj;
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public JSONPath getParent() {
            return null;
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public boolean isRef() {
            return true;
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public boolean remove(Object obj) {
            return false;
        }

        private RootPath() {
            super("$", new Feature[0]);
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public Object extract(JSONReader jSONReader) {
            if (jSONReader == null) {
                return null;
            }
            return jSONReader.readAny();
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public String extractScalar(JSONReader jSONReader) {
            return JSON.toJSONString(jSONReader.readAny());
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void set(Object obj, Object obj2) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void set(Object obj, Object obj2, JSONReader.Feature... featureArr) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void setCallback(Object obj, BiFunction biFunction) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void setInt(Object obj, int i) {
            throw new JSONException("unsupported operation");
        }

        @Override // com.alibaba.fastjson2.JSONPath
        public void setLong(Object obj, long j) {
            throw new JSONException("unsupported operation");
        }
    }

    static final class Context {
        final JSONPathSegment current;
        boolean eval;
        final JSONPathSegment next;
        final Context parent;
        final JSONPath path;
        final long readerFeatures;
        Object root;
        Object value;

        Context(JSONPath jSONPath, Context context, JSONPathSegment jSONPathSegment, JSONPathSegment jSONPathSegment2, long j) {
            this.path = jSONPath;
            this.current = jSONPathSegment;
            this.next = jSONPathSegment2;
            this.parent = context;
            this.readerFeatures = j;
        }
    }

    static class Sequence {
        final List values;

        public Sequence(List list) {
            this.values = list;
        }
    }

    public enum Feature {
        AlwaysReturnList(1),
        NullOnError(2),
        KeepNullValue(4);

        public final long mask;

        Feature(long j) {
            this.mask = j;
        }
    }
}
