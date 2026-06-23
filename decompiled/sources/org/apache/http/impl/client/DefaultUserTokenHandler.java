package org.apache.http.impl.client;

import java.security.Principal;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpConnection;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.protocol.HttpContext;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultUserTokenHandler implements UserTokenHandler {
    public static final DefaultUserTokenHandler INSTANCE = new DefaultUserTokenHandler();

    @Override // org.apache.http.client.UserTokenHandler
    public Object getUserToken(HttpContext httpContext) {
        Principal authPrincipal;
        SSLSession sSLSession;
        HttpClientContext httpClientContextAdapt = HttpClientContext.adapt(httpContext);
        AuthState targetAuthState = httpClientContextAdapt.getTargetAuthState();
        if (targetAuthState != null) {
            authPrincipal = getAuthPrincipal(targetAuthState);
            if (authPrincipal == null) {
                authPrincipal = getAuthPrincipal(httpClientContextAdapt.getProxyAuthState());
            }
        } else {
            authPrincipal = null;
        }
        if (authPrincipal == null) {
            HttpConnection connection = httpClientContextAdapt.getConnection();
            if (connection.isOpen() && (connection instanceof ManagedHttpClientConnection) && (sSLSession = ((ManagedHttpClientConnection) connection).getSSLSession()) != null) {
                return sSLSession.getLocalPrincipal();
            }
        }
        return authPrincipal;
    }

    private static Principal getAuthPrincipal(AuthState authState) {
        Credentials credentials;
        AuthScheme authScheme = authState.getAuthScheme();
        if (authScheme == null || !authScheme.isComplete() || !authScheme.isConnectionBased() || (credentials = authState.getCredentials()) == null) {
            return null;
        }
        return credentials.getUserPrincipal();
    }
}
