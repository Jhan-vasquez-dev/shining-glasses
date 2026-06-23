package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class HttpBrowserCompatibleMultipart extends AbstractMultipartForm {
    private final List<FormBodyPart> parts;

    public HttpBrowserCompatibleMultipart(Charset charset, String str, List<FormBodyPart> list) {
        super(charset, str);
        this.parts = list;
    }

    @Override // org.apache.http.entity.mime.AbstractMultipartForm
    public List<FormBodyPart> getBodyParts() {
        return this.parts;
    }

    @Override // org.apache.http.entity.mime.AbstractMultipartForm
    protected void formatMultipartHeader(FormBodyPart formBodyPart, OutputStream outputStream) throws IOException {
        Header header = formBodyPart.getHeader();
        writeField(header.getField("Content-Disposition"), this.charset, outputStream);
        if (formBodyPart.getBody().getFilename() != null) {
            writeField(header.getField("Content-Type"), this.charset, outputStream);
        }
    }
}
