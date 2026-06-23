package com.icwork.shiningglass.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import com.cdbwsoft.library.ble.BleDevice;
import com.cdbwsoft.library.ble.BleManager;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.App;
import com.icwork.shiningglass.base.AppManager;
import com.icwork.shiningglass.base.BaseActivity;
import com.icwork.shiningglass.base.app.C;
import com.icwork.shiningglass.base.app.LanguageUtil;
import com.icwork.shiningglass.base.app.SoundManager;
import com.icwork.shiningglass.dao.DeviceDao;
import com.icwork.shiningglass.dao.bean.Device;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.utils.SPUtils;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes.dex */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    Handler handler = new Handler();
    private ImageView imgvDe;
    private ImageView imgvEs;
    private ImageView imgvFr;
    private ImageView imgvJa;
    private ImageView imgvKo;
    private ImageView imgvPt;
    private ImageView imgvRu;
    private ImageView imgvZhTw;
    private ImageView ivChina;
    private ImageView ivEnglish;
    private ImageView ivForward;
    private RelativeLayout layoutChina;
    private RelativeLayout layoutEnglish;
    private String mLanguage;
    private RelativeLayout rltlaoDe;
    private RelativeLayout rltlaoEs;
    private RelativeLayout rltlaoFr;
    private RelativeLayout rltlaoJa;
    private RelativeLayout rltlaoKo;
    private RelativeLayout rltlaoPt;
    private RelativeLayout rltlaoRu;
    private RelativeLayout rltlaoZhTw;
    private TextView tvTitle;
    private TextView tvVersion;

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void bindListener() {
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initView() {
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.ivForward = (ImageView) findViewById(R.id.iv_forward);
        this.ivChina = (ImageView) findViewById(R.id.iv_china);
        this.layoutChina = (RelativeLayout) findViewById(R.id.layout_china);
        this.ivEnglish = (ImageView) findViewById(R.id.iv_english);
        this.layoutEnglish = (RelativeLayout) findViewById(R.id.layout_english);
        this.tvVersion = (TextView) findViewById(R.id.tv_version);
        this.imgvZhTw = (ImageView) findViewById(R.id.imgv_zh_tw);
        this.rltlaoZhTw = (RelativeLayout) findViewById(R.id.rltlao_zh_tw);
        this.imgvJa = (ImageView) findViewById(R.id.imgv_ja);
        this.rltlaoJa = (RelativeLayout) findViewById(R.id.rltlao_ja);
        this.imgvDe = (ImageView) findViewById(R.id.imgv_de);
        this.rltlaoDe = (RelativeLayout) findViewById(R.id.rltlao_de);
        this.imgvPt = (ImageView) findViewById(R.id.imgv_pt);
        this.rltlaoPt = (RelativeLayout) findViewById(R.id.rltlao_pt);
        this.imgvEs = (ImageView) findViewById(R.id.imgv_es);
        this.rltlaoEs = (RelativeLayout) findViewById(R.id.rltlao_es);
        this.imgvFr = (ImageView) findViewById(R.id.imgv_fr);
        this.rltlaoFr = (RelativeLayout) findViewById(R.id.rltlao_fr);
        this.imgvKo = (ImageView) findViewById(R.id.imgv_ko);
        this.rltlaoKo = (RelativeLayout) findViewById(R.id.rltlao_ko);
        this.imgvRu = (ImageView) findViewById(R.id.imgv_ru);
        this.rltlaoRu = (RelativeLayout) findViewById(R.id.rltlao_ru);
        this.ivForward.setOnClickListener(this);
        this.layoutChina.setOnClickListener(this);
        this.layoutEnglish.setOnClickListener(this);
        this.rltlaoZhTw.setOnClickListener(this);
        this.rltlaoJa.setOnClickListener(this);
        this.rltlaoDe.setOnClickListener(this);
        this.rltlaoPt.setOnClickListener(this);
        this.rltlaoEs.setOnClickListener(this);
        this.rltlaoFr.setOnClickListener(this);
        this.rltlaoKo.setOnClickListener(this);
        this.rltlaoRu.setOnClickListener(this);
        this.tvTitle.setText(R.string.setting);
        this.tvVersion.setText(getString(R.string.version) + ExifInterface.GPS_MEASUREMENT_INTERRUPTED + App.getInstance().getVersionName());
        this.ivForward.setImageResource(R.drawable.setting_main);
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initData() {
        this.mLanguage = LanguageUtil.getSaveLanguage(this);
        LogUtil.e("mLanguage:" + this.mLanguage);
        if (this.mLanguage.equals("zh")) {
            checkZh();
            return;
        }
        if (this.mLanguage.equals("en")) {
            checkEn();
            return;
        }
        if (this.mLanguage.equals("zh_TW")) {
            checkZhTw();
            return;
        }
        if (this.mLanguage.equals("ja")) {
            checkJa();
            return;
        }
        if (this.mLanguage.equals("de")) {
            checkDe();
            return;
        }
        if (this.mLanguage.equals("pt")) {
            checkPt();
            return;
        }
        if (this.mLanguage.equals("es")) {
            checkEs();
            return;
        }
        if (this.mLanguage.equals("fr")) {
            checkFr();
            return;
        }
        if (this.mLanguage.equals("ko")) {
            checkKo();
        } else if (this.mLanguage.equals("ru")) {
            checkRu();
        } else {
            checkEn();
        }
    }

    private void back() {
        SoundManager.getInstance().textBack();
        if (isChangedLanguage()) {
            EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY1);
            BleManager bleManager = ConnectActivity.getBleManager();
            saveLanguage();
            updateDeviceListToDb();
            List<BleDevice> deviceList = App.getAppData().getDeviceList();
            if (deviceList != null) {
                for (int i = 0; i < deviceList.size(); i++) {
                    deviceList.get(i).disconnect();
                }
            }
            deviceList.clear();
            bleManager.release();
            this.handler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.SettingActivity.1
                @Override // java.lang.Runnable
                public void run() {
                    AppManager.getAppManager().finishAllActivity();
                    SettingActivity.this.startActivity(new Intent(SettingActivity.this, (Class<?>) ConnectActivity.class));
                    SettingActivity.this.finish();
                    SettingActivity.this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
            }, 1200L);
            return;
        }
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
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

    private void checkZh() {
        this.mLanguage = "zh";
        this.ivChina.setVisibility(0);
        this.ivEnglish.setVisibility(8);
        this.imgvZhTw.setVisibility(8);
        this.imgvJa.setVisibility(8);
        this.imgvDe.setVisibility(8);
        this.imgvPt.setVisibility(8);
        this.imgvEs.setVisibility(8);
        this.imgvFr.setVisibility(8);
        this.imgvKo.setVisibility(8);
        this.imgvRu.setVisibility(8);
    }

    private void checkEn() {
        this.mLanguage = "en";
        this.ivChina.setVisibility(8);
        this.ivEnglish.setVisibility(0);
        this.imgvZhTw.setVisibility(8);
        this.imgvJa.setVisibility(8);
        this.imgvDe.setVisibility(8);
        this.imgvPt.setVisibility(8);
        this.imgvEs.setVisibility(8);
        this.imgvFr.setVisibility(8);
        this.imgvKo.setVisibility(8);
        this.imgvRu.setVisibility(8);
    }

    private void checkZhTw() {
        this.mLanguage = "zh_TW";
        this.ivChina.setVisibility(8);
        this.ivEnglish.setVisibility(8);
        this.imgvZhTw.setVisibility(0);
        this.imgvJa.setVisibility(8);
        this.imgvDe.setVisibility(8);
        this.imgvPt.setVisibility(8);
        this.imgvEs.setVisibility(8);
        this.imgvFr.setVisibility(8);
        this.imgvKo.setVisibility(8);
        this.imgvRu.setVisibility(8);
    }

    private void checkJa() {
        this.mLanguage = "ja";
        this.ivChina.setVisibility(8);
        this.ivEnglish.setVisibility(8);
        this.imgvZhTw.setVisibility(8);
        this.imgvJa.setVisibility(0);
        this.imgvDe.setVisibility(8);
        this.imgvPt.setVisibility(8);
        this.imgvEs.setVisibility(8);
        this.imgvFr.setVisibility(8);
        this.imgvKo.setVisibility(8);
        this.imgvRu.setVisibility(8);
    }

    private void checkDe() {
        this.mLanguage = "de";
        this.ivChina.setVisibility(8);
        this.ivEnglish.setVisibility(8);
        this.imgvZhTw.setVisibility(8);
        this.imgvJa.setVisibility(8);
        this.imgvDe.setVisibility(0);
        this.imgvPt.setVisibility(8);
        this.imgvEs.setVisibility(8);
        this.imgvFr.setVisibility(8);
        this.imgvKo.setVisibility(8);
        this.imgvRu.setVisibility(8);
    }

    private void checkEs() {
        this.mLanguage = "es";
        this.ivChina.setVisibility(8);
        this.ivEnglish.setVisibility(8);
        this.imgvZhTw.setVisibility(8);
        this.imgvJa.setVisibility(8);
        this.imgvDe.setVisibility(8);
        this.imgvPt.setVisibility(8);
        this.imgvEs.setVisibility(0);
        this.imgvFr.setVisibility(8);
        this.imgvKo.setVisibility(8);
        this.imgvRu.setVisibility(8);
    }

    private void checkPt() {
        this.mLanguage = "pt";
        this.ivChina.setVisibility(8);
        this.ivEnglish.setVisibility(8);
        this.imgvZhTw.setVisibility(8);
        this.imgvJa.setVisibility(8);
        this.imgvDe.setVisibility(8);
        this.imgvPt.setVisibility(0);
        this.imgvEs.setVisibility(8);
        this.imgvFr.setVisibility(8);
        this.imgvKo.setVisibility(8);
        this.imgvRu.setVisibility(8);
    }

    private void checkFr() {
        this.mLanguage = "fr";
        this.ivChina.setVisibility(8);
        this.ivEnglish.setVisibility(8);
        this.imgvZhTw.setVisibility(8);
        this.imgvJa.setVisibility(8);
        this.imgvDe.setVisibility(8);
        this.imgvPt.setVisibility(8);
        this.imgvEs.setVisibility(8);
        this.imgvFr.setVisibility(0);
        this.imgvKo.setVisibility(8);
        this.imgvRu.setVisibility(8);
    }

    private void checkKo() {
        this.mLanguage = "ko";
        this.ivChina.setVisibility(8);
        this.ivEnglish.setVisibility(8);
        this.imgvZhTw.setVisibility(8);
        this.imgvJa.setVisibility(8);
        this.imgvDe.setVisibility(8);
        this.imgvPt.setVisibility(8);
        this.imgvEs.setVisibility(8);
        this.imgvFr.setVisibility(8);
        this.imgvKo.setVisibility(0);
        this.imgvRu.setVisibility(8);
    }

    private void checkRu() {
        this.mLanguage = "ru";
        this.ivChina.setVisibility(8);
        this.ivEnglish.setVisibility(8);
        this.imgvZhTw.setVisibility(8);
        this.imgvJa.setVisibility(8);
        this.imgvDe.setVisibility(8);
        this.imgvPt.setVisibility(8);
        this.imgvEs.setVisibility(8);
        this.imgvFr.setVisibility(8);
        this.imgvKo.setVisibility(8);
        this.imgvRu.setVisibility(0);
    }

    private String getLocalLanguage() {
        return getResources().getConfiguration().locale.getLanguage();
    }

    public void saveLanguage() {
        SPUtils.put(this, C.SP.LANGUAGE, this.mLanguage);
    }

    public boolean isChangedLanguage() {
        return !this.mLanguage.equalsIgnoreCase(LanguageUtil.getSaveLanguage(this));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_forward) {
            back();
            return;
        }
        switch (id) {
            case R.id.layout_china /* 2131296464 */:
                checkZh();
                break;
            case R.id.layout_english /* 2131296465 */:
                checkEn();
                break;
            default:
                switch (id) {
                    case R.id.rltlao_de /* 2131296571 */:
                        checkDe();
                        break;
                    case R.id.rltlao_es /* 2131296572 */:
                        checkEs();
                        break;
                    case R.id.rltlao_fr /* 2131296573 */:
                        checkFr();
                        break;
                    case R.id.rltlao_ja /* 2131296574 */:
                        checkJa();
                        break;
                    case R.id.rltlao_ko /* 2131296575 */:
                        checkKo();
                        break;
                    case R.id.rltlao_pt /* 2131296576 */:
                        checkPt();
                        break;
                    case R.id.rltlao_ru /* 2131296577 */:
                        checkRu();
                        break;
                    case R.id.rltlao_zh_tw /* 2131296578 */:
                        checkZhTw();
                        break;
                }
                break;
        }
    }
}
