package com.icwork.shiningglass.ui.utils;

import android.view.animation.TranslateAnimation;

/* JADX INFO: loaded from: classes.dex */
public class AnimationUtil {
    private static final String TAG = "AnimationUtil";

    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
        translateAnimation.setDuration(500L);
        return translateAnimation;
    }

    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        translateAnimation.setDuration(500L);
        return translateAnimation;
    }
}
