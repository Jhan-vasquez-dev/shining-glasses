package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

/* JADX INFO: loaded from: classes.dex */
abstract class FieldWriterDate<T> extends FieldWriter<T> {
    protected ObjectWriter dateWriter;
    final boolean formatISO8601;
    final boolean formatMillis;
    final boolean formatUnixTime;
    protected DateTimeFormatter formatter;
    final boolean formatyyyyMMdd8;
    final boolean formatyyyyMMddhhmmss14;
    final boolean formatyyyyMMddhhmmss19;

    /* JADX WARN: Removed duplicated region for block: B:40:0x0088  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected FieldWriterDate(java.lang.String r13, int r14, long r15, java.lang.String r17, java.lang.String r18, java.lang.reflect.Type r19, java.lang.Class r20, java.lang.reflect.Field r21, java.lang.reflect.Method r22) {
        /*
            r12 = this;
            r6 = 0
            r0 = r12
            r1 = r13
            r2 = r14
            r3 = r15
            r5 = r17
            r7 = r18
            r8 = r19
            r9 = r20
            r10 = r21
            r11 = r22
            r0.<init>(r1, r2, r3, r5, r6, r7, r8, r9, r10, r11)
            r13 = 0
            if (r5 == 0) goto L88
            r5.hashCode()
            int r14 = r5.hashCode()
            r1 = 1
            r2 = -1
            switch(r14) {
                case -1074095546: goto L5b;
                case -288020395: goto L50;
                case -276306848: goto L45;
                case 1333195168: goto L3a;
                case 1349114208: goto L2f;
                case 2095190916: goto L24;
                default: goto L23;
            }
        L23:
            goto L65
        L24:
            java.lang.String r14 = "iso8601"
            boolean r14 = r5.equals(r14)
            if (r14 != 0) goto L2d
            goto L65
        L2d:
            r2 = 5
            goto L65
        L2f:
            java.lang.String r14 = "yyyyMMddHHmmss"
            boolean r14 = r5.equals(r14)
            if (r14 != 0) goto L38
            goto L65
        L38:
            r2 = 4
            goto L65
        L3a:
            java.lang.String r14 = "yyyy-MM-dd HH:mm:ss"
            boolean r14 = r5.equals(r14)
            if (r14 != 0) goto L43
            goto L65
        L43:
            r2 = 3
            goto L65
        L45:
            java.lang.String r14 = "yyyyMMdd"
            boolean r14 = r5.equals(r14)
            if (r14 != 0) goto L4e
            goto L65
        L4e:
            r2 = 2
            goto L65
        L50:
            java.lang.String r14 = "unixtime"
            boolean r14 = r5.equals(r14)
            if (r14 != 0) goto L59
            goto L65
        L59:
            r2 = r1
            goto L65
        L5b:
            java.lang.String r14 = "millis"
            boolean r14 = r5.equals(r14)
            if (r14 != 0) goto L64
            goto L65
        L64:
            r2 = r13
        L65:
            switch(r2) {
                case 0: goto L81;
                case 1: goto L7c;
                case 2: goto L77;
                case 3: goto L71;
                case 4: goto L6c;
                case 5: goto L69;
                default: goto L68;
            }
        L68:
            goto L88
        L69:
            r14 = r13
            r2 = r14
            goto L8b
        L6c:
            r14 = r13
            r2 = r14
            r4 = r2
            r3 = r1
            goto L86
        L71:
            r14 = r13
            r2 = r14
            r3 = r2
            r4 = r1
            r1 = r3
            goto L8d
        L77:
            r14 = r13
            r3 = r14
            r4 = r3
            r2 = r1
            goto L86
        L7c:
            r2 = r13
            r3 = r2
            r4 = r3
            r14 = r1
            goto L86
        L81:
            r14 = r13
            r2 = r14
            r3 = r2
            r4 = r3
            r13 = r1
        L86:
            r1 = r4
            goto L8d
        L88:
            r14 = r13
            r1 = r14
            r2 = r1
        L8b:
            r3 = r2
            r4 = r3
        L8d:
            r12.formatMillis = r13
            r12.formatISO8601 = r1
            r12.formatUnixTime = r14
            r12.formatyyyyMMdd8 = r2
            r12.formatyyyyMMddhhmmss14 = r3
            r12.formatyyyyMMddhhmmss19 = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.FieldWriterDate.<init>(java.lang.String, int, long, java.lang.String, java.lang.String, java.lang.reflect.Type, java.lang.Class, java.lang.reflect.Field, java.lang.reflect.Method):void");
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean isDateFormatMillis() {
        return this.formatMillis;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public boolean isDateFormatISO8601() {
        return this.formatISO8601;
    }

    public DateTimeFormatter getFormatter() {
        if (this.formatter == null && this.format != null && !this.formatMillis && !this.formatISO8601 && !this.formatUnixTime) {
            this.formatter = DateTimeFormatter.ofPattern(this.format);
        }
        return this.formatter;
    }

    @Override // com.alibaba.fastjson2.writer.FieldWriter
    public ObjectWriter getObjectWriter(JSONWriter jSONWriter, Class cls) {
        if (cls == this.fieldClass) {
            ObjectWriterProvider objectWriterProvider = jSONWriter.context.provider;
            if (this.dateWriter == null) {
                if ((objectWriterProvider.userDefineMask & 16) != 0) {
                    this.dateWriter = objectWriterProvider.getObjectWriter((Type) cls, cls, false);
                } else {
                    if (this.format == null) {
                        ObjectWriterImplDate objectWriterImplDate = ObjectWriterImplDate.INSTANCE;
                        this.dateWriter = objectWriterImplDate;
                        return objectWriterImplDate;
                    }
                    ObjectWriterImplDate objectWriterImplDate2 = new ObjectWriterImplDate(this.format, null);
                    this.dateWriter = objectWriterImplDate2;
                    return objectWriterImplDate2;
                }
            }
            return this.dateWriter;
        }
        return jSONWriter.getObjectWriter(cls);
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00f2  */
    @Override // com.alibaba.fastjson2.writer.FieldWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void writeDate(com.alibaba.fastjson2.JSONWriter r35, long r36) {
        /*
            Method dump skipped, instruction units count: 613
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.writer.FieldWriterDate.writeDate(com.alibaba.fastjson2.JSONWriter, long):void");
    }
}
