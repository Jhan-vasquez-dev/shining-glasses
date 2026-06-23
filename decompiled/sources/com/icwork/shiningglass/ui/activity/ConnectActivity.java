package com.icwork.shiningglass.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.exifinterface.media.ExifInterface;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.ble.BleDevice;
import com.cdbwsoft.library.ble.BleListener;
import com.cdbwsoft.library.ble.BleManager;
import com.cdbwsoft.library.panchip.FileInfo;
import com.cdbwsoft.library.panchip.OtaLisenter;
import com.cdbwsoft.library.panchip.PanchipOtaManager;
import com.cdbwsoft.library.panchip.VersionInfo;
import com.icwork.shiningglass.BuildConfig;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.App;
import com.icwork.shiningglass.base.AppManager;
import com.icwork.shiningglass.base.BaseActivity;
import com.icwork.shiningglass.base.DataManager;
import com.icwork.shiningglass.base.app.BleConfig;
import com.icwork.shiningglass.base.app.C;
import com.icwork.shiningglass.base.app.LanguageUtil;
import com.icwork.shiningglass.base.app.SoundManager;
import com.icwork.shiningglass.base.music.Music;
import com.icwork.shiningglass.base.music.MusicListenter;
import com.icwork.shiningglass.base.music.MusicPlayer;
import com.icwork.shiningglass.base.music.Song;
import com.icwork.shiningglass.base.music.VisualizerManager;
import com.icwork.shiningglass.base.update.UpdateManager;
import com.icwork.shiningglass.ble.HeartBeatDevice;
import com.icwork.shiningglass.ble.HeartBeatDeviceFactory;
import com.icwork.shiningglass.dao.DeviceDao;
import com.icwork.shiningglass.dao.bean.Device;
import com.icwork.shiningglass.model.bean.Language;
import com.icwork.shiningglass.model.data.Agreement;
import com.icwork.shiningglass.sevice.FFTDataListenter;
import com.icwork.shiningglass.sevice.MikeSevice;
import com.icwork.shiningglass.ui.adapter.DeviceAdapter;
import com.icwork.shiningglass.ui.adapter.LanguageAdapter;
import com.icwork.shiningglass.ui.utils.AppUtils;
import com.icwork.shiningglass.ui.utils.ByteUtils;
import com.icwork.shiningglass.ui.utils.ClickFilter;
import com.icwork.shiningglass.ui.utils.DensityUtil;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.utils.PermissionPageUtils;
import com.icwork.shiningglass.ui.utils.SPUtils;
import com.icwork.shiningglass.ui.utils.ToastUtil;
import com.icwork.shiningglass.ui.widget.image3d.Image3DSwitchView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.greendao.query.WhereCondition;
import pub.devrel.easypermissions.EasyPermissions;

/* JADX INFO: loaded from: classes.dex */
public class ConnectActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener {
    private static final int GSP_OPEN = 10001;
    private static final int OTA_FAIL = 6;
    private static final int OTA_PROGRESS = 7;
    private static final int OTA_SUCCESS = 8;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static final String TAG = "ConnectActivity";
    private static BleManager bleManager;
    private static MikeSevice mikeSevice;
    private static MusicPlayer musicPlayer;
    private static BlockingQueue<byte[]> queue;
    private BleListener bleListener;
    private int curLight;
    private BleDevice curOTADevice;
    private long curTime;
    private DeviceDao deviceDao;
    private FFTDataListenter fFTDataListenter;
    Image3DSwitchView imageSwitchView;
    private boolean isInitService;
    private boolean isShowDeviceConnect;
    private boolean isShowSetting;
    private boolean isStartOTA;
    boolean isTimer;
    ImageView ivAddDevice;
    RelativeLayout ivBottom;
    ImageView ivCenter;
    private AnimationDrawable ivCenterAnim;
    ImageView ivRefresh;
    ImageView ivSetting;
    ImageView ivTop;
    private LanguageAdapter languageAdapter;
    LinearLayout llBottom;
    RelativeLayout llCenter;
    RelativeLayout llSeekbar;
    LinearLayout llTop1;
    LinearLayout llVersion;
    ListView lvDevice;
    ListView lvLanguage;
    private DeviceAdapter mAdapter;
    private String mLanguage;
    private PanchipOtaManager mPanchipOtaManager;
    private ProgressDialog mUpdateDialog;
    private MusicListenterImpl musicListenter;
    private File newFile1;
    private File newFile2;
    private String path;
    private ObjectAnimator refreshRotationAnimator;
    RelativeLayout rlCenter;
    RelativeLayout rlRefresh;
    RelativeLayout rlRoot;
    SeekBar sbMoveLight;
    private ScanTimerTask scanTimerTask;
    private Timer timer;
    TextView tvSettingsTitle;
    TextView tvVersion;
    private final int HANDLER_WHAT = 2;
    private final int HANDLER_WHAT_DISCONNECT = 3;
    private final int REQUEST_ENABLE_BT = 4;
    private final int SCAN = 5;
    public List<BleDevice> mList = new ArrayList();
    List<Language> languageList = new ArrayList();
    ServiceConnection mikeServiceConnection = new ServiceConnection() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LogUtil.d("onServiceConnected");
            ConnectActivity.mikeSevice = ((MikeSevice.BinderImpl) iBinder).getService();
            ConnectActivity.mikeSevice.start();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtil.d("onServiceDisconnected");
        }
    };
    ServiceConnection serviceConnection = new ServiceConnection() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.7
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) throws Throwable {
            Log.v("ruis", "onServiceConnected");
            ConnectActivity.musicPlayer = ((MusicPlayer.BinderImpl) iBinder).getService();
            ConnectActivity.this.musicListenter = ConnectActivity.this.new MusicListenterImpl();
            ConnectActivity.musicPlayer.registerMusicListenter(ConnectActivity.this.musicListenter);
            try {
                List<Song> listSearchAndSortOrder = ConnectActivity.musicPlayer.searchAndSortOrder(null);
                Music.setSongList(listSearchAndSortOrder);
                if (listSearchAndSortOrder != null && listSearchAndSortOrder.size() > 0) {
                    Music.setCurrentSong(listSearchAndSortOrder.get(0));
                }
                ConnectActivity.this.initRythm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Handler handler = new Handler() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.9
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 2) {
                ConnectActivity.this.mAdapter.setList(ConnectActivity.this.mList);
                ConnectActivity.this.mAdapter.notifyDataSetChanged();
                return;
            }
            if (i == 3) {
                ConnectActivity.this.closeDialog();
                ConnectActivity.this.mAdapter.setList(ConnectActivity.this.mList);
                ConnectActivity.this.mAdapter.notifyDataSetChanged();
                return;
            }
            if (i == 5) {
                ConnectActivity.this.startScan();
                return;
            }
            if (i == 6) {
                ConnectActivity.this.hideProgress();
                ToastUtil.showToast(ConnectActivity.this.getResources().getString(R.string.update_fail));
                if (ConnectActivity.this.curOTADevice != null) {
                    ConnectActivity connectActivity = ConnectActivity.this;
                    connectActivity.saveDevice(connectActivity.curOTADevice, false);
                    ConnectActivity.this.curOTADevice.disconnect();
                }
                ConnectActivity.this.initScanTimer();
                return;
            }
            if (i == 7) {
                ConnectActivity.this.setDialogProgress(((Integer) message.obj).intValue());
            } else {
                if (i != 8) {
                    return;
                }
                ConnectActivity.this.hideProgress();
                ToastUtil.showToast(ConnectActivity.this.getResources().getString(R.string.ota_update_success));
                ConnectActivity.this.initScanTimer();
            }
        }
    };
    long outTime = 10000;
    private CountDownTimer sendOutTimeTimer = new CountDownTimer(this.outTime, 1000) { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.16
        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            ConnectActivity.this.isTimer = true;
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            ConnectActivity.this.isTimer = false;
            ConnectActivity.this.closeDialog();
        }
    };
    private OtaLisenter otaLisenter = new OtaLisenter() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.18
        @Override // com.cdbwsoft.library.panchip.OtaLisenter
        public void OtaFileInfo(FileInfo fileInfo) {
            LogUtil.d("文件版本信息：" + fileInfo.app + " " + fileInfo.dev);
        }

        @Override // com.cdbwsoft.library.panchip.OtaLisenter
        public void OtaDeviceInfo(VersionInfo versionInfo) {
            LogUtil.d("设备版本信息：" + versionInfo.app + " " + versionInfo.dev);
        }

        @Override // com.cdbwsoft.library.panchip.OtaLisenter
        public void OtaVerifyResult(boolean z, String str) {
            LogUtil.d("状态：" + str);
        }

        @Override // com.cdbwsoft.library.panchip.OtaLisenter
        public void OtaFail(String str) {
            LogUtil.d("状态：" + str);
            ConnectActivity.this.handler.sendEmptyMessage(6);
        }

        @Override // com.cdbwsoft.library.panchip.OtaLisenter
        public void OtaProgress(int i) {
            LogUtil.d("OTA进度：" + i);
            ConnectActivity.this.handler.obtainMessage(7, Integer.valueOf(i)).sendToTarget();
        }

        @Override // com.cdbwsoft.library.panchip.OtaLisenter
        public void OtaSuccess() {
            ConnectActivity.this.handler.sendEmptyMessage(8);
            LogUtil.d("OTA更新成功");
        }
    };
    Runnable runnable = new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.20
        @Override // java.lang.Runnable
        public void run() {
            ConnectActivity.this.ivRefresh.setEnabled(true);
            ConnectActivity.this.ivRefresh.setAlpha(1.0f);
            ConnectActivity.this.stopRefreshAnimator();
        }
    };

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected int getLayoutId() {
        return R.layout.activity_connect;
    }

    @Subscribe
    public void stopRhy1(String str) {
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initView() {
        this.ivRefresh = (ImageView) findViewById(R.id.iv_refresh);
        this.lvDevice = (ListView) findViewById(R.id.lv_device);
        this.ivAddDevice = (ImageView) findViewById(R.id.iv_add_device);
        this.ivTop = (ImageView) findViewById(R.id.iv_top);
        this.llCenter = (RelativeLayout) findViewById(R.id.ll_center);
        this.ivBottom = (RelativeLayout) findViewById(R.id.iv_bottom);
        this.ivCenter = (ImageView) findViewById(R.id.iv_center);
        this.sbMoveLight = (SeekBar) findViewById(R.id.sb_move_light);
        this.llSeekbar = (RelativeLayout) findViewById(R.id.ll_seekbar);
        this.imageSwitchView = (Image3DSwitchView) findViewById(R.id.image_switch_view);
        this.llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        this.llTop1 = (LinearLayout) findViewById(R.id.ll_top1);
        this.rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        this.ivSetting = (ImageView) findViewById(R.id.iv_setting);
        this.lvLanguage = (ListView) findViewById(R.id.lv_language);
        this.llVersion = (LinearLayout) findViewById(R.id.ll_version);
        this.tvSettingsTitle = (TextView) findViewById(R.id.tv_settings_title);
        this.rlRefresh = (RelativeLayout) findViewById(R.id.rl_refresh);
        this.rlCenter = (RelativeLayout) findViewById(R.id.rl_center);
        this.tvVersion = (TextView) findViewById(R.id.tv_version);
        this.ivRefresh.setOnClickListener(this);
        this.ivAddDevice.setOnClickListener(this);
        this.rlRoot.setOnClickListener(this);
        this.ivBottom.setOnClickListener(this);
        this.ivTop.setOnClickListener(this);
        this.ivSetting.setOnClickListener(this);
        this.tvVersion.setText(ExifInterface.GPS_MEASUREMENT_INTERRUPTED + App.getInstance().getVersionName());
        this.languageList = getLanguageListData();
        DeviceAdapter deviceAdapter = new DeviceAdapter(this, null);
        this.mAdapter = deviceAdapter;
        this.lvDevice.setAdapter((ListAdapter) deviceAdapter);
        LanguageAdapter languageAdapter = new LanguageAdapter(this, this.languageList, R.layout.item_language_single_choice);
        this.languageAdapter = languageAdapter;
        this.lvLanguage.setAdapter((ListAdapter) languageAdapter);
        this.lvLanguage.setItemChecked(0, true);
        this.ivCenter.setImageResource(R.drawable.anim_splash_image);
        AnimationDrawable animationDrawable = (AnimationDrawable) this.ivCenter.getDrawable();
        this.ivCenterAnim = animationDrawable;
        animationDrawable.start();
    }

    private void initLanguage() {
        this.mLanguage = LanguageUtil.getSaveLanguage(this.mActivity);
        LogUtil.e("mLanguage:" + this.mLanguage);
        if (this.mLanguage.equals("zh")) {
            this.lvLanguage.setItemChecked(0, true);
            return;
        }
        if (this.mLanguage.equals("en")) {
            this.lvLanguage.setItemChecked(1, true);
            return;
        }
        if (this.mLanguage.equals("zh_TW")) {
            this.lvLanguage.setItemChecked(2, true);
            return;
        }
        if (this.mLanguage.equals("ja")) {
            this.lvLanguage.setItemChecked(3, true);
            return;
        }
        if (this.mLanguage.equals("de")) {
            this.lvLanguage.setItemChecked(4, true);
            return;
        }
        if (this.mLanguage.equals("pt")) {
            this.lvLanguage.setItemChecked(5, true);
            return;
        }
        if (this.mLanguage.equals("es")) {
            this.lvLanguage.setItemChecked(6, true);
            return;
        }
        if (this.mLanguage.equals("fr")) {
            this.lvLanguage.setItemChecked(7, true);
            return;
        }
        if (this.mLanguage.equals("ko")) {
            this.lvLanguage.setItemChecked(8, true);
        } else if (this.mLanguage.equals("ru")) {
            this.lvLanguage.setItemChecked(9, true);
        } else {
            this.lvLanguage.setItemChecked(1, true);
        }
    }

    private List<Language> getLanguageListData() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new Language("zh", getString(R.string.language_zh)));
        arrayList.add(new Language("en", getString(R.string.language_en)));
        arrayList.add(new Language(C.SP.LANGUAGE_HK, getString(R.string.language_tw)));
        arrayList.add(new Language("ja", getString(R.string.language_ja)));
        arrayList.add(new Language("de", getString(R.string.language_de)));
        arrayList.add(new Language("pt", getString(R.string.language_pt)));
        arrayList.add(new Language("es", getString(R.string.language_es)));
        arrayList.add(new Language("fr", getString(R.string.language_fr)));
        arrayList.add(new Language("ko", getString(R.string.language_ko)));
        arrayList.add(new Language("ru", getString(R.string.language_ru)));
        return arrayList;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initData() {
        fadeOut(this.ivAddDevice);
        this.deviceDao = App.getDaoSession().getDeviceDao();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (!BuildConfig.FLAVOR.equals(AppUtils.getAppMetaData(getApplicationContext(), AppConfig.META_CHANNEL))) {
            new UpdateManager(this.mActivity).versionUpdate();
        }
        this.bleListener = new BleListenerImpl();
        initDevice();
        queue = new LinkedTransferQueue();
        new Thread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.2
            @Override // java.lang.Runnable
            public void run() {
                while (true) {
                    try {
                        ConnectActivity.bleManager.writeDataBy4((byte[]) ConnectActivity.queue.take());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        requestPermissions();
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void bindListener() {
        this.mAdapter.setOnlistener(new DeviceAdapter.OnConnectListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.3
            @Override // com.icwork.shiningglass.ui.adapter.DeviceAdapter.OnConnectListener
            public void onLongClick(BleDevice bleDevice) {
            }

            @Override // com.icwork.shiningglass.ui.adapter.DeviceAdapter.OnConnectListener
            public void rename(BleDevice bleDevice) {
            }

            @Override // com.icwork.shiningglass.ui.adapter.DeviceAdapter.OnConnectListener
            public void connect(BleDevice bleDevice) {
                if (!bleDevice.isConnected()) {
                    SoundManager.getInstance().textOn();
                    ConnectActivity.this.stopScan();
                    ConnectActivity.this.connectDevice(bleDevice, true);
                } else {
                    SoundManager.getInstance().textOff();
                    ConnectActivity.this.saveDevice(bleDevice, false);
                    ConnectActivity.this.stopScan();
                    ConnectActivity.this.disconnectDevice(bleDevice);
                }
            }
        });
        this.imageSwitchView.setCurrentImage(0);
        this.imageSwitchView.setOnImageItemClickListener(new Image3DSwitchView.OnImageItemClickListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.4
            @Override // com.icwork.shiningglass.ui.widget.image3d.Image3DSwitchView.OnImageItemClickListener
            public void onClick(int i) {
                LogUtil.d("currentImage:" + i);
                SoundManager.getInstance().mainMenuSelet();
                switch (i) {
                    case 0:
                    case 4:
                        ConnectActivity.this.startActivity(new Intent(ConnectActivity.this, (Class<?>) TextEditActivity.class));
                        break;
                    case 1:
                    case 5:
                        ConnectActivity.this.startActivity(new Intent(ConnectActivity.this, (Class<?>) ImageActivity.class));
                        break;
                    case 2:
                    case 6:
                        ConnectActivity.this.startActivity(new Intent(ConnectActivity.this, (Class<?>) RhythmActivity.class));
                        break;
                    case 3:
                    case 7:
                        ConnectActivity.this.startActivity(new Intent(ConnectActivity.this, (Class<?>) DiyActivity.class));
                        break;
                }
            }

            @Override // com.icwork.shiningglass.ui.widget.image3d.Image3DSwitchView.OnImageItemClickListener
            public void onTouch() {
                LogUtil.d("onTouch");
            }

            @Override // com.icwork.shiningglass.ui.widget.image3d.Image3DSwitchView.OnImageItemClickListener
            public void onTouchStop() {
                LogUtil.d("onTouchStop");
            }
        });
        this.sbMoveLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.5
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                ConnectActivity.this.curLight = i;
                DataManager.getInstance().setCurLight(ConnectActivity.this.curLight);
                ConnectActivity connectActivity = ConnectActivity.this;
                connectActivity.sendLight(connectActivity.curLight);
            }
        });
        this.lvLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.6
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                ConnectActivity connectActivity = ConnectActivity.this;
                connectActivity.mLanguage = connectActivity.languageList.get(i).getAbbreviation();
                LogUtil.d("选择的语言是：" + ConnectActivity.this.mLanguage);
            }
        });
    }

    @Override // com.icwork.shiningglass.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
        if (this.isInitService) {
            return;
        }
        initService();
    }

    @Override // pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
    public void onPermissionsGranted(int i, List<String> list) {
        LogUtil.d("onPermissionsGranted==========");
        if (this.isInitService) {
            return;
        }
        initService();
    }

    @Override // pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
    public void onPermissionsDenied(int i, List<String> list) {
        LogUtil.d("onPermissionsDenied==========");
        finish();
    }

    private void requestPermissions() {
        LogUtil.d("requestPermissions================" + this.isInitService);
        if (Build.VERSION.SDK_INT >= 33) {
            String[] strArr = {"android.permission.RECORD_AUDIO", "android.permission.BLUETOOTH_ADMIN", "android.permission.ACCESS_FINE_LOCATION", "android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_ADVERTISE", "android.permission.READ_MEDIA_AUDIO"};
            if (!EasyPermissions.hasPermissions(this, strArr)) {
                EasyPermissions.requestPermissions(this, getResources().getString(R.string.request_permission), 1, strArr);
                return;
            } else {
                if (this.isInitService) {
                    return;
                }
                initService();
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= 31) {
            String[] strArr2 = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_ADVERTISE", "android.permission.ACCESS_FINE_LOCATION"};
            if (!EasyPermissions.hasPermissions(this, strArr2)) {
                EasyPermissions.requestPermissions(this, getResources().getString(R.string.request_permission), 1, strArr2);
                return;
            } else {
                if (this.isInitService) {
                    return;
                }
                initService();
                return;
            }
        }
        String[] strArr3 = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION"};
        if (!EasyPermissions.hasPermissions(this, strArr3)) {
            LogUtil.d("requestPermissions================1");
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.request_permission), 1, strArr3);
        } else {
            if (this.isInitService) {
                return;
            }
            initService();
        }
    }

    private void initService() {
        this.isInitService = true;
        Log.v("ruis", "initService");
        bindService(new Intent(this, (Class<?>) MusicPlayer.class), this.serviceConnection, 1);
        bindService(new Intent(this, (Class<?>) MikeSevice.class), this.mikeServiceConnection, 1);
        initBLE();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initRythm() {
        this.fFTDataListenter = new FFTDataListenter() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity$$ExternalSyntheticLambda0
            @Override // com.icwork.shiningglass.sevice.FFTDataListenter
            public final void onStart(byte[] bArr) {
                this.f$0.lambda$initRythm$0(bArr);
            }
        };
        VisualizerManager.getInstance().setFftDataListenter(this.fFTDataListenter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initRythm$0(byte[] bArr) {
        Log.v("ruis", "initRythm --fFTDataListenter " + musicPlayer + DataManager.getInstance().isMicrophoneEnable());
        MusicPlayer musicPlayer2 = musicPlayer;
        if (musicPlayer2 == null || !musicPlayer2.isPlaying() || DataManager.getInstance().isMicrophoneEnable()) {
            return;
        }
        sendRhythmData((byte) DataManager.getInstance().getCurSelectRhythmMode(), bArr);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (ClickFilter.filter()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_add_device /* 2131296415 */:
                LogUtil.d("====iv_add_device");
                showDeviceConnect();
                break;
            case R.id.iv_refresh /* 2131296457 */:
                LogUtil.d("刷新扫描");
                SoundManager.getInstance().mainConnect();
                LogUtil.d("====Refreshing");
                this.ivRefresh.setEnabled(false);
                this.ivRefresh.setAlpha(0.5f);
                refreshAnimator();
                this.mList.clear();
                initDevice();
                this.mAdapter.notifyDataSetChanged();
                startScan();
                this.handler.postDelayed(this.runnable, 10000L);
                break;
            case R.id.iv_setting /* 2131296458 */:
                this.ivSetting.setEnabled(false);
                if (this.isShowSetting) {
                    hideSetting();
                } else {
                    showSetting();
                }
                break;
            case R.id.rl_root /* 2131296566 */:
                if (this.isShowDeviceConnect) {
                    hideDeviceConnect();
                } else if (this.isShowSetting) {
                    hideSetting();
                    back();
                }
                break;
        }
    }

    public class MusicListenterImpl extends MusicListenter {
        public MusicListenterImpl() {
        }

        @Override // com.icwork.shiningglass.base.music.MusicListenter
        public void onStart() {
            super.onStart();
            Music.setSTATE(1);
        }

        @Override // com.icwork.shiningglass.base.music.MusicListenter
        public void onPause() {
            super.onPause();
            Music.setSTATE(2);
        }

        @Override // com.icwork.shiningglass.base.music.MusicListenter
        public void onCompletion(MediaPlayer mediaPlayer) {
            LogUtil.e("onCompletion");
            Music.setSTATE(0);
            try {
                int playMode = Music.getPlayMode();
                if (playMode == 1) {
                    ConnectActivity.musicPlayer.play(Music.getRandomSong().getFileUrl());
                } else if (playMode == 2) {
                    ConnectActivity.musicPlayer.rePlay(Music.getCurrentSong().getFileUrl());
                } else {
                    ConnectActivity.musicPlayer.play(Music.getNextSong().getFileUrl());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static MikeSevice getMikeSevice() {
        return mikeSevice;
    }

    public void initBLE() {
        LogUtil.d("初始化蓝牙");
        checkBLEStatus();
        bleManager = App.getInstance().getBleManager(HeartBeatDevice.class, this.bleListener, new HeartBeatDeviceFactory(this));
        this.mPanchipOtaManager = PanchipOtaManager.getInstance();
    }

    private void checkBLEStatus() {
        if (!getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            Toast.makeText(this, getString(R.string.ble_not_open), 0).show();
            stopRhy(C.MAIN_EVENT.STOP_RHY);
            if (this.serviceConnection != null) {
                MusicPlayer musicPlayer2 = musicPlayer;
                if (musicPlayer2 != null) {
                    musicPlayer2.release();
                }
                if (this.isInitService) {
                    unbindService(this.serviceConnection);
                    this.isInitService = false;
                }
            }
            finish();
            return;
        }
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            if (!defaultAdapter.isEnabled()) {
                Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                if (ActivityCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_CONNECT") != 0) {
                    return;
                }
                startActivityForResult(intent, 4);
                return;
            }
            if (AppUtils.isGpsOpen(this)) {
                initScanTimer();
            } else {
                if (DataManager.getInstance().isShowGPSDialog()) {
                    return;
                }
                gpsOpen();
            }
        }
    }

    class BleListenerImpl extends BleListener {
        BleListenerImpl() {
        }

        @Override // com.cdbwsoft.library.ble.BleListener
        public boolean filterDevice(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
            boolean z = true;
            if (BleConfig.matchProduct(bArr) == -1) {
                return true;
            }
            LogUtil.d("==扫到的设备：" + bluetoothDevice);
            if (i != 0 && bluetoothDevice != null) {
                String address = bluetoothDevice.getAddress();
                String name = bluetoothDevice.getName();
                if (address != null && !address.isEmpty() && name != null && !name.isEmpty()) {
                    BleDevice bleDevice = null;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= ConnectActivity.this.mList.size()) {
                            z = false;
                            break;
                        }
                        bleDevice = ConnectActivity.this.mList.get(i2);
                        if (bleDevice != null && address.equals(bleDevice.getBleAddress())) {
                            break;
                        }
                        i2++;
                    }
                    BleDevice bleDevice2 = new BleDevice(ConnectActivity.bleManager, address, name);
                    if (!z) {
                        ConnectActivity.this.mList.add(bleDevice2);
                        if (ConnectActivity.this.handler != null) {
                            Message messageObtainMessage = ConnectActivity.this.handler.obtainMessage();
                            messageObtainMessage.what = 2;
                            ConnectActivity.this.handler.sendMessage(messageObtainMessage);
                        }
                    } else {
                        ConnectActivity.this.updateUI(bleDevice);
                        if (bleDevice != null && ConnectActivity.this.isReConnect(bleDevice) && System.currentTimeMillis() - ConnectActivity.this.curTime > 2000) {
                            ConnectActivity.this.curTime = System.currentTimeMillis();
                            LogUtil.d("==重连2：" + bleDevice.getBleAddress());
                            ConnectActivity.this.connectDevice(bleDevice, false);
                        }
                    }
                }
            }
            return false;
        }

        @Override // com.cdbwsoft.library.ble.BleListener
        public void onRead(BleDevice bleDevice, byte[] bArr) {
            String[] strArrSplit;
            try {
                String str = new String(bArr);
                LogUtil.e("===onRead===".concat(str));
                if (!str.contains("-") || (strArrSplit = str.split("-")) == null || strArrSplit.length < 2 || Integer.valueOf(strArrSplit[1]).intValue() != 1 || strArrSplit.length < 3) {
                    return;
                }
                Integer.valueOf(strArrSplit[2]).intValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.cdbwsoft.library.ble.BleListener
        public void onChanged(BleDevice bleDevice, byte[] bArr) {
            super.onChanged(bleDevice, bArr);
        }

        @Override // com.cdbwsoft.library.ble.BleListener
        public void onConnectionChanged(final BleDevice bleDevice) {
            super.onConnectionChanged(bleDevice);
            if (bleDevice != null && bleDevice.isDisConnect()) {
                LogUtil.d("===连接断开");
                ConnectActivity.this.handler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.BleListenerImpl.1
                    @Override // java.lang.Runnable
                    public void run() {
                        List<BleDevice> deviceList = App.getAppData().getDeviceList();
                        for (int i = 0; i < deviceList.size(); i++) {
                            deviceList.remove(bleDevice);
                        }
                        ConnectActivity.this.updateUI(bleDevice);
                        if (ConnectActivity.this.curOTADevice != null && ConnectActivity.this.curOTADevice.getBleAddress().equals(bleDevice.getBleAddress())) {
                            ConnectActivity.this.hideProgress();
                            ConnectActivity.this.initScanTimer();
                        }
                        Message messageObtainMessage = ConnectActivity.this.handler.obtainMessage();
                        messageObtainMessage.what = 3;
                        ConnectActivity.this.handler.sendMessage(messageObtainMessage);
                    }
                }, 1000L);
            } else if (bleDevice != null) {
                bleDevice.isConnected();
            }
        }

        @Override // com.cdbwsoft.library.ble.BleListener
        public void onReady(final BleDevice bleDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
            super.onReady(bleDevice, bluetoothGattDescriptor);
            LogUtil.d("=====bleDevice连接成功:" + bleDevice);
            SPUtils.put(ConnectActivity.this, "isConnectFlag", true);
            ConnectActivity.this.handler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.BleListenerImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    List<BleDevice> deviceList = App.getAppData().getDeviceList();
                    ConnectActivity.this.closeDialog();
                    ConnectActivity.this.updateUI(bleDevice);
                    int i = 0;
                    for (int i2 = 0; i2 < deviceList.size(); i2++) {
                        if (bleDevice.getBleAddress().equals(deviceList.get(i2).getBleAddress())) {
                            return;
                        }
                    }
                    deviceList.add(bleDevice);
                    ConnectActivity.this.saveDevice(bleDevice, true);
                    BleDevice bleDevice2 = bleDevice;
                    if (bleDevice2 != null && bleDevice2.isConnected()) {
                        while (true) {
                            if (i >= deviceList.size()) {
                                break;
                            }
                            if (bleDevice.getBleAddress().equals(deviceList.get(i).getBleAddress())) {
                                deviceList.set(i, bleDevice);
                                LogUtil.d("==更新蓝牙设备");
                                break;
                            }
                            i++;
                        }
                    }
                    ConnectActivity.this.ota(bleDevice);
                    ConnectActivity.this.mPanchipOtaManager.setBleDevice(bleDevice);
                    ConnectActivity.this.handler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.BleListenerImpl.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ConnectActivity.this.mPanchipOtaManager.setMtuSize1(bleDevice, 100);
                        }
                    }, 500L);
                }
            }, 1000L);
        }
    }

    public static BleManager getBleManager() {
        return bleManager;
    }

    public void ota(BleDevice bleDevice) {
        LogUtil.e("ota");
        LogUtil.e("result:" + bleDevice.readCharacteristicByRead());
    }

    public void startScan() {
        if (bleManager != null) {
            LogUtil.d("====开始扫描设备");
            runOnUiThread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.8
                @Override // java.lang.Runnable
                public void run() {
                    ConnectActivity.bleManager.scanDevice();
                }
            });
        }
    }

    public void stopScan() {
        stopRefreshAnimator();
        this.ivRefresh.setEnabled(true);
        this.ivRefresh.setAlpha(1.0f);
    }

    public void initDevice() {
        List<BleDevice> deviceList = App.getAppData().getDeviceList();
        for (int i = 0; i < deviceList.size(); i++) {
            this.mList.add(deviceList.get(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectDevice(final BleDevice bleDevice, boolean z) {
        if (this.isStartOTA) {
            return;
        }
        if (z) {
            showDialog(getResources().getString(R.string.connecting));
        }
        LogUtil.d("====addDevice....");
        bleManager.addDevice(bleDevice);
        new Handler().postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.10
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.d("====连接设备：" + bleDevice);
                ConnectActivity.bleManager.stopScan();
                ConnectActivity.bleManager.connect(bleDevice);
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnectDevice(BleDevice bleDevice) {
        bleDevice.disconnect();
        showDialog(getResources().getString(R.string.dissconnecting));
    }

    public void updateUI(BleDevice bleDevice) {
        LogUtil.d("===updateUI");
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mList.size(); i++) {
            arrayList.add(this.mList.get(i).getBleAddress());
        }
        if (this.mAdapter == null || bleDevice == null) {
            return;
        }
        if (arrayList.contains(bleDevice.getBleAddress())) {
            LogUtil.d("当前列表中存在：" + bleDevice.getBleAddress());
            for (int i2 = 0; i2 < this.mList.size(); i2++) {
                if (bleDevice.getBleAddress().equals(this.mList.get(i2).getBleAddress())) {
                    LogUtil.d("更新扫描列表。。。" + bleDevice.toString());
                    this.mList.get(i2).setConnectionState(bleDevice.getConnectionState());
                }
            }
        } else if (bleDevice.isConnected()) {
            this.mList.add(bleDevice);
            LogUtil.d("添加设备");
            App.getAppData().getDeviceList().add(bleDevice);
        }
        this.mAdapter.setList(this.mList);
        this.mAdapter.notifyDataSetChanged();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 4 && i2 == 0) {
            stopRhy(C.MAIN_EVENT.STOP_RHY);
            if (this.serviceConnection != null) {
                MusicPlayer musicPlayer2 = musicPlayer;
                if (musicPlayer2 != null) {
                    musicPlayer2.release();
                }
                if (this.isInitService) {
                    unbindService(this.serviceConnection);
                    this.isInitService = false;
                }
            }
            finish();
            return;
        }
        if (i == 4 && i2 == -1) {
            LogUtil.d("=====REQUEST_ENABLE_BT==");
            if (AppUtils.isGpsOpen(this)) {
                initScanTimer();
            } else if (!DataManager.getInstance().isShowGPSDialog()) {
                gpsOpen();
            }
        } else if (i == GSP_OPEN) {
            LogUtil.d("=====GSP_OPEN==");
            initScanTimer();
        } else if (i == 1002) {
            LogUtil.d("跳转到权限申请界面返回的结果");
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // com.icwork.shiningglass.base.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("========onDestroy============");
        try {
            if (this.isInitService && this.serviceConnection != null) {
                stopRhy(C.MAIN_EVENT.STOP_RHY);
                unbindService(this.serviceConnection);
            }
            EventBus.getDefault().unregister(this);
            cancelTimerTask1();
            if (!this.isInitService || this.mikeServiceConnection == null) {
                return;
            }
            VisualizerManager.getInstance().removeFftDataListenter(this.fFTDataListenter);
            VisualizerManager.getInstance().stop();
            VisualizerManager.getInstance();
            VisualizerManager.clear();
            mikeSevice.stop();
            unbindService(this.mikeServiceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRhythmData(byte b, byte[] bArr) {
        LogUtil.d("发送音乐律动：" + ByteUtils.binaryToHexString(bArr));
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

    @Subscribe
    public void stopRhy(String str) {
        if (str.equals(C.MAIN_EVENT.STOP_RHY)) {
            LogUtil.d("======停止音乐和律动");
            Music.setSTATE(2);
            MusicPlayer musicPlayer2 = musicPlayer;
            if (musicPlayer2 != null && musicPlayer2.isPlaying()) {
                musicPlayer.pause();
            }
            Music.setIsOpenedRhythm(false);
            return;
        }
        if (str.equals(C.MAIN_EVENT.STOP_RHY1)) {
            LogUtil.d("======停止音乐和律动1");
            Music.setSTATE(2);
            MusicPlayer musicPlayer3 = musicPlayer;
            if (musicPlayer3 != null && musicPlayer3.isPlaying()) {
                musicPlayer.stop();
                musicPlayer.unregisterMusicListenter(this.musicListenter);
            }
            Music.setIsOpenedRhythm(false);
            if (this.serviceConnection != null) {
                MusicPlayer musicPlayer4 = musicPlayer;
                if (musicPlayer4 != null) {
                    musicPlayer4.release();
                }
                if (this.isInitService) {
                    unbindService(this.serviceConnection);
                    this.serviceConnection = null;
                }
            }
        }
    }

    private void exitRhythm() {
        BleDevice bleDevice;
        List<BleDevice> deviceList = App.getAppData().getDeviceList();
        if (deviceList == null || deviceList.size() <= 0) {
            return;
        }
        for (int i = 0; i < deviceList.size() && (bleDevice = deviceList.get(i)) != null; i++) {
            bleDevice.writeCharacteristic(Agreement.getEncryptData(Agreement.getExitRhyCommand()));
        }
    }

    class ScanTimerTask extends TimerTask {
        ScanTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Message message = new Message();
            message.what = 5;
            ConnectActivity.this.handler.sendMessage(message);
        }
    }

    public void cancelTimerTask1() {
        ScanTimerTask scanTimerTask = this.scanTimerTask;
        if (scanTimerTask != null) {
            scanTimerTask.cancel();
            this.handler.removeCallbacks(this.scanTimerTask);
        }
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer.purge();
            this.timer = null;
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveDevice(BleDevice bleDevice, boolean z) {
        if (bleDevice != null) {
            try {
                Device deviceUnique = this.deviceDao.queryBuilder().where(DeviceDao.Properties.Mac.eq(bleDevice.getBleAddress()), new WhereCondition[0]).unique();
                if (deviceUnique != null && !TextUtils.isEmpty(deviceUnique.getMac())) {
                    deviceUnique.setDeviceName(bleDevice.getBleName());
                    deviceUnique.setIsReConnect(z);
                    deviceUnique.setMac(bleDevice.getBleAddress());
                    this.deviceDao.update(deviceUnique);
                    return;
                }
                Device device = new Device();
                device.setDeviceName(bleDevice.getBleName());
                device.setIsReConnect(z);
                device.setMac(bleDevice.getBleAddress());
                this.deviceDao.save(device);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isReConnect(BleDevice bleDevice) {
        Device deviceUnique;
        if (bleDevice != null) {
            try {
                if (!TextUtils.isEmpty(bleDevice.getBleAddress()) && (deviceUnique = this.deviceDao.queryBuilder().where(DeviceDao.Properties.Mac.eq(bleDevice.getBleAddress()), new WhereCondition[0]).unique()) != null) {
                    return deviceUnique.getIsReConnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    protected void permissionOpen() {
        new AlertDialog.Builder(this).setCancelable(false).setTitle(getString(R.string.tip)).setMessage(getResources().getString(R.string.tip2)).setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) throws Throwable {
                dialogInterface.dismiss();
                ConnectActivity connectActivity = ConnectActivity.this;
                new PermissionPageUtils(connectActivity, AppUtils.getPackageName(connectActivity)).jumpPermissionPage();
            }
        }).create().show();
    }

    protected void gpsOpen() {
        LogUtil.d("===打开GPS");
        DataManager.getInstance().setShowGPSDialog(true);
        if (AppUtils.isGpsOpen(this)) {
            return;
        }
        new AlertDialog.Builder(this).setCancelable(false).setTitle(R.string.tip).setMessage(R.string.gps_tip).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.13
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ConnectActivity.this.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), ConnectActivity.GSP_OPEN);
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private void refreshAnimator() {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.ivRefresh, "rotation", 0.0f, 360.0f);
        this.refreshRotationAnimator = objectAnimatorOfFloat;
        objectAnimatorOfFloat.setDuration(1000L);
        this.refreshRotationAnimator.setRepeatCount(10);
        this.refreshRotationAnimator.setInterpolator(new LinearInterpolator());
        this.refreshRotationAnimator.addListener(new Animator.AnimatorListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.15
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                LogUtil.e("开始动画：ivForward.setEnabled(false)");
                ConnectActivity.this.ivRefresh.setEnabled(false);
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                LogUtil.e("停止动画：ivForward.setEnabled(true)");
                ConnectActivity.this.ivRefresh.setEnabled(true);
            }
        });
        this.refreshRotationAnimator.start();
    }

    public void stopRefreshAnimator() {
        ObjectAnimator objectAnimator = this.refreshRotationAnimator;
        if (objectAnimator == null || !objectAnimator.isRunning()) {
            return;
        }
        this.refreshRotationAnimator.cancel();
        this.refreshRotationAnimator = null;
        this.ivRefresh.setAlpha(1.0f);
    }

    private void showDialog(String str) {
        CountDownTimer countDownTimer = this.sendOutTimeTimer;
        if (countDownTimer != null) {
            countDownTimer.start();
        }
        showProgressDialog1(this, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeDialog() {
        dismissProgressDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initScanTimer() {
        if (this.timer == null) {
            this.timer = new Timer();
            ScanTimerTask scanTimerTask = new ScanTimerTask();
            this.scanTimerTask = scanTimerTask;
            this.timer.schedule(scanTimerTask, 1000L, 10000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initOTA(final BleDevice bleDevice, final boolean z) {
        this.curOTADevice = bleDevice;
        cancelTimerTask1();
        bleManager.stopScan();
        LogUtil.e("initOTA");
        this.mPanchipOtaManager.setBleDevice(bleDevice);
        this.mPanchipOtaManager.setOtaLisenter(this.otaLisenter);
        this.handler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.17
            @Override // java.lang.Runnable
            public void run() {
                if (z) {
                    ConnectActivity.this.mPanchipOtaManager.selectFile(ConnectActivity.this.newFile1);
                } else {
                    ConnectActivity.this.mPanchipOtaManager.selectFile(ConnectActivity.this.newFile2);
                }
                ConnectActivity.this.mPanchipOtaManager.getVersion(bleDevice);
            }
        }, 200L);
    }

    private void showProgress() {
        ProgressDialog progressDialog = new ProgressDialog(AppManager.getAppManager().currentActivity());
        this.mUpdateDialog = progressDialog;
        progressDialog.setTitle(R.string.ota_title);
        this.mUpdateDialog.setMessage(getString(R.string.updating));
        this.mUpdateDialog.setCancelable(false);
        this.mUpdateDialog.setCanceledOnTouchOutside(false);
        this.mUpdateDialog.setMax(100);
        this.mUpdateDialog.setIndeterminate(false);
        this.mUpdateDialog.setProgressStyle(1);
        if (this.mUpdateDialog.isShowing()) {
            return;
        }
        this.mUpdateDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDialogProgress(int i) {
        Log.e(TAG, "setDialogProgress: " + i);
        ProgressDialog progressDialog = this.mUpdateDialog;
        if (progressDialog != null) {
            progressDialog.setProgress(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideProgress() {
        this.isStartOTA = false;
        ProgressDialog progressDialog = this.mUpdateDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void startOTA(final BleDevice bleDevice, final boolean z) {
        this.isStartOTA = true;
        stopRhy(C.MAIN_EVENT.STOP_RHY);
        List<BleDevice> deviceList = App.getAppData().getDeviceList();
        for (int i = 0; i < deviceList.size(); i++) {
            if (!bleDevice.getBleAddress().equals(deviceList.get(i).getBleAddress())) {
                deviceList.get(i).disconnect();
                LogUtil.e("断开之前的设备：" + deviceList.get(i).getBleName());
            }
        }
        showProgress();
        this.handler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.19
            @Override // java.lang.Runnable
            public void run() {
                ConnectActivity.this.initOTA(bleDevice, z);
            }
        }, 1500L);
    }

    private void showDeviceConnect() {
        if (this.isShowDeviceConnect) {
            return;
        }
        SoundManager.getInstance().mainOpen();
        this.ivRefresh.setEnabled(false);
        this.ivRefresh.setAlpha(0.5f);
        this.mList.clear();
        initDevice();
        this.mAdapter.notifyDataSetChanged();
        startScan();
        refreshAnimator();
        this.handler.removeCallbacks(this.runnable);
        this.handler.postDelayed(this.runnable, 10000L);
        this.isShowDeviceConnect = true;
        this.ivSetting.setEnabled(false);
        this.lvDevice.setVisibility(0);
        this.lvLanguage.setVisibility(8);
        this.ivCenter.setVisibility(8);
        this.rlCenter.setVisibility(8);
        bottomMove(this.ivBottom);
        bottomMove(this.llBottom);
        this.ivAddDevice.clearAnimation();
    }

    private void hideDeviceConnect() {
        if (this.isShowDeviceConnect) {
            SoundManager.getInstance().mainColse();
            this.isShowDeviceConnect = false;
            this.ivSetting.setEnabled(true);
            topMove(this.ivBottom, -215.0f, 0);
            topMove(this.llBottom, 215.0f, 1);
            this.ivAddDevice.setVisibility(0);
            this.ivRefresh.setVisibility(8);
            this.rlRefresh.setVisibility(8);
            this.lvDevice.setVisibility(8);
            fadeOut(this.ivAddDevice);
        }
    }

    private void showSetting() {
        initLanguage();
        if (this.isShowSetting) {
            return;
        }
        SoundManager.getInstance().mainOpen();
        this.isShowSetting = true;
        this.llVersion.setVisibility(0);
        this.ivRefresh.setVisibility(8);
        this.rlRefresh.setVisibility(8);
        this.tvSettingsTitle.setVisibility(0);
        this.lvDevice.setVisibility(8);
        this.lvLanguage.setVisibility(0);
        this.isShowSetting = true;
        this.ivCenter.setVisibility(8);
        this.rlCenter.setVisibility(8);
        bottomMove(this.ivBottom);
        bottomMove(this.llBottom);
        this.ivAddDevice.clearAnimation();
        this.ivAddDevice.setVisibility(8);
    }

    private void hideSetting() {
        if (this.isShowSetting) {
            SoundManager.getInstance().mainColse();
            this.isShowSetting = false;
            this.llVersion.setVisibility(8);
            this.tvSettingsTitle.setVisibility(8);
            this.lvLanguage.setVisibility(8);
            topMove(this.ivBottom, -215.0f, 0);
            topMove(this.llBottom, 215.0f, 1);
            this.ivAddDevice.setVisibility(0);
            fadeOut(this.ivAddDevice);
        }
    }

    private void bottomMove(final View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (int) DensityUtil.dp2px(this, 215.0f));
        translateAnimation.setDuration(500L);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.21
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                ConnectActivity.this.rlRoot.setEnabled(false);
                ConnectActivity.this.ivSetting.setEnabled(false);
                LogUtil.d("动画开始下移 rlRoot fase");
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                ConnectActivity.this.llCenter.setVisibility(0);
                ConnectActivity.this.llBottom.setVisibility(4);
                if (ConnectActivity.this.isShowDeviceConnect) {
                    ConnectActivity.this.ivRefresh.setVisibility(0);
                    ConnectActivity.this.rlRefresh.setVisibility(0);
                }
                ConnectActivity.this.ivAddDevice.setVisibility(8);
                ConnectActivity.this.rlRoot.setEnabled(true);
                LogUtil.d("动画结束下移 rlRoot true");
                if (ConnectActivity.this.isShowDeviceConnect) {
                    return;
                }
                ConnectActivity.this.ivSetting.setEnabled(true);
            }
        });
    }

    private void topMove(final View view, float f, int i) {
        TranslateAnimation translateAnimation;
        float fDp2px = (int) DensityUtil.dp2px(this, f);
        if (i == 1) {
            translateAnimation = new TranslateAnimation(0.0f, 0.0f, fDp2px, 0.0f);
        } else {
            translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, fDp2px);
        }
        translateAnimation.setDuration(500L);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.22
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                LogUtil.d("动画开始上移：rlRoot false");
                ConnectActivity.this.rlRoot.setEnabled(false);
                ConnectActivity.this.ivSetting.setEnabled(false);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                ConnectActivity.this.ivCenter.setVisibility(0);
                ConnectActivity.this.rlCenter.setVisibility(0);
                ConnectActivity.this.llBottom.setVisibility(0);
                ConnectActivity.this.llCenter.setVisibility(8);
                if (ConnectActivity.this.isShowDeviceConnect) {
                    ConnectActivity.this.ivSetting.setEnabled(false);
                } else {
                    ConnectActivity.this.ivSetting.setEnabled(true);
                }
                ConnectActivity.this.tvSettingsTitle.setVisibility(8);
                LogUtil.d("动画结束上移：rlRoot true");
                ConnectActivity.this.rlRoot.setEnabled(true);
            }
        });
    }

    public static void fadeIn(View view, float f, float f2, long j) {
        if (view.getVisibility() == 0) {
            return;
        }
        view.setVisibility(0);
        AlphaAnimation alphaAnimation = new AlphaAnimation(f, f2);
        alphaAnimation.setDuration(j);
        view.startAnimation(alphaAnimation);
    }

    public static void fadeIn(View view) {
        fadeIn(view, 0.0f, 1.0f, 500L);
    }

    public static void fadeOut(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(800L);
        alphaAnimation.setRepeatCount(-1);
        alphaAnimation.setRepeatMode(2);
        view.startAnimation(alphaAnimation);
    }

    public void sendLight(int i) {
        if (bleManager == null) {
            bleManager = App.getInstance().getBleManager(HeartBeatDevice.class, this.bleListener, new HeartBeatDeviceFactory(this));
        }
        if (i == 0) {
            i = 1;
        }
        byte[] light = Agreement.getLight(i);
        LogUtil.e("sendLight:" + i);
        bleManager.writeData(Agreement.getEncryptData(light));
    }

    public boolean isChangedLanguage() {
        return !this.mLanguage.equalsIgnoreCase(LanguageUtil.getSaveLanguage(this.mActivity));
    }

    private void back() {
        if (isChangedLanguage()) {
            EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY1);
            saveLanguage();
            updateDeviceListToDb();
            List<BleDevice> deviceList = App.getAppData().getDeviceList();
            if (deviceList != null) {
                for (int i = 0; i < deviceList.size(); i++) {
                    deviceList.get(i).disconnect();
                }
            }
            deviceList.clear();
            this.isInitService = false;
            showProgressDialog(this.mActivity, "语言切换中");
            try {
                stopRhy(C.MAIN_EVENT.STOP_RHY);
                ServiceConnection serviceConnection = this.serviceConnection;
                if (serviceConnection != null) {
                    unbindService(serviceConnection);
                }
                mikeSevice.stop();
                ServiceConnection serviceConnection2 = this.mikeServiceConnection;
                if (serviceConnection2 != null) {
                    unbindService(serviceConnection2);
                }
                musicPlayer.unregisterMusicListenter(this.musicListenter);
                musicPlayer = null;
                this.musicListenter = null;
                VisualizerManager.getInstance().removeFftDataListenter(this.fFTDataListenter);
                VisualizerManager.getInstance().stop();
                VisualizerManager.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.handler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ConnectActivity.23
                @Override // java.lang.Runnable
                public void run() {
                    ConnectActivity.this.dismissProgressDialog();
                    ConnectActivity.this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                    ConnectActivity.this.finish();
                    Intent intent = new Intent(ConnectActivity.this.mActivity, (Class<?>) ConnectActivity.class);
                    intent.addFlags(268435456);
                    intent.addFlags(32768);
                    ConnectActivity.this.startActivity(intent);
                }
            }, 500L);
        }
    }

    public void saveLanguage() {
        LogUtil.d("保存设置语言的类型:" + this.mLanguage);
        SPUtils.put(this.mActivity, C.SP.LANGUAGE, this.mLanguage);
    }

    private void updateDeviceListToDb() {
        DeviceDao deviceDao = App.getDaoSession().getDeviceDao();
        List<Device> listLoadAll = deviceDao.loadAll();
        if (listLoadAll == null || listLoadAll.size() <= 0) {
            return;
        }
        for (int i = 0; i < listLoadAll.size(); i++) {
            listLoadAll.get(i).setIsReConnect(false);
        }
        deviceDao.updateInTx(listLoadAll);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }
}
