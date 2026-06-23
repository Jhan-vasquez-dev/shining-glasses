package com.alibaba.fastjson.support.jaxrs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson2.JSONWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

/* JADX INFO: loaded from: classes.dex */
@Produces({"*/*"})
@Provider
@Consumes({"*/*"})
public class FastJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
    public static final Class<?>[] DEFAULT_UNREADABLES = {InputStream.class, Reader.class};
    public static final Class<?>[] DEFAULT_UNWRITABLES = {InputStream.class, OutputStream.class, Writer.class, StreamingOutput.class, Response.class};

    @Deprecated
    protected Charset charset;
    private Class<?>[] clazzes;

    @Deprecated
    protected String dateFormat;
    private FastJsonConfig fastJsonConfig;

    @Deprecated
    protected SerializerFeature[] features;

    @Deprecated
    protected SerializeFilter[] filters;
    private boolean pretty;

    @Context
    protected Providers providers;

    public long getSize(Object obj, Class<?> cls, Type type, Annotation[] annotationArr, MediaType mediaType) {
        return -1L;
    }

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    public FastJsonProvider() {
        this.charset = StandardCharsets.UTF_8;
        this.features = SerializerFeature.EMPTY;
        this.filters = new SerializeFilter[0];
        this.fastJsonConfig = new FastJsonConfig();
    }

    public FastJsonProvider(Class<?>[] clsArr) {
        this.charset = StandardCharsets.UTF_8;
        this.features = SerializerFeature.EMPTY;
        this.filters = new SerializeFilter[0];
        this.fastJsonConfig = new FastJsonConfig();
        this.clazzes = clsArr;
    }

    public FastJsonProvider setPretty(boolean z) {
        this.pretty = z;
        return this;
    }

    @Deprecated
    public FastJsonProvider(String str) {
        this.charset = StandardCharsets.UTF_8;
        this.features = SerializerFeature.EMPTY;
        this.filters = new SerializeFilter[0];
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        this.fastJsonConfig = fastJsonConfig;
        fastJsonConfig.setCharset(Charset.forName(str));
    }

    @Deprecated
    public Charset getCharset() {
        return this.fastJsonConfig.getCharset();
    }

    @Deprecated
    public void setCharset(Charset charset) {
        this.fastJsonConfig.setCharset(charset);
    }

    @Deprecated
    public String getDateFormat() {
        return this.fastJsonConfig.getDateFormat();
    }

    @Deprecated
    public void setDateFormat(String str) {
        this.fastJsonConfig.setDateFormat(str);
    }

    @Deprecated
    public SerializerFeature[] getFeatures() {
        return this.fastJsonConfig.getSerializerFeatures();
    }

    @Deprecated
    public void setFeatures(SerializerFeature... serializerFeatureArr) {
        this.fastJsonConfig.setSerializerFeatures(serializerFeatureArr);
    }

    @Deprecated
    public SerializeFilter[] getFilters() {
        return this.fastJsonConfig.getSerializeFilters();
    }

    @Deprecated
    public void setFilters(SerializeFilter... serializeFilterArr) {
        this.fastJsonConfig.setSerializeFilters(serializeFilterArr);
    }

    protected boolean isAssignableFrom(Class<?> cls, Class<?>[] clsArr) {
        if (cls == null) {
            return false;
        }
        for (Class<?> cls2 : clsArr) {
            if (cls2.isAssignableFrom(cls)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isValidType(Class<?> cls, Annotation[] annotationArr) {
        if (cls == null) {
            return false;
        }
        Class<?>[] clsArr = this.clazzes;
        if (clsArr == null) {
            return true;
        }
        for (Class<?> cls2 : clsArr) {
            if (cls2 == cls) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasMatchingMediaType(MediaType mediaType) {
        if (mediaType != null) {
            String subtype = mediaType.getSubtype();
            if (!"json".equalsIgnoreCase(subtype) && !subtype.endsWith("+json") && !"javascript".equals(subtype) && !"x-javascript".equals(subtype) && !"x-json".equals(subtype) && !"x-www-form-urlencoded".equalsIgnoreCase(subtype) && !subtype.endsWith("x-www-form-urlencoded")) {
                return false;
            }
        }
        return true;
    }

    public boolean isWriteable(Class<?> cls, Type type, Annotation[] annotationArr, MediaType mediaType) {
        if (hasMatchingMediaType(mediaType) && isAssignableFrom(cls, DEFAULT_UNWRITABLES)) {
            return isValidType(cls, annotationArr);
        }
        return false;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.ws.rs.WebApplicationException */
    public void writeTo(Object obj, Class<?> cls, Type type, Annotation[] annotationArr, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws WebApplicationException, IOException {
        SerializerFeature[] serializerFeatureArr;
        FastJsonConfig fastJsonConfigLocateConfigProvider = locateConfigProvider(cls, mediaType);
        SerializerFeature[] serializerFeatures = fastJsonConfigLocateConfigProvider.getSerializerFeatures();
        if (this.pretty) {
            if (serializerFeatures == null) {
                serializerFeatureArr = new SerializerFeature[]{SerializerFeature.PrettyFormat};
            } else {
                ArrayList arrayList = new ArrayList(Arrays.asList(serializerFeatures));
                arrayList.add(SerializerFeature.PrettyFormat);
                serializerFeatureArr = (SerializerFeature[]) arrayList.toArray(serializerFeatures);
            }
            fastJsonConfigLocateConfigProvider.setSerializerFeatures(serializerFeatureArr);
        }
        try {
            writeJSONStringWithFastJsonConfig(outputStream, fastJsonConfigLocateConfigProvider.getCharset(), obj, fastJsonConfigLocateConfigProvider.getSerializeConfig(), fastJsonConfigLocateConfigProvider.getSerializeFilters(), fastJsonConfigLocateConfigProvider.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, fastJsonConfigLocateConfigProvider.getSerializerFeatures());
            outputStream.flush();
        } catch (JSONException e) {
            throw new WebApplicationException(e);
        }
    }

    static final int writeJSONStringWithFastJsonConfig(OutputStream outputStream, Charset charset, Object obj, SerializeConfig serializeConfig, SerializeFilter[] serializeFilterArr, String str, int i, SerializerFeature... serializerFeatureArr) throws IOException {
        for (SerializerFeature serializerFeature : serializerFeatureArr) {
            i |= serializerFeature.mask;
        }
        JSONWriter.Context contextCreateWriteContext = JSON.createWriteContext(serializeConfig, i, serializerFeatureArr);
        contextCreateWriteContext.setDateFormat(str);
        JSONWriter jSONWriterOfUTF8 = JSONWriter.ofUTF8(contextCreateWriteContext);
        if (serializeFilterArr != null) {
            try {
                for (SerializeFilter serializeFilter : serializeFilterArr) {
                    JSON.configFilter(contextCreateWriteContext, serializeFilter);
                }
            } catch (Throwable th) {
                if (jSONWriterOfUTF8 != null) {
                    try {
                        jSONWriterOfUTF8.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        jSONWriterOfUTF8.writeAny(obj);
        int iFlushTo = jSONWriterOfUTF8.flushTo(outputStream, charset);
        if (jSONWriterOfUTF8 != null) {
            jSONWriterOfUTF8.close();
        }
        return iFlushTo;
    }

    public boolean isReadable(Class<?> cls, Type type, Annotation[] annotationArr, MediaType mediaType) {
        if (hasMatchingMediaType(mediaType) && isAssignableFrom(cls, DEFAULT_UNREADABLES)) {
            return isValidType(cls, annotationArr);
        }
        return false;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.ws.rs.WebApplicationException */
    public Object readFrom(Class<Object> cls, Type type, Annotation[] annotationArr, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws WebApplicationException, IOException {
        try {
            FastJsonConfig fastJsonConfigLocateConfigProvider = locateConfigProvider(cls, mediaType);
            return JSON.parseObject(inputStream, fastJsonConfigLocateConfigProvider.getCharset(), type, fastJsonConfigLocateConfigProvider.getParserConfig(), fastJsonConfigLocateConfigProvider.getParseProcess(), JSON.DEFAULT_PARSER_FEATURE, fastJsonConfigLocateConfigProvider.getFeatures());
        } catch (JSONException e) {
            throw new WebApplicationException(e);
        }
    }

    protected FastJsonConfig locateConfigProvider(Class<?> cls, MediaType mediaType) {
        Providers providers = this.providers;
        if (providers != null) {
            ContextResolver contextResolver = providers.getContextResolver(FastJsonConfig.class, mediaType);
            if (contextResolver == null) {
                contextResolver = this.providers.getContextResolver(FastJsonConfig.class, (MediaType) null);
            }
            if (contextResolver != null) {
                return (FastJsonConfig) contextResolver.getContext(cls);
            }
        }
        return this.fastJsonConfig;
    }
}
