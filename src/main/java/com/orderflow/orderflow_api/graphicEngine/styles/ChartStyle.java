package com.orderflow.orderflow_api.graphicEngine.styles;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.RegularTimePeriod;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChartStyle {

    public static void applyBarStyleToChart(JFreeChart chart, ChartTheme chartTheme) {

        chart.setBackgroundPaint(chartTheme.getChartBackground());

        CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(chartTheme.getPlotBackground());
        plot.setRangeGridlinePaint(chartTheme.getGridColor());

        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        renderer.setSeriesPaint(0, chartTheme.getPlotBackground());
        renderer.setSeriesPaint(1, chartTheme.getSecondaryColor());
        renderer.setSeriesPaint(2, chartTheme.getTertiaryColor());

        chart.getTitle().setPaint(chartTheme.getTextColor());
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 18));
    }

    public static void applyPieStyleToChart(JFreeChart chart, ChartTheme chartTheme) {
        chart.setBackgroundPaint(chartTheme.getChartBackground());

        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setBackgroundPaint(chartTheme.getPlotBackground());

        plot.setSectionPaint("Category 1", chartTheme.getPrimaryColor());
        plot.setSectionPaint("Category 2", chartTheme.getPrimaryColor());
        plot.setSectionPaint("Category 3", chartTheme.getPrimaryColor());

        chart.getTitle().setPaint(chartTheme.getTextColor());
    }

    public static void applyTimeSeriesStyleToChart(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();

        DateAxis axis = (DateAxis) plot.getDomainAxis();

        axis.setDateFormatOverride(
                new SimpleDateFormat("d-MMMM-yyyy", new Locale("pt", "BR"))
        );

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRange(true);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        plot.setRenderer(renderer);

        if (plot.getDataset().getItemCount(0) == 1) {
            RegularTimePeriod period = ((org.jfree.data.time.TimeSeriesCollection) plot.getDataset())
                    .getSeries(0)
                    .getTimePeriod(0);

            RegularTimePeriod next = period.next();

            axis.setRange(period.getStart(), next.getStart());
        }
    }
}
