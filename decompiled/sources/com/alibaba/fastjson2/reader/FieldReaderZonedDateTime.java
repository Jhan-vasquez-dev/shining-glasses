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
public class FieldReaderZonedDateTime<T> extends FieldReaderDateTimeCodec<T> {
    final BiConsumer<T, ZonedDateTime> function;

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected Object apply(ZonedDateTime zonedDateTime) {
        return zonedDateTime;
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

    FieldReaderZonedDateTime(String str, Type type, Class cls, int i, long j, String str2, Locale locale, Object obj, JSONSchema jSONSchema, Field field, Method method, BiConsumer<T, ZonedDateTime> biConsumer) {
        super(str, type, cls, i, j, str2, locale, obj, jSONSchema, method, field, ObjectReaderImplZonedDateTime.of(str2, locale));
        this.function = biConsumer;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public final void readFieldValue(JSONReader jSONReader, T t) {
        accept((Object) t, (ZonedDateTime) this.dateReader.readObject(jSONReader, this.fieldType, this.fieldName, this.features));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public final void readFieldValueJSONB(JSONReader jSONReader, T t) {
        accept((Object) t, jSONReader.readZonedDateTime());
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected void accept(T t, Date date) {
        accept((Object) t, ZonedDateTime.ofInstant(date.toInstant(), DateUtils.DEFAULT_ZONE_ID));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected void accept(T t, Instant instant) {
        accept((Object) t, ZonedDateTime.ofInstant(instant, DateUtils.DEFAULT_ZONE_ID));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected void accept(T t, LocalDateTime localDateTime) {
        accept((Object) t, ZonedDateTime.of(localDateTime, DateUtils.DEFAULT_ZONE_ID));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected Object apply(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), DateUtils.DEFAULT_ZONE_ID);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected Object apply(Instant instant) {
        return ZonedDateTime.ofInstant(instant, DateUtils.DEFAULT_ZONE_ID);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected Object apply(long j) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(j), DateUtils.DEFAULT_ZONE_ID);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected Object apply(LocalDateTime localDateTime) {
        return localDateTime.atZone(DateUtils.DEFAULT_ZONE_ID);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected void acceptNull(T t) {
        accept((Object) t, (ZonedDateTime) null);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, long j) {
        accept((Object) t, ZonedDateTime.ofInstant(Instant.ofEpochMilli(j), DateUtils.DEFAULT_ZONE_ID));
    }

    @Override // com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec
    protected void accept(T t, ZonedDateTime zonedDateTime) {
        if (this.schema != null) {
            this.schema.assertValidate(zonedDateTime);
        }
        if (zonedDateTime != null || (this.features & JSONReader.Feature.IgnoreSetNullValue.mask) == 0) {
            if (t == null) {
                throw new JSONException("set " + this.fieldName + " error, object is null");
            }
            BiConsumer<T, ZonedDateTime> biConsumer = this.function;
            if (biConsumer != null) {
                biConsumer.accept(t, zonedDateTime);
                return;
            }
            if (this.method != null) {
                try {
                    this.method.invoke(t, zonedDateTime);
                } catch (Exception e) {
                    throw new JSONException("set " + this.fieldName + " error", e);
                }
            } else {
                if (this.fieldOffset != -1) {
                    JDKUtils.UNSAFE.putObject(t, this.fieldOffset, zonedDateTime);
                    return;
                }
                try {
                    this.field.set(t, zonedDateTime);
                } catch (Exception e2) {
                    throw new JSONException("set " + this.fieldName + " error", e2);
                }
            }
        }
    }
}
