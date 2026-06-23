package com.cdbwsoft.library.adapter;

import android.content.Context;
import android.view.View;
import com.cdbwsoft.library.adapter.DataVO;

/* JADX INFO: loaded from: classes.dex */
public class HolderAdapter<D extends DataVO> extends SuperAdapter<D, View> {
    public HolderAdapter(Context context, int i) {
        super(context, i);
    }

    @Override // com.cdbwsoft.library.adapter.SuperAdapter
    protected void bindData(View view, D d) {
        super.bindData(view, d);
        d.bindData(view);
    }
}
