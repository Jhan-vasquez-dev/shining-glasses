package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.DateUtils;
import java.io.Closeable;
import java.math.BigDecimal;
import java.util.Calendar;

/* JADX INFO: loaded from: classes.dex */
public class JSONScanner extends JSONLexerBase implements Closeable {
    protected Calendar calendar;
    private boolean orderedField;
    private final JSONReader reader;
    protected String str;
    private String strVal;
    protected int token;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    public JSONScanner(JSONReader jSONReader) {
        this.reader = jSONReader;
    }

    public JSONScanner(String str) {
        this.reader = JSONReader.of(str);
        this.str = str;
    }

    public JSONScanner(String str, int i) {
        this.reader = JSONReader.of(str, JSON.createReadContext(i, new Feature[0]));
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean z) {
        String str = this.str;
        if (str != null) {
            try {
                long millis = DateUtils.parseMillis(str);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(millis);
                this.calendar = calendar;
                return true;
            } catch (Exception unused) {
                return false;
            }
        }
        throw new JSONException("UnsupportedOperation");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public JSONReader getReader() {
        return this.reader;
    }

    public boolean isOrderedField() {
        return this.orderedField;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String stringVal() {
        return this.strVal;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public BigDecimal decimalValue() {
        return this.reader.getBigDecimal();
    }

    public int token() {
        return this.token;
    }

    /* JADX INFO: renamed from: com.alibaba.fastjson.parser.JSONScanner$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$alibaba$fastjson$parser$Feature;

        static {
            int[] iArr = new int[Feature.values().length];
            $SwitchMap$com$alibaba$fastjson$parser$Feature = iArr;
            try {
                iArr[Feature.AllowUnQuotedFieldNames.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.SupportArrayToBean.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.DisableFieldSmartMatch.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.SupportAutoType.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.NonStringKeyAsString.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.ErrorOnEnumNotMatch.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.SupportClassForName.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.ErrorOnNotSupportAutoType.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.UseNativeJavaObject.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.UseBigDecimal.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$alibaba$fastjson$parser$Feature[Feature.OrderedField.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    public void config(Feature feature, boolean z) {
        JSONReader.Feature feature2;
        boolean z2 = true;
        switch (AnonymousClass1.$SwitchMap$com$alibaba$fastjson$parser$Feature[feature.ordinal()]) {
            case 1:
                feature2 = JSONReader.Feature.AllowUnQuotedFieldNames;
                z2 = false;
                break;
            case 2:
                feature2 = JSONReader.Feature.SupportArrayToBean;
                z2 = false;
                break;
            case 3:
                feature2 = JSONReader.Feature.SupportSmartMatch;
                break;
            case 4:
                feature2 = JSONReader.Feature.SupportAutoType;
                z2 = false;
                break;
            case 5:
                feature2 = JSONReader.Feature.NonStringKeyAsString;
                z2 = false;
                break;
            case 6:
                feature2 = JSONReader.Feature.ErrorOnEnumNotMatch;
                z2 = false;
                break;
            case 7:
                feature2 = JSONReader.Feature.SupportClassForName;
                z2 = false;
                break;
            case 8:
                feature2 = JSONReader.Feature.ErrorOnNotSupportAutoType;
                z2 = false;
                break;
            case 9:
                feature2 = JSONReader.Feature.UseNativeObject;
                z2 = false;
                break;
            case 10:
                feature2 = JSONReader.Feature.UseDoubleForDecimals;
                break;
            case 11:
                this.orderedField = z;
            default:
                feature2 = null;
                z2 = false;
                break;
        }
        if (feature2 == null) {
            return;
        }
        if (z2) {
            z = !z;
        }
        this.reader.getContext().config(feature2, z);
    }

    public boolean isEnabled(Feature feature) {
        JSONReader.Feature feature2;
        switch (AnonymousClass1.$SwitchMap$com$alibaba$fastjson$parser$Feature[feature.ordinal()]) {
            case 1:
                feature2 = JSONReader.Feature.AllowUnQuotedFieldNames;
                break;
            case 2:
                feature2 = JSONReader.Feature.SupportArrayToBean;
                break;
            case 3:
                return !this.reader.isEnabled(JSONReader.Feature.SupportSmartMatch);
            case 4:
                feature2 = JSONReader.Feature.SupportAutoType;
                break;
            case 5:
                feature2 = JSONReader.Feature.NonStringKeyAsString;
                break;
            case 6:
                feature2 = JSONReader.Feature.ErrorOnEnumNotMatch;
                break;
            case 7:
                feature2 = JSONReader.Feature.SupportClassForName;
                break;
            case 8:
                feature2 = JSONReader.Feature.ErrorOnNotSupportAutoType;
                break;
            case 9:
                feature2 = JSONReader.Feature.UseNativeObject;
                break;
            case 10:
                return !this.reader.isEnabled(JSONReader.Feature.UseDoubleForDecimals);
            default:
                feature2 = null;
                break;
        }
        if (feature2 == null) {
            return true;
        }
        return this.reader.isEnabled(feature2);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public boolean isBlankInput() {
        return this.reader.isEnd();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public int intValue() {
        return this.reader.getInt32Value();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public long longValue() {
        return this.reader.getInt64Value();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextToken() {
        this.strVal = null;
        char cCurrent = this.reader.current();
        switch (cCurrent) {
            case 26:
                this.token = 20;
                return;
            case '\"':
            case '\'':
                this.strVal = this.reader.readString();
                this.token = 4;
                return;
            case '+':
            case '-':
                break;
            case '[':
                this.reader.next();
                this.token = 14;
                return;
            case ']':
                this.reader.next();
                this.token = 15;
                return;
            case 'f':
            case 't':
                this.token = this.reader.readBoolValue() ? 6 : 7;
                return;
            case 'n':
                this.reader.readNull();
                this.token = 8;
                return;
            case '{':
                this.reader.next();
                this.token = 12;
                return;
            case '}':
                this.reader.next();
                this.token = 13;
                return;
            default:
                switch (cCurrent) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        break;
                    case ':':
                        this.reader.next();
                        this.token = 17;
                        return;
                    default:
                        if (!this.reader.nextIfNull()) {
                            throw new JSONException("not support operation");
                        }
                        return;
                }
                break;
        }
        Number number = this.reader.readNumber();
        if ((number instanceof BigDecimal) || (number instanceof Float) || (number instanceof Double)) {
            this.token = 3;
        } else {
            this.token = 2;
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public char getCurrent() {
        return this.reader.current();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextToken(int i) {
        nextToken();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public boolean isEOF() {
        return this.reader.isEnd();
    }
}
