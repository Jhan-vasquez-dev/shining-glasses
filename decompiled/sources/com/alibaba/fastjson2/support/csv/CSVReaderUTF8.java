package com.alibaba.fastjson2.support.csv;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.reader.ByteArrayValueConsumer;
import com.alibaba.fastjson2.reader.FieldReaderAtomicReferenceMethodReadOnly;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.stream.StreamReader;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final class CSVReaderUTF8<T> extends CSVReader<T> {
    static final int ESCAPE_INDEX_NOT_SET = -2;
    static final Map<Long, Function<Consumer, ByteArrayValueConsumer>> valueConsumerCreators = new ConcurrentHashMap();
    byte[] buf;
    Charset charset;
    InputStream input;
    int nextEscapeIndex;
    ByteArrayValueConsumer valueConsumer;

    static boolean containsQuoteOrLineSeparator(long j) {
        long j2 = 2459565876494606882L ^ j;
        long j3 = 723401728380766730L ^ j;
        long j4 = j ^ 940422246894996749L;
        return ((((~j4) & (j4 - 72340172838076673L)) | (((~j2) & (j2 - 72340172838076673L)) | ((~j3) & (j3 - 72340172838076673L)))) & (-9187201950435737472L)) != 0;
    }

    CSVReaderUTF8(StreamReader.Feature... featureArr) {
        this.nextEscapeIndex = -2;
        this.charset = StandardCharsets.UTF_8;
        for (StreamReader.Feature feature : featureArr) {
            this.features |= feature.mask;
        }
    }

    CSVReaderUTF8(byte[] bArr, int i, int i2, Charset charset, Class<T> cls) {
        super(cls);
        this.nextEscapeIndex = -2;
        this.charset = StandardCharsets.UTF_8;
        this.buf = bArr;
        this.off = i;
        this.end = i + i2;
        this.charset = charset;
    }

    CSVReaderUTF8(byte[] bArr, int i, int i2, Charset charset, ByteArrayValueConsumer byteArrayValueConsumer) {
        this.nextEscapeIndex = -2;
        this.charset = StandardCharsets.UTF_8;
        this.valueConsumer = byteArrayValueConsumer;
        this.buf = bArr;
        this.off = i;
        this.end = i + i2;
        this.charset = charset;
    }

    CSVReaderUTF8(byte[] bArr, int i, int i2, Type[] typeArr) {
        super(typeArr);
        this.nextEscapeIndex = -2;
        this.charset = StandardCharsets.UTF_8;
        this.buf = bArr;
        this.off = i;
        this.end = i + i2;
        this.types = typeArr;
    }

    CSVReaderUTF8(byte[] bArr, int i, int i2, Class<T> cls) {
        super(cls);
        this.nextEscapeIndex = -2;
        this.charset = StandardCharsets.UTF_8;
        this.buf = bArr;
        this.off = i;
        this.end = i + i2;
    }

    CSVReaderUTF8(InputStream inputStream, Charset charset, Type[] typeArr) {
        super(typeArr);
        this.nextEscapeIndex = -2;
        Charset charset2 = StandardCharsets.UTF_8;
        this.charset = charset;
        this.input = inputStream;
    }

    CSVReaderUTF8(InputStream inputStream, Charset charset, Class<T> cls) {
        super(cls);
        this.nextEscapeIndex = -2;
        Charset charset2 = StandardCharsets.UTF_8;
        this.charset = charset;
        this.input = inputStream;
    }

    CSVReaderUTF8(InputStream inputStream, Charset charset, ByteArrayValueConsumer byteArrayValueConsumer) {
        this.nextEscapeIndex = -2;
        Charset charset2 = StandardCharsets.UTF_8;
        this.charset = charset;
        this.input = inputStream;
        this.valueConsumer = byteArrayValueConsumer;
    }

    private byte[] getBuf() throws IOException {
        byte[] bArr = this.buf;
        return bArr != null ? bArr : initBuf();
    }

    private byte[] initBuf() throws IOException {
        if (this.input == null) {
            throw new JSONException("init buf error");
        }
        byte[] bArr = new byte[524288];
        int i = 0;
        while (true) {
            if (i >= 524288) {
                break;
            }
            int i2 = this.input.read(bArr, this.off, 524288 - this.off);
            if (i2 == -1) {
                this.inputEnd = true;
                break;
            }
            i += i2;
        }
        this.end = i;
        if (i > 4 && IOUtils.isUTF8BOM(bArr, 0)) {
            this.off = 3;
            this.lineNextStart = 3;
        }
        this.buf = bArr;
        return bArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x0076  */
    @Override // com.alibaba.fastjson2.stream.StreamReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected boolean seekLine() throws java.io.IOException {
        /*
            r10 = this;
            byte[] r0 = r10.getBuf()
            int r1 = r10.off
            int r2 = r10.end
            int r3 = r10.nextEscapeIndex
            r4 = -2
            r5 = -1
            if (r3 == r4) goto L12
            if (r3 == r5) goto L18
            if (r3 >= r1) goto L18
        L12:
            int r3 = com.alibaba.fastjson2.util.IOUtils.indexOfDoubleQuote(r0, r1, r2)
            r10.nextEscapeIndex = r3
        L18:
            int r4 = com.alibaba.fastjson2.util.IOUtils.indexOfLineSeparator(r0, r1, r2)
            r6 = 1
            if (r4 != r5) goto L61
            java.io.InputStream r7 = r10.input
            r8 = 0
            if (r7 == 0) goto L56
            boolean r7 = r10.inputEnd
            if (r7 != 0) goto L56
            int r2 = r2 - r1
            if (r2 <= 0) goto L2e
            java.lang.System.arraycopy(r0, r1, r0, r8, r2)
        L2e:
            r4 = r2
        L2f:
            int r7 = r0.length
            if (r4 >= r7) goto L40
            java.io.InputStream r7 = r10.input
            int r9 = r0.length
            int r9 = r9 - r4
            int r7 = r7.read(r0, r4, r9)
            if (r7 == r5) goto L3e
            int r4 = r4 + r7
            goto L2f
        L3e:
            r10.inputEnd = r6
        L40:
            if (r3 < r1) goto L45
            int r1 = r3 - r1
            goto L49
        L45:
            int r1 = com.alibaba.fastjson2.util.IOUtils.indexOfDoubleQuote(r0, r2, r4)
        L49:
            r10.nextEscapeIndex = r1
            int r1 = com.alibaba.fastjson2.util.IOUtils.indexOfLineSeparator(r0, r2, r4)
            r10.off = r8
            r10.end = r4
            r2 = r4
            r4 = r1
            r1 = r8
        L56:
            if (r4 != r5) goto L61
            boolean r7 = r10.inputEnd
            if (r7 == 0) goto L61
            if (r1 >= r2) goto L60
            r4 = r2
            goto L61
        L60:
            return r8
        L61:
            if (r4 == r5) goto L7d
            if (r3 == r5) goto L67
            if (r3 <= r4) goto L7d
        L67:
            r10.lineTerminated = r6
            r10.lineStart = r1
            if (r4 == r1) goto L76
            int r1 = r4 + (-1)
            r0 = r0[r1]
            r2 = 13
            if (r0 != r2) goto L76
            goto L77
        L76:
            r1 = r4
        L77:
            r10.lineEnd = r1
            int r4 = r4 + r6
            r10.off = r4
            return r6
        L7d:
            boolean r0 = r10.seekLine0(r0, r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.support.csv.CSVReaderUTF8.seekLine():boolean");
    }

    private boolean seekLine0(byte[] bArr, int i, int i2) throws IOException {
        int i3 = i2;
        int i4 = i;
        for (int i5 = 0; i5 < 3; i5++) {
            this.lineTerminated = false;
            int i6 = i;
            while (i6 < i3) {
                byte b = bArr[i6];
                if (b == 34) {
                    this.lineSize++;
                    if (!this.quote) {
                        this.quote = true;
                    } else {
                        int i7 = i6 + 1;
                        if (i7 >= i3) {
                            break;
                        }
                        if (bArr[i7] == 34) {
                            this.lineSize++;
                            i6 = i7;
                        } else {
                            this.quote = false;
                        }
                    }
                    i6++;
                } else {
                    if (this.quote) {
                        this.lineSize++;
                    } else if (b == 13 || b == 10) {
                        if (this.lineSize > 0 || (this.features & StreamReader.Feature.IgnoreEmptyLine.mask) == 0) {
                            this.rowCount++;
                        }
                        this.lineTerminated = true;
                        this.lineSize = 0;
                        this.lineEnd = i6;
                        if (b != 13) {
                            this.lineStart = i4;
                            i = i6 + 1;
                            i4 = i;
                        } else {
                            int i8 = i6 + 1;
                            if (i8 < i3) {
                                if (bArr[i8] == 10) {
                                    i6 = i8;
                                }
                                this.lineStart = i4;
                                i = i6 + 1;
                                i4 = i;
                            }
                        }
                    } else {
                        this.lineSize++;
                    }
                    i6++;
                }
                this.off = i;
                return true;
            }
            if (!this.lineTerminated) {
                if (this.input != null && !this.inputEnd) {
                    int i9 = i3 - i;
                    int i10 = this.nextEscapeIndex;
                    if (i > 0) {
                        if (i9 > 0) {
                            System.arraycopy(bArr, i, bArr, 0, i9);
                            this.nextEscapeIndex = i10 >= i ? i10 - i : -2;
                        }
                        this.lineStart = 0;
                        this.quote = false;
                        i = 0;
                        i4 = 0;
                        i3 = i9;
                    }
                    int i11 = this.input.read(bArr, i3, bArr.length - i3);
                    if (i11 == -1) {
                        this.inputEnd = true;
                        if (i == i3) {
                            this.off = i;
                            return false;
                        }
                    } else {
                        i3 += i11;
                        this.end = i3;
                    }
                }
                this.lineStart = i4;
                this.lineEnd = i3;
                this.rowCount++;
                this.lineSize = 0;
                i = i3;
            }
            this.lineTerminated = i == i3;
            this.off = i;
            return true;
        }
        this.off = i;
        return true;
    }

    Object readValue(byte[] bArr, int i, int i2, Type type) {
        if (i2 == 0) {
            return null;
        }
        if (type == Integer.class) {
            return Integer.valueOf(TypeUtils.parseInt(bArr, i, i2));
        }
        if (type == Long.class) {
            return Long.valueOf(TypeUtils.parseLong(bArr, i, i2));
        }
        if (type == BigDecimal.class) {
            return TypeUtils.parseBigDecimal(bArr, i, i2);
        }
        if (type == Float.class) {
            return Float.valueOf(TypeUtils.parseFloat(bArr, i, i2));
        }
        if (type == Double.class) {
            return Double.valueOf(TypeUtils.parseDouble(bArr, i, i2));
        }
        if (type == Date.class) {
            return new Date(DateUtils.parseMillis(bArr, i, i2, this.charset, DateUtils.DEFAULT_ZONE_ID));
        }
        if (type == Boolean.class) {
            return TypeUtils.parseBoolean(bArr, i, i2);
        }
        return TypeUtils.cast(new String(bArr, i, i2, this.charset), type);
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public boolean isEnd() {
        return this.inputEnd;
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public Object[] readLineValues(boolean z) {
        try {
            if (this.inputEnd && this.off == this.end) {
                return null;
            }
            if (this.input == null && this.off >= this.end) {
                return null;
            }
            if (!seekLine()) {
                return null;
            }
            int i = this.nextEscapeIndex;
            if (i == -1 || i >= this.lineEnd) {
                return readLineValue(z);
            }
            return readLineValueEscaped(z);
        } catch (IOException e) {
            throw new JSONException("seekLine error", e);
        }
    }

    private Object[] readLineValue(boolean z) {
        List<Object> arrayList;
        Object[] objArr;
        boolean z2;
        List<String> list = this.columns;
        if (list != null) {
            int size = list.size();
            objArr = z ? new String[size] : new Object[size];
            arrayList = null;
        } else {
            arrayList = new ArrayList();
            objArr = null;
        }
        int i = this.lineStart;
        int i2 = this.lineEnd;
        byte[] bArr = this.buf;
        int i3 = i;
        int i4 = this.lineStart;
        int i5 = 0;
        int i6 = 0;
        while (i4 < i2) {
            if (bArr[i4] == 44) {
                z2 = z;
                readValue(z2, i5, i6, bArr, i3, objArr, arrayList);
                i5++;
                i3 = i4 + 1;
                i6 = 0;
            } else {
                z2 = z;
                i6++;
            }
            i4++;
            z = z2;
        }
        boolean z3 = z;
        if (i6 > 0) {
            readValue(z3, i5, i6, bArr, i3, objArr, arrayList);
        }
        if (objArr == null && arrayList != null) {
            int size2 = arrayList.size();
            objArr = z3 ? new String[size2] : new Object[size2];
            arrayList.toArray(objArr);
        }
        if (this.input == null && this.off == this.end) {
            this.inputEnd = true;
        }
        return objArr;
    }

    private void readValue(boolean z, int i, int i2, byte[] bArr, int i3, Object[] objArr, List<Object> list) {
        Object str;
        byte b;
        byte b2;
        byte b3;
        Type type = (this.types == null || i >= this.types.length) ? null : this.types[i];
        if (type != null && type != String.class && type != Object.class && !z) {
            try {
                str = readValue(bArr, i3, i2, type);
            } catch (Exception e) {
                str = error(i, e);
            }
        } else if (i2 == 1 && (b3 = bArr[i3]) >= 0) {
            str = TypeUtils.toString((char) b3);
        } else if (i2 == 2 && (b = bArr[i3]) >= 0 && (b2 = bArr[i3 + 1]) >= 0) {
            str = TypeUtils.toString((char) b, (char) b2);
        } else {
            str = new String(bArr, i3, i2, this.charset);
        }
        if (objArr != null) {
            if (i < objArr.length) {
                objArr[i] = str;
                return;
            }
            return;
        }
        list.add(str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x012b  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x01fb  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0201  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0113  */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1 */
    /* JADX WARN: Type inference failed for: r12v10 */
    /* JADX WARN: Type inference failed for: r12v11 */
    /* JADX WARN: Type inference failed for: r12v12 */
    /* JADX WARN: Type inference failed for: r12v13 */
    /* JADX WARN: Type inference failed for: r12v14 */
    /* JADX WARN: Type inference failed for: r12v15 */
    /* JADX WARN: Type inference failed for: r12v2, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r12v3, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r12v4, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r12v5 */
    /* JADX WARN: Type inference failed for: r12v6, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r12v7, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r12v8 */
    /* JADX WARN: Type inference failed for: r12v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.Object[] readLineValueEscaped(boolean r18) {
        /*
            Method dump skipped, instruction units count: 556
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.support.csv.CSVReaderUTF8.readLineValueEscaped(boolean):java.lang.Object[]");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        InputStream inputStream = this.input;
        if (inputStream != null) {
            IOUtils.close(inputStream);
        }
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public void statAll() {
        readAll(new ByteArrayValueConsumer() { // from class: com.alibaba.fastjson2.support.csv.CSVReaderUTF8$$ExternalSyntheticLambda1
            @Override // com.alibaba.fastjson2.reader.ByteArrayValueConsumer
            public final void accept(int i, int i2, byte[] bArr, int i3, int i4, Charset charset) {
                this.f$0.m54lambda$statAll$0$comalibabafastjson2supportcsvCSVReaderUTF8(i, i2, bArr, i3, i4, charset);
            }
        }, Integer.MAX_VALUE);
    }

    /* JADX INFO: renamed from: lambda$statAll$0$com-alibaba-fastjson2-support-csv-CSVReaderUTF8, reason: not valid java name */
    /* synthetic */ void m54lambda$statAll$0$comalibabafastjson2supportcsvCSVReaderUTF8(int i, int i2, byte[] bArr, int i3, int i4, Charset charset) {
        getColumnStat(i2).stat(bArr, i3, i4, charset);
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public void statAll(int i) {
        readAll(new ByteArrayValueConsumer() { // from class: com.alibaba.fastjson2.support.csv.CSVReaderUTF8$$ExternalSyntheticLambda0
            @Override // com.alibaba.fastjson2.reader.ByteArrayValueConsumer
            public final void accept(int i2, int i3, byte[] bArr, int i4, int i5, Charset charset) {
                this.f$0.m55lambda$statAll$1$comalibabafastjson2supportcsvCSVReaderUTF8(i2, i3, bArr, i4, i5, charset);
            }
        }, i);
    }

    /* JADX INFO: renamed from: lambda$statAll$1$com-alibaba-fastjson2-support-csv-CSVReaderUTF8, reason: not valid java name */
    /* synthetic */ void m55lambda$statAll$1$comalibabafastjson2supportcsvCSVReaderUTF8(int i, int i2, byte[] bArr, int i3, int i4, Charset charset) {
        getColumnStat(i2).stat(bArr, i3, i4, charset);
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public void readAll() {
        ByteArrayValueConsumer byteArrayValueConsumer = this.valueConsumer;
        if (byteArrayValueConsumer == null) {
            throw new JSONException("unsupported operation, consumer is null");
        }
        readAll(byteArrayValueConsumer, Integer.MAX_VALUE);
    }

    @Override // com.alibaba.fastjson2.support.csv.CSVReader
    public void readAll(int i) {
        ByteArrayValueConsumer byteArrayValueConsumer = this.valueConsumer;
        if (byteArrayValueConsumer == null) {
            throw new JSONException("unsupported operation, consumer is null");
        }
        readAll(byteArrayValueConsumer, i);
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
            Map<Long, Function<Consumer, ByteArrayValueConsumer>> map = valueConsumerCreators;
            Function<Consumer, ByteArrayValueConsumer> functionCreateValueConsumerCreator = map.get(Long.valueOf(jHashCode64));
            if (functionCreateValueConsumerCreator == null && (functionCreateValueConsumerCreator = defaultObjectReaderProvider.createValueConsumerCreator(this.objectClass, this.fieldReaders)) != null) {
                map.putIfAbsent(Long.valueOf(jHashCode64), functionCreateValueConsumerCreator);
            }
            ByteArrayValueConsumer byteArrayValueConsumerApply = functionCreateValueConsumerCreator != null ? functionCreateValueConsumerCreator.apply(consumer) : null;
            if (byteArrayValueConsumerApply == null) {
                byteArrayValueConsumerApply = new ByteArrayConsumerImpl(consumer);
            }
            readAll(byteArrayValueConsumerApply, Integer.MAX_VALUE);
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

    class ByteArrayConsumerImpl implements ByteArrayValueConsumer {
        final Consumer consumer;
        protected Object object;

        public ByteArrayConsumerImpl(Consumer consumer) {
            this.consumer = consumer;
        }

        @Override // com.alibaba.fastjson2.reader.ByteArrayValueConsumer
        public final void beforeRow(int i) {
            if (CSVReaderUTF8.this.objectCreator != null) {
                this.object = CSVReaderUTF8.this.objectCreator.get();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.alibaba.fastjson2.reader.ByteArrayValueConsumer
        public void accept(int i, int i2, byte[] bArr, int i3, int i4, Charset charset) {
            if (i2 >= CSVReaderUTF8.this.fieldReaders.length || i4 == 0) {
                return;
            }
            FieldReaderAtomicReferenceMethodReadOnly fieldReaderAtomicReferenceMethodReadOnly = CSVReaderUTF8.this.fieldReaders[i2];
            fieldReaderAtomicReferenceMethodReadOnly.accept(this.object, CSVReaderUTF8.this.readValue(bArr, i3, i4, fieldReaderAtomicReferenceMethodReadOnly.fieldType));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.alibaba.fastjson2.reader.ByteArrayValueConsumer
        public final void afterRow(int i) {
            this.consumer.accept(this.object);
            this.object = null;
        }
    }

    private void readAll(ByteArrayValueConsumer byteArrayValueConsumer, int i) {
        byteArrayValueConsumer.start();
        int i2 = 0;
        while (true) {
            if (i2 >= i && i >= 0) {
                break;
            }
            try {
                if ((this.inputEnd && (this.off == this.end)) || ((this.input == null && this.off >= this.end) || !seekLine())) {
                    break;
                }
                byteArrayValueConsumer.beforeRow(this.rowCount);
                int i3 = this.nextEscapeIndex;
                if (i3 == -1 || i3 >= this.lineEnd) {
                    readLine(byteArrayValueConsumer);
                } else {
                    readLineEscaped(byteArrayValueConsumer);
                }
                byteArrayValueConsumer.afterRow(this.rowCount);
                i2++;
            } catch (IOException e) {
                throw new JSONException("seekLine error", e);
            }
        }
        byteArrayValueConsumer.end();
    }

    private void readLine(ByteArrayValueConsumer byteArrayValueConsumer) {
        ByteArrayValueConsumer byteArrayValueConsumer2;
        int i = this.lineStart;
        byte[] bArr = this.buf;
        int i2 = i;
        int i3 = this.lineStart;
        int i4 = 0;
        int i5 = 0;
        while (i3 < this.lineEnd) {
            if (bArr[i3] == 44) {
                byteArrayValueConsumer2 = byteArrayValueConsumer;
                byteArrayValueConsumer2.accept(this.rowCount, i4, bArr, i2, i5, this.charset);
                i4++;
                i2 = i3 + 1;
                i5 = 0;
            } else {
                byteArrayValueConsumer2 = byteArrayValueConsumer;
                i5++;
            }
            i3++;
            byteArrayValueConsumer = byteArrayValueConsumer2;
        }
        ByteArrayValueConsumer byteArrayValueConsumer3 = byteArrayValueConsumer;
        if (i5 > 0) {
            byteArrayValueConsumer3.accept(this.rowCount, i4, bArr, i2, i5, this.charset);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0082  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readLineEscaped(com.alibaba.fastjson2.reader.ByteArrayValueConsumer r15) {
        /*
            r14 = this;
            int r0 = r14.lineStart
            int r1 = r14.lineStart
            r2 = 0
            r3 = r2
            r4 = r3
            r5 = r4
            r8 = r5
        L9:
            int r6 = r14.lineEnd
            r7 = 34
            r13 = 1
            if (r1 >= r6) goto L88
            byte[] r6 = r14.buf
            r6 = r6[r1]
            r9 = 44
            if (r3 == 0) goto L3d
            if (r6 != r7) goto L39
            int r10 = r1 + 1
            int r11 = r14.lineEnd
            if (r10 >= r11) goto L33
            byte[] r11 = r14.buf
            r11 = r11[r10]
            if (r11 != r7) goto L2e
            int r4 = r4 + 2
            int r5 = r5 + 1
            r6 = r15
            r1 = r10
            goto L85
        L2e:
            if (r11 != r9) goto L42
            r1 = r10
            r6 = r11
            goto L42
        L33:
            int r11 = r14.lineEnd
            if (r10 != r11) goto L42
            goto L88
        L39:
            int r4 = r4 + 1
            r6 = r15
            goto L85
        L3d:
            if (r6 != r7) goto L42
            r6 = r15
            r3 = r13
            goto L85
        L42:
            if (r6 != r9) goto L82
            byte[] r6 = r14.buf
            if (r3 == 0) goto L6e
            if (r5 != 0) goto L4d
            int r0 = r0 + 1
            goto L6e
        L4d:
            int r3 = r4 - r5
            byte[] r6 = new byte[r3]
            int r4 = r4 + r0
            int r0 = r0 + 1
            r5 = r2
        L55:
            if (r0 >= r4) goto L6b
            byte[] r9 = r14.buf
            r10 = r9[r0]
            int r11 = r5 + 1
            r6[r5] = r10
            if (r10 != r7) goto L68
            int r5 = r0 + 1
            r9 = r9[r5]
            if (r9 != r7) goto L68
            r0 = r5
        L68:
            int r0 = r0 + r13
            r5 = r11
            goto L55
        L6b:
            r10 = r2
            r11 = r3
            goto L70
        L6e:
            r10 = r0
            r11 = r4
        L70:
            r9 = r6
            int r7 = r14.rowCount
            java.nio.charset.Charset r12 = r14.charset
            r6 = r15
            r6.accept(r7, r8, r9, r10, r11, r12)
            int r15 = r1 + 1
            int r8 = r8 + 1
            r0 = r15
            r3 = r2
            r4 = r3
            r5 = r4
            goto L85
        L82:
            r6 = r15
            int r4 = r4 + 1
        L85:
            int r1 = r1 + r13
            r15 = r6
            goto L9
        L88:
            r6 = r15
            if (r4 <= 0) goto Lbe
            byte[] r15 = r14.buf
            if (r3 == 0) goto Lb4
            if (r5 != 0) goto L96
            int r2 = r0 + 1
        L93:
            r9 = r15
            r10 = r2
            goto Lb6
        L96:
            int r4 = r4 - r5
            byte[] r15 = new byte[r4]
            int r1 = r14.lineEnd
            int r0 = r0 + 1
            r3 = r2
        L9e:
            if (r0 >= r1) goto L93
            byte[] r5 = r14.buf
            r9 = r5[r0]
            int r10 = r3 + 1
            r15[r3] = r9
            if (r9 != r7) goto Lb1
            int r3 = r0 + 1
            r5 = r5[r3]
            if (r5 != r7) goto Lb1
            r0 = r3
        Lb1:
            int r0 = r0 + r13
            r3 = r10
            goto L9e
        Lb4:
            r9 = r15
            r10 = r0
        Lb6:
            r11 = r4
            int r7 = r14.rowCount
            java.nio.charset.Charset r12 = r14.charset
            r6.accept(r7, r8, r9, r10, r11, r12)
        Lbe:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.support.csv.CSVReaderUTF8.readLineEscaped(com.alibaba.fastjson2.reader.ByteArrayValueConsumer):void");
    }
}
