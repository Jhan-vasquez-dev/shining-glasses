package com.alibaba.fastjson2.support;

import com.alibaba.fastjson2.modules.ObjectReaderModule;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.reader.ObjectReaders;
import com.alibaba.fastjson2.util.Fnv;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class AwtRederModule implements ObjectReaderModule {
    static final long HASH_X = Fnv.hashCode64("x");
    static final long HASH_Y = Fnv.hashCode64("y");
    static final long HASH_NAME = Fnv.hashCode64("name");
    static final long HASH_SIZE = Fnv.hashCode64("size");
    static final long HASH_STYLE = Fnv.hashCode64("style");
    public static AwtRederModule INSTANCE = new AwtRederModule();

    @Override // com.alibaba.fastjson2.modules.ObjectReaderModule
    public ObjectReader getObjectReader(ObjectReaderProvider objectReaderProvider, Type type) {
        if (type == Color.class) {
            return ObjectReaders.objectReader(new ColorCreator(), ObjectReaders.fieldReader("rgb", Integer.TYPE), ObjectReaders.fieldReader("r", Integer.TYPE), ObjectReaders.fieldReader("g", Integer.TYPE), ObjectReaders.fieldReader("b", Integer.TYPE));
        }
        if (type == Point.class) {
            return ObjectReaders.objectReader(new Function() { // from class: com.alibaba.fastjson2.support.AwtRederModule$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return AwtRederModule.lambda$getObjectReader$0((Map) obj);
                }
            }, ObjectReaders.fieldReader("x", Integer.TYPE), ObjectReaders.fieldReader("y", Integer.TYPE));
        }
        if (type == Font.class) {
            return ObjectReaders.objectReader(new Function() { // from class: com.alibaba.fastjson2.support.AwtRederModule$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return AwtRederModule.lambda$getObjectReader$1((Map) obj);
                }
            }, ObjectReaders.fieldReader("name", String.class), ObjectReaders.fieldReader("style", Integer.TYPE), ObjectReaders.fieldReader("size", Integer.TYPE));
        }
        return null;
    }

    static /* synthetic */ Point lambda$getObjectReader$0(Map map) {
        return new Point(((Integer) map.get(Long.valueOf(HASH_X))).intValue(), ((Integer) map.get(Long.valueOf(HASH_Y))).intValue());
    }

    static /* synthetic */ Font lambda$getObjectReader$1(Map map) {
        return new Font((String) map.get(Long.valueOf(HASH_NAME)), ((Integer) map.get(Long.valueOf(HASH_STYLE))).intValue(), ((Integer) map.get(Long.valueOf(HASH_SIZE))).intValue());
    }

    static class ColorCreator implements Function<Map<Long, Object>, Color> {
        static final long HASH_RGB = Fnv.hashCode64("rgb");
        static final long HASH_R = Fnv.hashCode64("r");
        static final long HASH_G = Fnv.hashCode64("g");
        static final long HASH_B = Fnv.hashCode64("b");

        ColorCreator() {
        }

        @Override // java.util.function.Function
        public Color apply(Map<Long, Object> map) {
            Integer num = (Integer) map.get(Long.valueOf(HASH_RGB));
            if (num != null) {
                return new Color(num.intValue());
            }
            return new Color(((Integer) map.get(Long.valueOf(HASH_R))).intValue(), ((Integer) map.get(Long.valueOf(HASH_G))).intValue(), ((Integer) map.get(Long.valueOf(HASH_B))).intValue());
        }
    }
}
