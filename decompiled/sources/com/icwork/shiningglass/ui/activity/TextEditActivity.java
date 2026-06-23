package com.icwork.shiningglass.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cdbwsoft.library.ble.BleDevice;
import com.cdbwsoft.library.ble.BleManager;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.App;
import com.icwork.shiningglass.base.BaseActivity;
import com.icwork.shiningglass.base.DataManager;
import com.icwork.shiningglass.base.app.C;
import com.icwork.shiningglass.base.app.SoundManager;
import com.icwork.shiningglass.base.music.MusicPlayer;
import com.icwork.shiningglass.ble.HeartBeatDevice;
import com.icwork.shiningglass.ble.HeartBeatDeviceFactory;
import com.icwork.shiningglass.dao.DaoSession;
import com.icwork.shiningglass.dao.HistoryDataDao;
import com.icwork.shiningglass.model.bean.HistoryData;
import com.icwork.shiningglass.model.bean.TextData;
import com.icwork.shiningglass.model.data.Agreement;
import com.icwork.shiningglass.model.data.Text1456;
import com.icwork.shiningglass.model.data.TextAgreement;
import com.icwork.shiningglass.model.data.TextIconData;
import com.icwork.shiningglass.ui.adapter.HistoryListAdapter;
import com.icwork.shiningglass.ui.adapter.LedViewAdapter;
import com.icwork.shiningglass.ui.adapter.TextImageIconAdapter;
import com.icwork.shiningglass.ui.adapter.baseadapter.RecyclerAdapter;
import com.icwork.shiningglass.ui.utils.ClickFilter;
import com.icwork.shiningglass.ui.utils.DensityUtil;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.utils.SoftKeyboardStateHelper;
import com.icwork.shiningglass.ui.widget.LedTextView;
import com.icwork.shiningglass.ui.widget.MyScrollView;
import com.icwork.shiningglass.ui.widget.RectView;
import com.icwork.shiningglass.ui.widget.colorpickerview.ColorPickerView;
import com.icwork.shiningglass.ui.widget.colorpickerview.OnColorChangeListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes.dex */
public class TextEditActivity extends BaseActivity implements View.OnClickListener {
    private AnimationDrawable animMode;
    private BleManager bleManager;
    private CheckBox cbSpeed;
    private CheckBox cbTextBgColor;
    private CheckBox cbTextColor;
    private ColorPickerView cpvColor1;
    private ColorPickerView cpvColor2;
    private ColorPickerView cpvColor3;
    private int curAnimType;
    private int[] curDataColorArray;
    private byte[] curLedData;
    private int curTextBgColorB;
    private int curTextBgColorG;
    private int curTextBgColorR;
    private int curTextColorB;
    private int curTextColorG;
    private int curTextColorR;
    private DaoSession daoSession;
    private EditText etTextInput;
    private HistoryDataDao historyDataDao;
    private HistoryListAdapter historyListAdapter;
    private ImageButton ibtnSend;
    private boolean isShowAddText;
    private boolean isShowHistorical;
    private ImageView ivAminMode;
    private ImageView ivBack;
    private ImageView ivForward;
    private ImageView ivHist;
    private ImageView ivModeNext;
    private ImageView ivModePrevious;
    private ImageView ivOk;
    private RelativeLayout layoutTitlebar;
    private LedTextView ledPreView;
    private LedViewAdapter ledViewAdapter;
    private LinearLayout llBottom;
    private LinearLayout llLedViewPreview;
    private RelativeLayout llTextAdd;
    private RelativeLayout llTextColor;
    private RelativeLayout llTextColorBg;
    private RelativeLayout llTextEdit;
    private LinearLayout llTextEdit1;
    private LinearLayout llTextPreview;
    private RelativeLayout llTextSpeed;
    private MusicPlayer musicPlayer;
    private RadioButton rbGradientSelect1;
    private RadioButton rbGradientSelect2;
    private RadioButton rbGradientSelect3;
    private RadioButton rbTextSelect1;
    private RadioButton rbTextSelect2;
    private RadioButton rbTextSelect3;
    private RadioButton rbTextSelect4;
    private RadioGroup rgTextBgColor;
    private RadioGroup rgTextColor;
    private RelativeLayout rlAnim;
    private RelativeLayout rootView;
    private ListView rvHistoryList;
    private RecyclerView rvImageIconList;
    private RecyclerView rvLedViewList;
    private RectView rv_color;
    private SeekBar sbMoveSpeed;
    private MyScrollView scrollViewText;
    private TextAgreement textAgreement;
    private TextImageIconAdapter textImageIconAdapter;
    private TextView tvTitle;
    private View viewLed;
    private View viewTop;
    private boolean sendDataEnable = false;
    private List<BleDevice> deviceList = new ArrayList();
    private List<TextData> textList = new ArrayList();
    private List<byte[]> iconDataList = new ArrayList();
    private int curAminMode = 0;
    private List<Integer> imageList = new ArrayList();
    private int curTextBgColor = -15532288;
    private int gradientMode = 0;
    private int bgColorMode = 0;
    Handler mHandler = new Handler();
    private int curAddTextColor = -15532288;
    private LinkedList<HistoryData> historyDataList = new LinkedList<>();
    private int curSpeed = 50;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final AtomicBoolean isActivityAlive = new AtomicBoolean(true);

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected int getLayoutId() {
        return R.layout.activity_text_edit2;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initView() {
        findView();
        this.tvTitle.setText(getResources().getText(R.string.main_text));
        this.gradientMode = DataManager.getInstance().getTextColorMode();
        this.bgColorMode = DataManager.getInstance().getTextColorBgMode();
        LogUtil.d("gradientMode:" + this.gradientMode + " bgColorMode:" + this.bgColorMode);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(0);
        this.rvImageIconList.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(0);
        this.rvLedViewList.setLayoutManager(linearLayoutManager2);
        int[] data = getData(R.array.text_input_expression);
        if (data != null) {
            for (int i : data) {
                this.imageList.add(Integer.valueOf(i));
            }
        }
        TextImageIconAdapter textImageIconAdapter = new TextImageIconAdapter(this, R.layout.item_image, this.imageList);
        this.textImageIconAdapter = textImageIconAdapter;
        this.rvImageIconList.setAdapter(textImageIconAdapter);
        LedViewAdapter ledViewAdapter = new LedViewAdapter(this, R.layout.item_ledview, this.textList);
        this.ledViewAdapter = ledViewAdapter;
        this.rvLedViewList.setAdapter(ledViewAdapter);
        this.ledViewAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.1
            @Override // com.icwork.shiningglass.ui.adapter.baseadapter.RecyclerAdapter.OnItemClickListener
            public void onItemClick(ViewGroup viewGroup, View view, Object obj, int i2) {
                TextEditActivity.this.showKey();
            }
        });
        this.cpvColor1.setTouch(false);
        this.cpvColor2.setTouch(false);
        new SoftKeyboardStateHelper(this.llTextAdd).addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.2
            @Override // com.icwork.shiningglass.ui.utils.SoftKeyboardStateHelper.SoftKeyboardStateListener
            public void onSoftKeyboardOpened(int i2) {
                LogUtil.d("键盘弹出");
            }

            @Override // com.icwork.shiningglass.ui.utils.SoftKeyboardStateHelper.SoftKeyboardStateListener
            public void onSoftKeyboardClosed() {
                LogUtil.d("键盘收起");
                TextEditActivity.this.hideKey();
            }
        });
        getDbData();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        EditText editText;
        super.onWindowFocusChanged(z);
        if (!z || (editText = this.etTextInput) == null) {
            return;
        }
        editText.requestFocus();
    }

    private void findView() {
        this.ivBack = (ImageView) findViewById(R.id.iv_back);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.ivForward = (ImageView) findViewById(R.id.iv_forward);
        this.layoutTitlebar = (RelativeLayout) findViewById(R.id.layout_titlebar);
        this.llTextEdit = (RelativeLayout) findViewById(R.id.ll_text_edit);
        this.llTextColor = (RelativeLayout) findViewById(R.id.ll_text_color);
        this.llTextColorBg = (RelativeLayout) findViewById(R.id.ll_text_color_bg);
        this.sbMoveSpeed = (SeekBar) findViewById(R.id.sb_move_light);
        this.llTextSpeed = (RelativeLayout) findViewById(R.id.ll_text_speed);
        this.ivModePrevious = (ImageView) findViewById(R.id.iv_mode_previous);
        this.ivAminMode = (ImageView) findViewById(R.id.iv_amin_mode);
        this.ivModeNext = (ImageView) findViewById(R.id.iv_mode_next);
        this.ivHist = (ImageView) findViewById(R.id.iv_hist);
        this.rvImageIconList = (RecyclerView) findViewById(R.id.rv_image_icon_list);
        this.rv_color = (RectView) findViewById(R.id.rv_color);
        this.rootView = (RelativeLayout) findViewById(R.id.root_view);
        this.llLedViewPreview = (LinearLayout) findViewById(R.id.ll_ledView_preview);
        this.rbTextSelect1 = (RadioButton) findViewById(R.id.rb_text_select1);
        this.rbTextSelect2 = (RadioButton) findViewById(R.id.rb_text_select2);
        this.rbTextSelect3 = (RadioButton) findViewById(R.id.rb_text_select3);
        this.rbTextSelect4 = (RadioButton) findViewById(R.id.rb_text_select4);
        this.rbGradientSelect1 = (RadioButton) findViewById(R.id.rb_gradient_select1);
        this.rbGradientSelect2 = (RadioButton) findViewById(R.id.rb_gradient_select2);
        this.rbGradientSelect3 = (RadioButton) findViewById(R.id.rb_gradient_select3);
        this.llTextPreview = (LinearLayout) findViewById(R.id.ll_text_preview);
        this.ibtnSend = (ImageButton) findViewById(R.id.ibtn_send);
        this.ivOk = (ImageView) findViewById(R.id.iv_ok);
        this.llTextAdd = (RelativeLayout) findViewById(R.id.ll_text_add);
        this.cpvColor1 = (ColorPickerView) findViewById(R.id.cpv_color1);
        this.cpvColor2 = (ColorPickerView) findViewById(R.id.cpv_color2);
        this.cpvColor3 = (ColorPickerView) findViewById(R.id.cpv_color3);
        this.cbTextColor = (CheckBox) findViewById(R.id.cb_text_color);
        this.cbTextBgColor = (CheckBox) findViewById(R.id.cb_text_bg_color);
        this.cbSpeed = (CheckBox) findViewById(R.id.cb_speed);
        this.ledPreView = (LedTextView) findViewById(R.id.ltv_preview);
        this.rvLedViewList = (RecyclerView) findViewById(R.id.rv_ledview_list);
        this.llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        this.viewLed = findViewById(R.id.view_led);
        this.rgTextColor = (RadioGroup) findViewById(R.id.rg_text_clolor);
        this.rgTextBgColor = (RadioGroup) findViewById(R.id.rg_text_bg_clolor);
        this.llTextEdit1 = (LinearLayout) findViewById(R.id.ll_text_edit1);
        this.etTextInput = (EditText) findViewById(R.id.et_text_input);
        this.rvHistoryList = (ListView) findViewById(R.id.rv_history_list);
        this.scrollViewText = (MyScrollView) findViewById(R.id.scrollView_text);
        this.viewTop = findViewById(R.id.view_top);
        this.rlAnim = (RelativeLayout) findViewById(R.id.rl_anim);
        this.ivModePrevious.setOnClickListener(this);
        this.ivModeNext.setOnClickListener(this);
        this.ibtnSend.setOnClickListener(this);
        this.llTextEdit.setOnClickListener(this);
        this.ivBack.setOnClickListener(this);
        this.ivHist.setOnClickListener(this);
        this.llLedViewPreview.setOnClickListener(this);
        this.ivOk.setOnClickListener(this);
        this.rvLedViewList.setOnClickListener(this);
        findViewById(R.id.iv_go).setOnClickListener(this);
        this.ivForward.setOnClickListener(this);
        this.viewLed.setOnClickListener(this);
    }

    private void getDbData() {
        this.executorService.execute(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getDbData$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getDbData$1() {
        DaoSession daoSession = App.getDaoSession();
        this.daoSession = daoSession;
        HistoryDataDao historyDataDao = daoSession.getHistoryDataDao();
        this.historyDataDao = historyDataDao;
        final List<HistoryData> list = historyDataDao.queryBuilder().list();
        runOnUiThread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getDbData$0(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getDbData$0(List list) {
        HistoryData historyData;
        if (list != null) {
            setListViewHeight(list.size());
            this.historyDataList.clear();
            for (int size = list.size() - 1; size >= 0 && this.historyDataList.size() < 10; size--) {
                this.historyDataList.add((HistoryData) list.get(size));
            }
        }
        HistoryListAdapter historyListAdapter = new HistoryListAdapter(this.mActivity, this.historyDataList);
        this.historyListAdapter = historyListAdapter;
        this.rvHistoryList.setAdapter((ListAdapter) historyListAdapter);
        LinkedList<HistoryData> linkedList = this.historyDataList;
        if (linkedList == null || linkedList.size() <= 0 || (historyData = this.historyDataList.get(0)) == null) {
            return;
        }
        this.curLedData = historyData.getData();
        this.curDataColorArray = historyData.getColorArray();
        this.ledPreView.setTextData(historyData.getData(), historyData.convertArray(historyData.getColorList()), this.cbTextColor.isChecked(), this.cbTextBgColor.isChecked(), this.gradientMode, this.bgColorMode);
    }

    private int[] getData(int i) {
        TypedArray typedArrayObtainTypedArray = getResources().obtainTypedArray(i);
        int length = typedArrayObtainTypedArray.length();
        int[] iArr = new int[typedArrayObtainTypedArray.length()];
        for (int i2 = 0; i2 < length; i2++) {
            iArr[i2] = typedArrayObtainTypedArray.getResourceId(i2, 0);
        }
        typedArrayObtainTypedArray.recycle();
        return iArr;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initData() {
        BleManager bleManager = ConnectActivity.getBleManager();
        this.bleManager = bleManager;
        if (bleManager == null) {
            this.bleManager = App.getInstance().getBleManager(HeartBeatDevice.class, null, new HeartBeatDeviceFactory(App.getInstance()));
        }
        this.musicPlayer = ConnectActivity.getMusicPlayer();
        this.textAgreement = TextAgreement.getInstance();
        this.iconDataList = TextIconData.getIconArray();
        initLedView();
        setSeekBarClickable(false);
        this.cpvColor1.post(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.3
            @Override // java.lang.Runnable
            public void run() {
                TextEditActivity.this.cpvColor1.setPreviewColor();
            }
        });
        this.cpvColor2.post(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.4
            @Override // java.lang.Runnable
            public void run() {
                TextEditActivity.this.cpvColor2.setPreviewColor();
                TextEditActivity.this.initTextData();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSeekBarClickable(boolean z) {
        if (z) {
            this.sbMoveSpeed.setClickable(true);
            this.sbMoveSpeed.setEnabled(true);
            this.sbMoveSpeed.setSelected(true);
            this.sbMoveSpeed.setFocusable(true);
            return;
        }
        this.sbMoveSpeed.setClickable(false);
        this.sbMoveSpeed.setEnabled(false);
        this.sbMoveSpeed.setSelected(false);
        this.sbMoveSpeed.setFocusable(false);
    }

    private void initLedView() {
        this.ledPreView.setPointMargin(0);
        this.ledPreView.removeAllViews();
        this.ledPreView.init(36, 12);
        this.ledPreView.setSelectedColor(Color.rgb(120, 141, 128));
        this.ledPreView.setLayerType(1, null);
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void bindListener() {
        this.viewTop.setOnTouchListener(new View.OnTouchListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.5
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    motionEvent.getY();
                } else if (action == 1) {
                    float y = motionEvent.getY();
                    LogUtil.d("上滑" + (y - 0.0f));
                    LogUtil.d("y1:0.0  y2:" + y);
                    TextEditActivity.this.hideHistorical();
                } else if (action == 2) {
                    motionEvent.getY();
                }
                return true;
            }
        });
        this.textImageIconAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.6
            @Override // com.icwork.shiningglass.ui.adapter.baseadapter.RecyclerAdapter.OnItemClickListener
            public void onItemClick(ViewGroup viewGroup, View view, Object obj, int i) {
                LogUtil.d("onItem:" + i);
                TextEditActivity.this.setIconData((byte[]) TextEditActivity.this.iconDataList.get(i));
            }
        });
        this.cpvColor1.setOnColorChangeListener(new OnColorChangeListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.7
            @Override // com.icwork.shiningglass.ui.widget.colorpickerview.OnColorChangeListener
            public void colorChanged(int i, int i2, int i3, int i4, float f) {
                if (i == -515) {
                    return;
                }
                LogUtil.d("保存的x：" + f);
                DataManager.getInstance().setTextColorProgress(f);
                TextEditActivity.this.curTextColorR = i2;
                TextEditActivity.this.curTextColorG = i3;
                TextEditActivity.this.curTextColorB = i4;
                TextEditActivity.this.ledPreView.setSelectedColor(i);
                TextEditActivity.this.gradientMode = 0;
                TextEditActivity.this.cpvColor1.setProgressIconVisible(true);
                TextEditActivity textEditActivity = TextEditActivity.this;
                textEditActivity.initDataAnim(textEditActivity.curLedData, TextEditActivity.this.curAnimType);
                TextEditActivity.this.rgTextColor.clearCheck();
                TextEditActivity textEditActivity2 = TextEditActivity.this;
                textEditActivity2.sendTextColor((byte) i2, (byte) i3, (byte) i4, textEditActivity2.cbTextColor.isChecked());
            }
        });
        this.cpvColor2.setOnColorChangeListener(new OnColorChangeListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.8
            @Override // com.icwork.shiningglass.ui.widget.colorpickerview.OnColorChangeListener
            public void colorChanged(int i, int i2, int i3, int i4, float f) {
                if (i == -515) {
                    return;
                }
                DataManager.getInstance().setTextColorBgProgress(f);
                LogUtil.d("转换前：" + i2 + " " + i3 + " " + i4);
                double d = ((double) i2) * 0.5d;
                double d2 = ((double) i3) * 0.5d;
                double d3 = ((double) i4) * 0.5d;
                LogUtil.d("转换后：" + d + " " + d2 + " " + d3);
                TextEditActivity.this.curTextBgColorR = (int) d;
                TextEditActivity.this.curTextBgColorG = (int) d2;
                TextEditActivity.this.curTextBgColorB = (int) d3;
                if (TextEditActivity.this.cbTextBgColor.isChecked()) {
                    TextEditActivity.this.ledPreView.setBgColor(Color.rgb(TextEditActivity.this.curTextBgColorR, TextEditActivity.this.curTextBgColorG, TextEditActivity.this.curTextBgColorB));
                } else {
                    TextEditActivity.this.ledPreView.setBgColor(0);
                }
                TextEditActivity.this.bgColorMode = 0;
                TextEditActivity textEditActivity = TextEditActivity.this;
                textEditActivity.curTextBgColor = Color.rgb(textEditActivity.curTextBgColorR, TextEditActivity.this.curTextBgColorG, TextEditActivity.this.curTextBgColorB);
                TextEditActivity.this.cpvColor2.setProgressIconVisible(true);
                TextEditActivity textEditActivity2 = TextEditActivity.this;
                textEditActivity2.initDataAnim(textEditActivity2.curLedData, TextEditActivity.this.curAnimType);
                TextEditActivity.this.rgTextBgColor.clearCheck();
                TextEditActivity textEditActivity3 = TextEditActivity.this;
                textEditActivity3.sendTextBgColor((byte) textEditActivity3.curTextBgColorR, (byte) TextEditActivity.this.curTextBgColorG, (byte) TextEditActivity.this.curTextBgColorB, TextEditActivity.this.cbTextBgColor.isChecked());
            }
        });
        this.cpvColor3.setOnColorChangeListener(new OnColorChangeListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.9
            @Override // com.icwork.shiningglass.ui.widget.colorpickerview.OnColorChangeListener
            public void colorChanged(int i, int i2, int i3, int i4, float f) {
                if (i == -515) {
                    return;
                }
                LogUtil.d("设置颜色3：" + i);
                TextEditActivity.this.rv_color.setViewBackground(i);
                TextEditActivity.this.curAddTextColor = i;
            }
        });
        this.cbTextColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.10
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                DataManager.getInstance().setTextColorEnable(z);
                if (z) {
                    if (TextEditActivity.this.sendDataEnable) {
                        SoundManager.getInstance().textOn();
                    }
                    TextEditActivity.this.cpvColor1.setAlpha(1.0f);
                    TextEditActivity.this.rgTextColor.setAlpha(1.0f);
                    TextEditActivity.this.cpvColor1.setTouch(true);
                    TextEditActivity.this.rbTextSelect1.setClickable(true);
                    TextEditActivity.this.rbTextSelect2.setClickable(true);
                    TextEditActivity.this.rbTextSelect3.setClickable(true);
                    TextEditActivity.this.rbTextSelect4.setClickable(true);
                } else {
                    if (TextEditActivity.this.sendDataEnable) {
                        SoundManager.getInstance().textOff();
                    }
                    TextEditActivity.this.cpvColor1.setAlpha(0.6f);
                    TextEditActivity.this.rgTextColor.setAlpha(0.6f);
                    TextEditActivity.this.cpvColor1.setTouch(false);
                    TextEditActivity.this.rbTextSelect1.setClickable(false);
                    TextEditActivity.this.rbTextSelect2.setClickable(false);
                    TextEditActivity.this.rbTextSelect3.setClickable(false);
                    TextEditActivity.this.rbTextSelect4.setClickable(false);
                }
                TextEditActivity textEditActivity = TextEditActivity.this;
                textEditActivity.initDataAnim(textEditActivity.curLedData, TextEditActivity.this.curAnimType);
                int i = TextEditActivity.this.gradientMode;
                if (i == 0) {
                    TextEditActivity textEditActivity2 = TextEditActivity.this;
                    textEditActivity2.sendTextColor((byte) textEditActivity2.curTextColorR, (byte) TextEditActivity.this.curTextColorG, (byte) TextEditActivity.this.curTextColorB, TextEditActivity.this.cbTextColor.isChecked());
                    return;
                }
                if (i == 1) {
                    TextEditActivity.this.sendDefaultMode(0, z);
                    return;
                }
                if (i == 2) {
                    TextEditActivity.this.sendDefaultMode(1, z);
                } else if (i == 3) {
                    TextEditActivity.this.sendDefaultMode(2, z);
                } else {
                    if (i != 4) {
                        return;
                    }
                    TextEditActivity.this.sendDefaultMode(3, z);
                }
            }
        });
        this.cbTextBgColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.11
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                DataManager.getInstance().setTextColorBgEnable(z);
                LogUtil.d("isChecked:" + z);
                if (z) {
                    if (TextEditActivity.this.sendDataEnable) {
                        SoundManager.getInstance().textOn();
                    }
                    TextEditActivity.this.cpvColor2.setAlpha(1.0f);
                    TextEditActivity.this.rgTextBgColor.setAlpha(1.0f);
                    TextEditActivity.this.cpvColor2.setTouch(true);
                    TextEditActivity.this.rbGradientSelect1.setClickable(true);
                    TextEditActivity.this.rbGradientSelect2.setClickable(true);
                    TextEditActivity.this.rbGradientSelect3.setClickable(true);
                } else {
                    if (TextEditActivity.this.sendDataEnable) {
                        SoundManager.getInstance().textOff();
                    }
                    TextEditActivity.this.cpvColor2.setAlpha(0.6f);
                    TextEditActivity.this.rgTextBgColor.setAlpha(0.6f);
                    TextEditActivity.this.cpvColor2.setTouch(false);
                    TextEditActivity.this.rbGradientSelect1.setClickable(false);
                    TextEditActivity.this.rbGradientSelect2.setClickable(false);
                    TextEditActivity.this.rbGradientSelect3.setClickable(false);
                    TextEditActivity.this.ledPreView.setBgColor(0);
                }
                if (TextEditActivity.this.cbTextBgColor.isChecked()) {
                    TextEditActivity.this.ledPreView.setBgColor(TextEditActivity.this.curTextBgColor);
                } else {
                    TextEditActivity.this.ledPreView.setBgColor(0);
                }
                TextEditActivity textEditActivity = TextEditActivity.this;
                textEditActivity.initDataAnim(textEditActivity.curLedData, TextEditActivity.this.curAnimType);
                LogUtil.d("bgColorMode:" + TextEditActivity.this.bgColorMode);
                int i = TextEditActivity.this.bgColorMode;
                if (i == 0) {
                    TextEditActivity textEditActivity2 = TextEditActivity.this;
                    textEditActivity2.sendTextBgColor((byte) textEditActivity2.curTextBgColorR, (byte) TextEditActivity.this.curTextBgColorG, (byte) TextEditActivity.this.curTextBgColorB, TextEditActivity.this.cbTextBgColor.isChecked());
                } else if (i == 1) {
                    TextEditActivity.this.sendDefaultMode(4, z);
                } else if (i == 2) {
                    TextEditActivity.this.sendDefaultMode(5, z);
                } else {
                    if (i != 3) {
                        return;
                    }
                    TextEditActivity.this.sendDefaultMode(6, z);
                }
            }
        });
        this.cbSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.12
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    if (TextEditActivity.this.sendDataEnable) {
                        SoundManager.getInstance().textOn();
                    }
                    TextEditActivity.this.curAminMode = 1;
                    TextEditActivity.this.curAnimType = 1;
                    TextEditActivity.this.rlAnim.setAlpha(1.0f);
                    TextEditActivity.this.sbMoveSpeed.setAlpha(1.0f);
                    TextEditActivity.this.ivModeNext.setClickable(true);
                    TextEditActivity.this.ivModePrevious.setClickable(true);
                    TextEditActivity.this.setSeekBarClickable(true);
                    TextEditActivity.this.startLeftAnim();
                    TextEditActivity textEditActivity = TextEditActivity.this;
                    textEditActivity.initDataAnim(textEditActivity.curLedData, TextEditActivity.this.curAnimType);
                    TextEditActivity textEditActivity2 = TextEditActivity.this;
                    textEditActivity2.sendMode(textEditActivity2.curAnimType);
                } else {
                    if (TextEditActivity.this.sendDataEnable) {
                        SoundManager.getInstance().textOff();
                    }
                    TextEditActivity.this.rlAnim.setAlpha(0.6f);
                    TextEditActivity.this.sbMoveSpeed.setAlpha(0.6f);
                    TextEditActivity.this.ivModeNext.setClickable(false);
                    TextEditActivity.this.ivModePrevious.setClickable(false);
                    TextEditActivity.this.setSeekBarClickable(false);
                    TextEditActivity.this.clearAminMode();
                    TextEditActivity.this.ivAminMode.setImageResource(R.mipmap.anim_magic_left_1);
                    TextEditActivity.this.curAminMode = 0;
                    TextEditActivity.this.curAnimType = 0;
                    TextEditActivity.this.ledPreView.clearAnimation();
                    TextEditActivity.this.mHandler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.12.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TextEditActivity.this.initDataAnim(TextEditActivity.this.curLedData, 0);
                        }
                    }, 100L);
                    TextEditActivity textEditActivity3 = TextEditActivity.this;
                    textEditActivity3.sendMode(textEditActivity3.curAnimType);
                }
                DataManager.getInstance().setCurTextMode(TextEditActivity.this.curAnimType);
            }
        });
        this.rgTextColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.13
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!TextEditActivity.this.rbTextSelect1.isChecked() && !TextEditActivity.this.rbTextSelect2.isChecked() && !TextEditActivity.this.rbTextSelect3.isChecked() && !TextEditActivity.this.rbTextSelect4.isChecked()) {
                    LogUtil.d("都没选择");
                    return;
                }
                switch (i) {
                    case R.id.rb_text_select1 /* 2131296543 */:
                        TextEditActivity.this.gradientMode = 1;
                        TextEditActivity.this.cpvColor1.setProgressIconVisible(false);
                        TextEditActivity textEditActivity = TextEditActivity.this;
                        textEditActivity.initDataAnim(textEditActivity.curLedData, TextEditActivity.this.curAnimType);
                        TextEditActivity textEditActivity2 = TextEditActivity.this;
                        textEditActivity2.sendDefaultMode(0, textEditActivity2.cbTextColor.isChecked());
                        break;
                    case R.id.rb_text_select2 /* 2131296544 */:
                        TextEditActivity.this.gradientMode = 2;
                        TextEditActivity.this.cpvColor1.setProgressIconVisible(false);
                        TextEditActivity textEditActivity3 = TextEditActivity.this;
                        textEditActivity3.initDataAnim(textEditActivity3.curLedData, TextEditActivity.this.curAnimType);
                        TextEditActivity textEditActivity4 = TextEditActivity.this;
                        textEditActivity4.sendDefaultMode(1, textEditActivity4.cbTextColor.isChecked());
                        break;
                    case R.id.rb_text_select3 /* 2131296545 */:
                        TextEditActivity.this.gradientMode = 3;
                        TextEditActivity.this.cpvColor1.setProgressIconVisible(false);
                        TextEditActivity textEditActivity5 = TextEditActivity.this;
                        textEditActivity5.initDataAnim(textEditActivity5.curLedData, TextEditActivity.this.curAnimType);
                        TextEditActivity textEditActivity6 = TextEditActivity.this;
                        textEditActivity6.sendDefaultMode(2, textEditActivity6.cbTextColor.isChecked());
                        break;
                    case R.id.rb_text_select4 /* 2131296546 */:
                        TextEditActivity.this.gradientMode = 4;
                        TextEditActivity.this.cpvColor1.setProgressIconVisible(false);
                        TextEditActivity textEditActivity7 = TextEditActivity.this;
                        textEditActivity7.initDataAnim(textEditActivity7.curLedData, TextEditActivity.this.curAnimType);
                        TextEditActivity textEditActivity8 = TextEditActivity.this;
                        textEditActivity8.sendDefaultMode(3, textEditActivity8.cbTextColor.isChecked());
                        break;
                }
                boolean unused = TextEditActivity.this.sendDataEnable;
            }
        });
        this.rgTextBgColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.14
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (TextEditActivity.this.rbGradientSelect1.isChecked() || TextEditActivity.this.rbGradientSelect2.isChecked() || TextEditActivity.this.rbGradientSelect3.isChecked()) {
                    switch (i) {
                        case R.id.rb_gradient_select1 /* 2131296540 */:
                            TextEditActivity.this.bgColorMode = 1;
                            TextEditActivity.this.cpvColor2.setProgressIconVisible(false);
                            TextEditActivity textEditActivity = TextEditActivity.this;
                            textEditActivity.initDataAnim(textEditActivity.curLedData, TextEditActivity.this.curAnimType);
                            TextEditActivity textEditActivity2 = TextEditActivity.this;
                            textEditActivity2.sendDefaultMode(4, textEditActivity2.cbTextBgColor.isChecked());
                            break;
                        case R.id.rb_gradient_select2 /* 2131296541 */:
                            TextEditActivity.this.bgColorMode = 2;
                            TextEditActivity.this.cpvColor2.setProgressIconVisible(false);
                            TextEditActivity textEditActivity3 = TextEditActivity.this;
                            textEditActivity3.initDataAnim(textEditActivity3.curLedData, TextEditActivity.this.curAnimType);
                            TextEditActivity textEditActivity4 = TextEditActivity.this;
                            textEditActivity4.sendDefaultMode(5, textEditActivity4.cbTextBgColor.isChecked());
                            break;
                        case R.id.rb_gradient_select3 /* 2131296542 */:
                            TextEditActivity.this.bgColorMode = 3;
                            TextEditActivity.this.cpvColor2.setProgressIconVisible(false);
                            TextEditActivity textEditActivity5 = TextEditActivity.this;
                            textEditActivity5.initDataAnim(textEditActivity5.curLedData, TextEditActivity.this.curAnimType);
                            TextEditActivity textEditActivity6 = TextEditActivity.this;
                            textEditActivity6.sendDefaultMode(6, textEditActivity6.cbTextBgColor.isChecked());
                            break;
                    }
                    boolean unused = TextEditActivity.this.sendDataEnable;
                }
            }
        });
        this.rvHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.15
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                HistoryData historyData = (HistoryData) TextEditActivity.this.historyDataList.get(i);
                if (historyData != null) {
                    TextEditActivity.this.curLedData = historyData.getData();
                    TextEditActivity.this.curDataColorArray = historyData.convertArray(historyData.getColorList());
                    TextEditActivity.this.ledPreView.cancelTimerTask();
                    TextEditActivity.this.ledPreView.setTextData(TextEditActivity.this.curLedData, TextEditActivity.this.curDataColorArray, TextEditActivity.this.cbTextColor.isChecked(), TextEditActivity.this.cbTextBgColor.isChecked(), TextEditActivity.this.gradientMode, TextEditActivity.this.bgColorMode);
                }
            }
        });
        this.rvHistoryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.16
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                TextEditActivity.this.deleteText(i);
                return false;
            }
        });
        this.sbMoveSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.17
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextEditActivity.this.curSpeed = i;
                DataManager.getInstance().setCurSpeed(TextEditActivity.this.curSpeed);
                TextEditActivity textEditActivity = TextEditActivity.this;
                textEditActivity.sendSpeed(textEditActivity.curSpeed);
            }
        });
        this.etTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.18
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 4 && i != 5 && i != 6 && (keyEvent == null || 66 != keyEvent.getKeyCode() || keyEvent.getAction() != 0)) {
                    return false;
                }
                TextEditActivity.this.inputEnter();
                TextEditActivity.this.hideKey();
                return false;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLeftAnim() {
        clearAminMode();
        this.ivAminMode.setImageResource(R.drawable.anim_left);
        AnimationDrawable animationDrawable = (AnimationDrawable) this.ivAminMode.getDrawable();
        this.animMode = animationDrawable;
        animationDrawable.start();
    }

    private void startRightAnim() {
        clearAminMode();
        this.ivAminMode.setImageResource(R.drawable.anim_right);
        AnimationDrawable animationDrawable = (AnimationDrawable) this.ivAminMode.getDrawable();
        this.animMode = animationDrawable;
        animationDrawable.start();
    }

    private void startBlinkAnim() {
        clearAminMode();
        this.ivAminMode.setImageResource(R.drawable.anim_blink);
        AnimationDrawable animationDrawable = (AnimationDrawable) this.ivAminMode.getDrawable();
        this.animMode = animationDrawable;
        animationDrawable.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearAminMode() {
        AnimationDrawable animationDrawable = this.animMode;
        if (animationDrawable != null) {
            animationDrawable.stop();
            this.ivAminMode.clearAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void inputEnter() {
        String string = this.etTextInput.getText().toString();
        LogUtil.d("输入的文本：" + string);
        setText(string);
        this.etTextInput.setText("");
        viewLedUiUpdate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideHistorical() {
        if (this.sendDataEnable) {
            SoundManager.getInstance().textHistoryClose();
        }
        this.isShowHistorical = false;
        this.ivHist.setImageResource(R.mipmap.text_magic_input_bottom);
        this.rvHistoryList.setVisibility(8);
        this.scrollViewText.setScroll(true);
        this.viewTop.setVisibility(8);
    }

    private void showHistorical() {
        this.isShowHistorical = true;
        this.ivHist.setImageResource(R.mipmap.text_magic_input_top);
        this.rvHistoryList.setVisibility(0);
        this.scrollViewText.setScroll(false);
        this.viewTop.setVisibility(0);
    }

    private void showAddText() {
        this.isShowAddText = true;
        this.llTextPreview.setVisibility(8);
        this.ibtnSend.setVisibility(8);
        this.llTextAdd.setVisibility(0);
        this.llLedViewPreview.setVisibility(8);
        this.rvLedViewList.setVisibility(0);
        this.viewLed.setVisibility(0);
        this.ivForward.setImageResource(R.mipmap.text_magic_delete);
        this.ivForward.setVisibility(0);
        this.llBottom.setVisibility(0);
    }

    private void hideAddText() {
        this.isShowAddText = false;
        this.llTextAdd.setVisibility(8);
        this.llTextPreview.setVisibility(0);
        this.ibtnSend.setVisibility(0);
        this.llLedViewPreview.setVisibility(0);
        this.rvLedViewList.setVisibility(8);
        this.viewLed.setVisibility(8);
        this.ivForward.setVisibility(8);
        this.llBottom.setVisibility(8);
    }

    private void viewLedUiUpdate() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.textList.size(); i++) {
            byte[] data = this.textList.get(i).getData();
            if (data != null) {
                for (byte b : data) {
                    arrayList.add(Byte.valueOf(b));
                }
            }
        }
        if (arrayList.size() > 72) {
            this.viewLed.setVisibility(8);
        } else {
            this.viewLed.setVisibility(0);
        }
    }

    private void aminSwitchover(int i) {
        this.curAminMode = i;
        DataManager.getInstance().setCurTextMode(i);
        if (i == 1) {
            this.curAnimType = 1;
            startLeftAnim();
        } else if (i == 2) {
            this.curAnimType = 2;
            startRightAnim();
        } else {
            if (i != 3) {
                return;
            }
            this.curAnimType = 3;
            startBlinkAnim();
        }
    }

    private void sendTextModeCommand() {
        sendMode(this.curAnimType);
        this.ledPreView.cancelTimerTask();
        this.mHandler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.19
            @Override // java.lang.Runnable
            public void run() {
                TextEditActivity textEditActivity = TextEditActivity.this;
                textEditActivity.initDataAnim(textEditActivity.curLedData, TextEditActivity.this.curAnimType);
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initDataAnim(byte[] bArr, int i) {
        DataManager.getInstance().setTextColorMode(this.gradientMode);
        DataManager.getInstance().setTextColorBgMode(this.bgColorMode);
        this.ledPreView.cancelTimerTask();
        if (i == 1) {
            this.ledPreView.clearAnimation();
            this.ledPreView.setTextMarquee(bArr, this.curDataColorArray, this.cbTextColor.isChecked(), this.cbTextBgColor.isChecked(), this.gradientMode, this.bgColorMode);
        } else if (i == 2) {
            this.ledPreView.clearAnimation();
            this.ledPreView.setTextRight(bArr, this.curDataColorArray, this.cbTextColor.isChecked(), this.cbTextBgColor.isChecked(), this.gradientMode, this.bgColorMode);
        } else if (i == 3) {
            breatheAnim();
            this.ledPreView.setTextData(bArr, this.curDataColorArray, this.cbTextColor.isChecked(), this.cbTextBgColor.isChecked(), this.gradientMode, this.bgColorMode);
        } else {
            this.ledPreView.setTextData(bArr, this.curDataColorArray, this.cbTextColor.isChecked(), this.cbTextBgColor.isChecked(), this.gradientMode, this.bgColorMode);
        }
    }

    private void breatheAnim() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(800L);
        alphaAnimation.setRepeatCount(-1);
        alphaAnimation.setRepeatMode(2);
        this.ledPreView.startAnimation(alphaAnimation);
    }

    private void setText(final String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.executorService.execute(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setText$5(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setText$5(String str) {
        try {
            final ArrayList arrayList = new ArrayList();
            for (char c : str.toCharArray()) {
                String strValueOf = String.valueOf(c);
                byte[] stringBytes = Text1456.getStringBytes(strValueOf);
                TextData textData = new TextData();
                textData.setType(0);
                textData.setData(stringBytes);
                textData.setColor(this.curAddTextColor);
                textData.setWidthCount(stringBytes.length / 2);
                textData.setContent(strValueOf);
                arrayList.add(textData);
            }
            runOnUiThread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setText$2(arrayList);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setText$3();
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
            runOnUiThread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setText$4();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setText$2(List list) {
        if (this.isActivityAlive.get()) {
            this.textList.addAll(list);
            updateLedView(this.textList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setText$3() {
        this.isActivityAlive.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setText$4() {
        this.isActivityAlive.get();
    }

    @Override // com.icwork.shiningglass.base.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.isActivityAlive.set(false);
        this.textAgreement.clear();
        try {
            this.executorService.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIconData(byte[] bArr) {
        TextData textData = new TextData();
        textData.setType(1);
        textData.setData(bArr);
        textData.setWidthCount(12);
        textData.setColor(this.curAddTextColor);
        this.textList.add(textData);
        updateLedView(this.textList);
    }

    private void updateLedView(List<TextData> list) {
        this.ledViewAdapter.setList(list);
        this.ledViewAdapter.notifyDataSetChanged();
        this.rvLedViewList.scrollToPosition(this.ledViewAdapter.getItemCount() - 1);
        viewLedUiUpdate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showKey() {
        this.llBottom.setVisibility(0);
        showPopupWindow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideKey() {
        this.llBottom.setVisibility(8);
        this.etTextInput.setCursorVisible(true);
        hideSoftInput();
    }

    public void showPopupWindow() {
        this.etTextInput.requestFocus();
        ((InputMethodManager) getSystemService("input_method")).showSoftInput(this.etTextInput, 1);
    }

    public void hideSoftInput() {
        this.etTextInput.clearFocus();
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.etTextInput.getWindowToken(), 2);
    }

    private void saveLedData(final byte[] bArr, final int[] iArr) {
        this.executorService.execute(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$saveLedData$7(bArr, iArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveLedData$7(byte[] bArr, int[] iArr) {
        for (int i = 0; i < this.historyDataList.size(); i++) {
            if (Arrays.equals(this.historyDataList.get(i).getData(), bArr)) {
                return;
            }
        }
        HistoryData historyData = new HistoryData();
        historyData.setData(bArr);
        historyData.setColorList(iArr);
        this.historyDataList.addFirst(historyData);
        if (this.historyDataList.size() > 10) {
            deleteData(this.historyDataList.size() - 1);
        }
        App.getDaoSession().getHistoryDataDao().insert(historyData);
        runOnUiThread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$saveLedData$6();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveLedData$6() {
        setListViewHeight(this.historyDataList.size());
        this.historyListAdapter.setList(this.historyDataList);
        this.historyListAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteText(final int i) {
        new AlertDialog.Builder(this).setTitle(getString(R.string.reminder)).setMessage(R.string.delete_tip).setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.21
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(getString(R.string.btn_sure), new DialogInterface.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.20
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                TextEditActivity.this.deleteData(i);
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteData(int i) {
        try {
            this.historyListAdapter.setList(this.historyDataList);
            this.historyListAdapter.notifyDataSetChanged();
            this.historyDataDao.delete(this.historyDataList.get(i));
            setListViewHeight(this.historyDataList.size());
            this.historyDataList.remove(i);
            if (this.historyDataList.size() == 0) {
                this.ledPreView.cancelTimerTask();
                this.ledPreView.clearSelected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListViewHeight(final int i) {
        runOnUiThread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setListViewHeight$8(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setListViewHeight$8(int i) {
        if (i > 4) {
            ViewGroup.LayoutParams layoutParams = this.rvHistoryList.getLayoutParams();
            layoutParams.height = (int) DensityUtil.dp2px(this.mContext, 155.0f);
            this.rvHistoryList.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams2 = this.rvHistoryList.getLayoutParams();
            layoutParams2.height = -2;
            this.rvHistoryList.setLayoutParams(layoutParams2);
        }
    }

    public void sendSpeed(int i) {
        MusicPlayer musicPlayer = this.musicPlayer;
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
        }
        if (i == 0) {
            i = 1;
        }
        byte[] speed = Agreement.getSpeed(i);
        LogUtil.e("sendSpeed:" + ((int) speed[6]));
        this.bleManager.writeData(Agreement.getEncryptData(speed));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMode(int i) {
        int i2 = 1;
        if (i != 0) {
            if (i == 1) {
                i2 = 3;
            } else if (i == 2) {
                i2 = 4;
            } else if (i == 3) {
                i2 = 2;
            }
        }
        sendCommand(i2);
    }

    public void sendCommand(int i) {
        if (this.sendDataEnable) {
            MusicPlayer musicPlayer = this.musicPlayer;
            if (musicPlayer != null && musicPlayer.isPlaying()) {
                EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
            }
            this.bleManager.writeData(Agreement.getEncryptData(Agreement.getContentCommand(i)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDefaultMode(int i, boolean z) {
        LogUtil.d("发送命令：" + z);
        if (this.sendDataEnable) {
            MusicPlayer musicPlayer = this.musicPlayer;
            if (musicPlayer != null && musicPlayer.isPlaying()) {
                EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
            }
            this.bleManager.writeData(Agreement.getEncryptData(Agreement.getDefaultMode(i, z)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTextColor(byte b, byte b2, byte b3, boolean z) {
        if (this.sendDataEnable) {
            MusicPlayer musicPlayer = this.musicPlayer;
            if (musicPlayer != null && musicPlayer.isPlaying()) {
                EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
            }
            this.bleManager.writeData(Agreement.getEncryptData(Agreement.getTextColor(b, b2, b3, z)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTextBgColor(byte b, byte b2, byte b3, boolean z) {
        if (this.sendDataEnable) {
            MusicPlayer musicPlayer = this.musicPlayer;
            if (musicPlayer != null && musicPlayer.isPlaying()) {
                EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
            }
            this.bleManager.writeData(Agreement.getEncryptData(Agreement.getTextBgColor(b, b2, b3, z)));
        }
    }

    public void sendCotent(byte[] bArr, int[] iArr) {
        if (this.sendDataEnable) {
            MusicPlayer musicPlayer = this.musicPlayer;
            if (musicPlayer != null && musicPlayer.isPlaying()) {
                EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
            }
            List<BleDevice> deviceList = App.getAppData().getDeviceList();
            this.deviceList = deviceList;
            if (deviceList == null || deviceList.size() < 1) {
                return;
            }
            TextAgreement.TextAgreementListener textAgreementListener = new TextAgreement.TextAgreementListener() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.22
                @Override // com.icwork.shiningglass.model.data.TextAgreement.TextAgreementListener
                public void onFinishSend(BleDevice bleDevice) {
                    TextEditActivity textEditActivity = TextEditActivity.this;
                    textEditActivity.sendMode(textEditActivity.curAnimType);
                    TextEditActivity.this.mHandler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.22.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TextEditActivity.this.sendSpeed(TextEditActivity.this.curSpeed);
                        }
                    }, 150L);
                    TextEditActivity.this.dismissProgressDialog();
                }
            };
            showProgressDialog(this.mActivity, getString(R.string.send));
            for (int i = 0; i < this.deviceList.size(); i++) {
                this.textAgreement.sendTextTo1236(this.deviceList.get(i), bArr, iArr, textAgreementListener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initTextData() {
        boolean zIsTextColorEnable = DataManager.getInstance().isTextColorEnable();
        boolean zIsTextColorBgEnable = DataManager.getInstance().isTextColorBgEnable();
        int textColorMode = DataManager.getInstance().getTextColorMode();
        int textColorBgMode = DataManager.getInstance().getTextColorBgMode();
        float textColorProgress = DataManager.getInstance().getTextColorProgress();
        float textColorBgProgress = DataManager.getInstance().getTextColorBgProgress();
        int curSpeed = DataManager.getInstance().getCurSpeed();
        int curTextMode = DataManager.getInstance().getCurTextMode();
        this.curAminMode = curTextMode;
        this.curSpeed = curSpeed;
        LogUtil.d("curSpeed:" + curSpeed + " curTextMode:" + curTextMode + " textColorMode:" + textColorMode + " textColorBgMode:" + textColorBgMode + " textColorBgEnable:" + zIsTextColorBgEnable + " textColorEnable:" + zIsTextColorEnable);
        this.sbMoveSpeed.setProgress(curSpeed);
        if (curTextMode > 0) {
            this.cbSpeed.setChecked(true);
            aminSwitchover(curTextMode);
        } else {
            this.cbSpeed.setChecked(false);
        }
        this.cbTextColor.setChecked(zIsTextColorEnable);
        this.cbTextBgColor.setChecked(zIsTextColorBgEnable);
        if (textColorMode == 1) {
            this.rbTextSelect1.setChecked(true);
        } else if (textColorMode == 2) {
            this.rbTextSelect2.setChecked(true);
        } else if (textColorMode == 3) {
            this.rbTextSelect3.setChecked(true);
        } else if (textColorMode == 4) {
            this.rbTextSelect4.setChecked(true);
        } else {
            this.cpvColor1.setColorProgress(textColorProgress);
        }
        if (textColorBgMode == 1) {
            this.rbGradientSelect1.setChecked(true);
        } else if (textColorBgMode == 2) {
            this.rbGradientSelect2.setChecked(true);
        } else if (textColorBgMode == 3) {
            this.rbGradientSelect3.setChecked(true);
        } else {
            this.cpvColor2.setColorProgress(textColorBgProgress);
        }
        initDataAnim(this.curLedData, this.curAnimType);
        this.sendDataEnable = true;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (ClickFilter.filter()) {
            return;
        }
        switch (view.getId()) {
            case R.id.ibtn_send /* 2131296369 */:
                SoundManager.getInstance().textSend();
                saveLedData(this.curLedData, this.curDataColorArray);
                sendCotent(this.curLedData, this.curDataColorArray);
                break;
            case R.id.iv_back /* 2131296426 */:
                SoundManager.getInstance().textBack();
                if (this.isShowAddText) {
                    hideAddText();
                    hideKey();
                    this.textList.clear();
                    this.rvLedViewList.removeAllViews();
                    this.ivHist.setVisibility(0);
                } else {
                    finish();
                }
                break;
            case R.id.iv_forward /* 2131296441 */:
                List<TextData> list = this.textList;
                if (list != null && list.size() > 0) {
                    SoundManager.getInstance().textDelete();
                    List<TextData> list2 = this.textList;
                    list2.remove(list2.size() - 1);
                    this.ledViewAdapter.setList(this.textList);
                    this.ledViewAdapter.notifyDataSetChanged();
                    viewLedUiUpdate();
                    break;
                }
                break;
            case R.id.iv_go /* 2131296444 */:
                inputEnter();
                break;
            case R.id.iv_hist /* 2131296445 */:
                if (this.sendDataEnable) {
                    SoundManager.getInstance().textHistoryOpen();
                }
                if (this.isShowHistorical) {
                    hideHistorical();
                } else {
                    showHistorical();
                }
                break;
            case R.id.iv_mode_next /* 2131296449 */:
                if (this.cbSpeed.isChecked()) {
                    int i = this.curAminMode;
                    if (i <= 2) {
                        this.curAminMode = i + 1;
                    } else {
                        this.curAminMode = 1;
                    }
                    aminSwitchover(this.curAminMode);
                    sendTextModeCommand();
                    break;
                }
                break;
            case R.id.iv_mode_previous /* 2131296450 */:
                if (this.cbSpeed.isChecked()) {
                    int i2 = this.curAminMode;
                    if (i2 >= 2) {
                        this.curAminMode = i2 - 1;
                    } else {
                        this.curAminMode = 3;
                    }
                    aminSwitchover(this.curAminMode);
                    sendTextModeCommand();
                    break;
                }
                break;
            case R.id.iv_ok /* 2131296452 */:
                SoundManager.getInstance().textEditOk();
                this.ledPreView.cancelTimerTask();
                hideKey();
                List<TextData> list3 = this.textList;
                if (list3 != null && list3.size() > 0) {
                    ArrayList arrayList = new ArrayList();
                    for (int i3 = 0; i3 < this.textList.size(); i3++) {
                        byte[] data = this.textList.get(i3).getData();
                        if (data != null) {
                            for (byte b : data) {
                                arrayList.add(Byte.valueOf(b));
                            }
                        }
                    }
                    byte[] bArr = new byte[arrayList.size()];
                    for (int i4 = 0; i4 < arrayList.size(); i4++) {
                        bArr[i4] = ((Byte) arrayList.get(i4)).byteValue();
                    }
                    ArrayList arrayList2 = new ArrayList();
                    for (int i5 = 0; i5 < this.textList.size(); i5++) {
                        int color = this.textList.get(i5).getColor();
                        int widthCount = this.textList.get(i5).getWidthCount();
                        LogUtil.d("color:" + color + " count:" + widthCount);
                        for (int i6 = 0; i6 < widthCount; i6++) {
                            arrayList2.add(Integer.valueOf(color));
                        }
                    }
                    int[] iArr = new int[arrayList2.size()];
                    for (int i7 = 0; i7 < arrayList2.size(); i7++) {
                        iArr[i7] = ((Integer) arrayList2.get(i7)).intValue();
                    }
                    this.curLedData = bArr;
                    this.curDataColorArray = iArr;
                    hideAddText();
                    hideKey();
                    this.ivHist.setVisibility(0);
                    this.textList.clear();
                    this.rvLedViewList.removeAllViews();
                    initDataAnim(this.curLedData, this.curAnimType);
                    break;
                }
                break;
            case R.id.ll_ledView_preview /* 2131296486 */:
                if (this.sendDataEnable) {
                    SoundManager.getInstance().mainMenuSelet();
                }
                this.ivHist.setVisibility(4);
                this.isShowHistorical = false;
                this.ivHist.setImageResource(R.mipmap.text_magic_input_bottom);
                this.rvHistoryList.setVisibility(8);
                this.scrollViewText.setScroll(true);
                showAddText();
                this.cpvColor3.post(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.TextEditActivity.23
                    @Override // java.lang.Runnable
                    public void run() {
                        TextEditActivity.this.cpvColor3.setColorProgress(-1.0f);
                        TextEditActivity.this.showKey();
                    }
                });
                break;
            case R.id.view_led /* 2131296673 */:
                showKey();
                break;
        }
    }
}
