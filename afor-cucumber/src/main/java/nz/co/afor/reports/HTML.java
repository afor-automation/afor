package nz.co.afor.reports;

import cucumber.runtime.CucumberException;
import cucumber.runtime.io.URLOutputStream;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import gherkin.formatter.Formatter;
import gherkin.formatter.Mappable;
import gherkin.formatter.NiceAppendable;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Matt on 12/03/2016.
 */
public class HTML implements Formatter, Reporter {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Date CREATED_DATE = new Date();
    private static final String JS_FORMATTER_VAR = "formatter";
    private static final String JS_REPORT_FILENAME = "report.js";
    private static final String JS_SUMMARY_FORMATTER_VAR = "formatterSummary";
    private static final String JS_SUMMARY_REPORT_FILENAME = "summaryreport.js";
    private static final String[] TEXT_ASSETS = new String[]{"/nz/co/afor/reports/formatter/formatter.js", "/nz/co/afor/reports/formatter/details-shim.min.js", "/nz/co/afor/reports/formatter/index.html", "/nz/co/afor/reports/formatter/jquery-1.8.2.min.js", "/nz/co/afor/reports/formatter/moment.min.js", "/nz/co/afor/reports/formatter/loader.js", "/nz/co/afor/reports/formatter/render-charts.js", "/nz/co/afor/reports/formatter/style.css", "/nz/co/afor/reports/formatter/details-shim.min.css", "/nz/co/afor/reports/formatter/font1.woff2", "/nz/co/afor/reports/formatter/font3.woff2", "/nz/co/afor/reports/formatter/font2.woff2", "/nz/co/afor/reports/formatter/aforLogoLargeGradient.png"};
    private static final Map<String, String> MIME_TYPES_EXTENSIONS = new HashMap<String, String>() {
        {
            put("image/bmp", "bmp");
            put("image/gif", "gif");
            put("image/jpeg", "jpg");
            put("image/png", "png");
            put("image/svg+xml", "svg");
            put("video/ogg", "ogg");
        }
    };

    private static final List<FeatureResult> featureResults = new ArrayList<>();

    private final URL htmlReportDir;
    private NiceAppendable jsOut;
    private NiceAppendable jsSummaryOut;

    private boolean firstFeature = true;
    private int embeddedIndex;

    public HTML(URL htmlReportDir) {
        this.htmlReportDir = htmlReportDir;
    }

    @Override
    public void uri(String uri) {
        if (firstFeature) {
            jsOut().append("$(document).ready(function() {").append("var ")
                    .append(JS_FORMATTER_VAR).append(" = new CucumberHTML.DOMFormatter($('.afor-report'));");
            firstFeature = false;
        }
        jsFunctionCall("uri", uri);
    }

    @Override
    public void feature(Feature feature) {
        featureResults.add(new FeatureResult(feature));
        jsFunctionCall("feature", feature);
    }

    @Override
    public void background(Background background) {
        jsFunctionCall("background", background);
    }

    @Override
    public void scenario(Scenario scenario) {
        featureResults.get(featureResults.size() - 1).getScenarios().add(new ScenarioResult(scenario));
        jsFunctionCall("scenario", scenario);
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        jsFunctionCall("scenarioOutline", scenarioOutline);
    }

    @Override
    public void examples(Examples examples) {
        jsFunctionCall("examples", examples);
    }

    @Override
    public void step(Step step) {
        FeatureResult featureResult = featureResults.get(featureResults.size() - 1);
        List<ScenarioResult> scenariosResults = featureResult.getScenarios();
        ScenarioResult scenarioResult = scenariosResults.get(scenariosResults.size() - 1);
        scenarioResult.getSteps().add(new StepResult(step));
        jsFunctionCall("step", step);
    }

    @Override
    public void eof() {
    }

    @Override
    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
    }

    @Override
    public void done() {
        if (!firstFeature) {
            jsOut().append("});");
            SimpleDateFormat javascriptFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            jsSummaryOut().append(String.format("$(\"span.title-heading\").ready(function() {$(\"span.title-heading-date\").text(\"%s\")});\n", javascriptFormat.format(CREATED_DATE)));
            jsSummaryOut().append(String.format("var %s = %s", JS_SUMMARY_FORMATTER_VAR, gson.toJson(featureResults)));
            copyReportFiles();
        }
    }

    @Override
    public void close() {
        jsOut().close();
    }

    @Override
    public void startOfScenarioLifeCycle(Scenario scenario) {
        // NoOp
    }

    @Override
    public void endOfScenarioLifeCycle(Scenario scenario) {
        // NoOp
    }

    @Override
    public void result(Result result) {
        FeatureResult featureResult = featureResults.get(featureResults.size() - 1);
        List<ScenarioResult> scenariosResults = featureResult.getScenarios();
        ScenarioResult scenarioResult = scenariosResults.get(scenariosResults.size() - 1);
        for (StepResult stepResult : scenarioResult.getSteps()) {
            if (null == stepResult.getResult()) {
                stepResult.setResult(new Result(result.getStatus(), result.getDuration(), result.getErrorMessage()));
                break;
            }
        }
        jsFunctionCall("result", result);
    }

    @Override
    public void before(Match match, Result result) {
        jsFunctionCall("before", result);
    }

    @Override
    public void after(Match match, Result result) {
        jsFunctionCall("after", result);
    }

    @Override
    public void match(Match match) {
        jsFunctionCall("match", match);
    }

    @Override
    public void embedding(String mimeType, byte[] data) {
        if (mimeType.startsWith("text/")) {
            // just pass straight to the formatter to output in the html
            jsFunctionCall("embedding", mimeType, new String(data));
        } else {
            // Creating a file instead of using data urls to not clutter the js file
            String extension = MIME_TYPES_EXTENSIONS.get(mimeType);
            if (extension != null) {
                StringBuilder fileName = new StringBuilder("embedded").append(embeddedIndex++).append(".").append(extension);
                writeBytesAndClose(data, reportFileOutputStream(fileName.toString()));
                jsFunctionCall("embedding", mimeType, fileName);
            }
        }
    }

    @Override
    public void write(String text) {
        jsFunctionCall("write", text);
    }

    private void jsFunctionCall(String functionName, Object... args) {
        NiceAppendable out = jsOut().append(JS_FORMATTER_VAR + ".").append(functionName).append("(");
        boolean comma = false;
        for (Object arg : args) {
            if (comma) {
                out.append(", ");
            }
            arg = arg instanceof Mappable ? ((Mappable) arg).toMap() : arg;
            String stringArg = gson.toJson(arg);
            out.append(stringArg);
            comma = true;
        }
        out.append(");").println();
    }

    private void copyReportFiles() {
        for (String textAsset : TEXT_ASSETS) {
            InputStream textAssetStream = getClass().getResourceAsStream(textAsset);
            if (textAssetStream == null) {
                throw new CucumberException("Couldn't find " + textAsset + ". Is cucumber-html on your classpath? Make sure you have the right version.");
            }
            String baseName = new File(textAsset).getName();
            writeStreamAndClose(textAssetStream, reportFileOutputStream(baseName));
        }
    }

    private void writeStreamAndClose(InputStream in, OutputStream out) {
        byte[] buffer = new byte[16 * 1024];
        try {
            int len = in.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = in.read(buffer);
            }
            out.close();
        } catch (IOException e) {
            throw new CucumberException("Unable to write to report file item: ", e);
        }
    }

    private void writeBytesAndClose(byte[] buf, OutputStream out) {
        try {
            out.write(buf);
        } catch (IOException e) {
            throw new CucumberException("Unable to write to report file item: ", e);
        }
    }

    private NiceAppendable jsOut() {
        if (jsOut == null) {
            try {
                jsOut = new NiceAppendable(new OutputStreamWriter(reportFileOutputStream(JS_REPORT_FILENAME), "UTF-8"));
            } catch (IOException e) {
                throw new CucumberException(e);
            }
        }
        return jsOut;
    }

    private NiceAppendable jsSummaryOut() {
        if (jsSummaryOut == null) {
            try {
                jsSummaryOut = new NiceAppendable(new OutputStreamWriter(reportFileOutputStream(JS_SUMMARY_REPORT_FILENAME), "UTF-8"));
            } catch (IOException e) {
                throw new CucumberException(e);
            }
        }
        return jsSummaryOut;
    }

    private OutputStream reportFileOutputStream(String fileName) {
        try {
            return new URLOutputStream(new URL(htmlReportDir, fileName));
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

}