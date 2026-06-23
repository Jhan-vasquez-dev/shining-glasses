package org.apache.http.auth;

import java.security.Principal;

/* JADX INFO: loaded from: classes.dex */
public interface Credentials {
    String getPassword();

    Principal getUserPrincipal();
}
