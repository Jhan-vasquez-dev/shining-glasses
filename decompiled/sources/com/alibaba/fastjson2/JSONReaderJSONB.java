package com.alibaba.fastjson2;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.ViewCompat;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderImplInt32Array;
import com.alibaba.fastjson2.reader.ObjectReaderImplInt32ValueArray;
import com.alibaba.fastjson2.reader.ObjectReaderImplInt64Array;
import com.alibaba.fastjson2.reader.ObjectReaderImplInt64ValueArray;
import com.alibaba.fastjson2.reader.ObjectReaderImplStringArray;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import kotlin.UByte;
import kotlin.UShort;
import kotlin.time.DurationKt;
import me.leefeng.promptlibrary.BuildConfig;
import okhttp3.internal.ws.WebSocketProtocol;
import org.apache.http.message.TokenParser;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: classes.dex */
final class JSONReaderJSONB extends JSONReader {
    static final byte[] FIXED_TYPE_SIZE;
    static Charset GB18030;
    final byte[] bytes;
    final JSONFactory.CacheItem cacheItem;
    char[] charBuf;
    final int end;
    final int length;
    int strBegin;
    int strlen;
    byte strtype;
    int symbol0Begin;
    long symbol0Hash;
    int symbol0Length;
    byte symbol0StrType;
    final SymbolTable symbolTable;
    long[] symbols;
    byte type;
    byte[] valueBytes;
    static final long BASE = JDKUtils.UNSAFE.arrayBaseOffset(byte[].class);
    static final byte[] SHANGHAI_ZONE_ID_NAME_BYTES = JSONB.toBytes(DateUtils.SHANGHAI_ZONE_ID_NAME);

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfInfinity() {
        return false;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match2() {
        return false;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfSet() {
        return false;
    }

    static {
        byte[] bArr = new byte[256];
        for (int i = -16; i < 47; i++) {
            bArr[i & 255] = 1;
        }
        for (int i2 = 48; i2 < 63; i2++) {
            bArr[i2 & 255] = 2;
        }
        for (int i3 = 64; i3 < 71; i3++) {
            bArr[i3 & 255] = 3;
        }
        for (int i4 = -40; i4 < -17; i4++) {
            bArr[i4 & 255] = 1;
        }
        for (int i5 = -56; i5 < -41; i5++) {
            bArr[i5 & 255] = 2;
        }
        for (int i6 = -64; i6 < -57; i6++) {
            bArr[i6 & 255] = 3;
        }
        for (int i7 = 73; i7 < 120; i7++) {
            bArr[i7 & 255] = (byte) (i7 - 72);
        }
        bArr[148] = 1;
        bArr[73] = 1;
        bArr[175] = 1;
        bArr[176] = 1;
        bArr[177] = 1;
        bArr[189] = 2;
        bArr[188] = 3;
        bArr[72] = 5;
        bArr[172] = 5;
        bArr[183] = 5;
        bArr[191] = 5;
        bArr[190] = 9;
        bArr[171] = 9;
        bArr[181] = 9;
        bArr[121] = -1;
        bArr[122] = -1;
        bArr[123] = -1;
        bArr[124] = -1;
        bArr[125] = -1;
        FIXED_TYPE_SIZE = bArr;
    }

    JSONReaderJSONB(JSONReader.Context context, InputStream inputStream) {
        super(context, true, false);
        JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & (JSONFactory.CACHE_ITEMS.length - 1)];
        this.cacheItem = cacheItem;
        byte[] andSet = JSONFactory.BYTES_UPDATER.getAndSet(cacheItem, null);
        int i = context.bufferSize;
        andSet = andSet == null ? new byte[i] : andSet;
        int i2 = 0;
        while (true) {
            try {
                int i3 = inputStream.read(andSet, i2, andSet.length - i2);
                if (i3 != -1) {
                    i2 += i3;
                    if (i2 == andSet.length) {
                        andSet = Arrays.copyOf(andSet, andSet.length + i);
                    }
                } else {
                    this.bytes = andSet;
                    this.offset = 0;
                    this.length = i2;
                    this.end = i2;
                    this.symbolTable = context.symbolTable;
                    return;
                }
            } catch (IOException e) {
                throw new JSONException("read error", e);
            }
        }
    }

    JSONReaderJSONB(JSONReader.Context context, byte[] bArr, int i, int i2) {
        super(context, true, false);
        this.bytes = bArr;
        this.offset = i;
        this.length = i2;
        this.end = i + i2;
        this.symbolTable = context.symbolTable;
        this.cacheItem = JSONFactory.CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & (JSONFactory.CACHE_ITEMS.length - 1)];
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public String getString() {
        Charset charset;
        byte b = this.strtype;
        int i = this.strlen;
        if (b == -81) {
            return null;
        }
        if (i < 0) {
            return this.symbolTable.getName(-i);
        }
        if (b == 121) {
            charset = StandardCharsets.ISO_8859_1;
        } else if (b < 73 || b > 120) {
            if (b == 122) {
                charset = StandardCharsets.UTF_8;
            } else if (b == 123) {
                charset = StandardCharsets.UTF_16;
            } else if (b == 124) {
                charset = StandardCharsets.UTF_16LE;
            } else if (b == 125) {
                charset = StandardCharsets.UTF_16BE;
            } else {
                throw notSupportType(b);
            }
        } else {
            if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                return JDKUtils.latin1StringJDK8(this.bytes, this.strBegin, i);
            }
            if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                byte[] bArr = new byte[i];
                System.arraycopy(this.bytes, this.strBegin, bArr, 0, i);
                return JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.LATIN1);
            }
            charset = StandardCharsets.ISO_8859_1;
        }
        return new String(this.bytes, this.strBegin, i, charset);
    }

    public int readLength() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        int i2 = this.end;
        int i3 = i + 1;
        int intBE = bArr[i];
        if (!JSONB.isInt32Num(intBE)) {
            if (JSONB.isInt32Byte(intBE)) {
                intBE = getIntByte(bArr, i3, intBE);
                i3 = i + 2;
            } else if (JSONB.isInt32Short(intBE) && i + 2 < i2) {
                intBE = getInt3(bArr, i3, intBE);
                i3 = i + 3;
            } else if (intBE == 72 && i + 4 < i2) {
                intBE = IOUtils.getIntBE(bArr, i3);
                i3 = i + 5;
                if (intBE > 268435456) {
                    throw new JSONException("input length overflow");
                }
            } else {
                throw notSupportType((byte) intBE);
            }
        }
        this.offset = i3;
        return intBE;
    }

    private static int getIntByte(byte[] bArr, int i, int i2) {
        return ((i2 - 56) << 8) + (bArr[i] & 255);
    }

    private static int getInt3(byte[] bArr, int i, int i2) {
        return ((i2 - 68) << 16) + (IOUtils.getShortBE(bArr, i) & UShort.MAX_VALUE);
    }

    private static int getLongByte(byte[] bArr, int i, int i2) {
        return ((i2 + 48) << 8) + (bArr[i] & 255);
    }

    private static int getLong3(byte[] bArr, int i, int i2) {
        return ((i2 + 60) << 16) + (IOUtils.getShortBE(bArr, i) & UShort.MAX_VALUE);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean isArray() {
        byte b;
        return this.offset < this.end && (b = this.bytes[this.offset]) >= -108 && b <= -92;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean isObject() {
        return this.offset < this.end && this.bytes[this.offset] == -90;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean isNumber() {
        byte b = this.bytes[this.offset];
        return b >= -78 && b <= 72;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean isString() {
        if (this.offset >= this.end) {
            return false;
        }
        byte b = this.bytes[this.offset];
        this.type = b;
        return b >= 73;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfMatch(char c) {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfArrayStart() {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfComma() {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfArrayEnd() {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfObjectStart() {
        int i = this.offset;
        if (this.bytes[i] != -90) {
            return false;
        }
        this.offset = i + 1;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfObjectEnd() {
        int i = this.offset;
        if (this.bytes[i] != -91) {
            return false;
        }
        this.offset = i + 1;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfNullOrEmptyString() {
        int i = this.offset;
        byte b = this.bytes[i];
        if (b != -81 && b != 73) {
            return false;
        }
        this.offset = i + 1;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public <T> T read(Type type) {
        return (T) this.context.provider.getObjectReader(type, (this.context.features & JSONReader.Feature.FieldBased.mask) != 0).readJSONBObject(this, null, null, 0L);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public <T> T read(Class<T> cls) {
        return (T) this.context.provider.getObjectReader(cls, (this.context.features & JSONReader.Feature.FieldBased.mask) != 0).readJSONBObject(this, null, null, 0L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0216  */
    /* JADX WARN: Type inference failed for: r12v3 */
    /* JADX WARN: Type inference failed for: r20v0, types: [com.alibaba.fastjson2.JSONReader, com.alibaba.fastjson2.JSONReaderJSONB] */
    /* JADX WARN: Type inference failed for: r5v19 */
    /* JADX WARN: Type inference failed for: r5v7 */
    /* JADX WARN: Type inference failed for: r8v3, types: [java.lang.Object, java.util.Map, java.util.Map<java.lang.String, java.lang.Object>] */
    /* JADX WARN: Type inference failed for: r9v49 */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.Map<java.lang.String, java.lang.Object> readObject() {
        /*
            Method dump skipped, instruction units count: 574
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderJSONB.readObject():java.util.Map");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public void read(Map map, long j) {
        Object any;
        Object any2;
        if (this.bytes[this.offset] != -90) {
            throw new JSONException("object not support input " + error(this.type));
        }
        this.offset++;
        long features = j | this.context.getFeatures();
        while (true) {
            byte b = this.bytes[this.offset];
            if (b == -91) {
                this.offset++;
                return;
            }
            if (b >= 73) {
                any = readFieldName();
            } else {
                any = readAny();
            }
            if (isReference()) {
                String reference = readReference();
                if ("..".equals(reference)) {
                    map.put(any, map);
                } else {
                    addResolveTask(map, any, JSONPath.of(reference));
                    map.put(any, null);
                }
            } else {
                byte b2 = this.bytes[this.offset];
                if (b2 >= 73 && b2 <= 126) {
                    any2 = readString();
                } else if (JSONB.isInt32Num(b2)) {
                    this.offset++;
                    any2 = Integer.valueOf(b2);
                } else if (b2 == -79) {
                    this.offset++;
                    any2 = Boolean.TRUE;
                } else if (b2 == -80) {
                    this.offset++;
                    any2 = Boolean.FALSE;
                } else if (b2 == -90) {
                    any2 = readObject();
                } else {
                    any2 = readAny();
                }
                if (any2 != null || (JSONReader.Feature.IgnoreNullPropertyValue.mask & features) == 0) {
                    map.put(any, any2);
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r13v16 */
    /* JADX WARN: Type inference failed for: r26v0, types: [com.alibaba.fastjson2.JSONReader, com.alibaba.fastjson2.JSONReaderJSONB] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v2, types: [int] */
    /* JADX WARN: Type inference failed for: r9v5 */
    /* JADX WARN: Type inference failed for: r9v7 */
    /* JADX WARN: Type inference failed for: r9v8 */
    @Override // com.alibaba.fastjson2.JSONReader
    public Object readAny() {
        long j;
        Object any;
        ?? r9;
        Object any2;
        int i;
        String str;
        String str2;
        List jSONArray;
        int i2 = this.end;
        byte[] bArr = this.bytes;
        if (this.offset >= bArr.length) {
            throw new JSONException("readAny overflow : " + this.offset + "/" + bArr.length);
        }
        int i3 = this.offset;
        this.offset = i3 + 1;
        byte b = bArr[i3];
        this.type = b;
        if (b == 72) {
            int intBE = IOUtils.getIntBE(bArr, check3(this.offset, i2));
            this.offset += 4;
            return Integer.valueOf(intBE);
        }
        switch (b) {
            case -112:
                return Character.valueOf((char) readInt32Value());
            case -111:
                int length = readLength();
                byte[] bArrCopyOfRange = Arrays.copyOfRange(this.bytes, this.offset, this.offset + length);
                this.offset += length;
                return bArrCopyOfRange;
            case -110:
                long typeHashCode = readTypeHashCode();
                if (this.context.autoTypeBeforeHandler != null) {
                    Class<?> clsApply = this.context.autoTypeBeforeHandler.apply(typeHashCode, (Class<?>) null, this.context.features);
                    j = typeHashCode;
                    if (clsApply == null) {
                        clsApply = this.context.autoTypeBeforeHandler.apply(getString(), (Class<?>) null, this.context.features);
                    }
                    if (clsApply != null) {
                        return this.context.getObjectReader(clsApply).readJSONBObject(this, null, null, 0L);
                    }
                } else {
                    j = typeHashCode;
                }
                if ((this.context.features & JSONReader.Feature.SupportAutoType.mask) == 0) {
                    if (isObject()) {
                        return readObject();
                    }
                    if (isArray()) {
                        return readArray();
                    }
                    throw new JSONException("autoType not support , offset " + this.offset + "/" + bArr.length);
                }
                ObjectReader objectReaderAutoType = this.context.getObjectReaderAutoType(j);
                if (objectReaderAutoType == null) {
                    String string = getString();
                    ObjectReader objectReaderAutoType2 = this.context.getObjectReaderAutoType(string, null);
                    if (objectReaderAutoType2 == null) {
                        throw new JSONException("autoType not support : " + string + ", offset " + this.offset + "/" + bArr.length);
                    }
                    objectReaderAutoType = objectReaderAutoType2;
                }
                return objectReaderAutoType.readJSONBObject(this, null, null, 0L);
            default:
                byte b2 = 73;
                int i4 = 0;
                ?? r13 = 1;
                switch (b) {
                    case -90:
                        boolean z = (JSONReader.Feature.SupportAutoType.mask & this.context.features) != 0;
                        Map jSONObject = null;
                        while (true) {
                            byte b3 = bArr[this.offset];
                            if (b3 == -91) {
                                this.offset += r13;
                                if (jSONObject != null) {
                                    return jSONObject;
                                }
                                if ((this.context.features & JSONReader.Feature.UseNativeObject.mask) != 0) {
                                    return new HashMap();
                                }
                                return new JSONObject();
                            }
                            if (z && i4 == 0 && b3 >= b2) {
                                if (readFieldNameHashCode() == ObjectReader.HASH_TYPE) {
                                    ObjectReader objectReaderAutoType3 = this.context.getObjectReaderAutoType(readValueHashCode());
                                    if (objectReaderAutoType3 == null) {
                                        String string2 = getString();
                                        ObjectReader objectReaderAutoType4 = this.context.getObjectReaderAutoType(string2, null);
                                        if (objectReaderAutoType4 == null) {
                                            throw new JSONException("autoType not support : " + string2 + ", offset " + this.offset + "/" + bArr.length);
                                        }
                                        objectReaderAutoType3 = objectReaderAutoType4;
                                    }
                                    this.typeRedirect = r13;
                                    return objectReaderAutoType3.readJSONBObject(this, null, null, 0L);
                                }
                                any = getFieldName();
                            } else if (b3 >= b2) {
                                any = readFieldName();
                            } else {
                                any = readAny();
                            }
                            if (jSONObject == null) {
                                ?? r92 = r13;
                                if ((this.context.features & JSONReader.Feature.UseNativeObject.mask) != 0) {
                                    jSONObject = new HashMap();
                                    r9 = r92;
                                } else {
                                    jSONObject = new JSONObject();
                                    r9 = r92;
                                }
                            } else {
                                r9 = r13;
                            }
                            if (isReference()) {
                                String reference = readReference();
                                if ("..".equals(reference)) {
                                    jSONObject.put(any, jSONObject);
                                } else {
                                    addResolveTask(jSONObject, any, JSONPath.of(reference));
                                    jSONObject.put(any, null);
                                }
                                i = i4;
                            } else {
                                byte b4 = bArr[this.offset];
                                if (b4 >= b2 && b4 <= 126) {
                                    any2 = readString();
                                } else if (JSONB.isInt32Num(b4)) {
                                    this.offset += r9;
                                    any2 = Integer.valueOf(b4);
                                } else if (b4 == -79) {
                                    this.offset += r9;
                                    any2 = Boolean.TRUE;
                                } else if (b4 == -80) {
                                    this.offset += r9;
                                    any2 = Boolean.FALSE;
                                } else if (b4 == -90) {
                                    any2 = readObject();
                                } else {
                                    any2 = readAny();
                                }
                                if (any2 == null) {
                                    i = i4;
                                    if ((JSONReader.Feature.IgnoreNullPropertyValue.mask & this.context.features) != 0) {
                                    }
                                } else {
                                    i = i4;
                                }
                                jSONObject.put(any, any2);
                            }
                            i4 = i + 1;
                            b2 = 73;
                            r13 = 1;
                        }
                        break;
                    case -89:
                        int i5 = this.offset;
                        this.offset = i5 + 1;
                        byte b5 = bArr[i5];
                        int i6 = this.offset;
                        this.offset = i6 + 1;
                        byte b6 = bArr[i6];
                        int i7 = this.offset;
                        this.offset = i7 + 1;
                        return LocalTime.of(b5, b6, bArr[i7], readInt32Value());
                    case -88:
                        int i8 = this.offset;
                        this.offset = i8 + 1;
                        int i9 = bArr[i8] << 8;
                        int i10 = this.offset;
                        this.offset = i10 + 1;
                        int i11 = i9 + (bArr[i10] & UByte.MAX_VALUE);
                        int i12 = this.offset;
                        this.offset = i12 + 1;
                        byte b7 = bArr[i12];
                        int i13 = this.offset;
                        this.offset = i13 + 1;
                        byte b8 = bArr[i13];
                        int i14 = this.offset;
                        this.offset = i14 + 1;
                        byte b9 = bArr[i14];
                        int i15 = this.offset;
                        this.offset = i15 + 1;
                        byte b10 = bArr[i15];
                        int i16 = this.offset;
                        this.offset = i16 + 1;
                        return LocalDateTime.of(i11, b7, b8, b9, b10, bArr[i16], readInt32Value());
                    case -87:
                        int i17 = this.offset;
                        this.offset = i17 + 1;
                        int i18 = bArr[i17] << 8;
                        int i19 = this.offset;
                        this.offset = i19 + 1;
                        int i20 = i18 + (bArr[i19] & UByte.MAX_VALUE);
                        int i21 = this.offset;
                        this.offset = i21 + 1;
                        byte b11 = bArr[i21];
                        int i22 = this.offset;
                        this.offset = i22 + 1;
                        return LocalDate.of(i20, b11, bArr[i22]);
                    case -86:
                        return readTimestampWithTimeZone();
                    case -85:
                        long longBE = IOUtils.getLongBE(bArr, check7(this.offset, i2));
                        this.offset += 8;
                        return new Date(longBE);
                    case -84:
                        long intBE2 = IOUtils.getIntBE(bArr, check3(this.offset, i2));
                        this.offset += 4;
                        return new Date(intBE2 * 1000);
                    case -83:
                        long intBE3 = IOUtils.getIntBE(bArr, check3(this.offset, i2));
                        this.offset += 4;
                        return new Date(intBE3 * 60000);
                    case -82:
                        return Instant.ofEpochSecond(readInt64Value(), readInt32Value());
                    case -81:
                        return null;
                    case -80:
                        return false;
                    case -79:
                        return true;
                    case -78:
                        return Double.valueOf(0.0d);
                    case -77:
                        return Double.valueOf(1.0d);
                    case -76:
                        return Double.valueOf(readInt64Value());
                    case -75:
                        long longBE2 = IOUtils.getLongBE(bArr, check7(this.offset, i2));
                        this.offset += 8;
                        return Double.valueOf(Double.longBitsToDouble(longBE2));
                    case -74:
                        return Float.valueOf(readInt32Value());
                    case -73:
                        int intBE4 = IOUtils.getIntBE(bArr, check3(this.offset, i2));
                        this.offset += 4;
                        return Float.valueOf(Float.intBitsToFloat(intBE4));
                    case -72:
                        return BigDecimal.valueOf(readInt64Value());
                    case -71:
                        int int32Value = readInt32Value();
                        BigInteger bigInteger = readBigInteger();
                        if (int32Value == 0) {
                            return new BigDecimal(bigInteger);
                        }
                        return new BigDecimal(bigInteger, int32Value);
                    case -70:
                        return BigInteger.valueOf(readInt64Value());
                    case -69:
                        int int32Value2 = readInt32Value();
                        byte[] bArr2 = new byte[int32Value2];
                        System.arraycopy(bArr, this.offset, bArr2, 0, int32Value2);
                        this.offset += int32Value2;
                        return new BigInteger(bArr2);
                    case -68:
                        int i23 = this.offset;
                        this.offset = i23 + 1;
                        int i24 = bArr[i23] << 8;
                        int i25 = this.offset;
                        this.offset = i25 + 1;
                        return Short.valueOf((short) (i24 + (bArr[i25] & UByte.MAX_VALUE)));
                    case -67:
                        int i26 = this.offset;
                        this.offset = i26 + 1;
                        return Byte.valueOf(bArr[i26]);
                    case -66:
                        long longBE3 = IOUtils.getLongBE(bArr, check7(this.offset, i2));
                        this.offset += 8;
                        return Long.valueOf(longBE3);
                    case -65:
                        int intBE5 = IOUtils.getIntBE(bArr, check3(this.offset, i2));
                        this.offset += 4;
                        return Long.valueOf(intBE5);
                    default:
                        switch (b) {
                            case 122:
                                int length2 = readLength();
                                if (JDKUtils.STRING_CREATOR_JDK11 != null && !JDKUtils.BIG_ENDIAN) {
                                    if (this.valueBytes == null) {
                                        byte[] andSet = JSONFactory.BYTES_UPDATER.getAndSet(this.cacheItem, null);
                                        this.valueBytes = andSet;
                                        if (andSet == null) {
                                            this.valueBytes = new byte[8192];
                                        }
                                    }
                                    int i27 = length2 << 1;
                                    if (i27 > this.valueBytes.length) {
                                        this.valueBytes = new byte[i27];
                                    }
                                    int iDecodeUTF8 = IOUtils.decodeUTF8(bArr, this.offset, length2, this.valueBytes);
                                    if (iDecodeUTF8 != -1) {
                                        byte[] bArr3 = new byte[iDecodeUTF8];
                                        System.arraycopy(this.valueBytes, 0, bArr3, 0, iDecodeUTF8);
                                        String strApply = JDKUtils.STRING_CREATOR_JDK11.apply(bArr3, JDKUtils.UTF16);
                                        this.offset += length2;
                                        return strApply;
                                    }
                                }
                                String str3 = new String(bArr, this.offset, length2, StandardCharsets.UTF_8);
                                this.offset += length2;
                                return str3;
                            case 123:
                                int length3 = readLength();
                                String str4 = new String(bArr, this.offset, length3, StandardCharsets.UTF_16);
                                this.offset += length3;
                                return str4;
                            case 124:
                                int length4 = readLength();
                                if (JDKUtils.STRING_CREATOR_JDK11 != null && !JDKUtils.BIG_ENDIAN) {
                                    byte[] bArr4 = new byte[length4];
                                    System.arraycopy(bArr, this.offset, bArr4, 0, length4);
                                    str = JDKUtils.STRING_CREATOR_JDK11.apply(bArr4, length4 == 0 ? JDKUtils.LATIN1 : JDKUtils.UTF16);
                                } else {
                                    str = new String(bArr, this.offset, length4, StandardCharsets.UTF_16LE);
                                }
                                this.offset += length4;
                                return str;
                            case 125:
                                int length5 = readLength();
                                if (JDKUtils.STRING_CREATOR_JDK11 != null && JDKUtils.BIG_ENDIAN) {
                                    byte[] bArr5 = new byte[length5];
                                    System.arraycopy(bArr, this.offset, bArr5, 0, length5);
                                    str2 = JDKUtils.STRING_CREATOR_JDK11.apply(bArr5, length5 == 0 ? JDKUtils.LATIN1 : JDKUtils.UTF16);
                                } else {
                                    str2 = new String(bArr, this.offset, length5, StandardCharsets.UTF_16BE);
                                }
                                this.offset += length5;
                                return str2;
                            case 126:
                                if (GB18030 == null) {
                                    GB18030 = Charset.forName("GB18030");
                                }
                                int length6 = readLength();
                                String str5 = new String(bArr, this.offset, length6, GB18030);
                                this.offset += length6;
                                return str5;
                            default:
                                if (JSONB.isInt32Num(b)) {
                                    return Integer.valueOf(b);
                                }
                                if (JSONB.isInt32Byte(b)) {
                                    int i28 = this.offset;
                                    this.offset = i28 + 1;
                                    return Integer.valueOf(getIntByte(bArr, i28, b));
                                }
                                if (JSONB.isInt32Short(b) && this.offset + 1 < i2) {
                                    int int3 = getInt3(bArr, this.offset, b);
                                    this.offset += 2;
                                    return Integer.valueOf(int3);
                                }
                                if (JSONB.isInt64Num(b)) {
                                    return Long.valueOf(((long) (b + 40)) - 8);
                                }
                                if (JSONB.isInt64Byte(b)) {
                                    this.offset = this.offset + 1;
                                    return Long.valueOf(getLongByte(bArr, r0, b));
                                }
                                if (JSONB.isInt64Short(b) && this.offset + 1 < i2) {
                                    long long3 = getLong3(bArr, this.offset, b);
                                    this.offset += 2;
                                    return Long.valueOf(long3);
                                }
                                if (b >= -108 && b <= -92) {
                                    int length7 = b == -92 ? readLength() : b + 108;
                                    if (length7 == 0) {
                                        if ((this.context.features & JSONReader.Feature.UseNativeObject.mask) != 0) {
                                            return new ArrayList();
                                        }
                                        if (this.context.arraySupplier != null) {
                                            return this.context.arraySupplier.get();
                                        }
                                        return new JSONArray();
                                    }
                                    if ((this.context.features & JSONReader.Feature.UseNativeObject.mask) != 0) {
                                        jSONArray = new ArrayList(length7);
                                    } else {
                                        jSONArray = new JSONArray(length7);
                                    }
                                    while (i4 < length7) {
                                        if (isReference()) {
                                            String reference2 = readReference();
                                            if ("..".equals(reference2)) {
                                                jSONArray.add(jSONArray);
                                            } else {
                                                jSONArray.add(null);
                                                addResolveTask(jSONArray, i4, JSONPath.of(reference2));
                                            }
                                        } else {
                                            jSONArray.add(readAny());
                                        }
                                        i4++;
                                    }
                                    return jSONArray;
                                }
                                if (b < 73 || b > 121) {
                                    if (b == 127) {
                                        int length8 = readLength();
                                        this.strlen = length8;
                                        if (length8 >= 0) {
                                            throw new JSONException("not support symbol : " + this.strlen);
                                        }
                                        return this.symbolTable.getName(-length8);
                                    }
                                    throw new JSONException("not support type : " + error(b));
                                }
                                int length9 = b == 121 ? readLength() : b + JSONB.Constants.BC_FLOAT;
                                this.strlen = length9;
                                if (length9 < 0) {
                                    return this.symbolTable.getName(-length9);
                                }
                                if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                                    String strLatin1StringJDK8 = JDKUtils.latin1StringJDK8(bArr, this.offset, this.strlen);
                                    this.offset += this.strlen;
                                    if ((this.context.features & JSONReader.Feature.TrimString.mask) != 0) {
                                        strLatin1StringJDK8 = strLatin1StringJDK8.trim();
                                    }
                                    if (!strLatin1StringJDK8.isEmpty() || (this.context.features & JSONReader.Feature.EmptyStringAsNull.mask) == 0) {
                                        return strLatin1StringJDK8;
                                    }
                                    return null;
                                }
                                if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                                    byte[] bArr6 = new byte[this.strlen];
                                    System.arraycopy(bArr, this.offset, bArr6, 0, this.strlen);
                                    this.offset += this.strlen;
                                    String strApply2 = JDKUtils.STRING_CREATOR_JDK11.apply(bArr6, JDKUtils.LATIN1);
                                    if ((this.context.features & JSONReader.Feature.TrimString.mask) != 0) {
                                        strApply2 = strApply2.trim();
                                    }
                                    if (!strApply2.isEmpty() || (this.context.features & JSONReader.Feature.EmptyStringAsNull.mask) == 0) {
                                        return strApply2;
                                    }
                                    return null;
                                }
                                String str6 = new String(bArr, this.offset, this.strlen, StandardCharsets.ISO_8859_1);
                                this.offset += this.strlen;
                                if ((this.context.features & JSONReader.Feature.TrimString.mask) != 0) {
                                    str6 = str6.trim();
                                }
                                if (!str6.isEmpty() || (this.context.features & JSONReader.Feature.EmptyStringAsNull.mask) == 0) {
                                    return str6;
                                }
                                return null;
                        }
                }
                break;
        }
    }

    private ZonedDateTime readTimestampWithTimeZone() {
        ZoneId zoneId;
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        int i2 = bArr[i] << 8;
        int i3 = this.offset;
        this.offset = i3 + 1;
        int i4 = i2 + (bArr[i3] & UByte.MAX_VALUE);
        int i5 = this.offset;
        this.offset = i5 + 1;
        byte b = bArr[i5];
        int i6 = this.offset;
        this.offset = i6 + 1;
        byte b2 = bArr[i6];
        int i7 = this.offset;
        this.offset = i7 + 1;
        byte b3 = bArr[i7];
        int i8 = this.offset;
        this.offset = i8 + 1;
        byte b4 = bArr[i8];
        int i9 = this.offset;
        this.offset = i9 + 1;
        byte b5 = bArr[i9];
        int int32Value = readInt32Value();
        byte[] bArr2 = SHANGHAI_ZONE_ID_NAME_BYTES;
        if (this.offset + bArr2.length < bArr.length) {
            for (int i10 = 0; i10 < bArr2.length; i10++) {
                if (bArr[this.offset + i10] != bArr2[i10]) {
                    zoneId = DateUtils.getZoneId(readString(), DateUtils.SHANGHAI_ZONE_ID);
                    break;
                }
            }
            this.offset += bArr2.length;
            zoneId = DateUtils.SHANGHAI_ZONE_ID;
        } else {
            zoneId = DateUtils.getZoneId(readString(), DateUtils.SHANGHAI_ZONE_ID);
            break;
        }
        return ZonedDateTime.of(LocalDateTime.of(i4, b, b2, b3, b4, b5, int32Value), zoneId);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public byte getType() {
        return this.bytes[this.offset];
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public List readArray() {
        Object any;
        List jSONArray;
        Object any2;
        int iStartArray = startArray();
        JSONArray jSONArray2 = new JSONArray(iStartArray);
        for (int i = 0; i < iStartArray; i++) {
            int i2 = this.bytes[this.offset];
            if (i2 >= 73 && i2 <= 126) {
                any = readString();
            } else if (JSONB.isInt32Num(i2)) {
                this.offset++;
                any = Integer.valueOf(i2);
            } else if (i2 == -79) {
                this.offset++;
                any = Boolean.TRUE;
            } else if (i2 == -80) {
                this.offset++;
                any = Boolean.FALSE;
            } else if (i2 == -90) {
                any = readObject();
            } else if (i2 == -66) {
                this.offset++;
                any = Long.valueOf(IOUtils.getLongBE(this.bytes, check7(this.offset, this.end)));
                this.offset += 8;
            } else if (i2 >= -108 && i2 <= -92) {
                this.offset++;
                int length = i2 == -92 ? readLength() : i2 + 108;
                if (length != 0) {
                    if ((this.context.features & JSONReader.Feature.UseNativeObject.mask) != 0) {
                        jSONArray = new ArrayList(length);
                    } else {
                        jSONArray = new JSONArray(length);
                    }
                    for (int i3 = 0; i3 < length; i3++) {
                        if (isReference()) {
                            String reference = readReference();
                            if ("..".equals(reference)) {
                                jSONArray.add(jSONArray);
                            } else {
                                jSONArray.add(null);
                                addResolveTask(jSONArray, i3, JSONPath.of(reference));
                            }
                        } else {
                            byte b = this.bytes[this.offset];
                            if (b >= 73 && b <= 126) {
                                any2 = readString();
                            } else if (b == -90) {
                                any2 = readObject();
                            } else {
                                any2 = readAny();
                            }
                            jSONArray.add(any2);
                        }
                    }
                    any = jSONArray;
                } else if ((this.context.features & JSONReader.Feature.UseNativeObject.mask) != 0) {
                    any = new ArrayList();
                } else if (this.context.arraySupplier != null) {
                    any = this.context.arraySupplier.get();
                } else {
                    any = new JSONArray();
                }
            } else if (JSONB.isInt32Byte(i2)) {
                any = Integer.valueOf(getIntByte(this.bytes, this.offset + 1, i2));
                this.offset += 2;
            } else if (JSONB.isInt64Short(i2) && this.offset + 2 < this.end) {
                int long3 = getLong3(this.bytes, this.offset + 1, i2);
                this.offset += 3;
                any = Integer.valueOf(long3);
            } else if (i2 == 72) {
                any = Integer.valueOf(IOUtils.getIntBE(this.bytes, check3(this.offset + 1, this.end)));
                this.offset += 5;
            } else if (i2 == -109) {
                String reference2 = readReference();
                if ("..".equals(reference2)) {
                    any = jSONArray2;
                } else {
                    addResolveTask(jSONArray2, i, JSONPath.of(reference2));
                }
            } else {
                any = readAny();
            }
            jSONArray2.add(any);
        }
        return jSONArray2;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public List readArray(Type type) {
        if (nextIfNull()) {
            return null;
        }
        if (this.bytes[this.offset] == -110) {
            Object any = readAny();
            if (any instanceof List) {
                return (List) any;
            }
            if (any instanceof Collection) {
                return new JSONArray((Collection<?>) any);
            }
            throw new JSONException("not support class " + any.getClass());
        }
        int iStartArray = startArray();
        JSONArray jSONArray = new JSONArray(iStartArray);
        for (int i = 0; i < iStartArray; i++) {
            jSONArray.add(read(type));
        }
        return jSONArray;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public List readList(Type[] typeArr) {
        if (nextIfNull()) {
            return null;
        }
        int iStartArray = startArray();
        JSONArray jSONArray = new JSONArray(iStartArray);
        for (int i = 0; i < iStartArray; i++) {
            jSONArray.add(read(typeArr[i]));
        }
        return jSONArray;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public byte[] readHex() {
        String string = readString();
        int length = string.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            char cCharAt = string.charAt(i2);
            char cCharAt2 = string.charAt(i2 + 1);
            char c = '0';
            int i3 = cCharAt - (cCharAt <= '9' ? '0' : '7');
            if (cCharAt2 > '9') {
                c = '7';
            }
            bArr[i] = (byte) ((cCharAt2 - c) | (i3 << 4));
        }
        return bArr;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean isReference() {
        int i = this.offset;
        byte[] bArr = this.bytes;
        return i < bArr.length && bArr[this.offset] == -109;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public String readReference() {
        if (this.bytes[this.offset] != -109) {
            return null;
        }
        this.offset++;
        if (isString()) {
            return readString();
        }
        throw new JSONException("reference not support input " + error(this.type));
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean readReference(Collection collection, int i) {
        if (this.bytes[this.offset] != -109) {
            return false;
        }
        this.offset++;
        String string = readString();
        if ("..".equals(string)) {
            collection.add(collection);
        } else {
            addResolveTask(collection, i, JSONPath.of(string));
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x003d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    java.lang.Object readAnyObject() {
        /*
            r12 = this;
            byte[] r0 = r12.bytes
            int r1 = r12.offset
            r0 = r0[r1]
            r1 = -110(0xffffffffffffff92, float:NaN)
            if (r0 == r1) goto Lf
            java.lang.Object r0 = r12.readAny()
            return r0
        Lf:
            com.alibaba.fastjson2.JSONReader$Context r0 = r12.context
            int r1 = r12.offset
            int r1 = r1 + 1
            r12.offset = r1
            long r3 = r12.readTypeHashCode()
            com.alibaba.fastjson2.JSONReader$AutoTypeBeforeHandler r2 = r0.autoTypeBeforeHandler
            r1 = 0
            r8 = 0
            if (r2 == 0) goto L3d
            java.lang.Class<java.lang.Object> r5 = java.lang.Object.class
            r6 = 0
            java.lang.Class r5 = r2.apply(r3, r5, r6)
            if (r5 != 0) goto L36
            java.lang.String r5 = r12.getString()
            java.lang.Class<java.lang.Object> r6 = java.lang.Object.class
            java.lang.Class r5 = r2.apply(r5, r6, r8)
        L36:
            if (r5 == 0) goto L3d
            com.alibaba.fastjson2.reader.ObjectReader r2 = r0.getObjectReader(r5)
            goto L3e
        L3d:
            r2 = r1
        L3e:
            long r5 = r0.features
            if (r2 != 0) goto L5e
            com.alibaba.fastjson2.JSONReader$Feature r2 = com.alibaba.fastjson2.JSONReader.Feature.SupportAutoType
            long r10 = r2.mask
            long r10 = r10 & r5
            int r2 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r2 != 0) goto L58
            com.alibaba.fastjson2.JSONReader$Feature r2 = com.alibaba.fastjson2.JSONReader.Feature.ErrorOnNotSupportAutoType
            long r10 = r2.mask
            long r10 = r10 & r5
            int r2 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r2 != 0) goto L55
            return r1
        L55:
            r12.autoTypeError()
        L58:
            com.alibaba.fastjson2.reader.ObjectReaderProvider r2 = r0.provider
            com.alibaba.fastjson2.reader.ObjectReader r2 = r2.getObjectReader(r3)
        L5e:
            if (r2 == 0) goto L7a
            java.lang.Class r3 = r2.getObjectClass()
            if (r3 == 0) goto L7a
            java.lang.ClassLoader r4 = r3.getClassLoader()
            if (r4 == 0) goto L7a
            java.lang.Thread r7 = java.lang.Thread.currentThread()
            java.lang.ClassLoader r7 = r7.getContextClassLoader()
            if (r4 == r7) goto L7a
            com.alibaba.fastjson2.reader.ObjectReader r2 = r12.getObjectReaderContext(r2, r3, r7)
        L7a:
            if (r2 != 0) goto L97
            com.alibaba.fastjson2.reader.ObjectReaderProvider r2 = r0.provider
            java.lang.String r3 = r12.getString()
            java.lang.Class<java.lang.Object> r4 = java.lang.Object.class
            com.alibaba.fastjson2.reader.ObjectReader r2 = r2.getObjectReader(r3, r4, r5)
            if (r2 != 0) goto L97
            com.alibaba.fastjson2.JSONReader$Feature r3 = com.alibaba.fastjson2.JSONReader.Feature.ErrorOnNotSupportAutoType
            long r3 = r3.mask
            long r3 = r3 & r5
            int r3 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1))
            if (r3 != 0) goto L94
            return r1
        L94:
            r12.autoTypeError()
        L97:
            r4 = r2
            byte[] r1 = r12.bytes
            int r2 = r12.offset
            r1 = r1[r2]
            r12.type = r1
            java.lang.Class<java.lang.Object> r6 = java.lang.Object.class
            r7 = 0
            long r8 = r0.features
            r5 = r12
            java.lang.Object r0 = r4.readJSONBObject(r5, r6, r7, r8)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderJSONB.readAnyObject():java.lang.Object");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public ObjectReader checkAutoType(Class cls, long j, long j2) {
        Class objectClass;
        ClassLoader classLoader;
        ClassLoader contextClassLoader;
        ObjectReader objectReaderCheckAutoTypeWithHandler;
        ObjectReader objectReader;
        Class objectClass2;
        if (this.bytes[this.offset] != -110) {
            return null;
        }
        this.offset++;
        JSONReader.Context context = this.context;
        long typeHashCode = readTypeHashCode();
        if (j == typeHashCode && (objectClass2 = (objectReader = context.getObjectReader(cls)).getObjectClass()) != null && objectClass2 == cls) {
            context.provider.registerIfAbsent(typeHashCode, objectReader);
            return objectReader;
        }
        JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler = context.autoTypeBeforeHandler;
        if (autoTypeBeforeHandler != null && (objectReaderCheckAutoTypeWithHandler = checkAutoTypeWithHandler(cls, j2, autoTypeBeforeHandler, typeHashCode)) != null) {
            return objectReaderCheckAutoTypeWithHandler;
        }
        long j3 = context.features | j2;
        if ((JSONReader.Feature.SupportAutoType.mask & j3) == 0) {
            if ((JSONReader.Feature.ErrorOnNotSupportAutoType.mask & j3) == 0) {
                return null;
            }
            autoTypeError();
        }
        ObjectReader objectReader2 = context.provider.getObjectReader(typeHashCode);
        if (objectReader2 != null && (objectClass = objectReader2.getObjectClass()) != null && (classLoader = objectClass.getClassLoader()) != null && classLoader != (contextClassLoader = Thread.currentThread().getContextClassLoader())) {
            objectReader2 = getObjectReaderContext(objectReader2, objectClass, contextClassLoader);
        }
        if (objectReader2 == null && (objectReader2 = context.provider.getObjectReader(getString(), cls, j3)) == null) {
            if ((j3 & JSONReader.Feature.ErrorOnNotSupportAutoType.mask) == 0) {
                return null;
            }
            autoTypeError();
        }
        this.type = this.bytes[this.offset];
        return objectReader2;
    }

    ObjectReader checkAutoTypeWithHandler(Class cls, long j, JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler, long j2) {
        Class<?> clsApply = autoTypeBeforeHandler.apply(j2, (Class<?>) cls, j);
        if (clsApply == null) {
            clsApply = autoTypeBeforeHandler.apply(getString(), (Class<?>) cls, j);
        }
        if (clsApply != null) {
            return this.context.getObjectReader(clsApply);
        }
        return null;
    }

    void autoTypeError() {
        throw new JSONException("autoType not support : " + getString());
    }

    private ObjectReader getObjectReaderContext(ObjectReader objectReader, Class cls, ClassLoader classLoader) {
        String string = getString();
        Class<?> mapping = TypeUtils.getMapping(string);
        if (mapping == null) {
            if (classLoader == null) {
                try {
                    classLoader = JSON.class.getClassLoader();
                } catch (ClassNotFoundException unused) {
                }
            }
            mapping = classLoader.loadClass(string);
        }
        return (mapping == null || cls.equals(mapping)) ? objectReader : getObjectReader(mapping);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public int startArray() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        this.type = b;
        if (b == -81) {
            return -1;
        }
        if (b >= -108 && b <= -93) {
            this.ch = (char) (-b);
            return b - (-108);
        }
        if (b == -111) {
            return readInt32Value();
        }
        if (b != -92) {
            throw new JSONException("array not support input " + error(b));
        }
        return readInt32Value();
    }

    public String error(byte b) {
        String string;
        StringBuilder sb = new StringBuilder();
        sb.append(JSONB.typeName(b));
        if (isString()) {
            int i = this.offset;
            this.offset--;
            try {
                string = readString();
            } catch (Throwable unused) {
                string = null;
            }
            if (string != null) {
                sb.append(TokenParser.SP);
                sb.append(string);
            }
            this.offset = i;
        }
        sb.append(", offset ");
        sb.append(this.offset);
        sb.append('/');
        sb.append(this.bytes.length);
        return sb.toString();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public void next() {
        this.offset++;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public long readFieldNameHashCode() {
        int length;
        char c;
        long j;
        long hashCode;
        int i;
        long j2;
        long j3;
        int i2;
        int int32Value;
        byte[] bArr = this.bytes;
        int i3 = this.offset;
        this.offset = i3 + 1;
        byte b = bArr[i3];
        this.strtype = b;
        boolean z = b == 127;
        byte b2 = b;
        if (z) {
            byte b3 = bArr[this.offset];
            this.strtype = b3;
            if (JSONB.isInt32(b3)) {
                if (b3 <= 47) {
                    this.offset++;
                    int32Value = b3;
                } else {
                    int32Value = readInt32Value();
                }
                if (int32Value < 0) {
                    return this.symbolTable.getHashCode(-int32Value);
                }
                if (int32Value == 0) {
                    this.strtype = this.symbol0StrType;
                    this.strlen = this.symbol0Length;
                    this.strBegin = this.symbol0Begin;
                    if (this.symbol0Hash == 0) {
                        this.symbol0Hash = getNameHashCode();
                    }
                    return this.symbol0Hash;
                }
                int i4 = int32Value * 2;
                long[] jArr = this.symbols;
                long j4 = jArr[i4 + 1];
                int i5 = (int) j4;
                this.strtype = (byte) i5;
                this.strlen = i5 >> 8;
                this.strBegin = (int) (j4 >> 32);
                long j5 = jArr[i4];
                if (j5 != 0) {
                    return j5;
                }
                long nameHashCode = getNameHashCode();
                this.symbols[i4] = nameHashCode;
                return nameHashCode;
            }
            this.offset++;
            b2 = b3;
        }
        if (b2 >= 73 && b2 <= 120) {
            length = b2 + JSONB.Constants.BC_FLOAT;
        } else if (b2 == 121 || b2 == 122) {
            length = readLength();
        } else {
            throw readFieldNameHashCodeError();
        }
        this.strlen = length;
        this.strBegin = this.offset;
        if (length < 0) {
            hashCode = this.symbolTable.getHashCode(-length);
            c = '\b';
        } else {
            if (length <= 8 && this.offset + length <= bArr.length) {
                long j6 = ((long) this.offset) + BASE;
                switch (length) {
                    case 1:
                        c = '\b';
                        i = bArr[this.offset];
                        j = i;
                        break;
                    case 2:
                        c = '\b';
                        j = ((long) JDKUtils.UNSAFE.getShort(bArr, j6)) & WebSocketProtocol.PAYLOAD_SHORT_MAX;
                        break;
                    case 3:
                        c = '\b';
                        j2 = bArr[this.offset + 2] << 16;
                        j3 = ((long) JDKUtils.UNSAFE.getShort(bArr, j6)) & WebSocketProtocol.PAYLOAD_SHORT_MAX;
                        j = j2 + j3;
                        break;
                    case 4:
                        c = '\b';
                        i = JDKUtils.UNSAFE.getInt(bArr, j6);
                        j = i;
                        break;
                    case 5:
                        c = '\b';
                        j2 = ((long) bArr[this.offset + 4]) << 32;
                        i2 = JDKUtils.UNSAFE.getInt(bArr, j6);
                        j3 = ((long) i2) & 4294967295L;
                        j = j2 + j3;
                        break;
                    case 6:
                        c = '\b';
                        j2 = ((long) JDKUtils.UNSAFE.getShort(bArr, 4 + j6)) << 32;
                        i2 = JDKUtils.UNSAFE.getInt(bArr, j6);
                        j3 = ((long) i2) & 4294967295L;
                        j = j2 + j3;
                        break;
                    case 7:
                        c = '\b';
                        j = (((long) bArr[this.offset + 6]) << 48) + ((((long) bArr[this.offset + 5]) & 255) << 40) + ((((long) bArr[this.offset + 4]) & 255) << 32) + (((long) JDKUtils.UNSAFE.getInt(bArr, j6)) & 4294967295L);
                        break;
                    default:
                        c = '\b';
                        j = JDKUtils.UNSAFE.getLong(bArr, j6);
                        break;
                }
            } else {
                c = '\b';
                j = 0;
            }
            if (j != 0) {
                this.offset += length;
                hashCode = j;
            } else {
                hashCode = -3750763034362895579L;
                for (int i6 = 0; i6 < length; i6++) {
                    int i7 = this.offset;
                    this.offset = i7 + 1;
                    hashCode = Fnv.MAGIC_PRIME * (((long) bArr[i7]) ^ hashCode);
                }
            }
        }
        if (z) {
            int int32Value2 = bArr[this.offset];
            if (JSONB.isInt32Num(int32Value2)) {
                this.offset++;
            } else {
                int32Value2 = readInt32Value();
            }
            if (int32Value2 == 0) {
                this.symbol0Begin = this.strBegin;
                this.symbol0Length = length;
                this.symbol0StrType = b2;
                this.symbol0Hash = hashCode;
                return hashCode;
            }
            int i8 = int32Value2 << 1;
            int i9 = i8 + 2;
            long[] jArr2 = this.symbols;
            if (jArr2 == null) {
                this.symbols = new long[Math.max(i9, 32)];
            } else if (jArr2.length < i9) {
                this.symbols = Arrays.copyOf(jArr2, i8 + 18);
            }
            long[] jArr3 = this.symbols;
            jArr3[i8] = hashCode;
            jArr3[i8 + 1] = (((long) this.strBegin) << 32) + (((long) length) << c) + ((long) b2);
        }
        return hashCode;
    }

    JSONException readFieldNameHashCodeError() {
        StringBuilder sbAppend = new StringBuilder("fieldName not support input type ").append(JSONB.typeName(this.strtype));
        if (this.strtype == -109) {
            sbAppend.append(" ").append(readString());
        }
        sbAppend.append(", offset ").append(this.offset);
        return new JSONException(sbAppend.toString());
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean isInt() {
        byte b = this.bytes[this.offset];
        return (b >= -70 && b <= 72) || b == -84 || b == -83 || b == -85;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean isNull() {
        return this.bytes[this.offset] == -81;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public Date readNullOrNewDate() {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfNull() {
        if (this.bytes[this.offset] != -81) {
            return false;
        }
        this.offset++;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public void readNull() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        this.type = b;
        if (b != -81) {
            throw new JSONException("null not match, " + ((int) this.type));
        }
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean readIfNull() {
        if (this.bytes[this.offset] != -81) {
            return false;
        }
        this.offset++;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public long readTypeHashCode() {
        int i;
        int i2;
        byte[] bArr = this.bytes;
        int i3 = this.offset;
        byte b = bArr[i3];
        this.strtype = b;
        if (b == 121 && (i = bArr[i3 + 1]) > 8 && i <= 63) {
            if (i <= 47) {
                i2 = i3 + 2;
            } else {
                i = ((i - 56) << 8) + (bArr[i3 + 2] & 255);
                i2 = i3 + 3;
            }
            long j = Fnv.MAGIC_HASH_CODE;
            int i4 = 0;
            int i5 = i2;
            while (i4 < i) {
                j = (j ^ ((long) bArr[i5])) * Fnv.MAGIC_PRIME;
                i4++;
                i5++;
            }
            int i6 = bArr[i5];
            if (i6 >= 0 && i6 <= 47) {
                int i7 = i5 + 1;
                if (i6 == 0) {
                    this.symbol0Begin = i2;
                    this.symbol0Length = i;
                    this.symbol0StrType = b;
                    this.symbol0Hash = j;
                } else {
                    int i8 = i6 * 2;
                    int i9 = i8 + 2;
                    long[] jArr = this.symbols;
                    if (jArr == null) {
                        this.symbols = new long[Math.max(i9, 32)];
                    } else if (jArr.length < i9) {
                        this.symbols = Arrays.copyOf(jArr, i8 + 18);
                    }
                    this.symbols[i8 + 1] = (((long) i2) << 32) + (((long) i) << 8) + ((long) b);
                }
                this.strBegin = i2;
                this.strlen = i;
                this.offset = i7;
                return j;
            }
        }
        return readTypeHashCode0();
    }

    /* JADX WARN: Removed duplicated region for block: B:126:0x0294  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public long readTypeHashCode0() {
        /*
            Method dump skipped, instruction units count: 828
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderJSONB.readTypeHashCode0():long");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0241 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:158:0x02c6 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0341 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x016c A[RETURN] */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public long readValueHashCode() {
        /*
            Method dump skipped, instruction units count: 952
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderJSONB.readValueHashCode():long");
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x006d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x006e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    long getNameHashCode() {
        /*
            r11 = this;
            int r0 = r11.strBegin
            r1 = 0
            r3 = 0
            r5 = r1
            r4 = r3
        L7:
            int r7 = r11.strlen
            if (r4 >= r7) goto L69
            byte[] r7 = r11.bytes
            r8 = r7[r0]
            if (r8 < 0) goto L66
            r9 = 8
            if (r4 >= r9) goto L66
            if (r4 != 0) goto L1e
            int r9 = r11.strBegin
            r7 = r7[r9]
            if (r7 != 0) goto L1e
            goto L66
        L1e:
            switch(r4) {
                case 0: goto L60;
                case 1: goto L58;
                case 2: goto L51;
                case 3: goto L4a;
                case 4: goto L40;
                case 5: goto L36;
                case 6: goto L2c;
                case 7: goto L22;
                default: goto L21;
            }
        L21:
            goto L61
        L22:
            long r7 = (long) r8
            r9 = 56
            long r7 = r7 << r9
            r9 = 72057594037927935(0xffffffffffffff, double:7.291122019556397E-304)
            goto L5d
        L2c:
            long r7 = (long) r8
            r9 = 48
            long r7 = r7 << r9
            r9 = 281474976710655(0xffffffffffff, double:1.390671161566996E-309)
            goto L5d
        L36:
            long r7 = (long) r8
            r9 = 40
            long r7 = r7 << r9
            r9 = 1099511627775(0xffffffffff, double:5.432309224866E-312)
            goto L5d
        L40:
            long r7 = (long) r8
            r9 = 32
            long r7 = r7 << r9
            r9 = 4294967295(0xffffffff, double:2.1219957905E-314)
            goto L5d
        L4a:
            int r7 = r8 << 24
            long r7 = (long) r7
            r9 = 16777215(0xffffff, double:8.2890456E-317)
            goto L5d
        L51:
            int r7 = r8 << 16
            long r7 = (long) r7
            r9 = 65535(0xffff, double:3.23786E-319)
            goto L5d
        L58:
            int r7 = r8 << 8
            long r7 = (long) r7
            r9 = 255(0xff, double:1.26E-321)
        L5d:
            long r5 = r5 & r9
            long r5 = r5 + r7
            goto L61
        L60:
            long r5 = (long) r8
        L61:
            int r4 = r4 + 1
            int r0 = r0 + 1
            goto L7
        L66:
            int r0 = r11.strBegin
            r5 = r1
        L69:
            int r1 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r1 == 0) goto L6e
            return r5
        L6e:
            r1 = -3750763034362895579(0xcbf29ce484222325, double:-7.302176725335867E57)
        L73:
            int r4 = r11.strlen
            if (r3 >= r4) goto L8b
            byte[] r4 = r11.bytes
            int r5 = r0 + 1
            r0 = r4[r0]
            long r6 = (long) r0
            long r0 = r1 ^ r6
            r6 = 1099511628211(0x100000001b3, double:5.43230922702E-312)
            long r1 = r0 * r6
            int r3 = r3 + 1
            r0 = r5
            goto L73
        L8b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderJSONB.getNameHashCode():long");
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x008a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x008b  */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public long getNameHashCodeLCase() {
        /*
            Method dump skipped, instruction units count: 204
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderJSONB.getNameHashCodeLCase():long");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:66:0x00d0  */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void skipValue() {
        /*
            Method dump skipped, instruction units count: 464
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderJSONB.skipValue():void");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean skipName() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        this.strtype = b;
        if (b >= 73 && b <= 120) {
            this.offset += b - 73;
            return true;
        }
        if (b == 121 || b == 122 || b == 123 || b == 124 || b == 125) {
            this.strlen = readLength();
            this.offset += this.strlen;
            return true;
        }
        if (b == 127) {
            byte b2 = this.bytes[this.offset];
            if (b2 >= -16 && b2 <= 72) {
                readInt32Value();
                return true;
            }
            readString();
            readInt32Value();
            return true;
        }
        throw notSupportType(b);
    }

    static JSONException notSupportType(byte b) {
        return new JSONException("name not support input : " + JSONB.typeName(b));
    }

    JSONException notSupportString() {
        throw new JSONException("readString not support type " + JSONB.typeName(this.strtype) + ", offset " + this.offset + "/" + this.bytes.length);
    }

    JSONException readInt32ValueError(byte b) {
        throw new JSONException("readInt32Value not support " + JSONB.typeName(b) + ", offset " + this.offset + "/" + this.bytes.length);
    }

    JSONException readInt64ValueError(byte b) {
        throw new JSONException("readInt64Value not support " + JSONB.typeName(b) + ", offset " + this.offset + "/" + this.bytes.length);
    }

    JSONException readStringError() {
        throw new JSONException("string value not support input " + JSONB.typeName(this.type) + " offset " + this.offset + "/" + this.bytes.length);
    }

    static JSONException typeRefNotFound(int i) {
        throw new JSONException("type ref not found : " + i);
    }

    /* JADX WARN: Removed duplicated region for block: B:150:0x04f1  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x04fa  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x050c  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x03ad  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x03b0  */
    @Override // com.alibaba.fastjson2.JSONReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String readFieldName() {
        /*
            Method dump skipped, instruction units count: 1394
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderJSONB.readFieldName():java.lang.String");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public String getFieldName() {
        return getString();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public String readString() {
        int length;
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        this.strtype = b;
        String strLatin1StringJDK8 = null;
        if (b == -81) {
            return null;
        }
        this.strBegin = this.offset;
        if (b >= 73 && b <= 121) {
            if (b == 121) {
                length = bArr[this.offset];
                if (JSONB.isInt32Num(length)) {
                    this.offset++;
                } else {
                    length = readLength();
                }
                this.strBegin = this.offset;
            } else {
                length = b - 73;
            }
            this.strlen = length;
            if (length >= 0) {
                if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                    byte[] bArr2 = new byte[length];
                    System.arraycopy(bArr, this.offset, bArr2, 0, length);
                    strLatin1StringJDK8 = JDKUtils.STRING_CREATOR_JDK11.apply(bArr2, JDKUtils.LATIN1);
                } else if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                    strLatin1StringJDK8 = JDKUtils.latin1StringJDK8(bArr, this.offset, length);
                }
            }
            if (strLatin1StringJDK8 != null) {
                this.offset += length;
                return stringValue(strLatin1StringJDK8, this.context.features);
            }
        }
        return readStringNonAscii();
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0079  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String readStringNonAscii() {
        /*
            r9 = this;
            byte r0 = r9.strtype
            r1 = 73
            r2 = 0
            if (r0 < r1) goto L10
            r1 = 121(0x79, float:1.7E-43)
            if (r0 > r1) goto L10
            java.nio.charset.Charset r0 = java.nio.charset.StandardCharsets.ISO_8859_1
        Ld:
            r1 = r0
            r0 = r2
            goto L4f
        L10:
            r1 = 122(0x7a, float:1.71E-43)
            if (r0 != r1) goto L1b
            java.lang.String r0 = r9.readStringUTF8()
            java.nio.charset.Charset r1 = java.nio.charset.StandardCharsets.UTF_8
            goto L4f
        L1b:
            r1 = 123(0x7b, float:1.72E-43)
            if (r0 != r1) goto L2c
            int r0 = r9.readLength()
            r9.strlen = r0
            int r0 = r9.offset
            r9.strBegin = r0
            java.nio.charset.Charset r0 = java.nio.charset.StandardCharsets.UTF_16
            goto Ld
        L2c:
            r1 = 124(0x7c, float:1.74E-43)
            if (r0 != r1) goto L37
            java.lang.String r0 = r9.readUTF16LE()
            java.nio.charset.Charset r1 = java.nio.charset.StandardCharsets.UTF_16LE
            goto L4f
        L37:
            r1 = 125(0x7d, float:1.75E-43)
            if (r0 != r1) goto L45
            java.lang.String r0 = r9.readUTF16BE()
            if (r0 == 0) goto L42
            return r0
        L42:
            java.nio.charset.Charset r1 = java.nio.charset.StandardCharsets.UTF_16BE
            goto L4f
        L45:
            r1 = 126(0x7e, float:1.77E-43)
            if (r0 != r1) goto L7e
            r9.readGB18030()
            java.nio.charset.Charset r0 = com.alibaba.fastjson2.JSONReaderJSONB.GB18030
            goto Ld
        L4f:
            if (r0 == 0) goto L79
            com.alibaba.fastjson2.JSONReader$Context r1 = r9.context
            long r3 = r1.features
            com.alibaba.fastjson2.JSONReader$Feature r1 = com.alibaba.fastjson2.JSONReader.Feature.TrimString
            long r5 = r1.mask
            long r3 = r3 & r5
            r5 = 0
            int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r1 == 0) goto L64
            java.lang.String r0 = r0.trim()
        L64:
            boolean r1 = r0.isEmpty()
            if (r1 == 0) goto L78
            com.alibaba.fastjson2.JSONReader$Context r1 = r9.context
            long r3 = r1.features
            com.alibaba.fastjson2.JSONReader$Feature r1 = com.alibaba.fastjson2.JSONReader.Feature.EmptyStringAsNull
            long r7 = r1.mask
            long r3 = r3 & r7
            int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r1 == 0) goto L78
            return r2
        L78:
            return r0
        L79:
            java.lang.String r0 = r9.readString(r1)
            return r0
        L7e:
            java.lang.String r0 = r9.readStringTypeNotMatch()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderJSONB.readStringNonAscii():java.lang.String");
    }

    private String readString(Charset charset) {
        char[] andSet;
        String str;
        int i = this.strlen;
        if (i < 0) {
            return this.symbolTable.getName(-i);
        }
        if (JDKUtils.JVM_VERSION == 8 && this.strtype == 122 && this.strlen < 8192) {
            andSet = JSONFactory.CHARS_UPDATER.getAndSet(JSONFactory.CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & (JSONFactory.CACHE_ITEMS.length - 1)], null);
            if (andSet == null) {
                andSet = new char[8192];
            }
        } else {
            andSet = null;
        }
        if (andSet != null) {
            str = new String(andSet, 0, IOUtils.decodeUTF8(this.bytes, this.offset, this.strlen, andSet));
            if (andSet.length < 8388608) {
                JSONFactory.CHARS_UPDATER.lazySet(this.cacheItem, andSet);
            }
        } else {
            str = new String(this.bytes, this.offset, this.strlen, charset);
        }
        this.offset += this.strlen;
        if ((this.context.features & JSONReader.Feature.TrimString.mask) != 0) {
            str = str.trim();
        }
        if (!str.isEmpty() || (this.context.features & JSONReader.Feature.EmptyStringAsNull.mask) == 0) {
            return str;
        }
        return null;
    }

    private void readGB18030() {
        this.strlen = readLength();
        this.strBegin = this.offset;
        if (GB18030 == null) {
            GB18030 = Charset.forName("GB18030");
        }
    }

    private String readUTF16BE() {
        this.strlen = readLength();
        this.strBegin = this.offset;
        if (JDKUtils.STRING_CREATOR_JDK11 == null || !JDKUtils.BIG_ENDIAN) {
            return null;
        }
        byte[] bArr = new byte[this.strlen];
        System.arraycopy(this.bytes, this.offset, bArr, 0, this.strlen);
        String strApply = JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.UTF16);
        this.offset += this.strlen;
        if ((this.context.features & JSONReader.Feature.TrimString.mask) != 0) {
            strApply = strApply.trim();
        }
        if (!strApply.isEmpty() || (this.context.features & JSONReader.Feature.EmptyStringAsNull.mask) == 0) {
            return strApply;
        }
        return null;
    }

    private String readUTF16LE() {
        byte b = this.bytes[this.offset];
        if (JSONB.isInt32Num(b)) {
            this.offset++;
            this.strlen = b;
        } else if (JSONB.isInt32Byte(b)) {
            this.strlen = getIntByte(this.bytes, this.offset + 1, b);
            this.offset += 2;
        } else {
            this.strlen = readLength();
        }
        this.strBegin = this.offset;
        if (this.strlen == 0) {
            return "";
        }
        if (JDKUtils.STRING_CREATOR_JDK11 == null || JDKUtils.BIG_ENDIAN) {
            return null;
        }
        byte[] bArr = new byte[this.strlen];
        System.arraycopy(this.bytes, this.offset, bArr, 0, this.strlen);
        String strApply = JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.UTF16);
        this.offset += this.strlen;
        if ((this.context.features & JSONReader.Feature.TrimString.mask) != 0) {
            strApply = strApply.trim();
        }
        if (!strApply.isEmpty() || (this.context.features & JSONReader.Feature.EmptyStringAsNull.mask) == 0) {
            return strApply;
        }
        return null;
    }

    private String readStringUTF8() {
        byte b = this.bytes[this.offset];
        if (JSONB.isInt32Num(b)) {
            this.offset++;
            this.strlen = b;
        } else if (JSONB.isInt32Byte(b)) {
            this.strlen = getIntByte(this.bytes, this.offset + 1, b);
            this.offset += 2;
        } else {
            this.strlen = readLength();
        }
        this.strBegin = this.offset;
        if (JDKUtils.STRING_CREATOR_JDK11 != null && !JDKUtils.BIG_ENDIAN) {
            if (this.valueBytes == null) {
                byte[] andSet = JSONFactory.BYTES_UPDATER.getAndSet(this.cacheItem, null);
                this.valueBytes = andSet;
                if (andSet == null) {
                    this.valueBytes = new byte[8192];
                }
            }
            int i = this.strlen << 1;
            if (i > this.valueBytes.length) {
                this.valueBytes = new byte[i];
            }
            int iDecodeUTF8 = IOUtils.decodeUTF8(this.bytes, this.offset, this.strlen, this.valueBytes);
            if (iDecodeUTF8 != -1) {
                byte[] bArr = new byte[iDecodeUTF8];
                System.arraycopy(this.valueBytes, 0, bArr, 0, iDecodeUTF8);
                String strApply = JDKUtils.STRING_CREATOR_JDK11.apply(bArr, JDKUtils.UTF16);
                this.offset += this.strlen;
                if ((this.context.features & JSONReader.Feature.TrimString.mask) != 0) {
                    strApply = strApply.trim();
                }
                if (!strApply.isEmpty() || (this.context.features & JSONReader.Feature.EmptyStringAsNull.mask) == 0) {
                    return strApply;
                }
                return null;
            }
        }
        return null;
    }

    private String readStringTypeNotMatch() {
        BigDecimal bigDecimal;
        int i = this.end;
        byte b = this.strtype;
        if (JSONB.isInt32Num(b)) {
            return Byte.toString(b);
        }
        if (JSONB.isInt32Byte(b)) {
            byte[] bArr = this.bytes;
            int i2 = this.offset;
            this.offset = i2 + 1;
            return Integer.toString(getIntByte(bArr, i2, b));
        }
        if (JSONB.isInt32Short(b) && this.offset + 1 < i) {
            int int3 = getInt3(this.bytes, this.offset, b);
            this.offset += 2;
            return Integer.toString(int3);
        }
        if (JSONB.isInt64Num(b)) {
            return Integer.toString(b + 32);
        }
        if (JSONB.isInt64Byte(b)) {
            byte[] bArr2 = this.bytes;
            int i3 = this.offset;
            this.offset = i3 + 1;
            return Integer.toString(getLongByte(bArr2, i3, b));
        }
        if (JSONB.isInt64Short(b) && this.offset + 1 < i) {
            int long3 = getLong3(this.bytes, this.offset, b);
            this.offset += 2;
            return Integer.toString(long3);
        }
        if (b == -110) {
            this.offset--;
            Object any = readAny();
            if (any == null) {
                return null;
            }
            return JSON.toJSONString(any, JSONWriter.Feature.WriteThrowableClassName);
        }
        if (b == -81) {
            return null;
        }
        if (b != 72) {
            if (b == -66) {
                long longBE = IOUtils.getLongBE(this.bytes, check7(this.offset, i));
                this.offset += 8;
                return Long.toString(longBE);
            }
            if (b != -65) {
                switch (b) {
                    case -85:
                        long longBE2 = IOUtils.getLongBE(this.bytes, check7(this.offset, i));
                        this.offset += 8;
                        return DateUtils.toString(new Date(longBE2));
                    case -84:
                        long intBE = ((long) IOUtils.getIntBE(this.bytes, check3(this.offset, i))) * 1000;
                        this.offset += 4;
                        return DateUtils.toString(new Date(intBE));
                    case -83:
                        long intBE2 = ((long) IOUtils.getIntBE(this.bytes, check3(this.offset, i))) * 60000;
                        this.offset += 4;
                        return DateUtils.toString(new Date(intBE2));
                    default:
                        switch (b) {
                            case -78:
                                return "0.0";
                            case -77:
                                return BuildConfig.VERSION_NAME;
                            case -76:
                                return Double.toString(readInt64Value());
                            case -75:
                                long longBE3 = IOUtils.getLongBE(this.bytes, check7(this.offset, i));
                                this.offset += 8;
                                return Double.toString(Double.longBitsToDouble(longBE3));
                            case -74:
                                return Float.toString(readInt32Value());
                            case -73:
                                int intBE3 = IOUtils.getIntBE(this.bytes, check3(this.offset, i));
                                this.offset += 4;
                                return Float.toString(Float.intBitsToFloat(intBE3));
                            case -72:
                            case -70:
                                return Long.toString(readInt64Value());
                            case -71:
                                int int32Value = readInt32Value();
                                BigInteger bigInteger = readBigInteger();
                                if (int32Value == 0) {
                                    bigDecimal = new BigDecimal(bigInteger);
                                } else {
                                    bigDecimal = new BigDecimal(bigInteger, int32Value);
                                }
                                return bigDecimal.toString();
                            case -69:
                                int int32Value2 = readInt32Value();
                                byte[] bArr3 = new byte[int32Value2];
                                System.arraycopy(this.bytes, this.offset, bArr3, 0, int32Value2);
                                this.offset += int32Value2;
                                return new BigInteger(bArr3).toString();
                            default:
                                throw notSupportString();
                        }
                }
            }
        }
        int intBE4 = IOUtils.getIntBE(this.bytes, check3(this.offset, i));
        this.offset += 4;
        return Long.toString(intBE4);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public String[] readStringArray() {
        if (nextIfMatch(JSONB.Constants.BC_TYPED_ANY) && readTypeHashCode() != ObjectReaderImplStringArray.HASH_TYPE) {
            throw new JSONException(info("not support type " + getString()));
        }
        int iStartArray = startArray();
        if (iStartArray == -1) {
            return null;
        }
        String[] strArr = new String[iStartArray];
        for (int i = 0; i < iStartArray; i++) {
            strArr[i] = readString();
        }
        return strArr;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public char readCharValue() {
        int int32Value;
        byte b = this.bytes[this.offset];
        if (b == -112) {
            this.offset++;
            int32Value = readInt32Value();
        } else {
            if (b == 73) {
                this.offset++;
                return (char) 0;
            }
            if (b > 73 && b < 120) {
                this.offset++;
                byte[] bArr = this.bytes;
                int i = this.offset;
                this.offset = i + 1;
                int32Value = bArr[i] & UByte.MAX_VALUE;
            } else {
                String string = readString();
                if (string == null || string.isEmpty()) {
                    return (char) 0;
                }
                return string.charAt(0);
            }
        }
        return (char) int32Value;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public int[] readInt32ValueArray() {
        if (nextIfMatch(JSONB.Constants.BC_TYPED_ANY)) {
            long typeHashCode = readTypeHashCode();
            if (typeHashCode != ObjectReaderImplInt64ValueArray.HASH_TYPE && typeHashCode != ObjectReaderImplInt64Array.HASH_TYPE && typeHashCode != ObjectReaderImplInt32Array.HASH_TYPE && typeHashCode != ObjectReaderImplInt32ValueArray.HASH_TYPE) {
                throw new JSONException(info("not support " + getString()));
            }
        }
        int iStartArray = startArray();
        if (iStartArray == -1) {
            return null;
        }
        int[] iArr = new int[iStartArray];
        for (int i = 0; i < iStartArray; i++) {
            iArr[i] = readInt32Value();
        }
        return iArr;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public long[] readInt64ValueArray() {
        if (nextIfMatch(JSONB.Constants.BC_TYPED_ANY)) {
            long typeHashCode = readTypeHashCode();
            if (typeHashCode != ObjectReaderImplInt64ValueArray.HASH_TYPE && typeHashCode != ObjectReaderImplInt64Array.HASH_TYPE && typeHashCode != ObjectReaderImplInt32Array.HASH_TYPE && typeHashCode != ObjectReaderImplInt32ValueArray.HASH_TYPE) {
                throw new JSONException(info("not support " + getString()));
            }
        }
        int iStartArray = startArray();
        if (iStartArray == -1) {
            return null;
        }
        long[] jArr = new long[iStartArray];
        for (int i = 0; i < iStartArray; i++) {
            jArr[i] = readInt64Value();
        }
        return jArr;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public long readInt64Value() {
        long longBE;
        int i;
        long longByte;
        long j;
        this.wasNull = false;
        byte[] bArr = this.bytes;
        int i2 = this.offset;
        int i3 = this.end;
        int i4 = i2 + 1;
        byte b = bArr[i2];
        if (JSONB.isInt64Num(b)) {
            j = b + 32;
        } else {
            if (JSONB.isInt64Byte(b)) {
                longByte = getLongByte(bArr, i4, b);
                i4 = i2 + 2;
            } else {
                if (JSONB.isInt64Short(b) && i2 + 2 < i3) {
                    longBE = getLong3(bArr, i4, b);
                    i = i2 + 3;
                } else if (b == -65 && i2 + 4 < i3) {
                    longBE = IOUtils.getIntBE(bArr, i4);
                    i = i2 + 5;
                } else if (b == -66 && i2 + 8 < i3) {
                    longBE = IOUtils.getLongBE(bArr, i4);
                    i = i2 + 9;
                } else {
                    this.offset = i4;
                    return readInt64Value0(bArr, b);
                }
                longByte = longBE;
                i4 = i;
            }
            j = longByte;
        }
        this.offset = i4;
        return j;
    }

    private long readInt64Value0(byte[] bArr, byte b) {
        BigDecimal bigDecimal;
        int i = this.offset;
        if (JSONB.isInt32Num(b)) {
            return b;
        }
        if (JSONB.isInt32Byte(b)) {
            long intByte = getIntByte(bArr, i, b);
            this.offset = i + 1;
            return intByte;
        }
        if (JSONB.isInt32Short(b) && i + 1 < this.end) {
            this.offset = i + 2;
            return getInt3(bArr, i, b);
        }
        if (b == -71) {
            int int32Value = readInt32Value();
            BigInteger bigInteger = readBigInteger();
            if (int32Value == 0) {
                bigDecimal = new BigDecimal(bigInteger);
            } else {
                bigDecimal = new BigDecimal(bigInteger, int32Value);
            }
            return bigDecimal.longValue();
        }
        if (b == 72) {
            int intBE = IOUtils.getIntBE(bArr, check3(this.offset, this.end));
            this.offset += 4;
            return intBE;
        }
        if (b == 124) {
            int int32Value2 = readInt32Value();
            String str = new String(bArr, this.offset, int32Value2, StandardCharsets.UTF_16LE);
            this.offset += int32Value2;
            if (str.indexOf(46) == -1) {
                return new BigInteger(str).intValue();
            }
            return TypeUtils.toBigDecimal(str).intValue();
        }
        if (b == -68) {
            int i2 = (bArr[this.offset + 1] & UByte.MAX_VALUE) + (bArr[this.offset] << 8);
            this.offset += 2;
            return i2;
        }
        if (b == -67) {
            this.offset = this.offset + 1;
            return bArr[r8];
        }
        if (b == 121) {
            int int32Value3 = readInt32Value();
            String str2 = new String(bArr, this.offset, int32Value3, StandardCharsets.ISO_8859_1);
            this.offset += int32Value3;
            if (str2.indexOf(46) == -1) {
                return new BigInteger(str2).intValue();
            }
            return TypeUtils.toBigDecimal(str2).intValue();
        }
        if (b != 122) {
            switch (b) {
                case -85:
                    long longBE = IOUtils.getLongBE(bArr, check7(this.offset, this.end));
                    this.offset += 8;
                    return longBE;
                case -84:
                    long intBE2 = IOUtils.getIntBE(bArr, check3(this.offset, this.end));
                    this.offset += 4;
                    return intBE2 * 1000;
                case -83:
                    long intBE3 = IOUtils.getIntBE(bArr, check3(this.offset, this.end));
                    this.offset += 4;
                    return intBE3 * 60000;
                default:
                    switch (b) {
                        case -81:
                            if ((this.context.features & JSONReader.Feature.ErrorOnNullForPrimitives.mask) != 0) {
                                throw new JSONException(info("long value not support input null"));
                            }
                            this.wasNull = true;
                            return 0L;
                        case -80:
                        case -78:
                            return 0L;
                        case -79:
                        case -77:
                            return 1L;
                        case -76:
                            return readInt64Value();
                        case -75:
                            this.offset--;
                            return (long) readDoubleValue();
                        case -74:
                            return readInt32Value();
                        case -73:
                            int intBE4 = IOUtils.getIntBE(bArr, check3(this.offset, this.end));
                            this.offset += 4;
                            return (long) Float.intBitsToFloat(intBE4);
                        default:
                            if (b >= 73 && b <= 120) {
                                int i3 = b - 73;
                                String fixedAsciiString = readFixedAsciiString(i3);
                                this.offset += i3;
                                if (fixedAsciiString.indexOf(46) == -1) {
                                    return new BigInteger(fixedAsciiString).longValue();
                                }
                                return TypeUtils.toBigDecimal(fixedAsciiString).longValue();
                            }
                            throw readInt64ValueError(b);
                    }
            }
        }
        int int32Value4 = readInt32Value();
        String str3 = new String(bArr, this.offset, int32Value4, StandardCharsets.UTF_8);
        this.offset += int32Value4;
        if (str3.indexOf(46) == -1) {
            return new BigInteger(str3).intValue();
        }
        return TypeUtils.toBigDecimal(str3).intValue();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public int readInt32Value() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        int i2 = this.end;
        int i3 = i + 1;
        int intBE = bArr[i];
        if (!JSONB.isInt32Num(intBE)) {
            if (JSONB.isInt32Byte(intBE)) {
                intBE = getIntByte(bArr, i3, intBE);
                i3 = i + 2;
            } else if (JSONB.isInt32Short(intBE) && i + 2 < i2) {
                intBE = getInt3(bArr, i3, intBE);
                i3 = i + 3;
            } else if (intBE == 72 && i + 4 < i2) {
                intBE = IOUtils.getIntBE(bArr, i3);
                i3 = i + 5;
            } else {
                this.offset = i3;
                return readInt32Value0(bArr, (byte) intBE);
            }
        }
        this.offset = i3;
        return intBE;
    }

    private int readInt32Value0(byte[] bArr, byte b) {
        BigDecimal bigDecimal;
        if (JSONB.isInt64Num(b)) {
            return b + 32;
        }
        if (JSONB.isInt64Byte(b)) {
            int i = this.offset;
            this.offset = i + 1;
            return getLongByte(bArr, i, b);
        }
        int i2 = this.end;
        if (JSONB.isInt64Short(b) && this.offset + 1 < i2) {
            int long3 = getLong3(bArr, this.offset, b);
            this.offset += 2;
            return long3;
        }
        if (b == -71) {
            int int32Value = readInt32Value();
            BigInteger bigInteger = readBigInteger();
            if (int32Value == 0) {
                bigDecimal = new BigDecimal(bigInteger);
            } else {
                bigDecimal = new BigDecimal(bigInteger, int32Value);
            }
            return bigDecimal.intValue();
        }
        if (b == 124) {
            int int32Value2 = readInt32Value();
            String str = new String(bArr, this.offset, int32Value2, StandardCharsets.UTF_16LE);
            this.offset += int32Value2;
            if (str.indexOf(46) == -1) {
                return new BigInteger(str).intValue();
            }
            return TypeUtils.toBigDecimal(str).intValue();
        }
        if (b == 121) {
            int int32Value3 = readInt32Value();
            String str2 = new String(bArr, this.offset, int32Value3, StandardCharsets.ISO_8859_1);
            this.offset += int32Value3;
            if (str2.indexOf(46) == -1) {
                return new BigInteger(str2).intValue();
            }
            return TypeUtils.toBigDecimal(str2).intValue();
        }
        if (b == 122) {
            int int32Value4 = readInt32Value();
            String str3 = new String(bArr, this.offset, int32Value4, StandardCharsets.UTF_8);
            this.offset += int32Value4;
            if (str3.indexOf(46) == -1) {
                return new BigInteger(str3).intValue();
            }
            return TypeUtils.toBigDecimal(str3).intValue();
        }
        switch (b) {
            case -85:
                long longBE = IOUtils.getLongBE(bArr, check7(this.offset, i2));
                this.offset += 8;
                return (int) longBE;
            case -84:
            case -83:
                break;
            default:
                switch (b) {
                    case -81:
                        if ((this.context.features & JSONReader.Feature.ErrorOnNullForPrimitives.mask) != 0) {
                            throw new JSONException(info("int value not support input null"));
                        }
                        this.wasNull = true;
                        return 0;
                    case -80:
                    case -78:
                        return 0;
                    case -79:
                    case -77:
                        return 1;
                    case -76:
                        return (int) readInt64Value();
                    case -75:
                        this.offset--;
                        return (int) readDoubleValue();
                    case -74:
                        return readInt32Value();
                    case -73:
                        int intBE = IOUtils.getIntBE(bArr, check3(this.offset, i2));
                        this.offset += 4;
                        return (int) Float.intBitsToFloat(intBE);
                    default:
                        switch (b) {
                            case -68:
                                int i3 = (bArr[this.offset + 1] & UByte.MAX_VALUE) + (bArr[this.offset] << 8);
                                this.offset += 2;
                                return i3;
                            case -67:
                                int i4 = this.offset;
                                this.offset = i4 + 1;
                                return bArr[i4];
                            case -66:
                                long longBE2 = IOUtils.getLongBE(bArr, check7(this.offset, i2));
                                this.offset += 8;
                                return (int) longBE2;
                            case -65:
                                break;
                            default:
                                if (b >= 73 && b <= 120) {
                                    int i5 = b - 73;
                                    String fixedAsciiString = readFixedAsciiString(i5);
                                    this.offset += i5;
                                    if (fixedAsciiString.indexOf(46) == -1) {
                                        return new BigInteger(fixedAsciiString).intValue();
                                    }
                                    return TypeUtils.toBigDecimal(fixedAsciiString).intValue();
                                }
                                throw readInt32ValueError(b);
                        }
                        break;
                }
                break;
        }
        int intBE2 = IOUtils.getIntBE(bArr, check3(this.offset, i2));
        this.offset += 4;
        return intBE2;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean isBinary() {
        return this.bytes[this.offset] == -111;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public byte[] readBinary() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        if (b != -111) {
            throw notSupportType(b);
        }
        int length = readLength();
        byte[] bArr2 = new byte[length];
        System.arraycopy(this.bytes, this.offset, bArr2, 0, length);
        this.offset += length;
        return bArr2;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public Integer readInt32() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        int i2 = this.end;
        int i3 = i + 1;
        byte b = bArr[i];
        if (b == -81) {
            this.offset = i3;
            return null;
        }
        boolean zIsInt32Num = JSONB.isInt32Num(b);
        int i4 = b;
        if (!zIsInt32Num) {
            if (JSONB.isInt32Byte(b)) {
                int intByte = getIntByte(bArr, i3, b);
                i3 = i + 2;
                i4 = intByte;
            } else if (JSONB.isInt32Short(b) && i + 2 < i2) {
                int int3 = getInt3(bArr, i3, b);
                i3 = i + 3;
                i4 = int3;
            } else if (b == 72 && i + 4 < i2) {
                int intBE = IOUtils.getIntBE(bArr, i3);
                i3 = i + 5;
                i4 = intBE;
            } else {
                this.offset = i3;
                return Integer.valueOf(readInt32Value0(bArr, b));
            }
        }
        this.offset = i3;
        return Integer.valueOf(i4);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public Long readInt64() {
        long longBE;
        int i;
        long longByte;
        long j;
        byte[] bArr = this.bytes;
        int i2 = this.offset;
        int i3 = this.end;
        int i4 = i2 + 1;
        byte b = bArr[i2];
        if (b == -81) {
            this.offset = i4;
            return null;
        }
        if (JSONB.isInt64Num(b)) {
            j = b + 32;
        } else {
            if (JSONB.isInt64Byte(b)) {
                longByte = getLongByte(bArr, i4, b);
                i4 = i2 + 2;
            } else {
                if (JSONB.isInt64Short(b) && i2 + 2 < i3) {
                    longBE = getLong3(bArr, i4, b);
                    i = i2 + 3;
                } else if (b == -65 && i2 + 4 < i3) {
                    longBE = IOUtils.getIntBE(bArr, i4);
                    i = i2 + 5;
                } else if (b == -66 && i2 + 8 < i3) {
                    longBE = IOUtils.getLongBE(bArr, i4);
                    i = i2 + 9;
                } else {
                    this.offset = i4;
                    return Long.valueOf(readInt64Value0(bArr, b));
                }
                longByte = longBE;
                i4 = i;
            }
            j = longByte;
        }
        this.offset = i4;
        return Long.valueOf(j);
    }

    String readFixedAsciiString(int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset;
        if (i == 1) {
            return TypeUtils.toString((char) (bArr[i2] & UByte.MAX_VALUE));
        }
        if (i == 2) {
            return TypeUtils.toString((char) (bArr[i2] & UByte.MAX_VALUE), (char) (bArr[i2 + 1] & UByte.MAX_VALUE));
        }
        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            return JDKUtils.latin1StringJDK8(bArr, i2, i);
        }
        return new String(bArr, i2, i, StandardCharsets.ISO_8859_1);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public Float readFloat() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        byte b = bArr[i];
        if (b == -73) {
            int i2 = (bArr[i + 4] & UByte.MAX_VALUE) + ((bArr[i + 3] & UByte.MAX_VALUE) << 8) + ((bArr[i + 2] & UByte.MAX_VALUE) << 16) + (bArr[i + 1] << 24);
            this.offset = i + 5;
            return Float.valueOf(Float.intBitsToFloat(i2));
        }
        if (b == -81) {
            this.offset = i + 1;
            return null;
        }
        return Float.valueOf(readFloat0());
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public float readFloatValue() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        if (bArr[i] == -73) {
            int intBE = IOUtils.getIntBE(bArr, check3(i + 1, this.end));
            this.offset = i + 5;
            return Float.intBitsToFloat(intBE);
        }
        return readFloat0();
    }

    private float readFloat0() {
        BigDecimal bigDecimal;
        int i = this.end;
        byte[] bArr = this.bytes;
        int i2 = this.offset;
        this.offset = i2 + 1;
        byte b = bArr[i2];
        if (b != -71) {
            if (b != 72) {
                if (b == 124) {
                    int int32Value = readInt32Value();
                    String str = new String(bArr, this.offset, int32Value, StandardCharsets.UTF_16LE);
                    this.offset += int32Value;
                    if (str.indexOf(46) == -1) {
                        return new BigInteger(str).intValue();
                    }
                    return TypeUtils.toBigDecimal(str).intValue();
                }
                if (b == 121) {
                    int int32Value2 = readInt32Value();
                    String str2 = new String(bArr, this.offset, int32Value2, StandardCharsets.ISO_8859_1);
                    this.offset += int32Value2;
                    if (str2.indexOf(46) == -1) {
                        return new BigInteger(str2).intValue();
                    }
                    return TypeUtils.toBigDecimal(str2).intValue();
                }
                if (b != 122) {
                    switch (b) {
                        case -81:
                            if ((this.context.features & JSONReader.Feature.ErrorOnNullForPrimitives.mask) != 0) {
                                throw new JSONException(info("long value not support input null"));
                            }
                            this.wasNull = true;
                            return 0.0f;
                        case -80:
                        case -78:
                            return 0.0f;
                        case -79:
                        case -77:
                            return 1.0f;
                        case -76:
                            return readInt64Value();
                        case -75:
                            long longBE = IOUtils.getLongBE(bArr, check7(this.offset, i));
                            this.offset += 8;
                            return (float) Double.longBitsToDouble(longBE);
                        case -74:
                            return readInt32Value();
                        default:
                            switch (b) {
                                case -68:
                                    int i3 = (bArr[this.offset + 1] & UByte.MAX_VALUE) + (bArr[this.offset] << 8);
                                    this.offset += 2;
                                    return i3;
                                case -67:
                                    this.offset = this.offset + 1;
                                    return bArr[r0];
                                case -66:
                                    long longBE2 = IOUtils.getLongBE(bArr, check7(this.offset, i));
                                    this.offset += 8;
                                    return longBE2;
                                case -65:
                                    break;
                                default:
                                    if (JSONB.isInt32Num(b)) {
                                        return b;
                                    }
                                    if (JSONB.isInt32Byte(b)) {
                                        this.offset = this.offset + 1;
                                        return getIntByte(bArr, r0, b);
                                    }
                                    if (JSONB.isInt32Short(b) && this.offset + 1 < i) {
                                        int int3 = getInt3(bArr, this.offset, b);
                                        this.offset += 2;
                                        return int3;
                                    }
                                    if (JSONB.isInt64Num(b)) {
                                        return b + 32;
                                    }
                                    if (JSONB.isInt64Byte(b)) {
                                        this.offset = this.offset + 1;
                                        return getLongByte(bArr, r0, b);
                                    }
                                    if (JSONB.isInt64Short(b) && this.offset + 1 < i) {
                                        int long3 = getLong3(bArr, this.offset, b);
                                        this.offset += 2;
                                        return long3;
                                    }
                                    if (b >= 73 && b <= 120) {
                                        int i4 = b - 73;
                                        String fixedAsciiString = readFixedAsciiString(i4);
                                        this.offset += i4;
                                        if (fixedAsciiString.indexOf(46) == -1) {
                                            return new BigInteger(fixedAsciiString).intValue();
                                        }
                                        return TypeUtils.toBigDecimal(fixedAsciiString).intValue();
                                    }
                                    throw notSupportType(b);
                            }
                            break;
                    }
                } else {
                    int int32Value3 = readInt32Value();
                    String str3 = new String(bArr, this.offset, int32Value3, StandardCharsets.UTF_8);
                    this.offset += int32Value3;
                    if (str3.indexOf(46) == -1) {
                        return new BigInteger(str3).intValue();
                    }
                    return TypeUtils.toBigDecimal(str3).intValue();
                }
            }
            int intBE = IOUtils.getIntBE(bArr, check3(this.offset, i));
            this.offset += 4;
            return intBE;
        }
        int int32Value4 = readInt32Value();
        BigInteger bigInteger = readBigInteger();
        if (int32Value4 == 0) {
            bigDecimal = new BigDecimal(bigInteger);
        } else {
            bigDecimal = new BigDecimal(bigInteger, int32Value4);
        }
        return bigDecimal.intValue();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public double readDoubleValue() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        if (bArr[i] == -75) {
            long j = (((long) bArr[i + 8]) & 255) + ((((long) bArr[i + 7]) & 255) << 8) + ((((long) bArr[i + 6]) & 255) << 16) + ((((long) bArr[i + 5]) & 255) << 24) + ((((long) bArr[i + 4]) & 255) << 32) + ((((long) bArr[i + 3]) & 255) << 40) + ((255 & ((long) bArr[i + 2])) << 48) + (((long) bArr[i + 1]) << 56);
            this.offset = i + 9;
            return Double.longBitsToDouble(j);
        }
        return readDoubleValue0();
    }

    private double readDoubleValue0() {
        BigDecimal bigDecimal;
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        int i2 = this.end;
        if (b == -74) {
            return readInt32Value();
        }
        if (b == -73) {
            int intBE = IOUtils.getIntBE(bArr, check3(this.offset, i2));
            this.offset += 4;
            return Float.intBitsToFloat(intBE);
        }
        if (b != -71) {
            if (b != 72) {
                if (b == 124) {
                    int int32Value = readInt32Value();
                    String str = new String(bArr, this.offset, int32Value, StandardCharsets.UTF_16LE);
                    this.offset += int32Value;
                    if (str.indexOf(46) == -1) {
                        return new BigInteger(str).intValue();
                    }
                    return TypeUtils.toBigDecimal(str).intValue();
                }
                if (b == 121) {
                    int int32Value2 = readInt32Value();
                    String str2 = new String(bArr, this.offset, int32Value2, StandardCharsets.ISO_8859_1);
                    this.offset += int32Value2;
                    if (str2.indexOf(46) == -1) {
                        return new BigInteger(str2).intValue();
                    }
                    return TypeUtils.toBigDecimal(str2).intValue();
                }
                if (b != 122) {
                    switch (b) {
                        case -81:
                            if ((this.context.features & JSONReader.Feature.ErrorOnNullForPrimitives.mask) != 0) {
                                throw new JSONException(info("long value not support input null"));
                            }
                            this.wasNull = true;
                            return 0.0d;
                        case -80:
                        case -78:
                            return 0.0d;
                        case -79:
                        case -77:
                            return 1.0d;
                        case -76:
                            return readInt64Value();
                        default:
                            switch (b) {
                                case -68:
                                    int i3 = (bArr[this.offset + 1] & UByte.MAX_VALUE) + (bArr[this.offset] << 8);
                                    this.offset += 2;
                                    return i3;
                                case -67:
                                    this.offset = this.offset + 1;
                                    return bArr[r1];
                                case -66:
                                    long longBE = IOUtils.getLongBE(bArr, check7(this.offset, i2));
                                    this.offset += 8;
                                    return longBE;
                                case -65:
                                    break;
                                default:
                                    if (JSONB.isInt32Num(b)) {
                                        return b;
                                    }
                                    if (JSONB.isInt32Byte(b)) {
                                        this.offset = this.offset + 1;
                                        return getIntByte(bArr, r2, b);
                                    }
                                    if (JSONB.isInt32Short(b) && this.offset + 1 < i2) {
                                        int int3 = getInt3(bArr, this.offset, b);
                                        this.offset += 2;
                                        return int3;
                                    }
                                    if (JSONB.isInt64Num(b)) {
                                        return ((long) (b + 40)) - 8;
                                    }
                                    if (JSONB.isInt64Byte(b)) {
                                        this.offset = this.offset + 1;
                                        return getLongByte(bArr, r2, b);
                                    }
                                    if (JSONB.isInt64Short(b) && this.offset + 1 < i2) {
                                        int long3 = getLong3(bArr, this.offset, b);
                                        this.offset += 2;
                                        return long3;
                                    }
                                    if (b >= 73 && b <= 120) {
                                        int i4 = b - 73;
                                        String fixedAsciiString = readFixedAsciiString(i4);
                                        this.offset += i4;
                                        if (fixedAsciiString.indexOf(46) == -1) {
                                            return new BigInteger(fixedAsciiString).intValue();
                                        }
                                        return TypeUtils.toBigDecimal(fixedAsciiString).intValue();
                                    }
                                    throw notSupportType(b);
                            }
                            break;
                    }
                } else {
                    int int32Value3 = readInt32Value();
                    String str3 = new String(bArr, this.offset, int32Value3, StandardCharsets.UTF_8);
                    this.offset += int32Value3;
                    if (str3.indexOf(46) == -1) {
                        return new BigInteger(str3).intValue();
                    }
                    return TypeUtils.toBigDecimal(str3).intValue();
                }
            }
            int intBE2 = IOUtils.getIntBE(bArr, check3(this.offset, i2));
            this.offset += 4;
            return intBE2;
        }
        int int32Value4 = readInt32Value();
        BigInteger bigInteger = readBigInteger();
        if (int32Value4 == 0) {
            bigDecimal = new BigDecimal(bigInteger);
        } else {
            bigDecimal = new BigDecimal(bigInteger, int32Value4);
        }
        return bigDecimal.intValue();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected void readNumber0() {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public Number readNumber() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        if (JSONB.isInt32Num(b)) {
            return Integer.valueOf(b);
        }
        if (JSONB.isInt32Byte(b)) {
            byte[] bArr2 = this.bytes;
            int i2 = this.offset;
            this.offset = i2 + 1;
            return Integer.valueOf(getIntByte(bArr2, i2, b));
        }
        int i3 = this.end;
        if (JSONB.isInt32Short(b) && this.offset + 1 < i3) {
            int int3 = getInt3(this.bytes, this.offset, b);
            this.offset += 2;
            return Integer.valueOf(int3);
        }
        if (JSONB.isInt64Num(b)) {
            return Long.valueOf(((long) (b + 40)) - 8);
        }
        if (JSONB.isInt64Byte(b)) {
            byte[] bArr3 = this.bytes;
            this.offset = this.offset + 1;
            return Long.valueOf(getLongByte(bArr3, r2, b));
        }
        if (JSONB.isInt64Short(b) && this.offset + 1 < i3) {
            int long3 = getLong3(this.bytes, this.offset, b);
            this.offset += 2;
            return Integer.valueOf(long3);
        }
        if (b == -110) {
            throw new JSONException("not support input type : " + readString());
        }
        if (b == 72) {
            int intBE = IOUtils.getIntBE(this.bytes, check3(this.offset, i3));
            this.offset += 4;
            return Integer.valueOf(intBE);
        }
        if (b == 121) {
            int int32Value = readInt32Value();
            String str = new String(this.bytes, this.offset, int32Value, StandardCharsets.ISO_8859_1);
            this.offset += int32Value;
            return TypeUtils.toBigDecimal(str);
        }
        if (b != 122) {
            switch (b) {
                case -81:
                    return null;
                case -80:
                case -78:
                    return Double.valueOf(0.0d);
                case -79:
                case -77:
                    return Double.valueOf(1.0d);
                case -76:
                    return Double.valueOf(readInt64Value());
                case -75:
                    long longBE = IOUtils.getLongBE(this.bytes, check7(this.offset, i3));
                    this.offset += 8;
                    return Double.valueOf(Double.longBitsToDouble(longBE));
                case -74:
                    return Float.valueOf(readInt32Value());
                case -73:
                    int intBE2 = IOUtils.getIntBE(this.bytes, check3(this.offset, i3));
                    this.offset += 4;
                    return Float.valueOf(Float.intBitsToFloat(intBE2));
                case -72:
                    return BigDecimal.valueOf(readInt64Value());
                case -71:
                    int int32Value2 = readInt32Value();
                    BigInteger bigInteger = readBigInteger();
                    if (int32Value2 == 0) {
                        return new BigDecimal(bigInteger);
                    }
                    return new BigDecimal(bigInteger, int32Value2);
                case -70:
                    return BigInteger.valueOf(readInt64Value());
                case -69:
                    int int32Value3 = readInt32Value();
                    byte[] bArr4 = new byte[int32Value3];
                    System.arraycopy(this.bytes, this.offset, bArr4, 0, int32Value3);
                    this.offset += int32Value3;
                    return new BigInteger(bArr4);
                case -68:
                    int i4 = (this.bytes[this.offset + 1] & UByte.MAX_VALUE) + (this.bytes[this.offset] << 8);
                    this.offset += 2;
                    return Short.valueOf((short) i4);
                case -67:
                    byte[] bArr5 = this.bytes;
                    int i5 = this.offset;
                    this.offset = i5 + 1;
                    return Byte.valueOf(bArr5[i5]);
                case -66:
                    long longBE2 = IOUtils.getLongBE(this.bytes, check7(this.offset, i3));
                    this.offset += 8;
                    return Long.valueOf(longBE2);
                case -65:
                    int intBE3 = IOUtils.getIntBE(this.bytes, check3(this.offset, i3));
                    this.offset += 4;
                    return Long.valueOf(intBE3);
                default:
                    if (b >= 73 && b <= 120) {
                        int i6 = b - 73;
                        String fixedAsciiString = readFixedAsciiString(i6);
                        this.offset += i6;
                        return TypeUtils.toBigDecimal(fixedAsciiString);
                    }
                    throw notSupportType(b);
            }
        }
        int int32Value4 = readInt32Value();
        String str2 = new String(this.bytes, this.offset, int32Value4, StandardCharsets.UTF_8);
        this.offset += int32Value4;
        return TypeUtils.toBigDecimal(str2);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public BigDecimal readBigDecimal() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        if (b != -71) {
            if (b == -72) {
                return BigDecimal.valueOf(readInt64Value());
            }
            return readDecimal0(b);
        }
        int int32Value = readInt32Value();
        if (bArr[this.offset] == -70) {
            this.offset++;
            return BigDecimal.valueOf(readInt64Value(), int32Value);
        }
        if (bArr[this.offset] == 72) {
            BigDecimal bigDecimalValueOf = BigDecimal.valueOf(IOUtils.getIntBE(bArr, check3(this.offset + 1, this.end)), int32Value);
            this.offset += 5;
            return bigDecimalValueOf;
        }
        if (bArr[this.offset] == -66) {
            BigDecimal bigDecimalValueOf2 = BigDecimal.valueOf(IOUtils.getLongBE(bArr, check7(this.offset + 1, this.end)), int32Value);
            this.offset += 9;
            return bigDecimalValueOf2;
        }
        BigInteger bigInteger = readBigInteger();
        if (int32Value == 0) {
            return new BigDecimal(bigInteger);
        }
        return new BigDecimal(bigInteger, int32Value);
    }

    private BigDecimal readDecimal0(byte b) {
        int i = this.end;
        if (b != 72) {
            if (b == 124) {
                int int32Value = readInt32Value();
                String str = new String(this.bytes, this.offset, int32Value, StandardCharsets.UTF_16LE);
                this.offset += int32Value;
                return TypeUtils.toBigDecimal(str);
            }
            if (b == 121) {
                int int32Value2 = readInt32Value();
                String str2 = new String(this.bytes, this.offset, int32Value2, StandardCharsets.ISO_8859_1);
                this.offset += int32Value2;
                return TypeUtils.toBigDecimal(str2);
            }
            if (b != 122) {
                switch (b) {
                    case -81:
                        return null;
                    case -80:
                    case -78:
                        return BigDecimal.ZERO;
                    case -79:
                    case -77:
                        return BigDecimal.ONE;
                    case -76:
                        return BigDecimal.valueOf(readInt64Value());
                    case -75:
                        long longBE = IOUtils.getLongBE(this.bytes, check7(this.offset, i));
                        this.offset += 8;
                        return BigDecimal.valueOf((long) Double.longBitsToDouble(longBE));
                    case -74:
                        return BigDecimal.valueOf(readInt32Value());
                    case -73:
                        int intBE = IOUtils.getIntBE(this.bytes, check3(this.offset, i));
                        this.offset += 4;
                        return BigDecimal.valueOf((long) Float.intBitsToFloat(intBE));
                    default:
                        switch (b) {
                            case -69:
                                return new BigDecimal(readBigInteger());
                            case -68:
                                int i2 = (this.bytes[this.offset + 1] & UByte.MAX_VALUE) + (this.bytes[this.offset] << 8);
                                this.offset += 2;
                                return BigDecimal.valueOf(i2);
                            case -67:
                                byte[] bArr = this.bytes;
                                this.offset = this.offset + 1;
                                return BigDecimal.valueOf(bArr[r0]);
                            case -66:
                                long longBE2 = IOUtils.getLongBE(this.bytes, check7(this.offset, i));
                                this.offset += 8;
                                return BigDecimal.valueOf(longBE2);
                            case -65:
                                break;
                            default:
                                if (JSONB.isInt32Num(b)) {
                                    return BigDecimal.valueOf(b);
                                }
                                if (JSONB.isInt32Byte(b)) {
                                    byte[] bArr2 = this.bytes;
                                    this.offset = this.offset + 1;
                                    return BigDecimal.valueOf(getIntByte(bArr2, r1, b));
                                }
                                if (JSONB.isInt32Short(b) && this.offset + 1 < i) {
                                    int int3 = getInt3(this.bytes, this.offset, b);
                                    this.offset += 2;
                                    return BigDecimal.valueOf(int3);
                                }
                                if (JSONB.isInt64Num(b)) {
                                    return BigDecimal.valueOf(b + 32);
                                }
                                if (JSONB.isInt64Byte(b)) {
                                    byte[] bArr3 = this.bytes;
                                    this.offset = this.offset + 1;
                                    return BigDecimal.valueOf(getLongByte(bArr3, r1, b));
                                }
                                if (JSONB.isInt64Short(b) && this.offset + 1 < i) {
                                    int long3 = getLong3(this.bytes, this.offset, b);
                                    this.offset += 2;
                                    return BigDecimal.valueOf(long3);
                                }
                                if (b >= 73 && b <= 120) {
                                    int i3 = b - 73;
                                    String fixedAsciiString = readFixedAsciiString(i3);
                                    this.offset += i3;
                                    return TypeUtils.toBigDecimal(fixedAsciiString);
                                }
                                throw notSupportType(b);
                        }
                        break;
                }
            } else {
                int int32Value3 = readInt32Value();
                String str3 = new String(this.bytes, this.offset, int32Value3, StandardCharsets.UTF_8);
                this.offset += int32Value3;
                return TypeUtils.toBigDecimal(str3);
            }
        }
        int intBE2 = IOUtils.getIntBE(this.bytes, check3(this.offset, i));
        this.offset += 4;
        return BigDecimal.valueOf(intBE2);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public BigInteger readBigInteger() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        if (b == -70) {
            return BigInteger.valueOf(readInt64Value());
        }
        if (b == -69) {
            int int32Value = readInt32Value();
            byte[] bArr2 = new byte[int32Value];
            System.arraycopy(this.bytes, this.offset, bArr2, 0, int32Value);
            this.offset += int32Value;
            return new BigInteger(bArr2);
        }
        return readBigInteger0(b);
    }

    private BigInteger readBigInteger0(byte b) {
        BigDecimal bigDecimal;
        byte[] bArr = this.bytes;
        int i = this.end;
        if (b == -111) {
            int int32Value = readInt32Value();
            byte[] bArr2 = new byte[int32Value];
            System.arraycopy(this.bytes, this.offset, bArr2, 0, int32Value);
            this.offset += int32Value;
            return new BigInteger(bArr2);
        }
        if (b != -71) {
            if (b != 72) {
                if (b == 124) {
                    int int32Value2 = readInt32Value();
                    String str = new String(bArr, this.offset, int32Value2, StandardCharsets.UTF_16LE);
                    this.offset += int32Value2;
                    if (str.indexOf(46) == -1) {
                        return new BigInteger(str);
                    }
                    return TypeUtils.toBigDecimal(str).toBigInteger();
                }
                if (b == 121) {
                    int int32Value3 = readInt32Value();
                    String str2 = new String(bArr, this.offset, int32Value3, StandardCharsets.ISO_8859_1);
                    this.offset += int32Value3;
                    if (str2.indexOf(46) == -1) {
                        return new BigInteger(str2);
                    }
                    return TypeUtils.toBigDecimal(str2).toBigInteger();
                }
                if (b != 122) {
                    switch (b) {
                        case -81:
                            return null;
                        case -80:
                        case -78:
                            return BigInteger.ZERO;
                        case -79:
                        case -77:
                            return BigInteger.ONE;
                        case -76:
                            return BigInteger.valueOf(readInt64Value());
                        case -75:
                            long longBE = IOUtils.getLongBE(bArr, check7(this.offset, i));
                            this.offset += 8;
                            return BigInteger.valueOf((long) Double.longBitsToDouble(longBE));
                        case -74:
                            return BigInteger.valueOf(readInt32Value());
                        case -73:
                            int intBE = IOUtils.getIntBE(bArr, check3(this.offset, i));
                            this.offset += 4;
                            return BigInteger.valueOf((long) Float.intBitsToFloat(intBE));
                        default:
                            switch (b) {
                                case -68:
                                    int i2 = (bArr[this.offset + 1] & UByte.MAX_VALUE) + (bArr[this.offset] << 8);
                                    this.offset += 2;
                                    return BigInteger.valueOf(i2);
                                case -67:
                                    this.offset = this.offset + 1;
                                    return BigInteger.valueOf(bArr[r7]);
                                case -66:
                                    long longBE2 = IOUtils.getLongBE(bArr, check7(this.offset, i));
                                    this.offset += 8;
                                    return BigInteger.valueOf(longBE2);
                                case -65:
                                    break;
                                default:
                                    if (JSONB.isInt32Num(b)) {
                                        return BigInteger.valueOf(b);
                                    }
                                    if (JSONB.isInt32Byte(b)) {
                                        this.offset = this.offset + 1;
                                        return BigInteger.valueOf(getIntByte(bArr, r1, b));
                                    }
                                    if (JSONB.isInt32Short(b) && this.offset + 1 < i) {
                                        int int3 = getInt3(bArr, this.offset, b);
                                        this.offset += 2;
                                        return BigInteger.valueOf(int3);
                                    }
                                    if (JSONB.isInt64Num(b)) {
                                        return BigInteger.valueOf(b + 32);
                                    }
                                    if (JSONB.isInt64Byte(b)) {
                                        this.offset = this.offset + 1;
                                        return BigInteger.valueOf(getLongByte(bArr, r1, b));
                                    }
                                    if (JSONB.isInt64Short(b) && this.offset + 1 < i) {
                                        int long3 = getLong3(bArr, this.offset, b);
                                        this.offset += 2;
                                        return BigInteger.valueOf(long3);
                                    }
                                    if (b >= 73 && b <= 120) {
                                        int i3 = b - 73;
                                        String fixedAsciiString = readFixedAsciiString(i3);
                                        this.offset += i3;
                                        return new BigInteger(fixedAsciiString);
                                    }
                                    throw notSupportType(b);
                            }
                            break;
                    }
                } else {
                    int int32Value4 = readInt32Value();
                    String str3 = new String(bArr, this.offset, int32Value4, StandardCharsets.UTF_8);
                    this.offset += int32Value4;
                    if (str3.indexOf(46) == -1) {
                        return new BigInteger(str3);
                    }
                    return TypeUtils.toBigDecimal(str3).toBigInteger();
                }
            }
            int intBE2 = IOUtils.getIntBE(bArr, check3(this.offset, i));
            this.offset += 4;
            return BigInteger.valueOf(intBE2);
        }
        int int32Value5 = readInt32Value();
        BigInteger bigInteger = readBigInteger();
        if (int32Value5 == 0) {
            bigDecimal = new BigDecimal(bigInteger);
        } else {
            bigDecimal = new BigDecimal(bigInteger, int32Value5);
        }
        return bigDecimal.toBigInteger();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public LocalDate readLocalDate() {
        int i;
        int i2 = this.offset;
        byte[] bArr = this.bytes;
        int i3 = i2 + 1;
        byte b = bArr[i2];
        if (b != -87 || (i = i2 + 4) >= this.end) {
            if (b == -81) {
                this.offset = i3;
                return null;
            }
            return readLocalDate0(b);
        }
        short shortBE = IOUtils.getShortBE(bArr, i3);
        byte b2 = IOUtils.getByte(bArr, i2 + 3);
        byte b3 = IOUtils.getByte(bArr, i);
        this.offset = i2 + 5;
        return LocalDate.of(shortBE, b2, b3);
    }

    private LocalDate readLocalDate0(int i) {
        if (i == -88) {
            return readLocalDateTime().toLocalDate();
        }
        if (i == -86) {
            return readZonedDateTime().toLocalDate();
        }
        if (i >= 73 && i <= 120) {
            int stringLength = getStringLength();
            switch (stringLength) {
                case 8:
                    return readLocalDate8();
                case 9:
                    return readLocalDate9();
                case 10:
                    return readLocalDate10();
                case 11:
                    return readLocalDate11();
                default:
                    if (this.bytes[this.offset + stringLength] == 90) {
                        return readZonedDateTime().toInstant().atZone(this.context.getZoneId()).toLocalDate();
                    }
                    throw new JSONException("TODO : " + stringLength + ", " + readString());
            }
        }
        if (i == 122 || i == 121) {
            this.strtype = (byte) i;
            this.offset++;
            int length = readLength();
            this.strlen = length;
            switch (length) {
                case 8:
                    return readLocalDate8();
                case 9:
                    return readLocalDate9();
                case 10:
                    return readLocalDate10();
                case 11:
                    return readLocalDate11();
            }
        }
        throw notSupportType((byte) i);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public LocalDateTime readLocalDateTime() {
        byte b = this.bytes[this.offset];
        if (b != -88) {
            if (b == -81) {
                this.offset++;
                return null;
            }
            return readLocalDateTime0(b);
        }
        this.offset++;
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        int i2 = bArr[i] << 8;
        byte[] bArr2 = this.bytes;
        int i3 = this.offset;
        this.offset = i3 + 1;
        int i4 = i2 + (bArr2[i3] & UByte.MAX_VALUE);
        byte[] bArr3 = this.bytes;
        int i5 = this.offset;
        this.offset = i5 + 1;
        byte b2 = bArr3[i5];
        byte[] bArr4 = this.bytes;
        int i6 = this.offset;
        this.offset = i6 + 1;
        byte b3 = bArr4[i6];
        byte[] bArr5 = this.bytes;
        int i7 = this.offset;
        this.offset = i7 + 1;
        byte b4 = bArr5[i7];
        byte[] bArr6 = this.bytes;
        int i8 = this.offset;
        this.offset = i8 + 1;
        byte b5 = bArr6[i8];
        byte[] bArr7 = this.bytes;
        int i9 = this.offset;
        this.offset = i9 + 1;
        return LocalDateTime.of(i4, b2, b3, b4, b5, bArr7[i9], readInt32Value());
    }

    /* JADX WARN: Type inference failed for: r4v21, types: [java.time.LocalDateTime] */
    /* JADX WARN: Type inference failed for: r4v23, types: [java.time.LocalDateTime] */
    private LocalDateTime readLocalDateTime0(int i) {
        if (i == -87) {
            LocalDate localDate = readLocalDate();
            if (localDate == null) {
                return null;
            }
            return LocalDateTime.of(localDate, LocalTime.MIN);
        }
        if (i == -86) {
            return readZonedDateTime().toLocalDateTime();
        }
        if (i >= 73 && i <= 120) {
            int stringLength = getStringLength();
            switch (stringLength) {
                case 8:
                    LocalDate localDate8 = readLocalDate8();
                    if (localDate8 == null) {
                        return null;
                    }
                    return LocalDateTime.of(localDate8, LocalTime.MIN);
                case 9:
                    LocalDate localDate9 = readLocalDate9();
                    if (localDate9 == null) {
                        return null;
                    }
                    return LocalDateTime.of(localDate9, LocalTime.MIN);
                case 10:
                    LocalDate localDate10 = readLocalDate10();
                    if (localDate10 == null) {
                        return null;
                    }
                    return LocalDateTime.of(localDate10, LocalTime.MIN);
                case 11:
                    LocalDate localDate11 = readLocalDate11();
                    if (localDate11 == null) {
                        return null;
                    }
                    return LocalDateTime.of(localDate11, LocalTime.MIN);
                case 16:
                    return readLocalDateTime16();
                case 17:
                    return readLocalDateTime17();
                case 18:
                    return readLocalDateTime18();
                case 19:
                    return readLocalDateTime19();
                case 20:
                    return readLocalDateTime20();
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                    LocalDateTime localDateTimeX = readLocalDateTimeX(stringLength);
                    if (localDateTimeX != null) {
                        return localDateTimeX;
                    }
                    ZonedDateTime zonedDateTimeX = readZonedDateTimeX(stringLength);
                    if (zonedDateTimeX != null) {
                        return zonedDateTimeX.toLocalDateTime();
                    }
                    break;
            }
            throw new JSONException("TODO : " + stringLength + ", " + readString());
        }
        throw notSupportType((byte) i);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalDateTime readLocalDateTime12() {
        LocalDateTime localDateTime12;
        if (this.bytes[this.offset] != 85 || (localDateTime12 = DateUtils.parseLocalDateTime12(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 13;
        return localDateTime12;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalDateTime readLocalDateTime14() {
        LocalDateTime localDateTime14;
        if (this.bytes[this.offset] != 87 || (localDateTime14 = DateUtils.parseLocalDateTime14(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 15;
        return localDateTime14;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalDateTime readLocalDateTime16() {
        LocalDateTime localDateTime16;
        if (this.bytes[this.offset] != 89 || (localDateTime16 = DateUtils.parseLocalDateTime16(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 17;
        return localDateTime16;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalDateTime readLocalDateTime17() {
        LocalDateTime localDateTime17;
        if (this.bytes[this.offset] != 90 || (localDateTime17 = DateUtils.parseLocalDateTime17(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 18;
        return localDateTime17;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalTime readLocalTime10() {
        LocalTime localTime10;
        if (this.bytes[this.offset] != 83 || (localTime10 = DateUtils.parseLocalTime10(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 11;
        return localTime10;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalTime readLocalTime11() {
        LocalTime localTime11;
        if (this.bytes[this.offset] != 84 || (localTime11 = DateUtils.parseLocalTime11(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 12;
        return localTime11;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected ZonedDateTime readZonedDateTimeX(int i) {
        ZonedDateTime zonedDateTime;
        byte b = this.bytes[this.offset];
        this.type = b;
        if (b < 73 || b > 120) {
            throw new JSONException("date only support string input");
        }
        if (i < 19 || (zonedDateTime = DateUtils.parseZonedDateTime(this.bytes, this.offset + 1, i, this.context.zoneId)) == null) {
            throw new JSONException("illegal LocalDateTime string : " + readString());
        }
        this.offset += i + 1;
        return zonedDateTime;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public void skipComment() {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public LocalTime readLocalTime() {
        byte b = this.bytes[this.offset];
        if (b == -89) {
            this.offset++;
            byte[] bArr = this.bytes;
            int i = this.offset;
            this.offset = i + 1;
            byte b2 = bArr[i];
            byte[] bArr2 = this.bytes;
            int i2 = this.offset;
            this.offset = i2 + 1;
            byte b3 = bArr2[i2];
            byte[] bArr3 = this.bytes;
            int i3 = this.offset;
            this.offset = i3 + 1;
            return LocalTime.of(b2, b3, bArr3[i3], readInt32Value());
        }
        if (b == -81) {
            this.offset++;
            return null;
        }
        if (b >= 73 && b <= 120) {
            int stringLength = getStringLength();
            if (stringLength != 18) {
                switch (stringLength) {
                    case 5:
                        return readLocalTime5();
                    case 6:
                        return readLocalTime6();
                    case 7:
                        return readLocalTime7();
                    case 8:
                        return readLocalTime8();
                    case 9:
                        return readLocalTime9();
                    case 10:
                        return readLocalTime10();
                    case 11:
                        return readLocalTime11();
                    case 12:
                        return readLocalTime12();
                    default:
                        throw new JSONException("not support len : " + stringLength);
                }
            }
            return readLocalTime18();
        }
        throw new UnsupportedOperationException();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public Instant readInstant() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        if (b != -66) {
            switch (b) {
                case -85:
                    break;
                case -84:
                    long intBE = IOUtils.getIntBE(this.bytes, check3(this.offset, this.end));
                    this.offset += 4;
                    return Instant.ofEpochSecond(intBE, 0L);
                case -83:
                    long intBE2 = IOUtils.getIntBE(this.bytes, check3(this.offset, this.end));
                    this.offset += 4;
                    return Instant.ofEpochSecond(intBE2 * 60, 0L);
                case -82:
                    return Instant.ofEpochSecond(readInt64Value(), readInt32Value());
                default:
                    throw new UnsupportedOperationException();
            }
        }
        long longBE = IOUtils.getLongBE(this.bytes, check7(this.offset, this.end));
        this.offset += 8;
        return Instant.ofEpochMilli(longBE);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public OffsetTime readOffsetTime() {
        ZonedDateTime zonedDateTime = readZonedDateTime();
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.toOffsetDateTime().toOffsetTime();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public OffsetDateTime readOffsetDateTime() {
        ZonedDateTime zonedDateTime = readZonedDateTime();
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.toOffsetDateTime();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public ZonedDateTime readZonedDateTime() {
        ZoneId zoneId;
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        if (b == -86) {
            int i2 = this.offset;
            int i3 = (bArr[i2] << 8) + (bArr[i2 + 1] & UByte.MAX_VALUE);
            byte b2 = bArr[i2 + 2];
            byte b3 = bArr[i2 + 3];
            byte b4 = bArr[i2 + 4];
            byte b5 = bArr[i2 + 5];
            byte b6 = bArr[i2 + 6];
            this.offset = i2 + 7;
            LocalDateTime localDateTimeOf = LocalDateTime.of(i3, b2, b3, b4, b5, b6, readInt32Value());
            if (readValueHashCode() == -4800907791268808639L) {
                zoneId = DateUtils.SHANGHAI_ZONE_ID;
            } else {
                String string = getString();
                ZoneId zoneId2 = this.context.getZoneId();
                zoneId = zoneId2.getId().equals(string) ? zoneId2 : DateUtils.getZoneId(string, DateUtils.SHANGHAI_ZONE_ID);
            }
            return ZonedDateTime.ofLocal(localDateTimeOf, zoneId, null);
        }
        return readZonedDateTime0(b);
    }

    private ZonedDateTime readZonedDateTime0(int i) {
        if (i == -88) {
            byte[] bArr = this.bytes;
            int i2 = this.offset;
            this.offset = i2 + 1;
            int i3 = bArr[i2] << 8;
            byte[] bArr2 = this.bytes;
            int i4 = this.offset;
            this.offset = i4 + 1;
            int i5 = i3 + (bArr2[i4] & UByte.MAX_VALUE);
            byte[] bArr3 = this.bytes;
            int i6 = this.offset;
            this.offset = i6 + 1;
            byte b = bArr3[i6];
            byte[] bArr4 = this.bytes;
            int i7 = this.offset;
            this.offset = i7 + 1;
            byte b2 = bArr4[i7];
            byte[] bArr5 = this.bytes;
            int i8 = this.offset;
            this.offset = i8 + 1;
            byte b3 = bArr5[i8];
            byte[] bArr6 = this.bytes;
            int i9 = this.offset;
            this.offset = i9 + 1;
            byte b4 = bArr6[i9];
            byte[] bArr7 = this.bytes;
            int i10 = this.offset;
            this.offset = i10 + 1;
            return ZonedDateTime.of(LocalDateTime.of(i5, b, b2, b3, b4, bArr7[i10], readInt32Value()), DateUtils.DEFAULT_ZONE_ID);
        }
        if (i == -87) {
            byte[] bArr8 = this.bytes;
            int i11 = this.offset;
            this.offset = i11 + 1;
            int i12 = bArr8[i11] << 8;
            byte[] bArr9 = this.bytes;
            int i13 = this.offset;
            this.offset = i13 + 1;
            int i14 = i12 + (bArr9[i13] & UByte.MAX_VALUE);
            byte[] bArr10 = this.bytes;
            int i15 = this.offset;
            this.offset = i15 + 1;
            byte b5 = bArr10[i15];
            byte[] bArr11 = this.bytes;
            int i16 = this.offset;
            this.offset = i16 + 1;
            return ZonedDateTime.of(LocalDate.of(i14, b5, bArr11[i16]), LocalTime.MIN, DateUtils.DEFAULT_ZONE_ID);
        }
        if (i != -66) {
            switch (i) {
                case -85:
                    break;
                case -84:
                    long intBE = IOUtils.getIntBE(this.bytes, check3(this.offset, this.end));
                    this.offset += 4;
                    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(intBE), DateUtils.DEFAULT_ZONE_ID);
                case -83:
                    long intBE2 = IOUtils.getIntBE(this.bytes, check3(this.offset, this.end));
                    this.offset += 4;
                    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(intBE2 * 60), DateUtils.DEFAULT_ZONE_ID);
                case -82:
                    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(readInt64Value(), readInt32Value()), DateUtils.DEFAULT_ZONE_ID);
                case -81:
                    return null;
                default:
                    if (i >= 73 && i <= 120) {
                        this.offset--;
                        return readZonedDateTimeX(i - 73);
                    }
                    throw notSupportType((byte) i);
            }
        }
        long longBE = IOUtils.getLongBE(this.bytes, check7(this.offset, this.end));
        this.offset += 8;
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(longBE), DateUtils.DEFAULT_ZONE_ID);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public UUID readUUID() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        if (b == -111) {
            int i2 = this.offset;
            this.offset = i2 + 1;
            byte b2 = bArr[i2];
            if (b2 != 16 && this.offset + 15 >= this.end) {
                throw new JSONException("uuid not support " + ((int) b2));
            }
            UUID uuid = new UUID(IOUtils.getLongBE(bArr, this.offset), IOUtils.getLongBE(bArr, this.offset + 8));
            this.offset += 16;
            return uuid;
        }
        if (b == -81) {
            return null;
        }
        if (b == 105) {
            UUID uuid32 = JSONReaderUTF8.readUUID32(bArr, this.offset);
            this.offset += 32;
            return uuid32;
        }
        if (b == 109) {
            if (bArr[this.offset + 8] == 45 && bArr[this.offset + 13] == 45 && bArr[this.offset + 18] == 45 && bArr[this.offset + 23] == 45) {
                UUID uuid36 = JSONReaderUTF8.readUUID36(bArr, this.offset);
                this.offset += 36;
                return uuid36;
            }
            throw new JSONException("Invalid UUID string:  ".concat(new String(bArr, this.offset, 36, StandardCharsets.ISO_8859_1)));
        }
        if (b == 121 || b == 122) {
            int length = readLength();
            if (length == 32) {
                UUID uuid322 = JSONReaderUTF8.readUUID32(bArr, this.offset);
                this.offset += 32;
                return uuid322;
            }
            if (length == 36 && bArr[this.offset + 8] == 45 && bArr[this.offset + 13] == 45 && bArr[this.offset + 18] == 45 && bArr[this.offset + 23] == 45) {
                UUID uuid362 = JSONReaderUTF8.readUUID36(bArr, this.offset);
                this.offset += 36;
                return uuid362;
            }
            throw new JSONException("Invalid UUID string:  ".concat(new String(bArr, this.offset, length, StandardCharsets.UTF_8)));
        }
        throw notSupportType(b);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public Boolean readBool() {
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        if (b == -81) {
            return null;
        }
        if (b == -79) {
            return true;
        }
        if (b == -80) {
            return false;
        }
        return Boolean.valueOf(readBoolValue0(b));
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean readBoolValue() {
        this.wasNull = false;
        byte[] bArr = this.bytes;
        int i = this.offset;
        this.offset = i + 1;
        byte b = bArr[i];
        if (b == -79) {
            return true;
        }
        if (b == -80) {
            return false;
        }
        return readBoolValue0(b);
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x01b2  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x01ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean readBoolValue0(byte r20) {
        /*
            Method dump skipped, instruction units count: 744
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReaderJSONB.readBoolValue0(byte):boolean");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfMatch(byte b) {
        if (this.bytes[this.offset] != b) {
            return false;
        }
        this.offset++;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfMatchTypedAny() {
        if (this.bytes[this.offset] != -110) {
            return false;
        }
        this.offset++;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected int getStringLength() {
        byte b = this.bytes[this.offset];
        this.type = b;
        if (b < 73 || b >= 120) {
            throw new UnsupportedOperationException();
        }
        return b - 73;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean isDate() {
        byte b = this.bytes[this.offset];
        return b >= -89 && b <= -82;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public Date readDate() {
        long j;
        int i;
        int i2 = this.offset;
        byte[] bArr = this.bytes;
        ZonedDateTime zonedDateTimeOfLocal = null;
        switch (bArr[i2]) {
            case -89:
                zonedDateTimeOfLocal = ZonedDateTime.ofLocal(LocalDateTime.of(LocalDate.of(1970, 1, 1), readLocalTime()), this.context.getZoneId(), null);
                break;
            case -88:
                zonedDateTimeOfLocal = ZonedDateTime.ofLocal(readLocalDateTime(), this.context.getZoneId(), null);
                break;
            case -87:
                zonedDateTimeOfLocal = ZonedDateTime.ofLocal(LocalDateTime.of(readLocalDate(), LocalTime.MIN), this.context.getZoneId(), null);
                break;
            case -86:
                this.offset = i2 + 1;
                zonedDateTimeOfLocal = readTimestampWithTimeZone();
                break;
            case -85:
                long longBE = IOUtils.getLongBE(bArr, i2 + 1);
                this.offset += 9;
                return new Date(longBE);
            case -84:
                long intBE = IOUtils.getIntBE(bArr, check3(i2 + 1, this.end));
                this.offset += 5;
                return new Date(intBE * 1000);
            case -83:
                long intBE2 = IOUtils.getIntBE(bArr, check3(i2 + 1, this.end));
                this.offset += 5;
                return new Date(intBE2 * 60000);
            case -82:
                this.offset = i2 + 1;
                return Date.from(Instant.ofEpochSecond(readInt64Value(), readInt32Value()));
        }
        if (zonedDateTimeOfLocal != null) {
            long epochSecond = zonedDateTimeOfLocal.toEpochSecond();
            int nano = zonedDateTimeOfLocal.toLocalTime().getNano();
            if (epochSecond < 0 && nano > 0) {
                j = (epochSecond + 1) * 1000;
                i = (nano / DurationKt.NANOS_IN_MILLIS) + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
            } else {
                j = epochSecond * 1000;
                i = nano / DurationKt.NANOS_IN_MILLIS;
            }
            return new Date(j + ((long) i));
        }
        return super.readDate();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public LocalDate readLocalDate8() {
        LocalDate localDate8;
        if (this.bytes[this.offset] != 81 || (localDate8 = DateUtils.parseLocalDate8(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 9;
        return localDate8;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public LocalDate readLocalDate9() {
        LocalDate localDate9;
        if (this.bytes[this.offset] != 82 || (localDate9 = DateUtils.parseLocalDate9(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 10;
        return localDate9;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalDate readLocalDate10() {
        LocalDate localDate10;
        byte b = this.strtype;
        if ((b == 121 || b == 122) && this.strlen == 10) {
            localDate10 = DateUtils.parseLocalDate10(this.bytes, this.offset);
        } else if (this.bytes[this.offset] != 83 || (localDate10 = DateUtils.parseLocalDate10(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 11;
        return localDate10;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalDate readLocalDate11() {
        LocalDate localDate11;
        byte b = this.strtype;
        if ((b == 121 || b == 122) && this.strlen == 11) {
            localDate11 = DateUtils.parseLocalDate11(this.bytes, this.offset);
        } else if (this.bytes[this.offset] != 84 || (localDate11 = DateUtils.parseLocalDate11(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 12;
        return localDate11;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalTime readLocalTime5() {
        LocalTime localTime5;
        if (this.bytes[this.offset] != 78 || (localTime5 = DateUtils.parseLocalTime5(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 6;
        return localTime5;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalTime readLocalTime6() {
        LocalTime localTime6;
        if (this.bytes[this.offset] != 79 || (localTime6 = DateUtils.parseLocalTime6(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 7;
        return localTime6;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalTime readLocalTime7() {
        LocalTime localTime7;
        if (this.bytes[this.offset] != 80 || (localTime7 = DateUtils.parseLocalTime7(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 8;
        return localTime7;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalTime readLocalTime8() {
        LocalTime localTime8;
        if (this.bytes[this.offset] != 81 || (localTime8 = DateUtils.parseLocalTime8(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 9;
        return localTime8;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalTime readLocalTime9() {
        LocalTime localTime8;
        if (this.bytes[this.offset] != 82 || (localTime8 = DateUtils.parseLocalTime8(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 10;
        return localTime8;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalTime readLocalTime12() {
        LocalTime localTime12;
        if (this.bytes[this.offset] != 85 || (localTime12 = DateUtils.parseLocalTime12(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 13;
        return localTime12;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalTime readLocalTime15() {
        LocalTime localTime15;
        if (this.bytes[this.offset] != 88 || (localTime15 = DateUtils.parseLocalTime15(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 16;
        return localTime15;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalTime readLocalTime18() {
        LocalTime localTime18;
        if (this.bytes[this.offset] != 91 || (localTime18 = DateUtils.parseLocalTime18(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 19;
        return localTime18;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalDateTime readLocalDateTime18() {
        LocalDateTime localDateTime18;
        if (this.bytes[this.offset] != 91 || (localDateTime18 = DateUtils.parseLocalDateTime18(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 19;
        return localDateTime18;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalDateTime readLocalDateTime20() {
        LocalDateTime localDateTime20;
        if (this.bytes[this.offset] != 93 || (localDateTime20 = DateUtils.parseLocalDateTime20(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 21;
        return localDateTime20;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public long readMillis19() {
        if (this.bytes[this.offset] != 92) {
            throw new JSONException("date only support string input");
        }
        long millis19 = DateUtils.parseMillis19(this.bytes, this.offset + 1, this.context.zoneId);
        this.offset += 20;
        return millis19;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalDateTime readLocalDateTime19() {
        byte b = this.bytes[this.offset];
        this.type = b;
        if (b != 92) {
            throw new JSONException("date only support string input");
        }
        LocalDateTime localDateTime19 = DateUtils.parseLocalDateTime19(this.bytes, this.offset + 1);
        if (localDateTime19 == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 20;
        return localDateTime19;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    protected LocalDateTime readLocalDateTimeX(int i) {
        LocalDateTime localDateTimeX;
        byte b = this.bytes[this.offset];
        this.type = b;
        if (b < 73 || b > 120) {
            throw new JSONException("date only support string input");
        }
        if (i < 21 || i > 29 || (localDateTimeX = DateUtils.parseLocalDateTimeX(this.bytes, this.offset + 1, i)) == null) {
            throw new JSONException("illegal LocalDateTime string : " + readString());
        }
        this.offset += i + 1;
        return localDateTimeX;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public String readPattern() {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfMatchIdent(char c, char c2) {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfMatchIdent(char c, char c2, char c3) {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public long readFieldNameHashCodeUnquote() {
        return readFieldNameHashCode();
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfMatchIdent(char c, char c2, char c3, char c4) {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfMatchIdent(char c, char c2, char c3, char c4, char c5) {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfMatchIdent(char c, char c2, char c3, char c4, char c5, char c6) {
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public JSONReader.SavePoint mark() {
        return new JSONReader.SavePoint(this.offset, this.type);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public void reset(JSONReader.SavePoint savePoint) {
        this.offset = savePoint.offset;
        this.type = (byte) savePoint.current;
    }

    @Override // com.alibaba.fastjson2.JSONReader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        byte[] bArr = this.valueBytes;
        if (bArr != null && bArr.length < 8388608) {
            JSONFactory.BYTES_UPDATER.lazySet(this.cacheItem, bArr);
        }
        char[] cArr = this.charBuf;
        if (cArr == null || cArr.length >= 8388608) {
            return;
        }
        JSONFactory.CHARS_UPDATER.lazySet(this.cacheItem, cArr);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean isEnd() {
        return this.offset >= this.end;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public int getRawInt() {
        if (this.offset + 3 < this.end) {
            return JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) this.offset));
        }
        return 0;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public long getRawLong() {
        if (this.offset + 7 < this.end) {
            return JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + ((long) this.offset));
        }
        return 0L;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match3() {
        int i = this.offset + 4;
        if (i > this.end) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match4(byte b) {
        byte[] bArr = this.bytes;
        int i = this.offset;
        int i2 = i + 5;
        if (i2 > this.end || bArr[i + 4] != b) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match5(int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 6;
        if (i2 > this.end || JDKUtils.UNSAFE.getShort(bArr, (BASE + ((long) i2)) - 2) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match6(int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 7;
        if (i2 > this.end || (JDKUtils.UNSAFE.getInt(bArr, (BASE + ((long) i2)) - 3) & ViewCompat.MEASURED_SIZE_MASK) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match7(int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 8;
        if (i2 > this.end || JDKUtils.UNSAFE.getInt(bArr, (BASE + ((long) i2)) - 4) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match8(int i, byte b) {
        byte[] bArr = this.bytes;
        int i2 = this.offset;
        int i3 = i2 + 9;
        if (i3 >= this.end || JDKUtils.UNSAFE.getInt(bArr, (BASE + ((long) i3)) - 5) != i || bArr[i2 + 8] != b) {
            return false;
        }
        this.offset = i3;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match9(long j) {
        byte[] bArr = this.bytes;
        int i = this.offset;
        int i2 = i + 10;
        if (i + 11 >= this.end || (JDKUtils.UNSAFE.getLong(bArr, (BASE + ((long) i2)) - 6) & 281474976710655L) != j) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match10(long j) {
        byte[] bArr = this.bytes;
        int i = this.offset + 11;
        if (i >= this.end || (JDKUtils.UNSAFE.getLong(bArr, (BASE + ((long) i)) - 7) & 72057594037927935L) != j) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match11(long j) {
        byte[] bArr = this.bytes;
        int i = this.offset + 12;
        if (i >= this.end || JDKUtils.UNSAFE.getLong(bArr, (BASE + ((long) i)) - 8) != j) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match12(long j, byte b) {
        byte[] bArr = this.bytes;
        int i = this.offset;
        int i2 = i + 13;
        if (i2 >= this.end || JDKUtils.UNSAFE.getLong(bArr, (BASE + ((long) i2)) - 9) != j || bArr[i + 12] != b) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match13(long j, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset;
        int i3 = i2 + 14;
        if (i2 + 15 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j2 = BASE;
        long j3 = i3;
        if (unsafe.getLong(bArr, (j2 + j3) - 10) != j || JDKUtils.UNSAFE.getShort(bArr, (j2 + j3) - 2) != i) {
            return false;
        }
        this.offset = i3;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match14(long j, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 15;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j2 = BASE;
        long j3 = i2;
        if (unsafe.getLong(bArr, (j2 + j3) - 11) != j || (JDKUtils.UNSAFE.getInt(bArr, (j2 + j3) - 3) & ViewCompat.MEASURED_SIZE_MASK) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match15(long j, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 16;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j2 = BASE;
        long j3 = i2;
        if (unsafe.getLong(bArr, (j2 + j3) - 12) != j || JDKUtils.UNSAFE.getInt(bArr, (j2 + j3) - 4) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match16(long j, int i, byte b) {
        byte[] bArr = this.bytes;
        int i2 = this.offset;
        int i3 = i2 + 17;
        if (i3 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j2 = BASE;
        long j3 = i3;
        if (unsafe.getLong(bArr, (j2 + j3) - 13) != j || JDKUtils.UNSAFE.getInt(bArr, (j2 + j3) - 5) != i || bArr[i2 + 16] != b) {
            return false;
        }
        this.offset = i3;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match17(long j, long j2) {
        byte[] bArr = this.bytes;
        int i = this.offset;
        int i2 = i + 18;
        if (i + 19 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j3 = BASE;
        long j4 = i2;
        if (unsafe.getLong(bArr, (j3 + j4) - 14) != j || (JDKUtils.UNSAFE.getLong(bArr, (j3 + j4) - 6) & 281474976710655L) != j2) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match18(long j, long j2) {
        byte[] bArr = this.bytes;
        int i = this.offset + 19;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j3 = BASE;
        long j4 = i;
        if (unsafe.getLong(bArr, (j3 + j4) - 15) != j || (JDKUtils.UNSAFE.getLong(bArr, (j3 + j4) - 7) & 72057594037927935L) != j2) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match19(long j, long j2) {
        byte[] bArr = this.bytes;
        int i = this.offset + 20;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j3 = BASE;
        long j4 = i;
        if (unsafe.getLong(bArr, (j3 + j4) - 16) != j || JDKUtils.UNSAFE.getLong(bArr, (j3 + j4) - 8) != j2) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match20(long j, long j2, byte b) {
        byte[] bArr = this.bytes;
        int i = this.offset;
        int i2 = i + 21;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j3 = BASE;
        long j4 = i2;
        if (unsafe.getLong(bArr, (j3 + j4) - 17) != j || JDKUtils.UNSAFE.getLong(bArr, (j3 + j4) - 9) != j2 || bArr[i + 20] != b) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match21(long j, long j2, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 22;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j3 = BASE;
        long j4 = i2;
        if (unsafe.getLong(bArr, (j3 + j4) - 18) != j || JDKUtils.UNSAFE.getLong(bArr, (j3 + j4) - 10) != j2 || JDKUtils.UNSAFE.getShort(bArr, (j3 + j4) - 2) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match22(long j, long j2, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 23;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j3 = BASE;
        long j4 = i2;
        if (unsafe.getLong(bArr, (j3 + j4) - 19) != j || JDKUtils.UNSAFE.getLong(bArr, (j3 + j4) - 11) != j2 || (JDKUtils.UNSAFE.getInt(bArr, (j3 + j4) - 3) & ViewCompat.MEASURED_SIZE_MASK) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match23(long j, long j2, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 24;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j3 = BASE;
        long j4 = i2;
        if (unsafe.getLong(bArr, (j3 + j4) - 20) != j || JDKUtils.UNSAFE.getLong(bArr, (j3 + j4) - 12) != j2 || JDKUtils.UNSAFE.getInt(bArr, (j3 + j4) - 4) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match24(long j, long j2, int i, byte b) {
        byte[] bArr = this.bytes;
        int i2 = this.offset;
        int i3 = i2 + 25;
        if (i3 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j3 = BASE;
        long j4 = i3;
        if (unsafe.getLong(bArr, (j3 + j4) - 21) != j || JDKUtils.UNSAFE.getLong(bArr, (j3 + j4) - 13) != j2 || JDKUtils.UNSAFE.getInt(bArr, (j3 + j4) - 5) != i || bArr[i2 + 24] != b) {
            return false;
        }
        this.offset = i3;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match25(long j, long j2, long j3) {
        byte[] bArr = this.bytes;
        int i = this.offset + 26;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j4 = BASE;
        long j5 = i;
        if (unsafe.getLong(bArr, (j4 + j5) - 22) != j || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 14) != j2 || (JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 6) & 281474976710655L) != j3) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match26(long j, long j2, long j3) {
        byte[] bArr = this.bytes;
        int i = this.offset + 27;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j4 = BASE;
        long j5 = i;
        if (unsafe.getLong(bArr, (j4 + j5) - 23) != j || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 15) != j2 || (JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 7) & 72057594037927935L) != j3) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match27(long j, long j2, long j3) {
        byte[] bArr = this.bytes;
        int i = this.offset + 28;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j4 = BASE;
        long j5 = i;
        if (unsafe.getLong(bArr, (j4 + j5) - 24) != j || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 16) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 8) != j3) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match28(long j, long j2, long j3, byte b) {
        byte[] bArr = this.bytes;
        int i = this.offset;
        int i2 = i + 29;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j4 = BASE;
        long j5 = i2;
        if (unsafe.getLong(bArr, (j4 + j5) - 25) != j || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 17) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 9) != j3 || bArr[i + 28] != b) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match29(long j, long j2, long j3, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 30;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j4 = BASE;
        long j5 = i2;
        if (unsafe.getLong(bArr, (j4 + j5) - 26) != j || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 18) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 10) != j3 || JDKUtils.UNSAFE.getShort(bArr, (j4 + j5) - 2) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match30(long j, long j2, long j3, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 31;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j4 = BASE;
        long j5 = i2;
        if (unsafe.getLong(bArr, (j4 + j5) - 27) != j || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 19) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 11) != j3 || (JDKUtils.UNSAFE.getInt(bArr, (j4 + j5) - 3) & ViewCompat.MEASURED_SIZE_MASK) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match31(long j, long j2, long j3, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 32;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j4 = BASE;
        long j5 = i2;
        if (unsafe.getLong(bArr, (j4 + j5) - 28) != j || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 20) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 12) != j3 || JDKUtils.UNSAFE.getInt(bArr, (j4 + j5) - 4) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match32(long j, long j2, long j3, int i, byte b) {
        byte[] bArr = this.bytes;
        int i2 = this.offset;
        int i3 = i2 + 33;
        if (i3 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j4 = BASE;
        long j5 = i3;
        if (unsafe.getLong(bArr, (j4 + j5) - 29) != j || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 21) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j4 + j5) - 13) != j3 || JDKUtils.UNSAFE.getInt(bArr, (j4 + j5) - 5) != i || bArr[i2 + 32] != b) {
            return false;
        }
        this.offset = i3;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match33(long j, long j2, long j3, long j4) {
        byte[] bArr = this.bytes;
        int i = this.offset + 34;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j5 = BASE;
        long j6 = i;
        if (unsafe.getLong(bArr, (j5 + j6) - 30) != j || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 22) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 14) != j3 || (JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 6) & 281474976710655L) != j4) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match34(long j, long j2, long j3, long j4) {
        byte[] bArr = this.bytes;
        int i = this.offset + 35;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j5 = BASE;
        long j6 = i;
        if (unsafe.getLong(bArr, (j5 + j6) - 31) != j || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 23) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 15) != j3 || (JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 7) & 72057594037927935L) != j4) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match35(long j, long j2, long j3, long j4) {
        byte[] bArr = this.bytes;
        int i = this.offset + 36;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j5 = BASE;
        long j6 = i;
        if (unsafe.getLong(bArr, (j5 + j6) - 32) != j || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 24) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 16) != j3 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 8) != j4) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match36(long j, long j2, long j3, long j4, byte b) {
        byte[] bArr = this.bytes;
        int i = this.offset;
        int i2 = i + 37;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j5 = BASE;
        long j6 = i2;
        if (unsafe.getLong(bArr, (j5 + j6) - 33) != j || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 25) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 17) != j3 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 9) != j4 || bArr[i + 36] != b) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match37(long j, long j2, long j3, long j4, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 38;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j5 = BASE;
        long j6 = i2;
        if (unsafe.getLong(bArr, (j5 + j6) - 34) != j || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 26) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 18) != j3 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 10) != j4 || JDKUtils.UNSAFE.getShort(bArr, (j5 + j6) - 2) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match38(long j, long j2, long j3, long j4, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 39;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j5 = BASE;
        long j6 = i2;
        if (unsafe.getLong(bArr, (j5 + j6) - 35) != j || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 27) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 19) != j3 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 11) != j4 || (JDKUtils.UNSAFE.getInt(bArr, (j5 + j6) - 3) & ViewCompat.MEASURED_SIZE_MASK) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match39(long j, long j2, long j3, long j4, int i) {
        byte[] bArr = this.bytes;
        int i2 = this.offset + 40;
        if (i2 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j5 = BASE;
        long j6 = i2;
        if (unsafe.getLong(bArr, (j5 + j6) - 36) != j || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 28) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 20) != j3 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 12) != j4 || JDKUtils.UNSAFE.getInt(bArr, (j5 + j6) - 4) != i) {
            return false;
        }
        this.offset = i2;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match40(long j, long j2, long j3, long j4, int i, byte b) {
        byte[] bArr = this.bytes;
        int i2 = this.offset;
        int i3 = i2 + 41;
        if (i3 >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j5 = BASE;
        long j6 = i3;
        if (unsafe.getLong(bArr, (j5 + j6) - 37) != j || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 29) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 21) != j3 || JDKUtils.UNSAFE.getLong(bArr, (j5 + j6) - 13) != j4 || JDKUtils.UNSAFE.getInt(bArr, (j5 + j6) - 5) != i || bArr[i2 + 40] != b) {
            return false;
        }
        this.offset = i3;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match41(long j, long j2, long j3, long j4, long j5) {
        byte[] bArr = this.bytes;
        int i = this.offset + 42;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j6 = BASE;
        long j7 = i;
        if (unsafe.getLong(bArr, (j6 + j7) - 38) != j || JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 30) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 22) != j3 || JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 14) != j4 || (JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 6) & 281474976710655L) != j5) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match42(long j, long j2, long j3, long j4, long j5) {
        byte[] bArr = this.bytes;
        int i = this.offset + 43;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j6 = BASE;
        long j7 = i;
        if (unsafe.getLong(bArr, (j6 + j7) - 39) != j || JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 31) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 23) != j3 || JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 15) != j4 || (JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 7) & 72057594037927935L) != j5) {
            return false;
        }
        this.offset = i;
        return true;
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public boolean nextIfName4Match43(long j, long j2, long j3, long j4, long j5) {
        byte[] bArr = this.bytes;
        int i = this.offset + 44;
        if (i >= this.end) {
            return false;
        }
        Unsafe unsafe = JDKUtils.UNSAFE;
        long j6 = BASE;
        long j7 = i;
        if (unsafe.getLong(bArr, (j6 + j7) - 40) != j || JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 32) != j2 || JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 24) != j3 || JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 16) != j4 || JDKUtils.UNSAFE.getLong(bArr, (j6 + j7) - 8) != j5) {
            return false;
        }
        this.offset = i;
        return true;
    }

    static int check3(int i, int i2) {
        if (i + 3 < i2) {
            return i;
        }
        throw outOfBoundsCheckFromToIndex(i, i2);
    }

    static int check7(int i, int i2) {
        if (i + 7 < i2) {
            return i;
        }
        throw outOfBoundsCheckFromToIndex(i, i2);
    }

    static JSONException outOfBoundsCheckFromToIndex(int i, int i2) {
        return new JSONException("offset overflow, offset " + i + ", end " + i2);
    }

    @Override // com.alibaba.fastjson2.JSONReader
    public void readArray(Collection collection, Type type) {
        int iStartArray = startArray();
        for (int i = 0; i < iStartArray; i++) {
            collection.add(read(type));
        }
    }
}
