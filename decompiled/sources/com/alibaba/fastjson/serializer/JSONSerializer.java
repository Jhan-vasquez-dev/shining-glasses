package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class JSONSerializer {
    SerialContext context;
    public final SerializeWriter out;
    final JSONWriter raw;

    public void println() {
    }

    public JSONSerializer() {
        this(new SerializeWriter());
    }

    public JSONSerializer(SerializeConfig serializeConfig) {
        this(new SerializeWriter(serializeConfig, new SerializerFeature[0]));
    }

    public JSONSerializer(JSONWriter jSONWriter) {
        this(new SerializeWriter(jSONWriter));
    }

    public JSONSerializer(SerializeWriter serializeWriter) {
        this.out = serializeWriter;
        this.raw = serializeWriter.raw;
    }

    public JSONSerializer(SerializeWriter serializeWriter, SerializeConfig serializeConfig) {
        this.out = serializeWriter;
        this.raw = serializeWriter.raw;
    }

    public static JSONSerializer getJSONSerializer(JSONWriter jSONWriter) {
        Object attachment = jSONWriter.getAttachment();
        if (attachment == null) {
            JSONSerializer jSONSerializer = new JSONSerializer(jSONWriter);
            jSONWriter.setAttachment(jSONSerializer);
            return jSONSerializer;
        }
        if (attachment instanceof JSONSerializer) {
            return (JSONSerializer) attachment;
        }
        return new JSONSerializer(jSONWriter);
    }

    public void config(SerializerFeature serializerFeature, boolean z) {
        if (!z) {
            throw new JSONException("not support");
        }
        JSONWriter.Context context = this.raw.getContext();
        switch (AnonymousClass1.$SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[serializerFeature.ordinal()]) {
            case 1:
                context.setDateFormat("iso8601");
                return;
            case 2:
                context.config(JSONWriter.Feature.WriteNulls);
                return;
            case 3:
                context.config(JSONWriter.Feature.WriteNullListAsEmpty);
                return;
            case 4:
                context.config(JSONWriter.Feature.WriteNullStringAsEmpty);
                return;
            case 5:
                context.config(JSONWriter.Feature.WriteNullNumberAsZero);
                return;
            case 6:
                context.config(JSONWriter.Feature.WriteNullBooleanAsFalse);
                return;
            case 7:
                context.config(JSONWriter.Feature.BrowserCompatible);
                return;
            case 8:
                context.config(JSONWriter.Feature.BrowserSecure);
                return;
            case 9:
                context.config(JSONWriter.Feature.WriteClassName);
                return;
            case 10:
                context.config(JSONWriter.Feature.WriteNonStringValueAsString);
                return;
            case 11:
                context.config(JSONWriter.Feature.WriteEnumUsingToString);
                return;
            case 12:
                context.config(JSONWriter.Feature.NotWriteRootClassName);
                return;
            case 13:
                context.config(JSONWriter.Feature.IgnoreErrorGetter);
                return;
            case 14:
                context.setDateFormat(JSON.DEFAULT_DATE_FORMAT);
                return;
            case 15:
                context.config(JSONWriter.Feature.BeanToArray);
                return;
            case 16:
                context.config(JSONWriter.Feature.UseSingleQuotes);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: renamed from: com.alibaba.fastjson.serializer.JSONSerializer$1, reason: invalid class name */
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
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.BrowserSecure.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteClassName.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNonStringValueAsString.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteEnumUsingToString.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.NotWriteRootClassName.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.IgnoreErrorGetter.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteDateUseDateFormat.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.BeanToArray.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.UseSingleQuotes.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
        }
    }

    public void write(boolean z) {
        this.raw.writeBool(z);
    }

    public void writeInt(int i) {
        this.raw.writeInt32(i);
    }

    public void write(String str) {
        this.raw.writeString(str);
    }

    public void writeLong(long j) {
        this.raw.writeInt64(j);
    }

    public void writeNull() {
        this.raw.writeNull();
    }

    public final void write(Object obj) {
        this.raw.writeAny(obj);
    }

    public final void writeAs(Object obj, Class cls) {
        this.raw.getObjectWriter(cls).write(this.raw, obj);
    }

    public String toString() {
        return this.raw.toString();
    }

    public List<PropertyFilter> getPropertyFilters() {
        return this.out.getPropertyFilters();
    }

    public List<ValueFilter> getValueFilters() {
        return this.out.getValueFilters();
    }

    public List<NameFilter> getNameFilters() {
        return this.out.getNameFilters();
    }

    public List<BeforeFilter> getBeforeFilters() {
        return this.out.getBeforeFilters();
    }

    public List<AfterFilter> getAfterFilters() {
        return this.out.getAfterFilters();
    }

    public SerializeConfig getMapping() {
        return this.out.config;
    }

    public SerializeWriter getWriter() {
        return this.out;
    }

    public ObjectSerializer getObjectWriter(Class<?> cls) {
        return this.out.config.getObjectWriter(cls);
    }

    public static void write(SerializeWriter serializeWriter, Object obj) {
        serializeWriter.raw.writeAny(obj);
    }

    public SerialContext getContext() {
        return this.context;
    }

    public void setContext(SerialContext serialContext) {
        this.context = serialContext;
    }

    public final boolean containsReference(Object obj) {
        return this.out.raw.containsReference(obj);
    }

    public final void writeReference(Object obj) {
        this.out.raw.writeReference(obj);
    }

    public final void incrementIndent() {
        this.out.raw.incrementIndent();
    }

    public final void decrementIdent() {
        this.out.raw.decrementIdent();
    }

    public void setContext(SerialContext serialContext, Object obj, Object obj2, int i) {
        setContext(serialContext, obj, obj2, i, 0);
    }

    public void setContext(SerialContext serialContext, Object obj, Object obj2, int i, int i2) {
        setContext(new SerialContext(serialContext, obj, obj2, i, i2));
    }
}
