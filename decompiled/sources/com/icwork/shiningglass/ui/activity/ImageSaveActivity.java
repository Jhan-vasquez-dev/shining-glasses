package com.icwork.shiningglass.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.BaseActivity;
import com.icwork.shiningglass.base.app.MyTimeTask;
import com.icwork.shiningglass.model.data.ImageData;
import com.icwork.shiningglass.ui.fragment.AnimFragment;
import com.icwork.shiningglass.ui.fragment.ImageFragment;
import com.icwork.shiningglass.ui.utils.BitmapUtils;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.widget.ImageSaveLedView;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes.dex */
public class ImageSaveActivity extends BaseActivity {
    private FragmentManager fm;
    int index;
    ImageView ivAnim;
    ImageView ivGallery;
    ImageSaveLedView ledview_save;
    int[][] list;
    LinearLayout ll_ledView1;
    LinearLayout rl_bottom;
    private MyTimeTask task;
    private FragmentTransaction transaction;
    private int curSelect = 0;
    List<int[]> list1 = new ArrayList();
    int index22 = 12;
    final Handler handler = new Handler() { // from class: com.icwork.shiningglass.ui.activity.ImageSaveActivity.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1001) {
                if (ImageSaveActivity.this.index >= ImageSaveActivity.this.list1.size()) {
                    return;
                }
                ImageSaveActivity.this.ledview_save.setSelectImageData3(ImageSaveActivity.this.list1.get(ImageSaveActivity.this.index));
                ImageSaveActivity.this.handler.removeCallbacks(ImageSaveActivity.this.runnable);
                ImageSaveActivity.this.handler.postDelayed(ImageSaveActivity.this.runnable, 200L);
            }
            super.handleMessage(message);
        }
    };
    Runnable runnable = new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ImageSaveActivity.4
        @Override // java.lang.Runnable
        public void run() throws Throwable {
            LogUtil.d("保存后的图片路径：" + BitmapUtils.saveToLocalPNG(ImageSaveActivity.this.mContext, BitmapUtils.captureView(ImageSaveActivity.this.ll_ledView1), "diyImage"));
            ImageSaveActivity.this.index++;
        }
    };

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void bindListener() {
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected int getLayoutId() {
        return R.layout.activity_image_save;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initView() {
        this.ivAnim = (ImageView) findViewById(R.id.iv_anim);
        this.ivGallery = (ImageView) findViewById(R.id.iv_gallery);
        this.rl_bottom = (LinearLayout) findViewById(R.id.rl_bottom);
        this.ledview_save = (ImageSaveLedView) findViewById(R.id.ledview_save);
        this.ll_ledView1 = (LinearLayout) findViewById(R.id.ll_ledView1);
        this.ivAnim.setImageResource(R.mipmap.image_anim_selected);
        this.ivGallery.setImageResource(R.mipmap.image_gallery);
        this.ivGallery.setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.ImageSaveActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$initView$0(view);
            }
        });
        this.ivAnim.setOnClickListener(new View.OnClickListener() { // from class: com.icwork.shiningglass.ui.activity.ImageSaveActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$initView$1(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initView$0(View view) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.fm = supportFragmentManager;
        this.transaction = supportFragmentManager.beginTransaction();
        if (this.curSelect == 1) {
            return;
        }
        this.curSelect = 1;
        this.ivGallery.setImageResource(R.mipmap.image_gallery_selected);
        this.ivAnim.setImageResource(R.mipmap.image_anim);
        this.transaction.replace(R.id.fl_change, ImageFragment.newInstance(0));
        this.transaction.commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initView$1(View view) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.fm = supportFragmentManager;
        this.transaction = supportFragmentManager.beginTransaction();
        if (this.curSelect == 0) {
            return;
        }
        this.curSelect = 0;
        this.ivAnim.setImageResource(R.mipmap.image_anim_selected);
        this.ivGallery.setImageResource(R.mipmap.image_gallery);
        this.transaction.replace(R.id.fl_change, AnimFragment.newInstance(true));
        this.transaction.commit();
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initData() {
        initTab();
        this.ledview_save.setPointMargin(0);
        this.ledview_save.removeAllViews();
        this.ledview_save.init(48, 12);
        this.ledview_save.setLayerType(1, null);
        this.list1.add(ImageData.getImage000());
        this.list1.add(ImageData.getImage001());
        this.list1.add(ImageData.getImage002());
        this.list1.add(ImageData.getImage003());
        this.list1.add(ImageData.getImage004());
        this.list1.add(ImageData.getImage005());
        this.list1.add(ImageData.getImage006());
        this.list1.add(ImageData.getImage007());
        this.list1.add(ImageData.getImage008());
        this.list1.add(ImageData.getImage009());
        this.list1.add(ImageData.getImage0010());
        this.list1.add(ImageData.getImage0011());
        this.list1.add(ImageData.getImage0012());
        this.list1.add(ImageData.getImage0013());
        this.list1.add(ImageData.getImage0014());
        new Handler().postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.ImageSaveActivity.1
            @Override // java.lang.Runnable
            public void run() {
                ImageSaveActivity.this.setTimer(true);
            }
        }, 2000L);
    }

    private void initTab() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.fm = supportFragmentManager;
        FragmentTransaction fragmentTransactionBeginTransaction = supportFragmentManager.beginTransaction();
        this.transaction = fragmentTransactionBeginTransaction;
        fragmentTransactionBeginTransaction.replace(R.id.fl_change, AnimFragment.newInstance(false));
        this.transaction.commit();
    }

    public void setTimer(boolean z) {
        MyTimeTask myTimeTask = new MyTimeTask(1000L, new TimerTask() { // from class: com.icwork.shiningglass.ui.activity.ImageSaveActivity.2
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                ImageSaveActivity.this.handler.sendEmptyMessage(1001);
            }
        });
        this.task = myTimeTask;
        myTimeTask.start();
    }
}
