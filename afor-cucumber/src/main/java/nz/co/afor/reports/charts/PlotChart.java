package nz.co.afor.reports.charts;

import de.erichseifert.vectorgraphics2d.VectorGraphics2D;
import de.erichseifert.vectorgraphics2d.svg.SVGProcessor;
import de.erichseifert.vectorgraphics2d.util.PageSize;
import io.cucumber.plugin.event.Status;
import nz.co.afor.reports.results.ResultSummary;
import nz.co.afor.reports.results.ScenarioTimelineResult;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class PlotChart {
    private PlotChart() {
    }

    public static void getChart(ResultSummary resultSummary, List<ScenarioTimelineResult> scenarioTimelineResults, OutputStream outputStream) throws IOException {
        int width = 700;
        int height = 330;
        XYChart scatterPlotChart = new XYChartBuilder()
                .width(width)
                .height(height)
                .title("Scenario duration")
                .xAxisTitle("Number of steps")
                .yAxisTitle("Duration in seconds")
                .theme(Styler.ChartTheme.GGPlot2)
                .build();

        scatterPlotChart.getStyler()
                .setChartBackgroundColor(Color.white)
                .setLegendBackgroundColor(Color.white)
                .setPlotBackgroundColor(Color.white)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false);

        scatterPlotChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        scatterPlotChart.getStyler().setChartTitleVisible(false);
        scatterPlotChart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        scatterPlotChart.getStyler().setLegendBorderColor(Color.WHITE);
        scatterPlotChart.getStyler().setPlotGridLinesColor(Color.lightGray);
        scatterPlotChart.getStyler().setPlotGridLinesStroke(new BasicStroke(1.0F, 0, 0, 1.0F, new float[]{1.0F, 5.0F}, 10.0F));
        scatterPlotChart.getStyler().setMarkerSize(8);
        scatterPlotChart.getStyler().setXAxisTickMarkSpacingHint(90);

        Pair<List<Double>, List<Double>> passedResults = getResults(scenarioTimelineResults, Status.PASSED);
        XYSeries passed = scatterPlotChart.addSeries("Passed", passedResults.getLeft(), passedResults.getRight());
        passed.setMarkerColor(new Color(176, 214, 51));
        passed.setMarker(SeriesMarkers.CIRCLE);

        if (resultSummary.getScenarios().getFailed() > 0) {
            Pair<List<Double>, List<Double>> results = getResults(scenarioTimelineResults, Status.FAILED);
            XYSeries failed = scatterPlotChart.addSeries("Failed", results.getLeft(), results.getRight());
            failed.setMarkerColor(new Color(255, 0, 0));
            failed.setMarker(SeriesMarkers.CIRCLE);
        }

        if (resultSummary.getScenarios().getUndefined() > 0) {
            Pair<List<Double>, List<Double>> results = getResults(scenarioTimelineResults, Status.UNDEFINED);
            XYSeries undefined = scatterPlotChart.addSeries("Undefined", results.getLeft(), results.getRight());
            undefined.setFillColor(new Color(255, 197, 0));
            undefined.setMarker(SeriesMarkers.CIRCLE);
        }

        if (resultSummary.getScenarios().getPending() > 0) {
            Pair<List<Double>, List<Double>> results = getResults(scenarioTimelineResults, Status.PENDING);
            XYSeries pending = scatterPlotChart.addSeries("Pending", results.getLeft(), results.getRight());
            pending.setFillColor(new Color(234, 236, 45));
            pending.setMarker(SeriesMarkers.CIRCLE);
        }

        if (resultSummary.getScenarios().getSkipped() > 0) {
            Pair<List<Double>, List<Double>> results = getResults(scenarioTimelineResults, Status.PENDING);
            XYSeries skipped = scatterPlotChart.addSeries("Skipped", results.getLeft(), results.getRight());
            skipped.setFillColor(new Color(45, 234, 236));
            skipped.setMarker(SeriesMarkers.CIRCLE);
        }

        if (resultSummary.getScenarios().getAmbiguous() > 0) {
            Pair<List<Double>, List<Double>> results = getResults(scenarioTimelineResults, Status.PENDING);
            XYSeries ambiguous = scatterPlotChart.addSeries("Ambiguous", results.getLeft(), results.getRight());
            ambiguous.setFillColor(new Color(45, 234, 236));
            ambiguous.setMarker(SeriesMarkers.CIRCLE);
        }

        VectorGraphics2D vectorGraphics2D = new VectorGraphics2D();
        scatterPlotChart.paint(vectorGraphics2D, width, height);
        SVGProcessor svgProcessor = new SVGProcessor();
        svgProcessor.getDocument(vectorGraphics2D.getCommands(), new PageSize(width, height)).writeTo(outputStream);
    }

    private static Pair<List<Double>, List<Double>> getResults(List<ScenarioTimelineResult> scenarioTimelineResults, Status status) {
        List<Double> steps = new ArrayList<>();
        List<Double> duration = new ArrayList<>();
        scenarioTimelineResults.stream().filter(result -> result.getStatus().equals(status)).forEach(scenarioTimelineResult -> {
            duration.add(scenarioTimelineResult.getDurationInSeconds());
            steps.add((double) scenarioTimelineResult.getSteps());
        });
        return new ImmutablePair<>(steps, duration);
    }

}
