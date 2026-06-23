package com.alibaba.fastjson;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class JSONPObject extends com.alibaba.fastjson2.JSONPObject {
    private final List<Object> parameters;

    public JSONPObject() {
        this.parameters = new ArrayList();
    }

    public JSONPObject(String str) {
        super(str);
        this.parameters = new ArrayList();
    }
}
