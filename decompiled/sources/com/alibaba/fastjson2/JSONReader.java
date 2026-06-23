package com.alibaba.fastjson2;

import androidx.core.app.NotificationManagerCompat;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.ContextAutoTypeBeforeHandler;
import com.alibaba.fastjson2.filter.ExtraProcessor;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import com.alibaba.fastjson2.reader.ObjectReaderImplObject;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.reader.ValueConsumer;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.ReferenceKey;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.util.Wrapper;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.time.DurationKt;
import okhttp3.internal.ws.RealWebSocket;
import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
public abstract class JSONReader implements Closeable {
    static final char EOI = 26;
    static final boolean[] INT_VALUE_END;
    static final byte JSON_TYPE_ARRAY = 7;
    static final byte JSON_TYPE_BIG_DEC = 8;
    static final byte JSON_TYPE_BOOL = 4;
    static final byte JSON_TYPE_DEC = 2;
    static final byte JSON_TYPE_DOUBLE = 13;
    static final byte JSON_TYPE_FLOAT = 12;
    static final byte JSON_TYPE_INT = 1;
    static final byte JSON_TYPE_INT16 = 10;
    static final byte JSON_TYPE_INT64 = 11;
    static final byte JSON_TYPE_INT8 = 9;
    static final byte JSON_TYPE_NULL = 5;
    static final byte JSON_TYPE_NaN = 14;
    static final byte JSON_TYPE_OBJECT = 6;
    static final byte JSON_TYPE_STRING = 3;
    protected static final long MASK_ALLOW_UN_QUOTED_FIELD_NAMES = 131072;
    protected static final long MASK_DISABLE_REFERENCE_DETECT = 8589934592L;
    protected static final long MASK_DISABLE_SINGLE_QUOTE = 2147483648L;
    protected static final long MASK_EMPTY_STRING_AS_NULL = 134217728;
    protected static final long MASK_ERROR_ON_NONE_SERIALIZABLE = 4;
    protected static final long MASK_FIELD_BASED = 1;
    protected static final long MASK_IGNORE_NONE_SERIALIZABLE = 2;
    protected static final long MASK_INIT_STRING_FIELD_AS_EMPTY = 16;
    protected static final long MASK_SUPPORT_ARRAY_TO_BEAN = 8;
    protected static final long MASK_SUPPORT_AUTO_TYPE = 32;
    protected static final long MASK_SUPPORT_SMART_MATCH = 64;
    protected static final long MASK_TRIM_STRING = 16384;
    static final int MAX_EXP = 2047;
    static final long SPACE = 4294981376L;
    protected boolean boolValue;
    protected char ch;
    protected boolean comma;
    protected Object complex;
    protected final Context context;
    protected byte[] doubleChars;
    protected short exponent;
    public final boolean jsonb;
    protected int level;
    protected int mag0;
    protected int mag1;
    protected int mag2;
    protected int mag3;
    protected boolean nameEscape;
    protected boolean negative;
    protected int offset;
    List<ResolveTask> resolveTasks;
    protected short scale;
    protected String stringValue;
    protected boolean typeRedirect;
    public final boolean utf8;
    protected boolean valueEscape;
    protected byte valueType;
    protected boolean wasNull;

    public interface AutoTypeBeforeHandler extends Filter {
        default Class<?> apply(long j, Class<?> cls, long j2) {
            return null;
        }

        Class<?> apply(String str, Class<?> cls, long j);
    }

    static boolean isFirstIdentifier(int i) {
        if (i >= 65 && i <= 90) {
            return true;
        }
        if ((i >= 97 && i <= 122) || i == 95 || i == 36) {
            return true;
        }
        return (i >= 48 && i <= 57) || i > 127;
    }

    public ObjectReader checkAutoType(Class cls, long j, long j2) {
        return null;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public abstract void close();

    public abstract String getFieldName();

    public abstract long getNameHashCodeLCase();

    public abstract int getRawInt();

    public abstract long getRawLong();

    public abstract String getString();

    protected abstract int getStringLength();

    public byte getType() {
        return ByteCompanionObject.MIN_VALUE;
    }

    public boolean isBinary() {
        return false;
    }

    public boolean isDate() {
        return false;
    }

    public abstract boolean isNull();

    public abstract boolean isReference();

    public abstract void next();

    public abstract boolean nextIfArrayEnd();

    public abstract boolean nextIfArrayStart();

    public abstract boolean nextIfComma();

    public abstract boolean nextIfInfinity();

    public abstract boolean nextIfMatch(char c);

    public abstract boolean nextIfMatchIdent(char c, char c2);

    public abstract boolean nextIfMatchIdent(char c, char c2, char c3);

    public abstract boolean nextIfMatchIdent(char c, char c2, char c3, char c4);

    public abstract boolean nextIfMatchIdent(char c, char c2, char c3, char c4, char c5);

    public abstract boolean nextIfMatchIdent(char c, char c2, char c3, char c4, char c5, char c6);

    public abstract boolean nextIfName4Match10(long j);

    public abstract boolean nextIfName4Match11(long j);

    public abstract boolean nextIfName4Match12(long j, byte b);

    public abstract boolean nextIfName4Match13(long j, int i);

    public boolean nextIfName4Match14(long j, int i) {
        return false;
    }

    public boolean nextIfName4Match15(long j, int i) {
        return false;
    }

    public abstract boolean nextIfName4Match16(long j, int i, byte b);

    public abstract boolean nextIfName4Match17(long j, long j2);

    public abstract boolean nextIfName4Match18(long j, long j2);

    public boolean nextIfName4Match19(long j, long j2) {
        return false;
    }

    public abstract boolean nextIfName4Match2();

    public abstract boolean nextIfName4Match20(long j, long j2, byte b);

    public boolean nextIfName4Match21(long j, long j2, int i) {
        return false;
    }

    public abstract boolean nextIfName4Match22(long j, long j2, int i);

    public abstract boolean nextIfName4Match23(long j, long j2, int i);

    public abstract boolean nextIfName4Match24(long j, long j2, int i, byte b);

    public abstract boolean nextIfName4Match25(long j, long j2, long j3);

    public abstract boolean nextIfName4Match26(long j, long j2, long j3);

    public abstract boolean nextIfName4Match27(long j, long j2, long j3);

    public abstract boolean nextIfName4Match28(long j, long j2, long j3, byte b);

    public abstract boolean nextIfName4Match29(long j, long j2, long j3, int i);

    public abstract boolean nextIfName4Match3();

    public abstract boolean nextIfName4Match30(long j, long j2, long j3, int i);

    public abstract boolean nextIfName4Match31(long j, long j2, long j3, int i);

    public abstract boolean nextIfName4Match32(long j, long j2, long j3, int i, byte b);

    public abstract boolean nextIfName4Match33(long j, long j2, long j3, long j4);

    public abstract boolean nextIfName4Match34(long j, long j2, long j3, long j4);

    public abstract boolean nextIfName4Match35(long j, long j2, long j3, long j4);

    public abstract boolean nextIfName4Match36(long j, long j2, long j3, long j4, byte b);

    public abstract boolean nextIfName4Match37(long j, long j2, long j3, long j4, int i);

    public abstract boolean nextIfName4Match38(long j, long j2, long j3, long j4, int i);

    public abstract boolean nextIfName4Match39(long j, long j2, long j3, long j4, int i);

    public abstract boolean nextIfName4Match4(byte b);

    public abstract boolean nextIfName4Match40(long j, long j2, long j3, long j4, int i, byte b);

    public abstract boolean nextIfName4Match41(long j, long j2, long j3, long j4, long j5);

    public abstract boolean nextIfName4Match42(long j, long j2, long j3, long j4, long j5);

    public abstract boolean nextIfName4Match43(long j, long j2, long j3, long j4, long j5);

    public abstract boolean nextIfName4Match5(int i);

    public abstract boolean nextIfName4Match6(int i);

    public abstract boolean nextIfName4Match7(int i);

    public abstract boolean nextIfName4Match8(int i, byte b);

    public abstract boolean nextIfName4Match9(long j);

    public boolean nextIfName8Match0() {
        return false;
    }

    public boolean nextIfName8Match1() {
        return false;
    }

    public boolean nextIfName8Match2() {
        return false;
    }

    public abstract boolean nextIfNull();

    public abstract boolean nextIfNullOrEmptyString();

    public abstract boolean nextIfObjectEnd();

    public abstract boolean nextIfObjectStart();

    public abstract boolean nextIfSet();

    public boolean nextIfValue4Match10(long j) {
        return false;
    }

    public boolean nextIfValue4Match11(long j) {
        return false;
    }

    public boolean nextIfValue4Match2() {
        return false;
    }

    public boolean nextIfValue4Match3() {
        return false;
    }

    public boolean nextIfValue4Match4(byte b) {
        return false;
    }

    public boolean nextIfValue4Match5(byte b, byte b2) {
        return false;
    }

    public boolean nextIfValue4Match6(int i) {
        return false;
    }

    public boolean nextIfValue4Match7(int i) {
        return false;
    }

    public boolean nextIfValue4Match8(int i, byte b) {
        return false;
    }

    public boolean nextIfValue4Match9(int i, byte b, byte b2) {
        return false;
    }

    public abstract BigDecimal readBigDecimal();

    public abstract boolean readBoolValue();

    public abstract double readDoubleValue();

    public abstract String readFieldName();

    public abstract long readFieldNameHashCode();

    public abstract long readFieldNameHashCodeUnquote();

    public abstract float readFloatValue();

    public abstract byte[] readHex();

    public abstract boolean readIfNull();

    public abstract Integer readInt32();

    public abstract int readInt32Value();

    public abstract Long readInt64();

    public abstract long readInt64Value();

    protected abstract LocalDate readLocalDate10();

    protected abstract LocalDate readLocalDate11();

    protected abstract LocalDate readLocalDate8();

    protected abstract LocalDate readLocalDate9();

    protected abstract LocalDateTime readLocalDateTime12();

    protected abstract LocalDateTime readLocalDateTime14();

    protected abstract LocalDateTime readLocalDateTime16();

    protected abstract LocalDateTime readLocalDateTime17();

    protected abstract LocalDateTime readLocalDateTime18();

    protected abstract LocalDateTime readLocalDateTime19();

    protected abstract LocalDateTime readLocalDateTime20();

    protected abstract LocalDateTime readLocalDateTimeX(int i);

    protected abstract LocalTime readLocalTime10();

    protected abstract LocalTime readLocalTime11();

    protected abstract LocalTime readLocalTime12();

    protected abstract LocalTime readLocalTime15();

    protected abstract LocalTime readLocalTime18();

    protected abstract LocalTime readLocalTime5();

    protected abstract LocalTime readLocalTime6();

    protected abstract LocalTime readLocalTime7();

    protected abstract LocalTime readLocalTime8();

    protected abstract LocalTime readLocalTime9();

    public abstract long readMillis19();

    public abstract void readNull();

    public abstract Date readNullOrNewDate();

    protected abstract void readNumber0();

    public abstract OffsetDateTime readOffsetDateTime();

    public abstract OffsetTime readOffsetTime();

    public abstract String readPattern();

    public abstract String readReference();

    public abstract String readString();

    public abstract UUID readUUID();

    public abstract long readValueHashCode();

    protected abstract ZonedDateTime readZonedDateTimeX(int i);

    public abstract void skipComment();

    public abstract boolean skipName();

    public abstract void skipValue();

    static {
        boolean[] zArr = new boolean[256];
        INT_VALUE_END = zArr;
        Arrays.fill(zArr, true);
        char[] cArr = {'.', 'e', 'E', 't', 'f', 'n', '{', '[', '0', '1', '2', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (int i = 0; i < 19; i++) {
            INT_VALUE_END[cArr[i]] = false;
        }
    }

    public final char current() {
        return this.ch;
    }

    public boolean isEnd() {
        return this.ch == 26;
    }

    public boolean isInt() {
        char c = this.ch;
        if (c == '-' || c == '+') {
            return true;
        }
        return c >= '0' && c <= '9';
    }

    public final boolean hasComma() {
        return this.comma;
    }

    public JSONReader(Context context, boolean z, boolean z2) {
        this.context = context;
        this.jsonb = z;
        this.utf8 = z2;
    }

    public final Context getContext() {
        return this.context;
    }

    public final void errorOnNoneSerializable(Class cls) {
        if ((this.context.features & MASK_ERROR_ON_NONE_SERIALIZABLE) != 0 && !Serializable.class.isAssignableFrom(cls)) {
            throw new JSONException("not support none-Serializable, class ".concat(cls.getName()));
        }
    }

    public final boolean isEnabled(Feature feature) {
        return (this.context.features & feature.mask) != 0;
    }

    public final Locale getLocale() {
        return this.context.getLocale();
    }

    public final ZoneId getZoneId() {
        return this.context.getZoneId();
    }

    public final long features(long j) {
        return j | this.context.features;
    }

    public final void handleResolveTasks(Object obj) {
        List<ResolveTask> list = this.resolveTasks;
        if (list == null) {
            return;
        }
        Object objEval = null;
        for (ResolveTask resolveTask : list) {
            JSONPath jSONPath = resolveTask.reference;
            FieldReader fieldReader = resolveTask.fieldReader;
            if (!jSONPath.isPrevious()) {
                if (!jSONPath.isRef()) {
                    throw new JSONException("reference path invalid : " + jSONPath);
                }
                jSONPath.setReaderContext(this.context);
                if ((this.context.features & Feature.FieldBased.mask) != 0) {
                    JSONWriter.Context contextCreateWriteContext = JSONFactory.createWriteContext();
                    contextCreateWriteContext.features |= JSONWriter.Feature.FieldBased.mask;
                    jSONPath.setWriterContext(contextCreateWriteContext);
                }
                objEval = jSONPath.eval(obj);
            }
            Object obj2 = resolveTask.name;
            Object obj3 = resolveTask.object;
            if (obj2 != null) {
                if (obj3 instanceof Map) {
                    Map map = (Map) obj3;
                    if (obj2 instanceof ReferenceKey) {
                        if (map instanceof LinkedHashMap) {
                            int size = map.size();
                            if (size != 0) {
                                Object[] objArr = new Object[size];
                                Object[] objArr2 = new Object[size];
                                int i = 0;
                                for (Map.Entry entry : map.entrySet()) {
                                    Object key = entry.getKey();
                                    if (obj2 == key) {
                                        objArr[i] = objEval;
                                    } else {
                                        objArr[i] = key;
                                    }
                                    objArr2[i] = entry.getValue();
                                    i++;
                                }
                                map.clear();
                                for (int i2 = 0; i2 < size; i2++) {
                                    map.put(objArr[i2], objArr2[i2]);
                                }
                            }
                        } else {
                            map.put(objEval, map.remove(obj2));
                        }
                    } else {
                        map.put(obj2, objEval);
                    }
                } else if (obj2 instanceof Integer) {
                    if (obj3 instanceof List) {
                        int iIntValue = ((Integer) obj2).intValue();
                        List list2 = (List) obj3;
                        if (iIntValue == list2.size()) {
                            list2.add(objEval);
                        } else if (iIntValue < list2.size() && list2.get(iIntValue) == null) {
                            list2.set(iIntValue, objEval);
                        } else {
                            list2.add(iIntValue, objEval);
                        }
                    } else if (obj3 instanceof Object[]) {
                        ((Object[]) obj3)[((Integer) obj2).intValue()] = objEval;
                    } else if (obj3 instanceof Collection) {
                        ((Collection) obj3).add(objEval);
                    }
                }
            }
            fieldReader.accept(obj3, objEval);
        }
    }

    public final ObjectReader getObjectReader(Type type) {
        return this.context.provider.getObjectReader(type, (this.context.features & MASK_FIELD_BASED) != 0);
    }

    public final boolean isSupportSmartMatch() {
        return (this.context.features & MASK_SUPPORT_SMART_MATCH) != 0;
    }

    public final boolean isInitStringFieldAsEmpty() {
        return (this.context.features & MASK_INIT_STRING_FIELD_AS_EMPTY) != 0;
    }

    public final boolean isSupportSmartMatch(long j) {
        return ((j | this.context.features) & MASK_SUPPORT_SMART_MATCH) != 0;
    }

    public final boolean isSupportBeanArray() {
        return (this.context.features & MASK_SUPPORT_ARRAY_TO_BEAN) != 0;
    }

    public final boolean isSupportBeanArray(long j) {
        return ((j | this.context.features) & MASK_SUPPORT_ARRAY_TO_BEAN) != 0;
    }

    public final boolean isSupportAutoType(long j) {
        return ((j | this.context.features) & MASK_SUPPORT_AUTO_TYPE) != 0;
    }

    public final boolean isSupportAutoTypeOrHandler(long j) {
        return (((j | this.context.features) & MASK_SUPPORT_AUTO_TYPE) == 0 && this.context.autoTypeBeforeHandler == null) ? false : true;
    }

    public final boolean isJSONB() {
        return this.jsonb;
    }

    public final boolean isIgnoreNoneSerializable() {
        return (this.context.features & MASK_IGNORE_NONE_SERIALIZABLE) != 0;
    }

    public boolean hasAutoTypeBeforeHandler() {
        return this.context.autoTypeBeforeHandler != null;
    }

    final char char1(int i) {
        if (i != 34 && i != 35) {
            switch (i) {
                case 38:
                case 39:
                case 40:
                case 41:
                    break;
                default:
                    switch (i) {
                        case 44:
                        case 64:
                        case 95:
                        case 126:
                            break;
                        case 70:
                        case 102:
                            return '\f';
                        case 98:
                            return '\b';
                        case 110:
                            return '\n';
                        case 114:
                            return TokenParser.CR;
                        case 116:
                            return '\t';
                        case 118:
                            return (char) 11;
                        default:
                            switch (i) {
                                case 46:
                                case 47:
                                    break;
                                case 48:
                                    return (char) 0;
                                case 49:
                                    return (char) 1;
                                case 50:
                                    return (char) 2;
                                case 51:
                                    return (char) 3;
                                case 52:
                                    return (char) 4;
                                case 53:
                                    return (char) 5;
                                case 54:
                                    return (char) 6;
                                case 55:
                                    return (char) 7;
                                default:
                                    switch (i) {
                                        case 91:
                                        case 92:
                                        case 93:
                                            break;
                                        default:
                                            throw new JSONException(info("unclosed.str '\\" + ((char) i)));
                                    }
                                    break;
                            }
                            break;
                    }
                    break;
            }
        }
        return (char) i;
    }

    static char char2(int i, int i2) {
        return (char) ((JSONFactory.DIGITS2[i] * 16) + JSONFactory.DIGITS2[i2]);
    }

    public int startArray() {
        if (nextIfArrayStart()) {
            return Integer.MAX_VALUE;
        }
        throw new JSONException(info("illegal input, expect '[', but " + this.ch));
    }

    public final boolean readReference(List list, int i) {
        if (isReference()) {
            return readReference0(list, i);
        }
        return false;
    }

    public boolean readReference(Collection collection, int i) {
        if (isReference()) {
            return readReference0(collection, i);
        }
        return false;
    }

    private boolean readReference0(Collection collection, int i) {
        String reference = readReference();
        if ("..".equals(reference)) {
            collection.add(collection);
            return true;
        }
        addResolveTask(collection, i, JSONPath.of(reference));
        return true;
    }

    public final void addResolveTask(FieldReader fieldReader, Object obj, JSONPath jSONPath) {
        if (this.resolveTasks == null) {
            this.resolveTasks = new ArrayList();
        }
        this.resolveTasks.add(new ResolveTask(fieldReader, obj, fieldReader.fieldName, jSONPath));
    }

    public final void addResolveTask(Map map, Object obj, JSONPath jSONPath) {
        if (this.resolveTasks == null) {
            this.resolveTasks = new ArrayList();
        }
        if (map instanceof LinkedHashMap) {
            map.put(obj, null);
        }
        this.resolveTasks.add(new ResolveTask(null, map, obj, jSONPath));
    }

    public final void addResolveTask(Collection collection, int i, JSONPath jSONPath) {
        if (this.resolveTasks == null) {
            this.resolveTasks = new ArrayList();
        }
        this.resolveTasks.add(new ResolveTask(null, collection, Integer.valueOf(i), jSONPath));
    }

    public final void addResolveTask(Object[] objArr, int i, JSONPath jSONPath) {
        if (this.resolveTasks == null) {
            this.resolveTasks = new ArrayList();
        }
        this.resolveTasks.add(new ResolveTask(null, objArr, Integer.valueOf(i), jSONPath));
    }

    public boolean isArray() {
        return this.ch == '[';
    }

    public boolean isObject() {
        return this.ch == '{';
    }

    public boolean isNumber() {
        char c = this.ch;
        if (c == '+' || c == '-') {
            return true;
        }
        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
            default:
                return false;
        }
    }

    public boolean isString() {
        char c = this.ch;
        return c == '\"' || c == '\'';
    }

    public void endArray() {
        next();
    }

    public final int getOffset() {
        return this.offset;
    }

    public void nextWithoutComment() {
        next();
    }

    public long readTypeHashCode() {
        return readValueHashCode();
    }

    public final void setTypeRedirect(boolean z) {
        this.typeRedirect = z;
    }

    public final boolean isTypeRedirect() {
        return this.typeRedirect;
    }

    public final String readFieldNameUnquote() {
        if (this.ch == '/') {
            skipComment();
        }
        readFieldNameHashCodeUnquote();
        String fieldName = getFieldName();
        if (fieldName == null || fieldName.isEmpty()) {
            throw new JSONException(info("illegal input"));
        }
        return fieldName;
    }

    public byte[] readBinary() {
        if (this.ch == 'x') {
            return readHex();
        }
        if (isString()) {
            String string = readString();
            if (string.isEmpty()) {
                return null;
            }
            if ((this.context.features & Feature.Base64StringAsByteArray.mask) != 0) {
                return Base64.getDecoder().decode(string);
            }
            throw new JSONException(info("not support input " + string));
        }
        if (nextIfArrayStart()) {
            byte[] bArrCopyOf = new byte[64];
            int i = 0;
            while (this.ch != ']') {
                if (i == bArrCopyOf.length) {
                    int length = bArrCopyOf.length;
                    bArrCopyOf = Arrays.copyOf(bArrCopyOf, length + (length >> 1));
                }
                bArrCopyOf[i] = (byte) readInt32Value();
                i++;
            }
            next();
            nextIfComma();
            return Arrays.copyOf(bArrCopyOf, i);
        }
        throw new JSONException(info("not support read binary"));
    }

    public int[] readInt32ValueArray() {
        if (nextIfNull()) {
            return null;
        }
        if (nextIfArrayStart()) {
            int[] iArrCopyOf = new int[8];
            int i = 0;
            while (!nextIfArrayEnd()) {
                if (isEnd()) {
                    throw new JSONException(info("input end"));
                }
                if (i == iArrCopyOf.length) {
                    iArrCopyOf = Arrays.copyOf(iArrCopyOf, iArrCopyOf.length << 1);
                }
                iArrCopyOf[i] = readInt32Value();
                i++;
            }
            nextIfComma();
            return i == iArrCopyOf.length ? iArrCopyOf : Arrays.copyOf(iArrCopyOf, i);
        }
        if (isString()) {
            String string = readString();
            if (string.isEmpty()) {
                return null;
            }
            throw new JSONException(info("not support input " + string));
        }
        throw new JSONException(info("TODO"));
    }

    public boolean nextIfMatch(byte b) {
        throw new JSONException("UnsupportedOperation");
    }

    public boolean nextIfMatchTypedAny() {
        throw new JSONException("UnsupportedOperation");
    }

    public final Byte readInt8() {
        Integer int32 = readInt32();
        if (int32 == null) {
            return null;
        }
        return Byte.valueOf(int32.byteValue());
    }

    public byte readInt8Value() {
        return (byte) readInt32Value();
    }

    public final Short readInt16() {
        Integer int32 = readInt32();
        if (int32 == null) {
            return null;
        }
        return Short.valueOf(int32.shortValue());
    }

    public short readInt16Value() {
        return (short) readInt32Value();
    }

    protected final int readInt32ValueOverflow() {
        readNumber0();
        return getInt32Value();
    }

    protected final long readInt64ValueOverflow() {
        readNumber0();
        return getInt64Value();
    }

    public final int getInt32Value() {
        switch (this.valueType) {
            case 1:
            case 9:
            case 10:
                if (this.mag1 == 0 && this.mag2 == 0) {
                    if (this.negative) {
                        int i = this.mag3;
                        if (i == Integer.MIN_VALUE) {
                            return i;
                        }
                        if (i >= 0) {
                            return -i;
                        }
                    } else {
                        int i2 = this.mag3;
                        if (i2 >= 0) {
                            return i2;
                        }
                    }
                }
                Number number = getNumber();
                if (number instanceof Long) {
                    long jLongValue = number.longValue();
                    if (jLongValue < -2147483648L || jLongValue > 2147483647L) {
                        throw new JSONException(info("integer overflow " + jLongValue));
                    }
                    return (int) jLongValue;
                }
                if (number instanceof BigInteger) {
                    BigInteger bigInteger = (BigInteger) number;
                    if ((this.context.features & Feature.NonErrorOnNumberOverflow.mask) != 0) {
                        return bigInteger.intValue();
                    }
                    try {
                        return bigInteger.intValueExact();
                    } catch (ArithmeticException unused) {
                        throw numberError();
                    }
                }
                return number.intValue();
            case 2:
                return getNumber().intValue();
            case 3:
                return toInt32(this.stringValue);
            case 4:
                return this.boolValue ? 1 : 0;
            case 5:
                if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) == 0) {
                    return 0;
                }
                throw new JSONException(info("int value not support input null"));
            case 6:
                Number number2 = toNumber((Map) this.complex);
                if (number2 != null) {
                    return number2.intValue();
                }
                return 0;
            case 7:
                return toInt((List) this.complex);
            case 8:
                try {
                    return getBigDecimal().intValueExact();
                } catch (ArithmeticException unused2) {
                    throw numberError();
                }
            case 11:
            case 12:
            case 13:
                long jLongValue2 = getNumber().longValue();
                if ((jLongValue2 < -2147483648L || jLongValue2 > 2147483647L) && (this.context.features & Feature.NonErrorOnNumberOverflow.mask) == 0) {
                    throw new JSONException(info("integer overflow " + jLongValue2));
                }
                return (int) jLongValue2;
            default:
                throw new JSONException("TODO : " + ((int) this.valueType));
        }
    }

    public final long getInt64Value() {
        switch (this.valueType) {
            case 1:
            case 9:
            case 10:
                if (this.mag1 == 0 && this.mag2 == 0) {
                    if (this.negative) {
                        int i = this.mag3;
                        if (i == Integer.MIN_VALUE) {
                            return i;
                        }
                        if (i >= 0) {
                            return -i;
                        }
                    } else {
                        int i2 = this.mag3;
                        if (i2 >= 0) {
                            return i2;
                        }
                    }
                }
                Number number = getNumber();
                if (number instanceof BigInteger) {
                    BigInteger bigInteger = (BigInteger) number;
                    if ((this.context.features & Feature.NonErrorOnNumberOverflow.mask) != 0) {
                        return bigInteger.longValue();
                    }
                    try {
                        return bigInteger.longValueExact();
                    } catch (ArithmeticException unused) {
                        throw numberError();
                    }
                }
                return number.longValue();
            case 2:
            case 11:
            case 12:
            case 13:
                return getNumber().longValue();
            case 3:
                return toInt64(this.stringValue);
            case 4:
                if (this.boolValue) {
                    return MASK_FIELD_BASED;
                }
                return 0L;
            case 5:
                if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) == 0) {
                    return 0L;
                }
                throw new JSONException(info("long value not support input null"));
            case 6:
                return toLong((Map) this.complex);
            case 7:
                return toInt((List) this.complex);
            case 8:
                try {
                    return getBigDecimal().longValueExact();
                } catch (ArithmeticException unused2) {
                    throw numberError();
                }
            default:
                throw new JSONException("TODO : " + ((int) this.valueType));
        }
    }

    public final double getDoubleValue() {
        int i;
        switch (this.valueType) {
            case 1:
            case 9:
            case 10:
                if (this.mag1 == 0 && this.mag2 == 0 && (i = this.mag3) != Integer.MIN_VALUE) {
                    return this.negative ? -i : i;
                }
                Number number = getNumber();
                if (number instanceof BigInteger) {
                    BigInteger bigInteger = (BigInteger) number;
                    if ((this.context.features & Feature.NonErrorOnNumberOverflow.mask) != 0) {
                        return bigInteger.longValue();
                    }
                    try {
                        return bigInteger.longValueExact();
                    } catch (ArithmeticException unused) {
                        throw numberError();
                    }
                }
                return number.doubleValue();
            case 2:
            case 11:
            case 12:
            case 13:
                return getNumber().doubleValue();
            case 3:
                try {
                    return TypeUtils.toDoubleValue(this.stringValue);
                } catch (NumberFormatException e) {
                    throw new JSONException(info(e.getMessage()));
                }
            case 4:
                return this.boolValue ? 1.0d : 0.0d;
            case 5:
                if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) == 0) {
                    return 0.0d;
                }
                throw new JSONException(info("long value not support input null"));
            case 6:
                Map map = (Map) this.complex;
                if (map == null || map.isEmpty()) {
                    this.wasNull = true;
                    return 0.0d;
                }
                return TypeUtils.toDoubleValue(map);
            case 7:
                Collection collection = (Collection) this.complex;
                if (collection == null || collection.isEmpty()) {
                    this.wasNull = true;
                    return 0.0d;
                }
                return TypeUtils.toDoubleValue(this.complex);
            case 8:
                try {
                    return getBigDecimal().doubleValue();
                } catch (ArithmeticException unused2) {
                    throw numberError();
                }
            case 14:
                return Double.NaN;
            default:
                throw new JSONException("TODO : " + ((int) this.valueType));
        }
    }

    public final float getFloatValue() {
        return (float) getDoubleValue();
    }

    public long[] readInt64ValueArray() {
        if (nextIfNull()) {
            return null;
        }
        if (nextIfArrayStart()) {
            long[] jArrCopyOf = new long[8];
            int i = 0;
            while (!nextIfArrayEnd()) {
                if (isEnd()) {
                    throw new JSONException(info("input end"));
                }
                if (i == jArrCopyOf.length) {
                    jArrCopyOf = Arrays.copyOf(jArrCopyOf, jArrCopyOf.length << 1);
                }
                jArrCopyOf[i] = readInt64Value();
                i++;
            }
            return i == jArrCopyOf.length ? jArrCopyOf : Arrays.copyOf(jArrCopyOf, i);
        }
        if (isString()) {
            String string = readString();
            if (string.isEmpty()) {
                return null;
            }
            throw error("not support input ".concat(string));
        }
        throw new JSONException(info("TODO"));
    }

    public Float readFloat() {
        if (nextIfNull()) {
            return null;
        }
        this.wasNull = false;
        float floatValue = readFloatValue();
        if (this.wasNull) {
            return null;
        }
        return Float.valueOf(floatValue);
    }

    public final Double readDouble() {
        if (nextIfNull()) {
            return null;
        }
        this.wasNull = false;
        double doubleValue = readDoubleValue();
        if (this.wasNull) {
            return null;
        }
        return Double.valueOf(doubleValue);
    }

    public Number readNumber() {
        readNumber0();
        return getNumber();
    }

    public BigInteger readBigInteger() {
        readNumber0();
        return getBigInteger();
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.time.LocalDate readLocalDate() {
        /*
            Method dump skipped, instruction units count: 282
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReader.readLocalDate():java.time.LocalDate");
    }

    /* JADX WARN: Type inference failed for: r0v18, types: [java.time.LocalDateTime] */
    /* JADX WARN: Type inference failed for: r0v20, types: [java.time.LocalDateTime] */
    /* JADX WARN: Type inference failed for: r0v23, types: [java.time.LocalDateTime] */
    /* JADX WARN: Type inference failed for: r0v56, types: [java.time.LocalDateTime] */
    public LocalDateTime readLocalDateTime() {
        JSONReader jSONReader;
        if (isInt()) {
            return Instant.ofEpochMilli(readInt64Value()).atZone(this.context.getZoneId()).toLocalDateTime();
        }
        if (isTypeRedirect()) {
            jSONReader = this;
            if (jSONReader.nextIfMatchIdent('\"', 'v', 'a', 'l', '\"')) {
                nextIfMatch(':');
                LocalDateTime localDateTime = readLocalDateTime();
                nextIfObjectEnd();
                setTypeRedirect(false);
                return localDateTime;
            }
        } else {
            jSONReader = this;
        }
        if (jSONReader.context.dateFormat == null || jSONReader.context.formatyyyyMMddhhmmss19 || jSONReader.context.formatyyyyMMddhhmmssT19 || jSONReader.context.formatyyyyMMdd8 || jSONReader.context.formatISO8601) {
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
                    LocalDateTime localDateTime17 = readLocalDateTime17();
                    if (localDateTime17 != null) {
                        return localDateTime17;
                    }
                    break;
                case 18:
                    LocalDateTime localDateTime18 = readLocalDateTime18();
                    if (localDateTime18 != null) {
                        return localDateTime18;
                    }
                    break;
                case 19:
                    LocalDateTime localDateTime19 = readLocalDateTime19();
                    if (localDateTime19 != null) {
                        return localDateTime19;
                    }
                    break;
                case 20:
                    LocalDateTime localDateTime20 = readLocalDateTime20();
                    if (localDateTime20 != null) {
                        return localDateTime20;
                    }
                    ZonedDateTime zonedDateTimeX = readZonedDateTimeX(stringLength);
                    if (zonedDateTimeX != null) {
                        return zonedDateTimeX.toLocalDateTime();
                    }
                    break;
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
                    ZonedDateTime zonedDateTimeX2 = readZonedDateTimeX(stringLength);
                    if (zonedDateTimeX2 != null) {
                        ZoneId zoneId = jSONReader.context.getZoneId();
                        if (!zonedDateTimeX2.getZone().equals(zoneId)) {
                            return zonedDateTimeX2.toInstant().atZone(zoneId).toLocalDateTime();
                        }
                        return zonedDateTimeX2.toLocalDateTime();
                    }
                    break;
            }
        }
        String string = readString();
        if (string.isEmpty() || "null".equals(string)) {
            jSONReader.wasNull = true;
            return null;
        }
        DateTimeFormatter dateFormatter = jSONReader.context.getDateFormatter();
        if (dateFormatter != null) {
            if (!jSONReader.context.formatHasHour) {
                return LocalDateTime.of(LocalDate.parse(string, dateFormatter), LocalTime.MIN);
            }
            return LocalDateTime.parse(string, dateFormatter);
        }
        if (IOUtils.isNumber(string)) {
            long j = Long.parseLong(string);
            if (jSONReader.context.formatUnixTime) {
                j *= 1000;
            }
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(j), jSONReader.context.getZoneId());
        }
        if (string.startsWith("/Date(") && string.endsWith(")/")) {
            String strSubstring = string.substring(6, string.length() - 2);
            int iIndexOf = strSubstring.indexOf(43);
            if (iIndexOf == -1) {
                iIndexOf = strSubstring.indexOf(45);
            }
            if (iIndexOf != -1) {
                strSubstring = strSubstring.substring(0, iIndexOf);
            }
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(strSubstring)), jSONReader.context.getZoneId());
        }
        if ("0000-00-00 00:00:00".equals(string)) {
            jSONReader.wasNull = true;
            return null;
        }
        throw new JSONException(info("read LocalDateTime error " + string));
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00a6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.time.ZonedDateTime readZonedDateTime() {
        /*
            Method dump skipped, instruction units count: 340
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReader.readZonedDateTime():java.time.ZonedDateTime");
    }

    public Calendar readCalendar() {
        if (isString()) {
            long millisFromString = readMillisFromString();
            if (millisFromString == 0 && this.wasNull) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millisFromString);
            return calendar;
        }
        if (readIfNull()) {
            return null;
        }
        long int64Value = readInt64Value();
        if (this.context.formatUnixTime) {
            int64Value *= 1000;
        }
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(int64Value);
        return calendar2;
    }

    public Date readDate() {
        JSONReader jSONReader;
        long millisFromString;
        if (isInt()) {
            return new Date(readInt64Value());
        }
        if (readIfNull() || nextIfNullOrEmptyString()) {
            return null;
        }
        if (current() == 'n') {
            return readNullOrNewDate();
        }
        if (isTypeRedirect()) {
            jSONReader = this;
            if (jSONReader.nextIfMatchIdent('\"', 'v', 'a', 'l', '\"')) {
                nextIfMatch(':');
                millisFromString = readInt64Value();
                nextIfObjectEnd();
                setTypeRedirect(false);
            }
            if (millisFromString == 0 || !jSONReader.wasNull) {
                return new Date(millisFromString);
            }
            return null;
        }
        jSONReader = this;
        if (isObject()) {
            JSONObject jSONObject = readJSONObject();
            Object obj = jSONObject.get("$date");
            if (obj instanceof String) {
                return DateUtils.parseDate((String) obj, jSONReader.context.getZoneId());
            }
            return TypeUtils.toDate(jSONObject);
        }
        millisFromString = readMillisFromString();
        if (millisFromString == 0) {
        }
        return new Date(millisFromString);
    }

    public LocalTime readLocalTime() {
        if (nextIfNull()) {
            return null;
        }
        if (isInt()) {
            return Instant.ofEpochMilli(readInt64Value()).atZone(this.context.getZoneId()).toLocalTime();
        }
        switch (getStringLength()) {
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
            case 13:
            case 14:
            case 16:
            case 17:
            default:
                String string = readString();
                if (string.isEmpty() || "null".equals(string)) {
                    return null;
                }
                if (IOUtils.isNumber(string)) {
                    return Instant.ofEpochMilli(Long.parseLong(string)).atZone(this.context.getZoneId()).toLocalTime();
                }
                throw new JSONException("not support len : " + string);
            case 15:
                return readLocalTime15();
            case 18:
                return readLocalTime18();
            case 19:
                return readLocalDateTime19().toLocalTime();
            case 20:
                return readLocalDateTime20().toLocalTime();
        }
    }

    public Instant readInstant() {
        if (nextIfNull()) {
            return null;
        }
        if (isNumber()) {
            long int64Value = readInt64Value();
            if (this.context.formatUnixTime) {
                int64Value *= 1000;
            }
            return Instant.ofEpochMilli(int64Value);
        }
        if (isObject()) {
            return (Instant) getObjectReader(Instant.class).createInstance(readObject(), 0L);
        }
        ZonedDateTime zonedDateTime = readZonedDateTime();
        if (zonedDateTime == null) {
            return null;
        }
        return Instant.ofEpochSecond(zonedDateTime.toEpochSecond(), zonedDateTime.toLocalTime().getNano());
    }

    public final long readMillisFromString() {
        LocalDateTime localDateTimeOf;
        this.wasNull = false;
        String str = this.context.dateFormat;
        if (str == null || this.context.formatyyyyMMddhhmmss19 || this.context.formatyyyyMMddhhmmssT19 || this.context.formatyyyyMMdd8 || this.context.formatISO8601) {
            int stringLength = getStringLength();
            ZonedDateTime zonedDateTime = null;
            switch (stringLength) {
                case 8:
                    LocalDate localDate8 = readLocalDate8();
                    if (localDate8 == null) {
                        throw new JSONException("TODO : " + readString());
                    }
                    localDateTimeOf = LocalDateTime.of(localDate8, LocalTime.MIN);
                    break;
                    break;
                case 9:
                    LocalDate localDate9 = readLocalDate9();
                    localDateTimeOf = localDate9 == null ? null : LocalDateTime.of(localDate9, LocalTime.MIN);
                    break;
                case 10:
                    LocalDate localDate10 = readLocalDate10();
                    if (localDate10 == null) {
                        String string = readString();
                        if ("0000-00-00".equals(string)) {
                            this.wasNull = true;
                            return 0L;
                        }
                        if (IOUtils.isNumber(string)) {
                            return Long.parseLong(string);
                        }
                        throw new JSONException("TODO : " + string);
                    }
                    localDateTimeOf = LocalDateTime.of(localDate10, LocalTime.MIN);
                    break;
                    break;
                case 11:
                    LocalDate localDate11 = readLocalDate11();
                    localDateTimeOf = localDate11 == null ? null : LocalDateTime.of(localDate11, LocalTime.MIN);
                    break;
                case 12:
                    localDateTimeOf = readLocalDateTime12();
                    break;
                case 13:
                case 15:
                default:
                    localDateTimeOf = null;
                    break;
                case 14:
                    localDateTimeOf = readLocalDateTime14();
                    break;
                case 16:
                    localDateTimeOf = readLocalDateTime16();
                    break;
                case 17:
                    localDateTimeOf = readLocalDateTime17();
                    break;
                case 18:
                    localDateTimeOf = readLocalDateTime18();
                    break;
                case 19:
                    long millis19 = readMillis19();
                    if (millis19 != 0 || !this.wasNull) {
                        return millis19;
                    }
                    localDateTimeOf = readLocalDateTime19();
                    break;
                    break;
                case 20:
                    localDateTimeOf = readLocalDateTime20();
                    break;
            }
            if (localDateTimeOf != null) {
                zonedDateTime = ZonedDateTime.ofLocal(localDateTimeOf, this.context.getZoneId(), null);
            } else if (stringLength >= 20) {
                ZonedDateTime zonedDateTimeX = readZonedDateTimeX(stringLength);
                zonedDateTime = (zonedDateTimeX != null || stringLength < 32 || stringLength > 35) ? zonedDateTimeX : DateUtils.parseZonedDateTime(readString(), null);
            }
            if (zonedDateTime != null) {
                long epochSecond = zonedDateTime.toEpochSecond();
                int nano = zonedDateTime.toLocalTime().getNano();
                if (epochSecond < 0 && nano > 0) {
                    return ((epochSecond + MASK_FIELD_BASED) * 1000) + ((long) ((nano / DurationKt.NANOS_IN_MILLIS) + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED));
                }
                return (epochSecond * 1000) + ((long) (nano / DurationKt.NANOS_IN_MILLIS));
            }
        }
        String string2 = readString();
        if (string2.isEmpty() || "null".equals(string2)) {
            this.wasNull = true;
            return 0L;
        }
        if (this.context.formatMillis || this.context.formatUnixTime) {
            long j = Long.parseLong(string2);
            return this.context.formatUnixTime ? j * 1000 : j;
        }
        if (str != null && !str.isEmpty()) {
            if ("yyyy-MM-dd HH:mm:ss".equals(str)) {
                if ((string2.length() < 4 || string2.charAt(4) != '-') && IOUtils.isNumber(string2)) {
                    return Long.parseLong(string2);
                }
                return DateUtils.parseMillis19(string2, this.context.getZoneId());
            }
            if ("yyyy-MM-dd HH:mm:ss.SSS".equals(str) && string2.length() == 19 && string2.charAt(4) == '-' && string2.charAt(7) == '-' && string2.charAt(10) == ' ' && string2.charAt(13) == ':' && string2.charAt(16) == ':') {
                return DateUtils.parseMillis19(string2, this.context.getZoneId());
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
            try {
                return simpleDateFormat.parse(string2).getTime();
            } catch (ParseException unused) {
                throw new JSONException("parse date error, " + string2 + ", expect format " + simpleDateFormat.toPattern());
            }
        }
        if ("0000-00-00T00:00:00".equals(string2) || "0001-01-01T00:00:00+08:00".equals(string2)) {
            return 0L;
        }
        if (string2.startsWith("/Date(") && string2.endsWith(")/")) {
            String strSubstring = string2.substring(6, string2.length() - 2);
            int iIndexOf = strSubstring.indexOf(43);
            if (iIndexOf == -1) {
                iIndexOf = strSubstring.indexOf(45);
            }
            if (iIndexOf != -1) {
                strSubstring = strSubstring.substring(0, iIndexOf);
            }
            return Long.parseLong(strSubstring);
        }
        if (IOUtils.isNumber(string2)) {
            return Long.parseLong(string2);
        }
        throw new JSONException(info("format " + str + " not support, input " + string2));
    }

    public void readNumber(ValueConsumer valueConsumer, boolean z) {
        readNumber0();
        valueConsumer.accept(getNumber());
    }

    public void readString(ValueConsumer valueConsumer, boolean z) {
        String string = readString();
        if (z) {
            valueConsumer.accept(JSON.toJSONString(string));
        } else {
            valueConsumer.accept(string);
        }
    }

    public byte[] readBase64() {
        String string = readString();
        if (string != null && string.startsWith("data:image/jpeg;base64,")) {
            string = string.substring("data:image/jpeg;base64,".length());
        }
        if (string.isEmpty()) {
            return new byte[0];
        }
        return Base64.getDecoder().decode(string);
    }

    public String[] readStringArray() {
        String[] strArr = null;
        if (this.ch == 'n' && nextIfNull()) {
            return null;
        }
        if (nextIfArrayStart()) {
            int i = 0;
            while (!nextIfArrayEnd()) {
                if (isEnd()) {
                    throw new JSONException(info("input end"));
                }
                if (strArr == null) {
                    strArr = new String[16];
                } else if (i == strArr.length) {
                    strArr = (String[]) Arrays.copyOf(strArr, strArr.length << 1);
                }
                strArr[i] = readString();
                i++;
            }
            if (strArr == null) {
                strArr = new String[0];
            }
            return strArr.length == i ? strArr : (String[]) Arrays.copyOf(strArr, i);
        }
        char c = this.ch;
        if (c == '\"' || c == '\'') {
            String string = readString();
            if (string.isEmpty()) {
                return null;
            }
            throw new JSONException(info("not support input " + string));
        }
        throw new JSONException(info("not support input"));
    }

    public char readCharValue() {
        String string = readString();
        if (string == null || string.isEmpty()) {
            this.wasNull = true;
            return (char) 0;
        }
        return string.charAt(0);
    }

    public Character readCharacter() {
        String string = readString();
        if (string == null || string.isEmpty()) {
            this.wasNull = true;
            return (char) 0;
        }
        return Character.valueOf(string.charAt(0));
    }

    protected double readNaN() {
        throw new JSONException("not support");
    }

    public boolean wasNull() {
        return this.wasNull;
    }

    public <T> T read(Type type) {
        return (T) this.context.provider.getObjectReader(type, (this.context.features & Feature.FieldBased.mask) != 0).readObject(this, null, null, 0L);
    }

    public final void read(List list) {
        if (!nextIfArrayStart()) {
            throw new JSONException("illegal input, offset " + this.offset + ", char " + this.ch);
        }
        int i = this.level + 1;
        this.level = i;
        if (i >= this.context.maxLevel) {
            throw new JSONException("level too large : " + this.level);
        }
        while (!nextIfArrayEnd()) {
            list.add(ObjectReaderImplObject.INSTANCE.readObject(this, null, null, 0L));
            nextIfComma();
        }
        this.level--;
        nextIfComma();
    }

    public final void read(Collection collection) {
        if (!nextIfArrayStart()) {
            throw new JSONException("illegal input, offset " + this.offset + ", char " + this.ch);
        }
        int i = this.level + 1;
        this.level = i;
        if (i >= this.context.maxLevel) {
            throw new JSONException("level too large : " + this.level);
        }
        while (!nextIfArrayEnd()) {
            collection.add(readAny());
            nextIfComma();
        }
        this.level--;
        nextIfComma();
    }

    public final void readObject(Object obj, Feature... featureArr) {
        long j = 0;
        for (Feature feature : featureArr) {
            j |= feature.mask;
        }
        readObject(obj, j);
    }

    public final void readObject(Object obj, long j) {
        if (obj == null) {
            throw new JSONException("object is null");
        }
        ObjectReader objectReader = this.context.provider.getObjectReader(obj.getClass(), ((this.context.features | j) & Feature.FieldBased.mask) != 0);
        if (objectReader instanceof ObjectReaderBean) {
            ((ObjectReaderBean) objectReader).readObject(this, obj, j);
        } else {
            if (obj instanceof Map) {
                read((Map) obj, j);
                return;
            }
            throw new JSONException("read object not support");
        }
    }

    public void read(Map map, ObjectReader objectReader, long j) {
        Object objPut;
        nextIfObjectStart();
        if (map instanceof Wrapper) {
            map = (Map) ((Wrapper) map).unwrap(Map.class);
        }
        long features = this.context.getFeatures() | j;
        int i = 0;
        while (true) {
            if (this.ch == '/') {
                skipComment();
            }
            if (!nextIfObjectEnd()) {
                if (i != 0 && !this.comma) {
                    throw new JSONException(info());
                }
                String fieldName = readFieldName();
                ObjectReader objectReader2 = objectReader;
                long j2 = j;
                Object object = objectReader2.readObject(this, objectReader.getObjectClass(), fieldName, j2);
                if ((object != null || (Feature.IgnoreNullPropertyValue.mask & features) == 0) && (objPut = map.put(fieldName, object)) != null && (Feature.DuplicateKeyValueAsArray.mask & features) != 0) {
                    if (objPut instanceof Collection) {
                        ((Collection) objPut).add(object);
                        map.put(fieldName, objPut);
                    } else {
                        map.put(fieldName, JSONArray.of(objPut, object));
                    }
                }
                i++;
                objectReader = objectReader2;
                j = j2;
            } else {
                nextIfComma();
                return;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0256  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0261  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x026b  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x029e  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x02a8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void read(java.util.Map r19, long r20) {
        /*
            Method dump skipped, instruction units count: 772
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReader.read(java.util.Map, long):void");
    }

    public final void read(Map map, Type type, Type type2, long j) {
        Object object;
        ObjectReader objectReader;
        Object objPut;
        if (!nextIfObjectStart()) {
            throw new JSONException("illegal input， offset " + this.offset + ", char " + this.ch);
        }
        ObjectReader objectReader2 = this.context.getObjectReader(type);
        ObjectReader objectReader3 = this.context.getObjectReader(type2);
        long features = j | this.context.getFeatures();
        int i = 0;
        while (true) {
            if (this.ch == '/') {
                skipComment();
            }
            if (!nextIfObjectEnd()) {
                if (i != 0 && !this.comma) {
                    throw new JSONException(info());
                }
                if (type == String.class) {
                    object = readFieldName();
                    objectReader = objectReader2;
                } else {
                    object = objectReader2.readObject(this, null, null, 0L);
                    objectReader = objectReader2;
                    nextIfMatch(':');
                }
                Object obj = object;
                ObjectReader objectReader4 = objectReader3;
                Object object2 = objectReader4.readObject(this, null, null, 0L);
                if ((object2 != null || (Feature.IgnoreNullPropertyValue.mask & features) == 0) && (objPut = map.put(obj, object2)) != null && (Feature.DuplicateKeyValueAsArray.mask & features) != 0) {
                    if (objPut instanceof Collection) {
                        ((Collection) objPut).add(object2);
                        map.put(obj, objPut);
                    } else {
                        map.put(obj, JSONArray.of(objPut, object2));
                    }
                }
                i++;
                objectReader3 = objectReader4;
                objectReader2 = objectReader;
            } else {
                nextIfComma();
                return;
            }
        }
    }

    public <T> T read(Class<T> cls) {
        return (T) this.context.provider.getObjectReader(cls, (this.context.features & Feature.FieldBased.mask) != 0).readObject(this, null, null, 0L);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:79:0x017e  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01a1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.Map<java.lang.String, java.lang.Object> readObject() {
        /*
            Method dump skipped, instruction units count: 556
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReader.readObject():java.util.Map");
    }

    public Boolean readBool() {
        if (nextIfNull()) {
            return null;
        }
        this.wasNull = false;
        boolean boolValue = readBoolValue();
        if (boolValue || !this.wasNull) {
            return Boolean.valueOf(boolValue);
        }
        return null;
    }

    public Object readAny() {
        return read(Object.class);
    }

    /* JADX WARN: Type update failed for variable: r12v0 'this'  ??, new type: com.alibaba.fastjson2.JSONReader
    jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 2141. Try increasing type updates limit count.
    	at jadx.core.dex.visitors.typeinference.TypeUpdateInfo.requestUpdate(TypeUpdateInfo.java:37)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:224)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:121)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.ifListener(TypeUpdate.java:640)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyInvokeTypes(TypeUpdate.java:381)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.invokeListener(TypeUpdate.java:364)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:86)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:58)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
     */
    /* JADX WARN: Type update failed for variable: r12v0 'this'  ??, new type: com.alibaba.fastjson2.JSONReader
    jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 2141. Try increasing type updates limit count.
    	at jadx.core.dex.visitors.typeinference.TypeUpdateInfo.requestUpdate(TypeUpdateInfo.java:37)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:224)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:86)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:72)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setImmutableType(TypeInferenceVisitor.java:111)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:102)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:102)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
     */
    public List readArray(Type type) {
        JSONReader jSONReader;
        JSONReader jSONReader2;
        Object object;
        char c;
        if (nextIfNull()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        char c2 = this.ch;
        if (c2 == '[') {
            next();
            ObjectReader objectReader = this.context.provider.getObjectReader(type, (this.context.features & Feature.FieldBased.mask) != 0);
            int i = 0;
            while (!nextIfArrayEnd()) {
                int i2 = this.offset;
                if (isReference()) {
                    String reference = readReference();
                    if ("..".equals(reference)) {
                        object = arrayList;
                    } else {
                        addResolveTask(arrayList, i, JSONPath.of(reference));
                        object = null;
                    }
                    jSONReader2 = this;
                } else {
                    jSONReader2 = this;
                    object = objectReader.readObject(jSONReader2, null, null, 0L);
                }
                arrayList.add(object);
                if (i2 == jSONReader2.offset || (c = jSONReader2.ch) == '}' || c == 26) {
                    throw new JSONException("illegal input : " + jSONReader2.ch + ", offset " + getOffset());
                }
                i++;
            }
            jSONReader = this;
        } else {
            jSONReader = this;
            if (c2 == '\"' || c2 == '\'' || c2 == '{') {
                String string = readString();
                if (string != null && !string.isEmpty()) {
                    arrayList.add(string);
                }
            } else {
                throw new JSONException(info("syntax error"));
            }
        }
        boolean z = jSONReader.ch == ',';
        jSONReader.comma = z;
        if (z) {
            next();
        }
        return arrayList;
    }

    public List readList(Type[] typeArr) {
        char c;
        if (nextIfNull()) {
            return null;
        }
        if (!nextIfArrayStart()) {
            throw new JSONException("syntax error : " + this.ch);
        }
        int length = typeArr.length;
        ArrayList arrayList = new ArrayList(length);
        int i = 0;
        while (!nextIfArrayEnd() && i < length) {
            int i2 = this.offset;
            int i3 = i + 1;
            Object obj = read(typeArr[i]);
            if (i2 != this.offset && (c = this.ch) != '}' && c != 26) {
                arrayList.add(obj);
                i = i3;
            } else {
                throw new JSONException("illegal input : " + this.ch + ", offset " + getOffset());
            }
        }
        if (i != length) {
            throw new JSONException(info("element length mismatch"));
        }
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return arrayList;
    }

    public final Object[] readArray(Type[] typeArr) {
        char c;
        if (nextIfNull()) {
            return null;
        }
        if (!nextIfArrayStart()) {
            throw new JSONException(info("syntax error"));
        }
        int length = typeArr.length;
        Object[] objArr = new Object[length];
        int i = 0;
        while (!nextIfArrayEnd() && i < length) {
            int i2 = this.offset;
            Object obj = read(typeArr[i]);
            if (i2 != this.offset && (c = this.ch) != '}' && c != 26) {
                objArr[i] = obj;
                i++;
            } else {
                throw new JSONException("illegal input : " + this.ch + ", offset " + getOffset());
            }
        }
        if (i != length) {
            throw new JSONException(info("element length mismatch"));
        }
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
        return objArr;
    }

    public final void readArray(List list, Type type) {
        readArray((Collection) list, type);
    }

    public void readArray(Collection collection, Type type) {
        if (nextIfArrayStart()) {
            while (!nextIfArrayEnd()) {
                collection.add(read(type));
            }
            return;
        }
        if (isString()) {
            String string = readString();
            if (type == String.class) {
                collection.add(string);
            } else {
                Function typeConvert = this.context.getProvider().getTypeConvert(String.class, type);
                if (typeConvert == null) {
                    throw new JSONException(info("not support input " + string));
                }
                if (string.indexOf(44) != -1) {
                    for (String str : string.split(",")) {
                        collection.add(typeConvert.apply(str));
                    }
                } else {
                    collection.add(typeConvert.apply(string));
                }
            }
        } else {
            collection.add(read(type));
        }
        boolean z = this.ch == ',';
        this.comma = z;
        if (z) {
            next();
        }
    }

    public final JSONArray readJSONArray() {
        JSONArray jSONArray = new JSONArray();
        read((List) jSONArray);
        return jSONArray;
    }

    public final JSONObject readJSONObject() {
        JSONObject jSONObject = new JSONObject();
        read(jSONObject, 0L);
        return jSONObject;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0028  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00fb  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.List readArray() {
        /*
            Method dump skipped, instruction units count: 392
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReader.readArray():java.util.List");
    }

    private void add(List<Object> list, int i, Object obj) {
        if (obj instanceof JSONPath) {
            addResolveTask(list, i, (JSONPath) obj);
            list.add(null);
        } else {
            list.add(obj);
        }
    }

    public final BigInteger getBigInteger() {
        Number number = getNumber();
        if (number == null) {
            return null;
        }
        if (number instanceof BigInteger) {
            return (BigInteger) number;
        }
        return BigInteger.valueOf(number.longValue());
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x011a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.math.BigDecimal getBigDecimal() {
        /*
            Method dump skipped, instruction units count: 462
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReader.getBigDecimal():java.math.BigDecimal");
    }

    /* JADX WARN: Removed duplicated region for block: B:108:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0204  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0240  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x029c  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Number getNumber() {
        /*
            Method dump skipped, instruction units count: 988
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReader.getNumber():java.lang.Number");
    }

    protected final int toInt32(String str) {
        if (IOUtils.isNumber(str) || str.lastIndexOf(44) == str.length() - 4) {
            return TypeUtils.toIntValue(str);
        }
        throw error("parseInt error, value : " + str);
    }

    protected final long toInt64(String str) {
        if (IOUtils.isNumber(str) || str.lastIndexOf(44) == str.length() - 4) {
            return TypeUtils.toLongValue(str);
        }
        if (str.length() > 10 && str.length() < 40) {
            try {
                return DateUtils.parseMillis(str, this.context.zoneId);
            } catch (JSONException | NullPointerException | DateTimeException unused) {
            }
        }
        throw error("parseLong error, value : " + str);
    }

    protected final long toLong(Map map) {
        if (map.get("val") instanceof Number) {
            return ((Number) r0).intValue();
        }
        throw error("parseLong error, value : " + map);
    }

    protected final int toInt(List list) {
        if (list.size() == 1) {
            Object obj = list.get(0);
            if (obj instanceof Number) {
                return ((Number) obj).intValue();
            }
            if (obj instanceof String) {
                return Integer.parseInt((String) obj);
            }
        }
        throw error("parseLong error, field : value " + list);
    }

    protected final Number toNumber(Map map) {
        Object obj = map.get("val");
        if (obj instanceof Number) {
            return (Number) obj;
        }
        return null;
    }

    protected final BigDecimal decimal(JSONObject jSONObject) {
        BigDecimal bigDecimal = jSONObject.getBigDecimal("value");
        if (bigDecimal == null) {
            bigDecimal = jSONObject.getBigDecimal("$numberDecimal");
        }
        if (bigDecimal != null) {
            return bigDecimal;
        }
        throw error("can not cast to decimal " + jSONObject);
    }

    protected final Number toNumber(List list) {
        if (list.size() != 1) {
            return null;
        }
        Object obj = list.get(0);
        if (obj instanceof Number) {
            return (Number) obj;
        }
        if (obj instanceof String) {
            return TypeUtils.toBigDecimal((String) obj);
        }
        return null;
    }

    protected final String toString(List list) {
        JSONWriter jSONWriterOf = JSONWriter.of();
        jSONWriterOf.setRootObject(list);
        jSONWriterOf.write(list);
        return jSONWriterOf.toString();
    }

    protected final String toString(Map map) {
        JSONWriter jSONWriterOf = JSONWriter.of();
        jSONWriterOf.setRootObject(map);
        jSONWriterOf.write((Map<?, ?>) map);
        return jSONWriterOf.toString();
    }

    public static JSONReader of(byte[] bArr) {
        return of(bArr, 0, bArr.length, StandardCharsets.UTF_8, JSONFactory.createReadContext());
    }

    @Deprecated
    public static JSONReader of(Context context, byte[] bArr) {
        return JSONReaderUTF8.of(bArr, 0, bArr.length, context);
    }

    public static JSONReader of(byte[] bArr, Context context) {
        return JSONReaderUTF8.of(bArr, 0, bArr.length, context);
    }

    public static JSONReader of(char[] cArr) {
        return ofUTF16(null, cArr, 0, cArr.length, JSONFactory.createReadContext());
    }

    @Deprecated
    public static JSONReader of(Context context, char[] cArr) {
        return ofUTF16(null, cArr, 0, cArr.length, context);
    }

    public static JSONReader of(char[] cArr, Context context) {
        return ofUTF16(null, cArr, 0, cArr.length, context);
    }

    public static JSONReader ofJSONB(byte[] bArr) {
        return new JSONReaderJSONB(JSONFactory.createReadContext(), bArr, 0, bArr.length);
    }

    @Deprecated
    public static JSONReader ofJSONB(Context context, byte[] bArr) {
        return new JSONReaderJSONB(context, bArr, 0, bArr.length);
    }

    public static JSONReader ofJSONB(byte[] bArr, Context context) {
        return new JSONReaderJSONB(context, bArr, 0, bArr.length);
    }

    public static JSONReader ofJSONB(InputStream inputStream, Context context) {
        return new JSONReaderJSONB(context, inputStream);
    }

    public static JSONReader ofJSONB(byte[] bArr, Feature... featureArr) {
        Context contextCreateReadContext = JSONFactory.createReadContext();
        contextCreateReadContext.config(featureArr);
        return new JSONReaderJSONB(contextCreateReadContext, bArr, 0, bArr.length);
    }

    public static JSONReader ofJSONB(byte[] bArr, int i, int i2) {
        return new JSONReaderJSONB(JSONFactory.createReadContext(), bArr, i, i2);
    }

    public static JSONReader ofJSONB(byte[] bArr, int i, int i2, Context context) {
        return new JSONReaderJSONB(context, bArr, i, i2);
    }

    public static JSONReader ofJSONB(byte[] bArr, int i, int i2, SymbolTable symbolTable) {
        return new JSONReaderJSONB(JSONFactory.createReadContext(symbolTable), bArr, i, i2);
    }

    public static JSONReader of(byte[] bArr, int i, int i2, Charset charset) {
        Context contextCreateReadContext = JSONFactory.createReadContext();
        if (charset == StandardCharsets.UTF_8) {
            return JSONReaderUTF8.of(bArr, i, i2, contextCreateReadContext);
        }
        if (charset == StandardCharsets.UTF_16) {
            return ofUTF16(bArr, i, i2, contextCreateReadContext);
        }
        if (charset == StandardCharsets.US_ASCII || charset == StandardCharsets.ISO_8859_1) {
            return JSONReaderASCII.of(contextCreateReadContext, (String) null, bArr, i, i2);
        }
        throw new JSONException("not support charset " + charset);
    }

    private static JSONReader ofUTF16(byte[] bArr, int i, int i2, Context context) {
        return new JSONReaderUTF16(context, bArr, i, i2);
    }

    private static JSONReader ofUTF16(String str, char[] cArr, int i, int i2, Context context) {
        return new JSONReaderUTF16(context, str, cArr, i, i2);
    }

    public static JSONReader of(byte[] bArr, int i, int i2, Charset charset, Context context) {
        if (charset == StandardCharsets.UTF_8) {
            return JSONReaderUTF8.of(bArr, i, i2, context);
        }
        if (charset == StandardCharsets.UTF_16) {
            return ofUTF16(bArr, i, i2, context);
        }
        if (charset == StandardCharsets.US_ASCII || charset == StandardCharsets.ISO_8859_1) {
            return JSONReaderASCII.of(context, (String) null, bArr, i, i2);
        }
        throw new JSONException("not support charset " + charset);
    }

    public static JSONReader of(byte[] bArr, int i, int i2) {
        return of(bArr, i, i2, StandardCharsets.UTF_8, JSONFactory.createReadContext());
    }

    public static JSONReader of(byte[] bArr, int i, int i2, Context context) {
        return new JSONReaderUTF8(context, bArr, i, i2);
    }

    public static JSONReader of(char[] cArr, int i, int i2) {
        return ofUTF16(null, cArr, i, i2, JSONFactory.createReadContext());
    }

    public static JSONReader of(char[] cArr, int i, int i2, Context context) {
        return ofUTF16(null, cArr, i, i2, context);
    }

    public static JSONReader of(URL url, Context context) throws IOException {
        InputStream inputStreamOpenStream = url.openStream();
        try {
            JSONReader jSONReaderOf = of(inputStreamOpenStream, StandardCharsets.UTF_8, context);
            if (inputStreamOpenStream != null) {
                inputStreamOpenStream.close();
            }
            return jSONReaderOf;
        } catch (Throwable th) {
            if (inputStreamOpenStream != null) {
                try {
                    inputStreamOpenStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static JSONReader of(InputStream inputStream, Charset charset) {
        return of(inputStream, charset, JSONFactory.createReadContext());
    }

    public static JSONReader of(InputStream inputStream, Charset charset, Context context) {
        if (inputStream == null) {
            throw new JSONException("inputStream is null");
        }
        if (charset == StandardCharsets.UTF_8 || charset == null) {
            return new JSONReaderUTF8(context, inputStream);
        }
        if (charset == StandardCharsets.UTF_16) {
            return new JSONReaderUTF16(context, inputStream);
        }
        if (charset == StandardCharsets.US_ASCII) {
            return JSONReaderASCII.of(context, inputStream);
        }
        return of(new InputStreamReader(inputStream, charset), context);
    }

    public static JSONReader of(Reader reader) {
        return new JSONReaderUTF16(JSONFactory.createReadContext(), reader);
    }

    public static JSONReader of(Reader reader, Context context) {
        return new JSONReaderUTF16(context, reader);
    }

    public static JSONReader of(ByteBuffer byteBuffer, Charset charset) {
        Context contextCreateReadContext = JSONFactory.createReadContext();
        if (charset == StandardCharsets.UTF_8 || charset == null) {
            return new JSONReaderUTF8(contextCreateReadContext, byteBuffer);
        }
        throw new JSONException("not support charset " + charset);
    }

    public static JSONReader of(ByteBuffer byteBuffer, Charset charset, Context context) {
        if (charset == StandardCharsets.UTF_8 || charset == null) {
            return new JSONReaderUTF8(context, byteBuffer);
        }
        throw new JSONException("not support charset " + charset);
    }

    @Deprecated
    public static JSONReader of(Context context, String str) {
        return of(str, context);
    }

    public static JSONReader of(String str) {
        return of(str, JSONFactory.createReadContext());
    }

    public static JSONReader of(String str, Context context) {
        char[] charArray;
        if (str == null || context == null) {
            throw null;
        }
        if (JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER != null) {
            try {
                if (JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
                    byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
                    return JSONReaderASCII.of(context, str, bArrApply, 0, bArrApply.length);
                }
            } catch (Exception unused) {
                throw new JSONException("unsafe get String.coder error");
            }
        }
        int length = str.length();
        if (JDKUtils.JVM_VERSION == 8) {
            charArray = JDKUtils.getCharArray(str);
        } else {
            charArray = str.toCharArray();
        }
        return ofUTF16(str, charArray, 0, length, context);
    }

    public static JSONReader of(String str, int i, int i2) {
        return of(str, i, i2, JSONFactory.createReadContext());
    }

    public static JSONReader of(String str, int i, int i2, Context context) {
        char[] charArray;
        if (str == null || context == null) {
            throw null;
        }
        if (JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER != null) {
            try {
                if (JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
                    return JSONReaderASCII.of(context, str, JDKUtils.STRING_VALUE.apply(str), i, i2);
                }
            } catch (Exception unused) {
                throw new JSONException("unsafe get String.coder error");
            }
        }
        if (JDKUtils.JVM_VERSION == 8) {
            charArray = JDKUtils.getCharArray(str);
        } else {
            charArray = str.toCharArray();
        }
        return ofUTF16(str, charArray, i, i2, context);
    }

    final void bigInt(char[] cArr, int i, int i2) {
        int i3;
        long j;
        char c;
        long j2;
        int i4 = i2 - i;
        if (this.scale > 0) {
            i4--;
        }
        if (i4 > 38) {
            throw new JSONException("number too large : ".concat(new String(cArr, i, i4)));
        }
        int i5 = i4 % 9;
        int i6 = i + (i5 != 0 ? i5 : 9);
        int i7 = i + 1;
        char c2 = cArr[i];
        if (c2 == '.') {
            c2 = cArr[i7];
            i7 = i + 2;
            i3 = i6 + 1;
        } else {
            i3 = i6;
        }
        int i8 = c2 - '0';
        while (i7 < i6) {
            char c3 = cArr[i7];
            if (c3 == '.') {
                i7++;
                c3 = cArr[i7];
                i3++;
                if (i6 < i2) {
                    i6++;
                }
            }
            i8 = (i8 * 10) + (c3 - '0');
            i7++;
        }
        this.mag3 = i8;
        while (i3 < i2) {
            int i9 = i3 + 9;
            int i10 = i3 + 1;
            char c4 = cArr[i3];
            if (c4 == '.') {
                int i11 = i3 + 2;
                c4 = cArr[i10];
                i3 += 10;
                i10 = i11;
                i9 = i3;
            } else {
                i3 = i9;
            }
            int i12 = c4 - '0';
            while (i10 < i9) {
                char c5 = cArr[i10];
                if (c5 == '.') {
                    i10++;
                    c5 = cArr[i10];
                    i3++;
                    i9++;
                }
                i12 = (i12 * 10) + (c5 - '0');
                i10++;
            }
            long j3 = 0;
            for (int i13 = 3; i13 >= 0; i13--) {
                if (i13 == 0) {
                    c = TokenParser.SP;
                    j2 = j3 + (1000000000 * (((long) this.mag0) & 4294967295L));
                    this.mag0 = (int) j2;
                } else if (i13 == 1) {
                    c = TokenParser.SP;
                    j2 = j3 + (1000000000 * (((long) this.mag1) & 4294967295L));
                    this.mag1 = (int) j2;
                } else if (i13 == 2) {
                    c = TokenParser.SP;
                    j2 = j3 + (1000000000 * (((long) this.mag2) & 4294967295L));
                    this.mag2 = (int) j2;
                } else if (i13 == 3) {
                    int i14 = this.mag3;
                    c = TokenParser.SP;
                    j2 = j3 + (1000000000 * (((long) i14) & 4294967295L));
                    this.mag3 = (int) j2;
                } else {
                    throw new ArithmeticException("BigInteger would overflow supported range");
                }
                j3 = j2 >>> c;
            }
            long j4 = (((long) this.mag3) & 4294967295L) + (((long) i12) & 4294967295L);
            this.mag3 = (int) j4;
            long j5 = j4 >>> MASK_SUPPORT_AUTO_TYPE;
            for (int i15 = 2; i15 >= 0; i15--) {
                if (i15 == 0) {
                    j = (((long) this.mag0) & 4294967295L) + j5;
                    this.mag0 = (int) j;
                } else if (i15 == 1) {
                    j = (((long) this.mag1) & 4294967295L) + j5;
                    this.mag1 = (int) j;
                } else if (i15 == 2) {
                    j = (((long) this.mag2) & 4294967295L) + j5;
                    this.mag2 = (int) j;
                } else if (i15 == 3) {
                    j = (((long) this.mag3) & 4294967295L) + j5;
                    this.mag3 = (int) j;
                } else {
                    throw new ArithmeticException("BigInteger would overflow supported range");
                }
                j5 = j >>> MASK_SUPPORT_AUTO_TYPE;
            }
        }
    }

    final void bigInt(byte[] bArr, int i, int i2) {
        int i3;
        long j;
        long j2;
        long j3;
        int i4 = i2 - i;
        if (this.scale > 0) {
            i4--;
        }
        if (i4 > 38) {
            throw new JSONException("number too large : ".concat(new String(bArr, i, i4)));
        }
        int i5 = i4 % 9;
        int i6 = i + (i5 != 0 ? i5 : 9);
        int i7 = i + 1;
        char c = (char) bArr[i];
        if (c == '.') {
            c = (char) bArr[i7];
            i7 = i + 2;
            i3 = i6 + 1;
        } else {
            i3 = i6;
        }
        int i8 = c - '0';
        while (i7 < i6) {
            char c2 = (char) bArr[i7];
            if (c2 == '.') {
                i7++;
                c2 = (char) bArr[i7];
                i3++;
                if (i6 < i2) {
                    i6++;
                }
            }
            i8 = (i8 * 10) + (c2 - '0');
            i7++;
        }
        this.mag3 = i8;
        while (i3 < i2) {
            int i9 = i3 + 9;
            int i10 = i3 + 1;
            char c3 = (char) bArr[i3];
            if (c3 == '.') {
                int i11 = i3 + 2;
                c3 = (char) bArr[i10];
                i3 += 10;
                i10 = i11;
                i9 = i3;
            } else {
                i3 = i9;
            }
            int i12 = c3 - '0';
            while (i10 < i9) {
                char c4 = (char) bArr[i10];
                if (c4 == '.') {
                    i10++;
                    c4 = (char) bArr[i10];
                    i3++;
                    i9++;
                }
                i12 = (i12 * 10) + (c4 - '0');
                i10++;
            }
            long j4 = 4294967295L;
            long j5 = ((long) i12) & 4294967295L;
            long j6 = 0;
            int i13 = 3;
            while (i13 >= 0) {
                if (i13 == 0) {
                    j2 = j4;
                    j3 = (1000000000 * (((long) this.mag0) & j2)) + j6;
                    this.mag0 = (int) j3;
                } else if (i13 == 1) {
                    j2 = j4;
                    j3 = (1000000000 * (((long) this.mag1) & j2)) + j6;
                    this.mag1 = (int) j3;
                } else if (i13 == 2) {
                    j2 = j4;
                    j3 = (1000000000 * (((long) this.mag2) & j2)) + j6;
                    this.mag2 = (int) j3;
                } else if (i13 == 3) {
                    j2 = j4;
                    j3 = (1000000000 * (((long) this.mag3) & j2)) + j6;
                    this.mag3 = (int) j3;
                } else {
                    throw new ArithmeticException("BigInteger would overflow supported range");
                }
                j6 = j3 >>> MASK_SUPPORT_AUTO_TYPE;
                i13--;
                j4 = j2;
            }
            long j7 = j4;
            long j8 = (((long) this.mag3) & j7) + j5;
            this.mag3 = (int) j8;
            long j9 = j8 >>> MASK_SUPPORT_AUTO_TYPE;
            for (int i14 = 2; i14 >= 0; i14--) {
                if (i14 == 0) {
                    j = (((long) this.mag0) & j7) + j9;
                    this.mag0 = (int) j;
                } else if (i14 == 1) {
                    j = (((long) this.mag1) & j7) + j9;
                    this.mag1 = (int) j;
                } else if (i14 == 2) {
                    j = (((long) this.mag2) & j7) + j9;
                    this.mag2 = (int) j;
                } else if (i14 == 3) {
                    j = (((long) this.mag3) & j7) + j9;
                    this.mag3 = (int) j;
                } else {
                    throw new ArithmeticException("BigInteger would overflow supported range");
                }
                j9 = j >>> MASK_SUPPORT_AUTO_TYPE;
            }
        }
    }

    public static AutoTypeBeforeHandler autoTypeFilter(String... strArr) {
        return new ContextAutoTypeBeforeHandler(strArr);
    }

    public static AutoTypeBeforeHandler autoTypeFilter(boolean z, String... strArr) {
        return new ContextAutoTypeBeforeHandler(z, strArr);
    }

    public static AutoTypeBeforeHandler autoTypeFilter(Class... clsArr) {
        return new ContextAutoTypeBeforeHandler(clsArr);
    }

    public static AutoTypeBeforeHandler autoTypeFilter(boolean z, Class... clsArr) {
        return new ContextAutoTypeBeforeHandler(z, clsArr);
    }

    public static final class Context {
        Supplier<List> arraySupplier;
        AutoTypeBeforeHandler autoTypeBeforeHandler;
        int bufferSize;
        String dateFormat;
        DateTimeFormatter dateFormatter;
        ExtraProcessor extraProcessor;
        long features;
        boolean formatComplex;
        boolean formatHasDay;
        boolean formatHasHour;
        boolean formatISO8601;
        boolean formatMillis;
        boolean formatUnixTime;
        boolean formatyyyyMMdd8;
        boolean formatyyyyMMddhhmmss19;
        boolean formatyyyyMMddhhmmssT19;
        Locale locale;
        int maxLevel;
        Supplier<Map> objectSupplier;
        final ObjectReaderProvider provider;
        final SymbolTable symbolTable;
        TimeZone timeZone;
        boolean useSimpleFormatter;
        boolean yyyyMMddhhmm16;
        ZoneId zoneId;

        public Context(ObjectReaderProvider objectReaderProvider) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = objectReaderProvider;
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            String str = JSONFactory.defaultReaderFormat;
            if (str != null) {
                setDateFormat(str);
            }
        }

        public Context(ObjectReaderProvider objectReaderProvider, long j) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = j;
            this.provider = objectReaderProvider;
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            String str = JSONFactory.defaultReaderFormat;
            if (str != null) {
                setDateFormat(str);
            }
        }

        public Context(Feature... featureArr) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = JSONFactory.getDefaultObjectReaderProvider();
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            String str = JSONFactory.defaultReaderFormat;
            if (str != null) {
                setDateFormat(str);
            }
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
        }

        public Context(String str, Feature... featureArr) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = JSONFactory.getDefaultObjectReaderProvider();
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            String str2 = JSONFactory.defaultReaderFormat;
            if (str2 != null) {
                setDateFormat(str2);
            }
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
            setDateFormat(str);
        }

        public Context(ObjectReaderProvider objectReaderProvider, Feature... featureArr) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = objectReaderProvider;
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            String str = JSONFactory.defaultReaderFormat;
            if (str != null) {
                setDateFormat(str);
            }
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
        }

        public Context(ObjectReaderProvider objectReaderProvider, Filter filter, Feature... featureArr) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = objectReaderProvider;
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            config(filter);
            String str = JSONFactory.defaultReaderFormat;
            if (str != null) {
                setDateFormat(str);
            }
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
        }

        public Context(ObjectReaderProvider objectReaderProvider, SymbolTable symbolTable) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = objectReaderProvider;
            this.symbolTable = symbolTable;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            String str = JSONFactory.defaultReaderFormat;
            if (str != null) {
                setDateFormat(str);
            }
        }

        public Context(ObjectReaderProvider objectReaderProvider, SymbolTable symbolTable, Feature... featureArr) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = objectReaderProvider;
            this.symbolTable = symbolTable;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            String str = JSONFactory.defaultReaderFormat;
            if (str != null) {
                setDateFormat(str);
            }
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
        }

        public Context(ObjectReaderProvider objectReaderProvider, SymbolTable symbolTable, Filter[] filterArr, Feature... featureArr) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = objectReaderProvider;
            this.symbolTable = symbolTable;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            config(filterArr);
            String str = JSONFactory.defaultReaderFormat;
            if (str != null) {
                setDateFormat(str);
            }
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
        }

        public boolean isFormatUnixTime() {
            return this.formatUnixTime;
        }

        public boolean isFormatyyyyMMddhhmmss19() {
            return this.formatyyyyMMddhhmmss19;
        }

        public boolean isFormatyyyyMMddhhmmssT19() {
            return this.formatyyyyMMddhhmmssT19;
        }

        public boolean isFormatyyyyMMdd8() {
            return this.formatyyyyMMdd8;
        }

        public boolean isFormatMillis() {
            return this.formatMillis;
        }

        public boolean isFormatISO8601() {
            return this.formatISO8601;
        }

        public boolean isFormatHasHour() {
            return this.formatHasHour;
        }

        public ObjectReader getObjectReader(Type type) {
            return this.provider.getObjectReader(type, (this.features & Feature.FieldBased.mask) != 0);
        }

        public ObjectReaderProvider getProvider() {
            return this.provider;
        }

        public ObjectReader getObjectReaderAutoType(long j) {
            return this.provider.getObjectReader(j);
        }

        public ObjectReader getObjectReaderAutoType(String str, Class cls) {
            Class<?> clsApply;
            AutoTypeBeforeHandler autoTypeBeforeHandler = this.autoTypeBeforeHandler;
            if (autoTypeBeforeHandler != null && (clsApply = autoTypeBeforeHandler.apply(str, (Class<?>) cls, this.features)) != null) {
                return this.provider.getObjectReader(clsApply, (this.features & Feature.FieldBased.mask) != 0);
            }
            return this.provider.getObjectReader(str, cls, this.features);
        }

        public AutoTypeBeforeHandler getContextAutoTypeBeforeHandler() {
            return this.autoTypeBeforeHandler;
        }

        public ObjectReader getObjectReaderAutoType(String str, Class cls, long j) {
            Class<?> clsApply;
            AutoTypeBeforeHandler autoTypeBeforeHandler = this.autoTypeBeforeHandler;
            if (autoTypeBeforeHandler != null && (clsApply = autoTypeBeforeHandler.apply(str, (Class<?>) cls, j)) != null) {
                return this.provider.getObjectReader(clsApply, (Feature.FieldBased.mask & j) != 0);
            }
            return this.provider.getObjectReader(str, cls, j | this.features);
        }

        public ExtraProcessor getExtraProcessor() {
            return this.extraProcessor;
        }

        public void setExtraProcessor(ExtraProcessor extraProcessor) {
            this.extraProcessor = extraProcessor;
        }

        public Supplier<Map> getObjectSupplier() {
            return this.objectSupplier;
        }

        public void setObjectSupplier(Supplier<Map> supplier) {
            this.objectSupplier = supplier;
        }

        public Supplier<List> getArraySupplier() {
            return this.arraySupplier;
        }

        public void setArraySupplier(Supplier<List> supplier) {
            this.arraySupplier = supplier;
        }

        public DateTimeFormatter getDateFormatter() {
            String str;
            DateTimeFormatter dateTimeFormatterOfPattern;
            if (this.dateFormatter == null && (str = this.dateFormat) != null && !this.formatMillis && !this.formatISO8601 && !this.formatUnixTime) {
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

        public void setDateFormatter(DateTimeFormatter dateTimeFormatter) {
            this.dateFormatter = dateTimeFormatter;
        }

        public String getDateFormat() {
            return this.dateFormat;
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        public void setDateFormat(String str) {
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            boolean z5;
            byte b;
            boolean z6;
            boolean z7;
            if (str != null && str.isEmpty()) {
                str = null;
            }
            boolean z8 = false;
            if (str != null) {
                str.hashCode();
                switch (str.hashCode()) {
                    case -1172057030:
                        b = !str.equals("yyyy-MM-dd HH:mm") ? (byte) -1 : (byte) 0;
                        break;
                    case -1074095546:
                        b = !str.equals("millis") ? (byte) -1 : (byte) 1;
                        break;
                    case -347789785:
                        b = !str.equals("yyyyMMddHHmmssSSSZ") ? (byte) -1 : JSONReader.JSON_TYPE_DEC;
                        break;
                    case -288020395:
                        b = !str.equals("unixtime") ? (byte) -1 : JSONReader.JSON_TYPE_STRING;
                        break;
                    case -276306848:
                        b = !str.equals("yyyyMMdd") ? (byte) -1 : JSONReader.JSON_TYPE_BOOL;
                        break;
                    case -159776256:
                        b = !str.equals("yyyy-MM-dd") ? (byte) -1 : JSONReader.JSON_TYPE_NULL;
                        break;
                    case 1333195168:
                        b = !str.equals("yyyy-MM-dd HH:mm:ss") ? (byte) -1 : JSONReader.JSON_TYPE_OBJECT;
                        break;
                    case 1798231098:
                        b = !str.equals("yyyy-MM-dd'T'HH:mm:ss") ? (byte) -1 : JSONReader.JSON_TYPE_ARRAY;
                        break;
                    case 1834843604:
                        b = !str.equals("yyyy-MM-ddTHH:mm:ss") ? (byte) -1 : JSONReader.JSON_TYPE_BIG_DEC;
                        break;
                    case 2095190916:
                        b = !str.equals("iso8601") ? (byte) -1 : JSONReader.JSON_TYPE_INT8;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        this.yyyyMMddhhmm16 = true;
                        z6 = false;
                        z2 = false;
                        z3 = false;
                        z4 = false;
                        z5 = false;
                        break;
                    case 1:
                        z6 = false;
                        z3 = false;
                        z4 = false;
                        z5 = false;
                        z2 = true;
                        break;
                    case 2:
                        z7 = true;
                        this.formatyyyyMMddhhmmss19 = true;
                        z2 = false;
                        z5 = z7;
                        z3 = true;
                        z4 = true;
                        z6 = false;
                        break;
                    case 3:
                        z2 = false;
                        z3 = false;
                        z4 = false;
                        z5 = false;
                        z6 = true;
                        break;
                    case 4:
                    case 5:
                        this.formatyyyyMMdd8 = true;
                        z6 = false;
                        z2 = false;
                        z4 = false;
                        z5 = false;
                        z3 = true;
                        break;
                    case 6:
                    case 8:
                        z7 = false;
                        this.formatyyyyMMddhhmmss19 = true;
                        z2 = false;
                        z5 = z7;
                        z3 = true;
                        z4 = true;
                        z6 = false;
                        break;
                    case 7:
                        this.formatyyyyMMddhhmmssT19 = true;
                        z6 = false;
                        z2 = false;
                        z5 = false;
                        z3 = true;
                        z4 = true;
                        break;
                    case 9:
                        z6 = false;
                        z2 = false;
                        z3 = false;
                        z4 = false;
                        z5 = false;
                        z8 = true;
                        break;
                    default:
                        z5 = false;
                        z3 = str.indexOf(100) != -1;
                        z4 = (str.indexOf(72) == -1 && str.indexOf(104) == -1 && str.indexOf(75) == -1 && str.indexOf(107) == -1) ? false : true;
                        z6 = false;
                        z2 = false;
                        break;
                }
                this.formatComplex = true ^ (((this.formatyyyyMMddhhmmss19 | this.formatyyyyMMddhhmmssT19) | this.formatyyyyMMdd8) | z8);
                boolean z9 = z6;
                z = z8;
                z8 = z9;
            } else {
                z = false;
                z2 = false;
                z3 = false;
                z4 = false;
                z5 = false;
            }
            if (!Objects.equals(this.dateFormat, str)) {
                this.dateFormatter = null;
            }
            this.dateFormat = str;
            this.formatUnixTime = z8;
            this.formatMillis = z2;
            this.formatISO8601 = z;
            this.formatHasDay = z3;
            this.formatHasHour = z4;
            this.useSimpleFormatter = z5;
        }

        public ZoneId getZoneId() {
            if (this.zoneId == null) {
                this.zoneId = DateUtils.DEFAULT_ZONE_ID;
            }
            return this.zoneId;
        }

        public long getFeatures() {
            return this.features;
        }

        public void setFeatures(long j) {
            this.features = j;
        }

        public void setZoneId(ZoneId zoneId) {
            this.zoneId = zoneId;
        }

        public int getMaxLevel() {
            return this.maxLevel;
        }

        public void setMaxLevel(int i) {
            this.maxLevel = i;
        }

        public int getBufferSize() {
            return this.bufferSize;
        }

        public Context setBufferSize(int i) {
            if (i < 0) {
                throw new IllegalArgumentException("buffer size can not be less than zero");
            }
            this.bufferSize = i;
            return this;
        }

        public Locale getLocale() {
            return this.locale;
        }

        public void setLocale(Locale locale) {
            this.locale = locale;
        }

        public TimeZone getTimeZone() {
            return this.timeZone;
        }

        public void setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
        }

        public void config(Feature... featureArr) {
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
        }

        public void config(Filter filter, Feature... featureArr) {
            if (filter instanceof AutoTypeBeforeHandler) {
                this.autoTypeBeforeHandler = (AutoTypeBeforeHandler) filter;
            }
            if (filter instanceof ExtraProcessor) {
                this.extraProcessor = (ExtraProcessor) filter;
            }
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
        }

        public void config(Filter filter) {
            if (filter instanceof AutoTypeBeforeHandler) {
                this.autoTypeBeforeHandler = (AutoTypeBeforeHandler) filter;
            }
            if (filter instanceof ExtraProcessor) {
                this.extraProcessor = (ExtraProcessor) filter;
            }
        }

        public void config(Filter[] filterArr, Feature... featureArr) {
            for (Filter filter : filterArr) {
                if (filter instanceof AutoTypeBeforeHandler) {
                    this.autoTypeBeforeHandler = (AutoTypeBeforeHandler) filter;
                }
                if (filter instanceof ExtraProcessor) {
                    this.extraProcessor = (ExtraProcessor) filter;
                }
            }
            for (Feature feature : featureArr) {
                this.features |= feature.mask;
            }
        }

        public void config(Filter[] filterArr) {
            for (Filter filter : filterArr) {
                if (filter instanceof AutoTypeBeforeHandler) {
                    this.autoTypeBeforeHandler = (AutoTypeBeforeHandler) filter;
                }
                if (filter instanceof ExtraProcessor) {
                    this.extraProcessor = (ExtraProcessor) filter;
                }
            }
        }

        public boolean isEnabled(Feature feature) {
            return (this.features & feature.mask) != 0;
        }

        public void config(Feature feature, boolean z) {
            if (z) {
                this.features = feature.mask | this.features;
            } else {
                this.features = (~feature.mask) & this.features;
            }
        }
    }

    public enum Feature {
        FieldBased(JSONReader.MASK_FIELD_BASED),
        IgnoreNoneSerializable(JSONReader.MASK_IGNORE_NONE_SERIALIZABLE),
        ErrorOnNoneSerializable(JSONReader.MASK_ERROR_ON_NONE_SERIALIZABLE),
        SupportArrayToBean(JSONReader.MASK_SUPPORT_ARRAY_TO_BEAN),
        InitStringFieldAsEmpty(JSONReader.MASK_INIT_STRING_FIELD_AS_EMPTY),
        SupportAutoType(JSONReader.MASK_SUPPORT_AUTO_TYPE),
        SupportSmartMatch(JSONReader.MASK_SUPPORT_SMART_MATCH),
        UseNativeObject(128),
        SupportClassForName(256),
        IgnoreSetNullValue(512),
        UseDefaultConstructorAsPossible(RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE),
        UseBigDecimalForFloats(2048),
        UseBigDecimalForDoubles(4096),
        ErrorOnEnumNotMatch(8192),
        TrimString(16384),
        ErrorOnNotSupportAutoType(32768),
        DuplicateKeyValueAsArray(65536),
        AllowUnQuotedFieldNames(JSONReader.MASK_ALLOW_UN_QUOTED_FIELD_NAMES),
        NonStringKeyAsString(262144),
        Base64StringAsByteArray(524288),
        IgnoreCheckClose(1048576),
        ErrorOnNullForPrimitives(2097152),
        NullOnError(4194304),
        IgnoreAutoTypeNotMatch(8388608),
        NonZeroNumberCastToBooleanAsTrue(16777216),
        IgnoreNullPropertyValue(33554432),
        ErrorOnUnknownProperties(67108864),
        EmptyStringAsNull(JSONReader.MASK_EMPTY_STRING_AS_NULL),
        NonErrorOnNumberOverflow(268435456),
        UseBigIntegerForInts(536870912),
        UseLongForInts(1073741824),
        DisableSingleQuote(JSONReader.MASK_DISABLE_SINGLE_QUOTE),
        UseDoubleForDecimals(4294967296L),
        DisableReferenceDetect(JSONReader.MASK_DISABLE_REFERENCE_DETECT);

        public final long mask;

        Feature(long j) {
            this.mask = j;
        }

        public static long of(Feature[] featureArr) {
            long j = 0;
            if (featureArr == null) {
                return 0L;
            }
            for (Feature feature : featureArr) {
                j |= feature.mask;
            }
            return j;
        }

        public boolean isEnabled(long j) {
            return (j & this.mask) != 0;
        }

        public static boolean isEnabled(long j, Feature feature) {
            return (j & feature.mask) != 0;
        }
    }

    static final class ResolveTask {
        final FieldReader fieldReader;
        final Object name;
        final Object object;
        final JSONPath reference;

        ResolveTask(FieldReader fieldReader, Object obj, Object obj2, JSONPath jSONPath) {
            this.fieldReader = fieldReader;
            this.object = obj;
            this.name = obj2;
            this.reference = jSONPath;
        }

        public String toString() {
            return this.reference.toString();
        }
    }

    public SavePoint mark() {
        return new SavePoint(this.offset, this.ch);
    }

    public void reset(SavePoint savePoint) {
        this.offset = savePoint.offset;
        this.ch = (char) savePoint.current;
    }

    final boolean checkNameBegin(int i) {
        long j = this.context.features;
        if (i == 39 && (MASK_DISABLE_SINGLE_QUOTE & j) != 0) {
            throw notSupportName();
        }
        if (i == 34 || i == 39) {
            return false;
        }
        if ((j & MASK_ALLOW_UN_QUOTED_FIELD_NAMES) != 0) {
            readFieldNameHashCodeUnquote();
            return true;
        }
        throw notSupportName();
    }

    final JSONException notSupportName() {
        return new JSONException(info("not support unquoted name"));
    }

    final JSONException valueError() {
        return new JSONException(info("illegal value"));
    }

    final JSONException error(String str) {
        return new JSONException(info(str));
    }

    final JSONException error(String str, Exception exc) {
        return new JSONException(info(str), exc);
    }

    final JSONException error() {
        throw new JSONValidException("error, offset " + this.offset + ", char " + this.ch);
    }

    final JSONException error(int i, int i2) {
        throw new JSONValidException("error, offset " + i + ", char " + ((char) i2));
    }

    static JSONException syntaxError(int i) {
        return new JSONException("syntax error, expect ',', but '" + ((char) i) + "'");
    }

    static JSONException syntaxError(int i, int i2) {
        return new JSONException("syntax error, offset " + i + ", char " + ((char) i2));
    }

    static JSONException numberError(int i, int i2) {
        return new JSONException("illegal number, offset " + i + ", char " + ((char) i2));
    }

    JSONException numberError() {
        return new JSONException("illegal number, offset " + this.offset + ", char " + this.ch);
    }

    public final String info() {
        return info(null);
    }

    public String info(String str) {
        if (str == null || str.isEmpty()) {
            return "offset " + this.offset;
        }
        return str + ", offset " + this.offset;
    }

    public static class SavePoint {
        protected final int current;
        protected final int offset;

        protected SavePoint(int i, int i2) {
            this.offset = i;
            this.current = i2;
        }
    }

    static final class BigIntegerCreator implements BiFunction<Integer, int[], BigInteger> {
        static final BiFunction<Integer, int[], BigInteger> BIG_INTEGER_CREATOR;

        BigIntegerCreator() {
        }

        static {
            BiFunction bigIntegerCreator;
            if (JDKUtils.ANDROID || JDKUtils.GRAAL) {
                bigIntegerCreator = null;
            } else {
                try {
                    MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(BigInteger.class);
                    MethodHandle methodHandleFindConstructor = lookupTrustedLookup.findConstructor(BigInteger.class, MethodType.methodType(Void.TYPE, Integer.TYPE, int[].class));
                    bigIntegerCreator = (BiFunction) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", MethodType.methodType(BiFunction.class), methodHandleFindConstructor.type().generic(), methodHandleFindConstructor, MethodType.methodType(BigInteger.class, Integer.class, int[].class)).getTarget().invokeExact();
                } catch (Throwable unused) {
                    bigIntegerCreator = null;
                }
            }
            if (bigIntegerCreator == null) {
                bigIntegerCreator = new BigIntegerCreator();
            }
            BIG_INTEGER_CREATOR = bigIntegerCreator;
        }

        /* JADX WARN: Removed duplicated region for block: B:27:0x004b  */
        @Override // java.util.function.BiFunction
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.math.BigInteger apply(java.lang.Integer r12, int[] r13) {
            /*
                r11 = this;
                int r12 = r12.intValue()
                int r0 = r13.length
                r1 = 0
                r2 = 1
                if (r0 != 0) goto Lb
                r3 = r1
                goto L39
            Lb:
                r0 = r13[r1]
                int r0 = java.lang.Integer.numberOfLeadingZeros(r0)
                int r0 = 32 - r0
                int r3 = r13.length
                int r3 = r3 - r2
                int r3 = r3 << 5
                int r3 = r3 + r0
                if (r12 >= 0) goto L39
                r0 = r13[r1]
                int r0 = java.lang.Integer.bitCount(r0)
                if (r0 != r2) goto L24
                r0 = r2
                goto L25
            L24:
                r0 = r1
            L25:
                r4 = r2
            L26:
                int r5 = r13.length
                if (r4 >= r5) goto L35
                if (r0 == 0) goto L35
                r0 = r13[r4]
                if (r0 != 0) goto L31
                r0 = r2
                goto L32
            L31:
                r0 = r1
            L32:
                int r4 = r4 + 1
                goto L26
            L35:
                if (r0 == 0) goto L39
                int r3 = r3 + (-1)
            L39:
                int r3 = r3 / 8
                int r0 = r3 + 1
                byte[] r0 = new byte[r0]
                r4 = 4
                r6 = r1
                r7 = r6
                r5 = r4
            L43:
                if (r3 < 0) goto L7c
                if (r5 != r4) goto L72
                int r5 = r7 + 1
                if (r7 >= 0) goto L4d
            L4b:
                r6 = r1
                goto L6f
            L4d:
                int r6 = r13.length
                if (r7 < r6) goto L54
                if (r12 >= 0) goto L4b
                r6 = -1
                goto L6f
            L54:
                int r6 = r13.length
                int r6 = r6 - r7
                int r6 = r6 - r2
                r6 = r13[r6]
                if (r12 < 0) goto L5c
                goto L6f
            L5c:
                int r8 = r13.length
                int r9 = r8 + (-1)
            L5f:
                if (r9 < 0) goto L68
                r10 = r13[r9]
                if (r10 != 0) goto L68
                int r9 = r9 + (-1)
                goto L5f
            L68:
                int r8 = r8 - r9
                int r8 = r8 - r2
                if (r7 > r8) goto L6e
                int r6 = -r6
                goto L6f
            L6e:
                int r6 = ~r6
            L6f:
                r7 = r5
                r5 = r2
                goto L76
            L72:
                int r6 = r6 >>> 8
                int r5 = r5 + 1
            L76:
                byte r8 = (byte) r6
                r0[r3] = r8
                int r3 = r3 + (-1)
                goto L43
            L7c:
                java.math.BigInteger r12 = new java.math.BigInteger
                r12.<init>(r0)
                return r12
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONReader.BigIntegerCreator.apply(java.lang.Integer, int[]):java.math.BigInteger");
        }
    }

    public ObjectReader getObjectReaderAutoType(long j, Class cls, long j2) {
        Class<?> clsApply;
        ObjectReader objectReaderAutoType = this.context.getObjectReaderAutoType(j);
        if (objectReaderAutoType != null) {
            return objectReaderAutoType;
        }
        String string = getString();
        if (this.context.autoTypeBeforeHandler != null && (clsApply = this.context.autoTypeBeforeHandler.apply(string, (Class<?>) cls, j2)) != null) {
            return this.context.provider.getObjectReader(clsApply, (j2 & Feature.FieldBased.mask) != 0);
        }
        return this.context.provider.getObjectReader(string, cls, j2 | this.context.features);
    }

    protected final String readStringNotMatch() {
        char c = this.ch;
        if (c != '+' && c != '-') {
            if (c == '[') {
                List array = readArray();
                if (array.size() == 1) {
                    Object obj = array.get(0);
                    if (obj == null) {
                        return null;
                    }
                    if (obj instanceof String) {
                        return obj.toString();
                    }
                }
                return toString(array);
            }
            if (c != 'f') {
                if (c == 'n') {
                    readNull();
                    return null;
                }
                if (c != 't') {
                    if (c == '{') {
                        return toString(readObject());
                    }
                    switch (c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            break;
                        default:
                            throw new JSONException(info("illegal input : " + this.ch));
                    }
                }
            }
            boolean boolValue = readBoolValue();
            this.boolValue = boolValue;
            return boolValue ? "true" : "false";
        }
        readNumber0();
        return getNumber().toString();
    }

    protected static String stringValue(String str, long j) {
        if ((16384 & j) != 0) {
            str = str.trim();
        }
        if ((j & MASK_EMPTY_STRING_AS_NULL) == 0 || !str.isEmpty()) {
            return str;
        }
        return null;
    }
}
