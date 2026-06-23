package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Type;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
class ObjectWriterImplBoolValueArray extends ObjectWriterPrimitiveImpl {
    private final Function<Object, boolean[]> function;
    static final ObjectWriterImplBoolValueArray INSTANCE = new ObjectWriterImplBoolValueArray(null);
    static final byte[] JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[Z");
    static final long JSONB_TYPE_HASH = Fnv.hashCode64("[Z");

    public ObjectWriterImplBoolValueArray(Function<Object, boolean[]> function) {
        this.function = function;
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        boolean[] zArrApply;
        if (jSONWriter.isWriteTypeInfo(obj, type, j)) {
            jSONWriter.writeTypeName(JSONB_TYPE_NAME_BYTES, JSONB_TYPE_HASH);
        }
        Function<Object, boolean[]> function = this.function;
        if (function != null && obj != null) {
            zArrApply = function.apply(obj);
        } else {
            zArrApply = (boolean[]) obj;
        }
        jSONWriter.writeBool(zArrApply);
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        boolean[] zArrApply;
        Function<Object, boolean[]> function = this.function;
        if (function != null && obj != null) {
            zArrApply = function.apply(obj);
        } else {
            zArrApply = (boolean[]) obj;
        }
        jSONWriter.writeBool(zArrApply);
    }
}
