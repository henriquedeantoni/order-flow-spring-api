package com.orderflow.orderflow_api.graphicEngine.charts;

import com.orderflow.orderflow_api.graphicEngine.datasets.DatasetFactory;
import com.orderflow.orderflow_api.graphicEngine.styles.ChartStyle;
import com.orderflow.orderflow_api.graphicEngine.styles.ChartTheme;
import com.orderflow.orderflow_api.graphicEngine.styles.constants.StylesAppConsts;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
