package me.leefeng.promptlibrary;

import androidx.core.view.ViewCompat;

/* JADX INFO: loaded from: classes.dex */
public class Builder {
    private static Builder alertDefaultBuilder;
    private static Builder defaultBuilder;
    boolean cancleAble;
    int icon;
    long loadingDuration;
    String text;
    int backColor = ViewCompat.MEASURED_STATE_MASK;
    int backAlpha = 90;
    int textColor = -1;
    float textSize = 14.0f;
    float padding = 15.0f;
    float round = 8.0f;
    int roundColor = ViewCompat.MEASURED_STATE_MASK;
    int roundAlpha = 120;
    boolean touchAble = false;
    boolean withAnim = true;
    long stayDuration = 1000;
    int sheetPressAlph = 15;
    int sheetCellHeight = 48;
    int sheetCellPad = 13;

    public Builder sheetCellPad(int i) {
        this.sheetCellPad = i;
        return this;
    }

    public Builder sheetCellHeight(int i) {
        this.sheetCellHeight = i;
        return this;
    }

    public Builder sheetPressAlph(int i) {
        this.sheetPressAlph = i;
        return this;
    }

    public Builder backColor(int i) {
        this.backColor = i;
        return this;
    }

    public Builder backAlpha(int i) {
        this.backAlpha = i;
        return this;
    }

    public Builder textColor(int i) {
        this.textColor = i;
        return this;
    }

    public Builder textSize(float f) {
        this.textSize = f;
        return this;
    }

    public Builder padding(float f) {
        this.padding = f;
        return this;
    }

    public Builder round(float f) {
        this.round = f;
        return this;
    }

    public Builder roundColor(int i) {
        this.roundColor = i;
        return this;
    }

    public Builder roundAlpha(int i) {
        this.roundAlpha = i;
        return this;
    }

    public Builder touchAble(boolean z) {
        this.touchAble = z;
        return this;
    }

    public Builder withAnim(boolean z) {
        this.withAnim = z;
        return this;
    }

    public Builder stayDuration(long j) {
        this.stayDuration = j;
        return this;
    }

    public Builder cancleAble(boolean z) {
        this.cancleAble = z;
        return this;
    }

    static Builder getDefaultBuilder() {
        if (defaultBuilder == null) {
            defaultBuilder = new Builder();
        }
        return defaultBuilder;
    }

    static Builder getAlertDefaultBuilder() {
        if (alertDefaultBuilder == null) {
            alertDefaultBuilder = new Builder().roundColor(-1).roundAlpha(255).textColor(-7829368).textSize(15.0f).cancleAble(true);
        }
        return alertDefaultBuilder;
    }

    public Builder icon(int i) {
        this.icon = i;
        return this;
    }

    public Builder text(String str) {
        this.text = str;
        return this;
    }

    public Builder loadingDuration(long j) {
        this.loadingDuration = j;
        return this;
    }
}
