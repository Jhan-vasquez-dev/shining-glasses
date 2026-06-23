package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
class FieldReaderListFuncImpl<T, V> extends FieldReaderList<T, V> {
    final ObjectReader<V> itemObjectReader;
    final Supplier<List<V>> listCreator;

    public FieldReaderListFuncImpl(Supplier<List<V>> supplier, ObjectReader<V> objectReader, BiConsumer<T, List<V>> biConsumer, Type type, String str) {
        super(str, List.class, List.class, type, TypeUtils.getClass(type), 0, 0L, null, null, null, null, null, null, biConsumer);
        this.listCreator = supplier;
        this.itemObjectReader = objectReader;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderList
    public Collection<V> createList(JSONReader.Context context) {
        return this.listCreator.get();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderObject, com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        this.function.accept(t, obj);
    }
}
