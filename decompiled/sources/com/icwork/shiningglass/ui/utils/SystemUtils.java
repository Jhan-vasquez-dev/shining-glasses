package com.icwork.shiningglass.ui.utils;

import android.app.Dialog;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class SystemUtils {
    public static void hideDialogNavigationBar(final Dialog dialog) {
        dialog.getWindow().getDecorView().setSystemUiVisibility(2);
        dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() { // from class: com.icwork.shiningglass.ui.utils.SystemUtils.1
            @Override // android.view.View.OnSystemUiVisibilityChangeListener
            public void onSystemUiVisibilityChange(int i) {
                dialog.getWindow().getDecorView().setSystemUiVisibility(5894);
            }
        });
    }
}
