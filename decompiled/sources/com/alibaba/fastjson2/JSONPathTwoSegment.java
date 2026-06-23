package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONPathFilter;
import com.alibaba.fastjson2.JSONPathSegment;
import com.alibaba.fastjson2.JSONReader;
import java.util.List;
import java.util.function.BiFunction;

/* JADX INFO: loaded from: classes.dex */
class JSONPathTwoSegment extends JSONPath {
    final boolean extractSupport;
    final JSONPathSegment first;
    final boolean ref;
    final JSONPathSegment second;

    JSONPathTwoSegment(String str, JSONPathSegment jSONPathSegment, JSONPathSegment jSONPathSegment2, JSONPath.Feature... featureArr) {
        super(str, featureArr);
        this.first = jSONPathSegment;
        this.second = jSONPathSegment2;
        boolean z = jSONPathSegment instanceof JSONPathSegmentIndex;
        boolean z2 = true;
        this.ref = (z || (jSONPathSegment instanceof JSONPathSegmentName)) && ((jSONPathSegment2 instanceof JSONPathSegmentIndex) || (jSONPathSegment2 instanceof JSONPathSegmentName));
        if ((jSONPathSegment instanceof JSONPathSegment.EvalSegment) || ((z && ((JSONPathSegmentIndex) jSONPathSegment).index < 0) || (jSONPathSegment2 instanceof JSONPathSegment.EvalSegment) || ((jSONPathSegment2 instanceof JSONPathSegmentIndex) && ((JSONPathSegmentIndex) jSONPathSegment2).index < 0))) {
            z2 = false;
        }
        this.extractSupport = z2;
        if ((jSONPathSegment instanceof JSONPathSegment.CycleNameSegment) && ((JSONPathSegment.CycleNameSegment) jSONPathSegment).shouldRecursive() && (jSONPathSegment2 instanceof JSONPathFilter.NameFilter)) {
            ((JSONPathFilter.NameFilter) jSONPathSegment2).excludeArray();
        }
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public boolean endsWithFilter() {
        return this.second instanceof JSONPathFilter;
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public JSONPath getParent() {
        return JSONPathSingle.of(this.first);
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public boolean remove(Object obj) {
        JSONPath.Context context = new JSONPath.Context(this, null, this.first, this.second, 0L);
        context.root = obj;
        this.first.eval(context);
        if (context.value == null) {
            return false;
        }
        return this.second.remove(new JSONPath.Context(this, context, this.second, null, 0L));
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public boolean contains(Object obj) {
        JSONPath.Context context = new JSONPath.Context(this, null, this.first, this.second, 0L);
        context.root = obj;
        this.first.eval(context);
        if (context.value == null) {
            return false;
        }
        return this.second.contains(new JSONPath.Context(this, context, this.second, null, 0L));
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public boolean isRef() {
        return this.ref;
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public Object eval(Object obj) {
        JSONPath.Context context = new JSONPath.Context(this, null, this.first, this.second, 0L);
        context.root = obj;
        this.first.eval(context);
        if (context.value == null) {
            return null;
        }
        JSONPathSegment jSONPathSegment = this.first;
        if ((jSONPathSegment instanceof JSONPathSegment.CycleNameSegment) && ((JSONPathSegment.CycleNameSegment) jSONPathSegment).shouldRecursive()) {
            JSONPathSegment jSONPathSegment2 = this.second;
            if (jSONPathSegment2 instanceof JSONPathFilter.NameFilter) {
                ((JSONPathFilter.NameFilter) jSONPathSegment2).excludeArray();
            }
        }
        JSONPath.Context context2 = new JSONPath.Context(this, context, this.second, null, 0L);
        this.second.eval(context2);
        Object obj2 = context2.value;
        if ((this.features & JSONPath.Feature.AlwaysReturnList.mask) == 0) {
            return obj2;
        }
        if (obj2 == null) {
            return new JSONArray();
        }
        return !(obj2 instanceof List) ? JSONArray.of(obj2) : obj2;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x007d  */
    @Override // com.alibaba.fastjson2.JSONPath
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void set(java.lang.Object r8, java.lang.Object r9) {
        /*
            r7 = this;
            com.alibaba.fastjson2.JSONPath$Context r0 = new com.alibaba.fastjson2.JSONPath$Context
            com.alibaba.fastjson2.JSONPathSegment r3 = r7.first
            com.alibaba.fastjson2.JSONPathSegment r4 = r7.second
            r5 = 0
            r2 = 0
            r1 = r7
            r0.<init>(r1, r2, r3, r4, r5)
            r0.root = r8
            com.alibaba.fastjson2.JSONPathSegment r2 = r1.first
            r2.eval(r0)
            java.lang.Object r2 = r0.value
            if (r2 != 0) goto Laa
            com.alibaba.fastjson2.JSONPathSegment r2 = r1.second
            boolean r3 = r2 instanceof com.alibaba.fastjson2.JSONPathSegmentIndex
            if (r3 == 0) goto L37
            com.alibaba.fastjson2.JSONReader$Context r2 = r1.readerContext
            if (r2 == 0) goto L31
            com.alibaba.fastjson2.JSONReader$Context r2 = r1.readerContext
            java.util.function.Supplier<java.util.List> r2 = r2.arraySupplier
            if (r2 == 0) goto L31
            com.alibaba.fastjson2.JSONReader$Context r2 = r1.readerContext
            java.util.function.Supplier<java.util.List> r2 = r2.arraySupplier
            java.lang.Object r2 = r2.get()
            goto L53
        L31:
            com.alibaba.fastjson2.JSONArray r2 = new com.alibaba.fastjson2.JSONArray
            r2.<init>()
            goto L53
        L37:
            boolean r2 = r2 instanceof com.alibaba.fastjson2.JSONPathSegmentName
            if (r2 == 0) goto La9
            com.alibaba.fastjson2.JSONReader$Context r2 = r1.readerContext
            if (r2 == 0) goto L4e
            com.alibaba.fastjson2.JSONReader$Context r2 = r1.readerContext
            java.util.function.Supplier<java.util.Map> r2 = r2.objectSupplier
            if (r2 == 0) goto L4e
            com.alibaba.fastjson2.JSONReader$Context r2 = r1.readerContext
            java.util.function.Supplier<java.util.Map> r2 = r2.objectSupplier
            java.lang.Object r2 = r2.get()
            goto L53
        L4e:
            com.alibaba.fastjson2.JSONObject r2 = new com.alibaba.fastjson2.JSONObject
            r2.<init>()
        L53:
            r0.value = r2
            boolean r3 = r8 instanceof java.util.Map
            if (r3 == 0) goto L69
            com.alibaba.fastjson2.JSONPathSegment r3 = r1.first
            boolean r4 = r3 instanceof com.alibaba.fastjson2.JSONPathSegmentName
            if (r4 == 0) goto L69
            java.util.Map r8 = (java.util.Map) r8
            com.alibaba.fastjson2.JSONPathSegmentName r3 = (com.alibaba.fastjson2.JSONPathSegmentName) r3
            java.lang.String r3 = r3.name
            r8.put(r3, r2)
            goto Laa
        L69:
            boolean r3 = r8 instanceof java.util.List
            if (r3 == 0) goto L7d
            com.alibaba.fastjson2.JSONPathSegment r3 = r1.first
            boolean r4 = r3 instanceof com.alibaba.fastjson2.JSONPathSegmentIndex
            if (r4 == 0) goto L7d
            java.util.List r8 = (java.util.List) r8
            com.alibaba.fastjson2.JSONPathSegmentIndex r3 = (com.alibaba.fastjson2.JSONPathSegmentIndex) r3
            int r3 = r3.index
            r8.set(r3, r2)
            goto Laa
        L7d:
            if (r8 == 0) goto Laa
            java.lang.Class r2 = r8.getClass()
            com.alibaba.fastjson2.JSONReader$Context r3 = r7.getReaderContext()
            com.alibaba.fastjson2.reader.ObjectReader r2 = r3.getObjectReader(r2)
            com.alibaba.fastjson2.JSONPathSegment r4 = r1.first
            boolean r5 = r4 instanceof com.alibaba.fastjson2.JSONPathSegmentName
            if (r5 == 0) goto Laa
            com.alibaba.fastjson2.JSONPathSegmentName r4 = (com.alibaba.fastjson2.JSONPathSegmentName) r4
            long r4 = r4.nameHashCode
            com.alibaba.fastjson2.reader.FieldReader r2 = r2.getFieldReader(r4)
            if (r2 == 0) goto Laa
            com.alibaba.fastjson2.reader.ObjectReader r3 = r2.getObjectReader(r3)
            java.lang.Object r3 = r3.createInstance()
            r2.accept(r8, r3)
            r0.value = r3
            goto Laa
        La9:
            return
        Laa:
            r2 = r0
            com.alibaba.fastjson2.JSONPath$Context r0 = new com.alibaba.fastjson2.JSONPath$Context
            com.alibaba.fastjson2.JSONPathSegment r3 = r1.second
            r4 = 0
            r5 = 0
            r0.<init>(r1, r2, r3, r4, r5)
            com.alibaba.fastjson2.JSONPathSegment r8 = r1.second
            r8.set(r0, r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathTwoSegment.set(java.lang.Object, java.lang.Object):void");
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public void set(Object obj, Object obj2, JSONReader.Feature... featureArr) {
        long j = 0;
        for (JSONReader.Feature feature : featureArr) {
            j |= feature.mask;
        }
        JSONPath.Context context = new JSONPath.Context(this, null, this.first, this.second, j);
        context.root = obj;
        this.first.eval(context);
        if (context.value == null) {
            return;
        }
        this.second.set(new JSONPath.Context(this, context, this.second, null, j), obj2);
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public void setCallback(Object obj, BiFunction biFunction) {
        JSONPath.Context context = new JSONPath.Context(this, null, this.first, this.second, 0L);
        context.root = obj;
        this.first.eval(context);
        if (context.value == null) {
            return;
        }
        this.second.setCallback(new JSONPath.Context(this, context, this.second, null, 0L), biFunction);
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public void setInt(Object obj, int i) {
        JSONPath.Context context = new JSONPath.Context(this, null, this.first, this.second, 0L);
        context.root = obj;
        this.first.eval(context);
        if (context.value == null) {
            return;
        }
        this.second.setInt(new JSONPath.Context(this, context, this.second, null, 0L), i);
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public void setLong(Object obj, long j) {
        JSONPath.Context context = new JSONPath.Context(this, null, this.first, this.second, 0L);
        context.root = obj;
        this.first.eval(context);
        if (context.value == null) {
            return;
        }
        this.second.setLong(new JSONPath.Context(this, context, this.second, null, 0L), j);
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public Object extract(JSONReader jSONReader) {
        if (jSONReader == null) {
            return null;
        }
        if (!this.extractSupport) {
            return eval(jSONReader.readAny());
        }
        JSONPath.Context context = new JSONPath.Context(this, null, this.first, this.second, 0L);
        this.first.accept(jSONReader, context);
        JSONPath.Context context2 = new JSONPath.Context(this, context, this.second, null, 0L);
        if (context.eval) {
            this.second.eval(context2);
        } else {
            this.second.accept(jSONReader, context2);
        }
        Object objOf = context2.value;
        if ((this.features & JSONPath.Feature.AlwaysReturnList.mask) != 0) {
            if (objOf == null) {
                objOf = new JSONArray();
            } else if (!(objOf instanceof List)) {
                objOf = JSONArray.of(objOf);
            }
        }
        return objOf instanceof JSONPath.Sequence ? ((JSONPath.Sequence) objOf).values : objOf;
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public String extractScalar(JSONReader jSONReader) {
        JSONPath.Context context = new JSONPath.Context(this, null, this.first, this.second, 0L);
        this.first.accept(jSONReader, context);
        JSONPath.Context context2 = new JSONPath.Context(this, context, this.second, null, 0L);
        this.second.accept(jSONReader, context2);
        return JSON.toJSONString(context2.value);
    }
}
