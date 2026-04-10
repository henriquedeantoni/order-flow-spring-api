package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.models.SupplyEvent;
import com.orderflow.orderflow_api.payload.SupplyEventResponseDTO;
import com.orderflow.orderflow_api.services.SupplyEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.OffsetDateTime;
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

    @GetMapping(value = "/admin/supplyevent/dashboard/timeseries/supply/{supplyId}")
    public ResponseEntity<String> getDashboardTimeSeriesMonthlyItem(
            @PathVariable("supplyId") Long supplyId,
            @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime firstDate,
            @RequestParam(name = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime lastDate,
            @RequestParam(name = "chartTitleName", required = true) String chartTitleName,
            @RequestParam(name = "axisLabelName", required = true) String axisLabelName,
            @RequestParam(name = "valuesLabelName", required = true) String valuesLabelName
    ){
        String response = supplyEventService.createDashboardTimeSeriesMonthlySupply(firstDate, lastDate, chartTitleName, axisLabelName, valuesLabelName, supplyId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/svg+xml"));

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
