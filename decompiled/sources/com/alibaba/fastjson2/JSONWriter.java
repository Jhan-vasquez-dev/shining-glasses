package com.alibaba.fastjson2;

import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.filter.AfterFilter;
import com.alibaba.fastjson2.filter.BeforeFilter;
import com.alibaba.fastjson2.filter.ContextNameFilter;
import com.alibaba.fastjson2.filter.ContextValueFilter;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.filter.LabelFilter;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import okhttp3.HttpUrl;
import okhttp3.internal.ws.RealWebSocket;

/* JADX INFO: loaded from: classes.dex */
public abstract class JSONWriter implements Closeable {
    protected static final long MASK_BROWSER_COMPATIBLE = 32;
    protected static final long MASK_BROWSER_SECURE = 34359738368L;
    protected static final long MASK_ESCAPE_NONE_ASCII = 1073741824;
    protected static final long MASK_IGNORE_NON_FIELD_GETTER = 4294967296L;
    protected static final long MASK_NOT_WRITE_DEFAULT_VALUE = 4096;
    protected static final long MASK_NOT_WRITE_EMPTY_ARRAY = 67108864;
    protected static final long MASK_NOT_WRITE_NUMBER_CLASS_NAME = 1099511627776L;
    protected static final long MASK_NULL_AS_DEFAULT_VALUE = 64;
    protected static final long MASK_PRETTY_FORMAT = 65536;
    protected static final long MASK_REFERENCE_DETECTION = 131072;
    protected static final long MASK_USE_SINGLE_QUOTES = 1048576;
    protected static final long MASK_WRITE_BOOLEAN_AS_NUMBER = 128;
    protected static final long MASK_WRITE_CLASS_NAME = 512;
    protected static final long MASK_WRITE_ENUMS_USING_NAME = 8192;
    protected static final long MASK_WRITE_ENUM_USING_TO_STRING = 16384;
    protected static final long MASK_WRITE_LONG_AS_STRING = 17179869184L;
    protected static final long MASK_WRITE_MAP_NULL_VALUE = 16;
    protected static final long MASK_WRITE_NON_STRING_VALUE_AS_STRING = 256;
    protected static final long MASK_WRITE_NULL_BOOLEAN_AS_FALSE = 33554432;
    protected static final long MASK_WRITE_NULL_LIST_AS_EMPTY = 4194304;
    protected static final long MASK_WRITE_NULL_NUMBER_AS_ZERO = 16777216;
    protected static final long MASK_WRITE_NULL_STRING_AS_EMPTY = 8388608;
    static final byte PRETTY_2_SPACE = 2;
    static final byte PRETTY_4_SPACE = 4;
    static final byte PRETTY_NON = 0;
    static final byte PRETTY_TAB = 1;
    protected Object attachment;
    protected final Charset charset;
    public final Context context;
    public final boolean jsonb;
    protected String lastReference;
    protected int level;
    protected final int maxArraySize;
    protected int off;
    protected Path path;
    protected byte pretty;
    protected final char quote;
    protected IdentityHashMap<Object, Path> refs;
    protected Object rootObject;
    protected boolean startObject;
    public final SymbolTable symbolTable;
    public final boolean useSingleQuote;
    public final boolean utf16;
    public final boolean utf8;
    static final long WRITE_ARRAY_NULL_MASK = Feature.NullAsDefaultValue.mask | Feature.WriteNullListAsEmpty.mask;
    static final long NONE_DIRECT_FEATURES = (Feature.ReferenceDetection.mask | Feature.NotWriteEmptyArray.mask) | Feature.NotWriteDefaultValue.mask;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public abstract void close();

    public abstract void endArray();

    public abstract void endObject();

    public abstract Object ensureCapacity(int i);

    public abstract int flushTo(OutputStream outputStream) throws IOException;

    public abstract int flushTo(OutputStream outputStream, Charset charset) throws IOException;

    public abstract byte[] getBytes();

    public abstract byte[] getBytes(Charset charset);

    public abstract int size();

    public abstract void startArray();

    public abstract void startObject();

    public abstract void write(List list);

    protected abstract void write0(char c);

    public abstract void writeBase64(byte[] bArr);

    public abstract void writeBigInt(BigInteger bigInteger, long j);

    public abstract void writeBool(boolean z);

    public abstract void writeChar(char c);

    public abstract void writeColon();

    public abstract void writeComma();

    public abstract void writeDateTime14(int i, int i2, int i3, int i4, int i5, int i6);

    public abstract void writeDateTime19(int i, int i2, int i3, int i4, int i5, int i6);

    public abstract void writeDateTimeISO8601(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z);

    public abstract void writeDateYYYMMDD10(int i, int i2, int i3);

    public abstract void writeDateYYYMMDD8(int i, int i2, int i3);

    public abstract void writeDecimal(BigDecimal bigDecimal, long j, DecimalFormat decimalFormat);

    public abstract void writeDouble(double d);

    public abstract void writeDouble(double[] dArr);

    public abstract void writeFloat(float f);

    public abstract void writeFloat(float[] fArr);

    public abstract void writeHex(byte[] bArr);

    public abstract void writeInt16(short s);

    public abstract void writeInt32(int i);

    public abstract void writeInt32(Integer num);

    public abstract void writeInt32(int[] iArr);

    public abstract void writeInt64(long j);

    public abstract void writeInt64(Long l);

    public abstract void writeInt64(long[] jArr);

    public abstract void writeInt8(byte b);

    public abstract void writeInt8(byte[] bArr);

    public abstract void writeListInt32(List<Integer> list);

    public abstract void writeListInt64(List<Long> list);

    public abstract void writeLocalDate(LocalDate localDate);

    public abstract void writeLocalDateTime(LocalDateTime localDateTime);

    public abstract void writeLocalTime(LocalTime localTime);

    public abstract void writeName10Raw(long j, long j2);

    public abstract void writeName11Raw(long j, long j2);

    public abstract void writeName12Raw(long j, long j2);

    public abstract void writeName13Raw(long j, long j2);

    public abstract void writeName14Raw(long j, long j2);

    public abstract void writeName15Raw(long j, long j2);

    public abstract void writeName16Raw(long j, long j2);

    public abstract void writeName2Raw(long j);

    public abstract void writeName3Raw(long j);

    public abstract void writeName4Raw(long j);

    public abstract void writeName5Raw(long j);

    public abstract void writeName6Raw(long j);

    public abstract void writeName7Raw(long j);

    public abstract void writeName8Raw(long j);

    public abstract void writeName9Raw(long j, int i);

    public abstract void writeNameRaw(byte[] bArr);

    public abstract void writeNameRaw(char[] cArr);

    public abstract void writeNameRaw(char[] cArr, int i, int i2);

    public abstract void writeNull();

    public abstract void writeOffsetDateTime(OffsetDateTime offsetDateTime);

    public abstract void writeOffsetTime(OffsetTime offsetTime);

    public abstract void writeRaw(char c);

    public abstract void writeRaw(String str);

    public abstract void writeRaw(byte[] bArr);

    public abstract void writeReference(String str);

    public abstract void writeString(byte b);

    public abstract void writeString(int i);

    public abstract void writeString(long j);

    public abstract void writeString(String str);

    public abstract void writeString(short s);

    public abstract void writeString(boolean z);

    public abstract void writeString(char[] cArr);

    public abstract void writeString(char[] cArr, int i, int i2);

    public abstract void writeString(char[] cArr, int i, int i2, boolean z);

    public abstract void writeStringLatin1(byte[] bArr);

    public abstract void writeStringUTF16(byte[] bArr);

    public abstract void writeTimeHHMMSS8(int i, int i2, int i3);

    public abstract void writeUUID(UUID uuid);

    public abstract void writeZonedDateTime(ZonedDateTime zonedDateTime);

    protected JSONWriter(Context context, SymbolTable symbolTable, boolean z, Charset charset) {
        this.context = context;
        this.symbolTable = symbolTable;
        this.charset = charset;
        this.jsonb = z;
        this.utf8 = !z && charset == StandardCharsets.UTF_8;
        this.utf16 = !z && charset == StandardCharsets.UTF_16;
        boolean z2 = (z || (context.features & Feature.UseSingleQuotes.mask) == 0) ? false : true;
        this.useSingleQuote = z2;
        this.quote = z2 ? '\'' : '\"';
        this.maxArraySize = (context.features & Feature.LargeObject.mask) != 0 ? 1073741824 : 67108864;
        if ((context.features & Feature.PrettyFormatWith4Space.mask) != 0) {
            this.pretty = PRETTY_4_SPACE;
            return;
        }
        if ((context.features & Feature.PrettyFormatWith2Space.mask) != 0) {
            this.pretty = PRETTY_2_SPACE;
        } else if ((context.features & Feature.PrettyFormat.mask) != 0) {
            this.pretty = (byte) 1;
        } else {
            this.pretty = (byte) 0;
        }
    }

    public final Charset getCharset() {
        return this.charset;
    }

    public final boolean isUTF8() {
        return this.utf8;
    }

    public final boolean isUTF16() {
        return this.utf16;
    }

    public final boolean isIgnoreNoneSerializable() {
        return (this.context.features & Feature.IgnoreNoneSerializable.mask) != 0;
    }

    public final boolean isIgnoreNoneSerializable(Object obj) {
        return ((this.context.features & Feature.IgnoreNoneSerializable.mask) == 0 || obj == null || Serializable.class.isAssignableFrom(obj.getClass())) ? false : true;
    }

    public final SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public final void config(Feature... featureArr) {
        this.context.config(featureArr);
    }

    public final void config(Feature feature, boolean z) {
        this.context.config(feature, z);
    }

    public final Context getContext() {
        return this.context;
    }

    public final int level() {
        return this.level;
    }

    public final void setRootObject(Object obj) {
        this.rootObject = obj;
        this.path = Path.ROOT;
    }

    public final String setPath(String str, Object obj) {
        Path path;
        if (!isRefDetect(obj)) {
            return null;
        }
        this.path = new Path(this.path, str);
        if (obj == this.rootObject) {
            path = Path.ROOT;
        } else {
            IdentityHashMap<Object, Path> identityHashMap = this.refs;
            if (identityHashMap == null || (path = identityHashMap.get(obj)) == null) {
                if (this.refs == null) {
                    this.refs = new IdentityHashMap<>(8);
                }
                this.refs.put(obj, this.path);
                return null;
            }
        }
        return path.toString();
    }

    public final String setPath(FieldWriter fieldWriter, Object obj) {
        Path path;
        Path path2;
        if (!isRefDetect(obj)) {
            return null;
        }
        if (this.path == Path.ROOT) {
            path = fieldWriter.getRootParentPath();
        } else {
            path = fieldWriter.getPath(this.path);
        }
        this.path = path;
        if (obj == this.rootObject) {
            path2 = Path.ROOT;
        } else {
            IdentityHashMap<Object, Path> identityHashMap = this.refs;
            if (identityHashMap == null || (path2 = identityHashMap.get(obj)) == null) {
                if (this.refs == null) {
                    this.refs = new IdentityHashMap<>(8);
                }
                this.refs.put(obj, this.path);
                return null;
            }
        }
        return path2.toString();
    }

    public final String setPath0(FieldWriter fieldWriter, Object obj) {
        Path path;
        Path path2;
        if (this.path == Path.ROOT) {
            path = fieldWriter.getRootParentPath();
        } else {
            path = fieldWriter.getPath(this.path);
        }
        this.path = path;
        if (obj == this.rootObject) {
            path2 = Path.ROOT;
        } else {
            IdentityHashMap<Object, Path> identityHashMap = this.refs;
            if (identityHashMap == null || (path2 = identityHashMap.get(obj)) == null) {
                if (this.refs == null) {
                    this.refs = new IdentityHashMap<>(8);
                }
                this.refs.put(obj, this.path);
                return null;
            }
        }
        return path2.toString();
    }

    public final void addManagerReference(Object obj) {
        if (this.refs == null) {
            this.refs = new IdentityHashMap<>(8);
        }
        this.refs.putIfAbsent(obj, Path.MANGER_REFERNCE);
    }

    public final boolean writeReference(int i, Object obj) {
        String path = setPath(i, obj);
        if (path == null) {
            return false;
        }
        writeReference(path);
        popPath(obj);
        return true;
    }

    public final String setPath(int i, Object obj) {
        if (isRefDetect(obj)) {
            return setPath0(i, obj);
        }
        return null;
    }

    public final String setPath0(int i, Object obj) {
        Path path;
        Path path2;
        Path path3;
        Path path4 = this.path;
        if (path4 == null) {
            return null;
        }
        if (i == 0) {
            if (path4.child0 != null) {
                path = this.path.child0;
            } else {
                Path path5 = this.path;
                path2 = new Path(this.path, i);
                path5.child0 = path2;
                path = path2;
            }
        } else if (i == 1) {
            if (path4.child1 != null) {
                path = this.path.child1;
            } else {
                Path path6 = this.path;
                path2 = new Path(this.path, i);
                path6.child1 = path2;
                path = path2;
            }
        } else {
            path = new Path(this.path, i);
        }
        this.path = path;
        if (obj == this.rootObject) {
            path3 = Path.ROOT;
        } else {
            IdentityHashMap<Object, Path> identityHashMap = this.refs;
            if (identityHashMap == null || (path3 = identityHashMap.get(obj)) == null) {
                if (this.refs == null) {
                    this.refs = new IdentityHashMap<>(8);
                }
                this.refs.put(obj, this.path);
                return null;
            }
        }
        return path3.toString();
    }

    public final void popPath(Object obj) {
        if (isRefDetect(obj)) {
            popPath0(obj);
        }
    }

    public final void popPath0(Object obj) {
        if (this.path == null || (this.context.features & MASK_REFERENCE_DETECTION) == 0 || obj == Collections.EMPTY_LIST || obj == Collections.EMPTY_SET) {
            return;
        }
        this.path = this.path.parent;
    }

    public final boolean hasFilter() {
        return this.context.hasFilter;
    }

    public final boolean hasFilter(long j) {
        return this.context.hasFilter || (j & this.context.features) != 0;
    }

    public final boolean hasFilter(boolean z) {
        if (this.context.hasFilter) {
            return true;
        }
        return z && (this.context.features & Feature.IgnoreNonFieldGetter.mask) != 0;
    }

    public final boolean isWriteNulls() {
        return (this.context.features & Feature.WriteNulls.mask) != 0;
    }

    public final boolean isRefDetect() {
        return (this.context.features & Feature.ReferenceDetection.mask) != 0 && (this.context.features & FieldInfo.DISABLE_REFERENCE_DETECT) == 0;
    }

    public final boolean isUseSingleQuotes() {
        return this.useSingleQuote;
    }

    public final boolean isRefDetect(Object obj) {
        return ((this.context.features & Feature.ReferenceDetection.mask) == 0 || (this.context.features & FieldInfo.DISABLE_REFERENCE_DETECT) != 0 || obj == null || ObjectWriterProvider.isNotReferenceDetect(obj.getClass())) ? false : true;
    }

    public final boolean containsReference(Object obj) {
        IdentityHashMap<Object, Path> identityHashMap = this.refs;
        return identityHashMap != null && identityHashMap.containsKey(obj);
    }

    public final String getPath(Object obj) {
        Path path;
        IdentityHashMap<Object, Path> identityHashMap = this.refs;
        if (identityHashMap == null || (path = identityHashMap.get(obj)) == null) {
            return "$";
        }
        return path.toString();
    }

    public String getPath() {
        Path path = this.path;
        if (path == null) {
            return null;
        }
        return path.toString();
    }

    public final boolean removeReference(Object obj) {
        IdentityHashMap<Object, Path> identityHashMap = this.refs;
        return (identityHashMap == null || identityHashMap.remove(obj) == null) ? false : true;
    }

    public final boolean isBeanToArray() {
        return (this.context.features & Feature.BeanToArray.mask) != 0;
    }

    public final boolean isEnabled(Feature feature) {
        return (this.context.features & feature.mask) != 0;
    }

    public final boolean isEnabled(long j) {
        return (j & this.context.features) != 0;
    }

    public final long getFeatures() {
        return this.context.features;
    }

    public final long getFeatures(long j) {
        return j | this.context.features;
    }

    public final boolean isIgnoreErrorGetter() {
        return (this.context.features & Feature.IgnoreErrorGetter.mask) != 0;
    }

    public final boolean isWriteTypeInfo(Object obj, Class cls) {
        Class<?> cls2;
        long j = this.context.features;
        if ((Feature.WriteClassName.mask & j) == 0 || obj == null || (cls2 = obj.getClass()) == cls) {
            return false;
        }
        if ((Feature.NotWriteHashMapArrayListClassName.mask & j) == 0 || !(cls2 == HashMap.class || cls2 == ArrayList.class)) {
            return (j & Feature.NotWriteRootClassName.mask) == 0 || obj != this.rootObject;
        }
        return false;
    }

    public final boolean isWriteTypeInfo(Object obj, Type type) {
        Class<?> cls;
        long j = this.context.features;
        if ((Feature.WriteClassName.mask & j) == 0 || obj == null) {
            return false;
        }
        Class<?> cls2 = obj.getClass();
        if (type instanceof Class) {
            cls = (Class) type;
        } else {
            if (type instanceof GenericArrayType) {
                if (isWriteTypeInfoGenericArray((GenericArrayType) type, cls2)) {
                    return false;
                }
            } else if (type instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) type).getRawType();
                if (rawType instanceof Class) {
                    cls = (Class) rawType;
                }
            }
            cls = null;
        }
        if (cls2 == cls) {
            return false;
        }
        if ((Feature.NotWriteHashMapArrayListClassName.mask & j) == 0 || !(cls2 == HashMap.class || cls2 == ArrayList.class)) {
            return (j & Feature.NotWriteRootClassName.mask) == 0 || obj != this.rootObject;
        }
        return false;
    }

    private static boolean isWriteTypeInfoGenericArray(GenericArrayType genericArrayType, Class cls) {
        Type genericComponentType = genericArrayType.getGenericComponentType();
        if (genericComponentType instanceof ParameterizedType) {
            genericComponentType = ((ParameterizedType) genericComponentType).getRawType();
        }
        if (cls.isArray()) {
            return cls.getComponentType().equals(genericComponentType);
        }
        return false;
    }

    public final boolean isWriteTypeInfo(Object obj) {
        Class<?> cls;
        long j = this.context.features;
        if ((Feature.WriteClassName.mask & j) == 0) {
            return false;
        }
        if ((Feature.NotWriteHashMapArrayListClassName.mask & j) == 0 || obj == null || !((cls = obj.getClass()) == HashMap.class || cls == ArrayList.class)) {
            return (j & Feature.NotWriteRootClassName.mask) == 0 || obj != this.rootObject;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0031  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isWriteTypeInfo(java.lang.Object r7, java.lang.reflect.Type r8, long r9) {
        /*
            r6 = this;
            com.alibaba.fastjson2.JSONWriter$Context r0 = r6.context
            long r0 = r0.features
            long r9 = r9 | r0
            com.alibaba.fastjson2.JSONWriter$Feature r0 = com.alibaba.fastjson2.JSONWriter.Feature.WriteClassName
            long r0 = r0.mask
            long r0 = r0 & r9
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            r1 = 0
            if (r0 != 0) goto L12
            return r1
        L12:
            if (r7 != 0) goto L15
            return r1
        L15:
            java.lang.Class r0 = r7.getClass()
            boolean r4 = r8 instanceof java.lang.Class
            if (r4 == 0) goto L20
            java.lang.Class r8 = (java.lang.Class) r8
            goto L32
        L20:
            boolean r4 = r8 instanceof java.lang.reflect.ParameterizedType
            if (r4 == 0) goto L31
            java.lang.reflect.ParameterizedType r8 = (java.lang.reflect.ParameterizedType) r8
            java.lang.reflect.Type r8 = r8.getRawType()
            boolean r4 = r8 instanceof java.lang.Class
            if (r4 == 0) goto L31
            java.lang.Class r8 = (java.lang.Class) r8
            goto L32
        L31:
            r8 = 0
        L32:
            if (r0 != r8) goto L35
            return r1
        L35:
            com.alibaba.fastjson2.JSONWriter$Feature r4 = com.alibaba.fastjson2.JSONWriter.Feature.NotWriteHashMapArrayListClassName
            long r4 = r4.mask
            long r4 = r4 & r9
            int r4 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r4 == 0) goto L56
            java.lang.Class<java.util.HashMap> r4 = java.util.HashMap.class
            if (r0 != r4) goto L51
            if (r8 == 0) goto L50
            java.lang.Class<java.lang.Object> r0 = java.lang.Object.class
            if (r8 == r0) goto L50
            java.lang.Class<java.util.Map> r0 = java.util.Map.class
            if (r8 == r0) goto L50
            java.lang.Class<java.util.AbstractMap> r0 = java.util.AbstractMap.class
            if (r8 != r0) goto L56
        L50:
            return r1
        L51:
            java.lang.Class<java.util.ArrayList> r8 = java.util.ArrayList.class
            if (r0 != r8) goto L56
            return r1
        L56:
            com.alibaba.fastjson2.JSONWriter$Feature r8 = com.alibaba.fastjson2.JSONWriter.Feature.NotWriteRootClassName
            long r4 = r8.mask
            long r8 = r9 & r4
            int r8 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
            if (r8 == 0) goto L66
            java.lang.Object r8 = r6.rootObject
            if (r7 == r8) goto L65
            goto L66
        L65:
            return r1
        L66:
            r7 = 1
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriter.isWriteTypeInfo(java.lang.Object, java.lang.reflect.Type, long):boolean");
    }

    public final boolean isWriteTypeInfo(Object obj, Class cls, long j) {
        Class<?> cls2;
        if (obj == null || (cls2 = obj.getClass()) == cls) {
            return false;
        }
        long j2 = j | this.context.features;
        if ((Feature.WriteClassName.mask & j2) == 0) {
            return false;
        }
        if ((Feature.NotWriteHashMapArrayListClassName.mask & j2) != 0) {
            if (cls2 == HashMap.class) {
                if (cls == null || cls == Object.class || cls == Map.class || cls == AbstractMap.class) {
                    return false;
                }
            } else if (cls2 == ArrayList.class) {
                return false;
            }
        }
        return (j2 & Feature.NotWriteRootClassName.mask) == 0 || obj != this.rootObject;
    }

    public final boolean isWriteMapTypeInfo(Object obj, Class cls, long j) {
        Class<?> cls2;
        if (obj == null || (cls2 = obj.getClass()) == cls) {
            return false;
        }
        long j2 = j | this.context.features;
        if ((Feature.WriteClassName.mask & j2) == 0) {
            return false;
        }
        if ((Feature.NotWriteHashMapArrayListClassName.mask & j2) == 0 || cls2 != HashMap.class) {
            return (j2 & Feature.NotWriteRootClassName.mask) == 0 || obj != this.rootObject;
        }
        return false;
    }

    public final boolean isWriteTypeInfo(Object obj, long j) {
        Class<?> cls;
        long j2 = j | this.context.features;
        if ((Feature.WriteClassName.mask & j2) == 0) {
            return false;
        }
        if ((Feature.NotWriteHashMapArrayListClassName.mask & j2) == 0 || obj == null || !((cls = obj.getClass()) == HashMap.class || cls == ArrayList.class)) {
            return (j2 & Feature.NotWriteRootClassName.mask) == 0 || obj != this.rootObject;
        }
        return false;
    }

    public final ObjectWriter getObjectWriter(Class cls) {
        return this.context.provider.getObjectWriter(cls, cls, (this.context.features & Feature.FieldBased.mask) != 0);
    }

    public final ObjectWriter getObjectWriter(Class cls, String str) {
        return this.context.provider.getObjectWriter(cls, cls, str, (this.context.features & Feature.FieldBased.mask) != 0);
    }

    public final ObjectWriter getObjectWriter(Type type, Class cls) {
        return this.context.provider.getObjectWriter(type, cls, (this.context.features & Feature.FieldBased.mask) != 0);
    }

    public static JSONWriter of() {
        Context context = new Context(JSONFactory.defaultObjectWriterProvider);
        if (JDKUtils.JVM_VERSION == 8) {
            if (JDKUtils.FIELD_STRING_VALUE != null && !JDKUtils.ANDROID && !JDKUtils.OPENJ9) {
                return new JSONWriterUTF16JDK8UF(context);
            }
            return new JSONWriterUTF16JDK8(context);
        }
        if ((JSONFactory.defaultWriterFeatures & Feature.OptimizedForAscii.mask) != 0) {
            return ofUTF8(context);
        }
        if (JDKUtils.FIELD_STRING_VALUE != null && JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null) {
            return new JSONWriterUTF16JDK9UF(context);
        }
        return new JSONWriterUTF16(context);
    }

    public static JSONWriter of(ObjectWriterProvider objectWriterProvider, Feature... featureArr) {
        Context context = new Context(objectWriterProvider);
        context.config(featureArr);
        return of(context);
    }

    public static JSONWriter of(Context context) {
        if (context == null) {
            context = JSONFactory.createWriteContext();
        }
        if (JDKUtils.JVM_VERSION == 8) {
            if (JDKUtils.FIELD_STRING_VALUE != null && !JDKUtils.ANDROID && !JDKUtils.OPENJ9) {
                return new JSONWriterUTF16JDK8UF(context);
            }
            return new JSONWriterUTF16JDK8(context);
        }
        if ((context.features & Feature.OptimizedForAscii.mask) != 0) {
            return new JSONWriterUTF8(context);
        }
        if (JDKUtils.FIELD_STRING_VALUE != null && JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null) {
            return new JSONWriterUTF16JDK9UF(context);
        }
        return new JSONWriterUTF16(context);
    }

    public static JSONWriter of(Feature... featureArr) {
        Context contextCreateWriteContext = JSONFactory.createWriteContext(featureArr);
        if (JDKUtils.JVM_VERSION == 8) {
            if (JDKUtils.FIELD_STRING_VALUE != null && !JDKUtils.ANDROID && !JDKUtils.OPENJ9) {
                return new JSONWriterUTF16JDK8UF(contextCreateWriteContext);
            }
            return new JSONWriterUTF16JDK8(contextCreateWriteContext);
        }
        if ((contextCreateWriteContext.features & Feature.OptimizedForAscii.mask) != 0) {
            return ofUTF8(contextCreateWriteContext);
        }
        if (JDKUtils.FIELD_STRING_VALUE != null && JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null) {
            return new JSONWriterUTF16JDK9UF(contextCreateWriteContext);
        }
        return new JSONWriterUTF16(contextCreateWriteContext);
    }

    public static JSONWriter ofUTF16(Feature... featureArr) {
        Context contextCreateWriteContext = JSONFactory.createWriteContext(featureArr);
        if (JDKUtils.JVM_VERSION == 8) {
            if (JDKUtils.FIELD_STRING_VALUE != null && !JDKUtils.ANDROID && !JDKUtils.OPENJ9) {
                return new JSONWriterUTF16JDK8UF(contextCreateWriteContext);
            }
            return new JSONWriterUTF16JDK8(contextCreateWriteContext);
        }
        if (JDKUtils.FIELD_STRING_VALUE != null && JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null) {
            return new JSONWriterUTF16JDK9UF(contextCreateWriteContext);
        }
        return new JSONWriterUTF16(contextCreateWriteContext);
    }

    public static JSONWriter ofJSONB() {
        return new JSONWriterJSONB(new Context(JSONFactory.defaultObjectWriterProvider), null);
    }

    public static JSONWriter ofJSONB(Context context) {
        return new JSONWriterJSONB(context, null);
    }

    public static JSONWriter ofJSONB(Context context, SymbolTable symbolTable) {
        return new JSONWriterJSONB(context, symbolTable);
    }

    public static JSONWriter ofJSONB(Feature... featureArr) {
        return new JSONWriterJSONB(new Context(JSONFactory.defaultObjectWriterProvider, featureArr), null);
    }

    public static JSONWriter ofJSONB(SymbolTable symbolTable) {
        return new JSONWriterJSONB(new Context(JSONFactory.defaultObjectWriterProvider), symbolTable);
    }

    public static JSONWriter ofPretty() {
        return of(Feature.PrettyFormat);
    }

    public static JSONWriter ofPretty(JSONWriter jSONWriter) {
        if (jSONWriter.pretty == 0) {
            jSONWriter.pretty = (byte) 1;
            jSONWriter.context.features |= Feature.PrettyFormat.mask;
        }
        return jSONWriter;
    }

    public static JSONWriter ofUTF8() {
        return ofUTF8(JSONFactory.createWriteContext());
    }

    public static JSONWriter ofUTF8(Context context) {
        return new JSONWriterUTF8(context);
    }

    public static JSONWriter ofUTF8(Feature... featureArr) {
        return ofUTF8(JSONFactory.createWriteContext(featureArr));
    }

    public void writeBinary(byte[] bArr) {
        if (bArr == null) {
            writeArrayNull();
            return;
        }
        if ((this.context.features & Feature.WriteByteArrayAsBase64.mask) != 0) {
            writeBase64(bArr);
            return;
        }
        startArray();
        for (int i = 0; i < bArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeInt32(bArr[i]);
        }
        endArray();
    }

    public void writeRaw(byte b) {
        throw new JSONException("UnsupportedOperation");
    }

    public void writeNameRaw(byte[] bArr, int i, int i2) {
        throw new JSONException("UnsupportedOperation");
    }

    public final void writeRaw(char[] cArr) {
        writeRaw(cArr, 0, cArr.length);
    }

    public void writeRaw(char[] cArr, int i, int i2) {
        throw new JSONException("UnsupportedOperation");
    }

    public void writeRaw(char c, char c2) {
        throw new JSONException("UnsupportedOperation");
    }

    public void writeSymbol(int i) {
        throw new JSONException("UnsupportedOperation");
    }

    public void writeNameRaw(byte[] bArr, long j) {
        throw new JSONException("UnsupportedOperation");
    }

    protected static boolean isWriteAsString(long j, long j2) {
        if ((17179869440L & j2) == 0) {
            return ((j2 & MASK_BROWSER_COMPATIBLE) == 0 || TypeUtils.isJavaScriptSupport(j)) ? false : true;
        }
        return true;
    }

    protected static boolean isWriteAsString(BigInteger bigInteger, long j) {
        if ((MASK_WRITE_NON_STRING_VALUE_AS_STRING & j) == 0) {
            return ((j & MASK_BROWSER_COMPATIBLE) == 0 || TypeUtils.isJavaScriptSupport(bigInteger)) ? false : true;
        }
        return true;
    }

    protected static boolean isWriteAsString(BigDecimal bigDecimal, long j) {
        if ((MASK_WRITE_NON_STRING_VALUE_AS_STRING & j) == 0) {
            return ((j & MASK_BROWSER_COMPATIBLE) == 0 || TypeUtils.isJavaScriptSupport(bigDecimal)) ? false : true;
        }
        return true;
    }

    public void writeName(String str) {
        boolean z = false;
        if (this.startObject) {
            this.startObject = false;
        } else {
            writeComma();
        }
        boolean z2 = (this.context.features & Feature.UnquoteFieldName.mask) != 0;
        if (!z2 || (str.indexOf(this.quote) < 0 && str.indexOf(92) < 0)) {
            z = z2;
        }
        if (z) {
            writeRaw(str);
        } else {
            writeString(str);
        }
    }

    public final void writeNameValue(String str, Object obj) {
        writeName(str);
        writeColon();
        writeAny(obj);
    }

    public final void writeName(long j) {
        if (this.startObject) {
            this.startObject = false;
        } else {
            writeComma();
        }
        writeInt64(j);
    }

    public final void writeName(int i) {
        if (this.startObject) {
            this.startObject = false;
        } else {
            writeComma();
        }
        writeInt32(i);
    }

    public void writeNameAny(Object obj) {
        if (this.startObject) {
            this.startObject = false;
        } else {
            writeComma();
        }
        writeAny(obj);
    }

    public void startArray(int i) {
        throw new JSONException("UnsupportedOperation");
    }

    public void startArray0() {
        startArray(0);
    }

    public void startArray1() {
        startArray(1);
    }

    public void startArray2() {
        startArray(2);
    }

    public void startArray3() {
        startArray(3);
    }

    public void startArray4() {
        startArray(4);
    }

    public void startArray5() {
        startArray(5);
    }

    public void startArray6() {
        startArray(6);
    }

    public void startArray7() {
        startArray(7);
    }

    public void startArray8() {
        startArray(8);
    }

    public void startArray9() {
        startArray(9);
    }

    public void startArray10() {
        startArray(10);
    }

    public void startArray11() {
        startArray(11);
    }

    public void startArray12() {
        startArray(12);
    }

    public void startArray13() {
        startArray(13);
    }

    public void startArray14() {
        startArray(14);
    }

    public void startArray15() {
        startArray(15);
    }

    public void startArray(Object obj, int i) {
        throw new JSONException("UnsupportedOperation");
    }

    public void writeInt16(short[] sArr) {
        if (sArr == null) {
            writeArrayNull();
            return;
        }
        startArray();
        for (int i = 0; i < sArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeInt16(sArr[i]);
        }
        endArray();
    }

    public final void writeInt32(int i, DecimalFormat decimalFormat) {
        if (decimalFormat == null || this.jsonb) {
            writeInt32(i);
        } else {
            writeString(decimalFormat.format(i));
        }
    }

    public final void writeInt32(int i, String str) {
        if (str == null || this.jsonb) {
            writeInt32(i);
        } else {
            writeString(String.format(str, Integer.valueOf(i)));
        }
    }

    public void writeMillis(long j) {
        writeInt64(j);
    }

    public final void writeFloat(float f, DecimalFormat decimalFormat) {
        if (decimalFormat == null || this.jsonb) {
            writeFloat(f);
        } else if (Float.isNaN(f) || Float.isInfinite(f)) {
            writeNull();
        } else {
            writeRaw(decimalFormat.format(f));
        }
    }

    public final void writeFloat(float[] fArr, DecimalFormat decimalFormat) {
        if (decimalFormat == null || this.jsonb) {
            writeFloat(fArr);
            return;
        }
        if (fArr == null) {
            writeNull();
            return;
        }
        startArray();
        for (int i = 0; i < fArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeRaw(decimalFormat.format(fArr[i]));
        }
        endArray();
    }

    public final void writeFloat(Float f) {
        if (f == null) {
            writeNumberNull();
        } else {
            writeDouble(f.floatValue());
        }
    }

    public final void writeDouble(double d, DecimalFormat decimalFormat) {
        if (decimalFormat == null || this.jsonb) {
            writeDouble(d);
        } else if (Double.isNaN(d) || Double.isInfinite(d)) {
            writeNull();
        } else {
            writeRaw(decimalFormat.format(d));
        }
    }

    public void writeDoubleArray(double d, double d2) {
        startArray();
        writeDouble(d);
        writeComma();
        writeDouble(d2);
        endArray();
    }

    public final void writeDouble(double[] dArr, DecimalFormat decimalFormat) {
        if (decimalFormat == null || this.jsonb) {
            writeDouble(dArr);
            return;
        }
        if (dArr == null) {
            writeNull();
            return;
        }
        startArray();
        for (int i = 0; i < dArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeRaw(decimalFormat.format(dArr[i]));
        }
        endArray();
    }

    public void writeBool(boolean[] zArr) {
        if (zArr == null) {
            writeArrayNull();
            return;
        }
        startArray();
        for (int i = 0; i < zArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeBool(zArr[i]);
        }
        endArray();
    }

    public void writeObjectNull(Class<?> cls) {
        if ((this.context.features & MASK_NULL_AS_DEFAULT_VALUE) != 0) {
            if (cls == Character.class) {
                writeString("\u0000");
                return;
            } else {
                writeRaw('{', '}');
                return;
            }
        }
        writeNull();
    }

    public void writeStringNull() {
        String str;
        long j = this.context.features;
        if ((8388672 & j) != 0) {
            str = (j & MASK_USE_SINGLE_QUOTES) != 0 ? "''" : "\"\"";
        } else {
            str = "null";
        }
        writeRaw(str);
    }

    public void writeArrayNull() {
        writeArrayNull(this.context.features);
    }

    public void writeArrayNull(long j) {
        String str;
        if ((j & 4194368) != 0) {
            str = HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        } else {
            str = "null";
        }
        writeRaw(str);
    }

    public final void writeNumberNull() {
        if ((this.context.features & 16777280) != 0) {
            writeInt32(0);
        } else {
            writeNull();
        }
    }

    public final void writeDecimalNull() {
        if ((this.context.features & MASK_NULL_AS_DEFAULT_VALUE) != 0) {
            writeDouble(0.0d);
        } else if ((this.context.features & MASK_WRITE_NULL_NUMBER_AS_ZERO) != 0) {
            writeInt32(0);
        } else {
            writeNull();
        }
    }

    public final void writeInt64Null() {
        if ((this.context.features & 16777280) != 0) {
            writeInt64(0L);
        } else {
            writeNull();
        }
    }

    public final void writeBooleanNull() {
        if ((this.context.features & (Feature.WriteNullBooleanAsFalse.mask | MASK_NULL_AS_DEFAULT_VALUE)) != 0) {
            writeBool(false);
        } else {
            writeNull();
        }
    }

    public final void writeDecimal(BigDecimal bigDecimal) {
        writeDecimal(bigDecimal, 0L, null);
    }

    public final void writeDecimal(BigDecimal bigDecimal, long j) {
        writeDecimal(bigDecimal, j, null);
    }

    public void writeEnum(Enum r7) {
        if (r7 == null) {
            writeNull();
            return;
        }
        if ((this.context.features & Feature.WriteEnumUsingToString.mask) != 0) {
            writeString(r7.toString());
        } else if ((this.context.features & Feature.WriteEnumsUsingName.mask) != 0) {
            writeString(r7.name());
        } else {
            writeInt32(r7.ordinal());
        }
    }

    public final void writeBigInt(BigInteger bigInteger) {
        writeBigInt(bigInteger, 0L);
    }

    public final void checkAndWriteTypeName(Object obj, Class cls) {
        Class<?> cls2;
        long j = this.context.features;
        if ((Feature.WriteClassName.mask & j) == 0 || obj == null || (cls2 = obj.getClass()) == cls) {
            return;
        }
        if ((Feature.NotWriteHashMapArrayListClassName.mask & j) == 0 || !(cls2 == HashMap.class || cls2 == ArrayList.class)) {
            if ((j & Feature.NotWriteRootClassName.mask) == 0 || obj != this.rootObject) {
                writeTypeName(TypeUtils.getTypeName(cls2));
            }
        }
    }

    public void writeTypeName(String str) {
        throw new JSONException("UnsupportedOperation");
    }

    public boolean writeTypeName(byte[] bArr, long j) {
        throw new JSONException("UnsupportedOperation");
    }

    public final void writeString(Reader reader) {
        writeRaw(this.quote);
        try {
            char[] cArr = new char[2048];
            while (true) {
                int i = reader.read(cArr, 0, 2048);
                if (i < 0) {
                    writeRaw(this.quote);
                    return;
                } else if (i > 0) {
                    writeString(cArr, 0, i, false);
                }
            }
        } catch (Exception e) {
            throw new JSONException("read string from reader error", e);
        }
    }

    public void writeString(boolean[] zArr) {
        if (zArr == null) {
            writeArrayNull();
            return;
        }
        startArray();
        for (int i = 0; i < zArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeString(zArr[i]);
        }
        endArray();
    }

    public void writeString(byte[] bArr) {
        if (bArr == null) {
            writeArrayNull();
            return;
        }
        startArray();
        for (int i = 0; i < bArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeString(bArr[i]);
        }
        endArray();
    }

    public void writeString(short[] sArr) {
        if (sArr == null) {
            writeArrayNull();
            return;
        }
        startArray();
        for (int i = 0; i < sArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeString(sArr[i]);
        }
        endArray();
    }

    public void writeString(int[] iArr) {
        if (iArr == null) {
            writeArrayNull();
            return;
        }
        startArray();
        for (int i = 0; i < iArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeString(iArr[i]);
        }
        endArray();
    }

    public void writeString(long[] jArr) {
        if (jArr == null) {
            writeArrayNull();
            return;
        }
        startArray();
        for (int i = 0; i < jArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeString(jArr[i]);
        }
        endArray();
    }

    public void writeString(float[] fArr) {
        if (fArr == null) {
            writeArrayNull();
            return;
        }
        startArray();
        for (int i = 0; i < fArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeString(fArr[i]);
        }
        endArray();
    }

    public void writeString(double[] dArr) {
        if (dArr == null) {
            writeArrayNull();
            return;
        }
        startArray();
        for (int i = 0; i < dArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeString(dArr[i]);
        }
        endArray();
    }

    public void writeString(float f) {
        writeString(Float.toString(f));
    }

    public void writeString(double d) {
        writeString(Double.toString(d));
    }

    public void writeString(List<String> list) {
        startArray();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                writeComma();
            }
            writeString(list.get(i));
        }
        endArray();
    }

    public void writeString(String[] strArr) {
        if (strArr == null) {
            writeArrayNull();
            return;
        }
        startArray();
        for (int i = 0; i < strArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeString(strArr[i]);
        }
        endArray();
    }

    public void writeSymbol(String str) {
        writeString(str);
    }

    /* JADX WARN: Type inference failed for: r6v2, types: [java.time.ZonedDateTime] */
    protected final boolean writeLocalDateWithFormat(LocalDate localDate) {
        String str;
        Context context = this.context;
        if (context.dateFormatUnixTime || context.dateFormatMillis) {
            long epochMilli = LocalDateTime.of(localDate, LocalTime.MIN).atZone(context.getZoneId()).toInstant().toEpochMilli();
            if (!context.dateFormatMillis) {
                epochMilli /= 1000;
            }
            writeInt64(epochMilli);
            return true;
        }
        DateTimeFormatter dateFormatter = context.getDateFormatter();
        if (dateFormatter == null) {
            return false;
        }
        if (context.isDateFormatHasHour()) {
            str = dateFormatter.format(LocalDateTime.of(localDate, LocalTime.MIN));
        } else {
            str = dateFormatter.format(localDate);
        }
        writeString(str);
        return true;
    }

    public void writeInstant(Instant instant) {
        if (instant == null) {
            writeNull();
        } else {
            writeString(DateTimeFormatter.ISO_INSTANT.format(instant));
        }
    }

    public final void write(JSONObject jSONObject) {
        write((Map<?, ?>) jSONObject);
    }

    public void write(Map<?, ?> map) {
        if (map == null) {
            writeNull();
            return;
        }
        if (map.isEmpty()) {
            writeRaw('{', '}');
            return;
        }
        if ((this.context.features & NONE_DIRECT_FEATURES) != 0) {
            this.context.getObjectWriter(map.getClass()).write(this, map, null, null, 0L);
            return;
        }
        startObject();
        boolean z = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value != null || (this.context.features & Feature.WriteMapNullValue.mask) != 0) {
                if (!z) {
                    writeComma();
                }
                Object key = entry.getKey();
                if (key instanceof String) {
                    writeString((String) key);
                } else {
                    writeAny(key);
                }
                writeColon();
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
        endObject();
    }

    public void writeAny(Object obj) {
        if (obj == null) {
            writeNull();
        } else {
            Class<?> cls = obj.getClass();
            this.context.getObjectWriter(cls, cls).write(this, obj, null, null, 0L);
        }
    }

    public final void writeAs(Object obj, Class cls) {
        if (obj == null) {
            writeNull();
        } else {
            this.context.getObjectWriter(cls).write(this, obj, null, null, 0L);
        }
    }

    public void flushTo(Writer writer) {
        try {
            writer.write(toString());
            this.off = 0;
        } catch (IOException e) {
            throw new JSONException("flushTo error", e);
        }
    }

    public static final class Context {
        static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
        AfterFilter afterFilter;
        BeforeFilter beforeFilter;
        ContextNameFilter contextNameFilter;
        ContextValueFilter contextValueFilter;
        String dateFormat;
        boolean dateFormatISO8601;
        boolean dateFormatMillis;
        boolean dateFormatUnixTime;
        DateTimeFormatter dateFormatter;
        long features;
        boolean formatHasDay;
        boolean formatHasHour;
        boolean formatyyyyMMddhhmmss19;
        boolean hasFilter;
        LabelFilter labelFilter;
        Locale locale;
        int maxLevel;
        NameFilter nameFilter;
        PropertyFilter propertyFilter;
        PropertyPreFilter propertyPreFilter;
        public final ObjectWriterProvider provider;
        ValueFilter valueFilter;
        ZoneId zoneId;

        public Context(ObjectWriterProvider objectWriterProvider) {
            if (objectWriterProvider == null) {
                throw new IllegalArgumentException("objectWriterProvider must not null");
            }
            this.features = JSONFactory.defaultWriterFeatures;
            this.provider = objectWriterProvider;
            this.zoneId = JSONFactory.defaultWriterZoneId;
            this.maxLevel = JSONFactory.defaultMaxLevel;
            String str = JSONFactory.defaultWriterFormat;
            if (str != null) {
                setDateFormat(str);
            }
        }

        public Context(Feature... featureArr) {
            this.features = JSONFactory.defaultWriterFeatures;
            this.provider = JSONFactory.getDefaultObjectWriterProvider();
            this.zoneId = JSONFactory.defaultWriterZoneId;
            this.maxLevel = JSONFactory.defaultMaxLevel;
            String str = JSONFactory.defaultWriterFormat;
            if (str != null) {
                setDateFormat(str);
            }
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
        }

        public Context(String str, Feature... featureArr) {
            this.features = JSONFactory.defaultWriterFeatures;
            this.provider = JSONFactory.getDefaultObjectWriterProvider();
            this.zoneId = JSONFactory.defaultWriterZoneId;
            this.maxLevel = JSONFactory.defaultMaxLevel;
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
            str = str == null ? JSONFactory.defaultWriterFormat : str;
            if (str != null) {
                setDateFormat(str);
            }
        }

        public Context(ObjectWriterProvider objectWriterProvider, Feature... featureArr) {
            if (objectWriterProvider == null) {
                throw new IllegalArgumentException("objectWriterProvider must not null");
            }
            this.features = JSONFactory.defaultWriterFeatures;
            this.provider = objectWriterProvider;
            this.zoneId = JSONFactory.defaultWriterZoneId;
            this.maxLevel = JSONFactory.defaultMaxLevel;
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
            String str = JSONFactory.defaultWriterFormat;
            if (str != null) {
                setDateFormat(str);
            }
        }

        public long getFeatures() {
            return this.features;
        }

        public void setFeatures(long j) {
            this.features = j;
        }

        public boolean isEnabled(Feature feature) {
            return (this.features & feature.mask) != 0;
        }

        public boolean isEnabled(long j) {
            return (j & this.features) != 0;
        }

        public void config(Feature... featureArr) {
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
        }

        public void config(Feature feature, boolean z) {
            if (z) {
                this.features = feature.mask | this.features;
            } else {
                this.features = (~feature.mask) & this.features;
            }
        }

        public void configFilter(Filter... filterArr) {
            for (Filter filter : filterArr) {
                if (filter instanceof NameFilter) {
                    NameFilter nameFilter = this.nameFilter;
                    if (nameFilter == null) {
                        this.nameFilter = (NameFilter) filter;
                    } else {
                        this.nameFilter = NameFilter.compose(nameFilter, (NameFilter) filter);
                    }
                }
                if (filter instanceof ValueFilter) {
                    ValueFilter valueFilter = this.valueFilter;
                    if (valueFilter == null) {
                        this.valueFilter = (ValueFilter) filter;
                    } else {
                        this.valueFilter = ValueFilter.compose(valueFilter, (ValueFilter) filter);
                    }
                }
                if (filter instanceof PropertyFilter) {
                    this.propertyFilter = (PropertyFilter) filter;
                }
                if (filter instanceof PropertyPreFilter) {
                    this.propertyPreFilter = (PropertyPreFilter) filter;
                }
                if (filter instanceof BeforeFilter) {
                    this.beforeFilter = (BeforeFilter) filter;
                }
                if (filter instanceof AfterFilter) {
                    this.afterFilter = (AfterFilter) filter;
                }
                if (filter instanceof LabelFilter) {
                    this.labelFilter = (LabelFilter) filter;
                }
                if (filter instanceof ContextValueFilter) {
                    this.contextValueFilter = (ContextValueFilter) filter;
                }
                if (filter instanceof ContextNameFilter) {
                    this.contextNameFilter = (ContextNameFilter) filter;
                }
            }
            this.hasFilter = (this.propertyPreFilter == null && this.propertyFilter == null && this.nameFilter == null && this.valueFilter == null && this.beforeFilter == null && this.afterFilter == null && this.labelFilter == null && this.contextValueFilter == null && this.contextNameFilter == null) ? false : true;
        }

        public <T> ObjectWriter<T> getObjectWriter(Class<T> cls) {
            return this.provider.getObjectWriter(cls, cls, (this.features & Feature.FieldBased.mask) != 0);
        }

        public <T> ObjectWriter<T> getObjectWriter(Type type, Class<T> cls) {
            return this.provider.getObjectWriter(type, cls, (this.features & Feature.FieldBased.mask) != 0);
        }

        public ObjectWriterProvider getProvider() {
            return this.provider;
        }

        public ZoneId getZoneId() {
            if (this.zoneId == null) {
                this.zoneId = DEFAULT_ZONE_ID;
            }
            return this.zoneId;
        }

        public void setZoneId(ZoneId zoneId) {
            this.zoneId = zoneId;
        }

        public String getDateFormat() {
            return this.dateFormat;
        }

        public boolean isDateFormatMillis() {
            return this.dateFormatMillis;
        }

        public boolean isDateFormatUnixTime() {
            return this.dateFormatUnixTime;
        }

        public boolean isDateFormatISO8601() {
            return this.dateFormatISO8601;
        }

        public boolean isDateFormatHasDay() {
            return this.formatHasDay;
        }

        public boolean isDateFormatHasHour() {
            return this.formatHasHour;
        }

        public boolean isFormatyyyyMMddhhmmss19() {
            return this.formatyyyyMMddhhmmss19;
        }

        public DateTimeFormatter getDateFormatter() {
            String str;
            DateTimeFormatter dateTimeFormatterOfPattern;
            if (this.dateFormatter == null && (str = this.dateFormat) != null && !this.dateFormatMillis && !this.dateFormatISO8601 && !this.dateFormatUnixTime) {
                Locale locale = this.locale;
                if (locale == null) {
                    dateTimeFormatterOfPattern = DateTimeFormatter.ofPattern(str);
                } else {
                    dateTimeFormatterOfPattern = DateTimeFormatter.ofPattern(str, locale);
                }
                this.dateFormatter = dateTimeFormatterOfPattern;
            }
            return this.dateFormatter;
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        public void setDateFormat(String str) {
            boolean z;
            boolean z2;
            boolean zContains;
            boolean z3;
            if (str == null || !str.equals(this.dateFormat)) {
                this.dateFormatter = null;
            }
            if (str != null && !str.isEmpty()) {
                str.hashCode();
                boolean z4 = true;
                boolean z5 = false;
                byte b = -1;
                switch (str.hashCode()) {
                    case -1074095546:
                        if (str.equals("millis")) {
                            b = 0;
                        }
                        break;
                    case -288020395:
                        if (str.equals("unixtime")) {
                            b = 1;
                        }
                        break;
                    case 1333195168:
                        if (str.equals("yyyy-MM-dd HH:mm:ss")) {
                            b = JSONWriter.PRETTY_2_SPACE;
                        }
                        break;
                    case 1834843604:
                        if (str.equals("yyyy-MM-ddTHH:mm:ss")) {
                            b = 3;
                        }
                        break;
                    case 2095190916:
                        if (str.equals("iso8601")) {
                            b = JSONWriter.PRETTY_4_SPACE;
                        }
                        break;
                }
                switch (b) {
                    case 0:
                        z = false;
                        z2 = false;
                        zContains = z2;
                        z3 = zContains;
                        break;
                    case 1:
                        z = true;
                        z4 = false;
                        z2 = false;
                        zContains = z2;
                        z3 = zContains;
                        break;
                    case 2:
                        z2 = true;
                        zContains = true;
                        z3 = true;
                        z = false;
                        z4 = false;
                        break;
                    case 3:
                        str = "yyyy-MM-dd'T'HH:mm:ss";
                        z2 = true;
                        zContains = true;
                        z = false;
                        z4 = false;
                        z3 = false;
                        break;
                    case 4:
                        z = false;
                        z2 = false;
                        zContains = false;
                        z3 = false;
                        z5 = true;
                        z4 = false;
                        break;
                    default:
                        boolean zContains2 = str.contains("d");
                        zContains = str.contains("H");
                        z2 = zContains2;
                        z = false;
                        z4 = false;
                        z3 = false;
                        break;
                }
                this.dateFormatMillis = z4;
                this.dateFormatISO8601 = z5;
                this.dateFormatUnixTime = z;
                this.formatHasDay = z2;
                this.formatHasHour = zContains;
                this.formatyyyyMMddhhmmss19 = z3;
            }
            this.dateFormat = str;
        }

        public PropertyPreFilter getPropertyPreFilter() {
            return this.propertyPreFilter;
        }

        public void setPropertyPreFilter(PropertyPreFilter propertyPreFilter) {
            this.propertyPreFilter = propertyPreFilter;
            if (propertyPreFilter != null) {
                this.hasFilter = true;
            }
        }

        public NameFilter getNameFilter() {
            return this.nameFilter;
        }

        public void setNameFilter(NameFilter nameFilter) {
            this.nameFilter = nameFilter;
            if (nameFilter != null) {
                this.hasFilter = true;
            }
        }

        public ValueFilter getValueFilter() {
            return this.valueFilter;
        }

        public void setValueFilter(ValueFilter valueFilter) {
            this.valueFilter = valueFilter;
            if (valueFilter != null) {
                this.hasFilter = true;
            }
        }

        public ContextValueFilter getContextValueFilter() {
            return this.contextValueFilter;
        }

        public void setContextValueFilter(ContextValueFilter contextValueFilter) {
            this.contextValueFilter = contextValueFilter;
            if (contextValueFilter != null) {
                this.hasFilter = true;
            }
        }

        public ContextNameFilter getContextNameFilter() {
            return this.contextNameFilter;
        }

        public void setContextNameFilter(ContextNameFilter contextNameFilter) {
            this.contextNameFilter = contextNameFilter;
            if (contextNameFilter != null) {
                this.hasFilter = true;
            }
        }

        public PropertyFilter getPropertyFilter() {
            return this.propertyFilter;
        }

        public void setPropertyFilter(PropertyFilter propertyFilter) {
            this.propertyFilter = propertyFilter;
            if (propertyFilter != null) {
                this.hasFilter = true;
            }
        }

        public AfterFilter getAfterFilter() {
            return this.afterFilter;
        }

        public void setAfterFilter(AfterFilter afterFilter) {
            this.afterFilter = afterFilter;
            if (afterFilter != null) {
                this.hasFilter = true;
            }
        }

        public BeforeFilter getBeforeFilter() {
            return this.beforeFilter;
        }

        public void setBeforeFilter(BeforeFilter beforeFilter) {
            this.beforeFilter = beforeFilter;
            if (beforeFilter != null) {
                this.hasFilter = true;
            }
        }

        public LabelFilter getLabelFilter() {
            return this.labelFilter;
        }

        public void setLabelFilter(LabelFilter labelFilter) {
            this.labelFilter = labelFilter;
            if (labelFilter != null) {
                this.hasFilter = true;
            }
        }

        public int getMaxLevel() {
            return this.maxLevel;
        }

        public void setMaxLevel(int i) {
            this.maxLevel = i;
        }
    }

    public enum Feature {
        FieldBased(1),
        IgnoreNoneSerializable(2),
        ErrorOnNoneSerializable(4),
        BeanToArray(8),
        WriteNulls(JSONWriter.MASK_WRITE_MAP_NULL_VALUE),
        WriteMapNullValue(JSONWriter.MASK_WRITE_MAP_NULL_VALUE),
        BrowserCompatible(JSONWriter.MASK_BROWSER_COMPATIBLE),
        NullAsDefaultValue(JSONWriter.MASK_NULL_AS_DEFAULT_VALUE),
        WriteBooleanAsNumber(JSONWriter.MASK_WRITE_BOOLEAN_AS_NUMBER),
        WriteNonStringValueAsString(JSONWriter.MASK_WRITE_NON_STRING_VALUE_AS_STRING),
        WriteClassName(JSONWriter.MASK_WRITE_CLASS_NAME),
        NotWriteRootClassName(RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE),
        NotWriteHashMapArrayListClassName(2048),
        NotWriteDefaultValue(JSONWriter.MASK_NOT_WRITE_DEFAULT_VALUE),
        WriteEnumsUsingName(JSONWriter.MASK_WRITE_ENUMS_USING_NAME),
        WriteEnumUsingToString(16384),
        IgnoreErrorGetter(32768),
        PrettyFormat(JSONWriter.MASK_PRETTY_FORMAT),
        ReferenceDetection(JSONWriter.MASK_REFERENCE_DETECTION),
        WriteNameAsSymbol(262144),
        WriteBigDecimalAsPlain(524288),
        UseSingleQuotes(JSONWriter.MASK_USE_SINGLE_QUOTES),
        MapSortField(2097152),
        WriteNullListAsEmpty(JSONWriter.MASK_WRITE_NULL_LIST_AS_EMPTY),
        WriteNullStringAsEmpty(JSONWriter.MASK_WRITE_NULL_STRING_AS_EMPTY),
        WriteNullNumberAsZero(JSONWriter.MASK_WRITE_NULL_NUMBER_AS_ZERO),
        WriteNullBooleanAsFalse(JSONWriter.MASK_WRITE_NULL_BOOLEAN_AS_FALSE),
        NotWriteEmptyArray(JSONWriter.MASK_NOT_WRITE_EMPTY_ARRAY),
        IgnoreEmpty(JSONWriter.MASK_NOT_WRITE_EMPTY_ARRAY),
        WriteNonStringKeyAsString(134217728),
        WritePairAsJavaBean(268435456),
        OptimizedForAscii(536870912),
        EscapeNoneAscii(JSONWriter.MASK_ESCAPE_NONE_ASCII),
        WriteByteArrayAsBase64(2147483648L),
        IgnoreNonFieldGetter(JSONWriter.MASK_IGNORE_NON_FIELD_GETTER),
        LargeObject(8589934592L),
        WriteLongAsString(JSONWriter.MASK_WRITE_LONG_AS_STRING),
        BrowserSecure(JSONWriter.MASK_BROWSER_SECURE),
        WriteEnumUsingOrdinal(68719476736L),
        WriteThrowableClassName(137438953472L),
        UnquoteFieldName(274877906944L),
        NotWriteSetClassName(549755813888L),
        NotWriteNumberClassName(JSONWriter.MASK_NOT_WRITE_NUMBER_CLASS_NAME),
        SortMapEntriesByKeys(2199023255552L),
        PrettyFormatWith2Space(4398046511104L),
        PrettyFormatWith4Space(8796093022208L),
        WriterUtilDateAsMillis(17592186044416L);

        public final long mask;

        Feature(long j) {
            this.mask = j;
        }

        public boolean isEnabled(long j) {
            return (j & this.mask) != 0;
        }
    }

    public static final class Path {
        Path child0;
        Path child1;
        String fullPath;
        final int index;
        final String name;
        public final Path parent;
        public static final Path ROOT = new Path((Path) null, "$");
        public static final Path MANGER_REFERNCE = new Path((Path) null, "#");

        public Path(Path path, String str) {
            this.parent = path;
            this.name = str;
            this.index = -1;
        }

        public Path(Path path, int i) {
            this.parent = path;
            this.name = null;
            this.index = i;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                Path path = (Path) obj;
                if (this.index == path.index && Objects.equals(this.parent, path.parent) && Objects.equals(this.name, path.name)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.parent, this.name, Integer.valueOf(this.index));
        }

        /* JADX WARN: Removed duplicated region for block: B:139:0x02ca A[FALL_THROUGH] */
        /* JADX WARN: Removed duplicated region for block: B:81:0x018c A[FALL_THROUGH] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.String toString() {
            /*
                Method dump skipped, instruction units count: 968
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONWriter.Path.toString():java.lang.String");
        }
    }

    protected static IllegalArgumentException illegalYear(int i) {
        return new IllegalArgumentException("Only 4 digits numbers are supported. Provided: " + i);
    }

    public final void incrementIndent() {
        this.level++;
    }

    public final void decrementIdent() {
        this.level--;
    }

    public void println() {
        writeRaw('\n');
        for (int i = 0; i < this.level; i++) {
            writeRaw('\t');
        }
    }

    public final void writeReference(Object obj) {
        Path path;
        IdentityHashMap<Object, Path> identityHashMap = this.refs;
        if (identityHashMap == null || (path = identityHashMap.get(obj)) == null) {
            return;
        }
        writeReference(path.toString());
    }

    protected final int newCapacity(int i, int i2) {
        int i3 = i2 + (i2 >> 1);
        if (i3 - i < 0) {
            i3 = i;
        }
        int i4 = this.maxArraySize;
        if (i3 <= i4) {
            return i3;
        }
        if (i < i4) {
            return i4;
        }
        throw new JSONLargeObjectException("Maximum array size exceeded. Try enabling LargeObject feature instead. Requested size: " + i + ", max size: " + this.maxArraySize);
    }

    public Object getAttachment() {
        return this.attachment;
    }

    public void setAttachment(Object obj) {
        this.attachment = obj;
    }

    protected final void overflowLevel() {
        throw new JSONException("level too large : " + this.level);
    }

    public int getOffset() {
        return this.off;
    }

    public void setOffset(int i) {
        this.off = i;
    }
}
