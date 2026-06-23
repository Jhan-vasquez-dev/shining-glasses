package com.alibaba.fastjson.parser;

import com.alibaba.fastjson2.JSONReader;
import java.math.BigDecimal;

/* JADX INFO: loaded from: classes.dex */
public interface JSONLexer {
    public static final int ARRAY = 2;
    public static final int END = 4;
    public static final char EOI = 26;
    public static final int NOT_MATCH = -1;
    public static final int NOT_MATCH_NAME = -2;
    public static final int OBJECT = 1;
    public static final int UNKNOWN = 0;
    public static final int VALUE = 3;
    public static final int VALUE_NULL = 5;

    BigDecimal decimalValue();

    char getCurrent();

    JSONReader getReader();

    int intValue();

    boolean isBlankInput();

    boolean isEOF();

    long longValue();

    void nextToken();

    void nextToken(int i);

    String stringVal();
}
