package com.loopj.android.http;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/* JADX INFO: loaded from: classes.dex */
public final class HttpPatch extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "PATCH";

    public HttpPatch() {
    }

    public HttpPatch(URI uri) {
        setURI(uri);
    }

    public HttpPatch(String str) {
        setURI(URI.create(str));
    }

    @Override // org.apache.http.client.methods.HttpRequestBase, org.apache.http.client.methods.HttpUriRequest
    public String getMethod() {
        return "PATCH";
    }
}
