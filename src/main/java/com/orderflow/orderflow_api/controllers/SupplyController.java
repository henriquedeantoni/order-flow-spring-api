package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.config.AppConsts;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.payload.SupplyDTO;
import com.orderflow.orderflow_api.payload.SupplyResponse;
import com.orderflow.orderflow_api.services.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/v1")
public class SupplyController {
    @Autowired
    private SupplyService supplyService;

    @PostMapping("/admin/supplies/register")
    public ResponseEntity<SupplyDTO> registerSupply(
            @RequestBody SupplyDTO supplyDTO
    ){
        //.out.println("supplyDTO: " + supplyDTO);
        SupplyDTO savedSupply = supplyService.registerSupply(supplyDTO);
        return new ResponseEntity<>(savedSupply, HttpStatus.CREATED);
    }

    @PutMapping("/admin/supplies/{supplyId}")
    public ResponseEntity<SupplyDTO> updateSupply(
            @PathVariable Long supplyId,
            @RequestBody SupplyDTO supplyDTO
    ){
        SupplyDTO supplyUpdateDTO = supplyService.updateSupply(supplyId, supplyDTO);
        return new ResponseEntity<>(supplyUpdateDTO, HttpStatus.OK);
    }

    @GetMapping("/admin/supplies/all")
    public ResponseEntity<SupplyResponse> getAllSupplyRegistered(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_SUPPLIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ){
        SupplyResponse response = supplyService.getAllSupplyRegistered(keyword, pageSize, pageNumber, sortBy, sortOrder);

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @GetMapping("/admin/supplies/all/period")
    public ResponseEntity<SupplyResponse> getAllSupplyRegistered(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime firstDate,
            @RequestParam(name = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime lastDate,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_SUPPLIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ){
        SupplyResponse response = supplyService.getAllSupplyRegisteredByPeriod(keyword, firstDate, lastDate, pageSize, pageNumber, sortBy, sortOrder);

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }
}
