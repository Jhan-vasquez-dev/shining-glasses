package me.leefeng.promptlibrary;

import android.graphics.Color;
import android.graphics.RectF;
import androidx.core.view.ViewCompat;

/* JADX INFO: loaded from: classes.dex */
public class PromptButton {
    private boolean focus;
    private boolean isDelyClick;
    private PromptButtonListener listener;
    private RectF rect;
    private String text;
    private int textColor = ViewCompat.MEASURED_STATE_MASK;
    private float textSize = 18.0f;
    private int focusBacColor = Color.parseColor("#DCDCDC");
    private boolean dismissAfterClick = true;

    public PromptButton(String str, PromptButtonListener promptButtonListener) {
        this.text = "confirm";
        this.text = str;
        this.listener = promptButtonListener;
    }

    public PromptButton(String str, PromptButtonListener promptButtonListener, boolean z) {
        this.text = "confirm";
        this.text = str;
        this.listener = promptButtonListener;
        this.isDelyClick = z;
    }

    public boolean isFocus() {
        return this.focus;
    }

    public void setFocus(boolean z) {
        this.focus = z;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTouchRect(RectF rectF) {
        this.rect = rectF;
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }

    public void setTextSize(float f) {
        this.textSize = f;
    }

    public RectF getRect() {
        return this.rect;
    }

    public void setRect(RectF rectF) {
        this.rect = rectF;
    }

    public PromptButtonListener getListener() {
        return this.listener;
    }

    public void setListener(PromptButtonListener promptButtonListener) {
        this.listener = promptButtonListener;
    }

    public int getFocusBacColor() {
        return this.focusBacColor;
    }

    public void setFocusBacColor(int i) {
        this.focusBacColor = i;
    }

    public boolean isDismissAfterClick() {
        return this.dismissAfterClick;
    }

    public void setDismissAfterClick(boolean z) {
        this.dismissAfterClick = z;
    }

    public boolean isDelyClick() {
        return this.isDelyClick;
    }

    public void setDelyClick(boolean z) {
        this.isDelyClick = z;
    }
}
