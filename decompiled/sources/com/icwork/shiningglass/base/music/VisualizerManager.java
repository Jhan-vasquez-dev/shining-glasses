package com.icwork.shiningglass.base.music;

import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.Log;
import com.icwork.shiningglass.sevice.FFTDataListenter;
import com.icwork.shiningglass.sevice.MusicFftListenter;
import com.icwork.shiningglass.ui.activity.ConnectActivity;
import com.icwork.shiningglass.ui.utils.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class VisualizerManager {
    private static final long INTERVAL = 100;
    private static final String TAG = "VisualizerManager";
    private static long lastClickTime;
    private static VisualizerManager sVisualizerManager;
    private List<FFTDataListenter> fftDataListenters = new ArrayList();
    private Visualizer visualizer;

    public static VisualizerManager getInstance() {
        LogUtil.d("======mediaPlayer  sVisualizerManager:" + sVisualizerManager);
        if (sVisualizerManager == null) {
            MediaPlayer mediaPlayer = ConnectActivity.getMusicPlayer().getMediaPlayer();
            LogUtil.d("======mediaPlayer:" + mediaPlayer);
            sVisualizerManager = new VisualizerManager(mediaPlayer);
        }
        return sVisualizerManager;
    }

    private VisualizerManager(MediaPlayer mediaPlayer) {
        if (mediaPlayer == null) {
            return;
        }
        Visualizer visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        this.visualizer = visualizer;
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
        VisualizerView visualizerView = new VisualizerView();
        this.visualizer.setDataCaptureListener(visualizerView, Visualizer.getMaxCaptureRate(), true, false);
        this.visualizer.setEnabled(true);
        visualizerView.setMusicFftListenter(new MusicFftListenter() { // from class: com.icwork.shiningglass.base.music.VisualizerManager.1
            @Override // com.icwork.shiningglass.sevice.MusicFftListenter
            public void onStart(byte[] bArr) {
                if (VisualizerManager.filter() || !Music.isOpenedRhythm()) {
                    return;
                }
                Iterator it = VisualizerManager.this.fftDataListenters.iterator();
                while (it.hasNext()) {
                    ((FFTDataListenter) it.next()).onStart(bArr);
                }
            }
        });
    }

    public void setFftDataListenter(FFTDataListenter fFTDataListenter) {
        if (this.fftDataListenters.contains(fFTDataListenter)) {
            return;
        }
        this.fftDataListenters.add(fFTDataListenter);
    }

    public void stop() {
        Visualizer visualizer = this.visualizer;
        if (visualizer == null) {
            return;
        }
        visualizer.setEnabled(false);
        this.visualizer.setDataCaptureListener(null, Visualizer.getMaxCaptureRate(), true, false);
        this.visualizer.release();
        this.visualizer = null;
        VisualizerView.setIsExit(true);
        System.gc();
    }

    public void removeFftDataListenter(FFTDataListenter fFTDataListenter) {
        if (this.fftDataListenters.contains(fFTDataListenter)) {
            this.fftDataListenters.remove(fFTDataListenter);
        }
    }

    public static void clear() {
        Log.v("ruis", "clear");
        sVisualizerManager = null;
        Log.v("ruis", "clear" + sVisualizerManager);
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
}
