package com.alibaba.fastjson.support.retrofit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
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
    private FastJsonConfig fastJsonConfig;

    public Retrofit2ConverterFactory() {
        this.fastJsonConfig = new FastJsonConfig();
    }

    public Retrofit2ConverterFactory(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
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
        return this.fastJsonConfig;
    }

    public Retrofit2ConverterFactory setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
        return this;
    }

    final class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Type type;

        ResponseBodyConverter(Type type) {
            this.type = type;
        }

        public T convert(ResponseBody responseBody) throws IOException {
            try {
                try {
                    return (T) JSON.parseObject(responseBody.bytes(), this.type, Retrofit2ConverterFactory.this.fastJsonConfig.getFeatures());
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
            try {
                return RequestBody.create(Retrofit2ConverterFactory.MEDIA_TYPE, JSON.toJSONBytes(t, Retrofit2ConverterFactory.this.fastJsonConfig.getSerializeFilters(), Retrofit2ConverterFactory.this.fastJsonConfig.getSerializerFeatures()));
            } catch (Exception e) {
                throw new IOException("Could not write JSON: " + e.getMessage(), e);
            }
        }
    }
}
