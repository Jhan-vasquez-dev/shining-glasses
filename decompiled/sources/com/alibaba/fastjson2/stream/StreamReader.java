package com.alibaba.fastjson2.stream;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.support.csv.CSVReader;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import com.loopj.android.http.AsyncHttpClient;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import kotlin.time.DurationKt;

/* JADX INFO: loaded from: classes.dex */
public abstract class StreamReader<T> {
    protected static final int SIZE_512K = 524288;
    protected List<ColumnStat> columnStats;
    protected Map<String, ColumnStat> columnStatsMap;
    protected List<String> columns;
    protected int end;
    protected int errorCount;
    protected long features;
    protected FieldReader[] fieldReaders;
    protected boolean inputEnd;
    protected int lineEnd;
    protected int lineNextStart;
    protected int lineSize;
    protected int lineStart;
    protected boolean lineTerminated = true;
    protected int[] mapping;
    protected Supplier objectCreator;
    protected int off;
    protected int rowCount;
    protected ObjectReader[] typeReaders;
    protected Type[] types;

    public abstract <T> T readLineObject();

    protected abstract boolean seekLine() throws IOException;

    public StreamReader() {
    }

    public StreamReader(Type[] typeArr) {
        this.types = typeArr;
        if (typeArr.length == 0) {
            this.typeReaders = new ObjectReader[0];
            return;
        }
        ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
        ObjectReader[] objectReaderArr = new ObjectReader[typeArr.length];
        for (int i = 0; i < typeArr.length; i++) {
            Type type = typeArr[i];
            if (type == String.class || type == Object.class) {
                objectReaderArr[i] = null;
            } else {
                objectReaderArr[i] = defaultObjectReaderProvider.getObjectReader(type);
            }
        }
        this.typeReaders = objectReaderArr;
    }

    public enum Feature {
        IgnoreEmptyLine(1),
        ErrorAsNull(2);

        public final long mask;

        Feature(long j) {
            this.mask = j;
        }
    }

    public static class ColumnStat {
        public int arrays;
        public int booleans;
        public int dates;
        public int doubles;
        public int errors;
        public int integers;
        public int maps;

        @JSONField(ordinal = -1)
        public final String name;
        public int nonAsciiStrings;
        public int nulls;
        public int numbers;
        public int precision;
        public int scale;
        public int values;

        public ColumnStat(String str) {
            this.name = str;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r2v14 */
        /* JADX WARN: Type inference failed for: r2v16, types: [java.time.LocalDateTime] */
        /* JADX WARN: Type inference failed for: r2v17 */
        /* JADX WARN: Type inference failed for: r2v28 */
        /* JADX WARN: Type inference failed for: r2v29 */
        /* JADX WARN: Type inference failed for: r2v30 */
        public void stat(char[] cArr, int i, int i2) {
            LocalDateTime localDateTime;
            int i3;
            ZonedDateTime zonedDateTime;
            this.values++;
            if (i2 == 0) {
                this.nulls++;
                return;
            }
            int i4 = i + i2;
            for (int i5 = i; i5 < i4; i5++) {
                if (cArr[i5] > 127) {
                    if (this.precision < i2) {
                        this.precision = i2;
                    }
                    this.nonAsciiStrings++;
                    return;
                }
            }
            boolean z = false;
            if (TypeUtils.isNumber(cArr, i, i2)) {
                char c = cArr[i];
                int i6 = (c == '+' || c == '-') ? i2 - 1 : i2;
                this.numbers++;
                if (TypeUtils.isInteger(cArr, i, i2)) {
                    this.integers++;
                } else {
                    int i7 = -1;
                    while (i < i4) {
                        char c2 = cArr[i];
                        if (c2 == '.') {
                            i7 = i;
                        } else if (c2 == 'e' || c2 == 'E') {
                            z = true;
                        }
                        i++;
                    }
                    if (z) {
                        this.doubles++;
                    } else if (i7 != -1) {
                        int i8 = (i4 - i7) - 1;
                        if (this.scale < i8) {
                            this.scale = i8;
                        }
                        i6--;
                    }
                }
                i2 = i6;
            } else {
                int i9 = 0;
                int i10 = 0;
                int i11 = 0;
                int i12 = 0;
                for (int i13 = i; i13 < i4; i13++) {
                    char c3 = cArr[i13];
                    if (c3 != ':') {
                        switch (c3) {
                            case '-':
                                i9++;
                                break;
                            case '.':
                                break;
                            case '/':
                                i10++;
                                break;
                            default:
                                if (c3 >= '0' && c3 <= '9') {
                                    i12++;
                                }
                                break;
                        }
                    } else {
                        i11++;
                    }
                }
                boolean z2 = i9 == 2 || i10 == 2 || i11 == 2;
                if (z2 && (i12 < 2 || i2 > 36)) {
                    z2 = false;
                }
                if (z2) {
                    try {
                        switch (i2) {
                            case 8:
                                localDateTime = DateUtils.parseLocalDate8(cArr, i).atStartOfDay();
                                break;
                            case 9:
                                localDateTime = DateUtils.parseLocalDate9(cArr, i).atStartOfDay();
                                break;
                            case 10:
                                localDateTime = DateUtils.parseLocalDate10(cArr, i).atStartOfDay();
                                break;
                            default:
                                localDateTime = 0;
                                break;
                        }
                        if (localDateTime == 0 && (zonedDateTime = DateUtils.parseZonedDateTime(new String(cArr, i, i2))) != null) {
                            localDateTime = zonedDateTime.toLocalDateTime();
                        }
                        if (localDateTime != 0) {
                            try {
                                this.dates++;
                                i2 = 0;
                            } catch (Exception unused) {
                                i2 = 0;
                                this.errors++;
                            }
                        }
                        int nano = localDateTime.getNano();
                        if (nano != 0) {
                            if (nano % 100000000 == 0) {
                                i2 = 1;
                            } else if (nano % 10000000 == 0) {
                                i2 = 2;
                            } else {
                                if (nano % DurationKt.NANOS_IN_MILLIS == 0) {
                                    i3 = 3;
                                } else if (nano % 100000 == 0) {
                                    i3 = 4;
                                } else if (nano % AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT == 0) {
                                    i3 = 5;
                                } else if (nano % 1000 == 0) {
                                    i3 = 6;
                                } else if (nano % 100 == 0) {
                                    i3 = 7;
                                } else {
                                    i3 = nano % 10 == 0 ? 8 : 9;
                                }
                                i2 = i3;
                            }
                        }
                    } catch (Exception unused2) {
                    }
                }
            }
            if (this.precision < i2) {
                this.precision = i2;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r2v14 */
        /* JADX WARN: Type inference failed for: r2v16, types: [java.time.LocalDateTime] */
        /* JADX WARN: Type inference failed for: r2v17 */
        /* JADX WARN: Type inference failed for: r2v30 */
        /* JADX WARN: Type inference failed for: r2v31 */
        /* JADX WARN: Type inference failed for: r2v32 */
        public void stat(byte[] bArr, int i, int i2, Charset charset) {
            LocalDateTime localDateTime;
            int i3;
            ZonedDateTime zonedDateTime;
            this.values++;
            if (i2 == 0) {
                this.nulls++;
                return;
            }
            int i4 = i + i2;
            for (int i5 = i; i5 < i4; i5++) {
                if (bArr[i5] < 0) {
                    if (this.precision < i2) {
                        this.precision = i2;
                    }
                    this.nonAsciiStrings++;
                    return;
                }
            }
            boolean z = false;
            if (TypeUtils.isNumber(bArr, i, i2)) {
                char c = (char) bArr[i];
                int i6 = (c == '+' || c == '-') ? i2 - 1 : i2;
                this.numbers++;
                if (TypeUtils.isInteger(bArr, i, i2)) {
                    this.integers++;
                } else {
                    int i7 = -1;
                    while (i < i4) {
                        byte b = bArr[i];
                        if (b == 46) {
                            i7 = i;
                        } else if (b == 101 || b == 69) {
                            z = true;
                        }
                        i++;
                    }
                    if (z) {
                        this.doubles++;
                    } else if (i7 != -1) {
                        int i8 = (i4 - i7) - 1;
                        if (this.scale < i8) {
                            this.scale = i8;
                        }
                        i6--;
                    }
                }
                i2 = i6;
            } else {
                int i9 = 0;
                int i10 = 0;
                int i11 = 0;
                int i12 = 0;
                for (int i13 = i; i13 < i4; i13++) {
                    char c2 = (char) bArr[i13];
                    if (c2 != ':') {
                        switch (c2) {
                            case '-':
                                i9++;
                                break;
                            case '.':
                                break;
                            case '/':
                                i10++;
                                break;
                            default:
                                if (c2 >= '0' && c2 <= '9') {
                                    i12++;
                                }
                                break;
                        }
                    } else {
                        i11++;
                    }
                }
                boolean z2 = i9 == 2 || i10 == 2 || i11 == 2;
                if (z2 && (i12 < 2 || i2 > 36)) {
                    z2 = false;
                }
                if (z2) {
                    try {
                        switch (i2) {
                            case 8:
                                localDateTime = DateUtils.parseLocalDate8(bArr, i).atStartOfDay();
                                break;
                            case 9:
                                localDateTime = DateUtils.parseLocalDate9(bArr, i).atStartOfDay();
                                break;
                            case 10:
                                localDateTime = DateUtils.parseLocalDate10(bArr, i).atStartOfDay();
                                break;
                            default:
                                localDateTime = 0;
                                break;
                        }
                        if (localDateTime == 0 && (zonedDateTime = DateUtils.parseZonedDateTime(new String(bArr, i, i2, charset))) != null) {
                            localDateTime = zonedDateTime.toLocalDateTime();
                        }
                        if (localDateTime != 0) {
                            try {
                                this.dates++;
                                i2 = 0;
                            } catch (Exception unused) {
                                i2 = 0;
                                this.errors++;
                            }
                        }
                        int nano = localDateTime.getNano();
                        if (nano != 0) {
                            if (nano % 100000000 == 0) {
                                i2 = 1;
                            } else if (nano % 10000000 == 0) {
                                i2 = 2;
                            } else {
                                if (nano % DurationKt.NANOS_IN_MILLIS == 0) {
                                    i3 = 3;
                                } else if (nano % 100000 == 0) {
                                    i3 = 4;
                                } else if (nano % AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT == 0) {
                                    i3 = 5;
                                } else if (nano % 1000 == 0) {
                                    i3 = 6;
                                } else if (nano % 100 == 0) {
                                    i3 = 7;
                                } else {
                                    i3 = nano % 10 == 0 ? 8 : 9;
                                }
                                i2 = i3;
                            }
                        }
                    } catch (Exception unused2) {
                    }
                }
            }
            if (this.precision < i2) {
                this.precision = i2;
            }
        }

        public void stat(String str) {
            if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_CODER.applyAsInt(str) == JDKUtils.LATIN1.byteValue() && JDKUtils.STRING_VALUE != null) {
                byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
                stat(bArrApply, 0, bArrApply.length, StandardCharsets.ISO_8859_1);
            } else {
                char[] charArray = JDKUtils.getCharArray(str);
                stat(charArray, 0, charArray.length);
            }
        }

        public String getInferSQLType() {
            int i;
            int i2;
            if (this.nonAsciiStrings <= 0 && (i = this.nulls) != (i2 = this.values)) {
                if (i2 == this.dates + i) {
                    if (this.precision != 0) {
                        return "TIMESTAMP";
                    }
                    return "DATETIME";
                }
                if (i2 == this.integers + i) {
                    int i3 = this.precision;
                    if (i3 < 10) {
                        return "INT";
                    }
                    if (i3 < 20) {
                        return "BIGINT";
                    }
                    return "DECIMAL(" + this.precision + ", 0)";
                }
                if (i2 == this.numbers + i) {
                    if (this.doubles > 0 || this.scale > 5) {
                        return "DOUBLE";
                    }
                    int i4 = this.precision;
                    if (i4 < 19) {
                        i4 = 19;
                    }
                    return "DECIMAL(" + i4 + ", " + this.scale + ")";
                }
            }
            return "STRING";
        }

        public Type getInferType() {
            int i;
            int i2;
            if (this.nonAsciiStrings > 0 || (i = this.nulls) == (i2 = this.values)) {
                return String.class;
            }
            if (i2 == this.booleans + i) {
                return Boolean.class;
            }
            if (i2 == this.dates + i) {
                if (this.precision != 0) {
                    return Instant.class;
                }
                return Date.class;
            }
            if (this.doubles > 0) {
                return Double.class;
            }
            if (i2 == this.integers + i) {
                int i3 = this.precision;
                if (i3 < 10) {
                    return Integer.class;
                }
                if (i3 < 20) {
                    return Long.class;
                }
                return BigInteger.class;
            }
            if (i2 == this.numbers + i) {
                return BigDecimal.class;
            }
            if (this.arrays > 0) {
                return Collection.class;
            }
            if (this.maps > 0) {
                return Map.class;
            }
            return String.class;
        }
    }

    public <T> Stream<T> stream() {
        return StreamSupport.stream(new StreamReaderSpliterator(this), false);
    }

    public <T> Stream<T> stream(Class<T> cls) {
        return StreamSupport.stream(new StreamReaderSpliterator(this, cls), false);
    }

    protected static class StreamReaderSpliterator<T> implements Spliterator<T> {
        private Class<T> clazz;
        private CSVReader csvReader;
        private final StreamReader<T> streamReader;

        @Override // java.util.Spliterator
        public int characteristics() {
            return 1296;
        }

        public StreamReaderSpliterator(StreamReader<T> streamReader) {
            this.streamReader = streamReader;
            if (streamReader instanceof CSVReader) {
                CSVReader cSVReader = (CSVReader) streamReader;
                if (cSVReader.isObjectSupport()) {
                    return;
                }
                this.csvReader = cSVReader;
            }
        }

        public StreamReaderSpliterator(StreamReader<T> streamReader, Class<T> cls) {
            this.streamReader = streamReader;
            this.clazz = cls;
            if (streamReader instanceof CSVReader) {
                CSVReader cSVReader = (CSVReader) streamReader;
                if (cSVReader.isObjectSupport()) {
                    return;
                }
                this.csvReader = cSVReader;
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            if (consumer == null) {
                throw new IllegalArgumentException("action must not be null");
            }
            T next = next();
            if ((this.streamReader.inputEnd && this.streamReader.off >= this.streamReader.end) || next == null) {
                return false;
            }
            consumer.accept(next);
            return true;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private T next() {
            CSVReader cSVReader = this.csvReader;
            if (cSVReader != null) {
                T t = (T) cSVReader.readLineValues();
                if (!(!r1.isAssignableFrom(t.getClass())) || !(this.clazz != null)) {
                    return t;
                }
                throw new ClassCastException(String.format("%s can not cast to %s", t.getClass(), this.clazz));
            }
            return this.streamReader.readLineObject();
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            throw new UnsupportedOperationException("parallel stream not supported");
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return (!this.streamReader.inputEnd || this.streamReader.off < this.streamReader.end) ? Long.MAX_VALUE : 0L;
        }
    }
}
