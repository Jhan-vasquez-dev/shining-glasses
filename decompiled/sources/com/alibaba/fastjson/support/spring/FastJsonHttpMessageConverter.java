package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONPObject;
import com.alibaba.fastjson2.JSONReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

/* JADX INFO: loaded from: classes.dex */
public class FastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {
    public static final MediaType APPLICATION_JAVASCRIPT = new MediaType("application", "javascript");
    private FastJsonConfig fastJsonConfig;

    protected boolean supports(Class<?> cls) {
        return true;
    }

    public FastJsonHttpMessageConverter() {
        super(MediaType.ALL);
        this.fastJsonConfig = new FastJsonConfig();
    }

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    public boolean canRead(Type type, Class<?> cls, MediaType mediaType) {
        return super.canRead(cls, mediaType);
    }

    public boolean canWrite(Type type, Class<?> cls, MediaType mediaType) {
        return super.canWrite(cls, mediaType);
    }

    public Object read(Type type, Class<?> cls, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return readType(getType(type, cls), httpInputMessage);
    }

    public void write(Object obj, Type type, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        super.write(obj, mediaType, httpOutputMessage);
    }

    protected Object readInternal(Class<?> cls, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return readType(getType(cls, null), httpInputMessage);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.springframework.http.converter.HttpMessageNotReadableException */
    private Object readType(Type type, HttpInputMessage httpInputMessage) throws HttpMessageNotReadableException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                InputStream body = httpInputMessage.getBody();
                byte[] bArr = new byte[65536];
                while (true) {
                    int i = body.read(bArr);
                    if (i == -1) {
                        break;
                    }
                    if (i > 0) {
                        byteArrayOutputStream.write(bArr, 0, i);
                    }
                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                JSONReader.Context contextCreateReadContext = JSON.createReadContext(JSONFactory.getDefaultObjectReaderProvider(), JSON.DEFAULT_PARSER_FEATURE, this.fastJsonConfig.getFeatures());
                String dateFormat = this.fastJsonConfig.getDateFormat();
                if (dateFormat != null) {
                    contextCreateReadContext.setDateFormat(dateFormat);
                }
                Object object = JSON.parseObject(byteArray, type, contextCreateReadContext);
                byteArrayOutputStream.close();
                return object;
            } catch (Throwable th) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (JSONException e) {
            throw new HttpMessageNotReadableException("JSON parse error: " + e.getMessage(), e);
        } catch (IOException e2) {
            throw new HttpMessageNotReadableException("I/O error while reading input message", e2);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.springframework.http.converter.HttpMessageNotWritableException */
    protected void writeInternal(Object obj, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        int iWriteJSONString;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                HttpHeaders headers = httpOutputMessage.getHeaders();
                if ((obj instanceof String) && com.alibaba.fastjson2.JSON.isValidObject((String) obj)) {
                    byte[] bytes = ((String) obj).getBytes(this.fastJsonConfig.getCharset());
                    iWriteJSONString = bytes.length;
                    httpOutputMessage.getBody().write(bytes, 0, bytes.length);
                } else if ((obj instanceof byte[]) && com.alibaba.fastjson2.JSON.isValid((byte[]) obj)) {
                    byte[] bArr = (byte[]) obj;
                    iWriteJSONString = bArr.length;
                    httpOutputMessage.getBody().write(bArr, 0, bArr.length);
                } else {
                    if (obj instanceof JSONPObject) {
                        headers.setContentType(APPLICATION_JAVASCRIPT);
                    }
                    iWriteJSONString = JSON.writeJSONString(byteArrayOutputStream, obj, this.fastJsonConfig.getDateFormat(), this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getSerializerFeatures());
                }
                if (headers.getContentLength() < 0 && this.fastJsonConfig.isWriteContentLength()) {
                    headers.setContentLength(iWriteJSONString);
                }
                byteArrayOutputStream.writeTo(httpOutputMessage.getBody());
                byteArrayOutputStream.close();
            } catch (Throwable th) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (JSONException e) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + e.getMessage(), e);
        } catch (IOException e2) {
            throw new HttpMessageNotWritableException("I/O error while writing output message", e2);
        }
    }

    protected Type getType(Type type, Class<?> cls) {
        return Spring4TypeResolvableHelper.isSupport() ? Spring4TypeResolvableHelper.getType(type, cls) : type;
    }

    private static class Spring4TypeResolvableHelper {
        private static boolean hasClazzResolvableType;

        private Spring4TypeResolvableHelper() {
        }

        static {
            try {
                Class.forName("org.springframework.core.ResolvableType");
                hasClazzResolvableType = true;
            } catch (ClassNotFoundException unused) {
                hasClazzResolvableType = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isSupport() {
            return hasClazzResolvableType;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Type getType(Type type, Class<?> cls) {
            if (cls == null) {
                return type;
            }
            ResolvableType resolvableTypeForType = ResolvableType.forType(type);
            if (type instanceof TypeVariable) {
                ResolvableType resolvableTypeResolveVariable = resolveVariable((TypeVariable) type, ResolvableType.forClass(cls));
                return resolvableTypeResolveVariable != ResolvableType.NONE ? resolvableTypeResolveVariable.resolve() : type;
            }
            if (!(type instanceof ParameterizedType) || !resolvableTypeForType.hasUnresolvableGenerics()) {
                return type;
            }
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class[] clsArr = new Class[parameterizedType.getActualTypeArguments().length];
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type type2 = actualTypeArguments[i];
                if (type2 instanceof TypeVariable) {
                    ResolvableType resolvableTypeResolveVariable2 = resolveVariable((TypeVariable) type2, ResolvableType.forClass(cls));
                    if (resolvableTypeResolveVariable2 != ResolvableType.NONE) {
                        clsArr[i] = resolvableTypeResolveVariable2.resolve();
                    } else {
                        clsArr[i] = ResolvableType.forType(type2).resolve();
                    }
                } else {
                    clsArr[i] = ResolvableType.forType(type2).resolve();
                }
            }
            return ResolvableType.forClassWithGenerics(resolvableTypeForType.getRawClass(), clsArr).getType();
        }

        private static ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType resolvableType) {
            if (resolvableType.hasGenerics()) {
                ResolvableType resolvableTypeForType = ResolvableType.forType(typeVariable, resolvableType);
                if (resolvableTypeForType.resolve() != null) {
                    return resolvableTypeForType;
                }
            }
            ResolvableType superType = resolvableType.getSuperType();
            if (superType != ResolvableType.NONE) {
                ResolvableType resolvableTypeResolveVariable = resolveVariable(typeVariable, superType);
                if (resolvableTypeResolveVariable.resolve() != null) {
                    return resolvableTypeResolveVariable;
                }
            }
            for (ResolvableType resolvableType2 : resolvableType.getInterfaces()) {
                ResolvableType resolvableTypeResolveVariable2 = resolveVariable(typeVariable, resolvableType2);
                if (resolvableTypeResolveVariable2.resolve() != null) {
                    return resolvableTypeResolveVariable2;
                }
            }
            return ResolvableType.NONE;
        }
    }
}
