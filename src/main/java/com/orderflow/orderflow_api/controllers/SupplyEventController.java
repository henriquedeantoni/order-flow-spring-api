package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.models.SupplyEvent;
import com.orderflow.orderflow_api.payload.SupplyEventResponseDTO;
import com.orderflow.orderflow_api.services.SupplyEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class SupplyEventController {
    @Autowired
    SupplyEventService supplyEventService;

    @GetMapping("/admin/supplyevent/{supplyId}")
    public ResponseEntity<List<SupplyEventResponseDTO>> getSupplyEventList(@PathVariable("supplyId") Long supplyId){
        List<SupplyEventResponseDTO> supplyEvents = supplyEventService.getSupplyEventList(supplyId);
        return new ResponseEntity<>(supplyEvents, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/dashboard/timeseries/supply/{supplyId}")
    public ResponseEntity<String> getDashboardTimeSeriesMonthlyItem(
            @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant firstDate,
            @RequestParam(name = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant lastDate,
            @RequestParam(name = "chartTitleName", required = true) String chartTitleName,
            @RequestParam(name = "axisLabelName", required = true) String axisLabelName,
            @RequestParam(name = "valuesLabelName", required = true) String valuesLabelName
    ){
        String response = supplyEventService.createDashboardTimeSeriesMonthlyItem(firstDate, lastDate, chartTitleName, axisLabelName, valuesLabelName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
