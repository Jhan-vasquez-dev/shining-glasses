package com.cdbwsoft.library.net.entity;

import com.cdbwsoft.library.net.ErrorStatus;

/* JADX INFO: loaded from: classes.dex */
public interface SuperResponse extends ErrorStatus {
    String getData();

    String getMsg();

    int getStatus();

    boolean isSuccess();

    void setData(String str);

    void setMsg(String str);

    void setStatus(int i);
}
