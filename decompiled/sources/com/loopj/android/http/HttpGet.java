package com.loopj.android.http;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/* JADX INFO: loaded from: classes.dex */
public final class HttpGet extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "GET";

    public HttpGet() {
    }

    public HttpGet(URI uri) {
        setURI(uri);
    }

    public HttpGet(String str) {
        setURI(URI.create(str));
    }

    @Override // org.apache.http.client.methods.HttpRequestBase, org.apache.http.client.methods.HttpUriRequest
    public String getMethod() {
        return "GET";
    }
}
