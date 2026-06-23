package com.alibaba.fastjson.serializer;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v2 com.alibaba.fastjson.serializer.SerializerFeature, still in use, count: 1, list:
  (r0v2 com.alibaba.fastjson.serializer.SerializerFeature) from 0x014c: INVOKE (r0v2 com.alibaba.fastjson.serializer.SerializerFeature) VIRTUAL call: com.alibaba.fastjson.serializer.SerializerFeature.getMask():int A[MD:():int (m), WRAPPED] (LINE:159)
	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:99)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:98)
	at jadx.core.utils.InsnRemover.removeAllAndUnbind(InsnRemover.java:252)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:180)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX INFO: loaded from: classes.dex */
public final class SerializerFeature {
    QuoteFieldNames,
    UseSingleQuotes,
    WriteMapNullValue,
    WriteEnumUsingToString,
    WriteEnumUsingName,
    UseISO8601DateFormat,
    WriteNullListAsEmpty,
    WriteNullStringAsEmpty,
    WriteNullNumberAsZero,
    WriteNullBooleanAsFalse,
    SkipTransientField,
    SortField,
    WriteTabAsSpecial,
    PrettyFormat,
    WriteClassName,
    DisableCircularReferenceDetect,
    WriteSlashAsSpecial,
    BrowserCompatible,
    WriteDateUseDateFormat,
    NotWriteRootClassName,
    DisableCheckSpecialChar,
    BeanToArray,
    WriteNonStringKeyAsString,
    NotWriteDefaultValue,
    BrowserSecure,
    IgnoreNonFieldGetter,
    WriteNonStringValueAsString,
    IgnoreErrorGetter,
    WriteBigDecimalAsPlain,
    MapSortField;

    public static final int WRITE_MAP_NULL_FEATURES;
    public final int mask = 1 << ordinal();
    public static final SerializerFeature[] EMPTY = new SerializerFeature[0];

    public static SerializerFeature valueOf(String str) {
        return (SerializerFeature) Enum.valueOf(SerializerFeature.class, str);
    }

    public static SerializerFeature[] values() {
        return (SerializerFeature[]) $VALUES.clone();
    }

    static {
        WRITE_MAP_NULL_FEATURES = new SerializerFeature().getMask() | serializerFeature.getMask() | new SerializerFeature().getMask() | serializerFeature.getMask() | new SerializerFeature().getMask();
    }

    private SerializerFeature() {
    }

    public static boolean isEnabled(int i, SerializerFeature serializerFeature) {
        return (i & serializerFeature.mask) != 0;
    }

    public static boolean isEnabled(int i, int i2, SerializerFeature serializerFeature) {
        return ((i | i2) & serializerFeature.mask) != 0;
    }

    public static int config(int i, SerializerFeature serializerFeature, boolean z) {
        if (z) {
            return i | serializerFeature.mask;
        }
        return i & (~serializerFeature.mask);
    }

    public static int of(SerializerFeature[] serializerFeatureArr) {
        if (serializerFeatureArr == null) {
            return 0;
        }
        int i = 0;
        for (SerializerFeature serializerFeature : serializerFeatureArr) {
            i |= serializerFeature.mask;
        }
        return i;
    }

    public final int getMask() {
        return this.mask;
    }
}
