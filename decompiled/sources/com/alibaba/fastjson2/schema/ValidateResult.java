package com.alibaba.fastjson2.schema;

/* JADX INFO: loaded from: classes.dex */
public final class ValidateResult {
    final Object[] args;
    final ValidateResult cause;
    final String format;
    String message;
    private final boolean success;

    public ValidateResult(ValidateResult validateResult, String str, Object... objArr) {
        this.success = false;
        this.format = str;
        this.args = objArr;
        this.cause = validateResult;
        if (objArr.length == 0) {
            this.message = str;
        }
    }

    public ValidateResult(boolean z, String str, Object... objArr) {
        this.success = z;
        this.format = str;
        this.args = objArr;
        this.cause = null;
        if (objArr.length == 0) {
            this.message = str;
        }
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        String str;
        String str2 = this.message;
        if (str2 == null && (str = this.format) != null) {
            Object[] objArr = this.args;
            if (objArr.length > 0) {
                str2 = String.format(str, objArr);
                if (this.cause != null) {
                    str2 = str2 + "; " + this.cause.getMessage();
                }
                this.message = str2;
            }
        }
        return str2;
    }
}
