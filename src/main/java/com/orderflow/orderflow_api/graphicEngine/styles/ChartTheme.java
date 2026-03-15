package com.orderflow.orderflow_api.graphicEngine.styles;

import java.awt.*;

public class ChartTheme {

    private Color chartBackground;
    private Color plotBackground;
    private Color textColor;
    private Color gridColor;

    private Color primaryColor;
    private Color secondaryColor;
    private Color tertiaryColor;

    public ChartTheme(
            Color chartBackground,
            Color plotBackground,
            Color textColor,
            Color gridColor,
            Color primaryColor,
            Color secondaryColor,
            Color tertiaryColor
    ) {
        this.chartBackground = chartBackground;
        this.plotBackground = plotBackground;
        this.textColor = textColor;
        this.gridColor = gridColor;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.tertiaryColor = tertiaryColor;
    }

    public Color getChartBackground() { return chartBackground; }
    public Color getPlotBackground() { return plotBackground; }
    public Color getTextColor() { return textColor; }
    public Color getGridColor() { return gridColor; }

    public Color getPrimaryColor() { return primaryColor; }
    public Color getSecondaryColor() { return secondaryColor; }
    public Color getTertiaryColor() { return tertiaryColor; }
}
