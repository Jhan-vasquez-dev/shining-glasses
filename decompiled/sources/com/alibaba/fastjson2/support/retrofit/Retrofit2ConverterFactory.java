package com.alibaba.fastjson2.support.retrofit;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/* JADX INFO: loaded from: classes.dex */
public class Retrofit2ConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private FastJsonConfig config;

    public Retrofit2ConverterFactory() {
        this.config = new FastJsonConfig();
    }

    public Retrofit2ConverterFactory(FastJsonConfig fastJsonConfig) {
        this.config = fastJsonConfig;
    }

    public static Retrofit2ConverterFactory create() {
        return create(new FastJsonConfig());
    }

    public static Retrofit2ConverterFactory create(FastJsonConfig fastJsonConfig) {
        if (fastJsonConfig == null) {
            throw new NullPointerException("fastJsonConfig == null");
        }
        return new Retrofit2ConverterFactory(fastJsonConfig);
    }

    public Converter<ResponseBody, Object> responseBodyConverter(Type type, Annotation[] annotationArr, Retrofit retrofit) {
        return new ResponseBodyConverter(type);
    }

    public Converter<Object, RequestBody> requestBodyConverter(Type type, Annotation[] annotationArr, Annotation[] annotationArr2, Retrofit retrofit) {
        return new RequestBodyConverter();
    }

    public FastJsonConfig getFastJsonConfig() {
        return this.config;
    }

    public Retrofit2ConverterFactory setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.config = fastJsonConfig;
        return this;
    }

    final class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private Type type;

        ResponseBodyConverter(Type type) {
            this.type = type;
        }

        public T convert(ResponseBody responseBody) throws IOException {
            try {
                try {
                    if (Retrofit2ConverterFactory.this.config.isJSONB()) {
                        return (T) JSONB.parseObject(responseBody.bytes(), this.type, Retrofit2ConverterFactory.this.config.getSymbolTable(), Retrofit2ConverterFactory.this.config.getReaderFilters(), Retrofit2ConverterFactory.this.config.getReaderFeatures());
                    }
                    return (T) JSON.parseObject(responseBody.bytes(), this.type, Retrofit2ConverterFactory.this.config.getDateFormat(), Retrofit2ConverterFactory.this.config.getReaderFilters(), Retrofit2ConverterFactory.this.config.getReaderFeatures());
                } catch (Exception e) {
                    throw new IOException("JSON parse error: " + e.getMessage(), e);
                }
            } finally {
                responseBody.close();
            }
        }
    }

    final class RequestBodyConverter<T> implements Converter<T, RequestBody> {
        RequestBodyConverter() {
        }

        public RequestBody convert(T t) throws IOException {
            byte[] jSONBytes;
            try {
                if (Retrofit2ConverterFactory.this.config.isJSONB()) {
                    jSONBytes = JSONB.toBytes(t, Retrofit2ConverterFactory.this.config.getSymbolTable(), Retrofit2ConverterFactory.this.config.getWriterFilters(), Retrofit2ConverterFactory.this.config.getWriterFeatures());
                } else {
                    jSONBytes = JSON.toJSONBytes(t, Retrofit2ConverterFactory.this.config.getDateFormat(), Retrofit2ConverterFactory.this.config.getWriterFilters(), Retrofit2ConverterFactory.this.config.getWriterFeatures());
                }
                return RequestBody.create(Retrofit2ConverterFactory.MEDIA_TYPE, jSONBytes);
            } catch (Exception e) {
                throw new IOException("Could not write JSON: " + e.getMessage(), e);
            }
        }
    }
}
