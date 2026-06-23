package com.icwork.shiningglass.presenter;

import android.util.Log;
import com.icwork.shiningglass.base.BasePresenter;
import com.icwork.shiningglass.contract.MainContract;

/* JADX INFO: loaded from: classes.dex */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    @Override // com.icwork.shiningglass.contract.MainContract.Presenter
    public void testGetMpresenter() {
        Log.d("print", "我是P层的引用");
        ((MainContract.View) this.mView).testGetMview();
    }

    @Override // com.icwork.shiningglass.contract.MainContract.Presenter
    public void testDb() {
        this.mDataManager.testDb();
    }

    @Override // com.icwork.shiningglass.contract.MainContract.Presenter
    public void testRequestNetwork() {
        this.mDataManager.testRequestNetwork();
    }

    @Override // com.icwork.shiningglass.contract.MainContract.Presenter
    public void testPreference() {
        this.mDataManager.testPreference();
    }
}
