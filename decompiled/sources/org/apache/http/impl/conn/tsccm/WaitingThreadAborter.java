package org.apache.http.impl.conn.tsccm;

/* JADX INFO: loaded from: classes2.dex */
@Deprecated
public class WaitingThreadAborter {
    private boolean aborted;
    private WaitingThread waitingThread;

    public void abort() {
        this.aborted = true;
        WaitingThread waitingThread = this.waitingThread;
        if (waitingThread != null) {
            waitingThread.interrupt();
        }
    }

    public void setWaitingThread(WaitingThread waitingThread) {
        this.waitingThread = waitingThread;
        if (this.aborted) {
            waitingThread.interrupt();
        }
    }
}
