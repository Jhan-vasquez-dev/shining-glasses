package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.IOUtils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
abstract class FieldReaderDateTimeCodec<T> extends FieldReader<T> {
    final ObjectReader dateReader;
    final boolean formatMillis;
    final boolean formatUnixTime;

    protected abstract void accept(T t, Instant instant);

    protected abstract void accept(T t, LocalDateTime localDateTime);

    protected abstract void accept(T t, ZonedDateTime zonedDateTime);

    protected abstract void accept(T t, Date date);

    protected abstract void acceptNull(T t);

    protected abstract Object apply(long j);

    protected abstract Object apply(Instant instant);

    protected abstract Object apply(LocalDateTime localDateTime);

    protected abstract Object apply(ZonedDateTime zonedDateTime);

    protected abstract Object apply(Date date);

    /* JADX WARN: Removed duplicated region for block: B:10:0x0022  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public FieldReaderDateTimeCodec(java.lang.String r2, java.lang.reflect.Type r3, java.lang.Class r4, int r5, long r6, java.lang.String r8, java.util.Locale r9, java.lang.Object r10, com.alibaba.fastjson2.schema.JSONSchema r11, java.lang.reflect.Method r12, java.lang.reflect.Field r13, com.alibaba.fastjson2.reader.ObjectReader r14) {
        /*
            r1 = this;
            r1.<init>(r2, r3, r4, r5, r6, r8, r9, r10, r11, r12, r13)
            r2 = r1
            r2.dateReader = r14
            r3 = 0
            if (r8 == 0) goto L22
            r8.hashCode()
            java.lang.String r4 = "millis"
            boolean r4 = r8.equals(r4)
            r5 = 1
            if (r4 != 0) goto L23
            java.lang.String r4 = "unixtime"
            boolean r4 = r8.equals(r4)
            if (r4 != 0) goto L1e
            goto L22
        L1e:
            r0 = r5
            r5 = r3
            r3 = r0
            goto L23
        L22:
            r5 = r3
        L23:
            r2.formatUnixTime = r3
            r2.formatMillis = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.reader.FieldReaderDateTimeCodec.<init>(java.lang.String, java.lang.reflect.Type, java.lang.Class, int, long, java.lang.String, java.util.Locale, java.lang.Object, com.alibaba.fastjson2.schema.JSONSchema, java.lang.reflect.Method, java.lang.reflect.Field, com.alibaba.fastjson2.reader.ObjectReader):void");
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public final Object readFieldValue(JSONReader jSONReader) {
        return this.dateReader.readObject(jSONReader, this.fieldType, this.fieldName, this.features);
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public final ObjectReader getObjectReader(JSONReader jSONReader) {
        return this.dateReader;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public final ObjectReader getObjectReader(JSONReader.Context context) {
        return this.dateReader;
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public void accept(T t, Object obj) {
        if (obj == null) {
            acceptNull(t);
            return;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || "null".equals(str)) {
                acceptNull(t);
                return;
            }
            if ((this.format == null || this.formatUnixTime || this.formatMillis) && IOUtils.isNumber(str)) {
                long j = Long.parseLong(str);
                if (this.formatUnixTime) {
                    j *= 1000;
                }
                accept((Object) t, j);
                return;
            }
            obj = DateUtils.parseDate(str, this.format, DateUtils.DEFAULT_ZONE_ID);
        }
        if (obj instanceof Date) {
            accept((Object) t, (Date) obj);
            return;
        }
        if (obj instanceof Instant) {
            accept((Object) t, (Instant) obj);
            return;
        }
        if (obj instanceof Long) {
            accept((Object) t, ((Long) obj).longValue());
        } else if (obj instanceof LocalDateTime) {
            accept((Object) t, (LocalDateTime) obj);
        } else {
            if (obj instanceof ZonedDateTime) {
                accept((Object) t, (ZonedDateTime) obj);
                return;
            }
            throw new JSONException("not support value " + obj.getClass());
        }
    }

    @Override // com.alibaba.fastjson2.reader.FieldReader
    public boolean supportAcceptType(Class cls) {
        return cls == Date.class || cls == String.class;
    }
}
