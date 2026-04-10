package com.orderflow.orderflow_api.graphicEngine.charts;

import com.orderflow.orderflow_api.graphicEngine.datasets.DatasetFactory;
import com.orderflow.orderflow_api.graphicEngine.styles.ChartStyle;
import com.orderflow.orderflow_api.graphicEngine.styles.ChartTheme;
import com.orderflow.orderflow_api.graphicEngine.styles.constants.StylesAppConsts;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

@Component
public class ChartEngine {


    public void generateChartExample() throws IOException {

        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.addValue(46, "Sells", "Jan");
            dataset.addValue(28, "Sells", "Feb");
            dataset.addValue(35, "sells", "Mar");
            dataset.addValue(45, "Sells", "Apr");
            dataset.addValue(42, "Sells", "May");
            dataset.addValue(34, "Sells", "Jun");
            dataset.addValue(39, "sells", "Jul");
            dataset.addValue(42, "Sells", "Ago");
            dataset.addValue(37, "Sells", "Sep");
            dataset.addValue(34, "Sells", "Oct");
            dataset.addValue(43, "sells", "Nov");
            dataset.addValue(52, "Sells", "Dez");

            JFreeChart chart = ChartFactory.createBarChart(
                    "Seller Monthly Chart ",
                    "month",
                    "value",
                    dataset
            );

            int width = 800;
            int height = 600;

            ChartStyle.applyBarStyleToChart(chart, StylesAppConsts.DARK_THEME);

            SVGGraphics2D svgGraphics2D = new SVGGraphics2D(width, height);

            chart.draw(svgGraphics2D, new Rectangle(width, height));

            String svgElement = svgGraphics2D.getSVGElement();

            FileWriter out = new FileWriter("chart-exemplo.svg");
            out.write(svgElement);
            out.close();
            System.out.println("SVG gerado na raiz do projeto");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateChartSizeCustom(int width, int height, String theme) throws IOException {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.addValue(46, "Sells", "Jan");
            dataset.addValue(28, "Sells", "Feb");
            dataset.addValue(35, "sells", "Mar");
            dataset.addValue(45, "Sells", "Apr");
            dataset.addValue(42, "Sells", "May");
            dataset.addValue(34, "Sells", "Jun");
            dataset.addValue(39, "sells", "Jul");
            dataset.addValue(42, "Sells", "Ago");
            dataset.addValue(37, "Sells", "Sep");
            dataset.addValue(34, "Sells", "Oct");
            dataset.addValue(43, "sells", "Nov");
            dataset.addValue(52, "Sells", "Dez");

            JFreeChart chart = ChartFactory.createBarChart(
                    "Seller Monthly Chart ",
                    "month",
                    "value",
                    dataset
            );

            ChartStyle.applyBarStyleToChart(chart, StylesAppConsts.DARK_THEME);

            SVGGraphics2D svgGraphics2D = new SVGGraphics2D(width, height);

            chart.draw(svgGraphics2D, new Rectangle(width, height));

            String svgElement = svgGraphics2D.getSVGElement();

            FileWriter out = new FileWriter("/images/chart-exemplo.svg");
            out.write(svgElement);
            out.close();

            System.out.println("SVG gerado na pasta images");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T, R> JFreeChart createBarChartSvg(
            List<T> list,
            Function<T, R> extractor,
            Integer qntyCategories,
            String chartName,
            String categoryAxisLabel,
            String valueAxisLabel
            ){

        DefaultCategoryDataset dataset = DatasetFactory.createDefaultCategoryDatasetFromMap(list, extractor, qntyCategories, chartName);

        JFreeChart chart = ChartFactory.createBarChart(
                chartName,
                categoryAxisLabel,
                valueAxisLabel,
                dataset
        );
        return chart;
    }

    public static <T, R> JFreeChart createPieChartSvg(
            List<T> list,
            Function<T, R> extractor,
            Integer qntyCategories,
            String chartName
        ){
        DefaultPieDataset dataset = DatasetFactory.createDefaultPieDatasetFromMap(list, extractor, qntyCategories, chartName);

        JFreeChart chart = ChartFactory.createPieChart(
                chartName,
                dataset,
                true,
                true,
                false
        );
        return chart;
    }

    public static <T, R> JFreeChart createTimeSeriesChartSvg(
            List<T> list,
            Function<T, R> extractor,
            String chartName,
            String axisTitleName,
            String valuesTitleName,
            String period
    ) {
        TimeSeriesCollection dataset = DatasetFactory.createTimeSeriesCollection(list, extractor, chartName, period);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                chartName,
                axisTitleName,
                valuesTitleName,
                dataset,
                false,
                true,
                false
        );

        return chart;
    }

    public static String createTimeSeriesProgressionChartSvg(
            Map<OffsetDateTime, Integer> timeSeriesProgression,
            String chartName,
            String axisTitleName,
            String valuesTitleName,
            String timePeriod

    ){
        TimeSeriesCollection dataset = DatasetFactory.createTimeSeriesProgressionCollection(timeSeriesProgression, chartName, timePeriod);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                chartName,
                axisTitleName,
                valuesTitleName,
                dataset,
                false,
                true,
                false
        );

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

        ChartPanel chartPanel = new ChartPanel(chart);

        SVGGraphics2D svg = new SVGGraphics2D(800, 600);

        Rectangle2D area = new Rectangle2D.Double(0, 0, 800, 600);
        chart.draw(svg, area);

        return svg.getSVGElement();
    }

    public static <T, R> String createTimeSeriesChartSvgString(
            List<T> list,
            Function<T, R> extractor,
            String chartName,
            String axisTitleName,
            String valuesTitleName,
            String timePeriod
    ) {
        TimeSeriesCollection dataset = DatasetFactory.createTimeSeriesCollection(list, extractor, chartName, timePeriod);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                chartName,
                axisTitleName,
                valuesTitleName,
                dataset,
                false,
                true,
                false
        );

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

        ChartPanel chartPanel = new ChartPanel(chart);

        SVGGraphics2D svg = new SVGGraphics2D(800, 600);

        Rectangle2D area = new Rectangle2D.Double(0, 0, 800, 600);
        chart.draw(svg, area);

        return svg.getSVGElement();
    }

    public static ChartTheme getChartTheme(String themeName) {

        switch (themeName.toUpperCase()) {
            case "DARK_MODE":
                return StylesAppConsts.DARK_THEME;
            case "LIGHT_MODE":
                return StylesAppConsts.LIGHT_THEME;
            default:
                return StylesAppConsts.LIGHT_THEME;
        }
    }

}
