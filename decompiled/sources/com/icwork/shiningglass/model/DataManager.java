package com.icwork.shiningglass.model;

import com.icwork.shiningglass.model.db.AppDbHelper;
import com.icwork.shiningglass.model.http.AppApiHelper;
import com.icwork.shiningglass.model.preference.AppPreferenceHelper;

/* JADX INFO: loaded from: classes.dex */
public class DataManager implements AppDbHelper, AppApiHelper, AppPreferenceHelper {
    private AppApiHelper mAppApiHelper;
    private AppDbHelper mAppDbHelper;
    private AppPreferenceHelper mAppPreferenceHelper;

    public DataManager(AppDbHelper appDbHelper, AppApiHelper appApiHelper, AppPreferenceHelper appPreferenceHelper) {
        this.mAppDbHelper = appDbHelper;
        this.mAppApiHelper = appApiHelper;
        this.mAppPreferenceHelper = appPreferenceHelper;
    }

    @Override // com.icwork.shiningglass.model.db.AppDbHelper
    public void testDb() {
        this.mAppDbHelper.testDb();
    }

    @Override // com.icwork.shiningglass.model.http.AppApiHelper
    public void testRequestNetwork() {
        this.mAppApiHelper.testRequestNetwork();
    }

    @Override // com.icwork.shiningglass.model.preference.AppPreferenceHelper
    public void testPreference() {
        this.mAppPreferenceHelper.testPreference();
    }
}
