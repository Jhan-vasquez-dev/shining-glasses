package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.io.EofSensor;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.CharArrayBuffer;

/* JADX INFO: loaded from: classes2.dex */
@Deprecated
public class LoggingSessionInputBuffer implements SessionInputBuffer, EofSensor {
    private final String charset;
    private final EofSensor eofSensor;
    private final SessionInputBuffer in;
    private final Wire wire;

    public LoggingSessionInputBuffer(SessionInputBuffer sessionInputBuffer, Wire wire, String str) {
        this.in = sessionInputBuffer;
        this.eofSensor = sessionInputBuffer instanceof EofSensor ? (EofSensor) sessionInputBuffer : null;
        this.wire = wire;
        this.charset = str == null ? Consts.ASCII.name() : str;
    }

    public LoggingSessionInputBuffer(SessionInputBuffer sessionInputBuffer, Wire wire) {
        this(sessionInputBuffer, wire, null);
    }

    @Override // org.apache.http.io.SessionInputBuffer
    public boolean isDataAvailable(int i) throws IOException {
        return this.in.isDataAvailable(i);
    }

    @Override // org.apache.http.io.SessionInputBuffer
    public int read(byte[] bArr, int i, int i2) throws IOException {
        int i3 = this.in.read(bArr, i, i2);
        if (this.wire.enabled() && i3 > 0) {
            this.wire.input(bArr, i, i3);
        }
        return i3;
    }

    @Override // org.apache.http.io.SessionInputBuffer
    public int read() throws IOException {
        int i = this.in.read();
        if (this.wire.enabled() && i != -1) {
            this.wire.input(i);
        }
        return i;
    }

    @Override // org.apache.http.io.SessionInputBuffer
    public int read(byte[] bArr) throws IOException {
        int i = this.in.read(bArr);
        if (this.wire.enabled() && i > 0) {
            this.wire.input(bArr, 0, i);
        }
        return i;
    }

    @Override // org.apache.http.io.SessionInputBuffer
    public String readLine() throws IOException {
        String line = this.in.readLine();
        if (this.wire.enabled() && line != null) {
            this.wire.input((line + "\r\n").getBytes(this.charset));
        }
        return line;
    }

    @Override // org.apache.http.io.SessionInputBuffer
    public int readLine(CharArrayBuffer charArrayBuffer) throws IOException {
        int line = this.in.readLine(charArrayBuffer);
        if (this.wire.enabled() && line >= 0) {
            this.wire.input(new String(charArrayBuffer.buffer(), charArrayBuffer.length() - line, line).concat("\r\n").getBytes(this.charset));
        }
        return line;
    }

    @Override // org.apache.http.io.SessionInputBuffer
    public HttpTransportMetrics getMetrics() {
        return this.in.getMetrics();
    }

    @Override // org.apache.http.io.EofSensor
    public boolean isEof() {
        EofSensor eofSensor = this.eofSensor;
        if (eofSensor != null) {
            return eofSensor.isEof();
        }
        return false;
    }
}
