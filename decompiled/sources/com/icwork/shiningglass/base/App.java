package com.icwork.shiningglass.base;

import android.content.Context;
import com.cdbwsoft.library.BaseApplication;
import com.icwork.shiningglass.dao.DaoMaster;
import com.icwork.shiningglass.dao.DaoSession;
import com.icwork.shiningglass.model.data.AppData;
import csh.tiro.cc.aes;

/* JADX INFO: loaded from: classes.dex */
public class App extends BaseApplication {
    private static String DATA_BASE_NAME = "shinnigglass";
    private static App app;
    private static AppData appData = new AppData();
    private static DaoSession mDaoSession;

    @Override // com.cdbwsoft.library.BaseApplication, android.app.Application
    public void onCreate() {
        super.onCreate();
        app = this;
        aes.keyExpansionDefault();
        initDatabase(this);
    }

    public static App getInstance() {
        return app;
    }

    private void initDatabase(Context context) {
        mDaoSession = new DaoMaster(new DaoMaster.DevOpenHelper(context, DATA_BASE_NAME).getWritableDb()).newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

    public static AppData getAppData() {
        return appData;
    }
}
