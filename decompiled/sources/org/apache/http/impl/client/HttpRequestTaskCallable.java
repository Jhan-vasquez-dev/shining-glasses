package org.apache.http.impl.client;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;

/* JADX INFO: loaded from: classes2.dex */
class HttpRequestTaskCallable<V> implements Callable<V> {
    private final FutureCallback<V> callback;
    private final HttpContext context;
    private final HttpClient httpclient;
    private final FutureRequestExecutionMetrics metrics;
    private final HttpUriRequest request;
    private final ResponseHandler<V> responseHandler;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final long scheduled = System.currentTimeMillis();
    private long started = -1;
    private long ended = -1;

    HttpRequestTaskCallable(HttpClient httpClient, HttpUriRequest httpUriRequest, HttpContext httpContext, ResponseHandler<V> responseHandler, FutureCallback<V> futureCallback, FutureRequestExecutionMetrics futureRequestExecutionMetrics) {
        this.httpclient = httpClient;
        this.responseHandler = responseHandler;
        this.request = httpUriRequest;
        this.context = httpContext;
        this.callback = futureCallback;
        this.metrics = futureRequestExecutionMetrics;
    }

    public long getScheduled() {
        return this.scheduled;
    }

    public long getStarted() {
        return this.started;
    }

    public long getEnded() {
        return this.ended;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // java.util.concurrent.Callable
    public V call() throws Exception {
        if (!this.cancelled.get()) {
            try {
                this.metrics.getActiveConnections().incrementAndGet();
                this.started = System.currentTimeMillis();
                try {
                    this.metrics.getScheduledConnections().decrementAndGet();
                    V v = (V) this.httpclient.execute(this.request, this.responseHandler, this.context);
                    this.ended = System.currentTimeMillis();
                    this.metrics.getSuccessfulConnections().increment(this.started);
                    FutureCallback<V> futureCallback = this.callback;
                    if (futureCallback != null) {
                        futureCallback.completed(v);
                    }
                    return v;
                } catch (Exception e) {
                    this.metrics.getFailedConnections().increment(this.started);
                    this.ended = System.currentTimeMillis();
                    FutureCallback<V> futureCallback2 = this.callback;
                    if (futureCallback2 != null) {
                        futureCallback2.failed(e);
                    }
                    throw e;
                }
            } finally {
                this.metrics.getRequests().increment(this.started);
                this.metrics.getTasks().increment(this.started);
                this.metrics.getActiveConnections().decrementAndGet();
            }
        }
        throw new IllegalStateException("call has been cancelled for request " + this.request.getURI());
    }

    public void cancel() {
        this.cancelled.set(true);
        FutureCallback<V> futureCallback = this.callback;
        if (futureCallback != null) {
            futureCallback.cancelled();
        }
    }
}
