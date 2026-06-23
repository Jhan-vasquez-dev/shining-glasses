package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.ContextAutoTypeBeforeHandler;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/* JADX INFO: loaded from: classes.dex */
public class GenericFastJsonRedisSerializer implements RedisSerializer<Object> {
    ContextAutoTypeBeforeHandler contextFilter;

    public GenericFastJsonRedisSerializer() {
        this.contextFilter = null;
    }

    public GenericFastJsonRedisSerializer(String[] strArr) {
        this.contextFilter = new ContextAutoTypeBeforeHandler(strArr);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.springframework.data.redis.serializer.SerializationException */
    public byte[] serialize(Object obj) throws SerializationException {
        if (obj == null) {
            return new byte[0];
        }
        try {
            return JSON.toJSONBytes(obj, SerializerFeature.WriteClassName);
        } catch (Exception e) {
            throw new SerializationException("Could not serialize: " + e.getMessage(), e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.springframework.data.redis.serializer.SerializationException */
    public Object deserialize(byte[] bArr) throws SerializationException {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        try {
            return JSON.parseObject(bArr, Object.class, this.contextFilter, Feature.SupportAutoType);
        } catch (Exception e) {
            throw new SerializationException("Could not deserialize: " + e.getMessage(), e);
        }
    }
}
