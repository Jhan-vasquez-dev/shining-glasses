package com.icwork.shiningglass.ui.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/* JADX INFO: loaded from: classes.dex */
public class SoftKeyBoardManager {
    private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;
    private View rootView;
    int rootViewVisibleHeight;

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardHide(int i);

        void keyBoardShow(int i);
    }

    public SoftKeyBoardManager(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        this.rootView = decorView;
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.icwork.shiningglass.ui.utils.SoftKeyBoardManager.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                Rect rect = new Rect();
                SoftKeyBoardManager.this.rootView.getWindowVisibleDisplayFrame(rect);
                int iHeight = rect.height();
                System.out.println("" + iHeight);
                if (SoftKeyBoardManager.this.rootViewVisibleHeight == 0) {
                    SoftKeyBoardManager.this.rootViewVisibleHeight = iHeight;
                    return;
                }
                if (SoftKeyBoardManager.this.rootViewVisibleHeight == iHeight) {
                    return;
                }
                if (SoftKeyBoardManager.this.rootViewVisibleHeight - iHeight > 200) {
                    if (SoftKeyBoardManager.this.onSoftKeyBoardChangeListener != null) {
                        SoftKeyBoardManager.this.onSoftKeyBoardChangeListener.keyBoardShow(SoftKeyBoardManager.this.rootViewVisibleHeight - iHeight);
                    }
                    SoftKeyBoardManager.this.rootViewVisibleHeight = iHeight;
                } else if (iHeight - SoftKeyBoardManager.this.rootViewVisibleHeight > 200) {
                    if (SoftKeyBoardManager.this.onSoftKeyBoardChangeListener != null) {
                        SoftKeyBoardManager.this.onSoftKeyBoardChangeListener.keyBoardHide(iHeight - SoftKeyBoardManager.this.rootViewVisibleHeight);
                    }
                    SoftKeyBoardManager.this.rootViewVisibleHeight = iHeight;
                }
            }
        });
    }

    public void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
    }
}
