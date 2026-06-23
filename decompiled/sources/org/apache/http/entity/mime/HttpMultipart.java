package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class HttpMultipart extends AbstractMultipartForm {
    private final HttpMultipartMode mode;
    private final List<FormBodyPart> parts;
    private final String subType;

    @Override // org.apache.http.entity.mime.AbstractMultipartForm
    public /* bridge */ /* synthetic */ long getTotalLength() {
        return super.getTotalLength();
    }

    @Override // org.apache.http.entity.mime.AbstractMultipartForm
    public /* bridge */ /* synthetic */ void writeTo(OutputStream outputStream) throws IOException {
        super.writeTo(outputStream);
    }

    public HttpMultipart(String str, Charset charset, String str2, HttpMultipartMode httpMultipartMode) {
        super(charset, str2);
        this.subType = str;
        this.mode = httpMultipartMode;
        this.parts = new ArrayList();
    }

    public HttpMultipart(String str, Charset charset, String str2) {
        this(str, charset, str2, HttpMultipartMode.STRICT);
    }

    public HttpMultipart(String str, String str2) {
        this(str, null, str2);
    }

    public HttpMultipartMode getMode() {
        return this.mode;
    }

    /* JADX INFO: renamed from: org.apache.http.entity.mime.HttpMultipart$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$http$entity$mime$HttpMultipartMode;

        static {
            int[] iArr = new int[HttpMultipartMode.values().length];
            $SwitchMap$org$apache$http$entity$mime$HttpMultipartMode = iArr;
            try {
                iArr[HttpMultipartMode.BROWSER_COMPATIBLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    @Override // org.apache.http.entity.mime.AbstractMultipartForm
    protected void formatMultipartHeader(FormBodyPart formBodyPart, OutputStream outputStream) throws IOException {
        Header header = formBodyPart.getHeader();
        if (AnonymousClass1.$SwitchMap$org$apache$http$entity$mime$HttpMultipartMode[this.mode.ordinal()] == 1) {
            writeField(header.getField("Content-Disposition"), this.charset, outputStream);
            if (formBodyPart.getBody().getFilename() != null) {
                writeField(header.getField("Content-Type"), this.charset, outputStream);
                return;
            }
            return;
        }
        Iterator<MinimalField> it = header.iterator();
        while (it.hasNext()) {
            writeField(it.next(), outputStream);
        }
    }

    @Override // org.apache.http.entity.mime.AbstractMultipartForm
    public List<FormBodyPart> getBodyParts() {
        return this.parts;
    }

    public void addBodyPart(FormBodyPart formBodyPart) {
        if (formBodyPart == null) {
            return;
        }
        this.parts.add(formBodyPart);
    }

    public String getSubType() {
        return this.subType;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getBoundary() {
        return this.boundary;
    }
}
