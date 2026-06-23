package com.alibaba.fastjson2;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONPathFunction;
import com.alibaba.fastjson2.JSONPathSegment;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class JSONPathParser {
    boolean dollar;
    int filterNests;
    JSONPathSegment first;
    final JSONReader jsonReader;
    boolean lax;
    boolean negative;
    final String path;
    JSONPathSegment second;
    int segmentIndex;
    List<JSONPathSegment> segments;
    boolean strict;

    static boolean isOperator(char c) {
        return c == '=' || c == '<' || c == '>' || c == '!';
    }

    public JSONPathParser(String str) {
        this.path = str;
        JSONReader jSONReaderOf = JSONReader.of(str, JSONPath.PARSE_CONTEXT);
        this.jsonReader = jSONReaderOf;
        if (jSONReaderOf.ch == 'l' && jSONReaderOf.nextIfMatchIdent('l', 'a', 'x')) {
            this.lax = true;
        } else if (jSONReaderOf.ch == 's' && jSONReaderOf.nextIfMatchIdent('s', 't', 'r', 'i', 'c', 't')) {
            this.strict = true;
        }
        if (jSONReaderOf.ch == '-') {
            jSONReaderOf.next();
            this.negative = true;
        }
        if (jSONReaderOf.ch == '$') {
            jSONReaderOf.next();
            this.dollar = true;
        }
    }

    JSONPath parse(JSONPath.Feature... featureArr) {
        JSONPathSegment filter;
        if (this.dollar && this.jsonReader.ch == 26) {
            if (this.negative) {
                return new JSONPathSingle(JSONPathFunction.FUNC_NEGATIVE, this.path, new JSONPath.Feature[0]);
            }
            return JSONPath.RootPath.INSTANCE;
        }
        if (this.jsonReader.ch == 'e' && this.jsonReader.nextIfMatchIdent('e', 'x', 'i', 's', 't', 's')) {
            if (!this.jsonReader.nextIfMatch('(')) {
                throw new JSONException("syntax error " + this.path);
            }
            if (this.jsonReader.ch == '@') {
                this.jsonReader.next();
                if (!this.jsonReader.nextIfMatch('.')) {
                    throw new JSONException("syntax error " + this.path);
                }
            }
            char c = this.jsonReader.ch;
            if ((c >= 'a' && c <= 'z') || ((c >= 'A' && c <= 'Z') || c == '_' || c == '@' || Character.isIdeographic(c))) {
                JSONPathSegment property = parseProperty();
                if (!this.jsonReader.nextIfMatch(')')) {
                    throw new JSONException("syntax error " + this.path);
                }
                return new JSONPathTwoSegment(this.path, property, JSONPathFunction.FUNC_EXISTS, new JSONPath.Feature[0]);
            }
            throw new JSONException("syntax error " + this.path);
        }
        while (this.jsonReader.ch != 26) {
            char c2 = this.jsonReader.ch;
            if (c2 == '.') {
                this.jsonReader.next();
                filter = parseProperty();
            } else if (this.jsonReader.ch == '[') {
                filter = parseArrayAccess();
            } else if ((c2 >= 'a' && c2 <= 'z') || ((c2 >= 'A' && c2 <= 'Z') || c2 == '_' || Character.isIdeographic(c2))) {
                filter = parseProperty();
            } else if (c2 == '?') {
                if (this.dollar && this.segmentIndex == 0) {
                    this.first = JSONPathSegment.RootSegment.INSTANCE;
                    this.segmentIndex++;
                }
                this.jsonReader.next();
                filter = parseFilter();
            } else if (c2 == '@') {
                this.jsonReader.next();
                filter = JSONPathSegment.SelfSegment.INSTANCE;
            } else {
                throw new JSONException("not support " + c2);
            }
            int i = this.segmentIndex;
            if (i == 0) {
                this.first = filter;
            } else if (i == 1) {
                this.second = filter;
            } else if (i == 2) {
                ArrayList arrayList = new ArrayList();
                this.segments = arrayList;
                arrayList.add(this.first);
                this.segments.add(this.second);
                this.segments.add(filter);
            } else {
                this.segments.add(filter);
            }
            this.segmentIndex++;
        }
        if (this.negative) {
            int i2 = this.segmentIndex;
            if (i2 == 1) {
                this.second = JSONPathFunction.FUNC_NEGATIVE;
            } else if (i2 == 2) {
                ArrayList arrayList2 = new ArrayList();
                this.segments = arrayList2;
                arrayList2.add(this.first);
                this.segments.add(this.second);
                this.segments.add(JSONPathFunction.FUNC_NEGATIVE);
            } else {
                this.segments.add(JSONPathFunction.FUNC_NEGATIVE);
            }
            this.segmentIndex++;
        }
        int i3 = this.segmentIndex;
        if (i3 != 1) {
            if (i3 == 2) {
                return new JSONPathTwoSegment(this.path, this.first, this.second, featureArr);
            }
            return new JSONPathMulti(this.path, this.segments, featureArr);
        }
        JSONPathSegment jSONPathSegment = this.first;
        if (jSONPathSegment instanceof JSONPathSegmentName) {
            return new JSONPathSingleName(this.path, (JSONPathSegmentName) this.first, featureArr);
        }
        if (jSONPathSegment instanceof JSONPathSegmentIndex) {
            JSONPathSegmentIndex jSONPathSegmentIndex = (JSONPathSegmentIndex) jSONPathSegment;
            if (jSONPathSegmentIndex.index >= 0) {
                return new JSONPathSingleIndex(this.path, jSONPathSegmentIndex, featureArr);
            }
        }
        return new JSONPathSingle(this.first, this.path, featureArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x017c  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0239  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.alibaba.fastjson2.JSONPathSegment parseArrayAccess() {
        /*
            Method dump skipped, instruction units count: 772
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathParser.parseArrayAccess():com.alibaba.fastjson2.JSONPathSegment");
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private JSONPathSegment parseProperty() {
        JSONPathSegment jSONPathFunction;
        JSONPathFunction jSONPathFunction2;
        int length;
        if (this.jsonReader.ch == '*') {
            this.jsonReader.next();
            return JSONPathSegment.AllSegment.INSTANCE;
        }
        if (this.jsonReader.ch == '.') {
            this.jsonReader.next();
            if (this.jsonReader.ch == '*') {
                this.jsonReader.next();
                return new JSONPathSegment.CycleNameSegment("*", Fnv.hashCode64("*"));
            }
            return new JSONPathSegment.CycleNameSegment(this.jsonReader.getFieldName(), this.jsonReader.readFieldNameHashCodeUnquote());
        }
        boolean zIsNumber = this.jsonReader.isNumber();
        long fieldNameHashCodeUnquote = this.jsonReader.readFieldNameHashCodeUnquote();
        String fieldName = this.jsonReader.getFieldName();
        byte b = 0;
        if (zIsNumber && (length = fieldName.length()) <= 9) {
            for (int i = 0; i < length; i++) {
                char cCharAt = fieldName.charAt(i);
                if (cCharAt < '0' || cCharAt > '9') {
                    break;
                }
            }
        }
        if (this.jsonReader.ch == '(') {
            this.jsonReader.next();
            fieldName.hashCode();
            switch (fieldName.hashCode()) {
                case -2093674864:
                    if (!fieldName.equals("entrySet")) {
                        b = -1;
                    }
                    break;
                case -1325958191:
                    b = fieldName.equals("double") ? (byte) 1 : (byte) -1;
                    break;
                case -1106363674:
                    b = fieldName.equals("length") ? (byte) 2 : (byte) -1;
                    break;
                case -823812830:
                    b = fieldName.equals("values") ? (byte) 3 : (byte) -1;
                    break;
                case 96370:
                    b = fieldName.equals("abs") ? (byte) 4 : (byte) -1;
                    break;
                case 107876:
                    b = fieldName.equals("max") ? (byte) 5 : (byte) -1;
                    break;
                case 108114:
                    b = fieldName.equals("min") ? (byte) 6 : (byte) -1;
                    break;
                case 114251:
                    b = fieldName.equals("sum") ? (byte) 7 : (byte) -1;
                    break;
                case 3049733:
                    b = fieldName.equals("ceil") ? (byte) 8 : (byte) -1;
                    break;
                case 3288564:
                    b = fieldName.equals("keys") ? (byte) 9 : (byte) -1;
                    break;
                case 3314326:
                    b = fieldName.equals("last") ? (byte) 10 : (byte) -1;
                    break;
                case 3530753:
                    b = fieldName.equals("size") ? (byte) 11 : (byte) -1;
                    break;
                case 3568674:
                    b = fieldName.equals("trim") ? (byte) 12 : (byte) -1;
                    break;
                case 3575610:
                    b = fieldName.equals("type") ? (byte) 13 : (byte) -1;
                    break;
                case 97440432:
                    b = fieldName.equals("first") ? (byte) 14 : (byte) -1;
                    break;
                case 97526796:
                    b = fieldName.equals("floor") ? (byte) 15 : (byte) -1;
                    break;
                case 100346066:
                    b = fieldName.equals("index") ? JSONB.Constants.BC_INT32_NUM_16 : (byte) -1;
                    break;
                case 103164673:
                    b = fieldName.equals("lower") ? (byte) 17 : (byte) -1;
                    break;
                case 111499426:
                    b = fieldName.equals("upper") ? (byte) 18 : (byte) -1;
                    break;
                case 660387005:
                    b = fieldName.equals("ceiling") ? (byte) 19 : (byte) -1;
                    break;
                case 921111605:
                    b = fieldName.equals("negative") ? (byte) 20 : (byte) -1;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    jSONPathFunction = JSONPathSegment.EntrySetSegment.INSTANCE;
                    break;
                case 1:
                    jSONPathFunction = JSONPathFunction.FUNC_DOUBLE;
                    break;
                case 2:
                case 11:
                    jSONPathFunction = JSONPathSegment.LengthSegment.INSTANCE;
                    break;
                case 3:
                    jSONPathFunction = JSONPathSegment.ValuesSegment.INSTANCE;
                    break;
                case 4:
                    jSONPathFunction = JSONPathFunction.FUNC_ABS;
                    break;
                case 5:
                    jSONPathFunction = JSONPathSegment.MaxSegment.INSTANCE;
                    break;
                case 6:
                    jSONPathFunction = JSONPathSegment.MinSegment.INSTANCE;
                    break;
                case 7:
                    jSONPathFunction = JSONPathSegment.SumSegment.INSTANCE;
                    break;
                case 8:
                case 19:
                    jSONPathFunction = JSONPathFunction.FUNC_CEIL;
                    break;
                case 9:
                    jSONPathFunction = JSONPathSegment.KeysSegment.INSTANCE;
                    break;
                case 10:
                    jSONPathFunction = JSONPathFunction.FUNC_LAST;
                    break;
                case 12:
                    jSONPathFunction = JSONPathFunction.FUNC_TRIM;
                    break;
                case 13:
                    jSONPathFunction = JSONPathFunction.FUNC_TYPE;
                    break;
                case 14:
                    jSONPathFunction = JSONPathFunction.FUNC_FIRST;
                    break;
                case 15:
                    jSONPathFunction = JSONPathFunction.FUNC_FLOOR;
                    break;
                case 16:
                    if (this.jsonReader.isNumber()) {
                        Number number = this.jsonReader.readNumber();
                        boolean z = number instanceof BigDecimal;
                        Number numberValueOf = number;
                        if (z) {
                            BigDecimal bigDecimalM = JSONPathFunction$IndexDecimal$$ExternalSyntheticBackportWithForwarding0.m((BigDecimal) number);
                            if (bigDecimalM.scale() != 0) {
                                jSONPathFunction2 = new JSONPathFunction(new JSONPathFunction.IndexDecimal(bigDecimalM));
                                jSONPathFunction = jSONPathFunction2;
                            } else {
                                BigInteger bigIntegerUnscaledValue = bigDecimalM.unscaledValue();
                                int iCompareTo = bigIntegerUnscaledValue.compareTo(TypeUtils.BIGINT_INT64_MIN);
                                numberValueOf = bigIntegerUnscaledValue;
                                if (iCompareTo >= 0) {
                                    int iCompareTo2 = bigIntegerUnscaledValue.compareTo(TypeUtils.BIGINT_INT64_MAX);
                                    numberValueOf = bigIntegerUnscaledValue;
                                    if (iCompareTo2 <= 0) {
                                        numberValueOf = Long.valueOf(bigIntegerUnscaledValue.longValue());
                                    }
                                }
                            }
                            break;
                        }
                        if ((numberValueOf instanceof Integer) || (numberValueOf instanceof Long)) {
                            jSONPathFunction = new JSONPathFunction(new JSONPathFunction.IndexInt(numberValueOf.longValue()));
                            break;
                        }
                        throw new JSONException("not support syntax, path : " + this.path);
                    }
                    if (this.jsonReader.isString()) {
                        jSONPathFunction2 = new JSONPathFunction(new JSONPathFunction.IndexString(this.jsonReader.readString()));
                        jSONPathFunction = jSONPathFunction2;
                        break;
                    }
                    throw new JSONException("not support syntax, path : " + this.path);
                case 17:
                    jSONPathFunction = JSONPathFunction.FUNC_LOWER;
                    break;
                case 18:
                    jSONPathFunction = JSONPathFunction.FUNC_UPPER;
                    break;
                case 20:
                    jSONPathFunction = JSONPathFunction.FUNC_NEGATIVE;
                    break;
                default:
                    throw new JSONException("not support syntax, path : " + this.path);
            }
            if (this.jsonReader.nextIfMatch(')')) {
                return jSONPathFunction;
            }
            throw new JSONException("not support syntax, path : " + this.path);
        }
        return new JSONPathSegmentName(fieldName, fieldNameHashCodeUnquote);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0087  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    com.alibaba.fastjson2.JSONPathSegment parseFilterRest(com.alibaba.fastjson2.JSONPathSegment r7) {
        /*
            Method dump skipped, instruction units count: 265
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathParser.parseFilterRest(com.alibaba.fastjson2.JSONPathSegment):com.alibaba.fastjson2.JSONPathSegment");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:210:0x03c2  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x040b  */
    /* JADX WARN: Removed duplicated region for block: B:491:0x099a  */
    /* JADX WARN: Removed duplicated region for block: B:492:0x099d  */
    /* JADX WARN: Type inference failed for: r5v41 */
    /* JADX WARN: Type inference failed for: r5v44 */
    /* JADX WARN: Type inference failed for: r5v47 */
    /* JADX WARN: Type inference failed for: r5v48 */
    /* JADX WARN: Type inference failed for: r5v55 */
    /* JADX WARN: Type inference failed for: r7v28 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    com.alibaba.fastjson2.JSONPathSegment parseFilter() {
        /*
            Method dump skipped, instruction units count: 2606
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson2.JSONPathParser.parseFilter():com.alibaba.fastjson2.JSONPathSegment");
    }

    public JSONPathSegment parseSegment() {
        Object string;
        if (this.jsonReader.nextIfMatch('@')) {
            if (this.jsonReader.nextIfMatch('.')) {
                if (this.jsonReader.isNumber()) {
                    string = this.jsonReader.readNumber();
                } else {
                    string = this.jsonReader.readFieldNameUnquote();
                }
            } else if (this.jsonReader.nextIfArrayStart()) {
                if (this.jsonReader.isNumber()) {
                    string = this.jsonReader.readNumber();
                } else if (this.jsonReader.isString()) {
                    string = this.jsonReader.readString();
                } else {
                    throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                }
                if (!this.jsonReader.nextIfArrayEnd()) {
                    throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                }
            } else {
                string = null;
            }
            if (string instanceof String) {
                String str = (String) string;
                return new JSONPathSegmentName(str, Fnv.hashCode64(str));
            }
            if (string instanceof Integer) {
                return new JSONPathSegmentIndex(((Integer) string).intValue());
            }
        }
        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
    }
}
