package com.alibaba.fastjson2;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class JSONPObject {
    private String function;
    private final List<Object> parameters = new ArrayList();

    public JSONPObject() {
    }

    public JSONPObject(String str) {
        this.function = str;
    }

    public String getFunction() {
        return this.function;
    }

    public void setFunction(String str) {
        this.function = str;
    }

    public List<Object> getParameters() {
        return this.parameters;
    }

    public void addParameter(Object obj) {
        this.parameters.add(obj);
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
