package org.apache.http.entity.mime.content;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.util.Args;

/* JADX INFO: loaded from: classes.dex */
public class ByteArrayBody extends AbstractContentBody {
    private final byte[] data;
    private final String filename;

    @Override // org.apache.http.entity.mime.content.AbstractContentBody, org.apache.http.entity.mime.content.ContentDescriptor
    public String getCharset() {
        return null;
    }

    @Deprecated
    public ByteArrayBody(byte[] bArr, String str, String str2) {
        this(bArr, ContentType.create(str), str2);
    }

    public ByteArrayBody(byte[] bArr, ContentType contentType, String str) {
        super(contentType);
        Args.notNull(bArr, "byte[]");
        this.data = bArr;
        this.filename = str;
    }

    public ByteArrayBody(byte[] bArr, String str) {
        this(bArr, "application/octet-stream", str);
    }

    @Override // org.apache.http.entity.mime.content.ContentBody
    public String getFilename() {
        return this.filename;
    }

    @Override // org.apache.http.entity.mime.content.ContentBody
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.data);
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public long getContentLength() {
        return this.data.length;
    }
}
