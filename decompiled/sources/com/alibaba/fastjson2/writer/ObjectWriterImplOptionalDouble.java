package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.util.OptionalDouble;

/* JADX INFO: loaded from: classes.dex */
final class ObjectWriterImplOptionalDouble extends ObjectWriterPrimitiveImpl {
    static final ObjectWriterImplOptionalDouble INSTANCE = new ObjectWriterImplOptionalDouble();

    ObjectWriterImplOptionalDouble() {
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (obj == null) {
            jSONWriter.writeNull();
            return;
        }
        OptionalDouble optionalDouble = (OptionalDouble) obj;
        if (!optionalDouble.isPresent()) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeDouble(optionalDouble.getAsDouble());
        }
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (obj == null) {
            jSONWriter.writeNull();
            return;
        }
        OptionalDouble optionalDouble = (OptionalDouble) obj;
        if (!optionalDouble.isPresent()) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeDouble(optionalDouble.getAsDouble());
        }
    }
}
