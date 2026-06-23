package com.cdbwsoft.library.net.entity;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cdbwsoft.library.AppConfig;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ResponseList<T> extends Response {
    private List<T> mList;

    public List<T> getList() {
        return this.mList;
    }

    public void parseList(Class<T> cls) {
        if (TextUtils.isEmpty(getData())) {
            return;
        }
        try {
            this.mList = JSON.parseArray(getData(), cls);
        } catch (JSONException e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }
}
