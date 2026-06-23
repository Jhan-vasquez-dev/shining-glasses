package com.alibaba.fastjson2.util;

import androidx.exifinterface.media.ExifInterface;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderImplEnum;
import com.alibaba.fastjson2.reader.ObjectReaderImplInstant;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterPrimitiveImpl;
import java.io.IOException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import okhttp3.HttpUrl;
import okio.internal.BufferKt;
import org.apache.http.HttpStatus;
import org.apache.http.message.TokenParser;

/* JADX INFO: loaded from: classes.dex */
public class TypeUtils {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final Class CLASS_JSON_ARRAY_1x;
    public static final Class CLASS_JSON_OBJECT_1x;
    public static final Field FIELD_JSON_OBJECT_1x_map;
    private static final double L = 3.321928094887362d;
    static volatile MethodHandle METHOD_NEW_PROXY_INSTANCE = null;
    static volatile boolean METHOD_NEW_PROXY_INSTANCE_ERROR = false;
    private static final int P_D = 53;
    private static final int P_F = 24;
    private static final int Q_MAX_D = 971;
    private static final int Q_MAX_F = 104;
    private static final int Q_MIN_D = -1074;
    private static final int Q_MIN_F = -149;
    public static final Class CLASS_SINGLE_SET = Collections.singleton(1).getClass();
    public static final Class CLASS_SINGLE_LIST = Collections.singletonList(1).getClass();
    public static final Class CLASS_UNMODIFIABLE_COLLECTION = Collections.unmodifiableCollection(new ArrayList()).getClass();
    public static final Class CLASS_UNMODIFIABLE_LIST = Collections.unmodifiableList(new ArrayList()).getClass();
    public static final Class CLASS_UNMODIFIABLE_SET = Collections.unmodifiableSet(new HashSet()).getClass();
    public static final Class CLASS_UNMODIFIABLE_SORTED_SET = Collections.unmodifiableSortedSet(new TreeSet()).getClass();
    public static final Class CLASS_UNMODIFIABLE_NAVIGABLE_SET = Collections.unmodifiableNavigableSet(new TreeSet()).getClass();
    public static final ParameterizedType PARAM_TYPE_LIST_STR = new ParameterizedTypeImpl(List.class, String.class);
    public static final MethodType METHOD_TYPE_SUPPLIER = MethodType.methodType(Supplier.class);
    public static final MethodType METHOD_TYPE_FUNCTION = MethodType.methodType(Function.class);
    public static final MethodType METHOD_TYPE_TO_INT_FUNCTION = MethodType.methodType(ToIntFunction.class);
    public static final MethodType METHOD_TYPE_TO_LONG_FUNCTION = MethodType.methodType(ToLongFunction.class);
    public static final MethodType METHOD_TYPE_OBJECT_INT_CONSUMER = MethodType.methodType(ObjIntConsumer.class);
    public static final MethodType METHOD_TYPE_INT_FUNCTION = MethodType.methodType(IntFunction.class);
    public static final MethodType METHOD_TYPE_LONG_FUNCTION = MethodType.methodType(LongFunction.class);
    public static final MethodType METHOD_TYPE_BI_FUNCTION = MethodType.methodType(BiFunction.class);
    public static final MethodType METHOD_TYPE_BI_CONSUMER = MethodType.methodType(BiConsumer.class);
    public static final MethodType METHOD_TYPE_VOO = MethodType.methodType(Void.TYPE, Object.class, Object.class);
    public static final MethodType METHOD_TYPE_OBJECT = MethodType.methodType(Object.class);
    public static final MethodType METHOD_TYPE_OBJECT_OBJECT = MethodType.methodType((Class<?>) Object.class, (Class<?>) Object.class);
    public static final MethodType METHOD_TYPE_INT_OBJECT = MethodType.methodType((Class<?>) Integer.TYPE, (Class<?>) Object.class);
    public static final MethodType METHOD_TYPE_LONG_OBJECT = MethodType.methodType((Class<?>) Long.TYPE, (Class<?>) Object.class);
    public static final MethodType METHOD_TYPE_VOID_OBJECT_INT = MethodType.methodType(Void.TYPE, Object.class, Integer.TYPE);
    public static final MethodType METHOD_TYPE_OBJECT_LONG = MethodType.methodType((Class<?>) Object.class, (Class<?>) Long.TYPE);
    public static final MethodType METHOD_TYPE_VOID_LONG = MethodType.methodType((Class<?>) Void.TYPE, (Class<?>) Long.TYPE);
    public static final MethodType METHOD_TYPE_OBJECT_OBJECT_OBJECT = MethodType.methodType(Object.class, Object.class, Object.class);
    public static final MethodType METHOD_TYPE_VOID = MethodType.methodType(Void.TYPE);
    public static final MethodType METHOD_TYPE_VOID_INT = MethodType.methodType((Class<?>) Void.TYPE, (Class<?>) Integer.TYPE);
    public static final MethodType METHOD_TYPE_VOID_STRING = MethodType.methodType((Class<?>) Void.TYPE, (Class<?>) String.class);
    public static final MethodType METHOD_TYPE_OBJECT_INT = MethodType.methodType((Class<?>) Object.class, (Class<?>) Integer.TYPE);
    public static final BigInteger BIGINT_INT32_MIN = BigInteger.valueOf(-2147483648L);
    public static final BigInteger BIGINT_INT32_MAX = BigInteger.valueOf(2147483647L);
    public static final BigInteger BIGINT_INT64_MIN = BigInteger.valueOf(Long.MIN_VALUE);
    public static final BigInteger BIGINT_INT64_MAX = BigInteger.valueOf(Long.MAX_VALUE);
    static final long LONG_JAVASCRIPT_LOW = -9007199254740991L;
    static final BigInteger BIGINT_JAVASCRIPT_LOW = BigInteger.valueOf(LONG_JAVASCRIPT_LOW);
    static final long LONG_JAVASCRIPT_HIGH = 9007199254740991L;
    static final BigInteger BIGINT_JAVASCRIPT_HIGH = BigInteger.valueOf(LONG_JAVASCRIPT_HIGH);
    public static final double[] SMALL_10_POW = {1.0d, 10.0d, 100.0d, 1000.0d, 10000.0d, 100000.0d, 1000000.0d, 1.0E7d, 1.0E8d, 1.0E9d, 1.0E10d, 1.0E11d, 1.0E12d, 1.0E13d, 1.0E14d, 1.0E15d, 1.0E16d, 1.0E17d, 1.0E18d, 1.0E19d, 1.0E20d, 1.0E21d, 1.0E22d};
    static final float[] SINGLE_SMALL_10_POW = {1.0f, 10.0f, 100.0f, 1000.0f, 10000.0f, 100000.0f, 1000000.0f, 1.0E7f, 1.0E8f, 1.0E9f, 1.0E10f};
    static final double[] BIG_10_POW = {1.0E16d, 1.0E32d, 1.0E64d, 1.0E128d, 1.0E256d};
    static final double[] TINY_10_POW = {1.0E-16d, 1.0E-32d, 1.0E-64d, 1.0E-128d, 1.0E-256d};
    static final Cache CACHE = new Cache();
    static final AtomicReferenceFieldUpdater<Cache, char[]> CHARS_UPDATER = AtomicReferenceFieldUpdater.newUpdater(Cache.class, char[].class, "chars");
    static final Map<Class, String> NAME_MAPPINGS = new IdentityHashMap();
    static final Map<String, Class> TYPE_MAPPINGS = new ConcurrentHashMap();

    public static boolean isJavaScriptSupport(long j) {
        return j >= LONG_JAVASCRIPT_LOW && j <= LONG_JAVASCRIPT_HIGH;
    }

    static {
        Class clsLoadClass = loadClass("com.alibaba.fastjson.JSONObject");
        CLASS_JSON_OBJECT_1x = clsLoadClass;
        Field declaredField = null;
        if (clsLoadClass != null) {
            try {
                declaredField = clsLoadClass.getDeclaredField("map");
                declaredField.setAccessible(true);
            } catch (Throwable unused) {
            }
        }
        FIELD_JSON_OBJECT_1x_map = declaredField;
        CLASS_JSON_ARRAY_1x = loadClass("com.alibaba.fastjson.JSONArray");
        Map<Class, String> map = NAME_MAPPINGS;
        map.put(Byte.TYPE, "B");
        map.put(Short.TYPE, ExifInterface.LATITUDE_SOUTH);
        map.put(Integer.TYPE, "I");
        map.put(Long.TYPE, "J");
        map.put(Float.TYPE, "F");
        map.put(Double.TYPE, "D");
        map.put(Character.TYPE, "C");
        map.put(Boolean.TYPE, "Z");
        map.put(Object[].class, "[O");
        map.put(Object[][].class, "[[O");
        map.put(byte[].class, "[B");
        map.put(byte[][].class, "[[B");
        map.put(short[].class, "[S");
        map.put(short[][].class, "[[S");
        map.put(int[].class, "[I");
        map.put(int[][].class, "[[I");
        map.put(long[].class, "[J");
        map.put(long[][].class, "[[J");
        map.put(float[].class, "[F");
        map.put(float[][].class, "[[F");
        map.put(double[].class, "[D");
        map.put(double[][].class, "[[D");
        map.put(char[].class, "[C");
        map.put(char[][].class, "[[C");
        map.put(boolean[].class, "[Z");
        map.put(boolean[][].class, "[[Z");
        map.put(Byte[].class, "[Byte");
        map.put(Byte[][].class, "[[Byte");
        map.put(Short[].class, "[Short");
        map.put(Short[][].class, "[[Short");
        map.put(Integer[].class, "[Integer");
        map.put(Integer[][].class, "[[Integer");
        map.put(Long[].class, "[Long");
        map.put(Long[][].class, "[[Long");
        map.put(Float[].class, "[Float");
        map.put(Float[][].class, "[[Float");
        map.put(Double[].class, "[Double");
        map.put(Double[][].class, "[[Double");
        map.put(Character[].class, "[Character");
        map.put(Character[][].class, "[[Character");
        map.put(Boolean[].class, "[Boolean");
        map.put(Boolean[][].class, "[[Boolean");
        map.put(String[].class, "[String");
        map.put(String[][].class, "[[String");
        map.put(BigDecimal[].class, "[BigDecimal");
        map.put(BigDecimal[][].class, "[[BigDecimal");
        map.put(BigInteger[].class, "[BigInteger");
        map.put(BigInteger[][].class, "[[BigInteger");
        map.put(UUID[].class, "[UUID");
        map.put(UUID[][].class, "[[UUID");
        map.put(Object.class, "Object");
        map.put(HashMap.class, "M");
        Map<String, Class> map2 = TYPE_MAPPINGS;
        map2.put("HashMap", HashMap.class);
        map2.put("java.util.HashMap", HashMap.class);
        map.put(LinkedHashMap.class, "LM");
        map2.put("LinkedHashMap", LinkedHashMap.class);
        map2.put("java.util.LinkedHashMap", LinkedHashMap.class);
        map.put(TreeMap.class, "TM");
        map2.put("TreeMap", TreeMap.class);
        map.put(ArrayList.class, ExifInterface.GPS_MEASUREMENT_IN_PROGRESS);
        map2.put("ArrayList", ArrayList.class);
        map2.put("java.util.ArrayList", ArrayList.class);
        map.put(LinkedList.class, "LA");
        map2.put("LA", LinkedList.class);
        map2.put("LinkedList", LinkedList.class);
        map2.put("java.util.LinkedList", LinkedList.class);
        map2.put("java.util.concurrent.ConcurrentLinkedQueue", ConcurrentLinkedQueue.class);
        map2.put("java.util.concurrent.ConcurrentLinkedDeque", ConcurrentLinkedDeque.class);
        map.put(HashSet.class, "HashSet");
        map.put(TreeSet.class, "TreeSet");
        map.put(LinkedHashSet.class, "LinkedHashSet");
        map.put(ConcurrentHashMap.class, "ConcurrentHashMap");
        map.put(ConcurrentLinkedQueue.class, "ConcurrentLinkedQueue");
        map.put(ConcurrentLinkedDeque.class, "ConcurrentLinkedDeque");
        map.put(JSONObject.class, "JSONObject");
        map.put(JSONArray.class, "JSONArray");
        map.put(Currency.class, "Currency");
        map.put(TimeUnit.class, "TimeUnit");
        Class[] clsArr = {Object.class, Cloneable.class, AutoCloseable.class, Exception.class, RuntimeException.class, IllegalAccessError.class, IllegalAccessException.class, IllegalArgumentException.class, IllegalMonitorStateException.class, IllegalStateException.class, IllegalThreadStateException.class, IndexOutOfBoundsException.class, InstantiationError.class, InstantiationException.class, InternalError.class, InterruptedException.class, LinkageError.class, NegativeArraySizeException.class, NoClassDefFoundError.class, NoSuchFieldError.class, NoSuchFieldException.class, NoSuchMethodError.class, NoSuchMethodException.class, NullPointerException.class, NumberFormatException.class, OutOfMemoryError.class, SecurityException.class, StackOverflowError.class, StringIndexOutOfBoundsException.class, TypeNotPresentException.class, VerifyError.class, StackTraceElement.class, Hashtable.class, TreeMap.class, IdentityHashMap.class, WeakHashMap.class, HashSet.class, LinkedHashSet.class, TreeSet.class, LinkedList.class, TimeUnit.class, ConcurrentHashMap.class, AtomicInteger.class, AtomicLong.class, Collections.EMPTY_MAP.getClass(), Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Number.class, String.class, BigDecimal.class, BigInteger.class, BitSet.class, Calendar.class, Date.class, Locale.class, UUID.class, Currency.class, SimpleDateFormat.class, JSONObject.class, JSONArray.class, ConcurrentSkipListMap.class, ConcurrentSkipListSet.class};
        for (int i = 0; i < 68; i++) {
            Class cls = clsArr[i];
            Map<String, Class> map3 = TYPE_MAPPINGS;
            map3.put(cls.getSimpleName(), cls);
            map3.put(cls.getName(), cls);
            NAME_MAPPINGS.put(cls, cls.getSimpleName());
        }
        Map<String, Class> map4 = TYPE_MAPPINGS;
        map4.put("JO10", JSONObject1O.class);
        map4.put("[O", Object[].class);
        map4.put("[Ljava.lang.Object;", Object[].class);
        map4.put("[java.lang.Object", Object[].class);
        map4.put("[Object", Object[].class);
        map4.put("StackTraceElement", StackTraceElement.class);
        map4.put("[StackTraceElement", StackTraceElement[].class);
        String[] strArr = {"java.util.Collections$UnmodifiableMap", "java.util.Collections$UnmodifiableCollection"};
        for (int i2 = 0; i2 < 2; i2++) {
            Class clsLoadClass2 = loadClass(strArr[i2]);
            TYPE_MAPPINGS.put(clsLoadClass2.getName(), clsLoadClass2);
        }
        Class cls2 = CLASS_JSON_OBJECT_1x;
        if (cls2 != null) {
            Map<String, Class> map5 = TYPE_MAPPINGS;
            map5.putIfAbsent("JO1", cls2);
            map5.putIfAbsent(cls2.getName(), cls2);
        }
        Class cls3 = CLASS_JSON_ARRAY_1x;
        if (cls3 != null) {
            Map<String, Class> map6 = TYPE_MAPPINGS;
            map6.putIfAbsent("JA1", cls3);
            map6.putIfAbsent(cls3.getName(), cls3);
        }
        Map<Class, String> map7 = NAME_MAPPINGS;
        map7.put(new HashMap().keySet().getClass(), "Set");
        map7.put(new LinkedHashMap().keySet().getClass(), "Set");
        map7.put(new TreeMap().keySet().getClass(), "Set");
        map7.put(new ConcurrentHashMap().keySet().getClass(), "Set");
        map7.put(new ConcurrentSkipListMap().keySet().getClass(), "Set");
        Map<String, Class> map8 = TYPE_MAPPINGS;
        map8.put("Set", HashSet.class);
        map7.put(new HashMap().values().getClass(), "List");
        map7.put(new LinkedHashMap().values().getClass(), "List");
        map7.put(new TreeMap().values().getClass(), "List");
        map7.put(new ConcurrentHashMap().values().getClass(), "List");
        map7.put(new ConcurrentSkipListMap().values().getClass(), "List");
        map8.put("List", ArrayList.class);
        map8.put("java.util.ImmutableCollections$Map1", HashMap.class);
        map8.put("java.util.ImmutableCollections$MapN", LinkedHashMap.class);
        map8.put("java.util.ImmutableCollections$Set12", LinkedHashSet.class);
        map8.put("java.util.ImmutableCollections$SetN", LinkedHashSet.class);
        map8.put("java.util.ImmutableCollections$List12", ArrayList.class);
        map8.put("java.util.ImmutableCollections$ListN", ArrayList.class);
        map8.put("java.util.ImmutableCollections$SubList", ArrayList.class);
        for (Map.Entry<Class, String> entry : map7.entrySet()) {
            TYPE_MAPPINGS.putIfAbsent(entry.getValue(), entry.getKey());
        }
    }

    public static <T> T newProxyInstance(Class<T> cls, JSONObject jSONObject) {
        MethodHandle methodHandleFindStatic = METHOD_NEW_PROXY_INSTANCE;
        if (methodHandleFindStatic == null) {
            try {
                Class<?> cls2 = Class.forName("java.lang.reflect.Proxy");
                methodHandleFindStatic = JDKUtils.trustedLookup(cls2).findStatic(cls2, "newProxyInstance", MethodType.methodType(Object.class, ClassLoader.class, Class[].class, InvocationHandler.class));
                METHOD_NEW_PROXY_INSTANCE = methodHandleFindStatic;
            } catch (Throwable unused) {
                METHOD_NEW_PROXY_INSTANCE_ERROR = true;
            }
        }
        try {
            return (T) (Object) methodHandleFindStatic.invokeExact(cls.getClassLoader(), new Class[]{cls}, jSONObject);
        } catch (Throwable th) {
            throw new JSONException("create proxy error : " + cls, th);
        }
    }

    static class X1 {
        static final Function<byte[], char[]> TO_CHARS;

        X1() {
        }

        static {
            Function<byte[], char[]> functionInvokeExact;
            if (JDKUtils.JVM_VERSION > 9) {
                try {
                    Class<?> cls = Class.forName("java.lang.StringLatin1");
                    MethodHandles.Lookup lookupTrustedLookup = JDKUtils.trustedLookup(cls);
                    functionInvokeExact = (Function) LambdaMetafactory.metafactory(lookupTrustedLookup, "apply", MethodType.methodType(Function.class), MethodType.methodType((Class<?>) Object.class, (Class<?>) Object.class), lookupTrustedLookup.findStatic(cls, "toChars", MethodType.methodType((Class<?>) char[].class, (Class<?>) byte[].class)), MethodType.methodType((Class<?>) char[].class, (Class<?>) byte[].class)).getTarget().invokeExact();
                } catch (Throwable unused) {
                    functionInvokeExact = null;
                }
            } else {
                functionInvokeExact = null;
            }
            if (functionInvokeExact == null) {
                functionInvokeExact = new Function() { // from class: com.alibaba.fastjson2.util.TypeUtils$X1$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return TypeUtils.toAsciiCharArray((byte[]) obj);
                    }
                };
            }
            TO_CHARS = functionInvokeExact;
        }
    }

    static class X2 {
        static final char END = '~';
        static final int SIZE2 = 95;
        static final char START = ' ';
        static final String[] chars;
        static final String[] chars2;

        X2() {
        }

        static {
            String[] strArr = new String[128];
            for (char c = 0; c < 128; c = (char) (c + 1)) {
                strArr[c] = Character.toString(c);
            }
            chars = strArr;
            String[] strArr2 = new String[9025];
            for (char c2 = ' '; c2 <= '~'; c2 = (char) (c2 + 1)) {
                for (char c3 = ' '; c3 <= '~'; c3 = (char) (c3 + 1)) {
                    strArr2[((c2 - ' ') * 95) + (c3 - ' ')] = new String(new char[]{c2, c3});
                }
            }
            chars2 = strArr2;
        }
    }

    static char[] toAsciiCharArray(byte[] bArr) {
        char[] cArr = new char[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            cArr[i] = (char) bArr[i];
        }
        return cArr;
    }

    public static String toString(char c) {
        if (c < X2.chars.length) {
            return X2.chars[c];
        }
        return Character.toString(c);
    }

    public static String toString(byte b) {
        if (b >= 0 && b < X2.chars.length) {
            return X2.chars[b];
        }
        return new String(new byte[]{b}, StandardCharsets.ISO_8859_1);
    }

    public static String toString(char c, char c2) {
        if (c >= ' ' && c <= '~' && c2 >= ' ' && c2 <= '~') {
            return X2.chars2[((c - TokenParser.SP) * 95) + (c2 - TokenParser.SP)];
        }
        return new String(new char[]{c, c2});
    }

    public static String toString(byte b, byte b2) {
        if (b >= 32 && b <= 126 && b2 >= 32 && b2 <= 126) {
            return X2.chars2[((b - 32) * 95) + (b2 - 32)];
        }
        return new String(new byte[]{b, b2}, StandardCharsets.ISO_8859_1);
    }

    public static Type intern(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return type;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type rawType = parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return (rawType == List.class && actualTypeArguments.length == 1 && actualTypeArguments[0] == String.class) ? PARAM_TYPE_LIST_STR : type;
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x00e8 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x002d A[Catch: StringIndexOutOfBoundsException -> 0x011b, TRY_ENTER, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011b, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010e, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0113, B:100:0x011a), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0053 A[Catch: StringIndexOutOfBoundsException -> 0x011b, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011b, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010e, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0113, B:100:0x011a), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x009c A[Catch: StringIndexOutOfBoundsException -> 0x011b, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011b, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010e, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0113, B:100:0x011a), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x009e A[Catch: StringIndexOutOfBoundsException -> 0x011b, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011b, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010e, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0113, B:100:0x011a), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x00f6 A[Catch: StringIndexOutOfBoundsException -> 0x011b, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011b, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010e, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0113, B:100:0x011a), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x010e A[Catch: StringIndexOutOfBoundsException -> 0x011b, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011b, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010e, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0113, B:100:0x011a), top: B:104:0x0008 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static double parseDouble(byte[] r21, int r22, int r23) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instruction units count: 315
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.TypeUtils.parseDouble(byte[], int, int):double");
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x00ea A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x002d A[Catch: StringIndexOutOfBoundsException -> 0x011d, TRY_ENTER, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011d, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x0110, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0115, B:100:0x011c), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0053 A[Catch: StringIndexOutOfBoundsException -> 0x011d, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011d, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x0110, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0115, B:100:0x011c), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x009e A[Catch: StringIndexOutOfBoundsException -> 0x011d, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011d, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x0110, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0115, B:100:0x011c), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00a0 A[Catch: StringIndexOutOfBoundsException -> 0x011d, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011d, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x0110, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0115, B:100:0x011c), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x00f8 A[Catch: StringIndexOutOfBoundsException -> 0x011d, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011d, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x0110, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0115, B:100:0x011c), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0110 A[Catch: StringIndexOutOfBoundsException -> 0x011d, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011d, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x0110, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0115, B:100:0x011c), top: B:104:0x0008 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static double parseDouble(char[] r21, int r22, int r23) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instruction units count: 317
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.TypeUtils.parseDouble(char[], int, int):double");
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x00e8 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x002d A[Catch: StringIndexOutOfBoundsException -> 0x011a, TRY_ENTER, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011a, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010d, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0112, B:100:0x0119), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0053 A[Catch: StringIndexOutOfBoundsException -> 0x011a, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011a, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010d, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0112, B:100:0x0119), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x009c A[Catch: StringIndexOutOfBoundsException -> 0x011a, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011a, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010d, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0112, B:100:0x0119), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x009e A[Catch: StringIndexOutOfBoundsException -> 0x011a, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011a, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010d, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0112, B:100:0x0119), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x00f6 A[Catch: StringIndexOutOfBoundsException -> 0x011a, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011a, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010d, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0112, B:100:0x0119), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x010d A[Catch: StringIndexOutOfBoundsException -> 0x011a, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011a, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0082, B:36:0x0069, B:40:0x0079, B:42:0x007d, B:45:0x0088, B:46:0x008d, B:47:0x008e, B:54:0x009c, B:57:0x00a2, B:61:0x00ac, B:74:0x00d3, B:77:0x00df, B:79:0x00ea, B:83:0x00f3, B:89:0x0100, B:97:0x010d, B:84:0x00f6, B:68:0x00bf, B:55:0x009e, B:10:0x001b, B:99:0x0112, B:100:0x0119), top: B:104:0x0008 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static float parseFloat(byte[] r21, int r22, int r23) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instruction units count: 314
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.TypeUtils.parseFloat(byte[], int, int):float");
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x00ea A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x002d A[Catch: StringIndexOutOfBoundsException -> 0x011c, TRY_ENTER, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011c, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x010f, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0114, B:100:0x011b), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0053 A[Catch: StringIndexOutOfBoundsException -> 0x011c, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011c, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x010f, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0114, B:100:0x011b), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x009e A[Catch: StringIndexOutOfBoundsException -> 0x011c, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011c, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x010f, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0114, B:100:0x011b), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00a0 A[Catch: StringIndexOutOfBoundsException -> 0x011c, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011c, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x010f, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0114, B:100:0x011b), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x00f8 A[Catch: StringIndexOutOfBoundsException -> 0x011c, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011c, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x010f, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0114, B:100:0x011b), top: B:104:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x010f A[Catch: StringIndexOutOfBoundsException -> 0x011c, TryCatch #0 {StringIndexOutOfBoundsException -> 0x011c, blocks: (B:4:0x000a, B:11:0x001e, B:15:0x002d, B:17:0x0031, B:24:0x0041, B:20:0x0038, B:22:0x003c, B:25:0x0046, B:26:0x004b, B:30:0x0053, B:33:0x005b, B:44:0x0084, B:36:0x006a, B:40:0x007b, B:42:0x007f, B:45:0x008a, B:46:0x008f, B:47:0x0090, B:54:0x009e, B:57:0x00a4, B:61:0x00ae, B:74:0x00d5, B:77:0x00e1, B:79:0x00ec, B:83:0x00f5, B:89:0x0102, B:97:0x010f, B:84:0x00f8, B:68:0x00c1, B:55:0x00a0, B:10:0x001b, B:99:0x0114, B:100:0x011b), top: B:104:0x0008 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static float parseFloat(char[] r21, int r22, int r23) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instruction units count: 316
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.TypeUtils.parseFloat(char[], int, int):float");
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x00ca A[PHI: r18
      0x00ca: PHI (r18v2 double) = (r18v0 double), (r18v4 double) binds: [B:84:0x0101, B:58:0x00b4] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static double doubleValue(boolean r20, int r21, byte[] r22, int r23) {
        /*
            Method dump skipped, instruction units count: 494
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.TypeUtils.doubleValue(boolean, int, byte[], int):double");
    }

    public static float floatValue(boolean z, int i, byte[] bArr, int i2) {
        int i3;
        int i4;
        int i5;
        FDBigInteger fDBigIntegerRightInplaceSub;
        boolean z2;
        int i6 = i2;
        int length = SINGLE_SMALL_10_POW.length - 1;
        int i7 = 8;
        int iMin = Math.min(i6, 8);
        int i8 = bArr[0] + JSONB.Constants.BC_INT64_BYTE_ZERO;
        for (int i9 = 1; i9 < iMin; i9++) {
            i8 = ((i8 * 10) + bArr[i9]) - 48;
        }
        float f = i8;
        int i10 = i - iMin;
        if (i6 <= 7) {
            if (i10 == 0 || f == 0.0f) {
                return z ? -f : f;
            }
            if (i10 >= 0) {
                if (i10 <= length) {
                    float f2 = f * SINGLE_SMALL_10_POW[i10];
                    return z ? -f2 : f2;
                }
                int i11 = 7 - iMin;
                if (i10 <= length + i11) {
                    float[] fArr = SINGLE_SMALL_10_POW;
                    float f3 = f * fArr[i11] * fArr[i10 - i11];
                    return z ? -f3 : f3;
                }
            } else if (i10 >= (-length)) {
                float f4 = f / SINGLE_SMALL_10_POW[-i10];
                return z ? -f4 : f4;
            }
        } else if (i >= i6 && i6 + i <= 15) {
            long j = i8;
            while (iMin < i6) {
                j = (j * 10) + ((long) (bArr[iMin] + JSONB.Constants.BC_INT64_BYTE_ZERO));
                iMin++;
            }
            float f5 = (float) (j * SMALL_10_POW[i - i6]);
            return z ? -f5 : f5;
        }
        double d = f;
        if (i10 > 0) {
            if (i > 39) {
                return z ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
            }
            int i12 = i10 & 15;
            if (i12 != 0) {
                d *= SMALL_10_POW[i12];
            }
            int i13 = i10 >> 4;
            if (i13 != 0) {
                int i14 = 0;
                while (i13 > 0) {
                    if ((i13 & 1) != 0) {
                        d *= BIG_10_POW[i14];
                    }
                    i14++;
                    i13 >>= 1;
                }
            }
        } else if (i10 < 0) {
            int i15 = -i10;
            if (i < -46) {
                return z ? -0.0f : 0.0f;
            }
            int i16 = i15 & 15;
            if (i16 != 0) {
                d /= SMALL_10_POW[i16];
            }
            int i17 = i15 >> 4;
            if (i17 != 0) {
                int i18 = 0;
                while (i17 > 0) {
                    if ((i17 & 1) != 0) {
                        d *= TINY_10_POW[i18];
                    }
                    i18++;
                    i17 >>= 1;
                }
            }
        }
        float fMax = Math.max(Float.MIN_VALUE, Math.min(Float.MAX_VALUE, (float) d));
        if (i6 > 200) {
            bArr[200] = 49;
            i6 = HttpStatus.SC_CREATED;
        }
        int i19 = i6;
        FDBigInteger fDBigInteger = new FDBigInteger(i8, bArr, iMin, i19);
        int i20 = i - i19;
        int iFloatToRawIntBits = Float.floatToRawIntBits(fMax);
        int iMax = Math.max(0, -i20);
        int iMax2 = Math.max(0, i20);
        FDBigInteger fDBigIntegerMultByPow52 = fDBigInteger.multByPow52(iMax2, 0);
        fDBigIntegerMultByPow52.makeImmutable();
        FDBigInteger fDBigIntegerLeftShift = null;
        int i21 = 0;
        while (true) {
            int i22 = iFloatToRawIntBits >>> 23;
            int i23 = 8388607 & iFloatToRawIntBits;
            if (i22 > 0) {
                i3 = i23 | 8388608;
            } else {
                int iNumberOfLeadingZeros = Integer.numberOfLeadingZeros(i23) - i7;
                i3 = i23 << iNumberOfLeadingZeros;
                i22 = 1 - iNumberOfLeadingZeros;
            }
            int i24 = i22 - 127;
            int iNumberOfTrailingZeros = Integer.numberOfTrailingZeros(i3);
            int i25 = i3 >>> iNumberOfTrailingZeros;
            int i26 = (i22 - 150) + iNumberOfTrailingZeros;
            int i27 = 24 - iNumberOfTrailingZeros;
            if (i26 >= 0) {
                i5 = iMax + i26;
                i4 = iMax2;
            } else {
                i4 = iMax2 - i26;
                i5 = iMax;
            }
            int i28 = i24 <= -127 ? i24 + iNumberOfTrailingZeros + 127 : iNumberOfTrailingZeros + 1;
            int i29 = i5 + i28;
            int i30 = i4 + i28;
            int iMin2 = Math.min(i29, Math.min(i30, i5));
            int i31 = i30 - iMin2;
            int i32 = i5 - iMin2;
            FDBigInteger fDBigIntegerValueOfMulPow52 = FDBigInteger.valueOfMulPow52(i25, iMax, i29 - iMin2);
            if (fDBigIntegerLeftShift == null || i21 != i31) {
                fDBigIntegerLeftShift = fDBigIntegerMultByPow52.leftShift(i31);
                i21 = i31;
            }
            int iCmp = fDBigIntegerValueOfMulPow52.cmp(fDBigIntegerLeftShift);
            if (iCmp > 0) {
                fDBigIntegerRightInplaceSub = fDBigIntegerValueOfMulPow52.leftInplaceSub(fDBigIntegerLeftShift);
                if (i27 != 1 || i26 <= -126 || i32 - 1 >= 0) {
                    z2 = true;
                } else {
                    fDBigIntegerRightInplaceSub = fDBigIntegerRightInplaceSub.leftShift(1);
                    z2 = true;
                    i32 = 0;
                }
            } else {
                if (iCmp >= 0) {
                    break;
                }
                fDBigIntegerRightInplaceSub = fDBigIntegerLeftShift.rightInplaceSub(fDBigIntegerValueOfMulPow52);
                z2 = false;
            }
            int iCmpPow52 = fDBigIntegerRightInplaceSub.cmpPow52(iMax, i32);
            if (iCmpPow52 < 0) {
                break;
            }
            if (iCmpPow52 != 0) {
                iFloatToRawIntBits += z2 ? -1 : 1;
                if (iFloatToRawIntBits == 0 || iFloatToRawIntBits == 2139095040) {
                    break;
                }
                i7 = 8;
            } else if ((iFloatToRawIntBits & 1) != 0) {
                iFloatToRawIntBits += z2 ? -1 : 1;
            }
        }
        if (z) {
            iFloatToRawIntBits |= Integer.MIN_VALUE;
        }
        return Float.intBitsToFloat(iFloatToRawIntBits);
    }

    static class Cache {
        volatile char[] chars;

        Cache() {
        }
    }

    public static Class<?> getMapping(Type type) {
        if (type == null) {
            return null;
        }
        if (type.getClass() == Class.class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getMapping(((ParameterizedType) type).getRawType());
        }
        if (type instanceof TypeVariable) {
            Type type2 = ((TypeVariable) type).getBounds()[0];
            if (type2 instanceof Class) {
                return (Class) type2;
            }
            return getMapping(type2);
        }
        if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length == 1) {
                return getMapping(upperBounds[0]);
            }
        }
        if (type instanceof GenericArrayType) {
            return getArrayClass(getClass(((GenericArrayType) type).getGenericComponentType()));
        }
        return Object.class;
    }

    /* JADX WARN: Type inference failed for: r3v9, types: [java.time.ZonedDateTime] */
    public static Date toDate(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Date) {
            return (Date) obj;
        }
        if (obj instanceof Instant) {
            return new Date(((Instant) obj).toEpochMilli());
        }
        if (obj instanceof ZonedDateTime) {
            return new Date(((ZonedDateTime) obj).toInstant().toEpochMilli());
        }
        if (obj instanceof LocalDate) {
            return new Date(((LocalDate) obj).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (obj instanceof LocalDateTime) {
            return new Date(((LocalDateTime) obj).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (obj instanceof String) {
            return DateUtils.parseDate((String) obj);
        }
        if ((obj instanceof Long) || (obj instanceof Integer)) {
            return new Date(((Number) obj).longValue());
        }
        if (obj instanceof Map) {
            Object obj2 = ((Map) obj).get("$date");
            if (obj2 instanceof String) {
                return DateUtils.parseDate((String) obj2);
            }
        }
        throw new JSONException("can not cast to Date from " + obj.getClass());
    }

    public static Instant toInstant(Object obj) {
        JSONReader jSONReaderOf;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Instant) {
            return (Instant) obj;
        }
        if (obj instanceof Date) {
            return ((Date) obj).toInstant();
        }
        if (obj instanceof ZonedDateTime) {
            return ((ZonedDateTime) obj).toInstant();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            if (str.charAt(0) != '\"') {
                jSONReaderOf = JSONReader.of("\"" + str + '\"');
            } else {
                jSONReaderOf = JSONReader.of(str);
            }
            return (Instant) jSONReaderOf.read(Instant.class);
        }
        if (obj instanceof Map) {
            return (Instant) ObjectReaderImplInstant.INSTANCE.createInstance((Map) obj, 0L);
        }
        throw new JSONException("can not cast to Date from " + obj.getClass());
    }

    public static Object[] cast(Object obj, Type[] typeArr) {
        if (obj == null) {
            return null;
        }
        int length = typeArr.length;
        Object[] objArr = new Object[length];
        int i = 0;
        if (obj instanceof Collection) {
            Iterator it = ((Collection) obj).iterator();
            while (it.hasNext()) {
                objArr[i] = cast(it.next(), typeArr[i]);
                i++;
            }
        } else {
            Class<?> cls = obj.getClass();
            if (cls.isArray()) {
                int length2 = Array.getLength(obj);
                while (i < length && i < length2) {
                    objArr[i] = cast(Array.get(obj, i), typeArr[i]);
                    i++;
                }
            } else {
                throw new JSONException("can not cast to types " + JSON.toJSONString(typeArr) + " from " + cls);
            }
        }
        return objArr;
    }

    public static String[] toStringArray(Object obj) {
        if (obj == null || (obj instanceof String[])) {
            return (String[]) obj;
        }
        int i = 0;
        if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            String[] strArr = new String[collection.size()];
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                int i2 = i + 1;
                strArr[i] = (next == null || (next instanceof String)) ? (String) next : next.toString();
                i = i2;
            }
            return strArr;
        }
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            String[] strArr2 = new String[length];
            while (i < length) {
                Object obj2 = Array.get(obj, i);
                strArr2[i] = (obj2 == null || (obj2 instanceof String)) ? (String) obj2 : obj2.toString();
                i++;
            }
            return strArr2;
        }
        return (String[]) cast(obj, String[].class);
    }

    public static <T> T cast(Object obj, Type type) {
        return (T) cast(obj, type, JSONFactory.getDefaultObjectReaderProvider());
    }

    public static <T> T cast(Object obj, Type type, ObjectReaderProvider objectReaderProvider) {
        if (type instanceof Class) {
            return (T) cast(obj, (Class) type, objectReaderProvider);
        }
        if (obj instanceof Collection) {
            return (T) objectReaderProvider.getObjectReader(type).createInstance((Collection) obj);
        }
        if (obj instanceof Map) {
            return (T) objectReaderProvider.getObjectReader(type).createInstance((Map) obj, 0L);
        }
        return (T) JSON.parseObject(JSON.toJSONString(obj), type);
    }

    public static <T> T cast(Object obj, Class<T> cls) {
        return (T) cast(obj, (Class) cls, JSONFactory.getDefaultObjectReaderProvider());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T cast(Object obj, Class<T> cls, ObjectReaderProvider objectReaderProvider) {
        long jLongValue;
        Function function;
        Function function2;
        Function typeConvert;
        JSONReader jSONReaderOf;
        if (obj == 0) {
            return null;
        }
        if (cls.isInstance(obj)) {
            return obj;
        }
        if (cls == Date.class) {
            return (T) toDate(obj);
        }
        if (cls == Instant.class) {
            return (T) toInstant(obj);
        }
        if (cls == String.class) {
            if (obj instanceof Character) {
                return (T) obj.toString();
            }
            return (T) JSON.toJSONString(obj);
        }
        if (cls == AtomicInteger.class) {
            return (T) new AtomicInteger(toIntValue(obj));
        }
        if (cls == AtomicLong.class) {
            return (T) new AtomicLong(toLongValue(obj));
        }
        if (cls == AtomicBoolean.class) {
            return (T) new AtomicBoolean(((Boolean) obj).booleanValue());
        }
        if (obj instanceof Map) {
            return (T) objectReaderProvider.getObjectReader(cls).createInstance((Map) obj, 0L);
        }
        Function typeConvert2 = objectReaderProvider.getTypeConvert(obj.getClass(), cls);
        if (typeConvert2 != null) {
            return (T) typeConvert2.apply(obj);
        }
        if (cls.isEnum()) {
            ObjectReader objectReader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(cls);
            if (objectReader instanceof ObjectReaderImplEnum) {
                if (obj instanceof Integer) {
                    return (T) ((ObjectReaderImplEnum) objectReader).of(((Integer) obj).intValue());
                }
            } else {
                return (T) objectReader.readObject(JSONReader.of(JSON.toJSONString(obj)), cls, null, 0L);
            }
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            char cCharAt = str.trim().charAt(0);
            if (cCharAt == '\"' || cCharAt == '{' || cCharAt == '[') {
                jSONReaderOf = JSONReader.of(str);
            } else {
                jSONReaderOf = JSONReader.of(JSON.toJSONString(str));
            }
            return (T) JSONFactory.getDefaultObjectReaderProvider().getObjectReader(cls).readObject(jSONReaderOf, cls, null, 0L);
        }
        if (obj instanceof Collection) {
            return (T) objectReaderProvider.getObjectReader(cls).createInstance((Collection) obj);
        }
        String name = cls.getName();
        if ((obj instanceof Integer) || (obj instanceof Long)) {
            jLongValue = ((Number) obj).longValue();
            name.hashCode();
            switch (name) {
                case "java.time.LocalDateTime":
                    return (T) LocalDateTime.ofInstant(Instant.ofEpochMilli(jLongValue), DateUtils.DEFAULT_ZONE_ID);
                case "java.sql.Date":
                    return (T) JdbcSupport.createDate(jLongValue);
                case "java.sql.Time":
                    return (T) JdbcSupport.createTime(jLongValue);
                case "java.sql.Timestamp":
                    return (T) JdbcSupport.createTimestamp(jLongValue);
            }
        }
        if (obj.getClass().getName().equals("org.bson.types.Decimal128") && cls == Double.class) {
            ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter((Class) obj.getClass());
            if ((objectWriter instanceof ObjectWriterPrimitiveImpl) && (function2 = ((ObjectWriterPrimitiveImpl) objectWriter).getFunction()) != null && (typeConvert = objectReaderProvider.getTypeConvert(function2.apply(obj).getClass(), cls)) != null) {
                return (T) typeConvert.apply(obj);
            }
        }
        ObjectWriter objectWriter2 = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter((Class) obj.getClass());
        if ((objectWriter2 instanceof ObjectWriterPrimitiveImpl) && (function = ((ObjectWriterPrimitiveImpl) objectWriter2).getFunction()) != null) {
            T t = (T) function.apply(obj);
            if (cls.isInstance(t)) {
                return t;
            }
        }
        throw new JSONException("can not cast to " + name + ", from " + obj.getClass());
    }

    public static String getTypeName(Class cls) {
        Map<Class, String> map = NAME_MAPPINGS;
        String str = map.get(cls);
        if (str != null) {
            return str;
        }
        if (Proxy.isProxyClass(cls)) {
            Class<?>[] interfaces = cls.getInterfaces();
            if (interfaces.length > 0) {
                cls = interfaces[0];
            }
        }
        String typeName = cls.getTypeName();
        typeName.hashCode();
        if (typeName.equals("com.alibaba.fastjson.JSONObject")) {
            map.putIfAbsent(cls, "JO1");
            return map.get(cls);
        }
        if (typeName.equals("com.alibaba.fastjson.JSONArray")) {
            map.putIfAbsent(cls, "JA1");
            return map.get(cls);
        }
        int iIndexOf = typeName.indexOf(36);
        if (iIndexOf != -1 && isInteger(typeName.substring(iIndexOf + 1))) {
            Class superclass = cls.getSuperclass();
            if (Map.class.isAssignableFrom(superclass)) {
                return getTypeName(superclass);
            }
        }
        return typeName;
    }

    public static Class getMapping(String str) {
        return TYPE_MAPPINGS.get(str);
    }

    public static BigDecimal toBigDecimal(Object obj) {
        if (obj == null || (obj instanceof BigDecimal)) {
            return (BigDecimal) obj;
        }
        if ((obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Integer) || (obj instanceof Long)) {
            return BigDecimal.valueOf(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return new BigDecimal(str);
        }
        return (BigDecimal) cast(obj, BigDecimal.class);
    }

    public static BigDecimal toBigDecimal(long j) {
        return BigDecimal.valueOf(j);
    }

    public static BigDecimal toBigDecimal(float f) {
        byte[] bArr = new byte[15];
        return parseBigDecimal(bArr, 0, NumberUtils.writeFloat(bArr, 0, f, true));
    }

    public static BigDecimal toBigDecimal(double d) {
        byte[] bArr = new byte[24];
        return parseBigDecimal(bArr, 0, NumberUtils.writeDouble(bArr, 0, d, true));
    }

    public static BigDecimal toBigDecimal(String str) {
        if (str == null || str.isEmpty() || "null".equals(str)) {
            return null;
        }
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_CODER.applyAsInt(str) == JDKUtils.LATIN1.byteValue() && JDKUtils.STRING_VALUE != null) {
            byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
            return parseBigDecimal(bArrApply, 0, bArrApply.length);
        }
        char[] charArray = JDKUtils.getCharArray(str);
        return parseBigDecimal(charArray, 0, charArray.length);
    }

    public static BigDecimal toBigDecimal(char[] cArr) {
        if (cArr == null) {
            return null;
        }
        return parseBigDecimal(cArr, 0, cArr.length);
    }

    public static BigDecimal toBigDecimal(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return parseBigDecimal(bArr, 0, bArr.length);
    }

    public static boolean isInt32(BigInteger bigInteger) {
        int i;
        if (JDKUtils.FIELD_BIGINTEGER_MAG_OFFSET == -1) {
            return bigInteger.compareTo(BIGINT_INT32_MIN) >= 0 && bigInteger.compareTo(BIGINT_INT32_MAX) <= 0;
        }
        int[] iArr = (int[]) JDKUtils.UNSAFE.getObject(bigInteger, JDKUtils.FIELD_BIGINTEGER_MAG_OFFSET);
        return iArr.length == 0 || (iArr.length == 1 && ((i = iArr[0]) >= 0 || (i == Integer.MIN_VALUE && bigInteger.signum() == -1)));
    }

    public static boolean isInt64(BigInteger bigInteger) {
        if (JDKUtils.FIELD_BIGINTEGER_MAG_OFFSET != -1) {
            int[] iArr = (int[]) JDKUtils.UNSAFE.getObject(bigInteger, JDKUtils.FIELD_BIGINTEGER_MAG_OFFSET);
            if (iArr.length <= 1) {
                return true;
            }
            if (iArr.length == 2) {
                int i = iArr[0];
                return i >= 0 || (i == Integer.MIN_VALUE && iArr[1] == 0 && bigInteger.signum() == -1);
            }
        }
        return bigInteger.compareTo(BIGINT_INT64_MIN) >= 0 && bigInteger.compareTo(BIGINT_INT64_MAX) <= 0;
    }

    public static boolean isInteger(BigDecimal bigDecimal) {
        int iScale = bigDecimal.scale();
        if (iScale == 0) {
            return true;
        }
        if (bigDecimal.precision() < 20 && JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET != -1) {
            long j = JDKUtils.UNSAFE.getLong(bigDecimal, JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET);
            switch (iScale) {
                case 1:
                    if (j % 10 == 0) {
                    }
                    break;
                case 2:
                    if (j % 100 == 0) {
                    }
                    break;
                case 3:
                    if (j % 1000 == 0) {
                    }
                    break;
                case 4:
                    if (j % 10000 == 0) {
                    }
                    break;
                case 5:
                    if (j % 100000 == 0) {
                    }
                    break;
                case 6:
                    if (j % 1000000 == 0) {
                    }
                    break;
                case 7:
                    if (j % 10000000 == 0) {
                    }
                    break;
                case 8:
                    if (j % 100000000 == 0) {
                    }
                    break;
                case 9:
                    if (j % 1000000000 == 0) {
                    }
                    break;
            }
            return true;
        }
        return JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0.m(bigDecimal).scale() == 0;
    }

    public static BigInteger toBigInteger(Object obj) {
        if (obj == null || (obj instanceof BigInteger)) {
            return (BigInteger) obj;
        }
        if ((obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Integer) || (obj instanceof Long)) {
            return BigInteger.valueOf(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return new BigInteger(str);
        }
        throw new JSONException("can not cast to bigint");
    }

    public static Long toLong(Object obj) {
        if (obj == null || (obj instanceof Long)) {
            return (Long) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
        }
        return Long.valueOf(toLongValue(obj));
    }

    public static long toLongValue(Object obj) {
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return 0L;
            }
            try {
                if (str.lastIndexOf(44) == str.length() - 4 && str.indexOf(46) == -1) {
                    return NumberFormat.getNumberInstance().parse(str).longValue();
                }
            } catch (ParseException unused) {
            }
            if (IOUtils.isNumber(str)) {
                return Long.parseLong(str);
            }
            throw new JSONException("parseLong error " + str);
        }
        throw new JSONException("can not cast to long from " + obj.getClass());
    }

    public static Boolean parseBoolean(byte[] bArr, int i, int i2) {
        byte b;
        if (i2 == 0) {
            return null;
        }
        boolean z = true;
        if ((i2 != 1 || ((b = bArr[i]) != 49 && b != 89)) && (i2 != 4 || (IOUtils.getIntUnaligned(bArr, i) | 538976288) != IOUtils.TRUE)) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public static Boolean parseBoolean(char[] cArr, int i, int i2) {
        char c;
        if (i2 == 0) {
            return null;
        }
        boolean z = true;
        if ((i2 != 1 || ((c = cArr[i]) != '1' && c != 'Y')) && (i2 != 4 || (IOUtils.getLongLE(cArr, i) | 9007336695791648L) != 28429475166421108L)) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public static int parseInt(byte[] bArr, int i, int i2) {
        int i3;
        int iDigit2;
        int i4 = i + i2;
        int i5 = i + 1;
        byte b = bArr[i];
        int i6 = IOUtils.isDigit(b) ? 48 - b : (i2 == 1 || !(b == 45 || b == 43)) ? 1 : 0;
        while (true) {
            i3 = i5 + 1;
            if (i3 < i4 && (iDigit2 = IOUtils.digit2(bArr, i5)) != -1) {
                if (!(-21474836 <= i6) || !(i6 <= 0)) {
                    break;
                }
                i6 = (i6 * 100) - iDigit2;
                i5 += 2;
            } else {
                break;
            }
        }
        if (i5 < i4) {
            byte b2 = bArr[i5];
            if (IOUtils.isDigit(b2)) {
                if ((-214748364 <= i6) & (i6 <= 0)) {
                    i6 = ((i6 * 10) + 48) - b2;
                    i5 = i3;
                }
            }
        }
        if (((i5 == i4) & (i6 <= 0)) && (Integer.MIN_VALUE < i6 || b == 45)) {
            return b == 45 ? i6 : -i6;
        }
        throw new NumberFormatException("parseInt error ".concat(new String(bArr, i5, i2)));
    }

    public static int parseInt(char[] cArr, int i, int i2) {
        int i3;
        int iDigit2;
        int i4 = i + i2;
        int i5 = i + 1;
        char c = cArr[i];
        int i6 = IOUtils.isDigit(c) ? '0' - c : (i2 == 1 || !(c == '-' || c == '+')) ? 1 : 0;
        while (true) {
            i3 = i5 + 1;
            if (i3 < i4 && (iDigit2 = IOUtils.digit2(cArr, i5)) != -1) {
                if (!(-21474836 <= i6) || !(i6 <= 0)) {
                    break;
                }
                i6 = (i6 * 100) - iDigit2;
                i5 += 2;
            } else {
                break;
            }
        }
        if (i5 < i4) {
            char c2 = cArr[i5];
            if (IOUtils.isDigit(c2)) {
                if ((-214748364 <= i6) & (i6 <= 0)) {
                    i6 = ((i6 * 10) + 48) - c2;
                    i5 = i3;
                }
            }
        }
        if (((i5 == i4) & (i6 <= 0)) && (Integer.MIN_VALUE < i6 || c == '-')) {
            return c == '-' ? i6 : -i6;
        }
        throw new NumberFormatException("parseInt error ".concat(new String(cArr, i5, i2)));
    }

    public static long parseLong(byte[] bArr, int i, int i2) {
        int i3;
        int iDigit2;
        int i4 = i + i2;
        int i5 = i + 1;
        byte b = bArr[i];
        boolean z = true;
        long j = IOUtils.isDigit(b) ? 48 - b : (i2 == 1 || !(b == 45 || b == 43)) ? 1L : 0L;
        while (true) {
            i3 = i5 + 1;
            if (i3 < i4 && (iDigit2 = IOUtils.digit2(bArr, i5)) != -1) {
                if (!(-92233720368547758L <= j) || !(j <= 0)) {
                    break;
                }
                j = (j * 100) - ((long) iDigit2);
                i5 += 2;
            } else {
                break;
            }
        }
        if (i5 < i4) {
            byte b2 = bArr[i5];
            if (IOUtils.isDigit(b2)) {
                if ((BufferKt.OVERFLOW_ZONE <= j) & (j <= 0)) {
                    j = ((j * 10) + 48) - ((long) b2);
                    i5 = i3;
                }
            }
        }
        boolean z2 = (i5 == i4) & (j <= 0);
        if (Long.MIN_VALUE >= j && b != 45) {
            z = false;
        }
        if (z2 && z) {
            return b == 45 ? j : -j;
        }
        throw new NumberFormatException("parseInt error ".concat(new String(bArr, i5, i2)));
    }

    public static long parseLong(char[] cArr, int i, int i2) {
        int i3;
        int iDigit2;
        int i4 = i + i2;
        int i5 = i + 1;
        char c = cArr[i];
        boolean z = true;
        long j = IOUtils.isDigit(c) ? '0' - c : (i2 == 1 || !(c == '-' || c == '+')) ? 1L : 0L;
        while (true) {
            i3 = i5 + 1;
            if (i3 < i4 && (iDigit2 = IOUtils.digit2(cArr, i5)) != -1) {
                if (!(-92233720368547758L <= j) || !(j <= 0)) {
                    break;
                }
                j = (j * 100) - ((long) iDigit2);
                i5 += 2;
            } else {
                break;
            }
        }
        if (i5 < i4) {
            char c2 = cArr[i5];
            if (IOUtils.isDigit(c2)) {
                if ((BufferKt.OVERFLOW_ZONE <= j) & (j <= 0)) {
                    j = ((j * 10) + 48) - ((long) c2);
                    i5 = i3;
                }
            }
        }
        boolean z2 = (i5 == i4) & (j <= 0);
        if (Long.MIN_VALUE >= j && c != '-') {
            z = false;
        }
        if (z2 && z) {
            return c == '-' ? j : -j;
        }
        throw new NumberFormatException("parseInt error ".concat(new String(cArr, i5, i2)));
    }

    public static BigDecimal parseBigDecimal(char[] cArr, int i, int i2) {
        int i3;
        boolean z;
        if (cArr == null || i2 == 0) {
            return null;
        }
        if (cArr[i] == '-') {
            i3 = i + 1;
            z = true;
        } else {
            i3 = i;
            z = false;
        }
        if (i2 <= 20 || (z && i2 == 21)) {
            int i4 = i + i2;
            int i5 = -1;
            long j = 0;
            int i6 = 0;
            while (i3 < i4) {
                char c = cArr[i3];
                if (c != '.') {
                    if (c >= '0' && c <= '9') {
                        long j2 = j * 10;
                        if (((j | 10) >>> 31) == 0 || j2 / 10 == j) {
                            j = j2 + ((long) (c - '0'));
                            i3++;
                        }
                    }
                    j = -1;
                    break;
                }
                i6++;
                if (i6 > 1) {
                    break;
                }
                i5 = i3;
                i3++;
            }
            if (j >= 0 && i6 <= 1) {
                if (z) {
                    j = -j;
                }
                return BigDecimal.valueOf(j, i5 != -1 ? (i2 - (i5 - i)) - 1 : 0);
            }
        }
        return new BigDecimal(cArr, i, i2);
    }

    public static BigDecimal parseBigDecimal(byte[] bArr, int i, int i2) {
        int i3;
        boolean z;
        long j;
        char[] cArrApply;
        if (bArr == null || i2 == 0) {
            return null;
        }
        if (bArr[i] == 45) {
            i3 = i + 1;
            z = true;
        } else {
            i3 = i;
            z = false;
        }
        if (i2 <= 20 || (z && i2 == 21)) {
            int i4 = i + i2;
            int i5 = 0;
            int i6 = -1;
            long j2 = 0;
            while (i3 < i4) {
                byte b = bArr[i3];
                if (b != 46) {
                    j = 0;
                    if (b >= 48 && b <= 57) {
                        long j3 = j2 * 10;
                        if (((j2 | 10) >>> 31) == 0 || j3 / 10 == j2) {
                            j2 = j3 + ((long) (b + JSONB.Constants.BC_INT64_BYTE_ZERO));
                        }
                    }
                    j2 = -1;
                    break;
                }
                i5++;
                if (i5 > 1) {
                    break;
                }
                i6 = i3;
                i3++;
            }
            j = 0;
            if (j2 >= j && i5 <= 1) {
                if (z) {
                    j2 = -j2;
                }
                return BigDecimal.valueOf(j2, i6 != -1 ? (i2 - (i6 - i)) - 1 : 0);
            }
        }
        if (i == 0 && i2 == bArr.length) {
            cArrApply = X1.TO_CHARS.apply(bArr);
        } else {
            char[] cArr = new char[i2];
            for (int i7 = 0; i7 < i2; i7++) {
                cArr[i7] = (char) bArr[i + i7];
            }
            cArrApply = cArr;
        }
        return new BigDecimal(cArrApply, 0, cArrApply.length);
    }

    public static Integer toInteger(Object obj) {
        if (obj == null || (obj instanceof Integer)) {
            return (Integer) obj;
        }
        if (obj instanceof Number) {
            return Integer.valueOf(((Number) obj).intValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return Integer.valueOf(Integer.parseInt(str));
        }
        if ((obj instanceof Map) && ((Map) obj).isEmpty()) {
            return null;
        }
        if (obj instanceof Boolean) {
            return Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        }
        throw new JSONException("can not cast to integer");
    }

    public static Byte toByte(Object obj) {
        if (obj == null || (obj instanceof Byte)) {
            return (Byte) obj;
        }
        if (obj instanceof Number) {
            return Byte.valueOf(((Number) obj).byteValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return Byte.valueOf(Byte.parseByte(str));
        }
        throw new JSONException("can not cast to byte");
    }

    public static byte toByteValue(Object obj) {
        if (obj == null) {
            return (byte) 0;
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).byteValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return (byte) 0;
            }
            return Byte.parseByte(str);
        }
        throw new JSONException("can not cast to byte");
    }

    public static Short toShort(Object obj) {
        if (obj == null || (obj instanceof Short)) {
            return (Short) obj;
        }
        if (obj instanceof Number) {
            return Short.valueOf(((Number) obj).shortValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return Short.valueOf(Short.parseShort(str));
        }
        throw new JSONException("can not cast to byte");
    }

    public static short toShortValue(Object obj) {
        if (obj == null) {
            return (short) 0;
        }
        if (obj instanceof Short) {
            return ((Short) obj).shortValue();
        }
        if (obj instanceof Number) {
            return (byte) ((Number) obj).shortValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return (short) 0;
            }
            return Short.parseShort(str);
        }
        throw new JSONException("can not cast to byte");
    }

    public static int toIntValue(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return 0;
            }
            try {
                if (str.lastIndexOf(44) == str.length() - 4 && str.indexOf(46) == -1) {
                    return NumberFormat.getNumberInstance().parse(str).intValue();
                }
            } catch (ParseException unused) {
            }
            if (IOUtils.isNumber(str)) {
                return Integer.parseInt(str);
            }
            throw new JSONException("parseInt error, " + str);
        }
        throw new JSONException("can not cast to int");
    }

    public static boolean toBooleanValue(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return false;
            }
            return Boolean.parseBoolean(str);
        }
        if (obj instanceof Number) {
            int iIntValue = ((Number) obj).intValue();
            if (iIntValue == 1) {
                return true;
            }
            if (iIntValue == 0) {
                return false;
            }
        }
        throw new JSONException("can not cast to boolean");
    }

    public static Boolean toBoolean(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return Boolean.valueOf(Boolean.parseBoolean(str));
        }
        if (obj instanceof Number) {
            int iIntValue = ((Number) obj).intValue();
            if (iIntValue == 1) {
                return true;
            }
            if (iIntValue == 0) {
                return false;
            }
        }
        throw new JSONException("can not cast to boolean");
    }

    public static float toFloatValue(Object obj) {
        if (obj == null) {
            return 0.0f;
        }
        if (obj instanceof Float) {
            return ((Float) obj).floatValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return 0.0f;
            }
            return Float.parseFloat(str);
        }
        throw new JSONException("can not cast to decimal");
    }

    public static Float toFloat(Object obj) {
        if (obj == null || (obj instanceof Float)) {
            return (Float) obj;
        }
        if (obj instanceof Number) {
            return Float.valueOf(((Number) obj).floatValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return Float.valueOf(Float.parseFloat(str));
        }
        throw new JSONException("can not cast to decimal");
    }

    public static double toDoubleValue(Object obj) {
        if (obj == null) {
            return 0.0d;
        }
        if (obj instanceof Double) {
            return ((Double) obj).doubleValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return 0.0d;
            }
            return Double.parseDouble(str);
        }
        boolean z = obj instanceof Collection;
        if ((z && ((Collection) obj).isEmpty()) || ((obj instanceof Map) && ((Map) obj).isEmpty())) {
            return 0.0d;
        }
        if (z) {
            Collection collection = (Collection) obj;
            if (collection.size() == 1) {
                Object next = collection.iterator().next();
                if (next instanceof Number) {
                    return ((Number) next).doubleValue();
                }
                if (next instanceof String) {
                    return Double.parseDouble((String) next);
                }
            }
        }
        throw new JSONException("can not cast to double");
    }

    public static Double toDouble(Object obj) {
        if (obj == null || (obj instanceof Double)) {
            return (Double) obj;
        }
        if (obj instanceof Number) {
            return Double.valueOf(((Number) obj).doubleValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return Double.valueOf(Double.parseDouble(str));
        }
        throw new JSONException("can not cast to double");
    }

    public static int compare(Object obj, Object obj2) {
        BigDecimal bigDecimal;
        BigDecimal bigDecimal2;
        Class<?> cls = obj.getClass();
        Class<?> cls2 = obj2.getClass();
        if (cls == cls2) {
            return ((Comparable) obj).compareTo(obj2);
        }
        if (cls == BigDecimal.class) {
            if (cls2 == Integer.class || cls2 == Long.class) {
                obj2 = BigDecimal.valueOf(((Number) obj2).longValue());
            } else if (cls2 == Float.class || cls2 == Double.class) {
                obj2 = BigDecimal.valueOf(((Number) obj2).doubleValue());
            } else if (cls2 == BigInteger.class) {
                bigDecimal = new BigDecimal((BigInteger) obj2);
                obj2 = bigDecimal;
            }
        } else if (cls == BigInteger.class) {
            if (cls2 == Integer.class || cls2 == Long.class) {
                obj2 = BigInteger.valueOf(((Number) obj2).longValue());
            } else {
                if (cls2 == Float.class || cls2 == Double.class) {
                    obj2 = BigDecimal.valueOf(((Number) obj2).doubleValue());
                    bigDecimal2 = new BigDecimal((BigInteger) obj);
                } else if (cls2 == BigDecimal.class) {
                    bigDecimal2 = new BigDecimal((BigInteger) obj);
                }
                obj = bigDecimal2;
            }
        } else if (cls == Long.class) {
            if (cls2 == Integer.class) {
                return Long.compare(((Long) obj).longValue(), ((Integer) obj2).intValue());
            }
            if (cls2 == BigDecimal.class) {
                obj = BigDecimal.valueOf(((Long) obj).longValue());
            } else {
                if (cls2 == Float.class || cls2 == Double.class) {
                    return Double.compare(((Long) obj).longValue(), ((Number) obj2).doubleValue());
                }
                if (cls2 == BigInteger.class) {
                    obj = BigInteger.valueOf(((Long) obj).longValue());
                } else if (cls2 == String.class) {
                    obj = BigDecimal.valueOf(((Long) obj).longValue());
                    bigDecimal = new BigDecimal((String) obj2);
                    obj2 = bigDecimal;
                }
            }
        } else if (cls == Integer.class) {
            if (cls2 == Long.class) {
                return Long.compare(((Integer) obj).intValue(), ((Long) obj2).longValue());
            }
            if (cls2 == BigDecimal.class) {
                obj = BigDecimal.valueOf(((Integer) obj).intValue());
            } else if (cls2 == BigInteger.class) {
                obj = BigInteger.valueOf(((Integer) obj).intValue());
            } else {
                if (cls2 == Float.class || cls2 == Double.class) {
                    return Double.compare(((Integer) obj).intValue(), ((Number) obj2).doubleValue());
                }
                if (cls2 == String.class) {
                    obj = BigDecimal.valueOf(((Integer) obj).intValue());
                    bigDecimal = new BigDecimal((String) obj2);
                    obj2 = bigDecimal;
                }
            }
        } else if (cls == Double.class) {
            if (cls2 == Integer.class || cls2 == Long.class || cls2 == Float.class) {
                return Double.compare(((Double) obj).doubleValue(), ((Number) obj2).doubleValue());
            }
            if (cls2 == BigDecimal.class) {
                obj = BigDecimal.valueOf(((Double) obj).doubleValue());
            } else {
                if (cls2 == String.class) {
                    obj = BigDecimal.valueOf(((Double) obj).doubleValue());
                    bigDecimal = new BigDecimal((String) obj2);
                } else if (cls2 == BigInteger.class) {
                    obj = BigDecimal.valueOf(((Double) obj).doubleValue());
                    bigDecimal = new BigDecimal((BigInteger) obj2);
                }
                obj2 = bigDecimal;
            }
        } else if (cls == Float.class) {
            if (cls2 == Integer.class || cls2 == Long.class || cls2 == Double.class) {
                return Double.compare(((Float) obj).floatValue(), ((Number) obj2).doubleValue());
            }
            if (cls2 == BigDecimal.class) {
                obj = BigDecimal.valueOf(((Float) obj).floatValue());
            } else {
                if (cls2 == String.class) {
                    obj = BigDecimal.valueOf(((Float) obj).floatValue());
                    bigDecimal = new BigDecimal((String) obj2);
                } else if (cls2 == BigInteger.class) {
                    obj = BigDecimal.valueOf(((Float) obj).floatValue());
                    bigDecimal = new BigDecimal((BigInteger) obj2);
                }
                obj2 = bigDecimal;
            }
        } else if (cls == String.class) {
            String str = (String) obj;
            if (cls2 == Integer.class || cls2 == Long.class) {
                try {
                    return Long.compare(Long.parseLong(str), ((Number) obj2).longValue());
                } catch (NumberFormatException unused) {
                    obj = new BigDecimal(str);
                    obj2 = BigDecimal.valueOf(((Number) obj2).longValue());
                }
            } else {
                if (cls2 == Float.class || cls2 == Double.class) {
                    return Double.compare(Double.parseDouble(str), ((Number) obj2).doubleValue());
                }
                if (cls2 == BigInteger.class) {
                    obj = new BigInteger(str);
                } else if (cls2 == BigDecimal.class) {
                    obj = new BigDecimal(str);
                }
            }
        }
        return ((Comparable) obj).compareTo(obj2);
    }

    public static Object getDefaultValue(Type type) {
        if (type == Integer.TYPE) {
            return 0;
        }
        if (type == Long.TYPE) {
            return 0L;
        }
        if (type == Float.TYPE) {
            return Float.valueOf(0.0f);
        }
        if (type == Double.TYPE) {
            return Double.valueOf(0.0d);
        }
        if (type == Boolean.TYPE) {
            return Boolean.FALSE;
        }
        if (type == Short.TYPE) {
            return (short) 0;
        }
        if (type == Byte.TYPE) {
            return (byte) 0;
        }
        if (type == Character.TYPE) {
            return (char) 0;
        }
        if (type == Optional.class) {
            return Optional.empty();
        }
        if (type == OptionalInt.class) {
            return OptionalInt.empty();
        }
        if (type == OptionalLong.class) {
            return OptionalLong.empty();
        }
        if (type == OptionalDouble.class) {
            return OptionalDouble.empty();
        }
        return null;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static Class loadClass(String str) {
        if (str.length() >= 192) {
            return null;
        }
        str.hashCode();
        byte b = -1;
        switch (str.hashCode()) {
            case -2073921873:
                if (str.equals("java.util.OptionalInt")) {
                    b = 0;
                }
                break;
            case -2056817302:
                if (str.equals("java.lang.Integer")) {
                    b = 1;
                }
                break;
            case -2010664371:
                if (str.equals("java.io.IOException")) {
                    b = 2;
                }
                break;
            case -1939501217:
                if (str.equals("Object")) {
                    b = 3;
                }
                break;
            case -1932803762:
                if (str.equals("HashMap")) {
                    b = 4;
                }
                break;
            case -1932797868:
                if (str.equals("HashSet")) {
                    b = 5;
                }
                break;
            case -1899270121:
                if (str.equals("java.util.LinkedList")) {
                    b = 6;
                }
                break;
            case -1808118735:
                if (str.equals("String")) {
                    b = 7;
                }
                break;
            case -1671476816:
                if (str.equals("ConcurrentLinkedDeque")) {
                    b = 8;
                }
                break;
            case -1659005919:
                if (str.equals("ConcurrentLinkedQueue")) {
                    b = 9;
                }
                break;
            case -1418007307:
                if (str.equals("LinkedHashMap")) {
                    b = 10;
                }
                break;
            case -1418001413:
                if (str.equals("LinkedHashSet")) {
                    b = 11;
                }
                break;
            case -1402722386:
                if (str.equals("java.util.HashMap")) {
                    b = 12;
                }
                break;
            case -1402716492:
                if (str.equals("java.util.HashSet")) {
                    b = 13;
                }
                break;
            case -1383349348:
                if (str.equals("java.util.Map")) {
                    b = 14;
                }
                break;
            case -1383343454:
                if (str.equals("java.util.Set")) {
                    b = 15;
                }
                break;
            case -1374008726:
                if (str.equals("byte[]")) {
                    b = JSONB.Constants.BC_INT32_NUM_16;
                }
                break;
            case -1361632968:
                if (str.equals("char[]")) {
                    b = 17;
                }
                break;
            case -1325958191:
                if (str.equals("double")) {
                    b = 18;
                }
                break;
            case -1114099497:
                if (str.equals("java.util.ArrayList")) {
                    b = 19;
                }
                break;
            case -1097129250:
                if (str.equals("long[]")) {
                    b = 20;
                }
                break;
            case -1074506598:
                if (str.equals("java.util.Collections$SingletonList")) {
                    b = 21;
                }
                break;
            case -958795145:
                if (str.equals("LinkedList")) {
                    b = 22;
                }
                break;
            case -766441794:
                if (str.equals("float[]")) {
                    b = 23;
                }
                break;
            case -761719520:
                if (str.equals("java.util.Optional")) {
                    b = 24;
                }
                break;
            case -530663260:
                if (str.equals("java.lang.Class")) {
                    b = 25;
                }
                break;
            case -413661986:
                if (str.equals("java.util.Collections$EmptyMap")) {
                    b = 26;
                }
                break;
            case -413656092:
                if (str.equals("java.util.Collections$EmptySet")) {
                    b = 27;
                }
                break;
            case -113680546:
                if (str.equals("Calendar")) {
                    b = 28;
                }
                break;
            case 65:
                if (str.equals(ExifInterface.GPS_MEASUREMENT_IN_PROGRESS)) {
                    b = 29;
                }
                break;
            case 66:
                if (str.equals("B")) {
                    b = 30;
                }
                break;
            case 67:
                if (str.equals("C")) {
                    b = 31;
                }
                break;
            case 68:
                if (str.equals("D")) {
                    b = 32;
                }
                break;
            case 70:
                if (str.equals("F")) {
                    b = 33;
                }
                break;
            case 73:
                if (str.equals("I")) {
                    b = 34;
                }
                break;
            case 74:
                if (str.equals("J")) {
                    b = 35;
                }
                break;
            case 77:
                if (str.equals("M")) {
                    b = 36;
                }
                break;
            case 79:
                if (str.equals("O")) {
                    b = 37;
                }
                break;
            case 83:
                if (str.equals(ExifInterface.LATITUDE_SOUTH)) {
                    b = 38;
                }
                break;
            case 90:
                if (str.equals("Z")) {
                    b = 39;
                }
                break;
            case 2421:
                if (str.equals("LA")) {
                    b = 40;
                }
                break;
            case 2433:
                if (str.equals("LM")) {
                    b = 41;
                }
                break;
            case 2887:
                if (str.equals("[B")) {
                    b = 42;
                }
                break;
            case 2888:
                if (str.equals("[C")) {
                    b = 43;
                }
                break;
            case 2889:
                if (str.equals("[D")) {
                    b = 44;
                }
                break;
            case 2891:
                if (str.equals("[F")) {
                    b = 45;
                }
                break;
            case 2894:
                if (str.equals("[I")) {
                    b = 46;
                }
                break;
            case 2895:
                if (str.equals("[J")) {
                    b = JSONB.Constants.BC_INT32_NUM_MAX;
                }
                break;
            case 2900:
                if (str.equals("[O")) {
                    b = JSONB.Constants.BC_INT32_BYTE_MIN;
                }
                break;
            case 2904:
                if (str.equals("[S")) {
                    b = 49;
                }
                break;
            case 2911:
                if (str.equals("[Z")) {
                    b = 50;
                }
                break;
            case 73612:
                if (str.equals("JO1")) {
                    b = 51;
                }
                break;
            case 77116:
                if (str.equals("Map")) {
                    b = 52;
                }
                break;
            case 83010:
                if (str.equals("Set")) {
                    b = 53;
                }
                break;
            case 104431:
                if (str.equals("int")) {
                    b = 54;
                }
                break;
            case 2122702:
                if (str.equals("Date")) {
                    b = 55;
                }
                break;
            case 2368702:
                if (str.equals("List")) {
                    b = JSONB.Constants.BC_INT32_BYTE_ZERO;
                }
                break;
            case 2616251:
                if (str.equals("UUID")) {
                    b = 57;
                }
                break;
            case 3039496:
                if (str.equals("byte")) {
                    b = 58;
                }
                break;
            case 3052374:
                if (str.equals("char")) {
                    b = 59;
                }
                break;
            case 3327612:
                if (str.equals("long")) {
                    b = 60;
                }
                break;
            case 61358428:
                if (str.equals("java.util.Collections$EmptyList")) {
                    b = 61;
                }
                break;
            case 64711720:
                if (str.equals("boolean")) {
                    b = 62;
                }
                break;
            case 65821278:
                if (str.equals("java.util.List")) {
                    b = 63;
                }
                break;
            case 97526364:
                if (str.equals("float")) {
                    b = JSONB.Constants.BC_INT32_SHORT_MIN;
                }
                break;
            case 100361105:
                if (str.equals("int[]")) {
                    b = 65;
                }
                break;
            case 109413500:
                if (str.equals("short")) {
                    b = 66;
                }
                break;
            case 133021628:
                if (str.equals("java.util.OptionalLong")) {
                    b = 67;
                }
                break;
            case 179563853:
                if (str.equals("java.util.Arrays$ArrayList")) {
                    b = JSONB.Constants.BC_INT32_SHORT_ZERO;
                }
                break;
            case 398795216:
                if (str.equals("java.lang.Long")) {
                    b = 69;
                }
                break;
            case 578806391:
                if (str.equals("ArrayList")) {
                    b = 70;
                }
                break;
            case 600988612:
                if (str.equals("TreeSet")) {
                    b = JSONB.Constants.BC_INT32_SHORT_MAX;
                }
                break;
            case 889669201:
                if (str.equals("java.util.Collections$UnmodifiableRandomAccessList")) {
                    b = JSONB.Constants.BC_INT32;
                }
                break;
            case 935176422:
                if (str.equals("java.util.Collections$SingletonSet")) {
                    b = 73;
                }
                break;
            case 1063877011:
                if (str.equals("java.lang.Object")) {
                    b = JSONB.Constants.BC_STR_ASCII_FIX_1;
                }
                break;
            case 1131069988:
                if (str.equals("java.util.TreeSet")) {
                    b = 75;
                }
                break;
            case 1195259493:
                if (str.equals("java.lang.String")) {
                    b = 76;
                }
                break;
            case 1258621781:
                if (str.equals("java.util.LinkedHashMap")) {
                    b = JSONB.Constants.BC_STR_ASCII_FIX_4;
                }
                break;
            case 1258627675:
                if (str.equals("java.util.LinkedHashSet")) {
                    b = JSONB.Constants.BC_STR_ASCII_FIX_5;
                }
                break;
            case 1359468275:
                if (str.equals("double[]")) {
                    b = 79;
                }
                break;
            case 1372295063:
                if (str.equals("ConcurrentHashMap")) {
                    b = 80;
                }
                break;
            case 1645304908:
                if (str.equals("[String")) {
                    b = 81;
                }
                break;
            case 1752376903:
                if (str.equals("JSONObject")) {
                    b = 82;
                }
                break;
            case 2058423690:
                if (str.equals("boolean[]")) {
                    b = 83;
                }
                break;
            case 2067161310:
                if (str.equals("short[]")) {
                    b = 84;
                }
                break;
        }
        switch (b) {
            case 0:
                return OptionalInt.class;
            case 1:
                return Integer.class;
            case 2:
                return IOException.class;
            case 3:
            case 37:
            case 74:
                return Object.class;
            case 4:
            case 12:
            case 36:
                return HashMap.class;
            case 5:
            case 13:
                return HashSet.class;
            case 6:
            case 22:
            case 40:
                return LinkedList.class;
            case 7:
            case 76:
                return String.class;
            case 8:
                return ConcurrentLinkedDeque.class;
            case 9:
                return ConcurrentLinkedQueue.class;
            case 10:
            case 41:
            case 77:
                return LinkedHashMap.class;
            case 11:
            case 78:
                return LinkedHashSet.class;
            case 14:
            case 52:
                return Map.class;
            case 15:
            case 53:
                return Set.class;
            case 16:
            case 42:
                return byte[].class;
            case 17:
            case 43:
                return char[].class;
            case 18:
            case 32:
                return Double.TYPE;
            case 19:
            case 29:
            case 70:
                return ArrayList.class;
            case 20:
            case 47:
                return long[].class;
            case 21:
                return CLASS_SINGLE_LIST;
            case 23:
            case 45:
                return float[].class;
            case 24:
                return Optional.class;
            case 25:
                return Class.class;
            case 26:
                return Collections.emptyMap().getClass();
            case 27:
                return Collections.emptySet().getClass();
            case 28:
                return Calendar.class;
            case 30:
            case 58:
                return Byte.TYPE;
            case 31:
            case 59:
                return Character.TYPE;
            case 33:
            case 64:
                return Float.TYPE;
            case 34:
            case 54:
                return Integer.TYPE;
            case 35:
            case 60:
                return Long.TYPE;
            case 38:
            case 66:
                return Short.TYPE;
            case 39:
            case 62:
                return Boolean.TYPE;
            case 44:
            case 79:
                return double[].class;
            case 46:
            case 65:
                return int[].class;
            case 48:
                return Object[].class;
            case 49:
            case 84:
                return short[].class;
            case 50:
            case 83:
                return boolean[].class;
            case 51:
                str = "com.alibaba.fastjson.JSONObject";
                break;
            case 55:
                return Date.class;
            case 56:
            case 63:
                return List.class;
            case 57:
                return UUID.class;
            case 61:
                return Collections.emptyList().getClass();
            case 67:
                return OptionalLong.class;
            case 68:
                return Arrays.asList(1).getClass();
            case 69:
                return Long.class;
            case 71:
            case 75:
                return TreeSet.class;
            case 72:
                return CLASS_UNMODIFIABLE_LIST;
            case 73:
                return CLASS_SINGLE_SET;
            case 80:
                return ConcurrentHashMap.class;
            case 81:
                return String[].class;
            case 82:
                return JSONObject.class;
        }
        Class cls = TYPE_MAPPINGS.get(str);
        if (cls != null) {
            return cls;
        }
        if (str.startsWith("java.util.ImmutableCollections$")) {
            try {
                return Class.forName(str);
            } catch (ClassNotFoundException unused) {
                return CLASS_UNMODIFIABLE_LIST;
            }
        }
        if (str.charAt(0) == 'L' && str.charAt(str.length() - 1) == ';') {
            str = str.substring(1, str.length() - 1);
        }
        if (str.charAt(0) == '[' || str.endsWith(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI)) {
            Class clsLoadClass = loadClass(str.charAt(0) == '[' ? str.substring(1) : str.substring(0, str.length() - 2));
            if (clsLoadClass == null) {
                throw new JSONException("load class error " + str);
            }
            return Array.newInstance((Class<?>) clsLoadClass, 0).getClass();
        }
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                return contextClassLoader.loadClass(str);
            } catch (ClassNotFoundException unused2) {
            }
        }
        try {
            try {
                return JSON.class.getClassLoader().loadClass(str);
            } catch (ClassNotFoundException unused3) {
                return null;
            }
        } catch (ClassNotFoundException unused4) {
            return Class.forName(str);
        }
    }

    public static Class<?> getArrayClass(Class cls) {
        if (cls == Integer.TYPE) {
            return int[].class;
        }
        if (cls == Byte.TYPE) {
            return byte[].class;
        }
        if (cls == Short.TYPE) {
            return short[].class;
        }
        if (cls == Long.TYPE) {
            return long[].class;
        }
        if (cls == String.class) {
            return String[].class;
        }
        if (cls == Object.class) {
            return Object[].class;
        }
        return Array.newInstance((Class<?>) cls, 1).getClass();
    }

    public static Class nonePrimitive(Class cls) {
        if (!cls.isPrimitive()) {
            return cls;
        }
        String name = cls.getName();
        name.hashCode();
        switch (name) {
            case "double":
                return Double.class;
            case "int":
                return Integer.class;
            case "byte":
                return Byte.class;
            case "char":
                return Character.class;
            case "long":
                return Long.class;
            case "boolean":
                return Boolean.class;
            case "float":
                return Float.class;
            case "short":
                return Short.class;
            default:
                return cls;
        }
    }

    public static Class<?> getClass(Type type) {
        if (type == null) {
            return null;
        }
        if (type.getClass() == Class.class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof TypeVariable) {
            Type type2 = ((TypeVariable) type).getBounds()[0];
            if (type2 instanceof Class) {
                return (Class) type2;
            }
            return getClass(type2);
        }
        if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length == 1) {
                return getClass(upperBounds[0]);
            }
        }
        if (type instanceof GenericArrayType) {
            return getArrayClass(getClass(((GenericArrayType) type).getGenericComponentType()));
        }
        return Object.class;
    }

    public static boolean isProxy(Class<?> cls) {
        for (Class<?> cls2 : cls.getInterfaces()) {
            String name = cls2.getName();
            name.hashCode();
            switch (name) {
                case "javassist.util.proxy.ProxyObject":
                case "org.springframework.cglib.proxy.Factory":
                case "org.springframework.context.annotation.ConfigurationClassEnhancer$EnhancedConfiguration":
                case "org.mockito.cglib.proxy.Factory":
                case "org.apache.ibatis.javassist.util.proxy.ProxyObject":
                case "net.sf.cglib.proxy.Factory":
                case "org.hibernate.proxy.HibernateProxy":
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    public static Map getInnerMap(Map map) {
        Field field;
        Class cls = CLASS_JSON_OBJECT_1x;
        if (cls != null && cls.isInstance(map) && (field = FIELD_JSON_OBJECT_1x_map) != null) {
            try {
                return (Map) field.get(map);
            } catch (IllegalAccessException unused) {
            }
        }
        return map;
    }

    public static boolean isFunction(Class cls) {
        if (!cls.isInterface()) {
            return false;
        }
        if (cls.getName().startsWith("java.util.function.")) {
            return true;
        }
        return cls.isAnnotationPresent(FunctionalInterface.class);
    }

    public static boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char cCharAt = str.charAt(0);
        if (cCharAt == '-' || cCharAt == '+') {
            if (str.length() == 1) {
                return false;
            }
        } else if (cCharAt < '0' || cCharAt > '9') {
            return false;
        }
        for (int i = 1; i < str.length(); i++) {
            char cCharAt2 = str.charAt(i);
            if (cCharAt2 < '0' || cCharAt2 > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isInteger(byte[] bArr, int i, int i2) {
        if (bArr == null || i2 == 0) {
            return false;
        }
        char c = (char) bArr[i];
        if (c == '-' || c == '+') {
            if (i2 == 1) {
                return false;
            }
        } else if (c < '0' || c > '9') {
            return false;
        }
        int i3 = i2 + i;
        for (int i4 = i + 1; i4 < i3; i4++) {
            char c2 = (char) bArr[i4];
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isInteger(char[] cArr, int i, int i2) {
        if (cArr == null || i2 == 0) {
            return false;
        }
        char c = cArr[i];
        if (c == '-' || c == '+') {
            if (i2 == 1) {
                return false;
            }
        } else if (c < '0' || c > '9') {
            return false;
        }
        int i3 = i2 + i;
        for (int i4 = i + 1; i4 < i3; i4++) {
            char c2 = cArr[i4];
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x008e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x008f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isNumber(java.lang.String r11) {
        /*
            r0 = 0
            if (r11 == 0) goto Lb7
            boolean r1 = r11.isEmpty()
            if (r1 == 0) goto Lb
            goto Lb7
        Lb:
            char r1 = r11.charAt(r0)
            r2 = 43
            r3 = 45
            r4 = 46
            r5 = 1
            if (r1 == r3) goto L26
            if (r1 != r2) goto L1b
            goto L26
        L1b:
            if (r1 != r4) goto L24
            int r6 = r11.length()
            if (r6 != r5) goto L31
            return r0
        L24:
            r6 = r0
            goto L32
        L26:
            int r1 = r11.length()
            if (r1 != r5) goto L2d
            return r0
        L2d:
            char r1 = r11.charAt(r5)
        L31:
            r6 = r5
        L32:
            int r7 = r11.length()
            if (r1 != r4) goto L3a
            r8 = r5
            goto L3b
        L3a:
            r8 = r0
        L3b:
            r9 = 57
            r10 = 48
            if (r8 != 0) goto L5a
            if (r1 < r10) goto L5a
            if (r1 > r9) goto L5a
        L45:
            if (r6 >= r7) goto L59
            int r1 = r6 + 1
            char r6 = r11.charAt(r6)
            if (r6 < r10) goto L54
            if (r6 <= r9) goto L52
            goto L54
        L52:
            r6 = r1
            goto L45
        L54:
            r8 = r6
            r6 = r1
            r1 = r8
            r8 = r5
            goto L5b
        L59:
            return r5
        L5a:
            r8 = r0
        L5b:
            if (r1 != r4) goto L7e
            if (r6 >= r7) goto L7d
            int r1 = r6 + 1
            char r4 = r11.charAt(r6)
            if (r4 < r10) goto L79
            if (r4 > r9) goto L79
        L69:
            if (r1 >= r7) goto L78
            int r6 = r1 + 1
            char r1 = r11.charAt(r1)
            if (r1 < r10) goto L7b
            if (r1 <= r9) goto L76
            goto L7b
        L76:
            r1 = r6
            goto L69
        L78:
            return r5
        L79:
            r6 = r1
            r1 = r4
        L7b:
            r4 = r5
            goto L7f
        L7d:
            return r5
        L7e:
            r4 = r0
        L7f:
            if (r8 != 0) goto L84
            if (r4 != 0) goto L84
            return r0
        L84:
            r4 = 101(0x65, float:1.42E-43)
            if (r1 == r4) goto L8c
            r4 = 69
            if (r1 != r4) goto Lb5
        L8c:
            if (r6 != r7) goto L8f
            return r5
        L8f:
            int r1 = r6 + 1
            char r4 = r11.charAt(r6)
            if (r4 == r2) goto L99
            if (r4 != r3) goto La2
        L99:
            if (r1 >= r7) goto Lb7
            int r6 = r6 + 2
            char r4 = r11.charAt(r1)
            r1 = r6
        La2:
            if (r4 < r10) goto Lb7
            if (r4 > r9) goto Lb7
        La6:
            if (r1 >= r7) goto Lb6
            int r2 = r1 + 1
            char r1 = r11.charAt(r1)
            if (r1 < r10) goto Lb5
            if (r1 <= r9) goto Lb3
            goto Lb5
        Lb3:
            r1 = r2
            goto La6
        Lb5:
            return r0
        Lb6:
            return r5
        Lb7:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.TypeUtils.isNumber(java.lang.String):boolean");
    }

    public static boolean isNumber(byte[] bArr, int i, int i2) {
        int i3;
        boolean z;
        boolean z2;
        if (bArr != null && i2 != 0) {
            char c = (char) bArr[i];
            if (c == '-' || c == '+') {
                if (i2 == 1) {
                    return false;
                }
                i3 = i + 1;
                c = (char) bArr[i3];
            } else if (c != '.') {
                i3 = i;
            } else {
                if (i2 == 1) {
                    return false;
                }
                i3 = i + 1;
            }
            int i4 = i + i2;
            if (!(c == '.') && c >= '0' && c <= '9') {
                while (i3 < i4) {
                    int i5 = i3 + 1;
                    c = (char) bArr[i3];
                    if (c < '0' || c > '9') {
                        i3 = i5;
                        z = true;
                    } else {
                        i3 = i5;
                    }
                }
                return true;
            }
            z = false;
            if (c != '.') {
                z2 = false;
            } else {
                if (i3 >= i4) {
                    return true;
                }
                int i6 = i3 + 1;
                char c2 = (char) bArr[i3];
                if (c2 >= '0' && c2 <= '9') {
                    while (i6 < i4) {
                        i3 = i6 + 1;
                        c = (char) bArr[i6];
                        if (c >= '0' && c <= '9') {
                            i6 = i3;
                        }
                    }
                    return true;
                }
                i3 = i6;
                c = c2;
                z2 = true;
            }
            if (!z && !z2) {
                return false;
            }
            if (c == 'e' || c == 'E') {
                if (i3 == i4) {
                    return true;
                }
                int i7 = i3 + 1;
                char c3 = (char) bArr[i3];
                if (c3 == '+' || c3 == '-') {
                    if (i7 < i4) {
                        c3 = (char) bArr[i7];
                        i7 = i3 + 2;
                    }
                }
                if (c3 >= '0' && c3 <= '9') {
                    while (i7 < i4) {
                        int i8 = i7 + 1;
                        char c4 = (char) bArr[i7];
                        if (c4 >= '0' && c4 <= '9') {
                            i7 = i8;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isNumber(char[] cArr, int i, int i2) {
        int i3;
        boolean z;
        boolean z2;
        if (cArr != null && i2 != 0) {
            char c = cArr[i];
            if (c == '-' || c == '+') {
                if (i2 == 1) {
                    return false;
                }
                i3 = i + 1;
                c = cArr[i3];
            } else if (c != '.') {
                i3 = i;
            } else {
                if (i2 == 1) {
                    return false;
                }
                i3 = i + 1;
            }
            int i4 = i + i2;
            if (!(c == '.') && c >= '0' && c <= '9') {
                while (i3 < i4) {
                    int i5 = i3 + 1;
                    c = cArr[i3];
                    if (c < '0' || c > '9') {
                        i3 = i5;
                        z = true;
                    } else {
                        i3 = i5;
                    }
                }
                return true;
            }
            z = false;
            if (c != '.') {
                z2 = false;
            } else {
                if (i3 >= i4) {
                    return true;
                }
                int i6 = i3 + 1;
                char c2 = cArr[i3];
                if (c2 >= '0' && c2 <= '9') {
                    while (i6 < i4) {
                        i3 = i6 + 1;
                        c = cArr[i6];
                        if (c >= '0' && c <= '9') {
                            i6 = i3;
                        }
                    }
                    return true;
                }
                i3 = i6;
                c = c2;
                z2 = true;
            }
            if (!z && !z2) {
                return false;
            }
            if (c == 'e' || c == 'E') {
                if (i3 == i4) {
                    return true;
                }
                int i7 = i3 + 1;
                char c3 = cArr[i3];
                if (c3 == '+' || c3 == '-') {
                    if (i7 < i4) {
                        c3 = cArr[i7];
                        i7 = i3 + 2;
                    }
                }
                if (c3 >= '0' && c3 <= '9') {
                    while (i7 < i4) {
                        int i8 = i7 + 1;
                        char c4 = cArr[i7];
                        if (c4 >= '0' && c4 <= '9') {
                            i7 = i8;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isUUID(String str) {
        if (str == null) {
            return false;
        }
        if (str.length() == 32) {
            for (int i = 0; i < 32; i++) {
                char cCharAt = str.charAt(i);
                if ((cCharAt < '0' || cCharAt > '9') && ((cCharAt < 'A' || cCharAt > 'F') && (cCharAt < 'a' || cCharAt > 'f'))) {
                    return false;
                }
            }
            return true;
        }
        if (str.length() != 36) {
            return false;
        }
        for (int i2 = 0; i2 < 36; i2++) {
            char cCharAt2 = str.charAt(i2);
            if (i2 == 8 || i2 == 13 || i2 == 18 || i2 == 23) {
                if (cCharAt2 != '-') {
                    return false;
                }
            } else if ((cCharAt2 < '0' || cCharAt2 > '9') && ((cCharAt2 < 'A' || cCharAt2 > 'F') && (cCharAt2 < 'a' || cCharAt2 > 'f'))) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateIPv4(String str) {
        return validateIPv4(str, 0);
    }

    static boolean validateIPv4(String str, int i) {
        int length;
        int length2;
        if (str != null && (length2 = (length = str.length()) - i) >= 7 && length2 <= 25) {
            int i2 = i;
            int i3 = 0;
            while (i < length) {
                char cCharAt = str.charAt(i);
                if (cCharAt == '.' || i == length - 1) {
                    int i4 = cCharAt == '.' ? i : i + 1;
                    int i5 = i4 - i2;
                    if (i5 == 1) {
                        char cCharAt2 = str.charAt(i4 - 1);
                        if (cCharAt2 < '0' || cCharAt2 > '9') {
                            return false;
                        }
                    } else if (i5 == 2) {
                        char cCharAt3 = str.charAt(i4 - 2);
                        char cCharAt4 = str.charAt(i4 - 1);
                        if (cCharAt3 < '0' || cCharAt3 > '9' || cCharAt4 < '0' || cCharAt4 > '9') {
                            return false;
                        }
                    } else {
                        if (i5 != 3) {
                            return false;
                        }
                        char cCharAt5 = str.charAt(i4 - 3);
                        char cCharAt6 = str.charAt(i4 - 2);
                        char cCharAt7 = str.charAt(i4 - 1);
                        if (cCharAt5 < '0' || cCharAt5 > '2' || cCharAt6 < '0' || cCharAt6 > '9' || cCharAt7 < '0' || cCharAt7 > '9' || ((cCharAt5 - '0') * 100) + ((cCharAt6 - '0') * 10) + (cCharAt7 - '0') > 255) {
                            return false;
                        }
                    }
                    if (cCharAt == '.') {
                        i3++;
                        i2 = i + 1;
                    }
                }
                i++;
            }
            if (i3 == 3) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x0102, code lost:
    
        return r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x012a, code lost:
    
        r16 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x012c, code lost:
    
        if (r6 <= 0) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0130, code lost:
    
        if (r6 >= 8) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x0132, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0133, code lost:
    
        return r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00a3, code lost:
    
        return r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x00dc, code lost:
    
        return r16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean validateIPv6(java.lang.String r17) {
        /*
            Method dump skipped, instruction units count: 311
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.util.TypeUtils.validateIPv6(java.lang.String):boolean");
    }

    public static double doubleValue(int i, long j, int i2) {
        double d;
        double dScalb;
        double d2;
        double d3;
        long jNumberOfLeadingZeros = ((long) (64 - Long.numberOfLeadingZeros(j))) - ((long) Math.ceil(((double) i2) * L));
        if (jNumberOfLeadingZeros < -1076) {
            d2 = i;
            d3 = 0.0d;
        } else {
            if (jNumberOfLeadingZeros <= 1025) {
                if (i2 == 0) {
                    d = i;
                    dScalb = j;
                } else {
                    int i3 = ((int) jNumberOfLeadingZeros) - 56;
                    long jDivideKnuthLong = MutableBigInteger.divideKnuthLong(j, i3, i2);
                    int i4 = (-1076) - i3;
                    if (9 - Long.numberOfLeadingZeros(jDivideKnuthLong) >= i4) {
                        return ((double) i) * Math.scalb(jDivideKnuthLong | 1, i3);
                    }
                    d = i;
                    dScalb = Math.scalb(((long) Long.signum(jDivideKnuthLong & ((1 << i4) - 1))) | (jDivideKnuthLong >> i4) | 1, -1076);
                }
                return d * dScalb;
            }
            d2 = i;
            d3 = Double.POSITIVE_INFINITY;
        }
        return d2 * d3;
    }

    public static float floatValue(int i, long j, int i2) {
        float f;
        float fScalb;
        long jNumberOfLeadingZeros = ((long) (64 - Long.numberOfLeadingZeros(j))) - ((long) Math.ceil(((double) i2) * L));
        if (jNumberOfLeadingZeros < -151) {
            f = i;
            fScalb = 0.0f;
        } else if (jNumberOfLeadingZeros > 129) {
            f = i;
            fScalb = Float.POSITIVE_INFINITY;
        } else if (i2 == 0) {
            f = i;
            fScalb = j;
        } else {
            int i3 = ((int) jNumberOfLeadingZeros) - 27;
            int iDivideKnuthLong = (int) MutableBigInteger.divideKnuthLong(j, i3, i2);
            int i4 = j == 0 ? 0 : 1;
            if (6 - Integer.numberOfLeadingZeros(iDivideKnuthLong) >= (-151) - i3) {
                f = i;
                fScalb = Math.scalb(i4 | iDivideKnuthLong, i3);
            } else {
                f = i;
                fScalb = Math.scalb(i4 | (iDivideKnuthLong >> r2) | Integer.signum(iDivideKnuthLong & ((1 << r2) - 1)), -151);
            }
        }
        return f * fScalb;
    }

    public static boolean isJavaScriptSupport(BigDecimal bigDecimal) {
        boolean z = bigDecimal.precision() < 16 || isJavaScriptSupport(bigDecimal.unscaledValue());
        return (z || bigDecimal.scale() == 0) ? z : bigDecimal.compareTo(BigDecimal.valueOf(bigDecimal.doubleValue())) == 0;
    }

    public static boolean isJavaScriptSupport(BigInteger bigInteger) {
        return bigInteger.compareTo(BIGINT_JAVASCRIPT_LOW) >= 0 && bigInteger.compareTo(BIGINT_JAVASCRIPT_HIGH) <= 0;
    }

    public static Type getMapValueType(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (actualTypeArguments.length == 2) {
                return actualTypeArguments[1];
            }
            return Object.class;
        }
        return Object.class;
    }
}
