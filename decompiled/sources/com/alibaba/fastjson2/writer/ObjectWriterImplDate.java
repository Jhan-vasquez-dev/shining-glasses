package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.codec.DateTimeCodec;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class ObjectWriterImplDate extends DateTimeCodec implements ObjectWriter {
    static final ObjectWriterImplDate INSTANCE = new ObjectWriterImplDate(null, null);
    static final char[] PREFIX_CHARS = "new Date(".toCharArray();
    static final byte[] PREFIX_BYTES = "new Date(".getBytes(StandardCharsets.UTF_8);
    static final char[] PREFIX_CHARS_SQL = "{\"@type\":\"java.sql.Date\",\"val\":".toCharArray();
    static final byte[] PREFIX_BYTES_SQL = "{\"@type\":\"java.sql.Date\",\"val\":".getBytes(StandardCharsets.UTF_8);

    public ObjectWriterImplDate(String str, Locale locale) {
        super(str, locale);
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void writeJSONB(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        if (obj == null) {
            jSONWriter.writeNull();
        } else {
            jSONWriter.writeMillis(((Date) obj).getTime());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x0131  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0169  */
    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void write(com.alibaba.fastjson2.JSONWriter r33, java.lang.Object r34, java.lang.Object r35, java.lang.reflect.Type r36, long r37) {
        /*
            Method dump skipped, instruction units count: 616
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.ObjectWriterImplDate.write(com.alibaba.fastjson2.JSONWriter, java.lang.Object, java.lang.Object, java.lang.reflect.Type, long):void");
    }
}
