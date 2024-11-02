package nz.co.afor.reports.charts;

import de.erichseifert.vectorgraphics2d.VectorGraphics2D;
import de.erichseifert.vectorgraphics2d.svg.SVGProcessor;
import de.erichseifert.vectorgraphics2d.util.PageSize;
import nz.co.afor.reports.results.ResultSummary;
import org.knowm.xchart.AnnotationText;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

import static java.lang.String.format;

public class PieChart {
    private PieChart() {
    }

    public static void getChart(ResultSummary resultSummary, OutputStream outputStream) throws IOException {
        int width = 500;
        int height = 330;
        org.knowm.xchart.PieChart pieChart = new PieChartBuilder()
                .width(width)
                .height(height)
                .title(format("Scenario results (%s in total)", getTotal(resultSummary)))
                .theme(Styler.ChartTheme.GGPlot2)
                .build();

        pieChart.getStyler()
                .setDonutThickness(0.3)
                .setDefaultSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Donut)
                .setChartBackgroundColor(new Color(239, 235, 229))
                .setLegendBackgroundColor(new Color(239, 235, 229))
                .setPlotBackgroundColor(new Color(239, 235, 229))
                .setLegendBorderColor(new Color(239, 235, 229))
                .setChartTitleBoxVisible(false)
                .setAnnotationTextPanelPadding(250)
                .setPlotBorderVisible(false)
                .setAnnotationTextFont(new Font("Arial", Font.BOLD, 44))
                .setChartTitleFont(new Font("Arial", Font.BOLD, 18));

        addCentreText(resultSummary, pieChart, width, height);
        addData(resultSummary, pieChart);

        VectorGraphics2D vectorGraphics2D = new VectorGraphics2D();
        pieChart.paint(vectorGraphics2D, width, height);
        SVGProcessor svgProcessor = new SVGProcessor();
        svgProcessor.getDocument(vectorGraphics2D.getCommands(), new PageSize(width, height)).writeTo(outputStream);
    }

    private static int getTotal(ResultSummary resultSummary) {
        return resultSummary.getScenarios().getPassed() + resultSummary.getScenarios().getFailed() + resultSummary.getScenarios().getUndefined() + resultSummary.getScenarios().getPending();
    }

    private static void addCentreText(ResultSummary resultSummary, org.knowm.xchart.PieChart pieChart, int width, int height) {
        long passedPercentage = Math.round((resultSummary.getScenarios().getPassed().doubleValue() / getTotal(resultSummary)) * 100);
        AnnotationText centreText = new AnnotationText(passedPercentage + " %", ((double) width / 3) + 22, ((double) height / 2) - 11, true);
        centreText.init(pieChart);
        pieChart.addAnnotation(centreText);
    }

    private static void addData(ResultSummary resultSummary, org.knowm.xchart.PieChart pieChart) {
        PieSeries passed = pieChart.addSeries(format("Passed (%d)", resultSummary.getScenarios().getPassed()), resultSummary.getScenarios().getPassed());
        passed.setFillColor(new Color(86, 196, 76));

        if (resultSummary.getScenarios().getFailed() > 0) {
            PieSeries failed = pieChart.addSeries(format("Failed (%d)", resultSummary.getScenarios().getFailed()), resultSummary.getScenarios().getFailed());
            failed.setFillColor(new Color(255, 0, 0));
        }

        if (resultSummary.getScenarios().getUndefined() > 0) {
            PieSeries undefined = pieChart.addSeries(format("Undefined (%d)", resultSummary.getScenarios().getUndefined()), resultSummary.getScenarios().getUndefined());
            undefined.setFillColor(new Color(255, 197, 0));
        }

        if (resultSummary.getScenarios().getPending() > 0) {
            PieSeries pending = pieChart.addSeries(format("Pending (%d)", resultSummary.getScenarios().getPending()), resultSummary.getScenarios().getPending());
            pending.setFillColor(new Color(234, 236, 45));
        }

        if (resultSummary.getScenarios().getSkipped() > 0) {
            PieSeries skipped = pieChart.addSeries(format("Skipped (%d)", resultSummary.getScenarios().getSkipped()), resultSummary.getScenarios().getSkipped());
            skipped.setFillColor(new Color(45, 234, 236));
        }

        if (resultSummary.getScenarios().getAmbiguous() > 0) {
            PieSeries ambiguous = pieChart.addSeries(format("Ambiguous (%d)", resultSummary.getScenarios().getAmbiguous()), resultSummary.getScenarios().getAmbiguous());
            ambiguous.setFillColor(new Color(45, 234, 236));
        }
    }
}
