package com.alibaba.fastjson2.support;

import com.alibaba.fastjson2.modules.ObjectWriterModule;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriters;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/* JADX INFO: loaded from: classes.dex */
public class AwtWriterModule implements ObjectWriterModule {
    public static AwtWriterModule INSTANCE = new AwtWriterModule();

    @Override // com.alibaba.fastjson2.modules.ObjectWriterModule
    public ObjectWriter getObjectWriter(Type type, Class cls) {
        if (type == Color.class) {
            return ObjectWriters.objectWriter(Color.class, ObjectWriters.fieldWriter("r", new ToIntFunction() { // from class: com.alibaba.fastjson2.support.AwtWriterModule$$ExternalSyntheticLambda0
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Color) obj).getRed();
                }
            }), ObjectWriters.fieldWriter("g", new ToIntFunction() { // from class: com.alibaba.fastjson2.support.AwtWriterModule$$ExternalSyntheticLambda1
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Color) obj).getGreen();
                }
            }), ObjectWriters.fieldWriter("b", new ToIntFunction() { // from class: com.alibaba.fastjson2.support.AwtWriterModule$$ExternalSyntheticLambda2
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Color) obj).getBlue();
                }
            }), ObjectWriters.fieldWriter("alpha", new ToIntFunction() { // from class: com.alibaba.fastjson2.support.AwtWriterModule$$ExternalSyntheticLambda3
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Color) obj).getAlpha();
                }
            }));
        }
        if (type == Point.class) {
            return ObjectWriters.objectWriter(Point.class, ObjectWriters.fieldWriter("x", new ToIntFunction() { // from class: com.alibaba.fastjson2.support.AwtWriterModule$$ExternalSyntheticLambda4
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Point) obj).x;
                }
            }), ObjectWriters.fieldWriter("y", new ToIntFunction() { // from class: com.alibaba.fastjson2.support.AwtWriterModule$$ExternalSyntheticLambda5
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Point) obj).y;
                }
            }));
        }
        if (type == Font.class) {
            return ObjectWriters.objectWriter(Font.class, ObjectWriters.fieldWriter("name", new Function() { // from class: com.alibaba.fastjson2.support.AwtWriterModule$$ExternalSyntheticLambda6
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((Font) obj).getName();
                }
            }), ObjectWriters.fieldWriter("style", new ToIntFunction() { // from class: com.alibaba.fastjson2.support.AwtWriterModule$$ExternalSyntheticLambda7
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Font) obj).getStyle();
                }
            }), ObjectWriters.fieldWriter("size", new ToIntFunction() { // from class: com.alibaba.fastjson2.support.AwtWriterModule$$ExternalSyntheticLambda8
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Font) obj).getSize();
                }
            }));
        }
        return null;
    }
}
