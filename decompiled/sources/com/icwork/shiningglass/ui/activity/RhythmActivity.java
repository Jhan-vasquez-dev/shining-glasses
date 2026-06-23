package com.icwork.shiningglass.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.base.BaseActivity;
import com.icwork.shiningglass.base.DataManager;
import com.icwork.shiningglass.base.app.SoundManager;
import com.icwork.shiningglass.base.music.Music;
import com.icwork.shiningglass.base.music.MusicListenter;
import com.icwork.shiningglass.base.music.MusicPlayer;
import com.icwork.shiningglass.base.music.Song;
import com.icwork.shiningglass.base.music.VisualizerManager;
import com.icwork.shiningglass.sevice.FFTDataListenter;
import com.icwork.shiningglass.ui.adapter.SongAdapter;
import com.icwork.shiningglass.ui.utils.ClickFilter;
import com.icwork.shiningglass.ui.utils.DensityUtil;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.utils.ScreenUtils;
import com.icwork.shiningglass.ui.widget.RhythmLedView;
import com.icwork.shiningglass.ui.widget.image3d.RhyImage3DSwitchView;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes.dex */
public class RhythmActivity extends BaseActivity implements AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private static final String TAG = "RhythmActivity";
    private AnimationDrawable animBtn;
    private DataManager dataManager;
    private FFTDataListenter fftDataListenter;
    RhyImage3DSwitchView imageSwitchView;
    ImageView imgvLast;
    ImageView imgvNext;
    ImageView imgvPlayMode;
    ImageView imgvPlayPause;
    private boolean isPlayVisible;
    ImageView ivAnim;
    ImageView ivBack;
    ImageView ivBtn1;
    ImageView ivForward;
    private AnimationDrawable ivForwardAnim;
    ImageView ivPlayList;
    LinearLayout llSongInfo;
    LinearLayout lnrlaoActionTwo;
    RelativeLayout lnrlaoVolume;
    ListView lstvSong;
    private String minuteStr;
    MusicListenterImpl musicListenter;
    MusicPlayer musicPlayer;
    RhythmLedView rhythmLedView;
    RelativeLayout rlBottom;
    RelativeLayout rlPlay;
    RelativeLayout rlRhySelect;
    RelativeLayout rlRhyShow;
    RelativeLayout rlSongList;
    RelativeLayout rlSongName;
    SeekBar sbPlayTime;
    private String secondStr;
    SongAdapter songAdapter;
    List<Song> songList;
    TextView tvTitle;
    TextView txvPlayDuration;
    TextView txvPlayTime;
    TextView txvSinger;
    TextView txvSinger1;
    TextView txvSongName;
    TextView txvSongName1;
    private boolean rhythmStatus = true;
    private int curSelectMode = 0;
    private final ExecutorService fftExecutor = Executors.newSingleThreadExecutor();
    Runnable runnable = new Runnable() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity.4
        @Override // java.lang.Runnable
        public void run() {
            RhythmActivity.this.rlRhyShow.setVisibility(0);
        }
    };
    CountDownTimer playPositionTimer = new CountDownTimer(86400000, 1000) { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity.5
        @Override // android.os.CountDownTimer
        public void onFinish() {
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            RhythmActivity.this.sbPlayTime.setProgress(RhythmActivity.this.musicPlayer.getCurrentPosition());
        }
    };
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected int getLayoutId() {
        return R.layout.activity_rhythm;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initView() {
        findView();
        this.ivBack.setImageResource(R.mipmap.text_magic_back);
        this.ivBack.setVisibility(0);
        this.ivForward.setImageResource(R.mipmap.rhy_microphone);
        this.ivForward.setVisibility(0);
        this.sbPlayTime.setOnSeekBarChangeListener(this);
        this.ivAnim.setImageResource(R.drawable.anim_music_rhy);
        AnimationDrawable animationDrawable = (AnimationDrawable) this.ivAnim.getDrawable();
        this.animBtn = animationDrawable;
        animationDrawable.start();
    }

    private void findView() {
        this.ivBack = (ImageView) findViewById(R.id.iv_back);
        this.rlSongList = (RelativeLayout) findViewById(R.id.rl_song_list);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.ivForward = (ImageView) findViewById(R.id.iv_forward);
        this.txvSongName = (TextView) findViewById(R.id.txv_song_name);
        this.txvSinger = (TextView) findViewById(R.id.txv_singer);
        this.txvSongName1 = (TextView) findViewById(R.id.txv_song_name1);
        this.txvSinger1 = (TextView) findViewById(R.id.txv_singer1);
        this.txvPlayTime = (TextView) findViewById(R.id.txv_play_time);
        this.txvPlayDuration = (TextView) findViewById(R.id.txv_play_duration);
        this.sbPlayTime = (SeekBar) findViewById(R.id.sb_play_time);
        this.lnrlaoVolume = (RelativeLayout) findViewById(R.id.lnrlao_volume);
        this.lstvSong = (ListView) findViewById(R.id.lstv_song);
        this.imgvPlayMode = (ImageView) findViewById(R.id.imgv_play_mode);
        this.imgvLast = (ImageView) findViewById(R.id.imgv_last);
        this.imgvPlayPause = (ImageView) findViewById(R.id.imgv_play_pause);
        this.imgvNext = (ImageView) findViewById(R.id.imgv_next);
        this.lnrlaoActionTwo = (LinearLayout) findViewById(R.id.lnrlao_action_two);
        this.rlPlay = (RelativeLayout) findViewById(R.id.rl_play);
        this.ivAnim = (ImageView) findViewById(R.id.iv_anim);
        this.ivPlayList = (ImageView) findViewById(R.id.iv_play_list);
        this.rlRhySelect = (RelativeLayout) findViewById(R.id.rl_rhy_select);
        this.ivBtn1 = (ImageView) findViewById(R.id.iv_btn1);
        this.rlBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        this.llSongInfo = (LinearLayout) findViewById(R.id.ll_song_info);
        this.rlSongName = (RelativeLayout) findViewById(R.id.rl_song_name);
        this.rhythmLedView = (RhythmLedView) findViewById(R.id.rhyledview_1);
        this.imageSwitchView = (RhyImage3DSwitchView) findViewById(R.id.image_switch_view);
        this.rlRhyShow = (RelativeLayout) findViewById(R.id.rl_rhy_show);
        this.ivBack.setOnClickListener(this);
        this.imgvPlayMode.setOnClickListener(this);
        this.imgvLast.setOnClickListener(this);
        this.imgvPlayPause.setOnClickListener(this);
        this.imgvNext.setOnClickListener(this);
        this.ivPlayList.setOnClickListener(this);
        this.ivForward.setOnClickListener(this);
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void initData() {
        this.dataManager = DataManager.getInstance();
        this.curSelectMode = DataManager.getInstance().getCurSelectRhythmMode();
        this.songList = Music.getSongList();
        SongAdapter songAdapter = new SongAdapter(this, this.songList);
        this.songAdapter = songAdapter;
        this.lstvSong.setAdapter((ListAdapter) songAdapter);
        this.lstvSong.setOnItemClickListener(this);
        initMusicPlayer();
        this.rhythmLedView.setLayerType(1, null);
        this.rhythmLedView.setMode(0);
        this.rhythmLedView.setPointMargin(0);
        this.rhythmLedView.removeAllViews();
        this.rhythmLedView.init(36, 12);
        this.imageSwitchView.setCurrentImage(DataManager.getInstance().getCurSelectRhythmMode1());
    }

    @Override // com.icwork.shiningglass.base.BaseActivity
    protected void bindListener() {
        this.imageSwitchView.setOnImageItemClickListener(new RhyImage3DSwitchView.OnImageItemClickListener() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity.1
            @Override // com.icwork.shiningglass.ui.widget.image3d.RhyImage3DSwitchView.OnImageItemClickListener
            public void onClick(int i) {
            }

            @Override // com.icwork.shiningglass.ui.widget.image3d.RhyImage3DSwitchView.OnImageItemClickListener
            public void onTouch() {
                RhythmActivity.this.rhyUIShow(false);
            }

            @Override // com.icwork.shiningglass.ui.widget.image3d.RhyImage3DSwitchView.OnImageItemClickListener
            public void onTouchStop() {
                RhythmActivity.this.rhyUIShow(true);
            }
        });
        this.imageSwitchView.setOnImageSwitchListener(new RhyImage3DSwitchView.OnImageSwitchListener() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity.2
            @Override // com.icwork.shiningglass.ui.widget.image3d.RhyImage3DSwitchView.OnImageSwitchListener
            public void onImageSwitch(int i) {
                LogUtil.d("currentImage:" + i);
                if ((RhythmActivity.this.musicPlayer != null && RhythmActivity.this.musicPlayer.isPlaying()) || DataManager.getInstance().isMicrophoneEnable()) {
                    RhythmActivity.this.rhyUIShow(true);
                } else {
                    RhythmActivity.this.rhyUIShow(false);
                }
                RhythmActivity.this.dataManager.setCurSelectRhythmMode1(i);
                switch (i) {
                    case 0:
                    case 5:
                        RhythmActivity.this.selectRhythmMode(1);
                        break;
                    case 1:
                    case 6:
                        RhythmActivity.this.selectRhythmMode(2);
                        break;
                    case 2:
                    case 7:
                        RhythmActivity.this.selectRhythmMode(3);
                        break;
                    case 3:
                    case 8:
                        RhythmActivity.this.selectRhythmMode(4);
                        break;
                    case 4:
                    case 9:
                        RhythmActivity.this.selectRhythmMode(5);
                        break;
                }
            }
        });
        this.fftDataListenter = new FFTDataListenter() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity$$ExternalSyntheticLambda0
            @Override // com.icwork.shiningglass.sevice.FFTDataListenter
            public final void onStart(byte[] bArr) {
                this.f$0.lambda$bindListener$2(bArr);
            }
        };
        VisualizerManager.getInstance().setFftDataListenter(this.fftDataListenter);
        this.rlBottom.setOnTouchListener(new View.OnTouchListener() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return this.f$0.lambda$bindListener$3(view, motionEvent);
            }
        });
        this.rlSongName.setOnTouchListener(new View.OnTouchListener() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    motionEvent.getY();
                } else if (action == 1) {
                    float y = motionEvent.getY();
                    if (y - 0.0f >= 5.0f) {
                        LogUtil.d("y1:0.0  y2:" + y);
                        RhythmActivity.this.songListGone();
                    }
                } else if (action == 2) {
                    motionEvent.getY();
                }
                return true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindListener$2(final byte[] bArr) {
        this.fftExecutor.execute(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$bindListener$1(bArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindListener$1(byte[] bArr) {
        MusicPlayer musicPlayer;
        Log.v("ruis", "initRythm --fFTDataListenter " + this.musicPlayer + DataManager.getInstance().isMicrophoneEnable());
        if (!DataManager.getInstance().isMicrophoneEnable() && this.rhythmStatus && Music.isOpenedRhythm() && (musicPlayer = this.musicPlayer) != null && musicPlayer.isPlaying()) {
            final byte[] bArrProcessFFTData = processFFTData(bArr);
            runOnUiThread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$bindListener$0(bArrProcessFFTData);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$bindListener$3(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            motionEvent.getY();
        } else if (action == 1) {
            float y = motionEvent.getY();
            if (y - 0.0f <= -5.0f) {
                LogUtil.d("y1:0.0  y2:" + y);
                songListVisible();
            }
        } else if (action == 2) {
            motionEvent.getY();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: setRhyData, reason: merged with bridge method [inline-methods] */
    public void lambda$bindListener$0(byte[] bArr) {
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

    private byte[] processFFTData(byte[] bArr) {
        if (bArr == null) {
            return new byte[0];
        }
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = (byte) Math.min(127, Math.max(-127, (int) bArr[i]));
        }
        return bArr2;
    }

    @Override // com.icwork.shiningglass.base.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        this.dataManager.setCurSelectRhythmMode(this.curSelectMode);
        CountDownTimer countDownTimer = this.playPositionTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        VisualizerManager.getInstance().removeFftDataListenter(this.fftDataListenter);
        this.fftExecutor.shutdownNow();
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long j) {
        Song song;
        if (ClickFilter.filter() || (song = Music.getSong(i)) == null) {
            return;
        }
        try {
            this.musicPlayer.play(song.getFileUrl());
            runOnUiThread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onItemClick$4(i);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemClick$4(int i) {
        this.imgvPlayPause.setImageResource(R.mipmap.rhy_stop);
        openRhythm();
        Music.setCurrentSong(this.songList.get(i));
    }

    private void initMusicPlayer() {
        MusicPlayer musicPlayer = ConnectActivity.getMusicPlayer();
        this.musicPlayer = musicPlayer;
        if (musicPlayer == null) {
            return;
        }
        MusicListenterImpl musicListenterImpl = new MusicListenterImpl();
        this.musicListenter = musicListenterImpl;
        this.musicPlayer.registerMusicListenter(musicListenterImpl);
        for (int i = 0; i < this.songList.size(); i++) {
            this.songList.get(i).setState(0);
        }
        int playMode = Music.getPlayMode();
        if (playMode == 1) {
            this.imgvPlayMode.setImageResource(R.mipmap.rhy_random);
        } else if (playMode == 2) {
            this.imgvPlayMode.setImageResource(R.mipmap.rhy_single);
        } else {
            this.imgvPlayMode.setImageResource(R.mipmap.rhy_circulation);
        }
        Song currentSong = Music.getCurrentSong();
        if (currentSong != null) {
            this.txvSongName.setText(currentSong.getTitle());
            this.txvSinger.setText(currentSong.getSinger());
            this.txvSongName1.setText(currentSong.getTitle());
            this.txvSinger1.setText(currentSong.getSinger());
            int i2 = 0;
            while (true) {
                if (i2 >= this.songList.size()) {
                    break;
                }
                Song song = this.songList.get(i2);
                song.setState(0);
                if (song != null && song.getFileUrl().equals(currentSong.getFileUrl())) {
                    song.setState(1);
                    break;
                }
                i2++;
            }
        }
        if (Music.getSTATE() == 1) {
            this.imgvPlayPause.setImageResource(R.mipmap.rhy_stop);
            rhyUIShow(true);
        } else if (Music.getSTATE() == 2) {
            this.imgvPlayPause.setImageResource(R.mipmap.rhy_player);
            rhyUIShow(false);
        } else {
            this.imgvPlayPause.setImageResource(R.mipmap.rhy_player);
            rhyUIShow(false);
        }
        if (Music.getSTATE() == 1) {
            LogUtil.d("=====duration：" + this.musicPlayer.getDuration());
            this.sbPlayTime.setMax(this.musicPlayer.getDuration());
            this.playPositionTimer.start();
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int i2 = i / 1000;
        long j = ((long) i2) - (((long) (i2 / 3600)) * 3600);
        long j2 = j / 60;
        long j3 = j - (60 * j2);
        if (j2 < 10) {
            this.minuteStr = "0" + j2;
        } else {
            this.minuteStr = "" + j2;
        }
        if (j3 < 10) {
            this.secondStr = "0" + j3;
        } else {
            this.secondStr = "" + j3;
        }
        this.txvPlayTime.setText(this.minuteStr + ":" + this.secondStr);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        this.musicPlayer.seekTo(seekBar.getProgress());
    }

    class MusicListenterImpl extends MusicListenter {
        MusicListenterImpl() {
        }

        @Override // com.icwork.shiningglass.base.music.MusicListenter
        public void onStart() {
            super.onStart();
            LogUtil.v("onStart Rhy");
            RhythmActivity.this.runOnUiThread(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity$MusicListenterImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStart$0();
                }
            });
            RhythmActivity.this.playPositionTimer.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStart$0() {
            String str;
            String str2;
            for (int i = 0; i < RhythmActivity.this.songList.size(); i++) {
                RhythmActivity.this.songList.get(i).setState(0);
            }
            Song currentSong = Music.getCurrentSong();
            LogUtil.v("onStart Rhy" + currentSong.getFileUrl());
            for (int i2 = 0; i2 < RhythmActivity.this.songList.size(); i2++) {
                LogUtil.v("onStart Rhy" + RhythmActivity.this.songList.get(i2).getFileUrl());
                if (currentSong.getFileUrl().equals(RhythmActivity.this.songList.get(i2).getFileUrl())) {
                    RhythmActivity.this.songList.get(i2).setState(1);
                }
            }
            RhythmActivity.this.songAdapter.notifyDataSetChanged();
            RhythmActivity.this.txvSongName.setText(currentSong.getTitle());
            RhythmActivity.this.txvSinger.setText(currentSong.getSinger());
            RhythmActivity.this.txvSongName1.setText(currentSong.getTitle());
            RhythmActivity.this.txvSinger1.setText(currentSong.getSinger());
            int duration = RhythmActivity.this.musicPlayer.getDuration();
            int i3 = duration / 1000;
            long j = ((long) i3) - (((long) (i3 / 3600)) * 3600);
            long j2 = j / 60;
            long j3 = j - (60 * j2);
            if (j2 < 10) {
                str = "0" + j2;
            } else {
                str = "" + j2;
            }
            if (j3 < 10) {
                str2 = "0" + j3;
            } else {
                str2 = "" + j3;
            }
            RhythmActivity.this.txvPlayDuration.setText(str + ":" + str2);
            RhythmActivity.this.sbPlayTime.setMax(duration);
        }

        @Override // com.icwork.shiningglass.base.music.MusicListenter
        public void onPause() {
            super.onPause();
            RhythmActivity.this.playPositionTimer.cancel();
        }
    }

    private void playOrPause() {
        LogUtil.d("播放或暂停:" + Music.isOpenedRhythm() + "  " + Music.getSTATE());
        try {
            if (Music.getSTATE() == 0) {
                Music.setIsOpenedRhythm(true);
                setMicroPhoneOff();
                Song currentSong = Music.getCurrentSong();
                if (currentSong != null) {
                    this.musicPlayer.play(currentSong.getFileUrl());
                    this.imgvPlayPause.setImageResource(R.mipmap.rhy_stop);
                    rhyUIShow(true);
                    if (this.rhythmStatus) {
                        openRhythm();
                        return;
                    }
                    return;
                }
                return;
            }
            if (Music.getSTATE() == 1) {
                stopPlayMusic();
            } else if (Music.getSTATE() == 2) {
                startPalyMusic();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlayMusic() {
        Music.setIsOpenedRhythm(false);
        if (this.musicPlayer.isPlaying()) {
            this.musicPlayer.pause();
        }
        this.imgvPlayPause.setImageResource(R.mipmap.rhy_player);
        Music.setIsOpenedRhythm(false);
        rhyUIShow(false);
    }

    private void startPalyMusic() {
        Music.setIsOpenedRhythm(true);
        setMicroPhoneOff();
        this.musicPlayer.start();
        this.imgvPlayPause.setImageResource(R.mipmap.rhy_stop);
        openRhythm();
        rhyUIShow(true);
    }

    private void changeSong(Song song) {
        if (song == null) {
            return;
        }
        String fileUrl = song.getFileUrl();
        try {
            if (Music.getSTATE() == 0 || Music.getSTATE() == 1 || Music.getSTATE() == 2) {
                this.musicPlayer.play(fileUrl);
                Music.setSTATE(1);
            }
            if (this.rhythmStatus) {
                openRhythm();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openRhythm() {
        LogUtil.d("开启律动");
        Music.setIsOpenedRhythm(true);
        this.rhythmStatus = true;
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (ClickFilter.filter()) {
            return;
        }
        int i = 0;
        switch (view.getId()) {
            case R.id.imgv_last /* 2131296396 */:
                setMicroPhoneOff();
                changeSong(Music.getLastSong());
                if (Music.getSTATE() == 1) {
                    this.imgvPlayPause.setImageResource(R.mipmap.rhy_stop);
                    rhyUIShow(true);
                }
                break;
            case R.id.imgv_next /* 2131296397 */:
                setMicroPhoneOff();
                changeSong(Music.getNextSong());
                if (Music.getSTATE() == 1) {
                    this.imgvPlayPause.setImageResource(R.mipmap.rhy_stop);
                    rhyUIShow(true);
                }
                break;
            case R.id.imgv_play_mode /* 2131296399 */:
                int playMode = Music.getPlayMode();
                if (playMode == 0) {
                    i = 1;
                } else if (playMode == 1) {
                    i = 2;
                } else if (playMode != 2) {
                    i = playMode;
                }
                Music.setPlayMode(i);
                if (i == 1) {
                    this.imgvPlayMode.setImageResource(R.mipmap.rhy_random);
                } else if (i == 2) {
                    this.imgvPlayMode.setImageResource(R.mipmap.rhy_single);
                } else {
                    this.imgvPlayMode.setImageResource(R.mipmap.rhy_circulation);
                }
                break;
            case R.id.imgv_play_pause /* 2131296400 */:
                playOrPause();
                break;
            case R.id.iv_back /* 2131296426 */:
                SoundManager.getInstance().textBack();
                DataManager.getInstance().setMicrophoneEnable(false);
                finish();
                break;
            case R.id.iv_forward /* 2131296441 */:
                stopPlayMusic();
                toActivity(MicrophoneActivity.class);
                break;
            case R.id.iv_play_list /* 2131296456 */:
                if (this.isPlayVisible) {
                    songListGone();
                } else {
                    songListVisible();
                }
                break;
        }
    }

    private void setMicroPhoneOff() {
        DataManager.getInstance().setMicrophoneEnable(false);
        AnimationDrawable animationDrawable = this.ivForwardAnim;
        if (animationDrawable != null) {
            animationDrawable.stop();
            this.ivForward.clearAnimation();
            this.ivForward.setImageResource(R.mipmap.rhy_microphone);
        }
        rhyUIShow(false);
    }

    private void setMicroPhoneOn() {
        Music.setIsOpenedRhythm(false);
        DataManager.getInstance().setMicrophoneEnable(true);
        this.ivForward.setImageResource(R.drawable.anim_microphone);
        AnimationDrawable animationDrawable = (AnimationDrawable) this.ivForward.getDrawable();
        this.ivForwardAnim = animationDrawable;
        animationDrawable.start();
        MusicPlayer musicPlayer = this.musicPlayer;
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            this.musicPlayer.pause();
        }
        this.imgvPlayPause.setImageResource(R.mipmap.rhy_player);
        rhyUIShow(true);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        DataManager.getInstance().setMicrophoneEnable(false);
        super.onBackPressed();
    }

    private void songListVisible() {
        this.rlBottom.setEnabled(false);
        this.isPlayVisible = true;
        this.rlRhySelect.setVisibility(8);
        this.ivAnim.setVisibility(8);
        fadeOut(this.rlRhySelect);
        int screenHeight = ScreenUtils.getScreenHeight(this);
        this.lstvSong.setVisibility(0);
        topMove(this.rlSongList, -(screenHeight - ScreenUtils.dp2px(this, 215.0f)));
        this.handler.postDelayed(new Runnable() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity.6
            @Override // java.lang.Runnable
            public void run() {
                RhythmActivity rhythmActivity = RhythmActivity.this;
                rhythmActivity.topMove1(rhythmActivity.llSongInfo, -140.0f);
            }
        }, 380L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void songListGone() {
        this.rlBottom.setEnabled(true);
        fadeIn(this.rlRhySelect);
        this.isPlayVisible = false;
        this.rlSongName.setVisibility(4);
        bottomMove(this.rlSongList, -(ScreenUtils.getScreenHeight(this) - ScreenUtils.dp2px(this, 215.0f)));
        bottomMove1(this.llSongInfo, 140.0f);
    }

    public static void fadeOut(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(400L);
        alphaAnimation.setRepeatCount(0);
        alphaAnimation.setRepeatMode(1);
        view.startAnimation(alphaAnimation);
    }

    public static void fadeIn(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1500L);
        alphaAnimation.setRepeatCount(0);
        alphaAnimation.setRepeatMode(2);
        view.startAnimation(alphaAnimation);
    }

    private void topMove(final View view, float f) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, -f, 0.0f);
        translateAnimation.setDuration(1000L);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setRepeatMode(1);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity.7
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                RhythmActivity.this.ivPlayList.setEnabled(false);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                RhythmActivity.this.ivPlayList.setEnabled(true);
            }
        });
    }

    private void bottomMove(final View view, float f) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -f);
        translateAnimation.setDuration(1000L);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setRepeatMode(1);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity.8
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                RhythmActivity.this.ivPlayList.setEnabled(false);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                RhythmActivity.this.ivPlayList.setEnabled(true);
                view.clearAnimation();
                RhythmActivity.this.lstvSong.setVisibility(8);
                RhythmActivity.this.rlRhySelect.setVisibility(0);
                RhythmActivity.this.ivAnim.setVisibility(0);
                RhythmActivity.this.llSongInfo.setVisibility(0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void topMove1(final View view, float f) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (int) DensityUtil.dp2px(this, f));
        translateAnimation.setDuration(380L);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setRepeatMode(-1);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity.9
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                RhythmActivity.this.llSongInfo.setVisibility(4);
                RhythmActivity.this.rlSongName.setVisibility(0);
            }
        });
    }

    private void bottomMove1(final View view, float f) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, -((int) DensityUtil.dp2px(this, f)), 0.0f);
        translateAnimation.setDuration(1000L);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setRepeatMode(-1);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.icwork.shiningglass.ui.activity.RhythmActivity.10
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
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
}
