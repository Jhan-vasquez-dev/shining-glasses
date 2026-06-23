package com.android.volley.toolbox;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

/* JADX INFO: loaded from: classes.dex */
public class HurlStack implements HttpStack {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private final SSLSocketFactory mSslSocketFactory;
    private final UrlRewriter mUrlRewriter;

    public interface UrlRewriter {
        String rewriteUrl(String str);
    }

    private static boolean hasResponseBody(int i, int i2) {
        if (i != 4) {
            return ((100 <= i2 && i2 < 200) || i2 == 204 || i2 == 304) ? false : true;
        }
        return false;
    }

    public HurlStack() {
        this(null);
    }

    public HurlStack(UrlRewriter urlRewriter) {
        this(urlRewriter, null);
    }

    public HurlStack(UrlRewriter urlRewriter, SSLSocketFactory sSLSocketFactory) {
        this.mUrlRewriter = urlRewriter;
        this.mSslSocketFactory = sSLSocketFactory;
    }

    @Override // com.android.volley.toolbox.HttpStack
    public HttpResponse performRequest(Request<?> request, Map<String, String> map) throws IOException, AuthFailureError {
        String url = request.getUrl();
        HashMap map2 = new HashMap();
        map2.putAll(request.getHeaders());
        map2.putAll(map);
        UrlRewriter urlRewriter = this.mUrlRewriter;
        if (urlRewriter != null) {
            String strRewriteUrl = urlRewriter.rewriteUrl(url);
            if (strRewriteUrl == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }
            url = strRewriteUrl;
        }
        HttpURLConnection httpURLConnectionOpenConnection = openConnection(new URL(url), request);
        for (String str : map2.keySet()) {
            httpURLConnectionOpenConnection.addRequestProperty(str, (String) map2.get(str));
        }
        setConnectionParametersForRequest(httpURLConnectionOpenConnection, request);
        ProtocolVersion protocolVersion = new ProtocolVersion(HttpVersion.HTTP, 1, 1);
        if (httpURLConnectionOpenConnection.getResponseCode() == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        BasicStatusLine basicStatusLine = new BasicStatusLine(protocolVersion, httpURLConnectionOpenConnection.getResponseCode(), httpURLConnectionOpenConnection.getResponseMessage());
        BasicHttpResponse basicHttpResponse = new BasicHttpResponse(basicStatusLine);
        if (hasResponseBody(request.getMethod(), basicStatusLine.getStatusCode())) {
            basicHttpResponse.setEntity(entityFromConnection(httpURLConnectionOpenConnection));
        }
        for (Map.Entry<String, List<String>> entry : httpURLConnectionOpenConnection.getHeaderFields().entrySet()) {
            if (entry.getKey() != null) {
                basicHttpResponse.addHeader(new BasicHeader(entry.getKey(), entry.getValue().get(0)));
            }
        }
        return basicHttpResponse;
    }

    private static HttpEntity entityFromConnection(HttpURLConnection httpURLConnection) {
        InputStream errorStream;
        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        try {
            errorStream = httpURLConnection.getInputStream();
        } catch (IOException unused) {
            errorStream = httpURLConnection.getErrorStream();
        }
        basicHttpEntity.setContent(errorStream);
        basicHttpEntity.setContentLength(httpURLConnection.getContentLength());
        basicHttpEntity.setContentEncoding(httpURLConnection.getContentEncoding());
        basicHttpEntity.setContentType(httpURLConnection.getContentType());
        return basicHttpEntity;
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        SSLSocketFactory sSLSocketFactory;
        HttpURLConnection httpURLConnectionCreateConnection = createConnection(url);
        int timeoutMs = request.getTimeoutMs();
        httpURLConnectionCreateConnection.setConnectTimeout(timeoutMs);
        httpURLConnectionCreateConnection.setReadTimeout(timeoutMs);
        httpURLConnectionCreateConnection.setUseCaches(false);
        httpURLConnectionCreateConnection.setDoInput(true);
        if ("https".equals(url.getProtocol()) && (sSLSocketFactory = this.mSslSocketFactory) != null) {
            ((HttpsURLConnection) httpURLConnectionCreateConnection).setSSLSocketFactory(sSLSocketFactory);
        }
        return httpURLConnectionCreateConnection;
    }

    static void setConnectionParametersForRequest(HttpURLConnection httpURLConnection, Request<?> request) throws IOException, AuthFailureError {
        switch (request.getMethod()) {
            case -1:
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
                    httpURLConnection.addRequestProperty("Content-Type", request.getPostBodyContentType());
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.write(postBody);
                    dataOutputStream.close();
                    return;
                }
                return;
            case 0:
                httpURLConnection.setRequestMethod("GET");
                return;
            case 1:
                httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
                addBodyIfExists(httpURLConnection, request);
                return;
            case 2:
                httpURLConnection.setRequestMethod(HttpPut.METHOD_NAME);
                addBodyIfExists(httpURLConnection, request);
                return;
            case 3:
                httpURLConnection.setRequestMethod("DELETE");
                return;
            case 4:
                httpURLConnection.setRequestMethod(HttpHead.METHOD_NAME);
                return;
            case 5:
                httpURLConnection.setRequestMethod(HttpOptions.METHOD_NAME);
                return;
            case 6:
                httpURLConnection.setRequestMethod(HttpTrace.METHOD_NAME);
                return;
            case 7:
                httpURLConnection.setRequestMethod("PATCH");
                addBodyIfExists(httpURLConnection, request);
                return;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static void addBodyIfExists(HttpURLConnection httpURLConnection, Request<?> request) throws IOException, AuthFailureError {
        byte[] body = request.getBody();
        if (body != null) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.addRequestProperty("Content-Type", request.getBodyContentType());
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(body);
            dataOutputStream.close();
        }
    }
}
