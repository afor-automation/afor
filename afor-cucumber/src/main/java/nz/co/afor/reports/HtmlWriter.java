package nz.co.afor.reports;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.yahoo.platform.yui.compressor.CssCompressor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class HtmlWriter implements AutoCloseable {

    private static final String TEMPLATE_PATH = "formatter/";
    private static final String TEMPLATE_LOCATION = TEMPLATE_PATH + "index.html";
    private static final String[] JAVASCRIPT_ASSETS = new String[]{
            "/nz/co/afor/reports/formatter/loader.js",
            "/nz/co/afor/reports/formatter/jquery-1.8.2.min.js",
            "/nz/co/afor/reports/formatter/moment.min.js",
            "/nz/co/afor/reports/formatter/jquery.throttledresize.js",
            "/nz/co/afor/reports/formatter/formatter.js",
            "/nz/co/afor/reports/formatter/render-charts.js"};
    private static final List<String> CSS_ASSETS = List.of(
            "/nz/co/afor/reports/formatter/details-shim.min.css",
            "/nz/co/afor/reports/formatter/print.css",
            "/nz/co/afor/reports/formatter/style.css"
    );

    private final String template;
    private final Writer writer;
    private final File path;
    private boolean initialised = false;
    private boolean streamClosed = false;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public HtmlWriter(File path) throws IOException {
        this.path = path;
        path.mkdirs();
        bundleJs();
        bundleCss();
        this.writer = new OutputStreamWriter(new FileOutputStream(path.getAbsolutePath() + "/index.html"), UTF_8);
        this.template = readResource();
    }

    public void initialise(ReportContext reportContext) {
        initialised = true;
        String template = replacePng(this.template, "{{aforLogoBase64}}", "/nz/co/afor/reports/formatter/aforLogo.png");
        template = replacePng(template, "{{faviconBase64}}", "/nz/co/afor/reports/formatter/favicon.png");
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

    private void bundleCss() {
        StringBuilder compressedCss = new StringBuilder();
        try (FileOutputStream outputStream = new FileOutputStream(path.getAbsolutePath() + "/bundled.min.css")) {
            for (String cssAsset : CSS_ASSETS) {
                try (InputStream cssStream = getClass().getResourceAsStream(cssAsset);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(cssStream), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty() || line.startsWith("\n") || line.startsWith("\r\n") || line.startsWith("//"))
                            continue;
                        compressedCss.append(line.trim());
                    }
                } catch (IOException e) {
                    throw new WriterException(e);
                }
            }
            Reader reader = new StringReader(compressedCss.toString());
            Writer writer = new OutputStreamWriter(outputStream);
            CssCompressor compressor = new CssCompressor(reader);
            compressor.compress(writer, 0);
            writer.flush();
        } catch (IOException e) {
            throw new WriterException(e);
        }
    }

    private void bundleJs() {
        List<SourceFile> inputs = new ArrayList<>();
        for (String jsAsset : JAVASCRIPT_ASSETS) {
            try (InputStream resource = getClass().getResourceAsStream(jsAsset)) {
                inputs.add(SourceFile.fromCode(jsAsset, new String(Objects.requireNonNull(resource).readAllBytes(), UTF_8)));
            } catch (IOException | FileSystemNotFoundException e) {
                throw new WriterException("Bundler failed with resource '" + jsAsset + "'", e);
            }
        }
        Compiler compiler = new Compiler();
        CompilerOptions options = new CompilerOptions();
        options.setStrictModeInput(false);
        options.skipAllCompilerPasses();
        CompilationLevel.WHITESPACE_ONLY.setOptionsForCompilationLevel(options);
        compiler.compile(List.of(), inputs, options);
        try (FileOutputStream outputStream = new FileOutputStream(path.getAbsolutePath() + "/bundled.min.js"); Writer writer = new OutputStreamWriter(outputStream)) {
            writer.write(compiler.toSource());
            writer.flush();
        } catch (IOException e) {
            throw new WriterException(e);
        }
    }

    private String replacePng(String template, String placeholder, String resourcePath) {
        String result;
        try (InputStream logo = getClass().getResourceAsStream(resourcePath)) {
            result = template.replace(placeholder, Base64.getEncoder().encodeToString(logo != null ? logo.readAllBytes() : null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void addResource(String name, byte[] bytes) {
        try {
            Files.write(Path.of(path.getAbsolutePath(), "/", name), bytes);
        } catch (IOException e) {
            throw new WriterException(e);
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource, UTF_8));
        char[] buffer = new char[1024];

        for (int read = reader.read(buffer); read != -1; read = reader.read(buffer)) {
            writer.write(buffer, 0, read);
        }

    }

    private static String readResource() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos, UTF_8));

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
        return baos.toString(UTF_8);
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