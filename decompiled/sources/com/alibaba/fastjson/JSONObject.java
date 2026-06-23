package com.alibaba.fastjson;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.util.Wrapper;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public class JSONObject extends JSON implements Map<String, Object>, Cloneable, Serializable, InvocationHandler, Wrapper {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    static ObjectReader<JSONArray> arrayReader = null;
    static ObjectReader<JSONObject> objectReader = null;
    private static final long serialVersionUID = 1;
    private final Map<String, Object> map;

    public JSONObject() {
        this(16, false);
    }

    public JSONObject(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        }
        this.map = map;
    }

    public JSONObject(boolean z) {
        this(16, z);
    }

    public JSONObject(int i) {
        this(i, false);
    }

    public JSONObject(int i, boolean z) {
        if (z) {
            this.map = new LinkedHashMap(i);
        } else {
            this.map = new HashMap(i);
        }
    }

    public static <T> T toJavaObject(JSON json, Class<T> cls) {
        return (T) TypeUtils.cast((Object) json, (Class) cls, JSONFactory.getDefaultObjectReaderProvider());
    }

    @Override // java.util.Map
    public int size() {
        return this.map.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        boolean zContainsKey = this.map.containsKey(obj);
        return (zContainsKey || !((obj instanceof Number) || (obj instanceof Character) || (obj instanceof Boolean) || (obj instanceof UUID))) ? zContainsKey : this.map.containsKey(obj.toString());
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.map.containsValue(obj);
    }

    @Override // java.util.Map
    public Object get(Object obj) {
        Object obj2 = this.map.get(obj);
        if (obj2 == null && ((obj instanceof Number) || (obj instanceof Boolean) || (obj instanceof Character))) {
            obj2 = this.map.get(obj.toString());
        }
        return adaptResult(obj2);
    }

    public JSONObject getJSONObject(String str) {
        Object obj = this.map.get(str);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str2);
            if (objectReader == null) {
                objectReader = jSONReaderOf.getObjectReader(JSONObject.class);
            }
            return objectReader.readObject(jSONReaderOf, null, null, 0L);
        }
        if (obj instanceof Map) {
            return new JSONObject((Map<String, Object>) obj);
        }
        if (obj == null) {
            return null;
        }
        ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter((Class) obj.getClass());
        if (objectWriter instanceof ObjectWriterAdapter) {
            return new JSONObject(((ObjectWriterAdapter) objectWriter).toJSONObject(obj));
        }
        return null;
    }

    public JSONArray getJSONArray(String str) {
        Object obj = this.map.get(str);
        if (obj == null || (obj instanceof JSONArray)) {
            return (JSONArray) obj;
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str2);
            if (arrayReader == null) {
                arrayReader = jSONReaderOf.getObjectReader(JSONArray.class);
            }
            return arrayReader.readObject(jSONReaderOf, null, null, 0L);
        }
        if (obj instanceof List) {
            return new JSONArray((List) obj);
        }
        return JSON.parseArray(JSON.toJSONString(obj));
    }

    public <T> T getObject(String str, Class<T> cls) {
        return (T) getObject(str, (Class) cls, new Feature[0]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getObject(String str, Class<T> cls, Feature... featureArr) {
        T t = (T) this.map.get(str);
        if (t == 0) {
            return null;
        }
        if ((cls == Object.class && (t instanceof JSONObject)) || (cls != Object.class && cls.isInstance(t))) {
            return t;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        Function typeConvert = defaultObjectReaderProvider.getTypeConvert(t.getClass(), cls);
        if (typeConvert != null) {
            return (T) typeConvert.apply(t);
        }
        if (t instanceof String) {
            String str2 = (String) t;
            if (str2.isEmpty() || "null".equals(str2)) {
                return null;
            }
        }
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(JSON.toJSONString(t), JSON.createReadContext(JSONFactory.getDefaultObjectReaderProvider(), JSON.DEFAULT_PARSER_FEATURE, featureArr));
        ObjectReader objectReader2 = defaultObjectReaderProvider.getObjectReader(cls, jSONReaderOf.getContext().isEnabled(JSONReader.Feature.FieldBased));
        String str3 = JSON.DEFAULT_DATE_FORMAT;
        if (!"yyyy-MM-dd HH:mm:ss".equals(str3)) {
            jSONReaderOf.getContext().setDateFormat(str3);
        }
        return (T) objectReader2.readObject(jSONReaderOf, null, null, 0L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getObject(String str, TypeReference typeReference) {
        Type type;
        Class cls;
        T t = (T) this.map.get(str);
        if (t == 0 || typeReference == null || (type = typeReference.getType()) == null) {
            return t;
        }
        if ((type instanceof Class) && (cls = (Class) type) != Object.class && cls.isInstance(t)) {
            return t;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        Function typeConvert = defaultObjectReaderProvider.getTypeConvert(t.getClass(), type);
        if (typeConvert != null) {
            return (T) typeConvert.apply(t);
        }
        if (t instanceof String) {
            String str2 = (String) t;
            if (str2.isEmpty() || "null".equals(str2)) {
                return null;
            }
        }
        String jSONString = JSON.toJSONString(t);
        ObjectReader objectReader2 = defaultObjectReaderProvider.getObjectReader(type);
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(jSONString);
        String str3 = JSON.DEFAULT_DATE_FORMAT;
        if (!"yyyy-MM-dd HH:mm:ss".equals(str3)) {
            jSONReaderOf.getContext().setDateFormat(str3);
        }
        return (T) objectReader2.readObject(jSONReaderOf, null, null, 0L);
    }

    public Boolean getBoolean(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof Number) {
            return Boolean.valueOf(((Number) obj).intValue() == 1);
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            return Boolean.valueOf("true".equalsIgnoreCase(str2) || "1".equals(str2));
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to Boolean");
    }

    public Byte getByte(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Byte.valueOf(((Number) obj).byteValue());
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            return Byte.valueOf(Byte.parseByte(str2));
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to Byte");
    }

    public byte[] getBytes(String str) {
        Object obj = get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        if (obj instanceof String) {
            return IOUtils.decodeBase64((String) obj);
        }
        throw new JSONException("can not cast to byte[], value : " + obj);
    }

    public <T> T getObject(String str, Type type) {
        return (T) getObject(str, type, new Feature[0]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getObject(String str, Type type, Feature... featureArr) {
        Class cls;
        T t = (T) this.map.get(str);
        if (t == 0) {
            return null;
        }
        if ((type instanceof Class) && (cls = (Class) type) != Object.class && cls.isInstance(t)) {
            return t;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        Function typeConvert = defaultObjectReaderProvider.getTypeConvert(t.getClass(), type);
        if (typeConvert != null) {
            return (T) typeConvert.apply(t);
        }
        if ((t instanceof String) && ((String) t).isEmpty()) {
            return null;
        }
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(JSON.toJSONString(t), JSON.createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
        ObjectReader objectReader2 = defaultObjectReaderProvider.getObjectReader(type, jSONReaderOf.getContext().isEnabled(JSONReader.Feature.FieldBased));
        String str2 = JSON.DEFAULT_DATE_FORMAT;
        if (!"yyyy-MM-dd HH:mm:ss".equals(str2)) {
            jSONReaderOf.getContext().setDateFormat(str2);
        }
        return (T) objectReader2.readObject(jSONReaderOf, null, null, 0L);
    }

    public boolean getBooleanValue(String str) {
        Boolean bool = TypeUtils.toBoolean(get(str));
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public byte getByteValue(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return (byte) 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return (byte) 0;
            }
            return Byte.parseByte(str2);
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to byte value");
    }

    public Short getShort(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Short) {
            return (Short) obj;
        }
        if (obj instanceof Number) {
            return Short.valueOf(((Number) obj).shortValue());
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            return Short.valueOf(Short.parseShort(str2));
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to Short");
    }

    public short getShortValue(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return (short) 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return (short) 0;
            }
            return Short.parseShort(str2);
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to short value");
    }

    public Integer getInteger(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof Number) {
            return Integer.valueOf(((Number) obj).intValue());
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            if (str2.indexOf(46) != -1) {
                return Integer.valueOf((int) Double.parseDouble(str2));
            }
            return Integer.valueOf(Integer.parseInt(str2));
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? 1 : 0;
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to Integer");
    }

    public int getIntValue(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return 0;
            }
            if (str2.indexOf(46) != -1) {
                return (int) Double.parseDouble(str2);
            }
            return Integer.parseInt(str2);
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to int value");
    }

    public Long getLong(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Number) {
            return Long.valueOf(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            if (str2.indexOf(46) != -1) {
                return Long.valueOf((long) Double.parseDouble(str2));
            }
            return Long.valueOf(Long.parseLong(str2));
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to Long");
    }

    public long getLongValue(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return 0L;
            }
            if (str2.indexOf(46) != -1) {
                return (long) Double.parseDouble(str2);
            }
            return Long.parseLong(str2);
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to long value");
    }

    public Float getFloat(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Float) {
            return (Float) obj;
        }
        if (obj instanceof Number) {
            return Float.valueOf(((Number) obj).floatValue());
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            return Float.valueOf(Float.parseFloat(str2));
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to Float");
    }

    public float getFloatValue(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return 0.0f;
        }
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return 0.0f;
            }
            return Float.parseFloat(str2);
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to float value");
    }

    public Double getDouble(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Double) {
            return (Double) obj;
        }
        if (obj instanceof Number) {
            return Double.valueOf(((Number) obj).doubleValue());
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            return Double.valueOf(Double.parseDouble(str2));
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to Double");
    }

    public double getDoubleValue(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return 0.0d;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return 0.0d;
            }
            return Double.parseDouble(str2);
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to double value");
    }

    public BigDecimal getBigDecimal(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            if (obj instanceof BigDecimal) {
                return (BigDecimal) obj;
            }
            if (obj instanceof BigInteger) {
                return new BigDecimal((BigInteger) obj);
            }
            if (obj instanceof Float) {
                return TypeUtils.toBigDecimal(((Float) obj).floatValue());
            }
            if (obj instanceof Double) {
                return TypeUtils.toBigDecimal(((Double) obj).doubleValue());
            }
            return BigDecimal.valueOf(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            return TypeUtils.toBigDecimal((String) obj);
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? BigDecimal.ONE : BigDecimal.ZERO;
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to BigDecimal");
    }

    public BigInteger getBigInteger(String str) {
        Object obj = this.map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigInteger) {
            return (BigInteger) obj;
        }
        if (obj instanceof Number) {
            if (obj instanceof BigDecimal) {
                return ((BigDecimal) obj).toBigInteger();
            }
            return BigInteger.valueOf(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            return new BigInteger(str2);
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? BigInteger.ONE : BigInteger.ZERO;
        }
        throw new com.alibaba.fastjson2.JSONException("Can not cast '" + obj.getClass() + "' to BigInteger");
    }

    public String getString(String str) {
        Object obj = get(str);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public Date getDate(String str) {
        return TypeUtils.toDate(get(str));
    }

    public java.sql.Date getSqlDate(String str) {
        return (java.sql.Date) TypeUtils.cast(get(str), java.sql.Date.class, JSONFactory.getDefaultObjectReaderProvider());
    }

    public Timestamp getTimestamp(String str) {
        return (Timestamp) TypeUtils.cast(get(str), Timestamp.class, JSONFactory.getDefaultObjectReaderProvider());
    }

    @Override // java.util.Map
    public Object put(String str, Object obj) {
        return this.map.put(str, obj);
    }

    public JSONObject fluentPut(String str, Object obj) {
        this.map.put(str, obj);
        return this;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends String, ? extends Object> map) {
        this.map.putAll(map);
    }

    public JSONObject fluentPutAll(Map<? extends String, ?> map) {
        this.map.putAll(map);
        return this;
    }

    @Override // java.util.Map
    public void clear() {
        this.map.clear();
    }

    public JSONObject fluentClear() {
        this.map.clear();
        return this;
    }

    @Override // java.util.Map
    public Object remove(Object obj) {
        return this.map.remove(obj);
    }

    public JSONObject fluentRemove(Object obj) {
        this.map.remove(obj);
        return this;
    }

    @Override // java.util.Map
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

    public Object clone() {
        Map map;
        if (this.map instanceof LinkedHashMap) {
            map = new LinkedHashMap(this.map);
        } else {
            map = new HashMap(this.map);
        }
        return new JSONObject((Map<String, Object>) map);
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String strName = null;
        if (parameterTypes.length == 1) {
            if ("equals".equals(method.getName())) {
                return Boolean.valueOf(equals(objArr[0]));
            }
            if (method.getReturnType() != Void.TYPE) {
                throw new com.alibaba.fastjson2.JSONException("illegal setter");
            }
            JSONField jSONField = (JSONField) method.getAnnotation(JSONField.class);
            String strName2 = (jSONField == null || jSONField.name().length() == 0) ? null : jSONField.name();
            if (strName2 == null) {
                String name = method.getName();
                if (!name.startsWith("set")) {
                    throw new com.alibaba.fastjson2.JSONException("illegal setter");
                }
                String strSubstring = name.substring(3);
                if (strSubstring.length() == 0) {
                    throw new com.alibaba.fastjson2.JSONException("illegal setter");
                }
                strName2 = Character.toLowerCase(strSubstring.charAt(0)) + strSubstring.substring(1);
            }
            this.map.put(strName2, objArr[0]);
            return null;
        }
        if (parameterTypes.length == 0) {
            if (method.getReturnType() == Void.TYPE) {
                throw new com.alibaba.fastjson2.JSONException("illegal getter");
            }
            JSONField jSONField2 = (JSONField) method.getAnnotation(JSONField.class);
            if (jSONField2 != null && jSONField2.name().length() != 0) {
                strName = jSONField2.name();
            }
            if (strName == null) {
                String name2 = method.getName();
                if (name2.startsWith("get")) {
                    String strSubstring2 = name2.substring(3);
                    if (strSubstring2.length() == 0) {
                        throw new com.alibaba.fastjson2.JSONException("illegal getter");
                    }
                    strName = Character.toLowerCase(strSubstring2.charAt(0)) + strSubstring2.substring(1);
                } else if (name2.startsWith("is")) {
                    String strSubstring3 = name2.substring(2);
                    if (strSubstring3.length() == 0) {
                        throw new com.alibaba.fastjson2.JSONException("illegal getter");
                    }
                    strName = Character.toLowerCase(strSubstring3.charAt(0)) + strSubstring3.substring(1);
                } else {
                    if (name2.startsWith("hashCode")) {
                        return Integer.valueOf(hashCode());
                    }
                    if (name2.startsWith("toString")) {
                        return toString();
                    }
                    throw new com.alibaba.fastjson2.JSONException("illegal getter");
                }
            }
            return com.alibaba.fastjson.util.TypeUtils.cast(this.map.get(strName), method.getGenericReturnType(), ParserConfig.getGlobalInstance());
        }
        throw new UnsupportedOperationException(method.toGenericString());
    }

    public Map<String, Object> getInnerMap() {
        return this.map;
    }

    @Override // com.alibaba.fastjson.JSON
    public <T> T toJavaObject(Type type) {
        if (type instanceof Class) {
            return (T) JSONFactory.getDefaultObjectReaderProvider().getObjectReader(type).createInstance(this, 0L);
        }
        return (T) com.alibaba.fastjson2.JSON.parseObject(com.alibaba.fastjson2.JSON.toJSONString(this), type);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson.JSON
    public <T> T toJavaObject(Class<T> cls) {
        return (cls != Map.class && (cls != Object.class || containsKey(JSON.DEFAULT_TYPE_KEY)) && !cls.isInstance(this)) ? (T) JSONFactory.getDefaultObjectReaderProvider().getObjectReader(cls).createInstance(this, JSONReader.Feature.SupportSmartMatch.mask) : this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T toJavaObject(Class<T> cls, ParserConfig parserConfig, int i) {
        return (cls != Map.class && (cls != Object.class || containsKey(JSON.DEFAULT_TYPE_KEY))) ? (T) com.alibaba.fastjson.util.TypeUtils.castToJavaBean(this, cls, parserConfig) : this;
    }

    public String toString() {
        return com.alibaba.fastjson2.JSON.toJSONString(this, JSONWriter.Feature.ReferenceDetection);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.util.Wrapper
    public <T> T unwrap(Class<T> cls) {
        return cls == Map.class ? (T) this.map : this;
    }

    static final class Creator implements Supplier<Map> {
        static final Creator INSTANCE = new Creator();

        Creator() {
        }

        @Override // java.util.function.Supplier
        public Map get() {
            return new JSONObject();
        }
    }
}
