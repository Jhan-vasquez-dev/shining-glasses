package com.android.volley.toolbox;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import java.util.Map;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

/* JADX INFO: loaded from: classes.dex */
public class HttpHeaderParser {
    public static Cache.Entry parseCacheHeaders(NetworkResponse networkResponse) {
        long j;
        boolean z;
        long j2;
        long j3;
        long j4;
        long dateAsEpoch;
        long j5;
        long j6;
        long jCurrentTimeMillis = System.currentTimeMillis();
        Map<String, String> map = networkResponse.headers;
        String str = map.get("Date");
        long dateAsEpoch2 = str != null ? parseDateAsEpoch(str) : 0L;
        String str2 = map.get(HttpHeaders.CACHE_CONTROL);
        int i = 0;
        if (str2 != null) {
            String[] strArrSplit = str2.split(",");
            z = false;
            j2 = 0;
            j3 = 0;
            while (i < strArrSplit.length) {
                String strTrim = strArrSplit[i].trim();
                if (strTrim.equals("no-cache") || strTrim.equals("no-store")) {
                    return null;
                }
                if (strTrim.startsWith("max-age=")) {
                    try {
                        j2 = Long.parseLong(strTrim.substring(8));
                    } catch (Exception unused) {
                    }
                } else if (strTrim.startsWith("stale-while-revalidate=")) {
                    j3 = Long.parseLong(strTrim.substring(23));
                } else if (strTrim.equals("must-revalidate") || strTrim.equals("proxy-revalidate")) {
                    z = true;
                }
                i++;
            }
            j = 0;
            i = 1;
        } else {
            j = 0;
            z = false;
            j2 = 0;
            j3 = 0;
        }
        String str3 = map.get(HttpHeaders.EXPIRES);
        long dateAsEpoch3 = str3 != null ? parseDateAsEpoch(str3) : j;
        String str4 = map.get(HttpHeaders.LAST_MODIFIED);
        if (str4 != null) {
            j4 = jCurrentTimeMillis;
            dateAsEpoch = parseDateAsEpoch(str4);
        } else {
            j4 = jCurrentTimeMillis;
            dateAsEpoch = j;
        }
        String str5 = map.get(HttpHeaders.ETAG);
        if (i != 0) {
            long j7 = (j2 * 1000) + j4;
            j6 = z ? j7 : (j3 * 1000) + j7;
            j5 = j7;
        } else {
            j5 = (dateAsEpoch2 <= j || dateAsEpoch3 < dateAsEpoch2) ? j : (dateAsEpoch3 - dateAsEpoch2) + j4;
            j6 = j5;
        }
        Cache.Entry entry = new Cache.Entry();
        entry.data = networkResponse.data;
        entry.etag = str5;
        entry.softTtl = j5;
        entry.ttl = j6;
        entry.serverDate = dateAsEpoch2;
        entry.lastModified = dateAsEpoch;
        entry.responseHeaders = map;
        return entry;
    }

    public static long parseDateAsEpoch(String str) {
        try {
            return DateUtils.parseDate(str).getTime();
        } catch (DateParseException unused) {
            return 0L;
        }
    }

    public static String parseCharset(Map<String, String> map, String str) {
        String str2 = map.get("Content-Type");
        if (str2 != null) {
            String[] strArrSplit = str2.split(";");
            for (int i = 1; i < strArrSplit.length; i++) {
                String[] strArrSplit2 = strArrSplit[i].trim().split("=");
                if (strArrSplit2.length == 2 && strArrSplit2[0].equals("charset")) {
                    return strArrSplit2[1];
                }
            }
        }
        return str;
    }

    public static String parseCharset(Map<String, String> map) {
        return parseCharset(map, "ISO-8859-1");
    }
}
