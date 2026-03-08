package com.orderflow.orderflow_api.services;

import java.io.IOException;

public interface DashboardService {
    String getExampleChart() throws IOException;

    String getDashboardExampleWithSize(int width, int height) throws IOException;
}
