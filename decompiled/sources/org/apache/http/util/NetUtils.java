package org.apache.http.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/* JADX INFO: loaded from: classes2.dex */
public final class NetUtils {
    public static void formatAddress(StringBuilder sb, SocketAddress socketAddress) {
        Args.notNull(sb, "Buffer");
        Args.notNull(socketAddress, "Socket address");
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            InetAddress address = inetSocketAddress.getAddress();
            Object hostAddress = address;
            if (address != null) {
                hostAddress = address.getHostAddress();
            }
            sb.append(hostAddress).append(':').append(inetSocketAddress.getPort());
            return;
        }
        sb.append(socketAddress);
    }
}
