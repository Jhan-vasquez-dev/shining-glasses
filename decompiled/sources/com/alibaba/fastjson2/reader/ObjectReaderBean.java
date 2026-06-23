package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.filter.ExtraProcessor;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public abstract class ObjectReaderBean<T> implements ObjectReader<T> {
    protected JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler;
    protected final Function buildFunction;
    protected final Supplier<T> creator;
    protected FieldReader extraFieldReader;
    protected final long features;
    protected boolean hasDefaultValue;
    protected final Class objectClass;
    protected final JSONSchema schema;
    protected final boolean serializable;
    protected final String typeName;
    protected final long typeNameHash;

    protected void initDefaultValue(T t) {
    }

    protected void initStringFieldAsEmpty(Object obj) {
    }

    protected ObjectReaderBean(Class cls, Supplier<T> supplier, String str, long j, JSONSchema jSONSchema, Function function) {
        if (str == null && cls != null) {
            str = TypeUtils.getTypeName(cls);
        }
        this.objectClass = cls;
        this.creator = supplier;
        this.buildFunction = function;
        this.features = j;
        this.typeName = str;
        this.typeNameHash = str != null ? Fnv.hashCode64(str) : 0L;
        this.schema = jSONSchema;
        this.serializable = cls != null && Serializable.class.isAssignableFrom(cls);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public Class<T> getObjectClass() {
        return this.objectClass;
    }

    protected T processObjectInputSingleItemArray(JSONReader jSONReader, Type type, Object obj, long j) {
        String str = "expect {, but [, class " + this.typeName;
        if (obj != null) {
            str = str + ", parent fieldName " + obj;
        }
        String strInfo = jSONReader.info(str);
        if ((jSONReader.features(j) & JSONReader.Feature.SupportSmartMatch.mask) != 0) {
            if (type == null) {
                type = this.objectClass;
            }
            List array = jSONReader.readArray(type);
            if (array != null) {
                if (array.size() == 0) {
                    return null;
                }
                if (array.size() == 1) {
                    return (T) array.get(0);
                }
            }
        }
        throw new JSONException(strInfo);
    }

    protected void processExtra(JSONReader jSONReader, Object obj) {
        processExtra(jSONReader, obj, 0L);
    }

    protected void processExtra(JSONReader jSONReader, Object obj, long j) {
        FieldReader fieldReaderLCase;
        Class cls;
        if ((jSONReader.features(this.features | j) & JSONReader.Feature.SupportSmartMatch.mask) != 0) {
            String fieldName = jSONReader.getFieldName();
            if (fieldName.startsWith("is") && (fieldReaderLCase = getFieldReaderLCase(Fnv.hashCode64LCase(fieldName.substring(2)))) != null && ((cls = fieldReaderLCase.fieldClass) == Boolean.class || cls == Boolean.TYPE)) {
                fieldReaderLCase.readFieldValue(jSONReader, obj);
                return;
            }
        }
        FieldReader fieldReader = this.extraFieldReader;
        if (fieldReader != null && obj != null) {
            fieldReader.processExtra(jSONReader, obj);
            return;
        }
        ExtraProcessor extraProcessor = jSONReader.getContext().getExtraProcessor();
        if (extraProcessor != null) {
            String fieldName2 = jSONReader.getFieldName();
            extraProcessor.processExtra(obj, fieldName2, jSONReader.read(extraProcessor.getType(fieldName2)));
        } else {
            if ((jSONReader.features(j) & JSONReader.Feature.ErrorOnUnknownProperties.mask) != 0) {
                throw new JSONException("Unknown Property " + jSONReader.getFieldName());
            }
            jSONReader.skipValue();
        }
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReader
    public void acceptExtra(Object obj, String str, Object obj2, long j) {
        FieldReader fieldReaderLCase;
        Class cls;
        FieldReader fieldReader = this.extraFieldReader;
        if (fieldReader == null || obj == null) {
            if (str.startsWith("is") && (fieldReaderLCase = getFieldReaderLCase(Fnv.hashCode64LCase(str.substring(2)))) != null && ((cls = fieldReaderLCase.fieldClass) == Boolean.class || cls == Boolean.TYPE)) {
                fieldReaderLCase.accept(obj, obj2);
                return;
            } else {
                if ((j & JSONReader.Feature.ErrorOnUnknownProperties.mask) != 0) {
                    throw new JSONException("Unknown Property " + str);
                }
                return;
            }
        }
        fieldReader.acceptExtra(obj, str, obj2);
    }

    @Deprecated
    public final ObjectReader checkAutoType(JSONReader jSONReader, Class cls, long j) {
        return checkAutoType(jSONReader, j);
    }

    public final ObjectReader checkAutoType(JSONReader jSONReader, long j) {
        if (jSONReader.nextIfMatchTypedAny()) {
            return checkAutoType0(jSONReader, j);
        }
        return null;
    }

    protected final ObjectReader checkAutoType0(JSONReader jSONReader, long j) {
        ObjectReader objectReaderAutoType;
        Class<?> cls = this.objectClass;
        long typeHashCode = jSONReader.readTypeHashCode();
        JSONReader.Context context = jSONReader.getContext();
        long jFeatures = jSONReader.features(this.features | j);
        JSONReader.AutoTypeBeforeHandler contextAutoTypeBeforeHandler = context.getContextAutoTypeBeforeHandler();
        if (contextAutoTypeBeforeHandler != null) {
            Class<?> clsApply = contextAutoTypeBeforeHandler.apply(typeHashCode, cls, j);
            if (clsApply != null || (clsApply = contextAutoTypeBeforeHandler.apply(jSONReader.getString(), cls, j)) == null || cls.isAssignableFrom(clsApply)) {
                cls = clsApply;
            } else if ((jSONReader.features(j) & JSONReader.Feature.IgnoreAutoTypeNotMatch.mask) == 0) {
                throw notMatchError();
            }
            objectReaderAutoType = context.getObjectReader(cls);
        } else {
            objectReaderAutoType = jSONReader.getObjectReaderAutoType(typeHashCode, cls, j);
            if (objectReaderAutoType == null) {
                throw auotypeError(jSONReader);
            }
            Class<?> objectClass = objectReaderAutoType.getObjectClass();
            if (cls == null || objectClass == null || cls.isAssignableFrom(objectClass)) {
                if (typeHashCode == this.typeNameHash || (JSONReader.Feature.SupportAutoType.mask & jFeatures) == 0) {
                    objectReaderAutoType = null;
                }
            } else {
                if ((JSONReader.Feature.IgnoreAutoTypeNotMatch.mask & jFeatures) == 0) {
                    throw notMatchError();
                }
                objectReaderAutoType = context.getObjectReader(cls);
            }
        }
        if (objectReaderAutoType == this || (objectReaderAutoType != null && objectReaderAutoType.getObjectClass() == this.objectClass)) {
            return null;
        }
        return objectReaderAutoType;
    }

    private JSONException notMatchError() {
        return new JSONException("type not match. " + this.typeName + " -> " + this.objectClass.getName());
    }

    private JSONException auotypeError(JSONReader jSONReader) {
        return new JSONException(jSONReader.info("autoType not support"));
    }

    public void readObject(JSONReader jSONReader, Object obj, long j) {
        if (jSONReader.nextIfNull()) {
            jSONReader.nextIfComma();
            return;
        }
        if (!jSONReader.nextIfObjectStart()) {
            throw new JSONException(jSONReader.info());
        }
        while (!jSONReader.nextIfObjectEnd()) {
            FieldReader fieldReader = getFieldReader(jSONReader.readFieldNameHashCode());
            if (fieldReader == null && jSONReader.isSupportSmartMatch(getFeatures() | j)) {
                fieldReader = getFieldReaderLCase(jSONReader.getNameHashCodeLCase());
            }
            if (fieldReader == null) {
                processExtra(jSONReader, obj);
            } else {
                fieldReader.readFieldValue(jSONReader, obj);
            }
        }
        jSONReader.nextIfComma();
        JSONSchema jSONSchema = this.schema;
        if (jSONSchema != null) {
            jSONSchema.assertValidate(obj);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x0141 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x013b  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x013e  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0161  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.alibaba.fastjson2.reader.ObjectReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public T readObject(com.alibaba.fastjson2.JSONReader r20, java.lang.reflect.Type r21, java.lang.Object r22, long r23) {
        /*
            Method dump skipped, instruction units count: 416
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderBean.readObject(com.alibaba.fastjson2.JSONReader, java.lang.reflect.Type, java.lang.Object, long):java.lang.Object");
    }

    public JSONReader.AutoTypeBeforeHandler getAutoTypeBeforeHandler() {
        return this.autoTypeBeforeHandler;
    }

    public void setAutoTypeBeforeHandler(JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler) {
        this.autoTypeBeforeHandler = autoTypeBeforeHandler;
    }

    protected boolean readFieldValueWithLCase(JSONReader jSONReader, Object obj, long j, long j2) {
        FieldReader fieldReaderLCase;
        if (!jSONReader.isSupportSmartMatch(j2)) {
            return false;
        }
        long nameHashCodeLCase = jSONReader.getNameHashCodeLCase();
        if (nameHashCodeLCase == j || (fieldReaderLCase = getFieldReaderLCase(nameHashCodeLCase)) == null) {
            return false;
        }
        fieldReaderLCase.readFieldValue(jSONReader, obj);
        return true;
    }
}
