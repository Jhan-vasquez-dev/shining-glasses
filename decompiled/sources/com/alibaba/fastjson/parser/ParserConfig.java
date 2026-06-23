package com.alibaba.fastjson.parser;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

/* JADX INFO: loaded from: classes.dex */
public class ParserConfig {
    public static final String AUTOTYPE_ACCEPT = "fastjson.parser.autoTypeAccept";
    public static final String DENY_PROPERTY = "fastjson.parser.deny";
    public static ParserConfig global = new ParserConfig(JSONFactory.getDefaultObjectReaderProvider(), false);
    private boolean asmEnable;
    private boolean autoTypeSupport;
    public final boolean fieldBase;
    final ObjectReaderProvider provider;

    @Deprecated
    public void setDefaultClassLoader(ClassLoader classLoader) {
    }

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    ParserConfig(ObjectReaderProvider objectReaderProvider, boolean z) {
        this.provider = objectReaderProvider;
        this.fieldBase = z;
    }

    public ParserConfig() {
        this(new ObjectReaderProvider(), false);
    }

    public ParserConfig(ClassLoader classLoader) {
        this(new ObjectReaderProvider(), false);
    }

    public ParserConfig(boolean z) {
        this(new ObjectReaderProvider(), z);
    }

    public boolean isAsmEnable() {
        return this.asmEnable;
    }

    public void setAsmEnable(boolean z) {
        this.asmEnable = z;
    }

    public ObjectReaderProvider getProvider() {
        return this.provider;
    }

    public void putDeserializer(Type type, com.alibaba.fastjson.parser.deserializer.ObjectDeserializer objectDeserializer) {
        getProvider().register(type, objectDeserializer);
    }

    public Class<?> checkAutoType(Class cls) {
        return JSONFactory.getDefaultObjectReaderProvider().checkAutoType(cls.getName(), null, 0L);
    }

    public boolean isSafeMode() {
        return ObjectReaderProvider.SAFE_MODE;
    }

    public void setSafeMode(boolean z) {
        if (z != ObjectReaderProvider.SAFE_MODE) {
            throw new JSONException("not support operation");
        }
    }

    public boolean isAutoTypeSupport() {
        return this.autoTypeSupport;
    }

    public void setAutoTypeSupport(boolean z) {
        this.autoTypeSupport = z;
    }

    public void addAccept(String str) {
        getProvider().addAutoTypeAccept(str);
    }

    public void addDeny(String str) {
        getProvider().addAutoTypeDeny(str);
    }

    public void addDenyInternal(String str) {
        getProvider().addAutoTypeDeny(str);
    }

    public void addAutoTypeCheckHandler(AutoTypeCheckHandler autoTypeCheckHandler) {
        if (getProvider().getAutoTypeBeforeHandler() != null) {
            throw new JSONException("not support operation");
        }
        getProvider().setAutoTypeBeforeHandler(autoTypeCheckHandler);
    }

    public interface AutoTypeCheckHandler extends JSONReader.AutoTypeBeforeHandler {
        @Override // com.alibaba.fastjson2.JSONReader.AutoTypeBeforeHandler
        default Class<?> apply(long j, Class<?> cls, long j2) {
            return null;
        }

        Class<?> handler(String str, Class<?> cls, int i);

        @Override // com.alibaba.fastjson2.JSONReader.AutoTypeBeforeHandler
        default Class<?> apply(String str, Class<?> cls, long j) {
            return handler(str, cls, (int) j);
        }
    }

    public void configFromPropety(Properties properties) {
        addItemsToDeny(splitItemsFormProperty(properties.getProperty(DENY_PROPERTY)));
        addItemsToAccept(splitItemsFormProperty(properties.getProperty(AUTOTYPE_ACCEPT)));
    }

    private void addItemsToDeny(String[] strArr) {
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            addDeny(str);
        }
    }

    private void addItemsToAccept(String[] strArr) {
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            addAccept(str);
        }
    }

    private static String[] splitItemsFormProperty(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        return str.split(",");
    }

    public com.alibaba.fastjson.parser.deserializer.ObjectDeserializer get(Type type) {
        ObjectReader objectReader = getProvider().getObjectReader(type);
        if (objectReader instanceof com.alibaba.fastjson.parser.deserializer.ObjectDeserializer) {
            return (com.alibaba.fastjson.parser.deserializer.ObjectDeserializer) objectReader;
        }
        return new ObjectDeserializerWrapper(objectReader);
    }

    public com.alibaba.fastjson.parser.deserializer.ObjectDeserializer getDeserializer(Type type) {
        ObjectReader objectReader = getProvider().getObjectReader(type);
        if (objectReader instanceof com.alibaba.fastjson.parser.deserializer.ObjectDeserializer) {
            return (com.alibaba.fastjson.parser.deserializer.ObjectDeserializer) objectReader;
        }
        return new ObjectDeserializerWrapper(objectReader);
    }

    public com.alibaba.fastjson.parser.deserializer.ObjectDeserializer getDeserializer(Class<?> cls, Type type) {
        Type type2 = cls;
        if (type != null) {
            type2 = type;
        }
        ObjectReader objectReader = getProvider().getObjectReader(type2);
        if (objectReader instanceof com.alibaba.fastjson.parser.deserializer.ObjectDeserializer) {
            return (com.alibaba.fastjson.parser.deserializer.ObjectDeserializer) objectReader;
        }
        return new ObjectDeserializerWrapper(objectReader);
    }

    public static void parserAllFieldToCache(Class<?> cls, Map<String, Field> map) {
        for (Field field : cls.getDeclaredFields()) {
            String name = field.getName();
            if (!map.containsKey(name)) {
                map.put(name, field);
            }
        }
        if (cls.getSuperclass() == null || cls.getSuperclass() == Object.class) {
            return;
        }
        parserAllFieldToCache(cls.getSuperclass(), map);
    }

    public static Field getFieldFromCache(String str, Map<String, Field> map) {
        Field field = map.get(str);
        if (field == null) {
            field = map.get("_" + str);
        }
        if (field == null) {
            field = map.get("m_" + str);
        }
        if (field == null) {
            char cCharAt = str.charAt(0);
            if (cCharAt >= 'a' && cCharAt <= 'z') {
                char[] charArray = str.toCharArray();
                charArray[0] = (char) (charArray[0] - ' ');
                field = map.get(new String(charArray));
            }
            if (str.length() > 2) {
                char cCharAt2 = str.charAt(1);
                if (cCharAt >= 'a' && cCharAt <= 'z' && cCharAt2 >= 'A' && cCharAt2 <= 'Z') {
                    for (Map.Entry<String, Field> entry : map.entrySet()) {
                        if (str.equalsIgnoreCase(entry.getKey())) {
                            return entry.getValue();
                        }
                    }
                }
            }
        }
        return field;
    }
}
