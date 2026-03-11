package com.orderflow.orderflow_api.GraphicEngine.charts;

import com.orderflow.orderflow_api.GraphicEngine.styles.ChartStyle;
import com.orderflow.orderflow_api.GraphicEngine.styles.ChartTheme;
import com.orderflow.orderflow_api.GraphicEngine.styles.constants.StylesAppConsts;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

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
