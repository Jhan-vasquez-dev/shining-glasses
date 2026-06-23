package org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;
import org.apache.http.util.CharArrayBuffer;

/* JADX INFO: loaded from: classes2.dex */
@Deprecated
public abstract class AbstractAuthenticationHandler implements AuthenticationHandler {
    private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList("Negotiate", "NTLM", "Digest", "Basic"));
    private final Log log = LogFactory.getLog(getClass());

    protected Map<String, Header> parseChallenges(Header[] headerArr) throws MalformedChallengeException {
        CharArrayBuffer charArrayBuffer;
        int valuePos;
        HashMap map = new HashMap(headerArr.length);
        for (Header header : headerArr) {
            if (header instanceof FormattedHeader) {
                FormattedHeader formattedHeader = (FormattedHeader) header;
                charArrayBuffer = formattedHeader.getBuffer();
                valuePos = formattedHeader.getValuePos();
            } else {
                String value = header.getValue();
                if (value == null) {
                    throw new MalformedChallengeException("Header value is null");
                }
                charArrayBuffer = new CharArrayBuffer(value.length());
                charArrayBuffer.append(value);
                valuePos = 0;
            }
            while (valuePos < charArrayBuffer.length() && HTTP.isWhitespace(charArrayBuffer.charAt(valuePos))) {
                valuePos++;
            }
            int i = valuePos;
            while (i < charArrayBuffer.length() && !HTTP.isWhitespace(charArrayBuffer.charAt(i))) {
                i++;
            }
            map.put(charArrayBuffer.substring(valuePos, i).toLowerCase(Locale.ROOT), header);
        }
        return map;
    }

    protected List<String> getAuthPreferences() {
        return DEFAULT_SCHEME_PRIORITY;
    }

    protected List<String> getAuthPreferences(HttpResponse httpResponse, HttpContext httpContext) {
        return getAuthPreferences();
    }

    @Override // org.apache.http.client.AuthenticationHandler
    public AuthScheme selectScheme(Map<String, Header> map, HttpResponse httpResponse, HttpContext httpContext) throws AuthenticationException {
        AuthScheme authScheme;
        AuthSchemeRegistry authSchemeRegistry = (AuthSchemeRegistry) httpContext.getAttribute("http.authscheme-registry");
        Asserts.notNull(authSchemeRegistry, "AuthScheme registry");
        List<String> authPreferences = getAuthPreferences(httpResponse, httpContext);
        if (authPreferences == null) {
            authPreferences = DEFAULT_SCHEME_PRIORITY;
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Authentication schemes in the order of preference: " + authPreferences);
        }
        Iterator<String> it = authPreferences.iterator();
        while (true) {
            if (!it.hasNext()) {
                authScheme = null;
                break;
            }
            String next = it.next();
            if (map.get(next.toLowerCase(Locale.ENGLISH)) != null) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug(next + " authentication scheme selected");
                }
                try {
                    authScheme = authSchemeRegistry.getAuthScheme(next, httpResponse.getParams());
                    break;
                } catch (IllegalStateException unused) {
                    if (this.log.isWarnEnabled()) {
                        this.log.warn("Authentication scheme " + next + " not supported");
                    }
                }
            } else if (this.log.isDebugEnabled()) {
                this.log.debug("Challenge for " + next + " authentication scheme not available");
            }
        }
        if (authScheme != null) {
            return authScheme;
        }
        throw new AuthenticationException("Unable to respond to any of these challenges: " + map);
    }
}
