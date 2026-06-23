package org.apache.http.impl.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AbstractMessageParser<T extends HttpMessage> implements HttpMessageParser<T> {
    private static final int HEADERS = 1;
    private static final int HEAD_LINE = 0;
    private final List<CharArrayBuffer> headerLines;
    protected final LineParser lineParser;
    private T message;
    private final MessageConstraints messageConstraints;
    private final SessionInputBuffer sessionBuffer;
    private int state;

    protected abstract T parseHead(SessionInputBuffer sessionInputBuffer) throws HttpException, ParseException, IOException;

    @Deprecated
    public AbstractMessageParser(SessionInputBuffer sessionInputBuffer, LineParser lineParser, HttpParams httpParams) {
        Args.notNull(sessionInputBuffer, "Session input buffer");
        Args.notNull(httpParams, "HTTP parameters");
        this.sessionBuffer = sessionInputBuffer;
        this.messageConstraints = HttpParamConfig.getMessageConstraints(httpParams);
        this.lineParser = lineParser == null ? BasicLineParser.INSTANCE : lineParser;
        this.headerLines = new ArrayList();
        this.state = 0;
    }

    public AbstractMessageParser(SessionInputBuffer sessionInputBuffer, LineParser lineParser, MessageConstraints messageConstraints) {
        this.sessionBuffer = (SessionInputBuffer) Args.notNull(sessionInputBuffer, "Session input buffer");
        this.lineParser = lineParser == null ? BasicLineParser.INSTANCE : lineParser;
        this.messageConstraints = messageConstraints == null ? MessageConstraints.DEFAULT : messageConstraints;
        this.headerLines = new ArrayList();
        this.state = 0;
    }

    public static Header[] parseHeaders(SessionInputBuffer sessionInputBuffer, int i, int i2, LineParser lineParser) throws HttpException, IOException {
        ArrayList arrayList = new ArrayList();
        if (lineParser == null) {
            lineParser = BasicLineParser.INSTANCE;
        }
        return parseHeaders(sessionInputBuffer, i, i2, lineParser, arrayList);
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x008d, code lost:
    
        r8 = new org.apache.http.Header[r12.size()];
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0097, code lost:
    
        if (r5 >= r12.size()) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x009f, code lost:
    
        r8[r5] = r11.parseHeader(r12.get(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00a5, code lost:
    
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00a8, code lost:
    
        r8 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00b2, code lost:
    
        throw new org.apache.http.ProtocolException(r8.getMessage());
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b3, code lost:
    
        return r8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static org.apache.http.Header[] parseHeaders(org.apache.http.io.SessionInputBuffer r8, int r9, int r10, org.apache.http.message.LineParser r11, java.util.List<org.apache.http.util.CharArrayBuffer> r12) throws org.apache.http.HttpException, java.io.IOException {
        /*
            java.lang.String r0 = "Session input buffer"
            org.apache.http.util.Args.notNull(r8, r0)
            java.lang.String r0 = "Line parser"
            org.apache.http.util.Args.notNull(r11, r0)
            java.lang.String r0 = "Header line list"
            org.apache.http.util.Args.notNull(r12, r0)
            r0 = 0
            r1 = r0
            r2 = r1
        L12:
            if (r1 != 0) goto L1c
            org.apache.http.util.CharArrayBuffer r1 = new org.apache.http.util.CharArrayBuffer
            r3 = 64
            r1.<init>(r3)
            goto L1f
        L1c:
            r1.clear()
        L1f:
            int r3 = r8.readLine(r1)
            r4 = -1
            r5 = 0
            if (r3 == r4) goto L8d
            int r3 = r1.length()
            r4 = 1
            if (r3 >= r4) goto L2f
            goto L8d
        L2f:
            char r3 = r1.charAt(r5)
            r6 = 9
            r7 = 32
            if (r3 == r7) goto L3f
            char r3 = r1.charAt(r5)
            if (r3 != r6) goto L77
        L3f:
            if (r2 == 0) goto L77
        L41:
            int r3 = r1.length()
            if (r5 >= r3) goto L53
            char r3 = r1.charAt(r5)
            if (r3 == r7) goto L50
            if (r3 == r6) goto L50
            goto L53
        L50:
            int r5 = r5 + 1
            goto L41
        L53:
            if (r10 <= 0) goto L6b
            int r3 = r2.length()
            int r3 = r3 + r4
            int r4 = r1.length()
            int r3 = r3 + r4
            int r3 = r3 - r5
            if (r3 > r10) goto L63
            goto L6b
        L63:
            org.apache.http.MessageConstraintException r8 = new org.apache.http.MessageConstraintException
            java.lang.String r9 = "Maximum line length limit exceeded"
            r8.<init>(r9)
            throw r8
        L6b:
            r2.append(r7)
            int r3 = r1.length()
            int r3 = r3 - r5
            r2.append(r1, r5, r3)
            goto L7c
        L77:
            r12.add(r1)
            r2 = r1
            r1 = r0
        L7c:
            if (r9 <= 0) goto L12
            int r3 = r12.size()
            if (r3 >= r9) goto L85
            goto L12
        L85:
            org.apache.http.MessageConstraintException r8 = new org.apache.http.MessageConstraintException
            java.lang.String r9 = "Maximum header count exceeded"
            r8.<init>(r9)
            throw r8
        L8d:
            int r8 = r12.size()
            org.apache.http.Header[] r8 = new org.apache.http.Header[r8]
        L93:
            int r9 = r12.size()
            if (r5 >= r9) goto Lb3
            java.lang.Object r9 = r12.get(r5)
            org.apache.http.util.CharArrayBuffer r9 = (org.apache.http.util.CharArrayBuffer) r9
            org.apache.http.Header r9 = r11.parseHeader(r9)     // Catch: org.apache.http.ParseException -> La8
            r8[r5] = r9     // Catch: org.apache.http.ParseException -> La8
            int r5 = r5 + 1
            goto L93
        La8:
            r8 = move-exception
            org.apache.http.ProtocolException r9 = new org.apache.http.ProtocolException
            java.lang.String r8 = r8.getMessage()
            r9.<init>(r8)
            throw r9
        Lb3:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.io.AbstractMessageParser.parseHeaders(org.apache.http.io.SessionInputBuffer, int, int, org.apache.http.message.LineParser, java.util.List):org.apache.http.Header[]");
    }

    @Override // org.apache.http.io.HttpMessageParser
    public T parse() throws HttpException, IOException {
        int i = this.state;
        if (i == 0) {
            try {
                this.message = (T) parseHead(this.sessionBuffer);
                this.state = 1;
            } catch (ParseException e) {
                throw new ProtocolException(e.getMessage(), e);
            }
        } else if (i != 1) {
            throw new IllegalStateException("Inconsistent parser state");
        }
        this.message.setHeaders(parseHeaders(this.sessionBuffer, this.messageConstraints.getMaxHeaderCount(), this.messageConstraints.getMaxLineLength(), this.lineParser, this.headerLines));
        T t = this.message;
        this.message = null;
        this.headerLines.clear();
        this.state = 0;
        return t;
    }
}
