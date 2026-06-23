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
import com.cdbwsoft.library.net.entity.ResponseList;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class ListRequest<T> extends BaseRequest<ResponseList<T>> {
    public static final String TAG = "ListRequest";
    private final ResponseListener<ResponseList<T>> mListener;

    public ListRequest(int i, String str, ResponseListener<ResponseList<T>> responseListener, Response.ErrorListener errorListener) {
        super(i, str, errorListener);
        this.mListener = responseListener;
    }

    public ListRequest(String str, ResponseListener<ResponseList<T>> responseListener, Response.ErrorListener errorListener) {
        this(1, str, responseListener, errorListener);
    }

    public ListRequest(String str, ResponseListener<ResponseList<T>> responseListener) {
        this(1, str, responseListener, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.volley.Request
    public void deliverResponse(ResponseList<T> responseList) {
        ResponseListener<ResponseList<T>> responseListener = this.mListener;
        if (responseListener != null) {
            responseListener.onResponse(responseList);
        }
    }

    @Override // com.cdbwsoft.library.net.BaseRequest, com.android.volley.Request
    public void deliverError(VolleyError volleyError) {
        super.deliverError(volleyError);
        if (this.mListener != null) {
            ResponseList<T> responseList = new ResponseList<>();
            responseList.setStatus(-5);
            responseList.setData("网络繁忙");
            this.mListener.onResponse(responseList);
        }
        if (AppConfig.DEBUG) {
            Log.d(TAG, "请求地址：" + getUrl());
            Log.d(TAG, "请求参数：" + this.mParams);
            Log.d(TAG, "附加参数：" + JSON.toJSONString(getAttachParams()));
            Log.d(TAG, "返回内容：-5网络繁忙");
        }
    }

    @Override // com.android.volley.Request
    protected Response<ResponseList<T>> parseNetworkResponse(NetworkResponse networkResponse) {
        ResponseList responseList;
        Type[] actualTypeArguments;
        try {
            responseList = (ResponseList) JSON.parseObject(networkResponse.data, (Type) ResponseList.class, new Feature[0]);
            if (this.mListener.getType() instanceof Class) {
                responseList.parseList((Class) this.mListener.getType());
            } else if ((this.mListener.getType() instanceof ParameterizedType) && (actualTypeArguments = ((ParameterizedType) this.mListener.getType()).getActualTypeArguments()) != null && actualTypeArguments.length > 0) {
                Type type = actualTypeArguments[0];
                if (type instanceof Class) {
                    responseList.parseList((Class) type);
                }
            }
        } catch (JSONException unused) {
            responseList = new ResponseList();
            responseList.setStatus(511);
            responseList.setData("数据格式不正确");
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
        return Response.success(responseList, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
