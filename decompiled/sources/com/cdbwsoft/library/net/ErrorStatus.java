package com.cdbwsoft.library.net;

import com.cdbwsoft.library.AppConfig;

/* JADX INFO: loaded from: classes.dex */
public interface ErrorStatus {
    public static final int CLIENT_PROTOCOL_ERROR = 508;
    public static final int IO_ERROR = 510;
    public static final int JSON_PARSE_ERROR = 511;
    public static final int NETWORK_ERROR = -5;
    public static final int OK = AppConfig.RESPONSE_SUCCESS_CODE;
    public static final int REQUEST_FAILED = 512;
    public static final int UNKNOWN_ERROR = -1;
    public static final int UNSUPPORTED_ENCODING = 509;
}
