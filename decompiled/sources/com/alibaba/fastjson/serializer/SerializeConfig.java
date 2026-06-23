package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.util.TypeUtils;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.io.IOException;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class SerializeConfig {
    public static final ObjectWriterProvider DEFAULT_PROVIDER;
    public static final SerializeConfig global;
    public static final SerializeConfig globalInstance;
    public final boolean fieldBased;
    public PropertyNamingStrategy propertyNamingStrategy;
    private ObjectWriterProvider provider;

    public void setAsmEnable(boolean z) {
    }

    static {
        SerializeConfig serializeConfig = new SerializeConfig((ObjectWriterProvider) null);
        global = serializeConfig;
        globalInstance = serializeConfig;
        DEFAULT_PROVIDER = new ObjectWriterProvider(TypeUtils.compatibleWithFieldName ? null : com.alibaba.fastjson2.PropertyNamingStrategy.CamelCase1x);
    }

    public static SerializeConfig getGlobalInstance() {
        return global;
    }

    public SerializeConfig() {
        this(new ObjectWriterProvider());
    }

    public SerializeConfig(ObjectWriterProvider objectWriterProvider) {
        this.fieldBased = false;
        this.provider = objectWriterProvider;
    }

    public SerializeConfig(boolean z) {
        this.fieldBased = z;
    }

    public ObjectWriterProvider getProvider() {
        ObjectWriterProvider objectWriterProvider = this.provider;
        if (objectWriterProvider == null) {
            objectWriterProvider = DEFAULT_PROVIDER;
        }
        if (TypeUtils.compatibleWithFieldName && objectWriterProvider.getNamingStrategy() == com.alibaba.fastjson2.PropertyNamingStrategy.CamelCase1x) {
            objectWriterProvider.setNamingStrategy(null);
        }
        return objectWriterProvider;
    }

    public boolean put(Type type, ObjectSerializer objectSerializer) {
        ObjectWriterProvider objectWriterProvider = this.provider;
        if (objectWriterProvider == null) {
            objectWriterProvider = DEFAULT_PROVIDER;
        }
        return objectWriterProvider.register(type, new ObjectSerializerAdapter(objectSerializer), this.fieldBased) == null;
    }

    static final class ObjectSerializerAdapter implements ObjectWriter {
        final ObjectSerializer serializer;

        public ObjectSerializerAdapter(ObjectSerializer objectSerializer) {
            this.serializer = objectSerializer;
        }

        @Override // com.alibaba.fastjson2.writer.ObjectWriter
        public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
            try {
                this.serializer.write(new JSONSerializer(jSONWriter), obj, obj2, type, 0);
            } catch (IOException e) {
                throw new JSONException("serializer write error", e);
            }
        }
    }

    public void addFilter(Class<?> cls, SerializeFilter serializeFilter) {
        getProvider().getObjectWriter((Class) cls).setFilter(serializeFilter);
    }

    @Deprecated
    public boolean put(Object obj, Object obj2) {
        return put((Type) obj, (ObjectSerializer) obj2);
    }

    public ObjectSerializer getObjectWriter(Class<?> cls) {
        ObjectWriter objectWriter = getProvider().getObjectWriter((Class) cls);
        if (objectWriter instanceof ObjectSerializer) {
            return (ObjectSerializer) objectWriter;
        }
        return new JavaBeanSerializer(objectWriter);
    }

    public final ObjectSerializer get(Type type) {
        ObjectWriter objectWriter = getProvider().getObjectWriter(type, com.alibaba.fastjson2.util.TypeUtils.getClass(type));
        if (objectWriter instanceof ObjectSerializer) {
            return (ObjectSerializer) objectWriter;
        }
        return new JavaBeanSerializer(objectWriter);
    }

    public final ObjectSerializer createJavaBeanSerializer(Class<?> cls) {
        return new JavaBeanSerializer(getProvider().getCreator().createObjectWriter(cls));
    }

    public void configEnumAsJavaBean(Class<? extends Enum>... clsArr) {
        for (Class<? extends Enum> cls : clsArr) {
            put((Type) cls, createJavaBeanSerializer(cls));
        }
    }
}
