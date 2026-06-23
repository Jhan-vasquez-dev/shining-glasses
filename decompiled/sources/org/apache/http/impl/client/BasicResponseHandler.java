package org.apache.http.impl.client;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

/* JADX INFO: loaded from: classes2.dex */
public class BasicResponseHandler extends AbstractResponseHandler<String> {
    @Override // org.apache.http.impl.client.AbstractResponseHandler
    public String handleEntity(HttpEntity httpEntity) throws IOException {
        return EntityUtils.toString(httpEntity);
    }

    @Override // org.apache.http.impl.client.AbstractResponseHandler, org.apache.http.client.ResponseHandler
    public String handleResponse(HttpResponse httpResponse) throws IOException {
        return (String) super.handleResponse(httpResponse);
    }
}
