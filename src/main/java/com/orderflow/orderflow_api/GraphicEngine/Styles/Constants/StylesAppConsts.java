package com.orderflow.orderflow_api.GraphicEngine.Styles.Constants;

import com.orderflow.orderflow_api.GraphicEngine.Styles.ChartTheme;

import java.awt.*;

public class StylesAppConsts {

    public static final ChartTheme DARK_THEME = new ChartTheme(
            new Color(30,30,30),
            new Color(50,50,50),
            Color.WHITE,
            Color.LIGHT_GRAY,
            new Color(52,152,219),
            new Color(231,76,60),
            new Color(46,204,113)
    );

    public static final ChartTheme LIGHT_THEME = new ChartTheme(
            Color.WHITE,
            new Color(240,240,240),
            Color.DARK_GRAY,
            Color.GRAY,
            new Color(52,152,219),
            new Color(231,76,60),
            new Color(46,204,113)
    );

}
