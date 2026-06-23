package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.NumberUtils;
import com.alibaba.fastjson2.util.StringUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
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
import kotlin.text.Typography;
import org.apache.http.message.TokenParser;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: classes.dex */
class JSONWriterUTF16 extends JSONWriter {
    static final long BYTE_VEC_64_DOUBLE_QUOTE = 9570295239278626L;
    static final long BYTE_VEC_64_SINGLE_QUOTE = 10977691597996071L;
    static final int[] HEX256;
    static final int QUOTE2_COLON;
    static final int QUOTE_COLON;
    static final long REF_0;
    static final long REF_1;
    protected final long byteVectorQuote;
    final JSONFactory.CacheItem cacheItem;
    protected char[] chars;

    static long expand(long j) {
        return ((j & 4278190080L) << 24) | (255 & j) | ((65280 & j) << 8) | ((16711680 & j) << 16);
    }

    static {
        int[] iArr = new int[256];
        int i = 0;
        while (i < 16) {
            short s = (short) (i < 10 ? i + 48 : i + 87);
            int i2 = 0;
            while (i2 < 16) {
                iArr[(i << 4) + i2] = (((short) (i2 < 10 ? i2 + 48 : i2 + 87)) << 16) | s;
                i2++;
            }
            i++;
        }
        if (JDKUtils.BIG_ENDIAN) {
            for (int i3 = 0; i3 < 256; i3++) {
                iArr[i3] = Integer.reverseBytes(iArr[i3] << 8);
            }
        }
        HEX256 = iArr;
        char[] cArr = {'{', '\"', Typography.dollar, 'r', 'e', 'f', '\"', ':'};
        REF_0 = JDKUtils.UNSAFE.getLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET);
        REF_1 = JDKUtils.UNSAFE.getLong(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + 8);
        QUOTE2_COLON = JDKUtils.UNSAFE.getInt(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + 12);
        cArr[6] = '\'';
        QUOTE_COLON = JDKUtils.UNSAFE.getInt(cArr, JDKUtils.ARRAY_CHAR_BASE_OFFSET + 12);
    }

    JSONWriterUTF16(JSONWriter.Context context) {
        super(context, null, false, StandardCharsets.UTF_16);
        JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & (JSONFactory.CACHE_ITEMS.length - 1)];
        this.cacheItem = cacheItem;
        char[] andSet = JSONFactory.CHARS_UPDATER.getAndSet(cacheItem, null);
        this.chars = andSet == null ? new char[8192] : andSet;
        this.byteVectorQuote = this.useSingleQuote ? -2821266740684990248L : -2459565876494606883L;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeNull() {
        int i = this.off;
        int i2 = i + 4;
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        IOUtils.putNULL(cArrGrow, i);
        this.off = i2;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void flushTo(Writer writer) {
        try {
            int i = this.off;
            if (i > 0) {
                writer.write(this.chars, 0, i);
                this.off = 0;
            }
        } catch (IOException e) {
            throw new JSONException("flushTo error", e);
        }
    }

    @Override // com.alibaba.fastjson2.JSONWriter, java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        char[] cArr = this.chars;
        if (cArr.length > 8388608) {
            return;
        }
        JSONFactory.CHARS_UPDATER.lazySet(this.cacheItem, cArr);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    protected final void write0(char c) {
        int i = this.off;
        char[] cArrGrow = this.chars;
        if (i == cArrGrow.length) {
            cArrGrow = grow(i + 1);
        }
        cArrGrow[i] = c;
        this.off = i + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeColon() {
        int i = this.off;
        char[] cArrGrow = this.chars;
        if (i == cArrGrow.length) {
            cArrGrow = grow(i + 1);
        }
        cArrGrow[i] = ':';
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
        char[] cArrGrow = this.chars;
        int i3 = i2 + 3 + (this.pretty * this.level);
        if (i3 > cArrGrow.length) {
            cArrGrow = grow(i3);
        }
        int iIndent = i2 + 1;
        cArrGrow[i2] = '{';
        if (this.pretty != 0) {
            iIndent = indent(cArrGrow, iIndent);
        }
        this.off = iIndent;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void endObject() {
        this.level--;
        int iIndent = this.off;
        int i = iIndent + 1 + (this.pretty == 0 ? 0 : (this.pretty * this.level) + 1);
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        if (this.pretty != 0) {
            iIndent = indent(cArrGrow, iIndent);
        }
        cArrGrow[iIndent] = '}';
        this.off = iIndent + 1;
        this.startObject = false;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeComma() {
        this.startObject = false;
        int i = this.off;
        int i2 = i + 2 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        int iIndent = i + 1;
        cArrGrow[i] = ',';
        if (this.pretty != 0) {
            iIndent = indent(cArrGrow, iIndent);
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
        char[] cArrGrow = this.chars;
        if (i3 > cArrGrow.length) {
            cArrGrow = grow(i3);
        }
        int iIndent = i2 + 1;
        cArrGrow[i2] = '[';
        if (this.pretty != 0) {
            iIndent = indent(cArrGrow, iIndent);
        }
        this.off = iIndent;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void endArray() {
        this.level--;
        int iIndent = this.off;
        int i = iIndent + 1 + (this.pretty == 0 ? 0 : (this.pretty * this.level) + 1);
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        if (this.pretty != 0) {
            iIndent = indent(cArrGrow, iIndent);
        }
        cArrGrow[iIndent] = ']';
        this.off = iIndent + 1;
        this.startObject = false;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(List<String> list) {
        if (this.pretty != 0) {
            super.writeString(list);
            return;
        }
        if (this.off == this.chars.length) {
            grow(this.off + 1);
        }
        char[] cArr = this.chars;
        int i = this.off;
        this.off = i + 1;
        cArr[i] = '[';
        int size = list.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (i2 != 0) {
                if (this.off == this.chars.length) {
                    grow(this.off + 1);
                }
                char[] cArr2 = this.chars;
                int i3 = this.off;
                this.off = i3 + 1;
                cArr2[i3] = ',';
            }
            writeString(list.get(i2));
        }
        if (this.off == this.chars.length) {
            grow(this.off + 1);
        }
        char[] cArr3 = this.chars;
        int i4 = this.off;
        this.off = i4 + 1;
        cArr3[i4] = ']';
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public void writeStringLatin1(byte[] bArr) {
        int i;
        if ((this.context.features & 34359738368L) != 0) {
            writeStringLatin1BrowserSecure(bArr);
            return;
        }
        int i2 = this.off;
        char[] cArrGrow = this.chars;
        int length = bArr.length + i2 + 2;
        if (length >= cArrGrow.length) {
            cArrGrow = grow(length);
        }
        int i3 = i2 + 1;
        cArrGrow[i2] = this.quote;
        long j = this.byteVectorQuote;
        int length2 = bArr.length & (-8);
        int i4 = 0;
        while (i4 < length2) {
            long longLE = IOUtils.getLongLE(bArr, i4);
            if (!StringUtils.noneEscaped(longLE, j)) {
                break;
            }
            IOUtils.putLongLE(cArrGrow, i3, expand(longLE));
            IOUtils.putLongLE(cArrGrow, i3 + 4, expand(longLE >>> 32));
            i3 += 8;
            i4 += 8;
        }
        int i5 = i4;
        while (true) {
            i = i3;
            if (i5 < bArr.length) {
                byte b = bArr[i5];
                if (b == 92 || b == this.quote || b < 32) {
                    break;
                }
                i3 = i + 1;
                cArrGrow[i] = (char) b;
                i5++;
            } else {
                cArrGrow[i] = this.quote;
                this.off = i + 1;
                return;
            }
        }
        int length3 = length + (bArr.length * 5);
        if (length3 >= cArrGrow.length) {
            cArrGrow = grow(length3);
        }
        this.off = StringUtils.writeLatin1EscapedRest(cArrGrow, i, bArr, i5, this.quote, this.context.features);
    }

    protected final void writeStringLatin1BrowserSecure(byte[] bArr) {
        int i = this.off;
        int length = bArr.length + i + 2;
        if (length >= this.chars.length) {
            grow(length);
        }
        char[] cArr = this.chars;
        int i2 = i + 1;
        cArr[i] = this.quote;
        int length2 = bArr.length;
        int i3 = 0;
        while (i3 < length2) {
            byte b = bArr[i3];
            if (b != 92 && b != this.quote && b >= 32 && b != 60 && b != 62 && b != 40 && b != 41) {
                cArr[i2] = (char) b;
                i3++;
                i2++;
            } else {
                this.off = i;
                writeStringEscape(bArr);
                return;
            }
        }
        cArr[i2] = this.quote;
        this.off = i2 + 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0079  */
    @Override // com.alibaba.fastjson2.JSONWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void writeStringUTF16(byte[] r20) {
        /*
            r19 = this;
            r0 = r19
            r1 = r20
            if (r1 != 0) goto La
            r0.writeStringNull()
            return
        La:
            com.alibaba.fastjson2.JSONWriter$Context r2 = r0.context
            long r2 = r2.features
            com.alibaba.fastjson2.JSONWriter$Feature r4 = com.alibaba.fastjson2.JSONWriter.Feature.BrowserSecure
            long r4 = r4.mask
            com.alibaba.fastjson2.JSONWriter$Feature r6 = com.alibaba.fastjson2.JSONWriter.Feature.EscapeNoneAscii
            long r6 = r6.mask
            long r4 = r4 | r6
            long r2 = r2 & r4
            r4 = 0
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L22
            r19.writeStringUTF16BrowserSecure(r20)
            return
        L22:
            int r2 = r0.off
            int r3 = r1.length
            int r3 = r3 + r2
            int r3 = r3 + 2
            char[] r6 = r0.chars
            int r6 = r6.length
            if (r3 < r6) goto L30
            r0.grow(r3)
        L30:
            long r6 = r0.byteVectorQuote
            char[] r3 = r0.chars
            int r8 = r2 + 1
            char r9 = r0.quote
            r3[r2] = r9
            int r2 = r1.length
            int r2 = r2 >> 1
            r9 = 0
        L3e:
            if (r9 >= r2) goto L97
            int r10 = r9 + 8
            if (r10 >= r2) goto L79
            int r11 = r9 << 1
            long r11 = com.alibaba.fastjson2.util.IOUtils.getLongLE(r1, r11)
            int r13 = r9 + 4
            int r13 = r13 << 1
            long r13 = com.alibaba.fastjson2.util.IOUtils.getLongLE(r1, r13)
            long r15 = r11 | r13
            r17 = -71777214294589696(0xff00ff00ff00ff00, double:-5.82767264895205E303)
            long r15 = r15 & r17
            int r15 = (r15 > r4 ? 1 : (r15 == r4 ? 0 : -1))
            if (r15 != 0) goto L79
            r15 = 8
            long r15 = r11 << r15
            long r4 = r15 | r13
            boolean r4 = com.alibaba.fastjson2.util.StringUtils.noneEscaped(r4, r6)
            if (r4 == 0) goto L79
            com.alibaba.fastjson2.util.IOUtils.putLongLE(r3, r8, r11)
            int r4 = r8 + 4
            com.alibaba.fastjson2.util.IOUtils.putLongLE(r3, r4, r13)
            int r8 = r8 + 8
            r9 = r10
        L76:
            r4 = 0
            goto L3e
        L79:
            int r4 = r9 + 1
            char r5 = com.alibaba.fastjson2.util.IOUtils.getChar(r1, r9)
            r9 = 92
            if (r5 == r9) goto L93
            char r9 = r0.quote
            if (r5 == r9) goto L93
            r9 = 32
            if (r5 >= r9) goto L8c
            goto L93
        L8c:
            int r9 = r8 + 1
            r3[r8] = r5
            r8 = r9
            r9 = r4
            goto L76
        L93:
            r19.writeStringEscapeUTF16(r20)
            return
        L97:
            char r1 = r0.quote
            r3[r8] = r1
            int r8 = r8 + 1
            r0.off = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF16.writeStringUTF16(byte[]):void");
    }

    final void writeStringBrowserSecure(char[] cArr) {
        int i = 0;
        boolean z = (this.context.features & JSONWriter.Feature.EscapeNoneAscii.mask) != 0;
        int i2 = this.off;
        int length = cArr.length + i2 + 2;
        if (length >= this.chars.length) {
            grow(length);
        }
        char[] cArr2 = this.chars;
        int i3 = i2 + 1;
        cArr2[i2] = this.quote;
        int length2 = cArr.length;
        while (i < length2) {
            char c = IOUtils.getChar(cArr, i);
            if (c != '\\' && c != this.quote && c >= ' ' && c != '<' && c != '>' && c != '(' && c != ')' && (!z || c <= 127)) {
                cArr2[i3] = c;
                i++;
                i3++;
            } else {
                writeStringEscape(cArr);
                return;
            }
        }
        cArr2[i3] = this.quote;
        this.off = i3 + 1;
    }

    final void writeStringUTF16BrowserSecure(byte[] bArr) {
        int i = 0;
        boolean z = (this.context.features & JSONWriter.Feature.EscapeNoneAscii.mask) != 0;
        int i2 = this.off;
        int length = bArr.length + i2 + 2;
        if (length >= this.chars.length) {
            grow(length);
        }
        char[] cArr = this.chars;
        int i3 = i2 + 1;
        cArr[i2] = this.quote;
        int length2 = bArr.length >> 1;
        while (i < length2) {
            char c = IOUtils.getChar(bArr, i);
            if (c != '\\' && c != this.quote && c >= ' ' && c != '<' && c != '>' && c != '(' && c != ')' && (!z || c <= 127)) {
                cArr[i3] = c;
                i++;
                i3++;
            } else {
                writeStringEscapeUTF16(bArr);
                return;
            }
        }
        cArr[i3] = this.quote;
        this.off = i3 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public void writeString(String str) {
        if (str == null) {
            writeStringNull();
            return;
        }
        boolean z = (this.context.features & JSONWriter.Feature.EscapeNoneAscii.mask) != 0;
        boolean z2 = (this.context.features & JSONWriter.Feature.BrowserSecure.mask) != 0;
        char c = this.quote;
        int length = str.length();
        int i = this.off + length + 2;
        if (i >= this.chars.length) {
            grow(i);
        }
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\\' || cCharAt == c || cCharAt < ' ' || ((z2 && (cCharAt == '<' || cCharAt == '>' || cCharAt == '(' || cCharAt == ')')) || (z && cCharAt > 127))) {
                writeStringEscape(str);
                return;
            }
        }
        int i3 = this.off;
        char[] cArr = this.chars;
        int i4 = i3 + 1;
        cArr[i3] = c;
        str.getChars(0, length, cArr, i4);
        int i5 = i4 + length;
        cArr[i5] = c;
        this.off = i5 + 1;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:20:0x0053. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x007d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void writeStringEscape(java.lang.String r12) {
        /*
            Method dump skipped, instruction units count: 224
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF16.writeStringEscape(java.lang.String):void");
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:20:0x0057. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0081  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void writeStringEscapeUTF16(byte[] r14) {
        /*
            Method dump skipped, instruction units count: 228
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF16.writeStringEscapeUTF16(byte[]):void");
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:21:0x004f. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:31:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0079  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void writeStringEscape(char[] r12) {
        /*
            Method dump skipped, instruction units count: 220
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF16.writeStringEscape(char[]):void");
    }

    protected final void writeStringEscape(byte[] bArr) {
        int i = this.off;
        char[] cArrGrow = this.chars;
        int length = (bArr.length * 6) + i + 2;
        if (length >= cArrGrow.length) {
            cArrGrow = grow(length);
        }
        char[] cArr = cArrGrow;
        char c = this.quote;
        cArr[i] = c;
        this.off = StringUtils.writeLatin1EscapedRest(cArr, i + 1, bArr, 0, c, this.context.features);
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x005f  */
    @Override // com.alibaba.fastjson2.JSONWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void writeString(char[] r8, int r9, int r10, boolean r11) {
        /*
            r7 = this;
            com.alibaba.fastjson2.JSONWriter$Context r0 = r7.context
            long r0 = r0.features
            com.alibaba.fastjson2.JSONWriter$Feature r2 = com.alibaba.fastjson2.JSONWriter.Feature.EscapeNoneAscii
            long r2 = r2.mask
            long r0 = r0 & r2
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 == 0) goto L11
            r0 = 1
            goto L12
        L11:
            r0 = 0
        L12:
            char r1 = r7.quote
            int r2 = r7.off
            if (r11 == 0) goto L1b
            int r3 = r2 + 2
            goto L1c
        L1b:
            r3 = r2
        L1c:
            if (r0 == 0) goto L21
            int r4 = r10 * 6
            goto L23
        L21:
            int r4 = r10 * 2
        L23:
            int r3 = r3 + r4
            char[] r4 = r7.chars
            int r5 = r4.length
            int r5 = r3 - r5
            if (r5 <= 0) goto L2f
            char[] r4 = r7.grow(r3)
        L2f:
            if (r11 == 0) goto L36
            int r3 = r2 + 1
            r4[r2] = r1
            r2 = r3
        L36:
            if (r9 >= r10) goto L74
            char r3 = r8[r9]
            r5 = 34
            r6 = 92
            if (r3 == r5) goto L65
            r5 = 39
            if (r3 == r5) goto L65
            if (r3 == r6) goto L5f
            switch(r3) {
                case 0: goto L59;
                case 1: goto L59;
                case 2: goto L59;
                case 3: goto L59;
                case 4: goto L59;
                case 5: goto L59;
                case 6: goto L59;
                case 7: goto L59;
                case 8: goto L5f;
                case 9: goto L5f;
                case 10: goto L5f;
                case 11: goto L59;
                case 12: goto L5f;
                case 13: goto L5f;
                case 14: goto L59;
                case 15: goto L59;
                case 16: goto L59;
                case 17: goto L59;
                case 18: goto L59;
                case 19: goto L59;
                case 20: goto L59;
                case 21: goto L59;
                case 22: goto L59;
                case 23: goto L59;
                case 24: goto L59;
                case 25: goto L59;
                case 26: goto L59;
                case 27: goto L59;
                case 28: goto L59;
                case 29: goto L59;
                case 30: goto L59;
                case 31: goto L59;
                default: goto L49;
            }
        L49:
            if (r0 == 0) goto L53
            r5 = 127(0x7f, float:1.78E-43)
            if (r3 <= r5) goto L53
            com.alibaba.fastjson2.util.StringUtils.writeU4HexU(r4, r2, r3)
            goto L5c
        L53:
            int r5 = r2 + 1
            r4[r2] = r3
        L57:
            r2 = r5
            goto L71
        L59:
            com.alibaba.fastjson2.util.StringUtils.writeU4Hex2(r4, r2, r3)
        L5c:
            int r2 = r2 + 6
            goto L71
        L5f:
            com.alibaba.fastjson2.util.StringUtils.writeEscapedChar(r4, r2, r3)
            int r2 = r2 + 2
            goto L71
        L65:
            if (r3 != r1) goto L6c
            int r5 = r2 + 1
            r4[r2] = r6
            r2 = r5
        L6c:
            int r5 = r2 + 1
            r4[r2] = r3
            goto L57
        L71:
            int r9 = r9 + 1
            goto L36
        L74:
            if (r11 == 0) goto L7b
            int r8 = r2 + 1
            r4[r2] = r1
            r2 = r8
        L7b:
            r7.off = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF16.writeString(char[], int, int, boolean):void");
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(String[] strArr) {
        if (this.pretty != 0 || strArr == null) {
            super.writeString(strArr);
            return;
        }
        if (this.off == this.chars.length) {
            grow(this.off + 1);
        }
        char[] cArr = this.chars;
        int i = this.off;
        this.off = i + 1;
        cArr[i] = '[';
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (i2 != 0) {
                if (this.off == this.chars.length) {
                    grow(this.off + 1);
                }
                char[] cArr2 = this.chars;
                int i3 = this.off;
                this.off = i3 + 1;
                cArr2[i3] = ',';
            }
            writeString(strArr[i2]);
        }
        if (this.off == this.chars.length) {
            grow(this.off + 1);
        }
        char[] cArr3 = this.chars;
        int i4 = this.off;
        this.off = i4 + 1;
        cArr3[i4] = ']';
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeReference(String str) {
        this.lastReference = str;
        int i = this.off;
        char[] cArrGrow = this.chars;
        int i2 = i + 9;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        char[] cArr = cArrGrow;
        long j = (((long) i) << 1) + JDKUtils.ARRAY_BYTE_BASE_OFFSET;
        JDKUtils.UNSAFE.putLong(cArr, j, REF_0);
        JDKUtils.UNSAFE.putLong(cArr, j + 8, REF_1);
        this.off = i + 8;
        writeString(str);
        int i3 = this.off;
        char[] cArrGrow2 = this.chars;
        if (i3 == cArrGrow2.length) {
            cArrGrow2 = grow(i3 + 1);
        }
        cArrGrow2[i3] = '}';
        this.off = i3 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeBase64(byte[] bArr) {
        if (bArr == null) {
            writeArrayNull();
            return;
        }
        int i = this.off;
        ensureCapacityInternal(((((bArr.length - 1) / 3) + 1) << 2) + i + 2);
        char[] cArr = this.chars;
        int i2 = i + 1;
        cArr[i] = this.quote;
        int length = (bArr.length / 3) * 3;
        int i3 = 0;
        while (i3 < length) {
            int i4 = i3 + 2;
            int i5 = ((bArr[i3 + 1] & UByte.MAX_VALUE) << 8) | ((bArr[i3] & UByte.MAX_VALUE) << 16);
            i3 += 3;
            int i6 = i5 | (bArr[i4] & UByte.MAX_VALUE);
            cArr[i2] = JSONFactory.CA[(i6 >>> 18) & 63];
            cArr[i2 + 1] = JSONFactory.CA[(i6 >>> 12) & 63];
            cArr[i2 + 2] = JSONFactory.CA[(i6 >>> 6) & 63];
            cArr[i2 + 3] = JSONFactory.CA[i6 & 63];
            i2 += 4;
        }
        int length2 = bArr.length - length;
        if (length2 > 0) {
            int i7 = ((bArr[length] & UByte.MAX_VALUE) << 10) | (length2 == 2 ? (bArr[bArr.length - 1] & UByte.MAX_VALUE) << 2 : 0);
            cArr[i2] = JSONFactory.CA[i7 >> 12];
            cArr[i2 + 1] = JSONFactory.CA[(i7 >>> 6) & 63];
            cArr[i2 + 2] = length2 == 2 ? JSONFactory.CA[i7 & 63] : '=';
            cArr[i2 + 3] = '=';
            i2 += 4;
        }
        cArr[i2] = this.quote;
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
        char[] cArrGrow = this.chars;
        int i2 = length + i + 2;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        cArrGrow[i] = 'x';
        cArrGrow[i + 1] = '\'';
        int i3 = i + 2;
        for (byte b : bArr) {
            int i4 = (b & UByte.MAX_VALUE) >> 4;
            int i5 = b & 15;
            int i6 = 48;
            cArrGrow[i3] = (char) (i4 + (i4 < 10 ? 48 : 55));
            int i7 = i3 + 1;
            if (i5 >= 10) {
                i6 = 55;
            }
            cArrGrow[i7] = (char) (i5 + i6);
            i3 += 2;
        }
        cArrGrow[i3] = '\'';
        this.off = i3 + 1;
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
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        if (zIsWriteAsString) {
            cArrGrow[i] = '\"';
            i++;
        }
        string.getChars(0, length, cArrGrow, i);
        int i3 = i + length;
        if (zIsWriteAsString) {
            cArrGrow[i3] = '\"';
            i3++;
        }
        this.off = i3;
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
            char[] r3 = r8.chars
            int r4 = r3.length
            if (r2 <= r4) goto L35
            char[] r3 = r8.grow(r2)
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
            r9.getChars(r11, r10, r3, r1)
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
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF16.writeDecimal(java.math.BigDecimal, long, java.text.DecimalFormat):void");
    }

    static void putLong(char[] cArr, int i, int i2, int i3) {
        int[] iArr = HEX256;
        long jReverseBytes = (((long) iArr[i3 & 255]) << 32) | ((long) iArr[i2 & 255]);
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j = JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1);
        if (JDKUtils.BIG_ENDIAN) {
            jReverseBytes = Long.reverseBytes(jReverseBytes << 8);
        }
        unsafe.putLong(cArr, j, jReverseBytes);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeUUID(UUID uuid) {
        if (uuid == null) {
            writeNull();
            return;
        }
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        int i = this.off + 38;
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        int i2 = this.off;
        cArrGrow[i2] = '\"';
        putLong(cArrGrow, i2 + 1, (int) (mostSignificantBits >> 56), (int) (mostSignificantBits >> 48));
        putLong(cArrGrow, i2 + 5, (int) (mostSignificantBits >> 40), (int) (mostSignificantBits >> 32));
        cArrGrow[i2 + 9] = '-';
        int i3 = (int) mostSignificantBits;
        putLong(cArrGrow, i2 + 10, i3 >> 24, i3 >> 16);
        cArrGrow[i2 + 14] = '-';
        putLong(cArrGrow, i2 + 15, i3 >> 8, i3);
        cArrGrow[i2 + 19] = '-';
        putLong(cArrGrow, i2 + 20, (int) (leastSignificantBits >> 56), (int) (leastSignificantBits >> 48));
        cArrGrow[i2 + 24] = '-';
        putLong(cArrGrow, i2 + 25, (int) (leastSignificantBits >> 40), (int) (leastSignificantBits >> 32));
        int i4 = (int) leastSignificantBits;
        putLong(cArrGrow, i2 + 29, i4 >> 24, i4 >> 16);
        putLong(cArrGrow, i2 + 33, i4 >> 8, i4);
        cArrGrow[i2 + 37] = '\"';
        this.off += 38;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeRaw(String str) {
        int length = str.length();
        int i = this.off;
        char[] cArrGrow = this.chars;
        int i2 = i + length;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        str.getChars(0, length, cArrGrow, i);
        this.off = i2;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeRaw(char[] cArr, int i, int i2) {
        int i3 = this.off;
        char[] cArrGrow = this.chars;
        int i4 = i3 + i2;
        if (i4 > cArrGrow.length) {
            cArrGrow = grow(i4);
        }
        System.arraycopy(cArr, i, cArrGrow, i3, i2);
        this.off = i4;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0097  */
    @Override // com.alibaba.fastjson2.JSONWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void writeChar(char r8) {
        /*
            Method dump skipped, instruction units count: 248
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF16.writeChar(char):void");
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeRaw(char c) {
        if (this.off == this.chars.length) {
            grow0(this.off + 1);
        }
        char[] cArr = this.chars;
        int i = this.off;
        this.off = i + 1;
        cArr[i] = c;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeRaw(char c, char c2) {
        int i = this.off;
        char[] cArrGrow = this.chars;
        int i2 = i + 2;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        cArrGrow[i] = c;
        cArrGrow[i + 1] = c2;
        this.off = i2;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeNameRaw(char[] cArr) {
        int iIndent = this.off;
        int length = cArr.length + iIndent + 2 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (length > cArrGrow.length) {
            cArrGrow = grow(length);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i = iIndent + 1;
            cArrGrow[iIndent] = ',';
            iIndent = this.pretty != 0 ? indent(cArrGrow, i) : i;
        }
        System.arraycopy(cArr, 0, cArrGrow, iIndent, cArr.length);
        this.off = iIndent + cArr.length;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName2Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 10 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            cArrGrow[iIndent] = ',';
            iIndent = this.pretty != 0 ? indent(cArrGrow, i2) : i2;
        }
        putLong(cArrGrow, iIndent, j);
        this.off = iIndent + 5;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName3Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 10 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            cArrGrow[iIndent] = ',';
            iIndent = this.pretty != 0 ? indent(cArrGrow, i2) : i2;
        }
        putLong(cArrGrow, iIndent, j);
        this.off = iIndent + 6;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName4Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 10 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            cArrGrow[iIndent] = ',';
            iIndent = this.pretty != 0 ? indent(cArrGrow, i2) : i2;
        }
        putLong(cArrGrow, iIndent, j);
        this.off = iIndent + 7;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName5Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 10 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            cArrGrow[iIndent] = ',';
            iIndent = this.pretty != 0 ? indent(cArrGrow, i2) : i2;
        }
        putLong(cArrGrow, iIndent, j);
        this.off = iIndent + 8;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName6Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 11 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            cArrGrow[iIndent] = ',';
            iIndent = this.pretty != 0 ? indent(cArrGrow, i2) : i2;
        }
        putLong(cArrGrow, iIndent, j);
        cArrGrow[iIndent + 8] = ':';
        this.off = iIndent + 9;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName7Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 12 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            cArrGrow[iIndent] = ',';
            iIndent = this.pretty != 0 ? indent(cArrGrow, i2) : i2;
        }
        putLong(cArrGrow, iIndent, j);
        IOUtils.putIntUnaligned(cArrGrow, iIndent + 8, this.useSingleQuote ? QUOTE_COLON : QUOTE2_COLON);
        this.off = iIndent + 10;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName8Raw(long j) {
        int iIndent = this.off;
        int i = iIndent + 13 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            cArrGrow[iIndent] = ',';
            iIndent = this.pretty != 0 ? indent(cArrGrow, i2) : i2;
        }
        cArrGrow[iIndent] = this.quote;
        putLong(cArrGrow, iIndent + 1, j);
        IOUtils.putIntUnaligned(cArrGrow, iIndent + 9, this.useSingleQuote ? QUOTE_COLON : QUOTE2_COLON);
        this.off = iIndent + 11;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName9Raw(long j, int i) {
        int iIndent = this.off;
        int i2 = iIndent + 14 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i3 = iIndent + 1;
            cArrGrow[iIndent] = ',';
            iIndent = this.pretty != 0 ? indent(cArrGrow, i3) : i3;
        }
        putLong(cArrGrow, iIndent, j, i);
        this.off = iIndent + 12;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName10Raw(long j, long j2) {
        long j3;
        long j4;
        int i;
        int iIndent = this.off;
        int i2 = iIndent + 18 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        char[] cArr = cArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i3 = iIndent + 1;
            cArr[iIndent] = ',';
            if (this.pretty != 0) {
                iIndent = indent(cArr, i3);
            } else {
                j3 = j;
                j4 = j2;
                i = i3;
                putLong(cArr, i, j3, j4);
                this.off = i + 13;
            }
        }
        j3 = j;
        j4 = j2;
        i = iIndent;
        putLong(cArr, i, j3, j4);
        this.off = i + 13;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName11Raw(long j, long j2) {
        long j3;
        long j4;
        int i;
        int iIndent = this.off;
        int i2 = iIndent + 18 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        char[] cArr = cArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i3 = iIndent + 1;
            cArr[iIndent] = ',';
            if (this.pretty != 0) {
                iIndent = indent(cArr, i3);
            } else {
                j3 = j;
                j4 = j2;
                i = i3;
                putLong(cArr, i, j3, j4);
                this.off = i + 14;
            }
        }
        j3 = j;
        j4 = j2;
        i = iIndent;
        putLong(cArr, i, j3, j4);
        this.off = i + 14;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName12Raw(long j, long j2) {
        long j3;
        long j4;
        int i;
        int iIndent = this.off;
        int i2 = iIndent + 18 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        char[] cArr = cArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i3 = iIndent + 1;
            cArr[iIndent] = ',';
            if (this.pretty != 0) {
                iIndent = indent(cArr, i3);
            } else {
                j3 = j;
                j4 = j2;
                i = i3;
                putLong(cArr, i, j3, j4);
                this.off = i + 15;
            }
        }
        j3 = j;
        j4 = j2;
        i = iIndent;
        putLong(cArr, i, j3, j4);
        this.off = i + 15;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName13Raw(long j, long j2) {
        long j3;
        long j4;
        int i;
        int iIndent = this.off;
        int i2 = iIndent + 18 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        char[] cArr = cArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i3 = iIndent + 1;
            cArr[iIndent] = ',';
            if (this.pretty != 0) {
                iIndent = indent(cArr, i3);
            } else {
                j3 = j;
                j4 = j2;
                i = i3;
                putLong(cArr, i, j3, j4);
                this.off = i + 16;
            }
        }
        j3 = j;
        j4 = j2;
        i = iIndent;
        putLong(cArr, i, j3, j4);
        this.off = i + 16;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName14Raw(long j, long j2) {
        long j3;
        long j4;
        int i;
        int iIndent = this.off;
        int i2 = iIndent + 19 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        char[] cArr = cArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i3 = iIndent + 1;
            cArr[iIndent] = ',';
            if (this.pretty != 0) {
                iIndent = indent(cArr, i3);
            } else {
                j3 = j;
                j4 = j2;
                i = i3;
                putLong(cArr, i, j3, j4);
                int i4 = i;
                cArr[i4 + 16] = ':';
                this.off = i4 + 17;
            }
        }
        j3 = j;
        j4 = j2;
        i = iIndent;
        putLong(cArr, i, j3, j4);
        int i42 = i;
        cArr[i42 + 16] = ':';
        this.off = i42 + 17;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003e  */
    @Override // com.alibaba.fastjson2.JSONWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void writeName15Raw(long r10, long r12) {
        /*
            r9 = this;
            int r0 = r9.off
            int r1 = r0 + 20
            byte r2 = r9.pretty
            int r3 = r9.level
            int r2 = r2 * r3
            int r1 = r1 + r2
            char[] r2 = r9.chars
            int r3 = r2.length
            if (r1 <= r3) goto L13
            char[] r2 = r9.grow(r1)
        L13:
            r3 = r2
            boolean r1 = r9.startObject
            if (r1 == 0) goto L1f
            r1 = 0
            r9.startObject = r1
        L1b:
            r5 = r10
            r7 = r12
            r4 = r0
            goto L31
        L1f:
            int r1 = r0 + 1
            r2 = 44
            r3[r0] = r2
            byte r0 = r9.pretty
            if (r0 == 0) goto L2e
            int r0 = r9.indent(r3, r1)
            goto L1b
        L2e:
            r5 = r10
            r7 = r12
            r4 = r1
        L31:
            putLong(r3, r4, r5, r7)
            r0 = r4
            int r4 = r0 + 16
            boolean r10 = r9.useSingleQuote
            if (r10 == 0) goto L3e
            int r10 = com.alibaba.fastjson2.JSONWriterUTF16.QUOTE_COLON
            goto L40
        L3e:
            int r10 = com.alibaba.fastjson2.JSONWriterUTF16.QUOTE2_COLON
        L40:
            com.alibaba.fastjson2.util.IOUtils.putIntUnaligned(r3, r4, r10)
            int r4 = r0 + 18
            r9.off = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF16.writeName15Raw(long, long):void");
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeName16Raw(long j, long j2) {
        int iIndent = this.off;
        int i = iIndent + 21 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i > cArrGrow.length) {
            cArrGrow = grow(i);
        }
        char[] cArr = cArrGrow;
        if (this.startObject) {
            this.startObject = false;
        } else {
            int i2 = iIndent + 1;
            cArr[iIndent] = ',';
            iIndent = this.pretty != 0 ? indent(cArr, i2) : i2;
        }
        cArr[iIndent] = this.quote;
        putLong(cArr, iIndent + 1, j, j2);
        IOUtils.putIntUnaligned(cArr, iIndent + 17, this.useSingleQuote ? QUOTE_COLON : QUOTE2_COLON);
        this.off = iIndent + 19;
    }

    private static void putLong(char[] cArr, int i, long j) {
        long j2 = JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1);
        JDKUtils.UNSAFE.putLong(cArr, j2, (j & 255) | ((j & 65280) << 8) | ((j & 16711680) << 16) | ((j & 4278190080L) << 24));
        JDKUtils.UNSAFE.putLong(cArr, j2 + 8, ((j & 1095216660480L) >> 32) | ((j & 280375465082880L) >> 24) | ((j & 71776119061217280L) >> 16) | ((j & (-72057594037927936L)) >> 8));
    }

    private static void putLong(char[] cArr, int i, long j, int i2) {
        long j2 = JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1);
        JDKUtils.UNSAFE.putLong(cArr, j2, ((j & 4278190080L) << 24) | (j & 255) | ((j & 65280) << 8) | ((j & 16711680) << 16));
        JDKUtils.UNSAFE.putLong(cArr, j2 + 8, ((j & 1095216660480L) >> 32) | ((j & 280375465082880L) >> 24) | ((j & 71776119061217280L) >> 16) | ((j & (-72057594037927936L)) >> 8));
        long j3 = i2;
        JDKUtils.UNSAFE.putLong(cArr, j2 + 16, (255 & j3) | ((j3 & 65280) << 8) | ((j3 & 16711680) << 16) | ((j3 & 4278190080L) << 24));
    }

    private static void putLong(char[] cArr, int i, long j, long j2) {
        long j3 = JDKUtils.ARRAY_CHAR_BASE_OFFSET + (((long) i) << 1);
        JDKUtils.UNSAFE.putLong(cArr, j3, ((j & 4278190080L) << 24) | (j & 255) | ((j & 65280) << 8) | ((j & 16711680) << 16));
        JDKUtils.UNSAFE.putLong(cArr, j3 + 8, ((j & 1095216660480L) >> 32) | ((j & 280375465082880L) >> 24) | ((j & 71776119061217280L) >> 16) | ((j & (-72057594037927936L)) >> 8));
        JDKUtils.UNSAFE.putLong(cArr, j3 + 16, (j2 & 255) | ((j2 & 65280) << 8) | ((j2 & 16711680) << 16) | ((j2 & 4278190080L) << 24));
        JDKUtils.UNSAFE.putLong(cArr, j3 + 24, ((j2 & 1095216660480L) >> 32) | ((j2 & 280375465082880L) >> 24) | ((j2 & 71776119061217280L) >> 16) | ((j2 & (-72057594037927936L)) >> 8));
    }

    private int indent(char[] cArr, int i) {
        cArr[i] = '\n';
        int i2 = i + 1;
        int i3 = (this.pretty * this.level) + i2;
        Arrays.fill(cArr, i2, i3, this.pretty == 1 ? '\t' : TokenParser.SP);
        return i3;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeNameRaw(char[] cArr, int i, int i2) {
        int i3 = this.off;
        int i4 = i3 + i2 + 2 + (this.pretty * this.level);
        char[] cArrGrow = this.chars;
        if (i4 > cArrGrow.length) {
            cArrGrow = grow(i4);
        }
        if (this.startObject) {
            this.startObject = false;
        } else {
            cArrGrow[i3] = ',';
            i3++;
        }
        System.arraycopy(cArr, i, cArrGrow, i3, i2);
        this.off = i3 + i2;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final Object ensureCapacity(int i) {
        char[] cArr = this.chars;
        if (i < cArr.length) {
            return cArr;
        }
        char[] cArrCopyOf = Arrays.copyOf(cArr, newCapacity(i, cArr.length));
        this.chars = cArrCopyOf;
        return cArrCopyOf;
    }

    final void ensureCapacityInternal(int i) {
        if (i > this.chars.length) {
            grow0(i);
        }
    }

    private char[] grow(int i) {
        grow0(i);
        return this.chars;
    }

    protected final void grow0(int i) {
        char[] cArr = this.chars;
        this.chars = Arrays.copyOf(cArr, newCapacity(i, cArr.length));
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt32(int[] iArr) {
        if (iArr == null) {
            writeNull();
            return;
        }
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int length = (iArr.length * 13) + i + 2;
        char[] cArrGrow = this.chars;
        if (length > cArrGrow.length) {
            cArrGrow = grow(length);
        }
        int i2 = i + 1;
        cArrGrow[i] = '[';
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (i3 != 0) {
                cArrGrow[i2] = ',';
                i2++;
            }
            if (z) {
                cArrGrow[i2] = this.quote;
                i2++;
            }
            int iWriteInt32 = IOUtils.writeInt32(cArrGrow, i2, iArr[i3]);
            if (z) {
                i2 = iWriteInt32 + 1;
                cArrGrow[iWriteInt32] = this.quote;
            } else {
                i2 = iWriteInt32;
            }
        }
        cArrGrow[i2] = ']';
        this.off = i2 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt8(byte b) {
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int i2 = i + 7;
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        if (z) {
            cArrGrow[i] = this.quote;
            i++;
        }
        int iWriteInt8 = IOUtils.writeInt8(cArrGrow, i, b);
        if (z) {
            cArrGrow[iWriteInt8] = this.quote;
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
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int length = (bArr.length * 5) + i + 2;
        char[] cArrGrow = this.chars;
        if (length > cArrGrow.length) {
            cArrGrow = grow(length);
        }
        int i2 = i + 1;
        cArrGrow[i] = '[';
        for (int i3 = 0; i3 < bArr.length; i3++) {
            if (i3 != 0) {
                cArrGrow[i2] = ',';
                i2++;
            }
            if (z) {
                cArrGrow[i2] = this.quote;
                i2++;
            }
            int iWriteInt8 = IOUtils.writeInt8(cArrGrow, i2, bArr[i3]);
            if (z) {
                i2 = iWriteInt8 + 1;
                cArrGrow[iWriteInt8] = this.quote;
            } else {
                i2 = iWriteInt8;
            }
        }
        cArrGrow[i2] = ']';
        this.off = i2 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt16(short s) {
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int i2 = i + 7;
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        if (z) {
            cArrGrow[i] = this.quote;
            i++;
        }
        int iWriteInt16 = IOUtils.writeInt16(cArrGrow, i, s);
        if (z) {
            cArrGrow[iWriteInt16] = this.quote;
            iWriteInt16++;
        }
        this.off = iWriteInt16;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt32(int i) {
        boolean z = (this.context.features & 256) != 0;
        int i2 = this.off;
        int i3 = i2 + 13;
        char[] cArrGrow = this.chars;
        if (i3 > cArrGrow.length) {
            cArrGrow = grow(i3);
        }
        if (z) {
            cArrGrow[i2] = this.quote;
            i2++;
        }
        int iWriteInt32 = IOUtils.writeInt32(cArrGrow, i2, i);
        if (z) {
            cArrGrow[iWriteInt32] = this.quote;
            iWriteInt32++;
        }
        this.off = iWriteInt32;
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
    public final void writeInt64(long[] jArr) {
        if (jArr == null) {
            writeNull();
            return;
        }
        int i = this.off;
        int length = i + 2 + (jArr.length * 23);
        char[] cArrGrow = this.chars;
        if (length > cArrGrow.length) {
            cArrGrow = grow(length);
        }
        int iWriteInt64 = i + 1;
        cArrGrow[i] = '[';
        for (int i2 = 0; i2 < jArr.length; i2++) {
            if (i2 != 0) {
                cArrGrow[iWriteInt64] = ',';
                iWriteInt64++;
            }
            long j = jArr[i2];
            boolean zIsWriteAsString = isWriteAsString(j, this.context.features);
            if (zIsWriteAsString) {
                cArrGrow[iWriteInt64] = this.quote;
                iWriteInt64++;
            }
            iWriteInt64 = IOUtils.writeInt64(cArrGrow, iWriteInt64, j);
            if (zIsWriteAsString) {
                cArrGrow[iWriteInt64] = this.quote;
                iWriteInt64++;
            }
        }
        cArrGrow[iWriteInt64] = ']';
        this.off = iWriteInt64 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeListInt32(List<Integer> list) {
        if (list == null) {
            writeNull();
            return;
        }
        int size = list.size();
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int i2 = i + 2 + (size * 23);
        if (i2 >= this.chars.length) {
            grow0(i2);
        }
        char[] cArr = this.chars;
        int i3 = i + 1;
        cArr[i] = '[';
        for (int i4 = 0; i4 < size; i4++) {
            if (i4 != 0) {
                cArr[i3] = ',';
                i3++;
            }
            Integer num = list.get(i4);
            if (num == null) {
                cArr[i3] = 'n';
                cArr[i3 + 1] = 'u';
                cArr[i3 + 2] = 'l';
                cArr[i3 + 3] = 'l';
                i3 += 4;
            } else {
                int iIntValue = num.intValue();
                if (z) {
                    cArr[i3] = this.quote;
                    i3++;
                }
                int iWriteInt32 = IOUtils.writeInt32(cArr, i3, iIntValue);
                if (z) {
                    i3 = iWriteInt32 + 1;
                    cArr[iWriteInt32] = this.quote;
                } else {
                    i3 = iWriteInt32;
                }
            }
        }
        cArr[i3] = ']';
        this.off = i3 + 1;
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
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        int iWriteInt64 = i + 1;
        cArrGrow[i] = '[';
        for (int i3 = 0; i3 < size; i3++) {
            if (i3 != 0) {
                cArrGrow[iWriteInt64] = ',';
                iWriteInt64++;
            }
            Long l = list.get(i3);
            if (l == null) {
                cArrGrow[iWriteInt64] = 'n';
                cArrGrow[iWriteInt64 + 1] = 'u';
                cArrGrow[iWriteInt64 + 2] = 'l';
                cArrGrow[iWriteInt64 + 3] = 'l';
                iWriteInt64 += 4;
            } else {
                long jLongValue = l.longValue();
                boolean zIsWriteAsString = isWriteAsString(jLongValue, this.context.features);
                if (zIsWriteAsString) {
                    cArrGrow[iWriteInt64] = this.quote;
                    iWriteInt64++;
                }
                iWriteInt64 = IOUtils.writeInt64(cArrGrow, iWriteInt64, jLongValue);
                if (zIsWriteAsString) {
                    cArrGrow[iWriteInt64] = this.quote;
                    iWriteInt64++;
                }
            }
        }
        cArrGrow[iWriteInt64] = ']';
        this.off = iWriteInt64 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt64(long j) {
        int i;
        long j2 = this.context.features;
        int i2 = this.off;
        int i3 = i2 + 23;
        char[] cArrGrow = this.chars;
        if (i3 > cArrGrow.length) {
            cArrGrow = grow(i3);
        }
        boolean zIsWriteAsString = isWriteAsString(j, j2);
        if (zIsWriteAsString) {
            cArrGrow[i2] = this.quote;
            i2++;
        }
        int iWriteInt64 = IOUtils.writeInt64(cArrGrow, i2, j);
        if (zIsWriteAsString) {
            i = iWriteInt64 + 1;
            cArrGrow[iWriteInt64] = this.quote;
        } else {
            if ((512 & j2) != 0 && (j2 & 1099511627776L) == 0 && j >= -2147483648L && j <= 2147483647L) {
                i = iWriteInt64 + 1;
                cArrGrow[iWriteInt64] = 'L';
            }
            this.off = iWriteInt64;
        }
        iWriteInt64 = i;
        this.off = iWriteInt64;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeInt64(Long l) {
        if (l == null) {
            writeInt64Null();
        } else {
            writeInt64(l.longValue());
        }
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeFloat(float f) {
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int i2 = i + 15;
        if (z) {
            i2 = i + 17;
        }
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        if (z) {
            cArrGrow[i] = '\"';
            i++;
        }
        int iWriteFloat = NumberUtils.writeFloat(cArrGrow, i, f, true);
        if (z) {
            cArrGrow[iWriteFloat] = '\"';
            iWriteFloat++;
        }
        this.off = iWriteFloat;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeFloat(float[] fArr) {
        if (fArr == null) {
            writeArrayNull();
            return;
        }
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int length = (fArr.length * (z ? 16 : 18)) + i + 1;
        char[] cArrGrow = this.chars;
        if (length > cArrGrow.length) {
            cArrGrow = grow(length);
        }
        int iWriteFloat = i + 1;
        cArrGrow[i] = '[';
        for (int i2 = 0; i2 < fArr.length; i2++) {
            if (i2 != 0) {
                cArrGrow[iWriteFloat] = ',';
                iWriteFloat++;
            }
            if (z) {
                cArrGrow[iWriteFloat] = '\"';
                iWriteFloat++;
            }
            iWriteFloat = NumberUtils.writeFloat(cArrGrow, iWriteFloat, fArr[i2], true);
            if (z) {
                cArrGrow[iWriteFloat] = '\"';
                iWriteFloat++;
            }
        }
        cArrGrow[iWriteFloat] = ']';
        this.off = iWriteFloat + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDouble(double d) {
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int i2 = i + 24;
        if (z) {
            i2 = i + 26;
        }
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        if (z) {
            cArrGrow[i] = '\"';
            i++;
        }
        int iWriteDouble = NumberUtils.writeDouble(cArrGrow, i, d, true);
        if (z) {
            cArrGrow[iWriteDouble] = '\"';
            iWriteDouble++;
        }
        this.off = iWriteDouble;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDoubleArray(double d, double d2) {
        boolean z = (this.context.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0;
        int i = this.off;
        int i2 = i + 51;
        if (z) {
            i2 = i + 53;
        }
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        int i3 = i + 1;
        cArrGrow[i] = '[';
        if (z) {
            cArrGrow[i3] = '\"';
            i3 = i + 2;
        }
        int iWriteDouble = NumberUtils.writeDouble(cArrGrow, i3, d, true);
        if (z) {
            cArrGrow[iWriteDouble] = '\"';
            iWriteDouble++;
        }
        int i4 = iWriteDouble + 1;
        cArrGrow[iWriteDouble] = ',';
        if (z) {
            cArrGrow[i4] = '\"';
            i4 = iWriteDouble + 2;
        }
        int iWriteDouble2 = NumberUtils.writeDouble(cArrGrow, i4, d2, true);
        if (z) {
            cArrGrow[iWriteDouble2] = '\"';
            iWriteDouble2++;
        }
        cArrGrow[iWriteDouble2] = ']';
        this.off = iWriteDouble2 + 1;
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
        char[] cArrGrow = this.chars;
        if (length > cArrGrow.length) {
            cArrGrow = grow(length);
        }
        int iWriteDouble = i + 1;
        cArrGrow[i] = '[';
        for (int i2 = 0; i2 < dArr.length; i2++) {
            if (i2 != 0) {
                cArrGrow[iWriteDouble] = ',';
                iWriteDouble++;
            }
            if (z) {
                cArrGrow[iWriteDouble] = '\"';
                iWriteDouble++;
            }
            iWriteDouble = NumberUtils.writeDouble(cArrGrow, iWriteDouble, dArr[i2], true);
            if (z) {
                cArrGrow[iWriteDouble] = '\"';
                iWriteDouble++;
            }
        }
        cArrGrow[iWriteDouble] = ']';
        this.off = iWriteDouble + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDateTime14(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = this.off;
        int i8 = i7 + 16;
        char[] cArrGrow = this.chars;
        if (i8 > cArrGrow.length) {
            cArrGrow = grow(i8);
        }
        cArrGrow[i7] = this.quote;
        if (i < 0 || i > 9999) {
            throw illegalYear(i);
        }
        int i9 = i / 100;
        IOUtils.writeDigitPair(cArrGrow, i7 + 1, i9);
        IOUtils.writeDigitPair(cArrGrow, i7 + 3, i - (i9 * 100));
        IOUtils.writeDigitPair(cArrGrow, i7 + 5, i2);
        IOUtils.writeDigitPair(cArrGrow, i7 + 7, i3);
        IOUtils.writeDigitPair(cArrGrow, i7 + 9, i4);
        IOUtils.writeDigitPair(cArrGrow, i7 + 11, i5);
        IOUtils.writeDigitPair(cArrGrow, i7 + 13, i6);
        cArrGrow[i7 + 15] = this.quote;
        this.off = i8;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDateTime19(int i, int i2, int i3, int i4, int i5, int i6) {
        char[] cArrGrow = this.chars;
        if (this.off + 21 > cArrGrow.length) {
            cArrGrow = grow(this.off + 21);
        }
        int i7 = this.off;
        cArrGrow[i7] = this.quote;
        if (i < 0 || i > 9999) {
            throw illegalYear(i);
        }
        int iWriteLocalDate = IOUtils.writeLocalDate(cArrGrow, i7 + 1, i, i2, i3);
        cArrGrow[iWriteLocalDate] = TokenParser.SP;
        IOUtils.writeLocalTime(cArrGrow, iWriteLocalDate + 1, i4, i5, i6);
        cArrGrow[iWriteLocalDate + 9] = this.quote;
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
            char[] cArrGrow = this.chars;
            if (i2 > cArrGrow.length) {
                cArrGrow = grow(i2);
            }
            cArrGrow[i] = this.quote;
            int iWriteLocalDate = IOUtils.writeLocalDate(cArrGrow, i + 1, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
            cArrGrow[iWriteLocalDate] = this.quote;
            this.off = iWriteLocalDate + 1;
        }
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeLocalDateTime(LocalDateTime localDateTime) {
        int i = this.off;
        int i2 = i + 38;
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        int i3 = i + 1;
        cArrGrow[i] = this.quote;
        LocalDate localDate = localDateTime.toLocalDate();
        int iWriteLocalDate = IOUtils.writeLocalDate(cArrGrow, i3, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        cArrGrow[iWriteLocalDate] = TokenParser.SP;
        int iWriteLocalTime = IOUtils.writeLocalTime(cArrGrow, iWriteLocalDate + 1, localDateTime.toLocalTime());
        cArrGrow[iWriteLocalTime] = this.quote;
        this.off = iWriteLocalTime + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDateTimeISO8601(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        int i9 = z ? i8 == 0 ? 1 : 6 : 0;
        int i10 = this.off;
        int i11 = i10 + 25 + i9;
        char[] cArrGrow = this.chars;
        if (i11 > cArrGrow.length) {
            cArrGrow = grow(i11);
        }
        cArrGrow[i10] = this.quote;
        int iWriteLocalDate = IOUtils.writeLocalDate(cArrGrow, i10 + 1, i, i2, i3);
        cArrGrow[iWriteLocalDate] = z ? 'T' : TokenParser.SP;
        IOUtils.writeLocalTime(cArrGrow, iWriteLocalDate + 1, i4, i5, i6);
        int i12 = iWriteLocalDate + 9;
        if (i7 > 0) {
            int i13 = i7 / 10;
            int i14 = i13 / 10;
            if (i7 - (i13 * 10) != 0) {
                IOUtils.putLongLE(cArrGrow, i12, (IOUtils.DIGITS_K_64[i7 & 1023] & (-65536)) | IOUtils.DOT_X0);
                i12 = iWriteLocalDate + 13;
            } else {
                int i15 = iWriteLocalDate + 10;
                cArrGrow[i12] = '.';
                if (i13 - (i14 * 10) != 0) {
                    IOUtils.writeDigitPair(cArrGrow, i15, i13);
                    i12 = iWriteLocalDate + 12;
                } else {
                    i12 = iWriteLocalDate + 11;
                    cArrGrow[i15] = (char) ((byte) (i14 + 48));
                }
            }
        }
        if (z) {
            int i16 = i8 / 3600;
            if (i8 == 0) {
                cArrGrow[i12] = 'Z';
                i12++;
            } else {
                int iAbs = Math.abs(i16);
                cArrGrow[i12] = i16 >= 0 ? '+' : '-';
                IOUtils.writeDigitPair(cArrGrow, i12 + 1, iAbs);
                cArrGrow[i12 + 3] = ':';
                int i17 = (i8 - (i16 * 3600)) / 60;
                if (i17 < 0) {
                    i17 = -i17;
                }
                IOUtils.writeDigitPair(cArrGrow, i12 + 4, i17);
                i12 += 6;
            }
        }
        cArrGrow[i12] = this.quote;
        this.off = i12 + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDateYYYMMDD8(int i, int i2, int i3) {
        int i4 = this.off;
        int i5 = i4 + 10;
        char[] cArrGrow = this.chars;
        if (i5 > cArrGrow.length) {
            cArrGrow = grow(i5);
        }
        cArrGrow[i4] = this.quote;
        if (i < 0 || i > 9999) {
            throw illegalYear(i);
        }
        int i6 = i / 100;
        IOUtils.writeDigitPair(cArrGrow, i4 + 1, i6);
        IOUtils.writeDigitPair(cArrGrow, i4 + 3, i - (i6 * 100));
        IOUtils.writeDigitPair(cArrGrow, i4 + 5, i2);
        IOUtils.writeDigitPair(cArrGrow, i4 + 7, i3);
        cArrGrow[i4 + 9] = this.quote;
        this.off = i5;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeDateYYYMMDD10(int i, int i2, int i3) {
        int i4 = this.off;
        int i5 = i4 + 13;
        char[] cArrGrow = this.chars;
        if (i5 > cArrGrow.length) {
            cArrGrow = grow(i5);
        }
        cArrGrow[i4] = this.quote;
        int iWriteLocalDate = IOUtils.writeLocalDate(cArrGrow, i4 + 1, i, i2, i3);
        cArrGrow[iWriteLocalDate] = this.quote;
        this.off = iWriteLocalDate + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeTimeHHMMSS8(int i, int i2, int i3) {
        int i4 = this.off;
        int i5 = i4 + 10;
        char[] cArrGrow = this.chars;
        if (i5 > cArrGrow.length) {
            cArrGrow = grow(i5);
        }
        cArrGrow[i4] = (char) ((byte) this.quote);
        IOUtils.writeDigitPair(cArrGrow, i4 + 1, i);
        cArrGrow[i4 + 3] = ':';
        IOUtils.writeDigitPair(cArrGrow, i4 + 4, i2);
        cArrGrow[i4 + 6] = ':';
        IOUtils.writeDigitPair(cArrGrow, i4 + 7, i3);
        cArrGrow[i4 + 9] = (char) ((byte) this.quote);
        this.off = i5;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeLocalTime(LocalTime localTime) {
        int i = this.off;
        int i2 = i + 20;
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        cArrGrow[i] = this.quote;
        int iWriteLocalTime = IOUtils.writeLocalTime(cArrGrow, i + 1, localTime);
        cArrGrow[iWriteLocalTime] = this.quote;
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
        char[] cArrGrow = this.chars;
        if (i4 > cArrGrow.length) {
            cArrGrow = grow(i4);
        }
        cArrGrow[i3] = this.quote;
        LocalDate localDate = zonedDateTime.toLocalDate();
        int iWriteLocalDate = IOUtils.writeLocalDate(cArrGrow, i3 + 1, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        cArrGrow[iWriteLocalDate] = 'T';
        int iWriteLocalTime = IOUtils.writeLocalTime(cArrGrow, iWriteLocalDate + 1, zonedDateTime.toLocalTime());
        if (i == 1) {
            i2 = iWriteLocalTime + 1;
            cArrGrow[iWriteLocalTime] = 'Z';
        } else if (cCharAt == '+' || cCharAt == '-') {
            id.getChars(0, length, cArrGrow, iWriteLocalTime);
            i2 = iWriteLocalTime + length;
        } else {
            int i5 = iWriteLocalTime + 1;
            cArrGrow[iWriteLocalTime] = '[';
            id.getChars(0, length, cArrGrow, i5);
            int i6 = i5 + length;
            cArrGrow[i6] = ']';
            i2 = i6 + 1;
        }
        cArrGrow[i2] = this.quote;
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
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        cArrGrow[i] = this.quote;
        LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        int iWriteLocalDate = IOUtils.writeLocalDate(cArrGrow, i + 1, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        cArrGrow[iWriteLocalDate] = 'T';
        int iWriteLocalTime = IOUtils.writeLocalTime(cArrGrow, iWriteLocalDate + 1, localDateTime.toLocalTime());
        ZoneOffset offset = offsetDateTime.getOffset();
        if (offset.getTotalSeconds() == 0) {
            length = iWriteLocalTime + 1;
            cArrGrow[iWriteLocalTime] = 'Z';
        } else {
            String id = offset.getId();
            id.getChars(0, id.length(), cArrGrow, iWriteLocalTime);
            length = id.length() + iWriteLocalTime;
        }
        cArrGrow[length] = this.quote;
        this.off = length + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeOffsetTime(OffsetTime offsetTime) {
        int length;
        if (offsetTime == null) {
            writeNull();
            return;
        }
        ZoneOffset offset = offsetTime.getOffset();
        int i = this.off;
        int i2 = i + 28;
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        cArrGrow[i] = this.quote;
        int iWriteLocalTime = IOUtils.writeLocalTime(cArrGrow, i + 1, offsetTime.toLocalTime());
        if (offset.getTotalSeconds() == 0) {
            length = iWriteLocalTime + 1;
            cArrGrow[iWriteLocalTime] = 'Z';
        } else {
            String id = offset.getId();
            id.getChars(0, id.length(), cArrGrow, iWriteLocalTime);
            length = id.length() + iWriteLocalTime;
        }
        cArrGrow[length] = this.quote;
        this.off = length + 1;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeNameRaw(byte[] bArr) {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final int flushTo(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new JSONException("out is nulll");
        }
        for (int i = 0; i < this.off; i++) {
            if (this.chars[i] >= 128) {
                byte[] bArr = new byte[this.off * 3];
                int iEncodeUTF8 = IOUtils.encodeUTF8(this.chars, 0, this.off, bArr, 0);
                outputStream.write(bArr, 0, iEncodeUTF8);
                this.off = 0;
                return iEncodeUTF8;
            }
        }
        int i2 = this.off;
        byte[] bArr2 = new byte[i2];
        for (int i3 = 0; i3 < this.off; i3++) {
            bArr2[i3] = (byte) this.chars[i3];
        }
        outputStream.write(bArr2);
        this.off = 0;
        return i2;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final int flushTo(OutputStream outputStream, Charset charset) throws IOException {
        if (this.off == 0) {
            return 0;
        }
        if (outputStream == null) {
            throw new JSONException("out is null");
        }
        byte[] bytes = getBytes(charset);
        outputStream.write(bytes);
        this.off = 0;
        return bytes.length;
    }

    public final String toString() {
        return new String(this.chars, 0, this.off);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final byte[] getBytes() {
        for (int i = 0; i < this.off; i++) {
            if (this.chars[i] >= 128) {
                byte[] bArr = new byte[this.off * 3];
                return Arrays.copyOf(bArr, IOUtils.encodeUTF8(this.chars, 0, this.off, bArr, 0));
            }
        }
        byte[] bArr2 = new byte[this.off];
        for (int i2 = 0; i2 < this.off; i2++) {
            bArr2[i2] = (byte) this.chars[i2];
        }
        return bArr2;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final int size() {
        return this.off;
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final byte[] getBytes(Charset charset) {
        int i = 0;
        while (true) {
            if (i < this.off) {
                if (this.chars[i] >= 128) {
                    break;
                }
                i++;
            } else if (charset == StandardCharsets.UTF_8 || charset == StandardCharsets.ISO_8859_1 || charset == StandardCharsets.US_ASCII) {
                byte[] bArr = new byte[this.off];
                for (int i2 = 0; i2 < this.off; i2++) {
                    bArr[i2] = (byte) this.chars[i2];
                }
                return bArr;
            }
        }
        String str = new String(this.chars, 0, this.off);
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        return str.getBytes(charset);
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeRaw(byte[] bArr) {
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
        if ((this.context.features & NONE_DIRECT_FEATURES) != 0) {
            this.context.getObjectWriter(map.getClass()).write(this, map, null, null, 0L);
            return;
        }
        writeRaw('{');
        boolean z = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value != null || (this.context.features & JSONWriter.Feature.WriteMapNullValue.mask) != 0) {
                if (!z) {
                    writeRaw(',');
                }
                Object key = entry.getKey();
                if (key instanceof String) {
                    writeString((String) key);
                } else {
                    writeAny(key);
                }
                writeRaw(':');
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
        writeRaw('}');
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void write(List list) {
        if (list == null) {
            writeArrayNull();
            return;
        }
        if (((JSONWriter.Feature.ReferenceDetection.mask | JSONWriter.Feature.PrettyFormat.mask | JSONWriter.Feature.NotWriteEmptyArray.mask | JSONWriter.Feature.NotWriteDefaultValue.mask) & this.context.features) != 0) {
            this.context.getObjectWriter(list.getClass()).write(this, list, null, null, 0L);
            return;
        }
        if (this.off == this.chars.length) {
            grow0(this.off + 1);
        }
        char[] cArr = this.chars;
        int i = this.off;
        this.off = i + 1;
        cArr[i] = '[';
        boolean z = true;
        int i2 = 0;
        while (i2 < list.size()) {
            Object obj = list.get(i2);
            if (!z) {
                if (this.off == this.chars.length) {
                    grow(this.off + 1);
                }
                char[] cArr2 = this.chars;
                int i3 = this.off;
                this.off = i3 + 1;
                cArr2[i3] = ',';
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
        if (this.off == this.chars.length) {
            grow(this.off + 1);
        }
        char[] cArr3 = this.chars;
        int i4 = this.off;
        this.off = i4 + 1;
        cArr3[i4] = ']';
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(boolean z) {
        char[] cArr = this.chars;
        int i = this.off;
        this.off = i + 1;
        cArr[i] = this.quote;
        writeBool(z);
        char[] cArr2 = this.chars;
        int i2 = this.off;
        this.off = i2 + 1;
        cArr2[i2] = this.quote;
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
        if (this.off == this.chars.length) {
            grow(this.off + 1);
        }
        char[] cArr = this.chars;
        int i = this.off;
        this.off = i + 1;
        cArr[i] = this.quote;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0073  */
    @Override // com.alibaba.fastjson2.JSONWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void writeString(char[] r20) {
        /*
            r19 = this;
            r0 = r19
            r1 = r20
            if (r1 != 0) goto La
            r0.writeStringNull()
            return
        La:
            com.alibaba.fastjson2.JSONWriter$Context r2 = r0.context
            long r2 = r2.features
            com.alibaba.fastjson2.JSONWriter$Feature r4 = com.alibaba.fastjson2.JSONWriter.Feature.BrowserSecure
            long r4 = r4.mask
            com.alibaba.fastjson2.JSONWriter$Feature r6 = com.alibaba.fastjson2.JSONWriter.Feature.EscapeNoneAscii
            long r6 = r6.mask
            long r4 = r4 | r6
            long r2 = r2 & r4
            r4 = 0
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L22
            r19.writeStringBrowserSecure(r20)
            return
        L22:
            int r2 = r0.off
            int r3 = r1.length
            int r3 = r3 + r2
            int r3 = r3 + 2
            char[] r6 = r0.chars
            int r6 = r6.length
            if (r3 < r6) goto L30
            r0.grow(r3)
        L30:
            long r6 = r0.byteVectorQuote
            char[] r3 = r0.chars
            int r8 = r2 + 1
            char r9 = r0.quote
            r3[r2] = r9
            int r2 = r1.length
            r9 = 0
        L3c:
            if (r9 >= r2) goto L91
            int r10 = r9 + 8
            if (r10 >= r2) goto L73
            long r11 = com.alibaba.fastjson2.util.IOUtils.getLongLE(r1, r9)
            int r13 = r9 + 4
            long r13 = com.alibaba.fastjson2.util.IOUtils.getLongLE(r1, r13)
            long r15 = r11 | r13
            r17 = -71777214294589696(0xff00ff00ff00ff00, double:-5.82767264895205E303)
            long r15 = r15 & r17
            int r15 = (r15 > r4 ? 1 : (r15 == r4 ? 0 : -1))
            if (r15 != 0) goto L73
            r15 = 8
            long r15 = r11 << r15
            long r4 = r15 | r13
            boolean r4 = com.alibaba.fastjson2.util.StringUtils.noneEscaped(r4, r6)
            if (r4 == 0) goto L73
            com.alibaba.fastjson2.util.IOUtils.putLongLE(r3, r8, r11)
            int r4 = r8 + 4
            com.alibaba.fastjson2.util.IOUtils.putLongLE(r3, r4, r13)
            int r8 = r8 + 8
            r9 = r10
        L70:
            r4 = 0
            goto L3c
        L73:
            int r4 = r9 + 1
            char r5 = com.alibaba.fastjson2.util.IOUtils.getChar(r1, r9)
            r9 = 92
            if (r5 == r9) goto L8d
            char r9 = r0.quote
            if (r5 == r9) goto L8d
            r9 = 32
            if (r5 >= r9) goto L86
            goto L8d
        L86:
            int r9 = r8 + 1
            r3[r8] = r5
            r8 = r9
            r9 = r4
            goto L70
        L8d:
            r19.writeStringEscape(r20)
            return
        L91:
            char r1 = r0.quote
            r3[r8] = r1
            int r8 = r8 + 1
            r0.off = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriterUTF16.writeString(char[]):void");
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public final void writeString(char[] cArr, int i, int i2) {
        if (cArr == null) {
            writeStringNull();
            return;
        }
        boolean z = (this.context.features & JSONWriter.Feature.EscapeNoneAscii.mask) != 0;
        for (int i3 = i; i3 < i2; i3++) {
            char c = cArr[i3];
            if (c == '\\' || c == this.quote || c < ' ') {
                z = true;
                break;
            }
        }
        if (!z) {
            int i4 = this.off;
            int i5 = i4 + i2 + 2;
            char[] cArrGrow = this.chars;
            if (i5 > cArrGrow.length) {
                cArrGrow = grow(i5);
            }
            int i6 = i4 + 1;
            cArrGrow[i4] = this.quote;
            System.arraycopy(cArr, i, cArrGrow, i6, i2);
            int i7 = i6 + i2;
            cArrGrow[i7] = this.quote;
            this.off = i7 + 1;
            return;
        }
        writeStringEscape(new String(cArr, i, i2));
    }

    @Override // com.alibaba.fastjson2.JSONWriter
    public void writeBool(boolean z) {
        int i;
        int i2 = this.off + 5;
        char[] cArrGrow = this.chars;
        if (i2 > cArrGrow.length) {
            cArrGrow = grow(i2);
        }
        int i3 = this.off;
        if ((this.context.features & JSONWriter.Feature.WriteBooleanAsNumber.mask) != 0) {
            i = i3 + 1;
            cArrGrow[i3] = z ? '1' : '0';
        } else if (!z) {
            cArrGrow[i3] = 'f';
            cArrGrow[i3 + 1] = 'a';
            cArrGrow[i3 + 2] = 'l';
            cArrGrow[i3 + 3] = 's';
            cArrGrow[i3 + 4] = 'e';
            i = i3 + 5;
        } else {
            cArrGrow[i3] = 't';
            cArrGrow[i3 + 1] = 'r';
            cArrGrow[i3 + 2] = 'u';
            cArrGrow[i3 + 3] = 'e';
            i = i3 + 4;
        }
        this.off = i;
    }
}
