package org.apache.http;

/* JADX INFO: loaded from: classes.dex */
public interface StatusLine {
    ProtocolVersion getProtocolVersion();

    String getReasonPhrase();

    int getStatusCode();
}
