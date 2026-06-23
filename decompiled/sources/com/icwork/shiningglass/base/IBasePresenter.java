package com.icwork.shiningglass.base;

import com.icwork.shiningglass.base.IBaseView;

/* JADX INFO: loaded from: classes.dex */
public interface IBasePresenter<V extends IBaseView> {
    void attachView(V v);
}
