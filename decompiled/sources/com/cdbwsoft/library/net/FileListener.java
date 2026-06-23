package com.cdbwsoft.library.net;

import android.util.Log;
import com.android.volley.Response;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.net.entity.ProgressFileBody;
import com.cdbwsoft.library.net.entity.SuperResponse;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class FileListener implements Response.Listener<SuperResponse> {
    public static final String TAG = "FileListener";
    private FileRequest mFileRequest;

    public List<ProgressFileBody> getFiles() {
        return null;
    }

    public void onProgress(long j, long j2) {
        if (AppConfig.DEBUG) {
            Log.d(TAG, "正在上传：" + ((((long) ((int) j)) / j2) * 100) + "%");
        }
    }

    public void setFileRequest(FileRequest fileRequest) {
        this.mFileRequest = fileRequest;
    }

    public FileRequest getFileRequest() {
        return this.mFileRequest;
    }
}
