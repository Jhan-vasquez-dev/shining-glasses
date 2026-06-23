package com.alibaba.fastjson2.support.csv;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.internal.asm.Opcodes;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import kotlin.time.DurationKt;

/* JADX INFO: loaded from: classes.dex */
public abstract class CSVWriter implements Closeable, Flushable {
    private long features;
    int off;
    final ZoneId zoneId;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public abstract void close() throws IOException;

    @Override // java.io.Flushable
    public abstract void flush();

    public abstract void writeBoolean(boolean z);

    public abstract void writeComma();

    public abstract void writeDateTime19(int i, int i2, int i3, int i4, int i5, int i6);

    public abstract void writeDateYYYMMDD10(int i, int i2, int i3);

    public abstract void writeDecimal(long j, int i);

    public abstract void writeDecimal(BigDecimal bigDecimal);

    public abstract void writeDouble(double d);

    public abstract void writeFloat(float f);

    public abstract void writeInt32(int i);

    public abstract void writeInt64(long j);

    public abstract void writeLine();

    public abstract void writeLocalDateTime(LocalDateTime localDateTime);

    protected abstract void writeQuote();

    protected abstract void writeRaw(String str);

    public abstract void writeString(String str);

    public abstract void writeString(byte[] bArr);

    CSVWriter(ZoneId zoneId, Feature... featureArr) {
        for (Feature feature : featureArr) {
            this.features |= feature.mask;
        }
        this.zoneId = zoneId;
    }

    public static CSVWriter of() {
        return of(new ByteArrayOutputStream(), new Feature[0]);
    }

    public static CSVWriter of(File file) throws FileNotFoundException {
        return of(new FileOutputStream(file), StandardCharsets.UTF_8);
    }

    public static CSVWriter of(File file, Charset charset) throws FileNotFoundException {
        return of(new FileOutputStream(file), charset);
    }

    public final void writeLineObject(Object obj) {
        if (obj == null) {
            writeLine();
            return;
        }
        ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter((Class) obj.getClass());
        if (objectWriter instanceof ObjectWriterAdapter) {
            List<FieldWriter> fieldWriters = ((ObjectWriterAdapter) objectWriter).getFieldWriters();
            if (fieldWriters.size() == 1 && (fieldWriters.get(0).features & FieldInfo.VALUE_MASK) != 0) {
                writeLineObject(fieldWriters.get(0).getFieldValue(obj));
                return;
            }
            Object[] objArr = new Object[fieldWriters.size()];
            for (int i = 0; i < fieldWriters.size(); i++) {
                objArr[i] = fieldWriters.get(i).getFieldValue(obj);
            }
            writeLine(objArr);
            return;
        }
        writeLine(obj);
    }

    public final void writeDate(Date date) {
        if (date == null) {
            return;
        }
        writeDate(date.getTime());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v2, types: [java.time.LocalDateTime] */
    public final void writeInstant(Instant instant) {
        if (instant == null) {
            return;
        }
        if (instant.getNano() % DurationKt.NANOS_IN_MILLIS == 0) {
            writeDate(instant.toEpochMilli());
            return;
        }
        if ((this.features & Feature.AlwaysQuoteStrings.mask) != 0) {
            writeQuote();
        }
        writeLocalDateTime(instant.atZone(this.zoneId).toLocalDateTime());
    }

    public void writeLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return;
        }
        writeRaw(DateTimeFormatter.ISO_LOCAL_DATE.format(localDate));
    }

    public final void writeLine(int i, IntFunction intFunction) {
        for (int i2 = 0; i2 < i; i2++) {
            Object objApply = intFunction.apply(i2);
            if (i2 != 0) {
                writeComma();
            }
            writeValue(objApply);
        }
        writeLine();
    }

    public final void writeLine(List list) {
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                writeComma();
            }
            writeValue(list.get(i));
        }
        writeLine();
    }

    public final void writeLine(Object... objArr) {
        for (int i = 0; i < objArr.length; i++) {
            if (i != 0) {
                writeComma();
            }
            writeValue(objArr[i]);
        }
        writeLine();
    }

    public void writeValue(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Optional) {
            Optional optional = (Optional) obj;
            if (!optional.isPresent()) {
                return;
            } else {
                obj = optional.get();
            }
        }
        if (obj instanceof Integer) {
            writeInt32(((Integer) obj).intValue());
            return;
        }
        if (obj instanceof Long) {
            writeInt64(((Long) obj).longValue());
            return;
        }
        if (obj instanceof String) {
            writeString((String) obj);
            return;
        }
        if (obj instanceof Boolean) {
            writeBoolean(((Boolean) obj).booleanValue());
            return;
        }
        if (obj instanceof Float) {
            writeFloat(((Float) obj).floatValue());
            return;
        }
        if (obj instanceof Double) {
            writeDouble(((Double) obj).doubleValue());
            return;
        }
        if (obj instanceof Short) {
            writeInt32(((Short) obj).intValue());
            return;
        }
        if (obj instanceof Byte) {
            writeInt32(((Byte) obj).intValue());
            return;
        }
        if (obj instanceof BigDecimal) {
            writeDecimal((BigDecimal) obj);
            return;
        }
        if (obj instanceof BigInteger) {
            writeBigInteger((BigInteger) obj);
            return;
        }
        if (obj instanceof Date) {
            writeDate((Date) obj);
            return;
        }
        if (obj instanceof Instant) {
            writeInstant((Instant) obj);
            return;
        }
        if (obj instanceof LocalDate) {
            writeLocalDate((LocalDate) obj);
        } else if (obj instanceof LocalDateTime) {
            writeLocalDateTime((LocalDateTime) obj);
        } else {
            writeString(obj.toString());
        }
    }

    public void writeBigInteger(BigInteger bigInteger) {
        if (bigInteger == null) {
            return;
        }
        writeRaw(bigInteger.toString());
    }

    public final void writeDate(long j) {
        int shanghaiZoneOffsetTotalSeconds;
        long j2;
        ZoneId zoneId = this.zoneId;
        long jFloorDiv = Math.floorDiv(j, 1000L);
        if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
            shanghaiZoneOffsetTotalSeconds = DateUtils.getShanghaiZoneOffsetTotalSeconds(jFloorDiv);
        } else {
            shanghaiZoneOffsetTotalSeconds = zoneId.getRules().getOffset(Instant.ofEpochMilli(j)).getTotalSeconds();
        }
        long j3 = jFloorDiv + ((long) shanghaiZoneOffsetTotalSeconds);
        long jFloorDiv2 = Math.floorDiv(j3, 86400L);
        int iFloorMod = (int) Math.floorMod(j3, 86400L);
        long j4 = 719468 + jFloorDiv2;
        if (j4 < 0) {
            long j5 = ((jFloorDiv2 + 719469) / 146097) - 1;
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
        if (i5 < 0 || i5 > 9999 || ((int) Math.floorMod(j, 1000L)) != 0) {
            writeRaw(DateUtils.toString(j, false, zoneId));
        } else if (i6 == 0 && i7 == 0 && i8 == 0) {
            writeDateYYYMMDD10(i5, i3, i4);
        } else {
            writeDateTime19(i5, i3, i4, i6, i7, i8);
        }
    }

    public static CSVWriter of(OutputStream outputStream, Feature... featureArr) {
        return new CSVWriterUTF8(outputStream, StandardCharsets.UTF_8, DateUtils.DEFAULT_ZONE_ID, featureArr);
    }

    public static CSVWriter of(OutputStream outputStream, Charset charset) {
        return of(outputStream, charset, DateUtils.DEFAULT_ZONE_ID);
    }

    public static CSVWriter of(OutputStream outputStream, Charset charset, ZoneId zoneId) {
        if (charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return of(new OutputStreamWriter(outputStream, charset), zoneId);
        }
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        return new CSVWriterUTF8(outputStream, charset, zoneId, new Feature[0]);
    }

    public static CSVWriter of(Writer writer) {
        return new CSVWriterUTF16(writer, DateUtils.DEFAULT_ZONE_ID, new Feature[0]);
    }

    public static CSVWriter of(Writer writer, ZoneId zoneId) {
        return new CSVWriterUTF16(writer, zoneId, new Feature[0]);
    }

    public enum Feature {
        AlwaysQuoteStrings(1);

        public final long mask;

        Feature(long j) {
            this.mask = j;
        }
    }
}
