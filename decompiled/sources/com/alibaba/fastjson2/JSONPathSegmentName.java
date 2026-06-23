package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
class JSONPathSegmentName extends JSONPathSegment {
    static final long HASH_NAME = Fnv.hashCode64("name");
    static final long HASH_ORDINAL = Fnv.hashCode64("ordinal");
    final String name;
    final long nameHashCode;

    public JSONPathSegmentName(String str, long j) {
        this.name = str;
        this.nameHashCode = j;
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public boolean remove(JSONPath.Context context) {
        Object obj;
        if (context.parent == null) {
            obj = context.root;
        } else {
            obj = context.parent.value;
        }
        if (obj instanceof Map) {
            ((Map) obj).remove(this.name);
            context.eval = true;
            return true;
        }
        if (obj instanceof Collection) {
            for (Object obj2 : (Collection) obj) {
                if (obj2 != null) {
                    if (obj2 instanceof Map) {
                        ((Map) obj2).remove(this.name);
                    } else {
                        FieldReader fieldReader = context.path.getReaderContext().getProvider().getObjectReader(obj2.getClass()).getFieldReader(this.nameHashCode);
                        if (fieldReader != null) {
                            fieldReader.accept(obj2, (Object) null);
                        }
                    }
                }
            }
            context.eval = true;
            return true;
        }
        FieldReader fieldReader2 = context.path.getReaderContext().getProvider().getObjectReader(obj.getClass()).getFieldReader(this.nameHashCode);
        if (fieldReader2 != null) {
            fieldReader2.accept(obj, (Object) null);
        }
        context.eval = true;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public boolean contains(JSONPath.Context context) {
        Object obj;
        FieldWriter fieldWriter;
        FieldWriter fieldWriter2;
        FieldWriter fieldWriter3;
        FieldWriter fieldWriter4;
        if (context.parent == null) {
            obj = context.root;
        } else {
            obj = context.parent.value;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Map) {
            return ((Map) obj).containsKey(this.name);
        }
        if (obj instanceof Collection) {
            for (Object obj2 : (Collection) obj) {
                if (obj2 != null) {
                    if ((obj2 instanceof Map) && ((Map) obj2).get(this.name) != null) {
                        return true;
                    }
                    ObjectWriter objectWriter = context.path.getWriterContext().getObjectWriter(obj2.getClass());
                    if ((objectWriter instanceof ObjectWriterAdapter) && (fieldWriter4 = objectWriter.getFieldWriter(this.nameHashCode)) != null && fieldWriter4.getFieldValue(obj2) != null) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (obj instanceof JSONPath.Sequence) {
            for (Object obj3 : ((JSONPath.Sequence) obj).values) {
                if (obj3 != null) {
                    if ((obj3 instanceof Map) && ((Map) obj3).get(this.name) != null) {
                        return true;
                    }
                    ObjectWriter objectWriter2 = context.path.getWriterContext().getObjectWriter(obj3.getClass());
                    if ((objectWriter2 instanceof ObjectWriterAdapter) && (fieldWriter3 = objectWriter2.getFieldWriter(this.nameHashCode)) != null && fieldWriter3.getFieldValue(obj3) != null) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (obj instanceof Object[]) {
            for (Object obj4 : (Object[]) obj) {
                if (obj4 != null) {
                    if ((obj4 instanceof Map) && ((Map) obj4).get(this.name) != null) {
                        return true;
                    }
                    ObjectWriter objectWriter3 = context.path.getWriterContext().getObjectWriter(obj4.getClass());
                    if ((objectWriter3 instanceof ObjectWriterAdapter) && (fieldWriter2 = objectWriter3.getFieldWriter(this.nameHashCode)) != null && fieldWriter2.getFieldValue(obj4) != null) {
                        return true;
                    }
                }
            }
        }
        ObjectWriter objectWriter4 = context.path.getWriterContext().getObjectWriter(obj.getClass());
        return (!(objectWriter4 instanceof ObjectWriterAdapter) || (fieldWriter = objectWriter4.getFieldWriter(this.nameHashCode)) == null || fieldWriter.getFieldValue(obj) == null) ? false : true;
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public void eval(JSONPath.Context context) {
        Object obj;
        Object obj2;
        if (context.parent == null) {
            obj = context.root;
        } else {
            obj = context.parent.value;
        }
        if (obj == null) {
            return;
        }
        Collection jSONArray = null;
        Long lValueOf = null;
        if (obj instanceof Map) {
            Map map = (Map) obj;
            Object value = map.get(this.name);
            if (value == null) {
                boolean zIsNumber = IOUtils.isNumber(this.name);
                Iterator it = map.entrySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Map.Entry entry = (Map.Entry) it.next();
                    Object key = entry.getKey();
                    if ((key instanceof Enum) && ((Enum) key).name().equals(this.name)) {
                        value = entry.getValue();
                        break;
                    } else if (key instanceof Long) {
                        if (lValueOf == null && zIsNumber) {
                            lValueOf = Long.valueOf(Long.parseLong(this.name));
                        }
                        if (key.equals(lValueOf)) {
                            value = entry.getValue();
                            break;
                        }
                    }
                }
            }
            context.value = value;
            return;
        }
        if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            int size = collection.size();
            for (Object obj3 : collection) {
                if ((obj3 instanceof Map) && (obj2 = ((Map) obj3).get(this.name)) != null) {
                    if (!(obj2 instanceof Collection)) {
                        if (jSONArray == null) {
                            jSONArray = new JSONArray(size);
                        }
                        jSONArray.add(obj2);
                    } else if (size == 1) {
                        jSONArray = (Collection) obj2;
                    } else {
                        if (jSONArray == null) {
                            jSONArray = new JSONArray(size);
                        }
                        jSONArray.addAll((Collection) obj2);
                    }
                }
            }
            context.value = jSONArray;
            return;
        }
        if (obj instanceof JSONPath.Sequence) {
            List list = ((JSONPath.Sequence) obj).values;
            JSONArray jSONArray2 = new JSONArray(list.size());
            Iterator it2 = list.iterator();
            while (it2.hasNext()) {
                context.value = it2.next();
                JSONPath.Context context2 = context;
                JSONPath.Context context3 = new JSONPath.Context(context.path, context2, context.current, context.next, context.readerFeatures);
                eval(context3);
                Object obj4 = context3.value;
                if (obj4 != null || (context2.path.features & JSONPath.Feature.KeepNullValue.mask) != 0) {
                    if (obj4 instanceof Collection) {
                        jSONArray2.addAll((Collection) obj4);
                    } else {
                        jSONArray2.add(obj4);
                    }
                }
                context = context2;
            }
            JSONPath.Context context4 = context;
            if (context4.next != null) {
                context4.value = new JSONPath.Sequence(jSONArray2);
            } else {
                context4.value = jSONArray2;
            }
            context4.eval = true;
            return;
        }
        ObjectWriter objectWriter = context.path.getWriterContext().getObjectWriter(obj.getClass());
        if (objectWriter instanceof ObjectWriterAdapter) {
            FieldWriter fieldWriter = objectWriter.getFieldWriter(this.nameHashCode);
            if (fieldWriter != null) {
                context.value = fieldWriter.getFieldValue(obj);
                return;
            }
            return;
        }
        long j = this.nameHashCode;
        if (j == HASH_NAME && (obj instanceof Enum)) {
            context.value = ((Enum) obj).name();
            return;
        }
        if (j == HASH_ORDINAL && (obj instanceof Enum)) {
            context.value = Integer.valueOf(((Enum) obj).ordinal());
            return;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (!str.isEmpty() && str.charAt(0) == '{') {
                context.value = JSONPath.of("$." + this.name).extract(JSONReader.of(str));
                return;
            } else {
                context.value = null;
                return;
            }
        }
        if ((obj instanceof Number) || (obj instanceof Boolean)) {
            context.value = null;
            return;
        }
        throw new JSONException("not support : " + obj.getClass());
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public void set(JSONPath.Context context, Object obj) {
        Object obj2;
        Class<?> cls;
        Class<?> cls2;
        Function typeConvert;
        if (context.parent == null) {
            obj2 = context.root;
        } else {
            obj2 = context.parent.value;
        }
        if (obj2 instanceof JSONPath.Sequence) {
            obj2 = ((JSONPath.Sequence) obj2).values;
        }
        if (obj2 instanceof Map) {
            Map map = (Map) obj2;
            Object objPut = map.put(this.name, obj);
            if (objPut == null || (context.readerFeatures & JSONReader.Feature.DuplicateKeyValueAsArray.mask) == 0) {
                return;
            }
            if (objPut instanceof Collection) {
                ((Collection) objPut).add(obj);
                map.put(this.name, obj);
                return;
            } else {
                map.put(this.name, JSONArray.of(objPut, obj));
                return;
            }
        }
        if (obj2 instanceof Collection) {
            for (Object obj3 : (Collection) obj2) {
                if (obj3 != null) {
                    if (obj3 instanceof Map) {
                        Map map2 = (Map) obj3;
                        Object objPut2 = map2.put(this.name, obj);
                        if (objPut2 != null && (context.readerFeatures & JSONReader.Feature.DuplicateKeyValueAsArray.mask) != 0) {
                            if (objPut2 instanceof Collection) {
                                ((Collection) objPut2).add(obj);
                                map2.put(this.name, obj);
                            } else {
                                map2.put(this.name, JSONArray.of(objPut2, obj));
                            }
                        }
                    } else {
                        FieldReader fieldReader = context.path.getReaderContext().getProvider().getObjectReader(obj3.getClass()).getFieldReader(this.nameHashCode);
                        if (fieldReader != null) {
                            fieldReader.accept(obj3, obj);
                        }
                    }
                }
            }
            return;
        }
        ObjectReaderProvider provider = context.path.getReaderContext().getProvider();
        FieldReader fieldReader2 = provider.getObjectReader(obj2.getClass()).getFieldReader(this.nameHashCode);
        if (fieldReader2 == null) {
            return;
        }
        if (obj != null && (cls = obj.getClass()) != (cls2 = fieldReader2.fieldClass) && (typeConvert = provider.getTypeConvert(cls, cls2)) != null) {
            obj = typeConvert.apply(obj);
        }
        fieldReader2.accept(obj2, obj);
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public void setCallback(JSONPath.Context context, BiFunction biFunction) {
        Object obj;
        if (context.parent == null) {
            obj = context.root;
        } else {
            obj = context.parent.value;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            Object obj2 = map.get(this.name);
            if (obj2 != null) {
                map.put(this.name, biFunction.apply(map, obj2));
                return;
            }
            return;
        }
        ObjectReader objectReader = context.path.getReaderContext().getProvider().getObjectReader(obj.getClass());
        ObjectWriter objectWriter = context.path.getWriterContext().provider.getObjectWriter((Class) obj.getClass());
        FieldReader fieldReader = objectReader.getFieldReader(this.nameHashCode);
        FieldWriter fieldWriter = objectWriter.getFieldWriter(this.nameHashCode);
        if (fieldReader == null || fieldWriter == null) {
            return;
        }
        fieldReader.accept(obj, biFunction.apply(obj, fieldWriter.getFieldValue(obj)));
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0161  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x018a  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0234  */
    @Override // com.alibaba.fastjson2.JSONPathSegment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void accept(com.alibaba.fastjson2.JSONReader r20, com.alibaba.fastjson2.JSONPath.Context r21) {
        /*
            Method dump skipped, instruction units count: 710
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathSegmentName.accept(com.alibaba.fastjson2.JSONReader, com.alibaba.fastjson2.JSONPath$Context):void");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            JSONPathSegmentName jSONPathSegmentName = (JSONPathSegmentName) obj;
            if (this.nameHashCode == jSONPathSegmentName.nameHashCode && Objects.equals(this.name, jSONPathSegmentName.name)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.name, Long.valueOf(this.nameHashCode));
    }

    public String toString() {
        return this.name;
    }
}
