package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.codec.DateTimeCodec;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import kotlin.time.DurationKt;

/* JADX INFO: loaded from: classes.dex */
final class ObjectWriterImplCalendar extends DateTimeCodec implements ObjectWriter {
    static final ObjectWriterImplCalendar INSTANCE = new ObjectWriterImplCalendar(null, null);

    public ObjectWriterImplCalendar(String str, Locale locale) {
        super(str, locale);
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (obj == null) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeMillis(((Calendar) obj).getTimeInMillis());
        }
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        DateTimeFormatter dateFormatter;
        if (obj == null) {
            jSONWriter.writeNull();
            return;
        }
        JSONWriter.Context context = jSONWriter.context;
        long timeInMillis = ((Calendar) obj).getTimeInMillis();
        if (this.formatUnixTime || (this.format == null && context.isDateFormatUnixTime())) {
            jSONWriter.writeInt64(timeInMillis / 1000);
            return;
        }
        if (this.format == null && context.isDateFormatMillis()) {
            jSONWriter.writeInt64(timeInMillis);
            return;
        }
        ZonedDateTime zonedDateTimeOfInstant = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeInMillis), context.getZoneId());
        int totalSeconds = zonedDateTimeOfInstant.getOffset().getTotalSeconds();
        int year = zonedDateTimeOfInstant.getYear();
        if (year >= 0 && year <= 9999) {
            if (this.format == null && context.isDateFormatISO8601()) {
                jSONWriter.writeDateTimeISO8601(year, zonedDateTimeOfInstant.getMonthValue(), zonedDateTimeOfInstant.getDayOfMonth(), zonedDateTimeOfInstant.getHour(), zonedDateTimeOfInstant.getMinute(), zonedDateTimeOfInstant.getSecond(), zonedDateTimeOfInstant.getNano() / DurationKt.NANOS_IN_MILLIS, totalSeconds, true);
                return;
            }
            if ((this.format == null ? context.getDateFormat() : this.format) == null) {
                int monthValue = zonedDateTimeOfInstant.getMonthValue();
                int dayOfMonth = zonedDateTimeOfInstant.getDayOfMonth();
                int hour = zonedDateTimeOfInstant.getHour();
                int minute = zonedDateTimeOfInstant.getMinute();
                int second = zonedDateTimeOfInstant.getSecond();
                int nano = zonedDateTimeOfInstant.getNano();
                if (nano == 0) {
                    jSONWriter.writeDateTime19(year, monthValue, dayOfMonth, hour, minute, second);
                    return;
                } else {
                    jSONWriter.writeDateTimeISO8601(year, monthValue, dayOfMonth, hour, minute, second, nano / DurationKt.NANOS_IN_MILLIS, totalSeconds, false);
                    return;
                }
            }
        }
        if (this.format != null) {
            dateFormatter = getDateFormatter();
        } else {
            dateFormatter = context.getDateFormatter();
        }
        if (dateFormatter == null) {
            jSONWriter.writeZonedDateTime(zonedDateTimeOfInstant);
        } else {
            jSONWriter.writeString(dateFormatter.format(zonedDateTimeOfInstant));
        }
    }
}
