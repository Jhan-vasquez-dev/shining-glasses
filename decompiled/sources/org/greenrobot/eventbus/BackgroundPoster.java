package org.greenrobot.eventbus;

import java.util.logging.Level;

/* JADX INFO: loaded from: classes2.dex */
final class BackgroundPoster implements Runnable, Poster {
    private final EventBus eventBus;
    private volatile boolean executorRunning;
    private final PendingPostQueue queue = new PendingPostQueue();

    BackgroundPoster(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override // org.greenrobot.eventbus.Poster
    public void enqueue(Subscription subscription, Object obj) {
        PendingPost pendingPostObtainPendingPost = PendingPost.obtainPendingPost(subscription, obj);
        synchronized (this) {
            this.queue.enqueue(pendingPostObtainPendingPost);
            if (!this.executorRunning) {
                this.executorRunning = true;
                this.eventBus.getExecutorService().execute(this);
            }
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        PendingPost pendingPostPoll;
        while (true) {
            try {
                pendingPostPoll = this.queue.poll(1000);
            } catch (InterruptedException e) {
                this.eventBus.getLogger().log(Level.WARNING, Thread.currentThread().getName() + " was interruppted", e);
                return;
            } finally {
                this.executorRunning = false;
            }
            if (pendingPostPoll == null) {
                synchronized (this) {
                    pendingPostPoll = this.queue.poll();
                    if (pendingPostPoll == null) {
                        return;
                    }
                    this.executorRunning = false;
                }
            }
            this.eventBus.invokeSubscriber(pendingPostPoll);
        }
    }
}
