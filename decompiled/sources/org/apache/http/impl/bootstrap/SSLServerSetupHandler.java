package org.apache.http.impl.bootstrap;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocket;

/* JADX INFO: loaded from: classes2.dex */
public interface SSLServerSetupHandler {
    void initialize(SSLServerSocket sSLServerSocket) throws SSLException;
}
