package okio.internal;

import androidx.exifinterface.media.ExifInterface;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.internal.asm.Opcodes;
import java.io.EOFException;
import java.io.IOException;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;
import okhttp3.internal.connection.RealConnection;
import okio.Buffer;
import okio.ByteString;
import okio.Options;
import okio.Platform;
import okio.Segment;
import okio.SegmentPool;
import okio.SegmentedByteString;
import okio.Sink;
import okio.Source;
import okio.Utf8;
import okio.Util;

/* JADX INFO: compiled from: Buffer.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000v\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\bH\u0000\u001a\r\u0010\u0011\u001a\u00020\u0012*\u00020\u0013H\u0080\b\u001a\r\u0010\u0014\u001a\u00020\u0005*\u00020\u0013H\u0080\b\u001a\r\u0010\u0015\u001a\u00020\u0013*\u00020\u0013H\u0080\b\u001a%\u0010\u0016\u001a\u00020\u0013*\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\u0017\u0010\u001a\u001a\u00020\n*\u00020\u00132\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0080\b\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u0005H\u0080\b\u001a\r\u0010 \u001a\u00020\b*\u00020\u0013H\u0080\b\u001a%\u0010!\u001a\u00020\u0005*\u00020\u00132\u0006\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u0005H\u0080\b\u001a\u001d\u0010!\u001a\u00020\u0005*\u00020\u00132\u0006\u0010\u000e\u001a\u00020%2\u0006\u0010#\u001a\u00020\u0005H\u0080\b\u001a\u001d\u0010&\u001a\u00020\u0005*\u00020\u00132\u0006\u0010'\u001a\u00020%2\u0006\u0010#\u001a\u00020\u0005H\u0080\b\u001a-\u0010(\u001a\u00020\n*\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020%2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\u0080\b\u001a\u0015\u0010)\u001a\u00020\b*\u00020\u00132\u0006\u0010*\u001a\u00020\u0001H\u0080\b\u001a%\u0010)\u001a\u00020\b*\u00020\u00132\u0006\u0010*\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\u0080\b\u001a\u001d\u0010)\u001a\u00020\u0005*\u00020\u00132\u0006\u0010*\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010+\u001a\u00020\u0005*\u00020\u00132\u0006\u0010*\u001a\u00020,H\u0080\b\u001a\r\u0010-\u001a\u00020\u001e*\u00020\u0013H\u0080\b\u001a\r\u0010.\u001a\u00020\u0001*\u00020\u0013H\u0080\b\u001a\u0015\u0010.\u001a\u00020\u0001*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\r\u0010/\u001a\u00020%*\u00020\u0013H\u0080\b\u001a\u0015\u0010/\u001a\u00020%*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\r\u00100\u001a\u00020\u0005*\u00020\u0013H\u0080\b\u001a\u0015\u00101\u001a\u00020\u0012*\u00020\u00132\u0006\u0010*\u001a\u00020\u0001H\u0080\b\u001a\u001d\u00101\u001a\u00020\u0012*\u00020\u00132\u0006\u0010*\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\r\u00102\u001a\u00020\u0005*\u00020\u0013H\u0080\b\u001a\r\u00103\u001a\u00020\b*\u00020\u0013H\u0080\b\u001a\r\u00104\u001a\u00020\u0005*\u00020\u0013H\u0080\b\u001a\r\u00105\u001a\u000206*\u00020\u0013H\u0080\b\u001a\u0015\u00107\u001a\u000208*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\r\u00109\u001a\u00020\b*\u00020\u0013H\u0080\b\u001a\u000f\u0010:\u001a\u0004\u0018\u000108*\u00020\u0013H\u0080\b\u001a\u0015\u0010;\u001a\u000208*\u00020\u00132\u0006\u0010<\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010=\u001a\u00020\b*\u00020\u00132\u0006\u0010>\u001a\u00020?H\u0080\b\u001a\u0015\u0010@\u001a\u00020\u0012*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\r\u0010A\u001a\u00020%*\u00020\u0013H\u0080\b\u001a\u0015\u0010A\u001a\u00020%*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\bH\u0080\b\u001a\u0015\u0010B\u001a\u00020\f*\u00020\u00132\u0006\u0010C\u001a\u00020\bH\u0080\b\u001a\u0015\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010E\u001a\u00020\u0001H\u0080\b\u001a%\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010E\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\u0080\b\u001a\u001d\u0010D\u001a\u00020\u0012*\u00020\u00132\u0006\u0010E\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a)\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010F\u001a\u00020%2\b\b\u0002\u0010\u0018\u001a\u00020\b2\b\b\u0002\u0010\u0019\u001a\u00020\bH\u0080\b\u001a\u001d\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010E\u001a\u00020G2\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010H\u001a\u00020\u0005*\u00020\u00132\u0006\u0010E\u001a\u00020GH\u0080\b\u001a\u0015\u0010I\u001a\u00020\u0013*\u00020\u00132\u0006\u0010\"\u001a\u00020\bH\u0080\b\u001a\u0015\u0010J\u001a\u00020\u0013*\u00020\u00132\u0006\u0010K\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010L\u001a\u00020\u0013*\u00020\u00132\u0006\u0010K\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010M\u001a\u00020\u0013*\u00020\u00132\u0006\u0010N\u001a\u00020\bH\u0080\b\u001a\u0015\u0010O\u001a\u00020\u0013*\u00020\u00132\u0006\u0010K\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010P\u001a\u00020\u0013*\u00020\u00132\u0006\u0010Q\u001a\u00020\bH\u0080\b\u001a%\u0010R\u001a\u00020\u0013*\u00020\u00132\u0006\u0010S\u001a\u0002082\u0006\u0010T\u001a\u00020\b2\u0006\u0010U\u001a\u00020\bH\u0080\b\u001a\u0015\u0010V\u001a\u00020\u0013*\u00020\u00132\u0006\u0010W\u001a\u00020\bH\u0080\b\u001a\u0014\u0010X\u001a\u000208*\u00020\u00132\u0006\u0010Y\u001a\u00020\u0005H\u0000\u001a?\u0010Z\u001a\u0002H[\"\u0004\b\u0000\u0010[*\u00020\u00132\u0006\u0010#\u001a\u00020\u00052\u001a\u0010\\\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u00010\f\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H[0]H\u0080\bø\u0001\u0000¢\u0006\u0002\u0010^\u001a\u001e\u0010_\u001a\u00020\b*\u00020\u00132\u0006\u0010>\u001a\u00020?2\b\b\u0002\u0010`\u001a\u00020\nH\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0005X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\bX\u0080T¢\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006a"}, d2 = {"HEX_DIGIT_BYTES", "", "getHEX_DIGIT_BYTES", "()[B", "OVERFLOW_DIGIT_START", "", "OVERFLOW_ZONE", "SEGMENTING_THRESHOLD", "", "rangeEquals", "", "segment", "Lokio/Segment;", "segmentPos", "bytes", "bytesOffset", "bytesLimit", "commonClear", "", "Lokio/Buffer;", "commonCompleteSegmentByteCount", "commonCopy", "commonCopyTo", "out", "offset", "byteCount", "commonEquals", "other", "", "commonGet", "", "pos", "commonHashCode", "commonIndexOf", "b", "fromIndex", "toIndex", "Lokio/ByteString;", "commonIndexOfElement", "targetBytes", "commonRangeEquals", "commonRead", "sink", "commonReadAll", "Lokio/Sink;", "commonReadByte", "commonReadByteArray", "commonReadByteString", "commonReadDecimalLong", "commonReadFully", "commonReadHexadecimalUnsignedLong", "commonReadInt", "commonReadLong", "commonReadShort", "", "commonReadUtf8", "", "commonReadUtf8CodePoint", "commonReadUtf8Line", "commonReadUtf8LineStrict", "limit", "commonSelect", "options", "Lokio/Options;", "commonSkip", "commonSnapshot", "commonWritableSegment", "minimumCapacity", "commonWrite", "source", "byteString", "Lokio/Source;", "commonWriteAll", "commonWriteByte", "commonWriteDecimalLong", "v", "commonWriteHexadecimalUnsignedLong", "commonWriteInt", "i", "commonWriteLong", "commonWriteShort", "s", "commonWriteUtf8", "string", "beginIndex", "endIndex", "commonWriteUtf8CodePoint", "codePoint", "readUtf8Line", "newline", "seek", ExifInterface.GPS_DIRECTION_TRUE, "lambda", "Lkotlin/Function2;", "(Lokio/Buffer;JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "selectPrefix", "selectTruncated", "okio"}, k = 2, mv = {1, 4, 0})
public final class BufferKt {
    private static final byte[] HEX_DIGIT_BYTES = Platform.asUtf8ToByteArray("0123456789abcdef");
    public static final long OVERFLOW_DIGIT_START = -7;
    public static final long OVERFLOW_ZONE = -922337203685477580L;
    public static final int SEGMENTING_THRESHOLD = 4096;

    public static final byte[] getHEX_DIGIT_BYTES() {
        return HEX_DIGIT_BYTES;
    }

    public static final boolean rangeEquals(Segment segment, int i, byte[] bytes, int i2, int i3) {
        Intrinsics.checkNotNullParameter(segment, "segment");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        int i4 = segment.limit;
        byte[] bArr = segment.data;
        while (i2 < i3) {
            if (i == i4) {
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                byte[] bArr2 = segment.data;
                bArr = bArr2;
                i = segment.pos;
                i4 = segment.limit;
            }
            if (bArr[i] != bytes[i2]) {
                return false;
            }
            i++;
            i2++;
        }
        return true;
    }

    public static final String readUtf8Line(Buffer readUtf8Line, long j) throws EOFException {
        Intrinsics.checkNotNullParameter(readUtf8Line, "$this$readUtf8Line");
        if (j > 0) {
            long j2 = j - 1;
            if (readUtf8Line.getByte(j2) == ((byte) 13)) {
                String utf8 = readUtf8Line.readUtf8(j2);
                readUtf8Line.skip(2L);
                return utf8;
            }
        }
        String utf82 = readUtf8Line.readUtf8(j);
        readUtf8Line.skip(1L);
        return utf82;
    }

    public static final <T> T seek(Buffer seek, long j, Function2<? super Segment, ? super Long, ? extends T> lambda) {
        Intrinsics.checkNotNullParameter(seek, "$this$seek");
        Intrinsics.checkNotNullParameter(lambda, "lambda");
        Segment segment = seek.head;
        if (segment == null) {
            return lambda.invoke(null, -1L);
        }
        if (seek.size() - j < j) {
            long size = seek.size();
            while (size > j) {
                segment = segment.prev;
                Intrinsics.checkNotNull(segment);
                size -= (long) (segment.limit - segment.pos);
            }
            return lambda.invoke(segment, Long.valueOf(size));
        }
        long j2 = 0;
        while (true) {
            long j3 = ((long) (segment.limit - segment.pos)) + j2;
            if (j3 <= j) {
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j2 = j3;
            } else {
                return lambda.invoke(segment, Long.valueOf(j2));
            }
        }
    }

    public static /* synthetic */ int selectPrefix$default(Buffer buffer, Options options, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return selectPrefix(buffer, options, z);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x005d, code lost:
    
        if (r19 == false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x005f, code lost:
    
        return -2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0086, code lost:
    
        return r10;
     */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00ab A[LOOP:0: B:5:0x0022->B:44:0x00ab, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00aa A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final int selectPrefix(okio.Buffer r17, okio.Options r18, boolean r19) {
        /*
            r0 = r17
            java.lang.String r1 = "$this$selectPrefix"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r1)
            java.lang.String r1 = "options"
            r2 = r18
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r1)
            okio.Segment r0 = r0.head
            r1 = -2
            r3 = -1
            if (r0 == 0) goto Lb2
            byte[] r4 = r0.data
            int r5 = r0.pos
            int r6 = r0.limit
            int[] r2 = r2.getTrie()
            r7 = 0
            r9 = r0
            r10 = r3
            r8 = r7
        L22:
            int r11 = r8 + 1
            r12 = r2[r8]
            int r8 = r8 + 2
            r11 = r2[r11]
            if (r11 == r3) goto L2d
            r10 = r11
        L2d:
            if (r9 != 0) goto L30
            goto L5d
        L30:
            r11 = 0
            if (r12 >= 0) goto L7c
            int r12 = r12 * (-1)
            int r13 = r8 + r12
        L37:
            int r12 = r5 + 1
            r5 = r4[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r14 = r8 + 1
            r8 = r2[r8]
            if (r5 == r8) goto L44
            goto L86
        L44:
            if (r14 != r13) goto L48
            r5 = 1
            goto L49
        L48:
            r5 = r7
        L49:
            if (r12 != r6) goto L6c
            kotlin.jvm.internal.Intrinsics.checkNotNull(r9)
            okio.Segment r4 = r9.next
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            int r6 = r4.pos
            byte[] r8 = r4.data
            int r9 = r4.limit
            if (r4 != r0) goto L66
            if (r5 != 0) goto L60
        L5d:
            if (r19 == 0) goto L86
            return r1
        L60:
            r4 = r11
            okio.Segment r4 = (okio.Segment) r4
            r4 = r8
            r8 = r11
            goto L6f
        L66:
            r16 = r8
            r8 = r4
            r4 = r16
            goto L6f
        L6c:
            r8 = r9
            r9 = r6
            r6 = r12
        L6f:
            if (r5 == 0) goto L77
            r5 = r2[r14]
            r13 = r6
            r6 = r9
            r9 = r8
            goto La8
        L77:
            r5 = r6
            r6 = r9
            r9 = r8
            r8 = r14
            goto L37
        L7c:
            int r13 = r5 + 1
            r5 = r4[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r14 = r8 + r12
        L84:
            if (r8 != r14) goto L87
        L86:
            return r10
        L87:
            r15 = r2[r8]
            if (r5 != r15) goto Laf
            int r8 = r8 + r12
            r5 = r2[r8]
            if (r13 != r6) goto La8
            okio.Segment r9 = r9.next
            kotlin.jvm.internal.Intrinsics.checkNotNull(r9)
            int r4 = r9.pos
            byte[] r6 = r9.data
            int r8 = r9.limit
            if (r9 != r0) goto La5
            r9 = r11
            okio.Segment r9 = (okio.Segment) r9
            r13 = r4
            r4 = r6
            r6 = r8
            r9 = r11
            goto La8
        La5:
            r13 = r4
            r4 = r6
            r6 = r8
        La8:
            if (r5 < 0) goto Lab
            return r5
        Lab:
            int r8 = -r5
            r5 = r13
            goto L22
        Laf:
            int r8 = r8 + 1
            goto L84
        Lb2:
            if (r19 == 0) goto Lb5
            return r1
        Lb5:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.BufferKt.selectPrefix(okio.Buffer, okio.Options, boolean):int");
    }

    public static final Buffer commonCopyTo(Buffer commonCopyTo, Buffer out, long j, long j2) {
        Intrinsics.checkNotNullParameter(commonCopyTo, "$this$commonCopyTo");
        Intrinsics.checkNotNullParameter(out, "out");
        long j3 = j;
        Util.checkOffsetAndCount(commonCopyTo.size(), j3, j2);
        if (j2 != 0) {
            out.setSize$okio(out.size() + j2);
            Segment segment = commonCopyTo.head;
            while (true) {
                Intrinsics.checkNotNull(segment);
                if (j3 < segment.limit - segment.pos) {
                    break;
                }
                j3 -= (long) (segment.limit - segment.pos);
                segment = segment.next;
            }
            Segment segment2 = segment;
            long j4 = j2;
            while (j4 > 0) {
                Intrinsics.checkNotNull(segment2);
                Segment segmentSharedCopy = segment2.sharedCopy();
                segmentSharedCopy.pos += (int) j3;
                segmentSharedCopy.limit = Math.min(segmentSharedCopy.pos + ((int) j4), segmentSharedCopy.limit);
                if (out.head == null) {
                    segmentSharedCopy.prev = segmentSharedCopy;
                    segmentSharedCopy.next = segmentSharedCopy.prev;
                    out.head = segmentSharedCopy.next;
                } else {
                    Segment segment3 = out.head;
                    Intrinsics.checkNotNull(segment3);
                    Segment segment4 = segment3.prev;
                    Intrinsics.checkNotNull(segment4);
                    segment4.push(segmentSharedCopy);
                }
                j4 -= (long) (segmentSharedCopy.limit - segmentSharedCopy.pos);
                segment2 = segment2.next;
                j3 = 0;
            }
        }
        return commonCopyTo;
    }

    public static final long commonCompleteSegmentByteCount(Buffer commonCompleteSegmentByteCount) {
        Intrinsics.checkNotNullParameter(commonCompleteSegmentByteCount, "$this$commonCompleteSegmentByteCount");
        long size = commonCompleteSegmentByteCount.size();
        if (size == 0) {
            return 0L;
        }
        Segment segment = commonCompleteSegmentByteCount.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment.prev;
        Intrinsics.checkNotNull(segment2);
        return (segment2.limit >= 8192 || !segment2.owner) ? size : size - ((long) (segment2.limit - segment2.pos));
    }

    public static final byte commonReadByte(Buffer commonReadByte) throws EOFException {
        Intrinsics.checkNotNullParameter(commonReadByte, "$this$commonReadByte");
        if (commonReadByte.size() == 0) {
            throw new EOFException();
        }
        Segment segment = commonReadByte.head;
        Intrinsics.checkNotNull(segment);
        int i = segment.pos;
        int i2 = segment.limit;
        int i3 = i + 1;
        byte b = segment.data[i];
        commonReadByte.setSize$okio(commonReadByte.size() - 1);
        if (i3 == i2) {
            commonReadByte.head = segment.pop();
            SegmentPool.recycle(segment);
            return b;
        }
        segment.pos = i3;
        return b;
    }

    public static final short commonReadShort(Buffer commonReadShort) throws EOFException {
        Intrinsics.checkNotNullParameter(commonReadShort, "$this$commonReadShort");
        if (commonReadShort.size() < 2) {
            throw new EOFException();
        }
        Segment segment = commonReadShort.head;
        Intrinsics.checkNotNull(segment);
        int i = segment.pos;
        int i2 = segment.limit;
        if (i2 - i < 2) {
            return (short) ((commonReadShort.readByte() & UByte.MAX_VALUE) | ((commonReadShort.readByte() & UByte.MAX_VALUE) << 8));
        }
        byte[] bArr = segment.data;
        int i3 = i + 1;
        int i4 = (bArr[i] & UByte.MAX_VALUE) << 8;
        int i5 = i + 2;
        int i6 = (bArr[i3] & UByte.MAX_VALUE) | i4;
        commonReadShort.setSize$okio(commonReadShort.size() - 2);
        if (i5 == i2) {
            commonReadShort.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = i5;
        }
        return (short) i6;
    }

    public static final int commonReadInt(Buffer commonReadInt) throws EOFException {
        Intrinsics.checkNotNullParameter(commonReadInt, "$this$commonReadInt");
        if (commonReadInt.size() < 4) {
            throw new EOFException();
        }
        Segment segment = commonReadInt.head;
        Intrinsics.checkNotNull(segment);
        int i = segment.pos;
        int i2 = segment.limit;
        if (i2 - i < 4) {
            return (commonReadInt.readByte() & UByte.MAX_VALUE) | ((commonReadInt.readByte() & UByte.MAX_VALUE) << 24) | ((commonReadInt.readByte() & UByte.MAX_VALUE) << 16) | ((commonReadInt.readByte() & UByte.MAX_VALUE) << 8);
        }
        byte[] bArr = segment.data;
        int i3 = i + 3;
        int i4 = ((bArr[i + 1] & UByte.MAX_VALUE) << 16) | ((bArr[i] & UByte.MAX_VALUE) << 24) | ((bArr[i + 2] & UByte.MAX_VALUE) << 8);
        int i5 = i + 4;
        int i6 = (bArr[i3] & UByte.MAX_VALUE) | i4;
        commonReadInt.setSize$okio(commonReadInt.size() - 4);
        if (i5 == i2) {
            commonReadInt.head = segment.pop();
            SegmentPool.recycle(segment);
            return i6;
        }
        segment.pos = i5;
        return i6;
    }

    public static final long commonReadLong(Buffer commonReadLong) throws EOFException {
        Intrinsics.checkNotNullParameter(commonReadLong, "$this$commonReadLong");
        if (commonReadLong.size() < 8) {
            throw new EOFException();
        }
        Segment segment = commonReadLong.head;
        Intrinsics.checkNotNull(segment);
        int i = segment.pos;
        int i2 = segment.limit;
        if (i2 - i < 8) {
            return ((((long) commonReadLong.readInt()) & 4294967295L) << 32) | (4294967295L & ((long) commonReadLong.readInt()));
        }
        byte[] bArr = segment.data;
        int i3 = i + 7;
        long j = ((((long) bArr[i]) & 255) << 56) | ((((long) bArr[i + 1]) & 255) << 48) | ((((long) bArr[i + 2]) & 255) << 40) | ((((long) bArr[i + 3]) & 255) << 32) | ((((long) bArr[i + 4]) & 255) << 24) | ((((long) bArr[i + 5]) & 255) << 16) | ((((long) bArr[i + 6]) & 255) << 8);
        int i4 = i + 8;
        long j2 = j | (((long) bArr[i3]) & 255);
        commonReadLong.setSize$okio(commonReadLong.size() - 8);
        if (i4 == i2) {
            commonReadLong.head = segment.pop();
            SegmentPool.recycle(segment);
            return j2;
        }
        segment.pos = i4;
        return j2;
    }

    public static final byte commonGet(Buffer commonGet, long j) {
        Intrinsics.checkNotNullParameter(commonGet, "$this$commonGet");
        Util.checkOffsetAndCount(commonGet.size(), j, 1L);
        Segment segment = commonGet.head;
        if (segment == null) {
            Segment segment2 = null;
            Intrinsics.checkNotNull(null);
            byte[] bArr = segment2.data;
            throw null;
        }
        if (commonGet.size() - j < j) {
            long size = commonGet.size();
            while (size > j) {
                segment = segment.prev;
                Intrinsics.checkNotNull(segment);
                size -= (long) (segment.limit - segment.pos);
            }
            Intrinsics.checkNotNull(segment);
            return segment.data[(int) ((((long) segment.pos) + j) - size)];
        }
        long j2 = 0;
        while (true) {
            long j3 = ((long) (segment.limit - segment.pos)) + j2;
            if (j3 > j) {
                Intrinsics.checkNotNull(segment);
                return segment.data[(int) ((((long) segment.pos) + j) - j2)];
            }
            segment = segment.next;
            Intrinsics.checkNotNull(segment);
            j2 = j3;
        }
    }

    public static final void commonClear(Buffer commonClear) throws EOFException {
        Intrinsics.checkNotNullParameter(commonClear, "$this$commonClear");
        commonClear.skip(commonClear.size());
    }

    public static final void commonSkip(Buffer commonSkip, long j) throws EOFException {
        Intrinsics.checkNotNullParameter(commonSkip, "$this$commonSkip");
        while (j > 0) {
            Segment segment = commonSkip.head;
            if (segment == null) {
                throw new EOFException();
            }
            int iMin = (int) Math.min(j, segment.limit - segment.pos);
            long j2 = iMin;
            commonSkip.setSize$okio(commonSkip.size() - j2);
            j -= j2;
            segment.pos += iMin;
            if (segment.pos == segment.limit) {
                commonSkip.head = segment.pop();
                SegmentPool.recycle(segment);
            }
        }
    }

    public static /* synthetic */ Buffer commonWrite$default(Buffer commonWrite, ByteString byteString, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = byteString.size();
        }
        Intrinsics.checkNotNullParameter(commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        byteString.write$okio(commonWrite, i, i2);
        return commonWrite;
    }

    public static final Buffer commonWrite(Buffer commonWrite, ByteString byteString, int i, int i2) {
        Intrinsics.checkNotNullParameter(commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        byteString.write$okio(commonWrite, i, i2);
        return commonWrite;
    }

    public static final Buffer commonWriteDecimalLong(Buffer commonWriteDecimalLong, long j) {
        boolean z;
        Intrinsics.checkNotNullParameter(commonWriteDecimalLong, "$this$commonWriteDecimalLong");
        if (j == 0) {
            return commonWriteDecimalLong.writeByte(48);
        }
        int i = 1;
        if (j < 0) {
            j = -j;
            if (j < 0) {
                return commonWriteDecimalLong.writeUtf8("-9223372036854775808");
            }
            z = true;
        } else {
            z = false;
        }
        if (j >= 100000000) {
            i = j < 1000000000000L ? j < RealConnection.IDLE_CONNECTION_HEALTHY_NS ? j < 1000000000 ? 9 : 10 : j < 100000000000L ? 11 : 12 : j < 1000000000000000L ? j < 10000000000000L ? 13 : j < 100000000000000L ? 14 : 15 : j < 100000000000000000L ? j < 10000000000000000L ? 16 : 17 : j < 1000000000000000000L ? 18 : 19;
        } else if (j >= 10000) {
            i = j < 1000000 ? j < 100000 ? 5 : 6 : j < 10000000 ? 7 : 8;
        } else if (j >= 100) {
            i = j < 1000 ? 3 : 4;
        } else if (j >= 10) {
            i = 2;
        }
        if (z) {
            i++;
        }
        Segment segmentWritableSegment$okio = commonWriteDecimalLong.writableSegment$okio(i);
        byte[] bArr = segmentWritableSegment$okio.data;
        int i2 = segmentWritableSegment$okio.limit + i;
        while (j != 0) {
            long j2 = 10;
            i2--;
            bArr[i2] = getHEX_DIGIT_BYTES()[(int) (j % j2)];
            j /= j2;
        }
        if (z) {
            bArr[i2 - 1] = (byte) 45;
        }
        segmentWritableSegment$okio.limit += i;
        commonWriteDecimalLong.setSize$okio(commonWriteDecimalLong.size() + ((long) i));
        return commonWriteDecimalLong;
    }

    public static final Buffer commonWriteHexadecimalUnsignedLong(Buffer commonWriteHexadecimalUnsignedLong, long j) {
        Intrinsics.checkNotNullParameter(commonWriteHexadecimalUnsignedLong, "$this$commonWriteHexadecimalUnsignedLong");
        if (j == 0) {
            return commonWriteHexadecimalUnsignedLong.writeByte(48);
        }
        long j2 = (j >>> 1) | j;
        long j3 = j2 | (j2 >>> 2);
        long j4 = j3 | (j3 >>> 4);
        long j5 = j4 | (j4 >>> 8);
        long j6 = j5 | (j5 >>> 16);
        long j7 = j6 | (j6 >>> 32);
        long j8 = j7 - ((j7 >>> 1) & 6148914691236517205L);
        long j9 = ((j8 >>> 2) & 3689348814741910323L) + (j8 & 3689348814741910323L);
        long j10 = ((j9 >>> 4) + j9) & 1085102592571150095L;
        long j11 = j10 + (j10 >>> 8);
        long j12 = j11 + (j11 >>> 16);
        int i = (int) ((((j12 & 63) + ((j12 >>> 32) & 63)) + ((long) 3)) / ((long) 4));
        Segment segmentWritableSegment$okio = commonWriteHexadecimalUnsignedLong.writableSegment$okio(i);
        byte[] bArr = segmentWritableSegment$okio.data;
        int i2 = segmentWritableSegment$okio.limit;
        for (int i3 = (segmentWritableSegment$okio.limit + i) - 1; i3 >= i2; i3--) {
            bArr[i3] = getHEX_DIGIT_BYTES()[(int) (15 & j)];
            j >>>= 4;
        }
        segmentWritableSegment$okio.limit += i;
        commonWriteHexadecimalUnsignedLong.setSize$okio(commonWriteHexadecimalUnsignedLong.size() + ((long) i));
        return commonWriteHexadecimalUnsignedLong;
    }

    public static final Segment commonWritableSegment(Buffer commonWritableSegment, int i) {
        Intrinsics.checkNotNullParameter(commonWritableSegment, "$this$commonWritableSegment");
        if (!(i >= 1 && i <= 8192)) {
            throw new IllegalArgumentException("unexpected capacity".toString());
        }
        if (commonWritableSegment.head == null) {
            Segment segmentTake = SegmentPool.take();
            commonWritableSegment.head = segmentTake;
            segmentTake.prev = segmentTake;
            segmentTake.next = segmentTake;
            return segmentTake;
        }
        Segment segment = commonWritableSegment.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment.prev;
        Intrinsics.checkNotNull(segment2);
        return (segment2.limit + i > 8192 || !segment2.owner) ? segment2.push(SegmentPool.take()) : segment2;
    }

    public static final Buffer commonWrite(Buffer commonWrite, byte[] source) {
        Intrinsics.checkNotNullParameter(commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        return commonWrite.write(source, 0, source.length);
    }

    public static final Buffer commonWrite(Buffer commonWrite, byte[] source, int i, int i2) {
        Intrinsics.checkNotNullParameter(commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        long j = i2;
        Util.checkOffsetAndCount(source.length, i, j);
        int i3 = i2 + i;
        while (i < i3) {
            Segment segmentWritableSegment$okio = commonWrite.writableSegment$okio(1);
            int iMin = Math.min(i3 - i, 8192 - segmentWritableSegment$okio.limit);
            int i4 = i + iMin;
            ArraysKt.copyInto(source, segmentWritableSegment$okio.data, segmentWritableSegment$okio.limit, i, i4);
            segmentWritableSegment$okio.limit += iMin;
            i = i4;
        }
        commonWrite.setSize$okio(commonWrite.size() + j);
        return commonWrite;
    }

    public static final byte[] commonReadByteArray(Buffer commonReadByteArray) {
        Intrinsics.checkNotNullParameter(commonReadByteArray, "$this$commonReadByteArray");
        return commonReadByteArray.readByteArray(commonReadByteArray.size());
    }

    public static final byte[] commonReadByteArray(Buffer commonReadByteArray, long j) throws EOFException {
        Intrinsics.checkNotNullParameter(commonReadByteArray, "$this$commonReadByteArray");
        if (!(j >= 0 && j <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + j).toString());
        }
        if (commonReadByteArray.size() < j) {
            throw new EOFException();
        }
        byte[] bArr = new byte[(int) j];
        commonReadByteArray.readFully(bArr);
        return bArr;
    }

    public static final int commonRead(Buffer commonRead, byte[] sink) {
        Intrinsics.checkNotNullParameter(commonRead, "$this$commonRead");
        Intrinsics.checkNotNullParameter(sink, "sink");
        return commonRead.read(sink, 0, sink.length);
    }

    public static final void commonReadFully(Buffer commonReadFully, byte[] sink) throws EOFException {
        Intrinsics.checkNotNullParameter(commonReadFully, "$this$commonReadFully");
        Intrinsics.checkNotNullParameter(sink, "sink");
        int i = 0;
        while (i < sink.length) {
            int i2 = commonReadFully.read(sink, i, sink.length - i);
            if (i2 == -1) {
                throw new EOFException();
            }
            i += i2;
        }
    }

    public static final int commonRead(Buffer commonRead, byte[] sink, int i, int i2) {
        Intrinsics.checkNotNullParameter(commonRead, "$this$commonRead");
        Intrinsics.checkNotNullParameter(sink, "sink");
        Util.checkOffsetAndCount(sink.length, i, i2);
        Segment segment = commonRead.head;
        if (segment == null) {
            return -1;
        }
        int iMin = Math.min(i2, segment.limit - segment.pos);
        ArraysKt.copyInto(segment.data, sink, i, segment.pos, segment.pos + iMin);
        segment.pos += iMin;
        commonRead.setSize$okio(commonRead.size() - ((long) iMin));
        if (segment.pos == segment.limit) {
            commonRead.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return iMin;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00b8 A[EDGE_INSN: B:48:0x00b8->B:38:0x00b8 BREAK  A[LOOP:0: B:5:0x0016->B:50:?], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final long commonReadDecimalLong(okio.Buffer r15) throws java.io.EOFException {
        /*
            Method dump skipped, instruction units count: 206
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.BufferKt.commonReadDecimalLong(okio.Buffer):long");
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00b3 A[EDGE_INSN: B:43:0x00b3->B:37:0x00b3 BREAK  A[LOOP:0: B:5:0x0012->B:45:?], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final long commonReadHexadecimalUnsignedLong(okio.Buffer r14) throws java.io.EOFException {
        /*
            java.lang.String r0 = "$this$commonReadHexadecimalUnsignedLong"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            long r0 = r14.size()
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 == 0) goto Lbd
            r0 = 0
            r1 = r0
            r4 = r2
        L12:
            okio.Segment r6 = r14.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r6)
            byte[] r7 = r6.data
            int r8 = r6.pos
            int r9 = r6.limit
        L1d:
            if (r8 >= r9) goto L9f
            r10 = r7[r8]
            r11 = 48
            byte r11 = (byte) r11
            if (r10 < r11) goto L2e
            r12 = 57
            byte r12 = (byte) r12
            if (r10 > r12) goto L2e
            int r11 = r10 - r11
            goto L48
        L2e:
            r11 = 97
            byte r11 = (byte) r11
            if (r10 < r11) goto L3d
            r12 = 102(0x66, float:1.43E-43)
            byte r12 = (byte) r12
            if (r10 > r12) goto L3d
        L38:
            int r11 = r10 - r11
            int r11 = r11 + 10
            goto L48
        L3d:
            r11 = 65
            byte r11 = (byte) r11
            if (r10 < r11) goto L80
            r12 = 70
            byte r12 = (byte) r12
            if (r10 > r12) goto L80
            goto L38
        L48:
            r12 = -1152921504606846976(0xf000000000000000, double:-3.105036184601418E231)
            long r12 = r12 & r4
            int r12 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r12 != 0) goto L58
            r10 = 4
            long r4 = r4 << r10
            long r10 = (long) r11
            long r4 = r4 | r10
            int r8 = r8 + 1
            int r0 = r0 + 1
            goto L1d
        L58:
            okio.Buffer r14 = new okio.Buffer
            r14.<init>()
            okio.Buffer r14 = r14.writeHexadecimalUnsignedLong(r4)
            okio.Buffer r14 = r14.writeByte(r10)
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Number too large: "
            r1.<init>(r2)
            java.lang.String r14 = r14.readUtf8()
            java.lang.StringBuilder r14 = r1.append(r14)
            java.lang.String r14 = r14.toString()
            r0.<init>(r14)
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            throw r0
        L80:
            if (r0 == 0) goto L84
            r1 = 1
            goto L9f
        L84:
            java.lang.NumberFormatException r14 = new java.lang.NumberFormatException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Expected leading [0-9a-fA-F] character but was 0x"
            r0.<init>(r1)
            java.lang.String r1 = okio.Util.toHexString(r10)
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r14.<init>(r0)
            java.lang.Throwable r14 = (java.lang.Throwable) r14
            throw r14
        L9f:
            if (r8 != r9) goto Lab
            okio.Segment r7 = r6.pop()
            r14.head = r7
            okio.SegmentPool.recycle(r6)
            goto Lad
        Lab:
            r6.pos = r8
        Lad:
            if (r1 != 0) goto Lb3
            okio.Segment r6 = r14.head
            if (r6 != 0) goto L12
        Lb3:
            long r1 = r14.size()
            long r6 = (long) r0
            long r1 = r1 - r6
            r14.setSize$okio(r1)
            return r4
        Lbd:
            java.io.EOFException r14 = new java.io.EOFException
            r14.<init>()
            java.lang.Throwable r14 = (java.lang.Throwable) r14
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.BufferKt.commonReadHexadecimalUnsignedLong(okio.Buffer):long");
    }

    public static final ByteString commonReadByteString(Buffer commonReadByteString) {
        Intrinsics.checkNotNullParameter(commonReadByteString, "$this$commonReadByteString");
        return commonReadByteString.readByteString(commonReadByteString.size());
    }

    public static final ByteString commonReadByteString(Buffer commonReadByteString, long j) throws EOFException {
        Intrinsics.checkNotNullParameter(commonReadByteString, "$this$commonReadByteString");
        if (!(j >= 0 && j <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + j).toString());
        }
        if (commonReadByteString.size() < j) {
            throw new EOFException();
        }
        if (j >= 4096) {
            ByteString byteStringSnapshot = commonReadByteString.snapshot((int) j);
            commonReadByteString.skip(j);
            return byteStringSnapshot;
        }
        return new ByteString(commonReadByteString.readByteArray(j));
    }

    public static final int commonSelect(Buffer commonSelect, Options options) throws EOFException {
        Intrinsics.checkNotNullParameter(commonSelect, "$this$commonSelect");
        Intrinsics.checkNotNullParameter(options, "options");
        int iSelectPrefix$default = selectPrefix$default(commonSelect, options, false, 2, null);
        if (iSelectPrefix$default == -1) {
            return -1;
        }
        commonSelect.skip(options.getByteStrings()[iSelectPrefix$default].size());
        return iSelectPrefix$default;
    }

    public static final void commonReadFully(Buffer commonReadFully, Buffer sink, long j) throws EOFException {
        Intrinsics.checkNotNullParameter(commonReadFully, "$this$commonReadFully");
        Intrinsics.checkNotNullParameter(sink, "sink");
        if (commonReadFully.size() < j) {
            sink.write(commonReadFully, commonReadFully.size());
            throw new EOFException();
        }
        sink.write(commonReadFully, j);
    }

    public static final long commonReadAll(Buffer commonReadAll, Sink sink) throws IOException {
        Intrinsics.checkNotNullParameter(commonReadAll, "$this$commonReadAll");
        Intrinsics.checkNotNullParameter(sink, "sink");
        long size = commonReadAll.size();
        if (size > 0) {
            sink.write(commonReadAll, size);
        }
        return size;
    }

    public static final String commonReadUtf8(Buffer commonReadUtf8, long j) throws EOFException {
        Intrinsics.checkNotNullParameter(commonReadUtf8, "$this$commonReadUtf8");
        if (!(j >= 0 && j <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + j).toString());
        }
        if (commonReadUtf8.size() < j) {
            throw new EOFException();
        }
        if (j == 0) {
            return "";
        }
        Segment segment = commonReadUtf8.head;
        Intrinsics.checkNotNull(segment);
        if (((long) segment.pos) + j > segment.limit) {
            return _Utf8Kt.commonToUtf8String$default(commonReadUtf8.readByteArray(j), 0, 0, 3, null);
        }
        int i = (int) j;
        String strCommonToUtf8String = _Utf8Kt.commonToUtf8String(segment.data, segment.pos, segment.pos + i);
        segment.pos += i;
        commonReadUtf8.setSize$okio(commonReadUtf8.size() - j);
        if (segment.pos == segment.limit) {
            commonReadUtf8.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return strCommonToUtf8String;
    }

    public static final String commonReadUtf8Line(Buffer commonReadUtf8Line) {
        Intrinsics.checkNotNullParameter(commonReadUtf8Line, "$this$commonReadUtf8Line");
        long jIndexOf = commonReadUtf8Line.indexOf((byte) 10);
        if (jIndexOf != -1) {
            return readUtf8Line(commonReadUtf8Line, jIndexOf);
        }
        if (commonReadUtf8Line.size() != 0) {
            return commonReadUtf8Line.readUtf8(commonReadUtf8Line.size());
        }
        return null;
    }

    public static final String commonReadUtf8LineStrict(Buffer commonReadUtf8LineStrict, long j) throws EOFException {
        Intrinsics.checkNotNullParameter(commonReadUtf8LineStrict, "$this$commonReadUtf8LineStrict");
        if (!(j >= 0)) {
            throw new IllegalArgumentException(("limit < 0: " + j).toString());
        }
        long j2 = j != Long.MAX_VALUE ? j + 1 : Long.MAX_VALUE;
        byte b = (byte) 10;
        long jIndexOf = commonReadUtf8LineStrict.indexOf(b, 0L, j2);
        if (jIndexOf != -1) {
            return readUtf8Line(commonReadUtf8LineStrict, jIndexOf);
        }
        if (j2 < commonReadUtf8LineStrict.size() && commonReadUtf8LineStrict.getByte(j2 - 1) == ((byte) 13) && commonReadUtf8LineStrict.getByte(j2) == b) {
            return readUtf8Line(commonReadUtf8LineStrict, j2);
        }
        Buffer buffer = new Buffer();
        commonReadUtf8LineStrict.copyTo(buffer, 0L, Math.min(32, commonReadUtf8LineStrict.size()));
        throw new EOFException("\\n not found: limit=" + Math.min(commonReadUtf8LineStrict.size(), j) + " content=" + buffer.readByteString().hex() + Typography.ellipsis);
    }

    public static final int commonReadUtf8CodePoint(Buffer commonReadUtf8CodePoint) throws EOFException {
        int i;
        int i2;
        int i3;
        Intrinsics.checkNotNullParameter(commonReadUtf8CodePoint, "$this$commonReadUtf8CodePoint");
        if (commonReadUtf8CodePoint.size() == 0) {
            throw new EOFException();
        }
        byte b = commonReadUtf8CodePoint.getByte(0L);
        if ((b & ByteCompanionObject.MIN_VALUE) == 0) {
            i = b & 127;
            i3 = 0;
            i2 = 1;
        } else if ((b & 224) == 192) {
            i = b & 31;
            i2 = 2;
            i3 = 128;
        } else if ((b & JSONB.Constants.BC_INT32_NUM_MIN) == 224) {
            i = b & 15;
            i2 = 3;
            i3 = 2048;
        } else {
            if ((b & 248) != 240) {
                commonReadUtf8CodePoint.skip(1L);
                return Utf8.REPLACEMENT_CODE_POINT;
            }
            i = b & 7;
            i2 = 4;
            i3 = 65536;
        }
        long j = i2;
        if (commonReadUtf8CodePoint.size() < j) {
            throw new EOFException("size < " + i2 + ": " + commonReadUtf8CodePoint.size() + " (to read code point prefixed 0x" + Util.toHexString(b) + ')');
        }
        for (int i4 = 1; i4 < i2; i4++) {
            long j2 = i4;
            byte b2 = commonReadUtf8CodePoint.getByte(j2);
            if ((b2 & JSONB.Constants.BC_INT64_SHORT_MIN) != 128) {
                commonReadUtf8CodePoint.skip(j2);
                return Utf8.REPLACEMENT_CODE_POINT;
            }
            i = (i << 6) | (b2 & 63);
        }
        commonReadUtf8CodePoint.skip(j);
        return i > 1114111 ? Utf8.REPLACEMENT_CODE_POINT : ((55296 <= i && 57343 >= i) || i < i3) ? Utf8.REPLACEMENT_CODE_POINT : i;
    }

    public static final Buffer commonWriteUtf8(Buffer commonWriteUtf8, String string, int i, int i2) {
        int i3;
        Intrinsics.checkNotNullParameter(commonWriteUtf8, "$this$commonWriteUtf8");
        Intrinsics.checkNotNullParameter(string, "string");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("beginIndex < 0: " + i).toString());
        }
        if (!(i2 >= i)) {
            throw new IllegalArgumentException(("endIndex < beginIndex: " + i2 + " < " + i).toString());
        }
        if (!(i2 <= string.length())) {
            throw new IllegalArgumentException(("endIndex > string.length: " + i2 + " > " + string.length()).toString());
        }
        while (i < i2) {
            char cCharAt = string.charAt(i);
            if (cCharAt < 128) {
                Segment segmentWritableSegment$okio = commonWriteUtf8.writableSegment$okio(1);
                byte[] bArr = segmentWritableSegment$okio.data;
                int i4 = segmentWritableSegment$okio.limit - i;
                int iMin = Math.min(i2, 8192 - i4);
                i3 = i + 1;
                bArr[i + i4] = (byte) cCharAt;
                while (i3 < iMin) {
                    char cCharAt2 = string.charAt(i3);
                    if (cCharAt2 >= 128) {
                        break;
                    }
                    bArr[i3 + i4] = (byte) cCharAt2;
                    i3++;
                }
                int i5 = (i4 + i3) - segmentWritableSegment$okio.limit;
                segmentWritableSegment$okio.limit += i5;
                commonWriteUtf8.setSize$okio(commonWriteUtf8.size() + ((long) i5));
            } else {
                if (cCharAt < 2048) {
                    Segment segmentWritableSegment$okio2 = commonWriteUtf8.writableSegment$okio(2);
                    segmentWritableSegment$okio2.data[segmentWritableSegment$okio2.limit] = (byte) ((cCharAt >> 6) | Opcodes.CHECKCAST);
                    segmentWritableSegment$okio2.data[segmentWritableSegment$okio2.limit + 1] = (byte) ((cCharAt & '?') | 128);
                    segmentWritableSegment$okio2.limit += 2;
                    commonWriteUtf8.setSize$okio(commonWriteUtf8.size() + 2);
                } else if (cCharAt < 55296 || cCharAt > 57343) {
                    Segment segmentWritableSegment$okio3 = commonWriteUtf8.writableSegment$okio(3);
                    segmentWritableSegment$okio3.data[segmentWritableSegment$okio3.limit] = (byte) ((cCharAt >> '\f') | 224);
                    segmentWritableSegment$okio3.data[segmentWritableSegment$okio3.limit + 1] = (byte) ((63 & (cCharAt >> 6)) | 128);
                    segmentWritableSegment$okio3.data[segmentWritableSegment$okio3.limit + 2] = (byte) ((cCharAt & '?') | 128);
                    segmentWritableSegment$okio3.limit += 3;
                    commonWriteUtf8.setSize$okio(commonWriteUtf8.size() + 3);
                } else {
                    i3 = i + 1;
                    char cCharAt3 = i3 < i2 ? string.charAt(i3) : (char) 0;
                    if (cCharAt > 56319 || 56320 > cCharAt3 || 57343 < cCharAt3) {
                        commonWriteUtf8.writeByte(63);
                    } else {
                        int i6 = (((cCharAt & 1023) << 10) | (cCharAt3 & 1023)) + 65536;
                        Segment segmentWritableSegment$okio4 = commonWriteUtf8.writableSegment$okio(4);
                        segmentWritableSegment$okio4.data[segmentWritableSegment$okio4.limit] = (byte) ((i6 >> 18) | 240);
                        segmentWritableSegment$okio4.data[segmentWritableSegment$okio4.limit + 1] = (byte) (((i6 >> 12) & 63) | 128);
                        segmentWritableSegment$okio4.data[segmentWritableSegment$okio4.limit + 2] = (byte) (((i6 >> 6) & 63) | 128);
                        segmentWritableSegment$okio4.data[segmentWritableSegment$okio4.limit + 3] = (byte) ((i6 & 63) | 128);
                        segmentWritableSegment$okio4.limit += 4;
                        commonWriteUtf8.setSize$okio(commonWriteUtf8.size() + 4);
                        i += 2;
                    }
                }
                i++;
            }
            i = i3;
        }
        return commonWriteUtf8;
    }

    public static final Buffer commonWriteUtf8CodePoint(Buffer commonWriteUtf8CodePoint, int i) {
        Intrinsics.checkNotNullParameter(commonWriteUtf8CodePoint, "$this$commonWriteUtf8CodePoint");
        if (i < 128) {
            commonWriteUtf8CodePoint.writeByte(i);
            return commonWriteUtf8CodePoint;
        }
        if (i < 2048) {
            Segment segmentWritableSegment$okio = commonWriteUtf8CodePoint.writableSegment$okio(2);
            segmentWritableSegment$okio.data[segmentWritableSegment$okio.limit] = (byte) ((i >> 6) | Opcodes.CHECKCAST);
            segmentWritableSegment$okio.data[segmentWritableSegment$okio.limit + 1] = (byte) ((i & 63) | 128);
            segmentWritableSegment$okio.limit += 2;
            commonWriteUtf8CodePoint.setSize$okio(commonWriteUtf8CodePoint.size() + 2);
            return commonWriteUtf8CodePoint;
        }
        if (55296 <= i && 57343 >= i) {
            commonWriteUtf8CodePoint.writeByte(63);
            return commonWriteUtf8CodePoint;
        }
        if (i < 65536) {
            Segment segmentWritableSegment$okio2 = commonWriteUtf8CodePoint.writableSegment$okio(3);
            segmentWritableSegment$okio2.data[segmentWritableSegment$okio2.limit] = (byte) ((i >> 12) | 224);
            segmentWritableSegment$okio2.data[segmentWritableSegment$okio2.limit + 1] = (byte) (((i >> 6) & 63) | 128);
            segmentWritableSegment$okio2.data[segmentWritableSegment$okio2.limit + 2] = (byte) ((i & 63) | 128);
            segmentWritableSegment$okio2.limit += 3;
            commonWriteUtf8CodePoint.setSize$okio(commonWriteUtf8CodePoint.size() + 3);
            return commonWriteUtf8CodePoint;
        }
        if (i <= 1114111) {
            Segment segmentWritableSegment$okio3 = commonWriteUtf8CodePoint.writableSegment$okio(4);
            segmentWritableSegment$okio3.data[segmentWritableSegment$okio3.limit] = (byte) ((i >> 18) | 240);
            segmentWritableSegment$okio3.data[segmentWritableSegment$okio3.limit + 1] = (byte) (((i >> 12) & 63) | 128);
            segmentWritableSegment$okio3.data[segmentWritableSegment$okio3.limit + 2] = (byte) (((i >> 6) & 63) | 128);
            segmentWritableSegment$okio3.data[segmentWritableSegment$okio3.limit + 3] = (byte) ((i & 63) | 128);
            segmentWritableSegment$okio3.limit += 4;
            commonWriteUtf8CodePoint.setSize$okio(commonWriteUtf8CodePoint.size() + 4);
            return commonWriteUtf8CodePoint;
        }
        throw new IllegalArgumentException("Unexpected code point: 0x" + Util.toHexString(i));
    }

    public static final long commonWriteAll(Buffer commonWriteAll, Source source) throws IOException {
        Intrinsics.checkNotNullParameter(commonWriteAll, "$this$commonWriteAll");
        Intrinsics.checkNotNullParameter(source, "source");
        long j = 0;
        while (true) {
            long j2 = source.read(commonWriteAll, 8192);
            if (j2 == -1) {
                return j;
            }
            j += j2;
        }
    }

    public static final Buffer commonWrite(Buffer commonWrite, Source source, long j) throws IOException {
        Intrinsics.checkNotNullParameter(commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        while (j > 0) {
            long j2 = source.read(commonWrite, j);
            if (j2 == -1) {
                throw new EOFException();
            }
            j -= j2;
        }
        return commonWrite;
    }

    public static final Buffer commonWriteByte(Buffer commonWriteByte, int i) {
        Intrinsics.checkNotNullParameter(commonWriteByte, "$this$commonWriteByte");
        Segment segmentWritableSegment$okio = commonWriteByte.writableSegment$okio(1);
        byte[] bArr = segmentWritableSegment$okio.data;
        int i2 = segmentWritableSegment$okio.limit;
        segmentWritableSegment$okio.limit = i2 + 1;
        bArr[i2] = (byte) i;
        commonWriteByte.setSize$okio(commonWriteByte.size() + 1);
        return commonWriteByte;
    }

    public static final Buffer commonWriteShort(Buffer commonWriteShort, int i) {
        Intrinsics.checkNotNullParameter(commonWriteShort, "$this$commonWriteShort");
        Segment segmentWritableSegment$okio = commonWriteShort.writableSegment$okio(2);
        byte[] bArr = segmentWritableSegment$okio.data;
        int i2 = segmentWritableSegment$okio.limit;
        bArr[i2] = (byte) ((i >>> 8) & 255);
        bArr[i2 + 1] = (byte) (i & 255);
        segmentWritableSegment$okio.limit = i2 + 2;
        commonWriteShort.setSize$okio(commonWriteShort.size() + 2);
        return commonWriteShort;
    }

    public static final Buffer commonWriteInt(Buffer commonWriteInt, int i) {
        Intrinsics.checkNotNullParameter(commonWriteInt, "$this$commonWriteInt");
        Segment segmentWritableSegment$okio = commonWriteInt.writableSegment$okio(4);
        byte[] bArr = segmentWritableSegment$okio.data;
        int i2 = segmentWritableSegment$okio.limit;
        bArr[i2] = (byte) ((i >>> 24) & 255);
        bArr[i2 + 1] = (byte) ((i >>> 16) & 255);
        bArr[i2 + 2] = (byte) ((i >>> 8) & 255);
        bArr[i2 + 3] = (byte) (i & 255);
        segmentWritableSegment$okio.limit = i2 + 4;
        commonWriteInt.setSize$okio(commonWriteInt.size() + 4);
        return commonWriteInt;
    }

    public static final Buffer commonWriteLong(Buffer commonWriteLong, long j) {
        Intrinsics.checkNotNullParameter(commonWriteLong, "$this$commonWriteLong");
        Segment segmentWritableSegment$okio = commonWriteLong.writableSegment$okio(8);
        byte[] bArr = segmentWritableSegment$okio.data;
        int i = segmentWritableSegment$okio.limit;
        bArr[i] = (byte) ((j >>> 56) & 255);
        bArr[i + 1] = (byte) ((j >>> 48) & 255);
        bArr[i + 2] = (byte) ((j >>> 40) & 255);
        bArr[i + 3] = (byte) ((j >>> 32) & 255);
        bArr[i + 4] = (byte) ((j >>> 24) & 255);
        bArr[i + 5] = (byte) ((j >>> 16) & 255);
        bArr[i + 6] = (byte) ((j >>> 8) & 255);
        bArr[i + 7] = (byte) (j & 255);
        segmentWritableSegment$okio.limit = i + 8;
        commonWriteLong.setSize$okio(commonWriteLong.size() + 8);
        return commonWriteLong;
    }

    public static final void commonWrite(Buffer commonWrite, Buffer source, long j) {
        Segment segment;
        Intrinsics.checkNotNullParameter(commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        if (!(source != commonWrite)) {
            throw new IllegalArgumentException("source == this".toString());
        }
        Util.checkOffsetAndCount(source.size(), 0L, j);
        while (j > 0) {
            Segment segment2 = source.head;
            Intrinsics.checkNotNull(segment2);
            int i = segment2.limit;
            Intrinsics.checkNotNull(source.head);
            if (j < i - r2.pos) {
                if (commonWrite.head != null) {
                    Segment segment3 = commonWrite.head;
                    Intrinsics.checkNotNull(segment3);
                    segment = segment3.prev;
                } else {
                    segment = null;
                }
                if (segment != null && segment.owner) {
                    if ((((long) segment.limit) + j) - ((long) (segment.shared ? 0 : segment.pos)) <= 8192) {
                        Segment segment4 = source.head;
                        Intrinsics.checkNotNull(segment4);
                        segment4.writeTo(segment, (int) j);
                        source.setSize$okio(source.size() - j);
                        commonWrite.setSize$okio(commonWrite.size() + j);
                        return;
                    }
                }
                Segment segment5 = source.head;
                Intrinsics.checkNotNull(segment5);
                source.head = segment5.split((int) j);
            }
            Segment segment6 = source.head;
            Intrinsics.checkNotNull(segment6);
            long j2 = segment6.limit - segment6.pos;
            source.head = segment6.pop();
            if (commonWrite.head == null) {
                commonWrite.head = segment6;
                segment6.prev = segment6;
                segment6.next = segment6.prev;
            } else {
                Segment segment7 = commonWrite.head;
                Intrinsics.checkNotNull(segment7);
                Segment segment8 = segment7.prev;
                Intrinsics.checkNotNull(segment8);
                segment8.push(segment6).compact();
            }
            source.setSize$okio(source.size() - j2);
            commonWrite.setSize$okio(commonWrite.size() + j2);
            j -= j2;
        }
    }

    public static final long commonRead(Buffer commonRead, Buffer sink, long j) {
        Intrinsics.checkNotNullParameter(commonRead, "$this$commonRead");
        Intrinsics.checkNotNullParameter(sink, "sink");
        if (!(j >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + j).toString());
        }
        if (commonRead.size() == 0) {
            return -1L;
        }
        if (j > commonRead.size()) {
            j = commonRead.size();
        }
        sink.write(commonRead, j);
        return j;
    }

    public static final long commonIndexOf(Buffer commonIndexOf, byte b, long j, long j2) {
        int i;
        Intrinsics.checkNotNullParameter(commonIndexOf, "$this$commonIndexOf");
        long size = 0;
        if (!(0 <= j && j2 >= j)) {
            throw new IllegalArgumentException(("size=" + commonIndexOf.size() + " fromIndex=" + j + " toIndex=" + j2).toString());
        }
        if (j2 > commonIndexOf.size()) {
            j2 = commonIndexOf.size();
        }
        if (j == j2) {
            return -1L;
        }
        Segment segment = commonIndexOf.head;
        if (segment != null) {
            if (commonIndexOf.size() - j < j) {
                size = commonIndexOf.size();
                while (size > j) {
                    segment = segment.prev;
                    Intrinsics.checkNotNull(segment);
                    size -= (long) (segment.limit - segment.pos);
                }
                if (segment != null) {
                    while (size < j2) {
                        byte[] bArr = segment.data;
                        int iMin = (int) Math.min(segment.limit, (((long) segment.pos) + j2) - size);
                        i = (int) ((((long) segment.pos) + j) - size);
                        while (i < iMin) {
                            if (bArr[i] != b) {
                                i++;
                            }
                        }
                        size += (long) (segment.limit - segment.pos);
                        segment = segment.next;
                        Intrinsics.checkNotNull(segment);
                        j = size;
                    }
                }
                return -1L;
            }
            while (true) {
                long j3 = ((long) (segment.limit - segment.pos)) + size;
                if (j3 > j) {
                    break;
                }
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                size = j3;
            }
            if (segment != null) {
                while (size < j2) {
                    byte[] bArr2 = segment.data;
                    int iMin2 = (int) Math.min(segment.limit, (((long) segment.pos) + j2) - size);
                    i = (int) ((((long) segment.pos) + j) - size);
                    while (i < iMin2) {
                        if (bArr2[i] != b) {
                            i++;
                        }
                    }
                    size += (long) (segment.limit - segment.pos);
                    segment = segment.next;
                    Intrinsics.checkNotNull(segment);
                    j = size;
                }
            }
            return -1L;
            return ((long) (i - segment.pos)) + size;
        }
        return -1L;
    }

    public static final long commonIndexOf(Buffer commonIndexOf, ByteString bytes, long j) {
        long j2 = j;
        Intrinsics.checkNotNullParameter(commonIndexOf, "$this$commonIndexOf");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (!(bytes.size() > 0)) {
            throw new IllegalArgumentException("bytes is empty".toString());
        }
        long j3 = 0;
        if (!(j2 >= 0)) {
            throw new IllegalArgumentException(("fromIndex < 0: " + j2).toString());
        }
        Segment segment = commonIndexOf.head;
        if (segment == null) {
            return -1L;
        }
        if (commonIndexOf.size() - j2 < j2) {
            long size = commonIndexOf.size();
            while (size > j2) {
                segment = segment.prev;
                Intrinsics.checkNotNull(segment);
                size -= (long) (segment.limit - segment.pos);
            }
            if (segment == null) {
                return -1L;
            }
            byte[] bArrInternalArray$okio = bytes.internalArray$okio();
            byte b = bArrInternalArray$okio[0];
            int size2 = bytes.size();
            long size3 = (commonIndexOf.size() - ((long) size2)) + 1;
            while (size < size3) {
                byte[] bArr = segment.data;
                int iMin = (int) Math.min(segment.limit, (((long) segment.pos) + size3) - size);
                for (int i = (int) ((((long) segment.pos) + j2) - size); i < iMin; i++) {
                    if (bArr[i] == b && rangeEquals(segment, i + 1, bArrInternalArray$okio, 1, size2)) {
                        return ((long) (i - segment.pos)) + size;
                    }
                }
                size += (long) (segment.limit - segment.pos);
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j2 = size;
            }
            return -1L;
        }
        while (true) {
            long j4 = ((long) (segment.limit - segment.pos)) + j3;
            if (j4 > j2) {
                break;
            }
            segment = segment.next;
            Intrinsics.checkNotNull(segment);
            j3 = j4;
        }
        if (segment != null) {
            byte[] bArrInternalArray$okio2 = bytes.internalArray$okio();
            byte b2 = bArrInternalArray$okio2[0];
            int size4 = bytes.size();
            long size5 = (commonIndexOf.size() - ((long) size4)) + 1;
            while (j3 < size5) {
                byte[] bArr2 = segment.data;
                long j5 = j3;
                int iMin2 = (int) Math.min(segment.limit, (((long) segment.pos) + size5) - j3);
                for (int i2 = (int) ((((long) segment.pos) + j2) - j5); i2 < iMin2; i2++) {
                    if (bArr2[i2] == b2 && rangeEquals(segment, i2 + 1, bArrInternalArray$okio2, 1, size4)) {
                        return ((long) (i2 - segment.pos)) + j5;
                    }
                }
                j3 = j5 + ((long) (segment.limit - segment.pos));
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j2 = j3;
            }
        }
        return -1L;
    }

    public static final boolean commonRangeEquals(Buffer commonRangeEquals, long j, ByteString bytes, int i, int i2) {
        Intrinsics.checkNotNullParameter(commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (j < 0 || i < 0 || i2 < 0 || commonRangeEquals.size() - j < i2 || bytes.size() - i < i2) {
            return false;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            if (commonRangeEquals.getByte(((long) i3) + j) != bytes.getByte(i + i3)) {
                return false;
            }
        }
        return true;
    }

    public static final boolean commonEquals(Buffer commonEquals, Object obj) {
        Intrinsics.checkNotNullParameter(commonEquals, "$this$commonEquals");
        if (commonEquals == obj) {
            return true;
        }
        if (!(obj instanceof Buffer)) {
            return false;
        }
        Buffer buffer = (Buffer) obj;
        if (commonEquals.size() != buffer.size()) {
            return false;
        }
        if (commonEquals.size() == 0) {
            return true;
        }
        Segment segment = commonEquals.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = buffer.head;
        Intrinsics.checkNotNull(segment2);
        int i = segment.pos;
        int i2 = segment2.pos;
        long j = 0;
        while (j < commonEquals.size()) {
            long jMin = Math.min(segment.limit - i, segment2.limit - i2);
            long j2 = 0;
            while (j2 < jMin) {
                int i3 = i + 1;
                int i4 = i2 + 1;
                if (segment.data[i] != segment2.data[i2]) {
                    return false;
                }
                j2++;
                i = i3;
                i2 = i4;
            }
            if (i == segment.limit) {
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                i = segment.pos;
            }
            if (i2 == segment2.limit) {
                segment2 = segment2.next;
                Intrinsics.checkNotNull(segment2);
                i2 = segment2.pos;
            }
            j += jMin;
        }
        return true;
    }

    public static final int commonHashCode(Buffer commonHashCode) {
        Intrinsics.checkNotNullParameter(commonHashCode, "$this$commonHashCode");
        Segment segment = commonHashCode.head;
        if (segment == null) {
            return 0;
        }
        int i = 1;
        do {
            int i2 = segment.limit;
            for (int i3 = segment.pos; i3 < i2; i3++) {
                i = (i * 31) + segment.data[i3];
            }
            segment = segment.next;
            Intrinsics.checkNotNull(segment);
        } while (segment != commonHashCode.head);
        return i;
    }

    public static final Buffer commonCopy(Buffer commonCopy) {
        Intrinsics.checkNotNullParameter(commonCopy, "$this$commonCopy");
        Buffer buffer = new Buffer();
        if (commonCopy.size() == 0) {
            return buffer;
        }
        Segment segment = commonCopy.head;
        Intrinsics.checkNotNull(segment);
        Segment segmentSharedCopy = segment.sharedCopy();
        buffer.head = segmentSharedCopy;
        segmentSharedCopy.prev = buffer.head;
        segmentSharedCopy.next = segmentSharedCopy.prev;
        for (Segment segment2 = segment.next; segment2 != segment; segment2 = segment2.next) {
            Segment segment3 = segmentSharedCopy.prev;
            Intrinsics.checkNotNull(segment3);
            Intrinsics.checkNotNull(segment2);
            segment3.push(segment2.sharedCopy());
        }
        buffer.setSize$okio(commonCopy.size());
        return buffer;
    }

    public static final ByteString commonSnapshot(Buffer commonSnapshot) {
        Intrinsics.checkNotNullParameter(commonSnapshot, "$this$commonSnapshot");
        if (!(commonSnapshot.size() <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalStateException(("size > Int.MAX_VALUE: " + commonSnapshot.size()).toString());
        }
        return commonSnapshot.snapshot((int) commonSnapshot.size());
    }

    public static final ByteString commonSnapshot(Buffer commonSnapshot, int i) {
        Intrinsics.checkNotNullParameter(commonSnapshot, "$this$commonSnapshot");
        if (i == 0) {
            return ByteString.EMPTY;
        }
        Util.checkOffsetAndCount(commonSnapshot.size(), 0L, i);
        Segment segment = commonSnapshot.head;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            Intrinsics.checkNotNull(segment);
            if (segment.limit == segment.pos) {
                throw new AssertionError("s.limit == s.pos");
            }
            i3 += segment.limit - segment.pos;
            i4++;
            segment = segment.next;
        }
        byte[][] bArr = new byte[i4][];
        int[] iArr = new int[i4 * 2];
        Segment segment2 = commonSnapshot.head;
        int i5 = 0;
        while (i2 < i) {
            Intrinsics.checkNotNull(segment2);
            bArr[i5] = segment2.data;
            i2 += segment2.limit - segment2.pos;
            iArr[i5] = Math.min(i2, i);
            iArr[bArr.length + i5] = segment2.pos;
            segment2.shared = true;
            i5++;
            segment2 = segment2.next;
        }
        return new SegmentedByteString(bArr, iArr);
    }

    public static final long commonIndexOfElement(Buffer commonIndexOfElement, ByteString targetBytes, long j) {
        int i;
        int i2;
        Intrinsics.checkNotNullParameter(commonIndexOfElement, "$this$commonIndexOfElement");
        Intrinsics.checkNotNullParameter(targetBytes, "targetBytes");
        long size = 0;
        if (!(j >= 0)) {
            throw new IllegalArgumentException(("fromIndex < 0: " + j).toString());
        }
        Segment segment = commonIndexOfElement.head;
        if (segment == null) {
            return -1L;
        }
        if (commonIndexOfElement.size() - j < j) {
            size = commonIndexOfElement.size();
            while (size > j) {
                segment = segment.prev;
                Intrinsics.checkNotNull(segment);
                size -= (long) (segment.limit - segment.pos);
            }
            if (segment != null) {
                if (targetBytes.size() == 2) {
                    byte b = targetBytes.getByte(0);
                    byte b2 = targetBytes.getByte(1);
                    while (size < commonIndexOfElement.size()) {
                        byte[] bArr = segment.data;
                        i = (int) ((((long) segment.pos) + j) - size);
                        int i3 = segment.limit;
                        while (i < i3) {
                            byte b3 = bArr[i];
                            if (b3 == b || b3 == b2) {
                                i2 = segment.pos;
                            } else {
                                i++;
                            }
                        }
                        size += (long) (segment.limit - segment.pos);
                        segment = segment.next;
                        Intrinsics.checkNotNull(segment);
                        j = size;
                    }
                } else {
                    byte[] bArrInternalArray$okio = targetBytes.internalArray$okio();
                    while (size < commonIndexOfElement.size()) {
                        byte[] bArr2 = segment.data;
                        i = (int) ((((long) segment.pos) + j) - size);
                        int i4 = segment.limit;
                        while (i < i4) {
                            byte b4 = bArr2[i];
                            for (byte b5 : bArrInternalArray$okio) {
                                if (b4 == b5) {
                                    i2 = segment.pos;
                                }
                            }
                            i++;
                        }
                        size += (long) (segment.limit - segment.pos);
                        segment = segment.next;
                        Intrinsics.checkNotNull(segment);
                        j = size;
                    }
                }
            }
            return -1L;
        }
        while (true) {
            long j2 = ((long) (segment.limit - segment.pos)) + size;
            if (j2 > j) {
                break;
            }
            segment = segment.next;
            Intrinsics.checkNotNull(segment);
            size = j2;
        }
        if (segment != null) {
            if (targetBytes.size() == 2) {
                byte b6 = targetBytes.getByte(0);
                byte b7 = targetBytes.getByte(1);
                while (size < commonIndexOfElement.size()) {
                    byte[] bArr3 = segment.data;
                    i = (int) ((((long) segment.pos) + j) - size);
                    int i5 = segment.limit;
                    while (i < i5) {
                        byte b8 = bArr3[i];
                        if (b8 == b6 || b8 == b7) {
                            i2 = segment.pos;
                        } else {
                            i++;
                        }
                    }
                    size += (long) (segment.limit - segment.pos);
                    segment = segment.next;
                    Intrinsics.checkNotNull(segment);
                    j = size;
                }
            } else {
                byte[] bArrInternalArray$okio2 = targetBytes.internalArray$okio();
                while (size < commonIndexOfElement.size()) {
                    byte[] bArr4 = segment.data;
                    i = (int) ((((long) segment.pos) + j) - size);
                    int i6 = segment.limit;
                    while (i < i6) {
                        byte b9 = bArr4[i];
                        for (byte b10 : bArrInternalArray$okio2) {
                            if (b9 == b10) {
                                i2 = segment.pos;
                            }
                        }
                        i++;
                    }
                    size += (long) (segment.limit - segment.pos);
                    segment = segment.next;
                    Intrinsics.checkNotNull(segment);
                    j = size;
                }
            }
        }
        return -1L;
        return ((long) (i - i2)) + size;
    }
}
