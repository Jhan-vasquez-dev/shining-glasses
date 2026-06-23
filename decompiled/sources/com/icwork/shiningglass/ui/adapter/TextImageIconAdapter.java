package com.icwork.shiningglass.ui.adapter;

import android.content.Context;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.ui.adapter.baseadapter.RecyclerAdapter;
import com.icwork.shiningglass.ui.adapter.baseadapter.RecyclerViewHolder;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TextImageIconAdapter extends RecyclerAdapter<Integer> {
    private Context context;
    private OnItemClickListener onItemClickListener;

    interface OnItemClickListener {
        void onClick(int i);
    }

    public void setOniClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override // com.icwork.shiningglass.ui.adapter.baseadapter.RecyclerAdapter
    public void convert(RecyclerViewHolder recyclerViewHolder, Integer num) {
        recyclerViewHolder.setImageResource(R.id.iv_icon, num.intValue());
    }

    public TextImageIconAdapter(Context context, int i, List<Integer> list) {
        super(context, i, list);
        this.context = context;
    }
}
