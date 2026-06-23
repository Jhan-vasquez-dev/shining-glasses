package com.icwork.shiningglass.sevice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.icwork.shiningglass.base.music.VisualizerView;
import com.icwork.shiningglass.base.recoder.Recoder;
import com.icwork.shiningglass.base.recoder.SoundRecordHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import kotlin.jvm.internal.ShortCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public class MikeSevice extends Service {
    private static final long INTERVAL = 50;
    public static final int MSG_MUSIC_WAVE = 773;
    static boolean isExit;
    private static long lastClickTime;
    static BlockingQueue<byte[]> queue;
    private float mMaxLevel;
    private float mMinLevel;
    public SoundRecordHelper mRecorder = null;
    private boolean procssing = false;
    short[] pcmbuffer = null;
    private short _maxLevel = ShortCompanionObject.MIN_VALUE;
    private short _minLevel = ShortCompanionObject.MAX_VALUE;
    private List<MikeListenter> musicListenterList = new ArrayList();

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return new BinderImpl();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    public void start() {
        SoundRecordHelper soundRecordHelper = new SoundRecordHelper();
        this.mRecorder = soundRecordHelper;
        soundRecordHelper.setDatareportCallBack(new SoundRecordHelper.SoundRecoderHelperCallbackData() { // from class: com.icwork.shiningglass.sevice.MikeSevice$$ExternalSyntheticLambda0
            @Override // com.icwork.shiningglass.base.recoder.SoundRecordHelper.SoundRecoderHelperCallbackData
            public final void reportdata(short[] sArr) {
                this.f$0.lambda$start$0(sArr);
            }
        });
        this.mMaxLevel = 0.0f;
        this.mMinLevel = 0.0f;
        Recoder.state = 1;
        this.mRecorder.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0(short[] sArr) {
        if (this.procssing || filter()) {
            return;
        }
        short[] sArr2 = this.pcmbuffer;
        if (sArr2 == null || sArr2.length != sArr.length) {
            this.pcmbuffer = new short[sArr.length];
        }
        System.arraycopy(sArr, 0, this.pcmbuffer, 0, sArr.length);
        sendData(this.pcmbuffer);
    }

    private void sendData(short[] sArr) {
        for (int i = 0; i < this.musicListenterList.size(); i++) {
            MikeListenter mikeListenter = this.musicListenterList.get(i);
            if (mikeListenter != null && Recoder.state == 1) {
                mikeListenter.onStart(sArr);
            }
        }
    }

    public static boolean filter() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = jCurrentTimeMillis - lastClickTime;
        if (0 < j && j < INTERVAL) {
            return true;
        }
        lastClickTime = jCurrentTimeMillis;
        return false;
    }

    public void stop() {
        if (this.mRecorder != null) {
            Recoder.state = 0;
            this.mRecorder.stop();
        }
        VisualizerView.setIsExit(true);
        System.gc();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void registerMusicListenter(MikeListenter mikeListenter) {
        this.musicListenterList.add(mikeListenter);
    }

    public void unregisterMusicListenter(MikeListenter mikeListenter) {
        this.musicListenterList.remove(mikeListenter);
    }

    public class BinderImpl extends Binder {
        public BinderImpl() {
        }

        public MikeSevice getService() {
            return MikeSevice.this;
        }
    }
}
