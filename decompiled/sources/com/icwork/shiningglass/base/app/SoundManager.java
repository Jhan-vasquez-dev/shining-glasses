package com.icwork.shiningglass.base.app;

import android.media.SoundPool;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.App;

/* JADX INFO: loaded from: classes.dex */
public class SoundManager {
    private static SoundManager instance;
    private int mSoundAnimImageToDiy;
    private int mSoundAnimSelect;
    private int mSoundDiyClear;
    private int mSoundDiyDeleteAll;
    private int mSoundDiyPen;
    private int mSoundDiySave;
    private int mSoundDiyToImage;
    private int mSoundMainColse;
    private int mSoundMainConnect;
    private int mSoundMainLeftScrolled;
    private int mSoundMainMenuSelet;
    private int mSoundMainOpen;
    private int mSoundMainRightScrolled;
    private SoundPool mSoundPool;
    private int mSoundTextBack;
    private int mSoundTextDelete;
    private int mSoundTextEditOk;
    private int mSoundTextHistoryClose;
    private int mSoundTextHistoryOpen;
    private int mSoundTextInput;
    private int mSoundTextOff;
    private int mSoundTextOn;
    private int mSoundTextSend;
    private float cur_engine_volume = 0.7f;
    private int mSoundCount = 3;

    public void mainConnect() {
    }

    protected SoundManager() {
        SoundPool soundPool = new SoundPool(this.mSoundCount, 3, 0);
        this.mSoundPool = soundPool;
        this.mSoundMainOpen = soundPool.load(App.getInstance(), R.raw.main_unfold_0_1, 1);
        this.mSoundMainColse = this.mSoundPool.load(App.getInstance(), R.raw.main_packup_0_3, 1);
        this.mSoundMainLeftScrolled = this.mSoundPool.load(App.getInstance(), R.raw.main_menu_left_0_6, 1);
        this.mSoundMainRightScrolled = this.mSoundPool.load(App.getInstance(), R.raw.main_menu_right_0_5, 1);
        this.mSoundMainMenuSelet = this.mSoundPool.load(App.getInstance(), R.raw.main_menu_click, 1);
        this.mSoundTextOn = this.mSoundPool.load(App.getInstance(), R.raw.color_switch_1_7, 1);
        this.mSoundTextOff = this.mSoundPool.load(App.getInstance(), R.raw.color_switch_1_7, 1);
        this.mSoundTextInput = this.mSoundPool.load(App.getInstance(), R.raw.text_edit_1_1, 1);
        this.mSoundTextBack = this.mSoundPool.load(App.getInstance(), R.raw.back_1_2, 1);
        this.mSoundTextDelete = this.mSoundPool.load(App.getInstance(), R.raw.delete_1_3, 1);
        this.mSoundTextEditOk = this.mSoundPool.load(App.getInstance(), R.raw.text_edit_ok_1_4, 1);
        this.mSoundTextHistoryOpen = this.mSoundPool.load(App.getInstance(), R.raw.text_history_open_1_5, 1);
        this.mSoundTextHistoryClose = this.mSoundPool.load(App.getInstance(), R.raw.text_history_close_1_6, 1);
        this.mSoundTextSend = this.mSoundPool.load(App.getInstance(), R.raw.text_affirm_1_11, 1);
        this.mSoundAnimSelect = this.mSoundPool.load(App.getInstance(), R.raw.image_select_2_1, 1);
        this.mSoundAnimImageToDiy = this.mSoundPool.load(App.getInstance(), R.raw.image_to_diy_2_6, 1);
        this.mSoundDiySave = this.mSoundPool.load(App.getInstance(), R.raw.diy_save_3_2, 1);
        this.mSoundDiyToImage = this.mSoundPool.load(App.getInstance(), R.raw.image_to_diy_2_6, 1);
        this.mSoundDiyDeleteAll = this.mSoundPool.load(App.getInstance(), R.raw.diy_delete_3_5, 1);
        this.mSoundDiyClear = this.mSoundPool.load(App.getInstance(), R.raw.diy_clear_3_6, 1);
        this.mSoundDiyPen = this.mSoundPool.load(App.getInstance(), R.raw.diy_pen_3_7, 1);
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            synchronized (SoundManager.class) {
                if (instance == null) {
                    instance = new SoundManager();
                }
            }
        }
        return instance;
    }

    public void mainOpen() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundMainOpen;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void mainColse() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundMainColse;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void mainLeftScrolled() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundMainLeftScrolled;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void mainRightScrolled() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundMainRightScrolled;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void mainMenuSelet() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundMainMenuSelet;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void textInput() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundTextInput;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void textBack() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundTextBack;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void textDelete() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundTextDelete;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void textEditOk() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundTextEditOk;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void textHistoryOpen() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundTextHistoryOpen;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void textHistoryClose() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundTextHistoryClose;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void textOn() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundTextOn;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void textOff() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundTextOff;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void textSend() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundTextSend;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void animSelect() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundAnimSelect;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void animImageToDiy() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundAnimImageToDiy;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void diySave() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundDiySave;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void diyToImage() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundDiyToImage;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void diyDeleteAll() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundDiyDeleteAll;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void diyClear() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundDiyClear;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }

    public void diyPen() {
        SoundPool soundPool = this.mSoundPool;
        int i = this.mSoundDiyPen;
        float f = this.cur_engine_volume;
        soundPool.play(i, f, f, 1, 0, 1.0f);
    }
}
