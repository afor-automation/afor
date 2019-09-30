package nz.co.afor.reports;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.gagawa.java.elements.*;
import cucumber.api.HookTestStep;
import cucumber.api.PickleStepTestStep;
import cucumber.api.Result;
import cucumber.api.TestCase;
import cucumber.api.event.*;
import cucumber.api.formatter.NiceAppendable;
import cucumber.runtime.CucumberException;
import gherkin.ast.*;
import gherkin.pickles.*;
import nz.co.afor.reports.charts.PieChart;
import nz.co.afor.reports.results.ResultFinalValue;
import nz.co.afor.reports.results.ResultSummary;
import nz.co.afor.reports.results.ResultValue;

import java.io.*;
import java.lang.Object;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HTML implements EventListener {
    private static final ObjectMapper objectMapper = new ObjectMapper(new JsonFactory().configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false)).setDateFormat(new SimpleDateFormat("HH:mm:ss"));
    private static final ZonedDateTime CREATED_DATE = ZonedDateTime.now();
    private static final String JS_FORMATTER_VAR = "formatter";
    private static final String JS_REPORT_FILENAME = "report.js";
    private static final String JS_HIGH_LEVEL_SUMMARY_FORMATTER_VAR = "formatterHighLevelSummary";
    private static final String JS_SUMMARY_FORMATTER_VAR = "formatterSummary";
    private static final String JS_DURATION_VAR = "formatterSummaryDuration";
    private static final String JS_SUMMARY_REPORT_FILENAME = "summaryreport.js";
    private static final String PIE_CHART_REPORT_FILENAME = "piechart.svg";
    private static final String[] TEXT_ASSETS = new String[]{"/nz/co/afor/reports/formatter/formatter.js", "/nz/co/afor/reports/formatter/details-shim.min.js", "/nz/co/afor/reports/formatter/index.html", "/nz/co/afor/reports/formatter/jquery-1.8.2.min.js", "/nz/co/afor/reports/formatter/moment.min.js", "/nz/co/afor/reports/formatter/loader.js", "/nz/co/afor/reports/formatter/jquery.throttledresize.js", "/nz/co/afor/reports/formatter/render-charts.js", "/nz/co/afor/reports/formatter/style.css", "/nz/co/afor/reports/formatter/print.css", "/nz/co/afor/reports/formatter/details-shim.min.css", "/nz/co/afor/reports/formatter/font1.woff2", "/nz/co/afor/reports/formatter/font3.woff2", "/nz/co/afor/reports/formatter/font2.woff2", "/nz/co/afor/reports/formatter/aforLogoLargeGradient.png", "/nz/co/afor/reports/formatter/favicon-16x16.png", "/nz/co/afor/reports/formatter/favicon-32x32.png", "/nz/co/afor/reports/formatter/favicon-96x96.png", "/nz/co/afor/reports/formatter/favicon-120x120.png", "/nz/co/afor/reports/formatter/favicon-152x152.png", "/nz/co/afor/reports/formatter/favicon-180x180.png"};
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
    public static final DateTimeFormatter STEP_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final TestSourcesModel testSources = new TestSourcesModel();
    private final URL htmlReportDir;
    private ReportOutputStream jsOut;
    private ReportOutputStream jsSummaryOut;

    private static final List<FeatureResult> featureResults = new ArrayList<>();

    private boolean firstFeature = true;
    private String currentFeatureFile;
    private Map<String, Object> currentTestCaseMap;
    private ScenarioOutline currentScenarioOutline;
    private Examples currentExamples;
    private int embeddedIndex;

    private EventHandler<TestSourceRead> testSourceReadHandler = this::handleTestSourceRead;
    private EventHandler<TestCaseStarted> caseStartedHandler = this::handleTestCaseStarted;
    private EventHandler<TestStepStarted> stepStartedHandler = this::handleTestStepStarted;
    private EventHandler<TestStepFinished> stepFinishedHandler = this::handleTestStepFinished;
    private EventHandler<EmbedEvent> embedEventhandler = this::handleEmbed;
    private EventHandler<WriteEvent> writeEventhandler = this::handleWrite;
    private EventHandler<TestRunFinished> runFinishedHandler = event -> finishReport();

    public HTML(URL htmlReportDir) {
        this(htmlReportDir, createJsOut(htmlReportDir), createJsSummaryOut(htmlReportDir));
    }

    private HTML(URL htmlReportDir, ReportOutputStream jsOut, ReportOutputStream jsSummaryOut) {
        this.htmlReportDir = htmlReportDir;
        this.jsOut = jsOut;
        this.jsSummaryOut = jsSummaryOut;
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, testSourceReadHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
        publisher.registerHandlerFor(EmbedEvent.class, embedEventhandler);
        publisher.registerHandlerFor(WriteEvent.class, writeEventhandler);
        publisher.registerHandlerFor(TestRunFinished.class, runFinishedHandler);
    }

    private void handleTestSourceRead(TestSourceRead event) {
        testSources.addTestSourceReadEvent(event.uri, event);
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        if (firstFeature) {
            jsOut.append("$(document).ready(function() {").append("var ")
                    .append(JS_FORMATTER_VAR).append(" = new CucumberHTML.DOMFormatter($('.afor-report'));");
            firstFeature = false;
        }
        handleStartOfFeature(event.testCase);
        handleScenario(event.testCase);
        handleScenarioOutline(event.testCase);
        currentTestCaseMap = createTestCase(event.testCase);
        if (testSources.hasBackground(currentFeatureFile, event.testCase.getLine())) {
            jsFunctionCall("background", createBackground(event.testCase));
        } else {
            jsFunctionCall("scenario", currentTestCaseMap);
            currentTestCaseMap = null;
        }
    }

    private void handleTestStepStarted(TestStepStarted event) {
        if (event.testStep instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
            if (isFirstStepAfterBackground(testStep)) {
                jsFunctionCall("scenario", currentTestCaseMap);
                currentTestCaseMap = null;
            }
            jsFunctionCall("step", createTestStep(testStep));
            jsFunctionCall("match", createMatchMap((PickleStepTestStep) event.testStep));
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (event.testStep instanceof PickleStepTestStep) {
            String startTime = ZonedDateTime.now(ReportContextProvider.getTimezone()).minusNanos(null == event.result.getDuration() ? 0 : event.result.getDuration()).format(STEP_TIME_FORMAT);
            jsFunctionCall("result", createResultMap(event.result, startTime));
            FeatureResult featureResult = featureResults.get(featureResults.size() - 1);
            List<ScenarioResult> scenariosResults = featureResult.getScenarios();
            List<ScenarioOutlineResult> scenarioOutlineResults = featureResult.getScenarioOutlines();
            if (scenariosResults.size() > 0 && scenarioOutlineResults.size() > 0) {
                ScenarioResult scenarioResult = scenariosResults.get(scenariosResults.size() - 1);
                ScenarioOutlineResult scenarioOutlineResult = scenarioOutlineResults.get(scenarioOutlineResults.size() - 1);
                if (scenarioResult.getStartTime().isAfter(scenarioOutlineResult.getStartTime())) {
                    PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
                    scenarioResult.getSteps().add(new StepResult(testStep.getPickleStep(), event.result));
                } else {
                    PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
                    scenarioOutlineResult.getSteps().add(new StepResult(testStep.getPickleStep(), event.result));
                }
            } else if (scenariosResults.size() > 0) {
                ScenarioResult scenarioResult = scenariosResults.get(scenariosResults.size() - 1);
                PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
                scenarioResult.getSteps().add(new StepResult(testStep.getPickleStep(), event.result));
            } else {
                ScenarioOutlineResult scenarioOutlineResult = scenarioOutlineResults.get(scenarioOutlineResults.size() - 1);
                PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
                scenarioOutlineResult.getSteps().add(new StepResult(testStep.getPickleStep(), event.result));
            }
        } else if (event.testStep instanceof HookTestStep) {
            HookTestStep hookTestStep = (HookTestStep) event.testStep;
            String startTime = ZonedDateTime.now(ReportContextProvider.getTimezone()).minusNanos(event.result.getDuration()).format(STEP_TIME_FORMAT);
            jsFunctionCall(hookTestStep.getHookType().toString(), createResultMap(event.result, startTime));
        } else {
            throw new IllegalStateException();
        }
    }

    private void handleEmbed(EmbedEvent event) {
        String mimeType = event.mimeType;
        if (mimeType.startsWith("text/")) {
            // just pass straight to the formatter to output in the html
            jsFunctionCall("embedding", mimeType, new String(event.data));
        } else {
            // Creating a file instead of using data urls to not clutter the js file
            String extension = MIME_TYPES_EXTENSIONS.get(mimeType);
            if (extension != null) {
                StringBuilder fileName = new StringBuilder("embedded").append(embeddedIndex++).append(".").append(extension);
                writeBytesToURL(event.data, toUrl(fileName.toString()));
                jsFunctionCall("embedding", mimeType, fileName);
            }
        }
    }

    private void handleWrite(WriteEvent event) {
        jsFunctionCall("write", event.text);
    }

    public ResultSummary getSummaryTotals() {
        ResultSummary resultSummary = new ResultSummary();
        for (FeatureResult featureResult : featureResults) {
            ResultFinalValue featureResultFinalValue = new ResultFinalValue();
            for (ScenarioResult scenarioResult : featureResult.getScenarios()) {
                ResultFinalValue scenarioResultFinalValue = new ResultFinalValue();
                for (StepResult stepResult : scenarioResult.getSteps()) {
                    if (null != stepResult.getResult()) {
                        Result.Type status = stepResult.getResult();
                        resultSummary.getSteps().addResult(status);
                        scenarioResultFinalValue.addStatus(status);
                    }
                }
                resultSummary.getScenarios().addResult(scenarioResultFinalValue.getResultValue());
                featureResultFinalValue.addStatus(scenarioResultFinalValue.getResultValue());
            }
            for (ScenarioOutlineResult scenarioOutlineResult : featureResult.getScenarioOutlines()) {
                ResultFinalValue scenarioResultFinalValue = new ResultFinalValue();
                for (StepResult stepResult : scenarioOutlineResult.getSteps()) {
                    if (null != stepResult.getResult()) {
                        Result.Type status = stepResult.getResult();
                        resultSummary.getSteps().addResult(status);
                        scenarioResultFinalValue.addStatus(status);
                    }
                }
                resultSummary.getScenarios().addResult(scenarioResultFinalValue.getResultValue());
                featureResultFinalValue.addStatus(scenarioResultFinalValue.getResultValue());
            }
            resultSummary.getFeatures().addResult(featureResultFinalValue.getResultValue());
        }
        return resultSummary;
    }

    private String getDetailedDuration(long totalDuration) {
        long hours = Math.abs(totalDuration / (60 * 60 * 1000));
        long minutes = Math.abs(((totalDuration - (hours * 60 * 60 * 1000))) / (60 * 1000));
        long seconds = Math.abs(((totalDuration - (hours * 60 * 60 * 1000)) - (minutes * 60 * 1000)) / 1000);
        String duration;
        if (hours > 0) {
            duration = String.format("%s hour%s, %s min%s", hours, hours > 1 ? "s" : "", minutes, minutes > 1 ? "s" : "");
        } else if (minutes > 0) {
            duration = String.format("%s min%s, %s sec%s", minutes, minutes > 1 ? "s" : "", seconds, seconds > 1 ? "s" : "");
        } else if (seconds > 0) {
            duration = String.format("%s second%s", seconds, seconds != 1 ? "s" : "");
        } else {
            long milliseconds = Math.abs(totalDuration);
            duration = String.format("%s millisecond%s", milliseconds, milliseconds != 1 ? "s" : "");
        }
        return duration;
    }

    private String getDuration() {
        long totalDuration = (ZonedDateTime.now().toEpochSecond() - CREATED_DATE.toEpochSecond()) * 1000;
        long hours = Math.abs(totalDuration / (60 * 60 * 1000));
        long minutes = Math.abs(((totalDuration - (hours * 60 * 60 * 1000))) / (60 * 1000));
        long seconds = Math.abs(((totalDuration - (hours * 60 * 60 * 1000)) - (minutes * 60 * 1000)) / 1000);
        String duration;
        if (hours > 0) {
            duration = String.format("%s hour%s, %s min%s", hours, hours > 1 ? "s" : "", minutes, minutes > 1 ? "s" : "");
        } else if (minutes > 0) {
            duration = String.format("%s min%s, %s sec%s", minutes, minutes > 1 ? "s" : "", seconds, seconds > 1 ? "s" : "");
        } else {
            duration = String.format("%s second%s", seconds, seconds != 1 ? "s" : "");
        }
        return duration;
    }

    private void finishReport() {
        if (!firstFeature) {
            jsOut.append("});");
            jsSummaryOut.append(String.format("$(\"span.title-heading\").ready(function() {$(\"span.title-heading-name\").text(\"%s\");$(\"html title\").append(document.createTextNode(\" - %s\"))});\n", ReportContextProvider.getReportTitle(), ReportContextProvider.getReportTitle()));
            jsSummaryOut.append(String.format("$(\"span.title-heading\").ready(function() {$(\"span.title-heading-date\").text(\"%s\")});\n", ReportContextProvider.getDateFormat().format(CREATED_DATE.withZoneSameInstant(ReportContextProvider.getTimezone()))));
            jsSummaryOut.append(String.format("var %s = '%s';\n", JS_DURATION_VAR, getDuration()));
            jsSummaryOut.append(String.format("var %s = ", JS_HIGH_LEVEL_SUMMARY_FORMATTER_VAR));
            ResultSummary summaryTotals = getSummaryTotals();
            jsSummaryOut.writeObjectToStream(objectMapper, summaryTotals);
            jsSummaryOut.append(";\n");
            jsSummaryOut.append(String.format("var %s = ", JS_SUMMARY_FORMATTER_VAR));
            jsSummaryOut.writeObjectToStream(objectMapper, featureResults);
            jsSummaryOut.append(";\n");
            writePieChart(summaryTotals);
            copyReportFiles();
        }
        jsOut.close();
    }

    private void writePieChart(ResultSummary summaryTotals) {
        try {
            URLOutputStream outputStream = new URLOutputStream(new URL(htmlReportDir, PIE_CHART_REPORT_FILENAME));
            PieChart.getChart(summaryTotals, outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleStartOfFeature(TestCase testCase) {
        if (currentFeatureFile == null || !currentFeatureFile.equals(testCase.getUri())) {
            currentFeatureFile = testCase.getUri();
            jsFunctionCall("uri", currentFeatureFile);
            jsFunctionCall("feature", createFeature(testCase));
        }
    }

    private Map<String, Object> createFeature(TestCase testCase) {
        Map<String, Object> featureMap = new HashMap<>();
        Feature feature = testSources.getFeature(testCase.getUri());
        if (feature != null) {
            FeatureResult featureResult = new FeatureResult(feature);
            featureResults.add(featureResult);
            featureMap.put("keyword", feature.getKeyword());
            featureMap.put("name", feature.getName());
            featureMap.put("anchor", featureResult.getAnchor());
            featureMap.put("description", feature.getDescription() != null ? feature.getDescription() : "");
            if (!feature.getTags().isEmpty()) {
                featureMap.put("tags", createTagList(feature.getTags()));
            }
        }
        return featureMap;
    }

    private List<Map<String, Object>> createTagList(List<Tag> tags) {
        List<Map<String, Object>> tagList = new ArrayList<>();
        for (Tag tag : tags) {
            Map<String, Object> tagMap = new HashMap<>();
            tagMap.put("name", tag.getName());
            tagList.add(tagMap);
        }
        return tagList;
    }

    private void handleScenario(TestCase testCase) {
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testCase.getLine());
        if (!TestSourcesModel.isScenarioOutlineScenario(astNode)) {
            Scenario scenario = (Scenario) TestSourcesModel.getScenarioDefinition(astNode);
            featureResults.get(featureResults.size() - 1).getScenarios().add(new ScenarioResult(scenario));
        }
    }

    private void handleScenarioOutline(TestCase testCase) {
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testCase.getLine());
        if (TestSourcesModel.isScenarioOutlineScenario(astNode)) {
            ScenarioOutline scenarioOutline = (ScenarioOutline) TestSourcesModel.getScenarioDefinition(astNode);
            if (currentScenarioOutline == null || !currentScenarioOutline.equals(scenarioOutline)) {
                currentScenarioOutline = scenarioOutline;
                featureResults.get(featureResults.size() - 1).getScenarioOutlines().add(new ScenarioOutlineResult(scenarioOutline));
                jsFunctionCall("scenarioOutline", createScenarioOutline(currentScenarioOutline));
                addOutlineStepsToReport(scenarioOutline);
            } else if (currentScenarioOutline.equals(scenarioOutline)) {
                currentScenarioOutline = scenarioOutline;
                featureResults.get(featureResults.size() - 1).getScenarioOutlines().add(new ScenarioOutlineResult(scenarioOutline));
            }
            Examples examples = (Examples) astNode.parent.node;
            if (currentExamples == null || !currentExamples.equals(examples)) {
                currentExamples = examples;
                jsFunctionCall("examples", createExamples(currentExamples));
            }
        } else {
            currentScenarioOutline = null;
            currentExamples = null;
        }
    }

    private Map<String, Object> createScenarioOutline(ScenarioOutline scenarioOutline) {
        Map<String, Object> scenarioOutlineMap = new HashMap<>();
        scenarioOutlineMap.put("name", scenarioOutline.getName());
        scenarioOutlineMap.put("keyword", scenarioOutline.getKeyword());
        scenarioOutlineMap.put("description", scenarioOutline.getDescription() != null ? scenarioOutline.getDescription() : "");
        if (!scenarioOutline.getTags().isEmpty()) {
            scenarioOutlineMap.put("tags", createTagList(scenarioOutline.getTags()));
        }
        return scenarioOutlineMap;
    }

    private void addOutlineStepsToReport(ScenarioOutline scenarioOutline) {
        for (Step step : scenarioOutline.getSteps()) {
            Map<String, Object> stepMap = new HashMap<>();
            stepMap.put("name", step.getText());
            stepMap.put("keyword", step.getKeyword());
            if (step.getArgument() != null) {
                Node argument = step.getArgument();
                if (argument instanceof DocString) {
                    stepMap.put("doc_string", createDocStringMap((DocString) argument));
                } else if (argument instanceof DataTable) {
                    stepMap.put("rows", createDataTableList((DataTable) argument));
                }
            }
            jsFunctionCall("step", stepMap);
        }
    }

    private Map<String, Object> createDocStringMap(DocString docString) {
        Map<String, Object> docStringMap = new HashMap<>();
        docStringMap.put("value", docString.getContent());
        return docStringMap;
    }

    private List<Map<String, Object>> createDataTableList(DataTable dataTable) {
        List<Map<String, Object>> rowList = new ArrayList<>();
        for (TableRow row : dataTable.getRows()) {
            rowList.add(createRowMap(row));
        }
        return rowList;
    }

    private Map<String, Object> createRowMap(TableRow row) {
        Map<String, Object> rowMap = new HashMap<>();
        rowMap.put("cells", createCellList(row));
        return rowMap;
    }

    private List<String> createCellList(TableRow row) {
        List<String> cells = new ArrayList<>();
        for (TableCell cell : row.getCells()) {
            cells.add(cell.getValue());
        }
        return cells;
    }

    private Map<String, Object> createExamples(Examples examples) {
        Map<String, Object> examplesMap = new HashMap<>();
        examplesMap.put("name", examples.getName());
        examplesMap.put("keyword", examples.getKeyword());
        examplesMap.put("description", examples.getDescription() != null ? examples.getDescription() : "");
        List<Map<String, Object>> rowList = new ArrayList<>();
        rowList.add(createRowMap(examples.getTableHeader()));
        for (TableRow row : examples.getTableBody()) {
            rowList.add(createRowMap(row));
        }
        examplesMap.put("rows", rowList);
        if (!examples.getTags().isEmpty()) {
            examplesMap.put("tags", createTagList(examples.getTags()));
        }
        return examplesMap;
    }

    private Map<String, Object> createTestCase(TestCase testCase) {
        Map<String, Object> testCaseMap = new HashMap<>();
        testCaseMap.put("name", testCase.getName());
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testCase.getLine());
        if (astNode != null) {
            ScenarioDefinition scenarioDefinition = TestSourcesModel.getScenarioDefinition(astNode);
            testCaseMap.put("keyword", scenarioDefinition.getKeyword());
            testCaseMap.put("description", scenarioDefinition.getDescription() != null ? scenarioDefinition.getDescription() : "");
        }
        if (!testCase.getTags().isEmpty()) {
            List<Map<String, Object>> tagList = new ArrayList<>();
            for (PickleTag tag : testCase.getTags()) {
                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("name", tag.getName());
                tagList.add(tagMap);
            }
            testCaseMap.put("tags", tagList);
        }
        return testCaseMap;
    }

    private Map<String, Object> createBackground(TestCase testCase) {
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testCase.getLine());
        if (astNode != null) {
            Background background = TestSourcesModel.getBackgroundForTestCase(astNode);
            Map<String, Object> testCaseMap = new HashMap<>();
            testCaseMap.put("name", background.getName());
            testCaseMap.put("keyword", background.getKeyword());
            testCaseMap.put("description", background.getDescription() != null ? background.getDescription() : "");
            return testCaseMap;
        }
        return null;
    }

    private boolean isFirstStepAfterBackground(PickleStepTestStep testStep) {
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testStep.getStepLine());
        if (astNode != null) {
            if (currentTestCaseMap != null && !TestSourcesModel.isBackgroundStep(astNode)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Object> createTestStep(PickleStepTestStep testStep) {
        Map<String, Object> stepMap = new HashMap<>();
        stepMap.put("name", testStep.getStepText());
        if (!testStep.getStepArgument().isEmpty()) {
            Argument argument = testStep.getStepArgument().get(0);
            if (argument instanceof PickleString) {
                stepMap.put("doc_string", createDocStringMap((PickleString) argument));
            } else if (argument instanceof PickleTable) {
                stepMap.put("rows", createDataTableList((PickleTable) argument));
            }
        }
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testStep.getStepLine());
        if (astNode != null) {
            Step step = (Step) astNode.node;
            stepMap.put("keyword", step.getKeyword());
        }

        return stepMap;
    }

    private Map<String, Object> createDocStringMap(PickleString docString) {
        Map<String, Object> docStringMap = new HashMap<>();
        docStringMap.put("value", docString.getContent());
        return docStringMap;
    }

    private List<Map<String, Object>> createDataTableList(PickleTable dataTable) {
        List<Map<String, Object>> rowList = new ArrayList<>();
        for (PickleRow row : dataTable.getRows()) {
            rowList.add(createRowMap(row));
        }
        return rowList;
    }

    private Map<String, Object> createRowMap(PickleRow row) {
        Map<String, Object> rowMap = new HashMap<>();
        rowMap.put("cells", createCellList(row));
        return rowMap;
    }

    private List<String> createCellList(PickleRow row) {
        List<String> cells = new ArrayList<>();
        for (PickleCell cell : row.getCells()) {
            cells.add(cell.getValue());
        }
        return cells;
    }

    private Map<String, Object> createMatchMap(PickleStepTestStep testStep) {
        Map<String, Object> matchMap = new HashMap<>();
        String location = testStep.getCodeLocation();
        if (location != null) {
            matchMap.put("location", location);
        }
        return matchMap;
    }

    private Map<String, Object> createResultMap(Result result, String startTime) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", result.getStatus().lowerCaseName());
        resultMap.put("duration", result.getDuration());
        resultMap.put("startTime", startTime);
        resultMap.put("durationReadable", getDetailedDuration(null != result.getDuration() ? result.getDuration() / 1000000 : 0));
        if (result.getErrorMessage() != null) {
            resultMap.put("error_message", result.getErrorMessage());
        }
        return resultMap;
    }

    private void jsFunctionCall(String functionName, Object... args) {
        NiceAppendable out = jsOut.append(JS_FORMATTER_VAR + ".").append(functionName).append("(");
        boolean comma = false;
        for (Object arg : args) {
            if (comma) {
                out.append(", ");
            }
            String stringArg = null;
            try {
                stringArg = objectMapper.writeValueAsString(arg);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            out.append(stringArg);
            comma = true;
        }
        out.append(");").println();
    }

    private void copyReportFiles() {
        if (htmlReportDir == null) {
            return;
        }
        for (String textAsset : TEXT_ASSETS) {
            InputStream textAssetStream = getClass().getResourceAsStream(textAsset);
            if (textAssetStream == null) {
                throw new CucumberException("Couldn't find " + textAsset + ". Is cucumber-html on your classpath? Make sure you have the right version.");
            }
            String fileName = new File(textAsset).getName();
            if (fileName.endsWith(".html")) {
                ReplacingStream replacingStream = new ReplacingStream(writeFeatureResultTable(textAssetStream), "${chart-table2}".getBytes(), "Testing2".getBytes());
                writeStreamToURL(replacingStream, toUrl(fileName));
            } else {
                writeStreamToURL(textAssetStream, toUrl(fileName));
            }
        }
    }

    private ReplacingStream writeFeatureResultTable(InputStream inputStream) {
        boolean extendedTable = (getSummaryTotals().getFeatures().getUndefined() + getSummaryTotals().getFeatures().getPending() + getSummaryTotals().getFeatures().getSkipped() + getSummaryTotals().getFeatures().getAmbiguous()) > 0;
        Table table = new Table().setCSSClass("feature-results");
        Tr tableHeaderRow = new Tr().setCSSClass("headerRow");
        tableHeaderRow.appendChild(new Th().setCSSClass("headerCell").appendText("Feature"));
        tableHeaderRow.appendChild(new Th().setCSSClass("headerCell").appendText("Passed"));
        tableHeaderRow.appendChild(new Th().setCSSClass("headerCell").appendText("Failed"));
        if (extendedTable) {
            tableHeaderRow.appendChild(new Th().appendText("Undefined"));
            tableHeaderRow.appendChild(new Th().appendText("Pending"));
            tableHeaderRow.appendChild(new Th().appendText("Skipped"));
            tableHeaderRow.appendChild(new Th().appendText("Ambiguous"));
        }
        table.appendChild(tableHeaderRow);
        for (FeatureResult featureResult : featureResults) {
            ResultValue resultValue = featureResult.getResultCount();
            Tr resultRow = new Tr().setCSSClass("tableRow").appendChild(new Td().appendChild(new A().setHref("#" + featureResult.getAnchor()).appendText(featureResult.getName())));
            resultRow.appendChild(new Td().appendText(resultValue.getPassed().toString()));
            resultRow.appendChild(new Td().appendText(resultValue.getFailed().toString()));
            if (extendedTable) {
                resultRow.appendChild(new Td().appendText(resultValue.getUndefined().toString()));
                resultRow.appendChild(new Td().appendText(resultValue.getPending().toString()));
                resultRow.appendChild(new Td().appendText(resultValue.getSkipped().toString()));
                resultRow.appendChild(new Td().appendText(resultValue.getAmbiguous().toString()));
            }
            table.appendChild(resultRow);
        }
        return new ReplacingStream(inputStream, "${feature-result-table}".getBytes(), table.write().getBytes());
    }

    private URL toUrl(String fileName) {
        try {
            return new URL(htmlReportDir, fileName);
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private static void writeStreamToURL(InputStream in, URL url) {
        OutputStream out = createReportFileOutputStream(url);
        byte[] buffer = new byte[16384];

        try {
            for (int len = in.read(buffer); len != -1; len = in.read(buffer)) {
                out.write(buffer, 0, len);
            }
        } catch (IOException var8) {
            throw new CucumberException("Unable to write to report file item: ", var8);
        } finally {
            closeQuietly(out);
        }

    }

    private static void writeBytesToURL(byte[] buf, URL url) throws CucumberException {
        OutputStream out = createReportFileOutputStream(url);

        try {
            out.write(buf);
        } catch (IOException var7) {
            throw new CucumberException("Unable to write to report file item: ", var7);
        } finally {
            closeQuietly(out);
        }

    }

    private static ReportOutputStream createPieChartOut(URL htmlReportDir) {
        try {
            return new ReportOutputStream(new OutputStreamWriter(createReportFileOutputStream(new URL(htmlReportDir, PIE_CHART_REPORT_FILENAME)), "UTF-8"));
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private static ReportOutputStream createJsOut(URL htmlReportDir) {
        try {
            return new ReportOutputStream(new OutputStreamWriter(createReportFileOutputStream(new URL(htmlReportDir, JS_REPORT_FILENAME)), "UTF-8"));
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private static ReportOutputStream createJsSummaryOut(URL htmlReportDir) {
        try {
            return new ReportOutputStream(new OutputStreamWriter(createReportFileOutputStream(new URL(htmlReportDir, JS_SUMMARY_REPORT_FILENAME)), "UTF-8"));
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private static OutputStream createReportFileOutputStream(URL url) {
        try {
            return new URLOutputStream(url);
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private static void closeQuietly(Closeable out) {
        try {
            out.close();
        } catch (IOException ignored) {
            // go gentle into that good night
        }
    }

}
