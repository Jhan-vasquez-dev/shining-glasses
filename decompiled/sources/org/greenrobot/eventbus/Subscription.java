package org.greenrobot.eventbus;

/* JADX INFO: loaded from: classes2.dex */
final class Subscription {
    volatile boolean active = true;
    final Object subscriber;
    final SubscriberMethod subscriberMethod;

    Subscription(Object obj, SubscriberMethod subscriberMethod) {
        this.subscriber = obj;
        this.subscriberMethod = subscriberMethod;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Subscription) {
            Subscription subscription = (Subscription) obj;
            if (this.subscriber == subscription.subscriber && this.subscriberMethod.equals(subscription.subscriberMethod)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.subscriber.hashCode() + this.subscriberMethod.methodString.hashCode();
    }
}
