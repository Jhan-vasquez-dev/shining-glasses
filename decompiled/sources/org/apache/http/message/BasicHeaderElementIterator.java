package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

/* JADX INFO: loaded from: classes2.dex */
public class BasicHeaderElementIterator implements HeaderElementIterator {
    private CharArrayBuffer buffer;
    private HeaderElement currentElement;
    private ParserCursor cursor;
    private final HeaderIterator headerIt;
    private final HeaderValueParser parser;

    public BasicHeaderElementIterator(HeaderIterator headerIterator, HeaderValueParser headerValueParser) {
        this.currentElement = null;
        this.buffer = null;
        this.cursor = null;
        this.headerIt = (HeaderIterator) Args.notNull(headerIterator, "Header iterator");
        this.parser = (HeaderValueParser) Args.notNull(headerValueParser, "Parser");
    }

    public BasicHeaderElementIterator(HeaderIterator headerIterator) {
        this(headerIterator, BasicHeaderValueParser.INSTANCE);
    }

    private void bufferHeaderValue() {
        this.cursor = null;
        this.buffer = null;
        while (this.headerIt.hasNext()) {
            Header headerNextHeader = this.headerIt.nextHeader();
            if (headerNextHeader instanceof FormattedHeader) {
                FormattedHeader formattedHeader = (FormattedHeader) headerNextHeader;
                this.buffer = formattedHeader.getBuffer();
                ParserCursor parserCursor = new ParserCursor(0, this.buffer.length());
                this.cursor = parserCursor;
                parserCursor.updatePos(formattedHeader.getValuePos());
                return;
            }
            String value = headerNextHeader.getValue();
            if (value != null) {
                CharArrayBuffer charArrayBuffer = new CharArrayBuffer(value.length());
                this.buffer = charArrayBuffer;
                charArrayBuffer.append(value);
                this.cursor = new ParserCursor(0, this.buffer.length());
                return;
            }
        }
    }

    private void parseNextElement() {
        HeaderElement headerElement;
        loop0: while (true) {
            if (!this.headerIt.hasNext() && this.cursor == null) {
                return;
            }
            ParserCursor parserCursor = this.cursor;
            if (parserCursor == null || parserCursor.atEnd()) {
                bufferHeaderValue();
            }
            if (this.cursor != null) {
                while (!this.cursor.atEnd()) {
                    headerElement = this.parser.parseHeaderElement(this.buffer, this.cursor);
                    if (!headerElement.getName().isEmpty() || headerElement.getValue() != null) {
                        break loop0;
                    }
                }
                if (this.cursor.atEnd()) {
                    this.cursor = null;
                    this.buffer = null;
                }
            }
        }
        this.currentElement = headerElement;
    }

    @Override // org.apache.http.HeaderElementIterator, java.util.Iterator
    public boolean hasNext() {
        if (this.currentElement == null) {
            parseNextElement();
        }
        return this.currentElement != null;
    }

    @Override // org.apache.http.HeaderElementIterator
    public HeaderElement nextElement() throws NoSuchElementException {
        if (this.currentElement == null) {
            parseNextElement();
        }
        HeaderElement headerElement = this.currentElement;
        if (headerElement == null) {
            throw new NoSuchElementException("No more header elements available");
        }
        this.currentElement = null;
        return headerElement;
    }

    @Override // java.util.Iterator
    public final Object next() throws NoSuchElementException {
        return nextElement();
    }

    @Override // java.util.Iterator
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
