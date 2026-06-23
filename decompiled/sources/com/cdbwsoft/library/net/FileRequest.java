package com.cdbwsoft.library.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.net.entity.ProgressFileBody;
import com.cdbwsoft.library.net.entity.SuperResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class FileRequest extends BaseRequest<SuperResponse> implements ProgressFileBody.ProgressListener, Handler.Callback {
    public static final int MSG_PROGRESS = 4097;
    public static final String TAG = "FileRequest";
    private String boundary;
    private List<ProgressFileBody> mFiles;
    private Handler mHandler;
    private final FileListener mListener;
    private long mTotalAmount;

    public FileRequest(int i, String str, FileListener fileListener, Response.ErrorListener errorListener) {
        super(i, str, errorListener);
        this.mFiles = new ArrayList();
        this.boundary = "----AndroidFormBoundary7MA4YWxkTrZu0gW";
        this.mTotalAmount = 0L;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
        this.mListener = fileListener;
        fileListener.setFileRequest(this);
        initFileBody();
    }

    public FileRequest(String str, FileListener fileListener, Response.ErrorListener errorListener) {
        this(1, str, fileListener, errorListener);
    }

    public FileRequest(String str, FileListener fileListener) {
        this(1, str, fileListener, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.volley.Request
    public void deliverResponse(SuperResponse superResponse) {
        FileListener fileListener = this.mListener;
        if (fileListener != null) {
            fileListener.onResponse(superResponse);
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        List<ProgressFileBody> list;
        if (message == null || message.what != 4097 || message.obj == null) {
            return false;
        }
        ((Long) message.obj).longValue();
        if (this.mListener == null || (list = this.mFiles) == null) {
            return true;
        }
        Iterator<ProgressFileBody> it = list.iterator();
        long jMin = 0;
        while (it.hasNext()) {
            jMin += Math.min(r2.getCurrent(), it.next().getTotal());
        }
        this.mListener.onProgress(jMin, this.mTotalAmount);
        return true;
    }

    private void initFileBody() {
        List<ProgressFileBody> files;
        FileListener fileListener = this.mListener;
        if (fileListener == null || (files = fileListener.getFiles()) == null) {
            return;
        }
        for (int i = 0; i < files.size(); i++) {
            ProgressFileBody progressFileBody = files.get(i);
            progressFileBody.setProgressListener(this);
            progressFileBody.setIndex(i);
            this.mFiles.add(progressFileBody);
            this.mTotalAmount += progressFileBody.getContentLength();
        }
    }

    @Override // com.cdbwsoft.library.net.BaseRequest, com.android.volley.Request
    public void deliverError(VolleyError volleyError) {
        super.deliverError(volleyError);
        FileListener fileListener = this.mListener;
        if (fileListener != null) {
            fileListener.onResponse(com.cdbwsoft.library.net.entity.Response.result(-5, "网络繁忙"));
        }
        if (AppConfig.DEBUG) {
            Log.d(TAG, "请求地址：" + getUrl());
            Log.d(TAG, "请求参数：" + this.mParams);
            Log.d(TAG, "附加参数：" + JSON.toJSONString(getAttachParams()));
            Log.d(TAG, "返回内容：-5网络繁忙");
        }
    }

    @Override // com.cdbwsoft.library.net.BaseRequest
    public boolean hasBody() {
        if (super.hasBody()) {
            return true;
        }
        List<ProgressFileBody> list = this.mFiles;
        return list != null && list.size() > 0;
    }

    @Override // com.cdbwsoft.library.net.entity.ProgressFileBody.ProgressListener
    public void update(long j, long j2, int i) {
        this.mHandler.obtainMessage(4097, Long.valueOf(j)).sendToTarget();
    }

    @Override // com.cdbwsoft.library.net.BaseRequest
    public void writeBody(OutputStream outputStream) throws IOException, AuthFailureError {
        List<ProgressFileBody> list = this.mFiles;
        if (list != null && list.size() > 0) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                for (String str : params.keySet()) {
                    String str2 = params.get(str);
                    sb.append("--");
                    sb.append(this.boundary);
                    sb.append("\r\nContent-Disposition: form-data;name=\"");
                    sb.append(str);
                    sb.append("\"\r\n\r\n");
                    sb.append(str2);
                    sb.append("\r\n");
                }
            }
            String[][] attachParams = getAttachParams();
            if (attachParams != null && attachParams.length > 0) {
                for (String[] strArr : attachParams) {
                    if (strArr != null && strArr.length >= 2 && !TextUtils.isEmpty(strArr[0]) && !TextUtils.isEmpty(strArr[1])) {
                        String str3 = strArr[1];
                        sb.append("--");
                        sb.append(this.boundary);
                        sb.append("\r\nContent-Disposition: form-data;name=\"");
                        sb.append(strArr[0]);
                        sb.append("\"\r\n\r\n");
                        sb.append(str3);
                        sb.append("\r\n");
                    }
                }
            }
            if (sb.length() > 0) {
                outputStream.write(sb.toString().getBytes());
            }
        } else {
            outputStream.write(super.getBody());
        }
        List<ProgressFileBody> list2 = this.mFiles;
        if (list2 != null && list2.size() > 0) {
            for (ProgressFileBody progressFileBody : this.mFiles) {
                outputStream.write("--".getBytes());
                outputStream.write(this.boundary.getBytes());
                outputStream.write("\r\n".getBytes());
                StringBuilder sb2 = new StringBuilder("Content-Disposition: form-data; name=\"");
                sb2.append(progressFileBody.getKeyName()).append("\"; filename=\"");
                sb2.append(progressFileBody.getFilename()).append("\"\r\nContent-Type: ");
                sb2.append(progressFileBody.getMimeType()).append("\r\n\r\n");
                outputStream.write(sb2.toString().getBytes());
                progressFileBody.writeTo(outputStream);
                outputStream.write("\r\n".getBytes());
            }
            outputStream.write(("--" + this.boundary + "--\r\n").getBytes());
        }
        outputStream.flush();
    }

    @Override // com.android.volley.Request
    public String getBodyContentType() {
        List<ProgressFileBody> list = this.mFiles;
        if (list != null && list.size() > 0) {
            return "multipart/form-data; boundary=" + this.boundary;
        }
        return super.getBodyContentType();
    }

    public List<ProgressFileBody> getFiles() {
        return this.mFiles;
    }

    @Override // com.android.volley.Request
    protected Response<SuperResponse> parseNetworkResponse(NetworkResponse networkResponse) {
        Object objResult;
        try {
            objResult = (SuperResponse) JSON.parseObject(networkResponse.data, (Type) AppConfig.RESPONSE_CLASS, new Feature[0]);
        } catch (JSONException unused) {
            objResult = com.cdbwsoft.library.net.entity.Response.result(511, "数据格式不正确");
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
        return Response.success(objResult, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
