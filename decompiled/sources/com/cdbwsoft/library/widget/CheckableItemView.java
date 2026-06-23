package com.cdbwsoft.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.Checkable;
import android.widget.FrameLayout;
import com.cdbwsoft.library.R;

/* JADX INFO: loaded from: classes.dex */
public class CheckableItemView extends FrameLayout implements Checkable {
    private int mItemId;
    private Checkable mItemView;

    public CheckableItemView(Context context) {
        super(context);
    }

    public CheckableItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public CheckableItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CheckableItemView);
        this.mItemId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.CheckableItemView_check_id, -1);
        typedArrayObtainStyledAttributes.recycle();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        KeyEvent.Callback callbackFindViewById = findViewById(this.mItemId);
        if (callbackFindViewById == null || !(callbackFindViewById instanceof Checkable)) {
            return;
        }
        this.mItemView = (Checkable) callbackFindViewById;
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        if (this.mItemView == null || !isEnabled()) {
            return;
        }
        this.mItemView.setChecked(z);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        Checkable checkable = this.mItemView;
        return checkable != null && checkable.isChecked();
    }

    @Override // android.widget.Checkable
    public void toggle() {
        if (this.mItemView == null || !isEnabled()) {
            return;
        }
        this.mItemView.toggle();
    }
}
