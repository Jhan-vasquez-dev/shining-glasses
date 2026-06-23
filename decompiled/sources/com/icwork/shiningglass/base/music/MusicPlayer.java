package com.icwork.shiningglass.base.music;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.ui.utils.LogUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MusicPlayer extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {
    private MediaPlayer mediaPlayer;
    private List<MusicListenter> musicListenterList = new ArrayList();

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (this.mediaPlayer == null) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.mediaPlayer = mediaPlayer;
            mediaPlayer.setAudioStreamType(3);
            this.mediaPlayer.setOnErrorListener(this);
            this.mediaPlayer.setOnPreparedListener(this);
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setOnSeekCompleteListener(this);
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return new BinderImpl();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    public List<Song> searchAndOrderSongName() {
        return searchAndSortOrder("_display_name");
    }

    public List<Song> searchAndOrderSinger() {
        return searchAndSortOrder("artist");
    }

    public List<Song> searchAndSortOrder(String str) throws Throwable {
        ArrayList arrayList = new ArrayList();
        copyAssetsToExternalStorage(getApplicationContext(), "music", arrayList);
        Cursor cursorQuery = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_display_name", "title", "duration", "artist", "album", "year", "mime_type", "_size", "_data"}, "mime_type=? or mime_type=?", new String[]{"audio/mpeg", "audio/x-ms-wma"}, "title");
        if (cursorQuery != null && cursorQuery.moveToFirst()) {
            do {
                Song song = new Song();
                song.setFileName(cursorQuery.getString(1));
                song.setTitle(cursorQuery.getString(2));
                song.setDuration(cursorQuery.getInt(3));
                String string = cursorQuery.getString(4);
                if (TextUtils.isEmpty(string) || string.equals("<unknown>")) {
                    song.setSinger(getResources().getString(R.string.unkown_artist));
                } else {
                    song.setSinger(cursorQuery.getString(4));
                }
                song.setAlbum(cursorQuery.getString(5));
                if (cursorQuery.getString(6) != null) {
                    song.setYear(cursorQuery.getString(6));
                } else {
                    song.setYear("Unknown");
                }
                if ("audio/mpeg".equals(cursorQuery.getString(7).trim())) {
                    song.setType("mp3");
                } else if ("audio/x-ms-wma".equals(cursorQuery.getString(7).trim())) {
                    song.setType("wma");
                }
                if (cursorQuery.getString(8) == null) {
                    song.setSize("Unknown");
                }
                if (cursorQuery.getString(9) != null) {
                    song.setFileUrl(cursorQuery.getString(9));
                }
                if (song.getType().equals("mp3") || song.getType().equals("wma")) {
                    arrayList.add(song);
                }
            } while (cursorQuery.moveToNext());
            cursorQuery.close();
        }
        return arrayList;
    }

    public static void copyAssetsToExternalStorage(Context context, String str, List<Song> list) throws Throwable {
        String[] list2;
        FileOutputStream fileOutputStream;
        InputStream inputStreamOpen;
        AssetManager assets = context.getAssets();
        InputStream inputStream = null;
        try {
            list2 = assets.list(str);
        } catch (IOException e) {
            e.printStackTrace();
            list2 = null;
        }
        if (list2 != null) {
            for (String str2 : list2) {
                try {
                    inputStreamOpen = assets.open(str + "/" + str2);
                    try {
                        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/" + str2);
                        fileOutputStream = new FileOutputStream(file);
                        try {
                            try {
                                copyFile(inputStreamOpen, fileOutputStream);
                                list.add(MP3Info(file.getAbsolutePath(), str2));
                                if (inputStreamOpen != null) {
                                    try {
                                        inputStreamOpen.close();
                                    } catch (IOException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            } catch (Throwable th) {
                                th = th;
                                inputStream = inputStreamOpen;
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException e3) {
                                        e3.printStackTrace();
                                        throw th;
                                    }
                                }
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                                throw th;
                            }
                        } catch (IOException e4) {
                            e = e4;
                            e.printStackTrace();
                            if (inputStreamOpen != null) {
                                inputStreamOpen.close();
                            }
                            if (fileOutputStream != null) {
                            }
                        }
                    } catch (IOException e5) {
                        e = e5;
                        fileOutputStream = null;
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = null;
                    }
                } catch (IOException e6) {
                    e = e6;
                    inputStreamOpen = null;
                    fileOutputStream = null;
                } catch (Throwable th3) {
                    th = th3;
                    fileOutputStream = null;
                }
                fileOutputStream.close();
            }
        }
    }

    private static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int i = inputStream.read(bArr);
            if (i == -1) {
                return;
            } else {
                outputStream.write(bArr, 0, i);
            }
        }
    }

    public static Song MP3Info(String str, String str2) {
        Song song = new Song();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(str);
        song.setTitle(str2.replace(".mp3", ""));
        song.setAlbum(mediaMetadataRetriever.extractMetadata(1));
        song.setSinger(mediaMetadataRetriever.extractMetadata(2));
        song.setYear(mediaMetadataRetriever.extractMetadata(8));
        song.setDuration(Integer.parseInt(mediaMetadataRetriever.extractMetadata(9)));
        song.setFileUrl(str);
        try {
            mediaMetadataRetriever.release();
            return song;
        } catch (IOException e) {
            e.printStackTrace();
            return song;
        }
    }

    public List<Song> searchBySongName(String str) {
        int i;
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        Cursor cursorQuery = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_display_name", "title", "duration", "artist", "album", "year", "mime_type", "_size", "_data"}, "title like? ", new String[]{"%" + str + "%"}, null);
        if (cursorQuery == null || !cursorQuery.moveToFirst()) {
            return arrayList;
        }
        while (true) {
            Song song = new Song();
            song.setFileName(cursorQuery.getString(1));
            song.setTitle(cursorQuery.getString(2));
            song.setDuration(cursorQuery.getInt(3));
            song.setSinger(cursorQuery.getString(4));
            song.setAlbum(cursorQuery.getString(5));
            if (cursorQuery.getString(6) != null) {
                song.setYear(cursorQuery.getString(6));
            } else {
                song.setYear("Unknown");
            }
            if ("audio/mpeg".equals(cursorQuery.getString(7).trim())) {
                song.setType("mp3");
            } else if ("audio/x-ms-wma".equals(cursorQuery.getString(7).trim())) {
                song.setType("wma");
            }
            if (cursorQuery.getString(8) != null) {
                i = i2;
                song.setSize((((cursorQuery.getInt(8) / 1024.0f) / 1024.0f) + "").substring(i, 4) + "M");
            } else {
                i = i2;
                song.setSize("Unknown");
            }
            if (cursorQuery.getString(9) != null) {
                song.setFileUrl(cursorQuery.getString(9));
            }
            arrayList.add(song);
            if (!cursorQuery.moveToNext()) {
                cursorQuery.close();
                return arrayList;
            }
            i2 = i;
        }
    }

    public void play(String str) throws IOException {
        if (str == null || str.isEmpty()) {
            return;
        }
        new AudioRecord(1, 16000, 2, 2, AudioRecord.getMinBufferSize(16000, 2, 2));
        reset();
        this.mediaPlayer.setDataSource(str);
        this.mediaPlayer.prepare();
        start();
    }

    public void rePlay(String str) throws IOException {
        if (str == null || str.isEmpty()) {
            return;
        }
        play(str);
    }

    public void start() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.start();
        for (int i = 0; i < this.musicListenterList.size(); i++) {
            MusicListenter musicListenter = this.musicListenterList.get(i);
            if (musicListenter != null) {
                musicListenter.onStart();
            }
        }
    }

    public void pause() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.pause();
        for (int i = 0; i < this.musicListenterList.size(); i++) {
            MusicListenter musicListenter = this.musicListenterList.get(i);
            if (musicListenter != null) {
                musicListenter.onPause();
            }
        }
    }

    public void stop() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.stop();
    }

    public void reset() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.reset();
    }

    public int getVolume() {
        return ((AudioManager) getSystemService("audio")).getStreamVolume(3);
    }

    public void setVolume(int i) {
        AudioManager audioManager = (AudioManager) getSystemService("audio");
        int streamMaxVolume = audioManager.getStreamMaxVolume(3);
        if (i < 0) {
            i = 0;
        }
        if (streamMaxVolume >= i) {
            streamMaxVolume = i;
        }
        audioManager.setStreamVolume(3, streamMaxVolume, 0);
    }

    public int getDuration() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int i) {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.seekTo(i);
    }

    public boolean isLooping() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.isLooping();
    }

    public boolean isPlaying() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    public void release() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.release();
        this.mediaPlayer = null;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        for (int i3 = 0; i3 < this.musicListenterList.size(); i3++) {
            this.musicListenterList.get(i3).onError(mediaPlayer, i, i2);
        }
        return false;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        for (int i = 0; i < this.musicListenterList.size(); i++) {
            MusicListenter musicListenter = this.musicListenterList.get(i);
            if (musicListenter != null) {
                musicListenter.onStart();
            }
        }
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        for (int i = 0; i < this.musicListenterList.size(); i++) {
            LogUtil.d("===播放完成");
            this.musicListenterList.get(i).onCompletion(mediaPlayer);
        }
    }

    @Override // android.media.MediaPlayer.OnSeekCompleteListener
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        for (int i = 0; i < this.musicListenterList.size(); i++) {
            this.musicListenterList.get(i).onSeekComplete(mediaPlayer);
        }
    }

    public class BinderImpl extends Binder {
        public BinderImpl() {
        }

        public MusicPlayer getService() {
            return MusicPlayer.this;
        }
    }

    public void registerMusicListenter(MusicListenter musicListenter) {
        this.musicListenterList.add(musicListenter);
    }

    public void unregisterMusicListenter(MusicListenter musicListenter) {
        this.musicListenterList.remove(musicListenter);
    }

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }
}
