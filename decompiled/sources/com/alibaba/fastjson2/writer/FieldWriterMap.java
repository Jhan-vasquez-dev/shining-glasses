package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
abstract class FieldWriterMap extends FieldWriterObject {
    protected final Class<?> contentAs;
    protected Type contentAsFieldType;
    private final Type keyType;
    volatile ObjectWriter mapWriter;
    private final Type valueType;
    final boolean valueTypeRefDetect;
    volatile ObjectWriter valueWriter;

    /* JADX WARN: Removed duplicated region for block: B:7:0x001a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected FieldWriterMap(java.lang.String r1, int r2, long r3, java.lang.String r5, java.util.Locale r6, java.lang.String r7, java.lang.reflect.Type r8, java.lang.Class r9, java.lang.reflect.Field r10, java.lang.reflect.Method r11, java.lang.Class<?> r12) {
        /*
            r0 = this;
            r0.<init>(r1, r2, r3, r5, r6, r7, r8, r9, r10, r11)
            r1 = r0
            boolean r2 = r8 instanceof java.lang.reflect.ParameterizedType
            r3 = 0
            r4 = 2
            r5 = 1
            r6 = 0
            if (r2 == 0) goto L1a
            java.lang.reflect.ParameterizedType r8 = (java.lang.reflect.ParameterizedType) r8
            java.lang.reflect.Type[] r2 = r8.getActualTypeArguments()
            int r7 = r2.length
            if (r7 != r4) goto L1a
            r7 = r2[r3]
            r2 = r2[r5]
            goto L1c
        L1a:
            r2 = r6
            r7 = r2
        L1c:
            if (r7 != 0) goto L20
            java.lang.Class<java.lang.Object> r7 = java.lang.Object.class
        L20:
            if (r2 != 0) goto L24
            java.lang.Class<java.lang.Object> r2 = java.lang.Object.class
        L24:
            if (r12 == 0) goto L33
            com.alibaba.fastjson2.util.ParameterizedTypeImpl r6 = new com.alibaba.fastjson2.util.ParameterizedTypeImpl
            java.lang.reflect.Type[] r4 = new java.lang.reflect.Type[r4]
            java.lang.Class<java.lang.String> r8 = java.lang.String.class
            r4[r3] = r8
            r4[r5] = r12
            r6.<init>(r9, r4)
        L33:
            r1.contentAs = r12
            r1.contentAsFieldType = r6
            r1.keyType = r7
            r1.valueType = r2
            java.lang.Class r2 = com.alibaba.fastjson2.util.TypeUtils.getClass(r2)
            boolean r2 = com.alibaba.fastjson2.writer.ObjectWriterProvider.isNotReferenceDetect(r2)
            r2 = r2 ^ r5
            r1.valueTypeRefDetect = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.FieldWriterMap.<init>(java.lang.String, int, long, java.lang.String, java.util.Locale, java.lang.String, java.lang.reflect.Type, java.lang.Class, java.lang.reflect.Field, java.lang.reflect.Method, java.lang.Class):void");
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriterObject, com.alibaba.fastjson2.writer.FieldWriter
    public ObjectWriter getObjectWriter(JSONWriter jSONWriter, Class cls) {
        Type type;
        Class<?> cls2 = this.contentAs;
        if (cls2 == null || !this.fieldClass.isAssignableFrom(cls)) {
            return super.getObjectWriter(jSONWriter, cls);
        }
        ObjectWriter objectWriter = this.valueWriter;
        if (objectWriter != null) {
            return objectWriter;
        }
        Type type2 = this.fieldType;
        Type type3 = this.valueType;
        long j = this.features;
        if (cls2 != null) {
            type2 = this.contentAsFieldType;
            j |= Long.MIN_VALUE;
            type = cls2;
        } else {
            type = type3;
        }
        ObjectWriterImplMap objectWriterImplMap = new ObjectWriterImplMap(this.keyType, type, this.format, cls, type2, j);
        this.mapWriter = objectWriterImplMap;
        return objectWriterImplMap;
    }
}
