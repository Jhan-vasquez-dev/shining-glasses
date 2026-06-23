package org.apache.http.entity.mime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.ContentTooLongException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

/* JADX INFO: loaded from: classes.dex */
class MultipartFormEntity implements HttpEntity {
    private final long contentLength;
    private final org.apache.http.Header contentType;
    private final AbstractMultipartForm multipart;

    @Override // org.apache.http.HttpEntity
    public void consumeContent() {
    }

    @Override // org.apache.http.HttpEntity
    public org.apache.http.Header getContentEncoding() {
        return null;
    }

    MultipartFormEntity(AbstractMultipartForm abstractMultipartForm, ContentType contentType, long j) {
        this.multipart = abstractMultipartForm;
        this.contentType = new BasicHeader("Content-Type", contentType.toString());
        this.contentLength = j;
    }

    AbstractMultipartForm getMultipart() {
        return this.multipart;
    }

    @Override // org.apache.http.HttpEntity
    public boolean isRepeatable() {
        return this.contentLength != -1;
    }

    @Override // org.apache.http.HttpEntity
    public boolean isChunked() {
        return !isRepeatable();
    }

    @Override // org.apache.http.HttpEntity
    public boolean isStreaming() {
        return !isRepeatable();
    }

    @Override // org.apache.http.HttpEntity
    public long getContentLength() {
        return this.contentLength;
    }

    @Override // org.apache.http.HttpEntity
    public org.apache.http.Header getContentType() {
        return this.contentType;
    }

    @Override // org.apache.http.HttpEntity
    public InputStream getContent() throws IOException {
        long j = this.contentLength;
        if (j < 0) {
            throw new ContentTooLongException("Content length is unknown");
        }
        if (j > 25600) {
            throw new ContentTooLongException("Content length is too long: " + this.contentLength);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeTo(byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override // org.apache.http.HttpEntity
    public void writeTo(OutputStream outputStream) throws IOException {
        this.multipart.writeTo(outputStream);
    }
}
