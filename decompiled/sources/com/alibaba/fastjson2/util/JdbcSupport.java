package com.alibaba.fastjson2.util;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.codec.DateTimeCodec;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderImplDate;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import kotlin.time.DurationKt;

/* JADX INFO: loaded from: classes.dex */
public class JdbcSupport {
    static Class CLASS_CLOB;
    static volatile boolean CLASS_CLOB_ERROR;
    static Class CLASS_STRUCT;
    static volatile boolean CLASS_STRUCT_ERROR;

    public static ObjectReader createTimeReader(Class cls, String str, Locale locale) {
        return new TimeReader(str, locale);
    }

    public static ObjectReader createTimestampReader(Class cls, String str, Locale locale) {
        return new TimestampReader(str, locale);
    }

    public static ObjectReader createDateReader(Class cls, String str, Locale locale) {
        return new DateReader(str, locale);
    }

    public static ObjectWriter createTimeWriter(String str) {
        if (str == null) {
            return TimeWriter.INSTANCE;
        }
        return new TimeWriter(str);
    }

    public static Object createTimestamp(long j) {
        return new Timestamp(j);
    }

    public static Object createDate(long j) {
        return new Date(j);
    }

    public static Object createTime(long j) {
        return new Time(j);
    }

    public static ObjectWriter createClobWriter(Class cls) {
        return new ClobWriter(cls);
    }

    public static ObjectWriter createTimestampWriter(Class cls, String str) {
        return new TimestampWriter(str);
    }

    public static boolean isClob(Class cls) {
        if (CLASS_CLOB == null && !CLASS_CLOB_ERROR) {
            try {
                CLASS_CLOB = Class.forName("java.sql.Clob");
            } catch (Throwable unused) {
                CLASS_CLOB_ERROR = true;
            }
        }
        Class cls2 = CLASS_CLOB;
        return cls2 != null && cls2.isAssignableFrom(cls);
    }

    static class ClobWriter implements ObjectWriter {
        final Class objectClass;

        public ClobWriter(Class cls) {
            if (JdbcSupport.CLASS_CLOB == null && !JdbcSupport.CLASS_CLOB_ERROR) {
                try {
                    JdbcSupport.CLASS_CLOB = Class.forName("java.sql.Clob");
                } catch (Throwable unused) {
                    JdbcSupport.CLASS_CLOB_ERROR = true;
                }
            }
            if (JdbcSupport.CLASS_CLOB == null) {
                throw new JSONException("class java.sql.Clob not found");
            }
            this.objectClass = cls;
        }

        @Override // com.alibaba.fastjson2.writer.ObjectWriter
        public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
            try {
                jSONWriter.writeString(((Clob) obj).getCharacterStream());
            } catch (SQLException e) {
                throw new JSONException("Clob.getCharacterStream error", e);
            }
        }
    }

    static class TimeReader extends ObjectReaderImplDate {
        public TimeReader(String str, Locale locale) {
            super(str, locale);
        }

        @Override // com.alibaba.fastjson2.reader.ObjectReaderImplDate, com.alibaba.fastjson2.reader.ObjectReader
        public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
            return readObject(jSONReader, type, obj, j);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r9v7, types: [java.time.ZonedDateTime] */
        @Override // com.alibaba.fastjson2.reader.ObjectReaderImplDate, com.alibaba.fastjson2.reader.ObjectReader
        public Object readObject(JSONReader jSONReader, Type type, Object obj, long j) {
            long epochMilli;
            ZonedDateTime zonedDateTime;
            LocalDateTime localDateTimeOf;
            if (jSONReader.isInt()) {
                long int64Value = jSONReader.readInt64Value();
                if (this.formatUnixTime) {
                    int64Value *= 1000;
                }
                return new Time(int64Value);
            }
            if (jSONReader.readIfNull()) {
                return null;
            }
            if (this.formatISO8601 || this.formatMillis) {
                return new Time(jSONReader.readMillisFromString());
            }
            if (this.formatUnixTime) {
                return new Time(jSONReader.readInt64().longValue() * 1000);
            }
            if (this.format != null) {
                DateTimeFormatter dateFormatter = getDateFormatter(jSONReader.getLocale());
                if (dateFormatter != null) {
                    String string = jSONReader.readString();
                    if (string.isEmpty()) {
                        return null;
                    }
                    if (!this.formatHasHour) {
                        localDateTimeOf = LocalDateTime.of(LocalDate.parse(string, dateFormatter), LocalTime.MIN);
                    } else if (!this.formatHasDay) {
                        localDateTimeOf = LocalDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.parse(string, dateFormatter));
                    } else {
                        localDateTimeOf = LocalDateTime.parse(string, dateFormatter);
                    }
                    zonedDateTime = localDateTimeOf.atZone(jSONReader.getContext().getZoneId());
                } else {
                    zonedDateTime = jSONReader.readZonedDateTime();
                }
                epochMilli = zonedDateTime.toInstant().toEpochMilli();
            } else {
                String string2 = jSONReader.readString();
                if ("0000-00-00".equals(string2) || "0000-00-00 00:00:00".equals(string2)) {
                    epochMilli = 0;
                } else if (string2.length() == 9 && string2.charAt(8) == 'Z') {
                    epochMilli = LocalDateTime.of(DateUtils.LOCAL_DATE_19700101, DateUtils.parseLocalTime(string2.charAt(0), string2.charAt(1), string2.charAt(2), string2.charAt(3), string2.charAt(4), string2.charAt(5), string2.charAt(6), string2.charAt(7))).atZone(DateUtils.DEFAULT_ZONE_ID).toInstant().toEpochMilli();
                } else {
                    if (string2.isEmpty() || "null".equals(string2)) {
                        return null;
                    }
                    return Time.valueOf(string2);
                }
            }
            return new Time(epochMilli);
        }
    }

    static class TimeWriter extends DateTimeCodec implements ObjectWriter {
        public static final TimeWriter INSTANCE = new TimeWriter(null);

        public TimeWriter(String str) {
            super(str);
        }

        @Override // com.alibaba.fastjson2.writer.ObjectWriter
        public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
            String dateFormat;
            if (obj == null) {
                jSONWriter.writeNull();
                return;
            }
            JSONWriter.Context context = jSONWriter.context;
            if (this.formatUnixTime || context.isDateFormatUnixTime()) {
                jSONWriter.writeInt64(((java.util.Date) obj).getTime() / 1000);
                return;
            }
            if (this.formatMillis || context.isDateFormatMillis()) {
                jSONWriter.writeInt64(((java.util.Date) obj).getTime());
                return;
            }
            if (this.formatISO8601 || context.isDateFormatISO8601()) {
                ZonedDateTime zonedDateTimeOfInstant = ZonedDateTime.ofInstant(Instant.ofEpochMilli(((java.util.Date) obj).getTime()), context.getZoneId());
                jSONWriter.writeDateTimeISO8601(zonedDateTimeOfInstant.getYear(), zonedDateTimeOfInstant.getMonthValue(), zonedDateTimeOfInstant.getDayOfMonth(), zonedDateTimeOfInstant.getHour(), zonedDateTimeOfInstant.getMinute(), zonedDateTimeOfInstant.getSecond(), 0, zonedDateTimeOfInstant.getOffset().getTotalSeconds(), true);
                return;
            }
            DateTimeFormatter dateFormatter = (this.format == null || this.format.contains("dd")) ? null : getDateFormatter();
            if (dateFormatter == null && (dateFormat = context.getDateFormat()) != null && !dateFormat.contains("dd")) {
                dateFormatter = context.getDateFormatter();
            }
            if (dateFormatter == null) {
                jSONWriter.writeString(obj.toString());
            } else {
                jSONWriter.writeString(dateFormatter.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(((java.util.Date) obj).getTime()), context.getZoneId())));
            }
        }
    }

    static class TimestampWriter extends DateTimeCodec implements ObjectWriter {
        public TimestampWriter(String str) {
            super(str);
        }

        @Override // com.alibaba.fastjson2.writer.ObjectWriter
        public void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
            if (obj == null) {
                jSONWriter.writeNull();
                return;
            }
            Timestamp timestamp = (Timestamp) obj;
            if (this.format != null) {
                write(jSONWriter, obj, obj2, type, j);
            } else {
                jSONWriter.writeLocalDateTime(timestamp.toLocalDateTime());
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r13v8, types: [java.time.LocalDateTime] */
        @Override // com.alibaba.fastjson2.writer.ObjectWriter
        public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
            if (obj == null) {
                jSONWriter.writeNull();
                return;
            }
            JSONWriter.Context context = jSONWriter.context;
            Timestamp timestamp = (Timestamp) obj;
            if (this.formatUnixTime || context.isDateFormatUnixTime()) {
                jSONWriter.writeInt64(timestamp.getTime() / 1000);
                return;
            }
            ZonedDateTime zonedDateTimeOfInstant = ZonedDateTime.ofInstant(timestamp.toInstant(), context.getZoneId());
            int totalSeconds = zonedDateTimeOfInstant.getOffset().getTotalSeconds();
            if ((this.formatISO8601 || context.isDateFormatISO8601()) && zonedDateTimeOfInstant.getNano() % DurationKt.NANOS_IN_MILLIS == 0) {
                jSONWriter.writeDateTimeISO8601(zonedDateTimeOfInstant.getYear(), zonedDateTimeOfInstant.getMonthValue(), zonedDateTimeOfInstant.getDayOfMonth(), zonedDateTimeOfInstant.getHour(), zonedDateTimeOfInstant.getMinute(), zonedDateTimeOfInstant.getSecond(), zonedDateTimeOfInstant.getNano() / DurationKt.NANOS_IN_MILLIS, totalSeconds, true);
                return;
            }
            DateTimeFormatter dateFormatter = getDateFormatter();
            if (dateFormatter == null) {
                dateFormatter = context.getDateFormatter();
            }
            if (dateFormatter == null) {
                if (this.formatMillis || context.isDateFormatMillis()) {
                    jSONWriter.writeInt64(timestamp.getTime());
                    return;
                }
                int nanos = timestamp.getNanos();
                int year = zonedDateTimeOfInstant.getYear();
                int monthValue = zonedDateTimeOfInstant.getMonthValue();
                int dayOfMonth = zonedDateTimeOfInstant.getDayOfMonth();
                int hour = zonedDateTimeOfInstant.getHour();
                int minute = zonedDateTimeOfInstant.getMinute();
                int second = zonedDateTimeOfInstant.getSecond();
                if (nanos % DurationKt.NANOS_IN_MILLIS == 0) {
                    jSONWriter.writeDateTimeISO8601(year, monthValue, dayOfMonth, hour, minute, second, nanos / DurationKt.NANOS_IN_MILLIS, totalSeconds, false);
                    return;
                } else {
                    jSONWriter.writeLocalDateTime(zonedDateTimeOfInstant.toLocalDateTime());
                    return;
                }
            }
            jSONWriter.writeString(dateFormatter.format(zonedDateTimeOfInstant));
        }
    }

    static class TimestampReader extends ObjectReaderImplDate {
        public TimestampReader(String str, Locale locale) {
            super(str, locale);
        }

        /* JADX WARN: Type inference failed for: r7v4, types: [java.time.ZonedDateTime] */
        @Override // com.alibaba.fastjson2.reader.ObjectReaderImplDate, com.alibaba.fastjson2.reader.ObjectReader
        public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
            if (jSONReader.isInt()) {
                long int64Value = jSONReader.readInt64Value();
                if (this.formatUnixTime) {
                    int64Value *= 1000;
                }
                return createTimestamp(int64Value, 0);
            }
            if (jSONReader.readIfNull()) {
                return null;
            }
            if (jSONReader.getType() == -88) {
                Instant instant = jSONReader.readLocalDateTime().atZone(jSONReader.getContext().getZoneId()).toInstant();
                return createTimestamp(instant.toEpochMilli(), instant.getNano());
            }
            return readObject(jSONReader, type, obj, j);
        }

        Object createTimestamp(long j, int i) {
            Timestamp timestamp = new Timestamp(j);
            if (i != 0) {
                timestamp.setNanos(i);
            }
            return timestamp;
        }

        /* JADX WARN: Type inference failed for: r3v13, types: [java.time.ZonedDateTime] */
        /* JADX WARN: Type inference failed for: r3v6, types: [java.time.ZonedDateTime] */
        @Override // com.alibaba.fastjson2.reader.ObjectReaderImplDate, com.alibaba.fastjson2.reader.ObjectReader
        public Object readObject(JSONReader jSONReader, Type type, Object obj, long j) {
            Instant instant;
            if (jSONReader.isInt()) {
                long int64Value = jSONReader.readInt64Value();
                if (this.formatUnixTime) {
                    int64Value *= 1000;
                }
                return createTimestamp(int64Value, 0);
            }
            if (jSONReader.nextIfNullOrEmptyString()) {
                return null;
            }
            if (this.format == null || this.formatISO8601 || this.formatMillis) {
                LocalDateTime localDateTime = jSONReader.readLocalDateTime();
                if (localDateTime != null) {
                    return Timestamp.valueOf(localDateTime);
                }
                if (jSONReader.wasNull()) {
                    return null;
                }
                long millisFromString = jSONReader.readMillisFromString();
                if (millisFromString == 0 && jSONReader.wasNull()) {
                    return null;
                }
                return new Timestamp(millisFromString);
            }
            String string = jSONReader.readString();
            if (string.isEmpty()) {
                return null;
            }
            DateTimeFormatter dateFormatter = getDateFormatter();
            if (!this.formatHasHour) {
                instant = LocalDateTime.of(LocalDate.parse(string, dateFormatter), LocalTime.MIN).atZone(jSONReader.getContext().getZoneId()).toInstant();
            } else {
                instant = LocalDateTime.parse(string, dateFormatter).atZone(jSONReader.getContext().getZoneId()).toInstant();
            }
            return createTimestamp(instant.toEpochMilli(), instant.getNano());
        }
    }

    static class DateReader extends ObjectReaderImplDate {
        public DateReader(String str, Locale locale) {
            super(str, locale);
        }

        @Override // com.alibaba.fastjson2.reader.ObjectReaderImplDate, com.alibaba.fastjson2.reader.ObjectReader
        public Object readJSONBObject(JSONReader jSONReader, Type type, Object obj, long j) {
            return readObject(jSONReader, type, obj, j);
        }

        /* JADX WARN: Type inference failed for: r3v12, types: [java.time.ZonedDateTime] */
        /* JADX WARN: Type inference failed for: r3v7, types: [java.time.ZonedDateTime] */
        @Override // com.alibaba.fastjson2.reader.ObjectReaderImplDate, com.alibaba.fastjson2.reader.ObjectReader
        public Object readObject(JSONReader jSONReader, Type type, Object obj, long j) {
            Instant instant;
            if (jSONReader.isInt()) {
                long int64Value = jSONReader.readInt64Value();
                if (this.formatUnixTime) {
                    int64Value *= 1000;
                }
                return new Date(int64Value);
            }
            if (jSONReader.readIfNull()) {
                return null;
            }
            if (this.formatUnixTime && jSONReader.isString()) {
                return new Date(Long.parseLong(jSONReader.readString()) * 1000);
            }
            if (this.format == null || this.formatISO8601 || this.formatMillis) {
                LocalDateTime localDateTime = jSONReader.readLocalDateTime();
                if (localDateTime != null) {
                    return Date.valueOf(localDateTime.toLocalDate());
                }
                if (jSONReader.wasNull()) {
                    return null;
                }
                long millisFromString = jSONReader.readMillisFromString();
                if (millisFromString == 0 && jSONReader.wasNull()) {
                    return null;
                }
                return new Date(millisFromString);
            }
            String string = jSONReader.readString();
            if (string.isEmpty()) {
                return null;
            }
            DateTimeFormatter dateFormatter = getDateFormatter();
            if (!this.formatHasHour) {
                instant = LocalDateTime.of(LocalDate.parse(string, dateFormatter), LocalTime.MIN).atZone(jSONReader.getContext().getZoneId()).toInstant();
            } else {
                instant = LocalDateTime.parse(string, dateFormatter).atZone(jSONReader.getContext().getZoneId()).toInstant();
            }
            return new Date(instant.toEpochMilli());
        }
    }

    public static boolean isStruct(Class cls) {
        if (CLASS_STRUCT == null && !CLASS_STRUCT_ERROR) {
            try {
                CLASS_STRUCT = Class.forName("java.sql.Struct");
            } catch (Throwable unused) {
                CLASS_STRUCT_ERROR = true;
            }
        }
        Class cls2 = CLASS_STRUCT;
        return cls2 != null && cls2.isAssignableFrom(cls);
    }
}
