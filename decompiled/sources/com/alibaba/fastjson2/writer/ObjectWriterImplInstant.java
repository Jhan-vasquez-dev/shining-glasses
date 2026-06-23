package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.codec.DateTimeCodec;
import com.alibaba.fastjson2.internal.asm.Opcodes;
import com.alibaba.fastjson2.util.DateUtils;
import java.lang.reflect.Type;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import kotlin.time.DurationKt;

/* JADX INFO: loaded from: classes.dex */
final class ObjectWriterImplInstant extends DateTimeCodec implements ObjectWriter {
    static final ObjectWriterImplInstant INSTANCE = new ObjectWriterImplInstant(null, null);

    public ObjectWriterImplInstant(String str, Locale locale) {
        super(str, locale);
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        jSONWriter.writeInstant((Instant) obj);
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        String dateFormat;
        int shanghaiZoneOffsetTotalSeconds;
        long j2;
        if (obj == null) {
            jSONWriter.writeNull();
            return;
        }
        JSONWriter.Context context = jSONWriter.context;
        if (this.format != null) {
            dateFormat = this.format;
        } else {
            dateFormat = context.getDateFormat();
        }
        Instant instant = (Instant) obj;
        if (dateFormat == null) {
            jSONWriter.writeInstant(instant);
            return;
        }
        boolean z = this.yyyyMMddhhmmss19 || (context.isFormatyyyyMMddhhmmss19() && this.format == null);
        if (this.yyyyMMddhhmmss14 || z || this.yyyyMMdd8 || this.yyyyMMdd10) {
            ZoneId zoneId = context.getZoneId();
            long epochSecond = instant.getEpochSecond();
            if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
                shanghaiZoneOffsetTotalSeconds = DateUtils.getShanghaiZoneOffsetTotalSeconds(epochSecond);
            } else {
                shanghaiZoneOffsetTotalSeconds = zoneId.getRules().getOffset(instant).getTotalSeconds();
            }
            long j3 = epochSecond + ((long) shanghaiZoneOffsetTotalSeconds);
            long jFloorDiv = Math.floorDiv(j3, 86400L);
            int iFloorMod = (int) Math.floorMod(j3, 86400L);
            long j4 = 719468 + jFloorDiv;
            if (j4 < 0) {
                long j5 = ((jFloorDiv + 719469) / 146097) - 1;
                j2 = j5 * 400;
                j4 += (-j5) * 146097;
            } else {
                j2 = 0;
            }
            long j6 = ((j4 * 400) + 591) / 146097;
            long j7 = j4 - ((((j6 * 365) + (j6 / 4)) - (j6 / 100)) + (j6 / 400));
            if (j7 < 0) {
                j6--;
                j7 = j4 - ((((365 * j6) + (j6 / 4)) - (j6 / 100)) + (j6 / 400));
            }
            int i = (int) j7;
            int i2 = ((i * 5) + 2) / Opcodes.IFEQ;
            int i3 = ((i2 + 2) % 12) + 1;
            int i4 = (i - (((i2 * 306) + 5) / 10)) + 1;
            long j8 = j6 + j2 + ((long) (i2 / 10));
            if (j8 < -999999999 || j8 > 999999999) {
                throw new DateTimeException("Invalid year " + j8);
            }
            int i5 = (int) j8;
            long j9 = iFloorMod;
            if (j9 < 0 || j9 > 86399) {
                throw new DateTimeException("Invalid secondOfDay " + j9);
            }
            int i6 = (int) (j9 / 3600);
            long j10 = j9 - ((long) (i6 * 3600));
            int i7 = (int) (j10 / 60);
            int i8 = (int) (j10 - ((long) (i7 * 60)));
            if (z) {
                jSONWriter.writeDateTime19(i5, i3, i4, i6, i7, i8);
                return;
            }
            if (this.yyyyMMddhhmmss14) {
                jSONWriter.writeDateTime14(i5, i3, i4, i6, i7, i8);
                return;
            } else if (this.yyyyMMdd10) {
                jSONWriter.writeDateYYYMMDD10(i5, i3, i4);
                return;
            } else {
                jSONWriter.writeDateYYYMMDD8(i5, i3, i4);
                return;
            }
        }
        ZonedDateTime zonedDateTimeOfInstant = ZonedDateTime.ofInstant(instant, context.getZoneId());
        if (this.formatUnixTime || (this.format == null && context.isDateFormatUnixTime())) {
            jSONWriter.writeInt64(zonedDateTimeOfInstant.toInstant().toEpochMilli() / 1000);
            return;
        }
        if (this.formatMillis || (this.format == null && context.isDateFormatMillis())) {
            jSONWriter.writeInt64(zonedDateTimeOfInstant.toInstant().toEpochMilli());
            return;
        }
        int year = zonedDateTimeOfInstant.getYear();
        if (year >= 0 && year <= 9999 && (this.formatISO8601 || (this.format == null && context.isDateFormatISO8601()))) {
            jSONWriter.writeDateTimeISO8601(year, zonedDateTimeOfInstant.getMonthValue(), zonedDateTimeOfInstant.getDayOfMonth(), zonedDateTimeOfInstant.getHour(), zonedDateTimeOfInstant.getMinute(), zonedDateTimeOfInstant.getSecond(), zonedDateTimeOfInstant.getNano() / DurationKt.NANOS_IN_MILLIS, zonedDateTimeOfInstant.getOffset().getTotalSeconds(), true);
            return;
        }
        DateTimeFormatter dateFormatter = getDateFormatter();
        if (dateFormatter == null) {
            dateFormatter = context.getDateFormatter();
        }
        if (dateFormatter == null) {
            jSONWriter.writeZonedDateTime(zonedDateTimeOfInstant);
        } else {
            jSONWriter.writeString(dateFormatter.format(zonedDateTimeOfInstant));
        }
    }
}
