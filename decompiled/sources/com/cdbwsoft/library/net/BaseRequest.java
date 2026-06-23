package com.cdbwsoft.library.net;

import android.text.TextUtils;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cdbwsoft.library.AppConfig;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseRequest<T> extends Request<T> {
    public static final String TAG = "BaseRequest";
    protected String[][] mAttachParams;
    protected final Map<String, String> mParams;

    public BaseRequest(int i, String str, Response.ErrorListener errorListener) {
        super(i, str, errorListener);
        this.mParams = new HashMap();
        this.mAttachParams = null;
        setRetryPolicy(new DefaultRetryPolicy(15000, 0, 0.0f));
    }

    public void addParam(String str, String str2) {
        if (str != null) {
            String strTrim = str.trim();
            if ("".equals(strTrim)) {
                return;
            }
            this.mParams.put(strTrim, str2);
        }
    }

    public void addParams(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return;
        }
        this.mParams.putAll(map);
    }

    public void setAttachParams(String[][] strArr) {
        this.mAttachParams = strArr;
    }

    public String[][] getAttachParams() {
        return this.mAttachParams;
    }

    @Override // com.android.volley.Request
    public void deliverError(VolleyError volleyError) {
        super.deliverError(volleyError);
        if (AppConfig.DEBUG) {
            Log.e(TAG, getUrl() + "，请求失败：" + volleyError.getMessage(), volleyError);
            if (volleyError.networkResponse != null) {
                String str = new String(volleyError.networkResponse.data);
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                for (String str2 : str.split("\n")) {
                    Log.e(TAG, str2);
                }
            }
        }
    }

    @Override // com.android.volley.Request
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.mParams;
    }

    public boolean hasBody() {
        if (this.mParams.size() != 0) {
            return true;
        }
        String[][] strArr = this.mAttachParams;
        return strArr != null && strArr.length > 0;
    }

    @Override // com.android.volley.Request
    public byte[] getBody() throws AuthFailureError {
        String[][] strArr;
        Map<String, String> params = getParams();
        if ((params == null || params.size() == 0) && ((strArr = this.mAttachParams) == null || strArr.length == 0)) {
            return null;
        }
        return encodeParameters(params, getParamsEncoding());
    }

    private byte[] encodeParameters(Map<String, String> map, String str) {
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            try {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    sb.append(URLEncoder.encode(entry.getKey(), str));
                    sb.append('=');
                    sb.append(URLEncoder.encode(entry.getValue(), str));
                    sb.append(Typography.amp);
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Encoding not supported: " + str, e);
            }
        }
        String[][] strArr = this.mAttachParams;
        if (strArr != null && strArr.length > 0) {
            for (String[] strArr2 : strArr) {
                if (strArr2 != null && strArr2.length >= 2 && !TextUtils.isEmpty(strArr2[0]) && !TextUtils.isEmpty(strArr2[1])) {
                    sb.append(URLEncoder.encode(strArr2[0], str));
                    sb.append('=');
                    sb.append(URLEncoder.encode(strArr2[1], str));
                    sb.append(Typography.amp);
                }
            }
        }
        return sb.toString().getBytes(str);
    }

    public void writeBody(OutputStream outputStream) throws IOException, AuthFailureError {
        byte[] body = getBody();
        if (body != null) {
            outputStream.write(body);
            outputStream.flush();
        }
    }
}
