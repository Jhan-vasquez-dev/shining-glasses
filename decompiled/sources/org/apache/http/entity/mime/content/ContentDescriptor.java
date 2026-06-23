package org.apache.http.entity.mime.content;

/* JADX INFO: loaded from: classes.dex */
public interface ContentDescriptor {
    String getCharset();

    long getContentLength();

    String getMediaType();

    String getMimeType();

    String getSubType();

    String getTransferEncoding();
}
