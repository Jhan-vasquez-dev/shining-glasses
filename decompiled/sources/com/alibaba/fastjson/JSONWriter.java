package com.alibaba.fastjson;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSONWriter;
import java.io.IOException;
import java.io.Writer;

/* JADX INFO: loaded from: classes.dex */
public class JSONWriter {
    private final Writer out;
    final com.alibaba.fastjson2.JSONWriter raw = com.alibaba.fastjson2.JSONWriter.ofUTF8();

    public JSONWriter(Writer writer) {
        this.out = writer;
    }

    /* JADX INFO: renamed from: com.alibaba.fastjson.JSONWriter$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature;

        static {
            int[] iArr = new int[SerializerFeature.values().length];
            $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature = iArr;
            try {
                iArr[SerializerFeature.UseISO8601DateFormat.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteMapNullValue.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNullListAsEmpty.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNullStringAsEmpty.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNullNumberAsZero.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNullBooleanAsFalse.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.BrowserCompatible.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteClassName.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNonStringValueAsString.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteEnumUsingToString.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.NotWriteRootClassName.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.IgnoreErrorGetter.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteDateUseDateFormat.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.BeanToArray.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
        }
    }

    public void config(SerializerFeature serializerFeature, boolean z) {
        JSONWriter.Context context = this.raw.getContext();
        switch (AnonymousClass1.$SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[serializerFeature.ordinal()]) {
            case 1:
                if (z) {
                    context.setDateFormat("iso8601");
                }
                break;
            case 2:
                context.config(JSONWriter.Feature.WriteNulls, z);
                break;
            case 3:
                context.config(JSONWriter.Feature.WriteNullListAsEmpty, z);
                break;
            case 4:
                context.config(JSONWriter.Feature.WriteNullStringAsEmpty, z);
                break;
            case 5:
                context.config(JSONWriter.Feature.WriteNullNumberAsZero, z);
                break;
            case 6:
                context.config(JSONWriter.Feature.WriteNullBooleanAsFalse, z);
                break;
            case 7:
                context.config(JSONWriter.Feature.BrowserCompatible, z);
                break;
            case 8:
                context.config(JSONWriter.Feature.WriteClassName, z);
                break;
            case 9:
                context.config(JSONWriter.Feature.WriteNonStringValueAsString, z);
                break;
            case 10:
                context.config(JSONWriter.Feature.WriteEnumUsingToString, z);
                break;
            case 11:
                context.config(JSONWriter.Feature.NotWriteRootClassName, z);
                break;
            case 12:
                context.config(JSONWriter.Feature.IgnoreErrorGetter, z);
                break;
            case 13:
                if (z) {
                    context.setDateFormat(JSON.DEFAULT_DATE_FORMAT);
                }
                break;
            case 14:
                if (z) {
                    context.config(JSONWriter.Feature.BeanToArray);
                }
                break;
        }
    }

    public void writeObject(Object obj) {
        this.raw.writeAny(obj);
    }

    public void flush() throws IOException {
        this.raw.flushTo(this.out);
        this.out.flush();
    }

    public void close() {
        this.raw.close();
        try {
            this.out.close();
        } catch (IOException unused) {
        }
    }
}
