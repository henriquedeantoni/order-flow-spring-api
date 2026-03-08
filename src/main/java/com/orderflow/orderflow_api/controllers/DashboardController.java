package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin/charts/example")
    public ResponseEntity<String> getDashboardExample() throws IOException {
        String response = dashboardService.getExampleChart();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/charts/example/{width}/{height}", produces = "image/svg+xml")
    public ResponseEntity<String> getDashboardExampleWithSize(
            @PathVariable int width,
            @PathVariable int height

    ) throws IOException {
        String response = dashboardService.getDashboardExampleWithSize(width, height);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
