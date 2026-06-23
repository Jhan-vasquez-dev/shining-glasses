package com.alibaba.fastjson2.internal.asm;

import okhttp3.HttpUrl;

/* JADX INFO: loaded from: classes.dex */
public final class Type {
    static final int ARRAY = 9;
    static final int BOOLEAN = 1;
    static final int BYTE = 3;
    static final int CHAR = 2;
    static final int DOUBLE = 8;
    static final Type DOUBLE_TYPE;
    static final int FLOAT = 6;
    static final int INT = 5;
    static final int INTERNAL = 12;
    static final int LONG = 7;
    static final Type LONG_TYPE;
    static final int METHOD = 11;
    static final int OBJECT = 10;
    static final int SHORT = 4;
    static final Type[] TYPES_0;
    static final Type[] TYPES_1;
    static final Type[] TYPES_2;
    static final Type[] TYPES_3;
    static final Type[] TYPES_4;
    static final Type TYPE_CLASS;
    static final Type TYPE_JSON_READER;
    static final Type TYPE_JSON_WRITER;
    static final Type TYPE_LIST;
    static final Type TYPE_OBJECT;
    static final Type TYPE_STRING;
    static final Type TYPE_SUPPLIER;
    static final Type TYPE_TYPE;
    static final int VOID = 0;
    final int sort;
    final int valueBegin;
    final String valueBuffer;
    final int valueEnd;
    static final Type VOID_TYPE = new Type(0, "VZCBSIFJD", 0, 1);
    static final Type BOOLEAN_TYPE = new Type(1, "VZCBSIFJD", 1, 2);
    static final Type CHAR_TYPE = new Type(2, "VZCBSIFJD", 2, 3);
    static final Type BYTE_TYPE = new Type(3, "VZCBSIFJD", 3, 4);
    static final Type SHORT_TYPE = new Type(4, "VZCBSIFJD", 4, 5);
    static final Type INT_TYPE = new Type(5, "VZCBSIFJD", 5, 6);
    static final Type FLOAT_TYPE = new Type(6, "VZCBSIFJD", 6, 7);

    static {
        Type type = new Type(7, "VZCBSIFJD", 7, 8);
        LONG_TYPE = type;
        DOUBLE_TYPE = new Type(8, "VZCBSIFJD", 8, 9);
        Type type2 = new Type(10, "Ljava/lang/Class;", 1, 16);
        TYPE_CLASS = type2;
        Type type3 = new Type(10, "Ljava/lang/reflect/Type;", 1, 23);
        TYPE_TYPE = type3;
        Type type4 = new Type(10, "Ljava/lang/Object;", 1, 17);
        TYPE_OBJECT = type4;
        Type type5 = new Type(10, "Ljava/lang/String;", 1, 17);
        TYPE_STRING = type5;
        Type type6 = new Type(10, "Ljava/util/List;", 1, 15);
        TYPE_LIST = type6;
        Type type7 = new Type(10, "Lcom/alibaba/fastjson2/JSONReader;", 1, 33);
        TYPE_JSON_READER = type7;
        Type type8 = new Type(10, "Lcom/alibaba/fastjson2/JSONWriter;", 1, 33);
        TYPE_JSON_WRITER = type8;
        Type type9 = new Type(10, ASMUtils.DESC_SUPPLIER, 1, 28);
        TYPE_SUPPLIER = type9;
        TYPES_0 = new Type[]{type2, type5, type5, type, type6};
        TYPES_1 = new Type[]{type8, type4, type4, type3, type};
        TYPES_2 = new Type[]{type2, type9, type7};
        TYPES_3 = new Type[]{type};
        TYPES_4 = new Type[]{type7, type3, type4, type};
    }

    private Type(int i, String str, int i2, int i3) {
        this.sort = i;
        this.valueBuffer = str;
        this.valueBegin = i2;
        this.valueEnd = i3;
    }

    static Type[] getArgumentTypes(String str) {
        int i;
        str.hashCode();
        i = 0;
        switch (str) {
            case "(Lcom/alibaba/fastjson2/JSONReader;Ljava/lang/reflect/Type;Ljava/lang/Object;J)Ljava/lang/Object;":
                return TYPES_4;
            case "(Lcom/alibaba/fastjson2/JSONWriter;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;J)V":
                return TYPES_1;
            case "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;JLjava/util/List;)V":
                return TYPES_0;
            case "()V":
                return new Type[0];
            case "(J)Lcom/alibaba/fastjson2/reader/FieldReader;":
            case "(J)Ljava/lang/Object;":
                return TYPES_3;
            case "(Ljava/lang/Class;Ljava/util/function/Supplier;[Lcom/alibaba/fastjson2/reader/FieldReader;)V":
                return TYPES_2;
            default:
                int i2 = 0;
                int iMax = 1;
                while (str.charAt(iMax) != ')') {
                    while (str.charAt(iMax) == '[') {
                        iMax++;
                    }
                    int i3 = iMax + 1;
                    iMax = str.charAt(iMax) == 'L' ? Math.max(i3, str.indexOf(59, i3) + 1) : i3;
                    i2++;
                }
                Type[] typeArr = new Type[i2];
                int i4 = 1;
                while (str.charAt(i4) != ')') {
                    int i5 = i4;
                    while (str.charAt(i5) == '[') {
                        i5++;
                    }
                    int iMax2 = i5 + 1;
                    if (str.charAt(i5) == 'L') {
                        iMax2 = Math.max(iMax2, str.indexOf(59, iMax2) + 1);
                    }
                    typeArr[i] = getTypeInternal(str, i4, iMax2);
                    i++;
                    i4 = iMax2;
                }
                return typeArr;
        }
    }

    static Type getTypeInternal(String str, int i, int i2) {
        char cCharAt = str.charAt(i);
        if (cCharAt == '(') {
            return new Type(11, str, i, i2);
        }
        if (cCharAt == 'F') {
            return FLOAT_TYPE;
        }
        if (cCharAt != 'L') {
            if (cCharAt == 'S') {
                return SHORT_TYPE;
            }
            if (cCharAt == 'V') {
                return VOID_TYPE;
            }
            if (cCharAt == 'I') {
                return INT_TYPE;
            }
            if (cCharAt == 'J') {
                return LONG_TYPE;
            }
            if (cCharAt == 'Z') {
                return BOOLEAN_TYPE;
            }
            if (cCharAt != '[') {
                switch (cCharAt) {
                    case 'B':
                        return BYTE_TYPE;
                    case 'C':
                        return CHAR_TYPE;
                    case 'D':
                        return DOUBLE_TYPE;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            return new Type(9, str, i, i2);
        }
        int i3 = i2 - i;
        if (i3 == 24) {
            Type type = TYPE_TYPE;
            if (str.regionMatches(i, type.valueBuffer, 0, i3)) {
                return type;
            }
        } else if (i3 == 29) {
            Type type2 = TYPE_SUPPLIER;
            if (str.regionMatches(i, type2.valueBuffer, 0, i3)) {
                return type2;
            }
        } else if (i3 != 34) {
            switch (i3) {
                case 16:
                    Type type3 = TYPE_LIST;
                    if (str.regionMatches(i, type3.valueBuffer, 0, i3)) {
                        return type3;
                    }
                    break;
                case 17:
                    Type type4 = TYPE_CLASS;
                    if (str.regionMatches(i, type4.valueBuffer, 0, i3)) {
                        return type4;
                    }
                    break;
                case 18:
                    Type type5 = TYPE_STRING;
                    if (str.regionMatches(i, type5.valueBuffer, 0, i3)) {
                        return type5;
                    }
                    Type type6 = TYPE_OBJECT;
                    if (str.regionMatches(i, type6.valueBuffer, 0, i3)) {
                        return type6;
                    }
                    break;
            }
        } else {
            Type type7 = TYPE_JSON_WRITER;
            if (str.regionMatches(i, type7.valueBuffer, 0, i3)) {
                return type7;
            }
            Type type8 = TYPE_JSON_READER;
            if (str.regionMatches(i, type8.valueBuffer, 0, i3)) {
                return type8;
            }
        }
        return new Type(10, str, i + 1, i2 - 1);
    }

    public String getDescriptor() {
        int i = this.sort;
        if (i != 10) {
            if (i == 12) {
                return "L" + this.valueBuffer.substring(this.valueBegin, this.valueEnd) + ';';
            }
            String str = this.valueBuffer;
            str.hashCode();
            if (str.equals("VZCBSIFJD")) {
                if (this.valueBegin == 7 && this.valueEnd == 8) {
                    return "J";
                }
            } else if (str.equals("(Ljava/lang/Class;Ljava/util/function/Supplier;[Lcom/alibaba/fastjson2/reader/FieldReader;)V") && this.valueBegin == 47 && this.valueEnd == 90) {
                return "[Lcom/alibaba/fastjson2/reader/FieldReader;";
            }
            return this.valueBuffer.substring(this.valueBegin, this.valueEnd);
        }
        String str2 = this.valueBuffer;
        str2.hashCode();
        switch (str2) {
            case "(Lcom/alibaba/fastjson2/JSONReader;Ljava/lang/reflect/Type;Ljava/lang/Object;J)Ljava/lang/Object;":
                int i2 = this.valueBegin;
                if (i2 == 2 && this.valueEnd == 34) {
                    return "Lcom/alibaba/fastjson2/JSONReader;";
                }
                if (i2 == 36 && this.valueEnd == 58) {
                    return "Ljava/lang/reflect/Type;";
                }
                if (i2 == 60 && this.valueEnd == 76) {
                    return "Ljava/lang/Object;";
                }
                break;
            case "(Lcom/alibaba/fastjson2/JSONWriter;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;J)V":
                int i3 = this.valueBegin;
                if (i3 == 2 && this.valueEnd == 34) {
                    return "Lcom/alibaba/fastjson2/JSONWriter;";
                }
                if (i3 == 36 && this.valueEnd == 52) {
                    return "Ljava/lang/Object;";
                }
                if (i3 == 54 && this.valueEnd == 70) {
                    return "Ljava/lang/Object;";
                }
                if (i3 == 72 && this.valueEnd == 94) {
                    return "Ljava/lang/reflect/Type;";
                }
                break;
            case "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;JLjava/util/List;)V":
                int i4 = this.valueBegin;
                if (i4 == 2 && this.valueEnd == 17) {
                    return "Ljava/lang/Class;";
                }
                if (i4 == 19 && this.valueEnd == 35) {
                    return "Ljava/lang/String;";
                }
                if (i4 == 37 && this.valueEnd == 53) {
                    return "Ljava/lang/String;";
                }
                if (i4 == 56 && this.valueEnd == 70) {
                    return "Ljava/util/List;";
                }
                break;
            case "(Ljava/lang/Class;Ljava/util/function/Supplier;[Lcom/alibaba/fastjson2/reader/FieldReader;)V":
                int i5 = this.valueBegin;
                if (i5 == 2 && this.valueEnd == 17) {
                    return "Ljava/lang/Class;";
                }
                if (i5 == 19 && this.valueEnd == 46) {
                    return ASMUtils.DESC_SUPPLIER;
                }
                break;
        }
        if (this.valueBegin == 1 && this.valueEnd + 1 == this.valueBuffer.length()) {
            return this.valueBuffer;
        }
        return this.valueBuffer.substring(this.valueBegin - 1, this.valueEnd + 1);
    }

    public static int getArgumentsAndReturnSizes(String str) {
        str.hashCode();
        switch (str) {
            case "(Ljava/lang/Enum;)V":
            case "(Ljava/lang/String;)V":
            case "(I)V":
            case "(Lcom/alibaba/fastjson2/JSONWriter;)V":
            case "(Ljava/lang/Object;)V":
                return 8;
            case "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;JLcom/alibaba/fastjson2/schema/JSONSchema;Ljava/util/function/Supplier;Ljava/util/function/Function;[Lcom/alibaba/fastjson2/reader/FieldReader;)V":
                return 40;
            case "(Lcom/alibaba/fastjson2/JSONReader;)Ljava/lang/Object;":
            case "(Lcom/alibaba/fastjson2/JSONReader;)Lcom/alibaba/fastjson2/reader/ObjectReader;":
            case "(C)Z":
            case "(Lcom/alibaba/fastjson2/JSONWriter;)Z":
            case "(I)Ljava/lang/Object;":
            case "(Ljava/lang/Object;)Z":
            case "(I)Ljava/lang/Integer;":
                return 9;
            case "(Ljava/lang/Object;JLjava/lang/Object;)V":
            case "(Lcom/alibaba/fastjson2/JSONReader;Ljava/util/List;ILjava/lang/String;)V":
                return 20;
            case "(Lcom/alibaba/fastjson2/JSONReader;Ljava/lang/Class;J)Lcom/alibaba/fastjson2/reader/ObjectReader;":
            case "(Lcom/alibaba/fastjson2/JSONReader;Ljava/lang/Class;J)Ljava/lang/Object;":
                return 21;
            case "(Lcom/alibaba/fastjson2/JSONReader;Ljava/lang/reflect/Type;Ljava/lang/Object;J)Ljava/lang/Object;":
                return 25;
            case "(Lcom/alibaba/fastjson2/JSONWriter;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;J)V":
            case "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;JLjava/util/List;)V":
                return 28;
            case "(Lcom/alibaba/fastjson2/JSONReader;Ljava/lang/Object;Ljava/lang/String;)V":
            case "(Lcom/alibaba/fastjson2/JSONWriter;ZLjava/util/List;)V":
            case "(Ljava/lang/Class;Ljava/util/function/Supplier;[Lcom/alibaba/fastjson2/reader/FieldReader;)V":
            case "(Lcom/alibaba/fastjson2/JSONWriter;J)V":
                return 16;
            case "(Lcom/alibaba/fastjson2/JSONWriter;Ljava/lang/reflect/Type;)Lcom/alibaba/fastjson2/writer/ObjectWriter;":
            case "(J)Z":
            case "(Lcom/alibaba/fastjson2/writer/FieldWriter;Ljava/lang/Object;)Ljava/lang/String;":
            case "(Ljava/lang/Object;Ljava/lang/reflect/Type;)Z":
            case "(J)Lcom/alibaba/fastjson2/reader/FieldReader;":
            case "(J)Ljava/lang/Object;":
            case "(Lcom/alibaba/fastjson2/JSONWriter;Ljava/lang/Class;)Lcom/alibaba/fastjson2/writer/ObjectWriter;":
                return 13;
            case "(Ljava/util/List;Ljava/lang/reflect/Type;)V":
            case "(J)V":
            case "(Lcom/alibaba/fastjson2/JSONWriter;Ljava/lang/Enum;)V":
            case "(Lcom/alibaba/fastjson2/JSONReader;Ljava/lang/Object;)V":
            case "(Lcom/alibaba/fastjson2/JSONWriter;I)V":
                return 12;
            case "()Ljava/lang/Class;":
            case "()I":
            case "()Z":
            case "()Ljava/lang/String;":
                return 5;
            case "()J":
                return 6;
            case "()V":
                return 4;
            default:
                char cCharAt = str.charAt(1);
                int i = 1;
                int i2 = 1;
                while (cCharAt != ')') {
                    if (cCharAt == 'J' || cCharAt == 'D') {
                        i++;
                        i2 += 2;
                    } else {
                        while (str.charAt(i) == '[') {
                            i++;
                        }
                        int iMax = i + 1;
                        if (str.charAt(i) == 'L') {
                            iMax = Math.max(iMax, str.indexOf(59, iMax) + 1);
                        }
                        i2++;
                        i = iMax;
                    }
                    cCharAt = str.charAt(i);
                }
                char cCharAt2 = str.charAt(i + 1);
                if (cCharAt2 == 'V') {
                    return i2 << 2;
                }
                return (i2 << 2) | ((cCharAt2 == 'J' || cCharAt2 == 'D') ? 2 : 1);
        }
    }

    public String getClassName() {
        switch (this.sort) {
            case 0:
                return "void";
            case 1:
                return "boolean";
            case 2:
                return "char";
            case 3:
                return "byte";
            case 4:
                return "short";
            case 5:
                return "int";
            case 6:
                return "float";
            case 7:
                return "long";
            case 8:
                return "double";
            case 9:
                StringBuilder sb = new StringBuilder(getTypeInternal(this.valueBuffer, this.valueBegin + getDimensions(), this.valueEnd).getClassName());
                for (int dimensions = getDimensions(); dimensions > 0; dimensions--) {
                    sb.append(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
                }
                return sb.toString();
            case 10:
            case 12:
                return this.valueBuffer.substring(this.valueBegin, this.valueEnd).replace('/', '.');
            case 11:
            default:
                throw new AssertionError();
        }
    }

    public int getDimensions() {
        int i = 1;
        while (this.valueBuffer.charAt(this.valueBegin + i) == '[') {
            i++;
        }
        return i;
    }
}
