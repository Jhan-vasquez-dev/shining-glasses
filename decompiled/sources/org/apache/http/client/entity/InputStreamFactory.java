package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public interface InputStreamFactory {
    InputStream create(InputStream inputStream) throws IOException;
}
