package com.alibaba.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.alibaba.fastjson.serializer.LabelFilter;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.support.AwtRederModule;
import com.alibaba.fastjson2.support.AwtWriterModule;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public abstract class JSON implements JSONAware {
    static final Cache CACHE;
    static final AtomicReferenceFieldUpdater<Cache, char[]> CHARS_UPDATER;
    public static String DEFAULT_DATE_FORMAT = null;
    public static int DEFAULT_GENERATE_FEATURE = 0;
    public static int DEFAULT_PARSER_FEATURE = 0;
    private static final TimeZone DEFAULT_TIME_ZONE;
    public static String DEFAULT_TYPE_KEY = null;

    @Deprecated
    public static String DEFFAULT_DATE_FORMAT = null;
    private static final int MAX_LEVEL = 2048;
    public static final String VERSION = "2.0.58";
    static final Supplier<List> arraySupplier;
    public static Locale defaultLocale;
    static final Supplier<Map> defaultSupplier;
    public static TimeZone defaultTimeZone;
    static final Supplier<Map> orderedSupplier;

    public abstract <T> T toJavaObject(Class<T> cls);

    public abstract <T> T toJavaObject(Type type);

    static {
        TimeZone timeZone = TimeZone.getDefault();
        DEFAULT_TIME_ZONE = timeZone;
        CACHE = new Cache();
        CHARS_UPDATER = AtomicReferenceFieldUpdater.newUpdater(Cache.class, char[].class, "chars");
        defaultTimeZone = timeZone;
        defaultLocale = Locale.getDefault();
        DEFAULT_TYPE_KEY = "@type";
        DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        DEFAULT_PARSER_FEATURE = Feature.AutoCloseSource.getMask() | Feature.InternFieldNames.getMask() | Feature.UseBigDecimal.getMask() | Feature.AllowUnQuotedFieldNames.getMask() | Feature.AllowSingleQuotes.getMask() | Feature.AllowArbitraryCommas.getMask() | Feature.SortFeidFastMatch.getMask() | Feature.IgnoreNotMatch.getMask();
        DEFAULT_GENERATE_FEATURE = SerializerFeature.QuoteFieldNames.getMask() | SerializerFeature.SkipTransientField.getMask() | SerializerFeature.WriteEnumUsingName.getMask() | SerializerFeature.SortField.getMask();
        arraySupplier = new Supplier() { // from class: com.alibaba.fastjson.JSON$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return new JSONArray();
            }
        };
        defaultSupplier = JSONObject.Creator.INSTANCE;
        orderedSupplier = new Supplier() { // from class: com.alibaba.fastjson.JSON$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                return JSON.lambda$static$0();
            }
        };
        boolean z = JDKUtils.ANDROID;
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        if (!z) {
            defaultObjectReaderProvider.register(AwtRederModule.INSTANCE);
        }
        defaultObjectReaderProvider.register(new Fastjson1xReaderModule(defaultObjectReaderProvider));
        ObjectWriterProvider objectWriterProvider = SerializeConfig.DEFAULT_PROVIDER;
        if (!z) {
            objectWriterProvider.register(AwtWriterModule.INSTANCE);
        }
        objectWriterProvider.register(new Fastjson1xWriterModule(objectWriterProvider));
    }

    static /* synthetic */ Map lambda$static$0() {
        return new JSONObject(true);
    }

    public static JSONReader.Context createReadContext(int i, Feature... featureArr) {
        return createReadContext(JSONFactory.getDefaultObjectReaderProvider(), i, featureArr);
    }

    public static JSONReader.Context createReadContext(ObjectReaderProvider objectReaderProvider, int i, Feature... featureArr) {
        Supplier<Map> supplier;
        for (Feature feature : featureArr) {
            i |= feature.mask;
        }
        JSONReader.Context context = new JSONReader.Context(objectReaderProvider);
        if ((Feature.UseBigDecimal.mask & i) == 0) {
            context.config(JSONReader.Feature.UseBigDecimalForDoubles);
        }
        if ((Feature.SupportArrayToBean.mask & i) != 0) {
            context.config(JSONReader.Feature.SupportArrayToBean);
        }
        if ((Feature.ErrorOnEnumNotMatch.mask & i) != 0) {
            context.config(JSONReader.Feature.ErrorOnEnumNotMatch);
        }
        if ((Feature.SupportNonPublicField.mask & i) != 0) {
            context.config(JSONReader.Feature.FieldBased);
        }
        if ((Feature.SupportClassForName.mask & i) != 0) {
            context.config(JSONReader.Feature.SupportClassForName);
        }
        if ((Feature.TrimStringFieldValue.mask & i) != 0) {
            context.config(JSONReader.Feature.TrimString);
        }
        if ((Feature.ErrorOnNotSupportAutoType.mask & i) != 0) {
            context.config(JSONReader.Feature.ErrorOnNotSupportAutoType);
        }
        if ((Feature.AllowUnQuotedFieldNames.mask & i) != 0) {
            context.config(JSONReader.Feature.AllowUnQuotedFieldNames);
        }
        if ((Feature.UseNativeJavaObject.mask & i) != 0) {
            context.config(JSONReader.Feature.UseNativeObject);
        } else {
            context.setArraySupplier(arraySupplier);
            if ((Feature.OrderedField.mask & i) != 0) {
                supplier = orderedSupplier;
            } else {
                supplier = defaultSupplier;
            }
            context.setObjectSupplier(supplier);
        }
        if ((Feature.NonStringKeyAsString.mask & i) != 0) {
            context.config(JSONReader.Feature.NonStringKeyAsString);
        }
        if ((Feature.DisableFieldSmartMatch.mask & i) == 0) {
            context.config(JSONReader.Feature.SupportSmartMatch);
        }
        if ((Feature.SupportAutoType.mask & i) != 0) {
            context.config(JSONReader.Feature.SupportAutoType);
        }
        String str = DEFAULT_DATE_FORMAT;
        if (!"yyyy-MM-dd HH:mm:ss".equals(str)) {
            context.setDateFormat(str);
        }
        context.config(JSONReader.Feature.Base64StringAsByteArray);
        return context;
    }

    public static JSONObject parseObject(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, new Feature[0]));
        try {
            if (jSONReaderOf.nextIfNullOrEmptyString()) {
                return null;
            }
            HashMap map = new HashMap();
            jSONReaderOf.read(map, 0L);
            JSONObject jSONObject = new JSONObject(map);
            jSONReaderOf.handleResolveTasks(jSONObject);
            if (jSONReaderOf.isEnd()) {
                return jSONObject;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static JSONObject parseObject(String str, Feature... featureArr) {
        Map map;
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr);
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, contextCreateReadContext);
        String str2 = DEFAULT_DATE_FORMAT;
        if (!"yyyy-MM-dd HH:mm:ss".equals(str2)) {
            contextCreateReadContext.setDateFormat(str2);
        }
        int length = featureArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                map = new HashMap();
                break;
            }
            if (featureArr[i] == Feature.OrderedField) {
                map = new LinkedHashMap();
                break;
            }
            try {
                i++;
            } catch (com.alibaba.fastjson2.JSONException e) {
                Throwable cause = e.getCause();
                if (cause == null) {
                    cause = e;
                }
                throw new JSONException(e.getMessage(), cause);
            }
        }
        jSONReaderOf.read(map, 0L);
        JSONObject jSONObject = new JSONObject((Map<String, Object>) map);
        jSONReaderOf.handleResolveTasks(jSONObject);
        if (jSONReaderOf.isEnd()) {
            return jSONObject;
        }
        throw new JSONException(jSONReaderOf.info("input not end"));
    }

    public static <T> T parseObject(byte[] bArr, Charset charset, Type type, ParserConfig parserConfig, ParseProcess parseProcess, int i, Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        if (parserConfig == null) {
            parserConfig = ParserConfig.global;
        }
        JSONReader.Context contextCreateReadContext = createReadContext(parserConfig.getProvider(), i, featureArr);
        if (parseProcess != null) {
            contextCreateReadContext.config(parseProcess);
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(bArr, 0, bArr.length, charset, contextCreateReadContext);
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(byte[] bArr, int i, int i2, Charset charset, Type type, ParserConfig parserConfig, ParseProcess parseProcess, int i3, Feature... featureArr) {
        if (bArr == null || bArr.length == 0 || i2 == 0) {
            return null;
        }
        if (parserConfig == null) {
            parserConfig = ParserConfig.global;
        }
        JSONReader.Context contextCreateReadContext = createReadContext(parserConfig.getProvider(), i3, featureArr);
        if (parseProcess != null) {
            contextCreateReadContext.config(parseProcess);
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(bArr, i, i2, charset, contextCreateReadContext);
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(char[] cArr, int i, Type type, Feature... featureArr) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(cArr, 0, i, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
        try {
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(char[] cArr, Class<T> cls, Feature... featureArr) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(cArr, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
        try {
            T t = (T) jSONReaderOf.getObjectReader(cls).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(String str, Type type, ParserConfig parserConfig, ParseProcess parseProcess, int i, Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        if (parserConfig == null) {
            parserConfig = ParserConfig.global;
        }
        JSONReader.Context contextCreateReadContext = createReadContext(parserConfig.getProvider(), i, featureArr);
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, contextCreateReadContext);
        contextCreateReadContext.config(parseProcess);
        try {
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(String str, Type type, ParserConfig parserConfig, int i, Feature... featureArr) {
        return (T) parseObject(str, type, parserConfig, (ParseProcess) null, i, featureArr);
    }

    public static <T> T parseObject(String str, Type type, ParseProcess parseProcess, Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr);
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, contextCreateReadContext);
        contextCreateReadContext.config(parseProcess);
        try {
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(String str, Class<T> cls, ParseProcess parseProcess, Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr);
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, contextCreateReadContext);
        contextCreateReadContext.config(parseProcess);
        try {
            T t = (T) jSONReaderOf.getObjectReader(cls).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(String str, TypeReference<T> typeReference, Feature... featureArr) {
        return (T) parseObject(str, typeReference != null ? typeReference.getType() : Object.class, featureArr);
    }

    public static <T> T parseObject(String str, Type type, int i, Feature... featureArr) {
        return (T) parseObject(str, type, ParserConfig.global, i, featureArr);
    }

    public static <T> T parseObject(String str, Class<T> cls) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, new Feature[0]));
        try {
            T t = (T) jSONReaderOf.getObjectReader(cls).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(String str, Class<T> cls, Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
        try {
            T t = (T) jSONReaderOf.getObjectReader(cls).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(String str, Type type, Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
        try {
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(String str, Type type, ParserConfig parserConfig, Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = createReadContext(parserConfig.getProvider(), DEFAULT_PARSER_FEATURE, featureArr);
        if (parserConfig.fieldBase) {
            contextCreateReadContext.config(JSONReader.Feature.FieldBased);
        }
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, contextCreateReadContext);
        try {
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(InputStream inputStream, Type type, Feature... featureArr) throws IOException {
        return (T) parseObject(inputStream, StandardCharsets.UTF_8, type, featureArr);
    }

    public static <T> T parseObject(InputStream inputStream, Class<T> cls, Feature... featureArr) throws IOException {
        return (T) parseObject(inputStream, StandardCharsets.UTF_8, cls, featureArr);
    }

    public static <T> T parseObject(InputStream inputStream, Charset charset, Type type, ParserConfig parserConfig, ParseProcess parseProcess, int i, Feature... featureArr) throws IOException {
        if (inputStream == null) {
            return null;
        }
        if (parserConfig == null) {
            parserConfig = ParserConfig.global;
        }
        JSONReader.Context contextCreateReadContext = createReadContext(parserConfig.getProvider(), i, featureArr);
        if (parseProcess != null) {
            contextCreateReadContext.config(parseProcess);
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(inputStream, charset, contextCreateReadContext);
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(InputStream inputStream, Charset charset, Type type, ParserConfig parserConfig, Feature... featureArr) throws IOException {
        return (T) parseObject(inputStream, charset, type, parserConfig, (ParseProcess) null, DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static <T> T parseObject(InputStream inputStream, Charset charset, Type type, Feature... featureArr) throws IOException {
        if (inputStream == null) {
            return null;
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(inputStream, charset, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static JSONObject parseObject(byte[] bArr, Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr);
        int length = featureArr.length;
        boolean z = false;
        int i = 0;
        while (true) {
            if (i < length) {
                if (featureArr[i] == Feature.OrderedField) {
                    z = true;
                    break;
                }
                i++;
            }
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(bArr, contextCreateReadContext);
            Map linkedHashMap = z ? new LinkedHashMap() : new HashMap();
            jSONReaderOf.read(linkedHashMap, 0L);
            JSONObject jSONObject = new JSONObject((Map<String, Object>) linkedHashMap);
            jSONReaderOf.handleResolveTasks(jSONObject);
            if (jSONReaderOf.isEnd()) {
                return jSONObject;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(byte[] bArr, Class<T> cls, Feature... featureArr) {
        if (bArr == null) {
            return null;
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(bArr, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
            T t = (T) jSONReaderOf.getObjectReader(cls).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(byte[] bArr, Type type, Feature... featureArr) {
        if (bArr == null) {
            return null;
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(bArr, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(byte[] bArr, Type type, JSONReader.Context context) {
        if (bArr == null) {
            return null;
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(bArr, context);
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static <T> T parseObject(byte[] bArr, Type type, SerializeFilter serializeFilter, Feature... featureArr) {
        if (bArr == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr);
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(bArr, contextCreateReadContext);
            if (serializeFilter != null) {
                contextCreateReadContext.config(serializeFilter);
            }
            T t = (T) jSONReaderOf.getObjectReader(type).readObject(jSONReaderOf, null, null, 0L);
            if (t != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.isEnd()) {
                return t;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new JSONException(e.getMessage(), cause);
        }
    }

    public static Object parse(String str) {
        Object any;
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, new Feature[0]));
            try {
                if (jSONReaderOf.isObject() && !jSONReaderOf.isSupportAutoType(0L)) {
                    any = jSONReaderOf.read((Class<Object>) JSONObject.class);
                } else {
                    any = jSONReaderOf.readAny();
                }
                if (!jSONReaderOf.isEnd()) {
                    throw new JSONException(jSONReaderOf.info("input not end"));
                }
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return any;
            } finally {
            }
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
        throw new JSONException(e.getMessage(), e);
    }

    public static Object parse(String str, int i) {
        return parse(str, ParserConfig.global, i);
    }

    public static Object parse(String str, Feature... featureArr) {
        Object any;
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
            try {
                if (jSONReaderOf.isObject() && !jSONReaderOf.isSupportAutoType(0L)) {
                    any = jSONReaderOf.read((Class<Object>) JSONObject.class);
                } else {
                    any = jSONReaderOf.readAny();
                }
                if (!jSONReaderOf.isEnd()) {
                    throw new JSONException(jSONReaderOf.info("input not end"));
                }
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return any;
            } finally {
            }
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
        throw new JSONException(e.getMessage(), e);
    }

    public static Object parse(String str, ParserConfig parserConfig, Feature... featureArr) {
        Object obj;
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(parserConfig.getProvider(), DEFAULT_PARSER_FEATURE, featureArr));
            try {
                if (jSONReaderOf.isObject() && !jSONReaderOf.isSupportAutoType(0L)) {
                    obj = jSONReaderOf.read((Class<Object>) JSONObject.class);
                } else {
                    obj = jSONReaderOf.read((Class<Object>) Object.class);
                }
                if (!jSONReaderOf.isEnd()) {
                    throw new JSONException(jSONReaderOf.info("input not end"));
                }
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return obj;
            } finally {
            }
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
        throw new JSONException(e.getMessage(), e);
    }

    public static Object parse(String str, ParserConfig parserConfig) {
        Object any;
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(parserConfig.getProvider(), DEFAULT_PARSER_FEATURE, new Feature[0]));
            try {
                if (jSONReaderOf.isObject() && !jSONReaderOf.isSupportAutoType(0L)) {
                    any = jSONReaderOf.read((Class<Object>) JSONObject.class);
                } else {
                    any = jSONReaderOf.readAny();
                }
                if (!jSONReaderOf.isEnd()) {
                    throw new JSONException(jSONReaderOf.info("input not end"));
                }
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return any;
            } finally {
            }
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
        throw new JSONException(e.getMessage(), e);
    }

    public static Object parse(String str, ParserConfig parserConfig, int i) {
        Object any;
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(parserConfig.getProvider(), i, new Feature[0]));
            try {
                if (jSONReaderOf.isObject() && !jSONReaderOf.isSupportAutoType(0L)) {
                    any = jSONReaderOf.read((Class<Object>) JSONObject.class);
                } else {
                    any = jSONReaderOf.readAny();
                }
                if (!jSONReaderOf.isEnd()) {
                    throw new JSONException(jSONReaderOf.info("input not end"));
                }
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return any;
            } finally {
            }
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
        throw new JSONException(e.getMessage(), e);
    }

    public static Object parse(byte[] bArr, Feature... featureArr) {
        Object any;
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        try {
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(bArr, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
            try {
                if (jSONReaderOf.isObject() && !jSONReaderOf.isSupportAutoType(0L)) {
                    any = jSONReaderOf.read((Class<Object>) JSONObject.class);
                } else {
                    any = jSONReaderOf.readAny();
                }
                if (!jSONReaderOf.isEnd()) {
                    throw new JSONException(jSONReaderOf.info("input not end"));
                }
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return any;
            } finally {
            }
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
        throw new JSONException(e.getMessage(), e);
    }

    public static Object parse(byte[] bArr, int i, int i2, CharsetDecoder charsetDecoder, Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        int iConfig = DEFAULT_PARSER_FEATURE;
        for (Feature feature : featureArr) {
            iConfig = Feature.config(iConfig, feature, true);
        }
        return parse(bArr, i, i2, charsetDecoder, iConfig);
    }

    public static Object parse(byte[] bArr, int i, int i2, CharsetDecoder charsetDecoder, int i3) {
        charsetDecoder.reset();
        int iMaxCharsPerByte = (int) (((double) i2) * ((double) charsetDecoder.maxCharsPerByte()));
        char[] andSet = CHARS_UPDATER.getAndSet(CACHE, null);
        if (andSet == null || andSet.length < iMaxCharsPerByte) {
            andSet = new char[iMaxCharsPerByte];
        }
        try {
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, i, i2);
            CharBuffer charBufferWrap = CharBuffer.wrap(andSet);
            IOUtils.decode(charsetDecoder, byteBufferWrap, charBufferWrap);
            int iPosition = charBufferWrap.position();
            JSONReader.Context contextCreateReadContext = createReadContext(JSONFactory.getDefaultObjectReaderProvider(), i3, new Feature[0]);
            com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(andSet, 0, iPosition, contextCreateReadContext);
            for (Feature feature : Feature.values()) {
                if ((feature.mask & i3) != 0) {
                    int i4 = AnonymousClass1.$SwitchMap$com$alibaba$fastjson$parser$Feature[feature.ordinal()];
                    if (i4 == 1) {
                        contextCreateReadContext.config(JSONReader.Feature.SupportArrayToBean);
                    } else if (i4 == 2) {
                        contextCreateReadContext.config(JSONReader.Feature.SupportAutoType);
                    } else {
                        if (i4 == 3) {
                            contextCreateReadContext.config(JSONReader.Feature.ErrorOnEnumNotMatch);
                        } else if (i4 != 4) {
                        }
                        contextCreateReadContext.config(JSONReader.Feature.FieldBased);
                    }
                }
            }
            Object obj = jSONReaderOf.read((Class<Object>) Object.class);
            if (obj != null) {
                jSONReaderOf.handleResolveTasks(obj);
            }
            if (jSONReaderOf.isEnd()) {
                return obj;
            }
            throw new JSONException(jSONReaderOf.info("input not end"));
        } finally {
            if (andSet.length <= 65536) {
                CHARS_UPDATER.set(CACHE, andSet);
            }
        }
    }

    /* JADX INFO: renamed from: com.alibaba.fastjson.JSON$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$alibaba$fastjson$parser$Feature;

        static {
            int[] iArr = new int[Feature.values().length];
            $SwitchMap$com$alibaba$fastjson$parser$Feature = iArr;
            try {
                iArr[Feature.SupportArrayToBean.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.SupportAutoType.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.ErrorOnEnumNotMatch.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.SupportNonPublicField.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public static <T> T parseObject(byte[] bArr, int i, int i2, CharsetDecoder charsetDecoder, Type type, Feature... featureArr) {
        charsetDecoder.reset();
        int iMaxCharsPerByte = (int) (((double) i2) * ((double) charsetDecoder.maxCharsPerByte()));
        AtomicReferenceFieldUpdater<Cache, char[]> atomicReferenceFieldUpdater = CHARS_UPDATER;
        Cache cache = CACHE;
        char[] andSet = atomicReferenceFieldUpdater.getAndSet(cache, null);
        if (andSet == null || andSet.length < iMaxCharsPerByte) {
            andSet = new char[iMaxCharsPerByte];
        }
        try {
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, i, i2);
            CharBuffer charBufferWrap = CharBuffer.wrap(andSet);
            IOUtils.decode(charsetDecoder, byteBufferWrap, charBufferWrap);
            try {
                com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(andSet, 0, charBufferWrap.position(), createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
                try {
                    T t = (T) jSONReaderOf.read(type);
                    if (t != null) {
                        jSONReaderOf.handleResolveTasks(t);
                    }
                    if (!jSONReaderOf.isEnd()) {
                        throw new JSONException(jSONReaderOf.info("input not end"));
                    }
                    if (jSONReaderOf != null) {
                        jSONReaderOf.close();
                    }
                    if (andSet.length <= 65536) {
                        atomicReferenceFieldUpdater.set(cache, andSet);
                    }
                    return t;
                } catch (Throwable th) {
                    if (jSONReaderOf != null) {
                        try {
                            jSONReaderOf.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (com.alibaba.fastjson2.JSONException e) {
                throw new JSONException(e.getMessage(), e.getCause() != null ? e.getCause() : e);
            }
        } catch (Throwable th3) {
            if (andSet.length <= 65536) {
                CHARS_UPDATER.set(CACHE, andSet);
            }
            throw th3;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0047  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static <T> T parseObject(byte[] r2, int r3, int r4, java.nio.charset.Charset r5, java.lang.reflect.Type r6, com.alibaba.fastjson.parser.Feature... r7) {
        /*
            com.alibaba.fastjson2.reader.ObjectReaderProvider r0 = com.alibaba.fastjson2.JSONFactory.getDefaultObjectReaderProvider()     // Catch: com.alibaba.fastjson2.JSONException -> L3b
            int r1 = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE     // Catch: com.alibaba.fastjson2.JSONException -> L3b
            com.alibaba.fastjson2.JSONReader$Context r7 = createReadContext(r0, r1, r7)     // Catch: com.alibaba.fastjson2.JSONException -> L3b
            com.alibaba.fastjson2.JSONReader r2 = com.alibaba.fastjson2.JSONReader.of(r2, r3, r4, r5, r7)     // Catch: com.alibaba.fastjson2.JSONException -> L3b
            java.lang.Object r3 = r2.read(r6)     // Catch: java.lang.Throwable -> L2f
            if (r3 == 0) goto L17
            r2.handleResolveTasks(r3)     // Catch: java.lang.Throwable -> L2f
        L17:
            boolean r4 = r2.isEnd()     // Catch: java.lang.Throwable -> L2f
            if (r4 == 0) goto L23
            if (r2 == 0) goto L22
            r2.close()     // Catch: com.alibaba.fastjson2.JSONException -> L3b
        L22:
            return r3
        L23:
            com.alibaba.fastjson.JSONException r3 = new com.alibaba.fastjson.JSONException     // Catch: java.lang.Throwable -> L2f
            java.lang.String r4 = "input not end"
            java.lang.String r4 = r2.info(r4)     // Catch: java.lang.Throwable -> L2f
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L2f
            throw r3     // Catch: java.lang.Throwable -> L2f
        L2f:
            r3 = move-exception
            if (r2 == 0) goto L3a
            r2.close()     // Catch: java.lang.Throwable -> L36
            goto L3a
        L36:
            r2 = move-exception
            r3.addSuppressed(r2)     // Catch: com.alibaba.fastjson2.JSONException -> L3b
        L3a:
            throw r3     // Catch: com.alibaba.fastjson2.JSONException -> L3b
        L3b:
            r2 = move-exception
            java.lang.Throwable r3 = r2.getCause()
            if (r3 == 0) goto L47
            java.lang.Throwable r3 = r2.getCause()
            goto L48
        L47:
            r3 = r2
        L48:
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException
            java.lang.String r2 = r2.getMessage()
            r4.<init>(r2, r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSON.parseObject(byte[], int, int, java.nio.charset.Charset, java.lang.reflect.Type, com.alibaba.fastjson.parser.Feature[]):java.lang.Object");
    }

    public static JSONWriter.Context createWriteContext(SerializeConfig serializeConfig, int i, SerializerFeature... serializerFeatureArr) {
        for (SerializerFeature serializerFeature : serializerFeatureArr) {
            i |= serializerFeature.mask;
        }
        ObjectWriterProvider provider = serializeConfig.getProvider();
        provider.setCompatibleWithFieldName(TypeUtils.compatibleWithFieldName);
        JSONWriter.Context context = new JSONWriter.Context(provider);
        if (serializeConfig.fieldBased) {
            context.config(JSONWriter.Feature.FieldBased);
        }
        if (serializeConfig.propertyNamingStrategy != null && serializeConfig.propertyNamingStrategy != PropertyNamingStrategy.NeverUseThisValueExceptDefaultValue && serializeConfig.propertyNamingStrategy != PropertyNamingStrategy.CamelCase1x) {
            configFilter(context, NameFilter.of(serializeConfig.propertyNamingStrategy));
        }
        if ((SerializerFeature.DisableCircularReferenceDetect.mask & i) == 0) {
            context.config(JSONWriter.Feature.ReferenceDetection);
        }
        if ((SerializerFeature.UseISO8601DateFormat.mask & i) != 0) {
            context.setDateFormat("iso8601");
        } else {
            context.setDateFormat("millis");
        }
        if ((SerializerFeature.WriteMapNullValue.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteMapNullValue);
        }
        if ((SerializerFeature.WriteNullListAsEmpty.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteNullListAsEmpty);
        }
        if ((SerializerFeature.WriteNullStringAsEmpty.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteNullStringAsEmpty);
        }
        if ((SerializerFeature.WriteNullNumberAsZero.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteNullNumberAsZero);
        }
        if ((SerializerFeature.WriteNullBooleanAsFalse.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteNullBooleanAsFalse);
        }
        if ((SerializerFeature.BrowserCompatible.mask & i) != 0) {
            context.config(JSONWriter.Feature.BrowserCompatible);
            context.config(JSONWriter.Feature.EscapeNoneAscii);
        }
        if ((SerializerFeature.BrowserSecure.mask & i) != 0) {
            context.config(JSONWriter.Feature.BrowserSecure);
        }
        if ((SerializerFeature.WriteClassName.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteClassName);
        }
        if ((SerializerFeature.WriteNonStringValueAsString.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteNonStringValueAsString);
        }
        if ((SerializerFeature.WriteEnumUsingToString.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteEnumUsingToString);
        }
        if ((SerializerFeature.WriteEnumUsingName.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteEnumsUsingName);
        }
        if ((SerializerFeature.NotWriteRootClassName.mask & i) != 0) {
            context.config(JSONWriter.Feature.NotWriteRootClassName);
        }
        if ((SerializerFeature.IgnoreErrorGetter.mask & i) != 0) {
            context.config(JSONWriter.Feature.IgnoreErrorGetter);
        }
        if ((SerializerFeature.WriteDateUseDateFormat.mask & i) != 0) {
            context.setDateFormat(DEFAULT_DATE_FORMAT);
        }
        if ((SerializerFeature.BeanToArray.mask & i) != 0) {
            context.config(JSONWriter.Feature.BeanToArray);
        }
        if ((SerializerFeature.UseSingleQuotes.mask & i) != 0) {
            context.config(JSONWriter.Feature.UseSingleQuotes);
        }
        if ((SerializerFeature.MapSortField.mask & i) != 0) {
            context.config(JSONWriter.Feature.MapSortField);
        }
        if ((SerializerFeature.PrettyFormat.mask & i) != 0) {
            context.config(JSONWriter.Feature.PrettyFormat);
        }
        if ((SerializerFeature.WriteNonStringKeyAsString.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteNonStringKeyAsString);
        }
        if ((SerializerFeature.IgnoreNonFieldGetter.mask & i) != 0) {
            context.config(JSONWriter.Feature.IgnoreNonFieldGetter);
        }
        if ((SerializerFeature.NotWriteDefaultValue.mask & i) != 0) {
            context.config(JSONWriter.Feature.NotWriteDefaultValue);
        }
        if ((SerializerFeature.WriteBigDecimalAsPlain.mask & i) != 0) {
            context.config(JSONWriter.Feature.WriteBigDecimalAsPlain);
        }
        if ((SerializerFeature.QuoteFieldNames.mask & i) == 0 && (SerializerFeature.UseSingleQuotes.mask & i) == 0) {
            context.config(JSONWriter.Feature.UnquoteFieldName);
        }
        TimeZone timeZone = defaultTimeZone;
        if (timeZone != null && timeZone != DEFAULT_TIME_ZONE) {
            context.setZoneId(timeZone.toZoneId());
        }
        context.config(JSONWriter.Feature.WriteByteArrayAsBase64);
        context.config(JSONWriter.Feature.WriteThrowableClassName);
        return context;
    }

    public static String toJSONString(Object obj, SerializeConfig serializeConfig, SerializeFilter[] serializeFilterArr, String str, int i, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(serializeConfig, i, serializerFeatureArr);
        if (str != null && !str.isEmpty()) {
            contextCreateWriteContext.setDateFormat(str);
        }
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(contextCreateWriteContext);
            try {
                for (SerializeFilter serializeFilter : serializeFilterArr) {
                    configFilter(contextCreateWriteContext, serializeFilter);
                }
                if (obj == null) {
                    jSONWriterOf.writeNull();
                } else {
                    jSONWriterOf.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
                }
                String string = jSONWriterOf.toString();
                if (jSONWriterOf != null) {
                    jSONWriterOf.close();
                }
                return string;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONString error", e2);
        }
    }

    public static String toJSONString(Object obj, SerializeConfig serializeConfig, SerializeFilter[] serializeFilterArr, SerializerFeature... serializerFeatureArr) {
        return toJSONString(obj, serializeConfig, serializeFilterArr, null, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
    }

    public static String toJSONString(Object obj, SerializeFilter[] serializeFilterArr, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(contextCreateWriteContext);
            try {
                for (SerializeFilter serializeFilter : serializeFilterArr) {
                    configFilter(contextCreateWriteContext, serializeFilter);
                }
                if (obj == null) {
                    jSONWriterOf.writeNull();
                } else {
                    jSONWriterOf.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
                }
                String string = jSONWriterOf.toString();
                if (jSONWriterOf != null) {
                    jSONWriterOf.close();
                }
                return string;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONString error", e2);
        }
    }

    public static void configFilter(JSONWriter.Context context, SerializeFilter serializeFilter) {
        if (serializeFilter instanceof NameFilter) {
            context.setNameFilter((NameFilter) serializeFilter);
        }
        if (serializeFilter instanceof ValueFilter) {
            context.setValueFilter((ValueFilter) serializeFilter);
        }
        if (serializeFilter instanceof PropertyPreFilter) {
            context.setPropertyPreFilter((PropertyPreFilter) serializeFilter);
        }
        if (serializeFilter instanceof PropertyFilter) {
            context.setPropertyFilter((PropertyFilter) serializeFilter);
        }
        if (serializeFilter instanceof BeforeFilter) {
            context.setBeforeFilter((BeforeFilter) serializeFilter);
        }
        if (serializeFilter instanceof AfterFilter) {
            context.setAfterFilter((AfterFilter) serializeFilter);
        }
        if (serializeFilter instanceof LabelFilter) {
            context.setLabelFilter((LabelFilter) serializeFilter);
        }
        if (serializeFilter instanceof ContextValueFilter) {
            context.setContextValueFilter((ContextValueFilter) serializeFilter);
        }
    }

    public static byte[] toJSONBytes(Object obj, SerializeConfig serializeConfig, SerializeFilter[] serializeFilterArr, int i, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(serializeConfig, i, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                for (SerializeFilter serializeFilter : serializeFilterArr) {
                    configFilter(contextCreateWriteContext, serializeFilter);
                }
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return bytes;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONBytes error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONBytes error", e2);
        }
    }

    public static byte[] toJSONBytes(Object obj, SerializeConfig serializeConfig, SerializeFilter serializeFilter, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(serializeConfig, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                configFilter(contextCreateWriteContext, serializeFilter);
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return bytes;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONBytes error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONBytes error", e2);
        }
    }

    public static byte[] toJSONBytes(Charset charset, Object obj, SerializeConfig serializeConfig, SerializeFilter[] serializeFilterArr, String str, int i, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(serializeConfig, i, serializerFeatureArr);
        if (str != null && !str.isEmpty()) {
            contextCreateWriteContext.setDateFormat(str);
        }
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                for (SerializeFilter serializeFilter : serializeFilterArr) {
                    configFilter(contextCreateWriteContext, serializeFilter);
                }
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes(charset);
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return bytes;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONBytes error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONBytes error", e2);
        }
    }

    public static byte[] toJSONBytes(Object obj, SerializeFilter[] serializeFilterArr, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                for (SerializeFilter serializeFilter : serializeFilterArr) {
                    configFilter(contextCreateWriteContext, serializeFilter);
                }
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return bytes;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONBytes error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONBytes error", e2);
        }
    }

    public static byte[] toJSONBytes(Object obj, SerializeConfig serializeConfig, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(serializeConfig, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return bytes;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONBytes error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONBytes error", e2);
        }
    }

    public static String toJSONString(Object obj, boolean z) {
        SerializerFeature[] serializerFeatureArr;
        if (z) {
            serializerFeatureArr = new SerializerFeature[]{SerializerFeature.PrettyFormat};
        } else {
            serializerFeatureArr = SerializerFeature.EMPTY;
        }
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(contextCreateWriteContext);
            try {
                if (obj == null) {
                    jSONWriterOf.writeNull();
                } else {
                    jSONWriterOf.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
                }
                String string = jSONWriterOf.toString();
                if (jSONWriterOf != null) {
                    jSONWriterOf.close();
                }
                return string;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONString error", e2);
        }
    }

    public static String toJSONString(Object obj) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, new SerializerFeature[0]);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(contextCreateWriteContext);
            try {
                if (obj == null) {
                    jSONWriterOf.writeNull();
                } else {
                    jSONWriterOf.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
                }
                String string = jSONWriterOf.toString();
                if (jSONWriterOf != null) {
                    jSONWriterOf.close();
                }
                return string;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            throw new JSONException(e.getMessage(), e.getCause() != null ? e.getCause() : e);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONString error", e2);
        }
    }

    public static String toJSONString(Object obj, SerializeFilter serializeFilter, SerializeFilter serializeFilter2, SerializeFilter... serializeFilterArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, new SerializerFeature[0]);
        configFilter(contextCreateWriteContext, serializeFilter);
        configFilter(contextCreateWriteContext, serializeFilter2);
        for (SerializeFilter serializeFilter3 : serializeFilterArr) {
            configFilter(contextCreateWriteContext, serializeFilter3);
        }
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(contextCreateWriteContext);
            try {
                if (obj == null) {
                    jSONWriterOf.writeNull();
                } else {
                    jSONWriterOf.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
                }
                String string = jSONWriterOf.toString();
                if (jSONWriterOf != null) {
                    jSONWriterOf.close();
                }
                return string;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONString error", e2);
        }
    }

    public static String toJSONString(Object obj, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(contextCreateWriteContext);
            try {
                if (obj == null) {
                    jSONWriterOf.writeNull();
                } else {
                    jSONWriterOf.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
                }
                String string = jSONWriterOf.toString();
                if (jSONWriterOf != null) {
                    jSONWriterOf.close();
                }
                return string;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONString error", e2);
        }
    }

    public static byte[] toJSONBytes(Object obj) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, new SerializerFeature[0]);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return bytes;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONBytes error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONBytes error", e2);
        }
    }

    public static byte[] toJSONBytes(Object obj, SerializeFilter... serializeFilterArr) {
        return toJSONBytes(obj, serializeFilterArr, SerializerFeature.EMPTY);
    }

    public static byte[] toJSONBytes(Object obj, int i, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, i, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return bytes;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONBytes error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONBytes error", e2);
        }
    }

    public static byte[] toJSONBytes(Object obj, SerializeFilter serializeFilter) {
        return toJSONBytes(obj, SerializeConfig.global, new SerializeFilter[]{serializeFilter}, DEFAULT_GENERATE_FEATURE, new SerializerFeature[0]);
    }

    public static byte[] toJSONBytes(Object obj, SerializeFilter serializeFilter, SerializerFeature... serializerFeatureArr) {
        return toJSONBytes(obj, SerializeConfig.global, new SerializeFilter[]{serializeFilter}, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
    }

    public static byte[] toJSONBytes(Object obj, SerializeConfig serializeConfig, SerializeFilter[] serializeFilterArr, String str, int i, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(serializeConfig, i, serializerFeatureArr);
        if (str != null && !str.isEmpty()) {
            contextCreateWriteContext.setDateFormat(str);
        }
        for (SerializeFilter serializeFilter : serializeFilterArr) {
            configFilter(contextCreateWriteContext, serializeFilter);
        }
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return bytes;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONBytes error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONBytes error", e2);
        }
    }

    public static byte[] toJSONBytes(Object obj, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    Class<?> cls = obj.getClass();
                    contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return bytes;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("toJSONBytes error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("toJSONBytes error", e2);
        }
    }

    public static byte[] toJSONBytes(Object obj, SerializeConfig serializeConfig, int i, SerializerFeature... serializerFeatureArr) {
        return toJSONBytes(obj, serializeConfig, new SerializeFilter[0], i, serializerFeatureArr);
    }

    public static String toJSONString(Object obj, SerializeConfig serializeConfig, SerializerFeature... serializerFeatureArr) {
        com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(createWriteContext(serializeConfig, DEFAULT_GENERATE_FEATURE, serializerFeatureArr));
        try {
            jSONWriterOf.setRootObject(obj);
            jSONWriterOf.writeAny(obj);
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } catch (Throwable th) {
            if (jSONWriterOf != null) {
                try {
                    jSONWriterOf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static String toJSONStringZ(Object obj, SerializeConfig serializeConfig, SerializerFeature... serializerFeatureArr) {
        return toJSONString(obj, serializeConfig, new SerializeFilter[0], null, 0, serializerFeatureArr);
    }

    public static String toJSONString(Object obj, SerializeConfig serializeConfig, SerializeFilter serializeFilter, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(serializeConfig, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(contextCreateWriteContext);
        try {
            if (serializeConfig.propertyNamingStrategy != null && serializeConfig.propertyNamingStrategy != PropertyNamingStrategy.NeverUseThisValueExceptDefaultValue) {
                NameFilter nameFilterOf = NameFilter.of(serializeConfig.propertyNamingStrategy);
                if (serializeFilter instanceof NameFilter) {
                    serializeFilter = NameFilter.compose(nameFilterOf, (NameFilter) serializeFilter);
                } else {
                    configFilter(contextCreateWriteContext, nameFilterOf);
                }
            }
            configFilter(contextCreateWriteContext, serializeFilter);
            if (obj == null) {
                jSONWriterOf.writeNull();
            } else {
                jSONWriterOf.setRootObject(obj);
                Class<?> cls = obj.getClass();
                contextCreateWriteContext.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
            }
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } catch (Throwable th) {
            if (jSONWriterOf == null) {
                throw th;
            }
            try {
                jSONWriterOf.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    public static String toJSONString(Object obj, SerializeFilter serializeFilter, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        configFilter(contextCreateWriteContext, serializeFilter);
        com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(contextCreateWriteContext);
        try {
            if (obj == null) {
                jSONWriterOf.writeNull();
            } else {
                jSONWriterOf.setRootObject(obj);
                contextCreateWriteContext.getObjectWriter(obj.getClass()).write(jSONWriterOf, obj, null, null, 0L);
            }
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } catch (Throwable th) {
            if (jSONWriterOf == null) {
                throw th;
            }
            try {
                jSONWriterOf.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    public static String toJSONString(Object obj, int i, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, i, serializerFeatureArr);
        com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(contextCreateWriteContext);
        try {
            if (obj == null) {
                jSONWriterOf.writeNull();
            } else {
                jSONWriterOf.setRootObject(obj);
                contextCreateWriteContext.getObjectWriter(obj.getClass()).write(jSONWriterOf, obj, null, null, 0L);
            }
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } catch (Throwable th) {
            if (jSONWriterOf == null) {
                throw th;
            }
            try {
                jSONWriterOf.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    public static String toJSONStringWithDateFormat(Object obj, String str, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        com.alibaba.fastjson2.JSONWriter jSONWriterOf = com.alibaba.fastjson2.JSONWriter.of(contextCreateWriteContext);
        try {
            contextCreateWriteContext.setDateFormat(str);
            if (obj == null) {
                jSONWriterOf.writeNull();
            } else {
                jSONWriterOf.setRootObject(obj);
                contextCreateWriteContext.getObjectWriter(obj.getClass()).write(jSONWriterOf, obj, null, null, 0L);
            }
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } catch (Throwable th) {
            if (jSONWriterOf == null) {
                throw th;
            }
            try {
                jSONWriterOf.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    public static final int writeJSONString(OutputStream outputStream, Object obj, SerializerFeature... serializerFeatureArr) throws IOException {
        return writeJSONString(outputStream, obj, new SerializeFilter[0], serializerFeatureArr);
    }

    public static final int writeJSONString(OutputStream outputStream, Object obj, SerializeFilter[] serializeFilterArr) throws IOException {
        return writeJSONString(outputStream, obj, serializeFilterArr, SerializerFeature.EMPTY);
    }

    public static final int writeJSONString(OutputStream outputStream, Charset charset, Object obj, SerializerFeature... serializerFeatureArr) throws IOException {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    contextCreateWriteContext.getObjectWriter(obj.getClass()).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes(charset);
                outputStream.write(bytes);
                int length = bytes.length;
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return length;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("writeJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("writeJSONString error", e2);
        }
    }

    public static void writeJSONString(Writer writer, Object obj, SerializerFeature... serializerFeatureArr) {
        writeJSONString(writer, obj, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
    }

    public static void writeJSONString(Writer writer, Object obj, int i, SerializerFeature... serializerFeatureArr) {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, i, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                jSONWriterOfUTF8.setRootObject(obj);
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    contextCreateWriteContext.getObjectWriter(obj.getClass()).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                jSONWriterOfUTF8.flushTo(writer);
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("writeJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("writeJSONString error", e2);
        }
    }

    public static final int writeJSONString(OutputStream outputStream, Object obj, int i, SerializerFeature... serializerFeatureArr) throws IOException {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, i, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                jSONWriterOfUTF8.setRootObject(obj);
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    contextCreateWriteContext.getObjectWriter(obj.getClass()).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                outputStream.write(bytes);
                int length = bytes.length;
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return length;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("writeJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("writeJSONString error", e2);
        }
    }

    public static final int writeJSONString(OutputStream outputStream, Charset charset, Object obj, SerializeConfig serializeConfig, SerializeFilter[] serializeFilterArr, String str, int i, SerializerFeature... serializerFeatureArr) throws IOException {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(serializeConfig, i, serializerFeatureArr);
        if (str != null && !str.isEmpty()) {
            contextCreateWriteContext.setDateFormat(str);
        }
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                for (SerializeFilter serializeFilter : serializeFilterArr) {
                    configFilter(contextCreateWriteContext, serializeFilter);
                }
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    contextCreateWriteContext.getObjectWriter(obj.getClass()).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes(charset);
                outputStream.write(bytes);
                int length = bytes.length;
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return length;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            Throwable cause = e.getCause();
            com.alibaba.fastjson2.JSONException cause2 = e;
            if (cause != null) {
                cause2 = e.getCause();
            }
            throw new JSONException("writeJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("writeJSONString error", e2);
        }
    }

    public static final int writeJSONString(OutputStream outputStream, Object obj, SerializeFilter[] serializeFilterArr, SerializerFeature... serializerFeatureArr) throws IOException {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                for (SerializeFilter serializeFilter : serializeFilterArr) {
                    configFilter(contextCreateWriteContext, serializeFilter);
                }
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    contextCreateWriteContext.getObjectWriter(obj.getClass()).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                outputStream.write(bytes);
                int length = bytes.length;
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return length;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("writeJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("writeJSONString error", e2);
        }
    }

    public static final int writeJSONString(OutputStream outputStream, Object obj, String str, SerializeFilter[] serializeFilterArr, SerializerFeature... serializerFeatureArr) throws IOException {
        JSONWriter.Context contextCreateWriteContext = createWriteContext(SerializeConfig.global, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        if (str != null) {
            contextCreateWriteContext.setDateFormat(str);
        }
        try {
            com.alibaba.fastjson2.JSONWriter jSONWriterOfUTF8 = com.alibaba.fastjson2.JSONWriter.ofUTF8(contextCreateWriteContext);
            try {
                for (SerializeFilter serializeFilter : serializeFilterArr) {
                    configFilter(contextCreateWriteContext, serializeFilter);
                }
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.setRootObject(obj);
                    contextCreateWriteContext.getObjectWriter(obj.getClass()).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                byte[] bytes = jSONWriterOfUTF8.getBytes();
                outputStream.write(bytes);
                int length = bytes.length;
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return length;
            } finally {
            }
        } catch (com.alibaba.fastjson2.JSONException e) {
            com.alibaba.fastjson2.JSONException jSONException = e;
            Throwable cause = jSONException.getCause();
            com.alibaba.fastjson2.JSONException cause2 = jSONException;
            if (cause != null) {
                cause2 = jSONException.getCause();
            }
            throw new JSONException("writeJSONString error", cause2);
        } catch (RuntimeException e2) {
            throw new JSONException("writeJSONString error", e2);
        }
    }

    public static JSONArray parseArray(String str, Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_PARSER_FEATURE, featureArr));
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            jSONReaderOf.read((List) jSONArray);
            if (!jSONReaderOf.isEnd()) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONArray;
        } catch (Throwable th) {
            if (jSONReaderOf != null) {
                try {
                    jSONReaderOf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.alibaba.fastjson.JSONArray parseArray(java.lang.String r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L6d
            boolean r1 = r4.isEmpty()
            if (r1 == 0) goto La
            goto L6d
        La:
            com.alibaba.fastjson2.reader.ObjectReaderProvider r1 = com.alibaba.fastjson2.JSONFactory.getDefaultObjectReaderProvider()
            int r2 = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE
            r3 = 0
            com.alibaba.fastjson.parser.Feature[] r3 = new com.alibaba.fastjson.parser.Feature[r3]
            com.alibaba.fastjson2.JSONReader$Context r1 = createReadContext(r1, r2, r3)
            com.alibaba.fastjson2.JSONReader r4 = com.alibaba.fastjson2.JSONReader.of(r4, r1)     // Catch: com.alibaba.fastjson2.JSONException -> L5b
            boolean r1 = r4.nextIfNullOrEmptyString()     // Catch: java.lang.Throwable -> L4f
            if (r1 == 0) goto L27
            if (r4 == 0) goto L26
            r4.close()     // Catch: com.alibaba.fastjson2.JSONException -> L5b
        L26:
            return r0
        L27:
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L4f
            r0.<init>()     // Catch: java.lang.Throwable -> L4f
            r4.read(r0)     // Catch: java.lang.Throwable -> L4f
            com.alibaba.fastjson.JSONArray r1 = new com.alibaba.fastjson.JSONArray     // Catch: java.lang.Throwable -> L4f
            r1.<init>(r0)     // Catch: java.lang.Throwable -> L4f
            r4.handleResolveTasks(r1)     // Catch: java.lang.Throwable -> L4f
            boolean r0 = r4.isEnd()     // Catch: java.lang.Throwable -> L4f
            if (r0 == 0) goto L43
            if (r4 == 0) goto L42
            r4.close()     // Catch: com.alibaba.fastjson2.JSONException -> L5b
        L42:
            return r1
        L43:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch: java.lang.Throwable -> L4f
            java.lang.String r1 = "input not end"
            java.lang.String r1 = r4.info(r1)     // Catch: java.lang.Throwable -> L4f
            r0.<init>(r1)     // Catch: java.lang.Throwable -> L4f
            throw r0     // Catch: java.lang.Throwable -> L4f
        L4f:
            r0 = move-exception
            if (r4 == 0) goto L5a
            r4.close()     // Catch: java.lang.Throwable -> L56
            goto L5a
        L56:
            r4 = move-exception
            r0.addSuppressed(r4)     // Catch: com.alibaba.fastjson2.JSONException -> L5b
        L5a:
            throw r0     // Catch: com.alibaba.fastjson2.JSONException -> L5b
        L5b:
            r4 = move-exception
            java.lang.Throwable r0 = r4.getCause()
            if (r0 != 0) goto L63
            r0 = r4
        L63:
            com.alibaba.fastjson.JSONException r1 = new com.alibaba.fastjson.JSONException
            java.lang.String r4 = r4.getMessage()
            r1.<init>(r4, r0)
            throw r1
        L6d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSON.parseArray(java.lang.String):com.alibaba.fastjson.JSONArray");
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x005b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static <T> java.util.List<T> parseArray(java.lang.String r4, java.lang.Class<T> r5) {
        /*
            r0 = 0
            if (r4 == 0) goto L66
            boolean r1 = r4.isEmpty()
            if (r1 == 0) goto La
            goto L66
        La:
            com.alibaba.fastjson2.util.ParameterizedTypeImpl r1 = new com.alibaba.fastjson2.util.ParameterizedTypeImpl
            r2 = 1
            java.lang.reflect.Type[] r2 = new java.lang.reflect.Type[r2]
            r3 = 0
            r2[r3] = r5
            java.lang.Class<java.util.List> r5 = java.util.List.class
            r1.<init>(r2, r0, r5)
            com.alibaba.fastjson2.reader.ObjectReaderProvider r5 = com.alibaba.fastjson2.JSONFactory.getDefaultObjectReaderProvider()     // Catch: com.alibaba.fastjson2.JSONException -> L54
            int r0 = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE     // Catch: com.alibaba.fastjson2.JSONException -> L54
            com.alibaba.fastjson.parser.Feature[] r2 = new com.alibaba.fastjson.parser.Feature[r3]     // Catch: com.alibaba.fastjson2.JSONException -> L54
            com.alibaba.fastjson2.JSONReader$Context r5 = createReadContext(r5, r0, r2)     // Catch: com.alibaba.fastjson2.JSONException -> L54
            com.alibaba.fastjson2.JSONReader r4 = com.alibaba.fastjson2.JSONReader.of(r4, r5)     // Catch: com.alibaba.fastjson2.JSONException -> L54
            java.lang.Object r5 = r4.read(r1)     // Catch: java.lang.Throwable -> L48
            java.util.List r5 = (java.util.List) r5     // Catch: java.lang.Throwable -> L48
            r4.handleResolveTasks(r5)     // Catch: java.lang.Throwable -> L48
            boolean r0 = r4.isEnd()     // Catch: java.lang.Throwable -> L48
            if (r0 == 0) goto L3c
            if (r4 == 0) goto L3b
            r4.close()     // Catch: com.alibaba.fastjson2.JSONException -> L54
        L3b:
            return r5
        L3c:
            com.alibaba.fastjson.JSONException r5 = new com.alibaba.fastjson.JSONException     // Catch: java.lang.Throwable -> L48
            java.lang.String r0 = "input not end"
            java.lang.String r0 = r4.info(r0)     // Catch: java.lang.Throwable -> L48
            r5.<init>(r0)     // Catch: java.lang.Throwable -> L48
            throw r5     // Catch: java.lang.Throwable -> L48
        L48:
            r5 = move-exception
            if (r4 == 0) goto L53
            r4.close()     // Catch: java.lang.Throwable -> L4f
            goto L53
        L4f:
            r4 = move-exception
            r5.addSuppressed(r4)     // Catch: com.alibaba.fastjson2.JSONException -> L54
        L53:
            throw r5     // Catch: com.alibaba.fastjson2.JSONException -> L54
        L54:
            r4 = move-exception
            java.lang.Throwable r5 = r4.getCause()
            if (r5 != 0) goto L5c
            r5 = r4
        L5c:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.String r4 = r4.getMessage()
            r0.<init>(r4, r5)
            throw r0
        L66:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSON.parseArray(java.lang.String, java.lang.Class):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static <T> java.util.List<T> parseArray(java.lang.String r4, java.lang.Class<T> r5, com.alibaba.fastjson.parser.ParserConfig r6) {
        /*
            r0 = 0
            if (r4 == 0) goto L6a
            boolean r1 = r4.isEmpty()
            if (r1 == 0) goto La
            goto L6a
        La:
            if (r6 != 0) goto Le
            com.alibaba.fastjson.parser.ParserConfig r6 = com.alibaba.fastjson.parser.ParserConfig.global
        Le:
            com.alibaba.fastjson2.util.ParameterizedTypeImpl r1 = new com.alibaba.fastjson2.util.ParameterizedTypeImpl
            r2 = 1
            java.lang.reflect.Type[] r2 = new java.lang.reflect.Type[r2]
            r3 = 0
            r2[r3] = r5
            java.lang.Class<java.util.List> r5 = java.util.List.class
            r1.<init>(r2, r0, r5)
            com.alibaba.fastjson2.reader.ObjectReaderProvider r5 = r6.getProvider()     // Catch: com.alibaba.fastjson2.JSONException -> L58
            int r6 = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE     // Catch: com.alibaba.fastjson2.JSONException -> L58
            com.alibaba.fastjson.parser.Feature[] r0 = new com.alibaba.fastjson.parser.Feature[r3]     // Catch: com.alibaba.fastjson2.JSONException -> L58
            com.alibaba.fastjson2.JSONReader$Context r5 = createReadContext(r5, r6, r0)     // Catch: com.alibaba.fastjson2.JSONException -> L58
            com.alibaba.fastjson2.JSONReader r4 = com.alibaba.fastjson2.JSONReader.of(r4, r5)     // Catch: com.alibaba.fastjson2.JSONException -> L58
            java.lang.Object r5 = r4.read(r1)     // Catch: java.lang.Throwable -> L4c
            java.util.List r5 = (java.util.List) r5     // Catch: java.lang.Throwable -> L4c
            r4.handleResolveTasks(r5)     // Catch: java.lang.Throwable -> L4c
            boolean r6 = r4.isEnd()     // Catch: java.lang.Throwable -> L4c
            if (r6 == 0) goto L40
            if (r4 == 0) goto L3f
            r4.close()     // Catch: com.alibaba.fastjson2.JSONException -> L58
        L3f:
            return r5
        L40:
            com.alibaba.fastjson.JSONException r5 = new com.alibaba.fastjson.JSONException     // Catch: java.lang.Throwable -> L4c
            java.lang.String r6 = "input not end"
            java.lang.String r6 = r4.info(r6)     // Catch: java.lang.Throwable -> L4c
            r5.<init>(r6)     // Catch: java.lang.Throwable -> L4c
            throw r5     // Catch: java.lang.Throwable -> L4c
        L4c:
            r5 = move-exception
            if (r4 == 0) goto L57
            r4.close()     // Catch: java.lang.Throwable -> L53
            goto L57
        L53:
            r4 = move-exception
            r5.addSuppressed(r4)     // Catch: com.alibaba.fastjson2.JSONException -> L58
        L57:
            throw r5     // Catch: com.alibaba.fastjson2.JSONException -> L58
        L58:
            r4 = move-exception
            java.lang.Throwable r5 = r4.getCause()
            if (r5 != 0) goto L60
            r5 = r4
        L60:
            com.alibaba.fastjson.JSONException r6 = new com.alibaba.fastjson.JSONException
            java.lang.String r4 = r4.getMessage()
            r6.<init>(r4, r5)
            throw r6
        L6a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSON.parseArray(java.lang.String, java.lang.Class, com.alibaba.fastjson.parser.ParserConfig):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0059  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static <T> java.util.List<T> parseArray(java.lang.String r4, java.lang.Class<T> r5, com.alibaba.fastjson.parser.Feature... r6) {
        /*
            r0 = 0
            if (r4 == 0) goto L64
            boolean r1 = r4.isEmpty()
            if (r1 == 0) goto La
            goto L64
        La:
            com.alibaba.fastjson2.util.ParameterizedTypeImpl r1 = new com.alibaba.fastjson2.util.ParameterizedTypeImpl
            r2 = 1
            java.lang.reflect.Type[] r2 = new java.lang.reflect.Type[r2]
            r3 = 0
            r2[r3] = r5
            java.lang.Class<java.util.List> r5 = java.util.List.class
            r1.<init>(r2, r0, r5)
            com.alibaba.fastjson2.reader.ObjectReaderProvider r5 = com.alibaba.fastjson2.JSONFactory.getDefaultObjectReaderProvider()     // Catch: com.alibaba.fastjson2.JSONException -> L52
            int r0 = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE     // Catch: com.alibaba.fastjson2.JSONException -> L52
            com.alibaba.fastjson2.JSONReader$Context r5 = createReadContext(r5, r0, r6)     // Catch: com.alibaba.fastjson2.JSONException -> L52
            com.alibaba.fastjson2.JSONReader r4 = com.alibaba.fastjson2.JSONReader.of(r4, r5)     // Catch: com.alibaba.fastjson2.JSONException -> L52
            java.lang.Object r5 = r4.read(r1)     // Catch: java.lang.Throwable -> L46
            java.util.List r5 = (java.util.List) r5     // Catch: java.lang.Throwable -> L46
            r4.handleResolveTasks(r5)     // Catch: java.lang.Throwable -> L46
            boolean r6 = r4.isEnd()     // Catch: java.lang.Throwable -> L46
            if (r6 == 0) goto L3a
            if (r4 == 0) goto L39
            r4.close()     // Catch: com.alibaba.fastjson2.JSONException -> L52
        L39:
            return r5
        L3a:
            com.alibaba.fastjson.JSONException r5 = new com.alibaba.fastjson.JSONException     // Catch: java.lang.Throwable -> L46
            java.lang.String r6 = "input not end"
            java.lang.String r6 = r4.info(r6)     // Catch: java.lang.Throwable -> L46
            r5.<init>(r6)     // Catch: java.lang.Throwable -> L46
            throw r5     // Catch: java.lang.Throwable -> L46
        L46:
            r5 = move-exception
            if (r4 == 0) goto L51
            r4.close()     // Catch: java.lang.Throwable -> L4d
            goto L51
        L4d:
            r4 = move-exception
            r5.addSuppressed(r4)     // Catch: com.alibaba.fastjson2.JSONException -> L52
        L51:
            throw r5     // Catch: com.alibaba.fastjson2.JSONException -> L52
        L52:
            r4 = move-exception
            java.lang.Throwable r5 = r4.getCause()
            if (r5 != 0) goto L5a
            r5 = r4
        L5a:
            com.alibaba.fastjson.JSONException r6 = new com.alibaba.fastjson.JSONException
            java.lang.String r4 = r4.getMessage()
            r6.<init>(r4, r5)
            throw r6
        L64:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSON.parseArray(java.lang.String, java.lang.Class, com.alibaba.fastjson.parser.Feature[]):java.util.List");
    }

    public static boolean isValid(String str) {
        return com.alibaba.fastjson2.JSON.isValid(str);
    }

    public static boolean isValidArray(String str) {
        return com.alibaba.fastjson2.JSON.isValidArray(str);
    }

    public static boolean isValidObject(String str) {
        return com.alibaba.fastjson2.JSON.isValidObject(str);
    }

    public <T> T toJavaObject(TypeReference<T> typeReference) {
        return (T) toJavaObject(typeReference != null ? typeReference.getType() : Object.class);
    }

    public static <T> T toJavaObject(JSON json, Class<T> cls) {
        if (json instanceof JSONObject) {
            return (T) json.toJavaObject((Class) cls);
        }
        return (T) parseObject(toJSONString(json), cls);
    }

    public static Object toJSON(Object obj) {
        if (obj instanceof JSON) {
            return obj;
        }
        try {
            return adaptResult(com.alibaba.fastjson2.JSON.toJSON(obj));
        } catch (com.alibaba.fastjson2.JSONException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static Object adaptResult(Object obj) {
        return adaptResult(obj, 0);
    }

    private static Object adaptResult(Object obj, int i) {
        if (i > 2048) {
            throw new JSONException("level too large : " + i);
        }
        if (obj instanceof com.alibaba.fastjson2.JSONObject) {
            JSONObject jSONObject = new JSONObject();
            for (Map.Entry<String, Object> entry : ((com.alibaba.fastjson2.JSONObject) obj).entrySet()) {
                jSONObject.put(entry.getKey(), adaptResult(entry.getValue(), i + 1));
            }
            return jSONObject;
        }
        if (!(obj instanceof com.alibaba.fastjson2.JSONArray)) {
            return obj;
        }
        JSONArray jSONArray = new JSONArray();
        com.alibaba.fastjson2.JSONArray jSONArray2 = (com.alibaba.fastjson2.JSONArray) obj;
        for (int i2 = 0; i2 < jSONArray2.size(); i2++) {
            jSONArray.set(i2, adaptResult(jSONArray2.get(i2), i + 1));
        }
        return jSONArray;
    }

    public static Object toJSON(Object obj, SerializeConfig serializeConfig) {
        if (obj instanceof JSON) {
            return obj;
        }
        Object obj2 = parse(toJSONString(obj, serializeConfig, new SerializerFeature[0]));
        return obj2 instanceof List ? new JSONArray((List) obj2) : obj2;
    }

    public static Object toJSON(Object obj, ParserConfig parserConfig) {
        if (obj instanceof JSON) {
            return obj;
        }
        Object obj2 = parse(toJSONString(obj), parserConfig);
        return obj2 instanceof List ? new JSONArray((List) obj2) : obj2;
    }

    public static List<Object> parseArray(String str, Type[] typeArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONArray jSONArray = new JSONArray(typeArr.length);
        com.alibaba.fastjson2.JSONReader jSONReaderOf = com.alibaba.fastjson2.JSONReader.of(str, createReadContext(JSONFactory.getDefaultObjectReaderProvider(), DEFAULT_GENERATE_FEATURE, new Feature[0]));
        try {
            jSONReaderOf.startArray();
            for (Type type : typeArr) {
                jSONArray.add(jSONReaderOf.read(type));
            }
            jSONReaderOf.endArray();
            jSONReaderOf.handleResolveTasks(jSONArray);
            if (!jSONReaderOf.isEnd()) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONArray;
        } catch (Throwable th) {
            if (jSONReaderOf != null) {
                try {
                    jSONReaderOf.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static void addMixInAnnotations(Type type, Type type2) {
        if (type == null || type2 == null) {
            return;
        }
        Class cls = (Class) type;
        Class cls2 = (Class) type2;
        JSONFactory.getDefaultObjectReaderProvider().mixIn(cls, cls2);
        SerializeConfig.DEFAULT_PROVIDER.mixIn(cls, cls2);
    }

    public static void removeMixInAnnotations(Type type) {
        Class cls = (Class) type;
        JSONFactory.getDefaultObjectReaderProvider().mixIn(cls, null);
        JSONFactory.getDefaultObjectWriterProvider().mixIn(cls, null);
    }

    public static void clearMixInAnnotations() {
        JSONFactory.getDefaultObjectReaderProvider().cleanupMixIn();
        JSONFactory.getDefaultObjectWriterProvider().cleanupMixIn();
    }

    public static Type getMixInAnnotations(Type type) {
        Class cls = (Class) type;
        Class mixIn = JSONFactory.getDefaultObjectReaderProvider().getMixIn(cls);
        return mixIn == null ? JSONFactory.getDefaultObjectWriterProvider().getMixIn(cls) : mixIn;
    }

    static class Cache {
        volatile char[] chars;

        Cache() {
        }
    }

    @Override // com.alibaba.fastjson.JSONAware
    public String toJSONString() {
        return com.alibaba.fastjson2.JSON.toJSONString(this, JSONWriter.Feature.ReferenceDetection);
    }

    public String toString(SerializerFeature... serializerFeatureArr) {
        return toJSONString(this, serializerFeatureArr);
    }

    public void writeJSONString(Appendable appendable) {
        if (appendable instanceof Writer) {
            writeJSONString((Writer) appendable, this, new SerializerFeature[0]);
            return;
        }
        try {
            appendable.append(toJSONString(this));
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }
}
