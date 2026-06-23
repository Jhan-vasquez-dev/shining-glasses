package com.icwork.shiningglass.ui.activity;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cdbwsoft.library.ble.BleManager;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.BaseActivity;
import com.icwork.shiningglass.base.DataManager;
import com.icwork.shiningglass.base.app.SoundManager;
import com.icwork.shiningglass.base.music.VisualizerUtil;
import com.icwork.shiningglass.model.data.Agreement;
import com.icwork.shiningglass.sevice.MikeListenter;
import com.icwork.shiningglass.sevice.MikeSevice;
import com.icwork.shiningglass.ui.utils.ByteUtils;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.widget.RhythmLedView;
import com.icwork.shiningglass.ui.widget.image3d.RhyImage3DSwitchView;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

/* JADX INFO: loaded from: classes.dex */
public class MicrophoneActivity extends BaseActivity {
    private static BlockingQueue<byte[]> queue;
    private BleManager bleManager;
    private DataManager dataManager;
    RhyImage3DSwitchView imageSwitchView;
    ImageView ivBack;
    ImageView ivForward;
    private MikeListenter mikeListenter;
    private MikeSevice mikeSevice;
    RhythmLedView rhythmLedView;
    RelativeLayout rlRhySelect;
    RelativeLayout rlRhyShow;
    TextView tvTitle;
    private int curSelectMode = 0;
    private Handler handler = new Handler();
    Runnable runnable = new Runnable() { // from class: com.icwork.shiningglass.ui.activity.MicrophoneActivity.4
        @Override // java.lang.Runnable
        public void run() {
            MicrophoneActivity.this.rlRhyShow.setVisibility(0);
        }
    };

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected int getLayoutId() {
        return R.layout.activity_microphone;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initView() {
        this.ivBack = (ImageView) findViewById(R.id.iv_back);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.ivForward = (ImageView) findViewById(R.id.iv_forward);
        this.imageSwitchView = (RhyImage3DSwitchView) findViewById(R.id.image_switch_view);
        this.rlRhyShow = (RelativeLayout) findViewById(R.id.rl_rhy_show);
        this.rlRhySelect = (RelativeLayout) findViewById(R.id.rl_rhy_select);
        this.rhythmLedView = (RhythmLedView) findViewById(R.id.rhyledview_1);
        this.ivBack.setImageResource(R.mipmap.text_magic_back);
        this.ivBack.setVisibility(0);
        this.ivForward.setImageResource(R.mipmap.rhy_microphone);
        this.ivForward.setVisibility(8);
        this.mikeListenter = new MikePhoneListenter();
        MikeSevice mikeSevice = ConnectActivity.getMikeSevice();
        this.mikeSevice = mikeSevice;
        mikeSevice.registerMusicListenter(this.mikeListenter);
        DataManager.getInstance().setMicrophoneEnable(true);
        this.ivBack.setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.MicrophoneActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$initView$0(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initView$0(View view) {
        SoundManager.getInstance().textBack();
        DataManager.getInstance().setMicrophoneEnable(false);
        finish();
    }

    class MikePhoneListenter extends MikeListenter {
        MikePhoneListenter() {
        }

        @Override // com.icwork.shiningglass.sevice.MikeListenter
        public void onStart(short[] sArr) {
            if (DataManager.getInstance().isMicrophoneEnable()) {
                byte[] waveFormData = VisualizerUtil.getWaveFormData(sArr);
                LogUtil.d("麦克风律动：" + ByteUtils.binaryToHexString(waveFormData));
                MicrophoneActivity.this.setRhyData(waveFormData);
                MicrophoneActivity microphoneActivity = MicrophoneActivity.this;
                microphoneActivity.sendRhythmData((byte) microphoneActivity.curSelectMode, waveFormData);
            }
        }
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initData() {
        this.bleManager = ConnectActivity.getBleManager();
        this.dataManager = DataManager.getInstance();
        this.curSelectMode = DataManager.getInstance().getCurSelectRhythmMode();
        this.rhythmLedView.setLayerType(1, null);
        this.rhythmLedView.setMode(0);
        this.rhythmLedView.setPointMargin(0);
        this.rhythmLedView.removeAllViews();
        this.rhythmLedView.init(36, 12);
        this.imageSwitchView.setCurrentImage(DataManager.getInstance().getCurSelectRhythmMode1());
        queue = new LinkedTransferQueue();
        new Thread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.MicrophoneActivity.1
            @Override // java.lang.Runnable
            public void run() {
                while (true) {
                    try {
                        MicrophoneActivity.this.bleManager.writeDataBy4((byte[]) MicrophoneActivity.queue.take());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void bindListener() {
        this.imageSwitchView.setOnImageItemClickListener(new RhyImage3DSwitchView.OnImageItemClickListener() { // from class: com.icwork.shiningglass.ui.activity.MicrophoneActivity.2
            @Override // com.icwork.shiningglass.ui.widget.image3d.RhyImage3DSwitchView.OnImageItemClickListener
            public void onClick(int i) {
            }

            @Override // com.icwork.shiningglass.ui.widget.image3d.RhyImage3DSwitchView.OnImageItemClickListener
            public void onTouch() {
                MicrophoneActivity.this.rhyUIShow(false);
            }

            @Override // com.icwork.shiningglass.ui.widget.image3d.RhyImage3DSwitchView.OnImageItemClickListener
            public void onTouchStop() {
                MicrophoneActivity.this.rhyUIShow(true);
            }
        });
        this.imageSwitchView.setOnImageSwitchListener(new RhyImage3DSwitchView.OnImageSwitchListener() { // from class: com.icwork.shiningglass.ui.activity.MicrophoneActivity.3
            @Override // com.icwork.shiningglass.ui.widget.image3d.RhyImage3DSwitchView.OnImageSwitchListener
            public void onImageSwitch(int i) {
                LogUtil.d("currentImage:" + i);
                if (DataManager.getInstance().isMicrophoneEnable()) {
                    MicrophoneActivity.this.rhyUIShow(true);
                } else {
                    MicrophoneActivity.this.rhyUIShow(false);
                }
                MicrophoneActivity.this.dataManager.setCurSelectRhythmMode1(i);
                switch (i) {
                    case 0:
                    case 5:
                        MicrophoneActivity.this.selectRhythmMode(1);
                        break;
                    case 1:
                    case 6:
                        MicrophoneActivity.this.selectRhythmMode(2);
                        break;
                    case 2:
                    case 7:
                        MicrophoneActivity.this.selectRhythmMode(3);
                        break;
                    case 3:
                    case 8:
                        MicrophoneActivity.this.selectRhythmMode(4);
                        break;
                    case 4:
                    case 9:
                        MicrophoneActivity.this.selectRhythmMode(5);
                        break;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRhyData(byte[] bArr) {
        int i = this.curSelectMode;
        if (i == 0) {
            this.rhythmLedView.setRhyData1(bArr);
            return;
        }
        if (i == 1) {
            this.rhythmLedView.setRhyData2(bArr);
            return;
        }
        if (i == 2) {
            this.rhythmLedView.setRhyData3(bArr);
            return;
        }
        if (i == 3) {
            this.rhythmLedView.setRhyData4(bArr);
        } else if (i == 4) {
            this.rhythmLedView.setRhyData5(bArr);
        } else {
            this.rhythmLedView.setRhyData1(bArr);
        }
    }

    @Override // com.icwork.shiningglass.base.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        MikeSevice mikeSevice = this.mikeSevice;
        if (mikeSevice != null) {
            mikeSevice.unregisterMusicListenter(this.mikeListenter);
        }
        this.handler.removeCallbacks(this.runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectRhythmMode(int i) {
        if (i == 1) {
            this.curSelectMode = 0;
            this.dataManager.setCurSelectRhythmMode(0);
            return;
        }
        if (i == 2) {
            this.curSelectMode = 1;
            this.dataManager.setCurSelectRhythmMode(1);
            return;
        }
        if (i == 3) {
            this.curSelectMode = 2;
            this.dataManager.setCurSelectRhythmMode(2);
        } else if (i == 4) {
            this.curSelectMode = 3;
            this.dataManager.setCurSelectRhythmMode(3);
        } else {
            if (i != 5) {
                return;
            }
            this.curSelectMode = 4;
            this.dataManager.setCurSelectRhythmMode(4);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        DataManager.getInstance().setMicrophoneEnable(false);
        super.onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rhyUIShow(boolean z) {
        LogUtil.d("显示律动UI：" + z);
        if (z) {
            this.handler.removeCallbacks(this.runnable);
            this.handler.postDelayed(this.runnable, 550L);
        } else {
            this.rlRhyShow.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendRhythmData(byte b, byte[] bArr) {
        byte[] bArr2 = new byte[16];
        bArr2[0] = 15;
        bArr2[1] = b;
        System.arraycopy(bArr, 0, bArr2, 2, 12);
        try {
            queue.put(Agreement.getEncryptData(bArr2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
