package com.cdbwsoft.library.net.entity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.mime.content.FileBody;

/* JADX INFO: loaded from: classes.dex */
public class ProgressFileBody extends FileBody {
    public static final int BLOCK_SIZE = 8192;
    private int mIndex;
    private String mKeyName;
    private ProgressListener mProgressListener;
    private long mTotal;
    private int mUploadTotal;

    public interface ProgressListener {
        void update(long j, long j2, int i);
    }

    public ProgressFileBody(File file) {
        super(file);
        this.mKeyName = "";
        this.mUploadTotal = 0;
    }

    public ProgressFileBody(File file, String str) {
        super(file);
        this.mUploadTotal = 0;
        this.mKeyName = str;
    }

    public int getCurrent() {
        return this.mUploadTotal;
    }

    public long getTotal() {
        long j = this.mTotal;
        return j == 0 ? getContentLength() : j;
    }

    public ProgressListener getProgressListener() {
        return this.mProgressListener;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.mProgressListener = progressListener;
    }

    public void setIndex(int i) {
        this.mIndex = i;
    }

    @Override // org.apache.http.entity.mime.content.FileBody, org.apache.http.entity.mime.content.ContentBody
    public void writeTo(OutputStream outputStream) throws IOException {
        InputStream inputStream = super.getInputStream();
        try {
            byte[] bArr = new byte[8192];
            this.mTotal = getContentLength();
            while (true) {
                int i = inputStream.read(bArr);
                if (i != -1) {
                    outputStream.write(bArr, 0, i);
                    int i2 = this.mUploadTotal + i;
                    this.mUploadTotal = i2;
                    this.mProgressListener.update(i2, this.mTotal, this.mIndex);
                } else {
                    outputStream.flush();
                    return;
                }
            }
        } finally {
            inputStream.close();
        }
    }

    public String getKeyName() {
        return this.mKeyName;
    }

    public void setKeyName(String str) {
        this.mKeyName = str;
    }
}
