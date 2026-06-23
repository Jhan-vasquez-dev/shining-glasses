package com.icwork.shiningglass.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.BaseActivity;
import com.icwork.shiningglass.base.update.UpdateManager;

/* JADX INFO: loaded from: classes.dex */
public class SplashActivity extends BaseActivity {
    private static final int SHOW_TIME_MIN = 2700;
    ImageView ivCenter;
    private AnimationDrawable ivCenterAnim;

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void bindListener() {
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        if (!isTaskRoot()) {
            finish();
        }
        super.onCreate(bundle);
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initView() {
        ImageView imageView = (ImageView) findViewById(R.id.iv_center3);
        this.ivCenter = imageView;
        imageView.setImageResource(R.drawable.anim_splash_image);
        AnimationDrawable animationDrawable = (AnimationDrawable) this.ivCenter.getDrawable();
        this.ivCenterAnim = animationDrawable;
        animationDrawable.start();
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initData() {
        new Handler().postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.SplashActivity.1
            @Override // java.lang.Runnable
            public void run() {
                SplashActivity.this.toActivity(ConnectActivity.class);
                SplashActivity.this.finish();
            }
        }, 2700L);
        new UpdateManager(this.mActivity);
        UpdateManager.uploadInstallInfo(this.mContext);
    }

    @Override // com.icwork.shiningglass.base.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        AnimationDrawable animationDrawable = this.ivCenterAnim;
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
    }
}
