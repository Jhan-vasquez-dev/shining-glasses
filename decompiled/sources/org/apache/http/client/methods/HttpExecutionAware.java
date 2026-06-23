package org.apache.http.client.methods;

import org.apache.http.concurrent.Cancellable;

/* JADX INFO: loaded from: classes.dex */
public interface HttpExecutionAware {
    boolean isAborted();

    void setCancellable(Cancellable cancellable);
}
