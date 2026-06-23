package org.apache.http.impl.cookie;

import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

/* JADX INFO: loaded from: classes2.dex */
public class NetscapeDomainHandler extends BasicDomainHandler {
    @Override // org.apache.http.impl.cookie.BasicDomainHandler, org.apache.http.cookie.CookieAttributeHandler
    public void parse(SetCookie setCookie, String str) throws MalformedCookieException {
        Args.notNull(setCookie, SM.COOKIE);
        if (TextUtils.isBlank(str)) {
            throw new MalformedCookieException("Blank or null value for domain attribute");
        }
        setCookie.setDomain(str);
    }

    @Override // org.apache.http.impl.cookie.BasicDomainHandler, org.apache.http.cookie.CookieAttributeHandler
    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
        String host = cookieOrigin.getHost();
        String domain = cookie.getDomain();
        if (!host.equals(domain) && !BasicDomainHandler.domainMatch(domain, host)) {
            throw new CookieRestrictionViolationException("Illegal domain attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
        }
        if (host.contains(".")) {
            int iCountTokens = new StringTokenizer(domain, ".").countTokens();
            if (isSpecialDomain(domain)) {
                if (iCountTokens < 2) {
                    throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification for special domains");
                }
            } else if (iCountTokens < 3) {
                throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification");
            }
        }
    }

    private static boolean isSpecialDomain(String str) {
        String upperCase = str.toUpperCase(Locale.ROOT);
        return upperCase.endsWith(".COM") || upperCase.endsWith(".EDU") || upperCase.endsWith(".NET") || upperCase.endsWith(".GOV") || upperCase.endsWith(".MIL") || upperCase.endsWith(".ORG") || upperCase.endsWith(".INT");
    }

    @Override // org.apache.http.impl.cookie.BasicDomainHandler, org.apache.http.cookie.CookieAttributeHandler
    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        Args.notNull(cookie, SM.COOKIE);
        Args.notNull(cookieOrigin, "Cookie origin");
        String host = cookieOrigin.getHost();
        String domain = cookie.getDomain();
        if (domain == null) {
            return false;
        }
        return host.endsWith(domain);
    }

    @Override // org.apache.http.impl.cookie.BasicDomainHandler, org.apache.http.cookie.CommonCookieAttributeHandler
    public String getAttributeName() {
        return ClientCookie.DOMAIN_ATTR;
    }
}
