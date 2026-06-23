package com.cdbwsoft.library.net.entity;

import com.cdbwsoft.library.net.entity.SuperResponse;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseResponseFactory<T extends SuperResponse> {
    public abstract T newInstance(int i);

    public abstract T newInstance(int i, String str);

    public abstract T newInstance(int i, String str, String str2);
}
