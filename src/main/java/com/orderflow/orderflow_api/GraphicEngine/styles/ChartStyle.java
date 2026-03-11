package com.orderflow.orderflow_api.GraphicEngine.styles;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;

import java.awt.*;

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
}
