package com.icwork.shiningglass.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.cdbwsoft.library.ble.BleDevice;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.App;
import com.icwork.shiningglass.base.BaseFragment;
import com.icwork.shiningglass.base.app.C;
import com.icwork.shiningglass.base.app.SoundManager;
import com.icwork.shiningglass.dao.DaoSession;
import com.icwork.shiningglass.dao.DiyDataDao;
import com.icwork.shiningglass.model.bean.DiyData;
import com.icwork.shiningglass.model.data.DiyAgreement;
import com.icwork.shiningglass.ui.activity.DiyActivity;
import com.icwork.shiningglass.ui.adapter.DiyImageListAdapter;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.utils.PopupWindowUtil;
import com.icwork.shiningglass.ui.utils.ScreenUtils;
import com.icwork.shiningglass.ui.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* JADX INFO: loaded from: classes.dex */
public class DiyImageFragment extends BaseFragment {
    private DaoSession daoSession;
    private DiyDataDao diyDataDao;
    private List<DiyData> diyDataList = new ArrayList();
    private DiyImageListAdapter diyImageListAdapter;
    ListView lvImage;
    private PopupWindow mPopupWindow;

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected int getLayoutId() {
        return R.layout.fragment_deuault_fragment;
    }

    public static DiyImageFragment newInstance() {
        LogUtil.d("DiyImageFragment ");
        return new DiyImageFragment();
    }

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected void initView(View view, Bundle bundle) {
        this.lvImage = (ListView) view.findViewById(R.id.lv_image);
    }

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        LogUtil.d("initData ");
        DaoSession daoSession = App.getDaoSession();
        this.daoSession = daoSession;
        DiyDataDao diyDataDao = daoSession.getDiyDataDao();
        this.diyDataDao = diyDataDao;
        this.diyDataList = diyDataDao.queryBuilder().list();
        DiyImageListAdapter diyImageListAdapter = new DiyImageListAdapter(getActivity(), this.diyDataList);
        this.diyImageListAdapter = diyImageListAdapter;
        this.lvImage.setAdapter((ListAdapter) diyImageListAdapter);
        this.lvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.icwork.shiningglass.ui.fragment.DiyImageFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (i == DiyImageFragment.this.diyImageListAdapter.getSelectPosition()) {
                    DiyImageFragment.this.showPopupWindow(view, i);
                } else {
                    DiyImageFragment.this.sendDiyData((DiyData) DiyImageFragment.this.diyDataList.get(i));
                }
                SoundManager.getInstance().animSelect();
                DiyImageFragment.this.diyImageListAdapter.setSelectPosition(i);
                DiyImageFragment.this.diyImageListAdapter.notifyDataSetChanged();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPopupWindow(View view, int i) {
        View popupWindowContentView = getPopupWindowContentView(i);
        PopupWindow popupWindow = new PopupWindow(popupWindowContentView, -2, -2, true);
        this.mPopupWindow = popupWindow;
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        int[] iArrCalculatePopWindowPos = PopupWindowUtil.calculatePopWindowPos(view, popupWindowContentView);
        this.mPopupWindow.showAtLocation(view, 8388659, iArrCalculatePopWindowPos[0] / 2, iArrCalculatePopWindowPos[1] - ScreenUtils.dp2px(this.mContext, 8.5f));
    }

    private View getPopupWindowContentView(final int i) {
        View viewInflate = LayoutInflater.from(getActivity()).inflate(R.layout.popup_content_layout, (ViewGroup) null);
        viewInflate.findViewById(R.id.iv_edit).setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.fragment.DiyImageFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DiyImageFragment.this.mPopupWindow != null) {
                    DiyImageFragment.this.mPopupWindow.dismiss();
                    Intent intent = new Intent(DiyImageFragment.this.mActivity, (Class<?>) DiyActivity.class);
                    intent.putExtra("flag", 1);
                    intent.putExtra("diyData", ((DiyData) DiyImageFragment.this.diyDataList.get(i)).getData());
                    intent.putExtra("diyDataId", ((DiyData) DiyImageFragment.this.diyDataList.get(i)).getDiyId());
                    intent.putExtra("diyColor", ((DiyData) DiyImageFragment.this.diyDataList.get(i)).getColorArray());
                    DiyImageFragment.this.startActivityForResult(intent, 1);
                }
            }
        });
        viewInflate.findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.fragment.DiyImageFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DiyImageFragment.this.mPopupWindow != null) {
                    DiyImageFragment.this.mPopupWindow.dismiss();
                    DiyImageFragment.this.daoSession.getDiyDataDao().delete((DiyData) DiyImageFragment.this.diyDataList.get(i));
                    DiyImageFragment.this.diyDataList.remove(i);
                    DiyImageFragment.this.diyImageListAdapter.setList(DiyImageFragment.this.diyDataList);
                    DiyImageFragment.this.diyImageListAdapter.notifyDataSetChanged();
                    Toast.makeText(DiyImageFragment.this.getActivity(), DiyImageFragment.this.getResources().getString(R.string.delete_succe), 0).show();
                }
            }
        });
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 1001) {
            updateAdapter();
        }
    }

    private void updateAdapter() {
        this.diyDataList = this.diyDataDao.queryBuilder().list();
        DiyImageListAdapter diyImageListAdapter = new DiyImageListAdapter(getActivity(), this.diyDataList);
        this.diyImageListAdapter = diyImageListAdapter;
        this.lvImage.setAdapter((ListAdapter) diyImageListAdapter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDiyData(DiyData diyData) {
        EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
        DiyAgreement.DiyAgreementListener diyAgreementListener = new DiyAgreement.DiyAgreementListener() { // from class: com.icwork.shiningglass.ui.fragment.DiyImageFragment.4
            @Override // com.icwork.shiningglass.model.data.DiyAgreement.DiyAgreementListener
            public void onFinishSend(BleDevice bleDevice) {
                LogUtil.d("=====diy数据发送完成");
                DiyImageFragment.this.dismissProgressDialog();
                ToastUtil.showToast(DiyImageFragment.this.getString(R.string.send_successfully));
            }
        };
        List<BleDevice> deviceList = App.getAppData().getDeviceList();
        if (deviceList == null || deviceList.size() <= 0) {
            return;
        }
        DiyAgreement diyAgreement = DiyAgreement.getInstance();
        showProgressDialog(this.mContext, getString(R.string.send));
        for (int i = 0; i < deviceList.size(); i++) {
            diyAgreement.sendDiy(deviceList.get(i), diyData, diyAgreementListener);
        }
    }

    @Override // com.icwork.shiningglass.base.BaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        if (DiyAgreement.getInstance() != null) {
            DiyAgreement.getInstance().clear();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void updateData(String str) {
        if (str.equals(C.MAIN_EVENT.UPDATE_DIY_LIST)) {
            LogUtil.d("刷新diy图片列表");
            updateAdapter();
        }
    }
}
