package com.cdbwsoft.library.net;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.net.entity.Response;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class SimpleRequest extends BaseRequest<Response> {
    public static final String TAG = "SimpleRequest";
    private final Response.Listener<com.cdbwsoft.library.net.entity.Response> mListener;

    public SimpleRequest(int i, String str, Response.Listener<com.cdbwsoft.library.net.entity.Response> listener, Response.ErrorListener errorListener) {
        super(i, str, errorListener);
        this.mListener = listener;
    }

    public SimpleRequest(String str, Response.Listener<com.cdbwsoft.library.net.entity.Response> listener, Response.ErrorListener errorListener) {
        this(1, str, listener, errorListener);
    }

    public SimpleRequest(String str, Response.Listener<com.cdbwsoft.library.net.entity.Response> listener) {
        this(1, str, listener, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.volley.Request
    public void deliverResponse(com.cdbwsoft.library.net.entity.Response response) {
        Response.Listener<com.cdbwsoft.library.net.entity.Response> listener = this.mListener;
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override // com.cdbwsoft.library.net.BaseRequest, com.android.volley.Request
    public void deliverError(VolleyError volleyError) {
        super.deliverError(volleyError);
        Response.Listener<com.cdbwsoft.library.net.entity.Response> listener = this.mListener;
        if (listener != null) {
            listener.onResponse(com.cdbwsoft.library.net.entity.Response.result(-5, "网络繁忙"));
        }
        if (AppConfig.DEBUG) {
            Log.d(TAG, "请求地址：" + getUrl());
            Log.d(TAG, "请求参数：" + this.mParams);
            Log.d(TAG, "附加参数：" + JSON.toJSONString(getAttachParams()));
            Log.d(TAG, "返回内容：-5网络繁忙");
        }
    }

    @Override // com.android.volley.Request
    protected com.android.volley.Response<com.cdbwsoft.library.net.entity.Response> parseNetworkResponse(NetworkResponse networkResponse) {
        com.cdbwsoft.library.net.entity.Response responseResult;
        try {
            responseResult = (com.cdbwsoft.library.net.entity.Response) JSON.parseObject(networkResponse.data, (Type) com.cdbwsoft.library.net.entity.Response.class, new Feature[0]);
        } catch (JSONException unused) {
            responseResult = com.cdbwsoft.library.net.entity.Response.result(511, "数据格式不正确");
        }
        if (AppConfig.DEBUG) {
            Log.d(TAG, "请求地址：" + getUrl());
            Log.d(TAG, "请求参数：" + this.mParams);
            Log.d(TAG, "附加参数：" + JSON.toJSONString(getAttachParams()));
            Log.d(TAG, "返回内容：");
            String str = new String(networkResponse.data);
            if (!TextUtils.isEmpty(str)) {
                for (String str2 : str.split("\n")) {
                    Log.d(TAG, str2);
                }
            }
        }
        return com.android.volley.Response.success(responseResult, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
