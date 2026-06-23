package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONPathFilter;
import com.alibaba.fastjson2.JSONPathSegment;
import com.alibaba.fastjson2.JSONReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/* JADX INFO: loaded from: classes.dex */
final class JSONPathMulti extends JSONPath {
    final boolean extractSupport;
    final boolean ref;
    final List<JSONPathSegment> segments;

    JSONPathMulti(String str, List<JSONPathSegment> list, JSONPath.Feature... featureArr) {
        super(str, featureArr);
        this.segments = list;
        int size = list.size();
        boolean z = true;
        boolean z2 = true;
        int i = 0;
        while (true) {
            if (i >= size - 1) {
                break;
            }
            JSONPathSegment jSONPathSegment = list.get(i);
            if (jSONPathSegment instanceof JSONPathSegmentIndex) {
                if (((JSONPathSegmentIndex) jSONPathSegment).index < 0) {
                    z2 = false;
                }
            } else if (!(jSONPathSegment instanceof JSONPathSegmentName)) {
                if (i > 0) {
                    JSONPathSegment jSONPathSegment2 = list.get(i - 1);
                    if ((jSONPathSegment2 instanceof JSONPathSegment.CycleNameSegment) && ((JSONPathSegment.CycleNameSegment) jSONPathSegment2).shouldRecursive() && (jSONPathSegment instanceof JSONPathFilter.NameFilter)) {
                        ((JSONPathFilter.NameFilter) jSONPathSegment).excludeArray();
                    }
                }
                z = false;
            }
            i++;
        }
        this.extractSupport = z2;
        this.ref = z;
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public boolean remove(Object obj) {
        int size = this.segments.size();
        if (size == 0) {
            return false;
        }
        int i = 0;
        JSONPath.Context context = null;
        while (i < size) {
            JSONPathSegment jSONPathSegment = this.segments.get(i);
            int i2 = i + 1;
            JSONPath.Context context2 = new JSONPath.Context(this, context, jSONPathSegment, i2 < size ? this.segments.get(i2) : null, 0L);
            if (i == 0) {
                context2.root = obj;
            }
            if (i == size - 1) {
                return jSONPathSegment.remove(context2);
            }
            jSONPathSegment.eval(context2);
            if (context2.value == null) {
                return false;
            }
            context = context2;
            i = i2;
        }
        return false;
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public boolean contains(Object obj) {
        int size = this.segments.size();
        if (size == 0) {
            return obj != null;
        }
        int i = 0;
        JSONPath.Context context = null;
        while (i < size) {
            JSONPathSegment jSONPathSegment = this.segments.get(i);
            int i2 = i + 1;
            JSONPath.Context context2 = new JSONPath.Context(this, context, jSONPathSegment, i2 < size ? this.segments.get(i2) : null, 0L);
            if (i == 0) {
                context2.root = obj;
            }
            if (i == size - 1) {
                return jSONPathSegment.contains(context2);
            }
            jSONPathSegment.eval(context2);
            context = context2;
            i = i2;
        }
        return false;
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public boolean endsWithFilter() {
        return this.segments.get(this.segments.size() - 1) instanceof JSONPathFilter;
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public JSONPath getParent() {
        int size = this.segments.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return JSONPath.RootPath.INSTANCE;
        }
        if (size == 2) {
            return JSONPathSingle.of(this.segments.get(0));
        }
        StringBuilder sb = new StringBuilder("$");
        int i = size - 1;
        ArrayList arrayList = new ArrayList(i);
        for (int i2 = 0; i2 < i; i2++) {
            JSONPathSegment jSONPathSegment = this.segments.get(i2);
            arrayList.add(jSONPathSegment);
            if (!(jSONPathSegment instanceof JSONPathSegmentIndex) && !(jSONPathSegment instanceof JSONPathSegment.MultiIndexSegment) && !(jSONPathSegment instanceof JSONPathFilter)) {
                sb.append('.');
            }
            sb.append(jSONPathSegment);
        }
        String string = sb.toString();
        if (size == 3) {
            new JSONPathTwoSegment(string, this.segments.get(0), this.segments.get(1), new JSONPath.Feature[0]);
        }
        return new JSONPathMulti(string, arrayList, new JSONPath.Feature[0]);
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public boolean isRef() {
        return this.ref;
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public Object eval(Object obj) {
        int size = this.segments.size();
        if (size == 0) {
            return obj;
        }
        int i = 0;
        JSONPath.Context context = null;
        while (i < size) {
            JSONPathSegment jSONPathSegment = this.segments.get(i);
            int i2 = i + 1;
            JSONPath.Context context2 = new JSONPath.Context(this, context, jSONPathSegment, i2 < size ? this.segments.get(i2) : null, 0L);
            if (i == 0) {
                context2.root = obj;
            }
            if (i > 0) {
                JSONPathSegment jSONPathSegment2 = this.segments.get(i - 1);
                if ((jSONPathSegment2 instanceof JSONPathSegment.CycleNameSegment) && ((JSONPathSegment.CycleNameSegment) jSONPathSegment2).shouldRecursive() && (jSONPathSegment instanceof JSONPathFilter.NameFilter)) {
                    ((JSONPathFilter.NameFilter) jSONPathSegment).excludeArray();
                }
            }
            jSONPathSegment.eval(context2);
            context = context2;
            i = i2;
        }
        Object obj2 = context.value;
        if ((context.path.features & JSONPath.Feature.AlwaysReturnList.mask) == 0) {
            return obj2;
        }
        if (obj2 == null) {
            return new JSONArray();
        }
        return !(obj2 instanceof List) ? JSONArray.of(obj2) : obj2;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0096  */
    @Override // com.alibaba.fastjson2.JSONPath
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void set(java.lang.Object r13, java.lang.Object r14) {
        /*
            Method dump skipped, instruction units count: 296
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathMulti.set(java.lang.Object, java.lang.Object):void");
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public void set(Object obj, Object obj2, JSONReader.Feature... featureArr) {
        long j = 0;
        for (JSONReader.Feature feature : featureArr) {
            j |= feature.mask;
        }
        int size = this.segments.size();
        JSONPath.Context context = null;
        int i = 0;
        while (true) {
            int i2 = size - 1;
            if (i < i2) {
                JSONPathSegment jSONPathSegment = this.segments.get(i);
                int i3 = i + 1;
                JSONPath.Context context2 = new JSONPath.Context(this, context, jSONPathSegment, i3 < size ? this.segments.get(i3) : null, j);
                if (i == 0) {
                    context2.root = obj;
                }
                jSONPathSegment.eval(context2);
                i = i3;
                context = context2;
            } else {
                JSONPath.Context context3 = new JSONPath.Context(this, context, this.segments.get(0), null, j);
                context3.root = obj;
                this.segments.get(i2).set(context3, obj2);
                return;
            }
        }
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public void setCallback(Object obj, BiFunction biFunction) {
        int size = this.segments.size();
        JSONPath.Context context = null;
        int i = 0;
        while (true) {
            int i2 = size - 1;
            if (i < i2) {
                JSONPathSegment jSONPathSegment = this.segments.get(i);
                int i3 = i + 1;
                JSONPath.Context context2 = new JSONPath.Context(this, context, jSONPathSegment, i3 < size ? this.segments.get(i3) : null, 0L);
                if (i == 0) {
                    context2.root = obj;
                }
                jSONPathSegment.eval(context2);
                context = context2;
                i = i3;
            } else {
                JSONPath.Context context3 = new JSONPath.Context(this, context, this.segments.get(0), null, 0L);
                context3.root = obj;
                this.segments.get(i2).setCallback(context3, biFunction);
                return;
            }
        }
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public void setInt(Object obj, int i) {
        set(obj, Integer.valueOf(i));
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public void setLong(Object obj, long j) {
        set(obj, Long.valueOf(j));
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public Object extract(JSONReader jSONReader) {
        int size;
        JSONPathMulti jSONPathMulti;
        if (jSONReader == null || (size = this.segments.size()) == 0) {
            return null;
        }
        if (!this.extractSupport) {
            return eval(jSONReader.readAny());
        }
        int i = 0;
        JSONPath.Context context = null;
        boolean z = false;
        while (true) {
            if (i >= size) {
                jSONPathMulti = this;
                break;
            }
            JSONPathSegment jSONPathSegment = this.segments.get(i);
            i++;
            jSONPathMulti = this;
            JSONPath.Context context2 = new JSONPath.Context(jSONPathMulti, context, jSONPathSegment, i < size ? this.segments.get(i) : null, 0L);
            if (z) {
                jSONPathSegment.eval(context2);
            } else {
                jSONPathSegment.accept(jSONReader, context2);
            }
            if (context2.eval) {
                if (context2.value == null) {
                    context = context2;
                    break;
                }
                z = true;
            }
            context = context2;
        }
        Object obj = context.value;
        if (obj instanceof JSONPath.Sequence) {
            obj = ((JSONPath.Sequence) obj).values;
        }
        if ((jSONPathMulti.features & JSONPath.Feature.AlwaysReturnList.mask) == 0) {
            return obj;
        }
        if (obj == null) {
            return new JSONArray();
        }
        return !(obj instanceof List) ? JSONArray.of(obj) : obj;
    }

    @Override // com.alibaba.fastjson2.JSONPath
    public String extractScalar(JSONReader jSONReader) {
        int size = this.segments.size();
        if (size == 0) {
            return null;
        }
        int i = 0;
        JSONPath.Context context = null;
        boolean z = false;
        while (true) {
            if (i >= size) {
                break;
            }
            JSONPathSegment jSONPathSegment = this.segments.get(i);
            i++;
            JSONPath.Context context2 = new JSONPath.Context(this, context, jSONPathSegment, i < size ? this.segments.get(i) : null, 0L);
            if (z) {
                jSONPathSegment.eval(context2);
            } else {
                jSONPathSegment.accept(jSONReader, context2);
            }
            if (context2.eval) {
                if (context2.value == null) {
                    context = context2;
                    break;
                }
                z = true;
            }
            context = context2;
        }
        return JSON.toJSONString(context.value);
    }
}
