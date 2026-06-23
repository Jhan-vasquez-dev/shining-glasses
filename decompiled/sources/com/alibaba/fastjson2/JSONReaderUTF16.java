package com.alibaba.fastjson2;

import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import kotlin.UByte;
import okhttp3.internal.ws.WebSocketProtocol;
import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
final class JSONReaderUTF16 extends JSONReader {
    static final long CHAR_MASK;
    private int cacheIndex;
    protected final char[] chars;
    protected final int end;
    private Closeable input;
    protected final int length;
    private int nameBegin;
    private int nameEnd;
    private int nameLength;
    private int referenceBegin;
    protected final int start;
    protected final String str;

    static {
        CHAR_MASK = JDKUtils.BIG_ENDIAN ? 71777214294589695L : -71777214294589696L;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    JSONReaderUTF16(JSONReader.Context context, byte[] bArr, int i, int i2) {
        super(context, false, false);
        int i3 = 0;
        this.cacheIndex = -1;
        this.str = null;
        this.chars = new char[i2 / 2];
        int i4 = i + i2;
        int i5 = i;
        while (i5 < i4) {
            this.chars[i3] = (char) (((bArr[i5] & UByte.MAX_VALUE) << 8) | (bArr[i5 + 1] & UByte.MAX_VALUE));
            i5 += 2;
            i3++;
        }
        this.start = i;
        this.length = i3;
        this.end = i3;
        if (this.offset >= i3) {
            this.ch = JSONLexer.EOI;
            return;
        }
        this.ch = this.chars[this.offset];
        while (this.ch <= ' ' && ((1 << this.ch) & 4294981376L) != 0) {
            this.offset++;
            if (this.offset >= i2) {
                this.ch = JSONLexer.EOI;
                return;
            }
            this.ch = this.chars[this.offset];
        }
        while (this.ch <= ' ' && ((1 << this.ch) & 4294981376L) != 0) {
            this.offset++;
            if (this.offset >= i2) {
                this.ch = JSONLexer.EOI;
                return;
            }
            this.ch = this.chars[this.offset];
        }
        this.offset++;
        if (this.ch == 65534 || this.ch == 65279) {
            next();
        }
        if (this.ch == '/') {
            skipComment();
        }
    }

    JSONReaderUTF16(JSONReader.Context context, Reader reader) {
        super(context, false, false);
        this.cacheIndex = -1;
        this.input = reader;
        this.cacheIndex = System.identityHashCode(Thread.currentThread()) & (JSONFactory.CACHE_ITEMS.length - 1);
        char[] andSet = JSONFactory.CHARS_UPDATER.getAndSet(JSONFactory.CACHE_ITEMS[this.cacheIndex], null);
        andSet = andSet == null ? new char[8192] : andSet;
        int i = 0;
        while (true) {
            try {
                int i2 = reader.read(andSet, i, andSet.length - i);
                if (i2 == -1) {
                    break;
                }
                i += i2;
                if (i == andSet.length) {
                    int length = andSet.length;
                    andSet = Arrays.copyOf(andSet, length + (length >> 1));
                }
            } catch (IOException e) {
                throw new JSONException("read error", e);
            }
        }
        this.str = null;
        this.chars = andSet;
        this.offset = 0;
        this.length = i;
        this.start = 0;
        this.end = i;
        if (this.offset >= i) {
            this.ch = JSONLexer.EOI;
            return;
        }
        this.ch = andSet[this.offset];
        while (this.ch <= ' ' && ((1 << this.ch) & 4294981376L) != 0) {
            this.offset++;
            if (this.offset >= this.length) {
                this.ch = JSONLexer.EOI;
                return;
            }
            this.ch = andSet[this.offset];
        }
        this.offset++;
        if (this.ch == 65534 || this.ch == 65279) {
            next();
        }
        if (this.ch == '/') {
            skipComment();
        }
    }

    JSONReaderUTF16(JSONReader.Context context, String str, int i, int i2) {
        super(context, false, false);
        this.cacheIndex = -1;
        this.cacheIndex = System.identityHashCode(Thread.currentThread()) & (JSONFactory.CACHE_ITEMS.length - 1);
        char[] andSet = JSONFactory.CHARS_UPDATER.getAndSet(JSONFactory.CACHE_ITEMS[this.cacheIndex], null);
        andSet = (andSet == null || andSet.length < i2) ? new char[Math.max(i2, 8192)] : andSet;
        str.getChars(i, i + i2, andSet, 0);
        this.str = i != 0 ? null : str;
        this.chars = andSet;
        this.offset = 0;
        this.length = i2;
        this.start = 0;
        this.end = i2;
        if (this.offset >= i2) {
            this.ch = JSONLexer.EOI;
            return;
        }
        this.ch = andSet[this.offset];
        while (this.ch <= ' ' && ((1 << this.ch) & 4294981376L) != 0) {
            this.offset++;
            if (this.offset >= this.length) {
                this.ch = JSONLexer.EOI;
                return;
            }
            this.ch = andSet[this.offset];
        }
        this.offset++;
        if (this.ch == 65534 || this.ch == 65279) {
            next();
        }
        if (this.ch == '/') {
            skipComment();
        }
    }

    JSONReaderUTF16(JSONReader.Context context, String str, char[] cArr, int i, int i2) {
        super(context, false, false);
        this.cacheIndex = -1;
        this.str = str;
        this.chars = cArr;
        this.offset = i;
        this.length = i2;
        this.start = i;
        int i3 = i + i2;
        this.end = i3;
        if (this.offset >= i3) {
            this.ch = JSONLexer.EOI;
            return;
        }
        this.ch = cArr[this.offset];
        while (this.ch <= ' ' && ((1 << this.ch) & 4294981376L) != 0) {
            this.offset++;
            if (this.offset >= i2) {
                this.ch = JSONLexer.EOI;
                return;
            }
            this.ch = cArr[this.offset];
        }
        this.offset++;
        if (this.ch == 65534 || this.ch == 65279) {
            next();
        }
        if (this.ch == '/') {
            skipComment();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    JSONReaderUTF16(JSONReader.Context context, InputStream inputStream) {
        super(context, false, false);
        int i = 0;
        this.cacheIndex = -1;
        this.input = inputStream;
        JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & (JSONFactory.CACHE_ITEMS.length - 1)];
        byte[] andSet = JSONFactory.BYTES_UPDATER.getAndSet(cacheItem, null);
        int i2 = context.bufferSize;
        andSet = andSet == null ? new byte[i2] : andSet;
        int i3 = 0;
        while (true) {
            try {
                try {
                    int i4 = inputStream.read(andSet, i3, andSet.length - i3);
                    if (i4 == -1) {
                        break;
                    }
                    i3 += i4;
                    if (i3 == andSet.length) {
                        andSet = Arrays.copyOf(andSet, andSet.length + i2);
                    }
                } catch (IOException e) {
                    throw new JSONException("read error", e);
                }
            } catch (Throwable th) {
                JSONFactory.BYTES_UPDATER.lazySet(cacheItem, andSet);
                throw th;
            }
        }
        if (i3 % 2 == 1) {
            throw new JSONException("illegal input utf16 bytes, length " + i3);
        }
        int i5 = i3 / 2;
        char[] cArr = new char[i5];
        int i6 = 0;
        int i7 = 0;
        while (i6 < i3) {
            cArr[i7] = (char) (((andSet[i6] & UByte.MAX_VALUE) << 8) | (andSet[i6 + 1] & UByte.MAX_VALUE));
            i6 += 2;
            i7++;
        }
        JSONFactory.BYTES_UPDATER.lazySet(cacheItem, andSet);
        this.str = null;
        this.chars = cArr;
        this.offset = 0;
        this.length = i5;
        this.start = 0;
        this.end = i5;
        if (i5 == 0) {
            this.ch = JSONLexer.EOI;
            return;
        }
        char c = cArr[0];
        while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
            i++;
            if (i >= i5) {
                this.ch = JSONLexer.EOI;
                return;
            }
            c = cArr[i];
        }
        this.ch = c;
        this.offset++;
        if (c == 65534 || c == 65279) {
            next();
        }
        if (this.ch == '/') {
            skipComment();
        }
    }

    /* JADX WARN: Path cross not found for [B:22:0x003c, B:23:0x003e], limit reached: 101 */
    /* JADX WARN: Path cross not found for [B:23:0x003e, B:22:0x003c], limit reached: 101 */
    /* JADX WARN: Removed duplicated region for block: B:22:0x003c A[ADDED_TO_REGION] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x0048 -> B:18:0x0030). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:79:0x00e6 -> B:67:0x00c4). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final byte[] readHex() {
        /*
            Method dump skipped, instruction units count: 258
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readHex():byte[]");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean isReference() {
        int i;
        int i2;
        if ((this.context.features & 8589934592L) != 0) {
            return false;
        }
        char[] cArr = this.chars;
        if (this.ch != '{' || (i = this.offset) == (i2 = this.end)) {
            return false;
        }
        char c = cArr[i];
        while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
            i++;
            if (i >= i2) {
                return false;
            }
            c = cArr[i];
        }
        if (i + 6 < i2 && cArr[i + 1] == '$' && cArr[i + 2] == 'r' && cArr[i + 3] == 'e' && cArr[i + 4] == 'f' && cArr[i + 5] == c) {
            return isReference0(cArr, i, i2, c);
        }
        return false;
    }

    private boolean isReference0(char[] cArr, int i, int i2, char c) {
        int i3;
        int i4;
        char c2;
        int i5 = i + 6;
        char c3 = cArr[i5];
        while (c3 <= ' ' && ((1 << c3) & 4294981376L) != 0) {
            i5++;
            if (i5 >= i2) {
                return false;
            }
            c3 = cArr[i5];
        }
        if (c3 == ':' && (i3 = i5 + 1) < i2) {
            char c4 = cArr[i3];
            while (c4 <= ' ' && ((1 << c4) & 4294981376L) != 0) {
                i3++;
                if (i3 >= i2) {
                    return false;
                }
                c4 = cArr[i3];
            }
            if (c4 == c && ((i4 = i3 + 1) >= i2 || (c2 = cArr[i4]) == '$' || c2 == '.' || c2 == '@')) {
                this.referenceBegin = i3;
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x005c -> B:20:0x004b). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:22:0x0051
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final java.lang.String readReference() {
        /*
            r15 = this;
            int r0 = r15.referenceBegin
            int r1 = r15.end
            if (r0 != r1) goto L8
            r0 = 0
            return r0
        L8:
            char[] r1 = r15.chars
            r15.offset = r0
            int r0 = r15.offset
            int r2 = r0 + 1
            r15.offset = r2
            char r0 = r1[r0]
            r15.ch = r0
            java.lang.String r0 = r15.readString()
            char r2 = r15.ch
            int r3 = r15.offset
        L1e:
            r4 = 0
            r6 = 4294981376(0x100003700, double:2.1220027474E-314)
            r8 = 1
            r10 = 32
            r11 = 26
            if (r2 > r10) goto L42
            long r12 = r8 << r2
            long r12 = r12 & r6
            int r12 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1))
            if (r12 == 0) goto L42
            int r2 = r15.end
            if (r3 != r2) goto L3a
            r2 = r11
            goto L1e
        L3a:
            int r2 = r3 + 1
            char r3 = r1[r3]
            r14 = r3
            r3 = r2
            r2 = r14
            goto L1e
        L42:
            r12 = 125(0x7d, float:1.75E-43)
            if (r2 != r12) goto L99
            int r2 = r15.end
            if (r3 != r2) goto L4d
            r2 = r3
        L4b:
            r3 = r11
            goto L51
        L4d:
            int r2 = r3 + 1
            char r3 = r1[r3]
        L51:
            if (r3 > r10) goto L67
            long r12 = r8 << r3
            long r12 = r12 & r6
            int r12 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1))
            if (r12 == 0) goto L67
            int r3 = r15.end
            if (r2 != r3) goto L5f
            goto L4b
        L5f:
            int r3 = r2 + 1
            char r2 = r1[r2]
            r14 = r3
            r3 = r2
            r2 = r14
            goto L51
        L67:
            r12 = 44
            if (r3 != r12) goto L6d
            r12 = 1
            goto L6e
        L6d:
            r12 = 0
        L6e:
            r15.comma = r12
            if (r12 == 0) goto L94
            int r3 = r15.end
            if (r2 != r3) goto L79
            r3 = r2
            r2 = r11
            goto L7d
        L79:
            int r3 = r2 + 1
            char r2 = r1[r2]
        L7d:
            r14 = r3
            r3 = r2
            r2 = r14
        L80:
            if (r3 > r10) goto L94
            long r12 = r8 << r3
            long r12 = r12 & r6
            int r12 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1))
            if (r12 == 0) goto L94
            int r3 = r15.end
            if (r2 != r3) goto L8f
            r3 = r11
            goto L80
        L8f:
            int r3 = r2 + 1
            char r2 = r1[r2]
            goto L7d
        L94:
            r15.ch = r3
            r15.offset = r2
            return r0
        L99:
            com.alibaba.fastjson2.JSONException r1 = new com.alibaba.fastjson2.JSONException
            java.lang.String r2 = "illegal reference : "
            java.lang.String r0 = r2.concat(r0)
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readReference():java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0057 A[LOOP:1: B:19:0x0039->B:32:0x0057, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0033 A[EDGE_INSN: B:42:0x0033->B:17:0x0033 BREAK  A[LOOP:1: B:19:0x0039->B:32:0x0057], SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:30:0x0054 -> B:17:0x0033). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean nextIfMatch(char r15) {
        /*
            r14 = this;
            char[] r0 = r14.chars
            int r1 = r14.offset
            char r2 = r14.ch
        L6:
            r3 = 0
            r5 = 4294981376(0x100003700, double:2.1220027474E-314)
            r7 = 1
            r9 = 32
            r10 = 26
            if (r2 > r9) goto L2a
            long r11 = r7 << r2
            long r11 = r11 & r5
            int r11 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r11 == 0) goto L2a
            int r2 = r14.end
            if (r1 != r2) goto L22
            r2 = r10
            goto L6
        L22:
            int r2 = r1 + 1
            char r1 = r0[r1]
            r13 = r2
            r2 = r1
            r1 = r13
            goto L6
        L2a:
            if (r2 == r15) goto L2e
            r15 = 0
            return r15
        L2e:
            int r15 = r14.end
            if (r1 != r15) goto L35
            r15 = r1
        L33:
            r1 = r10
            goto L39
        L35:
            int r15 = r1 + 1
            char r1 = r0[r1]
        L39:
            if (r1 == 0) goto L52
            if (r1 > r9) goto L45
            long r11 = r7 << r1
            long r11 = r11 & r5
            int r2 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r2 == 0) goto L45
            goto L52
        L45:
            r14.offset = r15
            r14.ch = r1
            r15 = 47
            if (r1 != r15) goto L50
            r14.skipComment()
        L50:
            r15 = 1
            return r15
        L52:
            int r1 = r14.end
            if (r15 != r1) goto L57
            goto L33
        L57:
            int r1 = r15 + 1
            char r15 = r0[r15]
            r13 = r1
            r1 = r15
            r15 = r13
            goto L39
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfMatch(char):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0041 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x005d A[LOOP:1: B:20:0x003f->B:33:0x005d, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0039 A[EDGE_INSN: B:43:0x0039->B:18:0x0039 BREAK  A[LOOP:1: B:20:0x003f->B:33:0x005d], SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:31:0x005a -> B:18:0x0039). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean nextIfComma() {
        /*
            r14 = this;
            char[] r0 = r14.chars
            int r1 = r14.offset
            char r2 = r14.ch
        L6:
            r3 = 0
            r5 = 4294981376(0x100003700, double:2.1220027474E-314)
            r7 = 1
            r9 = 32
            r10 = 26
            if (r2 > r9) goto L2a
            long r11 = r7 << r2
            long r11 = r11 & r5
            int r11 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r11 == 0) goto L2a
            int r2 = r14.end
            if (r1 != r2) goto L22
            r2 = r10
            goto L6
        L22:
            int r2 = r1 + 1
            char r1 = r0[r1]
            r13 = r2
            r2 = r1
            r1 = r13
            goto L6
        L2a:
            r11 = 44
            if (r2 == r11) goto L34
            r14.offset = r1
            r14.ch = r2
            r0 = 0
            return r0
        L34:
            int r2 = r14.end
            if (r1 != r2) goto L3b
            r2 = r1
        L39:
            r1 = r10
            goto L3f
        L3b:
            int r2 = r1 + 1
            char r1 = r0[r1]
        L3f:
            if (r1 == 0) goto L58
            if (r1 > r9) goto L4b
            long r11 = r7 << r1
            long r11 = r11 & r5
            int r11 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r11 == 0) goto L4b
            goto L58
        L4b:
            r14.offset = r2
            r14.ch = r1
            r0 = 47
            if (r1 != r0) goto L56
            r14.skipComment()
        L56:
            r0 = 1
            return r0
        L58:
            int r1 = r14.end
            if (r2 != r1) goto L5d
            goto L39
        L5d:
            int r1 = r2 + 1
            char r2 = r0[r2]
            r13 = r2
            r2 = r1
            r1 = r13
            goto L3f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfComma():boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0041 A[LOOP:0: B:11:0x0019->B:25:0x0041, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0013 A[EDGE_INSN: B:26:0x0013->B:9:0x0013 BREAK  A[LOOP:0: B:11:0x0019->B:25:0x0041], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x003e -> B:9:0x0013). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean nextIfArrayStart() {
        /*
            r9 = this;
            char r0 = r9.ch
            r1 = 91
            if (r0 == r1) goto L8
            r0 = 0
            return r0
        L8:
            char[] r0 = r9.chars
            int r1 = r9.offset
            int r2 = r9.end
            r3 = 26
            if (r1 != r2) goto L15
            r2 = r1
        L13:
            r1 = r3
            goto L19
        L15:
            int r2 = r1 + 1
            char r1 = r0[r1]
        L19:
            if (r1 == 0) goto L3c
            r4 = 32
            if (r1 > r4) goto L2f
            r4 = 1
            long r4 = r4 << r1
            r6 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r4 = r4 & r6
            r6 = 0
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 == 0) goto L2f
            goto L3c
        L2f:
            r9.ch = r1
            r9.offset = r2
            r0 = 47
            if (r1 != r0) goto L3a
            r9.skipComment()
        L3a:
            r0 = 1
            return r0
        L3c:
            int r1 = r9.end
            if (r2 != r1) goto L41
            goto L13
        L41:
            int r1 = r2 + 1
            char r2 = r0[r2]
            r8 = r2
            r2 = r1
            r1 = r8
            goto L19
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfArrayStart():boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0070 A[LOOP:0: B:11:0x001b->B:39:0x0070, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0015 A[EDGE_INSN: B:40:0x0015->B:9:0x0015 BREAK  A[LOOP:0: B:11:0x001b->B:39:0x0070], SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:37:0x006d -> B:9:0x0015). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean nextIfArrayEnd() {
        /*
            r17 = this;
            r0 = r17
            char r1 = r0.ch
            r2 = 93
            if (r1 == r2) goto La
            r1 = 0
            return r1
        La:
            int r1 = r0.offset
            char[] r2 = r0.chars
            int r3 = r0.end
            r4 = 26
            if (r1 != r3) goto L17
            r3 = r1
        L15:
            r1 = r4
            goto L1b
        L17:
            int r3 = r1 + 1
            char r1 = r2[r1]
        L1b:
            if (r1 == 0) goto L6b
            r5 = 0
            r7 = 4294981376(0x100003700, double:2.1220027474E-314)
            r9 = 1
            r11 = 32
            if (r1 > r11) goto L32
            long r12 = r9 << r1
            long r12 = r12 & r7
            int r12 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r12 == 0) goto L32
            goto L6b
        L32:
            r12 = 44
            r13 = 1
            if (r1 != r12) goto L5f
            r0.comma = r13
            int r1 = r0.end
            if (r3 != r1) goto L40
            r1 = r3
            r3 = r4
            goto L44
        L40:
            int r1 = r3 + 1
            char r3 = r2[r3]
        L44:
            r16 = r3
            r3 = r1
            r1 = r16
        L49:
            if (r1 == 0) goto L54
            if (r1 > r11) goto L5f
            long r14 = r9 << r1
            long r14 = r14 & r7
            int r12 = (r14 > r5 ? 1 : (r14 == r5 ? 0 : -1))
            if (r12 == 0) goto L5f
        L54:
            int r1 = r0.end
            if (r3 != r1) goto L5a
            r1 = r4
            goto L49
        L5a:
            int r1 = r3 + 1
            char r3 = r2[r3]
            goto L44
        L5f:
            r0.ch = r1
            r0.offset = r3
            r2 = 47
            if (r1 != r2) goto L6a
            r0.skipComment()
        L6a:
            return r13
        L6b:
            int r1 = r0.end
            if (r3 != r1) goto L70
            goto L15
        L70:
            int r1 = r3 + 1
            char r3 = r2[r3]
            r16 = r3
            r3 = r1
            r1 = r16
            goto L1b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfArrayEnd():boolean");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:39:0x006a -> B:33:0x0050). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:36:0x0061
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfNullOrEmptyString() {
        /*
            r18 = this;
            r0 = r18
            char r1 = r0.ch
            int r2 = r0.end
            int r3 = r0.offset
            char[] r4 = r0.chars
            r5 = 110(0x6e, float:1.54E-43)
            r6 = 0
            r7 = 1
            if (r1 != r5) goto L29
            int r5 = r3 + 2
            if (r5 >= r2) goto L29
            char r8 = r4[r3]
            r9 = 117(0x75, float:1.64E-43)
            if (r8 != r9) goto L29
            int r8 = r3 + 1
            char r8 = r4[r8]
            r9 = 108(0x6c, float:1.51E-43)
            if (r8 != r9) goto L29
            char r5 = r4[r5]
            if (r5 != r9) goto L29
            int r3 = r3 + 3
            goto L4b
        L29:
            r5 = 34
            if (r1 == r5) goto L33
            r5 = 39
            if (r1 != r5) goto L32
            goto L33
        L32:
            return r6
        L33:
            if (r3 >= r2) goto L3b
            char r5 = r4[r3]
            if (r5 != r1) goto L3b
            int r3 = r3 + r7
            goto L4b
        L3b:
            int r5 = r3 + 4
            if (r5 >= r2) goto La3
            boolean r8 = com.alibaba.fastjson2.util.IOUtils.isNULL(r4, r3)
            if (r8 == 0) goto La3
            char r5 = r4[r5]
            if (r5 != r1) goto La3
            int r3 = r3 + 5
        L4b:
            r1 = 26
            if (r3 != r2) goto L52
            r5 = r3
        L50:
            r3 = r1
            goto L56
        L52:
            int r5 = r3 + 1
            char r3 = r4[r3]
        L56:
            r8 = 0
            r10 = 4294981376(0x100003700, double:2.1220027474E-314)
            r12 = 1
            r14 = 32
            if (r3 > r14) goto L77
            long r15 = r12 << r3
            long r15 = r15 & r10
            int r15 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r15 == 0) goto L77
            if (r5 != r2) goto L6d
            goto L50
        L6d:
            int r3 = r5 + 1
            char r5 = r4[r5]
            r17 = r5
            r5 = r3
            r3 = r17
            goto L56
        L77:
            r15 = 44
            if (r3 != r15) goto L7c
            r6 = r7
        L7c:
            r0.comma = r6
            if (r6 == 0) goto L8d
            if (r5 != r2) goto L84
        L82:
            r3 = r1
            goto L8d
        L84:
            int r3 = r5 + 1
            char r5 = r4[r5]
        L88:
            r17 = r5
            r5 = r3
            r3 = r17
        L8d:
            if (r3 > r14) goto L9e
            long r15 = r12 << r3
            long r15 = r15 & r10
            int r6 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r6 == 0) goto L9e
            if (r5 != r2) goto L99
            goto L82
        L99:
            int r3 = r5 + 1
            char r5 = r4[r5]
            goto L88
        L9e:
            r0.offset = r5
            r0.ch = r3
            return r7
        La3:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfNullOrEmptyString():boolean");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:20:0x0035 -> B:13:0x001a). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:16:0x0022
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfMatchIdent(char r9, char r10) {
        /*
            r8 = this;
            char r0 = r8.ch
            r1 = 0
            if (r0 == r9) goto L6
            return r1
        L6:
            char[] r9 = r8.chars
            int r0 = r8.offset
            int r2 = r0 + 1
            int r3 = r8.end
            if (r2 > r3) goto L67
            char r4 = r9[r0]
            if (r4 == r10) goto L15
            goto L67
        L15:
            r10 = 26
            if (r2 != r3) goto L1c
            r0 = r2
        L1a:
            r2 = r10
            goto L20
        L1c:
            int r0 = r0 + 2
            char r2 = r9[r2]
        L20:
            r3 = 32
            if (r2 > r3) goto L40
            r3 = 1
            long r3 = r3 << r2
            r5 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r3 = r3 & r5
            r5 = 0
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 == 0) goto L40
            int r2 = r8.end
            if (r0 != r2) goto L38
            goto L1a
        L38:
            int r2 = r0 + 1
            char r0 = r9[r0]
            r7 = r2
            r2 = r0
            r0 = r7
            goto L20
        L40:
            int r9 = r8.offset
            int r9 = r9 + 2
            if (r0 != r9) goto L61
            if (r2 == r10) goto L61
            r9 = 40
            if (r2 == r9) goto L61
            r9 = 91
            if (r2 == r9) goto L61
            r9 = 93
            if (r2 == r9) goto L61
            r9 = 41
            if (r2 == r9) goto L61
            r9 = 58
            if (r2 == r9) goto L61
            r9 = 44
            if (r2 == r9) goto L61
            return r1
        L61:
            r8.offset = r0
            r8.ch = r2
            r9 = 1
            return r9
        L67:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfMatchIdent(char, char):boolean");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x003b -> B:14:0x001f). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:17:0x0028
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfMatchIdent(char r8, char r9, char r10) {
        /*
            r7 = this;
            char r0 = r7.ch
            r1 = 0
            if (r0 == r8) goto L6
            return r1
        L6:
            char[] r8 = r7.chars
            int r0 = r7.offset
            int r2 = r0 + 2
            int r3 = r7.end
            if (r2 > r3) goto L6c
            char r4 = r8[r0]
            if (r4 != r9) goto L6c
            int r9 = r0 + 1
            char r9 = r8[r9]
            if (r9 == r10) goto L1b
            goto L6c
        L1b:
            r9 = 26
            if (r2 != r3) goto L21
        L1f:
            r10 = r9
            goto L26
        L21:
            int r0 = r0 + 3
            char r10 = r8[r2]
            r2 = r0
        L26:
            r0 = 32
            if (r10 > r0) goto L45
            r3 = 1
            long r3 = r3 << r10
            r5 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r3 = r3 & r5
            r5 = 0
            int r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r0 == 0) goto L45
            int r10 = r7.end
            if (r2 != r10) goto L3e
            goto L1f
        L3e:
            int r10 = r2 + 1
            char r0 = r8[r2]
            r2 = r10
            r10 = r0
            goto L26
        L45:
            int r8 = r7.offset
            int r8 = r8 + 3
            if (r2 != r8) goto L66
            if (r10 == r9) goto L66
            r8 = 40
            if (r10 == r8) goto L66
            r8 = 91
            if (r10 == r8) goto L66
            r8 = 93
            if (r10 == r8) goto L66
            r8 = 41
            if (r10 == r8) goto L66
            r8 = 58
            if (r10 == r8) goto L66
            r8 = 44
            if (r10 == r8) goto L66
            return r1
        L66:
            r7.offset = r2
            r7.ch = r10
            r8 = 1
            return r8
        L6c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfMatchIdent(char, char, char):boolean");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0041 -> B:16:0x0025). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:19:0x002e
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfMatchIdent(char r8, char r9, char r10, char r11) {
        /*
            r7 = this;
            char r0 = r7.ch
            r1 = 0
            if (r0 == r8) goto L6
            return r1
        L6:
            char[] r8 = r7.chars
            int r0 = r7.offset
            int r2 = r0 + 3
            int r3 = r7.end
            if (r2 > r3) goto L72
            char r4 = r8[r0]
            if (r4 != r9) goto L72
            int r9 = r0 + 1
            char r9 = r8[r9]
            if (r9 != r10) goto L72
            int r9 = r0 + 2
            char r9 = r8[r9]
            if (r9 == r11) goto L21
            goto L72
        L21:
            r9 = 26
            if (r2 != r3) goto L27
        L25:
            r10 = r9
            goto L2c
        L27:
            int r0 = r0 + 4
            char r10 = r8[r2]
            r2 = r0
        L2c:
            r11 = 32
            if (r10 > r11) goto L4b
            r3 = 1
            long r3 = r3 << r10
            r5 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r3 = r3 & r5
            r5 = 0
            int r11 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r11 == 0) goto L4b
            int r10 = r7.end
            if (r2 != r10) goto L44
            goto L25
        L44:
            int r10 = r2 + 1
            char r11 = r8[r2]
            r2 = r10
            r10 = r11
            goto L2c
        L4b:
            int r8 = r7.offset
            int r8 = r8 + 4
            if (r2 != r8) goto L6c
            if (r10 == r9) goto L6c
            r8 = 40
            if (r10 == r8) goto L6c
            r8 = 91
            if (r10 == r8) goto L6c
            r8 = 93
            if (r10 == r8) goto L6c
            r8 = 41
            if (r10 == r8) goto L6c
            r8 = 58
            if (r10 == r8) goto L6c
            r8 = 44
            if (r10 == r8) goto L6c
            return r1
        L6c:
            r7.offset = r2
            r7.ch = r10
            r8 = 1
            return r8
        L72:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfMatchIdent(char, char, char, char):boolean");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x0047 -> B:18:0x002b). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:21:0x0034
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfMatchIdent(char r6, char r7, char r8, char r9, char r10) {
        /*
            r5 = this;
            char r0 = r5.ch
            r1 = 0
            if (r0 == r6) goto L6
            return r1
        L6:
            char[] r6 = r5.chars
            int r0 = r5.offset
            int r2 = r0 + 4
            int r3 = r5.end
            if (r2 > r3) goto L78
            char r4 = r6[r0]
            if (r4 != r7) goto L78
            int r7 = r0 + 1
            char r7 = r6[r7]
            if (r7 != r8) goto L78
            int r7 = r0 + 2
            char r7 = r6[r7]
            if (r7 != r9) goto L78
            int r7 = r0 + 3
            char r7 = r6[r7]
            if (r7 == r10) goto L27
            goto L78
        L27:
            r7 = 26
            if (r2 != r3) goto L2d
        L2b:
            r8 = r7
            goto L32
        L2d:
            int r0 = r0 + 5
            char r8 = r6[r2]
            r2 = r0
        L32:
            r9 = 32
            if (r8 > r9) goto L51
            r9 = 1
            long r9 = r9 << r8
            r3 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r9 = r9 & r3
            r3 = 0
            int r9 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1))
            if (r9 == 0) goto L51
            int r8 = r5.end
            if (r2 != r8) goto L4a
            goto L2b
        L4a:
            int r8 = r2 + 1
            char r9 = r6[r2]
            r2 = r8
            r8 = r9
            goto L32
        L51:
            int r6 = r5.offset
            int r6 = r6 + 5
            if (r2 != r6) goto L72
            if (r8 == r7) goto L72
            r6 = 40
            if (r8 == r6) goto L72
            r6 = 91
            if (r8 == r6) goto L72
            r6 = 93
            if (r8 == r6) goto L72
            r6 = 41
            if (r8 == r6) goto L72
            r6 = 58
            if (r8 == r6) goto L72
            r6 = 44
            if (r8 == r6) goto L72
            return r1
        L72:
            r5.offset = r2
            r5.ch = r8
            r6 = 1
            return r6
        L78:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfMatchIdent(char, char, char, char, char):boolean");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x004d -> B:20:0x0031). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:23:0x003a
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfMatchIdent(char r6, char r7, char r8, char r9, char r10, char r11) {
        /*
            r5 = this;
            char r0 = r5.ch
            r1 = 0
            if (r0 == r6) goto L6
            return r1
        L6:
            char[] r6 = r5.chars
            int r0 = r5.offset
            int r2 = r0 + 5
            int r3 = r5.end
            if (r2 > r3) goto L7e
            char r4 = r6[r0]
            if (r4 != r7) goto L7e
            int r7 = r0 + 1
            char r7 = r6[r7]
            if (r7 != r8) goto L7e
            int r7 = r0 + 2
            char r7 = r6[r7]
            if (r7 != r9) goto L7e
            int r7 = r0 + 3
            char r7 = r6[r7]
            if (r7 != r10) goto L7e
            int r7 = r0 + 4
            char r7 = r6[r7]
            if (r7 == r11) goto L2d
            goto L7e
        L2d:
            r7 = 26
            if (r2 != r3) goto L33
        L31:
            r8 = r7
            goto L38
        L33:
            int r0 = r0 + 6
            char r8 = r6[r2]
            r2 = r0
        L38:
            r9 = 32
            if (r8 > r9) goto L57
            r9 = 1
            long r9 = r9 << r8
            r3 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r9 = r9 & r3
            r3 = 0
            int r9 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1))
            if (r9 == 0) goto L57
            int r8 = r5.end
            if (r2 != r8) goto L50
            goto L31
        L50:
            int r8 = r2 + 1
            char r9 = r6[r2]
            r2 = r8
            r8 = r9
            goto L38
        L57:
            int r6 = r5.offset
            int r6 = r6 + 6
            if (r2 != r6) goto L78
            if (r8 == r7) goto L78
            r6 = 40
            if (r8 == r6) goto L78
            r6 = 91
            if (r8 == r6) goto L78
            r6 = 93
            if (r8 == r6) goto L78
            r6 = 41
            if (r8 == r6) goto L78
            r6 = 58
            if (r8 == r6) goto L78
            r6 = 44
            if (r8 == r6) goto L78
            return r1
        L78:
            r5.offset = r2
            r5.ch = r8
            r6 = 1
            return r6
        L7e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfMatchIdent(char, char, char, char, char, char):boolean");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:20:0x003e -> B:13:0x0023). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:16:0x002b
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfSet() {
        /*
            r10 = this;
            char[] r0 = r10.chars
            int r1 = r10.offset
            char r2 = r10.ch
            r3 = 83
            if (r2 != r3) goto L4f
            int r2 = r1 + 1
            int r3 = r10.end
            if (r2 >= r3) goto L4f
            char r4 = r0[r1]
            r5 = 101(0x65, float:1.42E-43)
            if (r4 != r5) goto L4f
            char r2 = r0[r2]
            r4 = 116(0x74, float:1.63E-43)
            if (r2 != r4) goto L4f
            int r2 = r1 + 2
            r4 = 26
            if (r2 != r3) goto L25
            r1 = r2
        L23:
            r2 = r4
            goto L29
        L25:
            int r1 = r1 + 3
            char r2 = r0[r2]
        L29:
            r3 = 32
            if (r2 > r3) goto L49
            r5 = 1
            long r5 = r5 << r2
            r7 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r5 = r5 & r7
            r7 = 0
            int r3 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r3 == 0) goto L49
            int r2 = r10.end
            if (r1 != r2) goto L41
            goto L23
        L41:
            int r2 = r1 + 1
            char r1 = r0[r1]
            r9 = r2
            r2 = r1
            r1 = r9
            goto L29
        L49:
            r10.offset = r1
            r10.ch = r2
            r0 = 1
            return r0
        L4f:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfSet():boolean");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:30:0x0062 -> B:23:0x0047). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:26:0x004f
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfInfinity() {
        /*
            r10 = this;
            char[] r0 = r10.chars
            int r1 = r10.offset
            char r2 = r10.ch
            r3 = 73
            if (r2 != r3) goto L73
            int r2 = r1 + 6
            int r3 = r10.end
            if (r2 >= r3) goto L73
            char r4 = r0[r1]
            r5 = 110(0x6e, float:1.54E-43)
            if (r4 != r5) goto L73
            int r4 = r1 + 1
            char r4 = r0[r4]
            r6 = 102(0x66, float:1.43E-43)
            if (r4 != r6) goto L73
            int r4 = r1 + 2
            char r4 = r0[r4]
            r6 = 105(0x69, float:1.47E-43)
            if (r4 != r6) goto L73
            int r4 = r1 + 3
            char r4 = r0[r4]
            if (r4 != r5) goto L73
            int r4 = r1 + 4
            char r4 = r0[r4]
            if (r4 != r6) goto L73
            int r4 = r1 + 5
            char r4 = r0[r4]
            r5 = 116(0x74, float:1.63E-43)
            if (r4 != r5) goto L73
            char r2 = r0[r2]
            r4 = 121(0x79, float:1.7E-43)
            if (r2 != r4) goto L73
            int r2 = r1 + 7
            r4 = 26
            if (r2 != r3) goto L49
            r1 = r2
        L47:
            r2 = r4
            goto L4d
        L49:
            int r1 = r1 + 8
            char r2 = r0[r2]
        L4d:
            r3 = 32
            if (r2 > r3) goto L6d
            r5 = 1
            long r5 = r5 << r2
            r7 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r5 = r5 & r7
            r7 = 0
            int r3 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r3 == 0) goto L6d
            int r2 = r10.end
            if (r1 != r2) goto L65
            goto L47
        L65:
            int r2 = r1 + 1
            char r1 = r0[r1]
            r9 = r2
            r2 = r1
            r1 = r9
            goto L4d
        L6d:
            r10.offset = r1
            r10.ch = r2
            r0 = 1
            return r0
        L73:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfInfinity():boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0041 A[LOOP:0: B:11:0x0019->B:25:0x0041, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0013 A[EDGE_INSN: B:26:0x0013->B:9:0x0013 BREAK  A[LOOP:0: B:11:0x0019->B:25:0x0041], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x003e -> B:9:0x0013). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean nextIfObjectStart() {
        /*
            r9 = this;
            char r0 = r9.ch
            r1 = 123(0x7b, float:1.72E-43)
            if (r0 == r1) goto L8
            r0 = 0
            return r0
        L8:
            char[] r0 = r9.chars
            int r1 = r9.offset
            int r2 = r9.end
            r3 = 26
            if (r1 != r2) goto L15
            r2 = r1
        L13:
            r1 = r3
            goto L19
        L15:
            int r2 = r1 + 1
            char r1 = r0[r1]
        L19:
            if (r1 == 0) goto L3c
            r4 = 32
            if (r1 > r4) goto L2f
            r4 = 1
            long r4 = r4 << r1
            r6 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r4 = r4 & r6
            r6 = 0
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 == 0) goto L2f
            goto L3c
        L2f:
            r9.ch = r1
            r9.offset = r2
            r0 = 47
            if (r1 != r0) goto L3a
            r9.skipComment()
        L3a:
            r0 = 1
            return r0
        L3c:
            int r1 = r9.end
            if (r2 != r1) goto L41
            goto L13
        L41:
            int r1 = r2 + 1
            char r2 = r0[r2]
            r8 = r2
            r2 = r1
            r1 = r8
            goto L19
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfObjectStart():boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0070 A[LOOP:0: B:11:0x001b->B:39:0x0070, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0015 A[EDGE_INSN: B:40:0x0015->B:9:0x0015 BREAK  A[LOOP:0: B:11:0x001b->B:39:0x0070], SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:37:0x006d -> B:9:0x0015). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean nextIfObjectEnd() {
        /*
            r17 = this;
            r0 = r17
            char r1 = r0.ch
            r2 = 125(0x7d, float:1.75E-43)
            if (r1 == r2) goto La
            r1 = 0
            return r1
        La:
            int r1 = r0.offset
            char[] r2 = r0.chars
            int r3 = r0.end
            r4 = 26
            if (r1 != r3) goto L17
            r3 = r1
        L15:
            r1 = r4
            goto L1b
        L17:
            int r3 = r1 + 1
            char r1 = r2[r1]
        L1b:
            if (r1 == 0) goto L6b
            r5 = 0
            r7 = 4294981376(0x100003700, double:2.1220027474E-314)
            r9 = 1
            r11 = 32
            if (r1 > r11) goto L32
            long r12 = r9 << r1
            long r12 = r12 & r7
            int r12 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r12 == 0) goto L32
            goto L6b
        L32:
            r12 = 44
            r13 = 1
            if (r1 != r12) goto L5f
            r0.comma = r13
            int r1 = r0.end
            if (r3 != r1) goto L40
            r1 = r3
            r3 = r4
            goto L44
        L40:
            int r1 = r3 + 1
            char r3 = r2[r3]
        L44:
            r16 = r3
            r3 = r1
            r1 = r16
        L49:
            if (r1 == 0) goto L54
            if (r1 > r11) goto L5f
            long r14 = r9 << r1
            long r14 = r14 & r7
            int r12 = (r14 > r5 ? 1 : (r14 == r5 ? 0 : -1))
            if (r12 == 0) goto L5f
        L54:
            int r1 = r0.end
            if (r3 != r1) goto L5a
            r1 = r4
            goto L49
        L5a:
            int r1 = r3 + 1
            char r3 = r2[r3]
            goto L44
        L5f:
            r0.ch = r1
            r0.offset = r3
            r2 = 47
            if (r1 != r2) goto L6a
            r0.skipComment()
        L6a:
            return r13
        L6b:
            int r1 = r0.end
            if (r3 != r1) goto L70
            goto L15
        L70:
            int r1 = r3 + 1
            char r3 = r2[r3]
            r16 = r3
            r3 = r1
            r1 = r16
            goto L1b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfObjectEnd():boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0038 A[LOOP:0: B:7:0x0011->B:20:0x0038, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x000b A[EDGE_INSN: B:21:0x000b->B:5:0x000b BREAK  A[LOOP:0: B:7:0x0011->B:20:0x0038], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:18:0x0035 -> B:5:0x000b). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void next() {
        /*
            r9 = this;
            int r0 = r9.offset
            char[] r1 = r9.chars
            int r2 = r9.end
            r3 = 26
            if (r0 < r2) goto Ld
            r2 = r0
        Lb:
            r0 = r3
            goto L11
        Ld:
            int r2 = r0 + 1
            char r0 = r1[r0]
        L11:
            if (r0 == 0) goto L33
            r4 = 32
            if (r0 > r4) goto L27
            r4 = 1
            long r4 = r4 << r0
            r6 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r4 = r4 & r6
            r6 = 0
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 == 0) goto L27
            goto L33
        L27:
            r9.offset = r2
            r9.ch = r0
            r1 = 47
            if (r0 != r1) goto L32
            r9.skipComment()
        L32:
            return
        L33:
            int r0 = r9.end
            if (r2 != r0) goto L38
            goto Lb
        L38:
            int r0 = r2 + 1
            char r2 = r1[r2]
            r8 = r2
            r2 = r0
            r0 = r8
            goto L11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.next():void");
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0031 A[LOOP:0: B:7:0x0011->B:18:0x0031, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x000b A[EDGE_INSN: B:19:0x000b->B:5:0x000b BREAK  A[LOOP:0: B:7:0x0011->B:18:0x0031], SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x002e -> B:5:0x000b). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void nextWithoutComment() {
        /*
            r9 = this;
            int r0 = r9.offset
            char[] r1 = r9.chars
            int r2 = r9.end
            r3 = 26
            if (r0 < r2) goto Ld
            r2 = r0
        Lb:
            r0 = r3
            goto L11
        Ld:
            int r2 = r0 + 1
            char r0 = r1[r0]
        L11:
            if (r0 == 0) goto L2c
            r4 = 32
            if (r0 > r4) goto L27
            r4 = 1
            long r4 = r4 << r0
            r6 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r4 = r4 & r6
            r6 = 0
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 == 0) goto L27
            goto L2c
        L27:
            r9.offset = r2
            r9.ch = r0
            return
        L2c:
            int r0 = r9.end
            if (r2 != r0) goto L31
            goto Lb
        L31:
            int r0 = r2 + 1
            char r2 = r1[r2]
            r8 = r2
            r2 = r0
            r0 = r8
            goto L11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextWithoutComment():void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x0109, code lost:
    
        r28.nameLength = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x010b, code lost:
    
        if (r8 != 26) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x010d, code lost:
    
        r5 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x010f, code lost:
    
        r5 = r2 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0111, code lost:
    
        r28.nameEnd = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0115, code lost:
    
        if (r8 > ' ') goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x011d, code lost:
    
        if (((1 << r8) & 4294981376L) == 0) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x011f, code lost:
    
        if (r2 != r3) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0121, code lost:
    
        r5 = 26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0123, code lost:
    
        r5 = r4[r2];
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x012d, code lost:
    
        r5 = r8;
     */
    /* JADX WARN: Removed duplicated region for block: B:109:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01aa  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x022d  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00a0 A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0134  */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final long readFieldNameHashCodeUnquote() {
        /*
            Method dump skipped, instruction units count: 746
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readFieldNameHashCodeUnquote():long");
    }

    /* JADX WARN: Code restructure failed: missing block: B:197:0x0364, code lost:
    
        r10 = r28;
     */
    /* JADX WARN: Removed duplicated region for block: B:160:0x02aa  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x02fb  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0370  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x03ce  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x03d1  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x03e8  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x03ea  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x03fa  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0427  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x035f A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final long readFieldNameHashCode() {
        /*
            Method dump skipped, instruction units count: 1126
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readFieldNameHashCode():long");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final long readValueHashCode() {
        boolean z;
        boolean z2;
        char[] cArr;
        char cHexDigit4;
        long j;
        long j2;
        char c = this.ch;
        char c2 = '\"';
        if (c != '\"' && c != '\'') {
            return -1L;
        }
        char[] cArr2 = this.chars;
        this.nameEscape = false;
        int i = this.offset;
        this.nameBegin = i;
        int i2 = this.end;
        int i3 = 0;
        long j3 = 0;
        while (true) {
            z = true;
            if (i < i2) {
                char cHexDigit42 = cArr2[i];
                if (cHexDigit42 != c) {
                    if (cHexDigit42 == '\\') {
                        this.nameEscape = true;
                        int i4 = i + 1;
                        char c3 = cArr2[i4];
                        if (c3 == 'u') {
                            cHexDigit42 = (char) IOUtils.hexDigit4(cArr2, JSONReaderJSONB.check3(i + 2, i2));
                            i += 5;
                        } else if (c3 == 'x') {
                            char c4 = cArr2[i + 2];
                            i += 3;
                            cHexDigit42 = char2(c4, cArr2[i]);
                        } else {
                            cHexDigit42 = char1(c3);
                            i = i4;
                        }
                    }
                    if (cHexDigit42 <= 255 && i3 < 8 && (i3 != 0 || cHexDigit42 != 0)) {
                        switch (i3) {
                            case 0:
                                j3 = (byte) cHexDigit42;
                                continue;
                                i++;
                                i3++;
                                break;
                            case 1:
                                j = ((byte) cHexDigit42) << 8;
                                j2 = 255;
                                break;
                            case 2:
                                j = ((byte) cHexDigit42) << JSONB.Constants.BC_INT32_NUM_16;
                                j2 = WebSocketProtocol.PAYLOAD_SHORT_MAX;
                                break;
                            case 3:
                                j = ((byte) cHexDigit42) << 24;
                                j2 = 16777215;
                                break;
                            case 4:
                                j = ((long) ((byte) cHexDigit42)) << 32;
                                j2 = 4294967295L;
                                break;
                            case 5:
                                j = ((long) ((byte) cHexDigit42)) << 40;
                                j2 = 1099511627775L;
                                break;
                            case 6:
                                j = ((long) ((byte) cHexDigit42)) << 48;
                                j2 = 281474976710655L;
                                break;
                            case 7:
                                j = ((long) ((byte) cHexDigit42)) << 56;
                                j2 = 72057594037927935L;
                                break;
                            default:
                                i++;
                                i3++;
                                break;
                        }
                        j3 = (j3 & j2) + j;
                        i++;
                        i3++;
                    }
                } else if (i3 == 0) {
                    i = this.nameBegin;
                } else {
                    this.nameLength = i3;
                    this.nameEnd = i;
                    i++;
                }
            }
        }
        i = this.nameBegin;
        j3 = 0;
        if (j3 != 0) {
            z2 = true;
        } else {
            j3 = -3750763034362895579L;
            int i5 = 0;
            while (true) {
                char c5 = cArr2[i];
                if (c5 == '\\') {
                    this.nameEscape = z;
                    int i6 = i + 1;
                    z2 = z;
                    char c6 = cArr2[i6];
                    if (c6 == 'u') {
                        cHexDigit4 = (char) IOUtils.hexDigit4(cArr2, JSONReaderJSONB.check3(i + 2, i2));
                        i6 = i + 5;
                    } else if (c6 == 'x') {
                        i6 = i + 3;
                        cHexDigit4 = char2(cArr2[i + 2], cArr2[i6]);
                    } else {
                        cHexDigit4 = char1(c6);
                    }
                    cArr = cArr2;
                    j3 = (((long) cHexDigit4) ^ j3) * Fnv.MAGIC_PRIME;
                    i = i6 + 1;
                } else {
                    z2 = z;
                    if (c5 == c2) {
                        this.nameLength = i5;
                        this.nameEnd = i;
                        this.stringValue = null;
                        i++;
                    } else {
                        i++;
                        cArr = cArr2;
                        j3 = (((long) c5) ^ j3) * Fnv.MAGIC_PRIME;
                    }
                }
                i5++;
                z = z2;
                cArr2 = cArr;
                c2 = '\"';
            }
        }
        char c7 = JSONLexer.EOI;
        char c8 = i == i2 ? (char) 26 : cArr2[i];
        while (c8 <= ' ' && ((1 << c8) & 4294981376L) != 0) {
            i++;
            c8 = cArr2[i];
        }
        boolean z3 = c8 == ',' ? z2 : false;
        this.comma = z3;
        if (z3) {
            i++;
            if (i != i2) {
                c7 = cArr2[i];
            }
            while (c7 <= ' ' && ((1 << c7) & 4294981376L) != 0) {
                i++;
                c7 = cArr2[i];
            }
            c8 = c7;
        }
        this.offset = i + 1;
        this.ch = c8;
        return j3;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final long getNameHashCodeLCase() {
        char c;
        char c2;
        long j;
        long j2;
        int i = this.nameBegin;
        char[] cArr = this.chars;
        char c3 = '\"';
        char c4 = (i <= 0 || cArr[i + (-1)] != '\'') ? '\"' : '\'';
        int i2 = 0;
        long j3 = 0;
        while (true) {
            int i3 = this.end;
            if (i < i3) {
                char cHexDigit4 = cArr[i];
                if (cHexDigit4 == '\\') {
                    int i4 = i + 1;
                    char c5 = cArr[i4];
                    if (c5 == 'u') {
                        cHexDigit4 = (char) IOUtils.hexDigit4(cArr, JSONReaderJSONB.check3(i + 2, i3));
                        i += 5;
                    } else if (c5 == 'x') {
                        char c6 = cArr[i + 2];
                        i += 3;
                        cHexDigit4 = char2(c6, cArr[i]);
                    } else {
                        cHexDigit4 = char1(c5);
                        i = i4;
                    }
                } else if (cHexDigit4 == c4) {
                }
                if (cHexDigit4 <= 255 && i2 < 8 && (i2 != 0 || cHexDigit4 != 0)) {
                    if ((cHexDigit4 != '_' && cHexDigit4 != '-' && cHexDigit4 != ' ') || (c2 = cArr[i + 1]) == '\"' || c2 == '\'' || c2 == cHexDigit4) {
                        if (cHexDigit4 >= 'A' && cHexDigit4 <= 'Z') {
                            cHexDigit4 = (char) (cHexDigit4 + TokenParser.SP);
                        }
                        switch (i2) {
                            case 0:
                                j3 = (byte) cHexDigit4;
                                break;
                            case 1:
                                j = ((byte) cHexDigit4) << 8;
                                j2 = 255;
                                j3 = (j3 & j2) + j;
                                break;
                            case 2:
                                j = ((byte) cHexDigit4) << JSONB.Constants.BC_INT32_NUM_16;
                                j2 = WebSocketProtocol.PAYLOAD_SHORT_MAX;
                                j3 = (j3 & j2) + j;
                                break;
                            case 3:
                                j = ((byte) cHexDigit4) << 24;
                                j2 = 16777215;
                                j3 = (j3 & j2) + j;
                                break;
                            case 4:
                                j = ((long) ((byte) cHexDigit4)) << 32;
                                j2 = 4294967295L;
                                j3 = (j3 & j2) + j;
                                break;
                            case 5:
                                j = ((long) ((byte) cHexDigit4)) << 40;
                                j2 = 1099511627775L;
                                j3 = (j3 & j2) + j;
                                break;
                            case 6:
                                j = ((long) ((byte) cHexDigit4)) << 48;
                                j2 = 281474976710655L;
                                j3 = (j3 & j2) + j;
                                break;
                            case 7:
                                j = ((long) ((byte) cHexDigit4)) << 56;
                                j2 = 72057594037927935L;
                                j3 = (j3 & j2) + j;
                                break;
                        }
                        i2++;
                    }
                    i++;
                }
            }
        }
        i = this.nameBegin;
        j3 = 0;
        if (j3 != 0) {
            return j3;
        }
        long j4 = Fnv.MAGIC_HASH_CODE;
        while (true) {
            int i5 = this.end;
            if (i < i5) {
                char cHexDigit42 = cArr[i];
                if (cHexDigit42 == '\\') {
                    int i6 = i + 1;
                    char c7 = cArr[i6];
                    if (c7 == 'u') {
                        cHexDigit42 = (char) IOUtils.hexDigit4(cArr, JSONReaderJSONB.check3(i + 2, i5));
                        i += 5;
                    } else if (c7 == 'x') {
                        char c8 = cArr[i + 2];
                        i += 3;
                        cHexDigit42 = char2(c8, cArr[i]);
                    } else {
                        cHexDigit42 = char1(c7);
                        i = i6;
                    }
                } else if (cHexDigit42 == c4) {
                }
                i++;
                if (cHexDigit42 == '_' || cHexDigit42 == '-' || cHexDigit42 == ' ') {
                    char c9 = cArr[i];
                    if (c9 == c3 || c9 == '\'' || c9 == cHexDigit42) {
                        c = 'A';
                    }
                } else {
                    c = 'A';
                }
                if (cHexDigit42 >= c && cHexDigit42 <= 'Z') {
                    cHexDigit42 = (char) (cHexDigit42 + TokenParser.SP);
                }
                j4 = Fnv.MAGIC_PRIME * (((long) cHexDigit42) ^ j4);
                c3 = '\"';
            }
        }
        return j4;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final java.lang.String getFieldName() {
        /*
            r9 = this;
            boolean r0 = r9.nameEscape
            if (r0 != 0) goto L1e
            java.lang.String r0 = r9.str
            if (r0 == 0) goto L11
            int r1 = r9.nameBegin
            int r2 = r9.nameEnd
            java.lang.String r0 = r0.substring(r1, r2)
            return r0
        L11:
            java.lang.String r0 = new java.lang.String
            char[] r1 = r9.chars
            int r2 = r9.nameBegin
            int r3 = r9.nameEnd
            int r3 = r3 - r2
            r0.<init>(r1, r2, r3)
            return r0
        L1e:
            int r0 = r9.nameLength
            char[] r0 = new char[r0]
            char[] r1 = r9.chars
            int r2 = r9.nameBegin
            r3 = 0
        L27:
            int r4 = r9.nameEnd
            if (r2 >= r4) goto L83
            char r4 = r1[r2]
            r5 = 34
            r6 = 92
            if (r4 != r6) goto L79
            int r4 = r2 + 1
            char r7 = r1[r4]
            if (r7 == r5) goto L76
            r5 = 58
            if (r7 == r5) goto L76
            r5 = 64
            if (r7 == r5) goto L76
            if (r7 == r6) goto L76
            r5 = 117(0x75, float:1.64E-43)
            if (r7 == r5) goto L66
            r5 = 120(0x78, float:1.68E-43)
            if (r7 == r5) goto L59
            switch(r7) {
                case 42: goto L76;
                case 43: goto L76;
                case 44: goto L76;
                case 45: goto L76;
                case 46: goto L76;
                case 47: goto L76;
                default: goto L4e;
            }
        L4e:
            switch(r7) {
                case 60: goto L76;
                case 61: goto L76;
                case 62: goto L76;
                default: goto L51;
            }
        L51:
            char r2 = r9.char1(r7)
            r8 = r4
            r4 = r2
            r2 = r8
            goto L7c
        L59:
            int r4 = r2 + 2
            char r4 = r1[r4]
            int r2 = r2 + 3
            char r5 = r1[r2]
            char r4 = char2(r4, r5)
            goto L7c
        L66:
            int r4 = r2 + 2
            int r5 = r9.end
            int r4 = com.alibaba.fastjson2.JSONReaderJSONB.check3(r4, r5)
            int r4 = com.alibaba.fastjson2.util.IOUtils.hexDigit4(r1, r4)
            char r4 = (char) r4
            int r2 = r2 + 5
            goto L7c
        L76:
            r2 = r4
            r4 = r7
            goto L7c
        L79:
            if (r4 != r5) goto L7c
            goto L83
        L7c:
            r0[r3] = r4
            int r2 = r2 + 1
            int r3 = r3 + 1
            goto L27
        L83:
            java.lang.String r1 = new java.lang.String
            r1.<init>(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.getFieldName():java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:352:0x0826 A[PHI: r17 r22 r27 r28
      0x0826: PHI (r17v18 long) = 
      (r17v0 long)
      (r17v0 long)
      (r17v0 long)
      (r17v1 long)
      (r17v1 long)
      (r17v1 long)
      (r17v1 long)
      (r17v2 long)
      (r17v2 long)
      (r17v2 long)
      (r17v2 long)
      (r17v2 long)
      (r17v3 long)
      (r17v3 long)
      (r17v3 long)
      (r17v3 long)
      (r17v3 long)
      (r17v3 long)
      (r17v4 long)
      (r17v4 long)
      (r17v4 long)
      (r17v4 long)
      (r17v4 long)
      (r17v4 long)
      (r17v4 long)
      (r17v6 long)
      (r17v6 long)
      (r17v6 long)
      (r17v6 long)
      (r17v6 long)
      (r17v6 long)
      (r17v6 long)
      (r17v6 long)
      (r17v7 long)
      (r17v7 long)
      (r17v7 long)
      (r17v7 long)
      (r17v7 long)
      (r17v7 long)
      (r17v7 long)
      (r17v7 long)
      (r17v7 long)
      (r17v8 long)
      (r17v8 long)
      (r17v8 long)
      (r17v8 long)
      (r17v8 long)
      (r17v8 long)
      (r17v8 long)
      (r17v8 long)
      (r17v8 long)
      (r17v8 long)
      (r17v9 long)
      (r17v9 long)
      (r17v9 long)
      (r17v9 long)
      (r17v9 long)
      (r17v9 long)
      (r17v9 long)
      (r17v9 long)
      (r17v9 long)
      (r17v9 long)
      (r17v9 long)
      (r17v10 long)
      (r17v10 long)
      (r17v10 long)
      (r17v10 long)
      (r17v10 long)
      (r17v10 long)
      (r17v10 long)
      (r17v10 long)
      (r17v10 long)
      (r17v10 long)
      (r17v10 long)
      (r17v10 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v11 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v12 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v15 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v16 long)
      (r17v19 long)
     binds: [B:341:0x07f5, B:343:0x07f9, B:345:0x07fd, B:332:0x07c2, B:334:0x07c6, B:336:0x07ca, B:338:0x07ce, B:321:0x077d, B:323:0x0781, B:325:0x0785, B:327:0x0789, B:329:0x078d, B:308:0x072b, B:310:0x072f, B:312:0x0733, B:314:0x0737, B:316:0x073b, B:318:0x073f, B:293:0x06cb, B:295:0x06cf, B:297:0x06d3, B:299:0x06d7, B:301:0x06db, B:303:0x06df, B:305:0x06e3, B:276:0x065d, B:278:0x0661, B:280:0x0665, B:282:0x0669, B:284:0x066d, B:286:0x0671, B:288:0x0675, B:290:0x0679, B:257:0x05e3, B:259:0x05e7, B:261:0x05eb, B:263:0x05ef, B:265:0x05f3, B:267:0x05f7, B:269:0x05fb, B:271:0x05ff, B:273:0x0603, B:235:0x055e, B:237:0x0562, B:239:0x0566, B:241:0x056a, B:243:0x056e, B:245:0x0572, B:247:0x0576, B:249:0x057a, B:251:0x057e, B:253:0x0582, B:212:0x04d0, B:214:0x04d4, B:216:0x04d8, B:218:0x04dc, B:220:0x04e0, B:222:0x04e4, B:224:0x04e8, B:226:0x04ec, B:228:0x04f0, B:230:0x04f4, B:232:0x04f8, B:186:0x0437, B:188:0x043b, B:190:0x043f, B:192:0x0443, B:194:0x0447, B:196:0x044b, B:198:0x044f, B:200:0x0453, B:202:0x0457, B:204:0x045b, B:206:0x045f, B:208:0x0463, B:159:0x0390, B:161:0x0394, B:163:0x0398, B:165:0x039c, B:167:0x03a0, B:169:0x03a4, B:171:0x03a8, B:173:0x03ac, B:175:0x03b0, B:177:0x03b4, B:179:0x03b8, B:181:0x03bc, B:183:0x03c0, B:130:0x02dd, B:132:0x02e1, B:134:0x02e5, B:136:0x02e9, B:138:0x02ed, B:140:0x02f1, B:142:0x02f5, B:144:0x02f9, B:146:0x02fd, B:148:0x0301, B:150:0x0305, B:152:0x0309, B:154:0x030d, B:156:0x0311, B:99:0x021a, B:101:0x021e, B:103:0x0222, B:105:0x0226, B:107:0x022a, B:109:0x022e, B:111:0x0232, B:113:0x0236, B:115:0x023a, B:117:0x023e, B:119:0x0242, B:121:0x0246, B:123:0x024a, B:125:0x024e, B:127:0x0252, B:66:0x0143, B:68:0x0147, B:70:0x014b, B:72:0x014f, B:74:0x0153, B:76:0x0157, B:78:0x015b, B:80:0x015f, B:82:0x0163, B:84:0x0167, B:86:0x016b, B:88:0x016f, B:90:0x0173, B:92:0x0177, B:94:0x017b, B:96:0x0183, B:64:0x00df] A[DONT_GENERATE, DONT_INLINE]
      0x0826: PHI (r22v18 char) = 
      (r22v0 char)
      (r22v0 char)
      (r22v0 char)
      (r22v1 char)
      (r22v1 char)
      (r22v1 char)
      (r22v1 char)
      (r22v2 char)
      (r22v2 char)
      (r22v2 char)
      (r22v2 char)
      (r22v2 char)
      (r22v3 char)
      (r22v3 char)
      (r22v3 char)
      (r22v3 char)
      (r22v3 char)
      (r22v3 char)
      (r22v4 char)
      (r22v4 char)
      (r22v4 char)
      (r22v4 char)
      (r22v4 char)
      (r22v4 char)
      (r22v4 char)
      (r22v6 char)
      (r22v6 char)
      (r22v6 char)
      (r22v6 char)
      (r22v6 char)
      (r22v6 char)
      (r22v6 char)
      (r22v6 char)
      (r22v7 char)
      (r22v7 char)
      (r22v7 char)
      (r22v7 char)
      (r22v7 char)
      (r22v7 char)
      (r22v7 char)
      (r22v7 char)
      (r22v7 char)
      (r22v8 char)
      (r22v8 char)
      (r22v8 char)
      (r22v8 char)
      (r22v8 char)
      (r22v8 char)
      (r22v8 char)
      (r22v8 char)
      (r22v8 char)
      (r22v8 char)
      (r22v9 char)
      (r22v9 char)
      (r22v9 char)
      (r22v9 char)
      (r22v9 char)
      (r22v9 char)
      (r22v9 char)
      (r22v9 char)
      (r22v9 char)
      (r22v9 char)
      (r22v9 char)
      (r22v10 char)
      (r22v10 char)
      (r22v10 char)
      (r22v10 char)
      (r22v10 char)
      (r22v10 char)
      (r22v10 char)
      (r22v10 char)
      (r22v10 char)
      (r22v10 char)
      (r22v10 char)
      (r22v10 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v11 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v12 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v15 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v16 char)
      (r22v19 char)
     binds: [B:341:0x07f5, B:343:0x07f9, B:345:0x07fd, B:332:0x07c2, B:334:0x07c6, B:336:0x07ca, B:338:0x07ce, B:321:0x077d, B:323:0x0781, B:325:0x0785, B:327:0x0789, B:329:0x078d, B:308:0x072b, B:310:0x072f, B:312:0x0733, B:314:0x0737, B:316:0x073b, B:318:0x073f, B:293:0x06cb, B:295:0x06cf, B:297:0x06d3, B:299:0x06d7, B:301:0x06db, B:303:0x06df, B:305:0x06e3, B:276:0x065d, B:278:0x0661, B:280:0x0665, B:282:0x0669, B:284:0x066d, B:286:0x0671, B:288:0x0675, B:290:0x0679, B:257:0x05e3, B:259:0x05e7, B:261:0x05eb, B:263:0x05ef, B:265:0x05f3, B:267:0x05f7, B:269:0x05fb, B:271:0x05ff, B:273:0x0603, B:235:0x055e, B:237:0x0562, B:239:0x0566, B:241:0x056a, B:243:0x056e, B:245:0x0572, B:247:0x0576, B:249:0x057a, B:251:0x057e, B:253:0x0582, B:212:0x04d0, B:214:0x04d4, B:216:0x04d8, B:218:0x04dc, B:220:0x04e0, B:222:0x04e4, B:224:0x04e8, B:226:0x04ec, B:228:0x04f0, B:230:0x04f4, B:232:0x04f8, B:186:0x0437, B:188:0x043b, B:190:0x043f, B:192:0x0443, B:194:0x0447, B:196:0x044b, B:198:0x044f, B:200:0x0453, B:202:0x0457, B:204:0x045b, B:206:0x045f, B:208:0x0463, B:159:0x0390, B:161:0x0394, B:163:0x0398, B:165:0x039c, B:167:0x03a0, B:169:0x03a4, B:171:0x03a8, B:173:0x03ac, B:175:0x03b0, B:177:0x03b4, B:179:0x03b8, B:181:0x03bc, B:183:0x03c0, B:130:0x02dd, B:132:0x02e1, B:134:0x02e5, B:136:0x02e9, B:138:0x02ed, B:140:0x02f1, B:142:0x02f5, B:144:0x02f9, B:146:0x02fd, B:148:0x0301, B:150:0x0305, B:152:0x0309, B:154:0x030d, B:156:0x0311, B:99:0x021a, B:101:0x021e, B:103:0x0222, B:105:0x0226, B:107:0x022a, B:109:0x022e, B:111:0x0232, B:113:0x0236, B:115:0x023a, B:117:0x023e, B:119:0x0242, B:121:0x0246, B:123:0x024a, B:125:0x024e, B:127:0x0252, B:66:0x0143, B:68:0x0147, B:70:0x014b, B:72:0x014f, B:74:0x0153, B:76:0x0157, B:78:0x015b, B:80:0x015f, B:82:0x0163, B:84:0x0167, B:86:0x016b, B:88:0x016f, B:90:0x0173, B:92:0x0177, B:94:0x017b, B:96:0x0183, B:64:0x00df] A[DONT_GENERATE, DONT_INLINE]
      0x0826: PHI (r27v20 char[]) = 
      (r27v2 char[])
      (r27v2 char[])
      (r27v2 char[])
      (r27v3 char[])
      (r27v3 char[])
      (r27v3 char[])
      (r27v3 char[])
      (r27v4 char[])
      (r27v4 char[])
      (r27v4 char[])
      (r27v4 char[])
      (r27v4 char[])
      (r27v5 char[])
      (r27v5 char[])
      (r27v5 char[])
      (r27v5 char[])
      (r27v5 char[])
      (r27v5 char[])
      (r27v6 char[])
      (r27v6 char[])
      (r27v6 char[])
      (r27v6 char[])
      (r27v6 char[])
      (r27v6 char[])
      (r27v6 char[])
      (r27v8 char[])
      (r27v8 char[])
      (r27v8 char[])
      (r27v8 char[])
      (r27v8 char[])
      (r27v8 char[])
      (r27v8 char[])
      (r27v8 char[])
      (r27v9 char[])
      (r27v9 char[])
      (r27v9 char[])
      (r27v9 char[])
      (r27v9 char[])
      (r27v9 char[])
      (r27v9 char[])
      (r27v9 char[])
      (r27v9 char[])
      (r27v10 char[])
      (r27v10 char[])
      (r27v10 char[])
      (r27v10 char[])
      (r27v10 char[])
      (r27v10 char[])
      (r27v10 char[])
      (r27v10 char[])
      (r27v10 char[])
      (r27v10 char[])
      (r27v11 char[])
      (r27v11 char[])
      (r27v11 char[])
      (r27v11 char[])
      (r27v11 char[])
      (r27v11 char[])
      (r27v11 char[])
      (r27v11 char[])
      (r27v11 char[])
      (r27v11 char[])
      (r27v11 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v12 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v13 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v14 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v17 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v18 char[])
      (r27v21 char[])
     binds: [B:341:0x07f5, B:343:0x07f9, B:345:0x07fd, B:332:0x07c2, B:334:0x07c6, B:336:0x07ca, B:338:0x07ce, B:321:0x077d, B:323:0x0781, B:325:0x0785, B:327:0x0789, B:329:0x078d, B:308:0x072b, B:310:0x072f, B:312:0x0733, B:314:0x0737, B:316:0x073b, B:318:0x073f, B:293:0x06cb, B:295:0x06cf, B:297:0x06d3, B:299:0x06d7, B:301:0x06db, B:303:0x06df, B:305:0x06e3, B:276:0x065d, B:278:0x0661, B:280:0x0665, B:282:0x0669, B:284:0x066d, B:286:0x0671, B:288:0x0675, B:290:0x0679, B:257:0x05e3, B:259:0x05e7, B:261:0x05eb, B:263:0x05ef, B:265:0x05f3, B:267:0x05f7, B:269:0x05fb, B:271:0x05ff, B:273:0x0603, B:235:0x055e, B:237:0x0562, B:239:0x0566, B:241:0x056a, B:243:0x056e, B:245:0x0572, B:247:0x0576, B:249:0x057a, B:251:0x057e, B:253:0x0582, B:212:0x04d0, B:214:0x04d4, B:216:0x04d8, B:218:0x04dc, B:220:0x04e0, B:222:0x04e4, B:224:0x04e8, B:226:0x04ec, B:228:0x04f0, B:230:0x04f4, B:232:0x04f8, B:186:0x0437, B:188:0x043b, B:190:0x043f, B:192:0x0443, B:194:0x0447, B:196:0x044b, B:198:0x044f, B:200:0x0453, B:202:0x0457, B:204:0x045b, B:206:0x045f, B:208:0x0463, B:159:0x0390, B:161:0x0394, B:163:0x0398, B:165:0x039c, B:167:0x03a0, B:169:0x03a4, B:171:0x03a8, B:173:0x03ac, B:175:0x03b0, B:177:0x03b4, B:179:0x03b8, B:181:0x03bc, B:183:0x03c0, B:130:0x02dd, B:132:0x02e1, B:134:0x02e5, B:136:0x02e9, B:138:0x02ed, B:140:0x02f1, B:142:0x02f5, B:144:0x02f9, B:146:0x02fd, B:148:0x0301, B:150:0x0305, B:152:0x0309, B:154:0x030d, B:156:0x0311, B:99:0x021a, B:101:0x021e, B:103:0x0222, B:105:0x0226, B:107:0x022a, B:109:0x022e, B:111:0x0232, B:113:0x0236, B:115:0x023a, B:117:0x023e, B:119:0x0242, B:121:0x0246, B:123:0x024a, B:125:0x024e, B:127:0x0252, B:66:0x0143, B:68:0x0147, B:70:0x014b, B:72:0x014f, B:74:0x0153, B:76:0x0157, B:78:0x015b, B:80:0x015f, B:82:0x0163, B:84:0x0167, B:86:0x016b, B:88:0x016f, B:90:0x0173, B:92:0x0177, B:94:0x017b, B:96:0x0183, B:64:0x00df] A[DONT_GENERATE, DONT_INLINE]
      0x0826: PHI (r28v20 int) = 
      (r28v2 int)
      (r28v2 int)
      (r28v2 int)
      (r28v3 int)
      (r28v3 int)
      (r28v3 int)
      (r28v3 int)
      (r28v4 int)
      (r28v4 int)
      (r28v4 int)
      (r28v4 int)
      (r28v4 int)
      (r28v5 int)
      (r28v5 int)
      (r28v5 int)
      (r28v5 int)
      (r28v5 int)
      (r28v5 int)
      (r28v6 int)
      (r28v6 int)
      (r28v6 int)
      (r28v6 int)
      (r28v6 int)
      (r28v6 int)
      (r28v6 int)
      (r28v8 int)
      (r28v8 int)
      (r28v8 int)
      (r28v8 int)
      (r28v8 int)
      (r28v8 int)
      (r28v8 int)
      (r28v8 int)
      (r28v9 int)
      (r28v9 int)
      (r28v9 int)
      (r28v9 int)
      (r28v9 int)
      (r28v9 int)
      (r28v9 int)
      (r28v9 int)
      (r28v9 int)
      (r28v10 int)
      (r28v10 int)
      (r28v10 int)
      (r28v10 int)
      (r28v10 int)
      (r28v10 int)
      (r28v10 int)
      (r28v10 int)
      (r28v10 int)
      (r28v10 int)
      (r28v11 int)
      (r28v11 int)
      (r28v11 int)
      (r28v11 int)
      (r28v11 int)
      (r28v11 int)
      (r28v11 int)
      (r28v11 int)
      (r28v11 int)
      (r28v11 int)
      (r28v11 int)
      (r28v12 int)
      (r28v12 int)
      (r28v12 int)
      (r28v12 int)
      (r28v12 int)
      (r28v12 int)
      (r28v12 int)
      (r28v12 int)
      (r28v12 int)
      (r28v12 int)
      (r28v12 int)
      (r28v12 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v13 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v14 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v17 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v18 int)
      (r28v21 int)
     binds: [B:341:0x07f5, B:343:0x07f9, B:345:0x07fd, B:332:0x07c2, B:334:0x07c6, B:336:0x07ca, B:338:0x07ce, B:321:0x077d, B:323:0x0781, B:325:0x0785, B:327:0x0789, B:329:0x078d, B:308:0x072b, B:310:0x072f, B:312:0x0733, B:314:0x0737, B:316:0x073b, B:318:0x073f, B:293:0x06cb, B:295:0x06cf, B:297:0x06d3, B:299:0x06d7, B:301:0x06db, B:303:0x06df, B:305:0x06e3, B:276:0x065d, B:278:0x0661, B:280:0x0665, B:282:0x0669, B:284:0x066d, B:286:0x0671, B:288:0x0675, B:290:0x0679, B:257:0x05e3, B:259:0x05e7, B:261:0x05eb, B:263:0x05ef, B:265:0x05f3, B:267:0x05f7, B:269:0x05fb, B:271:0x05ff, B:273:0x0603, B:235:0x055e, B:237:0x0562, B:239:0x0566, B:241:0x056a, B:243:0x056e, B:245:0x0572, B:247:0x0576, B:249:0x057a, B:251:0x057e, B:253:0x0582, B:212:0x04d0, B:214:0x04d4, B:216:0x04d8, B:218:0x04dc, B:220:0x04e0, B:222:0x04e4, B:224:0x04e8, B:226:0x04ec, B:228:0x04f0, B:230:0x04f4, B:232:0x04f8, B:186:0x0437, B:188:0x043b, B:190:0x043f, B:192:0x0443, B:194:0x0447, B:196:0x044b, B:198:0x044f, B:200:0x0453, B:202:0x0457, B:204:0x045b, B:206:0x045f, B:208:0x0463, B:159:0x0390, B:161:0x0394, B:163:0x0398, B:165:0x039c, B:167:0x03a0, B:169:0x03a4, B:171:0x03a8, B:173:0x03ac, B:175:0x03b0, B:177:0x03b4, B:179:0x03b8, B:181:0x03bc, B:183:0x03c0, B:130:0x02dd, B:132:0x02e1, B:134:0x02e5, B:136:0x02e9, B:138:0x02ed, B:140:0x02f1, B:142:0x02f5, B:144:0x02f9, B:146:0x02fd, B:148:0x0301, B:150:0x0305, B:152:0x0309, B:154:0x030d, B:156:0x0311, B:99:0x021a, B:101:0x021e, B:103:0x0222, B:105:0x0226, B:107:0x022a, B:109:0x022e, B:111:0x0232, B:113:0x0236, B:115:0x023a, B:117:0x023e, B:119:0x0242, B:121:0x0246, B:123:0x024a, B:125:0x024e, B:127:0x0252, B:66:0x0143, B:68:0x0147, B:70:0x014b, B:72:0x014f, B:74:0x0153, B:76:0x0157, B:78:0x015b, B:80:0x015f, B:82:0x0163, B:84:0x0167, B:86:0x016b, B:88:0x016f, B:90:0x0173, B:92:0x0177, B:94:0x017b, B:96:0x0183, B:64:0x00df] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:355:0x082d  */
    /* JADX WARN: Removed duplicated region for block: B:383:0x08ba  */
    /* JADX WARN: Removed duplicated region for block: B:386:0x08c4  */
    /* JADX WARN: Removed duplicated region for block: B:388:0x08cb  */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.String readFieldName() {
        /*
            Method dump skipped, instruction units count: 2324
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readFieldName():java.lang.String");
    }

    /* JADX WARN: Path cross not found for [B:73:0x00b5, B:117:?], limit reached: 119 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:81:0x00d3 -> B:74:0x00b7). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:78:0x00c9
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final int readInt32Value() {
        /*
            Method dump skipped, instruction units count: 274
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readInt32Value():int");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final Integer readInt32() {
        char c = this.ch;
        if ((c == '\"' || c == '\'' || c == 'n') && nextIfNullOrEmptyString()) {
            return null;
        }
        return Integer.valueOf(readInt32Value());
    }

    /* JADX WARN: Path cross not found for [B:76:0x00d3, B:120:?], limit reached: 122 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:84:0x00ee -> B:77:0x00d5). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:81:0x00e4
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final long readInt64Value() {
        /*
            Method dump skipped, instruction units count: 302
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readInt64Value():long");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final Long readInt64() {
        char c = this.ch;
        if ((c == '\"' || c == '\'' || c == 'n') && nextIfNullOrEmptyString()) {
            return null;
        }
        return Long.valueOf(readInt64Value());
    }

    /* JADX WARN: Code restructure failed: missing block: B:173:0x0217, code lost:
    
        r14 = -r14;
     */
    /* JADX WARN: Removed duplicated region for block: B:160:0x01f7  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x020e  */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public double readDoubleValue() {
        /*
            Method dump skipped, instruction units count: 636
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readDoubleValue():double");
    }

    /* JADX WARN: Code restructure failed: missing block: B:173:0x0217, code lost:
    
        r14 = -r11;
     */
    /* JADX WARN: Removed duplicated region for block: B:160:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x020e  */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public float readFloatValue() {
        /*
            Method dump skipped, instruction units count: 635
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readFloatValue():float");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final String getString() {
        if (this.stringValue != null) {
            return this.stringValue;
        }
        int i = this.nameEnd - this.nameBegin;
        if (!this.nameEscape) {
            return new String(this.chars, this.nameBegin, i);
        }
        char[] cArr = this.chars;
        char[] cArr2 = new char[this.nameLength];
        int i2 = this.nameBegin;
        int i3 = 0;
        while (true) {
            char cHexDigit4 = cArr[i2];
            if (cHexDigit4 == '\\') {
                int i4 = i2 + 1;
                char c = cArr[i4];
                if (c == '\"' || c == '\\') {
                    i2 = i4;
                    cHexDigit4 = c;
                } else if (c == 'u') {
                    cHexDigit4 = (char) IOUtils.hexDigit4(cArr, JSONReaderJSONB.check3(i2 + 2, this.end));
                    i2 += 5;
                } else if (c == 'x') {
                    char c2 = cArr[i2 + 2];
                    i2 += 3;
                    cHexDigit4 = char2(c2, cArr[i2]);
                } else {
                    cHexDigit4 = char1(c);
                    i2 = i4;
                }
            } else if (cHexDigit4 == '\"') {
                String str = new String(cArr2);
                this.stringValue = str;
                return str;
            }
            cArr2[i3] = cHexDigit4;
            i2++;
            i3++;
        }
    }

    protected final void readString0() {
        String str;
        char[] cArr = this.chars;
        char c = this.ch;
        int i = this.offset;
        this.valueEscape = false;
        int i2 = i;
        int i3 = 0;
        while (true) {
            char c2 = cArr[i2];
            if (c2 == '\\') {
                this.valueEscape = true;
                char c3 = cArr[i2 + 1];
                i2 += c3 == 'u' ? 6 : c3 == 'x' ? 4 : 2;
            } else if (c2 == c) {
                break;
            } else {
                i2++;
            }
            i3++;
        }
        if (this.valueEscape) {
            char[] cArr2 = new char[i3];
            int i4 = 0;
            while (true) {
                char[] cArr3 = this.chars;
                char cChar1 = cArr3[i];
                if (cChar1 == '\\') {
                    int i5 = i + 1;
                    char c4 = cArr3[i5];
                    if (c4 == 'u') {
                        cChar1 = (char) IOUtils.hexDigit4(cArr, JSONReaderJSONB.check3(i + 2, this.end));
                        i += 5;
                    } else if (c4 == 'x') {
                        char c5 = cArr[i + 2];
                        i += 3;
                        cChar1 = char2(c5, cArr[i]);
                    } else if (c4 == '\\' || c4 == '\"') {
                        i = i5;
                        cChar1 = c4;
                    } else {
                        cChar1 = char1(c4);
                        i = i5;
                    }
                } else if (cChar1 == '\"') {
                    break;
                }
                cArr2[i4] = cChar1;
                i++;
                i4++;
            }
            str = new String(cArr2);
            i2 = i;
        } else {
            str = new String(cArr, this.offset, i2 - this.offset);
        }
        int i6 = i2 + 1;
        char c6 = i6 == this.end ? (char) 26 : cArr[i6];
        while (c6 <= ' ' && ((1 << c6) & 4294981376L) != 0) {
            i6++;
            c6 = cArr[i6];
        }
        boolean z = c6 == ',';
        this.comma = z;
        if (z) {
            this.offset = i6 + 1;
            int i7 = this.offset;
            this.offset = i7 + 1;
            this.ch = cArr[i7];
            while (this.ch <= ' ' && ((1 << this.ch) & 4294981376L) != 0) {
                if (this.offset >= this.end) {
                    this.ch = JSONLexer.EOI;
                } else {
                    int i8 = this.offset;
                    this.offset = i8 + 1;
                    this.ch = cArr[i8];
                }
            }
        } else {
            this.offset = i6 + 1;
            this.ch = c6;
        }
        this.stringValue = str;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:97:0x0160 -> B:91:0x0149). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:94:0x0157
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public java.lang.String readString() {
        /*
            Method dump skipped, instruction units count: 428
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readString():java.lang.String");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean skipName() {
        this.offset = skipName(this, this.chars, this.offset, this.end);
        return true;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x0045 -> B:20:0x002b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:38:0x0066 -> B:33:0x0057). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:23:0x003c
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private static int skipName(com.alibaba.fastjson2.JSONReaderUTF16 r12, char[] r13, int r14, int r15) {
        /*
            char r0 = r12.ch
            boolean r1 = r12.checkNameBegin(r0)
            if (r1 == 0) goto Lb
            int r12 = r12.offset
            return r12
        Lb:
            int r1 = r14 + 1
            char r2 = r13[r14]
            r3 = 92
            if (r2 != r3) goto L24
            char r14 = r13[r1]
            r2 = 117(0x75, float:1.64E-43)
            if (r14 != r2) goto L1b
            r14 = 5
            goto L22
        L1b:
            r2 = 120(0x78, float:1.68E-43)
            if (r14 != r2) goto L21
            r14 = 3
            goto L22
        L21:
            r14 = 1
        L22:
            int r14 = r14 + r1
            goto Lb
        L24:
            if (r2 != r0) goto L79
            r0 = 26
            if (r1 != r15) goto L2d
            r14 = r1
        L2b:
            r1 = r0
            goto L31
        L2d:
            int r14 = r14 + 2
            char r1 = r13[r1]
        L31:
            r2 = 0
            r4 = 4294981376(0x100003700, double:2.1220027474E-314)
            r6 = 1
            r8 = 32
            if (r1 > r8) goto L50
            long r9 = r6 << r1
            long r9 = r9 & r4
            int r9 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1))
            if (r9 == 0) goto L50
            if (r14 != r15) goto L48
            goto L2b
        L48:
            int r1 = r14 + 1
            char r14 = r13[r14]
            r11 = r1
            r1 = r14
            r14 = r11
            goto L31
        L50:
            r9 = 58
            if (r1 != r9) goto L74
            if (r14 != r15) goto L59
            r1 = r14
        L57:
            r14 = r0
            goto L5d
        L59:
            int r1 = r14 + 1
            char r14 = r13[r14]
        L5d:
            if (r14 > r8) goto L71
            long r9 = r6 << r14
            long r9 = r9 & r4
            int r9 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1))
            if (r9 == 0) goto L71
            if (r1 != r15) goto L69
            goto L57
        L69:
            int r14 = r1 + 1
            char r1 = r13[r1]
            r11 = r1
            r1 = r14
            r14 = r11
            goto L5d
        L71:
            r12.ch = r14
            return r1
        L74:
            com.alibaba.fastjson2.JSONException r12 = syntaxError(r1)
            throw r12
        L79:
            r14 = r1
            goto Lb
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.skipName(com.alibaba.fastjson2.JSONReaderUTF16, char[], int, int):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:137:0x00f8, code lost:
    
        r2 = 26;
     */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x00e9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x00ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int skipNumber(com.alibaba.fastjson2.JSONReaderUTF16 r20, char[] r21, int r22, int r23) {
        /*
            Method dump skipped, instruction units count: 364
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.skipNumber(com.alibaba.fastjson2.JSONReaderUTF16, char[], int, int):int");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x0057 -> B:22:0x003d). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:25:0x004e
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private static int skipString(com.alibaba.fastjson2.JSONReaderUTF16 r18, char[] r19, int r20, int r21) {
        /*
            r0 = r18
            r1 = r20
            r2 = r21
            char r3 = r0.ch
            r4 = 26
            if (r1 != r2) goto Le
            r5 = r4
            goto L14
        Le:
            int r5 = r1 + 1
            char r1 = r19[r1]
            goto Laf
        L14:
            r6 = 92
            if (r5 != r6) goto L38
            int r5 = r1 + 1
            char r7 = r19[r1]
            r8 = 117(0x75, float:1.64E-43)
            if (r7 != r8) goto L23
            int r5 = r1 + 5
            goto L33
        L23:
            r8 = 120(0x78, float:1.68E-43)
            if (r7 != r8) goto L2a
            int r5 = r1 + 3
            goto L33
        L2a:
            if (r7 == r6) goto L33
            r1 = 34
            if (r7 == r1) goto L33
            r0.char1(r7)
        L33:
            int r1 = r5 + 1
            char r5 = r19[r5]
            goto L14
        L38:
            if (r5 != r3) goto Lab
            if (r1 != r2) goto L3f
            r3 = r1
        L3d:
            r1 = r4
            goto L43
        L3f:
            int r3 = r1 + 1
            char r1 = r19[r1]
        L43:
            r5 = 0
            r7 = 4294981376(0x100003700, double:2.1220027474E-314)
            r9 = 1
            r11 = 32
            if (r1 > r11) goto L64
            long r12 = r9 << r1
            long r12 = r12 & r7
            int r12 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r12 == 0) goto L64
            if (r3 != r2) goto L5a
            goto L3d
        L5a:
            int r1 = r3 + 1
            char r3 = r19[r3]
            r17 = r3
            r3 = r1
            r1 = r17
            goto L43
        L64:
            r12 = 44
            r13 = 93
            r14 = 125(0x7d, float:1.75E-43)
            if (r1 != r12) goto L99
            if (r3 != r2) goto L71
            r1 = r3
            r3 = r4
            goto L75
        L71:
            int r1 = r3 + 1
            char r3 = r19[r3]
        L75:
            r17 = r3
            r3 = r1
            r1 = r17
        L7a:
            if (r1 > r11) goto L8c
            long r15 = r9 << r1
            long r15 = r15 & r7
            int r12 = (r15 > r5 ? 1 : (r15 == r5 ? 0 : -1))
            if (r12 == 0) goto L8c
            if (r3 != r2) goto L87
            r1 = r4
            goto L7a
        L87:
            int r1 = r3 + 1
            char r3 = r19[r3]
            goto L75
        L8c:
            if (r1 == r14) goto L94
            if (r1 == r13) goto L94
            if (r1 == r4) goto L94
            r2 = 1
            goto La6
        L94:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r3, r1)
            throw r0
        L99:
            if (r1 == r14) goto La5
            if (r1 == r13) goto La5
            if (r1 != r4) goto La0
            goto La5
        La0:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r3, r1)
            throw r0
        La5:
            r2 = 0
        La6:
            r0.comma = r2
            r0.ch = r1
            return r3
        Lab:
            int r5 = r1 + 1
            char r1 = r19[r1]
        Laf:
            r17 = r5
            r5 = r1
            r1 = r17
            goto L14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.skipString(com.alibaba.fastjson2.JSONReaderUTF16, char[], int, int):int");
    }

    private static int skipStringEscaped(JSONReaderUTF16 jSONReaderUTF16, char[] cArr, int i, int i2) {
        int i3 = i + 1;
        char c = cArr[i];
        while (true) {
            if (c == '\\') {
                int i4 = i3 + 1;
                char c2 = cArr[i3];
                if (c2 == 'u') {
                    i4 = i3 + 5;
                } else if (c2 == 'x') {
                    i4 = i3 + 3;
                } else if (c2 != '\\' && c2 != '\"') {
                    jSONReaderUTF16.char1(c2);
                }
                i3 = i4 + 1;
                c = cArr[i4];
            } else {
                if (c == i2) {
                    return i3;
                }
                int i5 = i3 + 1;
                char c3 = cArr[i3];
                i3 = i5;
                c = c3;
            }
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:14:0x0030 -> B:8:0x0016). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:11:0x0027
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private static int skipObject(com.alibaba.fastjson2.JSONReaderUTF16 r18, char[] r19, int r20, int r21) {
        /*
            r0 = r18
            r1 = r19
            r2 = r21
            int r3 = next(r18, r19, r20, r21)
            r5 = 0
        Lb:
            char r6 = r0.ch
            r7 = 125(0x7d, float:1.75E-43)
            if (r6 != r7) goto L83
            r5 = 26
            if (r3 != r2) goto L18
            r6 = r3
        L16:
            r3 = r5
            goto L1c
        L18:
            int r6 = r3 + 1
            char r3 = r1[r3]
        L1c:
            r8 = 0
            r10 = 4294981376(0x100003700, double:2.1220027474E-314)
            r12 = 1
            r14 = 32
            if (r3 > r14) goto L3d
            long r15 = r12 << r3
            long r15 = r15 & r10
            int r15 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r15 == 0) goto L3d
            if (r6 != r2) goto L33
            goto L16
        L33:
            int r3 = r6 + 1
            char r6 = r1[r6]
            r17 = r6
            r6 = r3
            r3 = r17
            goto L1c
        L3d:
            r15 = 44
            r4 = 93
            if (r3 != r15) goto L70
            if (r6 != r2) goto L48
            r3 = r6
            r6 = r5
            goto L4c
        L48:
            int r3 = r6 + 1
            char r6 = r1[r6]
        L4c:
            r17 = r6
            r6 = r3
            r3 = r17
        L51:
            if (r3 > r14) goto L63
            long r15 = r12 << r3
            long r15 = r15 & r10
            int r15 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r15 == 0) goto L63
            if (r6 != r2) goto L5e
            r3 = r5
            goto L51
        L5e:
            int r3 = r6 + 1
            char r6 = r1[r6]
            goto L4c
        L63:
            if (r3 == r7) goto L6b
            if (r3 == r4) goto L6b
            if (r3 == r5) goto L6b
            r4 = 1
            goto L7d
        L6b:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r6, r3)
            throw r0
        L70:
            if (r3 == r7) goto L7c
            if (r3 == r4) goto L7c
            if (r3 != r5) goto L77
            goto L7c
        L77:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r6, r3)
            throw r0
        L7c:
            r4 = 0
        L7d:
            r0.comma = r4
            char r1 = (char) r3
            r0.ch = r1
            return r6
        L83:
            if (r5 == 0) goto L8f
            boolean r4 = r0.comma
            if (r4 == 0) goto L8a
            goto L8f
        L8a:
            com.alibaba.fastjson2.JSONException r0 = r0.valueError()
            throw r0
        L8f:
            int r3 = skipName(r0, r1, r3, r2)
            int r3 = skipValue(r0, r1, r3, r2)
            int r5 = r5 + 1
            goto Lb
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.skipObject(com.alibaba.fastjson2.JSONReaderUTF16, char[], int, int):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0005, code lost:
    
        if (r1 != r10) goto L20;
     */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0028  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0030 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x000d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int next(com.alibaba.fastjson2.JSONReaderUTF16 r7, char[] r8, int r9, int r10) {
        /*
            r0 = 26
            if (r9 != r10) goto L7
            r1 = r9
        L5:
            r9 = r0
            goto Lb
        L7:
            int r1 = r9 + 1
            char r9 = r8[r9]
        Lb:
            if (r9 == 0) goto L31
            r2 = 32
            if (r9 > r2) goto L21
            r2 = 1
            long r2 = r2 << r9
            r4 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r2 = r2 & r4
            r4 = 0
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L21
            goto L31
        L21:
            char r8 = (char) r9
            r7.ch = r8
            r8 = 47
            if (r9 != r8) goto L30
            r7.offset = r1
            r7.skipComment()
            int r7 = r7.offset
            return r7
        L30:
            return r1
        L31:
            if (r1 != r10) goto L34
            goto L5
        L34:
            int r9 = r1 + 1
            char r1 = r8[r1]
            r6 = r1
            r1 = r9
            r9 = r6
            goto Lb
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.next(com.alibaba.fastjson2.JSONReaderUTF16, char[], int, int):int");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:14:0x0031 -> B:8:0x0017). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:11:0x0028
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private static int skipArray(com.alibaba.fastjson2.JSONReaderUTF16 r18, char[] r19, int r20, int r21) {
        /*
            r0 = r18
            r1 = r19
            r2 = r21
            int r3 = next(r18, r19, r20, r21)
            r4 = 0
            r5 = r4
        Lc:
            char r6 = r0.ch
            r7 = 93
            if (r6 != r7) goto L84
            r5 = 26
            if (r3 != r2) goto L19
            r6 = r3
        L17:
            r3 = r5
            goto L1d
        L19:
            int r6 = r3 + 1
            char r3 = r1[r3]
        L1d:
            r8 = 0
            r10 = 4294981376(0x100003700, double:2.1220027474E-314)
            r12 = 1
            r14 = 32
            if (r3 > r14) goto L3e
            long r15 = r12 << r3
            long r15 = r15 & r10
            int r15 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r15 == 0) goto L3e
            if (r6 != r2) goto L34
            goto L17
        L34:
            int r3 = r6 + 1
            char r6 = r1[r6]
            r17 = r6
            r6 = r3
            r3 = r17
            goto L1d
        L3e:
            r15 = 44
            if (r3 != r15) goto L60
            if (r6 != r2) goto L46
            r4 = r5
            goto L4b
        L46:
            int r3 = r6 + 1
            char r4 = r1[r6]
            goto L5d
        L4b:
            r3 = r4
        L4c:
            if (r3 > r14) goto L5f
            long r15 = r12 << r3
            long r15 = r15 & r10
            int r4 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r4 == 0) goto L5f
            if (r6 != r2) goto L59
            r3 = r5
            goto L4c
        L59:
            int r3 = r6 + 1
            char r4 = r1[r6]
        L5d:
            r6 = r3
            goto L4b
        L5f:
            r4 = 1
        L60:
            r1 = 125(0x7d, float:1.75E-43)
            if (r4 != 0) goto L70
            if (r3 == r1) goto L70
            if (r3 == r7) goto L70
            if (r3 != r5) goto L6b
            goto L70
        L6b:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r6, r3)
            throw r0
        L70:
            if (r4 == 0) goto L7e
            if (r3 == r1) goto L79
            if (r3 == r7) goto L79
            if (r3 == r5) goto L79
            goto L7e
        L79:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r6, r3)
            throw r0
        L7e:
            r0.comma = r4
            char r1 = (char) r3
            r0.ch = r1
            return r6
        L84:
            if (r5 == 0) goto L90
            boolean r6 = r0.comma
            if (r6 == 0) goto L8b
            goto L90
        L8b:
            com.alibaba.fastjson2.JSONException r0 = r0.valueError()
            throw r0
        L90:
            int r3 = skipValue(r0, r1, r3, r2)
            int r5 = r5 + 1
            goto Lc
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.skipArray(com.alibaba.fastjson2.JSONReaderUTF16, char[], int, int):int");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x002d -> B:9:0x0013). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:12:0x0024
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private static int skipFalse(com.alibaba.fastjson2.JSONReaderUTF16 r18, char[] r19, int r20, int r21) {
        /*
            r0 = r18
            r1 = r21
            int r2 = r20 + 4
            if (r2 > r1) goto L82
            boolean r3 = com.alibaba.fastjson2.util.IOUtils.notALSE(r19, r20)
            if (r3 != 0) goto L82
            r3 = 26
            if (r2 != r1) goto L15
            r4 = r2
        L13:
            r2 = r3
            goto L19
        L15:
            int r4 = r20 + 5
            char r2 = r19[r2]
        L19:
            r5 = 0
            r7 = 4294981376(0x100003700, double:2.1220027474E-314)
            r9 = 1
            r11 = 32
            if (r2 > r11) goto L3a
            long r12 = r9 << r2
            long r12 = r12 & r7
            int r12 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r12 == 0) goto L3a
            if (r4 != r1) goto L30
            goto L13
        L30:
            int r2 = r4 + 1
            char r4 = r19[r4]
            r17 = r4
            r4 = r2
            r2 = r17
            goto L19
        L3a:
            r12 = 44
            r13 = 93
            r14 = 125(0x7d, float:1.75E-43)
            if (r2 != r12) goto L6f
            if (r4 != r1) goto L47
            r2 = r4
            r4 = r3
            goto L4b
        L47:
            int r2 = r4 + 1
            char r4 = r19[r4]
        L4b:
            r17 = r4
            r4 = r2
            r2 = r17
        L50:
            if (r2 > r11) goto L62
            long r15 = r9 << r2
            long r15 = r15 & r7
            int r12 = (r15 > r5 ? 1 : (r15 == r5 ? 0 : -1))
            if (r12 == 0) goto L62
            if (r4 != r1) goto L5d
            r2 = r3
            goto L50
        L5d:
            int r2 = r4 + 1
            char r4 = r19[r4]
            goto L4b
        L62:
            if (r2 == r14) goto L6a
            if (r2 == r13) goto L6a
            if (r2 == r3) goto L6a
            r1 = 1
            goto L7c
        L6a:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r4, r2)
            throw r0
        L6f:
            if (r2 == r14) goto L7b
            if (r2 == r13) goto L7b
            if (r2 != r3) goto L76
            goto L7b
        L76:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r4, r2)
            throw r0
        L7b:
            r1 = 0
        L7c:
            r0.comma = r1
            char r1 = (char) r2
            r0.ch = r1
            return r4
        L82:
            com.alibaba.fastjson2.JSONException r0 = r0.error()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.skipFalse(com.alibaba.fastjson2.JSONReaderUTF16, char[], int, int):int");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x0031 -> B:9:0x0017). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:12:0x0028
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private static int skipTrue(com.alibaba.fastjson2.JSONReaderUTF16 r19, char[] r20, int r21, int r22) {
        /*
            r0 = r19
            r1 = r20
            r2 = r22
            int r3 = r21 + 3
            if (r3 > r2) goto L87
            int r4 = r21 + (-1)
            boolean r4 = com.alibaba.fastjson2.util.IOUtils.notTRUE(r1, r4)
            if (r4 != 0) goto L87
            r4 = 26
            if (r3 != r2) goto L19
            r5 = r3
        L17:
            r3 = r4
            goto L1d
        L19:
            int r5 = r21 + 4
            char r3 = r1[r3]
        L1d:
            r6 = 0
            r8 = 4294981376(0x100003700, double:2.1220027474E-314)
            r10 = 1
            r12 = 32
            if (r3 > r12) goto L3e
            long r13 = r10 << r3
            long r13 = r13 & r8
            int r13 = (r13 > r6 ? 1 : (r13 == r6 ? 0 : -1))
            if (r13 == 0) goto L3e
            if (r5 != r2) goto L34
            goto L17
        L34:
            int r3 = r5 + 1
            char r5 = r1[r5]
            r18 = r5
            r5 = r3
            r3 = r18
            goto L1d
        L3e:
            r13 = 44
            r14 = 93
            r15 = 125(0x7d, float:1.75E-43)
            if (r3 != r13) goto L74
            if (r5 != r2) goto L4b
            r3 = r5
            r5 = r4
            goto L4f
        L4b:
            int r3 = r5 + 1
            char r5 = r1[r5]
        L4f:
            r18 = r5
            r5 = r3
            r3 = r18
        L54:
            if (r3 > r12) goto L67
            long r16 = r10 << r3
            long r16 = r16 & r8
            int r13 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1))
            if (r13 == 0) goto L67
            if (r5 != r2) goto L62
            r3 = r4
            goto L54
        L62:
            int r3 = r5 + 1
            char r5 = r1[r5]
            goto L4f
        L67:
            if (r3 == r15) goto L6f
            if (r3 == r14) goto L6f
            if (r3 == r4) goto L6f
            r1 = 1
            goto L81
        L6f:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r5, r3)
            throw r0
        L74:
            if (r3 == r15) goto L80
            if (r3 == r14) goto L80
            if (r3 != r4) goto L7b
            goto L80
        L7b:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r5, r3)
            throw r0
        L80:
            r1 = 0
        L81:
            r0.comma = r1
            char r1 = (char) r3
            r0.ch = r1
            return r5
        L87:
            com.alibaba.fastjson2.JSONException r0 = r0.error()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.skipTrue(com.alibaba.fastjson2.JSONReaderUTF16, char[], int, int):int");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x0031 -> B:9:0x0017). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:12:0x0028
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private static int skipNull(com.alibaba.fastjson2.JSONReaderUTF16 r19, char[] r20, int r21, int r22) {
        /*
            r0 = r19
            r1 = r20
            r2 = r22
            int r3 = r21 + 3
            if (r3 > r2) goto L87
            int r4 = r21 + (-1)
            boolean r4 = com.alibaba.fastjson2.util.IOUtils.notNULL(r1, r4)
            if (r4 != 0) goto L87
            r4 = 26
            if (r3 != r2) goto L19
            r5 = r3
        L17:
            r3 = r4
            goto L1d
        L19:
            int r5 = r21 + 4
            char r3 = r1[r3]
        L1d:
            r6 = 0
            r8 = 4294981376(0x100003700, double:2.1220027474E-314)
            r10 = 1
            r12 = 32
            if (r3 > r12) goto L3e
            long r13 = r10 << r3
            long r13 = r13 & r8
            int r13 = (r13 > r6 ? 1 : (r13 == r6 ? 0 : -1))
            if (r13 == 0) goto L3e
            if (r5 != r2) goto L34
            goto L17
        L34:
            int r3 = r5 + 1
            char r5 = r1[r5]
            r18 = r5
            r5 = r3
            r3 = r18
            goto L1d
        L3e:
            r13 = 44
            r14 = 93
            r15 = 125(0x7d, float:1.75E-43)
            if (r3 != r13) goto L74
            if (r5 != r2) goto L4b
            r3 = r5
            r5 = r4
            goto L4f
        L4b:
            int r3 = r5 + 1
            char r5 = r1[r5]
        L4f:
            r18 = r5
            r5 = r3
            r3 = r18
        L54:
            if (r3 > r12) goto L67
            long r16 = r10 << r3
            long r16 = r16 & r8
            int r13 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1))
            if (r13 == 0) goto L67
            if (r5 != r2) goto L62
            r3 = r4
            goto L54
        L62:
            int r3 = r5 + 1
            char r5 = r1[r5]
            goto L4f
        L67:
            if (r3 == r15) goto L6f
            if (r3 == r14) goto L6f
            if (r3 == r4) goto L6f
            r1 = 1
            goto L81
        L6f:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r5, r3)
            throw r0
        L74:
            if (r3 == r15) goto L80
            if (r3 == r14) goto L80
            if (r3 != r4) goto L7b
            goto L80
        L7b:
            com.alibaba.fastjson2.JSONException r0 = r0.error(r5, r3)
            throw r0
        L80:
            r1 = 0
        L81:
            r0.comma = r1
            char r1 = (char) r3
            r0.ch = r1
            return r5
        L87:
            com.alibaba.fastjson2.JSONException r0 = r0.error()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.skipNull(com.alibaba.fastjson2.JSONReaderUTF16, char[], int, int):int");
    }

    private static int skipSet(JSONReaderUTF16 jSONReaderUTF16, char[] cArr, int i, int i2) {
        if (nextIfSet(jSONReaderUTF16, cArr, i, i2)) {
            return skipArray(jSONReaderUTF16, cArr, jSONReaderUTF16.offset, i2);
        }
        throw jSONReaderUTF16.error();
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:17:0x0030 -> B:11:0x0017). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:14:0x001f
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private static boolean nextIfSet(com.alibaba.fastjson2.JSONReaderUTF16 r7, char[] r8, int r9, int r10) {
        /*
            int r0 = r9 + 1
            if (r0 >= r10) goto L41
            char r1 = r8[r9]
            r2 = 101(0x65, float:1.42E-43)
            if (r1 != r2) goto L41
            char r0 = r8[r0]
            r1 = 116(0x74, float:1.63E-43)
            if (r0 != r1) goto L41
            int r0 = r9 + 2
            r1 = 26
            if (r0 != r10) goto L19
            r9 = r0
        L17:
            r0 = r1
            goto L1d
        L19:
            int r9 = r9 + 3
            char r0 = r8[r0]
        L1d:
            r2 = 32
            if (r0 > r2) goto L3b
            r2 = 1
            long r2 = r2 << r0
            r4 = 4294981376(0x100003700, double:2.1220027474E-314)
            long r2 = r2 & r4
            r4 = 0
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L3b
            if (r9 != r10) goto L33
            goto L17
        L33:
            int r0 = r9 + 1
            char r9 = r8[r9]
            r6 = r0
            r0 = r9
            r9 = r6
            goto L1d
        L3b:
            r7.offset = r9
            r7.ch = r0
            r7 = 1
            return r7
        L41:
            r7 = 0
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.nextIfSet(com.alibaba.fastjson2.JSONReaderUTF16, char[], int, int):boolean");
    }

    private static int skipValue(JSONReaderUTF16 jSONReaderUTF16, char[] cArr, int i, int i2) {
        char c = jSONReaderUTF16.ch;
        if (c == '\"' || c == '\'') {
            return skipString(jSONReaderUTF16, cArr, i, i2);
        }
        if (c == 'S') {
            return skipSet(jSONReaderUTF16, cArr, i, i2);
        }
        if (c == '[') {
            return skipArray(jSONReaderUTF16, cArr, i, i2);
        }
        if (c == 'f') {
            return skipFalse(jSONReaderUTF16, cArr, i, i2);
        }
        if (c == 'n') {
            return skipNull(jSONReaderUTF16, cArr, i, i2);
        }
        if (c == 't') {
            return skipTrue(jSONReaderUTF16, cArr, i, i2);
        }
        if (c == '{') {
            return skipObject(jSONReaderUTF16, cArr, i, i2);
        }
        return skipNumber(jSONReaderUTF16, cArr, i, i2);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final void skipValue() {
        this.offset = skipValue(this, this.chars, this.offset, this.end);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final void skipComment() {
        boolean z;
        boolean z2;
        char c;
        int i = this.offset;
        int i2 = i + 1;
        if (i2 >= this.end) {
            throw new JSONException(info());
        }
        char[] cArr = this.chars;
        char c2 = cArr[i];
        if (c2 == '*') {
            z = true;
        } else {
            if (c2 != '/') {
                throw new JSONException(info("parse comment error"));
            }
            z = false;
        }
        int i3 = i + 2;
        char c3 = cArr[i2];
        while (true) {
            if (z) {
                if (c3 == '*' && i3 <= this.end && cArr[i3] == '/') {
                    i3++;
                }
            } else {
                z2 = c3 == '\n';
            }
            c = JSONLexer.EOI;
            if (z2) {
                if (i3 < this.end) {
                    char c4 = cArr[i3];
                    while (c4 <= ' ' && ((1 << c4) & 4294981376L) != 0) {
                        i3++;
                        if (i3 >= this.end) {
                            break;
                        } else {
                            c4 = cArr[i3];
                        }
                    }
                    c = c4;
                    i3++;
                }
            } else {
                if (i3 >= this.end) {
                    break;
                }
                c3 = cArr[i3];
                i3++;
            }
        }
        this.ch = c;
        this.offset = i3;
        if (c == '/') {
            skipComment();
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x0055 -> B:11:0x0044). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:216:0x0326 -> B:209:0x0311). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:13:0x004b
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final void readNumber0() {
        /*
            Method dump skipped, instruction units count: 879
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readNumber0():void");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean readIfNull() {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        if (this.ch != 'n' || cArr[i2] != 'u' || cArr[i2 + 1] != 'l' || cArr[i2 + 2] != 'l') {
            return false;
        }
        int i3 = i2 + 3;
        char c2 = i3 == this.end ? (char) 26 : cArr[i3];
        int i4 = i2 + 4;
        while (c2 <= ' ' && ((1 << c2) & 4294981376L) != 0) {
            if (i4 == this.end) {
                c2 = 26;
            } else {
                c2 = cArr[i4];
                i4++;
            }
        }
        boolean z = c2 == ',';
        this.comma = z;
        if (z) {
            if (i4 == this.end) {
                i = i4;
                c = 26;
            } else {
                i = i4 + 1;
                c = cArr[i4];
            }
            loop1: while (true) {
                int i5 = i;
                c2 = c;
                i4 = i5;
                while (c2 <= ' ' && ((1 << c2) & 4294981376L) != 0) {
                    if (i4 == this.end) {
                        c2 = 26;
                    }
                }
                i = i4 + 1;
                c = cArr[i4];
            }
        }
        this.ch = c2;
        this.offset = i4;
        return true;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:50:0x009f -> B:44:0x0090). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:46:0x0094
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final java.util.Date readNullOrNewDate() {
        /*
            Method dump skipped, instruction units count: 396
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readNullOrNewDate():java.util.Date");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean isNull() {
        return this.ch == 'n' && this.offset < this.end && this.chars[this.offset] == 'u';
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfNull() {
        if (this.ch != 'n' || this.offset + 2 >= this.end || this.chars[this.offset] != 'u') {
            return false;
        }
        readNull();
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final void readNull() {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        if (cArr[i2] == 'u' && cArr[i2 + 1] == 'l' && cArr[i2 + 2] == 'l') {
            int i3 = i2 + 3;
            char c2 = i3 == this.end ? (char) 26 : cArr[i3];
            int i4 = i2 + 4;
            while (c2 <= ' ' && ((1 << c2) & 4294981376L) != 0) {
                if (i4 == this.end) {
                    c2 = 26;
                } else {
                    c2 = cArr[i4];
                    i4++;
                }
            }
            boolean z = c2 == ',';
            this.comma = z;
            if (z) {
                if (i4 == this.end) {
                    i = i4;
                    c = 26;
                } else {
                    i = i4 + 1;
                    c = cArr[i4];
                }
                loop1: while (true) {
                    int i5 = i;
                    c2 = c;
                    i4 = i5;
                    while (c2 <= ' ' && ((1 << c2) & 4294981376L) != 0) {
                        if (i4 == this.end) {
                            c2 = 26;
                        }
                    }
                    i = i4 + 1;
                    c = cArr[i4];
                }
            }
            this.ch = c2;
            this.offset = i4;
            return;
        }
        throw new JSONException("json syntax error, not match null, offset " + i2);
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x0037 -> B:9:0x001b). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:12:0x002c
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final double readNaN() {
        /*
            r14 = this;
            char[] r0 = r14.chars
            int r1 = r14.offset
            char r2 = r0[r1]
            r3 = 97
            if (r2 != r3) goto L77
            int r2 = r1 + 1
            char r2 = r0[r2]
            r3 = 78
            if (r2 != r3) goto L77
            int r2 = r1 + 2
            int r3 = r14.end
            r4 = 26
            if (r2 != r3) goto L1d
            r1 = r2
        L1b:
            r2 = r4
            goto L21
        L1d:
            int r1 = r1 + 3
            char r2 = r0[r2]
        L21:
            r5 = 0
            r7 = 4294981376(0x100003700, double:2.1220027474E-314)
            r9 = 1
            r3 = 32
            if (r2 > r3) goto L42
            long r11 = r9 << r2
            long r11 = r11 & r7
            int r11 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1))
            if (r11 == 0) goto L42
            int r2 = r14.end
            if (r1 < r2) goto L3a
            goto L1b
        L3a:
            int r2 = r1 + 1
            char r1 = r0[r1]
            r13 = r2
            r2 = r1
            r1 = r13
            goto L21
        L42:
            r11 = 44
            if (r2 != r11) goto L48
            r11 = 1
            goto L49
        L48:
            r11 = 0
        L49:
            r14.comma = r11
            if (r11 == 0) goto L6f
            int r2 = r14.end
            if (r1 < r2) goto L54
            r2 = r1
            r1 = r4
            goto L58
        L54:
            int r2 = r1 + 1
            char r1 = r0[r1]
        L58:
            r13 = r2
            r2 = r1
            r1 = r13
        L5b:
            if (r2 > r3) goto L6f
            long r11 = r9 << r2
            long r11 = r11 & r7
            int r11 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1))
            if (r11 == 0) goto L6f
            int r2 = r14.end
            if (r1 < r2) goto L6a
            r2 = r4
            goto L5b
        L6a:
            int r2 = r1 + 1
            char r1 = r0[r1]
            goto L58
        L6f:
            char r0 = (char) r2
            r14.ch = r0
            r14.offset = r1
            r0 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            return r0
        L77:
            com.alibaba.fastjson2.JSONException r0 = new com.alibaba.fastjson2.JSONException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "json syntax error, not NaN "
            r2.<init>(r3)
            java.lang.StringBuilder r1 = r2.append(r1)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readNaN():double");
    }

    /* JADX WARN: Path cross not found for [B:201:0x02b8, B:251:?], limit reached: 255 */
    /* JADX WARN: Removed duplicated region for block: B:242:0x008b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0092 A[LOOP:0: B:26:0x0058->B:41:0x0092, LOOP_END] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:212:0x02de -> B:203:0x02bc). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:208:0x02d1
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.math.BigDecimal readBigDecimal() {
        /*
            Method dump skipped, instruction units count: 808
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readBigDecimal():java.math.BigDecimal");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:39:0x00d2 -> B:33:0x00b6). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:36:0x00bf
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final java.util.UUID readUUID() {
        /*
            Method dump skipped, instruction units count: 286
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readUUID():java.util.UUID");
    }

    private static long parse4Nibbles(char[] cArr, int i) {
        byte[] bArr = JSONFactory.NIBBLES;
        char c = cArr[i];
        char c2 = cArr[i + 1];
        char c3 = cArr[i + 2];
        if ((c | c2 | c3 | cArr[i + 3]) > 255) {
            return -1L;
        }
        return bArr[r5] | (bArr[c] << 12) | (bArr[c2] << 8) | (bArr[c3] << 4);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final int getStringLength() {
        int i;
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("string length only support string input");
        }
        char c = this.ch;
        int i2 = this.offset;
        char[] cArr = this.chars;
        int i3 = i2 + 8;
        if (i3 >= this.end || i3 >= cArr.length || cArr[i2] == c || cArr[i2 + 1] == c || cArr[i2 + 2] == c || cArr[i2 + 3] == c || cArr[i2 + 4] == c || cArr[i2 + 5] == c || cArr[i2 + 6] == c || cArr[i2 + 7] == c) {
            i = 0;
        } else {
            i = 8;
            i2 = i3;
        }
        while (i2 < this.end && cArr[i2] != c) {
            i2++;
            i++;
        }
        return i;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalDateTime readLocalDateTime14() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        LocalDateTime localDateTime14 = DateUtils.parseLocalDateTime14(this.chars, this.offset);
        if (localDateTime14 == null) {
            return null;
        }
        this.offset += 15;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localDateTime14;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalDateTime readLocalDateTime12() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        LocalDateTime localDateTime12 = DateUtils.parseLocalDateTime12(this.chars, this.offset);
        if (localDateTime12 == null) {
            return null;
        }
        this.offset += 13;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localDateTime12;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalDateTime readLocalDateTime16() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        LocalDateTime localDateTime16 = DateUtils.parseLocalDateTime16(this.chars, this.offset);
        if (localDateTime16 == null) {
            return null;
        }
        this.offset += 17;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localDateTime16;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalDateTime readLocalDateTime17() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        LocalDateTime localDateTime17 = DateUtils.parseLocalDateTime17(this.chars, this.offset);
        if (localDateTime17 == null) {
            return null;
        }
        this.offset += 18;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localDateTime17;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalDateTime readLocalDateTime18() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        LocalDateTime localDateTime18 = DateUtils.parseLocalDateTime18(this.chars, this.offset);
        this.offset += 19;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localDateTime18;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalTime readLocalTime5() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        LocalTime localTime5 = DateUtils.parseLocalTime5(this.chars, this.offset);
        if (localTime5 == null) {
            return null;
        }
        this.offset += 6;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localTime5;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalTime readLocalTime6() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        LocalTime localTime6 = DateUtils.parseLocalTime6(this.chars, this.offset);
        if (localTime6 == null) {
            return null;
        }
        this.offset += 7;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localTime6;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalTime readLocalTime7() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        LocalTime localTime7 = DateUtils.parseLocalTime7(this.chars, this.offset);
        if (localTime7 == null) {
            return null;
        }
        this.offset += 8;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localTime7;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalTime readLocalTime8() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        LocalTime localTime8 = DateUtils.parseLocalTime8(this.chars, this.offset);
        if (localTime8 == null) {
            return null;
        }
        this.offset += 9;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localTime8;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalTime readLocalTime9() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        LocalTime localTime8 = DateUtils.parseLocalTime8(this.chars, this.offset);
        if (localTime8 == null) {
            return null;
        }
        this.offset += 10;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localTime8;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final LocalDate readLocalDate8() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localDate only support string input");
        }
        try {
            LocalDate localDate8 = DateUtils.parseLocalDate8(this.chars, this.offset);
            this.offset += 9;
            next();
            boolean z = this.ch == ',';
            this.comma = z;
            if (z) {
                next();
            }
            return localDate8;
        } catch (DateTimeException e) {
            throw new JSONException(info("read date error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final LocalDate readLocalDate9() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localDate only support string input");
        }
        try {
            LocalDate localDate9 = DateUtils.parseLocalDate9(this.chars, this.offset);
            this.offset += 10;
            next();
            boolean z = this.ch == ',';
            this.comma = z;
            if (z) {
                next();
            }
            return localDate9;
        } catch (DateTimeException e) {
            throw new JSONException(info("read date error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final LocalDate readLocalDate10() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localDate only support string input");
        }
        try {
            LocalDate localDate10 = DateUtils.parseLocalDate10(this.chars, this.offset);
            if (localDate10 == null) {
                return null;
            }
            this.offset += 11;
            next();
            boolean z = this.ch == ',';
            this.comma = z;
            if (z) {
                next();
            }
            return localDate10;
        } catch (DateTimeException e) {
            throw new JSONException(info("read date error"), e);
        }
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final LocalDate readLocalDate11() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localDate only support string input");
        }
        LocalDate localDate11 = DateUtils.parseLocalDate11(this.chars, this.offset);
        if (localDate11 == null) {
            return null;
        }
        this.offset += 12;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localDate11;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final LocalDate readLocalDate() {
        LocalDate localDateOf;
        char[] cArr = this.chars;
        int i = this.offset;
        if ((this.ch == '\"' || this.ch == '\'') && !this.context.formatComplex) {
            char c = this.ch;
            int i2 = i + 10;
            if (i2 < cArr.length && i2 < this.end && cArr[i + 4] == '-' && cArr[i + 7] == '-' && cArr[i2] == c) {
                int iDigit4 = IOUtils.digit4(cArr, i);
                int iDigit2 = IOUtils.digit2(cArr, i + 5);
                int iDigit22 = IOUtils.digit2(cArr, i + 8);
                int i3 = iDigit4 | iDigit2 | iDigit22;
                if (i3 < 0) {
                    throw new JSONException(info("read date error"));
                }
                if (i3 == 0) {
                    localDateOf = null;
                } else {
                    try {
                        localDateOf = LocalDate.of(iDigit4, iDigit2, iDigit22);
                    } catch (DateTimeException e) {
                        throw new JSONException(info("read date error"), e);
                    }
                }
                this.offset = i + 11;
                next();
                boolean z = this.ch == ',';
                this.comma = z;
                if (z) {
                    next();
                }
                return localDateOf;
            }
            LocalDate localDate0 = readLocalDate0(i, cArr, c);
            if (localDate0 != null) {
                return localDate0;
            }
        }
        return super.readLocalDate();
    }

    private LocalDate readLocalDate0(int i, char[] cArr, char c) {
        int i2;
        int iMin = Math.min(i + 17, this.end);
        int i3 = -1;
        for (int i4 = i; i4 < iMin; i4++) {
            if (cArr[i4] == c) {
                i3 = i4;
            }
        }
        if (i3 == -1 || (i2 = i3 - i) <= 10 || cArr[i3 - 6] != '-' || cArr[i3 - 3] != '-') {
            return null;
        }
        LocalDate localDateOf = LocalDate.of(TypeUtils.parseInt(cArr, i, i2 - 6), IOUtils.digit2(cArr, i3 - 5), IOUtils.digit2(cArr, i3 - 2));
        this.offset = i3 + 1;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localDateOf;
    }

    /* JADX WARN: Removed duplicated region for block: B:80:0x012b  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:75:0x0116 -> B:69:0x0105). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:72:0x010c
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.time.OffsetDateTime readOffsetDateTime() {
        /*
            Method dump skipped, instruction units count: 321
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readOffsetDateTime():java.time.OffsetDateTime");
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x009c, code lost:
    
        r8 = 0;
     */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.time.OffsetTime readOffsetTime() {
        /*
            Method dump skipped, instruction units count: 259
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readOffsetTime():java.time.OffsetTime");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final ZonedDateTime readZonedDateTimeX(int i) {
        ZonedDateTime zonedDateTime;
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        if (i < 19) {
            return null;
        }
        char[] cArr = this.chars;
        if (i == 30 && cArr[this.offset + 29] == 'Z') {
            zonedDateTime = ZonedDateTime.of(DateUtils.parseLocalDateTime29(cArr, this.offset), ZoneOffset.UTC);
        } else if (i == 29 && cArr[this.offset + 28] == 'Z') {
            zonedDateTime = ZonedDateTime.of(DateUtils.parseLocalDateTime28(cArr, this.offset), ZoneOffset.UTC);
        } else if (i == 28 && cArr[this.offset + 27] == 'Z') {
            zonedDateTime = ZonedDateTime.of(DateUtils.parseLocalDateTime27(cArr, this.offset), ZoneOffset.UTC);
        } else if (i == 27 && cArr[this.offset + 26] == 'Z') {
            zonedDateTime = ZonedDateTime.of(DateUtils.parseLocalDateTime26(cArr, this.offset), ZoneOffset.UTC);
        } else {
            zonedDateTime = DateUtils.parseZonedDateTime(cArr, this.offset, i, this.context.zoneId);
        }
        if (zonedDateTime == null) {
            return null;
        }
        this.offset += i + 1;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return zonedDateTime;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalDateTime readLocalDateTime19() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        LocalDateTime localDateTime19 = DateUtils.parseLocalDateTime19(this.chars, this.offset);
        if (localDateTime19 == null) {
            return null;
        }
        this.offset += 20;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localDateTime19;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalDateTime readLocalDateTime20() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        LocalDateTime localDateTime20 = DateUtils.parseLocalDateTime20(this.chars, this.offset);
        if (localDateTime20 == null) {
            return null;
        }
        this.offset += 21;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localDateTime20;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final long readMillis19() {
        char c = this.ch;
        if (c != '\"' && c != '\'') {
            throw new JSONException("date only support string input");
        }
        if (this.offset + 18 >= this.end) {
            this.wasNull = true;
            return 0L;
        }
        long millis19 = DateUtils.parseMillis19(this.chars, this.offset, this.context.zoneId);
        if (this.chars[this.offset + 19] != c) {
            throw new JSONException(info("illegal date input"));
        }
        this.offset += 20;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return millis19;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalDateTime readLocalDateTimeX(int i) {
        LocalDateTime localDateTimeX;
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        if (this.chars[(this.offset + i) - 1] == 'Z') {
            localDateTimeX = DateUtils.parseZonedDateTime(this.chars, this.offset, i).toInstant().atZone(this.context.getZoneId()).toLocalDateTime();
        } else {
            localDateTimeX = DateUtils.parseLocalDateTimeX(this.chars, this.offset, i);
        }
        if (localDateTimeX == 0) {
            return null;
        }
        this.offset += i + 1;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localDateTimeX;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalTime readLocalTime10() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        LocalTime localTime10 = DateUtils.parseLocalTime10(this.chars, this.offset);
        if (localTime10 == null) {
            return null;
        }
        this.offset += 11;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localTime10;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalTime readLocalTime11() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        LocalTime localTime11 = DateUtils.parseLocalTime11(this.chars, this.offset);
        if (localTime11 == null) {
            return null;
        }
        this.offset += 12;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localTime11;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalTime readLocalTime12() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        LocalTime localTime12 = DateUtils.parseLocalTime12(this.chars, this.offset);
        if (localTime12 == null) {
            return null;
        }
        this.offset += 13;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localTime12;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalTime readLocalTime15() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        LocalTime localTime15 = DateUtils.parseLocalTime15(this.chars, this.offset);
        if (localTime15 == null) {
            return null;
        }
        this.offset += 16;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localTime15;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected final LocalTime readLocalTime18() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        LocalTime localTime18 = DateUtils.parseLocalTime18(this.chars, this.offset);
        if (localTime18 == null) {
            return null;
        }
        this.offset += 19;
        next();
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return localTime18;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x0043 -> B:14:0x0027). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:17:0x0038
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final java.lang.String readPattern() {
        /*
            r15 = this;
            char r0 = r15.ch
            r1 = 47
            if (r0 != r1) goto L80
            char[] r0 = r15.chars
            int r2 = r15.offset
            r3 = r2
        Lb:
            int r4 = r15.end
            if (r3 >= r4) goto L17
            char r4 = r0[r3]
            if (r4 != r1) goto L14
            goto L17
        L14:
            int r3 = r3 + 1
            goto Lb
        L17:
            java.lang.String r1 = new java.lang.String
            int r4 = r3 - r2
            r1.<init>(r0, r2, r4)
            int r2 = r3 + 1
            int r4 = r15.end
            r5 = 26
            if (r2 != r4) goto L29
            r3 = r2
        L27:
            r2 = r5
            goto L2d
        L29:
            int r3 = r3 + 2
            char r2 = r0[r2]
        L2d:
            r6 = 0
            r8 = 4294981376(0x100003700, double:2.1220027474E-314)
            r10 = 1
            r4 = 32
            if (r2 > r4) goto L4e
            long r12 = r10 << r2
            long r12 = r12 & r8
            int r12 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
            if (r12 == 0) goto L4e
            int r2 = r15.end
            if (r3 != r2) goto L46
            goto L27
        L46:
            int r2 = r3 + 1
            char r3 = r0[r3]
            r14 = r3
            r3 = r2
            r2 = r14
            goto L2d
        L4e:
            r12 = 44
            if (r2 != r12) goto L54
            r12 = 1
            goto L55
        L54:
            r12 = 0
        L55:
            r15.comma = r12
            if (r12 == 0) goto L7b
            int r2 = r15.end
            if (r3 != r2) goto L60
            r2 = r3
            r3 = r5
            goto L64
        L60:
            int r2 = r3 + 1
            char r3 = r0[r3]
        L64:
            r14 = r3
            r3 = r2
            r2 = r14
        L67:
            if (r2 > r4) goto L7b
            long r12 = r10 << r2
            long r12 = r12 & r8
            int r12 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
            if (r12 == 0) goto L7b
            int r2 = r15.end
            if (r3 != r2) goto L76
            r2 = r5
            goto L67
        L76:
            int r2 = r3 + 1
            char r3 = r0[r3]
            goto L64
        L7b:
            r15.offset = r3
            r15.ch = r2
            return r1
        L80:
            com.alibaba.fastjson2.JSONException r0 = new com.alibaba.fastjson2.JSONException
            java.lang.String r1 = "illegal pattern"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readPattern():java.lang.String");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:101:0x0152 -> B:94:0x0140). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:84:0x0126 -> B:77:0x010c). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:80:0x011b
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean readBoolValue() {
        /*
            Method dump skipped, instruction units count: 467
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderUTF16.readBoolValue():boolean");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public String info(String str) {
        int i = 1;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (!(i2 < this.offset) || !(i2 < this.end)) {
                break;
            }
            if (this.chars[i2] == '\n') {
                i++;
                i3 = 0;
            }
            i2++;
            i3++;
        }
        StringBuilder sb = new StringBuilder();
        if (str != null && !str.isEmpty()) {
            sb.append(str).append(", ");
        }
        sb.append("offset ").append(this.offset).append(", character ").append(this.ch).append(", line ").append(i).append(", column ").append(i3).append(", fastjson-version 2.0.58").append(i <= 1 ? TokenParser.SP : '\n');
        sb.append(this.chars, this.start, Math.min(this.length, 65535));
        return sb.toString();
    }

    @Override // com.alibaba.fastjson2.JSONReader, java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        if (this.cacheIndex != -1 && this.chars.length < 8388608) {
            JSONFactory.CHARS_UPDATER.lazySet(JSONFactory.CACHE_ITEMS[this.cacheIndex], this.chars);
        }
        Closeable closeable = this.input;
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final int getRawInt() {
        int i = this.offset + 3;
        char[] cArr = this.chars;
        if (i < cArr.length) {
            return getInt(cArr, this.offset - 1);
        }
        return 0;
    }

    static int getInt(char[] cArr, int i) {
        long longUnaligned = IOUtils.getLongUnaligned(cArr, i);
        if ((CHAR_MASK & longUnaligned) != 0) {
            return 0;
        }
        if (JDKUtils.BIG_ENDIAN) {
            longUnaligned >>= 8;
        }
        return (int) (((longUnaligned & 71776119061217280L) >> 24) | ((16711680 & longUnaligned) >> 8) | (255 & longUnaligned) | ((1095216660480L & longUnaligned) >> 16));
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final long getRawLong() {
        int i = this.offset + 7;
        char[] cArr = this.chars;
        if (i < cArr.length) {
            return getLong(cArr, this.offset - 1);
        }
        return 0L;
    }

    static long getLong(char[] cArr, int i) {
        long longUnaligned = IOUtils.getLongUnaligned(cArr, i);
        long longUnaligned2 = IOUtils.getLongUnaligned(cArr, i + 4);
        if (((longUnaligned | longUnaligned2) & CHAR_MASK) != 0) {
            return 0L;
        }
        if (JDKUtils.BIG_ENDIAN) {
            longUnaligned >>= 8;
            longUnaligned2 >>= 8;
        }
        return ((longUnaligned & 71776119061217280L) >> 24) | (longUnaligned & 255) | ((longUnaligned & 16711680) >> 8) | ((longUnaligned & 1095216660480L) >> 16) | ((255 & longUnaligned2) << 32) | ((longUnaligned2 & 16711680) << 24) | ((longUnaligned2 & 1095216660480L) << 16) | ((longUnaligned2 & 71776119061217280L) << 8);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName8Match0() {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset + 7;
        if (i2 == this.end) {
            this.ch = JSONLexer.EOI;
            return false;
        }
        while (true) {
            i = i2 + 1;
            c = cArr[i2];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i2 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName8Match1() {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 8;
        if (i3 >= this.end || cArr[i2 + 7] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName8Match2() {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 9;
        if (i3 >= this.end || cArr[i2 + 7] != '\"' || cArr[i2 + 8] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match2() {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 4;
        if (i3 >= this.end || cArr[i2 + 3] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match3() {
        char c;
        char[] cArr = this.chars;
        int i = this.offset;
        int i2 = i + 5;
        if (i2 >= this.end || cArr[i + 3] != '\"' || cArr[i + 4] != ':') {
            return false;
        }
        while (true) {
            c = cArr[i2];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i2++;
        }
        this.offset = i2 + 1;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match4(byte b) {
        char c;
        char[] cArr = this.chars;
        int i = this.offset;
        int i2 = i + 6;
        if (i2 >= this.end || cArr[i + 3] != b || cArr[i + 4] != '\"' || cArr[i + 5] != ':') {
            return false;
        }
        while (true) {
            c = cArr[i2];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i2++;
        }
        this.offset = i2 + 1;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match5(int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 7;
        if (i4 >= this.end || getInt(cArr, i3 + 3) != i) {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match6(int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 8;
        if (i4 >= this.end || getInt(cArr, i3 + 3) != i || cArr[i3 + 7] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match7(int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 9;
        if (i4 >= this.end || getInt(cArr, i3 + 3) != i || cArr[i3 + 7] != '\"' || cArr[i3 + 8] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match8(int i, byte b) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 10;
        if (i4 >= this.end || getInt(cArr, i3 + 3) != i || cArr[i3 + 7] != b || cArr[i3 + 8] != '\"' || cArr[i3 + 9] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match9(long j) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 11;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j) {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match10(long j) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 12;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || cArr[i2 + 11] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match11(long j) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 13;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || cArr[i2 + 11] != '\"' || cArr[i2 + 12] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match12(long j, byte b) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 14;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || cArr[i2 + 11] != b || cArr[i2 + 12] != '\"' || cArr[i2 + 13] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match13(long j, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 15;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getInt(cArr, i3 + 11) != i) {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match14(long j, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 16;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getInt(cArr, i3 + 11) != i || cArr[i3 + 15] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match15(long j, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 17;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getInt(cArr, i3 + 11) != i || cArr[i3 + 15] != '\"' || cArr[i3 + 16] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match16(long j, int i, byte b) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 18;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getInt(cArr, i3 + 11) != i || cArr[i3 + 15] != b || cArr[i3 + 16] != '\"' || cArr[i3 + 17] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match17(long j, long j2) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 19;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2) {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match18(long j, long j2) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 20;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || cArr[i2 + 19] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match19(long j, long j2) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 21;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || cArr[i2 + 19] != '\"' || cArr[i2 + 20] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match20(long j, long j2, byte b) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 22;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || cArr[i2 + 19] != b || cArr[i2 + 20] != '\"' || cArr[i2 + 21] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match21(long j, long j2, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 23;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getInt(cArr, i3 + 19) != i) {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match22(long j, long j2, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 24;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getInt(cArr, i3 + 19) != i || cArr[i3 + 23] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match23(long j, long j2, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 25;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getInt(cArr, i3 + 19) != i || cArr[i3 + 23] != '\"' || cArr[i3 + 24] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match24(long j, long j2, int i, byte b) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 26;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getInt(cArr, i3 + 19) != i || cArr[i3 + 23] != b || cArr[i3 + 24] != '\"' || cArr[i3 + 25] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match25(long j, long j2, long j3) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 27;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3) {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match26(long j, long j2, long j3) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 28;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3 || cArr[i2 + 27] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = this.chars[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match27(long j, long j2, long j3) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 29;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3 || cArr[i2 + 27] != '\"' || cArr[i2 + 28] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = this.chars[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match28(long j, long j2, long j3, byte b) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 30;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3 || cArr[i2 + 27] != b || cArr[i2 + 28] != '\"' || cArr[i2 + 29] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match29(long j, long j2, long j3, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 31;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getLong(cArr, i3 + 19) != j3 || getInt(cArr, i3 + 27) != i) {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match30(long j, long j2, long j3, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 32;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getLong(cArr, i3 + 19) != j3 || getInt(cArr, i3 + 27) != i || cArr[i3 + 31] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match31(long j, long j2, long j3, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 33;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getLong(cArr, i3 + 19) != j3 || getInt(cArr, i3 + 27) != i || cArr[i3 + 31] != '\"' || cArr[i3 + 32] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match32(long j, long j2, long j3, int i, byte b) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 34;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getLong(cArr, i3 + 19) != j3 || getInt(cArr, i3 + 27) != i || cArr[i3 + 31] != b || cArr[i3 + 32] != '\"' || cArr[i3 + 33] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match33(long j, long j2, long j3, long j4) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 35;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3 || getLong(cArr, i2 + 27) != j4) {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match34(long j, long j2, long j3, long j4) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 36;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3 || getLong(cArr, i2 + 27) != j4 || cArr[i2 + 35] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match35(long j, long j2, long j3, long j4) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 37;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3 || getLong(cArr, i2 + 27) != j4 || cArr[i2 + 35] != '\"' || cArr[i2 + 36] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match36(long j, long j2, long j3, long j4, byte b) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 38;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3 || getLong(cArr, i2 + 27) != j4 || cArr[i2 + 35] != b || cArr[i2 + 36] != '\"' || cArr[i2 + 37] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match37(long j, long j2, long j3, long j4, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 39;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getLong(cArr, i3 + 19) != j3 || getLong(cArr, i3 + 27) != j4 || getInt(cArr, i3 + 35) != i) {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match38(long j, long j2, long j3, long j4, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 40;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getLong(cArr, i3 + 19) != j3 || getLong(cArr, i3 + 27) != j4 || getInt(cArr, i3 + 35) != i || cArr[i3 + 39] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match39(long j, long j2, long j3, long j4, int i) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 41;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getLong(cArr, i3 + 19) != j3 || getLong(cArr, i3 + 27) != j4 || getInt(cArr, i3 + 35) != i || cArr[i3 + 39] != '\"' || cArr[i3 + 40] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match40(long j, long j2, long j3, long j4, int i, byte b) {
        int i2;
        char c;
        char[] cArr = this.chars;
        int i3 = this.offset;
        int i4 = i3 + 42;
        if (i4 >= this.end || getLong(cArr, i3 + 3) != j || getLong(cArr, i3 + 11) != j2 || getLong(cArr, i3 + 19) != j3 || getLong(cArr, i3 + 27) != j4 || getInt(cArr, i3 + 35) != i || cArr[i3 + 39] != b || cArr[i3 + 40] != '\"' || cArr[i3 + 41] != ':') {
            return false;
        }
        while (true) {
            i2 = i4 + 1;
            c = cArr[i4];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i4 = i2;
        }
        this.offset = i2;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match41(long j, long j2, long j3, long j4, long j5) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 43;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3 || getLong(cArr, i2 + 27) != j4 || getLong(cArr, i2 + 35) != j5) {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match42(long j, long j2, long j3, long j4, long j5) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 44;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3 || getLong(cArr, i2 + 27) != j4 || getLong(cArr, i2 + 35) != j5 || cArr[i2 + 43] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfName4Match43(long j, long j2, long j3, long j4, long j5) {
        int i;
        char c;
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 45;
        if (i3 >= this.end || getLong(cArr, i2 + 3) != j || getLong(cArr, i2 + 11) != j2 || getLong(cArr, i2 + 19) != j3 || getLong(cArr, i2 + 27) != j4 || getLong(cArr, i2 + 35) != j5 || cArr[i2 + 43] != '\"' || cArr[i2 + 44] != ':') {
            return false;
        }
        while (true) {
            i = i3 + 1;
            c = cArr[i3];
            if (c > ' ' || ((1 << c) & 4294981376L) == 0) {
                break;
            }
            i3 = i;
        }
        this.offset = i;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfValue4Match2() {
        char[] cArr = this.chars;
        int i = this.offset;
        int i2 = i + 3;
        if (i2 >= this.end) {
            return false;
        }
        char c = cArr[i2];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            i2 = i + 4;
            c = i2 == this.end ? JSONLexer.EOI : cArr[i2];
        }
        while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
            i2++;
            c = cArr[i2];
        }
        this.offset = i2 + 1;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfValue4Match3() {
        char[] cArr = this.chars;
        int i = this.offset;
        int i2 = i + 4;
        if (i2 >= this.end || cArr[i + 3] != '\"') {
            return false;
        }
        char c = cArr[i2];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            i2 = i + 5;
            c = i2 == this.end ? JSONLexer.EOI : cArr[i2];
        }
        while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
            i2++;
            c = cArr[i2];
        }
        this.offset = i2 + 1;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfValue4Match4(byte b) {
        char[] cArr = this.chars;
        int i = this.offset;
        int i2 = i + 5;
        boolean z = false;
        if (i2 >= this.end) {
            return false;
        }
        if (cArr[i + 3] == b && cArr[i + 4] == '\"') {
            char c = cArr[i2];
            if (c != ',' && c != '}' && c != ']') {
                return false;
            }
            z = true;
            if (c == ',') {
                this.comma = true;
                i2 = i + 6;
                c = i2 == this.end ? JSONLexer.EOI : cArr[i2];
            }
            while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
                i2++;
                c = cArr[i2];
            }
            this.offset = i2 + 1;
            this.ch = c;
        }
        return z;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfValue4Match5(byte b, byte b2) {
        char[] cArr = this.chars;
        int i = this.offset;
        int i2 = i + 6;
        if (i2 >= this.end || cArr[i + 3] != b || cArr[i + 4] != b2 || cArr[i + 5] != '\"') {
            return false;
        }
        char c = cArr[i2];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            i2 = i + 7;
            c = i2 == this.end ? JSONLexer.EOI : cArr[i2];
        }
        while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
            i2++;
            c = cArr[i2];
        }
        this.offset = i2 + 1;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfValue4Match6(int i) {
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 7;
        if (i3 >= this.end || getInt(cArr, i2 + 3) != i) {
            return false;
        }
        char c = cArr[i3];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            i3 = i2 + 8;
            c = i3 == this.end ? JSONLexer.EOI : cArr[i3];
        }
        while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
            i3++;
            c = cArr[i3];
        }
        this.offset = i3 + 1;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfValue4Match7(int i) {
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 8;
        boolean z = false;
        if (i3 >= this.end) {
            return false;
        }
        if (getInt(cArr, i2 + 3) == i && cArr[i2 + 7] == '\"') {
            char c = cArr[i3];
            if (c != ',' && c != '}' && c != ']') {
                return false;
            }
            z = true;
            if (c == ',') {
                this.comma = true;
                i3 = i2 + 9;
                c = i3 == this.end ? JSONLexer.EOI : cArr[i3];
            }
            while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
                i3++;
                c = cArr[i3];
            }
            this.offset = i3 + 1;
            this.ch = c;
        }
        return z;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfValue4Match8(int i, byte b) {
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 9;
        if (i3 >= this.end || getInt(cArr, i2 + 3) != i || cArr[i2 + 7] != b || cArr[i2 + 8] != '\"') {
            return false;
        }
        char c = cArr[i3];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            i3 = i2 + 10;
            c = i3 == this.end ? JSONLexer.EOI : cArr[i3];
        }
        while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
            i3++;
            c = cArr[i3];
        }
        this.offset = i3 + 1;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfValue4Match9(int i, byte b, byte b2) {
        char[] cArr = this.chars;
        int i2 = this.offset;
        int i3 = i2 + 10;
        if (i3 >= this.end || getInt(cArr, i2 + 3) != i || cArr[i2 + 7] != b || cArr[i2 + 8] != b2 || cArr[i2 + 9] != '\"') {
            return false;
        }
        char c = cArr[i3];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            i3 = i2 + 11;
            c = i3 == this.end ? JSONLexer.EOI : cArr[i3];
        }
        while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
            i3++;
            c = cArr[i3];
        }
        this.offset = i3 + 1;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfValue4Match10(long j) {
        char[] cArr = this.chars;
        int i = this.offset;
        int i2 = i + 11;
        if (i2 >= this.end || getLong(cArr, i + 3) != j) {
            return false;
        }
        char c = cArr[i2];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            i2 = i + 12;
            c = i2 == this.end ? JSONLexer.EOI : cArr[i2];
        }
        while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
            i2++;
            c = cArr[i2];
        }
        this.offset = i2 + 1;
        this.ch = c;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public final boolean nextIfValue4Match11(long j) {
        char[] cArr = this.chars;
        int i = this.offset;
        int i2 = i + 12;
        if (i2 >= this.end || getLong(cArr, i + 3) != j || cArr[i + 11] != '\"') {
            return false;
        }
        char c = cArr[i2];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            i2 = i + 13;
            c = i2 == this.end ? JSONLexer.EOI : cArr[i2];
        }
        while (c <= ' ' && ((1 << c) & 4294981376L) != 0) {
            i2++;
            c = cArr[i2];
        }
        this.offset = i2 + 1;
        this.ch = c;
        return true;
    }
}
