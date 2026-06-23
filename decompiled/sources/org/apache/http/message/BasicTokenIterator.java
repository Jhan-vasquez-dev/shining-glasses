package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.HeaderIterator;
import org.apache.http.ParseException;
import org.apache.http.TokenIterator;
import org.apache.http.util.Args;

/* JADX INFO: loaded from: classes2.dex */
public class BasicTokenIterator implements TokenIterator {
    public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
    protected String currentHeader;
    protected String currentToken;
    protected final HeaderIterator headerIt;
    protected int searchPos = findNext(-1);

    protected boolean isTokenSeparator(char c) {
        return c == ',';
    }

    public BasicTokenIterator(HeaderIterator headerIterator) {
        this.headerIt = (HeaderIterator) Args.notNull(headerIterator, "Header iterator");
    }

    @Override // org.apache.http.TokenIterator, java.util.Iterator
    public boolean hasNext() {
        return this.currentToken != null;
    }

    @Override // org.apache.http.TokenIterator
    public String nextToken() throws ParseException, NoSuchElementException {
        String str = this.currentToken;
        if (str == null) {
            throw new NoSuchElementException("Iteration already finished.");
        }
        this.searchPos = findNext(this.searchPos);
        return str;
    }

    @Override // java.util.Iterator
    public final Object next() throws ParseException, NoSuchElementException {
        return nextToken();
    }

    @Override // java.util.Iterator
    public final void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing tokens is not supported.");
    }

    protected int findNext(int i) throws ParseException {
        int iFindTokenSeparator;
        if (i < 0) {
            if (!this.headerIt.hasNext()) {
                return -1;
            }
            this.currentHeader = this.headerIt.nextHeader().getValue();
            iFindTokenSeparator = 0;
        } else {
            iFindTokenSeparator = findTokenSeparator(i);
        }
        int iFindTokenStart = findTokenStart(iFindTokenSeparator);
        if (iFindTokenStart < 0) {
            this.currentToken = null;
            return -1;
        }
        int iFindTokenEnd = findTokenEnd(iFindTokenStart);
        this.currentToken = createToken(this.currentHeader, iFindTokenStart, iFindTokenEnd);
        return iFindTokenEnd;
    }

    protected String createToken(String str, int i, int i2) {
        return str.substring(i, i2);
    }

    protected int findTokenStart(int i) {
        int iNotNegative = Args.notNegative(i, "Search position");
        boolean z = false;
        while (!z) {
            String str = this.currentHeader;
            if (str == null) {
                break;
            }
            int length = str.length();
            while (!z && iNotNegative < length) {
                char cCharAt = this.currentHeader.charAt(iNotNegative);
                if (isTokenSeparator(cCharAt) || isWhitespace(cCharAt)) {
                    iNotNegative++;
                } else {
                    if (!isTokenChar(this.currentHeader.charAt(iNotNegative))) {
                        throw new ParseException("Invalid character before token (pos " + iNotNegative + "): " + this.currentHeader);
                    }
                    z = true;
                }
            }
            if (!z) {
                if (this.headerIt.hasNext()) {
                    this.currentHeader = this.headerIt.nextHeader().getValue();
                    iNotNegative = 0;
                } else {
                    this.currentHeader = null;
                }
            }
        }
        if (z) {
            return iNotNegative;
        }
        return -1;
    }

    protected int findTokenSeparator(int i) {
        int iNotNegative = Args.notNegative(i, "Search position");
        int length = this.currentHeader.length();
        boolean z = false;
        while (!z && iNotNegative < length) {
            char cCharAt = this.currentHeader.charAt(iNotNegative);
            if (isTokenSeparator(cCharAt)) {
                z = true;
            } else {
                if (!isWhitespace(cCharAt)) {
                    if (isTokenChar(cCharAt)) {
                        throw new ParseException("Tokens without separator (pos " + iNotNegative + "): " + this.currentHeader);
                    }
                    throw new ParseException("Invalid character after token (pos " + iNotNegative + "): " + this.currentHeader);
                }
                iNotNegative++;
            }
        }
        return iNotNegative;
    }

    protected int findTokenEnd(int i) {
        Args.notNegative(i, "Search position");
        int length = this.currentHeader.length();
        do {
            i++;
            if (i >= length) {
                break;
            }
        } while (isTokenChar(this.currentHeader.charAt(i)));
        return i;
    }

    protected boolean isWhitespace(char c) {
        return c == '\t' || Character.isSpaceChar(c);
    }

    protected boolean isTokenChar(char c) {
        if (Character.isLetterOrDigit(c)) {
            return true;
        }
        return (Character.isISOControl(c) || isHttpSeparator(c)) ? false : true;
    }

    protected boolean isHttpSeparator(char c) {
        return HTTP_SEPARATORS.indexOf(c) >= 0;
    }
}
