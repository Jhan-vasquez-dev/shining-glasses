package org.apache.http;

/* JADX INFO: loaded from: classes.dex */
public interface Header extends NameValuePair {
    HeaderElement[] getElements() throws ParseException;
}
