package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.internal.asm.ASMUtils;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
final class ObjectReaderException<T> extends ObjectReaderAdapter<T> {
    final Constructor constructorCause;
    final Constructor constructorDefault;
    final Constructor constructorMessage;
    final Constructor constructorMessageCause;
    final List<String[]> constructorParameters;
    final List<Constructor> constructors;
    private final FieldReader fieldReaderStackTrace;
    static final long HASH_TYPE = Fnv.hashCode64("@type");
    static final long HASH_MESSAGE = Fnv.hashCode64("message");
    static final long HASH_DETAIL_MESSAGE = Fnv.hashCode64("detailMessage");
    static final long HASH_LOCALIZED_MESSAGE = Fnv.hashCode64("localizedMessage");
    static final long HASH_CAUSE = Fnv.hashCode64("cause");
    static final long HASH_STACKTRACE = Fnv.hashCode64("stackTrace");
    static final long HASH_SUPPRESSED_EXCEPTIONS = Fnv.hashCode64("suppressedExceptions");

    ObjectReaderException(Class<T> cls) {
        this(cls, Arrays.asList(BeanUtils.getConstructor(cls)), ObjectReaders.fieldReader("stackTrace", StackTraceElement[].class, new BiConsumer() { // from class: com.alibaba.fastjson2.reader.ObjectReaderException$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((Throwable) obj).setStackTrace((StackTraceElement[]) obj2);
            }
        }));
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    ObjectReaderException(Class<T> cls, List<Constructor> list, FieldReader... fieldReaderArr) {
        int i;
        String[] strArrLookupParameterNames;
        super(cls, null, cls.getName(), 0L, null, null, null, fieldReaderArr);
        FieldReader fieldReader = null;
        this.constructors = list;
        Iterator<Constructor> it = list.iterator();
        Constructor constructor = null;
        Constructor constructor2 = null;
        Constructor constructor3 = null;
        Constructor constructor4 = null;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Constructor next = it.next();
            if (next != null && constructor3 == null) {
                int parameterCount = next.getParameterCount();
                if (parameterCount == 0) {
                    constructor = next;
                } else {
                    Class<?>[] parameterTypes = next.getParameterTypes();
                    Class<?> cls2 = parameterTypes[0];
                    if (parameterCount == 1) {
                        if (cls2 == String.class) {
                            constructor2 = next;
                        } else if (Throwable.class.isAssignableFrom(cls2)) {
                            constructor4 = next;
                        }
                    }
                    if (parameterCount == 2 && cls2 == String.class && Throwable.class.isAssignableFrom(parameterTypes[1])) {
                        constructor3 = next;
                    }
                }
            }
        }
        this.constructorDefault = constructor;
        this.constructorMessage = constructor2;
        this.constructorMessageCause = constructor3;
        this.constructorCause = constructor4;
        list.sort(new Comparator() { // from class: com.alibaba.fastjson2.reader.ObjectReaderException$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Integer.compare(((Constructor) obj2).getParameterCount(), ((Constructor) obj).getParameterCount());
            }
        });
        this.constructorParameters = new ArrayList(list.size());
        for (Constructor constructor5 : list) {
            if (constructor5.getParameterCount() > 0) {
                strArrLookupParameterNames = ASMUtils.lookupParameterNames(constructor5);
                Parameter[] parameters = constructor5.getParameters();
                FieldInfo fieldInfo = new FieldInfo();
                for (int i2 = 0; i2 < parameters.length && i2 < strArrLookupParameterNames.length; i2++) {
                    fieldInfo.init();
                    JSONFactory.getDefaultObjectReaderProvider().getFieldInfo(fieldInfo, cls, constructor5, i2, parameters[i2]);
                    if (fieldInfo.fieldName != null) {
                        strArrLookupParameterNames[i2] = fieldInfo.fieldName;
                    }
                }
            } else {
                strArrLookupParameterNames = null;
            }
            this.constructorParameters.add(strArrLookupParameterNames);
        }
        for (FieldReader fieldReader2 : fieldReaderArr) {
            if ("stackTrace".equals(fieldReader2.fieldName) && fieldReader2.fieldClass == StackTraceElement[].class) {
                fieldReader = fieldReader2;
            }
        }
        this.fieldReaderStackTrace = fieldReader;
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x008a  */
    @Override // com.alibaba.fastjson2.reader.ObjectReaderBean, com.alibaba.fastjson2.reader.ObjectReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public T readObject(com.alibaba.fastjson2.JSONReader r21, java.lang.reflect.Type r22, java.lang.Object r23, long r24) {
        /*
            Method dump skipped, instruction units count: 810
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderException.readObject(com.alibaba.fastjson2.JSONReader, java.lang.reflect.Type, java.lang.Object, long):java.lang.Object");
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderAdapter, com.alibaba.fastjson2.reader.ObjectReader
    public T createInstance(Map map, long j) {
        if (map == null) {
            return null;
        }
        return readObject(JSONReader.of(JSON.toJSONString(map)), j);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderAdapter, com.alibaba.fastjson2.reader.ObjectReader
    public T readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        ObjectReader objectReader;
        if (jSONReader.getType() == -110) {
            JSONReader.Context context = jSONReader.getContext();
            if (jSONReader.isSupportAutoType(j) || context.getContextAutoTypeBeforeHandler() != null) {
                jSONReader.next();
                ObjectReader objectReaderAutoType = context.getObjectReaderAutoType(jSONReader.readTypeHashCode());
                if (objectReaderAutoType == null) {
                    String string = jSONReader.getString();
                    ObjectReader objectReaderAutoType2 = context.getObjectReaderAutoType(string, null);
                    if (objectReaderAutoType2 == null) {
                        throw new JSONException("autoType not support : " + string + ", offset " + jSONReader.getOffset());
                    }
                    objectReader = objectReaderAutoType2;
                } else {
                    objectReader = objectReaderAutoType;
                }
                return (T) objectReader.readJSONBObject(jSONReader, type, obj, 0L);
            }
        }
        return readObject(jSONReader, type, obj, j);
    }

    private Throwable createObject(String str, Throwable th) {
        try {
            Constructor constructor = this.constructorMessageCause;
            if (constructor != null && th != null && str != null) {
                return (Throwable) constructor.newInstance(str, th);
            }
            Constructor constructor2 = this.constructorMessage;
            if (constructor2 != null && str != null) {
                return (Throwable) constructor2.newInstance(str);
            }
            Constructor constructor3 = this.constructorCause;
            if (constructor3 != null && th != null) {
                return (Throwable) constructor3.newInstance(th);
            }
            if (constructor != null && (th != null || str != null)) {
                return (Throwable) constructor.newInstance(str, th);
            }
            Constructor constructor4 = this.constructorDefault;
            if (constructor4 != null) {
                return (Throwable) constructor4.newInstance(new Object[0]);
            }
            if (constructor != null) {
                return (Throwable) constructor.newInstance(str, th);
            }
            if (constructor2 != null) {
                return (Throwable) constructor2.newInstance(str);
            }
            if (constructor3 != null) {
                return (Throwable) constructor3.newInstance(th);
            }
            return null;
        } catch (Throwable th2) {
            throw new JSONException("create Exception error, class " + this.objectClass.getName() + ", " + th2.getMessage(), th2);
        }
    }
}
