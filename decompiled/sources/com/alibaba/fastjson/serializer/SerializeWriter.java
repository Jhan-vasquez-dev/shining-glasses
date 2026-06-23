package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SerializeWriter implements Cloneable {
    final ListWrapper<AfterFilter> afterFilters;
    final ListWrapper<BeforeFilter> beforeFilters;
    SerializeConfig config;
    final ListWrapper<NameFilter> nameFilters;
    final ListWrapper<PropertyFilter> propertyFilters;
    final JSONWriter raw;
    final ListWrapper<ValueFilter> valueFilters;

    public SerializeWriter() {
        this(JSONWriter.of());
    }

    public SerializeWriter(SerializerFeature... serializerFeatureArr) {
        this(JSONWriter.of(JSON.createWriteContext(SerializeConfig.global, JSON.DEFAULT_PARSER_FEATURE, serializerFeatureArr)));
    }

    public SerializeWriter(SerializeConfig serializeConfig, SerializerFeature... serializerFeatureArr) {
        this(JSONWriter.of(JSON.createWriteContext(serializeConfig, JSON.DEFAULT_PARSER_FEATURE, serializerFeatureArr)));
        this.config = serializeConfig;
    }

    public SerializeWriter(JSONWriter jSONWriter) {
        this.raw = jSONWriter;
        this.propertyFilters = new ListWrapper<>();
        this.valueFilters = new ListWrapper<>();
        this.nameFilters = new ListWrapper<>();
        this.beforeFilters = new ListWrapper<>();
        this.afterFilters = new ListWrapper<>();
    }

    public void writeNull() {
        this.raw.writeNull();
    }

    public void writeNull(SerializerFeature serializerFeature) {
        this.raw.writeNull();
    }

    public void writeString(String str) {
        this.raw.writeString(str);
    }

    public void write(String str) {
        this.raw.writeRaw(str);
    }

    public List<PropertyFilter> getPropertyFilters() {
        return this.propertyFilters;
    }

    public List<ValueFilter> getValueFilters() {
        return this.valueFilters;
    }

    public List<NameFilter> getNameFilters() {
        return this.nameFilters;
    }

    public List<BeforeFilter> getBeforeFilters() {
        return this.beforeFilters;
    }

    public List<AfterFilter> getAfterFilters() {
        return this.afterFilters;
    }

    class ListWrapper<T> extends ArrayList<T> {
        ListWrapper() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(T t) {
            JSONWriter.Context context = SerializeWriter.this.raw.getContext();
            if (t instanceof PropertyFilter) {
                context.setPropertyFilter((PropertyFilter) t);
            }
            if (t instanceof ValueFilter) {
                context.setValueFilter((ValueFilter) t);
            }
            if (t instanceof NameFilter) {
                context.setNameFilter((NameFilter) t);
            }
            if (t instanceof com.alibaba.fastjson2.filter.PropertyPreFilter) {
                context.setPropertyPreFilter((com.alibaba.fastjson2.filter.PropertyPreFilter) t);
            }
            if (t instanceof BeforeFilter) {
                context.setBeforeFilter((BeforeFilter) t);
            }
            if (t instanceof AfterFilter) {
                context.setAfterFilter((AfterFilter) t);
            }
            if (t instanceof com.alibaba.fastjson2.filter.LabelFilter) {
                context.setLabelFilter((com.alibaba.fastjson2.filter.LabelFilter) t);
            }
            return super.add(t);
        }
    }

    public void write(int i) {
        this.raw.writeRaw((char) i);
    }

    public void write(char c) {
        this.raw.writeRaw(c);
    }

    public void writeInt(int i) {
        this.raw.writeInt32(i);
    }

    public void writeLong(long j) {
        this.raw.writeInt64(j);
    }

    public void writeFieldName(String str) {
        this.raw.writeName(str);
    }

    public String toString() {
        return this.raw.toString();
    }

    public byte[] toBytes(Charset charset) {
        return this.raw.getBytes(charset);
    }

    public byte[] toBytes(String str) {
        return this.raw.getBytes(Charset.forName(str));
    }

    public void close() {
        this.raw.close();
    }

    public void writeTo(Writer writer) throws IOException {
        this.raw.flushTo(writer);
    }

    /* JADX INFO: renamed from: com.alibaba.fastjson.serializer.SerializeWriter$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature;

        static {
            int[] iArr = new int[SerializerFeature.values().length];
            $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature = iArr;
            try {
                iArr[SerializerFeature.BeanToArray.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteMapNullValue.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteEnumUsingToString.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteEnumUsingName.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNullListAsEmpty.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNullStringAsEmpty.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNullNumberAsZero.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNullBooleanAsFalse.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteClassName.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.NotWriteRootClassName.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNonStringKeyAsString.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.NotWriteDefaultValue.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.BrowserCompatible.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.BrowserSecure.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.IgnoreNonFieldGetter.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteNonStringValueAsString.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.IgnoreErrorGetter.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[SerializerFeature.WriteBigDecimalAsPlain.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
        }
    }

    public boolean isEnabled(SerializerFeature serializerFeature) {
        JSONWriter.Feature feature;
        switch (AnonymousClass1.$SwitchMap$com$alibaba$fastjson$serializer$SerializerFeature[serializerFeature.ordinal()]) {
            case 1:
                feature = JSONWriter.Feature.BeanToArray;
                break;
            case 2:
                feature = JSONWriter.Feature.WriteMapNullValue;
                break;
            case 3:
                feature = JSONWriter.Feature.WriteEnumUsingToString;
                break;
            case 4:
                feature = JSONWriter.Feature.WriteEnumsUsingName;
                break;
            case 5:
                feature = JSONWriter.Feature.WriteNullListAsEmpty;
                break;
            case 6:
                feature = JSONWriter.Feature.WriteNullStringAsEmpty;
                break;
            case 7:
                feature = JSONWriter.Feature.WriteNullNumberAsZero;
                break;
            case 8:
                feature = JSONWriter.Feature.WriteNullBooleanAsFalse;
                break;
            case 9:
                feature = JSONWriter.Feature.WriteClassName;
                break;
            case 10:
                feature = JSONWriter.Feature.NotWriteRootClassName;
                break;
            case 11:
                feature = JSONWriter.Feature.WriteNonStringKeyAsString;
                break;
            case 12:
                feature = JSONWriter.Feature.NotWriteDefaultValue;
                break;
            case 13:
                feature = JSONWriter.Feature.BrowserCompatible;
                break;
            case 14:
                feature = JSONWriter.Feature.BrowserSecure;
                break;
            case 15:
                feature = JSONWriter.Feature.IgnoreNonFieldGetter;
                break;
            case 16:
                feature = JSONWriter.Feature.WriteNonStringValueAsString;
                break;
            case 17:
                feature = JSONWriter.Feature.IgnoreErrorGetter;
                break;
            case 18:
                feature = JSONWriter.Feature.WriteBigDecimalAsPlain;
                break;
            default:
                feature = null;
                break;
        }
        if (feature != null) {
            return this.raw.isEnabled(feature);
        }
        return false;
    }

    public SerializeWriter append(char c) {
        this.raw.writeRaw(c);
        return this;
    }

    public JSONWriter getJSONWriter() {
        return this.raw;
    }

    public SerializeWriter append(CharSequence charSequence) {
        this.raw.writeRaw(charSequence == null ? "null" : charSequence.toString());
        return this;
    }

    public SerializeWriter append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            charSequence = "null";
        }
        this.raw.writeRaw(charSequence.subSequence(i, i2).toString());
        return this;
    }
}
