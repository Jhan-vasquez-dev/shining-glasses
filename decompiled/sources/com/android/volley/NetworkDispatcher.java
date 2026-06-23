package com.android.volley;

import android.net.TrafficStats;
import android.os.Process;
import android.os.SystemClock;
import java.util.concurrent.BlockingQueue;

/* JADX INFO: loaded from: classes.dex */
public class NetworkDispatcher extends Thread {
    private final Cache mCache;
    private final ResponseDelivery mDelivery;
    private final Network mNetwork;
    private final BlockingQueue<Request<?>> mQueue;
    private volatile boolean mQuit = false;

    public NetworkDispatcher(BlockingQueue<Request<?>> blockingQueue, Network network, Cache cache, ResponseDelivery responseDelivery) {
        this.mQueue = blockingQueue;
        this.mNetwork = network;
        this.mCache = cache;
        this.mDelivery = responseDelivery;
    }

    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    private void addTrafficStatsTag(Request<?> request) {
        TrafficStats.setThreadStatsTag(request.getTrafficStatsTag());
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Process.setThreadPriority(10);
        while (true) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            try {
                Request<?> requestTake = this.mQueue.take();
                try {
                    requestTake.addMarker("network-queue-take");
                    if (requestTake.isCanceled()) {
                        requestTake.finish("network-discard-cancelled");
                    } else {
                        addTrafficStatsTag(requestTake);
                        NetworkResponse networkResponsePerformRequest = this.mNetwork.performRequest(requestTake);
                        requestTake.addMarker("network-http-complete");
                        if (networkResponsePerformRequest.notModified && requestTake.hasHadResponseDelivered()) {
                            requestTake.finish("not-modified");
                        } else {
                            Response<?> networkResponse = requestTake.parseNetworkResponse(networkResponsePerformRequest);
                            requestTake.addMarker("network-parse-complete");
                            if (requestTake.shouldCache() && networkResponse.cacheEntry != null) {
                                this.mCache.put(requestTake.getCacheKey(), networkResponse.cacheEntry);
                                requestTake.addMarker("network-cache-written");
                            }
                            requestTake.markDelivered();
                            this.mDelivery.postResponse(requestTake, networkResponse);
                        }
                    }
                } catch (VolleyError e) {
                    e.setNetworkTimeMs(SystemClock.elapsedRealtime() - jElapsedRealtime);
                    parseAndDeliverNetworkError(requestTake, e);
                } catch (Exception e2) {
                    VolleyLog.e(e2, "Unhandled exception %s", e2.toString());
                    VolleyError volleyError = new VolleyError(e2);
                    volleyError.setNetworkTimeMs(SystemClock.elapsedRealtime() - jElapsedRealtime);
                    this.mDelivery.postError(requestTake, volleyError);
                }
            } catch (InterruptedException unused) {
                if (this.mQuit) {
                    return;
                }
            }
        }
    }

    private void parseAndDeliverNetworkError(Request<?> request, VolleyError volleyError) {
        this.mDelivery.postError(request, request.parseNetworkError(volleyError));
    }
}
