package com.icwork.shiningglass.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import com.icwork.shiningglass.ui.adapter.AnimListAdapter;
import com.icwork.shiningglass.ui.utils.ClickFilter;
import com.icwork.shiningglass.ui.utils.LogUtil;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes.dex */
public class AnimFragment extends BaseFragment implements View.OnClickListener {
    private static boolean isClick = false;
    private BleManager bleManager;
    private int curPosition = -1;
    private AnimListAdapter imageListAdapter;
    ImageView ivBack;
    ImageView ivForward;
    ListView lvImage;
    private ArrayList<Integer> namesList;
    View top;
    TextView tvTitle;

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected int getLayoutId() {
        return R.layout.fragment_anim;
    }

    public static AnimFragment newInstance(boolean z) {
        AnimFragment animFragment = new AnimFragment();
        isClick = z;
        return animFragment;
    }

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected void initView(View view, Bundle bundle) {
        this.ivBack = (ImageView) view.findViewById(R.id.iv_back);
        this.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        this.ivForward = (ImageView) view.findViewById(R.id.iv_forward);
        this.lvImage = (ListView) view.findViewById(R.id.lv_image);
        this.top = view.findViewById(R.id.top);
        this.ivBack.setOnClickListener(this);
        this.ivForward.setOnClickListener(this);
        this.ivBack.setImageResource(R.mipmap.text_magic_back);
        this.ivBack.setVisibility(0);
        this.ivForward.setVisibility(0);
        this.tvTitle.setText(getString(R.string.anim));
        this.ivForward.setImageResource(R.mipmap.anim_loop);
    }

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected void initData() {
        BleManager bleManager = ConnectActivity.getBleManager();
        this.bleManager = bleManager;
        if (bleManager == null) {
            this.bleManager = App.getInstance().getBleManager(HeartBeatDevice.class, null, new HeartBeatDeviceFactory(App.getInstance()));
        }
        initDatas1664();
        this.lvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.icwork.shiningglass.ui.fragment.AnimFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                SoundManager.getInstance().animSelect();
                LogUtil.d("position:" + i);
                LogUtil.d("curPosition:" + AnimFragment.this.curPosition);
                AnimFragment.this.ivForward.setImageResource(R.mipmap.anim_loop);
                AnimFragment.this.imageListAdapter.setSelectPosition(i);
                AnimFragment animFragment = AnimFragment.this;
                animFragment.updateItem(animFragment.curPosition);
                AnimFragment.this.curPosition = i;
                AnimFragment animFragment2 = AnimFragment.this;
                animFragment2.updateItem(animFragment2.curPosition);
                AnimFragment.this.sendAmin(i);
            }
        });
    }

    private void initDatas1664() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        this.namesList = arrayList;
        arrayList.add(Integer.valueOf(R.array.anim17));
        this.namesList.add(Integer.valueOf(R.array.anim16));
        this.namesList.add(Integer.valueOf(R.array.anim18));
        this.namesList.add(Integer.valueOf(R.array.anim19));
        this.namesList.add(Integer.valueOf(R.array.anim20));
        this.namesList.add(Integer.valueOf(R.array.anim21));
        this.namesList.add(Integer.valueOf(R.array.anim5));
        this.namesList.add(Integer.valueOf(R.array.anim1));
        this.namesList.add(Integer.valueOf(R.array.anim2));
        this.namesList.add(Integer.valueOf(R.array.anim3));
        this.namesList.add(Integer.valueOf(R.array.anim4));
        this.namesList.add(Integer.valueOf(R.array.anim0));
        this.namesList.add(Integer.valueOf(R.array.anim6));
        this.namesList.add(Integer.valueOf(R.array.anim7));
        this.namesList.add(Integer.valueOf(R.array.anim8));
        this.namesList.add(Integer.valueOf(R.array.anim9));
        this.namesList.add(Integer.valueOf(R.array.anim10));
        this.namesList.add(Integer.valueOf(R.array.anim11));
        this.namesList.add(Integer.valueOf(R.array.anim12));
        this.namesList.add(Integer.valueOf(R.array.anim13));
        this.namesList.add(Integer.valueOf(R.array.anim14));
        this.namesList.add(Integer.valueOf(R.array.anim22));
        AnimListAdapter animListAdapter = new AnimListAdapter(getActivity(), this.namesList);
        this.imageListAdapter = animListAdapter;
        this.lvImage.setAdapter((ListAdapter) animListAdapter);
        this.imageListAdapter.notifyDataSetChanged();
    }

    @Override // com.icwork.shiningglass.base.BaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    public void sendAmin(int i) {
        EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
        if (i >= 0) {
            this.bleManager.writeData(Agreement.getEncryptData(Agreement.getAnimCommand(i)));
        } else {
            this.bleManager.writeData(Agreement.getEncryptData(Agreement.getAnimLoopCommand()));
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (ClickFilter.filter()) {
            return;
        }
        int id = view.getId();
        if (id == R.id.iv_back) {
            SoundManager.getInstance().textBack();
            getActivity().finish();
        } else {
            if (id != R.id.iv_forward) {
                return;
            }
            SoundManager.getInstance().textDelete();
            this.curPosition = -1;
            this.ivForward.setImageResource(R.mipmap.anim_loop_select);
            this.imageListAdapter.setSelectPosition(-1);
            this.imageListAdapter.notifyDataSetChanged();
            sendAmin(-1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateItem(int i) {
        int firstVisiblePosition = this.lvImage.getFirstVisiblePosition();
        Log.e("BaseFragment", "updateItem: " + i);
        int lastVisiblePosition = this.lvImage.getLastVisiblePosition();
        if (i < firstVisiblePosition || i > lastVisiblePosition) {
            return;
        }
        this.imageListAdapter.getView(i, this.lvImage.getChildAt(i - firstVisiblePosition), this.lvImage);
    }
}
