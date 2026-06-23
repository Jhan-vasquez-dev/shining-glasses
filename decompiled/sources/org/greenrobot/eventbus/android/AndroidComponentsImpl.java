package org.greenrobot.eventbus.android;

/* JADX INFO: loaded from: classes2.dex */
public class AndroidComponentsImpl extends AndroidComponents {
    public AndroidComponentsImpl() {
        super(new AndroidLogger("EventBus"), new DefaultAndroidMainThreadSupport());
    }
}
