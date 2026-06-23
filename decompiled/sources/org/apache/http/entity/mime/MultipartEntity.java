package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Random;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.protocol.HTTP;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class MultipartEntity implements HttpEntity {
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final MultipartEntityBuilder builder;
    private volatile MultipartFormEntity entity;

    public MultipartEntity(HttpMultipartMode httpMultipartMode, String str, Charset charset) {
        this.builder = new MultipartEntityBuilder().setMode(httpMultipartMode).setCharset(charset == null ? MIME.DEFAULT_CHARSET : charset).setBoundary(str);
        this.entity = null;
    }

    public MultipartEntity(HttpMultipartMode httpMultipartMode) {
        this(httpMultipartMode, null, null);
    }

    public MultipartEntity() {
        this(HttpMultipartMode.STRICT, null, null);
    }

    protected String generateContentType(String str, Charset charset) {
        StringBuilder sb = new StringBuilder("multipart/form-data; boundary=");
        sb.append(str);
        if (charset != null) {
            sb.append(HTTP.CHARSET_PARAM);
            sb.append(charset.name());
        }
        return sb.toString();
    }

    protected String generateBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int iNextInt = random.nextInt(11) + 30;
        for (int i = 0; i < iNextInt; i++) {
            char[] cArr = MULTIPART_CHARS;
            sb.append(cArr[random.nextInt(cArr.length)]);
        }
        return sb.toString();
    }

    private MultipartFormEntity getEntity() {
        if (this.entity == null) {
            this.entity = this.builder.buildEntity();
        }
        return this.entity;
    }

    public void addPart(FormBodyPart formBodyPart) {
        this.builder.addPart(formBodyPart);
        this.entity = null;
    }

    public void addPart(String str, ContentBody contentBody) {
        addPart(new FormBodyPart(str, contentBody));
    }

    @Override // org.apache.http.HttpEntity
    public boolean isRepeatable() {
        return getEntity().isRepeatable();
    }

    @Override // org.apache.http.HttpEntity
    public boolean isChunked() {
        return getEntity().isChunked();
    }

    @Override // org.apache.http.HttpEntity
    public boolean isStreaming() {
        return getEntity().isStreaming();
    }

    @Override // org.apache.http.HttpEntity
    public long getContentLength() {
        return getEntity().getContentLength();
    }

    @Override // org.apache.http.HttpEntity
    public org.apache.http.Header getContentType() {
        return getEntity().getContentType();
    }

    @Override // org.apache.http.HttpEntity
    public org.apache.http.Header getContentEncoding() {
        return getEntity().getContentEncoding();
    }

    @Override // org.apache.http.HttpEntity
    public void consumeContent() throws UnsupportedOperationException, IOException {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    @Override // org.apache.http.HttpEntity
    public InputStream getContent() throws UnsupportedOperationException, IOException {
        throw new UnsupportedOperationException("Multipart form entity does not implement #getContent()");
    }

    @Override // org.apache.http.HttpEntity
    public void writeTo(OutputStream outputStream) throws IOException {
        getEntity().writeTo(outputStream);
    }
}
