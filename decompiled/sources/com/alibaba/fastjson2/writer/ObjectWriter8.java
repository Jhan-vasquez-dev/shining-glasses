package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ObjectWriter8<T> extends ObjectWriterAdapter<T> {
    public final FieldWriter fieldWriter0;
    public final FieldWriter fieldWriter1;
    public final FieldWriter fieldWriter2;
    public final FieldWriter fieldWriter3;
    public final FieldWriter fieldWriter4;
    public final FieldWriter fieldWriter5;
    public final FieldWriter fieldWriter6;
    public final FieldWriter fieldWriter7;

    public ObjectWriter8(Class<T> cls, String str, String str2, long j, List<FieldWriter> list) {
        super(cls, str, str2, j, list);
        this.fieldWriter0 = list.get(0);
        this.fieldWriter1 = list.get(1);
        this.fieldWriter2 = list.get(2);
        this.fieldWriter3 = list.get(3);
        this.fieldWriter4 = list.get(4);
        this.fieldWriter5 = list.get(5);
        this.fieldWriter6 = list.get(6);
        this.fieldWriter7 = list.get(7);
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriterAdapter, com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        long features = this.features | j | jSONWriter.getFeatures();
        boolean z = (JSONWriter.Feature.BeanToArray.mask & features) != 0;
        if (jSONWriter.jsonb) {
            if (z) {
                writeArrayMappingJSONB(jSONWriter, obj, obj2, type, j);
                return;
            } else {
                writeJSONB(jSONWriter, obj, obj2, type, j);
                return;
            }
        }
        if (z) {
            writeArrayMapping(jSONWriter, obj, obj2, type, j | this.features);
            return;
        }
        if (!this.serializable) {
            if ((JSONWriter.Feature.ErrorOnNoneSerializable.mask & features) != 0) {
                errorOnNoneSerializable();
                return;
            } else if ((features & JSONWriter.Feature.IgnoreNoneSerializable.mask) != 0) {
                jSONWriter.writeNull();
                return;
            }
        }
        if (hasFilter(jSONWriter)) {
            writeWithFilter(jSONWriter, obj, obj2, type, 0L);
            return;
        }
        jSONWriter.startObject();
        if (((this.features | j) & JSONWriter.Feature.WriteClassName.mask) != 0 || jSONWriter.isWriteTypeInfo(obj, j)) {
            writeTypeInfo(jSONWriter);
        }
        this.fieldWriter0.write(jSONWriter, obj);
        this.fieldWriter1.write(jSONWriter, obj);
        this.fieldWriter2.write(jSONWriter, obj);
        this.fieldWriter3.write(jSONWriter, obj);
        this.fieldWriter4.write(jSONWriter, obj);
        this.fieldWriter5.write(jSONWriter, obj);
        this.fieldWriter6.write(jSONWriter, obj);
        this.fieldWriter7.write(jSONWriter, obj);
        jSONWriter.endObject();
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriterAdapter, com.alibaba.fastjson2.writer.ObjectWriter
    public final FieldWriter getFieldWriter(long j) {
        if (j == this.fieldWriter0.hashCode) {
            return this.fieldWriter0;
        }
        if (j == this.fieldWriter1.hashCode) {
            return this.fieldWriter1;
        }
        if (j == this.fieldWriter2.hashCode) {
            return this.fieldWriter2;
        }
        if (j == this.fieldWriter3.hashCode) {
            return this.fieldWriter3;
        }
        if (j == this.fieldWriter4.hashCode) {
            return this.fieldWriter4;
        }
        if (j == this.fieldWriter5.hashCode) {
            return this.fieldWriter5;
        }
        if (j == this.fieldWriter6.hashCode) {
            return this.fieldWriter6;
        }
        if (j == this.fieldWriter7.hashCode) {
            return this.fieldWriter7;
        }
        return null;
    }
}
