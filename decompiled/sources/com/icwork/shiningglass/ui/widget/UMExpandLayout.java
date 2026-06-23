package com.icwork.shiningglass.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/* JADX INFO: loaded from: classes.dex */
public class UMExpandLayout extends RelativeLayout {
    private long animationDuration;
    private boolean isExpand;
    private View layoutView;
    private int viewHeight;

    public UMExpandLayout(Context context) {
        this(context, null);
    }

    public UMExpandLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public UMExpandLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    private void initView() {
        this.layoutView = this;
        this.isExpand = true;
        this.animationDuration = 300L;
        setViewDimensions();
    }

    public void initExpand(boolean z) {
        this.isExpand = z;
        if (z) {
            return;
        }
        animateToggle(10L);
    }

    public void setAnimationDuration(long j) {
        this.animationDuration = j;
    }

    private void setViewDimensions() {
        this.layoutView.post(new Runnable() { // from class: com.icwork.shiningglass.ui.widget.UMExpandLayout.1
            @Override // java.lang.Runnable
            public void run() {
                if (UMExpandLayout.this.viewHeight <= 0) {
                    UMExpandLayout uMExpandLayout = UMExpandLayout.this;
                    uMExpandLayout.viewHeight = uMExpandLayout.layoutView.getMeasuredHeight();
                }
            }
        });
    }

    public static void setViewHeight(View view, int i) {
        view.getLayoutParams().height = i;
        view.requestLayout();
    }

    private void animateToggle(long j) {
        ValueAnimator valueAnimatorOfFloat = this.isExpand ? ValueAnimator.ofFloat(0.0f, this.viewHeight) : ValueAnimator.ofFloat(this.viewHeight, 0.0f);
        long j2 = j / 2;
        valueAnimatorOfFloat.setDuration(j2);
        valueAnimatorOfFloat.setStartDelay(j2);
        valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.icwork.shiningglass.ui.widget.UMExpandLayout.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                UMExpandLayout.setViewHeight(UMExpandLayout.this.layoutView, (int) ((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        valueAnimatorOfFloat.start();
    }

    public boolean isExpand() {
        return this.isExpand;
    }

    public void collapse() {
        this.isExpand = false;
        animateToggle(this.animationDuration);
    }

    public void expand() {
        this.isExpand = true;
        animateToggle(this.animationDuration);
    }

    public void toggleExpand() {
        if (this.isExpand) {
            collapse();
        } else {
            expand();
        }
    }
}
