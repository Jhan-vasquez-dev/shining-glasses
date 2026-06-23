package org.apache.http.impl.auth;

import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
@Deprecated
public interface SpnegoTokenGenerator {
    byte[] generateSpnegoDERObject(byte[] bArr) throws IOException;
}
