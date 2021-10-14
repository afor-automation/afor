package nz.co.afor.reports;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ReportOutputStream implements Appendable {
    private final Appendable out;

    public ReportOutputStream(Appendable out) {
        this.out = out;
    }

    public void writeObjectToStream(ObjectMapper objectMapper, Object object) {
        try {
            OutputStreamWriter out = (OutputStreamWriter) this.out;
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, object);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ReportOutputStream append(CharSequence csq) {
        try {
            out.append(csq);
            tryFlush();
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ReportOutputStream append(CharSequence csq, int start, int end) {
        try {
            out.append(csq, start, end);
            tryFlush();
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ReportOutputStream append(char c) {
        try {
            out.append(c);
            tryFlush();
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void tryFlush() {
        if (!(out instanceof Flushable)) {
            return;
        }

        try {
            ((Flushable) out).flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            tryFlush();
            if (out instanceof Closeable) {
                ((Closeable) out).close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
