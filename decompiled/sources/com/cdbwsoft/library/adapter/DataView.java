package com.cdbwsoft.library.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import com.cdbwsoft.library.R;
import com.cdbwsoft.library.adapter.DataVO;

/* JADX INFO: loaded from: classes.dex */
public class DataView<D extends DataVO> extends FrameLayout implements Checkable {
    private static final int[] STATE_FIRST = {R.attr.state_first};
    private static final int[] STATE_LAST = {R.attr.state_last};
    private boolean mIsFirst;
    private boolean mIsLast;
    private int mItemId;
    private Checkable mItemView;

    public DataView(Context context) {
        super(context);
        this.mIsFirst = false;
        this.mIsLast = false;
    }

    public DataView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsFirst = false;
        this.mIsLast = false;
        init(context, attributeSet);
    }

    public DataView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsFirst = false;
        this.mIsLast = false;
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CheckableItemView);
        this.mItemId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.CheckableItemView_check_id, -1);
        typedArrayObtainStyledAttributes.recycle();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        View viewFindViewById = findViewById(this.mItemId);
        if (viewFindViewById == 0 || !(viewFindViewById instanceof Checkable)) {
            return;
        }
        viewFindViewById.setClickable(false);
        this.mItemView = (Checkable) viewFindViewById;
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        Checkable checkable = this.mItemView;
        if (checkable != null) {
            checkable.setChecked(z);
        }
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        Checkable checkable = this.mItemView;
        return checkable != null && checkable.isChecked();
    }

    @Override // android.widget.Checkable
    public void toggle() {
        Checkable checkable = this.mItemView;
        if (checkable != null) {
            checkable.toggle();
        }
    }

    public void bindData(D d) {
        if (d == null) {
            return;
        }
        d.bindData(this);
    }

    public void setFirst(boolean z) {
        if (this.mIsFirst != z) {
            this.mIsFirst = z;
            drawableStateChanged();
        }
    }

    public void setLast(boolean z) {
        if (this.mIsLast != z) {
            this.mIsLast = z;
            drawableStateChanged();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected int[] onCreateDrawableState(int i) {
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i + 2);
        if (this.mIsFirst) {
            mergeDrawableStates(iArrOnCreateDrawableState, STATE_FIRST);
        }
        if (this.mIsLast) {
            mergeDrawableStates(iArrOnCreateDrawableState, STATE_LAST);
        }
        return iArrOnCreateDrawableState;
    }
}
