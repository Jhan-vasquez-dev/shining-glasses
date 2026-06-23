package org.apache.http.impl.client;

import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;

/* JADX INFO: loaded from: classes2.dex */
public class LaxRedirectStrategy extends DefaultRedirectStrategy {
    public static final LaxRedirectStrategy INSTANCE = new LaxRedirectStrategy();
    private static final String[] REDIRECT_METHODS = {"GET", HttpPost.METHOD_NAME, HttpHead.METHOD_NAME, "DELETE"};

    @Override // org.apache.http.impl.client.DefaultRedirectStrategy
    protected boolean isRedirectable(String str) {
        for (String str2 : REDIRECT_METHODS) {
            if (str2.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
