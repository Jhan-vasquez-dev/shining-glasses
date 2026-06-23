package org.apache.http.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.Arrays;
import org.apache.http.HttpHost;

/* JADX INFO: loaded from: classes.dex */
public class HttpHostConnectException extends ConnectException {
    private static final long serialVersionUID = -3194482710275220224L;
    private final HttpHost host;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    @Deprecated
    public HttpHostConnectException(HttpHost httpHost, ConnectException connectException) {
        this(connectException, httpHost, null);
    }

    public HttpHostConnectException(IOException iOException, HttpHost httpHost, InetAddress... inetAddressArr) {
        super("Connect to " + (httpHost != null ? httpHost.toHostString() : "remote host") + ((inetAddressArr == null || inetAddressArr.length <= 0) ? "" : " " + Arrays.asList(inetAddressArr)) + ((iOException == null || iOException.getMessage() == null) ? " refused" : " failed: " + iOException.getMessage()));
        this.host = httpHost;
        initCause(iOException);
    }

    public HttpHost getHost() {
        return this.host;
    }
}
