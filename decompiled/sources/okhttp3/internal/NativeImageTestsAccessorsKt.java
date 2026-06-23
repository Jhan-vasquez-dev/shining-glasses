package okhttp3.internal;

import androidx.core.app.NotificationCompat;
import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Response;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.io.FileSystem;

/* JADX INFO: compiled from: NativeImageTestsAccessors.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u001e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u0016\u001a\u0016\u0010\u0017\u001a\u00020\u0018*\u00020\u00192\n\u0010\u001a\u001a\u00060\u001bR\u00020\u001c\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0017\u0010\u0005\u001a\u0004\u0018\u00010\u0002*\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b\"(\u0010\u000b\u001a\u00020\n*\u00020\u00012\u0006\u0010\t\u001a\u00020\n8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f¨\u0006\u001d"}, d2 = {"connection", "Lokhttp3/internal/connection/RealConnection;", "Lokhttp3/internal/connection/Exchange;", "getConnection", "(Lokhttp3/internal/connection/Exchange;)Lokhttp3/internal/connection/RealConnection;", "exchange", "Lokhttp3/Response;", "getExchange", "(Lokhttp3/Response;)Lokhttp3/internal/connection/Exchange;", "value", "", "idleAtNsAccessor", "getIdleAtNsAccessor", "(Lokhttp3/internal/connection/RealConnection;)J", "setIdleAtNsAccessor", "(Lokhttp3/internal/connection/RealConnection;J)V", "buildCache", "Lokhttp3/Cache;", "file", "Ljava/io/File;", "maxSize", "fileSystem", "Lokhttp3/internal/io/FileSystem;", "finished", "", "Lokhttp3/Dispatcher;", NotificationCompat.CATEGORY_CALL, "Lokhttp3/internal/connection/RealCall$AsyncCall;", "Lokhttp3/internal/connection/RealCall;", "okhttp"}, k = 2, mv = {1, 4, 1})
public final class NativeImageTestsAccessorsKt {
    public static final Cache buildCache(File file, long j, FileSystem fileSystem) {
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(fileSystem, "fileSystem");
        return new Cache(file, j, fileSystem);
    }

    public static final long getIdleAtNsAccessor(RealConnection idleAtNsAccessor) {
        Intrinsics.checkNotNullParameter(idleAtNsAccessor, "$this$idleAtNsAccessor");
        return idleAtNsAccessor.getIdleAtNs();
    }

    public static final void setIdleAtNsAccessor(RealConnection idleAtNsAccessor, long j) {
        Intrinsics.checkNotNullParameter(idleAtNsAccessor, "$this$idleAtNsAccessor");
        idleAtNsAccessor.setIdleAtNs(j);
    }

    public static final Exchange getExchange(Response exchange) {
        Intrinsics.checkNotNullParameter(exchange, "$this$exchange");
        return exchange.getExchange();
    }

    public static final RealConnection getConnection(Exchange connection) {
        Intrinsics.checkNotNullParameter(connection, "$this$connection");
        return connection.getConnection();
    }

    public static final void finished(Dispatcher finished, RealCall.AsyncCall call) {
        Intrinsics.checkNotNullParameter(finished, "$this$finished");
        Intrinsics.checkNotNullParameter(call, "call");
        finished.finished$okhttp(call);
    }
}
