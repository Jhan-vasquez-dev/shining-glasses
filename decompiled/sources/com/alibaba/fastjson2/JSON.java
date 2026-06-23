package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
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
import com.alibaba.fastjson2.modules.ObjectReaderModule;
import com.alibaba.fastjson2.modules.ObjectWriterModule;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import com.alibaba.fastjson2.reader.ObjectReaderNoneDefaultConstructor;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.MapMultiValueType;
import com.alibaba.fastjson2.util.MultiType;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public interface JSON {
    public static final String VERSION = "2.0.58";

    static Object parse(String str) {
        Object object;
        Object obj;
        if (str == null || str.isEmpty()) {
            return null;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        JSONReader.Context context = new JSONReader.Context(defaultObjectReaderProvider);
        JSONReader jSONReaderOf = JSONReader.of(str, context);
        try {
            char cCurrent = jSONReaderOf.current();
            if (context.objectSupplier == null && (context.features & JSONReader.Feature.UseNativeObject.mask) == 0 && (cCurrent == '{' || cCurrent == '[')) {
                if (cCurrent == '{') {
                    JSONObject jSONObject = new JSONObject();
                    jSONReaderOf.read(jSONObject, 0L);
                    obj = jSONObject;
                } else {
                    JSONArray jSONArray = new JSONArray();
                    jSONReaderOf.read((List) jSONArray);
                    obj = jSONArray;
                }
                object = obj;
                if (jSONReaderOf.resolveTasks != null) {
                    jSONReaderOf.handleResolveTasks(obj);
                    object = obj;
                }
            } else {
                object = defaultObjectReaderProvider.getObjectReader(Object.class, false).readObject(jSONReaderOf, null, null, 0L);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return object;
        } finally {
        }
    }

    static Object parse(String str, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        JSONReader.Context context = new JSONReader.Context(defaultObjectReaderProvider, featureArr);
        ObjectReader objectReader = defaultObjectReaderProvider.getObjectReader(Object.class, false);
        JSONReader jSONReaderOf = JSONReader.of(str, context);
        try {
            context.config(featureArr);
            Object object = objectReader.readObject(jSONReaderOf, null, null, 0L);
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return object;
        } finally {
        }
    }

    static Object parse(String str, int i, int i2, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty() || i2 == 0) {
            return null;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        JSONReader.Context context = new JSONReader.Context(defaultObjectReaderProvider, featureArr);
        ObjectReader objectReader = defaultObjectReaderProvider.getObjectReader(Object.class, false);
        JSONReader jSONReaderOf = JSONReader.of(str, i, i2, context);
        try {
            Object object = objectReader.readObject(jSONReaderOf, null, null, 0L);
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return object;
        } finally {
        }
    }

    static Object parse(String str, JSONReader.Context context) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ObjectReader objectReader = context.provider.getObjectReader(Object.class, false);
        JSONReader jSONReaderOf = JSONReader.of(str, context);
        try {
            Object object = objectReader.readObject(jSONReaderOf, null, null, 0L);
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return object;
        } finally {
        }
    }

    static Object parse(byte[] bArr, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        JSONReader.Context context = new JSONReader.Context(defaultObjectReaderProvider, featureArr);
        ObjectReader objectReader = defaultObjectReaderProvider.getObjectReader(Object.class, false);
        JSONReader jSONReaderOf = JSONReader.of(bArr, context);
        try {
            Object object = objectReader.readObject(jSONReaderOf, null, null, 0L);
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return object;
        } finally {
        }
    }

    static Object parse(byte[] bArr, JSONReader.Context context) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        ObjectReader objectReader = context.getObjectReader(Object.class);
        JSONReader jSONReaderOf = JSONReader.of(bArr, context);
        try {
            Object object = objectReader.readObject(jSONReaderOf, null, null, 0L);
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return object;
        } finally {
        }
    }

    static Object parse(byte[] bArr, int i, int i2, Charset charset, JSONReader.Context context) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        ObjectReader objectReader = context.getObjectReader(Object.class);
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, charset, context);
        try {
            Object object = objectReader.readObject(jSONReaderOf, null, null, 0L);
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return object;
        } finally {
        }
    }

    static Object parse(char[] cArr, JSONReader.Feature... featureArr) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        JSONReader.Context context = new JSONReader.Context(defaultObjectReaderProvider, featureArr);
        ObjectReader objectReader = defaultObjectReaderProvider.getObjectReader(Object.class, false);
        JSONReader jSONReaderOf = JSONReader.of(cArr, context);
        try {
            Object object = objectReader.readObject(jSONReaderOf, null, null, 0L);
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return object;
        } finally {
        }
    }

    static Object parse(char[] cArr, JSONReader.Context context) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        ObjectReader objectReader = context.getObjectReader(Object.class);
        JSONReader jSONReaderOf = JSONReader.of(cArr, context);
        try {
            Object object = objectReader.readObject(jSONReaderOf, null, null, 0L);
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return object;
        } finally {
        }
    }

    static Object parse(InputStream inputStream, JSONReader.Context context) {
        if (inputStream == null) {
            return null;
        }
        ObjectReader objectReader = context.getObjectReader(Object.class);
        JSONReaderUTF8 jSONReaderUTF8 = new JSONReaderUTF8(context, inputStream);
        try {
            Object object = objectReader.readObject(jSONReaderUTF8, null, null, 0L);
            if (jSONReaderUTF8.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderUTF8.info("input not end"));
            }
            jSONReaderUTF8.close();
            return object;
        } finally {
        }
    }

    static JSONObject parseObject(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(String str, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(String str, int i, int i2, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty() || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(str, i, i2, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(String str, int i, int i2, JSONReader.Context context) {
        if (str == null || str.isEmpty() || i2 == 0) {
            return null;
        }
        JSONReader jSONReaderOf = JSONReader.of(str, i, i2, context);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(String str, JSONReader.Context context) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader jSONReaderOf = JSONReader.of(str, context);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(Reader reader, JSONReader.Feature... featureArr) {
        if (reader == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(reader, contextCreateReadContext);
        try {
            if (jSONReaderOf.isEnd()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(InputStream inputStream, JSONReader.Feature... featureArr) {
        if (inputStream == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(inputStream, StandardCharsets.UTF_8, contextCreateReadContext);
        try {
            if (jSONReaderOf.isEnd()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(char[] cArr) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(cArr, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(InputStream inputStream, Charset charset) {
        if (inputStream == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(inputStream, charset, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(InputStream inputStream, Charset charset, JSONReader.Context context) {
        if (inputStream == null) {
            return null;
        }
        JSONReader jSONReaderOf = JSONReader.of(inputStream, charset, context);
        try {
            if (jSONReaderOf.isEnd()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(URL url) {
        if (url == null) {
            return null;
        }
        try {
            InputStream inputStreamOpenStream = url.openStream();
            try {
                JSONObject object = parseObject(inputStreamOpenStream, StandardCharsets.UTF_8);
                if (inputStreamOpenStream != null) {
                    inputStreamOpenStream.close();
                }
                return object;
            } finally {
            }
        } catch (IOException e) {
            throw new JSONException("JSON#parseObject cannot parse '" + url + "'", e);
        }
    }

    static JSONObject parseObject(byte[] bArr, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(byte[] bArr, int i, int i2, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0 || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(char[] cArr, int i, int i2, JSONReader.Feature... featureArr) {
        if (cArr == null || cArr.length == 0 || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(cArr, i, i2, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static JSONObject parseObject(byte[] bArr, int i, int i2, Charset charset, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0 || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, charset, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONReaderOf.read(jSONObject, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONObject);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return jSONObject;
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

    static <T> T parseObject(String str, Class<T> cls) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        JSONReader.Context context = new JSONReader.Context(defaultObjectReaderProvider);
        ObjectReader objectReader = defaultObjectReaderProvider.getObjectReader(cls, (JSONFactory.defaultReaderFeatures & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(str, context);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Class<T> cls, Filter filter, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(filter, featureArr);
        ObjectReader objectReader = contextCreateReadContext.provider.getObjectReader(cls, (contextCreateReadContext.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Type type, String str2, Filter[] filterArr, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context context = new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider(), null, filterArr, featureArr);
        context.setDateFormat(str2);
        ObjectReader objectReader = context.provider.getObjectReader(type, (context.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(str, context);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Type type) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        JSONReader.Context context = new JSONReader.Context(defaultObjectReaderProvider);
        ObjectReader objectReader = defaultObjectReaderProvider.getObjectReader(type, (JSONFactory.defaultReaderFeatures & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(str, context);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Type type, JSONReader.Context context) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ObjectReader objectReader = context.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(str, context);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T extends Map<String, Object>> T parseObject(String str, MapMultiValueType<T> mapMultiValueType) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(mapMultiValueType);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, mapMultiValueType, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Type... typeArr) {
        return (T) parseObject(str, new MultiType(typeArr));
    }

    static <T> T parseObject(String str, TypeReference<T> typeReference, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        Type type = typeReference.getType();
        ObjectReader objectReader = contextCreateReadContext.provider.getObjectReader(type, (contextCreateReadContext.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, TypeReference<T> typeReference, Filter filter, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(filter, featureArr);
        Type type = typeReference.getType();
        ObjectReader objectReader = contextCreateReadContext.provider.getObjectReader(type, (contextCreateReadContext.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Class<T> cls, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.provider.getObjectReader(cls, (contextCreateReadContext.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, int i, int i2, Class<T> cls, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty() || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.provider.getObjectReader(cls, (contextCreateReadContext.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(str, i, i2, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Class<T> cls, JSONReader.Context context) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ObjectReader objectReader = context.provider.getObjectReader(cls, (context.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(str, context);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Class<T> cls, String str2, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        if (str2 != null && !str2.isEmpty()) {
            contextCreateReadContext.setDateFormat(str2);
        }
        ObjectReader objectReader = contextCreateReadContext.provider.getObjectReader(cls, (contextCreateReadContext.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Type type, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Type type, Filter filter, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(filter, featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(String str, Type type, String str2, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        if (str2 != null && !str2.isEmpty()) {
            contextCreateReadContext.setDateFormat(str2);
        }
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            T t = (T) contextCreateReadContext.getObjectReader(type).readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(char[] cArr, int i, int i2, Type type, JSONReader.Feature... featureArr) {
        if (cArr == null || cArr.length == 0 || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(cArr, i, i2, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(char[] cArr, Class<T> cls) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(cls);
        JSONReader jSONReaderOf = JSONReader.of(cArr, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, int i, int i2, Type type, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0 || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, Type type) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, Class<T> cls) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        JSONReader.Context context = new JSONReader.Context(defaultObjectReaderProvider);
        ObjectReader objectReader = defaultObjectReaderProvider.getObjectReader(cls, (JSONFactory.defaultReaderFeatures & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(bArr, context);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, Class<T> cls, Filter filter, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(filter, featureArr);
        ObjectReader objectReader = contextCreateReadContext.provider.getObjectReader(cls, (contextCreateReadContext.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, Class<T> cls, JSONReader.Context context) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        ObjectReader objectReader = context.provider.getObjectReader(cls, (context.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(bArr, context);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, Type type, String str, Filter[] filterArr, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context context = new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider(), null, filterArr, featureArr);
        context.setDateFormat(str);
        ObjectReader objectReader = context.provider.getObjectReader(type, (context.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(bArr, context);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, Class<T> cls, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(cls);
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, Type type, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(char[] cArr, Class<T> cls, JSONReader.Feature... featureArr) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(cls);
        JSONReader jSONReaderOf = JSONReader.of(cArr, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(char[] cArr, Type type, JSONReader.Feature... featureArr) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(cArr, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, Type type, Filter filter, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(filter, featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, Type type, String str, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        if (str != null && !str.isEmpty()) {
            contextCreateReadContext.setDateFormat(str);
        }
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(ByteBuffer byteBuffer, Class<T> cls) {
        if (byteBuffer == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(cls);
        JSONReader jSONReaderOf = JSONReader.of(byteBuffer, (Charset) null, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(Reader reader, Type type, JSONReader.Feature... featureArr) {
        if (reader == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(reader, contextCreateReadContext);
        try {
            if (jSONReaderOf.isEnd()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(InputStream inputStream, Type type, JSONReader.Feature... featureArr) {
        if (inputStream == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        contextCreateReadContext.config(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(inputStream, StandardCharsets.UTF_8, contextCreateReadContext);
        try {
            if (jSONReaderOf.isEnd()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(InputStream inputStream, Charset charset, Type type, JSONReader.Context context) {
        if (inputStream == null) {
            return null;
        }
        ObjectReader objectReader = context.provider.getObjectReader(type, (context.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(inputStream, charset, context);
        try {
            if (jSONReaderOf.isEnd()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(InputStream inputStream, Charset charset, Class<T> cls, JSONReader.Context context) {
        if (inputStream == null) {
            return null;
        }
        ObjectReader objectReader = context.provider.getObjectReader(cls, (context.features & JSONReader.Feature.FieldBased.mask) != 0);
        JSONReader jSONReaderOf = JSONReader.of(inputStream, charset, context);
        try {
            if (jSONReaderOf.isEnd()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(URL url, Type type, JSONReader.Feature... featureArr) {
        if (url == null) {
            return null;
        }
        try {
            InputStream inputStreamOpenStream = url.openStream();
            try {
                T t = (T) parseObject(inputStreamOpenStream, type, featureArr);
                if (inputStreamOpenStream != null) {
                    inputStreamOpenStream.close();
                }
                return t;
            } finally {
            }
        } catch (IOException e) {
            throw new JSONException("parseObject error", e);
        }
    }

    static <T> T parseObject(URL url, Class<T> cls, JSONReader.Feature... featureArr) {
        if (url == null) {
            return null;
        }
        try {
            InputStream inputStreamOpenStream = url.openStream();
            try {
                T t = (T) parseObject(inputStreamOpenStream, cls, featureArr);
                if (inputStreamOpenStream != null) {
                    inputStreamOpenStream.close();
                }
                return t;
            } finally {
            }
        } catch (IOException e) {
            throw new JSONException("JSON#parseObject cannot parse '" + url + "' to '" + cls + "'", e);
        }
    }

    static <T> T parseObject(URL url, Function<JSONObject, T> function, JSONReader.Feature... featureArr) {
        if (url == null) {
            return null;
        }
        try {
            InputStream inputStreamOpenStream = url.openStream();
            try {
                JSONObject object = parseObject(inputStreamOpenStream, featureArr);
                if (object == null) {
                    if (inputStreamOpenStream != null) {
                        inputStreamOpenStream.close();
                    }
                    return null;
                }
                T tApply = function.apply(object);
                if (inputStreamOpenStream != null) {
                    inputStreamOpenStream.close();
                }
                return tApply;
            } finally {
            }
        } catch (IOException e) {
            throw new JSONException("JSON#parseObject cannot parse '" + url + "'", e);
        }
        throw new JSONException("JSON#parseObject cannot parse '" + url + "'", e);
    }

    static <T> T parseObject(InputStream inputStream, Type type, String str, JSONReader.Feature... featureArr) {
        if (inputStream == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        if (str != null && !str.isEmpty()) {
            contextCreateReadContext.setDateFormat(str);
        }
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(inputStream, StandardCharsets.UTF_8, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(InputStream inputStream, Charset charset, Type type, JSONReader.Feature... featureArr) {
        if (inputStream == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(inputStream, charset, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, int i, int i2, Charset charset, Type type) {
        if (bArr == null || bArr.length == 0 || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(type);
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, charset, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, type, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, int i, int i2, Charset charset, Class<T> cls) {
        if (bArr == null || bArr.length == 0 || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(cls);
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, charset, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> T parseObject(byte[] bArr, int i, int i2, Charset charset, Class<T> cls, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0 || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        ObjectReader objectReader = contextCreateReadContext.getObjectReader(cls);
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, charset, contextCreateReadContext);
        try {
            T t = (T) objectReader.readObject(jSONReaderOf, cls, null, 0L);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(t);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return t;
        } finally {
        }
    }

    static <T> void parseObject(InputStream inputStream, Type type, Consumer<T> consumer, JSONReader.Feature... featureArr) {
        parseObject(inputStream, StandardCharsets.UTF_8, '\n', type, (Consumer) consumer, featureArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <T> void parseObject(InputStream inputStream, Charset charset, char c, Type type, Consumer<T> consumer, JSONReader.Feature... featureArr) {
        int i;
        JSONReader.Context context;
        JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & (JSONFactory.CACHE_ITEMS.length - 1)];
        ObjectReader objectReader = null;
        byte[] andSet = JSONFactory.BYTES_UPDATER.getAndSet(cacheItem, null);
        int i2 = 524288;
        if (andSet == null) {
            andSet = new byte[524288];
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        byte[] bArrCopyOf = andSet;
        int length = 0;
        int i3 = 0;
        while (true) {
            try {
                try {
                    int i4 = inputStream.read(bArrCopyOf, length, bArrCopyOf.length - length);
                    if (i4 == -1) {
                        return;
                    }
                    int i5 = length + i4;
                    int i6 = length;
                    boolean z = false;
                    while (i6 < i5) {
                        if (bArrCopyOf[i6] == c) {
                            JSONReader jSONReaderOf = JSONReader.of(bArrCopyOf, i3, i6 - i3, charset, contextCreateReadContext);
                            if (objectReader == null) {
                                objectReader = contextCreateReadContext.getObjectReader(type);
                            }
                            ObjectReader objectReader2 = objectReader;
                            Object object = objectReader2.readObject(jSONReaderOf, type, null, 0L);
                            if (jSONReaderOf.resolveTasks != null) {
                                jSONReaderOf.handleResolveTasks(object);
                            }
                            if (jSONReaderOf.ch != 26) {
                                i = i2;
                                context = contextCreateReadContext;
                                if ((contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                                    throw new JSONException(jSONReaderOf.info("input not end"));
                                }
                            } else {
                                i = i2;
                                context = contextCreateReadContext;
                            }
                            consumer.accept(object);
                            i3 = i6 + 1;
                            objectReader = objectReader2;
                            z = true;
                        } else {
                            i = i2;
                            context = contextCreateReadContext;
                        }
                        i6++;
                        contextCreateReadContext = context;
                        i2 = i;
                    }
                    int i7 = i2;
                    JSONReader.Context context2 = contextCreateReadContext;
                    if (i5 != bArrCopyOf.length) {
                        length = i5;
                    } else if (z) {
                        length = bArrCopyOf.length - i3;
                        System.arraycopy(bArrCopyOf, i3, bArrCopyOf, 0, length);
                        i3 = 0;
                    } else {
                        bArrCopyOf = Arrays.copyOf(bArrCopyOf, bArrCopyOf.length + i7);
                        length = i5;
                    }
                    contextCreateReadContext = context2;
                    i2 = i7;
                } catch (IOException e) {
                    throw new JSONException("JSON#parseObject cannot parse the 'InputStream' to '" + type + "'", e);
                }
            } finally {
                JSONFactory.BYTES_UPDATER.lazySet(cacheItem, bArrCopyOf);
            }
            JSONFactory.BYTES_UPDATER.lazySet(cacheItem, bArrCopyOf);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <T> void parseObject(Reader reader, char c, Type type, Consumer<T> consumer) {
        JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & (JSONFactory.CACHE_ITEMS.length - 1)];
        ObjectReader objectReader = null;
        char[] andSet = JSONFactory.CHARS_UPDATER.getAndSet(cacheItem, null);
        if (andSet == null) {
            andSet = new char[8192];
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        char[] cArrCopyOf = andSet;
        int length = 0;
        int i = 0;
        while (true) {
            try {
                try {
                    int i2 = reader.read(cArrCopyOf, length, cArrCopyOf.length - length);
                    if (i2 == -1) {
                        return;
                    }
                    int i3 = length + i2;
                    boolean z = false;
                    for (int i4 = length; i4 < i3; i4++) {
                        if (cArrCopyOf[i4] == c) {
                            JSONReader jSONReaderOf = JSONReader.of(cArrCopyOf, i, i4 - i, contextCreateReadContext);
                            if (objectReader == null) {
                                objectReader = contextCreateReadContext.getObjectReader(type);
                            }
                            ObjectReader objectReader2 = objectReader;
                            consumer.accept(objectReader2.readObject(jSONReaderOf, type, null, 0L));
                            i = i4 + 1;
                            objectReader = objectReader2;
                            z = true;
                        }
                    }
                    if (i3 == cArrCopyOf.length) {
                        if (z) {
                            length = cArrCopyOf.length - i;
                            System.arraycopy(cArrCopyOf, i, cArrCopyOf, 0, length);
                            i = 0;
                        } else {
                            cArrCopyOf = Arrays.copyOf(cArrCopyOf, cArrCopyOf.length + 8192);
                        }
                    }
                    length = i3;
                } catch (IOException e) {
                    throw new JSONException("JSON#parseObject cannot parse the 'Reader' to '" + type + "'", e);
                }
            } finally {
                JSONFactory.CHARS_UPDATER.lazySet(cacheItem, cArrCopyOf);
            }
        }
    }

    static JSONArray parseArray(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            jSONReaderOf.read((List) jSONArray);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONArray);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
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

    static JSONArray parseArray(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            jSONReaderOf.read((List) jSONArray);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONArray);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
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

    static JSONArray parseArray(byte[] bArr, int i, int i2, Charset charset) {
        if (bArr == null || bArr.length == 0 || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, charset, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            jSONReaderOf.read((List) jSONArray);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONArray);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
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

    static JSONArray parseArray(char[] cArr) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(cArr, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            jSONReaderOf.read((List) jSONArray);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONArray);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
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

    static JSONArray parseArray(String str, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            jSONReaderOf.read((List) jSONArray);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONArray);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
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

    static JSONArray parseArray(URL url, JSONReader.Feature... featureArr) {
        if (url == null) {
            return null;
        }
        try {
            InputStream inputStreamOpenStream = url.openStream();
            try {
                JSONArray array = parseArray(inputStreamOpenStream, featureArr);
                if (inputStreamOpenStream != null) {
                    inputStreamOpenStream.close();
                }
                return array;
            } finally {
            }
        } catch (IOException e) {
            throw new JSONException("JSON#parseArray cannot parse '" + url + "' to '" + JSONArray.class + "'", e);
        }
    }

    static JSONArray parseArray(Reader reader, JSONReader.Feature... featureArr) {
        if (reader == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(reader, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            jSONReaderOf.read((List) jSONArray);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONArray);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
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

    static JSONArray parseArray(InputStream inputStream, JSONReader.Feature... featureArr) {
        if (inputStream == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(inputStream, StandardCharsets.UTF_8, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            jSONReaderOf.read((List) jSONArray);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONArray);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
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

    static JSONArray parseArray(InputStream inputStream, Charset charset, JSONReader.Context context) {
        if (inputStream == null) {
            return null;
        }
        JSONReader jSONReaderOf = JSONReader.of(inputStream, charset, context);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            jSONReaderOf.read((List) jSONArray);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(jSONArray);
            }
            if (jSONReaderOf.ch != 26 && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
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

    static <T> List<T> parseArray(String str, Type type, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            List<T> array = jSONReaderOf.readArray(type);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(array);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return array;
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

    static <T> List<T> parseArray(String str, Type type) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            List<T> array = jSONReaderOf.readArray(type);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(array);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return array;
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

    static <T> List<T> parseArray(String str, Class<T> cls) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            List<T> array = jSONReaderOf.readArray(cls);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(array);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return array;
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

    static <T> List<T> parseArray(String str, Type... typeArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext();
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            List<T> list = jSONReaderOf.readList(typeArr);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(list);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return list;
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

    static <T> List<T> parseArray(String str, Class<T> cls, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            List<T> array = jSONReaderOf.readArray(cls);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(array);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return array;
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

    static <T> List<T> parseArray(char[] cArr, Class<T> cls, JSONReader.Feature... featureArr) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(cArr, contextCreateReadContext);
        try {
            List<T> array = jSONReaderOf.readArray(cls);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(array);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return array;
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

    static <T> List<T> parseArray(String str, Type[] typeArr, JSONReader.Feature... featureArr) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(str, contextCreateReadContext);
        try {
            if (jSONReaderOf.nextIfNull()) {
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return null;
            }
            jSONReaderOf.startArray();
            ArrayList arrayList = new ArrayList(typeArr.length);
            for (Type type : typeArr) {
                arrayList.add(jSONReaderOf.read(type));
            }
            jSONReaderOf.endArray();
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(arrayList);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return arrayList;
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

    static <T> List<T> parseArray(Reader reader, Type type, JSONReader.Feature... featureArr) {
        if (reader == null) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(reader, contextCreateReadContext);
        try {
            List<T> array = jSONReaderOf.readArray(type);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(array);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return array;
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

    static <T> List<T> parseArray(byte[] bArr, Type type, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            List<T> array = jSONReaderOf.readArray(type);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(array);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return array;
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

    static <T> List<T> parseArray(byte[] bArr, Class<T> cls, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(bArr, contextCreateReadContext);
        try {
            List<T> array = jSONReaderOf.readArray(cls);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(array);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return array;
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

    static <T> List<T> parseArray(byte[] bArr, int i, int i2, Charset charset, Class<T> cls, JSONReader.Feature... featureArr) {
        if (bArr == null || bArr.length == 0 || i2 == 0) {
            return null;
        }
        JSONReader.Context contextCreateReadContext = JSONFactory.createReadContext(featureArr);
        JSONReader jSONReaderOf = JSONReader.of(bArr, i, i2, charset, contextCreateReadContext);
        try {
            List<T> array = jSONReaderOf.readArray(cls);
            if (jSONReaderOf.resolveTasks != null) {
                jSONReaderOf.handleResolveTasks(array);
            }
            if (jSONReaderOf.ch != 26 && (contextCreateReadContext.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0) {
                throw new JSONException(jSONReaderOf.info("input not end"));
            }
            if (jSONReaderOf != null) {
                jSONReaderOf.close();
            }
            return array;
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

    /* JADX WARN: Removed duplicated region for block: B:21:0x004f A[Catch: NumberFormatException -> 0x0063, NullPointerException -> 0x0065, TRY_ENTER, TRY_LEAVE, TryCatch #6 {NullPointerException -> 0x0065, NumberFormatException -> 0x0063, blocks: (B:21:0x004f, B:33:0x0062, B:32:0x005f, B:29:0x005a), top: B:46:0x000b, inners: #6 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static java.lang.String toJSONString(java.lang.Object r11) throws java.lang.Throwable {
        /*
            com.alibaba.fastjson2.writer.ObjectWriterProvider r0 = com.alibaba.fastjson2.JSONFactory.defaultObjectWriterProvider
            com.alibaba.fastjson2.JSONWriter$Context r1 = new com.alibaba.fastjson2.JSONWriter$Context
            r1.<init>(r0)
            com.alibaba.fastjson2.JSONWriter r3 = com.alibaba.fastjson2.JSONWriter.of(r1)     // Catch: java.lang.NumberFormatException -> L67 java.lang.NullPointerException -> L69
            if (r11 != 0) goto L12
            r3.writeNull()     // Catch: java.lang.Throwable -> L55
        L10:
            r4 = r11
            goto L49
        L12:
            r3.rootObject = r11     // Catch: java.lang.Throwable -> L55
            com.alibaba.fastjson2.JSONWriter$Path r2 = com.alibaba.fastjson2.JSONWriter.Path.ROOT     // Catch: java.lang.Throwable -> L55
            r3.path = r2     // Catch: java.lang.Throwable -> L55
            java.lang.Class r2 = r11.getClass()     // Catch: java.lang.Throwable -> L55
            java.lang.Class<com.alibaba.fastjson2.JSONObject> r4 = com.alibaba.fastjson2.JSONObject.class
            r5 = 0
            if (r2 != r4) goto L2f
            long r7 = r1.features     // Catch: java.lang.Throwable -> L55
            int r1 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r1 != 0) goto L2f
            r0 = r11
            com.alibaba.fastjson2.JSONObject r0 = (com.alibaba.fastjson2.JSONObject) r0     // Catch: java.lang.Throwable -> L55
            r3.write(r0)     // Catch: java.lang.Throwable -> L55
            goto L10
        L2f:
            long r7 = com.alibaba.fastjson2.JSONFactory.defaultWriterFeatures     // Catch: java.lang.Throwable -> L55
            com.alibaba.fastjson2.JSONWriter$Feature r1 = com.alibaba.fastjson2.JSONWriter.Feature.FieldBased     // Catch: java.lang.Throwable -> L55
            long r9 = r1.mask     // Catch: java.lang.Throwable -> L55
            long r7 = r7 & r9
            int r1 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r1 == 0) goto L3c
            r1 = 1
            goto L3d
        L3c:
            r1 = 0
        L3d:
            com.alibaba.fastjson2.writer.ObjectWriter r2 = r0.getObjectWriter(r2, r2, r1)     // Catch: java.lang.Throwable -> L55
            r6 = 0
            r7 = 0
            r5 = 0
            r4 = r11
            r2.write(r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L53
        L49:
            java.lang.String r11 = r3.toString()     // Catch: java.lang.Throwable -> L53
            if (r3 == 0) goto L52
            r3.close()     // Catch: java.lang.NumberFormatException -> L63 java.lang.NullPointerException -> L65
        L52:
            return r11
        L53:
            r0 = move-exception
            goto L57
        L55:
            r0 = move-exception
            r4 = r11
        L57:
            r11 = r0
            if (r3 == 0) goto L62
            r3.close()     // Catch: java.lang.Throwable -> L5e
            goto L62
        L5e:
            r0 = move-exception
            r11.addSuppressed(r0)     // Catch: java.lang.NumberFormatException -> L63 java.lang.NullPointerException -> L65
        L62:
            throw r11     // Catch: java.lang.NumberFormatException -> L63 java.lang.NullPointerException -> L65
        L63:
            r0 = move-exception
            goto L6b
        L65:
            r0 = move-exception
            goto L6b
        L67:
            r0 = move-exception
            goto L6a
        L69:
            r0 = move-exception
        L6a:
            r4 = r11
        L6b:
            r11 = r0
            com.alibaba.fastjson2.JSONException r0 = new com.alibaba.fastjson2.JSONException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "JSON#toJSONString cannot serialize '"
            r1.<init>(r2)
            java.lang.StringBuilder r1 = r1.append(r4)
            java.lang.String r2 = "'"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1, r11)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.toJSONString(java.lang.Object):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x003c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:? A[Catch: NumberFormatException -> 0x0046, NullPointerException -> 0x0048, SYNTHETIC, TRY_LEAVE, TryCatch #8 {NullPointerException -> 0x0048, NumberFormatException -> 0x0046, blocks: (B:15:0x0031, B:28:0x0045, B:27:0x0042, B:23:0x003c), top: B:47:0x000a, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static java.lang.String toJSONString(java.lang.Object r7, com.alibaba.fastjson2.JSONWriter.Context r8) throws java.lang.Throwable {
        /*
            if (r8 != 0) goto L6
            com.alibaba.fastjson2.JSONWriter$Context r8 = com.alibaba.fastjson2.JSONFactory.createWriteContext()
        L6:
            com.alibaba.fastjson2.JSONWriter r1 = com.alibaba.fastjson2.JSONWriter.of(r8)     // Catch: java.lang.NumberFormatException -> L4a java.lang.NullPointerException -> L4c
            if (r7 != 0) goto L15
            r1.writeNull()     // Catch: java.lang.Throwable -> L11
            r2 = r7
            goto L2b
        L11:
            r0 = move-exception
            r8 = r0
            r2 = r7
            goto L3a
        L15:
            r1.rootObject = r7     // Catch: java.lang.Throwable -> L37
            com.alibaba.fastjson2.JSONWriter$Path r0 = com.alibaba.fastjson2.JSONWriter.Path.ROOT     // Catch: java.lang.Throwable -> L37
            r1.path = r0     // Catch: java.lang.Throwable -> L37
            java.lang.Class r0 = r7.getClass()     // Catch: java.lang.Throwable -> L37
            com.alibaba.fastjson2.writer.ObjectWriter r0 = r8.getObjectWriter(r0, r0)     // Catch: java.lang.Throwable -> L37
            r4 = 0
            r5 = 0
            r3 = 0
            r2 = r7
            r0.write(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L35
        L2b:
            java.lang.String r7 = r1.toString()     // Catch: java.lang.Throwable -> L35
            if (r1 == 0) goto L34
            r1.close()     // Catch: java.lang.NumberFormatException -> L46 java.lang.NullPointerException -> L48
        L34:
            return r7
        L35:
            r0 = move-exception
            goto L39
        L37:
            r0 = move-exception
            r2 = r7
        L39:
            r8 = r0
        L3a:
            if (r1 == 0) goto L45
            r1.close()     // Catch: java.lang.Throwable -> L40
            goto L45
        L40:
            r0 = move-exception
            r7 = r0
            r8.addSuppressed(r7)     // Catch: java.lang.NumberFormatException -> L46 java.lang.NullPointerException -> L48
        L45:
            throw r8     // Catch: java.lang.NumberFormatException -> L46 java.lang.NullPointerException -> L48
        L46:
            r0 = move-exception
            goto L4e
        L48:
            r0 = move-exception
            goto L4e
        L4a:
            r0 = move-exception
            goto L4d
        L4c:
            r0 = move-exception
        L4d:
            r2 = r7
        L4e:
            r7 = r0
            com.alibaba.fastjson2.JSONException r8 = new com.alibaba.fastjson2.JSONException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "JSON#toJSONString cannot serialize '"
            r0.<init>(r1)
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.String r1 = "'"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r8.<init>(r0, r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.toJSONString(java.lang.Object, com.alibaba.fastjson2.JSONWriter$Context):java.lang.String");
    }

    static String toJSONString(Object obj, JSONWriter.Feature... featureArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, featureArr);
        JSONWriter jSONWriterOf = JSONWriter.of(context);
        try {
            if (obj == null) {
                jSONWriterOf.writeNull();
            } else {
                jSONWriterOf.rootObject = obj;
                jSONWriterOf.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                context.provider.getObjectWriter(cls, cls, (context.features & JSONWriter.Feature.FieldBased.mask) != 0).write(jSONWriterOf, obj, null, null, 0L);
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

    static String toJSONString(Object obj, Filter filter, JSONWriter.Feature... featureArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, featureArr);
        JSONWriter jSONWriterOf = JSONWriter.of(context);
        try {
            if (obj == null) {
                jSONWriterOf.writeNull();
            } else {
                jSONWriterOf.rootObject = obj;
                jSONWriterOf.path = JSONWriter.Path.ROOT;
                if (filter != null) {
                    jSONWriterOf.context.configFilter(filter);
                }
                Class<?> cls = obj.getClass();
                context.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
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

    static String toJSONString(Object obj, Filter[] filterArr, JSONWriter.Feature... featureArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, featureArr);
        if (filterArr != null && filterArr.length != 0) {
            context.configFilter(filterArr);
        }
        JSONWriter jSONWriterOf = JSONWriter.of(context);
        try {
            if (obj == null) {
                jSONWriterOf.writeNull();
            } else {
                jSONWriterOf.rootObject = obj;
                jSONWriterOf.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                context.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
            }
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } finally {
        }
    }

    static String toJSONString(Object obj, String str, JSONWriter.Feature... featureArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, featureArr);
        if (str != null && !str.isEmpty()) {
            context.setDateFormat(str);
        }
        JSONWriter jSONWriterOf = JSONWriter.of(context);
        try {
            if (obj == null) {
                jSONWriterOf.writeNull();
            } else {
                jSONWriterOf.rootObject = obj;
                jSONWriterOf.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                context.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
            }
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } finally {
        }
    }

    static String toJSONString(Object obj, String str, Filter[] filterArr, JSONWriter.Feature... featureArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, featureArr);
        if (str != null && !str.isEmpty()) {
            context.setDateFormat(str);
        }
        if (filterArr != null && filterArr.length != 0) {
            context.configFilter(filterArr);
        }
        JSONWriter jSONWriterOf = JSONWriter.of(context);
        try {
            if (obj == null) {
                jSONWriterOf.writeNull();
            } else {
                jSONWriterOf.rootObject = obj;
                jSONWriterOf.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                context.getObjectWriter(cls, cls).write(jSONWriterOf, obj, null, null, 0L);
            }
            String string = jSONWriterOf.toString();
            if (jSONWriterOf != null) {
                jSONWriterOf.close();
            }
            return string;
        } finally {
        }
    }

    static byte[] toJSONBytes(Object obj) {
        ObjectWriterProvider objectWriterProvider = JSONFactory.defaultObjectWriterProvider;
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(new JSONWriter.Context(objectWriterProvider));
        try {
            if (obj == null) {
                jSONWriterOfUTF8.writeNull();
            } else {
                jSONWriterOfUTF8.rootObject = obj;
                jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                if (cls == JSONObject.class && jSONWriterOfUTF8.context.features == 0) {
                    jSONWriterOfUTF8.write((JSONObject) obj);
                } else {
                    objectWriterProvider.getObjectWriter(cls, cls, (JSONFactory.defaultWriterFeatures & JSONWriter.Feature.FieldBased.mask) != 0).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
            }
            byte[] bytes = jSONWriterOfUTF8.getBytes();
            if (jSONWriterOfUTF8 != null) {
                jSONWriterOfUTF8.close();
            }
            return bytes;
        } catch (Throwable th) {
            if (jSONWriterOfUTF8 == null) {
                throw th;
            }
            try {
                jSONWriterOfUTF8.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    static byte[] toJSONBytes(Object obj, Charset charset, JSONWriter.Feature... featureArr) {
        ObjectWriterProvider objectWriterProvider = JSONFactory.defaultObjectWriterProvider;
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(new JSONWriter.Context(objectWriterProvider, featureArr));
        try {
            if (obj == null) {
                jSONWriterOfUTF8.writeNull();
            } else {
                jSONWriterOfUTF8.rootObject = obj;
                jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                if (cls == JSONObject.class && jSONWriterOfUTF8.context.features == 0) {
                    jSONWriterOfUTF8.write((JSONObject) obj);
                } else {
                    objectWriterProvider.getObjectWriter(cls, cls, (JSONFactory.defaultWriterFeatures & JSONWriter.Feature.FieldBased.mask) != 0).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
            }
            byte[] bytes = jSONWriterOfUTF8.getBytes(charset);
            if (jSONWriterOfUTF8 != null) {
                jSONWriterOfUTF8.close();
            }
            return bytes;
        } catch (Throwable th) {
            if (jSONWriterOfUTF8 == null) {
                throw th;
            }
            try {
                jSONWriterOfUTF8.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    static byte[] toJSONBytes(Object obj, Charset charset, JSONWriter.Context context) {
        ObjectWriterProvider objectWriterProvider = context.provider;
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(context);
        try {
            if (obj == null) {
                jSONWriterOfUTF8.writeNull();
            } else {
                jSONWriterOfUTF8.rootObject = obj;
                jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                if (cls == JSONObject.class && jSONWriterOfUTF8.context.features == 0) {
                    jSONWriterOfUTF8.write((JSONObject) obj);
                } else {
                    objectWriterProvider.getObjectWriter(cls, cls, (JSONFactory.defaultWriterFeatures & JSONWriter.Feature.FieldBased.mask) != 0).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
            }
            byte[] bytes = jSONWriterOfUTF8.getBytes(charset);
            if (jSONWriterOfUTF8 != null) {
                jSONWriterOfUTF8.close();
            }
            return bytes;
        } catch (Throwable th) {
            if (jSONWriterOfUTF8 == null) {
                throw th;
            }
            try {
                jSONWriterOfUTF8.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    static byte[] toJSONBytes(Object obj, String str, JSONWriter.Feature... featureArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, featureArr);
        if (str != null && !str.isEmpty()) {
            context.setDateFormat(str);
        }
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(context);
        try {
            if (obj == null) {
                jSONWriterOfUTF8.writeNull();
            } else {
                jSONWriterOfUTF8.rootObject = obj;
                jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                context.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
            }
            byte[] bytes = jSONWriterOfUTF8.getBytes();
            if (jSONWriterOfUTF8 != null) {
                jSONWriterOfUTF8.close();
            }
            return bytes;
        } finally {
        }
    }

    static byte[] toJSONBytes(Object obj, Filter... filterArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider);
        if (filterArr != null && filterArr.length != 0) {
            context.configFilter(filterArr);
        }
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(context);
        try {
            if (obj == null) {
                jSONWriterOfUTF8.writeNull();
            } else {
                jSONWriterOfUTF8.rootObject = obj;
                jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                context.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
            }
            byte[] bytes = jSONWriterOfUTF8.getBytes();
            if (jSONWriterOfUTF8 != null) {
                jSONWriterOfUTF8.close();
            }
            return bytes;
        } finally {
        }
    }

    static byte[] toJSONBytes(Object obj, JSONWriter.Feature... featureArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, featureArr);
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(context);
        try {
            if (obj == null) {
                jSONWriterOfUTF8.writeNull();
            } else {
                jSONWriterOfUTF8.rootObject = obj;
                jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                context.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
            }
            byte[] bytes = jSONWriterOfUTF8.getBytes();
            if (jSONWriterOfUTF8 != null) {
                jSONWriterOfUTF8.close();
            }
            return bytes;
        } catch (Throwable th) {
            if (jSONWriterOfUTF8 == null) {
                throw th;
            }
            try {
                jSONWriterOfUTF8.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    static byte[] toJSONBytes(Object obj, Filter[] filterArr, JSONWriter.Feature... featureArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, featureArr);
        if (filterArr != null && filterArr.length != 0) {
            context.configFilter(filterArr);
        }
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(context);
        try {
            if (obj == null) {
                jSONWriterOfUTF8.writeNull();
            } else {
                jSONWriterOfUTF8.rootObject = obj;
                jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                context.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
            }
            byte[] bytes = jSONWriterOfUTF8.getBytes();
            if (jSONWriterOfUTF8 != null) {
                jSONWriterOfUTF8.close();
            }
            return bytes;
        } finally {
        }
    }

    static byte[] toJSONBytes(Object obj, String str, Filter[] filterArr, JSONWriter.Feature... featureArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, featureArr);
        if (str != null && !str.isEmpty()) {
            context.setDateFormat(str);
        }
        if (filterArr != null && filterArr.length != 0) {
            context.configFilter(filterArr);
        }
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(context);
        try {
            if (obj == null) {
                jSONWriterOfUTF8.writeNull();
            } else {
                jSONWriterOfUTF8.rootObject = obj;
                jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                Class<?> cls = obj.getClass();
                context.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
            }
            byte[] bytes = jSONWriterOfUTF8.getBytes();
            if (jSONWriterOfUTF8 != null) {
                jSONWriterOfUTF8.close();
            }
            return bytes;
        } finally {
        }
    }

    static int writeTo(OutputStream outputStream, Object obj) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider);
        try {
            JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(context);
            try {
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.rootObject = obj;
                    jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                    Class<?> cls = obj.getClass();
                    context.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                int iFlushTo = jSONWriterOfUTF8.flushTo(outputStream);
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return iFlushTo;
            } finally {
            }
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    static int writeTo(OutputStream outputStream, Object obj, JSONWriter.Context context) {
        try {
            JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(context);
            try {
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.rootObject = obj;
                    jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                    Class<?> cls = obj.getClass();
                    context.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                int iFlushTo = jSONWriterOfUTF8.flushTo(outputStream);
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return iFlushTo;
            } finally {
            }
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    static int writeTo(OutputStream outputStream, Object obj, JSONWriter.Feature... featureArr) {
        JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, featureArr);
        try {
            JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(context);
            try {
                if (obj == null) {
                    jSONWriterOfUTF8.writeNull();
                } else {
                    jSONWriterOfUTF8.rootObject = obj;
                    jSONWriterOfUTF8.path = JSONWriter.Path.ROOT;
                    Class<?> cls = obj.getClass();
                    context.getObjectWriter(cls, cls).write(jSONWriterOfUTF8, obj, null, null, 0L);
                }
                int iFlushTo = jSONWriterOfUTF8.flushTo(outputStream);
                if (jSONWriterOfUTF8 != null) {
                    jSONWriterOfUTF8.close();
                }
                return iFlushTo;
            } finally {
            }
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x0045 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:49:? A[Catch: Exception -> 0x004f, SYNTHETIC, TRY_LEAVE, TryCatch #1 {Exception -> 0x004f, blocks: (B:18:0x003a, B:31:0x004e, B:30:0x004b, B:26:0x0045), top: B:40:0x0013, inners: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static int writeTo(java.io.OutputStream r8, java.lang.Object r9, com.alibaba.fastjson2.filter.Filter[] r10, com.alibaba.fastjson2.JSONWriter.Feature... r11) throws java.lang.Throwable {
        /*
            com.alibaba.fastjson2.JSONWriter$Context r0 = new com.alibaba.fastjson2.JSONWriter$Context
            com.alibaba.fastjson2.writer.ObjectWriterProvider r1 = com.alibaba.fastjson2.JSONFactory.defaultObjectWriterProvider
            r0.<init>(r1, r11)
            if (r10 == 0) goto Lf
            int r11 = r10.length
            if (r11 == 0) goto Lf
            r0.configFilter(r10)
        Lf:
            com.alibaba.fastjson2.JSONWriter r2 = com.alibaba.fastjson2.JSONWriter.ofUTF8(r0)     // Catch: java.lang.Exception -> L51
            if (r9 != 0) goto L1e
            r2.writeNull()     // Catch: java.lang.Throwable -> L1a
            r3 = r9
            goto L34
        L1a:
            r0 = move-exception
            r8 = r0
            r3 = r9
            goto L43
        L1e:
            r2.rootObject = r9     // Catch: java.lang.Throwable -> L40
            com.alibaba.fastjson2.JSONWriter$Path r10 = com.alibaba.fastjson2.JSONWriter.Path.ROOT     // Catch: java.lang.Throwable -> L40
            r2.path = r10     // Catch: java.lang.Throwable -> L40
            java.lang.Class r10 = r9.getClass()     // Catch: java.lang.Throwable -> L40
            com.alibaba.fastjson2.writer.ObjectWriter r1 = r0.getObjectWriter(r10, r10)     // Catch: java.lang.Throwable -> L40
            r5 = 0
            r6 = 0
            r4 = 0
            r3 = r9
            r1.write(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L3e
        L34:
            int r8 = r2.flushTo(r8)     // Catch: java.lang.Throwable -> L3e
            if (r2 == 0) goto L3d
            r2.close()     // Catch: java.lang.Exception -> L4f
        L3d:
            return r8
        L3e:
            r0 = move-exception
            goto L42
        L40:
            r0 = move-exception
            r3 = r9
        L42:
            r8 = r0
        L43:
            if (r2 == 0) goto L4e
            r2.close()     // Catch: java.lang.Throwable -> L49
            goto L4e
        L49:
            r0 = move-exception
            r9 = r0
            r8.addSuppressed(r9)     // Catch: java.lang.Exception -> L4f
        L4e:
            throw r8     // Catch: java.lang.Exception -> L4f
        L4f:
            r0 = move-exception
            goto L53
        L51:
            r0 = move-exception
            r3 = r9
        L53:
            r8 = r0
            com.alibaba.fastjson2.JSONException r9 = new com.alibaba.fastjson2.JSONException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r11 = "JSON#writeTo cannot serialize '"
            r10.<init>(r11)
            java.lang.StringBuilder r10 = r10.append(r3)
            java.lang.String r11 = "' to 'OutputStream'"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10, r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.writeTo(java.io.OutputStream, java.lang.Object, com.alibaba.fastjson2.filter.Filter[], com.alibaba.fastjson2.JSONWriter$Feature[]):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x0050 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:? A[Catch: Exception -> 0x005a, SYNTHETIC, TRY_LEAVE, TryCatch #3 {Exception -> 0x005a, blocks: (B:22:0x0045, B:35:0x0059, B:34:0x0056, B:30:0x0050), top: B:48:0x001e, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static int writeTo(java.io.OutputStream r8, java.lang.Object r9, java.lang.String r10, com.alibaba.fastjson2.filter.Filter[] r11, com.alibaba.fastjson2.JSONWriter.Feature... r12) throws java.lang.Throwable {
        /*
            com.alibaba.fastjson2.JSONWriter$Context r0 = new com.alibaba.fastjson2.JSONWriter$Context
            com.alibaba.fastjson2.writer.ObjectWriterProvider r1 = com.alibaba.fastjson2.JSONFactory.defaultObjectWriterProvider
            r0.<init>(r1, r12)
            if (r10 == 0) goto L12
            boolean r12 = r10.isEmpty()
            if (r12 != 0) goto L12
            r0.setDateFormat(r10)
        L12:
            if (r11 == 0) goto L1a
            int r10 = r11.length
            if (r10 == 0) goto L1a
            r0.configFilter(r11)
        L1a:
            com.alibaba.fastjson2.JSONWriter r2 = com.alibaba.fastjson2.JSONWriter.ofUTF8(r0)     // Catch: java.lang.Exception -> L5c
            if (r9 != 0) goto L29
            r2.writeNull()     // Catch: java.lang.Throwable -> L25
            r3 = r9
            goto L3f
        L25:
            r0 = move-exception
            r8 = r0
            r3 = r9
            goto L4e
        L29:
            r2.rootObject = r9     // Catch: java.lang.Throwable -> L4b
            com.alibaba.fastjson2.JSONWriter$Path r10 = com.alibaba.fastjson2.JSONWriter.Path.ROOT     // Catch: java.lang.Throwable -> L4b
            r2.path = r10     // Catch: java.lang.Throwable -> L4b
            java.lang.Class r10 = r9.getClass()     // Catch: java.lang.Throwable -> L4b
            com.alibaba.fastjson2.writer.ObjectWriter r1 = r0.getObjectWriter(r10, r10)     // Catch: java.lang.Throwable -> L4b
            r5 = 0
            r6 = 0
            r4 = 0
            r3 = r9
            r1.write(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L49
        L3f:
            int r8 = r2.flushTo(r8)     // Catch: java.lang.Throwable -> L49
            if (r2 == 0) goto L48
            r2.close()     // Catch: java.lang.Exception -> L5a
        L48:
            return r8
        L49:
            r0 = move-exception
            goto L4d
        L4b:
            r0 = move-exception
            r3 = r9
        L4d:
            r8 = r0
        L4e:
            if (r2 == 0) goto L59
            r2.close()     // Catch: java.lang.Throwable -> L54
            goto L59
        L54:
            r0 = move-exception
            r9 = r0
            r8.addSuppressed(r9)     // Catch: java.lang.Exception -> L5a
        L59:
            throw r8     // Catch: java.lang.Exception -> L5a
        L5a:
            r0 = move-exception
            goto L5e
        L5c:
            r0 = move-exception
            r3 = r9
        L5e:
            r8 = r0
            com.alibaba.fastjson2.JSONException r9 = new com.alibaba.fastjson2.JSONException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r11 = "JSON#writeTo cannot serialize '"
            r10.<init>(r11)
            java.lang.StringBuilder r10 = r10.append(r3)
            java.lang.String r11 = "' to 'OutputStream'"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10, r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.writeTo(java.io.OutputStream, java.lang.Object, java.lang.String, com.alibaba.fastjson2.filter.Filter[], com.alibaba.fastjson2.JSONWriter$Feature[]):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean isValid(java.lang.String r2) {
        /*
            r0 = 0
            if (r2 == 0) goto L30
            boolean r1 = r2.isEmpty()
            if (r1 == 0) goto La
            goto L30
        La:
            com.alibaba.fastjson2.JSONReader r2 = com.alibaba.fastjson2.JSONReader.of(r2)     // Catch: java.lang.Throwable -> L30
            r2.skipValue()     // Catch: java.lang.Throwable -> L24
            boolean r1 = r2.isEnd()     // Catch: java.lang.Throwable -> L24
            if (r1 == 0) goto L1d
            boolean r1 = r2.comma     // Catch: java.lang.Throwable -> L24
            if (r1 != 0) goto L1d
            r1 = 1
            goto L1e
        L1d:
            r1 = r0
        L1e:
            if (r2 == 0) goto L23
            r2.close()     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L30
        L23:
            return r1
        L24:
            r1 = move-exception
            if (r2 == 0) goto L2f
            r2.close()     // Catch: java.lang.Throwable -> L2b
            goto L2f
        L2b:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L30
        L2f:
            throw r1     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L30
        L30:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.isValid(java.lang.String):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean isValid(java.lang.String r2, com.alibaba.fastjson2.JSONReader.Feature... r3) {
        /*
            r0 = 0
            if (r2 == 0) goto L34
            boolean r1 = r2.isEmpty()
            if (r1 == 0) goto La
            goto L34
        La:
            com.alibaba.fastjson2.JSONReader$Context r3 = com.alibaba.fastjson2.JSONFactory.createReadContext(r3)     // Catch: java.lang.Throwable -> L34
            com.alibaba.fastjson2.JSONReader r2 = com.alibaba.fastjson2.JSONReader.of(r2, r3)     // Catch: java.lang.Throwable -> L34
            r2.skipValue()     // Catch: java.lang.Throwable -> L28
            boolean r3 = r2.isEnd()     // Catch: java.lang.Throwable -> L28
            if (r3 == 0) goto L21
            boolean r3 = r2.comma     // Catch: java.lang.Throwable -> L28
            if (r3 != 0) goto L21
            r3 = 1
            goto L22
        L21:
            r3 = r0
        L22:
            if (r2 == 0) goto L27
            r2.close()     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L34
        L27:
            return r3
        L28:
            r3 = move-exception
            if (r2 == 0) goto L33
            r2.close()     // Catch: java.lang.Throwable -> L2f
            goto L33
        L2f:
            r2 = move-exception
            r3.addSuppressed(r2)     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L34
        L33:
            throw r3     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L34
        L34:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.isValid(java.lang.String, com.alibaba.fastjson2.JSONReader$Feature[]):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x001a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean isValid(char[] r2) {
        /*
            r0 = 0
            if (r2 == 0) goto L2d
            int r1 = r2.length
            if (r1 != 0) goto L7
            goto L2d
        L7:
            com.alibaba.fastjson2.JSONReader r2 = com.alibaba.fastjson2.JSONReader.of(r2)     // Catch: java.lang.Throwable -> L2d
            r2.skipValue()     // Catch: java.lang.Throwable -> L21
            boolean r1 = r2.isEnd()     // Catch: java.lang.Throwable -> L21
            if (r1 == 0) goto L1a
            boolean r1 = r2.comma     // Catch: java.lang.Throwable -> L21
            if (r1 != 0) goto L1a
            r1 = 1
            goto L1b
        L1a:
            r1 = r0
        L1b:
            if (r2 == 0) goto L20
            r2.close()     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L2d
        L20:
            return r1
        L21:
            r1 = move-exception
            if (r2 == 0) goto L2c
            r2.close()     // Catch: java.lang.Throwable -> L28
            goto L2c
        L28:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L2d
        L2c:
            throw r1     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L2d
        L2d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.isValid(char[]):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean isValidObject(java.lang.String r2) {
        /*
            r0 = 0
            if (r2 == 0) goto L3c
            boolean r1 = r2.isEmpty()
            if (r1 == 0) goto La
            goto L3c
        La:
            com.alibaba.fastjson2.JSONReader r2 = com.alibaba.fastjson2.JSONReader.of(r2)     // Catch: java.lang.Throwable -> L3c
            boolean r1 = r2.isObject()     // Catch: java.lang.Throwable -> L30
            if (r1 != 0) goto L1a
            if (r2 == 0) goto L19
            r2.close()     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L3c
        L19:
            return r0
        L1a:
            r2.skipValue()     // Catch: java.lang.Throwable -> L30
            boolean r1 = r2.isEnd()     // Catch: java.lang.Throwable -> L30
            if (r1 == 0) goto L29
            boolean r1 = r2.comma     // Catch: java.lang.Throwable -> L30
            if (r1 != 0) goto L29
            r1 = 1
            goto L2a
        L29:
            r1 = r0
        L2a:
            if (r2 == 0) goto L2f
            r2.close()     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L3c
        L2f:
            return r1
        L30:
            r1 = move-exception
            if (r2 == 0) goto L3b
            r2.close()     // Catch: java.lang.Throwable -> L37
            goto L3b
        L37:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L3c
        L3b:
            throw r1     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L3c
        L3c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.isValidObject(java.lang.String):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean isValidObject(byte[] r2) {
        /*
            r0 = 0
            if (r2 == 0) goto L39
            int r1 = r2.length
            if (r1 != 0) goto L7
            goto L39
        L7:
            com.alibaba.fastjson2.JSONReader r2 = com.alibaba.fastjson2.JSONReader.of(r2)     // Catch: java.lang.Throwable -> L39
            boolean r1 = r2.isObject()     // Catch: java.lang.Throwable -> L2d
            if (r1 != 0) goto L17
            if (r2 == 0) goto L16
            r2.close()     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L39
        L16:
            return r0
        L17:
            r2.skipValue()     // Catch: java.lang.Throwable -> L2d
            boolean r1 = r2.isEnd()     // Catch: java.lang.Throwable -> L2d
            if (r1 == 0) goto L26
            boolean r1 = r2.comma     // Catch: java.lang.Throwable -> L2d
            if (r1 != 0) goto L26
            r1 = 1
            goto L27
        L26:
            r1 = r0
        L27:
            if (r2 == 0) goto L2c
            r2.close()     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L39
        L2c:
            return r1
        L2d:
            r1 = move-exception
            if (r2 == 0) goto L38
            r2.close()     // Catch: java.lang.Throwable -> L34
            goto L38
        L34:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L39
        L38:
            throw r1     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L39
        L39:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.isValidObject(byte[]):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean isValidArray(java.lang.String r2) {
        /*
            r0 = 0
            if (r2 == 0) goto L3c
            boolean r1 = r2.isEmpty()
            if (r1 == 0) goto La
            goto L3c
        La:
            com.alibaba.fastjson2.JSONReader r2 = com.alibaba.fastjson2.JSONReader.of(r2)     // Catch: java.lang.Throwable -> L3c
            boolean r1 = r2.isArray()     // Catch: java.lang.Throwable -> L30
            if (r1 != 0) goto L1a
            if (r2 == 0) goto L19
            r2.close()     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L3c
        L19:
            return r0
        L1a:
            r2.skipValue()     // Catch: java.lang.Throwable -> L30
            boolean r1 = r2.isEnd()     // Catch: java.lang.Throwable -> L30
            if (r1 == 0) goto L29
            boolean r1 = r2.comma     // Catch: java.lang.Throwable -> L30
            if (r1 != 0) goto L29
            r1 = 1
            goto L2a
        L29:
            r1 = r0
        L2a:
            if (r2 == 0) goto L2f
            r2.close()     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L3c
        L2f:
            return r1
        L30:
            r1 = move-exception
            if (r2 == 0) goto L3b
            r2.close()     // Catch: java.lang.Throwable -> L37
            goto L3b
        L37:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L3c
        L3b:
            throw r1     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L3c
        L3c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.isValidArray(java.lang.String):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x001a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean isValid(byte[] r2) {
        /*
            r0 = 0
            if (r2 == 0) goto L2d
            int r1 = r2.length
            if (r1 != 0) goto L7
            goto L2d
        L7:
            com.alibaba.fastjson2.JSONReader r2 = com.alibaba.fastjson2.JSONReader.of(r2)     // Catch: java.lang.Throwable -> L2d
            r2.skipValue()     // Catch: java.lang.Throwable -> L21
            boolean r1 = r2.isEnd()     // Catch: java.lang.Throwable -> L21
            if (r1 == 0) goto L1a
            boolean r1 = r2.comma     // Catch: java.lang.Throwable -> L21
            if (r1 != 0) goto L1a
            r1 = 1
            goto L1b
        L1a:
            r1 = r0
        L1b:
            if (r2 == 0) goto L20
            r2.close()     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L2d
        L20:
            return r1
        L21:
            r1 = move-exception
            if (r2 == 0) goto L2c
            r2.close()     // Catch: java.lang.Throwable -> L28
            goto L2c
        L28:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L2d
        L2c:
            throw r1     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L2d
        L2d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.isValid(byte[]):boolean");
    }

    static boolean isValid(byte[] bArr, Charset charset) {
        if (bArr == null || bArr.length == 0) {
            return false;
        }
        return isValid(bArr, 0, bArr.length, charset);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean isValidArray(byte[] r2) {
        /*
            r0 = 0
            if (r2 == 0) goto L39
            int r1 = r2.length
            if (r1 != 0) goto L7
            goto L39
        L7:
            com.alibaba.fastjson2.JSONReader r2 = com.alibaba.fastjson2.JSONReader.of(r2)     // Catch: java.lang.Throwable -> L39
            boolean r1 = r2.isArray()     // Catch: java.lang.Throwable -> L2d
            if (r1 != 0) goto L17
            if (r2 == 0) goto L16
            r2.close()     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L39
        L16:
            return r0
        L17:
            r2.skipValue()     // Catch: java.lang.Throwable -> L2d
            boolean r1 = r2.isEnd()     // Catch: java.lang.Throwable -> L2d
            if (r1 == 0) goto L26
            boolean r1 = r2.comma     // Catch: java.lang.Throwable -> L2d
            if (r1 != 0) goto L26
            r1 = 1
            goto L27
        L26:
            r1 = r0
        L27:
            if (r2 == 0) goto L2c
            r2.close()     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L39
        L2c:
            return r1
        L2d:
            r1 = move-exception
            if (r2 == 0) goto L38
            r2.close()     // Catch: java.lang.Throwable -> L34
            goto L38
        L34:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L39
        L38:
            throw r1     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L39
        L39:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.isValidArray(byte[]):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x001c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean isValid(byte[] r2, int r3, int r4, java.nio.charset.Charset r5) {
        /*
            r0 = 0
            if (r2 == 0) goto L2f
            int r1 = r2.length
            if (r1 == 0) goto L2f
            if (r4 != 0) goto L9
            goto L2f
        L9:
            com.alibaba.fastjson2.JSONReader r2 = com.alibaba.fastjson2.JSONReader.of(r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L2f
            r2.skipValue()     // Catch: java.lang.Throwable -> L23
            boolean r3 = r2.isEnd()     // Catch: java.lang.Throwable -> L23
            if (r3 == 0) goto L1c
            boolean r3 = r2.comma     // Catch: java.lang.Throwable -> L23
            if (r3 != 0) goto L1c
            r3 = 1
            goto L1d
        L1c:
            r3 = r0
        L1d:
            if (r2 == 0) goto L22
            r2.close()     // Catch: java.lang.Throwable -> L2f java.lang.Throwable -> L2f
        L22:
            return r3
        L23:
            r3 = move-exception
            if (r2 == 0) goto L2e
            r2.close()     // Catch: java.lang.Throwable -> L2a
            goto L2e
        L2a:
            r2 = move-exception
            r3.addSuppressed(r2)     // Catch: java.lang.Throwable -> L2f java.lang.Throwable -> L2f
        L2e:
            throw r3     // Catch: java.lang.Throwable -> L2f java.lang.Throwable -> L2f
        L2f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.isValid(byte[], int, int, java.nio.charset.Charset):boolean");
    }

    static Object toJSON(Object obj) {
        return toJSON(obj, null);
    }

    static Object toJSON(Object obj, JSONWriter.Feature... featureArr) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof JSONObject) || (obj instanceof JSONArray)) {
            return obj;
        }
        JSONWriter.Context contextCreateWriteContext = featureArr == null ? JSONFactory.createWriteContext() : JSONFactory.createWriteContext(featureArr);
        Class<?> cls = obj.getClass();
        ObjectWriter objectWriter = contextCreateWriteContext.getObjectWriter(cls, cls);
        if ((objectWriter instanceof ObjectWriterAdapter) && !contextCreateWriteContext.isEnabled(JSONWriter.Feature.ReferenceDetection) && (objectWriter.getFeatures() & JSONWriter.Feature.WriteClassName.mask) == 0) {
            return ((ObjectWriterAdapter) objectWriter).toJSONObject(obj, contextCreateWriteContext.features);
        }
        try {
            JSONWriter jSONWriterOf = JSONWriter.of(contextCreateWriteContext);
            try {
                objectWriter.write(jSONWriterOf, obj, null, null, contextCreateWriteContext.features);
                String string = jSONWriterOf.toString();
                if (jSONWriterOf != null) {
                    jSONWriterOf.close();
                }
                return parse(string);
            } finally {
            }
        } catch (NullPointerException | NumberFormatException e) {
            throw new JSONException("toJSONString error", e);
        }
    }

    static <T> T to(Class<T> cls, Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONObject) {
            return (T) ((JSONObject) obj).to((Class) cls, new JSONReader.Feature[0]);
        }
        return (T) TypeUtils.cast(obj, (Class) cls, JSONFactory.getDefaultObjectReaderProvider());
    }

    static <T> T toJavaObject(Object obj, Class<T> cls) {
        return (T) to(cls, obj);
    }

    static void mixIn(Class<?> cls, Class<?> cls2) {
        JSONFactory.defaultObjectWriterProvider.mixIn(cls, cls2);
        JSONFactory.getDefaultObjectReaderProvider().mixIn(cls, cls2);
    }

    static ObjectReader<?> register(Type type, ObjectReader<?> objectReader) {
        return JSONFactory.getDefaultObjectReaderProvider().register(type, objectReader);
    }

    static ObjectReader<?> register(Type type, ObjectReader<?> objectReader, boolean z) {
        return JSONFactory.getDefaultObjectReaderProvider().register(type, objectReader, z);
    }

    static ObjectReader<?> registerIfAbsent(Type type, ObjectReader<?> objectReader) {
        return JSONFactory.getDefaultObjectReaderProvider().registerIfAbsent(type, objectReader);
    }

    static ObjectReader<?> registerIfAbsent(Type type, ObjectReader<?> objectReader, boolean z) {
        return JSONFactory.getDefaultObjectReaderProvider().registerIfAbsent(type, objectReader, z);
    }

    static boolean register(ObjectReaderModule objectReaderModule) {
        return JSONFactory.getDefaultObjectReaderProvider().register(objectReaderModule);
    }

    static void registerSeeAlsoSubType(Class cls) {
        registerSeeAlsoSubType(cls, null);
    }

    static void registerSeeAlsoSubType(Class cls, String str) {
        JSONFactory.getDefaultObjectReaderProvider().registerSeeAlsoSubType(cls, str);
    }

    static boolean register(ObjectWriterModule objectWriterModule) {
        return JSONFactory.getDefaultObjectWriterProvider().register(objectWriterModule);
    }

    static ObjectWriter<?> register(Type type, ObjectWriter<?> objectWriter) {
        return JSONFactory.getDefaultObjectWriterProvider().register(type, objectWriter);
    }

    static ObjectWriter<?> register(Type type, ObjectWriter<?> objectWriter, boolean z) {
        return JSONFactory.getDefaultObjectWriterProvider().register(type, objectWriter, z);
    }

    static ObjectWriter<?> registerIfAbsent(Type type, ObjectWriter<?> objectWriter) {
        return JSONFactory.getDefaultObjectWriterProvider().registerIfAbsent(type, objectWriter);
    }

    static ObjectWriter<?> registerIfAbsent(Type type, ObjectWriter<?> objectWriter, boolean z) {
        return JSONFactory.getDefaultObjectWriterProvider().registerIfAbsent(type, objectWriter, z);
    }

    static void register(Class cls, Filter filter) {
        if ((filter instanceof AfterFilter) || (filter instanceof BeforeFilter) || (filter instanceof ContextNameFilter) || (filter instanceof ContextValueFilter) || (filter instanceof LabelFilter) || (filter instanceof NameFilter) || (filter instanceof PropertyFilter) || (filter instanceof PropertyPreFilter) || (filter instanceof ValueFilter)) {
            JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(cls).setFilter(filter);
        }
    }

    static void config(JSONReader.Feature... featureArr) {
        for (JSONReader.Feature feature : featureArr) {
            if (feature == JSONReader.Feature.SupportAutoType) {
                throw new JSONException("not support config global autotype support");
            }
            JSONFactory.defaultReaderFeatures |= feature.mask;
        }
    }

    static void config(JSONReader.Feature feature, boolean z) {
        if (feature == JSONReader.Feature.SupportAutoType && z) {
            throw new JSONException("not support config global autotype support");
        }
        if (z) {
            JSONFactory.defaultReaderFeatures = feature.mask | JSONFactory.defaultReaderFeatures;
        } else {
            JSONFactory.defaultReaderFeatures = (~feature.mask) & JSONFactory.defaultReaderFeatures;
        }
    }

    static boolean isEnabled(JSONReader.Feature feature) {
        return (JSONFactory.defaultReaderFeatures & feature.mask) != 0;
    }

    static void configReaderDateFormat(String str) {
        JSONFactory.defaultReaderFormat = str;
    }

    static void configWriterDateFormat(String str) {
        JSONFactory.defaultWriterFormat = str;
    }

    static void configReaderZoneId(ZoneId zoneId) {
        JSONFactory.defaultReaderZoneId = zoneId;
    }

    static void configWriterZoneId(ZoneId zoneId) {
        JSONFactory.defaultWriterZoneId = zoneId;
    }

    static void config(JSONWriter.Feature... featureArr) {
        for (JSONWriter.Feature feature : featureArr) {
            JSONFactory.defaultWriterFeatures |= feature.mask;
        }
    }

    static void config(JSONWriter.Feature feature, boolean z) {
        if (z) {
            JSONFactory.defaultWriterFeatures = feature.mask | JSONFactory.defaultWriterFeatures;
        } else {
            JSONFactory.defaultWriterFeatures = (~feature.mask) & JSONFactory.defaultWriterFeatures;
        }
    }

    static boolean isEnabled(JSONWriter.Feature feature) {
        return (JSONFactory.defaultWriterFeatures & feature.mask) != 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x00dd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static <T> T copy(T r14, com.alibaba.fastjson2.JSONWriter.Feature... r15) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 251
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSON.copy(java.lang.Object, com.alibaba.fastjson2.JSONWriter$Feature[]):java.lang.Object");
    }

    static <T> T copyTo(Object obj, Class<T> cls, JSONWriter.Feature... featureArr) throws Throwable {
        JSONWriter jSONWriter;
        if (obj == null) {
            return null;
        }
        Class<?> cls2 = obj.getClass();
        long j = JSONFactory.defaultReaderFeatures;
        boolean z = false;
        boolean z2 = false;
        for (JSONWriter.Feature feature : featureArr) {
            j |= feature.mask;
            if (feature == JSONWriter.Feature.FieldBased) {
                z = true;
            } else if (feature == JSONWriter.Feature.BeanToArray) {
                z2 = true;
            }
        }
        ObjectWriter objectWriter = JSONFactory.defaultObjectWriterProvider.getObjectWriter(cls2, cls2, z);
        ObjectReader objectReader = JSONFactory.defaultObjectReaderProvider.getObjectReader(cls, z);
        if ((objectWriter instanceof ObjectWriterAdapter) && (objectReader instanceof ObjectReaderBean)) {
            List<FieldWriter> fieldWriters = objectWriter.getFieldWriters();
            if (objectReader instanceof ObjectReaderNoneDefaultConstructor) {
                HashMap map = new HashMap(fieldWriters.size(), 1.0f);
                for (int i = 0; i < fieldWriters.size(); i++) {
                    FieldWriter fieldWriter = fieldWriters.get(i);
                    map.put(fieldWriter.fieldName, fieldWriter.getFieldValue(obj));
                }
                return (T) objectReader.createInstance(map, j);
            }
            T t = (T) objectReader.createInstance(j);
            for (int i2 = 0; i2 < fieldWriters.size(); i2++) {
                FieldWriter fieldWriter2 = fieldWriters.get(i2);
                FieldReader fieldReader = objectReader.getFieldReader(fieldWriter2.fieldName);
                if (fieldReader != null) {
                    Object fieldValue = fieldWriter2.getFieldValue(obj);
                    if (fieldWriter2.fieldClass == Date.class && fieldReader.fieldClass == String.class) {
                        fieldValue = DateUtils.format((Date) fieldValue, fieldWriter2.format);
                    } else if (fieldWriter2.fieldClass == LocalDate.class && fieldReader.fieldClass == String.class) {
                        fieldValue = DateUtils.format((LocalDate) fieldValue, fieldWriter2.format);
                    } else if (fieldValue != null && !fieldReader.supportAcceptType(fieldValue.getClass())) {
                        fieldValue = copy(fieldValue, new JSONWriter.Feature[0]);
                    }
                    fieldReader.accept(t, fieldValue);
                }
            }
            return t;
        }
        JSONWriter jSONWriterOfJSONB = JSONWriter.ofJSONB(featureArr);
        try {
            jSONWriterOfJSONB.config(JSONWriter.Feature.WriteClassName);
            jSONWriter = jSONWriterOfJSONB;
            try {
                objectWriter.writeJSONB(jSONWriter, obj, null, null, 0L);
                byte[] bytes = jSONWriter.getBytes();
                if (jSONWriter != null) {
                    jSONWriter.close();
                }
                JSONReader jSONReaderOfJSONB = JSONReader.ofJSONB(bytes, JSONReader.Feature.SupportAutoType, JSONReader.Feature.SupportClassForName);
                if (z2) {
                    try {
                        jSONReaderOfJSONB.context.config(JSONReader.Feature.SupportArrayToBean);
                    } finally {
                    }
                }
                T t2 = (T) objectReader.readJSONBObject(jSONReaderOfJSONB, null, null, 0L);
                if (jSONReaderOfJSONB != null) {
                    jSONReaderOfJSONB.close();
                }
                return t2;
            } catch (Throwable th) {
                th = th;
                Throwable th2 = th;
                if (jSONWriter == null) {
                    throw th2;
                }
                try {
                    jSONWriter.close();
                    throw th2;
                } catch (Throwable th3) {
                    th2.addSuppressed(th3);
                    throw th2;
                }
            }
        } catch (Throwable th4) {
            th = th4;
            jSONWriter = jSONWriterOfJSONB;
        }
    }

    @SafeVarargs
    static void configEnumAsJavaBean(Class<? extends Enum>... clsArr) {
        JSONFactory.getDefaultObjectWriterProvider().configEnumAsJavaBean(clsArr);
    }
}
