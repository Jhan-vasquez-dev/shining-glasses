package com.alibaba.fastjson.support.spring.messaging;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import java.nio.charset.StandardCharsets;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

/* JADX INFO: loaded from: classes.dex */
public class MappingFastJsonMessageConverter extends AbstractMessageConverter {
    private FastJsonConfig fastJsonConfig;

    protected boolean supports(Class<?> cls) {
        return true;
    }

    public MappingFastJsonMessageConverter() {
        super(new MimeType("application", "json", StandardCharsets.UTF_8));
        this.fastJsonConfig = new FastJsonConfig();
    }

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    protected boolean canConvertFrom(Message<?> message, Class<?> cls) {
        return supports(cls);
    }

    protected boolean canConvertTo(Object obj, MessageHeaders messageHeaders) {
        return supports(obj.getClass());
    }

    protected Object convertFromInternal(Message<?> message, Class<?> cls, Object obj) {
        Object payload = message.getPayload();
        if (payload instanceof byte[]) {
            return JSON.parseObject((byte[]) payload, (Class) cls, this.fastJsonConfig.getFeatures());
        }
        if (payload instanceof String) {
            return JSON.parseObject((String) payload, (Class) cls, this.fastJsonConfig.getFeatures());
        }
        return null;
    }

    protected Object convertToInternal(Object obj, MessageHeaders messageHeaders, Object obj2) {
        if (byte[].class != getSerializedPayloadClass()) {
            return ((obj instanceof String) && JSON.isValid((String) obj)) ? obj : JSON.toJSONString(obj, this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getSerializerFeatures());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (JSON.isValid(str)) {
                return str.getBytes(this.fastJsonConfig.getCharset());
            }
        }
        return JSON.toJSONBytes(obj, this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getSerializerFeatures());
    }
}
