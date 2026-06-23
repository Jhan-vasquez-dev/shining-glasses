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
import com.cdbwsoft.library.net.entity.SuperResponse;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class CommonRequest<T> extends BaseRequest<T> {
    public static final String TAG = "CommonRequest";
    private final ResponseListener<T> mListener;
    private SuperResponse mResponse;

    public CommonRequest(int i, String str, ResponseListener<T> responseListener, Response.ErrorListener errorListener) {
        super(i, str, errorListener);
        this.mListener = responseListener;
    }

    public CommonRequest(String str, ResponseListener<T> responseListener, Response.ErrorListener errorListener) {
        this(1, str, responseListener, errorListener);
    }

    public CommonRequest(String str, ResponseListener<T> responseListener) {
        this(1, str, responseListener, null);
    }

    @Override // com.android.volley.Request
    protected void deliverResponse(T t) {
        ResponseListener<T> responseListener = this.mListener;
        if (responseListener != null) {
            responseListener.onResponse(this.mResponse, t);
        }
    }

    @Override // com.cdbwsoft.library.net.BaseRequest, com.android.volley.Request
    public void deliverError(VolleyError volleyError) {
        super.deliverError(volleyError);
        ResponseListener<T> responseListener = this.mListener;
        if (responseListener != null) {
            responseListener.onResponse(AppConfig.RESPONSE_FACTORY.newInstance(-5, "网络繁忙"), null);
        }
        if (AppConfig.DEBUG) {
            Log.d(TAG, "请求地址：" + getUrl());
            Log.d(TAG, "请求参数：" + this.mParams);
            Log.d(TAG, "附加参数：" + JSON.toJSONString(getAttachParams()));
            Log.d(TAG, "返回内容：-5网络繁忙");
        }
    }

    @Override // com.android.volley.Request
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        Object object = null;
        try {
            SuperResponse superResponse = (SuperResponse) JSON.parseObject(networkResponse.data, (Type) AppConfig.RESPONSE_CLASS, new Feature[0]);
            this.mResponse = superResponse;
            if (this.mListener != null) {
                object = JSON.parseObject(superResponse.getData(), this.mListener.getType(), new Feature[0]);
            }
        } catch (JSONException unused) {
            if (this.mResponse == null) {
                this.mResponse = AppConfig.RESPONSE_FACTORY.newInstance(511, "数据格式不正确");
            }
        }
        if (AppConfig.DEBUG) {
            Log.d(TAG, "请求地址：" + getUrl());
            Log.d(TAG, "请求参数：" + this.mParams);
            Log.d(TAG, "附加参数：" + JSON.toJSONString(getAttachParams()));
            Log.d(TAG, "返回内容：");
            String str = new String(networkResponse.data);
            if (!TextUtils.isEmpty(str)) {
                for (String str2 : str.split("\n")) {
                    Log.e(TAG, str2);
                }
            }
        }
        return Response.success(object, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
