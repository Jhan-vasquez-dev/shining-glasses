package okhttp3.internal.graal;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.platform.Jdk9Platform;
import okhttp3.internal.platform.Platform;

/* JADX INFO: compiled from: svm.kt */
/* JADX INFO: loaded from: classes.dex */
@TargetClass(Platform.Companion.class)
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007¨\u0006\u0005"}, d2 = {"Lokhttp3/internal/graal/TargetPlatform;", "", "()V", "findPlatform", "Lokhttp3/internal/platform/Platform;", "okhttp"}, k = 1, mv = {1, 4, 1})
public final class TargetPlatform {
    @Substitute
    public final Platform findPlatform() {
        Jdk9Platform jdk9PlatformBuildIfSupported = Jdk9Platform.INSTANCE.buildIfSupported();
        Intrinsics.checkNotNull(jdk9PlatformBuildIfSupported);
        return jdk9PlatformBuildIfSupported;
    }
}
