package com.alibaba.fastjson2.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import kotlin.UByte;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: classes.dex */
public class JDKUtils {
    public static final boolean ANDROID;
    public static final int ANDROID_SDK_INT;
    public static final long ARRAY_BYTE_BASE_OFFSET;
    public static final long ARRAY_CHAR_BASE_OFFSET;
    public static final boolean BIG_ENDIAN;
    static final Class<?> CLASS_SQL_DATASOURCE;
    static final Class<?> CLASS_SQL_ROW_SET;
    public static final Class CLASS_TRANSIENT;
    static volatile MethodHandle CONSTRUCTOR_LOOKUP;
    static volatile boolean CONSTRUCTOR_LOOKUP_ERROR;
    public static final long FIELD_BIGINTEGER_MAG_OFFSET;
    public static final long FIELD_DECIMAL_INT_COMPACT_OFFSET;
    public static final Field FIELD_STRING_CODER;
    public static volatile boolean FIELD_STRING_CODER_ERROR;
    public static final long FIELD_STRING_CODER_OFFSET;
    public static final Field FIELD_STRING_VALUE;
    public static volatile boolean FIELD_STRING_VALUE_ERROR;
    public static final long FIELD_STRING_VALUE_OFFSET;
    public static final boolean GRAAL;
    public static final boolean HAS_SQL;
    static final MethodHandles.Lookup IMPL_LOOKUP;
    public static final MethodHandle INDEX_OF_CHAR_LATIN1;
    public static final int JVM_VERSION;
    public static final MethodHandle METHOD_HANDLE_HAS_NEGATIVE;
    public static final boolean OPENJ9;
    public static final Predicate<byte[]> PREDICATE_IS_ASCII;
    public static final ToIntFunction<String> STRING_CODER;
    public static final BiFunction<byte[], Byte, String> STRING_CREATOR_JDK11;
    public static final BiFunction<char[], Boolean, String> STRING_CREATOR_JDK8;
    public static final Function<String, byte[]> STRING_VALUE;
    public static final Unsafe UNSAFE;
    public static final int VECTOR_BIT_LENGTH;
    public static final boolean VECTOR_SUPPORT;
    static volatile Throwable initErrorLast;
    static volatile Throwable reflectErrorLast;
    public static final Byte LATIN1 = (byte) 0;
    public static final Byte UTF16 = (byte) 1;
    static final AtomicInteger reflectErrorCount = new AtomicInteger();

    static /* synthetic */ int lambda$static$0(String str) {
        return 1;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(56:8|240|9|234|10|229|11|(1:13)(1:14)|(2:225|18)|19|(1:21)|22|(2:24|25)(1:26)|(2:219|28)(1:43)|44|(2:260|45)|(3:262|46|47)|50|(2:274|52)(1:54)|55|(4:57|(5:268|59|266|60|66)(1:64)|65|66)(7:67|(8:252|69|250|70|76|(5:236|78|270|79|85)(1:83)|84|85)(1:74)|75|76|(0)(0)|84|85)|86|(2:88|(2:278|94)(1:93))|276|95|96|(2:256|97)|100|(1:102)(1:103)|104|(7:244|106|107|248|108|(1:113)|114)(1:115)|116|232|117|(22:119|(3:223|121|122)|130|(4:132|258|133|134)(1:140)|141|(2:238|145)|149|(2:272|151)(1:155)|156|(2:254|158)(1:162)|163|(2:246|165)(1:167)|168|242|169|(1:171)(1:173)|174|(3:217|177|(10:184|227|185|221|186|187|200|(1:202)|203|204))|194|(0)|203|204)(1:125)|129|130|(0)(0)|141|(3:143|238|145)|149|(0)(0)|156|(0)(0)|163|(0)(0)|168|242|169|(0)(0)|174|(3:217|177|(11:182|184|227|185|221|186|187|200|(0)|203|204))|194|(0)|203|204) */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x042d, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x042e, code lost:
    
        r1 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x018f  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0191  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x01b8  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x01c2 A[Catch: all -> 0x0226, TRY_LEAVE, TryCatch #8 {all -> 0x0226, blocks: (B:117:0x01be, B:119:0x01c2), top: B:232:0x01be }] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0224  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0256  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x029b  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x02c0  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x02ef  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x02f6 A[Catch: all -> 0x042d, TRY_LEAVE, TryCatch #13 {all -> 0x042d, blocks: (B:169:0x02f2, B:171:0x02f6), top: B:242:0x02f2 }] */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0340  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0345 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0439  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x012b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0198 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:246:0x02c9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:254:0x02a2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:272:0x0270 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:274:0x00d3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00e7  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x014c  */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    static {
        /*
            Method dump skipped, instruction units count: 1114
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.JDKUtils.<clinit>():void");
    }

    public static boolean isSQLDataSourceOrRowSet(Class<?> cls) {
        Class<?> cls2 = CLASS_SQL_DATASOURCE;
        if (cls2 != null && cls2.isAssignableFrom(cls)) {
            return true;
        }
        Class<?> cls3 = CLASS_SQL_ROW_SET;
        return cls3 != null && cls3.isAssignableFrom(cls);
    }

    public static void setReflectErrorLast(Throwable th) {
        reflectErrorCount.incrementAndGet();
        reflectErrorLast = th;
    }

    public static char[] getCharArray(String str) {
        if (!FIELD_STRING_VALUE_ERROR) {
            try {
                return (char[]) UNSAFE.getObject(str, FIELD_STRING_VALUE_OFFSET);
            } catch (Exception unused) {
                FIELD_STRING_VALUE_ERROR = true;
            }
        }
        return str.toCharArray();
    }

    public static MethodHandles.Lookup trustedLookup(Class cls) {
        if (!CONSTRUCTOR_LOOKUP_ERROR) {
            try {
                MethodHandle methodHandleFindConstructor = CONSTRUCTOR_LOOKUP;
                if (JVM_VERSION < 15) {
                    if (methodHandleFindConstructor == null) {
                        methodHandleFindConstructor = IMPL_LOOKUP.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(Void.TYPE, Class.class, Integer.TYPE));
                        CONSTRUCTOR_LOOKUP = methodHandleFindConstructor;
                    }
                    return (MethodHandles.Lookup) methodHandleFindConstructor.invoke(cls, OPENJ9 ? 31 : -1);
                }
                if (methodHandleFindConstructor == null) {
                    methodHandleFindConstructor = IMPL_LOOKUP.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(Void.TYPE, Class.class, Class.class, Integer.TYPE));
                    CONSTRUCTOR_LOOKUP = methodHandleFindConstructor;
                }
                return (MethodHandles.Lookup) methodHandleFindConstructor.invoke(cls, null, -1);
            } catch (Throwable unused) {
                CONSTRUCTOR_LOOKUP_ERROR = true;
            }
        }
        return IMPL_LOOKUP.in(cls);
    }

    public static String asciiStringJDK8(byte[] bArr, int i, int i2) {
        char[] cArr = new char[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            cArr[i3] = (char) bArr[i + i3];
        }
        return STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
    }

    public static String latin1StringJDK8(byte[] bArr, int i, int i2) {
        char[] cArr = new char[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            cArr[i3] = (char) (bArr[i + i3] & UByte.MAX_VALUE);
        }
        return STRING_CREATOR_JDK8.apply(cArr, Boolean.TRUE);
    }
}
