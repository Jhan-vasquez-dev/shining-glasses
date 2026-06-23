package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.Enum;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
final class ObjectWriterImplEnum<E extends Enum<E>> extends ObjectWriterPrimitiveImpl {
    final String[] annotationNames;
    final Class defineClass;
    final Enum[] enumConstants;
    final Class enumType;
    final long features;
    final long[] hashCodes;
    byte[][] jsonbNames;
    final String[] names;
    long typeNameHash;
    byte[] typeNameJSONB;
    final Member valueField;

    /* JADX WARN: Multi-variable type inference failed */
    public ObjectWriterImplEnum(Class cls, Class cls2, Member member, String[] strArr, long j) {
        this.defineClass = cls;
        this.enumType = cls2;
        this.features = j;
        this.valueField = member;
        if (member instanceof AccessibleObject) {
            try {
                ((AccessibleObject) member).setAccessible(true);
            } catch (Throwable unused) {
            }
        }
        Enum[] enumArr = (Enum[]) cls2.getEnumConstants();
        this.enumConstants = enumArr;
        this.names = new String[enumArr.length];
        this.hashCodes = new long[enumArr.length];
        int i = 0;
        while (true) {
            Enum[] enumArr2 = this.enumConstants;
            if (i < enumArr2.length) {
                String strName = enumArr2[i].name();
                this.names[i] = strName;
                this.hashCodes[i] = Fnv.hashCode64(strName);
                i++;
            } else {
                this.annotationNames = strArr;
                return;
            }
        }
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (jSONWriter.isWriteTypeInfo(obj, type, j)) {
            if (this.typeNameJSONB == null) {
                String typeName = TypeUtils.getTypeName(this.enumType);
                this.typeNameJSONB = JSONB.toBytes(typeName);
                this.typeNameHash = Fnv.hashCode64(typeName);
            }
            jSONWriter.writeTypeName(this.typeNameJSONB, this.typeNameHash);
        }
        Enum r2 = (Enum) obj;
        if (jSONWriter.isEnabled(JSONWriter.Feature.WriteEnumUsingToString)) {
            jSONWriter.writeString(r2.toString());
            return;
        }
        if (this.jsonbNames == null) {
            this.jsonbNames = new byte[this.names.length][];
        }
        int iOrdinal = r2.ordinal();
        byte[] bytes = this.jsonbNames[iOrdinal];
        if (bytes == null) {
            bytes = JSONB.toBytes(this.names[iOrdinal]);
            this.jsonbNames[iOrdinal] = bytes;
        }
        jSONWriter.writeRaw(bytes);
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x006c  */
    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void write(com.alibaba.fastjson2.JSONWriter r5, java.lang.Object r6, java.lang.Object r7, java.lang.reflect.Type r8, long r9) {
        /*
            r4 = this;
            r7 = r6
            java.lang.Enum r7 = (java.lang.Enum) r7
            if (r7 != 0) goto L9
            r5.writeNull()
            return
        L9:
            java.lang.reflect.Member r8 = r4.valueField
            if (r8 == 0) goto L30
            boolean r0 = r8 instanceof java.lang.reflect.Field     // Catch: java.lang.Exception -> L27
            if (r0 == 0) goto L18
            java.lang.reflect.Field r8 = (java.lang.reflect.Field) r8     // Catch: java.lang.Exception -> L27
            java.lang.Object r8 = r8.get(r6)     // Catch: java.lang.Exception -> L27
            goto L21
        L18:
            java.lang.reflect.Method r8 = (java.lang.reflect.Method) r8     // Catch: java.lang.Exception -> L27
            r0 = 0
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch: java.lang.Exception -> L27
            java.lang.Object r8 = r8.invoke(r6, r0)     // Catch: java.lang.Exception -> L27
        L21:
            if (r8 == r6) goto L30
            r5.writeAny(r8)     // Catch: java.lang.Exception -> L27
            return
        L27:
            r5 = move-exception
            com.alibaba.fastjson2.JSONException r6 = new com.alibaba.fastjson2.JSONException
            java.lang.String r7 = "getEnumValue error"
            r6.<init>(r7, r5)
            throw r6
        L30:
            long r0 = r4.features
            long r8 = r9 | r0
            long r8 = r5.getFeatures(r8)
            com.alibaba.fastjson2.JSONWriter$Feature r6 = com.alibaba.fastjson2.JSONWriter.Feature.WriteEnumUsingToString
            long r0 = r6.mask
            long r0 = r0 & r8
            r2 = 0
            int r6 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r6 == 0) goto L4b
            java.lang.String r6 = r7.toString()
            r5.writeString(r6)
            return
        L4b:
            com.alibaba.fastjson2.JSONWriter$Feature r6 = com.alibaba.fastjson2.JSONWriter.Feature.WriteEnumUsingOrdinal
            long r0 = r6.mask
            long r8 = r8 & r0
            int r6 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
            if (r6 == 0) goto L5c
            int r6 = r7.ordinal()
            r5.writeInt32(r6)
            return
        L5c:
            java.lang.String[] r6 = r4.annotationNames
            if (r6 == 0) goto L6c
            int r6 = r7.ordinal()
            java.lang.String[] r8 = r4.annotationNames
            int r9 = r8.length
            if (r6 >= r9) goto L6c
            r6 = r8[r6]
            goto L6d
        L6c:
            r6 = 0
        L6d:
            if (r6 != 0) goto L73
            java.lang.String r6 = r7.name()
        L73:
            r5.writeString(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterImplEnum.write(com.alibaba.fastjson2.JSONWriter, java.lang.Object, java.lang.Object, java.lang.reflect.Type, long):void");
    }
}
