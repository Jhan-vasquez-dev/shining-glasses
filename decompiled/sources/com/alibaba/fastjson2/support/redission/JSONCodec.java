package com.alibaba.fastjson2.support.redission;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

/* JADX INFO: loaded from: classes.dex */
public class JSONCodec extends BaseCodec {
    final JSONDecoder decoder;
    final JSONEncoder encoder;

    public JSONCodec(Type type) {
        this(JSONFactory.createWriteContext(), JSONFactory.createReadContext(), type, StandardCharsets.UTF_8);
    }

    public JSONCodec(Type type, Charset charset) {
        this(JSONFactory.createWriteContext(), JSONFactory.createReadContext(), type, charset);
    }

    public JSONCodec(JSONWriter.Context context, JSONReader.Context context2) {
        this(context, context2, null, StandardCharsets.UTF_8);
    }

    public JSONCodec(JSONWriter.Context context, JSONReader.Context context2, Charset charset) {
        this(context, context2, null, charset);
    }

    public JSONCodec(JSONWriter.Context context, JSONReader.Context context2, Type type, Charset charset) {
        this(new JSONEncoder(context, charset), new JSONDecoder(type, charset, context2));
    }

    JSONCodec(JSONEncoder jSONEncoder, JSONDecoder jSONDecoder) {
        this.encoder = jSONEncoder;
        this.decoder = jSONDecoder;
    }

    public Decoder<Object> getValueDecoder() {
        return this.decoder;
    }

    public Encoder getValueEncoder() {
        return this.encoder;
    }

    static final class JSONEncoder implements Encoder {
        final Charset charset;
        final JSONWriter.Context context;

        public JSONEncoder() {
            this(new JSONWriter.Context(new JSONWriter.Feature[0]));
        }

        public JSONEncoder(JSONWriter.Context context) {
            this(context, StandardCharsets.UTF_8);
        }

        public JSONEncoder(JSONWriter.Context context, Charset charset) {
            this.context = context;
            this.charset = charset;
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x0047 A[Catch: all -> 0x0063, TryCatch #1 {all -> 0x0063, blocks: (B:14:0x0039, B:16:0x0047, B:17:0x0049, B:13:0x0036), top: B:42:0x0036 }] */
        /* JADX WARN: Removed duplicated region for block: B:19:0x005f A[Catch: NumberFormatException -> 0x0073, NullPointerException -> 0x0075, TRY_ENTER, TRY_LEAVE, TryCatch #6 {NullPointerException -> 0x0075, NumberFormatException -> 0x0073, blocks: (B:19:0x005f, B:31:0x0072, B:30:0x006f, B:27:0x006a), top: B:44:0x0006, inners: #6 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public io.netty.buffer.ByteBuf encode(java.lang.Object r9) throws java.lang.Throwable {
            /*
                r8 = this;
                com.alibaba.fastjson2.JSONWriter$Context r0 = r8.context     // Catch: java.lang.NumberFormatException -> L77 java.lang.NullPointerException -> L79
                com.alibaba.fastjson2.JSONWriter r2 = com.alibaba.fastjson2.JSONWriter.of(r0)     // Catch: java.lang.NumberFormatException -> L77 java.lang.NullPointerException -> L79
                if (r9 != 0) goto Ld
                r2.writeNull()     // Catch: java.lang.Throwable -> L65
            Lb:
                r3 = r9
                goto L39
            Ld:
                r2.setRootObject(r9)     // Catch: java.lang.Throwable -> L65
                java.lang.Class r0 = r9.getClass()     // Catch: java.lang.Throwable -> L65
                java.lang.Class<com.alibaba.fastjson2.JSONObject> r1 = com.alibaba.fastjson2.JSONObject.class
                if (r0 != r1) goto L2b
                com.alibaba.fastjson2.JSONWriter$Context r1 = r8.context     // Catch: java.lang.Throwable -> L65
                long r3 = r1.getFeatures()     // Catch: java.lang.Throwable -> L65
                r5 = 0
                int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r1 != 0) goto L2b
                r0 = r9
                com.alibaba.fastjson2.JSONObject r0 = (com.alibaba.fastjson2.JSONObject) r0     // Catch: java.lang.Throwable -> L65
                r2.write(r0)     // Catch: java.lang.Throwable -> L65
                goto Lb
            L2b:
                com.alibaba.fastjson2.JSONWriter$Context r1 = r8.context     // Catch: java.lang.Throwable -> L65
                com.alibaba.fastjson2.writer.ObjectWriter r1 = r1.getObjectWriter(r0, r0)     // Catch: java.lang.Throwable -> L65
                r5 = 0
                r6 = 0
                r4 = 0
                r3 = r9
                r1.write(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L63
            L39:
                int r9 = r2.size()     // Catch: java.lang.Throwable -> L63
                com.alibaba.fastjson2.JSONWriter$Context r0 = r8.context     // Catch: java.lang.Throwable -> L63
                com.alibaba.fastjson2.JSONWriter$Feature r1 = com.alibaba.fastjson2.JSONWriter.Feature.OptimizedForAscii     // Catch: java.lang.Throwable -> L63
                boolean r0 = r0.isEnabled(r1)     // Catch: java.lang.Throwable -> L63
                if (r0 != 0) goto L49
                int r9 = r9 << 2
            L49:
                io.netty.buffer.ByteBufAllocator r0 = io.netty.buffer.ByteBufAllocator.DEFAULT     // Catch: java.lang.Throwable -> L63
                io.netty.buffer.ByteBuf r9 = r0.buffer(r9)     // Catch: java.lang.Throwable -> L63
                io.netty.buffer.ByteBufOutputStream r0 = new io.netty.buffer.ByteBufOutputStream     // Catch: java.lang.Throwable -> L63
                r0.<init>(r9)     // Catch: java.lang.Throwable -> L63
                java.nio.charset.Charset r9 = r8.charset     // Catch: java.lang.Throwable -> L63
                r2.flushTo(r0, r9)     // Catch: java.lang.Throwable -> L63
                io.netty.buffer.ByteBuf r9 = r0.buffer()     // Catch: java.lang.Throwable -> L63
                if (r2 == 0) goto L62
                r2.close()     // Catch: java.lang.NumberFormatException -> L73 java.lang.NullPointerException -> L75
            L62:
                return r9
            L63:
                r0 = move-exception
                goto L67
            L65:
                r0 = move-exception
                r3 = r9
            L67:
                r9 = r0
                if (r2 == 0) goto L72
                r2.close()     // Catch: java.lang.Throwable -> L6e
                goto L72
            L6e:
                r0 = move-exception
                r9.addSuppressed(r0)     // Catch: java.lang.NumberFormatException -> L73 java.lang.NullPointerException -> L75
            L72:
                throw r9     // Catch: java.lang.NumberFormatException -> L73 java.lang.NullPointerException -> L75
            L73:
                r0 = move-exception
                goto L7b
            L75:
                r0 = move-exception
                goto L7b
            L77:
                r0 = move-exception
                goto L7a
            L79:
                r0 = move-exception
            L7a:
                r3 = r9
            L7b:
                r9 = r0
                com.alibaba.fastjson2.JSONException r0 = new com.alibaba.fastjson2.JSONException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "JSON#toJSONString cannot serialize '"
                r1.<init>(r2)
                java.lang.StringBuilder r1 = r1.append(r3)
                java.lang.String r2 = "'"
                java.lang.StringBuilder r1 = r1.append(r2)
                java.lang.String r1 = r1.toString()
                r0.<init>(r1, r9)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.support.redission.JSONCodec.JSONEncoder.encode(java.lang.Object):io.netty.buffer.ByteBuf");
        }
    }

    static final class JSONDecoder implements Decoder<Object> {
        final Charset charset;
        final JSONReader.Context context;
        final Type valueType;

        public JSONDecoder(Type type) {
            this(type, JSONFactory.createReadContext());
        }

        public JSONDecoder(Type type, JSONReader.Context context) {
            this(type, StandardCharsets.UTF_8, context);
        }

        public JSONDecoder(Type type, Charset charset, JSONReader.Context context) {
            this.valueType = type;
            this.charset = charset;
            this.context = context;
        }

        public Object decode(ByteBuf byteBuf, State state) throws IOException {
            Object any;
            JSONReader jSONReaderOf = JSONReader.of((InputStream) new ByteBufInputStream(byteBuf), this.charset, this.context);
            try {
                Type type = this.valueType;
                if (type == null || type == Object.class) {
                    any = jSONReaderOf.readAny();
                } else {
                    any = jSONReaderOf.read(type);
                }
                if (jSONReaderOf != null) {
                    jSONReaderOf.close();
                }
                return any;
            } catch (Throwable th) {
                if (jSONReaderOf != null) {
                    try {
                        jSONReaderOf.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }
}
