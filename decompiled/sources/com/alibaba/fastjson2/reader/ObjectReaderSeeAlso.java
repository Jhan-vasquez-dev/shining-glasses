package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.annotation.JSONType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
final class ObjectReaderSeeAlso<T> extends ObjectReaderAdapter<T> {
    ObjectReaderSeeAlso(Class cls, Supplier<T> supplier, String str, Class[] clsArr, String[] strArr, Class cls2, FieldReader... fieldReaderArr) {
        super(cls, str, null, JSONReader.Feature.SupportAutoType.mask, null, supplier, null, clsArr, strArr, cls2, fieldReaderArr);
    }

    ObjectReaderSeeAlso addSubType(Class cls, String str) {
        JSONType jSONType;
        for (int i = 0; i < this.seeAlso.length; i++) {
            if (this.seeAlso[i] == cls) {
                return this;
            }
        }
        Class[] clsArr = (Class[]) Arrays.copyOf(this.seeAlso, this.seeAlso.length + 1);
        String[] strArr = (String[]) Arrays.copyOf(this.seeAlsoNames, this.seeAlsoNames.length + 1);
        clsArr[clsArr.length - 1] = cls;
        if (str == null && (jSONType = (JSONType) cls.getAnnotation(JSONType.class)) != null) {
            str = jSONType.typeName();
        }
        if (str != null) {
            strArr[strArr.length - 1] = str;
        }
        return new ObjectReaderSeeAlso(this.objectClass, this.creator, this.typeKey, clsArr, strArr, this.seeAlsoDefault, this.fieldReaders);
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderAdapter, com.alibaba.fastjson2.reader.ObjectReader
    public T createInstance(long j) {
        if (this.creator == null) {
            return null;
        }
        return this.creator.get();
    }

    @Override // com.alibaba.fastjson2.reader.ObjectReaderAdapter, com.alibaba.fastjson2.reader.ObjectReader
    public T readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
        if (jSONReader.nextIfNull()) {
            return null;
        }
        ObjectReader objectReaderCheckAutoType = jSONReader.checkAutoType(this.objectClass, this.typeNameHash, this.features | j);
        if (objectReaderCheckAutoType != null && objectReaderCheckAutoType.getObjectClass() != this.objectClass) {
            return (T) objectReaderCheckAutoType.readJSONBObject(jSONReader, type, obj, j);
        }
        if (!this.serializable) {
            jSONReader.errorOnNoneSerializable(this.objectClass);
        }
        if (jSONReader.isArray()) {
            if (jSONReader.isSupportBeanArray()) {
                return readArrayMappingJSONBObject(jSONReader, type, obj, j);
            }
            throw new JSONException(jSONReader.info("expect object, but " + JSONB.typeName(jSONReader.getType())));
        }
        JSONReader.SavePoint savePointMark = jSONReader.mark();
        jSONReader.nextIfObjectStart();
        int i = 0;
        T tCreateInstance = null;
        while (!jSONReader.nextIfObjectEnd()) {
            long fieldNameHashCode = jSONReader.readFieldNameHashCode();
            if (fieldNameHashCode == this.typeKeyHashCode) {
                long valueHashCode = jSONReader.readValueHashCode();
                JSONReader.Context context = jSONReader.getContext();
                ObjectReader objectReaderAutoType = autoType(context, valueHashCode);
                if (objectReaderAutoType == null) {
                    String string = jSONReader.getString();
                    ObjectReader objectReaderAutoType2 = context.getObjectReaderAutoType(string, null);
                    if (objectReaderAutoType2 == null) {
                        throw new JSONException(jSONReader.info("autoType not support : " + string));
                    }
                    objectReaderAutoType = objectReaderAutoType2;
                }
                if (objectReaderAutoType != this) {
                    if (i != 0) {
                        jSONReader.reset(savePointMark);
                    }
                    jSONReader.setTypeRedirect(true);
                    return (T) objectReaderAutoType.readJSONBObject(jSONReader, type, obj, j);
                }
            } else if (fieldNameHashCode != 0) {
                FieldReader fieldReader = getFieldReader(fieldNameHashCode);
                if (fieldReader == null && jSONReader.isSupportSmartMatch(this.features | j)) {
                    fieldReader = getFieldReaderLCase(jSONReader.getNameHashCodeLCase());
                }
                if (fieldReader == null) {
                    processExtra(jSONReader, tCreateInstance);
                } else {
                    if (tCreateInstance == null) {
                        tCreateInstance = createInstance(jSONReader.getContext().getFeatures() | j);
                    }
                    fieldReader.readFieldValue(jSONReader, tCreateInstance);
                }
            }
            i++;
        }
        if (tCreateInstance == null) {
            tCreateInstance = createInstance(jSONReader.getContext().getFeatures() | j);
        }
        if (this.schema != null) {
            this.schema.assertValidate(tCreateInstance);
        }
        return tCreateInstance;
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x0215 A[PHI: r9
      0x0215: PHI (r9v2 com.alibaba.fastjson2.reader.ObjectReader) = 
      (r9v1 com.alibaba.fastjson2.reader.ObjectReader)
      (r9v7 com.alibaba.fastjson2.reader.ObjectReader)
      (r9v7 com.alibaba.fastjson2.reader.ObjectReader)
     binds: [B:112:0x0202, B:114:0x0208, B:116:0x020e] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0254 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:163:0x024d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0160  */
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
    @Override // com.alibaba.fastjson2.reader.ObjectReaderBean, com.alibaba.fastjson2.reader.ObjectReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public T readObject(com.alibaba.fastjson2.JSONReader r23, java.lang.reflect.Type r24, java.lang.Object r25, long r26) {
        /*
            Method dump skipped, instruction units count: 664
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.ObjectReaderSeeAlso.readObject(com.alibaba.fastjson2.JSONReader, java.lang.reflect.Type, java.lang.Object, long):java.lang.Object");
    }
}
