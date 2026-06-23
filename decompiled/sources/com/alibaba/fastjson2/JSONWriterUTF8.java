package com.alibaba.fastjson2;

import androidx.core.view.InputDeviceCompat;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.internal.asm.Opcodes;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.NumberUtils;
import com.alibaba.fastjson2.util.StringUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
final class JSONWriterUTF8 extends JSONWriter {
    static final short QUOTE2_COLON;
    static final short QUOTE_COLON;
    static final long REF;
    private final long byteVectorQuote;
    byte[] bytes;
    final JSONFactory.CacheItem cacheItem;

    private static long expand(long j) {
        long j2 = (j & (-281470681743361L)) | ((j << 16) & 281470681743360L);
        long j3 = (j2 & (-71776119077928961L)) | ((j2 << 8) & 71776119077928960L);
        return ((j3 & (-1080880403494997761L)) | ((j3 << 4) & 1080880403494997760L)) & 1085102592571150095L;
    }

    static {
        byte[] bArr = {JSONB.Constants.BC_STR_UTF16, 34, 36, 114, 101, 102, 34, 58};
        REF = JDKUtils.UNSAFE.getLong(bArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET);
        QUOTE2_COLON = JDKUtils.UNSAFE.getShort(bArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + 6);
        bArr[6] = 39;
        QUOTE_COLON = JDKUtils.UNSAFE.getShort(bArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + 6);
    }

    JSONWriterUTF8(JSONWriter.Context context) {
        super(context, null, false, StandardCharsets.UTF_8);
        JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & (JSONFactory.CACHE_ITEMS.length - 1)];
        this.cacheItem = cacheItem;
        byte[] andSet = JSONFactory.BYTES_UPDATER.getAndSet(cacheItem, null);
        this.bytes = andSet == null ? new byte[8192] : andSet;
        this.byteVectorQuote = this.useSingleQuote ? -2821266740684990248L : -2459565876494606883L;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeNull() {
        int i = this.off;
        byte[] bArrGrow = this.bytes;
        int i2 = i + 4;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        IOUtils.putNULL(bArrGrow, i);
        this.off = i2;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeReference(String str) {
        this.lastReference = str;
        int i = this.off;
        byte[] bArrGrow = this.bytes;
        int i2 = i + 8;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        JDKUtils.UNSAFE.putLong(bArrGrow, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i), REF);
        this.off = i2;
        writeString(str);
        writeRaw(JSONB.Constants.BC_STR_UTF16BE);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeBase64(byte[] bArr) {
        int i = this.off;
        int length = ((((bArr.length - 1) / 3) + 1) << 2) + i + 2;
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        int i2 = i + 1;
        bArrGrow[i] = (byte) this.quote;
        int length2 = (bArr.length / 3) * 3;
        int i3 = 0;
        while (i3 < length2) {
            int i4 = i3 + 2;
            int i5 = ((bArr[i3 + 1] & UByte.MAX_VALUE) << 8) | ((bArr[i3] & UByte.MAX_VALUE) << 16);
            i3 += 3;
            int i6 = i5 | (bArr[i4] & UByte.MAX_VALUE);
            bArrGrow[i2] = (byte) JSONFactory.CA[(i6 >>> 18) & 63];
            bArrGrow[i2 + 1] = (byte) JSONFactory.CA[(i6 >>> 12) & 63];
            bArrGrow[i2 + 2] = (byte) JSONFactory.CA[(i6 >>> 6) & 63];
            bArrGrow[i2 + 3] = (byte) JSONFactory.CA[i6 & 63];
            i2 += 4;
        }
        int length3 = bArr.length - length2;
        if (length3 > 0) {
            int i7 = ((bArr[length2] & UByte.MAX_VALUE) << 10) | (length3 == 2 ? (bArr[bArr.length - 1] & UByte.MAX_VALUE) << 2 : 0);
            bArrGrow[i2] = (byte) JSONFactory.CA[i7 >> 12];
            bArrGrow[i2 + 1] = (byte) JSONFactory.CA[(i7 >>> 6) & 63];
            bArrGrow[i2 + 2] = length3 == 2 ? (byte) JSONFactory.CA[i7 & 63] : (byte) 61;
            bArrGrow[i2 + 3] = 61;
            i2 += 4;
        }
        bArrGrow[i2] = (byte) this.quote;
        this.off = i2 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeHex(byte[] bArr) {
        if (bArr == null) {
            writeNull();
            return;
        }
        int length = (bArr.length * 2) + 3;
        int i = this.off;
        int i2 = length + i + 2;
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        IOUtils.putShortLE(bArrGrow, i, (short) 10104);
        int i3 = i + 2;
        for (byte b : bArr) {
            IOUtils.putShortLE(bArrGrow, i3, IOUtils.hex2U(b));
            i3 += 2;
        }
        bArrGrow[i3] = 39;
        this.off = i3 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter, java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        byte[] bArr = this.bytes;
        if (bArr.length > 8388608) {
            return;
        }
        JSONFactory.BYTES_UPDATER.lazySet(this.cacheItem, bArr);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final int size() {
        return this.off;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final byte[] getBytes() {
        return Arrays.copyOf(this.bytes, this.off);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final byte[] getBytes(Charset charset) {
        if (charset == StandardCharsets.UTF_8) {
            return Arrays.copyOf(this.bytes, this.off);
        }
        return toString().getBytes(charset);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final int flushTo(OutputStream outputStream) throws IOException {
        int i = this.off;
        if (i > 0) {
            outputStream.write(this.bytes, 0, i);
            this.off = 0;
        }
        return i;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    protected final void write0(char c) {
        int i = this.off;
        if (i == this.bytes.length) {
            grow0(i + 1);
        }
        this.bytes[i] = (byte) c;
        this.off = i + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeColon() {
        int i = this.off;
        grow1(i)[i] = 58;
        this.off = i + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void startObject() {
        int i = this.level + 1;
        this.level = i;
        if (i > this.context.maxLevel) {
            overflowLevel();
        }
        this.startObject = true;
        int i2 = this.off;
        int i3 = i2 + 3 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i3 > bArrGrow.length) {
            bArrGrow = grow(i3);
        }
        int iIndent = i2 + 1;
        bArrGrow[i2] = JSONB.Constants.BC_STR_UTF16;
        if (this.pretty != 0) {
            iIndent = indent(bArrGrow, iIndent);
        }
        this.off = iIndent;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void endObject() {
        this.level--;
        int iIndent = this.off;
        int i = iIndent + 1 + (this.pretty == 0 ? 0 : (this.pretty * this.level) + 1);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        if (this.pretty != 0) {
            iIndent = indent(bArrGrow, iIndent);
        }
        bArrGrow[iIndent] = JSONB.Constants.BC_STR_UTF16BE;
        this.off = iIndent + 1;
        this.startObject = false;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeComma() {
        this.startObject = false;
        int i = this.off;
        int i2 = i + 2 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        int iIndent = i + 1;
        bArrGrow[i] = 44;
        if (this.pretty != 0) {
            iIndent = indent(bArrGrow, iIndent);
        }
        this.off = iIndent;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void startArray() {
        int i = this.level + 1;
        this.level = i;
        if (i > this.context.maxLevel) {
            overflowLevel();
        }
        int i2 = this.off;
        int i3 = i2 + 3 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i3 > bArrGrow.length) {
            bArrGrow = grow(i3);
        }
        int iIndent = i2 + 1;
        bArrGrow[i2] = 91;
        if (this.pretty != 0) {
            iIndent = indent(bArrGrow, iIndent);
        }
        this.off = iIndent;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void endArray() {
        this.level--;
        int iIndent = this.off;
        int i = iIndent + 1 + (this.pretty == 0 ? 0 : (this.pretty * this.level) + 1);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        if (this.pretty != 0) {
            iIndent = indent(bArrGrow, iIndent);
        }
        bArrGrow[iIndent] = 93;
        this.off = iIndent + 1;
        this.startObject = false;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(String[] strArr) {
        if (this.pretty != 0 || strArr == null) {
            super.writeString(strArr);
            return;
        }
        int i = this.off;
        grow1(i)[i] = 91;
        this.off = i + 1;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (i2 != 0) {
                int i3 = this.off;
                grow1(i3)[i3] = 44;
                this.off = i3 + 1;
            }
            writeString(strArr[i2]);
        }
        int i4 = this.off;
        grow1(i4)[i4] = 93;
        this.off = i4 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(List<String> list) {
        if (this.pretty != 0) {
            super.writeString(list);
            return;
        }
        int i = this.off;
        grow1(i)[i] = 91;
        this.off = i + 1;
        int size = list.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (i2 != 0) {
                int i3 = this.off;
                grow1(i3)[i3] = 44;
                this.off = i3 + 1;
            }
            String str = list.get(i2);
            if (str == null) {
                writeStringNull();
            } else if (JDKUtils.STRING_VALUE != null) {
                byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
                if (JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
                    writeStringLatin1(bArrApply);
                } else {
                    writeStringUTF16(bArrApply);
                }
            } else {
                writeStringJDK8(str);
            }
        }
        int i4 = this.off;
        grow1(i4)[i4] = 93;
        this.off = i4 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(boolean z) {
        byte b = (byte) this.quote;
        byte[] bArr = this.bytes;
        int i = this.off;
        this.off = i + 1;
        bArr[i] = b;
        writeBool(z);
        byte[] bArr2 = this.bytes;
        int i2 = this.off;
        this.off = i2 + 1;
        bArr2[i2] = b;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(byte b) {
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) == 0;
        if (z) {
            writeQuote();
        }
        writeInt8(b);
        if (z) {
            writeQuote();
        }
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(short s) {
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) == 0;
        if (z) {
            writeQuote();
        }
        writeInt16(s);
        if (z) {
            writeQuote();
        }
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(int i) {
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) == 0;
        if (z) {
            writeQuote();
        }
        writeInt32(i);
        if (z) {
            writeQuote();
        }
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(long j) {
        boolean z = (this.context.features & (JSONWriter.Feature.WriteNonStringValueAsString.mask | JSONWriter.Feature.WriteLongAsString.mask)) == 0;
        if (z) {
            writeQuote();
        }
        writeInt64(j);
        if (z) {
            writeQuote();
        }
    }

    private void writeQuote() {
        writeRaw((byte) this.quote);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public void writeString(String str) {
        if (str == null) {
            writeStringNull();
            return;
        }
        if (JDKUtils.STRING_VALUE != null) {
            byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
            if (JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
                writeStringLatin1(bArrApply);
                return;
            } else {
                writeStringUTF16(bArrApply);
                return;
            }
        }
        writeStringJDK8(str);
    }

    private void writeStringJDK8(String str) {
        JSONWriterUTF8 jSONWriterUTF8;
        char c;
        char[] charArray = JDKUtils.getCharArray(str);
        boolean z = (this.context.features & JSONWriter.Feature.BrowserSecure.mask) != 0;
        boolean z2 = (this.context.features & JSONWriter.Feature.EscapeNoneAscii.mask) != 0;
        int i = this.off;
        int length = (charArray.length * 3) + i + 2;
        if (z2 || z) {
            length += charArray.length * 3;
        }
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        bArrGrow[i] = (byte) this.quote;
        int i2 = i + 1;
        int i3 = 0;
        while (i3 < charArray.length && (c = charArray[i3]) != this.quote && c != '\\' && c >= ' ' && c <= 127 && (!z || (c != '<' && c != '>' && c != '(' && c != ')'))) {
            bArrGrow[i2] = (byte) c;
            i3++;
            i2++;
        }
        if (i3 == charArray.length) {
            bArrGrow[i2] = (byte) this.quote;
            this.off = i2 + 1;
            return;
        }
        this.off = i2;
        if (i3 < charArray.length) {
            jSONWriterUTF8 = this;
            jSONWriterUTF8.writeStringEscapedRest(charArray, charArray.length, z, z2, i3);
        } else {
            jSONWriterUTF8 = this;
        }
        byte[] bArr = jSONWriterUTF8.bytes;
        int i4 = jSONWriterUTF8.off;
        jSONWriterUTF8.off = i4 + 1;
        bArr[i4] = (byte) jSONWriterUTF8.quote;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeStringLatin1(byte[] bArr) {
        if ((this.context.features & 34359738368L) != 0) {
            writeStringLatin1BrowserSecure(bArr);
            return;
        }
        byte b = (byte) this.quote;
        if (StringUtils.escaped(bArr, b, this.byteVectorQuote)) {
            writeStringEscaped(bArr);
            return;
        }
        int i = this.off;
        int length = bArr.length + i + 2;
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        this.off = StringUtils.writeLatin1(bArrGrow, i, bArr, b);
    }

    protected final void writeStringLatin1BrowserSecure(byte[] bArr) {
        byte b;
        byte b2 = (byte) this.quote;
        int i = 0;
        while (i < bArr.length && (b = bArr[i]) != b2 && b != 92 && b >= 32 && b != 60 && b != 62 && b != 40 && b != 41) {
            i++;
        }
        int i2 = this.off;
        if (i == bArr.length) {
            int length = bArr.length + i2 + 2;
            byte[] bArrGrow = this.bytes;
            if (length > bArrGrow.length) {
                bArrGrow = grow(length);
            }
            bArrGrow[i2] = b2;
            System.arraycopy(bArr, 0, bArrGrow, i2 + 1, bArr.length);
            int length2 = i2 + bArr.length + 1;
            bArrGrow[length2] = b2;
            this.off = length2 + 1;
            return;
        }
        writeStringEscaped(bArr);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeStringUTF16(byte[] bArr) {
        if (bArr == null) {
            writeStringNull();
            return;
        }
        int i = this.off;
        byte[] bArrGrow = this.bytes;
        int length = (bArr.length * 6) + i + 2;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        this.off = StringUtils.writeUTF16(bArrGrow, i, bArr, (byte) this.quote, this.context.features);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(char[] cArr) {
        JSONWriterUTF8 jSONWriterUTF8;
        char c;
        if (cArr == null) {
            writeStringNull();
            return;
        }
        boolean z = (this.context.features & JSONWriter.Feature.BrowserSecure.mask) != 0;
        boolean z2 = (this.context.features & JSONWriter.Feature.EscapeNoneAscii.mask) != 0;
        int i = this.off;
        int length = (cArr.length * 3) + i + 2;
        if (z2 || z) {
            length += cArr.length * 3;
        }
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        int i2 = i + 1;
        bArrGrow[i] = (byte) this.quote;
        int i3 = 0;
        while (i3 < cArr.length && (c = cArr[i3]) != this.quote && c != '\\' && c >= ' ' && c <= 127 && (!z || (c != '<' && c != '>' && c != '(' && c != ')'))) {
            bArrGrow[i2] = (byte) c;
            i3++;
            i2++;
        }
        this.off = i2;
        if (i3 < cArr.length) {
            jSONWriterUTF8 = this;
            jSONWriterUTF8.writeStringEscapedRest(cArr, cArr.length, z, z2, i3);
        } else {
            jSONWriterUTF8 = this;
        }
        byte[] bArr = jSONWriterUTF8.bytes;
        int i4 = jSONWriterUTF8.off;
        jSONWriterUTF8.off = i4 + 1;
        bArr[i4] = (byte) jSONWriterUTF8.quote;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(char[] cArr, int i, int i2) {
        if (cArr == null) {
            if (isEnabled(JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask)) {
                writeString("");
                return;
            } else {
                writeNull();
                return;
            }
        }
        int i3 = i + i2;
        boolean z = (this.context.features & JSONWriter.Feature.BrowserSecure.mask) != 0;
        boolean z2 = (this.context.features & JSONWriter.Feature.EscapeNoneAscii.mask) != 0;
        int i4 = this.off;
        int i5 = i2 * 3;
        int i6 = i4 + i5 + 2;
        if (z2 || z) {
            i6 += i5;
        }
        byte[] bArrGrow = this.bytes;
        if (i6 > bArrGrow.length) {
            bArrGrow = grow(i6);
        }
        int i7 = i4 + 1;
        bArrGrow[i4] = (byte) this.quote;
        int i8 = i;
        while (i8 < i3) {
            char c = cArr[i8];
            if (c == this.quote || c == '\\' || c < ' ' || c > 127 || (z && (c == '<' || c == '>' || c == '(' || c == ')'))) {
                break;
            }
            bArrGrow[i7] = (byte) c;
            i8++;
            i7++;
        }
        this.off = i7;
        if (i8 < i3) {
            writeStringEscapedRest(cArr, i3, z, z2, i8);
        }
        byte[] bArr = this.bytes;
        int i9 = this.off;
        this.off = i9 + 1;
        bArr[i9] = (byte) this.quote;
    }

    protected final void writeStringEscaped(byte[] bArr) {
        int length = this.off + (bArr.length * 6) + 2;
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        this.off = StringUtils.writeLatin1Escaped(bArrGrow, this.off, bArr, (byte) this.quote, this.context.features);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:19:0x0030. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:26:0x004d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void writeStringEscapedRest(char[] r9, int r10, boolean r11, boolean r12, int r13) {
        /*
            Method dump skipped, instruction units count: 332
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF8.writeStringEscapedRest(char[], int, boolean, boolean, int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x0092  */
    @Override // com.alibaba.fastjson2.JSONWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void writeString(char[] r17, int r18, int r19, boolean r20) {
        /*
            Method dump skipped, instruction units count: 398
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF8.writeString(char[], int, int, boolean):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0039  */
    @Override // com.alibaba.fastjson2.JSONWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void writeChar(char r6) {
        /*
            Method dump skipped, instruction units count: 228
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF8.writeChar(char):void");
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeUUID(UUID uuid) {
        if (uuid == null) {
            writeNull();
            return;
        }
        int i = this.off;
        int i2 = i + 38;
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        byte[] bArr = bArrGrow;
        byte b = (byte) this.quote;
        long j = JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) i);
        JDKUtils.UNSAFE.putByte(bArr, j, b);
        JDKUtils.UNSAFE.putByte(bArr, 9 + j, (byte) 45);
        JDKUtils.UNSAFE.putByte(bArr, 14 + j, (byte) 45);
        JDKUtils.UNSAFE.putByte(bArr, 19 + j, (byte) 45);
        JDKUtils.UNSAFE.putByte(bArr, 24 + j, (byte) 45);
        JDKUtils.UNSAFE.putByte(bArr, 37 + j, b);
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        long jHex8 = hex8(mostSignificantBits >>> 32);
        long jHex82 = hex8(mostSignificantBits);
        JDKUtils.UNSAFE.putLong(bArr, 1 + j, jHex8);
        JDKUtils.UNSAFE.putInt(bArr, 10 + j, (int) jHex82);
        JDKUtils.UNSAFE.putInt(bArr, 15 + j, (int) (jHex82 >>> 32));
        long jHex83 = hex8(leastSignificantBits >>> 32);
        long jHex84 = hex8(leastSignificantBits);
        JDKUtils.UNSAFE.putInt(bArr, 20 + j, (int) jHex83);
        JDKUtils.UNSAFE.putInt(bArr, 25 + j, (int) (jHex83 >>> 32));
        JDKUtils.UNSAFE.putLong(bArr, j + 29, jHex84);
        this.off += 38;
    }

    private static long hex8(long j) {
        long jExpand = expand(j);
        long j2 = (434041037028460038L + jExpand) & 1157442765409226768L;
        long j3 = (((j2 << 1) + (j2 >> 1)) - (j2 >> 4)) + 3472328296227680304L + jExpand;
        return !JDKUtils.BIG_ENDIAN ? Long.reverseBytes(j3) : j3;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeRaw(String str) {
        char[] charArray = JDKUtils.getCharArray(str);
        int i = this.off;
        int length = (charArray.length * 3) + i;
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        for (char c : charArray) {
            if (c >= 1 && c <= 127) {
                bArrGrow[i] = (byte) c;
                i++;
            } else if (c > 2047) {
                bArrGrow[i] = (byte) (((c >> '\f') & 15) | 224);
                bArrGrow[i + 1] = (byte) (((c >> 6) & 63) | 128);
                bArrGrow[i + 2] = (byte) ((c & '?') | 128);
                i += 3;
            } else {
                bArrGrow[i] = (byte) (((c >> 6) & 31) | Opcodes.CHECKCAST);
                bArrGrow[i + 1] = (byte) ((c & '?') | 128);
                i += 2;
            }
        }
        this.off = i;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeRaw(byte[] bArr) {
        int i = this.off;
        int length = bArr.length + i;
        if (length > this.bytes.length) {
            grow(length);
        }
        System.arraycopy(bArr, 0, this.bytes, i, bArr.length);
        this.off = i + bArr.length;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeNameRaw(byte[] bArr) {
        int iIndent = this.off;
        int length = bArr.length + iIndent + 2 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i = iIndent + 1;
            bArrGrow[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArrGrow, i) : i;
        }
        System.arraycopy(bArr, 0, bArrGrow, iIndent, bArr.length);
        this.off = iIndent + bArr.length;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName2Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 10 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        JDKUtils.UNSAFE.putLong(bArr, ((long) iIndent) + JDKUtils.ARRAY_BYTE_BASE_OFFSET, j);
        this.off = iIndent + 5;
    }

    private int indent(byte[] bArr, int i) {
        bArr[i] = 10;
        int i2 = i + 1;
        int i3 = (this.pretty * this.level) + i2;
        Arrays.fill(bArr, i2, i3, this.pretty == 1 ? (byte) 9 : (byte) 32);
        return i3;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName3Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 10 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        JDKUtils.UNSAFE.putLong(bArr, ((long) iIndent) + JDKUtils.ARRAY_BYTE_BASE_OFFSET, j);
        this.off = iIndent + 6;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName4Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 10 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        JDKUtils.UNSAFE.putLong(bArr, ((long) iIndent) + JDKUtils.ARRAY_BYTE_BASE_OFFSET, j);
        this.off = iIndent + 7;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName5Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 10 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        JDKUtils.UNSAFE.putLong(bArr, ((long) iIndent) + JDKUtils.ARRAY_BYTE_BASE_OFFSET, j);
        this.off = iIndent + 8;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName6Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 11 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        JDKUtils.UNSAFE.putLong(bArr, ((long) iIndent) + JDKUtils.ARRAY_BYTE_BASE_OFFSET, j);
        bArr[iIndent + 8] = 58;
        this.off = iIndent + 9;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName7Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 12 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        JDKUtils.UNSAFE.putLong(bArr, ((long) iIndent) + JDKUtils.ARRAY_BYTE_BASE_OFFSET, j);
        bArr[iIndent + 8] = (byte) this.quote;
        bArr[iIndent + 9] = 58;
        this.off = iIndent + 10;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName8Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 13 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        bArr[iIndent] = (byte) this.quote;
        long j2 = iIndent;
        JDKUtils.UNSAFE.putLong(bArr, 1 + JDKUtils.ARRAY_BYTE_BASE_OFFSET + j2, j);
        JDKUtils.UNSAFE.putShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j2 + 9, this.useSingleQuote ? QUOTE_COLON : QUOTE2_COLON);
        this.off = iIndent + 11;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName9Raw(long j, int i) {
        int iIndent = this.off;
        int i2 = iIndent + 14 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i3 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i3) : i3;
        }
        long j2 = iIndent;
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j2, j);
        JDKUtils.UNSAFE.putInt(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j2 + 8, i);
        this.off = iIndent + 12;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName10Raw(long j, long j2) {
        int iIndent = this.off;
        int i = iIndent + 18 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        long j3 = iIndent;
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3, j);
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3 + 8, j2);
        this.off = iIndent + 13;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName11Raw(long j, long j2) {
        int iIndent = this.off;
        int i = iIndent + 18 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        long j3 = iIndent;
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3, j);
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3 + 8, j2);
        this.off = iIndent + 14;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName12Raw(long j, long j2) {
        int iIndent = this.off;
        int i = iIndent + 18 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        long j3 = iIndent;
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3, j);
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3 + 8, j2);
        this.off = iIndent + 15;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName13Raw(long j, long j2) {
        int iIndent = this.off;
        int i = iIndent + 18 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        long j3 = iIndent;
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3, j);
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3 + 8, j2);
        this.off = iIndent + 16;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName14Raw(long j, long j2) {
        int iIndent = this.off;
        int i = iIndent + 19 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        long j3 = iIndent;
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3, j);
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3 + 8, j2);
        bArr[iIndent + 16] = 58;
        this.off = iIndent + 17;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName15Raw(long j, long j2) {
        int iIndent = this.off;
        int i = iIndent + 20 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        long j3 = iIndent;
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3, j);
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3 + 8, j2);
        JDKUtils.UNSAFE.putShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3 + 16, this.useSingleQuote ? QUOTE_COLON : QUOTE2_COLON);
        this.off = iIndent + 18;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName16Raw(long j, long j2) {
        int iIndent = this.off;
        int i = iIndent + 21 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        byte[] bArr = bArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            bArr[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArr, i2) : i2;
        }
        bArr[iIndent] = (byte) this.quote;
        long j3 = iIndent + 1;
        JDKUtils.UNSAFE.putLong(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3, j);
        JDKUtils.UNSAFE.putLong(bArr, 8 + JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3, j2);
        JDKUtils.UNSAFE.putShort(bArr, JDKUtils.ARRAY_BYTE_BASE_OFFSET + j3 + 16, this.useSingleQuote ? QUOTE_COLON : QUOTE2_COLON);
        this.off = iIndent + 19;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeRaw(byte b) {
        int i = this.off;
        grow1(i)[i] = b;
        this.off = i + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeRaw(char c) {
        if (c > 128) {
            throw new JSONException("not support " + c);
        }
        if (this.off == this.bytes.length) {
            grow0(this.off + 1);
        }
        byte[] bArr = this.bytes;
        int i = this.off;
        this.off = i + 1;
        bArr[i] = (byte) c;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeRaw(char c, char c2) {
        if (c > 128 || c2 > 128) {
            throw new JSONException("not support " + c + ", " + c2);
        }
        int i = this.off;
        byte[] bArrGrow = this.bytes;
        int i2 = i + 2;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        bArrGrow[i] = (byte) c;
        bArrGrow[i + 1] = (byte) c2;
        this.off = i2;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeNameRaw(byte[] bArr, int i, int i2) {
        int iIndent = this.off;
        int i3 = iIndent + i2 + 2 + (this.pretty * this.level);
        byte[] bArrGrow = this.bytes;
        if (i3 > bArrGrow.length) {
            bArrGrow = grow(i3);
        }
        if (!this.startObject) {
            int i4 = iIndent + 1;
            bArrGrow[iIndent] = 44;
            iIndent = this.pretty != 0 ? indent(bArrGrow, i4) : i4;
        }
        this.startObject = false;
        System.arraycopy(bArr, i, bArrGrow, iIndent, i2);
        this.off = iIndent + i2;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final Object ensureCapacity(int i) {
        byte[] bArr = this.bytes;
        if (i < bArr.length) {
            return bArr;
        }
        byte[] bArrCopyOf = Arrays.copyOf(bArr, newCapacity(i, bArr.length));
        this.bytes = bArrCopyOf;
        return bArrCopyOf;
    }

    private byte[] grow(int i) {
        grow0(i);
        return this.bytes;
    }

    private byte[] grow1(int i) {
        byte[] bArr = this.bytes;
        return i == bArr.length ? grow(i + 1) : bArr;
    }

    private void grow0(int i) {
        byte[] bArr = this.bytes;
        this.bytes = Arrays.copyOf(bArr, newCapacity(i, bArr.length));
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt32(int[] iArr) {
        if (iArr == null) {
            writeNull();
            return;
        }
        boolean z = (this.context.features & 256) != 0;
        int i = this.off;
        int length = (iArr.length * 13) + i + 2;
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        int i2 = i + 1;
        bArrGrow[i] = 91;
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (i3 != 0) {
                bArrGrow[i2] = 44;
                i2++;
            }
            if (z) {
                bArrGrow[i2] = (byte) this.quote;
                i2++;
            }
            int iWriteInt32 = IOUtils.writeInt32(bArrGrow, i2, iArr[i3]);
            if (z) {
                i2 = iWriteInt32 + 1;
                bArrGrow[iWriteInt32] = (byte) this.quote;
            } else {
                i2 = iWriteInt32;
            }
        }
        bArrGrow[i2] = 93;
        this.off = i2 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt8(byte b) {
        boolean z = (this.context.features & 256) != 0;
        int i = this.off;
        int i2 = i + 5;
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        if (z) {
            bArrGrow[i] = (byte) this.quote;
            i++;
        }
        int iWriteInt8 = IOUtils.writeInt8(bArrGrow, i, b);
        if (z) {
            bArrGrow[iWriteInt8] = (byte) this.quote;
            iWriteInt8++;
        }
        this.off = iWriteInt8;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt8(byte[] bArr) {
        if (bArr == null) {
            writeNull();
            return;
        }
        boolean z = (this.context.features & 256) != 0;
        int i = this.off;
        int length = (bArr.length * 5) + i + 2;
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        int i2 = i + 1;
        bArrGrow[i] = 91;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            if (i3 != 0) {
                bArrGrow[i2] = 44;
                i2++;
            }
            if (z) {
                bArrGrow[i2] = (byte) this.quote;
                i2++;
            }
            int iWriteInt8 = IOUtils.writeInt8(bArrGrow, i2, bArr[i3]);
            if (z) {
                i2 = iWriteInt8 + 1;
                bArrGrow[iWriteInt8] = (byte) this.quote;
            } else {
                i2 = iWriteInt8;
            }
        }
        bArrGrow[i2] = 93;
        this.off = i2 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt16(short s) {
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int i2 = i + 7;
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        if (z) {
            bArrGrow[i] = (byte) this.quote;
            i++;
        }
        int iWriteInt16 = IOUtils.writeInt16(bArrGrow, i, s);
        if (z) {
            bArrGrow[iWriteInt16] = (byte) this.quote;
            iWriteInt16++;
        }
        this.off = iWriteInt16;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt32(Integer num) {
        if (num == null) {
            writeNumberNull();
        } else {
            writeInt32(num.intValue());
        }
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt32(int i) {
        boolean z = (this.context.features & 256) != 0;
        int i2 = this.off;
        int i3 = i2 + 13;
        byte[] bArrGrow = this.bytes;
        if (i3 > bArrGrow.length) {
            bArrGrow = grow(i3);
        }
        if (z) {
            bArrGrow[i2] = (byte) this.quote;
            i2++;
        }
        int iWriteInt32 = IOUtils.writeInt32(bArrGrow, i2, i);
        if (z) {
            bArrGrow[iWriteInt32] = (byte) this.quote;
            iWriteInt32++;
        }
        this.off = iWriteInt32;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeListInt32(List<Integer> list) {
        if (list == null) {
            writeNull();
            return;
        }
        int size = list.size();
        boolean z = (this.context.features & 256) != 0;
        int i = this.off;
        int i2 = i + 2 + (size * 23);
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        int i3 = i + 1;
        bArrGrow[i] = 91;
        for (int i4 = 0; i4 < size; i4++) {
            if (i4 != 0) {
                bArrGrow[i3] = 44;
                i3++;
            }
            Integer num = list.get(i4);
            if (num == null) {
                IOUtils.putNULL(bArrGrow, i3);
                i3 += 4;
            } else {
                int iIntValue = num.intValue();
                if (z) {
                    bArrGrow[i3] = (byte) this.quote;
                    i3++;
                }
                int iWriteInt32 = IOUtils.writeInt32(bArrGrow, i3, iIntValue);
                if (z) {
                    i3 = iWriteInt32 + 1;
                    bArrGrow[iWriteInt32] = (byte) this.quote;
                } else {
                    i3 = iWriteInt32;
                }
            }
        }
        bArrGrow[i3] = 93;
        this.off = i3 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt64(long[] jArr) {
        if (jArr == null) {
            writeNull();
            return;
        }
        int i = this.off;
        int length = i + 2 + (jArr.length * 23);
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        int iWriteInt64 = i + 1;
        bArrGrow[i] = 91;
        for (int i2 = 0; i2 < jArr.length; i2++) {
            if (i2 != 0) {
                bArrGrow[iWriteInt64] = 44;
                iWriteInt64++;
            }
            long j = jArr[i2];
            boolean zIsWriteAsString = isWriteAsString(j, this.context.features);
            if (zIsWriteAsString) {
                bArrGrow[iWriteInt64] = (byte) this.quote;
                iWriteInt64++;
            }
            iWriteInt64 = IOUtils.writeInt64(bArrGrow, iWriteInt64, j);
            if (zIsWriteAsString) {
                bArrGrow[iWriteInt64] = (byte) this.quote;
                iWriteInt64++;
            }
        }
        bArrGrow[iWriteInt64] = 93;
        this.off = iWriteInt64 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeListInt64(List<Long> list) {
        if (list == null) {
            writeNull();
            return;
        }
        int size = list.size();
        int i = this.off;
        int i2 = i + 2 + (size * 23);
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        int iWriteInt64 = i + 1;
        bArrGrow[i] = 91;
        for (int i3 = 0; i3 < size; i3++) {
            if (i3 != 0) {
                bArrGrow[iWriteInt64] = 44;
                iWriteInt64++;
            }
            Long l = list.get(i3);
            if (l == null) {
                IOUtils.putNULL(bArrGrow, iWriteInt64);
                iWriteInt64 += 4;
            } else {
                long jLongValue = l.longValue();
                boolean zIsWriteAsString = isWriteAsString(jLongValue, this.context.features);
                if (zIsWriteAsString) {
                    bArrGrow[iWriteInt64] = (byte) this.quote;
                    iWriteInt64++;
                }
                iWriteInt64 = IOUtils.writeInt64(bArrGrow, iWriteInt64, jLongValue);
                if (zIsWriteAsString) {
                    bArrGrow[iWriteInt64] = (byte) this.quote;
                    iWriteInt64++;
                }
            }
        }
        bArrGrow[iWriteInt64] = 93;
        this.off = iWriteInt64 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt64(long j) {
        int i;
        long j2 = this.context.features;
        int i2 = this.off;
        int i3 = i2 + 23;
        byte[] bArrGrow = this.bytes;
        if (i3 > bArrGrow.length) {
            bArrGrow = grow(i3);
        }
        boolean zIsWriteAsString = isWriteAsString(j, j2);
        if (zIsWriteAsString) {
            bArrGrow[i2] = (byte) this.quote;
            i2++;
        }
        int iWriteInt64 = IOUtils.writeInt64(bArrGrow, i2, j);
        if (zIsWriteAsString) {
            i = iWriteInt64 + 1;
            bArrGrow[iWriteInt64] = (byte) this.quote;
        } else {
            if ((512 & j2) != 0 && (j2 & 1099511627776L) == 0 && j >= -2147483648L && j <= 2147483647L) {
                i = iWriteInt64 + 1;
                bArrGrow[iWriteInt64] = 76;
            }
            this.off = iWriteInt64;
        }
        iWriteInt64 = i;
        this.off = iWriteInt64;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt64(Long l) {
        if (l == null) {
            writeNumberNull();
        } else {
            writeInt64(l.longValue());
        }
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeFloat(float f) {
        boolean z = (this.context.features & 256) != 0;
        int i = this.off;
        int i2 = i + 17;
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        if (z) {
            bArrGrow[i] = 34;
            i++;
        }
        int iWriteFloat = NumberUtils.writeFloat(bArrGrow, i, f, true);
        if (z) {
            bArrGrow[iWriteFloat] = 34;
            iWriteFloat++;
        }
        this.off = iWriteFloat;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDouble(double d) {
        boolean z = (this.context.features & 256) != 0;
        int i = this.off;
        int i2 = i + 26;
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        if (z) {
            bArrGrow[i] = 34;
            i++;
        }
        int iWriteDouble = NumberUtils.writeDouble(bArrGrow, i, d, true);
        if (z) {
            bArrGrow[iWriteDouble] = 34;
            iWriteDouble++;
        }
        this.off = iWriteDouble;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeFloat(float[] fArr) {
        if (fArr == null) {
            writeArrayNull();
            return;
        }
        boolean z = (this.context.features & 256) != 0;
        int i = this.off;
        int length = (fArr.length * (z ? 16 : 18)) + i + 1;
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        int iWriteFloat = i + 1;
        bArrGrow[i] = 91;
        for (int i2 = 0; i2 < fArr.length; i2++) {
            if (i2 != 0) {
                bArrGrow[iWriteFloat] = 44;
                iWriteFloat++;
            }
            if (z) {
                bArrGrow[iWriteFloat] = 34;
                iWriteFloat++;
            }
            iWriteFloat = NumberUtils.writeFloat(bArrGrow, iWriteFloat, fArr[i2], true);
            if (z) {
                bArrGrow[iWriteFloat] = 34;
                iWriteFloat++;
            }
        }
        bArrGrow[iWriteFloat] = 93;
        this.off = iWriteFloat + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDouble(double[] dArr) {
        if (dArr == null) {
            writeNull();
            return;
        }
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int length = (dArr.length * 27) + i + 1;
        byte[] bArrGrow = this.bytes;
        if (length > bArrGrow.length) {
            bArrGrow = grow(length);
        }
        int iWriteDouble = i + 1;
        bArrGrow[i] = 91;
        for (int i2 = 0; i2 < dArr.length; i2++) {
            if (i2 != 0) {
                bArrGrow[iWriteDouble] = 44;
                iWriteDouble++;
            }
            if (z) {
                bArrGrow[iWriteDouble] = 34;
                iWriteDouble++;
            }
            iWriteDouble = NumberUtils.writeDouble(bArrGrow, iWriteDouble, dArr[i2], true);
            if (z) {
                bArrGrow[iWriteDouble] = 34;
                iWriteDouble++;
            }
        }
        bArrGrow[iWriteDouble] = 93;
        this.off = iWriteDouble + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDateTime14(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = this.off;
        int i8 = i7 + 16;
        byte[] bArrGrow = this.bytes;
        if (i8 > bArrGrow.length) {
            bArrGrow = grow(i8);
        }
        bArrGrow[i7] = (byte) this.quote;
        if (i < 0 || i > 9999) {
            throw illegalYear(i);
        }
        int i9 = i / 100;
        IOUtils.writeDigitPair(bArrGrow, i7 + 1, i9);
        IOUtils.writeDigitPair(bArrGrow, i7 + 3, i - (i9 * 100));
        IOUtils.writeDigitPair(bArrGrow, i7 + 5, i2);
        IOUtils.writeDigitPair(bArrGrow, i7 + 7, i3);
        IOUtils.writeDigitPair(bArrGrow, i7 + 9, i4);
        IOUtils.writeDigitPair(bArrGrow, i7 + 11, i5);
        IOUtils.writeDigitPair(bArrGrow, i7 + 13, i6);
        bArrGrow[i7 + 15] = (byte) this.quote;
        this.off = i8;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDateTime19(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = this.off;
        int i8 = i7 + 21;
        byte[] bArrGrow = this.bytes;
        if (i8 > bArrGrow.length) {
            bArrGrow = grow(i8);
        }
        bArrGrow[i7] = (byte) this.quote;
        int iWriteLocalDate = IOUtils.writeLocalDate(bArrGrow, i7 + 1, i, i2, i3);
        bArrGrow[iWriteLocalDate] = 32;
        IOUtils.writeLocalTime(bArrGrow, iWriteLocalDate + 1, i4, i5, i6);
        bArrGrow[iWriteLocalDate + 9] = (byte) this.quote;
        this.off = iWriteLocalDate + 10;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeLocalDate(LocalDate localDate) {
        if (localDate == null) {
            writeNull();
            return;
        }
        if (this.context.dateFormat == null || !writeLocalDateWithFormat(localDate)) {
            int i = this.off;
            int i2 = i + 18;
            byte[] bArrGrow = this.bytes;
            if (i2 > bArrGrow.length) {
                bArrGrow = grow(i2);
            }
            bArrGrow[i] = (byte) this.quote;
            int iWriteLocalDate = IOUtils.writeLocalDate(bArrGrow, i + 1, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
            bArrGrow[iWriteLocalDate] = (byte) this.quote;
            this.off = iWriteLocalDate + 1;
        }
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeLocalDateTime(LocalDateTime localDateTime) {
        int i = this.off;
        int i2 = i + 38;
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        int i3 = i + 1;
        bArrGrow[i] = (byte) this.quote;
        LocalDate localDate = localDateTime.toLocalDate();
        int iWriteLocalDate = IOUtils.writeLocalDate(bArrGrow, i3, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        bArrGrow[iWriteLocalDate] = 32;
        int iWriteLocalTime = IOUtils.writeLocalTime(bArrGrow, iWriteLocalDate + 1, localDateTime.toLocalTime());
        bArrGrow[iWriteLocalTime] = (byte) this.quote;
        this.off = iWriteLocalTime + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDateYYYMMDD8(int i, int i2, int i3) {
        int i4 = this.off;
        int i5 = i4 + 10;
        byte[] bArrGrow = this.bytes;
        if (i5 > bArrGrow.length) {
            bArrGrow = grow(i5);
        }
        bArrGrow[i4] = (byte) this.quote;
        if (i < 0 || i > 9999) {
            throw illegalYear(i);
        }
        int i6 = i / 100;
        IOUtils.writeDigitPair(bArrGrow, i4 + 1, i6);
        IOUtils.writeDigitPair(bArrGrow, i4 + 3, i - (i6 * 100));
        IOUtils.writeDigitPair(bArrGrow, i4 + 5, i2);
        IOUtils.writeDigitPair(bArrGrow, i4 + 7, i3);
        bArrGrow[i4 + 9] = (byte) this.quote;
        this.off = i5;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDateYYYMMDD10(int i, int i2, int i3) {
        int i4 = this.off;
        int i5 = i4 + 13;
        byte[] bArrGrow = this.bytes;
        if (i5 > bArrGrow.length) {
            bArrGrow = grow(i5);
        }
        bArrGrow[i4] = (byte) this.quote;
        int iWriteLocalDate = IOUtils.writeLocalDate(bArrGrow, i4 + 1, i, i2, i3);
        bArrGrow[iWriteLocalDate] = (byte) this.quote;
        this.off = iWriteLocalDate + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeTimeHHMMSS8(int i, int i2, int i3) {
        int i4 = this.off;
        int i5 = i4 + 10;
        byte[] bArrGrow = this.bytes;
        if (i5 > bArrGrow.length) {
            bArrGrow = grow(i5);
        }
        bArrGrow[i4] = (byte) this.quote;
        IOUtils.writeLocalTime(bArrGrow, i4 + 1, i, i2, i3);
        bArrGrow[i4 + 9] = (byte) this.quote;
        this.off = i5;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeLocalTime(LocalTime localTime) {
        int i = this.off;
        int i2 = i + 20;
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        bArrGrow[i] = (byte) this.quote;
        int iWriteLocalTime = IOUtils.writeLocalTime(bArrGrow, i + 1, localTime);
        bArrGrow[iWriteLocalTime] = (byte) this.quote;
        this.off = iWriteLocalTime + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeZonedDateTime(ZonedDateTime zonedDateTime) {
        char cCharAt;
        int i;
        int i2;
        if (zonedDateTime == null) {
            writeNull();
            return;
        }
        ZoneId zone = zonedDateTime.getZone();
        String id = zone.getId();
        int length = id.length();
        if (ZoneOffset.UTC == zone || (length <= 3 && ("UTC".equals(id) || "Z".equals(id)))) {
            id = "Z";
            cCharAt = 0;
            i = 1;
        } else {
            if (length != 0) {
                cCharAt = id.charAt(0);
                if (cCharAt == '+' || cCharAt == '-') {
                    i = length;
                }
            } else {
                cCharAt = 0;
            }
            i = length + 2;
        }
        int i3 = this.off;
        int i4 = i3 + i + 38;
        byte[] bArrGrow = this.bytes;
        if (i4 > bArrGrow.length) {
            bArrGrow = grow(i4);
        }
        bArrGrow[i3] = (byte) this.quote;
        LocalDate localDate = zonedDateTime.toLocalDate();
        int iWriteLocalDate = IOUtils.writeLocalDate(bArrGrow, i3 + 1, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        bArrGrow[iWriteLocalDate] = 84;
        int iWriteLocalTime = IOUtils.writeLocalTime(bArrGrow, iWriteLocalDate + 1, zonedDateTime.toLocalTime());
        if (i == 1) {
            i2 = iWriteLocalTime + 1;
            bArrGrow[iWriteLocalTime] = 90;
        } else if (cCharAt == '+' || cCharAt == '-') {
            id.getBytes(0, length, bArrGrow, iWriteLocalTime);
            i2 = iWriteLocalTime + length;
        } else {
            int i5 = iWriteLocalTime + 1;
            bArrGrow[iWriteLocalTime] = 91;
            id.getBytes(0, length, bArrGrow, i5);
            int i6 = i5 + length;
            bArrGrow[i6] = 93;
            i2 = i6 + 1;
        }
        bArrGrow[i2] = (byte) this.quote;
        this.off = i2 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeOffsetDateTime(OffsetDateTime offsetDateTime) {
        int length;
        if (offsetDateTime == null) {
            writeNull();
            return;
        }
        int i = this.off;
        int i2 = i + 45;
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        bArrGrow[i] = (byte) this.quote;
        LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        int iWriteLocalDate = IOUtils.writeLocalDate(bArrGrow, i + 1, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        bArrGrow[iWriteLocalDate] = 84;
        int iWriteLocalTime = IOUtils.writeLocalTime(bArrGrow, iWriteLocalDate + 1, localDateTime.toLocalTime());
        ZoneOffset offset = offsetDateTime.getOffset();
        if (offset.getTotalSeconds() == 0) {
            length = iWriteLocalTime + 1;
            bArrGrow[iWriteLocalTime] = 90;
        } else {
            String id = offset.getId();
            id.getBytes(0, id.length(), bArrGrow, iWriteLocalTime);
            length = id.length() + iWriteLocalTime;
        }
        bArrGrow[length] = (byte) this.quote;
        this.off = length + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeOffsetTime(OffsetTime offsetTime) {
        int length;
        if (offsetTime == null) {
            writeNull();
            return;
        }
        int i = this.off;
        int i2 = i + 28;
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        bArrGrow[i] = (byte) this.quote;
        int iWriteLocalTime = IOUtils.writeLocalTime(bArrGrow, i + 1, offsetTime.toLocalTime());
        ZoneOffset offset = offsetTime.getOffset();
        if (offset.getTotalSeconds() == 0) {
            length = iWriteLocalTime + 1;
            bArrGrow[iWriteLocalTime] = 90;
        } else {
            String id = offset.getId();
            id.getBytes(0, id.length(), bArrGrow, iWriteLocalTime);
            length = id.length() + iWriteLocalTime;
        }
        bArrGrow[length] = (byte) this.quote;
        this.off = length + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeBigInt(BigInteger bigInteger, long j) {
        if (bigInteger == null) {
            writeNumberNull();
            return;
        }
        if (TypeUtils.isInt64(bigInteger) && j == 0) {
            writeInt64(bigInteger.longValue());
            return;
        }
        String string = bigInteger.toString(10);
        boolean zIsWriteAsString = isWriteAsString(bigInteger, j | this.context.features);
        int i = this.off;
        int length = string.length();
        int i2 = i + length + (zIsWriteAsString ? 2 : 0);
        byte[] bArrGrow = this.bytes;
        if (i2 > bArrGrow.length) {
            bArrGrow = grow(i2);
        }
        if (zIsWriteAsString) {
            bArrGrow[i] = 34;
            i++;
        }
        string.getBytes(0, length, bArrGrow, i);
        int i3 = i + length;
        if (zIsWriteAsString) {
            bArrGrow[i3] = 34;
            i3++;
        }
        this.off = i3;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDateTimeISO8601(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        int i9 = z ? i8 == 0 ? 1 : 6 : 0;
        int i10 = this.off;
        int i11 = i10 + 25 + i9;
        byte[] bArrGrow = this.bytes;
        if (i11 > bArrGrow.length) {
            bArrGrow = grow(i11);
        }
        bArrGrow[i10] = (byte) this.quote;
        int iWriteLocalDate = IOUtils.writeLocalDate(bArrGrow, i10 + 1, i, i2, i3);
        bArrGrow[iWriteLocalDate] = (byte) (z ? 84 : 32);
        IOUtils.writeLocalTime(bArrGrow, iWriteLocalDate + 1, i4, i5, i6);
        int i12 = iWriteLocalDate + 9;
        if (i7 > 0) {
            int i13 = i7 / 10;
            int i14 = i13 / 10;
            if (i7 - (i13 * 10) != 0) {
                IOUtils.putIntLE(bArrGrow, i12, (IOUtils.DIGITS_K_32[i7 & 1023] & InputDeviceCompat.SOURCE_ANY) | 46);
                i12 = iWriteLocalDate + 13;
            } else {
                int i15 = iWriteLocalDate + 10;
                bArrGrow[i12] = 46;
                if (i13 - (i14 * 10) != 0) {
                    IOUtils.writeDigitPair(bArrGrow, i15, i13);
                    i12 = iWriteLocalDate + 12;
                } else {
                    i12 = iWriteLocalDate + 11;
                    bArrGrow[i15] = (byte) (i14 + 48);
                }
            }
        }
        if (z) {
            int i16 = i8 / 3600;
            if (i8 == 0) {
                bArrGrow[i12] = 90;
                i12++;
            } else {
                int iAbs = Math.abs(i16);
                bArrGrow[i12] = i16 >= 0 ? (byte) 43 : (byte) 45;
                IOUtils.writeDigitPair(bArrGrow, i12 + 1, iAbs);
                bArrGrow[i12 + 3] = 58;
                int i17 = (i8 - (i16 * 3600)) / 60;
                if (i17 < 0) {
                    i17 = -i17;
                }
                IOUtils.writeDigitPair(bArrGrow, i12 + 4, i17);
                i12 += 6;
            }
        }
        bArrGrow[i12] = (byte) this.quote;
        this.off = i12 + 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0074  */
    @Override // com.alibaba.fastjson2.JSONWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void writeDecimal(java.math.BigDecimal r9, long r10, java.text.DecimalFormat r12) {
        /*
            r8 = this;
            if (r9 != 0) goto L6
            r8.writeDecimalNull()
            return
        L6:
            if (r12 == 0) goto L10
            java.lang.String r9 = r12.format(r9)
            r8.writeRaw(r9)
            return
        L10:
            com.alibaba.fastjson2.JSONWriter$Context r12 = r8.context
            long r0 = r12.features
            long r10 = r10 | r0
            int r12 = r9.precision()
            boolean r0 = isWriteAsString(r9, r10)
            int r1 = r8.off
            int r2 = r1 + r12
            int r3 = r9.scale()
            int r3 = java.lang.Math.abs(r3)
            int r2 = r2 + r3
            int r2 = r2 + 7
            byte[] r3 = r8.bytes
            int r4 = r3.length
            if (r2 <= r4) goto L35
            byte[] r3 = r8.grow(r2)
        L35:
            r2 = 34
            if (r0 == 0) goto L3e
            int r4 = r1 + 1
            r3[r1] = r2
            r1 = r4
        L3e:
            com.alibaba.fastjson2.JSONWriter$Feature r4 = com.alibaba.fastjson2.JSONWriter.Feature.WriteBigDecimalAsPlain
            long r4 = r4.mask
            long r10 = r10 & r4
            r4 = 0
            int r10 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            r11 = 0
            if (r10 == 0) goto L4c
            r10 = 1
            goto L4d
        L4c:
            r10 = r11
        L4d:
            r4 = 19
            if (r12 >= r4) goto L74
            int r12 = r9.scale()
            if (r12 < 0) goto L74
            long r4 = com.alibaba.fastjson2.util.JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET
            r6 = -1
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 == 0) goto L74
            sun.misc.Unsafe r4 = com.alibaba.fastjson2.util.JDKUtils.UNSAFE
            long r5 = com.alibaba.fastjson2.util.JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET
            long r4 = r4.getLong(r9, r5)
            r6 = -9223372036854775808
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 == 0) goto L74
            if (r10 != 0) goto L74
            int r9 = com.alibaba.fastjson2.util.IOUtils.writeDecimal(r3, r1, r4, r12)
            goto L8b
        L74:
            if (r10 == 0) goto L7b
            java.lang.String r9 = r9.toPlainString()
            goto L7f
        L7b:
            java.lang.String r9 = r9.toString()
        L7f:
            int r10 = r9.length()
            r9.getBytes(r11, r10, r3, r1)
            int r9 = r9.length()
            int r9 = r9 + r1
        L8b:
            if (r0 == 0) goto L92
            int r10 = r9 + 1
            r3[r9] = r2
            r9 = r10
        L92:
            r8.off = r9
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF8.writeDecimal(java.math.BigDecimal, long, java.text.DecimalFormat):void");
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeNameRaw(char[] cArr) {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeNameRaw(char[] cArr, int i, int i2) {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void write(Map<?, ?> map) {
        if (this.pretty != 0) {
            super.write(map);
            return;
        }
        if (map == null) {
            writeNull();
            return;
        }
        long j = this.context.features;
        if ((NONE_DIRECT_FEATURES & j) != 0) {
            this.context.getObjectWriter(map.getClass()).write(this, map, null, null, 0L);
            return;
        }
        if (this.off == this.bytes.length) {
            grow(this.off + 1);
        }
        byte[] bArr = this.bytes;
        int i = this.off;
        this.off = i + 1;
        bArr[i] = JSONB.Constants.BC_STR_UTF16;
        boolean z = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value != null || (16 & j) != 0) {
                if (!z) {
                    if (this.off == this.bytes.length) {
                        grow0(this.off + 1);
                    }
                    byte[] bArr2 = this.bytes;
                    int i2 = this.off;
                    this.off = i2 + 1;
                    bArr2[i2] = 44;
                }
                Object key = entry.getKey();
                if (key instanceof String) {
                    writeString((String) key);
                } else {
                    writeAny(key);
                }
                if (this.off == this.bytes.length) {
                    grow0(this.off + 1);
                }
                byte[] bArr3 = this.bytes;
                int i3 = this.off;
                this.off = i3 + 1;
                bArr3[i3] = 58;
                if (value == null) {
                    writeNull();
                } else {
                    Class<?> cls = value.getClass();
                    if (cls == String.class) {
                        writeString((String) value);
                    } else if (cls == Integer.class) {
                        writeInt32((Integer) value);
                    } else if (cls == Long.class) {
                        writeInt64((Long) value);
                    } else if (cls == Boolean.class) {
                        writeBool(((Boolean) value).booleanValue());
                    } else if (cls == BigDecimal.class) {
                        writeDecimal((BigDecimal) value, 0L, null);
                    } else if (cls == JSONArray.class) {
                        write((JSONArray) value);
                    } else if (cls == JSONObject.class) {
                        write((JSONObject) value);
                    } else {
                        this.context.getObjectWriter(cls, cls).write(this, value, null, null, 0L);
                    }
                }
                z = false;
            }
        }
        if (this.off == this.bytes.length) {
            grow(this.off + 1);
        }
        byte[] bArr4 = this.bytes;
        int i4 = this.off;
        this.off = i4 + 1;
        bArr4[i4] = JSONB.Constants.BC_STR_UTF16BE;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void write(List list) {
        if (list == null) {
            writeArrayNull();
            return;
        }
        if ((this.context.features & 67309568) != 0) {
            this.context.getObjectWriter(list.getClass()).write(this, list, null, null, 0L);
            return;
        }
        if (this.off == this.bytes.length) {
            grow(this.off + 1);
        }
        byte[] bArr = this.bytes;
        int i = this.off;
        this.off = i + 1;
        bArr[i] = 91;
        boolean z = true;
        int i2 = 0;
        while (i2 < list.size()) {
            Object obj = list.get(i2);
            if (!z) {
                if (this.off == this.bytes.length) {
                    grow(this.off + 1);
                }
                byte[] bArr2 = this.bytes;
                int i3 = this.off;
                this.off = i3 + 1;
                bArr2[i3] = 44;
            }
            if (obj == null) {
                writeNull();
            } else {
                Class<?> cls = obj.getClass();
                if (cls == String.class) {
                    writeString((String) obj);
                } else if (cls == Integer.class) {
                    writeInt32((Integer) obj);
                } else if (cls == Long.class) {
                    writeInt64((Long) obj);
                } else if (cls == Boolean.class) {
                    writeBool(((Boolean) obj).booleanValue());
                } else if (cls == BigDecimal.class) {
                    writeDecimal((BigDecimal) obj, 0L, null);
                } else if (cls == JSONArray.class) {
                    write((JSONArray) obj);
                } else if (cls == JSONObject.class) {
                    write((JSONObject) obj);
                } else {
                    this.context.getObjectWriter(cls, cls).write(this, obj, null, null, 0L);
                }
            }
            i2++;
            z = false;
        }
        if (this.off == this.bytes.length) {
            grow(this.off + 1);
        }
        byte[] bArr3 = this.bytes;
        int i4 = this.off;
        this.off = i4 + 1;
        bArr3[i4] = 93;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public void writeBool(boolean z) {
        int iPutBoolean;
        int i = this.off + 5;
        byte[] bArrGrow = this.bytes;
        if (i > bArrGrow.length) {
            bArrGrow = grow(i);
        }
        int i2 = this.off;
        if ((this.context.features & 128) != 0) {
            iPutBoolean = i2 + 1;
            bArrGrow[i2] = (byte) (z ? 49 : 48);
        } else {
            iPutBoolean = IOUtils.putBoolean(bArrGrow, i2, z);
        }
        this.off = iPutBoolean;
    }

    public final String toString() {
        return new String(this.bytes, 0, this.off, StandardCharsets.UTF_8);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x002b  */
    @Override // com.alibaba.fastjson2.JSONWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int flushTo(java.io.OutputStream r5, java.nio.charset.Charset r6) throws java.io.IOException {
        /*
            r4 = this;
            int r0 = r4.off
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            if (r6 == 0) goto L49
            java.nio.charset.Charset r0 = java.nio.charset.StandardCharsets.UTF_8
            if (r6 == r0) goto L49
            java.nio.charset.Charset r0 = java.nio.charset.StandardCharsets.US_ASCII
            if (r6 != r0) goto L11
            goto L49
        L11:
            java.nio.charset.Charset r0 = java.nio.charset.StandardCharsets.ISO_8859_1
            if (r6 != r0) goto L37
            java.lang.invoke.MethodHandle r0 = com.alibaba.fastjson2.util.JDKUtils.METHOD_HANDLE_HAS_NEGATIVE
            if (r0 == 0) goto L28
            java.lang.invoke.MethodHandle r0 = com.alibaba.fastjson2.util.JDKUtils.METHOD_HANDLE_HAS_NEGATIVE     // Catch: java.lang.Throwable -> L28
            byte[] r2 = r4.bytes     // Catch: java.lang.Throwable -> L28
            int r3 = r2.length     // Catch: java.lang.Throwable -> L28
            java.lang.Boolean r0 = (java.lang.Boolean) r0.invoke(r2, r1, r3)     // Catch: java.lang.Throwable -> L28
            boolean r0 = r0.booleanValue()     // Catch: java.lang.Throwable -> L28
            goto L29
        L28:
            r0 = r1
        L29:
            if (r0 != 0) goto L37
            int r6 = r4.off
            byte[] r0 = r4.bytes
            int r2 = r4.off
            r5.write(r0, r1, r2)
            r4.off = r1
            return r6
        L37:
            java.lang.String r0 = new java.lang.String
            byte[] r2 = r4.bytes
            int r3 = r4.off
            r0.<init>(r2, r1, r3)
            byte[] r6 = r0.getBytes(r6)
            r5.write(r6)
            int r5 = r6.length
            return r5
        L49:
            int r6 = r4.off
            byte[] r0 = r4.bytes
            int r2 = r4.off
            r5.write(r0, r1, r2)
            r4.off = r1
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF8.flushTo(java.io.OutputStream, java.nio.charset.Charset):int");
    }
}
