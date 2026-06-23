package org.apache.http.impl.client;

/* JADX INFO: loaded from: classes2.dex */
class SystemClock implements Clock {
    SystemClock() {
    }

    @Override // org.apache.http.impl.client.Clock
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
