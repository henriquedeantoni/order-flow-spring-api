package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.GraphicEngine.Charts.ChartEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private ChartEngine chartEngine;


    @Override
    public String getExampleChart() throws IOException {
        ChartEngine chartEngine = new ChartEngine();

        chartEngine.generateChartExample();

        return "Your chart example is here";
    }

    @Override
    public String getDashboardExampleWithSize(int width, int height) throws IOException {
        ChartEngine chartEngine = new ChartEngine();

        chartEngine.generateChartSizeCustom(width, height);

        return "Your chart example is here";
    }
}
