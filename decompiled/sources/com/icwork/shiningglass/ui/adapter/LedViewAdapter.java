package com.icwork.shiningglass.ui.adapter;

import android.content.Context;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.model.bean.TextData;
import com.icwork.shiningglass.ui.adapter.baseadapter.RecyclerAdapter;
import com.icwork.shiningglass.ui.adapter.baseadapter.RecyclerViewHolder;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.widget.ledaddview.LedView;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class LedViewAdapter extends RecyclerAdapter<TextData> {
    private Context context;

    @Override // com.icwork.shiningglass.ui.adapter.baseadapter.RecyclerAdapter
    public void convert(RecyclerViewHolder recyclerViewHolder, TextData textData) {
        LedView ledView = (LedView) recyclerViewHolder.getView(R.id.ltv_preview);
        ledView.setMode(1);
        ledView.setPointMargin(0);
        ledView.setLayerType(1, null);
        ledView.init(textData.getWidthCount(), 12);
        LogUtil.d("textColor:" + textData.getColor());
        if (textData.getColor() != 0) {
            ledView.setSelectedColor(textData.getColor());
        }
        ledView.setTextData(textData.getData());
    }

    public LedViewAdapter(Context context, int i, List<TextData> list) {
        super(context, i, list);
        this.context = context;
    }
}
