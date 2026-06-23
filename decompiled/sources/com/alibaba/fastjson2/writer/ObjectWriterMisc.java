package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import org.apache.http.cookie.ClientCookie;

/* JADX INFO: loaded from: classes.dex */
final class ObjectWriterMisc implements ObjectWriter {
    static final ObjectWriterMisc INSTANCE = new ObjectWriterMisc();

    ObjectWriterMisc() {
    }

    @Override // com.alibaba.fastjson2.writer.ObjectWriter
    public void write(JSONWriter jSONWriter, Object obj, Object obj2, Type type, long j) {
        String name;
        String strPattern;
        if (obj == null) {
            jSONWriter.writeNull();
            return;
        }
        name = obj.getClass().getName();
        name.hashCode();
        switch (name) {
            case "java.util.regex.Pattern":
                strPattern = ((Pattern) obj).pattern();
                break;
            case "net.sf.json.JSONNull":
                jSONWriter.writeNull();
                return;
            case "java.net.Inet6Address":
            case "java.net.Inet4Address":
                strPattern = ((InetAddress) obj).getHostName();
                break;
            case "com.fasterxml.jackson.databind.node.ArrayNode":
                String string = obj.toString();
                if (jSONWriter.isUTF8()) {
                    jSONWriter.writeRaw(string.getBytes(StandardCharsets.UTF_8));
                    return;
                } else {
                    jSONWriter.writeRaw(string);
                    return;
                }
            case "java.text.SimpleDateFormat":
                strPattern = ((SimpleDateFormat) obj).toPattern();
                break;
            case "java.net.InetSocketAddress":
                InetSocketAddress inetSocketAddress = (InetSocketAddress) obj;
                jSONWriter.startObject();
                jSONWriter.writeName("address");
                jSONWriter.writeColon();
                jSONWriter.writeAny(inetSocketAddress.getAddress());
                jSONWriter.writeName(ClientCookie.PORT_ATTR);
                jSONWriter.writeColon();
                jSONWriter.writeInt32(inetSocketAddress.getPort());
                jSONWriter.endObject();
                return;
            default:
                throw new JSONException("not support class : " + name);
        }
        jSONWriter.writeString(strPattern);
    }
}
