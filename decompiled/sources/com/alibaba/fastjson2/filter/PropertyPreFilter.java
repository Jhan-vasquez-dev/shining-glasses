package com.alibaba.fastjson2.filter;

import com.alibaba.fastjson2.JSONWriter;

/* JADX INFO: loaded from: classes.dex */
public interface PropertyPreFilter extends Filter {
    boolean process(JSONWriter jSONWriter, Object obj, String str);
}
