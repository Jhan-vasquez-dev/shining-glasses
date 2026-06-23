package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
public class FieldReaderMapField<T> extends FieldReaderObjectField<T> {
    protected final BiConsumer arrayToMapDuplicateHandler;
    protected final String arrayToMapKey;
    protected final PropertyNamingStrategy namingStrategy;
    protected final Type valueType;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ void accept(Object obj, byte b) {
        super.accept(obj, b);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ void accept(Object obj, char c) {
        super.accept(obj, c);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ void accept(Object obj, double d) {
        super.accept(obj, d);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ void accept(Object obj, float f) {
        super.accept(obj, f);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ void accept(Object obj, int i) {
        super.accept(obj, i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ void accept(Object obj, long j) {
        super.accept(obj, j);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ void accept(Object obj, Object obj2) {
        super.accept(obj, obj2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ void accept(Object obj, short s) {
        super.accept(obj, s);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReaderObjectField, com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ void accept(Object obj, boolean z) {
        super.accept(obj, z);
    }

    FieldReaderMapField(String str, Type type, Class cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Field field, String str3, BiConsumer biConsumer) {
        super(str, type, cls, i, j, str2, locale, obj, jSONSchema, field);
        this.valueType = TypeUtils.getMapValueType(type);
        this.arrayToMapKey = str3;
        this.namingStrategy = PropertyNamingStrategy.of(str2);
        this.arrayToMapDuplicateHandler = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    protected void acceptAny(T t, Object obj, long j) {
        if (this.arrayToMapKey != null && (obj instanceof Collection)) {
            Map map = (Map) getObjectReader(JSONFactory.createReadContext()).createInstance(j);
            arrayToMap(map, (Collection) obj, this.arrayToMapKey, this.namingStrategy, JSONFactory.getObjectReader(this.valueType, j | this.features), this.arrayToMapDuplicateHandler);
            accept(t, map);
            return;
        }
        super.acceptAny(t, obj, j);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        if (this.arrayToMapKey != null && jSONReader.isArray()) {
            Map map = (Map) getObjectReader(jSONReader).createInstance(this.features);
            arrayToMap(map, jSONReader.readArray(this.valueType), this.arrayToMapKey, this.namingStrategy, JSONFactory.getObjectReader(this.valueType, this.features), this.arrayToMapDuplicateHandler);
            accept(t, map);
            return;
        }
        super.readFieldValue(jSONReader, t);
    }
}
