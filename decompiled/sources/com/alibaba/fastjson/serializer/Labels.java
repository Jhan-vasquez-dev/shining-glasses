package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson2.filter.Labels;

/* JADX INFO: loaded from: classes.dex */
public class Labels {

    private static class DefaultLabelFilter extends Labels.DefaultLabelFilter implements LabelFilter {
        public DefaultLabelFilter(String[] strArr, String[] strArr2) {
            super(strArr, strArr2);
        }
    }

    public static LabelFilter includes(String... strArr) {
        return new DefaultLabelFilter(strArr, null);
    }

    public static LabelFilter excludes(String... strArr) {
        return new DefaultLabelFilter(null, strArr);
    }
}
