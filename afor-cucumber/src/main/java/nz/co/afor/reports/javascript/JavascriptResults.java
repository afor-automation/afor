package nz.co.afor.reports.javascript;

import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import nz.co.afor.reports.results.FeatureSummaryResult;
import nz.co.afor.reports.results.ResultSummary;
import nz.co.afor.reports.results.ScenarioTimelineResult;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class JavascriptResults {
    public static void writeHighLevelSummary(ResultSummary resultSummary, OutputStream outputStream) throws IOException {
        outputStream.write("var formatterHighLevelSummary = ".getBytes());
        outputStream.write(new ObjectMapper().writeValueAsBytes(resultSummary));
        outputStream.write(";".getBytes());
    }

    public static void writePerformanceSummary(List<ScenarioTimelineResult> scenarioTimelineResults, String totalRunTime, OutputStream outputStream) throws IOException {
        outputStream.write(("var formatterSummaryDuration = '" + totalRunTime + "';").getBytes());
        outputStream.write("var formatterPerformanceSummary = ".getBytes());
        outputStream.write(new ObjectMapper().writeValueAsBytes(scenarioTimelineResults));
        outputStream.write(";".getBytes());
    }

    public static void writeFeatureSummary(List<FeatureSummaryResult> featureSummaryResults, OutputStream outputStream) throws IOException {
        outputStream.write("var formatterFeatureSummary = ".getBytes());
        outputStream.write(new ObjectMapper().writeValueAsBytes(featureSummaryResults));
        outputStream.write(";".getBytes());
    }
}
