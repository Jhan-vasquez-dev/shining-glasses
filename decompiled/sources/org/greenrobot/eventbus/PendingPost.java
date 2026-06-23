package org.greenrobot.eventbus;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
final class PendingPost {
    private static final List<PendingPost> pendingPostPool = new ArrayList();
    Object event;
    PendingPost next;
    Subscription subscription;

    private PendingPost(Object obj, Subscription subscription) {
        this.event = obj;
        this.subscription = subscription;
    }

    static PendingPost obtainPendingPost(Subscription subscription, Object obj) {
        List<PendingPost> list = pendingPostPool;
        synchronized (list) {
            int size = list.size();
            if (size > 0) {
                PendingPost pendingPostRemove = list.remove(size - 1);
                pendingPostRemove.event = obj;
                pendingPostRemove.subscription = subscription;
                pendingPostRemove.next = null;
                return pendingPostRemove;
            }
            return new PendingPost(obj, subscription);
        }
    }

    static void releasePendingPost(PendingPost pendingPost) {
        pendingPost.event = null;
        pendingPost.subscription = null;
        pendingPost.next = null;
        List<PendingPost> list = pendingPostPool;
        synchronized (list) {
            if (list.size() < 10000) {
                list.add(pendingPost);
            }
        }
    }
}
