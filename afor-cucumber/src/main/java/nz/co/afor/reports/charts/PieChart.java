package nz.co.afor.reports.charts;

import de.erichseifert.vectorgraphics2d.VectorGraphics2D;
import de.erichseifert.vectorgraphics2d.svg.SVGProcessor;
import de.erichseifert.vectorgraphics2d.util.PageSize;
import nz.co.afor.reports.results.ResultSummary;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;

import static java.lang.String.format;

public class PieChart {
    private PieChart() {
    }

    public static void getChart(ResultSummary resultSummary, OutputStream outputStream) throws IOException {
        int width = 500;
        int height = 400;
        org.knowm.xchart.PieChart pieChart = new PieChartBuilder()
                .width(width)
                .height(height)
                .title(format("Scenario results (%s in total)", getTotal(resultSummary)))
                .theme(Styler.ChartTheme.GGPlot2)
                .build();

        pieChart.getStyler()
                .setChartBackgroundColor(Color.white)
                .setLegendBackgroundColor(Color.white)
                .setPlotBackgroundColor(Color.white)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setToolTipsEnabled(true)
                .setToolTipsAlwaysVisible(false)
                .setToolTipType(Styler.ToolTipType.xAndYLabels)
                .setChartTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        addData(resultSummary, pieChart);

        VectorGraphics2D vectorGraphics2D = new VectorGraphics2D();
        pieChart.paint(vectorGraphics2D, width, height);
        SVGProcessor svgProcessor = new SVGProcessor();
        svgProcessor.getDocument(vectorGraphics2D.getCommands(), new PageSize(width, height)).writeTo(outputStream);
    }

    private static int getTotal(ResultSummary resultSummary) {
        return resultSummary.getScenarios().getPassed() + resultSummary.getScenarios().getFailed() + resultSummary.getScenarios().getUndefined() + resultSummary.getScenarios().getPending();
    }

    private static org.knowm.xchart.PieChart addData(ResultSummary resultSummary, org.knowm.xchart.PieChart pieChart) {
        PieSeries passed = pieChart.addSeries("Passed", resultSummary.getScenarios().getPassed());
        passed.setToolTip(resultSummary.getScenarios().getPassed() + " passed scenarios");
        passed.setFillColor(new Color(176, 214, 51));

        //if (resultSummary.getScenarios().getFailed() > 0)
        PieSeries failed = pieChart.addSeries("Failed", resultSummary.getScenarios().getFailed());
        failed.setToolTip(resultSummary.getScenarios().getFailed() + " failed scenarios");
        failed.setFillColor(new Color(255, 0, 0));
        //if (resultSummary.getScenarios().getUndefined() > 0)
        PieSeries undefined = pieChart.addSeries("Undefined", resultSummary.getScenarios().getUndefined());
        undefined.setToolTip(resultSummary.getScenarios().getUndefined() + " undefined scenarios");
        undefined.setFillColor(new Color(255, 197, 0));
        //if (resultSummary.getScenarios().getPending() > 0)
        PieSeries pending = pieChart.addSeries("Pending", resultSummary.getScenarios().getPending());
        pending.setToolTip(resultSummary.getScenarios().getPending() + " pending scenarios");
        pending.setFillColor(new Color(234, 236, 45));
        //if (resultSummary.getScenarios().getSkipped() > 0)
        PieSeries skipped = pieChart.addSeries("Skipped", resultSummary.getScenarios().getSkipped());
        skipped.setToolTip(resultSummary.getScenarios().getSkipped() + " skipped scenarios");
        skipped.setFillColor(new Color(45, 234, 236));
        //if (resultSummary.getScenarios().getAmbiguous() > 0)
        PieSeries ambiguous = pieChart.addSeries("Ambiguous", resultSummary.getScenarios().getAmbiguous());
        ambiguous.setToolTip(resultSummary.getScenarios().getAmbiguous() + " ambiguous scenarios");
        ambiguous.setFillColor(new Color(45, 234, 236));
        return pieChart;
    }
}
