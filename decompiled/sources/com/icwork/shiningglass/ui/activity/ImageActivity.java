package com.icwork.shiningglass.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.BaseActivity;
import com.icwork.shiningglass.ui.fragment.AnimFragment;
import com.icwork.shiningglass.ui.fragment.ImageFragment;

/* JADX INFO: loaded from: classes.dex */
public class ImageActivity extends BaseActivity implements View.OnClickListener {
    private int curSelect = 0;
    private int flag;
    private FragmentManager fm;
    ImageView ivAnim;
    ImageView ivGallery;
    LinearLayout rl_bottom;
    private FragmentTransaction transaction;

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void bindListener() {
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initView() {
        this.ivAnim = (ImageView) findViewById(R.id.iv_anim);
        this.ivGallery = (ImageView) findViewById(R.id.iv_gallery);
        this.rl_bottom = (LinearLayout) findViewById(R.id.rl_bottom);
        this.ivAnim.setOnClickListener(this);
        this.ivGallery.setOnClickListener(this);
        this.ivAnim.setImageResource(R.mipmap.image_anim_selected);
        this.ivGallery.setImageResource(R.mipmap.image_gallery);
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initData() {
        this.flag = getIntent().getIntExtra("flag", 0);
        initTab();
    }

    private void initTab() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.fm = supportFragmentManager;
        FragmentTransaction fragmentTransactionBeginTransaction = supportFragmentManager.beginTransaction();
        this.transaction = fragmentTransactionBeginTransaction;
        if (this.flag == 1) {
            this.curSelect = 1;
            this.ivGallery.setImageResource(R.mipmap.image_gallery_selected);
            this.ivAnim.setImageResource(R.mipmap.image_anim);
            this.transaction.replace(R.id.fl_change, ImageFragment.newInstance(1));
            this.transaction.commit();
            return;
        }
        fragmentTransactionBeginTransaction.replace(R.id.fl_change, AnimFragment.newInstance(false));
        this.transaction.commit();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.fm = supportFragmentManager;
        this.transaction = supportFragmentManager.beginTransaction();
        int id = view.getId();
        if (id == R.id.iv_anim) {
            if (this.curSelect == 0) {
                return;
            }
            this.curSelect = 0;
            this.ivAnim.setImageResource(R.mipmap.image_anim_selected);
            this.ivGallery.setImageResource(R.mipmap.image_gallery);
            this.transaction.replace(R.id.fl_change, AnimFragment.newInstance(true));
            this.transaction.commit();
            return;
        }
        if (id == R.id.iv_gallery && this.curSelect != 1) {
            this.curSelect = 1;
            this.ivGallery.setImageResource(R.mipmap.image_gallery_selected);
            this.ivAnim.setImageResource(R.mipmap.image_anim);
            this.transaction.replace(R.id.fl_change, ImageFragment.newInstance(0));
            this.transaction.commit();
        }
    }
}
