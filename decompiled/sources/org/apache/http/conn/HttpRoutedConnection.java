package org.apache.http.conn;

import javax.net.ssl.SSLSession;
import org.apache.http.HttpInetConnection;
import org.apache.http.conn.routing.HttpRoute;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public interface HttpRoutedConnection extends HttpInetConnection {
    HttpRoute getRoute();

    SSLSession getSSLSession();

    boolean isSecure();
}
