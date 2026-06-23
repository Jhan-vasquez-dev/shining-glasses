package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderImplEnum;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class JSONObject extends LinkedHashMap<String, Object> implements InvocationHandler {
    static final long NONE_DIRECT_FEATURES = ((JSONWriter.Feature.ReferenceDetection.mask | JSONWriter.Feature.PrettyFormat.mask) | JSONWriter.Feature.NotWriteEmptyArray.mask) | JSONWriter.Feature.NotWriteDefaultValue.mask;
    static ObjectReader<JSONArray> arrayReader = null;
    private static final long serialVersionUID = 1;

    public JSONObject() {
    }

    public JSONObject(int i) {
        super(i);
    }

    public JSONObject(int i, float f) {
        super(i, f);
    }

    public JSONObject(int i, float f, boolean z) {
        super(i, f, z);
    }

    public JSONObject(Map map) {
        super(map);
    }

    public Object get(String str) {
        return super.get((Object) str);
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Object get(Object obj) {
        Object obj2;
        return (((obj instanceof Number) || (obj instanceof Character) || (obj instanceof Boolean) || (obj instanceof UUID)) && (obj2 = super.get((Object) obj.toString())) != null) ? obj2 : super.get(obj);
    }

    public Object getByPath(String str) {
        JSONPath jSONPathOf = JSONPath.of(str);
        if (jSONPathOf instanceof JSONPathSingleName) {
            return get(((JSONPathSingleName) jSONPathOf).name);
        }
        return jSONPathOf.eval(this);
    }

    public boolean containsKey(String str) {
        return super.containsKey((Object) str);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        if ((obj instanceof Number) || (obj instanceof Character) || (obj instanceof Boolean) || (obj instanceof UUID)) {
            return super.containsKey(obj) || super.containsKey((Object) obj.toString());
        }
        return super.containsKey(obj);
    }

    public Object getOrDefault(String str, Object obj) {
        return super.getOrDefault((Object) str, obj);
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.Map
    public Object getOrDefault(Object obj, Object obj2) {
        if ((obj instanceof Number) || (obj instanceof Character) || (obj instanceof Boolean) || (obj instanceof UUID)) {
            return super.getOrDefault((Object) obj.toString(), obj2);
        }
        return super.getOrDefault(obj, obj2);
    }

    @Deprecated
    public void forEchArrayObject(String str, Consumer<JSONObject> consumer) {
        forEachArrayObject(str, consumer);
    }

    public void forEachArrayObject(String str, Consumer<JSONObject> consumer) {
        JSONArray jSONArray = getJSONArray(str);
        if (jSONArray == null) {
            return;
        }
        for (int i = 0; i < jSONArray.size(); i++) {
            consumer.accept(jSONArray.getJSONObject(i));
        }
    }

    public JSONArray getJSONArray(String str) {
        Object obj = super.get((Object) str);
        JSONArray jSONArray = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        if (obj instanceof JSONObject) {
            return JSONArray.of(obj);
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            if (str2.charAt(0) != '[') {
                return JSONArray.of((Object) str2);
            }
            JSONReader jSONReaderOf = JSONReader.of(str2);
            if (arrayReader == null) {
                arrayReader = jSONReaderOf.getObjectReader(JSONArray.class);
            }
            return arrayReader.readObject(jSONReaderOf, null, null, 0L);
        }
        if (obj instanceof Collection) {
            JSONArray jSONArray2 = new JSONArray((Collection<?>) obj);
            put(str, jSONArray2);
            return jSONArray2;
        }
        if (obj instanceof Object[]) {
            return JSONArray.of((Object[]) obj);
        }
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            jSONArray = new JSONArray(length);
            for (int i = 0; i < length; i++) {
                jSONArray.add(Array.get(obj, i));
            }
        }
        return jSONArray;
    }

    public <T> List<T> getList(String str, Class<T> cls, JSONReader.Feature... featureArr) {
        JSONArray jSONArray = getJSONArray(str);
        if (jSONArray == null) {
            return null;
        }
        return jSONArray.toList(cls, featureArr);
    }

    public JSONObject getJSONObject(String str) {
        Object obj = super.get((Object) str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.isEmpty() || "null".equalsIgnoreCase(str2)) {
                return null;
            }
            return JSONFactory.OBJECT_READER.readObject(JSONReader.of(str2), null, null, 0L);
        }
        if (obj instanceof Map) {
            JSONObject jSONObject = new JSONObject((Map) obj);
            put(str, jSONObject);
            return jSONObject;
        }
        ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter((Class) obj.getClass());
        if (objectWriter instanceof ObjectWriterAdapter) {
            return ((ObjectWriterAdapter) objectWriter).toJSONObject(obj);
        }
        return null;
    }

    public String getString(String str) {
        Object obj = super.get((Object) str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Date) {
            return DateUtils.toString(((Date) obj).getTime(), false, DateUtils.DEFAULT_ZONE_ID);
        }
        if ((obj instanceof Boolean) || (obj instanceof Character) || (obj instanceof Number) || (obj instanceof UUID) || (obj instanceof Enum) || (obj instanceof TemporalAccessor)) {
            return obj.toString();
        }
        return JSON.toJSONString(obj);
    }

    public Double getDouble(String str) {
        Object obj = super.get((Object) str);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Double");
    }

    public double getDoubleValue(String str) {
        Object obj = super.get((Object) str);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to double value");
    }

    public Float getFloat(String str) {
        Object obj = super.get((Object) str);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Float");
    }

    public float getFloatValue(String str) {
        Object obj = super.get((Object) str);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to float value");
    }

    public Long getLong(String str) {
        Object obj = super.get((Object) str);
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
        if (obj instanceof Boolean) {
            if (((Boolean) obj).booleanValue()) {
                return Long.valueOf(serialVersionUID);
            }
            return 0L;
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Long");
    }

    public long getLongValue(String str) {
        return getLongValue(str, 0L);
    }

    public long getLongValue(String str, long j) {
        Object obj = super.get((Object) str);
        if (obj != null) {
            if (obj instanceof Number) {
                return ((Number) obj).longValue();
            }
            if (obj instanceof String) {
                String str2 = (String) obj;
                if (!str2.isEmpty() && !"null".equalsIgnoreCase(str2)) {
                    if (str2.indexOf(46) != -1) {
                        return (long) Double.parseDouble(str2);
                    }
                    return Long.parseLong(str2);
                }
            } else {
                throw new JSONException("Can not cast '" + obj.getClass() + "' to long value");
            }
        }
        return j;
    }

    public Integer getInteger(String str) {
        Object obj = super.get((Object) str);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Integer");
    }

    public int getIntValue(String str) {
        return getIntValue(str, 0);
    }

    public int getIntValue(String str, int i) {
        Object obj = super.get((Object) str);
        if (obj != null) {
            if (obj instanceof Number) {
                return ((Number) obj).intValue();
            }
            if (obj instanceof String) {
                String str2 = (String) obj;
                if (!str2.isEmpty() && !"null".equalsIgnoreCase(str2)) {
                    if (str2.indexOf(46) != -1) {
                        return (int) Double.parseDouble(str2);
                    }
                    return Integer.parseInt(str2);
                }
            } else {
                throw new JSONException("Can not cast '" + obj.getClass() + "' to int value");
            }
        }
        return i;
    }

    public Short getShort(String str) {
        Object obj = super.get((Object) str);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to short");
    }

    public short getShortValue(String str) {
        Short sh = getShort(str);
        if (sh == null) {
            return (short) 0;
        }
        return sh.shortValue();
    }

    public Byte getByte(String str) {
        Object obj = super.get((Object) str);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to byte");
    }

    public byte getByteValue(String str) {
        Byte b = getByte(str);
        if (b == null) {
            return (byte) 0;
        }
        return b.byteValue();
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
            return Base64.getDecoder().decode((String) obj);
        }
        throw new JSONException("can not cast to byte[], value : " + obj);
    }

    public Boolean getBoolean(String str) {
        Object obj = super.get((Object) str);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to boolean");
    }

    public boolean getBooleanValue(String str) {
        Boolean bool = getBoolean(str);
        return bool != null && bool.booleanValue();
    }

    public boolean getBooleanValue(String str, boolean z) {
        Boolean bool = getBoolean(str);
        return bool == null ? z : bool.booleanValue();
    }

    public BigInteger getBigInteger(String str) {
        Object obj = super.get((Object) str);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to BigInteger");
    }

    public BigDecimal getBigDecimal(String str) {
        Object obj = super.get((Object) str);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to BigDecimal");
    }

    public Date getDate(String str) {
        Object obj = super.get((Object) str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Date) {
            return (Date) obj;
        }
        if (obj instanceof String) {
            return DateUtils.parseDate((String) obj);
        }
        if (obj instanceof Number) {
            return new Date(((Number) obj).longValue());
        }
        return TypeUtils.toDate(obj);
    }

    public Date getDate(String str, Date date) {
        Date date2 = getDate(str);
        return date2 == null ? date : date2;
    }

    public Instant getInstant(String str) {
        Object obj = super.get((Object) str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Instant) {
            return (Instant) obj;
        }
        if (obj instanceof Number) {
            long jLongValue = ((Number) obj).longValue();
            if (jLongValue == 0) {
                return null;
            }
            return Instant.ofEpochMilli(jLongValue);
        }
        return TypeUtils.toInstant(obj);
    }

    public LocalDate getLocalDate(String str) {
        return getLocalDate(str, null);
    }

    public LocalDate getLocalDate(String str, LocalDate localDate) {
        Object obj = super.get((Object) str);
        if (obj == null) {
            return localDate;
        }
        if (obj instanceof LocalDate) {
            return (LocalDate) obj;
        }
        return (LocalDate) TypeUtils.cast(obj, LocalDate.class);
    }

    public LocalTime getLocalTime(String str) {
        return getLocalTime(str, null);
    }

    public LocalTime getLocalTime(String str, LocalTime localTime) {
        Object obj = super.get((Object) str);
        if (obj == null) {
            return localTime;
        }
        if (obj instanceof LocalTime) {
            return (LocalTime) obj;
        }
        return (LocalTime) TypeUtils.cast(obj, LocalTime.class);
    }

    public OffsetTime getOffsetTime(String str) {
        return getOffsetTime(str, null);
    }

    public OffsetTime getOffsetTime(String str, OffsetTime offsetTime) {
        Object obj = super.get((Object) str);
        if (obj == null) {
            return offsetTime;
        }
        if (obj instanceof OffsetTime) {
            return (OffsetTime) obj;
        }
        return (OffsetTime) TypeUtils.cast(obj, OffsetTime.class);
    }

    public LocalDateTime getLocalDateTime(String str) {
        return getLocalDateTime(str, null);
    }

    public LocalDateTime getLocalDateTime(String str, LocalDateTime localDateTime) {
        Object obj = super.get((Object) str);
        if (obj == null) {
            return localDateTime;
        }
        if (obj instanceof LocalDateTime) {
            return (LocalDateTime) obj;
        }
        return (LocalDateTime) TypeUtils.cast(obj, LocalDateTime.class);
    }

    public OffsetDateTime getOffsetDateTime(String str) {
        return getOffsetDateTime(str, null);
    }

    public OffsetDateTime getOffsetDateTime(String str, OffsetDateTime offsetDateTime) {
        Object obj = super.get((Object) str);
        if (obj == null) {
            return offsetDateTime;
        }
        if (obj instanceof OffsetDateTime) {
            return (OffsetDateTime) obj;
        }
        return (OffsetDateTime) TypeUtils.cast(obj, OffsetDateTime.class);
    }

    public ZonedDateTime getZonedDateTime(String str) {
        return getZonedDateTime(str, null);
    }

    public ZonedDateTime getZonedDateTime(String str, ZonedDateTime zonedDateTime) {
        Object obj = super.get((Object) str);
        if (obj == null) {
            return zonedDateTime;
        }
        if (obj instanceof ZonedDateTime) {
            return (ZonedDateTime) obj;
        }
        return (ZonedDateTime) TypeUtils.cast(obj, ZonedDateTime.class);
    }

    @Override // java.util.AbstractMap
    public String toString() {
        JSONWriter jSONWriterOf = JSONWriter.of();
        try {
            jSONWriterOf.setRootObject(this);
            jSONWriterOf.write(this);
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } catch (Throwable th) {
            if (jSONWriterOf != null) {
                try {
                    jSONWriterOf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public String toString(JSONWriter.Feature... featureArr) {
        JSONWriter jSONWriterOf = JSONWriter.of(featureArr);
        try {
            jSONWriterOf.setRootObject(this);
            jSONWriterOf.write(this);
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } catch (Throwable th) {
            if (jSONWriterOf != null) {
                try {
                    jSONWriterOf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public String toJSONString(JSONWriter.Feature... featureArr) {
        return toString(featureArr);
    }

    public static String toJSONString(Object obj, JSONWriter.Feature... featureArr) {
        return JSON.toJSONString(obj, featureArr);
    }

    public byte[] toJSONBBytes(JSONWriter.Feature... featureArr) {
        JSONWriter jSONWriterOfJSONB = JSONWriter.ofJSONB(featureArr);
        try {
            jSONWriterOfJSONB.setRootObject(this);
            jSONWriterOfJSONB.write(this);
            byte[] bytes = jSONWriterOfJSONB.getBytes();
            if (jSONWriterOfJSONB != null) {
                jSONWriterOfJSONB.close();
            }
            return bytes;
        } catch (Throwable th) {
            if (jSONWriterOfJSONB != null) {
                try {
                    jSONWriterOfJSONB.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public <T> T to(Function<JSONObject, T> function) {
        return function.apply(this);
    }

    public <T> T to(Type type, JSONReader.Feature... featureArr) {
        long j = JSONFactory.defaultReaderFeatures;
        boolean z = false;
        for (JSONReader.Feature feature : featureArr) {
            if (feature == JSONReader.Feature.FieldBased) {
                z = true;
            }
            j |= feature.mask;
        }
        if (type == String.class) {
            return (T) toString();
        }
        return (T) JSONFactory.getDefaultObjectReaderProvider().getObjectReader(type, z).createInstance(this, j);
    }

    public <T> T to(TypeReference<T> typeReference, JSONReader.Feature... featureArr) {
        return (T) to(typeReference.getType(), featureArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T to(Class<T> cls, JSONReader.Feature... featureArr) {
        long jOf = JSONFactory.defaultReaderFeatures | JSONReader.Feature.of(featureArr);
        boolean zIsEnabled = JSONReader.Feature.FieldBased.isEnabled(jOf);
        if (cls == String.class) {
            return (T) toString();
        }
        if (cls == JSON.class) {
            return this;
        }
        if (cls == Void.class || cls == Void.TYPE) {
            return null;
        }
        return (T) JSONFactory.getDefaultObjectReaderProvider().getObjectReader(cls, zIsEnabled).createInstance(this, jOf);
    }

    public void copyTo(Object obj, JSONReader.Feature... featureArr) {
        long jOf = JSONFactory.defaultReaderFeatures | JSONReader.Feature.of(featureArr);
        boolean zIsEnabled = JSONReader.Feature.FieldBased.isEnabled(jOf);
        JSONFactory.getDefaultObjectReaderProvider().getObjectReader(obj.getClass(), zIsEnabled).accept(obj, this, jOf);
    }

    public <T> T toJavaObject(Class<T> cls, JSONReader.Feature... featureArr) {
        return (T) to((Class) cls, featureArr);
    }

    public <T> T toJavaObject(Type type, JSONReader.Feature... featureArr) {
        return (T) to(type, featureArr);
    }

    public <T> T toJavaObject(TypeReference<T> typeReference, JSONReader.Feature... featureArr) {
        return (T) to(typeReference, featureArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getObject(String str, Class<T> cls, JSONReader.Feature... featureArr) throws Throwable {
        T t = (T) super.get((Object) str);
        ObjectReader objectReader = null;
        if (t == 0) {
            return null;
        }
        if (cls != Object.class || featureArr.length != 0) {
            int length = featureArr.length;
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (featureArr[i] == JSONReader.Feature.FieldBased) {
                    z = true;
                    break;
                }
                i++;
            }
            Class<?> cls2 = t.getClass();
            ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
            Function typeConvert = defaultObjectReaderProvider.getTypeConvert(cls2, cls);
            if (typeConvert != null) {
                return (T) typeConvert.apply(t);
            }
            if (t instanceof Map) {
                return (T) defaultObjectReaderProvider.getObjectReader(cls, z).createInstance((Map) t, featureArr);
            }
            if (t instanceof Collection) {
                return (T) defaultObjectReaderProvider.getObjectReader(cls, z).createInstance((Collection) t, featureArr);
            }
            Class<?> mapping = TypeUtils.getMapping(cls);
            if (!mapping.isInstance(t)) {
                if (t instanceof String) {
                    String str2 = (String) t;
                    if (str2.isEmpty() || "null".equals(str2)) {
                        return null;
                    }
                    if (mapping.isEnum()) {
                        objectReader = defaultObjectReaderProvider.getObjectReader(mapping, z);
                        if (objectReader instanceof ObjectReaderImplEnum) {
                            return (T) ((ObjectReaderImplEnum) objectReader).getEnumByHashCode(Fnv.hashCode64(str2));
                        }
                    }
                }
                String jSONString = JSON.toJSONString(t);
                JSONReader jSONReaderOf = JSONReader.of(jSONString);
                jSONReaderOf.context.config(featureArr);
                if (objectReader == null) {
                    objectReader = defaultObjectReaderProvider.getObjectReader(mapping, z);
                }
                T t2 = (T) objectReader.readObject(jSONReaderOf, null, null, 0L);
                if (jSONReaderOf.isEnd()) {
                    return t2;
                }
                throw new JSONException("not support input " + jSONString);
            }
        }
        return t;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getObject(String str, Type type, JSONReader.Feature... featureArr) {
        T t = (T) super.get((Object) str);
        if (t == 0) {
            return null;
        }
        if (type != Object.class || featureArr.length != 0) {
            int length = featureArr.length;
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (featureArr[i] == JSONReader.Feature.FieldBased) {
                    z = true;
                    break;
                }
                i++;
            }
            Class<?> cls = t.getClass();
            ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
            Function typeConvert = defaultObjectReaderProvider.getTypeConvert(cls, type);
            if (typeConvert != null) {
                return (T) typeConvert.apply(t);
            }
            if (t instanceof Map) {
                return (T) defaultObjectReaderProvider.getObjectReader(type, z).createInstance((Map) t, featureArr);
            }
            if (t instanceof Collection) {
                return (T) defaultObjectReaderProvider.getObjectReader(type, z).createInstance((Collection) t, featureArr);
            }
            if (!(type instanceof Class) || !((Class) type).isInstance(t)) {
                if (t instanceof String) {
                    String str2 = (String) t;
                    if (str2.isEmpty() || "null".equals(str2)) {
                        return null;
                    }
                }
                JSONReader jSONReaderOf = JSONReader.of(JSON.toJSONString(t));
                jSONReaderOf.context.config(featureArr);
                return (T) defaultObjectReaderProvider.getObjectReader(type, z).readObject(jSONReaderOf, null, null, 0L);
            }
        }
        return t;
    }

    public <T> T getObject(String str, TypeReference<T> typeReference, JSONReader.Feature... featureArr) {
        return (T) getObject(str, typeReference.type, featureArr);
    }

    public <T> T getObject(String str, Function<JSONObject, T> function) {
        JSONObject jSONObject = getJSONObject(str);
        if (jSONObject == null) {
            return null;
        }
        return function.apply(jSONObject);
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x0138  */
    @Override // java.lang.reflect.InvocationHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object invoke(java.lang.Object r11, java.lang.reflect.Method r12, java.lang.Object[] r13) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 621
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONObject.invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]):java.lang.Object");
    }

    private String getJSONFieldName(Method method) {
        String strName = null;
        for (Annotation annotation : BeanUtils.getAnnotations(method)) {
            Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
            JSONField jSONField = (JSONField) BeanUtils.findAnnotation(annotation, JSONField.class);
            Objects.nonNull(jSONField);
            if (jSONField != null) {
                strName = jSONField.name();
                if (strName.isEmpty()) {
                    strName = null;
                }
            } else if ("com.alibaba.fastjson.annotation.JSONField".equals(clsAnnotationType.getName())) {
                NameConsumer nameConsumer = new NameConsumer(annotation);
                BeanUtils.annotationMethods(clsAnnotationType, nameConsumer);
                if (nameConsumer.name != null) {
                    strName = nameConsumer.name;
                }
            }
        }
        return strName;
    }

    public JSONArray putArray(String str) {
        JSONArray jSONArray = new JSONArray();
        put(str, jSONArray);
        return jSONArray;
    }

    public JSONObject putObject(String str) {
        JSONObject jSONObject = new JSONObject();
        put(str, jSONObject);
        return jSONObject;
    }

    static class NameConsumer implements Consumer<Method> {
        final Annotation annotation;
        String name;

        NameConsumer(Annotation annotation) {
            this.annotation = annotation;
        }

        @Override // java.util.function.Consumer
        public void accept(Method method) {
            if ("name".equals(method.getName())) {
                try {
                    String str = (String) method.invoke(this.annotation, new Object[0]);
                    if (str.isEmpty()) {
                        return;
                    }
                    this.name = str;
                } catch (IllegalAccessException | InvocationTargetException unused) {
                }
            }
        }
    }

    public JSONObject fluentPut(String str, Object obj) {
        put(str, obj);
        return this;
    }

    public boolean isValid(JSONSchema jSONSchema) {
        return jSONSchema.isValid(this);
    }

    static void nameFilter(Iterable<?> iterable, NameFilter nameFilter) {
        for (Object obj : iterable) {
            if (obj instanceof JSONObject) {
                ((JSONObject) obj).nameFilter(nameFilter);
            } else if (obj instanceof Iterable) {
                nameFilter((Iterable<?>) obj, nameFilter);
            }
        }
    }

    static void nameFilter(Map map, NameFilter nameFilter) {
        String str;
        String strProcess;
        Iterator it = map.entrySet().iterator();
        JSONObject jSONObject = null;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JSONObject) {
                ((JSONObject) value).nameFilter(nameFilter);
            } else if (value instanceof Iterable) {
                nameFilter((Iterable<?>) value, nameFilter);
            }
            if ((key instanceof String) && (strProcess = nameFilter.process(map, (str = (String) key), value)) != null && !strProcess.equals(str)) {
                if (jSONObject == null) {
                    jSONObject = new JSONObject();
                }
                jSONObject.put(strProcess, value);
                it.remove();
            }
        }
        if (jSONObject != null) {
            map.putAll(jSONObject);
        }
    }

    static void valueFilter(Iterable<?> iterable, ValueFilter valueFilter) {
        for (Object obj : iterable) {
            if (obj instanceof Map) {
                valueFilter((Map) obj, valueFilter);
            } else if (obj instanceof Iterable) {
                valueFilter((Iterable<?>) obj, valueFilter);
            }
        }
    }

    static void valueFilter(Map map, ValueFilter valueFilter) {
        Object objApply;
        for (Map.Entry entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                valueFilter((Map) value, valueFilter);
            } else if (value instanceof Iterable) {
                valueFilter((Iterable<?>) value, valueFilter);
            }
            if ((key instanceof String) && (objApply = valueFilter.apply(map, (String) key, value)) != value) {
                entry.setValue(objApply);
            }
        }
    }

    public void valueFilter(ValueFilter valueFilter) {
        valueFilter(this, valueFilter);
    }

    public void nameFilter(NameFilter nameFilter) {
        nameFilter(this, nameFilter);
    }

    @Override // java.util.HashMap, java.util.AbstractMap
    public JSONObject clone() {
        return new JSONObject(this);
    }

    public Object eval(JSONPath jSONPath) {
        return jSONPath.eval(this);
    }

    public int getSize(String str) {
        Object obj = get(str);
        if (obj instanceof Map) {
            return ((Map) obj).size();
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        return 0;
    }

    public static JSONObject of() {
        return new JSONObject();
    }

    public static JSONObject of(String str, Object obj) {
        JSONObject jSONObject = new JSONObject(1, 1.0f);
        jSONObject.put(str, obj);
        return jSONObject;
    }

    public static JSONObject of(String str, Object obj, String str2, Object obj2) {
        JSONObject jSONObject = new JSONObject(2, 1.0f);
        jSONObject.put(str, obj);
        jSONObject.put(str2, obj2);
        return jSONObject;
    }

    public static JSONObject of(String str, Object obj, String str2, Object obj2, String str3, Object obj3) {
        JSONObject jSONObject = new JSONObject(3);
        jSONObject.put(str, obj);
        jSONObject.put(str2, obj2);
        jSONObject.put(str3, obj3);
        return jSONObject;
    }

    public static JSONObject of(String str, Object obj, String str2, Object obj2, String str3, Object obj3, String str4, Object obj4) {
        JSONObject jSONObject = new JSONObject(4, 1.0f);
        jSONObject.put(str, obj);
        jSONObject.put(str2, obj2);
        jSONObject.put(str3, obj3);
        jSONObject.put(str4, obj4);
        return jSONObject;
    }

    public static JSONObject of(String str, Object obj, String str2, Object obj2, String str3, Object obj3, String str4, Object obj4, String str5, Object obj5) {
        JSONObject jSONObject = new JSONObject(5);
        jSONObject.put(str, obj);
        jSONObject.put(str2, obj2);
        jSONObject.put(str3, obj3);
        jSONObject.put(str4, obj4);
        jSONObject.put(str5, obj5);
        return jSONObject;
    }

    public static JSONObject of(String str, Object obj, String str2, Object obj2, String str3, Object obj3, String str4, Object obj4, String str5, Object obj5, Object... objArr) {
        JSONObject jSONObject = new JSONObject(5);
        jSONObject.put(str, obj);
        jSONObject.put(str2, obj2);
        jSONObject.put(str3, obj3);
        jSONObject.put(str4, obj4);
        jSONObject.put(str5, obj5);
        if (objArr != null && objArr.length > 0) {
            of(jSONObject, objArr);
        }
        return jSONObject;
    }

    private static JSONObject of(JSONObject jSONObject, Object... objArr) {
        if (objArr == null || objArr.length == 0) {
            throw new JSONException("The kvArray cannot be empty");
        }
        int length = objArr.length;
        if ((length & 1) == 1) {
            throw new JSONException("The length of kvArray cannot be odd");
        }
        boolean z = false;
        for (int i = 0; i < length; i += 2) {
            int i2 = i + 1;
            Object obj = objArr[i];
            if (!(obj instanceof String)) {
                throw new JSONException("The value corresponding to the even bit index of kvArray is key, which cannot be null and must be of type string");
            }
            String str = (String) obj;
            if (z) {
                if (jSONObject.containsKey(str)) {
                    throw new JSONException("The value corresponding to the even bit index of kvArray is key and cannot be duplicated");
                }
                jSONObject.put(str, objArr[i2]);
            } else {
                if (jSONObject.put(str, objArr[i2]) != null) {
                    throw new JSONException("The value corresponding to the even bit index of kvArray is key and cannot be duplicated");
                }
                z = objArr[i2] == null;
            }
        }
        return jSONObject;
    }

    public static <T> T parseObject(String str, Class<T> cls) {
        return (T) JSON.parseObject(str, (Class) cls);
    }

    public static <T> T parseObject(String str, Class<T> cls, JSONReader.Feature... featureArr) {
        return (T) JSON.parseObject(str, (Class) cls, featureArr);
    }

    public static <T> T parseObject(String str, Type type, JSONReader.Feature... featureArr) {
        return (T) JSON.parseObject(str, type, featureArr);
    }

    public static <T> T parseObject(String str, TypeReference<T> typeReference, JSONReader.Feature... featureArr) {
        return (T) JSON.parseObject(str, typeReference, featureArr);
    }

    public static JSONObject parseObject(String str) {
        return JSON.parseObject(str);
    }

    public static JSONObject parse(String str, JSONReader.Feature... featureArr) {
        return JSON.parseObject(str, featureArr);
    }

    public static JSONObject from(Object obj) {
        return (JSONObject) JSON.toJSON(obj);
    }

    public static JSONObject from(Object obj, JSONWriter.Feature... featureArr) {
        return (JSONObject) JSON.toJSON(obj, featureArr);
    }

    public boolean isArray(Object obj) {
        Object obj2 = super.get(obj);
        if (obj2 instanceof JSONArray) {
            return true;
        }
        return obj2 != null && obj2.getClass().isArray();
    }
}
