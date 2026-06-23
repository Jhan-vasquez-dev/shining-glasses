package org.apache.http.impl.cookie;

/* JADX INFO: loaded from: classes2.dex */
@Deprecated
public class BestMatchSpec extends DefaultCookieSpec {
    public BestMatchSpec(String[] strArr, boolean z) {
        super(strArr, z);
    }

    public BestMatchSpec() {
        this(null, false);
    }

    @Override // org.apache.http.impl.cookie.DefaultCookieSpec
    public String toString() {
        return "best-match";
    }
}
