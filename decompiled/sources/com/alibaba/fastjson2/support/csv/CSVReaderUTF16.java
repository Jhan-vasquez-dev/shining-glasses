package com.alibaba.fastjson2.support.csv;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.reader.CharArrayValueConsumer;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.stream.StreamReader;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class CSVReaderUTF16<T> extends CSVReader<T> {
    static final Map<Long, Function<Consumer, CharArrayValueConsumer>> valueConsumerCreators = new ConcurrentHashMap();
    char[] buf;
    Reader input;
    CharArrayValueConsumer valueConsumer;

    CSVReaderUTF16(StreamReader.Feature... featureArr) {
        for (StreamReader.Feature feature : featureArr) {
            this.features |= feature.mask;
        }
    }

    CSVReaderUTF16(Reader reader, Class<T> cls) {
        super(cls);
        this.input = reader;
    }

    CSVReaderUTF16(Reader reader, CharArrayValueConsumer charArrayValueConsumer) {
        this.valueConsumer = charArrayValueConsumer;
        this.input = reader;
    }

    CSVReaderUTF16(Reader reader, Type[] typeArr) {
        super(typeArr);
        this.input = reader;
    }

    CSVReaderUTF16(char[] cArr, int i, int i2, Class<T> cls) {
        super(cls);
        this.buf = cArr;
        this.off = i;
        this.end = i + i2;
    }

    CSVReaderUTF16(char[] cArr, int i, int i2, CharArrayValueConsumer charArrayValueConsumer) {
        this.valueConsumer = charArrayValueConsumer;
        this.buf = cArr;
        this.off = i;
        this.end = i + i2;
    }

    CSVReaderUTF16(char[] cArr, int i, int i2, Type[] typeArr) {
        super(typeArr);
        this.buf = cArr;
        this.off = i;
        this.end = i + i2;
    }

    @Override // com.alibaba.fastjson2.stream.StreamReader
    protected boolean seekLine() throws IOException {
        Reader reader;
        char[] cArr = this.buf;
        int i = this.off;
        if (cArr == null && (reader = this.input) != null) {
            cArr = new char[524288];
            this.buf = cArr;
            int i2 = reader.read(cArr);
            if (i2 == -1) {
                this.inputEnd = true;
                return false;
            }
            this.end = i2;
        }
        for (int i3 = 0; i3 < 3; i3++) {
            this.lineTerminated = false;
            int i4 = this.end;
            int i5 = i;
            while (true) {
                int i6 = i5 + 4;
                if (i6 >= i4 || CSVReaderUTF8.containsQuoteOrLineSeparator(IOUtils.getLongUnaligned(cArr, i5))) {
                    break;
                }
                i5 = i6;
            }
            while (true) {
                if (i5 >= i4) {
                    break;
                }
                char c = cArr[i5];
                if (c == '\"') {
                    this.lineSize++;
                    if (!this.quote) {
                        this.quote = true;
                    } else {
                        int i7 = i5 + 1;
                        if (i7 >= i4) {
                            break;
                        }
                        if (cArr[i7] == '\"') {
                            this.lineSize++;
                            i5 = i7;
                        } else {
                            this.quote = false;
                        }
                    }
                    i5++;
                } else {
                    if (this.quote) {
                        this.lineSize++;
                    } else if (c == '\n') {
                        if (this.lineSize > 0 || (this.features & StreamReader.Feature.IgnoreEmptyLine.mask) == 0) {
                            this.rowCount++;
                        }
                        this.lineTerminated = true;
                        this.lineSize = 0;
                        this.lineEnd = i5;
                        this.lineStart = this.lineNextStart;
                        i = i5 + 1;
                        this.lineNextStart = i;
                    } else if (c == '\r') {
                        if (this.lineSize > 0 || (this.features & StreamReader.Feature.IgnoreEmptyLine.mask) == 0) {
                            this.rowCount++;
                        }
                        this.lineTerminated = true;
                        this.lineSize = 0;
                        this.lineEnd = i5;
                        int i8 = i5 + 1;
                        if (i8 < i4) {
                            if (cArr[i8] == '\n') {
                                i5 = i8;
                            }
                            this.lineStart = this.lineNextStart;
                            i = i5 + 1;
                            this.lineNextStart = i;
                        }
                    } else {
                        this.lineSize++;
                    }
                    i5++;
                }
                this.off = i;
                return true;
            }
            if (!this.lineTerminated) {
                if (this.input != null && !this.inputEnd) {
                    int i9 = i4 - i;
                    if (i > 0) {
                        if (i9 > 0) {
                            System.arraycopy(cArr, i, cArr, 0, i9);
                        }
                        this.lineNextStart = 0;
                        this.lineStart = 0;
                        this.quote = false;
                        i = 0;
                        i4 = i9;
                    }
                    int i10 = this.input.read(cArr, i4, cArr.length - i4);
                    if (i10 == -1) {
                        this.inputEnd = true;
                        if (i == i4) {
                            this.off = i;
                            return false;
                        }
                    } else {
                        this.end = i4 + i10;
                    }
                }
                this.lineStart = this.lineNextStart;
                this.lineEnd = i4;
                this.rowCount++;
                this.lineSize = 0;
                i = i4;
            }
            this.lineTerminated = i == i4;
            this.off = i;
            return true;
        }
        this.off = i;
        return true;
    }

    Object readValue(char[] cArr, int i, int i2, Type type) {
        if (i2 == 0) {
            return null;
        }
        if (type == Integer.class) {
            return Integer.valueOf(TypeUtils.parseInt(cArr, i, i2));
        }
        if (type == Long.class) {
            return Long.valueOf(TypeUtils.parseLong(cArr, i, i2));
        }
        if (type == BigDecimal.class) {
            return TypeUtils.parseBigDecimal(cArr, i, i2);
        }
        if (type == Float.class) {
            return Float.valueOf(TypeUtils.parseFloat(cArr, i, i2));
        }
        if (type == Double.class) {
            return Double.valueOf(TypeUtils.parseDouble(cArr, i, i2));
        }
        if (type == Date.class) {
            return new Date(DateUtils.parseMillis(cArr, i, i2, DateUtils.DEFAULT_ZONE_ID));
        }
        if (type == Boolean.class) {
            return TypeUtils.parseBoolean(cArr, i, i2);
        }
        return TypeUtils.cast(new String(cArr, i, i2), type);
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public boolean isEnd() {
        return this.inputEnd;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0131  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0137  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0203  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x006d A[PHI: r4
      0x006d: PHI (r4v12 int) = (r4v13 int), (r4v1 int) binds: [B:40:0x0076, B:26:0x004d] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0078  */
    /* JADX WARN: Type inference failed for: r10v0 */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v10 */
    /* JADX WARN: Type inference failed for: r10v11 */
    /* JADX WARN: Type inference failed for: r10v12 */
    /* JADX WARN: Type inference failed for: r10v2, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r10v3, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r10v4, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r10v5, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r10v6, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r10v7 */
    /* JADX WARN: Type inference failed for: r10v8 */
    /* JADX WARN: Type inference failed for: r10v9 */
    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object[] readLineValues(boolean r18) {
        /*
            Method dump skipped, instruction units count: 570
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.support.csv.CSVReaderUTF16.readLineValues(boolean):java.lang.Object[]");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Reader reader = this.input;
        if (reader != null) {
            IOUtils.close(reader);
        }
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public void statAll() {
        readAll(new CharArrayValueConsumer() { // from class: com.alibaba.fastjson2.support.csv.CSVReaderUTF16$$ExternalSyntheticLambda0
            @Override // com.alibaba.fastjson2.reader.CharArrayValueConsumer
            public final void accept(int i, int i2, char[] cArr, int i3, int i4) {
                this.f$0.m52xd268b34c(i, i2, cArr, i3, i4);
            }
        }, Integer.MAX_VALUE);
    }

    /* JADX INFO: renamed from: lambda$statAll$0$com-alibaba-fastjson2-support-csv-CSVReaderUTF16, reason: not valid java name */
    /* synthetic */ void m52xd268b34c(int i, int i2, char[] cArr, int i3, int i4) {
        getColumnStat(i2).stat(cArr, i3, i4);
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public void statAll(int i) {
        readAll(new CharArrayValueConsumer() { // from class: com.alibaba.fastjson2.support.csv.CSVReaderUTF16$$ExternalSyntheticLambda1
            @Override // com.alibaba.fastjson2.reader.CharArrayValueConsumer
            public final void accept(int i2, int i3, char[] cArr, int i4, int i5) {
                this.f$0.m53xc412596b(i2, i3, cArr, i4, i5);
            }
        }, i);
    }

    /* JADX INFO: renamed from: lambda$statAll$1$com-alibaba-fastjson2-support-csv-CSVReaderUTF16, reason: not valid java name */
    /* synthetic */ void m53xc412596b(int i, int i2, char[] cArr, int i3, int i4) {
        getColumnStat(i2).stat(cArr, i3, i4);
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public void readLineObjectAll(boolean z, Consumer<T> consumer) {
        if (z) {
            readHeader();
        }
        int i = 0;
        if (this.fieldReaders != null) {
            ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
            if (this.fieldReaders == null && this.objectClass != null) {
                this.fieldReaders = ((ObjectReaderAdapter) defaultObjectReaderProvider.getObjectReader(this.objectClass)).getFieldReaders();
                this.objectCreator = defaultObjectReaderProvider.createObjectCreator(this.objectClass, this.features);
            }
            String[] strArr = new String[this.fieldReaders.length + 1];
            strArr[0] = this.objectClass.getName();
            while (i < this.fieldReaders.length) {
                int i2 = i + 1;
                strArr[i2] = this.fieldReaders[i].fieldName;
                i = i2;
            }
            long jHashCode64 = Fnv.hashCode64(strArr);
            Map<Long, Function<Consumer, CharArrayValueConsumer>> map = valueConsumerCreators;
            Function<Consumer, CharArrayValueConsumer> functionCreateCharArrayValueConsumerCreator = map.get(Long.valueOf(jHashCode64));
            if (functionCreateCharArrayValueConsumerCreator == null && (functionCreateCharArrayValueConsumerCreator = defaultObjectReaderProvider.createCharArrayValueConsumerCreator(this.objectClass, this.fieldReaders)) != null) {
                map.putIfAbsent(Long.valueOf(jHashCode64), functionCreateCharArrayValueConsumerCreator);
            }
            CharArrayValueConsumer charArrayValueConsumerApply = functionCreateCharArrayValueConsumerCreator != null ? functionCreateCharArrayValueConsumerCreator.apply(consumer) : null;
            if (charArrayValueConsumerApply == null) {
                charArrayValueConsumerApply = new CharArrayConsumerImpl<>(consumer);
            }
            readAll(charArrayValueConsumerApply, Integer.MAX_VALUE);
            return;
        }
        while (true) {
            Object[] lineValues = readLineValues(false);
            if (lineValues == null) {
                return;
            } else {
                consumer.accept(lineValues);
            }
        }
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public void readAll() {
        CharArrayValueConsumer<T> charArrayValueConsumer = this.valueConsumer;
        if (charArrayValueConsumer == null) {
            throw new JSONException("unsupported operation, consumer is null");
        }
        readAll(charArrayValueConsumer, Integer.MAX_VALUE);
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public void readAll(int i) {
        CharArrayValueConsumer<T> charArrayValueConsumer = this.valueConsumer;
        if (charArrayValueConsumer == null) {
            throw new JSONException("unsupported operation, consumer is null");
        }
        readAll(charArrayValueConsumer, i);
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00bc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void readAll(com.alibaba.fastjson2.reader.CharArrayValueConsumer<T> r17, int r18) {
        /*
            Method dump skipped, instruction units count: 285
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.support.csv.CSVReaderUTF16.readAll(com.alibaba.fastjson2.reader.CharArrayValueConsumer, int):void");
    }

    class CharArrayConsumerImpl<T> implements CharArrayValueConsumer {
        final Consumer<T> consumer;
        protected T object;

        public CharArrayConsumerImpl(Consumer<T> consumer) {
            this.consumer = consumer;
        }

        @Override // com.alibaba.fastjson2.reader.CharArrayValueConsumer
        public final void beforeRow(int i) {
            if (CSVReaderUTF16.this.objectCreator != null) {
                this.object = (T) CSVReaderUTF16.this.objectCreator.get();
            }
        }

        @Override // com.alibaba.fastjson2.reader.CharArrayValueConsumer
        public void accept(int i, int i2, char[] cArr, int i3, int i4) {
            if (i2 >= CSVReaderUTF16.this.fieldReaders.length || i4 == 0) {
                return;
            }
            FieldReader fieldReader = CSVReaderUTF16.this.fieldReaders[i2];
            fieldReader.accept(this.object, CSVReaderUTF16.this.readValue(cArr, i3, i4, fieldReader.fieldType));
        }

        @Override // com.alibaba.fastjson2.reader.CharArrayValueConsumer
        public final void afterRow(int i) {
            this.consumer.accept(this.object);
            this.object = null;
        }
    }
}
