package org.apache.http.conn.util;

import java.net.IDN;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.util.Args;

/* JADX INFO: loaded from: classes.dex */
public final class PublicSuffixMatcher {
    private final Map<String, DomainType> exceptions;
    private final Map<String, DomainType> rules;

    public PublicSuffixMatcher(Collection<String> collection, Collection<String> collection2) {
        this(DomainType.UNKNOWN, collection, collection2);
    }

    public PublicSuffixMatcher(DomainType domainType, Collection<String> collection, Collection<String> collection2) {
        Args.notNull(domainType, "Domain type");
        Args.notNull(collection, "Domain suffix rules");
        this.rules = new ConcurrentHashMap(collection.size());
        Iterator<String> it = collection.iterator();
        while (it.hasNext()) {
            this.rules.put(it.next(), domainType);
        }
        this.exceptions = new ConcurrentHashMap();
        if (collection2 != null) {
            Iterator<String> it2 = collection2.iterator();
            while (it2.hasNext()) {
                this.exceptions.put(it2.next(), domainType);
            }
        }
    }

    public PublicSuffixMatcher(Collection<PublicSuffixList> collection) {
        Args.notNull(collection, "Domain suffix lists");
        this.rules = new ConcurrentHashMap();
        this.exceptions = new ConcurrentHashMap();
        for (PublicSuffixList publicSuffixList : collection) {
            DomainType type = publicSuffixList.getType();
            Iterator<String> it = publicSuffixList.getRules().iterator();
            while (it.hasNext()) {
                this.rules.put(it.next(), type);
            }
            List<String> exceptions = publicSuffixList.getExceptions();
            if (exceptions != null) {
                Iterator<String> it2 = exceptions.iterator();
                while (it2.hasNext()) {
                    this.exceptions.put(it2.next(), type);
                }
            }
        }
    }

    private static boolean hasEntry(Map<String, DomainType> map, String str, DomainType domainType) {
        DomainType domainType2;
        if (map == null || (domainType2 = map.get(str)) == null) {
            return false;
        }
        return domainType == null || domainType2.equals(domainType);
    }

    private boolean hasRule(String str, DomainType domainType) {
        return hasEntry(this.rules, str, domainType);
    }

    private boolean hasException(String str, DomainType domainType) {
        return hasEntry(this.exceptions, str, domainType);
    }

    public String getDomainRoot(String str) {
        return getDomainRoot(str, null);
    }

    public String getDomainRoot(String str, DomainType domainType) {
        if (str == null || str.startsWith(".")) {
            return null;
        }
        String lowerCase = str.toLowerCase(Locale.ROOT);
        String str2 = null;
        while (lowerCase != null) {
            if (!hasException(IDN.toUnicode(lowerCase), domainType)) {
                if (!hasRule(IDN.toUnicode(lowerCase), domainType)) {
                    int iIndexOf = lowerCase.indexOf(46);
                    String strSubstring = iIndexOf != -1 ? lowerCase.substring(iIndexOf + 1) : null;
                    if (strSubstring != null && hasRule("*." + IDN.toUnicode(strSubstring), domainType)) {
                        break;
                    }
                    if (iIndexOf != -1) {
                        str2 = lowerCase;
                    }
                    lowerCase = strSubstring;
                } else {
                    break;
                }
            } else {
                return lowerCase;
            }
        }
        return str2;
    }

    public boolean matches(String str) {
        return matches(str, null);
    }

    public boolean matches(String str, DomainType domainType) {
        if (str == null) {
            return false;
        }
        if (str.startsWith(".")) {
            str = str.substring(1);
        }
        return getDomainRoot(str, domainType) == null;
    }
}
