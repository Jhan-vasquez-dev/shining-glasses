package com.alibaba.fastjson;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson2.JSONReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Locale;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes.dex */
public class JSONReader implements Closeable {
    private final Reader input;
    private final com.alibaba.fastjson2.JSONReader raw;

    public JSONReader(Reader reader) {
        this(reader, new Feature[0]);
    }

    public JSONReader(Reader reader, Feature... featureArr) {
        JSONReader.Context contextCreateReadContext = JSON.createReadContext(JSON.DEFAULT_PARSER_FEATURE, featureArr);
        this.raw = com.alibaba.fastjson2.JSONReader.of(reader, contextCreateReadContext);
        this.input = reader;
        for (Feature feature : featureArr) {
            if (feature == Feature.SupportArrayToBean) {
                contextCreateReadContext.config(JSONReader.Feature.SupportArrayToBean);
            }
        }
    }

    public void setLocale(Locale locale) {
        this.raw.getContext().setLocale(locale);
    }

    public void setTimzeZone(TimeZone timeZone) {
        this.raw.getContext().setTimeZone(timeZone);
    }

    public <T> T readObject(Class<T> cls) {
        this.raw.nextIfMatch(':');
        try {
            return (T) this.raw.read((Class) cls);
        } catch (com.alibaba.fastjson2.JSONException e) {
            throw new JSONException(e.getMessage(), e.getCause());
        }
    }

    public Object readObject() {
        this.raw.nextIfMatch(':');
        return this.raw.readAny();
    }

    public void readObject(Object obj) {
        this.raw.nextIfMatch(':');
        this.raw.readObject(obj, new JSONReader.Feature[0]);
    }

    public Integer readInteger() {
        this.raw.nextIfMatch(':');
        return this.raw.readInt32();
    }

    public Long readLong() {
        this.raw.nextIfMatch(':');
        return this.raw.readInt64();
    }

    public String readString() {
        this.raw.nextIfMatch(':');
        return this.raw.readString();
    }

    public boolean hasNext() {
        char cCurrent;
        return (this.raw.isEnd() || (cCurrent = this.raw.current()) == ']' || cCurrent == '}') ? false : true;
    }

    public void startArray() {
        this.raw.nextIfMatch(':');
        if (!this.raw.nextIfArrayStart()) {
            throw new JSONException("not support operation");
        }
    }

    public void endArray() {
        if (!this.raw.nextIfArrayEnd()) {
            throw new JSONException("not support operation");
        }
        this.raw.nextIfComma();
    }

    public void startObject() {
        this.raw.nextIfMatch(':');
        if (!this.raw.nextIfObjectStart()) {
            throw new JSONException("not support operation");
        }
    }

    public void endObject() {
        if (!this.raw.nextIfObjectEnd()) {
            throw new JSONException(this.raw.info("not support operation"));
        }
        this.raw.nextIfComma();
    }

    public Locale getLocal() {
        return this.raw.getContext().getLocale();
    }

    public TimeZone getTimeZone() {
        return this.raw.getContext().getTimeZone();
    }

    /* JADX INFO: renamed from: com.alibaba.fastjson.JSONReader$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$alibaba$fastjson$parser$Feature;

        static {
            int[] iArr = new int[Feature.values().length];
            $SwitchMap$com$alibaba$fastjson$parser$Feature = iArr;
            try {
                iArr[Feature.SupportArrayToBean.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.UseNativeJavaObject.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.SupportAutoType.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public void config(Feature feature, boolean z) {
        JSONReader.Feature feature2;
        int i = AnonymousClass1.$SwitchMap$com$alibaba$fastjson$parser$Feature[feature.ordinal()];
        if (i == 1) {
            feature2 = JSONReader.Feature.SupportArrayToBean;
        } else if (i == 2) {
            feature2 = JSONReader.Feature.UseNativeObject;
        } else {
            feature2 = i != 3 ? null : JSONReader.Feature.SupportAutoType;
        }
        if (feature2 == null) {
            return;
        }
        this.raw.getContext().config(feature2, z);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.input.close();
    }
}
