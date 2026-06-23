package com.cdbwsoft.library.net;

import com.android.volley.Response;
import com.cdbwsoft.library.net.entity.SuperResponse;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public abstract class ResponseListener<T> implements Response.Listener<T> {
    private final Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override // com.android.volley.Response.Listener
    public void onResponse(T t) {
    }

    public Type getType() {
        return this.type;
    }

    public void onResponse(SuperResponse superResponse, T t) {
        onResponse(t);
    }
}
