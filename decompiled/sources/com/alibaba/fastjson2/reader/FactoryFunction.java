package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class FactoryFunction<T> implements Function<Map<Long, Object>, T> {
    final BiFunction biFunction;
    final Method factoryMethod;
    final Function function;
    final long[] hashCodes;
    final String[] paramNames;

    /* JADX WARN: Removed duplicated region for block: B:18:0x004c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    FactoryFunction(java.lang.reflect.Method r8, java.lang.String... r9) {
        /*
            r7 = this;
            r7.<init>()
            r7.factoryMethod = r8
            java.lang.reflect.Parameter[] r0 = r8.getParameters()
            int r1 = r0.length
            java.lang.String[] r1 = new java.lang.String[r1]
            r7.paramNames = r1
            int r1 = r0.length
            long[] r1 = new long[r1]
            r7.hashCodes = r1
            r1 = 0
        L14:
            int r2 = r0.length
            if (r1 >= r2) goto L30
            int r2 = r9.length
            if (r1 >= r2) goto L1d
            r2 = r9[r1]
            goto L23
        L1d:
            r2 = r0[r1]
            java.lang.String r2 = r2.getName()
        L23:
            r9[r1] = r2
            long[] r3 = r7.hashCodes
            long r4 = com.alibaba.fastjson2.util.Fnv.hashCode64(r2)
            r3[r1] = r4
            int r1 = r1 + 1
            goto L14
        L30:
            boolean r9 = com.alibaba.fastjson2.reader.ObjectReaderCreator.JIT
            r0 = 0
            if (r9 == 0) goto L4c
            int r9 = r8.getParameterCount()
            r1 = 1
            if (r9 != r1) goto L44
            java.util.function.Function r8 = com.alibaba.fastjson2.support.LambdaMiscCodec.createFunction(r8)
            r6 = r0
            r0 = r8
            r8 = r6
            goto L4d
        L44:
            r1 = 2
            if (r9 != r1) goto L4c
            java.util.function.BiFunction r8 = com.alibaba.fastjson2.support.LambdaMiscCodec.createBiFunction(r8)
            goto L4d
        L4c:
            r8 = r0
        L4d:
            r7.function = r0
            r7.biFunction = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.FactoryFunction.<init>(java.lang.reflect.Method, java.lang.String[]):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Function
    public T apply(Map<Long, Object> map) {
        if (this.function != null) {
            return (T) this.function.apply(map.get(Long.valueOf(this.hashCodes[0])));
        }
        if (this.biFunction != null) {
            return (T) this.biFunction.apply(map.get(Long.valueOf(this.hashCodes[0])), map.get(Long.valueOf(this.hashCodes[1])));
        }
        int length = this.hashCodes.length;
        Object[] objArr = new Object[length];
        for (int i = 0; i < length; i++) {
            objArr[i] = map.get(Long.valueOf(this.hashCodes[i]));
        }
        return createInstance(objArr);
    }

    public T createInstance(Object[] objArr) {
        try {
            return (T) this.factoryMethod.invoke(null, objArr);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new JSONException("invoke factoryMethod error", e);
        }
    }
}
