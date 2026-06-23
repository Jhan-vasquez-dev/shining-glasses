package com.alibaba.fastjson2.stream;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class JSONStreamReaderUTF16<T> extends JSONStreamReader<T> {
    char[] buf;
    final JSONReader.Context context;
    final Reader input;

    JSONStreamReaderUTF16(Reader reader, ObjectReaderAdapter objectReaderAdapter) {
        super(objectReaderAdapter);
        this.input = reader;
        this.context = JSONFactory.createReadContext();
    }

    JSONStreamReaderUTF16(Reader reader, Type[] typeArr) {
        super(typeArr);
        this.input = reader;
        this.context = JSONFactory.createReadContext();
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0050  */
    @Override // com.alibaba.fastjson2.stream.StreamReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected boolean seekLine() throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 294
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.stream.JSONStreamReaderUTF16.seekLine():boolean");
    }

    @Override // com.alibaba.fastjson2.stream.StreamReader
    public <T> T readLineObject() {
        try {
            if (this.inputEnd) {
                return null;
            }
            if (this.input == null && this.off >= this.end) {
                return null;
            }
            if (!seekLine()) {
                return null;
            }
            JSONReader jSONReaderOf = JSONReader.of(this.buf, this.lineStart, this.lineEnd - this.lineStart, this.context);
            if (this.objectReader != null) {
                return this.objectReader.readObject(jSONReaderOf, null, null, this.features);
            }
            if (jSONReaderOf.isArray() && this.types != null && this.types.length != 0) {
                return (T) jSONReaderOf.readList(this.types);
            }
            return (T) jSONReaderOf.readAny();
        } catch (IOException e) {
            throw new JSONException("seekLine error", e);
        }
    }
}
