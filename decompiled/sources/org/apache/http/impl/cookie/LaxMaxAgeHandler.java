package org.apache.http.impl.cookie;

import java.util.Date;
import java.util.regex.Pattern;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

/* JADX INFO: loaded from: classes2.dex */
public class LaxMaxAgeHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
    private static final Pattern MAX_AGE_PATTERN = Pattern.compile("^\\-?[0-9]+$");

    @Override // org.apache.http.cookie.CookieAttributeHandler
    public void parse(SetCookie setCookie, String str) throws MalformedCookieException {
        Args.notNull(setCookie, SM.COOKIE);
        if (!TextUtils.isBlank(str) && MAX_AGE_PATTERN.matcher(str).matches()) {
            try {
                int i = Integer.parseInt(str);
                setCookie.setExpiryDate(i >= 0 ? new Date(System.currentTimeMillis() + (((long) i) * 1000)) : new Date(Long.MIN_VALUE));
            } catch (NumberFormatException unused) {
            }
        }
    }

    @Override // org.apache.http.cookie.CommonCookieAttributeHandler
    public String getAttributeName() {
        return ClientCookie.MAX_AGE_ATTR;
    }
}
