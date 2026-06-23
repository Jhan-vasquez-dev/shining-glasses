package com.icwork.shiningglass.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.cdbwsoft.library.ble.BleManager;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.BaseFragment;
import com.icwork.shiningglass.base.app.C;
import com.icwork.shiningglass.base.app.SoundManager;
import com.icwork.shiningglass.base.music.MusicPlayer;
import com.icwork.shiningglass.model.data.Agreement;
import com.icwork.shiningglass.ui.activity.ConnectActivity;
import com.icwork.shiningglass.ui.activity.DiyActivity;
import com.icwork.shiningglass.ui.activity.ImageSelectActivity;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes.dex */
public class ImageFragment extends BaseFragment implements View.OnClickListener {
    private static int flag;
    private FragmentManager fm;
    ImageView ivBack;
    ImageView ivDefaultImage;
    ImageView ivDiyEdit;
    ImageView ivDiyImage;
    ImageView ivDiyImageSelect;
    ImageView ivDiyPlay;
    ImageView ivForward;
    LinearLayout llDiyImageEdit;
    private MusicPlayer musicPlayer;
    private FragmentTransaction transaction;
    TextView tvTitle;

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected int getLayoutId() {
        return R.layout.fragment_image;
    }

    public static ImageFragment newInstance(int i) {
        ImageFragment imageFragment = new ImageFragment();
        flag = i;
        return imageFragment;
    }

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected void initView(View view, Bundle bundle) {
        this.ivBack = (ImageView) view.findViewById(R.id.iv_back);
        this.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        this.ivForward = (ImageView) view.findViewById(R.id.iv_forward);
        this.ivDefaultImage = (ImageView) view.findViewById(R.id.iv_default_image);
        this.ivDiyImage = (ImageView) view.findViewById(R.id.iv_diy_image);
        this.ivDiyImageSelect = (ImageView) view.findViewById(R.id.iv_diy_image_select);
        this.llDiyImageEdit = (LinearLayout) view.findViewById(R.id.ll_diy_image_edit);
        this.ivDiyEdit = (ImageView) view.findViewById(R.id.iv_diy_edit);
        this.ivDiyPlay = (ImageView) view.findViewById(R.id.iv_diy_player);
        this.ivBack.setOnClickListener(this);
        this.ivForward.setOnClickListener(this);
        this.ivDefaultImage.setOnClickListener(this);
        this.ivDiyImage.setOnClickListener(this);
        this.ivDiyImageSelect.setOnClickListener(this);
        this.ivDiyEdit.setOnClickListener(this);
        this.ivDiyPlay.setOnClickListener(this);
        this.ivDefaultImage.setImageResource(R.mipmap.image_default);
        this.ivDiyImage.setImageResource(R.mipmap.image_diy);
        this.ivBack.setImageResource(R.mipmap.text_magic_back);
        this.ivBack.setVisibility(0);
        this.ivForward.setVisibility(8);
        this.tvTitle.setText(getString(R.string.image_title));
    }

    @Override // com.icwork.shiningglass.base.BaseFragment
    protected void initData() {
        initTab();
    }

    private void initTab() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        this.fm = childFragmentManager;
        FragmentTransaction fragmentTransactionBeginTransaction = childFragmentManager.beginTransaction();
        this.transaction = fragmentTransactionBeginTransaction;
        if (flag == 1) {
            selectDiyImage();
        } else {
            fragmentTransactionBeginTransaction.replace(R.id.fl_change, DefaultImageFragment.newInstance());
            this.transaction.commit();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        this.fm = childFragmentManager;
        this.transaction = childFragmentManager.beginTransaction();
        int id = view.getId();
        if (id == R.id.iv_back) {
            getActivity().finish();
            return;
        }
        if (id == R.id.iv_default_image) {
            this.llDiyImageEdit.setVisibility(4);
            this.ivDefaultImage.setImageResource(R.mipmap.image_default);
            this.ivDiyImage.setImageResource(R.mipmap.image_diy);
            this.transaction.replace(R.id.fl_change, DefaultImageFragment.newInstance());
            this.transaction.commit();
            return;
        }
        switch (id) {
            case R.id.iv_diy_edit /* 2131296435 */:
                SoundManager.getInstance().animImageToDiy();
                startActivityForResult(new Intent(getActivity(), (Class<?>) DiyActivity.class), 1);
                break;
            case R.id.iv_diy_image /* 2131296436 */:
                selectDiyImage();
                break;
            case R.id.iv_diy_image_select /* 2131296437 */:
                startActivity(new Intent(getActivity(), (Class<?>) ImageSelectActivity.class));
                break;
            case R.id.iv_diy_player /* 2131296438 */:
                MusicPlayer musicPlayer = ConnectActivity.getMusicPlayer();
                this.musicPlayer = musicPlayer;
                if (musicPlayer != null && musicPlayer.isPlaying()) {
                    EventBus.getDefault().post(C.MAIN_EVENT.STOP_RHY);
                }
                byte[] encryptData = Agreement.getEncryptData(Agreement.getPlayerDiyCommand());
                BleManager bleManager = ConnectActivity.getBleManager();
                if (bleManager != null) {
                    bleManager.writeData(encryptData);
                }
                break;
        }
    }

    private void selectDiyImage() {
        this.llDiyImageEdit.setVisibility(0);
        this.ivDefaultImage.setImageResource(R.mipmap.image_default_selected);
        this.ivDiyImage.setImageResource(R.mipmap.image_diy_selected);
        this.transaction.replace(R.id.fl_change, DiyImageFragment.newInstance());
        this.transaction.commit();
    }
}
