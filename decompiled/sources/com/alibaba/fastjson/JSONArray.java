package com.alibaba.fastjson;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class JSONArray extends JSON implements List<Object>, Serializable, Cloneable {
    static ObjectReader<JSONArray> arrayReader;
    static ObjectReader<JSONObject> objectReader;
    protected transient Type componentType;
    private List list;
    protected transient Object relatedArray;

    public JSONArray() {
        this.list = new com.alibaba.fastjson2.JSONArray();
    }

    public JSONArray(List list) {
        new com.alibaba.fastjson2.JSONArray();
        this.list = list;
    }

    public JSONArray(int i) {
        this.list = new com.alibaba.fastjson2.JSONArray();
        this.list = new ArrayList(i);
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

    public Long getLong(int i) {
        Object obj = this.list.get(i);
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
        throw new JSONException("Can not cast '" + obj.getClass() + "' to Long");
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

    public JSONObject getJSONObject(int i) {
        Object obj = get(i);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str);
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

    @Override // java.util.List, java.util.Collection
    public int size() {
        return this.list.size();
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object obj) {
        return this.list.contains(obj);
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        return this.list.iterator();
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        return this.list.toArray();
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray(Object[] objArr) {
        return this.list.toArray(objArr);
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(Object obj) {
        return this.list.add(obj);
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object obj) {
        return this.list.remove(obj);
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection collection) {
        return this.list.addAll(collection);
    }

    @Override // java.util.List
    public boolean addAll(int i, Collection<? extends Object> collection) {
        return this.list.addAll(i, collection);
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        this.list.clear();
    }

    public JSONArray fluentClear() {
        this.list.clear();
        return this;
    }

    public JSONArray fluentRemove(int i) {
        this.list.remove(i);
        return this;
    }

    public JSONArray fluentRemove(Object obj) {
        this.list.remove(obj);
        return this;
    }

    public JSONArray fluentSet(int i, Object obj) {
        set(i, obj);
        return this;
    }

    public JSONArray fluentRemoveAll(Collection<?> collection) {
        this.list.removeAll(collection);
        return this;
    }

    public JSONArray fluentAddAll(Collection<?> collection) {
        this.list.addAll(collection);
        return this;
    }

    public short getShortValue(int i) {
        Object obj = this.list.get(i);
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

    public float getFloatValue(int i) {
        Object obj = this.list.get(i);
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

    public double getDoubleValue(int i) {
        Object obj = this.list.get(i);
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

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection collection) {
        return this.list.retainAll(collection);
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection collection) {
        return this.list.removeAll(collection);
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection collection) {
        return this.list.containsAll(collection);
    }

    @Override // java.util.List
    public Object get(int i) {
        return adaptResult(this.list.get(i));
    }

    public byte getByteValue(int i) {
        Object obj = this.list.get(i);
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

    public BigInteger getBigInteger(int i) {
        Object obj = this.list.get(i);
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

    public Date getSqlDate(int i) {
        return (Date) TypeUtils.cast(get(i), Date.class);
    }

    public Timestamp getTimestamp(int i) {
        return (Timestamp) TypeUtils.cast(get(i), Timestamp.class);
    }

    public java.util.Date getDate(int i) {
        Object obj = get(i);
        if (obj == null) {
            return null;
        }
        if (obj instanceof java.util.Date) {
            return (java.util.Date) obj;
        }
        if (obj instanceof Number) {
            long jLongValue = ((Number) obj).longValue();
            if (jLongValue == 0) {
                return null;
            }
            return new java.util.Date(jLongValue);
        }
        return TypeUtils.toDate(obj);
    }

    @Override // java.util.List
    public Object set(int i, Object obj) {
        if (i == -1) {
            this.list.add(obj);
            return null;
        }
        if (this.list.size() <= i) {
            for (int size = this.list.size(); size < i; size++) {
                this.list.add(null);
            }
            this.list.add(obj);
            return null;
        }
        return this.list.set(i, obj);
    }

    @Override // java.util.List
    public void add(int i, Object obj) {
        this.list.add(i, obj);
    }

    @Override // java.util.List
    public Object remove(int i) {
        return this.list.remove(i);
    }

    @Override // java.util.List
    public int indexOf(Object obj) {
        return this.list.indexOf(obj);
    }

    @Override // java.util.List
    public int lastIndexOf(Object obj) {
        return this.list.lastIndexOf(obj);
    }

    @Override // java.util.List
    public ListIterator<Object> listIterator() {
        return this.list.listIterator();
    }

    @Override // java.util.List
    public ListIterator<Object> listIterator(int i) {
        return this.list.listIterator(i);
    }

    @Override // java.util.List
    public List<Object> subList(int i, int i2) {
        return this.list.subList(i, i2);
    }

    public String getString(int i) {
        Object obj = this.list.get(i);
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof java.util.Date) {
            return DateUtils.toString(((java.util.Date) obj).getTime(), false, DateUtils.DEFAULT_ZONE_ID);
        }
        if ((obj instanceof Boolean) || (obj instanceof Character) || (obj instanceof Number) || (obj instanceof UUID) || (obj instanceof Enum) || (obj instanceof TemporalAccessor)) {
            return obj.toString();
        }
        return com.alibaba.fastjson2.JSON.toJSONString(obj);
    }

    public JSONArray getJSONArray(int i) {
        Object obj = get(i);
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str);
            if (arrayReader == null) {
                arrayReader = jSONReaderOf.getObjectReader(JSONArray.class);
            }
            return arrayReader.readObject(jSONReaderOf, null, null, 0L);
        }
        if (obj instanceof List) {
            return new JSONArray((List) obj);
        }
        return null;
    }

    public <T> T getObject(int i, Type type) {
        Object obj = this.list.get(i);
        if (type instanceof Class) {
            return (T) TypeUtils.cast(obj, (Class) type);
        }
        return (T) JSON.parseObject(JSON.toJSONString(obj), type, new Feature[0]);
    }

    public <T> T getObject(int i, Class<T> cls) {
        T t = (T) this.list.get(i);
        if (t == null) {
            return null;
        }
        if (cls.isInstance(t)) {
            return t;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        Function typeConvert = defaultObjectReaderProvider.getTypeConvert(t.getClass(), cls);
        if (typeConvert != null) {
            return (T) typeConvert.apply(t);
        }
        String jSONString = JSON.toJSONString(t);
        ObjectReader objectReader2 = defaultObjectReaderProvider.getObjectReader(cls);
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(jSONString);
        String str = JSON.DEFAULT_DATE_FORMAT;
        if (!"yyyy-MM-dd HH:mm:ss".equals(str)) {
            jSONReaderOf.getContext().setDateFormat(str);
        }
        return (T) objectReader2.readObject(jSONReaderOf, null, null, 0L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> List<T> toJavaList(Class<T> cls) {
        ArrayList arrayList = new ArrayList(size());
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        ObjectReader objectReader2 = defaultObjectReaderProvider.getObjectReader(cls);
        for (Object obj : this) {
            if (!(obj instanceof Map)) {
                if (obj == null || obj.getClass() == cls) {
                    arrayList.add(obj);
                } else {
                    Function typeConvert = defaultObjectReaderProvider.getTypeConvert(obj.getClass(), cls);
                    if (typeConvert != 0) {
                        arrayList.add(typeConvert.apply(obj));
                    } else {
                        throw new com.alibaba.fastjson2.JSONException(obj.getClass() + " cannot be converted to " + cls);
                    }
                }
            } else {
                arrayList.add(objectReader2.createInstance((Map) obj, JSONReader.Feature.SupportSmartMatch.mask));
            }
        }
        return arrayList;
    }

    public JSONArray fluentAdd(Object obj) {
        this.list.add(obj);
        return this;
    }

    @Override // com.alibaba.fastjson.JSON
    public <T> T toJavaObject(Class<T> cls) {
        return (T) JSONFactory.getDefaultObjectReaderProvider().getObjectReader(cls).createInstance(this);
    }

    public String toString() {
        return toJSONString(this);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public JSONArray m26clone() {
        return new JSONArray(new ArrayList(this.list));
    }

    @Override // java.util.List, java.util.Collection
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override // java.util.List, java.util.Collection
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof JSONArray) {
            return this.list.equals(((JSONArray) obj).list);
        }
        return this.list.equals(obj);
    }

    @Override // com.alibaba.fastjson.JSON
    public <T> T toJavaObject(Type type) {
        return (T) com.alibaba.fastjson.util.TypeUtils.cast(this, type, ParserConfig.getGlobalInstance());
    }

    @Deprecated
    public Type getComponentType() {
        return this.componentType;
    }

    @Deprecated
    public void setComponentType(Type type) {
        this.componentType = type;
    }

    @Deprecated
    public Object getRelatedArray() {
        return this.relatedArray;
    }

    @Deprecated
    public void setRelatedArray(Object obj) {
        this.relatedArray = obj;
    }
}
