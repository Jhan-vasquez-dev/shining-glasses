package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.util.OptionalInt;

/* JADX INFO: loaded from: classes.dex */
final class ObjectWriterImplOptionalInt extends ObjectWriterPrimitiveImpl {
    static final ObjectWriterImplOptionalInt INSTANCE = new ObjectWriterImplOptionalInt();

    ObjectWriterImplOptionalInt() {
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (obj == null) {
            jSONWriter.writeNull();
            return;
        }
        OptionalInt optionalInt = (OptionalInt) obj;
        if (!optionalInt.isPresent()) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeInt32(optionalInt.getAsInt());
        }
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (obj == null) {
            jSONWriter.writeNull();
            return;
        }
        OptionalInt optionalInt = (OptionalInt) obj;
        if (!optionalInt.isPresent()) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeInt32(optionalInt.getAsInt());
        }
    }
}
