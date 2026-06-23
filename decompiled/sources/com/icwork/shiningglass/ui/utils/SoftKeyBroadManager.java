package com.icwork.shiningglass.ui.utils;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SoftKeyBroadManager implements ViewTreeObserver.OnGlobalLayoutListener {
    private final View activityRootView;
    private boolean isSoftKeyboardOpened;
    private int lastSoftKeyboardHeightInPx;
    private final List<SoftKeyboardStateListener> listeners;

    public interface SoftKeyboardStateListener {
        void onSoftKeyboardClosed();

        void onSoftKeyboardOpened(int i);
    }

    public SoftKeyBroadManager(View view) {
        this(view, false);
    }

    public SoftKeyBroadManager(View view, boolean z) {
        this.listeners = new LinkedList();
        this.activityRootView = view;
        this.isSoftKeyboardOpened = z;
        view.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        Rect rect = new Rect();
        this.activityRootView.getWindowVisibleDisplayFrame(rect);
        int height = this.activityRootView.getRootView().getHeight() - (rect.bottom - rect.top);
        Log.d("SoftKeyboardStateHelper", "heightDiff:" + height);
        boolean z = this.isSoftKeyboardOpened;
        if (!z && height > 500) {
            this.isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened(height);
        } else {
            if (!z || height >= 500) {
                return;
            }
            this.isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
        }
    }

    public void setIsSoftKeyboardOpened(boolean z) {
        this.isSoftKeyboardOpened = z;
    }

    public boolean isSoftKeyboardOpened() {
        return this.isSoftKeyboardOpened;
    }

    public int getLastSoftKeyboardHeightInPx() {
        return this.lastSoftKeyboardHeightInPx;
    }

    public void addSoftKeyboardStateListener(SoftKeyboardStateListener softKeyboardStateListener) {
        this.listeners.add(softKeyboardStateListener);
    }

    public void removeSoftKeyboardStateListener(SoftKeyboardStateListener softKeyboardStateListener) {
        this.listeners.remove(softKeyboardStateListener);
    }

    private void notifyOnSoftKeyboardOpened(int i) {
        this.lastSoftKeyboardHeightInPx = i;
        for (SoftKeyboardStateListener softKeyboardStateListener : this.listeners) {
            if (softKeyboardStateListener != null) {
                softKeyboardStateListener.onSoftKeyboardOpened(i);
            }
        }
    }

    private void notifyOnSoftKeyboardClosed() {
        for (SoftKeyboardStateListener softKeyboardStateListener : this.listeners) {
            if (softKeyboardStateListener != null) {
                softKeyboardStateListener.onSoftKeyboardClosed();
            }
        }
    }
}
