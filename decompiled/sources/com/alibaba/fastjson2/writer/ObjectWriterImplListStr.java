package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class ObjectWriterImplListStr extends ObjectWriterPrimitiveImpl {
    static final ObjectWriterImplListStr INSTANCE = new ObjectWriterImplListStr();

    ObjectWriterImplListStr() {
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (obj == null) {
            jSONWriter.writeNull();
            return;
        }
        List list = (List) obj;
        jSONWriter.startArray();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                jSONWriter.writeComma();
            }
            String str = (String) list.get(i);
            if (str == null) {
                jSONWriter.writeNull();
            } else {
                jSONWriter.writeString(str);
            }
        }
        jSONWriter.endArray();
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0026  */
    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void writeJSONB(com.alibaba.fastjson2.JSONWriter r2, java.lang.Object r3, java.lang.Object r4, java.lang.reflect.Type r5, long r6) {
        /*
            r1 = this;
            if (r3 != 0) goto L6
            r2.writeArrayNull()
            return
        L6:
            java.lang.reflect.ParameterizedType r4 = com.alibaba.fastjson2.util.TypeUtils.PARAM_TYPE_LIST_STR
            if (r5 != r4) goto Ld
            java.lang.Class<java.util.List> r4 = java.util.List.class
            goto L27
        Ld:
            boolean r4 = r5 instanceof java.lang.Class
            if (r4 == 0) goto L15
            r4 = r5
            java.lang.Class r4 = (java.lang.Class) r4
            goto L27
        L15:
            boolean r4 = r5 instanceof java.lang.reflect.ParameterizedType
            if (r4 == 0) goto L26
            java.lang.reflect.ParameterizedType r5 = (java.lang.reflect.ParameterizedType) r5
            java.lang.reflect.Type r4 = r5.getRawType()
            boolean r5 = r4 instanceof java.lang.Class
            if (r5 == 0) goto L26
            java.lang.Class r4 = (java.lang.Class) r4
            goto L27
        L26:
            r4 = 0
        L27:
            java.lang.Class r5 = r3.getClass()
            java.lang.Class<java.util.ArrayList> r0 = java.util.ArrayList.class
            if (r5 == r0) goto L42
            boolean r4 = r2.isWriteTypeInfo(r3, r4, r6)
            if (r4 == 0) goto L42
            java.lang.Class r4 = com.alibaba.fastjson2.writer.ObjectWriterImplList.CLASS_SUBLIST
            if (r5 != r4) goto L3b
            java.lang.Class<java.util.ArrayList> r5 = java.util.ArrayList.class
        L3b:
            java.lang.String r4 = com.alibaba.fastjson2.util.TypeUtils.getTypeName(r5)
            r2.writeTypeName(r4)
        L42:
            java.util.List r3 = (java.util.List) r3
            r2.writeString(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterImplListStr.writeJSONB(com.alibaba.fastjson2.JSONWriter, java.lang.Object, java.lang.Object, java.lang.reflect.Type, long):void");
    }
}
