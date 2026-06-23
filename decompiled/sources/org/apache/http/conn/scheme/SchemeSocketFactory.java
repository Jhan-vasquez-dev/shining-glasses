package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.http.params.HttpParams;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public interface SchemeSocketFactory {
    Socket connectSocket(Socket socket, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, HttpParams httpParams) throws IOException;

    Socket createSocket(HttpParams httpParams) throws IOException;

    boolean isSecure(Socket socket) throws IllegalArgumentException;
}
