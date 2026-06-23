package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionOperator;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultHttpClientConnectionOperator implements HttpClientConnectionOperator {
    static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";
    private final DnsResolver dnsResolver;
    private final Log log = LogFactory.getLog(getClass());
    private final SchemePortResolver schemePortResolver;
    private final Lookup<ConnectionSocketFactory> socketFactoryRegistry;

    public DefaultHttpClientConnectionOperator(Lookup<ConnectionSocketFactory> lookup, SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
        Args.notNull(lookup, "Socket factory registry");
        this.socketFactoryRegistry = lookup;
        this.schemePortResolver = schemePortResolver == null ? DefaultSchemePortResolver.INSTANCE : schemePortResolver;
        this.dnsResolver = dnsResolver == null ? SystemDefaultDnsResolver.INSTANCE : dnsResolver;
    }

    private Lookup<ConnectionSocketFactory> getSocketFactoryRegistry(HttpContext httpContext) {
        Lookup<ConnectionSocketFactory> lookup = (Lookup) httpContext.getAttribute("http.socket-factory-registry");
        return lookup == null ? this.socketFactoryRegistry : lookup;
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0120 A[SYNTHETIC] */
    @Override // org.apache.http.conn.HttpClientConnectionOperator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void connect(org.apache.http.conn.ManagedHttpClientConnection r17, org.apache.http.HttpHost r18, java.net.InetSocketAddress r19, int r20, org.apache.http.config.SocketConfig r21, org.apache.http.protocol.HttpContext r22) throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 330
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.DefaultHttpClientConnectionOperator.connect(org.apache.http.conn.ManagedHttpClientConnection, org.apache.http.HttpHost, java.net.InetSocketAddress, int, org.apache.http.config.SocketConfig, org.apache.http.protocol.HttpContext):void");
    }

    @Override // org.apache.http.conn.HttpClientConnectionOperator
    public void upgrade(ManagedHttpClientConnection managedHttpClientConnection, HttpHost httpHost, HttpContext httpContext) throws IOException {
        ConnectionSocketFactory connectionSocketFactoryLookup = getSocketFactoryRegistry(HttpClientContext.adapt(httpContext)).lookup(httpHost.getSchemeName());
        if (connectionSocketFactoryLookup == null) {
            throw new UnsupportedSchemeException(httpHost.getSchemeName() + " protocol is not supported");
        }
        if (!(connectionSocketFactoryLookup instanceof LayeredConnectionSocketFactory)) {
            throw new UnsupportedSchemeException(httpHost.getSchemeName() + " protocol does not support connection upgrade");
        }
        managedHttpClientConnection.bind(((LayeredConnectionSocketFactory) connectionSocketFactoryLookup).createLayeredSocket(managedHttpClientConnection.getSocket(), httpHost.getHostName(), this.schemePortResolver.resolve(httpHost), httpContext));
    }
}
