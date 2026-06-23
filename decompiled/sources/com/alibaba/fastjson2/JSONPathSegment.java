package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
abstract class JSONPathSegment {

    interface EvalSegment {
    }

    public abstract void accept(JSONReader jSONReader, JSONPath.Context context);

    public abstract void eval(JSONPath.Context context);

    JSONPathSegment() {
    }

    public boolean contains(JSONPath.Context context) {
        eval(context);
        return context.value != null;
    }

    public boolean remove(JSONPath.Context context) {
        throw new JSONException("UnsupportedOperation " + getClass());
    }

    public void set(JSONPath.Context context, Object obj) {
        throw new JSONException("UnsupportedOperation " + getClass());
    }

    public void setCallback(JSONPath.Context context, BiFunction biFunction) {
        throw new JSONException("UnsupportedOperation " + getClass());
    }

    public void setInt(JSONPath.Context context, int i) {
        set(context, Integer.valueOf(i));
    }

    public void setLong(JSONPath.Context context, long j) {
        set(context, Long.valueOf(j));
    }

    static final class RandomIndexSegment extends JSONPathSegment {
        public static final RandomIndexSegment INSTANCE = new RandomIndexSegment();
        Random random;

        RandomIndexSegment() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:59:0x00df, code lost:
        
            if (r5.random != null) goto L61;
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x00e1, code lost:
        
            r5.random = new java.util.Random();
         */
        /* JADX WARN: Code restructure failed: missing block: B:61:0x00e8, code lost:
        
            r7.value = r0.get(java.lang.Math.abs(r5.random.nextInt()) % r0.size());
            r7.eval = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:62:0x00ff, code lost:
        
            return;
         */
        /* JADX WARN: Removed duplicated region for block: B:53:0x00bf  */
        @Override // com.alibaba.fastjson2.JSONPathSegment
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void accept(com.alibaba.fastjson2.JSONReader r6, com.alibaba.fastjson2.JSONPath.Context r7) {
            /*
                Method dump skipped, instruction units count: 280
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathSegment.RandomIndexSegment.accept(com.alibaba.fastjson2.JSONReader, com.alibaba.fastjson2.JSONPath$Context):void");
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void eval(JSONPath.Context context) {
            Object obj;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            if (obj instanceof List) {
                List list = (List) obj;
                if (list.isEmpty()) {
                    return;
                }
                if (this.random == null) {
                    this.random = new Random();
                }
                context.value = list.get(Math.abs(this.random.nextInt()) % list.size());
                context.eval = true;
                return;
            }
            if (obj instanceof Object[]) {
                Object[] objArr = (Object[]) obj;
                if (objArr.length == 0) {
                    return;
                }
                if (this.random == null) {
                    this.random = new Random();
                }
                context.value = objArr[this.random.nextInt() % objArr.length];
                context.eval = true;
                return;
            }
            throw new JSONException("TODO");
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
                if (this.random == null) {
                    this.random = new Random();
                }
                int iAbs = Math.abs(this.random.nextInt()) % list.size();
                list.set(iAbs, biFunction.apply(list, list.get(iAbs)));
                return;
            }
            throw new JSONException("UnsupportedOperation ");
        }
    }

    static final class RangeIndexSegment extends JSONPathSegment {
        final int begin;
        final int end;

        public RangeIndexSegment(int i, int i2) {
            this.begin = i;
            this.end = i2;
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void eval(JSONPath.Context context) {
            Object obj;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            JSONArray jSONArray = new JSONArray();
            int i = 0;
            if (obj instanceof List) {
                List list = (List) obj;
                int size = list.size();
                while (i < size) {
                    int i2 = this.begin;
                    int i3 = i2 >= 0 ? i : i - size;
                    if (i3 >= i2 && i3 < this.end) {
                        jSONArray.add(list.get(i));
                    }
                    i++;
                }
                context.value = jSONArray;
                context.eval = true;
                return;
            }
            if (obj instanceof Object[]) {
                Object[] objArr = (Object[]) obj;
                while (i < objArr.length) {
                    int i4 = this.begin;
                    if ((i >= i4 && i <= this.end) || (i - objArr.length > i4 && i - objArr.length <= this.end)) {
                        jSONArray.add(objArr[i]);
                    }
                    i++;
                }
                context.value = jSONArray;
                context.eval = true;
                return;
            }
            throw new JSONException("TODO");
        }

        /* JADX WARN: Removed duplicated region for block: B:77:0x00e5  */
        @Override // com.alibaba.fastjson2.JSONPathSegment
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void accept(com.alibaba.fastjson2.JSONReader r6, com.alibaba.fastjson2.JSONPath.Context r7) {
            /*
                Method dump skipped, instruction units count: 320
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathSegment.RangeIndexSegment.accept(com.alibaba.fastjson2.JSONReader, com.alibaba.fastjson2.JSONPath$Context):void");
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void set(JSONPath.Context context, Object obj) {
            Object obj2;
            if (context.parent == null) {
                obj2 = context.root;
            } else {
                obj2 = context.parent.value;
            }
            int i = 0;
            if (obj2 instanceof List) {
                List list = (List) obj2;
                int size = list.size();
                while (i < size) {
                    int i2 = this.begin;
                    int i3 = i2 >= 0 ? i : i - size;
                    if (i3 >= i2 && i3 < this.end) {
                        list.set(i, obj);
                    }
                    i++;
                }
                return;
            }
            if (obj2 != null && obj2.getClass().isArray()) {
                int length = Array.getLength(obj2);
                while (i < length) {
                    int i4 = this.begin;
                    int i5 = i4 >= 0 ? i : i - length;
                    if (i5 >= i4 && i5 < this.end) {
                        Array.set(obj2, i, obj);
                    }
                    i++;
                }
                return;
            }
            throw new JSONException("UnsupportedOperation " + getClass());
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void setCallback(JSONPath.Context context, BiFunction biFunction) {
            Object obj;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            int i = 0;
            if (obj instanceof List) {
                List list = (List) obj;
                int size = list.size();
                while (i < size) {
                    int i2 = this.begin;
                    int i3 = i2 >= 0 ? i : i - size;
                    if (i3 >= i2 && i3 < this.end) {
                        list.set(i3, biFunction.apply(list, list.get(i)));
                    }
                    i++;
                }
                return;
            }
            if (obj != null && obj.getClass().isArray()) {
                int length = Array.getLength(obj);
                while (i < length) {
                    int i4 = this.begin;
                    int i5 = i4 >= 0 ? i : i - length;
                    if (i5 >= i4 && i5 < this.end) {
                        Array.set(obj, i, biFunction.apply(obj, Array.get(obj, i)));
                    }
                    i++;
                }
                return;
            }
            throw new JSONException("UnsupportedOperation " + getClass());
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
                int size = list.size();
                int i = 0;
                for (int i2 = size - 1; i2 >= 0; i2--) {
                    int i3 = this.begin;
                    int i4 = i3 >= 0 ? i2 : i2 - size;
                    if (i4 >= i3 && i4 < this.end) {
                        list.remove(i2);
                        i++;
                    }
                }
                return i > 0;
            }
            throw new JSONException("UnsupportedOperation " + getClass());
        }
    }

    static final class MultiIndexSegment extends JSONPathSegment {
        final int[] indexes;

        public MultiIndexSegment(int[] iArr) {
            this.indexes = iArr;
        }

        /* JADX WARN: Removed duplicated region for block: B:41:0x008e  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x0094  */
        @Override // com.alibaba.fastjson2.JSONPathSegment
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void eval(com.alibaba.fastjson2.JSONPath.Context r13) {
            /*
                r12 = this;
                com.alibaba.fastjson2.JSONPath$Context r0 = r13.parent
                if (r0 != 0) goto L7
                java.lang.Object r0 = r13.root
                goto Lb
            L7:
                com.alibaba.fastjson2.JSONPath$Context r0 = r13.parent
                java.lang.Object r0 = r0.value
            Lb:
                com.alibaba.fastjson2.JSONArray r1 = new com.alibaba.fastjson2.JSONArray
                r1.<init>()
                boolean r2 = r0 instanceof com.alibaba.fastjson2.JSONPath.Sequence
                r3 = 0
                if (r2 == 0) goto L4d
                com.alibaba.fastjson2.JSONPath$Sequence r0 = (com.alibaba.fastjson2.JSONPath.Sequence) r0
                java.util.List r0 = r0.values
                int r2 = r0.size()
            L1d:
                if (r3 >= r2) goto L49
                java.lang.Object r4 = r0.get(r3)
                r13.value = r4
                com.alibaba.fastjson2.JSONPath$Context r5 = new com.alibaba.fastjson2.JSONPath$Context
                com.alibaba.fastjson2.JSONPath r6 = r13.path
                com.alibaba.fastjson2.JSONPathSegment r8 = r13.current
                com.alibaba.fastjson2.JSONPathSegment r9 = r13.next
                long r10 = r13.readerFeatures
                r7 = r13
                r5.<init>(r6, r7, r8, r9, r10)
                r12.eval(r5)
                java.lang.Object r13 = r5.value
                boolean r4 = r13 instanceof java.util.Collection
                if (r4 == 0) goto L42
                java.util.Collection r13 = (java.util.Collection) r13
                r1.addAll(r13)
                goto L45
            L42:
                r1.add(r13)
            L45:
                int r3 = r3 + 1
                r13 = r7
                goto L1d
            L49:
                r7 = r13
                r7.value = r1
                return
            L4d:
                r7 = r13
                int[] r13 = r12.indexes
                int r2 = r13.length
            L51:
                if (r3 >= r2) goto L9a
                r4 = r13[r3]
                boolean r5 = r0 instanceof java.util.List
                if (r5 == 0) goto L75
                r5 = r0
                java.util.List r5 = (java.util.List) r5
                if (r4 < 0) goto L69
                int r6 = r5.size()
                if (r4 >= r6) goto L97
                java.lang.Object r4 = r5.get(r4)
                goto L8a
            L69:
                int r6 = r5.size()
                int r6 = r6 + r4
                if (r6 < 0) goto L97
                java.lang.Object r4 = r5.get(r6)
                goto L8a
            L75:
                boolean r5 = r0 instanceof java.lang.Object[]
                if (r5 == 0) goto L97
                r5 = r0
                java.lang.Object[] r5 = (java.lang.Object[]) r5
                if (r4 < 0) goto L84
                int r6 = r5.length
                if (r4 >= r6) goto L97
                r4 = r5[r4]
                goto L8a
            L84:
                int r6 = r5.length
                int r6 = r6 + r4
                if (r6 < 0) goto L97
                r4 = r5[r6]
            L8a:
                boolean r5 = r4 instanceof java.util.Collection
                if (r5 == 0) goto L94
                java.util.Collection r4 = (java.util.Collection) r4
                r1.addAll(r4)
                goto L97
            L94:
                r1.add(r4)
            L97:
                int r3 = r3 + 1
                goto L51
            L9a:
                r7.value = r1
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathSegment.MultiIndexSegment.eval(com.alibaba.fastjson2.JSONPath$Context):void");
        }

        /* JADX WARN: Code restructure failed: missing block: B:63:0x00da, code lost:
        
            r6.value = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:64:0x00dc, code lost:
        
            return;
         */
        /* JADX WARN: Removed duplicated region for block: B:54:0x00ac  */
        @Override // com.alibaba.fastjson2.JSONPathSegment
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void accept(com.alibaba.fastjson2.JSONReader r5, com.alibaba.fastjson2.JSONPath.Context r6) {
            /*
                Method dump skipped, instruction units count: 246
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathSegment.MultiIndexSegment.accept(com.alibaba.fastjson2.JSONReader, com.alibaba.fastjson2.JSONPath$Context):void");
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
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    for (int i2 : this.indexes) {
                        if (i2 == i) {
                            list.set(i, biFunction.apply(obj, list.get(i)));
                        }
                    }
                }
                return;
            }
            if (obj != null && obj.getClass().isArray()) {
                int length = Array.getLength(obj);
                for (int i3 = 0; i3 < length; i3++) {
                    for (int i4 : this.indexes) {
                        if (i4 == i3) {
                            Array.set(obj, i3, biFunction.apply(obj, Array.get(obj, i3)));
                        }
                    }
                }
                return;
            }
            throw new JSONException("UnsupportedOperation " + getClass());
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
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    for (int i2 : this.indexes) {
                        if (i2 == i) {
                            list.set(i, obj);
                        }
                    }
                }
                return;
            }
            if (obj2 != null && obj2.getClass().isArray()) {
                int length = Array.getLength(obj2);
                for (int i3 = 0; i3 < length; i3++) {
                    for (int i4 : this.indexes) {
                        if (i4 == i3) {
                            Array.set(obj2, i3, obj);
                        }
                    }
                }
                return;
            }
            throw new JSONException("UnsupportedOperation " + getClass());
        }
    }

    static final class MultiNameSegment extends JSONPathSegment {
        final long[] nameHashCodes;
        final Set<String> nameSet = new HashSet();
        final String[] names;

        public MultiNameSegment(String[] strArr) {
            this.names = strArr;
            this.nameHashCodes = new long[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                this.nameHashCodes[i] = Fnv.hashCode64(strArr[i]);
                this.nameSet.add(strArr[i]);
            }
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void eval(JSONPath.Context context) {
            Object obj;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            int i = 0;
            if (obj instanceof Map) {
                Map map = (Map) obj;
                JSONArray jSONArray = new JSONArray(this.names.length);
                String[] strArr = this.names;
                int length = strArr.length;
                while (i < length) {
                    jSONArray.add(map.get(strArr[i]));
                    i++;
                }
                context.value = jSONArray;
                return;
            }
            if (obj instanceof Collection) {
                context.value = obj;
                return;
            }
            ObjectWriter objectWriter = context.path.getWriterContext().provider.getObjectWriter((Class) obj.getClass());
            JSONArray jSONArray2 = new JSONArray(this.names.length);
            while (i < this.names.length) {
                FieldWriter fieldWriter = objectWriter.getFieldWriter(this.nameHashCodes[i]);
                jSONArray2.add(fieldWriter != null ? fieldWriter.getFieldValue(obj) : null);
                i++;
            }
            context.value = jSONArray2;
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            if (context.parent != null && (context.parent.eval || (context.parent.current instanceof JSONPathFilter) || (context.parent.current instanceof MultiIndexSegment))) {
                eval(context);
                return;
            }
            Object any = jSONReader.readAny();
            if (any instanceof Map) {
                Map map = (Map) any;
                JSONArray jSONArray = new JSONArray(this.names.length);
                for (String str : this.names) {
                    jSONArray.add(map.get(str));
                }
                context.value = jSONArray;
                return;
            }
            if (any instanceof Collection) {
                if (context.next == null) {
                    Collection collection = (Collection) any;
                    JSONArray jSONArray2 = new JSONArray(collection.size());
                    for (Object obj : collection) {
                        if (obj instanceof Map) {
                            Map map2 = (Map) obj;
                            JSONArray jSONArray3 = new JSONArray(this.names.length);
                            for (String str2 : this.names) {
                                jSONArray3.add(map2.get(str2));
                            }
                            jSONArray2.add(jSONArray3);
                        }
                    }
                    context.value = jSONArray2;
                    return;
                }
                context.value = any;
                return;
            }
            throw new JSONException("UnsupportedOperation " + getClass());
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void set(JSONPath.Context context, Object obj) {
            Object obj2;
            if (context.parent == null) {
                obj2 = context.root;
            } else {
                obj2 = context.parent.value;
            }
            int i = 0;
            if (obj2 instanceof Map) {
                Map map = (Map) obj2;
                String[] strArr = this.names;
                int length = strArr.length;
                while (i < length) {
                    map.put(strArr[i], obj);
                    i++;
                }
                return;
            }
            ObjectReader objectReader = context.path.getReaderContext().provider.getObjectReader(obj2.getClass());
            if (objectReader instanceof ObjectReaderBean) {
                long[] jArr = this.nameHashCodes;
                int length2 = jArr.length;
                while (i < length2) {
                    FieldReader fieldReader = objectReader.getFieldReader(jArr[i]);
                    if (fieldReader != null) {
                        fieldReader.accept(obj2, obj);
                    }
                    i++;
                }
                return;
            }
            throw new JSONException("UnsupportedOperation " + getClass());
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void setCallback(JSONPath.Context context, BiFunction biFunction) {
            Object obj;
            FieldReader fieldReader;
            Object fieldValue;
            Object objApply;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            int i = 0;
            if (obj instanceof Map) {
                Map map = (Map) obj;
                String[] strArr = this.names;
                int length = strArr.length;
                while (i < length) {
                    String str = strArr[i];
                    Object obj2 = map.get(str);
                    Object objApply2 = biFunction.apply(map, obj2);
                    if (objApply2 != obj2) {
                        map.put(str, objApply2);
                    }
                    i++;
                }
                return;
            }
            ObjectWriter objectWriter = context.path.getWriterContext().provider.getObjectWriter((Class) obj.getClass());
            if (objectWriter instanceof ObjectWriterAdapter) {
                ObjectReader objectReader = context.path.getReaderContext().provider.getObjectReader(obj.getClass());
                if (objectReader instanceof ObjectReaderBean) {
                    long[] jArr = this.nameHashCodes;
                    int length2 = jArr.length;
                    while (i < length2) {
                        long j = jArr[i];
                        FieldWriter fieldWriter = objectWriter.getFieldWriter(j);
                        if (fieldWriter != null && (fieldReader = objectReader.getFieldReader(j)) != null && (objApply = biFunction.apply(obj, (fieldValue = fieldWriter.getFieldValue(obj)))) != fieldValue) {
                            fieldReader.accept(obj, objApply);
                        }
                        i++;
                    }
                    return;
                }
            }
            throw new JSONException("UnsupportedOperation " + getClass());
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
                Map map = (Map) obj;
                int i = 0;
                for (String str : this.names) {
                    if (map.remove(str) != null) {
                        i++;
                    }
                }
                return i > 0;
            }
            ObjectReader objectReader = context.path.getReaderContext().provider.getObjectReader(obj.getClass());
            if (objectReader instanceof ObjectReaderBean) {
                int i2 = 0;
                for (long j : this.nameHashCodes) {
                    FieldReader fieldReader = objectReader.getFieldReader(j);
                    if (fieldReader != null) {
                        fieldReader.accept(obj, (Object) null);
                        i2++;
                    }
                }
                return i2 > 0;
            }
            throw new JSONException("UnsupportedOperation " + getClass());
        }
    }

    static final class AllSegment extends JSONPathSegment {
        static final AllSegment INSTANCE = new AllSegment(false);
        static final AllSegment INSTANCE_ARRAY = new AllSegment(true);
        final boolean array;

        AllSegment(boolean z) {
            this.array = z;
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
                context.value = null;
                context.eval = true;
                return;
            }
            if (obj instanceof Map) {
                Map map = (Map) obj;
                JSONArray jSONArray = new JSONArray(map.size());
                for (Object obj2 : map.values()) {
                    if (this.array && (obj2 instanceof Collection)) {
                        jSONArray.addAll((Collection) obj2);
                    } else {
                        jSONArray.add(obj2);
                    }
                }
                if (context.next != null) {
                    context.value = new JSONPath.Sequence(jSONArray);
                } else {
                    context.value = jSONArray;
                }
                context.eval = true;
                return;
            }
            int i = 0;
            if (obj instanceof List) {
                List list = (List) obj;
                JSONArray jSONArray2 = new JSONArray(list.size());
                if (context.next == null && !this.array) {
                    while (i < list.size()) {
                        Object obj3 = list.get(i);
                        if (obj3 instanceof Map) {
                            jSONArray2.addAll(((Map) obj3).values());
                        } else {
                            jSONArray2.add(obj3);
                        }
                        i++;
                    }
                    context.value = jSONArray2;
                    context.eval = true;
                    return;
                }
                if (context.next != null) {
                    context.value = new JSONPath.Sequence(list);
                } else {
                    context.value = obj;
                }
                context.eval = true;
                return;
            }
            if (obj instanceof Collection) {
                context.value = obj;
                context.eval = true;
                return;
            }
            if (obj instanceof JSONPath.Sequence) {
                List list2 = ((JSONPath.Sequence) obj).values;
                JSONArray jSONArray3 = new JSONArray(list2.size());
                if (context.next == null) {
                    while (i < list2.size()) {
                        Object obj4 = list2.get(i);
                        if ((obj4 instanceof Map) && !this.array) {
                            jSONArray3.addAll(((Map) obj4).values());
                        } else if (obj4 instanceof Collection) {
                            jSONArray3.addAll((Collection) obj4);
                        } else {
                            jSONArray3.add(obj4);
                        }
                        i++;
                    }
                    context.value = jSONArray3;
                    context.eval = true;
                    return;
                }
                context.value = new JSONPath.Sequence(list2);
                context.eval = true;
                return;
            }
            List<FieldWriter> fieldWriters = context.path.getWriterContext().provider.getObjectWriter((Class) obj.getClass()).getFieldWriters();
            int size = fieldWriters.size();
            JSONArray jSONArray4 = new JSONArray(size);
            while (i < size) {
                jSONArray4.add(fieldWriters.get(i).getFieldValue(obj));
                i++;
            }
            context.value = jSONArray4;
            context.eval = true;
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
                ((Map) obj).clear();
                return true;
            }
            if (obj instanceof Collection) {
                ((Collection) obj).clear();
                return true;
            }
            throw new JSONException("UnsupportedOperation " + getClass());
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void set(JSONPath.Context context, Object obj) {
            Object obj2;
            if (context.parent == null) {
                obj2 = context.root;
            } else {
                obj2 = context.parent.value;
            }
            if (obj2 instanceof Map) {
                Iterator it = ((Map) obj2).entrySet().iterator();
                while (it.hasNext()) {
                    ((Map.Entry) it.next()).setValue(obj);
                }
            } else {
                if (obj2 instanceof List) {
                    Collections.fill((List) obj2, obj);
                    return;
                }
                if (obj2 != null && obj2.getClass().isArray()) {
                    int length = Array.getLength(obj2);
                    for (int i = 0; i < length; i++) {
                        Array.set(obj2, i, obj);
                    }
                    return;
                }
                throw new JSONException("UnsupportedOperation " + getClass());
            }
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
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    Object value = entry.getValue();
                    Object objApply = biFunction.apply(obj, value);
                    if (objApply != value) {
                        entry.setValue(objApply);
                    }
                }
                return;
            }
            int i = 0;
            if (obj instanceof List) {
                List list = (List) obj;
                while (i < list.size()) {
                    Object obj2 = list.get(i);
                    Object objApply2 = biFunction.apply(obj, obj2);
                    if (objApply2 != obj2) {
                        list.set(i, objApply2);
                    }
                    i++;
                }
                return;
            }
            if (obj != null && obj.getClass().isArray()) {
                int length = Array.getLength(obj);
                while (i < length) {
                    Object obj3 = Array.get(obj, i);
                    Object objApply3 = biFunction.apply(obj, obj3);
                    if (objApply3 != obj3) {
                        Array.set(obj, i, objApply3);
                    }
                    i++;
                }
                return;
            }
            throw new JSONException("UnsupportedOperation " + getClass());
        }

        /* JADX WARN: Removed duplicated region for block: B:69:0x00e2  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x00f8  */
        @Override // com.alibaba.fastjson2.JSONPathSegment
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void accept(com.alibaba.fastjson2.JSONReader r9, com.alibaba.fastjson2.JSONPath.Context r10) {
            /*
                Method dump skipped, instruction units count: 360
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathSegment.AllSegment.accept(com.alibaba.fastjson2.JSONReader, com.alibaba.fastjson2.JSONPath$Context):void");
        }
    }

    static final class SelfSegment extends JSONPathSegment {
        static final SelfSegment INSTANCE = new SelfSegment();

        private SelfSegment() {
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            context.value = jSONReader.readAny();
            context.eval = true;
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void eval(JSONPath.Context context) {
            Object obj;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            context.value = obj;
        }
    }

    static final class RootSegment extends JSONPathSegment {
        static final RootSegment INSTANCE = new RootSegment();

        private RootSegment() {
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            if (context.parent != null) {
                throw new JSONException("not support operation");
            }
            context.value = jSONReader.readAny();
            context.eval = true;
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void eval(JSONPath.Context context) {
            Object obj;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.root;
            }
            context.value = obj;
        }
    }

    static final class CycleNameSegment extends JSONPathSegment {
        final String name;
        final long nameHashCode;
        static final long HASH_STAR = Fnv.hashCode64("*");
        static final long HASH_EMPTY = Fnv.hashCode64("");

        public CycleNameSegment(String str, long j) {
            this.name = str;
            this.nameHashCode = j;
        }

        public String toString() {
            return ".." + this.name;
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public boolean remove(JSONPath.Context context) {
            Object obj;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            new LoopRemove(context).accept(obj);
            context.eval = true;
            return true;
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void eval(JSONPath.Context context) {
            Object obj;
            Consumer mapLoop;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            JSONArray jSONArray = new JSONArray();
            if (shouldRecursive()) {
                mapLoop = new MapRecursive(context, jSONArray, 0);
            } else {
                mapLoop = new MapLoop(context, jSONArray);
            }
            mapLoop.accept(obj);
            if (jSONArray.size() == 1 && (jSONArray.get(0) instanceof Collection)) {
                context.value = jSONArray.get(0);
            } else {
                context.value = jSONArray;
            }
            if ((context.value instanceof List) && (context.next instanceof JSONPathFilter)) {
                context.value = new JSONPath.Sequence((List) context.value);
            }
            context.eval = true;
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void set(JSONPath.Context context, Object obj) {
            Object obj2;
            if (context.parent == null) {
                obj2 = context.root;
            } else {
                obj2 = context.parent.value;
            }
            new LoopSet(context, obj).accept(obj2);
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void setCallback(JSONPath.Context context, BiFunction biFunction) {
            Object obj;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            new LoopCallback(context, biFunction).accept(obj);
        }

        boolean shouldRecursive() {
            long j = this.nameHashCode;
            return j == HASH_STAR || j == HASH_EMPTY;
        }

        class MapLoop implements BiConsumer, Consumer {
            final JSONPath.Context context;
            final List values;

            public MapLoop(JSONPath.Context context, List list) {
                this.context = context;
                this.values = list;
            }

            @Override // java.util.function.BiConsumer
            public void accept(Object obj, Object obj2) {
                if (CycleNameSegment.this.name.equals(obj)) {
                    this.values.add(obj2);
                }
                if (obj2 instanceof Map) {
                    ((Map) obj2).forEach(this);
                } else if (obj2 instanceof List) {
                    ((List) obj2).forEach(this);
                } else if (CycleNameSegment.this.nameHashCode == CycleNameSegment.HASH_STAR) {
                    this.values.add(obj2);
                }
            }

            @Override // java.util.function.Consumer
            public void accept(Object obj) {
                if (obj == null) {
                    return;
                }
                if (obj instanceof Map) {
                    ((Map) obj).forEach(this);
                    return;
                }
                if (obj instanceof List) {
                    ((List) obj).forEach(this);
                    return;
                }
                ObjectWriter objectWriter = this.context.path.getWriterContext().getObjectWriter(obj.getClass());
                if (!(objectWriter instanceof ObjectWriterAdapter)) {
                    if (CycleNameSegment.this.nameHashCode == CycleNameSegment.HASH_STAR) {
                        this.values.add(obj);
                        return;
                    }
                    return;
                }
                FieldWriter fieldWriter = objectWriter.getFieldWriter(CycleNameSegment.this.nameHashCode);
                if (fieldWriter != null) {
                    Object fieldValue = fieldWriter.getFieldValue(obj);
                    if (fieldValue != null) {
                        this.values.add(fieldValue);
                        return;
                    }
                    return;
                }
                for (int i = 0; i < objectWriter.getFieldWriters().size(); i++) {
                    accept(objectWriter.getFieldWriters().get(i).getFieldValue(obj));
                }
            }
        }

        class MapRecursive implements Consumer {
            static final int maxLevel = 2048;
            final JSONPath.Context context;
            final int level;
            final List values;

            public MapRecursive(JSONPath.Context context, List list, int i) {
                this.context = context;
                this.values = list;
                this.level = i;
            }

            @Override // java.util.function.Consumer
            public void accept(Object obj) {
                recursive(obj, this.values, this.level);
            }

            private void recursive(final Object obj, List list, int i) {
                Object arrayList;
                if (i >= 2048) {
                    throw new JSONException("level too large");
                }
                if (obj instanceof Map) {
                    Collection collectionValues = ((Map) obj).values();
                    if (CycleNameSegment.this.nameHashCode == CycleNameSegment.HASH_STAR) {
                        list.addAll(collectionValues);
                    } else if (CycleNameSegment.this.nameHashCode == CycleNameSegment.HASH_EMPTY) {
                        list.add(obj);
                    }
                    collectionValues.forEach(this);
                    return;
                }
                if (obj instanceof Collection) {
                    Collection collection = (Collection) obj;
                    if (CycleNameSegment.this.nameHashCode == CycleNameSegment.HASH_STAR) {
                        list.addAll(collection);
                    } else if (CycleNameSegment.this.nameHashCode == CycleNameSegment.HASH_EMPTY) {
                        list.add(obj);
                    }
                    collection.forEach(this);
                    return;
                }
                if (obj != null) {
                    ObjectWriter objectWriter = this.context.path.getWriterContext().getObjectWriter(obj.getClass());
                    if (objectWriter instanceof ObjectWriterAdapter) {
                        List<FieldWriter> fieldWriters = ((ObjectWriterAdapter) objectWriter).getFieldWriters();
                        if (fieldWriters == null || fieldWriters.isEmpty()) {
                            arrayList = new ArrayList();
                        } else {
                            arrayList = fieldWriters.stream().filter(new Predicate() { // from class: com.alibaba.fastjson2.JSONPathSegment$CycleNameSegment$MapRecursive$$ExternalSyntheticLambda0
                                @Override // java.util.function.Predicate
                                public final boolean test(Object obj2) {
                                    return Objects.nonNull((FieldWriter) obj2);
                                }
                            }).map(new Function() { // from class: com.alibaba.fastjson2.JSONPathSegment$CycleNameSegment$MapRecursive$$ExternalSyntheticLambda1
                                @Override // java.util.function.Function
                                public final Object apply(Object obj2) {
                                    return ((FieldWriter) obj2).getFieldValue(obj);
                                }
                            }).collect(Collectors.toList());
                        }
                        recursive(arrayList, list, i + 1);
                    }
                }
            }
        }

        class LoopRemove {
            final JSONPath.Context context;

            public LoopRemove(JSONPath.Context context) {
                this.context = context;
            }

            public void accept(Object obj) {
                FieldReader fieldReader;
                if (obj instanceof Map) {
                    Iterator it = ((Map) obj).entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        if (CycleNameSegment.this.name.equals(entry.getKey())) {
                            it.remove();
                            this.context.eval = true;
                        } else {
                            Object value = entry.getValue();
                            if (value != null) {
                                accept(value);
                            }
                        }
                    }
                    return;
                }
                if (obj instanceof Collection) {
                    for (Object obj2 : (List) obj) {
                        if (obj2 != null) {
                            accept(obj2);
                        }
                    }
                    return;
                }
                Class<?> cls = obj.getClass();
                ObjectReader objectReader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(cls);
                if ((objectReader instanceof ObjectReaderBean) && (fieldReader = objectReader.getFieldReader(CycleNameSegment.this.nameHashCode)) != null) {
                    fieldReader.accept(obj, (Object) null);
                    this.context.eval = true;
                } else {
                    Iterator<FieldWriter> it2 = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter((Class) cls).getFieldWriters().iterator();
                    while (it2.hasNext()) {
                        accept(it2.next().getFieldValue(obj));
                    }
                }
            }
        }

        class LoopSet {
            final JSONPath.Context context;
            final Object value;

            public LoopSet(JSONPath.Context context, Object obj) {
                this.context = context;
                this.value = obj;
            }

            public void accept(Object obj) {
                FieldReader fieldReader;
                if (obj instanceof Map) {
                    for (Map.Entry entry : ((Map) obj).entrySet()) {
                        if (CycleNameSegment.this.name.equals(entry.getKey())) {
                            entry.setValue(this.value);
                            this.context.eval = true;
                        } else {
                            Object value = entry.getValue();
                            if (value != null) {
                                accept(value);
                            }
                        }
                    }
                    return;
                }
                if (obj instanceof Collection) {
                    for (Object obj2 : (List) obj) {
                        if (obj2 != null) {
                            accept(obj2);
                        }
                    }
                    return;
                }
                Class<?> cls = obj.getClass();
                ObjectReader objectReader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(cls);
                if ((objectReader instanceof ObjectReaderBean) && (fieldReader = objectReader.getFieldReader(CycleNameSegment.this.nameHashCode)) != null) {
                    fieldReader.accept(obj, this.value);
                    this.context.eval = true;
                } else {
                    Iterator<FieldWriter> it = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter((Class) cls).getFieldWriters().iterator();
                    while (it.hasNext()) {
                        accept(it.next().getFieldValue(obj));
                    }
                }
            }
        }

        class LoopCallback {
            final BiFunction callback;
            final JSONPath.Context context;

            public LoopCallback(JSONPath.Context context, BiFunction biFunction) {
                this.context = context;
                this.callback = biFunction;
            }

            public void accept(Object obj) {
                if (obj instanceof Map) {
                    for (Map.Entry entry : ((Map) obj).entrySet()) {
                        Object value = entry.getValue();
                        if (CycleNameSegment.this.name.equals(entry.getKey())) {
                            entry.setValue(this.callback.apply(obj, value));
                            this.context.eval = true;
                        } else if (value != null) {
                            accept(value);
                        }
                    }
                    return;
                }
                if (obj instanceof Collection) {
                    for (Object obj2 : (List) obj) {
                        if (obj2 != null) {
                            accept(obj2);
                        }
                    }
                    return;
                }
                Class<?> cls = obj.getClass();
                ObjectReader objectReader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(cls);
                ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter((Class) cls);
                if (objectReader instanceof ObjectReaderBean) {
                    FieldReader fieldReader = objectReader.getFieldReader(CycleNameSegment.this.nameHashCode);
                    FieldWriter fieldWriter = objectWriter.getFieldWriter(CycleNameSegment.this.nameHashCode);
                    if (fieldWriter != null && fieldReader != null) {
                        fieldReader.accept(obj, this.callback.apply(obj, fieldWriter.getFieldValue(obj)));
                        this.context.eval = true;
                        return;
                    }
                }
                Iterator<FieldWriter> it = objectWriter.getFieldWriters().iterator();
                while (it.hasNext()) {
                    accept(it.next().getFieldValue(obj));
                }
            }
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            JSONArray jSONArray = new JSONArray();
            accept(jSONReader, context, jSONArray);
            context.value = jSONArray;
            context.eval = true;
        }

        /* JADX WARN: Removed duplicated region for block: B:136:0x0107 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:137:0x0129 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:143:0x00fb A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:149:0x0085 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:86:0x011c  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x0122  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void accept(com.alibaba.fastjson2.JSONReader r10, com.alibaba.fastjson2.JSONPath.Context r11, java.util.List<java.lang.Object> r12) {
            /*
                Method dump skipped, instruction units count: 378
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathSegment.CycleNameSegment.accept(com.alibaba.fastjson2.JSONReader, com.alibaba.fastjson2.JSONPath$Context, java.util.List):void");
        }
    }

    static final class MinSegment extends JSONPathSegment implements EvalSegment {
        static final MinSegment INSTANCE = new MinSegment();

        MinSegment() {
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            eval(context);
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
                return;
            }
            Object obj2 = null;
            if (obj instanceof Collection) {
                for (Object obj3 : (Collection) obj) {
                    if (obj3 != null && (obj2 == null || TypeUtils.compare(obj2, obj3) > 0)) {
                        obj2 = obj3;
                    }
                }
            } else if (obj instanceof Object[]) {
                for (Object obj4 : (Object[]) obj) {
                    if (obj4 != null && (obj2 == null || TypeUtils.compare(obj2, obj4) > 0)) {
                        obj2 = obj4;
                    }
                }
            } else if (obj instanceof JSONPath.Sequence) {
                for (Object obj5 : ((JSONPath.Sequence) obj).values) {
                    if (obj5 != null && (obj2 == null || TypeUtils.compare(obj2, obj5) > 0)) {
                        obj2 = obj5;
                    }
                }
            } else {
                throw new UnsupportedOperationException();
            }
            context.value = obj2;
            context.eval = true;
        }
    }

    static final class MaxSegment extends JSONPathSegment implements EvalSegment {
        static final MaxSegment INSTANCE = new MaxSegment();

        MaxSegment() {
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            eval(context);
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
                return;
            }
            Object obj2 = null;
            if (obj instanceof Collection) {
                for (Object obj3 : (Collection) obj) {
                    if (obj3 != null && (obj2 == null || TypeUtils.compare(obj2, obj3) < 0)) {
                        obj2 = obj3;
                    }
                }
            } else if (obj instanceof Object[]) {
                for (Object obj4 : (Object[]) obj) {
                    if (obj4 != null && (obj2 == null || TypeUtils.compare(obj2, obj4) < 0)) {
                        obj2 = obj4;
                    }
                }
            } else if (obj instanceof JSONPath.Sequence) {
                for (Object obj5 : ((JSONPath.Sequence) obj).values) {
                    if (obj5 != null && (obj2 == null || TypeUtils.compare(obj2, obj5) < 0)) {
                        obj2 = obj5;
                    }
                }
            } else {
                throw new UnsupportedOperationException();
            }
            context.value = obj2;
            context.eval = true;
        }
    }

    static final class SumSegment extends JSONPathSegment implements EvalSegment {
        static final SumSegment INSTANCE = new SumSegment();

        SumSegment() {
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            eval(context);
        }

        static Number add(Number number, Number number2) {
            boolean z = (number instanceof Byte) || (number instanceof Short) || (number instanceof Integer) || (number instanceof Long);
            boolean z2 = (number2 instanceof Byte) || (number2 instanceof Short) || (number2 instanceof Integer) || (number2 instanceof Long);
            if (z && z2) {
                return Long.valueOf(number.longValue() + number2.longValue());
            }
            boolean z3 = (number instanceof Float) || (number instanceof Double);
            boolean z4 = (number2 instanceof Float) || (number2 instanceof Double);
            if (z3 || z4) {
                return Double.valueOf(number.doubleValue() + number2.doubleValue());
            }
            if ((number instanceof BigDecimal) || (number2 instanceof BigDecimal)) {
                return TypeUtils.toBigDecimal(number).add(TypeUtils.toBigDecimal(number2));
            }
            if ((number instanceof BigInteger) || (number2 instanceof BigInteger)) {
                return TypeUtils.toBigInteger(number).add(TypeUtils.toBigInteger(number2));
            }
            throw new JSONException("not support operation");
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
                return;
            }
            Number numberAdd = 0;
            if (obj instanceof Collection) {
                for (Object obj2 : (Collection) obj) {
                    if (obj2 != null) {
                        numberAdd = add(numberAdd, (Number) obj2);
                    }
                }
            } else if (obj instanceof Object[]) {
                for (Object obj3 : (Object[]) obj) {
                    if (obj3 != null) {
                        numberAdd = add(numberAdd, (Number) obj3);
                    }
                }
            } else if (obj instanceof JSONPath.Sequence) {
                for (Object obj4 : ((JSONPath.Sequence) obj).values) {
                    if (obj4 != null) {
                        numberAdd = add(numberAdd, (Number) obj4);
                    }
                }
            } else {
                throw new UnsupportedOperationException();
            }
            context.value = numberAdd;
            context.eval = true;
        }
    }

    static final class LengthSegment extends JSONPathSegment implements EvalSegment {
        static final LengthSegment INSTANCE = new LengthSegment();

        LengthSegment() {
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            if (context.parent == null) {
                context.root = jSONReader.readAny();
                context.eval = true;
            }
            eval(context);
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void eval(JSONPath.Context context) {
            Object obj;
            int size;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            if (obj == null) {
                return;
            }
            if (obj instanceof Collection) {
                size = ((Collection) obj).size();
            } else if (obj.getClass().isArray()) {
                size = Array.getLength(obj);
            } else if (obj instanceof Map) {
                size = ((Map) obj).size();
            } else if (obj instanceof String) {
                size = ((String) obj).length();
            } else {
                size = obj instanceof JSONPath.Sequence ? ((JSONPath.Sequence) obj).values.size() : 1;
            }
            context.value = Integer.valueOf(size);
        }
    }

    static final class ValuesSegment extends JSONPathSegment implements EvalSegment {
        static final ValuesSegment INSTANCE = new ValuesSegment();

        ValuesSegment() {
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            eval(context);
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
                context.value = null;
                context.eval = true;
            } else {
                if (obj instanceof Map) {
                    context.value = new JSONArray((Collection<?>) ((Map) obj).values());
                    context.eval = true;
                    return;
                }
                throw new JSONException("TODO");
            }
        }
    }

    static final class KeysSegment extends JSONPathSegment implements EvalSegment {
        static final KeysSegment INSTANCE = new KeysSegment();

        KeysSegment() {
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            if (jSONReader.isObject()) {
                jSONReader.next();
                JSONArray jSONArray = new JSONArray();
                while (!jSONReader.nextIfObjectEnd()) {
                    jSONArray.add(jSONReader.readFieldName());
                    jSONReader.skipValue();
                }
                context.value = jSONArray;
                return;
            }
            throw new JSONException("TODO");
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void eval(JSONPath.Context context) {
            Object obj;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            if (obj instanceof Map) {
                context.value = new JSONArray(((Map) obj).keySet());
                context.eval = true;
                return;
            }
            throw new JSONException("TODO");
        }
    }

    static final class EntrySetSegment extends JSONPathSegment implements EvalSegment {
        static final EntrySetSegment INSTANCE = new EntrySetSegment();

        EntrySetSegment() {
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void accept(JSONReader jSONReader, JSONPath.Context context) {
            if (jSONReader.isObject()) {
                jSONReader.next();
                JSONArray jSONArray = new JSONArray();
                while (!jSONReader.nextIfObjectEnd()) {
                    jSONArray.add(JSONObject.of("key", (Object) jSONReader.readFieldName(), "value", jSONReader.readAny()));
                }
                context.value = jSONArray;
                return;
            }
            throw new JSONException("TODO");
        }

        @Override // com.alibaba.fastjson2.JSONPathSegment
        public void eval(JSONPath.Context context) {
            Object obj;
            if (context.parent == null) {
                obj = context.root;
            } else {
                obj = context.parent.value;
            }
            if (obj instanceof Map) {
                Map map = (Map) obj;
                JSONArray jSONArray = new JSONArray(map.size());
                for (Map.Entry entry : map.entrySet()) {
                    jSONArray.add(JSONObject.of("key", entry.getKey(), "value", entry.getValue()));
                }
                context.value = jSONArray;
                context.eval = true;
                return;
            }
            throw new JSONException("TODO");
        }
    }
}
