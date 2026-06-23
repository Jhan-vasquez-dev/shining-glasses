package com.alibaba.fastjson2;

/* JADX INFO: loaded from: classes.dex */
public class JSONValidator {
    private final JSONReader jsonReader;
    private Type type;
    private Boolean validateResult;

    public enum Type {
        Object,
        Array,
        Value
    }

    protected JSONValidator(JSONReader jSONReader) {
        this.jsonReader = jSONReader;
    }

    public static JSONValidator fromUtf8(byte[] bArr) {
        return new JSONValidator(JSONReader.of(bArr));
    }

    public static JSONValidator from(String str) {
        return new JSONValidator(JSONReader.of(str));
    }

    public static JSONValidator from(JSONReader jSONReader) {
        return new JSONValidator(jSONReader);
    }

    public boolean validate() {
        Boolean bool = this.validateResult;
        try {
            if (bool != null) {
                return bool.booleanValue();
            }
            try {
                char cCurrent = this.jsonReader.current();
                this.jsonReader.skipValue();
                this.jsonReader.close();
                if (cCurrent == '{') {
                    this.type = Type.Object;
                } else if (cCurrent == '[') {
                    this.type = Type.Array;
                } else {
                    this.type = Type.Value;
                }
                Boolean boolValueOf = Boolean.valueOf(this.jsonReader.isEnd());
                this.validateResult = boolValueOf;
                return boolValueOf.booleanValue();
            } catch (JSONException | ArrayIndexOutOfBoundsException unused) {
                Boolean bool2 = false;
                this.validateResult = bool2;
                boolean zBooleanValue = bool2.booleanValue();
                this.jsonReader.close();
                return zBooleanValue;
            }
        } catch (Throwable th) {
            this.jsonReader.close();
            throw th;
        }
    }

    public Type getType() {
        if (this.type == null) {
            validate();
        }
        return this.type;
    }
}
