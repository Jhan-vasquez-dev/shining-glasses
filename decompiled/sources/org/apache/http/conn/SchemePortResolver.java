package org.apache.http.conn;

import org.apache.http.HttpHost;

/* JADX INFO: loaded from: classes.dex */
public interface SchemePortResolver {
    int resolve(HttpHost httpHost) throws UnsupportedSchemeException;
}
