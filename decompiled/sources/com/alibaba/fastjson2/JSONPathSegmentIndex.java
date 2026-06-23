package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.function.BiFunction;

/* JADX INFO: loaded from: classes.dex */
final class JSONPathSegmentIndex extends JSONPathSegment {
    final int index;
    static final JSONPathSegmentIndex ZERO = new JSONPathSegmentIndex(0);
    static final JSONPathSegmentIndex ONE = new JSONPathSegmentIndex(1);
    static final JSONPathSegmentIndex TWO = new JSONPathSegmentIndex(2);
    static final JSONPathSegmentIndex LAST = new JSONPathSegmentIndex(-1);

    public JSONPathSegmentIndex(int i) {
        this.index = i;
    }

    static JSONPathSegmentIndex of(int i) {
        if (i == 0) {
            return ZERO;
        }
        if (i == 1) {
            return ONE;
        }
        if (i == 2) {
            return TWO;
        }
        if (i == -1) {
            return LAST;
        }
        return new JSONPathSegmentIndex(i);
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public void eval(JSONPath.Context context) {
        Object obj;
        if (context.parent == null) {
            obj = context.root;
        } else {
            obj = context.parent.value;
        }
        if (obj == null) {
            context.eval = true;
            return;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            int i = this.index;
            if (i >= 0) {
                if (i < list.size()) {
                    context.value = list.get(this.index);
                }
            } else {
                int size = list.size() + this.index;
                if (size >= 0 && size < list.size()) {
                    context.value = list.get(size);
                }
            }
            context.eval = true;
            return;
        }
        if ((obj instanceof SortedSet) || (obj instanceof LinkedHashSet) || (obj instanceof Queue) || (this.index == 0 && (obj instanceof Collection) && ((Collection) obj).size() == 1)) {
            Iterator it = ((Collection) obj).iterator();
            int i2 = 0;
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                if (i2 == this.index) {
                    context.value = next;
                    break;
                }
                i2++;
            }
            context.eval = true;
            return;
        }
        if (obj instanceof Object[]) {
            Object[] objArr = (Object[]) obj;
            int i3 = this.index;
            if (i3 >= 0) {
                if (i3 < objArr.length) {
                    context.value = objArr[i3];
                }
            } else {
                int length = objArr.length + i3;
                if (length >= 0 && length < objArr.length) {
                    context.value = objArr[length];
                }
            }
            context.eval = true;
            return;
        }
        Class<?> cls = obj.getClass();
        if (cls.isArray()) {
            int length2 = Array.getLength(obj);
            int i4 = this.index;
            if (i4 < 0) {
                int i5 = i4 + length2;
                if (i5 >= 0 && i5 < length2) {
                    context.value = Array.get(obj, i5);
                }
            } else if (i4 < length2) {
                context.value = Array.get(obj, i4);
            }
            context.eval = true;
            return;
        }
        if (obj instanceof JSONPath.Sequence) {
            List list2 = ((JSONPath.Sequence) obj).values;
            JSONArray jSONArray = new JSONArray(list2.size());
            Iterator it2 = list2.iterator();
            while (it2.hasNext()) {
                context.value = it2.next();
                JSONPath.Context context2 = context;
                JSONPath.Context context3 = new JSONPath.Context(context.path, context2, context.current, context.next, context.readerFeatures);
                eval(context3);
                jSONArray.add(context3.value);
                context = context2;
            }
            JSONPath.Context context4 = context;
            if (context4.next != null) {
                context4.value = new JSONPath.Sequence(jSONArray);
            } else {
                context4.value = jSONArray;
            }
            context4.eval = true;
            return;
        }
        if (Map.class.isAssignableFrom(cls)) {
            context.value = eval((Map) obj);
            context.eval = true;
        } else {
            if (this.index == 0) {
                context.value = obj;
                context.eval = true;
                return;
            }
            throw new JSONException("jsonpath not support operate : " + context.path + ", objectClass" + cls.getName());
        }
    }

    private Object eval(Map map) {
        Object obj = map.get(Integer.valueOf(this.index));
        if (obj == null) {
            obj = map.get(Integer.toString(this.index));
        }
        if (obj == null) {
            int size = map.size();
            Iterator it = map.entrySet().iterator();
            int i = 0;
            if (size == 1 || (map instanceof LinkedHashMap) || (map instanceof SortedMap)) {
                while (i <= this.index && i < size && it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (key instanceof Long) {
                        if (key.equals(Long.valueOf(this.index))) {
                            return value;
                        }
                    } else if (i == this.index) {
                        obj = value;
                    }
                    i++;
                }
            } else {
                while (i <= this.index && i < map.size() && it.hasNext()) {
                    Map.Entry entry2 = (Map.Entry) it.next();
                    Object key2 = entry2.getKey();
                    Object value2 = entry2.getValue();
                    if ((key2 instanceof Long) && key2.equals(Long.valueOf(this.index))) {
                        return value2;
                    }
                    i++;
                }
            }
        }
        return obj;
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public void set(JSONPath.Context context, Object obj) {
        Object obj2;
        if (context.parent == null) {
            obj2 = context.root;
        } else {
            obj2 = context.parent.value;
        }
        if (obj2 instanceof List) {
            List list = (List) obj2;
            int i = this.index;
            if (i >= 0) {
                if (i > list.size()) {
                    for (int size = list.size(); size < this.index; size++) {
                        list.add(null);
                    }
                }
                if (this.index < list.size()) {
                    list.set(this.index, obj);
                    return;
                } else {
                    if (this.index <= list.size()) {
                        list.add(obj);
                        return;
                    }
                    return;
                }
            }
            int size2 = list.size() + this.index;
            if (size2 >= 0) {
                list.set(size2, obj);
                return;
            }
            return;
        }
        if (obj2 instanceof Object[]) {
            Object[] objArr = (Object[]) obj2;
            int length = objArr.length;
            int i2 = this.index;
            if (i2 >= 0) {
                if (i2 < length) {
                    objArr[i2] = obj;
                    return;
                }
                return;
            } else {
                int i3 = i2 + length;
                if (i3 < 0 || i3 >= length) {
                    return;
                }
                objArr[i3] = obj;
                return;
            }
        }
        if (obj2 != null && obj2.getClass().isArray()) {
            int length2 = Array.getLength(obj2);
            int i4 = this.index;
            if (i4 >= 0) {
                if (i4 < length2) {
                    Array.set(obj2, i4, obj);
                    return;
                }
                return;
            } else {
                int i5 = i4 + length2;
                if (i5 < 0 || i5 >= length2) {
                    return;
                }
                Array.set(obj2, i5, obj);
                return;
            }
        }
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public void setCallback(JSONPath.Context context, BiFunction biFunction) {
        Object obj;
        if (context.parent == null) {
            obj = context.root;
        } else {
            obj = context.parent.value;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            int i = this.index;
            if (i >= 0) {
                if (i < list.size()) {
                    list.set(this.index, biFunction.apply(obj, list.get(this.index)));
                    return;
                }
                return;
            }
            int size = list.size() + this.index;
            if (size >= 0) {
                list.set(size, biFunction.apply(obj, list.get(size)));
                return;
            }
            return;
        }
        if (obj instanceof Object[]) {
            Object[] objArr = (Object[]) obj;
            int i2 = this.index;
            if (i2 >= 0) {
                if (i2 < objArr.length) {
                    objArr[this.index] = biFunction.apply(obj, objArr[i2]);
                    return;
                }
                return;
            }
            int length = objArr.length + i2;
            if (length >= 0) {
                objArr[length] = biFunction.apply(obj, objArr[length]);
                return;
            }
            return;
        }
        if (obj != null && obj.getClass().isArray()) {
            int length2 = Array.getLength(obj);
            int i3 = this.index;
            if (i3 >= 0) {
                if (i3 < length2) {
                    Array.set(obj, this.index, biFunction.apply(obj, Array.get(obj, i3)));
                    return;
                }
                return;
            }
            int i4 = length2 + i3;
            if (i4 >= 0) {
                Array.set(obj, i4, biFunction.apply(obj, Array.get(obj, i4)));
                return;
            }
            return;
        }
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public boolean remove(JSONPath.Context context) {
        Object obj;
        if (context.parent == null) {
            obj = context.root;
        } else {
            obj = context.parent.value;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            int i = this.index;
            if (i >= 0) {
                if (i >= list.size()) {
                    return false;
                }
                list.remove(this.index);
                return true;
            }
            int size = list.size() + this.index;
            if (size < 0) {
                return false;
            }
            list.remove(size);
            return true;
        }
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public void setInt(JSONPath.Context context, int i) {
        Object obj;
        if (context.parent == null) {
            obj = context.root;
        } else {
            obj = context.parent.value;
        }
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            int i2 = this.index;
            if (i2 >= 0) {
                if (i2 < iArr.length) {
                    iArr[i2] = i;
                    return;
                }
                return;
            } else {
                int length = iArr.length + i2;
                if (length >= 0) {
                    iArr[length] = i;
                    return;
                }
                return;
            }
        }
        if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            int i3 = this.index;
            if (i3 >= 0) {
                if (i3 < jArr.length) {
                    jArr[i3] = i;
                    return;
                }
                return;
            } else {
                int length2 = jArr.length + i3;
                if (length2 >= 0) {
                    jArr[length2] = i;
                    return;
                }
                return;
            }
        }
        set(context, Integer.valueOf(i));
    }

    @Override // com.alibaba.fastjson2.JSONPathSegment
    public void setLong(JSONPath.Context context, long j) {
        Object obj;
        if (context.parent == null) {
            obj = context.root;
        } else {
            obj = context.parent.value;
        }
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            int i = this.index;
            if (i >= 0) {
                if (i < iArr.length) {
                    iArr[i] = (int) j;
                    return;
                }
                return;
            } else {
                int length = iArr.length + i;
                if (length >= 0) {
                    iArr[length] = (int) j;
                    return;
                }
                return;
            }
        }
        if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            int i2 = this.index;
            if (i2 >= 0) {
                if (i2 < jArr.length) {
                    jArr[i2] = j;
                    return;
                }
                return;
            } else {
                int length2 = jArr.length + i2;
                if (length2 >= 0) {
                    jArr[length2] = j;
                    return;
                }
                return;
            }
        }
        set(context, Long.valueOf(j));
    }

    /* JADX WARN: Removed duplicated region for block: B:75:0x00e4  */
    @Override // com.alibaba.fastjson2.JSONPathSegment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void accept(com.alibaba.fastjson2.JSONReader r8, com.alibaba.fastjson2.JSONPath.Context r9) {
        /*
            Method dump skipped, instruction units count: 308
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathSegmentIndex.accept(com.alibaba.fastjson2.JSONReader, com.alibaba.fastjson2.JSONPath$Context):void");
    }

    public String toString() {
        int i = this.index;
        int iStringSize = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        byte[] bArr = new byte[iStringSize + 2];
        bArr[0] = 91;
        int i2 = iStringSize + 1;
        IOUtils.getChars(this.index, i2, bArr);
        bArr[i2] = 93;
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            return JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
        }
        return new String(bArr, StandardCharsets.ISO_8859_1);
    }
}
