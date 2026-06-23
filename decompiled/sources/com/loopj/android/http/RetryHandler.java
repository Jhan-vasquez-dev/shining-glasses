package com.loopj.android.http;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import javax.net.ssl.SSLException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;

/* JADX INFO: loaded from: classes.dex */
class RetryHandler implements HttpRequestRetryHandler {
    private static final HashSet<Class<?>> exceptionBlacklist;
    private static final HashSet<Class<?>> exceptionWhitelist;
    private final int maxRetries;
    private final int retrySleepTimeMS;

    static {
        HashSet<Class<?>> hashSet = new HashSet<>();
        exceptionWhitelist = hashSet;
        HashSet<Class<?>> hashSet2 = new HashSet<>();
        exceptionBlacklist = hashSet2;
        hashSet.add(NoHttpResponseException.class);
        hashSet.add(UnknownHostException.class);
        hashSet.add(SocketException.class);
        hashSet2.add(InterruptedIOException.class);
        hashSet2.add(SSLException.class);
    }

    public RetryHandler(int i, int i2) {
        this.maxRetries = i;
        this.retrySleepTimeMS = i2;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0013  */
    @Override // org.apache.http.client.HttpRequestRetryHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean retryRequest(java.io.IOException r3, int r4, org.apache.http.protocol.HttpContext r5) {
        /*
            r2 = this;
            java.lang.String r0 = "http.request_sent"
            java.lang.Object r0 = r5.getAttribute(r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            if (r0 == 0) goto Le
            boolean r0 = r0.booleanValue()
        Le:
            int r0 = r2.maxRetries
            r1 = 0
            if (r4 <= r0) goto L15
        L13:
            r0 = r1
            goto L28
        L15:
            java.util.HashSet<java.lang.Class<?>> r4 = com.loopj.android.http.RetryHandler.exceptionWhitelist
            boolean r4 = r2.isInList(r4, r3)
            r0 = 1
            if (r4 == 0) goto L1f
            goto L28
        L1f:
            java.util.HashSet<java.lang.Class<?>> r4 = com.loopj.android.http.RetryHandler.exceptionBlacklist
            boolean r4 = r2.isInList(r4, r3)
            if (r4 == 0) goto L28
            goto L13
        L28:
            if (r0 == 0) goto L35
            java.lang.String r4 = "http.request"
            java.lang.Object r4 = r5.getAttribute(r4)
            org.apache.http.client.methods.HttpUriRequest r4 = (org.apache.http.client.methods.HttpUriRequest) r4
            if (r4 != 0) goto L35
            return r1
        L35:
            if (r0 == 0) goto L3e
            int r3 = r2.retrySleepTimeMS
            long r3 = (long) r3
            android.os.SystemClock.sleep(r3)
            return r0
        L3e:
            r3.printStackTrace()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loopj.android.http.RetryHandler.retryRequest(java.io.IOException, int, org.apache.http.protocol.HttpContext):boolean");
    }

    static void addClassToWhitelist(Class<?> cls) {
        exceptionWhitelist.add(cls);
    }

    static void addClassToBlacklist(Class<?> cls) {
        exceptionBlacklist.add(cls);
    }

    protected boolean isInList(HashSet<Class<?>> hashSet, Throwable th) {
        Iterator<Class<?>> it = hashSet.iterator();
        while (it.hasNext()) {
            if (it.next().isInstance(th)) {
                return true;
            }
        }
        return false;
    }
}
