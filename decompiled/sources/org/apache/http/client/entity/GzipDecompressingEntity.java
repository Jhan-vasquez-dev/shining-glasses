package org.apache.http.client.entity;

import org.apache.http.HttpEntity;

/* JADX INFO: loaded from: classes.dex */
public class GzipDecompressingEntity extends DecompressingEntity {
    public GzipDecompressingEntity(HttpEntity httpEntity) {
        super(httpEntity, GZIPInputStreamFactory.getInstance());
    }
}
