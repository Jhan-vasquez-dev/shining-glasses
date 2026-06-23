package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Type;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class ObjectWriterImplCharValueArray extends ObjectWriterPrimitiveImpl {
    private final Function<Object, char[]> function;
    static final ObjectWriterImplCharValueArray INSTANCE = new ObjectWriterImplCharValueArray(null);
    static final byte[] JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[C");
    static final long JSONB_TYPE_HASH = Fnv.hashCode64("[C");

    public ObjectWriterImplCharValueArray(Function<Object, char[]> function) {
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        char[] cArrApply;
        if (jSONWriter.isWriteTypeInfo(obj, type, j)) {
            jSONWriter.writeTypeName(JSONB_TYPE_NAME_BYTES, JSONB_TYPE_HASH);
        }
        Function<Object, char[]> function = this.function;
        if (function != null && obj != null) {
            cArrApply = function.apply(obj);
        } else {
            cArrApply = (char[]) obj;
        }
        jSONWriter.writeString(cArrApply, 0, cArrApply.length);
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        char[] cArrApply;
        Function<Object, char[]> function = this.function;
        if (function != null && obj != null) {
            cArrApply = function.apply(obj);
        } else {
            cArrApply = (char[]) obj;
        }
        if (jSONWriter.utf16) {
            jSONWriter.writeString(cArrApply, 0, cArrApply.length);
        } else {
            jSONWriter.writeString(new String(cArrApply));
        }
    }
}
