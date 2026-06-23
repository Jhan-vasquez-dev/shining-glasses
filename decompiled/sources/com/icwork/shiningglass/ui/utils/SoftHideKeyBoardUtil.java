package com.icwork.shiningglass.ui.utils;

import android.R;
import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes.dex */
public class SoftHideKeyBoardUtil {
    private int contentHeight;
    private FrameLayout.LayoutParams frameLayoutParams;
    private boolean isfirst = true;
    private View mChildOfContent;
    private int statusBarHeight;
    private int usableHeightPrevious;

    public static void assistActivity(Activity activity) {
        new SoftHideKeyBoardUtil(activity);
    }

    private SoftHideKeyBoardUtil(Activity activity) {
        FrameLayout frameLayout = (FrameLayout) activity.findViewById(R.id.content);
        frameLayout.setBackgroundColor(0);
        View childAt = frameLayout.getChildAt(0);
        this.mChildOfContent = childAt;
        childAt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.icwork.shiningglass.ui.utils.SoftHideKeyBoardUtil.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                if (SoftHideKeyBoardUtil.this.isfirst) {
                    SoftHideKeyBoardUtil softHideKeyBoardUtil = SoftHideKeyBoardUtil.this;
                    softHideKeyBoardUtil.contentHeight = softHideKeyBoardUtil.mChildOfContent.getHeight();
                    SoftHideKeyBoardUtil.this.isfirst = false;
                }
                SoftHideKeyBoardUtil.this.possiblyResizeChildOfContent();
            }
        });
        this.frameLayoutParams = (FrameLayout.LayoutParams) this.mChildOfContent.getLayoutParams();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void possiblyResizeChildOfContent() {
        int iComputeUsableHeight = computeUsableHeight();
        if (iComputeUsableHeight != this.usableHeightPrevious) {
            int height = this.mChildOfContent.getRootView().getHeight();
            int i = height - iComputeUsableHeight;
            if (i > height / 4) {
                this.frameLayoutParams.height = (height - i) + this.statusBarHeight;
            } else {
                this.frameLayoutParams.height = this.contentHeight;
            }
            this.mChildOfContent.requestLayout();
            this.usableHeightPrevious = iComputeUsableHeight;
        }
    }

    private int computeUsableHeight() {
        Rect rect = new Rect();
        this.mChildOfContent.getWindowVisibleDisplayFrame(rect);
        return rect.bottom - rect.top;
    }
}
