package org.apache.http.entity;

import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
public interface ContentProducer {
    void writeTo(OutputStream outputStream) throws IOException;
}
