package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class FieldReaderCollectionMethodReadOnly<T> extends FieldReaderObject<T> {
    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean isReadOnly() {
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x002a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    FieldReaderCollectionMethodReadOnly(java.lang.String r15, java.lang.reflect.Type r16, java.lang.Class r17, int r18, long r19, java.lang.String r21, com.alibaba.fastjson2.schema.JSONSchema r22, java.lang.reflect.Method r23, java.lang.reflect.Field r24) {
        /*
            r14 = this;
            r9 = 0
            r13 = 0
            r8 = 0
            r0 = r14
            r1 = r15
            r2 = r16
            r3 = r17
            r4 = r18
            r5 = r19
            r7 = r21
            r10 = r22
            r11 = r23
            r12 = r24
            r0.<init>(r1, r2, r3, r4, r5, r7, r8, r9, r10, r11, r12, r13)
            boolean r15 = r2 instanceof java.lang.reflect.ParameterizedType
            if (r15 == 0) goto L2a
            r15 = r2
            java.lang.reflect.ParameterizedType r15 = (java.lang.reflect.ParameterizedType) r15
            java.lang.reflect.Type[] r15 = r15.getActualTypeArguments()
            int r1 = r15.length
            if (r1 <= 0) goto L2a
            r1 = 0
            r15 = r15[r1]
            goto L2b
        L2a:
            r15 = 0
        L2b:
            r14.itemType = r15
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.FieldReaderCollectionMethodReadOnly.<init>(java.lang.String, java.lang.reflect.Type, java.lang.Class, int, long, java.lang.String, com.alibaba.fastjson2.schema.JSONSchema, java.lang.reflect.Method, java.lang.reflect.Field):void");
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (obj == null) {
            return;
        }
        try {
            Collection collection = (Collection) this.method.invoke(t, new Object[0]);
            if (collection == Collections.EMPTY_LIST || collection == Collections.EMPTY_SET || collection == null || collection.equals(obj)) {
                if (this.schema != null) {
                    this.schema.assertValidate(collection);
                    return;
                }
                return;
            }
            String name = collection.getClass().getName();
            if ("java.util.Collections$UnmodifiableRandomAccessList".equals(name) || "java.util.Arrays$ArrayList".equals(name) || "java.util.Collections$SingletonList".equals(name) || name.startsWith("java.util.ImmutableCollections$") || name.startsWith("java.util.Collections$Unmodifiable")) {
                return;
            }
            for (Object objCreateInstance : (Collection) obj) {
                if (objCreateInstance == null) {
                    collection.add(null);
                } else {
                    if ((objCreateInstance instanceof Map) && (this.itemType instanceof Class) && !((Class) this.itemType).isAssignableFrom(objCreateInstance.getClass())) {
                        if (this.itemReader == null) {
                            this.itemReader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(this.itemType);
                        }
                        objCreateInstance = this.itemReader.createInstance((Map) objCreateInstance, 0L);
                    }
                    collection.add(objCreateInstance);
                }
            }
            if (this.schema != null) {
                this.schema.assertValidate(collection);
            }
        } catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        Object object;
        if (this.initReader == null) {
            this.initReader = jSONReader.getContext().getObjectReader(this.fieldType);
        }
        if (jSONReader.jsonb) {
            object = this.initReader.readJSONBObject(jSONReader, this.fieldType, this.fieldName, 0L);
        } else {
            object = this.initReader.readObject(jSONReader, this.fieldType, this.fieldName, 0L);
        }
        accept(t, object);
    }
}
