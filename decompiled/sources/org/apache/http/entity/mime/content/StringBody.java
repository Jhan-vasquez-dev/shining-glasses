package org.apache.http.entity.mime.content;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

/* JADX INFO: loaded from: classes.dex */
public class StringBody extends AbstractContentBody {
    private final byte[] content;

    @Override // org.apache.http.entity.mime.content.ContentBody
    public String getFilename() {
        return null;
    }

    @Deprecated
    public static StringBody create(String str, String str2, Charset charset) throws IllegalArgumentException {
        try {
            return new StringBody(str, str2, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Charset " + charset + " is not supported", e);
        }
    }

    @Deprecated
    public static StringBody create(String str, Charset charset) throws IllegalArgumentException {
        return create(str, null, charset);
    }

    @Deprecated
    public static StringBody create(String str) throws IllegalArgumentException {
        return create(str, null, null);
    }

    @Deprecated
    public StringBody(String str, String str2, Charset charset) throws UnsupportedEncodingException {
        this(str, ContentType.create(str2, charset == null ? Consts.ASCII : charset));
    }

    @Deprecated
    public StringBody(String str, Charset charset) throws UnsupportedEncodingException {
        this(str, HTTP.PLAIN_TEXT_TYPE, charset);
    }

    @Deprecated
    public StringBody(String str) throws UnsupportedEncodingException {
        this(str, HTTP.PLAIN_TEXT_TYPE, Consts.ASCII);
    }

    public StringBody(String str, ContentType contentType) {
        super(contentType);
        Args.notNull(str, "Text");
        Charset charset = contentType.getCharset();
        this.content = str.getBytes(charset == null ? Consts.ASCII : charset);
    }

    public Reader getReader() {
        Charset charset = getContentType().getCharset();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.content);
        if (charset == null) {
            charset = Consts.ASCII;
        }
        return new InputStreamReader(byteArrayInputStream, charset);
    }

    @Override // org.apache.http.entity.mime.content.ContentBody
    public void writeTo(OutputStream outputStream) throws IOException {
        Args.notNull(outputStream, "Output stream");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.content);
        byte[] bArr = new byte[4096];
        while (true) {
            int i = byteArrayInputStream.read(bArr);
            if (i != -1) {
                outputStream.write(bArr, 0, i);
            } else {
                outputStream.flush();
                return;
            }
        }
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public String getTransferEncoding() {
        return MIME.ENC_8BIT;
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public long getContentLength() {
        return this.content.length;
    }
}
