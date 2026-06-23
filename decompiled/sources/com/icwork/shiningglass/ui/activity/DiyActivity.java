package com.icwork.shiningglass.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cdbwsoft.library.ble.BleDevice;
import com.cdbwsoft.library.ble.BleManager;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.App;
import com.icwork.shiningglass.base.BaseActivity;
import com.icwork.shiningglass.base.app.C;
import com.icwork.shiningglass.base.app.SoundManager;
import com.icwork.shiningglass.base.music.MusicPlayer;
import com.icwork.shiningglass.ble.HeartBeatDevice;
import com.icwork.shiningglass.ble.HeartBeatDeviceFactory;
import com.icwork.shiningglass.model.bean.DiyData;
import com.icwork.shiningglass.model.data.Agreement;
import com.icwork.shiningglass.model.data.DiyAgreement;
import com.icwork.shiningglass.ui.utils.ByteUtils;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.utils.ToastUtil;
import com.icwork.shiningglass.ui.widget.LedViewDiy;
import com.icwork.shiningglass.ui.widget.RectView;
import com.icwork.shiningglass.ui.widget.colorpickerview.ColorPickerView;
import com.icwork.shiningglass.ui.widget.colorpickerview.OnColorChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes.dex */
public class DiyActivity extends BaseActivity implements LedViewDiy.LedListener, View.OnClickListener {
    private BleManager bleManager;
    ColorPickerView colorPickerView;
    private ArrayList<Integer> diyColor;
    byte[] diyData;
    private long diyDataId;
    private int flag;
    ImageView imgvClear;
    ImageView imgvDelete;
    ImageView imgvPaint;
    ImageView ivBack;
    ImageView ivForward;
    ImageView ivForward1;
    ImageView ivPen;
    private ImageView ivPen2;
    private ImageView ivPen3;
    ImageView ivTitle;
    LinearLayout llDiy;
    RelativeLayout lnrlaoBottomEdit;
    LedViewDiy lvEdit;
    private int mHeight;
    private int mWidth;
    private MusicPlayer musicPlayer;
    RelativeLayout rl_top;
    RectView rvColor;
    TextView tvTitle;

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected int getLayoutId() {
        return R.layout.activity_diy;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initView() {
        this.ivBack = (ImageView) findViewById(R.id.iv_back);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.ivForward = (ImageView) findViewById(R.id.iv_forward);
        this.ivForward1 = (ImageView) findViewById(R.id.iv_forward_1);
        this.ivTitle = (ImageView) findViewById(R.id.iv_title);
        this.lvEdit = (LedViewDiy) findViewById(R.id.lv_edit);
        this.imgvClear = (ImageView) findViewById(R.id.imgv_clear);
        this.imgvPaint = (ImageView) findViewById(R.id.imgv_paint);
        this.imgvDelete = (ImageView) findViewById(R.id.imgv_delete);
        this.ivPen = (ImageView) findViewById(R.id.iv_pen);
        this.llDiy = (LinearLayout) findViewById(R.id.ll_diy);
        this.lnrlaoBottomEdit = (RelativeLayout) findViewById(R.id.lnrlao_bottom_edit);
        this.rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        this.colorPickerView = (ColorPickerView) findViewById(R.id.cpv_color_preview);
        this.rvColor = (RectView) findViewById(R.id.rv_color);
        this.ivPen2 = (ImageView) findViewById(R.id.iv_pen2);
        this.ivPen3 = (ImageView) findViewById(R.id.iv_pen3);
        this.ivBack.setOnClickListener(this);
        this.ivForward.setOnClickListener(this);
        this.imgvPaint.setOnClickListener(this);
        this.imgvDelete.setOnClickListener(this);
        this.ivPen.setOnClickListener(this);
        this.imgvClear.setOnClickListener(this);
        this.ivPen2.setOnClickListener(this);
        this.ivPen3.setOnClickListener(this);
        this.ivForward1.setOnClickListener(this);
        this.ivBack.setImageResource(R.mipmap.text_magic_back);
        this.ivBack.setVisibility(0);
        this.ivForward.setImageResource(R.drawable.diy_save);
        this.ivForward.setVisibility(0);
        this.tvTitle.setVisibility(8);
        this.ivTitle.setVisibility(0);
        this.ivForward1.setVisibility(0);
        this.lvEdit.setLayerType(1, null);
        this.lvEdit.setLedListener(this);
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initData() {
        BleManager bleManager = ConnectActivity.getBleManager();
        this.bleManager = bleManager;
        if (bleManager == null) {
            this.bleManager = App.getInstance().getBleManager(HeartBeatDevice.class, null, new HeartBeatDeviceFactory(App.getInstance()));
        }
        initLedView();
        this.diyData = getIntent().getByteArrayExtra("diyData");
        this.flag = getIntent().getIntExtra("flag", 0);
        this.diyColor = getIntent().getIntegerArrayListExtra("diyColor");
        this.diyDataId = getIntent().getLongExtra("diyDataId", 0L);
        MusicPlayer musicPlayer = ConnectActivity.getMusicPlayer();
        this.musicPlayer = musicPlayer;
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
        }
        LogUtil.d("设置diy数据" + this.diyColor + " diydata:" + this.diyData);
        if (this.diyColor != null) {
            initDiy1();
        } else {
            initDiy();
        }
        this.colorPickerView.post(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.DiyActivity.1
            @Override // java.lang.Runnable
            public void run() {
                DiyActivity.this.colorPickerView.setPreviewColor();
                DiyActivity.this.colorPickerView.setColorProgress(-1.0f);
            }
        });
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void bindListener() {
        this.colorPickerView.setOnColorChangeListener(new OnColorChangeListener() { // from class: com.icwork.shiningglass.ui.activity.DiyActivity.2
            @Override // com.icwork.shiningglass.ui.widget.colorpickerview.OnColorChangeListener
            public void colorChanged(int i, int i2, int i3, int i4, float f) {
                LogUtil.d("颜色：" + i);
                DiyActivity.this.rvColor.setViewBackground(i);
                DiyActivity.this.lvEdit.setPaintColor(i);
            }
        });
        this.colorPickerView.setColorProgress(-1.0f);
    }

    private void initLedView() {
        this.llDiy.post(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.DiyActivity.3
            @Override // java.lang.Runnable
            public void run() {
                DiyActivity diyActivity = DiyActivity.this;
                diyActivity.mWidth = diyActivity.llDiy.getWidth();
                DiyActivity diyActivity2 = DiyActivity.this;
                diyActivity2.mHeight = diyActivity2.llDiy.getHeight();
                LogUtil.d("控件的宽度：" + DiyActivity.this.mWidth + "高度：" + DiyActivity.this.mHeight + " 时间：" + System.currentTimeMillis());
                DiyActivity.this.initLedView1();
                if (DiyActivity.this.diyColor != null) {
                    DiyActivity.this.lvEdit.setData(DiyActivity.this.diyColor, DiyActivity.this.diyData);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initLedView1() {
        this.ivPen.setImageResource(R.mipmap.diy_pen_selected);
        this.lvEdit.setMode(1);
        this.lvEdit.setPointMargin(1);
        this.lvEdit.removeAllViews();
        ViewGroup.LayoutParams layoutParams = this.lvEdit.getLayoutParams();
        int i = this.mHeight;
        int i2 = i / 12;
        float f = i;
        this.lvEdit.removeAllViews();
        this.lvEdit.init(36, 12, 0.0f);
        layoutParams.height = (int) f;
        int i3 = i2 * 36;
        int i4 = this.mWidth;
        if (i3 > i4) {
            layoutParams.width = i4;
            layoutParams.height = (int) ((i4 / 36.0f) * 12.0f);
        } else {
            layoutParams.width = i3;
        }
        this.lvEdit.setLayoutParams(layoutParams);
        LogUtil.d("====layoutParams.itemViewWidth :" + layoutParams.width + " viewHeight:" + f + "  view width:" + this.mWidth + " lewview height：" + this.lvEdit.getHeight());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_clear /* 2131296389 */:
                SoundManager.getInstance().diyClear();
                this.imgvClear.setImageResource(R.mipmap.diy_clear_selected);
                this.ivPen.setImageResource(R.mipmap.diy_pen);
                this.lvEdit.setMode(2);
                break;
            case R.id.imgv_delete /* 2131296391 */:
                SoundManager.getInstance().diyDeleteAll();
                this.lvEdit.clearSelected();
                clearDiyData();
                break;
            case R.id.iv_back /* 2131296426 */:
                SoundManager.getInstance().textBack();
                setResult(1001);
                LogUtil.d("flag:" + this.flag);
                if (this.flag == 1) {
                    exitDiy(Agreement.getExitDiyCommand());
                } else {
                    exitDiy(getExitCommand());
                }
                finish();
                break;
            case R.id.iv_forward /* 2131296441 */:
                SoundManager.getInstance().diySave();
                if (ledViewIsHasValue(this.lvEdit.getData())) {
                    if (this.diyData == null) {
                        showSaveDialog();
                    } else {
                        showListDialog();
                    }
                } else {
                    ToastUtil.showToast(getString(R.string.not_data));
                }
                break;
            case R.id.iv_forward_1 /* 2131296442 */:
                SoundManager.getInstance().diyToImage();
                Intent intent = new Intent(this.mContext, (Class<?>) ImageActivity.class);
                intent.putExtra("flag", 1);
                startActivity(intent);
                finish();
                break;
            case R.id.iv_pen /* 2131296453 */:
                SoundManager.getInstance().diyPen();
                this.imgvClear.setImageResource(R.mipmap.diy_clear);
                this.ivPen.setImageResource(R.mipmap.diy_pen_selected);
                this.lvEdit.setMode(1);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveImage() {
        byte[] data = this.lvEdit.getData();
        ArrayList<Integer> dataColor = this.lvEdit.getDataColor();
        if (dataColor != null) {
            LogUtil.d("获取diy图片的色值大小：" + dataColor.size());
            for (int i = 0; i < dataColor.size(); i++) {
                LogUtil.d("获取diy图片的色值：" + dataColor.get(i));
            }
        }
        if (ledViewIsHasValue(data)) {
            DiyData diyData = new DiyData(data, dataColor);
            LogUtil.d("保存diy数据：" + diyData.toString());
            App.getDaoSession().getDiyDataDao().insert(diyData);
            Toast.makeText(this, getString(R.string.save_sucess), 0).show();
            setResult(1001);
            EventBus.getDefault().post(C.MAIN_EVENT.UPDATE_DIY_LIST);
            exitDiy(getExitCommand());
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateData() {
        byte[] data = this.lvEdit.getData();
        if (ledViewIsHasValue(data)) {
            DiyData diyData = new DiyData(data, this.lvEdit.getDataColor());
            diyData.setDiyId(Long.valueOf(this.diyDataId));
            LogUtil.d("保存diy数据：" + diyData.toString());
            App.getDaoSession().getDiyDataDao().update(diyData);
            Toast.makeText(this, getString(R.string.save_sucess), 0).show();
            setResult(1001);
            exitDiy(getExitCommand());
            finish();
        }
    }

    private boolean ledViewIsHasValue(byte[] bArr) {
        for (byte b : bArr) {
            if (b > 0) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        LogUtil.d("====1001");
        setResult(1001);
    }

    private void initDiy() {
        this.bleManager.writeData(Agreement.getEncryptData(Agreement.getEnterDiyCommand()));
    }

    private void initDiy1() {
        this.bleManager.writeData(Agreement.getEncryptData(Agreement.getEnterDiyCommand1()));
    }

    @Override // com.icwork.shiningglass.ui.widget.LedViewDiy.LedListener
    public void onItemSelect(int i, int i2, int i3, boolean z) {
        LogUtil.d("======rowNumber==" + i3 + " columnNumber:" + i2);
        this.lvEdit.getRealTime(i3, i2, new LedViewDiy.RealTimeDataListener() { // from class: com.icwork.shiningglass.ui.activity.DiyActivity.4
            @Override // com.icwork.shiningglass.ui.widget.LedViewDiy.RealTimeDataListener
            public void onRealTimeData(int i4, byte[] bArr) {
                BleDevice bleDevice;
                List<BleDevice> deviceList = App.getAppData().getDeviceList();
                if (deviceList == null || deviceList.size() <= 0) {
                    return;
                }
                byte[] bArr2 = new byte[bArr.length];
                System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
                LogUtil.d("===发送diy数据：" + ByteUtils.binaryToHexString(bArr2));
                for (int i5 = 0; i5 < deviceList.size() && (bleDevice = deviceList.get(i5)) != null; i5++) {
                    bleDevice.writeCharacteristicBy3(bArr2);
                }
            }
        });
    }

    private void clearDiyData() {
        List<BleDevice> deviceList = App.getAppData().getDeviceList();
        if (deviceList == null || deviceList.size() <= 0) {
            return;
        }
        this.bleManager.writeData(Agreement.getEncryptData(Agreement.getEnterDiyCommand()));
    }

    private void exitDiy(byte[] bArr) {
        List<BleDevice> deviceList = App.getAppData().getDeviceList();
        if (deviceList == null || deviceList.size() <= 0) {
            return;
        }
        this.bleManager.writeData(Agreement.getEncryptData(bArr));
    }

    private byte[] getExitCommand() {
        if (ledViewIsHasValue(this.lvEdit.getData())) {
            return Agreement.getExitDiySaveCommand();
        }
        return Agreement.getExitDiyCommand();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (this.flag == 1) {
                exitDiy(Agreement.getExitDiyCommand());
            } else {
                exitDiy(getExitCommand());
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void showSaveDialog() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.reminder)).setMessage(getString(R.string.reminder_save)).setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.DiyActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(getString(R.string.btn_sure), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.DiyActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                DiyActivity.this.saveImage();
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public void showListDialog() {
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).create();
        alertDialogCreate.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialogCreate.show();
        alertDialogCreate.setCanceledOnTouchOutside(true);
        alertDialogCreate.getWindow().setContentView(R.layout.changestate_dialog);
        TextView textView = (TextView) alertDialogCreate.getWindow().findViewById(R.id.tv_cancel);
        TextView textView2 = (TextView) alertDialogCreate.getWindow().findViewById(R.id.tv_new_file);
        TextView textView3 = (TextView) alertDialogCreate.getWindow().findViewById(R.id.tv_ok);
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.DiyActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                alertDialogCreate.dismiss();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.DiyActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                alertDialogCreate.dismiss();
                DiyActivity.this.saveImage();
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.DiyActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                alertDialogCreate.dismiss();
                DiyActivity.this.updateData();
            }
        });
    }

    @Override // com.icwork.shiningglass.base.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (DiyAgreement.getInstance() != null) {
            DiyAgreement.getInstance().clear();
        }
    }
}
