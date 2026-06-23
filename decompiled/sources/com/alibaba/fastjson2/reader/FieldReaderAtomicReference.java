package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public abstract class FieldReaderAtomicReference<T> extends FieldReader<T> {
    final Type referenceType;

    /* JADX WARN: Removed duplicated region for block: B:7:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public FieldReaderAtomicReference(java.lang.String r14, java.lang.reflect.Type r15, java.lang.Class r16, int r17, long r18, java.lang.String r20, com.alibaba.fastjson2.schema.JSONSchema r21, java.lang.reflect.Method r22, java.lang.reflect.Field r23) {
        /*
            r13 = this;
            r8 = 0
            r9 = 0
            r0 = r13
            r1 = r14
            r2 = r15
            r3 = r16
            r4 = r17
            r5 = r18
            r7 = r20
            r10 = r21
            r11 = r22
            r12 = r23
            r0.<init>(r1, r2, r3, r4, r5, r7, r8, r9, r10, r11, r12)
            boolean r14 = r15 instanceof java.lang.reflect.ParameterizedType
            if (r14 == 0) goto L28
            java.lang.reflect.ParameterizedType r15 = (java.lang.reflect.ParameterizedType) r15
            java.lang.reflect.Type[] r14 = r15.getActualTypeArguments()
            int r15 = r14.length
            r1 = 1
            if (r15 != r1) goto L28
            r15 = 0
            r14 = r14[r15]
            goto L29
        L28:
            r14 = 0
        L29:
            r13.referenceType = r14
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.FieldReaderAtomicReference.<init>(java.lang.String, java.lang.reflect.Type, java.lang.Class, int, long, java.lang.String, com.alibaba.fastjson2.schema.JSONSchema, java.lang.reflect.Method, java.lang.reflect.Field):void");
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        if (jSONReader.nextIfNull()) {
            return;
        }
        accept(t, jSONReader.read(this.referenceType));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public Object readFieldValue(JSONReader jSONReader) {
        return jSONReader.read(this.referenceType);
    }
}
