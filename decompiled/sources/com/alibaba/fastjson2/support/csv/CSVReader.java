package com.alibaba.fastjson2.support.csv;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ByteArrayValueConsumer;
import com.alibaba.fastjson2.reader.CharArrayValueConsumer;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.stream.StreamReader;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/* JADX INFO: loaded from: classes.dex */
public abstract class CSVReader<T> extends StreamReader<T> implements Closeable {
    protected Class<T> objectClass;
    private boolean objectSupport;
    boolean quote;

    public abstract boolean isEnd();

    public abstract void readAll();

    public abstract void readAll(int i);

    public abstract void readLineObjectAll(boolean z, Consumer<T> consumer);

    protected abstract Object[] readLineValues(boolean z);

    public abstract void statAll();

    public abstract void statAll(int i);

    CSVReader() {
        this.objectSupport = true;
    }

    CSVReader(Class<T> cls) {
        this.objectSupport = true;
        this.objectClass = cls;
    }

    public CSVReader(Type[] typeArr) {
        super(typeArr);
        this.objectSupport = false;
    }

    public void config(StreamReader.Feature... featureArr) {
        for (StreamReader.Feature feature : featureArr) {
            this.features |= feature.mask;
        }
    }

    public void config(StreamReader.Feature feature, boolean z) {
        if (z) {
            this.features = feature.mask | this.features;
        } else {
            this.features = (~feature.mask) & this.features;
        }
    }

    public static <T> CSVReader<T> of(Reader reader, Class<T> cls) {
        return new CSVReaderUTF16(reader, cls);
    }

    public static <T> CSVReader of(String str, Class<T> cls) {
        if (JDKUtils.JVM_VERSION > 8 && JDKUtils.STRING_VALUE != null) {
            try {
                if (JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
                    byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
                    return new CSVReaderUTF8(bArrApply, 0, bArrApply.length, StandardCharsets.ISO_8859_1, cls);
                }
            } catch (Exception unused) {
                throw new JSONException("unsafe get String.coder error");
            }
        }
        char[] charArray = JDKUtils.getCharArray(str);
        return new CSVReaderUTF16(charArray, 0, charArray.length, cls);
    }

    public static <T> CSVReader<T> of(char[] cArr, Class<T> cls) {
        return new CSVReaderUTF16(cArr, 0, cArr.length, cls);
    }

    public static <T> CSVReader<T> of(byte[] bArr, Class<T> cls) {
        return of(bArr, 0, bArr.length, StandardCharsets.UTF_8, cls);
    }

    public static CSVReader of(File file, Type... typeArr) throws IOException {
        return new CSVReaderUTF8(Files.newInputStream(file.toPath(), new OpenOption[0]), StandardCharsets.UTF_8, typeArr);
    }

    public static CSVReader of(File file, ByteArrayValueConsumer byteArrayValueConsumer) throws IOException {
        return of(file, StandardCharsets.UTF_8, byteArrayValueConsumer);
    }

    public static CSVReader of(File file, Charset charset, ByteArrayValueConsumer byteArrayValueConsumer) throws IOException {
        if (charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            throw new JSONException("not support charset : " + charset);
        }
        return new CSVReaderUTF8(Files.newInputStream(file.toPath(), new OpenOption[0]), charset, byteArrayValueConsumer);
    }

    public static CSVReader of(File file, CharArrayValueConsumer charArrayValueConsumer) throws IOException {
        return of(file, StandardCharsets.UTF_8, charArrayValueConsumer);
    }

    public static CSVReader of(File file, Charset charset, CharArrayValueConsumer charArrayValueConsumer) throws IOException {
        return new CSVReaderUTF16(new InputStreamReader(Files.newInputStream(file.toPath(), new OpenOption[0]), charset), charArrayValueConsumer);
    }

    public static CSVReader of(File file, Charset charset, Type... typeArr) throws IOException {
        if (JDKUtils.JVM_VERSION == 8 || charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return new CSVReaderUTF16(new InputStreamReader(Files.newInputStream(file.toPath(), new OpenOption[0]), charset), typeArr);
        }
        return new CSVReaderUTF8(Files.newInputStream(file.toPath(), new OpenOption[0]), charset, typeArr);
    }

    public static <T> CSVReader<T> of(File file, Class<T> cls) throws IOException {
        return of(file, StandardCharsets.UTF_8, cls);
    }

    public static <T> CSVReader<T> of(File file, Charset charset, Class<T> cls) throws IOException {
        if (JDKUtils.JVM_VERSION == 8 || charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return new CSVReaderUTF16(new InputStreamReader(Files.newInputStream(file.toPath(), new OpenOption[0]), charset), cls);
        }
        return new CSVReaderUTF8(Files.newInputStream(file.toPath(), new OpenOption[0]), charset, cls);
    }

    public static CSVReader of(InputStream inputStream, Type... typeArr) throws IOException {
        return of(inputStream, StandardCharsets.UTF_8, typeArr);
    }

    public static <T> CSVReader<T> of(InputStream inputStream, Class<T> cls) {
        return of(inputStream, StandardCharsets.UTF_8, cls);
    }

    public static <T> CSVReader<T> of(InputStream inputStream, Charset charset, Class<T> cls) {
        if (JDKUtils.JVM_VERSION == 8 || charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return new CSVReaderUTF16(new InputStreamReader(inputStream, charset), cls);
        }
        return new CSVReaderUTF8(inputStream, charset, cls);
    }

    public static CSVReader of(InputStream inputStream, Charset charset, Type... typeArr) {
        if (JDKUtils.JVM_VERSION == 8 || charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return new CSVReaderUTF16(new InputStreamReader(inputStream, charset), typeArr);
        }
        return new CSVReaderUTF8(inputStream, charset, typeArr);
    }

    public static CSVReader of(Reader reader, Type... typeArr) {
        return new CSVReaderUTF16(reader, typeArr);
    }

    public static CSVReader of(String str, Type... typeArr) {
        if (JDKUtils.JVM_VERSION > 8 && JDKUtils.STRING_VALUE != null) {
            try {
                if (JDKUtils.STRING_CODER.applyAsInt(str) == 0) {
                    byte[] bArrApply = JDKUtils.STRING_VALUE.apply(str);
                    return new CSVReaderUTF8(bArrApply, 0, bArrApply.length, typeArr);
                }
            } catch (Exception unused) {
                throw new JSONException("unsafe get String.coder error");
            }
        }
        char[] charArray = JDKUtils.getCharArray(str);
        return new CSVReaderUTF16(charArray, 0, charArray.length, typeArr);
    }

    public static CSVReader of(char[] cArr, Type... typeArr) {
        return new CSVReaderUTF16(cArr, 0, cArr.length, typeArr);
    }

    public static <T> CSVReader<T> of(char[] cArr, int i, int i2, CharArrayValueConsumer charArrayValueConsumer) {
        return new CSVReaderUTF16(cArr, i, i2, charArrayValueConsumer);
    }

    public static CSVReader of(byte[] bArr, Type... typeArr) {
        return new CSVReaderUTF8(bArr, 0, bArr.length, typeArr);
    }

    public static CSVReader of(byte[] bArr, ByteArrayValueConsumer byteArrayValueConsumer) {
        return of(bArr, 0, bArr.length, StandardCharsets.UTF_8, byteArrayValueConsumer);
    }

    public static <T> CSVReader<T> of(byte[] bArr, int i, int i2, Charset charset, ByteArrayValueConsumer byteArrayValueConsumer) {
        return new CSVReaderUTF8(bArr, i, i2, charset, byteArrayValueConsumer);
    }

    public static <T> CSVReader<T> of(byte[] bArr, Charset charset, Class<T> cls) {
        return of(bArr, 0, bArr.length, charset, cls);
    }

    public static <T> CSVReader<T> of(byte[] bArr, int i, int i2, Class<T> cls) {
        return new CSVReaderUTF8(bArr, i, i2, StandardCharsets.UTF_8, cls);
    }

    public static <T> CSVReader<T> of(byte[] bArr, int i, int i2, Charset charset, Class<T> cls) {
        if (charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            char[] cArr = new char[i2];
            return new CSVReaderUTF16(cArr, 0, IOUtils.decodeUTF8(bArr, i, i2, cArr), cls);
        }
        return new CSVReaderUTF8(bArr, i, i2, charset, cls);
    }

    public static <T> CSVReader<T> of(char[] cArr, int i, int i2, Class<T> cls) {
        return new CSVReaderUTF16(cArr, i, i2, cls);
    }

    public void skipLines(int i) throws IOException {
        if (i < 0) {
            throw new IllegalArgumentException();
        }
        for (int i2 = 0; i2 < i; i2++) {
            seekLine();
        }
    }

    public List<String> readHeader() {
        this.objectSupport = true;
        final String[] strArr = (String[]) readLineValues(true);
        if (this.objectClass != null) {
            ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
            boolean z = (this.features & JSONReader.Feature.FieldBased.mask) != 0;
            Type[] typeArr = new Type[strArr.length];
            ObjectReader[] objectReaderArr = new ObjectReader[strArr.length];
            FieldReader[] fieldReaderArr = new FieldReader[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                FieldReader fieldReaderCreateFieldReader = defaultObjectReaderProvider.createFieldReader(this.objectClass, strArr[i].trim(), this.features);
                if (fieldReaderCreateFieldReader != null) {
                    fieldReaderArr[i] = fieldReaderCreateFieldReader;
                    Type typeNonePrimitive = fieldReaderCreateFieldReader.fieldType;
                    if (typeNonePrimitive instanceof Class) {
                        Class cls = (Class) typeNonePrimitive;
                        if (cls.isPrimitive()) {
                            typeNonePrimitive = TypeUtils.nonePrimitive(cls);
                        }
                    }
                    typeArr[i] = typeNonePrimitive;
                    objectReaderArr[i] = defaultObjectReaderProvider.getObjectReader(typeNonePrimitive, z);
                } else {
                    typeArr[i] = String.class;
                }
            }
            this.types = typeArr;
            this.typeReaders = objectReaderArr;
            this.fieldReaders = fieldReaderArr;
            this.objectCreator = defaultObjectReaderProvider.createObjectCreator(this.objectClass, this.features);
        }
        this.columns = Arrays.asList(strArr);
        this.columnStats = new ArrayList();
        IntStream.range(0, strArr.length).forEach(new IntConsumer() { // from class: com.alibaba.fastjson2.support.csv.CSVReader$$ExternalSyntheticLambda0
            @Override // java.util.function.IntConsumer
            public final void accept(int i2) {
                this.f$0.m51lambda$readHeader$0$comalibabafastjson2supportcsvCSVReader(strArr, i2);
            }
        });
        if (this.rowCount == 1) {
            this.rowCount = this.lineTerminated ? 0 : -1;
        }
        return this.columns;
    }

    /* JADX INFO: renamed from: lambda$readHeader$0$com-alibaba-fastjson2-support-csv-CSVReader, reason: not valid java name */
    /* synthetic */ void m51lambda$readHeader$0$comalibabafastjson2supportcsvCSVReader(String[] strArr, int i) {
        this.columnStats.add(new StreamReader.ColumnStat(strArr[i]));
    }

    public List<String> getColumns() {
        return this.columns;
    }

    public String getColumn(int i) {
        if (this.columns == null || i >= this.columns.size()) {
            return null;
        }
        return this.columns.get(i);
    }

    public Type getColumnType(int i) {
        if (this.types == null || i >= this.types.length) {
            return null;
        }
        return this.types[i];
    }

    public List<StreamReader.ColumnStat> getColumnStats() {
        return this.columnStats;
    }

    public void readLineObjectAll(Consumer<T> consumer) {
        readLineObjectAll(true, consumer);
    }

    @Override // com.alibaba.fastjson2.stream.StreamReader
    public T readLineObject() {
        if (!this.objectSupport) {
            throw new UnsupportedOperationException("this method should not be called, try specify objectClass or method readLineValues instead ?");
        }
        if (this.inputEnd && this.off == this.end) {
            return null;
        }
        if (this.fieldReaders == null) {
            ObjectReaderProvider defaultObjectReaderProvider = JSONFactory.getDefaultObjectReaderProvider();
            if (this.objectClass != null) {
                ObjectReader objectReader = defaultObjectReaderProvider.getObjectReader(this.objectClass, (this.features & JSONReader.Feature.FieldBased.mask) != 0);
                if (objectReader instanceof ObjectReaderAdapter) {
                    this.fieldReaders = ((ObjectReaderAdapter) objectReader).getFieldReaders();
                    this.types = new Type[this.fieldReaders.length];
                    for (int i = 0; i < this.types.length; i++) {
                        this.types[i] = this.fieldReaders[i].fieldType;
                    }
                    this.objectCreator = defaultObjectReaderProvider.createObjectCreator(this.objectClass, this.features);
                } else {
                    throw new JSONException("not support operation : " + this.objectClass);
                }
            } else {
                throw new JSONException("not support operation, objectClass is null");
            }
        }
        if (this.objectCreator == null) {
            throw new JSONException("not support operation, objectClass is null");
        }
        Object[] lineValues = readLineValues(false);
        if (lineValues == null) {
            return null;
        }
        if (this.fieldReaders != null) {
            T t = (T) this.objectCreator.get();
            for (int i2 = 0; i2 < this.fieldReaders.length; i2++) {
                FieldReader fieldReader = this.fieldReaders[i2];
                if (fieldReader != null) {
                    fieldReader.accept(t, lineValues[i2]);
                }
            }
            return t;
        }
        throw new JSONException("not support operation, objectClass is null");
    }

    public final Object[] readLineValues() {
        return readLineValues(false);
    }

    public final String[] readLine() {
        return (String[]) readLineValues(true);
    }

    public static int rowCount(String str, StreamReader.Feature... featureArr) {
        CSVReaderUTF8 cSVReaderUTF8 = new CSVReaderUTF8(featureArr);
        cSVReaderUTF8.rowCount(str, str.length());
        return cSVReaderUTF8.rowCount();
    }

    public static int rowCount(byte[] bArr, StreamReader.Feature... featureArr) {
        CSVReaderUTF8 cSVReaderUTF8 = new CSVReaderUTF8(featureArr);
        cSVReaderUTF8.rowCount(bArr, bArr.length);
        return cSVReaderUTF8.rowCount();
    }

    public static int rowCount(char[] cArr, StreamReader.Feature... featureArr) {
        CSVReaderUTF16 cSVReaderUTF16 = new CSVReaderUTF16(featureArr);
        cSVReaderUTF16.rowCount(cArr, cArr.length);
        return cSVReaderUTF16.rowCount();
    }

    public static int rowCount(File file) throws IOException {
        if (!file.exists()) {
            return -1;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            int iRowCount = rowCount(fileInputStream);
            fileInputStream.close();
            return iRowCount;
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static int rowCount(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[524288];
        CSVReaderUTF8 cSVReaderUTF8 = new CSVReaderUTF8(new StreamReader.Feature[0]);
        while (true) {
            int i = inputStream.read(bArr);
            if (i != -1) {
                cSVReaderUTF8.rowCount(bArr, i);
            } else {
                return cSVReaderUTF8.rowCount();
            }
        }
    }

    public int errorCount() {
        return this.errorCount;
    }

    public int rowCount() {
        return this.lineTerminated ? this.rowCount : this.rowCount + 1;
    }

    void rowCount(String str, int i) {
        this.lineTerminated = false;
        int i2 = 0;
        while (i2 < i) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\"') {
                this.lineSize++;
                if (this.quote) {
                    int i3 = i2 + 1;
                    if (i3 >= i) {
                        return;
                    }
                    if (str.charAt(i3) == '\"') {
                        i2 = i3;
                    } else {
                        this.quote = false;
                    }
                } else {
                    this.quote = true;
                }
            } else if (this.quote) {
                this.lineSize++;
            } else if (cCharAt == '\n') {
                if (this.lineSize > 0 || (this.features & StreamReader.Feature.IgnoreEmptyLine.mask) == 0) {
                    this.rowCount++;
                    this.lineSize = 0;
                }
                this.lineTerminated = i2 + 1 == i;
            } else if (cCharAt == '\r') {
                this.lineTerminated = true;
                if (this.lineSize > 0 || (this.features & StreamReader.Feature.IgnoreEmptyLine.mask) == 0) {
                    this.rowCount++;
                }
                this.lineSize = 0;
                int i4 = i2 + 1;
                if (i4 >= i) {
                    return;
                }
                if (str.charAt(i4) == '\n') {
                    i2 = i4;
                }
                this.lineTerminated = i2 + 1 == i;
            } else {
                this.lineSize++;
            }
            i2++;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x002c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void rowCount(byte[] r12, int r13) {
        /*
            r11 = this;
            r0 = 0
            r11.lineTerminated = r0
            r1 = r0
        L4:
            if (r1 >= r13) goto Lb6
            int r2 = r1 + 4
            r3 = 34
            r4 = 1
            if (r2 >= r13) goto L2c
            r2 = r12[r1]
            int r5 = r1 + 1
            r5 = r12[r5]
            int r6 = r1 + 2
            r6 = r12[r6]
            int r7 = r1 + 3
            r8 = r12[r7]
            if (r2 <= r3) goto L2c
            if (r5 <= r3) goto L2c
            if (r6 <= r3) goto L2c
            if (r8 <= r3) goto L2c
            int r1 = r11.lineSize
            int r1 = r1 + 4
            r11.lineSize = r1
            r1 = r7
            goto Lb3
        L2c:
            r2 = r12[r1]
            if (r2 != r3) goto L4e
            int r2 = r11.lineSize
            int r2 = r2 + r4
            r11.lineSize = r2
            boolean r2 = r11.quote
            if (r2 != 0) goto L3d
            r11.quote = r4
            goto Lb3
        L3d:
            int r2 = r1 + 1
            if (r2 < r13) goto L43
            goto Lb6
        L43:
            r5 = r12[r2]
            if (r5 != r3) goto L4a
            r1 = r2
            goto Lb3
        L4a:
            r11.quote = r0
            goto Lb3
        L4e:
            boolean r3 = r11.quote
            if (r3 == 0) goto L58
            int r2 = r11.lineSize
            int r2 = r2 + r4
            r11.lineSize = r2
            goto Lb3
        L58:
            r5 = 0
            r3 = 10
            if (r2 != r3) goto L7e
            int r2 = r11.lineSize
            if (r2 > 0) goto L6d
            long r2 = r11.features
            com.alibaba.fastjson2.stream.StreamReader$Feature r7 = com.alibaba.fastjson2.stream.StreamReader.Feature.IgnoreEmptyLine
            long r7 = r7.mask
            long r2 = r2 & r7
            int r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r2 != 0) goto L72
        L6d:
            int r2 = r11.rowCount
            int r2 = r2 + r4
            r11.rowCount = r2
        L72:
            r11.lineSize = r0
            int r2 = r1 + 1
            if (r2 != r13) goto L7a
            r2 = r4
            goto L7b
        L7a:
            r2 = r0
        L7b:
            r11.lineTerminated = r2
            goto Lb3
        L7e:
            r7 = 13
            if (r2 != r7) goto Lae
            int r2 = r11.lineSize
            if (r2 > 0) goto L91
            long r7 = r11.features
            com.alibaba.fastjson2.stream.StreamReader$Feature r2 = com.alibaba.fastjson2.stream.StreamReader.Feature.IgnoreEmptyLine
            long r9 = r2.mask
            long r7 = r7 & r9
            int r2 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r2 != 0) goto L96
        L91:
            int r2 = r11.rowCount
            int r2 = r2 + r4
            r11.rowCount = r2
        L96:
            r11.lineTerminated = r4
            r11.lineSize = r0
            int r2 = r1 + 1
            if (r2 < r13) goto L9f
            goto Lb6
        L9f:
            r5 = r12[r2]
            if (r5 != r3) goto La4
            r1 = r2
        La4:
            int r2 = r1 + 1
            if (r2 != r13) goto Laa
            r2 = r4
            goto Lab
        Laa:
            r2 = r0
        Lab:
            r11.lineTerminated = r2
            goto Lb3
        Lae:
            int r2 = r11.lineSize
            int r2 = r2 + r4
            r11.lineSize = r2
        Lb3:
            int r1 = r1 + r4
            goto L4
        Lb6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.support.csv.CSVReader.rowCount(byte[], int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x002c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void rowCount(char[] r12, int r13) {
        /*
            r11 = this;
            r0 = 0
            r11.lineTerminated = r0
            r1 = r0
        L4:
            if (r1 >= r13) goto Lb7
            int r2 = r1 + 4
            r3 = 34
            r4 = 1
            if (r2 >= r13) goto L2c
            char r2 = r12[r1]
            int r5 = r1 + 1
            char r5 = r12[r5]
            int r6 = r1 + 2
            char r6 = r12[r6]
            int r7 = r1 + 3
            char r8 = r12[r7]
            if (r2 <= r3) goto L2c
            if (r5 <= r3) goto L2c
            if (r6 <= r3) goto L2c
            if (r8 <= r3) goto L2c
            int r1 = r11.lineSize
            int r1 = r1 + 4
            r11.lineSize = r1
            r1 = r7
            goto Lb4
        L2c:
            char r2 = r12[r1]
            if (r2 != r3) goto L4e
            int r2 = r11.lineSize
            int r2 = r2 + r4
            r11.lineSize = r2
            boolean r2 = r11.quote
            if (r2 != 0) goto L3d
            r11.quote = r4
            goto Lb4
        L3d:
            int r2 = r1 + 1
            if (r2 < r13) goto L43
            goto Lb7
        L43:
            char r5 = r12[r2]
            if (r5 != r3) goto L4a
            r1 = r2
            goto Lb4
        L4a:
            r11.quote = r0
            goto Lb4
        L4e:
            boolean r3 = r11.quote
            if (r3 == 0) goto L58
            int r2 = r11.lineSize
            int r2 = r2 + r4
            r11.lineSize = r2
            goto Lb4
        L58:
            r5 = 0
            r3 = 10
            if (r2 != r3) goto L7e
            int r2 = r11.lineSize
            if (r2 > 0) goto L6d
            long r2 = r11.features
            com.alibaba.fastjson2.stream.StreamReader$Feature r7 = com.alibaba.fastjson2.stream.StreamReader.Feature.IgnoreEmptyLine
            long r7 = r7.mask
            long r2 = r2 & r7
            int r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r2 != 0) goto L72
        L6d:
            int r2 = r11.rowCount
            int r2 = r2 + r4
            r11.rowCount = r2
        L72:
            r11.lineSize = r0
            int r2 = r1 + 1
            if (r2 != r13) goto L7a
            r2 = r4
            goto L7b
        L7a:
            r2 = r0
        L7b:
            r11.lineTerminated = r2
            goto Lb4
        L7e:
            r7 = 13
            if (r2 == r7) goto L94
            long r7 = r11.features
            com.alibaba.fastjson2.stream.StreamReader$Feature r2 = com.alibaba.fastjson2.stream.StreamReader.Feature.IgnoreEmptyLine
            long r9 = r2.mask
            long r7 = r7 & r9
            int r2 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r2 != 0) goto L8e
            goto L94
        L8e:
            int r2 = r11.lineSize
            int r2 = r2 + r4
            r11.lineSize = r2
            goto Lb4
        L94:
            int r2 = r11.lineSize
            if (r2 <= 0) goto L9d
            int r2 = r11.rowCount
            int r2 = r2 + r4
            r11.rowCount = r2
        L9d:
            r11.lineTerminated = r4
            r11.lineSize = r0
            int r2 = r1 + 1
            if (r2 < r13) goto La6
            goto Lb7
        La6:
            char r5 = r12[r2]
            if (r5 != r3) goto Lab
            r1 = r2
        Lab:
            int r2 = r1 + 1
            if (r2 != r13) goto Lb1
            r2 = r4
            goto Lb2
        Lb1:
            r2 = r0
        Lb2:
            r11.lineTerminated = r2
        Lb4:
            int r1 = r1 + r4
            goto L4
        Lb7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.support.csv.CSVReader.rowCount(char[], int):void");
    }

    protected Object error(int i, Exception exc) {
        String str;
        this.errorCount++;
        getColumnStat(i).errors++;
        String str2 = null;
        if ((this.features & StreamReader.Feature.ErrorAsNull.mask) != 0) {
            return null;
        }
        String str3 = "read csv error, line " + this.rowCount + ", column ";
        if (this.columns != null && i < this.columns.size()) {
            str2 = this.columns.get(i);
        }
        if (str2 != null && !str2.isEmpty()) {
            str = str3 + str2;
        } else {
            str = str3 + i;
        }
        throw new JSONException(str, exc);
    }

    public StreamReader.ColumnStat getColumnStat(String str) {
        if (this.columnStats == null) {
            return null;
        }
        for (StreamReader.ColumnStat columnStat : this.columnStats) {
            if (str.equals(columnStat.name)) {
                return columnStat;
            }
        }
        return null;
    }

    public StreamReader.ColumnStat getColumnStat(int i) {
        if (this.columnStats == null) {
            this.columnStats = new ArrayList();
        }
        if (i >= this.columnStats.size()) {
            int size = this.columnStats.size();
            StreamReader.ColumnStat columnStat = null;
            while (size <= i) {
                StreamReader.ColumnStat columnStat2 = new StreamReader.ColumnStat((this.columns == null || i >= this.columns.size()) ? null : this.columns.get(i));
                this.columnStats.add(columnStat2);
                size++;
                columnStat = columnStat2;
            }
            return columnStat;
        }
        return this.columnStats.get(i);
    }

    public List<String[]> readLineAll() {
        ArrayList arrayList = new ArrayList();
        while (true) {
            String[] line = readLine();
            if (line == null) {
                return arrayList;
            }
            arrayList.add(line);
        }
    }

    public List<T> readLineObjectAll() {
        ArrayList arrayList = new ArrayList();
        while (true) {
            T lineObject = readLineObject();
            if (lineObject == null) {
                return arrayList;
            }
            arrayList.add(lineObject);
        }
    }

    public boolean isObjectSupport() {
        return this.objectSupport;
    }
}
