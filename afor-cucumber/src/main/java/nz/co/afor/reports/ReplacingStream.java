package nz.co.afor.reports;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

public class ReplacingStream extends InputStream {

    private final InputStream source;
    private final byte[] search;
    private final byte[] replace;
    private int matchIndex = 0;
    private int bytesIndex = 0;
    private boolean unwinding;
    private int mismatch;

    public ReplacingStream(InputStream source, byte[] search, byte[] replace) {
        if (null == search || search.length < 1)
            throw new InvalidParameterException("search must be populated");
        this.source = source;
        this.search = search;
        this.replace = replace;
    }

    @Override
    public int read() throws IOException {

        if (unwinding) {
            if (bytesIndex < matchIndex) {
                return search[bytesIndex++];
            } else {
                bytesIndex = 0;
                matchIndex = 0;
                unwinding = false;
                return mismatch;
            }
        } else if (matchIndex == search.length) {
            if (bytesIndex == replace.length) {
                bytesIndex = 0;
                matchIndex = 0;
            } else {
                return replace[bytesIndex++];
            }
        }

        int b = source.read();
        if (b == search[matchIndex]) {
            matchIndex++;
        } else if (matchIndex > 0) {
            mismatch = b;
            unwinding = true;
        } else {
            return b;
        }

        return read();

    }

    @Override
    public void close() throws IOException {
        source.close();
    }
}
