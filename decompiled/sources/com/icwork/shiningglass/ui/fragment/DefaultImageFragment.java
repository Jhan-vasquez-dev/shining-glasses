package com.icwork.shiningglass.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.cdbwsoft.library.ble.BleManager;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.App;
import com.icwork.shiningglass.base.BaseFragment;
import com.icwork.shiningglass.base.app.C;
import com.icwork.shiningglass.base.app.SoundManager;
import com.icwork.shiningglass.ble.HeartBeatDevice;
import com.icwork.shiningglass.ble.HeartBeatDeviceFactory;
import com.icwork.shiningglass.model.data.Agreement;
import com.icwork.shiningglass.ui.activity.ConnectActivity;
import com.icwork.shiningglass.ui.adapter.ImageListAdapter;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes.dex */
public class DefaultImageFragment extends BaseFragment {
    private BleManager bleManager;
    private ArrayList<Integer> imageList;
    private ImageListAdapter imageListAdapter;
    ListView lvImage;

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected int getLayoutId() {
        return R.layout.fragment_deuault_fragment;
    }

    public static DefaultImageFragment newInstance() {
        return new DefaultImageFragment();
    }

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected void initView(View view, Bundle bundle) {
        this.lvImage = (ListView) view.findViewById(R.id.lv_image);
    }

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected void initData() {
        BleManager bleManager = ConnectActivity.getBleManager();
        this.bleManager = bleManager;
        if (bleManager == null) {
            this.bleManager = App.getInstance().getBleManager(HeartBeatDevice.class, null, new HeartBeatDeviceFactory(App.getInstance()));
        }
        initImageData();
        this.lvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.icwork.shiningglass.ui.fragment.DefaultImageFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DefaultImageFragment.this.imageListAdapter.setSelectPosition(i);
                DefaultImageFragment.this.imageListAdapter.notifyDataSetChanged();
                SoundManager.getInstance().animSelect();
                DefaultImageFragment.this.sendImage(i);
            }
        });
    }

    private void initImageData() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        this.imageList = arrayList;
        arrayList.add(Integer.valueOf(R.mipmap.image_default_0));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_1));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_2));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_3));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_4));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_5));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_6));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_7));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_8));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_9));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_10));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_11));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_12));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_13));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_14));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_15));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_16));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_17));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_18));
        this.imageList.add(Integer.valueOf(R.mipmap.image_default_19));
        ImageListAdapter imageListAdapter = new ImageListAdapter(getActivity(), this.imageList);
        this.imageListAdapter = imageListAdapter;
        this.lvImage.setAdapter((ListAdapter) imageListAdapter);
    }

    public void sendImage(int i) {
        EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
        this.bleManager.writeData(Agreement.getEncryptData(Agreement.getImageCommand(i)));
    }
}
