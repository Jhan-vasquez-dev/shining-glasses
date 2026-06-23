package com.cdbwsoft.library.audio;

import android.media.AudioTrack;
import android.util.Log;
import com.cdbwsoft.library.AppConfig;

/* JADX INFO: loaded from: classes.dex */
public abstract class AudioPlayer {
    public static final int AUDIO_SAMPLE_RATE = 48000;
    public static final int HEADER_1K_HZ_RATE = 1000;
    public static final int HEADER_SAMPLE_RATE = 2000;
    public static final int NUM_0_SAMPLE_RATE = 4000;
    public static final int NUM_1_SAMPLE_RATE = 2800;
    public static final String TAG = "MessageOut";
    private AudioTrack mAudioTrack;
    private boolean mDualChannel;
    private int mEncodedBufferSize;
    private boolean msgIsSending = false;

    public abstract byte[] encode(byte[] bArr);

    public AudioPlayer(boolean z) {
        this.mDualChannel = z;
    }

    public boolean msgIsSending() {
        return this.msgIsSending;
    }

    public void setEncodedBufferSize(int i) {
        this.mEncodedBufferSize = i;
    }

    public long play(byte[] bArr, int i) {
        byte[] bArrEncode = encode(bArr);
        this.mEncodedBufferSize = bArrEncode.length;
        if (this.msgIsSending) {
            stop();
        }
        int i2 = AUDIO_SAMPLE_RATE;
        int minBufferSize = AudioTrack.getMinBufferSize(AUDIO_SAMPLE_RATE, 12, 3);
        boolean z = this.mDualChannel;
        if (z) {
            i2 = 24000;
        }
        this.mAudioTrack = new AudioTrack(3, i2, z ? 12 : 4, 3, minBufferSize, 0);
        this.msgIsSending = true;
        int i3 = this.mEncodedBufferSize;
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArrEncode, 0, bArr2, 0, bArrEncode.length);
        int iWrite = this.mAudioTrack.write(bArr2, 0, i3);
        if (AppConfig.DEBUG) {
            Log.d(TAG, "写入音频数据完成：" + iWrite);
        }
        if (iWrite < 0) {
            return iWrite;
        }
        if (this.mDualChannel) {
            this.mAudioTrack.setStereoVolume(1.0f, 1.0f);
            this.mAudioTrack.setLoopPoints(0, this.mEncodedBufferSize / 2, i);
        } else {
            this.mAudioTrack.setStereoVolume(0.0f, 1.0f);
            this.mAudioTrack.setLoopPoints(0, this.mEncodedBufferSize, i);
        }
        this.mAudioTrack.play();
        return ((((long) bArrEncode.length) * 1000) / 48000) + 100;
    }

    public void stop() {
        AudioTrack audioTrack = this.mAudioTrack;
        if (audioTrack != null) {
            audioTrack.release();
            this.mAudioTrack = null;
        }
        this.msgIsSending = false;
    }
}
