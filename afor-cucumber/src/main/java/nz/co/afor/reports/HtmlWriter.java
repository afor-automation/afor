package nz.co.afor.reports;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class HtmlWriter implements AutoCloseable {

    private static final String TEMPLATE_PATH = "formatter/";
    private static final String TEMPLATE_LOCATION = TEMPLATE_PATH + "index.html";
    private static final String[] TEXT_ASSETS = new String[]{"/nz/co/afor/reports/formatter/aforLogo.png",
            "/nz/co/afor/reports/formatter/details-shim.min.css",
            "/nz/co/afor/reports/formatter/favicon.png",
            "/nz/co/afor/reports/formatter/font1.woff2",
            "/nz/co/afor/reports/formatter/font2.woff2",
            "/nz/co/afor/reports/formatter/font3.woff2",
            "/nz/co/afor/reports/formatter/formatter.js",
            "/nz/co/afor/reports/formatter/jquery-1.8.2.min.js",
            "/nz/co/afor/reports/formatter/jquery.throttledresize.js",
            "/nz/co/afor/reports/formatter/loader.js",
            "/nz/co/afor/reports/formatter/moment.min.js",
            "/nz/co/afor/reports/formatter/print.css",
            "/nz/co/afor/reports/formatter/render-charts.js",
            "/nz/co/afor/reports/formatter/style.css"};


    private final String template;
    private final Writer writer;
    private final File path;
    private boolean initialised = false;
    private boolean streamClosed = false;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public HtmlWriter(File path) throws IOException {
        this.path = path;
        path.mkdirs();
        copyResources();
        this.writer = new OutputStreamWriter(new FileOutputStream(path.getAbsolutePath() + "/index.html"), UTF_8);
        this.template = readResource();
    }

    public void initialise(ReportContext reportContext) {
        initialised = true;
        writeTemplateBetween(writer, template, null, "{{title}}");
        if (Objects.equals(reportContext.getReportHeading(), "Afor Automation")) {
            write(reportContext.getReportHeading() + " - ");
        } else {
            write("Afor Automation - " + reportContext.getReportHeading() + " ");
        }
        write(reportContext.getFormattedTitle());
        writeTemplateBetween(writer, template, "{{title}}", "{{mainheading}}");
        write(reportContext.getReportHeading());
        writeTemplateBetween(writer, template, "{{mainheading}}", "{{heading}}");
        write(reportContext.getFormattedTitle());
        writeTemplateBetween(writer, template, "{{heading}}", "{{detailedreport}}");
    }

    public void addResource(String name, byte[] bytes) {
        try {
            Files.write(Path.of(path.getAbsolutePath(), "/", name), bytes);
        } catch (IOException e) {
            throw new WriterException(e);
        }
    }

    private void copyResources() throws IOException {
        for (String textAsset : TEXT_ASSETS) {
            InputStream textAssetStream = getClass().getResourceAsStream(textAsset);
            if (textAssetStream != null) {
                Files.copy(textAssetStream, Path.of(path.getAbsolutePath() + textAsset.substring(textAsset.lastIndexOf("/"))), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public void write(String message) {
        if (!initialised)
            throw new WriterException("Writer not initialised");
        if (this.streamClosed)
            throw new WriterException("Stream closed");
        try {
            writer.write(message);
        } catch (IOException e) {
            throw new WriterException(e);
        }
    }

    public void close() {
        if (!this.streamClosed) {
            writeTemplateBetween(writer, template, "{{detailedreport}}", null);
            try {
                this.writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.streamClosed = true;
        }
    }

    private static void writeTemplateBetween(Writer writer, String template, String begin, String end) {
        int beginIndex = begin == null ? 0 : template.indexOf(begin) + begin.length();
        int endIndex = end == null ? template.length() : template.indexOf(end);
        try {
            writer.write(template.substring(beginIndex, endIndex));
        } catch (IOException e) {
            throw new WriterException(e);
        }
    }

    private static void writeResource(Writer writer, String name) throws IOException {
        InputStream resource = HtmlWriter.class.getResourceAsStream(name);
        Objects.requireNonNull(resource, name + " could not be loaded");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
        char[] buffer = new char[1024];

        for (int read = reader.read(buffer); read != -1; read = reader.read(buffer)) {
            writer.write(buffer, 0, read);
        }

    }

    private static String readResource() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8));

        try {
            writeResource(writer, TEMPLATE_LOCATION);
        } catch (Throwable var6) {
            try {
                writer.close();
            } catch (Throwable var5) {
                var6.addSuppressed(var5);
            }

            throw var6;
        }

        writer.close();
        return baos.toString(StandardCharsets.UTF_8);
    }

    public static final class WriterException extends RuntimeException {
        public WriterException(String message) {
            super(message);
        }

        public WriterException(String message, Throwable cause) {
            super(message, cause);
        }

        public WriterException(Throwable cause) {
            super(cause);
        }
    }
}