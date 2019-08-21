package nz.co.afor.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.formatter.NiceAppendable;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class ReportOutputStream extends NiceAppendable {
    private final Appendable out;

    public ReportOutputStream(Appendable out) {
        super(out);
        this.out = out;
    }

    public void writeObjectToStream(ObjectMapper objectMapper, Object object) {
        try {
            OutputStreamWriter out = (OutputStreamWriter) this.out;
            objectMapper.writeValue(out, object);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
