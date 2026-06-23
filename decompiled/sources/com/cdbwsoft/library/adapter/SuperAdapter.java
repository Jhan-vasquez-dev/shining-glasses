package com.cdbwsoft.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.cdbwsoft.library.adapter.DataVO;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SuperAdapter<D extends DataVO, V extends View> extends BaseAdapter {
    private V mContentView;
    private Context mContext;
    private int mLayout;
    private final List<D> mList = new ArrayList();

    protected void bindData(View view, D d) {
    }

    public SuperAdapter(Context context, int i) {
        this.mContext = context;
        this.mLayout = i;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mList.size();
    }

    @Override // android.widget.Adapter
    public D getItem(int i) {
        return this.mList.get(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return this.mList.get(i).getId();
    }

    public void setData(List<D> list) {
        synchronized (this.mList) {
            this.mList.clear();
            if (list != null) {
                this.mList.addAll(list);
            }
        }
        notifyDataSetChanged();
    }

    public void addData(D d) {
        synchronized (this.mList) {
            if (d != null) {
                this.mList.add(d);
            }
        }
        notifyDataSetChanged();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (this.mContentView == null) {
            this.mContentView = (V) LayoutInflater.from(this.mContext).inflate(this.mLayout, viewGroup, false);
        }
        DataVO item = getItem(i);
        if (item != null) {
            item.setPosition(i);
            V v = this.mContentView;
            if (v instanceof DataView) {
                ((DataView) v).bindData(item);
                ((DataView) this.mContentView).setFirst(i == 0);
                ((DataView) this.mContentView).setLast(i == this.mList.size());
            } else {
                bindData(v, item);
            }
        }
        return this.mContentView;
    }
}
