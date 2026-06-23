package com.cdbwsoft.library.net.entity;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cdbwsoft.library.AppConfig;

/* JADX INFO: loaded from: classes.dex */
public class ResponseVo<T> extends Response {
    private T mVo;

    public T getVo() {
        return this.mVo;
    }

    public void parseVo(Class<T> cls) {
        if (TextUtils.isEmpty(getData())) {
            return;
        }
        try {
            this.mVo = (T) JSON.parseObject(getData(), cls);
        } catch (JSONException e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }
}
