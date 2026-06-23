package com.cdbwsoft.library.audio;

import android.content.Context;
import android.media.AudioTrack;
import com.cdbwsoft.library.base.DataListener;

/* JADX INFO: loaded from: classes.dex */
public abstract class AudioListener extends DataListener {
    private android.media.AudioManager mAudioManager;
    private AudioTrack mAudioTrack;
    private final Context mContext;
    public final String TAG = "AudioListener";
    private boolean mSending = false;

    @Override // com.cdbwsoft.library.base.DataListener
    protected boolean doReadData(byte[] bArr) {
        return false;
    }

    @Override // com.cdbwsoft.library.base.DataListener
    protected boolean doWriteData(byte[] bArr) {
        return false;
    }

    public void onConnectionChanged(boolean z) {
    }

    public AudioListener(Context context) {
        this.mContext = context;
    }

    public boolean checkMaxVolume() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (android.media.AudioManager) this.mContext.getSystemService("audio");
        }
        return this.mAudioManager.getStreamVolume(3) == this.mAudioManager.getStreamMaxVolume(3);
    }
}
