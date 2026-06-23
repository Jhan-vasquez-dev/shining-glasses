package com.icwork.shiningglass.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.icwork.shiningglass.R;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ImageListAdapter extends BaseAdapter {
    List<Integer> brandsList;
    Context context;
    LayoutInflater mInflater;
    private int selectPosition = -1;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public ImageListAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.brandsList = list;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public void setList(List<Integer> list) {
        this.brandsList = list;
    }

    public void setSelectPosition(int i) {
        this.selectPosition = i;
    }

    public int getSelectPosition() {
        return this.selectPosition;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.brandsList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.item_image_default_adapter, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.iv_item = (ImageView) view.findViewById(R.id.item_ledview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.iv_item.setImageResource(this.brandsList.get(i).intValue());
        if (this.selectPosition == i) {
            viewHolder.iv_item.setAlpha(1.0f);
            view.setBackgroundResource(R.mipmap.input_text_show_bg);
            return view;
        }
        viewHolder.iv_item.setAlpha(0.65f);
        view.setBackgroundColor(Color.parseColor("#00000000"));
        return view;
    }

    public class ViewHolder {
        ImageView iv_item;

        public ViewHolder() {
        }
    }
}
