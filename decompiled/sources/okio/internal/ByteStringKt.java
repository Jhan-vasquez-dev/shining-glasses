package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okio.Base64;
import okio.Buffer;
import okio.ByteString;
import okio.Platform;
import okio.SegmentedByteString;
import okio.Util;

/* JADX INFO: compiled from: ByteString.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000X\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005H\u0002\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0007H\u0080\b\u001a\u0010\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eH\u0002\u001a\r\u0010\u000f\u001a\u00020\u0010*\u00020\nH\u0080\b\u001a\r\u0010\u0011\u001a\u00020\u0010*\u00020\nH\u0080\b\u001a\u0015\u0010\u0012\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\nH\u0080\b\u001a\u000f\u0010\u0014\u001a\u0004\u0018\u00010\n*\u00020\u0010H\u0080\b\u001a\r\u0010\u0015\u001a\u00020\n*\u00020\u0010H\u0080\b\u001a\u0014\u0010\u0016\u001a\u00020\n*\u00020\n2\u0006\u0010\u0017\u001a\u00020\u0010H\u0000\u001a\r\u0010\u0018\u001a\u00020\n*\u00020\u0010H\u0080\b\u001a\u0015\u0010\u0019\u001a\u00020\u001a*\u00020\n2\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010\u0019\u001a\u00020\u001a*\u00020\n2\u0006\u0010\u001b\u001a\u00020\nH\u0080\b\u001a\u0017\u0010\u001c\u001a\u00020\u001a*\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\u001dH\u0080\b\u001a\u0015\u0010\u001e\u001a\u00020\u001f*\u00020\n2\u0006\u0010 \u001a\u00020\u0005H\u0080\b\u001a\r\u0010!\u001a\u00020\u0005*\u00020\nH\u0080\b\u001a\r\u0010\"\u001a\u00020\u0005*\u00020\nH\u0080\b\u001a\r\u0010#\u001a\u00020\u0010*\u00020\nH\u0080\b\u001a\u001d\u0010$\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010%\u001a\u00020\u0005H\u0080\b\u001a\r\u0010&\u001a\u00020\u0007*\u00020\nH\u0080\b\u001a\u001d\u0010'\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010%\u001a\u00020\u0005H\u0080\b\u001a\u001d\u0010'\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\n2\u0006\u0010%\u001a\u00020\u0005H\u0080\b\u001a-\u0010(\u001a\u00020\u001a*\u00020\n2\u0006\u0010)\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010*\u001a\u00020\u00052\u0006\u0010+\u001a\u00020\u0005H\u0080\b\u001a-\u0010(\u001a\u00020\u001a*\u00020\n2\u0006\u0010)\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\n2\u0006\u0010*\u001a\u00020\u00052\u0006\u0010+\u001a\u00020\u0005H\u0080\b\u001a\u0014\u0010,\u001a\u00020\n*\u00020-2\u0006\u0010\u0017\u001a\u00020\u0010H\u0000\u001a\u0015\u0010.\u001a\u00020\u001a*\u00020\n2\u0006\u0010/\u001a\u00020\u0007H\u0080\b\u001a\u0015\u0010.\u001a\u00020\u001a*\u00020\n2\u0006\u0010/\u001a\u00020\nH\u0080\b\u001a\u001d\u00100\u001a\u00020\n*\u00020\n2\u0006\u00101\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u0005H\u0080\b\u001a\r\u00103\u001a\u00020\n*\u00020\nH\u0080\b\u001a\r\u00104\u001a\u00020\n*\u00020\nH\u0080\b\u001a\r\u00105\u001a\u00020\u0007*\u00020\nH\u0080\b\u001a\u001d\u00106\u001a\u00020\n*\u00020\u00072\u0006\u0010)\u001a\u00020\u00052\u0006\u0010+\u001a\u00020\u0005H\u0080\b\u001a\r\u00107\u001a\u00020\u0010*\u00020\nH\u0080\b\u001a\r\u00108\u001a\u00020\u0010*\u00020\nH\u0080\b\u001a$\u00109\u001a\u00020:*\u00020\n2\u0006\u0010;\u001a\u00020<2\u0006\u0010)\u001a\u00020\u00052\u0006\u0010+\u001a\u00020\u0005H\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003¨\u0006="}, d2 = {"HEX_DIGIT_CHARS", "", "getHEX_DIGIT_CHARS", "()[C", "codePointIndexToCharIndex", "", "s", "", "codePointCount", "commonOf", "Lokio/ByteString;", "data", "decodeHexDigit", "c", "", "commonBase64", "", "commonBase64Url", "commonCompareTo", "other", "commonDecodeBase64", "commonDecodeHex", "commonDigest", "algorithm", "commonEncodeUtf8", "commonEndsWith", "", "suffix", "commonEquals", "", "commonGetByte", "", "pos", "commonGetSize", "commonHashCode", "commonHex", "commonIndexOf", "fromIndex", "commonInternalArray", "commonLastIndexOf", "commonRangeEquals", "offset", "otherOffset", "byteCount", "commonSegmentDigest", "Lokio/SegmentedByteString;", "commonStartsWith", "prefix", "commonSubstring", "beginIndex", "endIndex", "commonToAsciiLowercase", "commonToAsciiUppercase", "commonToByteArray", "commonToByteString", "commonToString", "commonUtf8", "commonWrite", "", "buffer", "Lokio/Buffer;", "okio"}, k = 2, mv = {1, 4, 0})
public final class ByteStringKt {
    private static final char[] HEX_DIGIT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static final String commonUtf8(ByteString commonUtf8) {
        Intrinsics.checkNotNullParameter(commonUtf8, "$this$commonUtf8");
        String utf8$okio = commonUtf8.getUtf8$okio();
        if (utf8$okio != null) {
            return utf8$okio;
        }
        String utf8String = Platform.toUtf8String(commonUtf8.internalArray$okio());
        commonUtf8.setUtf8$okio(utf8String);
        return utf8String;
    }

    public static final String commonBase64(ByteString commonBase64) {
        Intrinsics.checkNotNullParameter(commonBase64, "$this$commonBase64");
        return Base64.encodeBase64$default(commonBase64.getData$okio(), null, 1, null);
    }

    public static final String commonBase64Url(ByteString commonBase64Url) {
        Intrinsics.checkNotNullParameter(commonBase64Url, "$this$commonBase64Url");
        return Base64.encodeBase64(commonBase64Url.getData$okio(), Base64.getBASE64_URL_SAFE());
    }

    public static final char[] getHEX_DIGIT_CHARS() {
        return HEX_DIGIT_CHARS;
    }

    public static final String commonHex(ByteString commonHex) {
        Intrinsics.checkNotNullParameter(commonHex, "$this$commonHex");
        char[] cArr = new char[commonHex.getData$okio().length * 2];
        int i = 0;
        for (byte b : commonHex.getData$okio()) {
            int i2 = i + 1;
            cArr[i] = getHEX_DIGIT_CHARS()[(b >> 4) & 15];
            i += 2;
            cArr[i2] = getHEX_DIGIT_CHARS()[b & 15];
        }
        return new String(cArr);
    }

    public static final ByteString commonDigest(ByteString commonDigest, String algorithm) {
        Intrinsics.checkNotNullParameter(commonDigest, "$this$commonDigest");
        Intrinsics.checkNotNullParameter(algorithm, "algorithm");
        HashFunction hashFunctionNewHashFunction = HashFunctionKt.newHashFunction(algorithm);
        hashFunctionNewHashFunction.update(commonDigest.getData$okio(), 0, commonDigest.size());
        return new ByteString(hashFunctionNewHashFunction.digest());
    }

    public static final ByteString commonSegmentDigest(SegmentedByteString commonSegmentDigest, String algorithm) {
        Intrinsics.checkNotNullParameter(commonSegmentDigest, "$this$commonSegmentDigest");
        Intrinsics.checkNotNullParameter(algorithm, "algorithm");
        HashFunction hashFunctionNewHashFunction = HashFunctionKt.newHashFunction(algorithm);
        int length = commonSegmentDigest.getSegments().length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = commonSegmentDigest.getDirectory()[length + i];
            int i4 = commonSegmentDigest.getDirectory()[i];
            hashFunctionNewHashFunction.update(commonSegmentDigest.getSegments()[i], i3, i4 - i2);
            i++;
            i2 = i4;
        }
        return new ByteString(hashFunctionNewHashFunction.digest());
    }

    public static final ByteString commonToAsciiLowercase(ByteString commonToAsciiLowercase) {
        byte b;
        Intrinsics.checkNotNullParameter(commonToAsciiLowercase, "$this$commonToAsciiLowercase");
        for (int i = 0; i < commonToAsciiLowercase.getData$okio().length; i++) {
            byte b2 = commonToAsciiLowercase.getData$okio()[i];
            byte b3 = (byte) 65;
            if (b2 >= b3 && b2 <= (b = (byte) 90)) {
                byte[] data$okio = commonToAsciiLowercase.getData$okio();
                byte[] bArrCopyOf = Arrays.copyOf(data$okio, data$okio.length);
                Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
                bArrCopyOf[i] = (byte) (b2 + 32);
                for (int i2 = i + 1; i2 < bArrCopyOf.length; i2++) {
                    byte b4 = bArrCopyOf[i2];
                    if (b4 >= b3 && b4 <= b) {
                        bArrCopyOf[i2] = (byte) (b4 + 32);
                    }
                }
                return new ByteString(bArrCopyOf);
            }
        }
        return commonToAsciiLowercase;
    }

    public static final ByteString commonToAsciiUppercase(ByteString commonToAsciiUppercase) {
        byte b;
        Intrinsics.checkNotNullParameter(commonToAsciiUppercase, "$this$commonToAsciiUppercase");
        for (int i = 0; i < commonToAsciiUppercase.getData$okio().length; i++) {
            byte b2 = commonToAsciiUppercase.getData$okio()[i];
            byte b3 = (byte) 97;
            if (b2 >= b3 && b2 <= (b = (byte) 122)) {
                byte[] data$okio = commonToAsciiUppercase.getData$okio();
                byte[] bArrCopyOf = Arrays.copyOf(data$okio, data$okio.length);
                Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
                bArrCopyOf[i] = (byte) (b2 - 32);
                for (int i2 = i + 1; i2 < bArrCopyOf.length; i2++) {
                    byte b4 = bArrCopyOf[i2];
                    if (b4 >= b3 && b4 <= b) {
                        bArrCopyOf[i2] = (byte) (b4 - 32);
                    }
                }
                return new ByteString(bArrCopyOf);
            }
        }
        return commonToAsciiUppercase;
    }

    public static final ByteString commonSubstring(ByteString commonSubstring, int i, int i2) {
        Intrinsics.checkNotNullParameter(commonSubstring, "$this$commonSubstring");
        if (!(i >= 0)) {
            throw new IllegalArgumentException("beginIndex < 0".toString());
        }
        if (!(i2 <= commonSubstring.getData$okio().length)) {
            throw new IllegalArgumentException(("endIndex > length(" + commonSubstring.getData$okio().length + ')').toString());
        }
        if (i2 - i >= 0) {
            return (i == 0 && i2 == commonSubstring.getData$okio().length) ? commonSubstring : new ByteString(ArraysKt.copyOfRange(commonSubstring.getData$okio(), i, i2));
        }
        throw new IllegalArgumentException("endIndex < beginIndex".toString());
    }

    public static final byte commonGetByte(ByteString commonGetByte, int i) {
        Intrinsics.checkNotNullParameter(commonGetByte, "$this$commonGetByte");
        return commonGetByte.getData$okio()[i];
    }

    public static final int commonGetSize(ByteString commonGetSize) {
        Intrinsics.checkNotNullParameter(commonGetSize, "$this$commonGetSize");
        return commonGetSize.getData$okio().length;
    }

    public static final byte[] commonToByteArray(ByteString commonToByteArray) {
        Intrinsics.checkNotNullParameter(commonToByteArray, "$this$commonToByteArray");
        byte[] data$okio = commonToByteArray.getData$okio();
        byte[] bArrCopyOf = Arrays.copyOf(data$okio, data$okio.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return bArrCopyOf;
    }

    public static final byte[] commonInternalArray(ByteString commonInternalArray) {
        Intrinsics.checkNotNullParameter(commonInternalArray, "$this$commonInternalArray");
        return commonInternalArray.getData$okio();
    }

    public static final boolean commonRangeEquals(ByteString commonRangeEquals, int i, ByteString other, int i2, int i3) {
        Intrinsics.checkNotNullParameter(commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkNotNullParameter(other, "other");
        return other.rangeEquals(i2, commonRangeEquals.getData$okio(), i, i3);
    }

    public static final boolean commonRangeEquals(ByteString commonRangeEquals, int i, byte[] other, int i2, int i3) {
        Intrinsics.checkNotNullParameter(commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkNotNullParameter(other, "other");
        return i >= 0 && i <= commonRangeEquals.getData$okio().length - i3 && i2 >= 0 && i2 <= other.length - i3 && Util.arrayRangeEquals(commonRangeEquals.getData$okio(), i, other, i2, i3);
    }

    public static final boolean commonStartsWith(ByteString commonStartsWith, ByteString prefix) {
        Intrinsics.checkNotNullParameter(commonStartsWith, "$this$commonStartsWith");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        return commonStartsWith.rangeEquals(0, prefix, 0, prefix.size());
    }

    public static final boolean commonStartsWith(ByteString commonStartsWith, byte[] prefix) {
        Intrinsics.checkNotNullParameter(commonStartsWith, "$this$commonStartsWith");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        return commonStartsWith.rangeEquals(0, prefix, 0, prefix.length);
    }

    public static final boolean commonEndsWith(ByteString commonEndsWith, ByteString suffix) {
        Intrinsics.checkNotNullParameter(commonEndsWith, "$this$commonEndsWith");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        return commonEndsWith.rangeEquals(commonEndsWith.size() - suffix.size(), suffix, 0, suffix.size());
    }

    public static final boolean commonEndsWith(ByteString commonEndsWith, byte[] suffix) {
        Intrinsics.checkNotNullParameter(commonEndsWith, "$this$commonEndsWith");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        return commonEndsWith.rangeEquals(commonEndsWith.size() - suffix.length, suffix, 0, suffix.length);
    }

    public static final int commonIndexOf(ByteString commonIndexOf, byte[] other, int i) {
        Intrinsics.checkNotNullParameter(commonIndexOf, "$this$commonIndexOf");
        Intrinsics.checkNotNullParameter(other, "other");
        int length = commonIndexOf.getData$okio().length - other.length;
        int iMax = Math.max(i, 0);
        if (iMax > length) {
            return -1;
        }
        while (!Util.arrayRangeEquals(commonIndexOf.getData$okio(), iMax, other, 0, other.length)) {
            if (iMax == length) {
                return -1;
            }
            iMax++;
        }
        return iMax;
    }

    public static final int commonLastIndexOf(ByteString commonLastIndexOf, ByteString other, int i) {
        Intrinsics.checkNotNullParameter(commonLastIndexOf, "$this$commonLastIndexOf");
        Intrinsics.checkNotNullParameter(other, "other");
        return commonLastIndexOf.lastIndexOf(other.internalArray$okio(), i);
    }

    public static final int commonLastIndexOf(ByteString commonLastIndexOf, byte[] other, int i) {
        Intrinsics.checkNotNullParameter(commonLastIndexOf, "$this$commonLastIndexOf");
        Intrinsics.checkNotNullParameter(other, "other");
        for (int iMin = Math.min(i, commonLastIndexOf.getData$okio().length - other.length); iMin >= 0; iMin--) {
            if (Util.arrayRangeEquals(commonLastIndexOf.getData$okio(), iMin, other, 0, other.length)) {
                return iMin;
            }
        }
        return -1;
    }

    public static final boolean commonEquals(ByteString commonEquals, Object obj) {
        Intrinsics.checkNotNullParameter(commonEquals, "$this$commonEquals");
        if (obj == commonEquals) {
            return true;
        }
        if (obj instanceof ByteString) {
            ByteString byteString = (ByteString) obj;
            if (byteString.size() == commonEquals.getData$okio().length && byteString.rangeEquals(0, commonEquals.getData$okio(), 0, commonEquals.getData$okio().length)) {
                return true;
            }
        }
        return false;
    }

    public static final int commonHashCode(ByteString commonHashCode) {
        Intrinsics.checkNotNullParameter(commonHashCode, "$this$commonHashCode");
        int hashCode$okio = commonHashCode.getHashCode$okio();
        if (hashCode$okio != 0) {
            return hashCode$okio;
        }
        int iHashCode = Arrays.hashCode(commonHashCode.getData$okio());
        commonHashCode.setHashCode$okio(iHashCode);
        return iHashCode;
    }

    public static final int commonCompareTo(ByteString commonCompareTo, ByteString other) {
        Intrinsics.checkNotNullParameter(commonCompareTo, "$this$commonCompareTo");
        Intrinsics.checkNotNullParameter(other, "other");
        int size = commonCompareTo.size();
        int size2 = other.size();
        int iMin = Math.min(size, size2);
        for (int i = 0; i < iMin; i++) {
            int i2 = commonCompareTo.getByte(i) & UByte.MAX_VALUE;
            int i3 = other.getByte(i) & UByte.MAX_VALUE;
            if (i2 != i3) {
                return i2 < i3 ? -1 : 1;
            }
        }
        if (size == size2) {
            return 0;
        }
        return size < size2 ? -1 : 1;
    }

    public static final ByteString commonOf(byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        byte[] bArrCopyOf = Arrays.copyOf(data, data.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return new ByteString(bArrCopyOf);
    }

    public static final ByteString commonToByteString(byte[] commonToByteString, int i, int i2) {
        Intrinsics.checkNotNullParameter(commonToByteString, "$this$commonToByteString");
        Util.checkOffsetAndCount(commonToByteString.length, i, i2);
        return new ByteString(ArraysKt.copyOfRange(commonToByteString, i, i2 + i));
    }

    public static final ByteString commonEncodeUtf8(String commonEncodeUtf8) {
        Intrinsics.checkNotNullParameter(commonEncodeUtf8, "$this$commonEncodeUtf8");
        ByteString byteString = new ByteString(Platform.asUtf8ToByteArray(commonEncodeUtf8));
        byteString.setUtf8$okio(commonEncodeUtf8);
        return byteString;
    }

    public static final ByteString commonDecodeBase64(String commonDecodeBase64) {
        Intrinsics.checkNotNullParameter(commonDecodeBase64, "$this$commonDecodeBase64");
        byte[] bArrDecodeBase64ToArray = Base64.decodeBase64ToArray(commonDecodeBase64);
        if (bArrDecodeBase64ToArray != null) {
            return new ByteString(bArrDecodeBase64ToArray);
        }
        return null;
    }

    public static final ByteString commonDecodeHex(String commonDecodeHex) {
        Intrinsics.checkNotNullParameter(commonDecodeHex, "$this$commonDecodeHex");
        if (!(commonDecodeHex.length() % 2 == 0)) {
            throw new IllegalArgumentException(("Unexpected hex string: " + commonDecodeHex).toString());
        }
        int length = commonDecodeHex.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) ((decodeHexDigit(commonDecodeHex.charAt(i2)) << 4) + decodeHexDigit(commonDecodeHex.charAt(i2 + 1)));
        }
        return new ByteString(bArr);
    }

    public static final void commonWrite(ByteString commonWrite, Buffer buffer, int i, int i2) {
        Intrinsics.checkNotNullParameter(commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        buffer.write(commonWrite.getData$okio(), i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int decodeHexDigit(char c) {
        if ('0' <= c && '9' >= c) {
            return c - '0';
        }
        if ('a' <= c && 'f' >= c) {
            return c - 'W';
        }
        if ('A' > c || 'F' < c) {
            throw new IllegalArgumentException("Unexpected hex digit: " + c);
        }
        return c - '7';
    }

    public static final String commonToString(ByteString byteString) {
        ByteString commonToString = byteString;
        Intrinsics.checkNotNullParameter(commonToString, "$this$commonToString");
        if (commonToString.getData$okio().length == 0) {
            return "[size=0]";
        }
        int iCodePointIndexToCharIndex = codePointIndexToCharIndex(commonToString.getData$okio(), 64);
        if (iCodePointIndexToCharIndex == -1) {
            if (commonToString.getData$okio().length <= 64) {
                return "[hex=" + commonToString.hex() + ']';
            }
            StringBuilder sbAppend = new StringBuilder("[size=").append(commonToString.getData$okio().length).append(" hex=");
            if (!(64 <= commonToString.getData$okio().length)) {
                throw new IllegalArgumentException(("endIndex > length(" + commonToString.getData$okio().length + ')').toString());
            }
            if (64 != commonToString.getData$okio().length) {
                commonToString = new ByteString(ArraysKt.copyOfRange(commonToString.getData$okio(), 0, 64));
            }
            return sbAppend.append(commonToString.hex()).append("…]").toString();
        }
        String strUtf8 = commonToString.utf8();
        if (strUtf8 != null) {
            String strSubstring = strUtf8.substring(0, iCodePointIndexToCharIndex);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            String strReplace$default = StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(strSubstring, "\\", "\\\\", false, 4, (Object) null), "\n", "\\n", false, 4, (Object) null), "\r", "\\r", false, 4, (Object) null);
            if (iCodePointIndexToCharIndex < strUtf8.length()) {
                return "[size=" + commonToString.getData$okio().length + " text=" + strReplace$default + "…]";
            }
            return "[text=" + strReplace$default + ']';
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x010d, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0035, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x01ae, code lost:
    
        return r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x005d, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x009d, code lost:
    
        return -1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final int codePointIndexToCharIndex(byte[] r18, int r19) {
        /*
            Method dump skipped, instruction units count: 431
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.ByteStringKt.codePointIndexToCharIndex(byte[], int):int");
    }
}
