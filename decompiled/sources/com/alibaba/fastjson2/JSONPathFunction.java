package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONPathSegment;
import com.alibaba.fastjson2.function.impl.ToDouble;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class JSONPathFunction extends JSONPathSegment implements JSONPathSegment.EvalSegment {
    final Function function;
    static final JSONPathFunction FUNC_TYPE = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda0
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.type(obj);
        }
    });
    static final JSONPathFunction FUNC_DOUBLE = new JSONPathFunction(new ToDouble(null));
    static final JSONPathFunction FUNC_FLOOR = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda3
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.floor(obj);
        }
    });
    static final JSONPathFunction FUNC_CEIL = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda4
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.ceil(obj);
        }
    });
    static final JSONPathFunction FUNC_ABS = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda5
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.abs(obj);
        }
    });
    static final JSONPathFunction FUNC_NEGATIVE = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda6
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.negative(obj);
        }
    });
    static final JSONPathFunction FUNC_EXISTS = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda7
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.exists(obj);
        }
    });
    static final JSONPathFunction FUNC_LOWER = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda8
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.lower(obj);
        }
    });
    static final JSONPathFunction FUNC_UPPER = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda9
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.upper(obj);
        }
    });
    static final JSONPathFunction FUNC_TRIM = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda10
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.trim(obj);
        }
    });
    static final JSONPathFunction FUNC_FIRST = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda1
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.first(obj);
        }
    });
    static final JSONPathFunction FUNC_LAST = new JSONPathFunction(new Function() { // from class: com.alibaba.fastjson2.JSONPathFunction$$ExternalSyntheticLambda2
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return JSONPathFunction.last(obj);
        }
    });

    public JSONPathFunction(Function function) {
        this.function = function;
    }

    static Object floor(Object obj) {
        if (obj instanceof Double) {
            return Double.valueOf(Math.floor(((Double) obj).doubleValue()));
        }
        if (obj instanceof Float) {
            return Double.valueOf(Math.floor(((Float) obj).floatValue()));
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).setScale(0, RoundingMode.FLOOR);
        }
        if (obj instanceof List) {
            List list = (List) obj;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Object obj2 = list.get(i);
                if (obj2 instanceof Double) {
                    list.set(i, Double.valueOf(Math.floor(((Double) obj2).doubleValue())));
                } else if (obj2 instanceof Float) {
                    list.set(i, Double.valueOf(Math.floor(((Float) obj2).floatValue())));
                } else if (obj2 instanceof BigDecimal) {
                    list.set(i, ((BigDecimal) obj2).setScale(0, RoundingMode.FLOOR));
                }
            }
        }
        return obj;
    }

    static Object ceil(Object obj) {
        if (obj instanceof Double) {
            return Double.valueOf(Math.ceil(((Double) obj).doubleValue()));
        }
        if (obj instanceof Float) {
            return Double.valueOf(Math.ceil(((Float) obj).floatValue()));
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).setScale(0, RoundingMode.CEILING);
        }
        if (obj instanceof List) {
            List list = (List) obj;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Object obj2 = list.get(i);
                if (obj2 instanceof Double) {
                    list.set(i, Double.valueOf(Math.ceil(((Double) obj2).doubleValue())));
                } else if (obj2 instanceof Float) {
                    list.set(i, Double.valueOf(Math.ceil(((Float) obj2).floatValue())));
                } else if (obj2 instanceof BigDecimal) {
                    list.set(i, ((BigDecimal) obj2).setScale(0, RoundingMode.CEILING));
                }
            }
        }
        return obj;
    }

    static Object exists(Object obj) {
        return Boolean.valueOf(obj != null);
    }

    static Object negative(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            int iIntValue = ((Integer) obj).intValue();
            if (iIntValue == Integer.MIN_VALUE) {
                return Long.valueOf(-iIntValue);
            }
            return Integer.valueOf(-iIntValue);
        }
        if (obj instanceof Long) {
            long jLongValue = ((Long) obj).longValue();
            if (jLongValue == Long.MIN_VALUE) {
                return BigInteger.valueOf(jLongValue).negate();
            }
            return Long.valueOf(-jLongValue);
        }
        if (obj instanceof Byte) {
            byte bByteValue = ((Byte) obj).byteValue();
            if (bByteValue == -128) {
                return Integer.valueOf(-bByteValue);
            }
            return Byte.valueOf((byte) (-bByteValue));
        }
        if (obj instanceof Short) {
            short sShortValue = ((Short) obj).shortValue();
            if (sShortValue == Short.MIN_VALUE) {
                return Integer.valueOf(-sShortValue);
            }
            return Short.valueOf((short) (-sShortValue));
        }
        if (obj instanceof Double) {
            return Double.valueOf(-((Double) obj).doubleValue());
        }
        if (obj instanceof Float) {
            return Float.valueOf(-((Float) obj).floatValue());
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).negate();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).negate();
        }
        if (!(obj instanceof List)) {
            return obj;
        }
        List list = (List) obj;
        JSONArray jSONArray = new JSONArray(list.size());
        for (int i = 0; i < list.size(); i++) {
            jSONArray.add(negative(list.get(i)));
        }
        return jSONArray;
    }

    static Object first(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONPath.Sequence) {
            obj = ((JSONPath.Sequence) obj).values;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            if (list.isEmpty()) {
                return null;
            }
            return list.get(0);
        }
        if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            if (collection.isEmpty()) {
                return null;
            }
            return collection.iterator().next();
        }
        if (!obj.getClass().isArray()) {
            return obj;
        }
        if (Array.getLength(obj) == 0) {
            return null;
        }
        return Array.get(obj, 0);
    }

    static Object last(Object obj) {
        Object next = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONPath.Sequence) {
            obj = ((JSONPath.Sequence) obj).values;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            int size = list.size();
            if (size == 0) {
                return null;
            }
            return list.get(size - 1);
        }
        if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            if (collection.isEmpty()) {
                return null;
            }
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                next = it.next();
            }
            return next;
        }
        if (!obj.getClass().isArray()) {
            return obj;
        }
        int length = Array.getLength(obj);
        if (length == 0) {
            return null;
        }
        return Array.get(obj, length - 1);
    }

    static Object abs(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            int iIntValue = ((Integer) obj).intValue();
            return iIntValue < 0 ? Integer.valueOf(-iIntValue) : obj;
        }
        if (obj instanceof Long) {
            long jLongValue = ((Long) obj).longValue();
            return jLongValue < 0 ? Long.valueOf(-jLongValue) : obj;
        }
        if (obj instanceof Byte) {
            byte bByteValue = ((Byte) obj).byteValue();
            return bByteValue < 0 ? Byte.valueOf((byte) (-bByteValue)) : obj;
        }
        if (obj instanceof Short) {
            short sShortValue = ((Short) obj).shortValue();
            return sShortValue < 0 ? Short.valueOf((short) (-sShortValue)) : obj;
        }
        if (obj instanceof Double) {
            double dDoubleValue = ((Double) obj).doubleValue();
            return dDoubleValue < 0.0d ? Double.valueOf(-dDoubleValue) : obj;
        }
        if (obj instanceof Float) {
            float fFloatValue = ((Float) obj).floatValue();
            return fFloatValue < 0.0f ? Float.valueOf(-fFloatValue) : obj;
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).abs();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).abs();
        }
        if (obj instanceof List) {
            List list = (List) obj;
            JSONArray jSONArray = new JSONArray(list.size());
            for (int i = 0; i < list.size(); i++) {
                jSONArray.add(abs(list.get(i)));
            }
            return jSONArray;
        }
        throw new JSONException("abs not support " + obj);
    }

    static String type(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Collection) {
            return "array";
        }
        if (obj instanceof Number) {
            return "number";
        }
        if (obj instanceof Boolean) {
            return "boolean";
        }
        if ((obj instanceof String) || (obj instanceof UUID) || (obj instanceof Enum)) {
            return "string";
        }
        return "object";
    }

    static Object lower(Object obj) {
        String string;
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            string = (String) obj;
        } else {
            string = obj.toString();
        }
        return string.toLowerCase();
    }

    static Object upper(Object obj) {
        String string;
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            string = (String) obj;
        } else {
            string = obj.toString();
        }
        return string.toUpperCase();
    }

    static Object trim(Object obj) {
        String string;
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            string = (String) obj;
        } else {
            string = obj.toString();
        }
        return string.trim();
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
        if (context.parent == null) {
            obj = context.root;
        } else {
            obj = context.parent.value;
        }
        context.value = this.function.apply(obj);
    }

    static final class TypeFunction implements Function {
        static final TypeFunction INSTANCE = new TypeFunction();

        TypeFunction() {
        }

        @Override // java.util.function.Function
        public Object apply(Object obj) {
            return JSONPathFunction.type(obj);
        }
    }

    static final class SizeFunction implements Function {
        static final SizeFunction INSTANCE = new SizeFunction();

        SizeFunction() {
        }

        @Override // java.util.function.Function
        public Object apply(Object obj) {
            if (obj == null) {
                return -1;
            }
            if (obj instanceof Collection) {
                return Integer.valueOf(((Collection) obj).size());
            }
            if (obj.getClass().isArray()) {
                return Integer.valueOf(Array.getLength(obj));
            }
            if (obj instanceof Map) {
                return Integer.valueOf(((Map) obj).size());
            }
            if (obj instanceof JSONPath.Sequence) {
                return Integer.valueOf(((JSONPath.Sequence) obj).values.size());
            }
            return 1;
        }
    }

    static final class BiFunctionAdapter implements BiFunction {
        private final Function function;

        BiFunctionAdapter(Function function) {
            this.function = function;
        }

        @Override // java.util.function.BiFunction
        public Object apply(Object obj, Object obj2) {
            return this.function.apply(obj2);
        }
    }

    static abstract class Index implements Function {
        protected abstract boolean eq(Object obj);

        Index() {
        }

        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            if (obj == null) {
                return null;
            }
            int i = 0;
            if (obj instanceof List) {
                List list = (List) obj;
                while (i < list.size()) {
                    if (eq(list.get(i))) {
                        return Integer.valueOf(i);
                    }
                    i++;
                }
                return -1;
            }
            if (!obj.getClass().isArray()) {
                return eq(obj) ? 0 : null;
            }
            int length = Array.getLength(obj);
            while (i < length) {
                if (eq(Array.get(obj, i))) {
                    return Integer.valueOf(i);
                }
                i++;
            }
            return -1;
        }
    }

    static final class IndexInt extends Index {
        transient BigDecimal decimalValue;
        final long value;

        public IndexInt(long j) {
            this.value = j;
        }

        @Override // com.alibaba.fastjson2.JSONPathFunction.Index
        protected boolean eq(Object obj) {
            if ((obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Byte) || (obj instanceof Short)) {
                return ((Number) obj).longValue() == this.value;
            }
            if ((obj instanceof Float) || (obj instanceof Double)) {
                return ((Number) obj).doubleValue() == ((double) this.value);
            }
            if (!(obj instanceof BigDecimal)) {
                return false;
            }
            BigDecimal bigDecimalM = JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0.m((BigDecimal) obj);
            if (this.decimalValue == null) {
                this.decimalValue = BigDecimal.valueOf(this.value);
            }
            return this.decimalValue.equals(bigDecimalM);
        }
    }

    static final class IndexDecimal extends Index {
        final BigDecimal value;

        public IndexDecimal(BigDecimal bigDecimal) {
            this.value = bigDecimal;
        }

        @Override // com.alibaba.fastjson2.JSONPathFunction.Index
        protected boolean eq(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof BigDecimal) {
                return this.value.equals(JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0.m((BigDecimal) obj));
            }
            if ((obj instanceof Float) || (obj instanceof Double)) {
                return this.value.equals(JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0.m(new BigDecimal(((Number) obj).doubleValue())));
            }
            if (obj instanceof String) {
                String str = (String) obj;
                if (TypeUtils.isNumber(str)) {
                    return this.value.equals(JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0.m(new BigDecimal(str)));
                }
            }
            return false;
        }
    }

    static final class IndexString extends Index {
        final String value;

        public IndexString(String str) {
            this.value = str;
        }

        @Override // com.alibaba.fastjson2.JSONPathFunction.Index
        protected boolean eq(Object obj) {
            if (obj == null) {
                return false;
            }
            return this.value.equals(obj.toString());
        }
    }

    static final class IndexValue implements Function {
        final int index;

        public IndexValue(int i) {
            this.index = i;
        }

        @Override // java.util.function.Function
        public Object apply(Object obj) {
            if (obj == null) {
                return null;
            }
            if (obj instanceof List) {
                return ((List) obj).get(this.index);
            }
            if (obj.getClass().isArray()) {
                return Array.get(obj, this.index);
            }
            return null;
        }
    }

    static final class FilterFunction implements Function {
        final JSONPathFilter filter;

        @Override // java.util.function.Function
        public Object apply(Object obj) {
            return null;
        }

        FilterFunction(JSONPathFilter jSONPathFilter) {
            this.filter = jSONPathFilter;
        }
    }
}
