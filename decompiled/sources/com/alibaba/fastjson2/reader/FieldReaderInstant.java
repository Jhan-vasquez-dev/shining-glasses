package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
public final class FieldReaderInstant<T> extends FieldReaderDateTimeCodec<T> {
    final BiConsumer<T, Instant> function;

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected Object apply(Instant instant) {
        return instant;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ void accept(Object obj, Object obj2) {
        super.accept(obj, obj2);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec, com.alibaba.fastjson2.reader.FieldReader
    public /* bridge */ /* synthetic */ boolean supportAcceptType(Class cls) {
        return super.supportAcceptType(cls);
    }

    FieldReaderInstant(String str, Type type, Class cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Field field, Method method, BiConsumer<T, Instant> biConsumer) {
        super(str, type, cls, i, j, str2, locale, obj, jSONSchema, method, field, ObjectReaderImplInstant.of(str2, locale));
        this.function = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValue(JSONReader jSONReader, T t) {
        accept((Object) t, (Instant) this.dateReader.readObject(jSONReader, this.fieldType, this.fieldName, this.features));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void readFieldValueJSONB(JSONReader jSONReader, T t) {
        accept((Object) t, jSONReader.readInstant());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected void accept(T t, LocalDateTime localDateTime) {
        accept((Object) t, localDateTime.toInstant(DateUtils.DEFAULT_ZONE_ID.getRules().getOffset(localDateTime)));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected void accept(T t, Date date) {
        accept((Object) t, date.toInstant());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected void accept(T t, ZonedDateTime zonedDateTime) {
        accept((Object) t, zonedDateTime.toInstant());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected Object apply(Date date) {
        return date.toInstant();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected Object apply(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant();
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected Object apply(LocalDateTime localDateTime) {
        return localDateTime.toInstant(DateUtils.DEFAULT_ZONE_ID.getRules().getOffset(localDateTime));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected Object apply(long j) {
        return Instant.ofEpochMilli(j);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected void acceptNull(T t) {
        accept((Object) t, (Instant) null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        accept((Object) t, Instant.ofEpochMilli(j));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected void accept(T t, Instant instant) {
        if (this.schema != null) {
            this.schema.assertValidate(instant);
        }
        if (t == null) {
            throw new JSONException("set " + this.fieldName + " error, object is null");
        }
        if (instant != null || (this.features & JSONReader.Feature.IgnoreSetNullValue.mask) == 0) {
            BiConsumer<T, Instant> biConsumer = this.function;
            if (biConsumer != null) {
                biConsumer.accept(t, instant);
                return;
            }
            if (this.method != null) {
                try {
                    this.method.invoke(t, instant);
                } catch (Exception e) {
                    throw new JSONException("set " + this.fieldName + " error", e);
                }
            } else {
                if (this.fieldOffset != -1) {
                    JDKUtils.UNSAFE.putObject(t, this.fieldOffset, instant);
                    return;
                }
                try {
                    this.field.set(t, instant);
                } catch (Exception e2) {
                    throw new JSONException("set " + this.fieldName + " error", e2);
                }
            }
        }
    }
}
