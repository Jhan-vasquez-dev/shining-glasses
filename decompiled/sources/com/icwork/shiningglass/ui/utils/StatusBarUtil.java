package com.icwork.shiningglass.ui.utils;

import android.R;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes.dex */
public class StatusBarUtil {
    public static void setStatusBarUpperAPI19(Activity activity) {
        activity.getWindow().addFlags(67108864);
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(R.id.content);
        int statusBarHeight = getStatusBarHeight(activity);
        int color = activity.getResources().getColor(0);
        View childAt = viewGroup.getChildAt(0);
        if (childAt != null && childAt.getLayoutParams() != null && childAt.getLayoutParams().height == statusBarHeight) {
            childAt.setBackgroundColor(color);
            return;
        }
        View view = new View(activity);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, statusBarHeight);
        view.setBackgroundColor(color);
        viewGroup.addView(view, 0, layoutParams);
    }

    private static int getStatusBarHeight(Activity activity) {
        int identifier = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return activity.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }
}
