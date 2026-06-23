package com.cdbwsoft.library.net.entity;

import com.cdbwsoft.library.AppConfig;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Response implements SuperResponse {
    protected String data = "";
    private int status = -1;
    private String msg = "";

    public Response() {
    }

    public Response(int i) {
        setStatus(i);
    }

    public Response(int i, String str) {
        setStatus(i);
        setMsg(str);
    }

    public Response(int i, String str, String str2) {
        setStatus(i);
        setMsg(str);
        setData(str2);
    }

    public Response(Map<String, Object> map) {
        if (map == null || map.size() <= 0) {
            return;
        }
        try {
            setData(String.valueOf(map.get(AppConfig.RESPONSE_DATA_KEY)));
            setMsg(String.valueOf(map.get(AppConfig.RESPONSE_MSG_KEY)));
            Object obj = map.get(AppConfig.RESPONSE_CODE_KEY);
            if (obj != null) {
                if (obj instanceof Integer) {
                    setStatus(((Integer) obj).intValue());
                } else {
                    setStatus(Integer.valueOf(String.valueOf(obj)).intValue());
                }
            }
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.cdbwsoft.library.net.entity.SuperResponse
    public boolean isSuccess() {
        return getStatus() == OK;
    }

    @Override // com.cdbwsoft.library.net.entity.SuperResponse
    public String getData() {
        return this.data;
    }

    @Override // com.cdbwsoft.library.net.entity.SuperResponse
    public void setData(String str) {
        this.data = str;
    }

    @Override // com.cdbwsoft.library.net.entity.SuperResponse
    public int getStatus() {
        return this.status;
    }

    @Override // com.cdbwsoft.library.net.entity.SuperResponse
    public void setStatus(int i) {
        this.status = i;
    }

    @Override // com.cdbwsoft.library.net.entity.SuperResponse
    public String getMsg() {
        return this.msg;
    }

    @Override // com.cdbwsoft.library.net.entity.SuperResponse
    public void setMsg(String str) {
        this.msg = str;
    }

    public static Response result(int i) {
        return new Response(i);
    }

    public static Response result(int i, String str) {
        return new Response(i, str);
    }

    public static Response result(int i, String str, String str2) {
        return new Response(i, str, str2);
    }
}
