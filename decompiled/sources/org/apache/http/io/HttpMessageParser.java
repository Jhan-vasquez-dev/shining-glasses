package org.apache.http.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

/* JADX INFO: loaded from: classes2.dex */
public interface HttpMessageParser<T extends HttpMessage> {
    T parse() throws HttpException, IOException;
}
