package com.icwork.shiningglass.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cdbwsoft.library.ble.BleDevice;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.BaseAdapter;
import com.icwork.shiningglass.ui.utils.LogUtil;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DeviceAdapter extends BaseAdapter<BleDevice> {
    private int mCheckedItemPosition;
    private OnConnectListener onConnectListener;

    public interface OnConnectListener {
        void connect(BleDevice bleDevice);

        void onLongClick(BleDevice bleDevice);

        void rename(BleDevice bleDevice);
    }

    @Override // com.icwork.shiningglass.base.BaseAdapter
    public int getContentView() {
        return R.layout.item_device;
    }

    public DeviceAdapter(Context context, List<BleDevice> list) {
        super(context, list);
    }

    public void setOnlistener(OnConnectListener onConnectListener) {
        this.onConnectListener = onConnectListener;
    }

    public void setCheckedItemPosition(int i) {
        this.mCheckedItemPosition = i;
    }

    @Override // com.icwork.shiningglass.base.BaseAdapter
    public void onInitView(View view, int i) {
        final BleDevice item = getItem(i);
        ImageView imageView = (ImageView) getAdapterView(view, R.id.cb_connect);
        TextView textView = (TextView) getAdapterView(view, R.id.tv_name);
        RelativeLayout relativeLayout = (RelativeLayout) getAdapterView(view, R.id.ll_root);
        textView.setText(item.getBleName());
        if (item.isConnected()) {
            textView.setTextColor(this.mContext.getResources().getColor(R.color.white));
            imageView.setImageResource(R.mipmap.magic_device_on);
            relativeLayout.setBackgroundResource(R.mipmap.magic_device_item_bg1);
        } else {
            textView.setTextColor(this.mContext.getResources().getColor(R.color.white));
            imageView.setImageResource(R.mipmap.magic_device_off);
            relativeLayout.setBackgroundResource(R.mipmap.magic_device_item_bg);
        }
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.adapter.DeviceAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (DeviceAdapter.this.onConnectListener != null) {
                    DeviceAdapter.this.onConnectListener.rename(item);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.adapter.DeviceAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (DeviceAdapter.this.onConnectListener != null) {
                    DeviceAdapter.this.onConnectListener.connect(item);
                }
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.icwork.shiningglass.ui.adapter.DeviceAdapter.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                LogUtil.d("===onLongClick111===");
                if (DeviceAdapter.this.onConnectListener == null) {
                    return false;
                }
                DeviceAdapter.this.onConnectListener.onLongClick(item);
                return false;
            }
        });
    }
}
