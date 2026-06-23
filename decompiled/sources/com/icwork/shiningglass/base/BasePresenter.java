package com.icwork.shiningglass.base;

import com.icwork.shiningglass.base.IBaseView;
import com.icwork.shiningglass.model.db.DbHelper;
import com.icwork.shiningglass.model.http.ApiHelper;
import com.icwork.shiningglass.model.preference.PreferenceHelper;

/* JADX INFO: loaded from: classes.dex */
public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {
    protected com.icwork.shiningglass.model.DataManager mDataManager = new com.icwork.shiningglass.model.DataManager(new DbHelper(), new ApiHelper(), new PreferenceHelper());
    protected V mView;

    @Override // com.icwork.shiningglass.base.IBasePresenter
    public void attachView(V v) {
        this.mView = v;
    }
}
