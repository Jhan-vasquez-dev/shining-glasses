package org.apache.http.impl.conn.tsccm;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.conn.AbstractPoolEntry;
import org.apache.http.impl.conn.AbstractPooledConnAdapter;

/* JADX INFO: loaded from: classes2.dex */
@Deprecated
public class BasicPooledConnAdapter extends AbstractPooledConnAdapter {
    protected BasicPooledConnAdapter(ThreadSafeClientConnManager threadSafeClientConnManager, AbstractPoolEntry abstractPoolEntry) {
        super(threadSafeClientConnManager, abstractPoolEntry);
        markReusable();
    }

    @Override // org.apache.http.impl.conn.AbstractClientConnAdapter
    protected ClientConnectionManager getManager() {
        return super.getManager();
    }

    @Override // org.apache.http.impl.conn.AbstractPooledConnAdapter
    protected AbstractPoolEntry getPoolEntry() {
        return super.getPoolEntry();
    }

    @Override // org.apache.http.impl.conn.AbstractPooledConnAdapter, org.apache.http.impl.conn.AbstractClientConnAdapter
    protected void detach() {
        super.detach();
    }
}
