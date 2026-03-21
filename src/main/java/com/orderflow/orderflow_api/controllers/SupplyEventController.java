package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.models.SupplyEvent;
import com.orderflow.orderflow_api.services.SupplyEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class SupplyEventController {
    @Autowired
    SupplyEventService supplyEventService;

    @GetMapping("/admin/supplyevent/{supplyId}")
    public ResponseEntity<List<SupplyEvent>> getSupplyEventList(@PathVariable("supplyId") Long supplyId){
        List<SupplyEvent> supplyEvents = supplyEventService.getSupplyEventList(supplyId);
        return new ResponseEntity<>(supplyEvents, HttpStatus.OK);
    }
}
