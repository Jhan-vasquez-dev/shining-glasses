package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class ObjectWriterImplList extends ObjectWriterPrimitiveImpl {
    static final Class CLASS_SUBLIST;
    static final ObjectWriterImplList INSTANCE = new ObjectWriterImplList(null, null, null, null, 0);
    static final ObjectWriterImplList INSTANCE_JSON_ARRAY = new ObjectWriterImplList(JSONArray.class, null, null, null, 0);
    static final ObjectWriterImplList INSTANCE_JSON_ARRAY_1x;
    static final String TYPE_NAME_ARRAY_LIST;
    static final long TYPE_NAME_HASH_ARRAY_LIST;
    static final byte[] TYPE_NAME_JSONB_ARRAY_LIST;
    final Class defineClass;
    final Type defineType;
    final long features;
    final Class itemClass;
    final boolean itemClassRefDetect;
    volatile ObjectWriter itemClassWriter;
    final Type itemType;

    static {
        if (TypeUtils.CLASS_JSON_ARRAY_1x == null) {
            INSTANCE_JSON_ARRAY_1x = null;
        } else {
            INSTANCE_JSON_ARRAY_1x = new ObjectWriterImplList(TypeUtils.CLASS_JSON_ARRAY_1x, null, null, null, 0L);
        }
        CLASS_SUBLIST = new ArrayList().subList(0, 0).getClass();
        String typeName = TypeUtils.getTypeName(ArrayList.class);
        TYPE_NAME_ARRAY_LIST = typeName;
        TYPE_NAME_JSONB_ARRAY_LIST = JSONB.toBytes(typeName);
        TYPE_NAME_HASH_ARRAY_LIST = Fnv.hashCode64(typeName);
    }

    public ObjectWriterImplList(Class cls, Type type, Class cls2, Type type2, long j) {
        this.defineClass = cls;
        this.defineType = type;
        this.itemClass = cls2;
        this.itemType = type2;
        this.features = j;
        this.itemClassRefDetect = (cls2 == null || ObjectWriterProvider.isNotReferenceDetect(cls2)) ? false : true;
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriterPrimitiveImpl, com.alibaba.fastjson2.writer.ObjectWriter
    public void writeArrayMappingJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        JSONWriter jSONWriter2;
        if (obj == null) {
            jSONWriter.writeArrayNull();
            return;
        }
        List list = getList(obj);
        jSONWriter.startArray(list.size());
        Class<?> cls = null;
        int i = 0;
        ObjectWriter objectWriter = null;
        while (i < list.size()) {
            Object obj3 = list.get(i);
            if (obj3 == null) {
                jSONWriter.writeNull();
                jSONWriter2 = jSONWriter;
            } else {
                Class<?> cls2 = obj3.getClass();
                if (cls2 != cls) {
                    objectWriter = jSONWriter.getObjectWriter(cls2);
                    cls = cls2;
                }
                ObjectWriter objectWriter2 = objectWriter;
                jSONWriter2 = jSONWriter;
                objectWriter2.writeArrayMappingJSONB(jSONWriter2, obj3, Integer.valueOf(i), this.itemType, this.features | j);
                objectWriter = objectWriter2;
            }
            i++;
            jSONWriter = jSONWriter2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x007a  */
    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void writeJSONB(com.alibaba.fastjson2.JSONWriter r18, java.lang.Object r19, java.lang.Object r20, java.lang.reflect.Type r21, long r22) {
        /*
            Method dump skipped, instruction units count: 339
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterImplList.writeJSONB(com.alibaba.fastjson2.JSONWriter, java.lang.Object, java.lang.Object, java.lang.reflect.Type, long):void");
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        Class<?> cls;
        ObjectWriter objectWriter;
        boolean zIsRefDetect;
        boolean z;
        String path;
        if (obj == null) {
            jSONWriter.writeArrayNull();
            return;
        }
        List list = getList(obj);
        Class<?> cls2 = null;
        if (jSONWriter.jsonb) {
            jSONWriter.startArray(list.size());
            ObjectWriter objectWriter2 = null;
            for (int i = 0; i < list.size(); i++) {
                Object obj3 = list.get(i);
                if (obj3 == null) {
                    jSONWriter.writeNull();
                } else {
                    Class<?> cls3 = obj3.getClass();
                    if (cls3 != cls2) {
                        objectWriter2 = jSONWriter.getObjectWriter(cls3);
                        cls2 = cls3;
                    }
                    objectWriter2.writeJSONB(jSONWriter, obj3, Integer.valueOf(i), this.itemType, j);
                }
            }
            return;
        }
        JSONWriter.Context context = jSONWriter.context;
        ObjectWriterProvider objectWriterProvider = context.provider;
        int size = list.size();
        if (size == 0) {
            jSONWriter.writeRaw('[', ']');
            return;
        }
        jSONWriter.startArray();
        Class<?> cls4 = null;
        ObjectWriter objectWriter3 = null;
        boolean z2 = true;
        for (int i2 = 0; i2 < size; i2++) {
            if (i2 != 0) {
                jSONWriter.writeComma();
            }
            Object obj4 = list.get(i2);
            if (obj4 == null) {
                jSONWriter.writeNull();
            } else {
                Class<?> cls5 = obj4.getClass();
                if (cls5 == String.class) {
                    jSONWriter.writeString((String) obj4);
                } else {
                    if (cls5 != Integer.class) {
                        cls = cls4;
                        if (cls5 == Long.class) {
                            if ((objectWriterProvider.userDefineMask & 4) == 0) {
                                jSONWriter.writeInt64((Long) obj4);
                            } else {
                                objectWriterProvider.getObjectWriter((Type) cls5, (Class) cls5, false).write(jSONWriter, obj4, Integer.valueOf(i2), Long.class, j);
                            }
                        } else if (cls5 == Boolean.class) {
                            if ((objectWriterProvider.userDefineMask & 2) == 0) {
                                jSONWriter.writeBool(((Boolean) obj4).booleanValue());
                            } else {
                                objectWriterProvider.getObjectWriter((Type) cls5, (Class) cls5, false).write(jSONWriter, obj4, Integer.valueOf(i2), Boolean.class, j);
                            }
                        } else if (cls5 != BigDecimal.class) {
                            if (cls5 != this.itemClass || this.itemClassWriter == null) {
                                if (cls5 == cls) {
                                    objectWriter = objectWriter3;
                                } else {
                                    if (cls5 == JSONObject.class) {
                                        objectWriter = ObjectWriterImplMap.INSTANCE;
                                        zIsRefDetect = jSONWriter.isRefDetect();
                                    } else if (cls5 == TypeUtils.CLASS_JSON_OBJECT_1x) {
                                        objectWriter = ObjectWriterImplMap.INSTANCE_1x;
                                        zIsRefDetect = jSONWriter.isRefDetect();
                                    } else if (cls5 == JSONArray.class) {
                                        objectWriter = INSTANCE_JSON_ARRAY;
                                        zIsRefDetect = jSONWriter.isRefDetect();
                                    } else if (cls5 == TypeUtils.CLASS_JSON_ARRAY_1x) {
                                        objectWriter = INSTANCE_JSON_ARRAY_1x;
                                        zIsRefDetect = jSONWriter.isRefDetect();
                                    } else {
                                        objectWriter = context.getObjectWriter(cls5);
                                        zIsRefDetect = jSONWriter.isRefDetect(obj4);
                                    }
                                    z2 = zIsRefDetect;
                                    if (cls5 == this.itemClass) {
                                        this.itemClassWriter = objectWriter;
                                    }
                                    objectWriter3 = objectWriter;
                                    cls = cls5;
                                }
                                z = z2;
                            } else {
                                objectWriter = this.itemClassWriter;
                                z = z2;
                                z2 = this.itemClassRefDetect && jSONWriter.isRefDetect();
                            }
                            if (z2 && (path = jSONWriter.setPath(i2, obj4)) != null) {
                                jSONWriter.writeReference(path);
                                jSONWriter.popPath(obj4);
                            } else {
                                objectWriter.write(jSONWriter, obj4, Integer.valueOf(i2), this.itemType, this.features);
                                if (z2) {
                                    jSONWriter.popPath(obj4);
                                }
                            }
                            z2 = z;
                        } else if ((objectWriterProvider.userDefineMask & 8) == 0) {
                            jSONWriter.writeDecimal((BigDecimal) obj4, j, null);
                        } else {
                            objectWriterProvider.getObjectWriter((Type) cls5, (Class) cls5, false).write(jSONWriter, obj4, Integer.valueOf(i2), BigDecimal.class, j);
                        }
                    } else if ((objectWriterProvider.userDefineMask & 2) == 0) {
                        jSONWriter.writeInt32((Integer) obj4);
                    } else {
                        cls = cls4;
                        objectWriterProvider.getObjectWriter((Type) cls5, (Class) cls5, false).write(jSONWriter, obj4, Integer.valueOf(i2), Integer.class, j);
                    }
                    cls4 = cls;
                }
            }
            cls = cls4;
            cls4 = cls;
        }
        jSONWriter.endArray();
    }

    private List getList(Object obj) {
        if (obj instanceof List) {
            return (List) obj;
        }
        if (obj instanceof Iterable) {
            Iterable iterable = (Iterable) obj;
            ArrayList arrayList = iterable instanceof Collection ? new ArrayList(((Collection) iterable).size()) : new ArrayList();
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
            return arrayList;
        }
        throw new JSONException("Can not cast '" + obj.getClass() + "' to List");
    }
}
