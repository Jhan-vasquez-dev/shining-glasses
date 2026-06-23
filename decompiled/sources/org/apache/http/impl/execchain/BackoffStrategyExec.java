package org.apache.http.impl.execchain;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import org.apache.http.HttpException;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

/* JADX INFO: loaded from: classes2.dex */
public class BackoffStrategyExec implements ClientExecChain {
    private final BackoffManager backoffManager;
    private final ConnectionBackoffStrategy connectionBackoffStrategy;
    private final ClientExecChain requestExecutor;

    public BackoffStrategyExec(ClientExecChain clientExecChain, ConnectionBackoffStrategy connectionBackoffStrategy, BackoffManager backoffManager) {
        Args.notNull(clientExecChain, "HTTP client request executor");
        Args.notNull(connectionBackoffStrategy, "Connection backoff strategy");
        Args.notNull(backoffManager, "Backoff manager");
        this.requestExecutor = clientExecChain;
        this.connectionBackoffStrategy = connectionBackoffStrategy;
        this.backoffManager = backoffManager;
    }

    @Override // org.apache.http.impl.execchain.ClientExecChain
    public CloseableHttpResponse execute(HttpRoute httpRoute, HttpRequestWrapper httpRequestWrapper, HttpClientContext httpClientContext, HttpExecutionAware httpExecutionAware) throws HttpException, IOException {
        Args.notNull(httpRoute, "HTTP route");
        Args.notNull(httpRequestWrapper, "HTTP request");
        Args.notNull(httpClientContext, "HTTP context");
        try {
            CloseableHttpResponse closeableHttpResponseExecute = this.requestExecutor.execute(httpRoute, httpRequestWrapper, httpClientContext, httpExecutionAware);
            if (this.connectionBackoffStrategy.shouldBackoff(closeableHttpResponseExecute)) {
                this.backoffManager.backOff(httpRoute);
                return closeableHttpResponseExecute;
            }
            this.backoffManager.probe(httpRoute);
            return closeableHttpResponseExecute;
        } catch (Exception e) {
            if (this.connectionBackoffStrategy.shouldBackoff(e)) {
                this.backoffManager.backOff(httpRoute);
            }
            if (e instanceof RuntimeException) {
                throw ((RuntimeException) e);
            }
            if (e instanceof HttpException) {
                throw ((HttpException) e);
            }
            if (e instanceof IOException) {
                throw ((IOException) e);
            }
            throw new UndeclaredThrowableException(e);
        }
    }
}
