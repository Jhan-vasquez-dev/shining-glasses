package org.apache.http.impl.conn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.http.conn.DnsResolver;

/* JADX INFO: loaded from: classes2.dex */
public class SystemDefaultDnsResolver implements DnsResolver {
    public static final SystemDefaultDnsResolver INSTANCE = new SystemDefaultDnsResolver();

    @Override // org.apache.http.conn.DnsResolver
    public InetAddress[] resolve(String str) throws UnknownHostException {
        return InetAddress.getAllByName(str);
    }
}
