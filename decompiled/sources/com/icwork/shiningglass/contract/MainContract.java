package com.icwork.shiningglass.contract;

import com.icwork.shiningglass.base.IBasePresenter;
import com.icwork.shiningglass.base.IBaseView;

/* JADX INFO: loaded from: classes.dex */
public interface MainContract {

    public interface Presenter extends IBasePresenter<View> {
        void testDb();

        void testGetMpresenter();

        void testPreference();

        void testRequestNetwork();
    }

    public interface View extends IBaseView {
        void testGetMview();
    }
}
