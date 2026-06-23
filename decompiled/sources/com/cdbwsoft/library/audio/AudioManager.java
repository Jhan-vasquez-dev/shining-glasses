package com.cdbwsoft.library.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.cdbwsoft.library.AppConfig;

/* JADX INFO: loaded from: classes.dex */
public class AudioManager implements Handler.Callback {
    public static final int MSG_CONNECTED = 3073;
    public static final int MSG_DISCONNECTED = 3074;
    private AudioListener mAudioListener;
    private AudioPlayer mAudioPlayer;
    private boolean mConnected;
    private Context mContext;
    private Handler mHandler;
    private HeadsetReceiver mReceiver = new HeadsetReceiver();

    public AudioManager(Context context, AudioListener audioListener, AudioPlayer audioPlayer) {
        this.mContext = context;
        this.mAudioListener = audioListener;
        this.mHandler = new Handler(this.mContext.getMainLooper(), this);
        this.mAudioPlayer = audioPlayer;
    }

    public boolean isConnected() {
        return this.mConnected;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message == null) {
            return false;
        }
        int i = message.what;
        if (i == 3073) {
            this.mConnected = true;
            AudioListener audioListener = this.mAudioListener;
            if (audioListener != null) {
                audioListener.onConnectionChanged(true);
            }
        } else if (i == 3074) {
            this.mConnected = false;
            AudioListener audioListener2 = this.mAudioListener;
            if (audioListener2 != null) {
                audioListener2.onConnectionChanged(false);
            }
        }
        return false;
    }

    public void onPause() {
        try {
            Context context = this.mContext;
            if (context != null) {
                context.unregisterReceiver(this.mReceiver);
            }
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void onResume() {
        try {
            if (this.mContext != null) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.HEADSET_PLUG");
                this.mContext.registerReceiver(this.mReceiver, intentFilter);
            }
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void release() {
        try {
            this.mContext.unregisterReceiver(this.mReceiver);
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        this.mContext = null;
        this.mReceiver = null;
        this.mAudioPlayer.stop();
        this.mAudioPlayer = null;
    }

    public long writeData(byte[] bArr, int i) {
        AudioPlayer audioPlayer = this.mAudioPlayer;
        if (audioPlayer == null || !this.mConnected) {
            return 0L;
        }
        return audioPlayer.play(bArr, i);
    }

    private class HeadsetReceiver extends BroadcastReceiver {
        private HeadsetReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (action.equals("android.intent.action.HEADSET_PLUG") && intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 0) {
                    AudioManager.this.mHandler.obtainMessage(3074).sendToTarget();
                } else if (1 == intent.getIntExtra("state", 0)) {
                    AudioManager.this.mHandler.obtainMessage(3073).sendToTarget();
                }
            }
        }
    }
}
