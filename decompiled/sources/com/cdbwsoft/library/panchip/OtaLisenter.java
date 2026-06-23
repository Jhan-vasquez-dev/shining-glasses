package com.cdbwsoft.library.panchip;

/* JADX INFO: loaded from: classes.dex */
public interface OtaLisenter {
    void OtaDeviceInfo(VersionInfo versionInfo);

    void OtaFail(String str);

    void OtaFileInfo(FileInfo fileInfo);

    void OtaProgress(int i);

    void OtaSuccess();

    void OtaVerifyResult(boolean z, String str);
}
