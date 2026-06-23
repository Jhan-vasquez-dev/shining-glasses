package com.icwork.shiningglass.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.model.bean.DiyData;
import com.icwork.shiningglass.ui.widget.LedView1;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DiyImageSelectListAdapter extends BaseAdapter {
    List<DiyData> brandsList;
    Context context;
    LayoutInflater mInflater;
    private int selectPosition = -1;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public DiyImageSelectListAdapter(Context context, List<DiyData> list) {
        this.context = context;
        this.brandsList = list;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public void setList(List<DiyData> list) {
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
            view = this.mInflater.inflate(R.layout.item_image_select_adapter, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.iv_item = (LedView1) view.findViewById(R.id.item_ledview);
            viewHolder.tv_index = (TextView) view.findViewById(R.id.tv_select_index);
            viewHolder.iv_item.setLayerType(1, null);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.iv_item.setPointMargin(0);
        viewHolder.iv_item.removeAllViews();
        viewHolder.iv_item.init(36, 12);
        ArrayList<Integer> colorArray = this.brandsList.get(i).getColorArray();
        if (this.brandsList.get(i).getIndex() > 0) {
            viewHolder.iv_item.setSaveDiyData(colorArray, true);
            viewHolder.iv_item.setAlpha(1.0f);
            viewHolder.tv_index.setBackgroundResource(R.mipmap.diy_image_selected);
            view.setBackgroundColor(0);
            viewHolder.tv_index.setText(this.brandsList.get(i).getIndex() + "");
            return view;
        }
        viewHolder.iv_item.setAlpha(0.65f);
        viewHolder.iv_item.setSaveDiyData(colorArray, false);
        viewHolder.tv_index.setText("");
        viewHolder.tv_index.setBackgroundResource(R.mipmap.diy_image_unselected);
        view.setBackgroundColor(0);
        return view;
    }

    public class ViewHolder {
        LedView1 iv_item;
        TextView tv_index;

        public ViewHolder() {
        }
    }
}
