package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import java.io.Closeable;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DefaultJSONParser implements Closeable {
    private final ParserConfig config;
    private Object input;
    private final JSONScanner lexer;
    private final JSONReader reader;

    public DefaultJSONParser(String str) {
        this(JSONReader.of(str), ParserConfig.global);
        this.input = str;
    }

    public DefaultJSONParser(Object obj, JSONLexer jSONLexer, ParserConfig parserConfig) {
        this.lexer = (JSONScanner) jSONLexer;
        this.reader = jSONLexer.getReader();
        this.config = parserConfig;
        this.input = obj;
    }

    public ParserConfig getConfig() {
        return this.config;
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig) {
        this(JSONReader.of(str), parserConfig);
    }

    public DefaultJSONParser(JSONReader jSONReader, ParserConfig parserConfig) {
        this.reader = jSONReader;
        this.config = parserConfig;
        this.lexer = new JSONScanner(jSONReader);
    }

    public JSONLexer getLexer() {
        return this.lexer;
    }

    public JSONReader getRawReader() {
        return this.reader;
    }

    public Object parse() {
        return this.reader.readAny();
    }

    public <T> List<T> parseArray(Class<T> cls) {
        return this.reader.readArray(cls);
    }

    public void parseArray(Type type, Collection collection) {
        this.reader.readArray(collection, type);
    }

    public void parseArray(Class<?> cls, Collection collection) {
        this.reader.readArray(collection, cls);
    }

    public Object[] parseArray(Type[] typeArr) {
        return this.reader.readArray(typeArr);
    }

    public final void parseArray(Collection collection) {
        this.reader.readArray(collection, Object.class);
    }

    public <T> T parseObject(Class<T> cls) {
        return (T) this.reader.read((Class) cls);
    }

    public <T> T parseObject(Type type) {
        return (T) this.reader.read(type);
    }

    public void parseObject(Object obj) {
        this.reader.readObject(obj, new JSONReader.Feature[0]);
    }

    @Deprecated
    public Object parse(Object obj) {
        return this.reader.readAny();
    }

    @Deprecated
    public void handleResovleTask(Object obj) {
        this.reader.handleResolveTasks(obj);
    }

    public void handleResolveTasks(Object obj) {
        this.reader.handleResolveTasks(obj);
    }

    public final void accept(int i) {
        char c;
        if (i == 2 || i == 3) {
            char cCurrent = this.reader.current();
            if (cCurrent != '-' && cCurrent != '+' && (cCurrent < '0' || cCurrent > '9')) {
                throw new JSONException("syntax error, expect int, actual " + this.reader.current());
            }
            Number number = this.reader.readNumber();
            if ((number instanceof Integer) || (number instanceof Long) || (number instanceof BigInteger)) {
                if (i == 2) {
                    return;
                }
            } else if (i == 3) {
                return;
            }
            throw new JSONException("syntax error, expect int, actual " + this.reader.current());
        }
        if (i == 4) {
            char cCurrent2 = this.reader.current();
            if (cCurrent2 == '\"' || cCurrent2 == '\'') {
                this.reader.readString();
                return;
            }
            throw new JSONException("syntax error, expect string, actual " + cCurrent2);
        }
        if (i == 6) {
            if (!this.reader.nextIfMatchIdent('t', 'r', 'u', 'e')) {
                throw new JSONException("syntax error, expect true, actual " + this.reader.current());
            }
            return;
        }
        if (i == 7) {
            if (!this.reader.nextIfMatchIdent('f', 'a', 'l', 's', 'e')) {
                throw new JSONException("syntax error, expect false, actual " + this.reader.current());
            }
            return;
        }
        if (i == 8) {
            if (!this.reader.nextIfNull()) {
                throw new JSONException("syntax error, expect false, actual " + this.reader.current());
            }
            return;
        }
        if (i == 21) {
            if (!this.reader.nextIfSet()) {
                throw new JSONException("syntax error, expect set, actual " + this.reader.current());
            }
            return;
        }
        if (i != 25) {
            switch (i) {
                case 10:
                    c = '(';
                    break;
                case 11:
                    c = ')';
                    break;
                case 12:
                    c = '{';
                    break;
                case 13:
                    c = '}';
                    break;
                case 14:
                    c = '[';
                    break;
                case 15:
                    c = ']';
                    break;
                case 16:
                    if (!this.reader.hasComma() && !this.reader.nextIfComma()) {
                        throw new JSONException("syntax error, expect ',', actual " + this.reader.current());
                    }
                    return;
                case 17:
                    c = ':';
                    break;
                default:
                    throw new JSONException("not support accept token " + JSONToken.name(i));
            }
        } else {
            c = '.';
        }
        if (!this.reader.nextIfMatch(c)) {
            throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + this.reader.current());
        }
    }

    public JSONObject parseObject() {
        if (this.reader.nextIfNull()) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(this.lexer.isOrderedField());
        this.reader.read(jSONObject, 0L);
        return jSONObject;
    }

    public void config(Feature feature, boolean z) {
        this.lexer.config(feature, z);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.reader.close();
    }

    public String getInput() {
        Object obj = this.input;
        if (obj instanceof char[]) {
            return new String((char[]) this.input);
        }
        return obj.toString();
    }

    public boolean isEnabled(Feature feature) {
        return this.lexer.isEnabled(feature);
    }
}
