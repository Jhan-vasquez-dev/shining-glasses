package androidx.core.location;

import android.location.LocationListener;
import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public interface LocationListenerCompat extends LocationListener {
    @Override // android.location.LocationListener
    default void onProviderDisabled(String str) {
    }

    @Override // android.location.LocationListener
    default void onProviderEnabled(String str) {
    }

    @Override // android.location.LocationListener
    default void onStatusChanged(String str, int i, Bundle bundle) {
    }
}
