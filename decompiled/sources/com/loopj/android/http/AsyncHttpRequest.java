package com.loopj.android.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

/* JADX INFO: loaded from: classes.dex */
public class AsyncHttpRequest implements Runnable {
    private boolean cancelIsNotified;
    private final AbstractHttpClient client;
    private final HttpContext context;
    private int executionCount;
    private final AtomicBoolean isCancelled = new AtomicBoolean();
    private volatile boolean isFinished;
    private boolean isRequestPreProcessed;
    private final HttpUriRequest request;
    private final ResponseHandlerInterface responseHandler;

    public void onPostProcessRequest(AsyncHttpRequest asyncHttpRequest) {
    }

    public void onPreProcessRequest(AsyncHttpRequest asyncHttpRequest) {
    }

    public AsyncHttpRequest(AbstractHttpClient abstractHttpClient, HttpContext httpContext, HttpUriRequest httpUriRequest, ResponseHandlerInterface responseHandlerInterface) {
        this.client = (AbstractHttpClient) Utils.notNull(abstractHttpClient, "client");
        this.context = (HttpContext) Utils.notNull(httpContext, "context");
        this.request = (HttpUriRequest) Utils.notNull(httpUriRequest, "request");
        this.responseHandler = (ResponseHandlerInterface) Utils.notNull(responseHandlerInterface, "responseHandler");
    }

    @Override // java.lang.Runnable
    public void run() {
        if (isCancelled()) {
            return;
        }
        if (!this.isRequestPreProcessed) {
            this.isRequestPreProcessed = true;
            onPreProcessRequest(this);
        }
        if (isCancelled()) {
            return;
        }
        this.responseHandler.sendStartMessage();
        if (isCancelled()) {
            return;
        }
        try {
            makeRequestWithRetries();
        } catch (IOException e) {
            if (!isCancelled()) {
                this.responseHandler.sendFailureMessage(0, null, null, e);
            } else {
                AsyncHttpClient.log.e("AsyncHttpRequest", "makeRequestWithRetries returned error", e);
            }
        }
        if (isCancelled()) {
            return;
        }
        this.responseHandler.sendFinishMessage();
        if (isCancelled()) {
            return;
        }
        onPostProcessRequest(this);
        this.isFinished = true;
    }

    private void makeRequest() throws IOException {
        if (isCancelled()) {
            return;
        }
        if (this.request.getURI().getScheme() == null) {
            throw new MalformedURLException("No valid URI scheme was provided");
        }
        ResponseHandlerInterface responseHandlerInterface = this.responseHandler;
        if (responseHandlerInterface instanceof RangeFileAsyncHttpResponseHandler) {
            ((RangeFileAsyncHttpResponseHandler) responseHandlerInterface).updateRequestHeaders(this.request);
        }
        HttpResponse httpResponseExecute = this.client.execute(this.request, this.context);
        if (isCancelled()) {
            return;
        }
        ResponseHandlerInterface responseHandlerInterface2 = this.responseHandler;
        responseHandlerInterface2.onPreProcessResponse(responseHandlerInterface2, httpResponseExecute);
        if (isCancelled()) {
            return;
        }
        this.responseHandler.sendResponseMessage(httpResponseExecute);
        if (isCancelled()) {
            return;
        }
        ResponseHandlerInterface responseHandlerInterface3 = this.responseHandler;
        responseHandlerInterface3.onPostProcessResponse(responseHandlerInterface3, httpResponseExecute);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0085 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0009 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void makeRequestWithRetries() throws java.io.IOException {
        /*
            r7 = this;
            org.apache.http.impl.client.AbstractHttpClient r0 = r7.client
            org.apache.http.client.HttpRequestRetryHandler r0 = r0.getHttpRequestRetryHandler()
            r1 = 1
            r2 = 0
            r3 = r1
        L9:
            if (r3 == 0) goto Laf
            r7.makeRequest()     // Catch: java.lang.Exception -> Lf java.io.IOException -> L12 java.lang.NullPointerException -> L26 java.net.UnknownHostException -> L4f
            goto L19
        Lf:
            r0 = move-exception
            goto L8e
        L12:
            r2 = move-exception
            boolean r3 = r7.isCancelled()     // Catch: java.lang.Exception -> Lf
            if (r3 == 0) goto L1a
        L19:
            return
        L1a:
            int r3 = r7.executionCount     // Catch: java.lang.Exception -> Lf
            int r3 = r3 + r1
            r7.executionCount = r3     // Catch: java.lang.Exception -> Lf
            org.apache.http.protocol.HttpContext r4 = r7.context     // Catch: java.lang.Exception -> Lf
            boolean r3 = r0.retryRequest(r2, r3, r4)     // Catch: java.lang.Exception -> Lf
            goto L83
        L26:
            r2 = move-exception
            java.io.IOException r3 = new java.io.IOException     // Catch: java.lang.Exception -> Lf
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lf
            r4.<init>()     // Catch: java.lang.Exception -> Lf
            java.lang.String r5 = "NPE in HttpClient: "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> Lf
            java.lang.String r2 = r2.getMessage()     // Catch: java.lang.Exception -> Lf
            java.lang.StringBuilder r2 = r4.append(r2)     // Catch: java.lang.Exception -> Lf
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> Lf
            r3.<init>(r2)     // Catch: java.lang.Exception -> Lf
            int r2 = r7.executionCount     // Catch: java.lang.Exception -> Lf
            int r2 = r2 + r1
            r7.executionCount = r2     // Catch: java.lang.Exception -> Lf
            org.apache.http.protocol.HttpContext r4 = r7.context     // Catch: java.lang.Exception -> Lf
            boolean r2 = r0.retryRequest(r3, r2, r4)     // Catch: java.lang.Exception -> Lf
            goto L80
        L4f:
            r2 = move-exception
            java.io.IOException r3 = new java.io.IOException     // Catch: java.lang.Exception -> Lf
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lf
            r4.<init>()     // Catch: java.lang.Exception -> Lf
            java.lang.String r5 = "UnknownHostException exception: "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> Lf
            java.lang.String r5 = r2.getMessage()     // Catch: java.lang.Exception -> Lf
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> Lf
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> Lf
            r3.<init>(r4)     // Catch: java.lang.Exception -> Lf
            int r4 = r7.executionCount     // Catch: java.lang.Exception -> Lf
            if (r4 <= 0) goto L7f
            int r4 = r7.executionCount     // Catch: java.lang.Exception -> Lf
            int r4 = r4 + r1
            r7.executionCount = r4     // Catch: java.lang.Exception -> Lf
            org.apache.http.protocol.HttpContext r5 = r7.context     // Catch: java.lang.Exception -> Lf
            boolean r2 = r0.retryRequest(r2, r4, r5)     // Catch: java.lang.Exception -> Lf
            if (r2 == 0) goto L7f
            r2 = r1
            goto L80
        L7f:
            r2 = 0
        L80:
            r6 = r3
            r3 = r2
            r2 = r6
        L83:
            if (r3 == 0) goto L9
            com.loopj.android.http.ResponseHandlerInterface r4 = r7.responseHandler     // Catch: java.lang.Exception -> Lf
            int r5 = r7.executionCount     // Catch: java.lang.Exception -> Lf
            r4.sendRetryMessage(r5)     // Catch: java.lang.Exception -> Lf
            goto L9
        L8e:
            com.loopj.android.http.LogInterface r1 = com.loopj.android.http.AsyncHttpClient.log
            java.lang.String r2 = "AsyncHttpRequest"
            java.lang.String r3 = "Unhandled exception origin cause"
            r1.e(r2, r3, r0)
            java.io.IOException r2 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r3 = "Unhandled exception: "
            r1.<init>(r3)
            java.lang.String r0 = r0.getMessage()
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r0 = r0.toString()
            r2.<init>(r0)
        Laf:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loopj.android.http.AsyncHttpRequest.makeRequestWithRetries():void");
    }

    public boolean isCancelled() {
        boolean z = this.isCancelled.get();
        if (z) {
            sendCancelNotification();
        }
        return z;
    }

    private synchronized void sendCancelNotification() {
        if (!this.isFinished && this.isCancelled.get() && !this.cancelIsNotified) {
            this.cancelIsNotified = true;
            this.responseHandler.sendCancelMessage();
        }
    }

    public boolean isDone() {
        return isCancelled() || this.isFinished;
    }

    public boolean cancel(boolean z) {
        this.isCancelled.set(true);
        this.request.abort();
        return isCancelled();
    }

    public AsyncHttpRequest setRequestTag(Object obj) {
        this.responseHandler.setTag(obj);
        return this;
    }

    public Object getTag() {
        return this.responseHandler.getTag();
    }
}
