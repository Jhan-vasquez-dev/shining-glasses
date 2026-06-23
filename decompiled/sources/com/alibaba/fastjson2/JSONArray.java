package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderImplEnum;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.lang.reflect.Array;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class JSONArray extends ArrayList<Object> {
    static ObjectWriter<JSONArray> arrayWriter = null;
    private static final long serialVersionUID = 1;

    public JSONArray() {
    }

    public JSONArray(int i) {
        super(i);
    }

    public JSONArray(Collection<?> collection) {
        super(collection);
    }

    public JSONArray(Object... objArr) {
        super(objArr.length);
        super.addAll(Arrays.asList(objArr));
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public Object set(int i, Object obj) {
        int size = super.size();
        if (i < 0) {
            int i2 = i + size;
            if (i2 < 0) {
                super.add(0, obj);
                return null;
            }
            return super.set(i2, obj);
        }
        if (i < size) {
            return super.set(i, obj);
        }
        if (i < size + 4096) {
            while (true) {
                int i3 = i - 1;
                if (i == size) {
                    break;
                }
                super.add(null);
                i = i3;
            }
            super.add(obj);
        }
        return null;
    }

    public JSONArray getJSONArray(int i) {
        Object obj = get(i);
        JSONArray jSONArray = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            if (str.charAt(0) != '[') {
                return of((Object) str);
            }
            return JSONFactory.ARRAY_READER.readObject(JSONReader.of(str), null, null, 0L);
        }
        if (obj instanceof Collection) {
            JSONArray jSONArray2 = new JSONArray((Collection<?>) obj);
            set(i, jSONArray2);
            return jSONArray2;
        }
        if (obj instanceof Object[]) {
            return of((Object[]) obj);
        }
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            jSONArray = new JSONArray(length);
            for (int i2 = 0; i2 < length; i2++) {
                jSONArray.add(Array.get(obj, i2));
            }
        }
        return jSONArray;
    }

    public JSONObject getJSONObject(int i) {
        Object obj = get(i);
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            return JSONFactory.OBJECT_READER.readObject(JSONReader.of(str), null, null, 0L);
        }
        if (obj instanceof Map) {
            JSONObject jSONObject = new JSONObject((Map) obj);
            set(i, jSONObject);
            return jSONObject;
        }
        ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter((Class) obj.getClass());
        if (objectWriter instanceof ObjectWriterAdapter) {
            return ((ObjectWriterAdapter) objectWriter).toJSONObject(obj);
        }
        return (JSONObject) JSON.toJSON(obj);
    }

    public String getString(int i) {
        Object obj = get(i);
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

    public Double getDouble(int i) {
        Object obj = get(i);
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
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            return Double.valueOf(Double.parseDouble(str));
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Double");
    }

    public double getDoubleValue(int i) {
        Object obj = get(i);
        if (obj == null) {
            return 0.0d;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return 0.0d;
            }
            return Double.parseDouble(str);
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to double value");
    }

    public Float getFloat(int i) {
        Object obj = get(i);
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
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            return Float.valueOf(Float.parseFloat(str));
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Float");
    }

    public float getFloatValue(int i) {
        Object obj = get(i);
        if (obj == null) {
            return 0.0f;
        }
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return 0.0f;
            }
            return Float.parseFloat(str);
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to float value");
    }

    public Long getLong(int i) {
        Object obj = get(i);
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
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            return Long.valueOf(Long.parseLong(str));
        }
        if (obj instanceof Boolean) {
            if (((Boolean) obj).booleanValue()) {
                return Long.valueOf(serialVersionUID);
            }
            return 0L;
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Long");
    }

    public long getLongValue(int i) {
        Object obj = get(i);
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return 0L;
            }
            return Long.parseLong(str);
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to long value");
    }

    public Integer getInteger(int i) {
        Object obj = get(i);
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
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            return Integer.valueOf(Integer.parseInt(str));
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? 1 : 0;
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Integer");
    }

    public int getIntValue(int i) {
        Object obj = get(i);
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return 0;
            }
            return Integer.parseInt(str);
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to int value");
    }

    public Short getShort(int i) {
        Object obj = get(i);
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
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            return Short.valueOf(Short.parseShort(str));
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Short");
    }

    public short getShortValue(int i) {
        Object obj = get(i);
        if (obj == null) {
            return (short) 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return (short) 0;
            }
            return Short.parseShort(str);
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to short value");
    }

    public Byte getByte(int i) {
        Object obj = get(i);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Byte.valueOf(((Number) obj).byteValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            return Byte.valueOf(Byte.parseByte(str));
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Byte");
    }

    public byte getByteValue(int i) {
        Object obj = get(i);
        if (obj == null) {
            return (byte) 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return (byte) 0;
            }
            return Byte.parseByte(str);
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to byte value");
    }

    public Boolean getBoolean(int i) {
        Object obj = get(i);
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
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            return Boolean.valueOf("true".equalsIgnoreCase(str) || "1".equals(str));
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Boolean");
    }

    public boolean getBooleanValue(int i) {
        Object obj = get(i);
        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue() == 1;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            return "true".equalsIgnoreCase(str) || "1".equals(str);
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to boolean value");
    }

    public BigInteger getBigInteger(int i) {
        Object obj = get(i);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            if (obj instanceof BigInteger) {
                return (BigInteger) obj;
            }
            if (obj instanceof BigDecimal) {
                return ((BigDecimal) obj).toBigInteger();
            }
            return BigInteger.valueOf(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            return new BigInteger(str);
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? BigInteger.ONE : BigInteger.ZERO;
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to BigInteger");
    }

    public BigDecimal getBigDecimal(int i) {
        Object obj = get(i);
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

    public Date getDate(int i) {
        Object obj = get(i);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Date) {
            return (Date) obj;
        }
        if (obj instanceof Number) {
            long jLongValue = ((Number) obj).longValue();
            if (jLongValue == 0) {
                return null;
            }
            return new Date(jLongValue);
        }
        return TypeUtils.toDate(obj);
    }

    public Date getDate(int i, Date date) {
        Date date2 = getDate(i);
        return date2 == null ? date : date2;
    }

    public Instant getInstant(int i) {
        Object obj = get(i);
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

    public LocalDate getLocalDate(int i) {
        return getLocalDate(i, null);
    }

    public LocalDate getLocalDate(int i, LocalDate localDate) {
        Object obj = super.get(i);
        if (obj == null) {
            return localDate;
        }
        if (obj instanceof LocalDate) {
            return (LocalDate) obj;
        }
        return (LocalDate) TypeUtils.cast(obj, LocalDate.class);
    }

    public LocalTime getLocalTime(int i) {
        return getLocalTime(i, null);
    }

    public LocalTime getLocalTime(int i, LocalTime localTime) {
        Object obj = super.get(i);
        if (obj == null) {
            return localTime;
        }
        if (obj instanceof LocalTime) {
            return (LocalTime) obj;
        }
        return (LocalTime) TypeUtils.cast(obj, LocalTime.class);
    }

    public OffsetTime getOffsetTime(int i) {
        return getOffsetTime(i, null);
    }

    public OffsetTime getOffsetTime(int i, OffsetTime offsetTime) {
        Object obj = super.get(i);
        if (obj == null) {
            return offsetTime;
        }
        if (obj instanceof OffsetTime) {
            return (OffsetTime) obj;
        }
        return (OffsetTime) TypeUtils.cast(obj, OffsetTime.class);
    }

    public LocalDateTime getLocalDateTime(int i) {
        return getLocalDateTime(i, null);
    }

    public LocalDateTime getLocalDateTime(int i, LocalDateTime localDateTime) {
        Object obj = super.get(i);
        if (obj == null) {
            return localDateTime;
        }
        if (obj instanceof LocalDateTime) {
            return (LocalDateTime) obj;
        }
        return (LocalDateTime) TypeUtils.cast(obj, LocalDateTime.class);
    }

    public OffsetDateTime getOffsetDateTime(int i) {
        return getOffsetDateTime(i, null);
    }

    public OffsetDateTime getOffsetDateTime(int i, OffsetDateTime offsetDateTime) {
        Object obj = super.get(i);
        if (obj == null) {
            return offsetDateTime;
        }
        if (obj instanceof OffsetDateTime) {
            return (OffsetDateTime) obj;
        }
        return (OffsetDateTime) TypeUtils.cast(obj, OffsetDateTime.class);
    }

    public ZonedDateTime getZonedDateTime(int i) {
        return getZonedDateTime(i, null);
    }

    public ZonedDateTime getZonedDateTime(int i, ZonedDateTime zonedDateTime) {
        Object obj = super.get(i);
        if (obj == null) {
            return zonedDateTime;
        }
        if (obj instanceof ZonedDateTime) {
            return (ZonedDateTime) obj;
        }
        return (ZonedDateTime) TypeUtils.cast(obj, ZonedDateTime.class);
    }

    @Override // java.util.AbstractCollection
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
            if ((jSONWriterOf.context.features & JSONObject.NONE_DIRECT_FEATURES) == 0) {
                jSONWriterOf.write(this);
            } else {
                jSONWriterOf.setRootObject(this);
                if (arrayWriter == null) {
                    arrayWriter = jSONWriterOf.getObjectWriter(JSONArray.class, JSONArray.class);
                }
                arrayWriter.write(jSONWriterOf, this, null, null, 0L);
            }
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } catch (Throwable th) {
            if (jSONWriterOf == null) {
                throw th;
            }
            try {
                jSONWriterOf.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
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

    public <T> T to(Type type) {
        return (T) to(type, 0L);
    }

    public <T> T to(Type type, long j) {
        if (type == String.class) {
            return (T) toString();
        }
        return (T) JSONFactory.getDefaultObjectReaderProvider().getObjectReader(type).createInstance(this, j);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T to(Class<T> cls) {
        if (cls == String.class) {
            return (T) toString();
        }
        return cls == JSON.class ? this : (T) JSONFactory.getDefaultObjectReaderProvider().getObjectReader(cls).createInstance(this);
    }

    @Deprecated
    public <T> T toJavaObject(Type type) {
        return (T) to(type);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> List<T> toList(Class<T> cls, JSONReader.Feature... featureArr) {
        long j = JSONFactory.defaultReaderFeatures;
        boolean z = false;
        for (JSONReader.Feature feature : featureArr) {
            j |= feature.mask;
            if (feature == JSONReader.Feature.FieldBased) {
                z = true;
            }
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        ObjectReader objectReader = defaultObjectReaderProvider.getObjectReader(cls, z);
        ArrayList arrayList = new ArrayList(size());
        for (int i = 0; i < size(); i++) {
            Object objCreateInstance = get(i);
            if ((objCreateInstance instanceof JSONObject) || (objCreateInstance instanceof Map)) {
                objCreateInstance = objectReader.createInstance((Map) objCreateInstance, j);
            } else {
                if (objCreateInstance != null && !cls.isInstance(objCreateInstance)) {
                    Class<?> cls2 = objCreateInstance.getClass();
                    Function typeConvert = defaultObjectReaderProvider.getTypeConvert(cls2, cls);
                    if (typeConvert != 0) {
                        arrayList.add(typeConvert.apply(objCreateInstance));
                    } else {
                        throw new JSONException(cls2 + " cannot be converted to " + cls);
                    }
                }
            }
            arrayList.add(objCreateInstance);
        }
        return arrayList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T[] toArray(Class<T> cls, JSONReader.Feature... featureArr) {
        long j = JSONFactory.defaultReaderFeatures;
        boolean z = false;
        for (JSONReader.Feature feature : featureArr) {
            j |= feature.mask;
            if (feature == JSONReader.Feature.FieldBased) {
                z = true;
            }
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        ObjectReader objectReader = defaultObjectReaderProvider.getObjectReader(cls, z);
        T[] tArr = (T[]) ((Object[]) Array.newInstance((Class<?>) cls, size()));
        for (int i = 0; i < size(); i++) {
            Object objCreateInstance = get(i);
            if ((objCreateInstance instanceof JSONObject) || (objCreateInstance instanceof Map)) {
                objCreateInstance = objectReader.createInstance((Map) objCreateInstance, j);
            } else {
                if (objCreateInstance != null && !cls.isInstance(objCreateInstance)) {
                    Class<?> cls2 = objCreateInstance.getClass();
                    Function typeConvert = defaultObjectReaderProvider.getTypeConvert(cls2, cls);
                    if (typeConvert != 0) {
                        tArr[i] = typeConvert.apply(objCreateInstance);
                    } else {
                        throw new JSONException(cls2 + " cannot be converted to " + cls);
                    }
                }
            }
            tArr[i] = objCreateInstance;
        }
        return tArr;
    }

    public <T> List<T> toJavaList(Class<T> cls, JSONReader.Feature... featureArr) {
        return toList(cls, featureArr);
    }

    public <T> T getObject(int i, Type type, JSONReader.Feature... featureArr) {
        T t = (T) get(i);
        if (t == null) {
            return null;
        }
        Class<?> cls = t.getClass();
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        Function typeConvert = defaultObjectReaderProvider.getTypeConvert(cls, type);
        if (typeConvert != null) {
            return (T) typeConvert.apply(t);
        }
        long j = JSONFactory.defaultReaderFeatures;
        boolean z = false;
        for (JSONReader.Feature feature : featureArr) {
            j |= feature.mask;
            if (feature == JSONReader.Feature.FieldBased) {
                z = true;
            }
        }
        if (t instanceof Map) {
            return (T) defaultObjectReaderProvider.getObjectReader(type, z).createInstance((Map) t, j);
        }
        if (t instanceof Collection) {
            return (T) defaultObjectReaderProvider.getObjectReader(type, z).createInstance((Collection) t, j);
        }
        Class<?> mapping = TypeUtils.getMapping(type);
        if (mapping.isInstance(t)) {
            return t;
        }
        JSONReader jSONReaderOf = JSONReader.of(JSON.toJSONString(t));
        jSONReaderOf.context.config(featureArr);
        return (T) defaultObjectReaderProvider.getObjectReader(mapping, z).readObject(jSONReaderOf, null, null, 0L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getObject(int i, Class<T> cls, JSONReader.Feature... featureArr) throws Throwable {
        T t = (T) get(i);
        ObjectReader objectReader = null;
        if (t == 0) {
            return null;
        }
        Class<?> cls2 = t.getClass();
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        Function typeConvert = defaultObjectReaderProvider.getTypeConvert(cls2, cls);
        if (typeConvert != null) {
            return (T) typeConvert.apply(t);
        }
        long j = JSONFactory.defaultReaderFeatures;
        boolean z = false;
        for (JSONReader.Feature feature : featureArr) {
            j |= feature.mask;
            if (feature == JSONReader.Feature.FieldBased) {
                z = true;
            }
        }
        if (t instanceof Map) {
            return (T) defaultObjectReaderProvider.getObjectReader(cls, z).createInstance((Map) t, j);
        }
        if (t instanceof Collection) {
            return (T) defaultObjectReaderProvider.getObjectReader(cls, z).createInstance((Collection) t, j);
        }
        Class<?> mapping = TypeUtils.getMapping(cls);
        if (mapping.isInstance(t)) {
            return t;
        }
        if (t instanceof String) {
            String str = (String) t;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            if (mapping.isEnum()) {
                objectReader = defaultObjectReaderProvider.getObjectReader(mapping, z);
                if (objectReader instanceof ObjectReaderImplEnum) {
                    return (T) ((ObjectReaderImplEnum) objectReader).getEnumByHashCode(Fnv.hashCode64(str));
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

    public <T> T getObject(int i, Function<JSONObject, T> function) {
        JSONObject jSONObject = getJSONObject(i);
        if (jSONObject == null) {
            return null;
        }
        return function.apply(jSONObject);
    }

    public JSONObject addObject() {
        JSONObject jSONObject = new JSONObject();
        add(jSONObject);
        return jSONObject;
    }

    public JSONArray addArray() {
        JSONArray jSONArray = new JSONArray();
        add(jSONArray);
        return jSONArray;
    }

    public JSONArray fluentAdd(Object obj) {
        add(obj);
        return this;
    }

    public JSONArray fluentClear() {
        clear();
        return this;
    }

    public JSONArray fluentRemove(int i) {
        remove(i);
        return this;
    }

    public JSONArray fluentSet(int i, Object obj) {
        set(i, obj);
        return this;
    }

    public JSONArray fluentRemove(Object obj) {
        remove(obj);
        return this;
    }

    public JSONArray fluentRemoveAll(Collection<?> collection) {
        removeAll(collection);
        return this;
    }

    public JSONArray fluentAddAll(Collection<?> collection) {
        addAll(collection);
        return this;
    }

    public boolean isValid(JSONSchema jSONSchema) {
        return jSONSchema.validate(this).isSuccess();
    }

    @Override // java.util.ArrayList
    public Object clone() {
        return new JSONArray(this);
    }

    public static JSONArray of(Object... objArr) {
        return new JSONArray(objArr);
    }

    public static JSONArray of(Object obj) {
        JSONArray jSONArray = new JSONArray(1);
        jSONArray.add(obj);
        return jSONArray;
    }

    public static JSONArray copyOf(Collection collection) {
        return new JSONArray((Collection<?>) collection);
    }

    public static JSONArray of(Object obj, Object obj2) {
        JSONArray jSONArray = new JSONArray(2);
        jSONArray.add(obj);
        jSONArray.add(obj2);
        return jSONArray;
    }

    public static JSONArray of(Object obj, Object obj2, Object obj3) {
        JSONArray jSONArray = new JSONArray(3);
        jSONArray.add(obj);
        jSONArray.add(obj2);
        jSONArray.add(obj3);
        return jSONArray;
    }

    public static JSONArray parseArray(String str, JSONReader.Feature... featureArr) {
        return JSON.parseArray(str, featureArr);
    }

    public static <T> List<T> parseArray(String str, Class<T> cls, JSONReader.Feature... featureArr) {
        return JSON.parseArray(str, (Class) cls, featureArr);
    }

    public static JSONArray parse(String str, JSONReader.Feature... featureArr) {
        return JSON.parseArray(str, featureArr);
    }

    public static <T> List<T> parseArray(String str, Class<T> cls) {
        return JSON.parseArray(str, (Class) cls);
    }

    public static JSONArray from(Object obj) {
        return (JSONArray) JSON.toJSON(obj);
    }

    public static JSONArray from(Object obj, JSONWriter.Feature... featureArr) {
        return (JSONArray) JSON.toJSON(obj, featureArr);
    }
}
