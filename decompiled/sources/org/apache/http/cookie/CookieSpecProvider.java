package org.apache.http.cookie;

import org.apache.http.protocol.HttpContext;

/* JADX INFO: loaded from: classes.dex */
public interface CookieSpecProvider {
    CookieSpec create(HttpContext httpContext);
}
