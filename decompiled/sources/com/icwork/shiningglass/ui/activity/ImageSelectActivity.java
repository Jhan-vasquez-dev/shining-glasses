package com.icwork.shiningglass.ui.activity;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.cdbwsoft.library.ble.BleDevice;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.App;
import com.icwork.shiningglass.base.BaseActivity;
import com.icwork.shiningglass.base.app.C;
import com.icwork.shiningglass.dao.DaoSession;
import com.icwork.shiningglass.dao.DiyDataDao;
import com.icwork.shiningglass.model.bean.DiyData;
import com.icwork.shiningglass.model.data.DiyMutiAgreement;
import com.icwork.shiningglass.ui.adapter.DiyImageSelectListAdapter;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.utils.ToastUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes.dex */
public class ImageSelectActivity extends BaseActivity {
    private int curIndex;
    private DaoSession daoSession;
    private DiyDataDao diyDataDao;
    private DiyImageSelectListAdapter diyImageListAdapter;
    ImageView ivAnim;
    ImageView ivGallery;
    ListView lvImage;
    private List<DiyData> diyDataList = new ArrayList();
    Handler handler = new Handler();
    Runnable runnable = new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ImageSelectActivity.2
        @Override // java.lang.Runnable
        public void run() {
            ImageSelectActivity.this.dismissProgressDialog();
            if (DiyMutiAgreement.getInstance() != null) {
                DiyMutiAgreement.getInstance().clear();
            }
        }
    };

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void bindListener() {
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected int getLayoutId() {
        return R.layout.activity_image_select;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initView() {
        this.lvImage = (ListView) findViewById(R.id.lv_image);
        this.ivAnim = (ImageView) findViewById(R.id.iv_anim);
        this.ivGallery = (ImageView) findViewById(R.id.iv_gallery);
        this.ivAnim.setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.ImageSelectActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$initView$0(view);
            }
        });
        this.ivGallery.setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.ImageSelectActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$initView$1(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initView$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initView$1(View view) {
        List<DiyData> selectDiyData = getSelectDiyData();
        if (selectDiyData == null || selectDiyData.size() == 0) {
            return;
        }
        sendDiyData(selectDiyData);
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initData() {
        LogUtil.d("initData ");
        DaoSession daoSession = App.getDaoSession();
        this.daoSession = daoSession;
        DiyDataDao diyDataDao = daoSession.getDiyDataDao();
        this.diyDataDao = diyDataDao;
        this.diyDataList = diyDataDao.queryBuilder().list();
        for (int i = 0; i < this.diyDataList.size(); i++) {
            this.diyDataList.get(i).setIndex(0);
            LogUtil.d("排序后的结果3：" + this.diyDataList.get(i).toString());
        }
        DiyImageSelectListAdapter diyImageSelectListAdapter = new DiyImageSelectListAdapter(this.mContext, this.diyDataList);
        this.diyImageListAdapter = diyImageSelectListAdapter;
        this.lvImage.setAdapter((ListAdapter) diyImageSelectListAdapter);
        this.lvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.icwork.shiningglass.ui.activity.ImageSelectActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                DiyData diyData = (DiyData) ImageSelectActivity.this.diyDataList.get(i2);
                if (diyData.getIndex() > 0) {
                    diyData.setIndex(0);
                    for (int i3 = 0; i3 < ImageSelectActivity.this.diyDataList.size(); i3++) {
                        LogUtil.d("diyData:" + ((DiyData) ImageSelectActivity.this.diyDataList.get(i3)).toString());
                    }
                    ImageSelectActivity imageSelectActivity = ImageSelectActivity.this;
                    imageSelectActivity.diyDataSort(imageSelectActivity.diyDataList);
                } else {
                    if (ImageSelectActivity.this.curIndex == 20) {
                        ToastUtil.showToast(ImageSelectActivity.this.getString(R.string.max_image_select));
                        return;
                    }
                    ImageSelectActivity imageSelectActivity2 = ImageSelectActivity.this;
                    int i4 = imageSelectActivity2.curIndex + 1;
                    imageSelectActivity2.curIndex = i4;
                    diyData.setIndex(i4);
                }
                ImageSelectActivity.this.diyImageListAdapter.notifyDataSetChanged();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showProgress() {
        this.handler.removeCallbacks(this.runnable);
        this.handler.postDelayed(this.runnable, 10000L);
    }

    private void sendDiyData(final List<DiyData> list) {
        List<BleDevice> deviceList = App.getAppData().getDeviceList();
        EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
        DiyMutiAgreement.DiyAgreementListener diyAgreementListener = new DiyMutiAgreement.DiyAgreementListener() { // from class: com.icwork.shiningglass.ui.activity.ImageSelectActivity.3
            @Override // com.icwork.shiningglass.model.data.DiyMutiAgreement.DiyAgreementListener
            public void onStartSendCommand(BleDevice bleDevice) {
                LogUtil.d("开始发送数据");
                ImageSelectActivity.this.showProgress();
            }

            @Override // com.icwork.shiningglass.model.data.DiyMutiAgreement.DiyAgreementListener
            public void onFinishSend(BleDevice bleDevice) {
                LogUtil.d("=====diy数据发送完成");
                ImageSelectActivity.this.handler.removeCallbacks(ImageSelectActivity.this.runnable);
                ImageSelectActivity.this.dismissProgressDialog();
                ToastUtil.showToast(ImageSelectActivity.this.getString(R.string.send_successfully));
            }

            @Override // com.icwork.shiningglass.model.data.DiyMutiAgreement.DiyAgreementListener
            public void onManyOk(BleDevice bleDevice) {
                LogUtil.d("=====onManyOk");
                ImageSelectActivity.this.showProgress();
                DiyMutiAgreement.getInstance().sendultiDiy(bleDevice, list);
            }
        };
        if (deviceList == null || deviceList.size() <= 0) {
            return;
        }
        DiyMutiAgreement diyMutiAgreement = DiyMutiAgreement.getInstance();
        showProgress();
        showProgressDialog1(this.mContext, getString(R.string.send));
        for (int i = 0; i < deviceList.size(); i++) {
            diyMutiAgreement.sendMutiImage(deviceList.get(i), list, diyAgreementListener);
        }
    }

    private List<DiyData> getSelectDiyData() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.diyDataList.size(); i++) {
            DiyData diyData = this.diyDataList.get(i);
            if (diyData.getIndex() > 0) {
                arrayList.add(diyData);
            }
        }
        Collections.sort(arrayList, new Comparator<DiyData>() { // from class: com.icwork.shiningglass.ui.activity.ImageSelectActivity.4
            @Override // java.util.Comparator
            public int compare(DiyData diyData2, DiyData diyData3) {
                if (diyData2.getIndex() > diyData3.getIndex()) {
                    return 1;
                }
                return diyData2.getDiyId() == diyData3.getDiyId() ? 0 : -1;
            }
        });
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            LogUtil.d("排序后的结果6：" + ((DiyData) arrayList.get(i2)).toString());
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void diyDataSort(List<DiyData> list) {
        Collections.sort(list, new Comparator<DiyData>() { // from class: com.icwork.shiningglass.ui.activity.ImageSelectActivity.5
            @Override // java.util.Comparator
            public int compare(DiyData diyData, DiyData diyData2) {
                if (diyData.getIndex() > diyData2.getIndex()) {
                    return 1;
                }
                return diyData.getIndex() == diyData2.getIndex() ? 0 : -1;
            }
        });
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (list.get(i2).getIndex() != 0) {
                i++;
                list.get(i2).setIndex(i);
            }
        }
        this.curIndex = i;
        Collections.sort(list, new Comparator<DiyData>() { // from class: com.icwork.shiningglass.ui.activity.ImageSelectActivity.6
            @Override // java.util.Comparator
            public int compare(DiyData diyData, DiyData diyData2) {
                if (diyData.getDiyId().longValue() > diyData2.getDiyId().longValue()) {
                    return 1;
                }
                return diyData.getDiyId() == diyData2.getDiyId() ? 0 : -1;
            }
        });
    }

    @Override // com.icwork.shiningglass.base.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < this.diyDataList.size(); i++) {
            this.diyDataList.get(i).setIndex(0);
            LogUtil.d("排序后的结果4：" + this.diyDataList.get(i).toString());
        }
        if (DiyMutiAgreement.getInstance() != null) {
            DiyMutiAgreement.getInstance().clear();
        }
    }
}
