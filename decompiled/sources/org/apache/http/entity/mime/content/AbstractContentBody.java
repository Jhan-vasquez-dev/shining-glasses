package org.apache.http.entity.mime.content;

import java.nio.charset.Charset;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractContentBody implements ContentBody {
    private final ContentType contentType;

    public AbstractContentBody(ContentType contentType) {
        Args.notNull(contentType, "Content type");
        this.contentType = contentType;
    }

    @Deprecated
    public AbstractContentBody(String str) {
        this(ContentType.parse(str));
    }

    public ContentType getContentType() {
        return this.contentType;
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public String getMimeType() {
        return this.contentType.getMimeType();
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public String getMediaType() {
        String mimeType = this.contentType.getMimeType();
        int iIndexOf = mimeType.indexOf(47);
        return iIndexOf != -1 ? mimeType.substring(0, iIndexOf) : mimeType;
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public String getSubType() {
        String mimeType = this.contentType.getMimeType();
        int iIndexOf = mimeType.indexOf(47);
        if (iIndexOf != -1) {
            return mimeType.substring(iIndexOf + 1);
        }
        return null;
    }

    @Override // org.apache.http.entity.mime.content.ContentDescriptor
    public String getCharset() {
        Charset charset = this.contentType.getCharset();
        if (charset != null) {
            return charset.name();
        }
        return null;
    }
}
