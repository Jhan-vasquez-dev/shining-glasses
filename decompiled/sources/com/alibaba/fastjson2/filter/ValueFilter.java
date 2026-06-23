package com.alibaba.fastjson2.filter;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public interface ValueFilter extends Filter {
    Object apply(Object obj, String str, Object obj2);

    static ValueFilter compose(final ValueFilter valueFilter, final ValueFilter valueFilter2) {
        return new ValueFilter() { // from class: com.alibaba.fastjson2.filter.ValueFilter$$ExternalSyntheticLambda0
            @Override // com.alibaba.fastjson2.filter.ValueFilter
            public final Object apply(Object obj, String str, Object obj2) {
                return this.f$0.apply(obj, str, valueFilter.apply(obj, str, obj2));
            }
        };
    }

    static ValueFilter of(final String str, final Function function) {
        return new ValueFilter() { // from class: com.alibaba.fastjson2.filter.ValueFilter$$ExternalSyntheticLambda1
            @Override // com.alibaba.fastjson2.filter.ValueFilter
            public final Object apply(Object obj, String str2, Object obj2) {
                return ValueFilter.lambda$of$1(str, function, obj, str2, obj2);
            }
        };
    }

    static /* synthetic */ Object lambda$of$1(String str, Function function, Object obj, String str2, Object obj2) {
        return (str == null || str.equals(str2)) ? function.apply(obj2) : obj2;
    }

    static ValueFilter of(final String str, final Map map) {
        return new ValueFilter() { // from class: com.alibaba.fastjson2.filter.ValueFilter$$ExternalSyntheticLambda2
            @Override // com.alibaba.fastjson2.filter.ValueFilter
            public final Object apply(Object obj, String str2, Object obj2) {
                return ValueFilter.lambda$of$2(str, map, obj, str2, obj2);
            }
        };
    }

    static /* synthetic */ Object lambda$of$2(String str, Map map, Object obj, String str2, Object obj2) {
        Object obj3;
        return ((str == null || str.equals(str2)) && ((obj3 = map.get(obj2)) != null || map.containsKey(obj2))) ? obj3 : obj2;
    }

    static ValueFilter of(final Predicate<String> predicate, final Function function) {
        return new ValueFilter() { // from class: com.alibaba.fastjson2.filter.ValueFilter$$ExternalSyntheticLambda3
            @Override // com.alibaba.fastjson2.filter.ValueFilter
            public final Object apply(Object obj, String str, Object obj2) {
                return ValueFilter.lambda$of$3(predicate, function, obj, str, obj2);
            }
        };
    }

    static /* synthetic */ Object lambda$of$3(Predicate predicate, Function function, Object obj, String str, Object obj2) {
        return (predicate == null || predicate.test(str)) ? function.apply(obj2) : obj2;
    }
}
