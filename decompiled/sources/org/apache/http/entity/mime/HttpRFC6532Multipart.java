package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class HttpRFC6532Multipart extends AbstractMultipartForm {
    private final List<FormBodyPart> parts;

    public HttpRFC6532Multipart(Charset charset, String str, List<FormBodyPart> list) {
        super(charset, str);
        this.parts = list;
    }

    @Override // org.apache.http.entity.mime.AbstractMultipartForm
    public List<FormBodyPart> getBodyParts() {
        return this.parts;
    }

    @Override // org.apache.http.entity.mime.AbstractMultipartForm
    protected void formatMultipartHeader(FormBodyPart formBodyPart, OutputStream outputStream) throws IOException {
        Iterator<MinimalField> it = formBodyPart.getHeader().iterator();
        while (it.hasNext()) {
            writeField(it.next(), MIME.UTF8_CHARSET, outputStream);
        }
    }
}
