package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.config.AppConsts;
import com.orderflow.orderflow_api.payload.InventoryResponse;
import com.orderflow.orderflow_api.payload.InventorySupplyDTO;
import com.orderflow.orderflow_api.services.InventorySupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/v1")
public class InventorySupplyController {
    @Autowired
    private InventorySupplyService inventorySupplyService;

    @GetMapping("/admin/inventory/supplies")
    public ResponseEntity<InventoryResponse> getSupplyOnInventory(
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_INVENTORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ){
        InventoryResponse inventoryResponse = inventorySupplyService.getAllInventoryItems(pageSize, pageNumber, sortBy, sortOrder);
        return new ResponseEntity<>(inventoryResponse, HttpStatus.OK);
    }

    @PostMapping("/amdin/inventory/supplies/movein")
    public ResponseEntity<InventoryResponse> moveSupplyInInventory(
            @RequestParam Integer quantity,
            @RequestBody InventorySupplyDTO inventorySupplyDTO,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber
    ){
        InventoryResponse response = inventorySupplyService.moveSupplyOnInventory(quantity, inventorySupplyDTO, pageSize, pageNumber);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/admin/inventory/supplies/moveout")
    public ResponseEntity<InventoryResponse> moveSupplyOutInventory(
            @RequestParam int quantity,
            @RequestBody InventorySupplyDTO inventorySupplyDTO,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber
    ){
        InventoryResponse response = inventorySupplyService.moveSupplyOutInventory(quantity, inventorySupplyDTO, pageSize, pageNumber);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/admin/inventory/supply/movements/period")
    public ResponseEntity<InventoryResponse> movementsSupplyOnPeriod(
            @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant firstDate,
            @RequestParam(name = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant lastDate,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_INVENTORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ){
        InventoryResponse response = inventorySupplyService.movementsSupplyOnPeriod(firstDate, lastDate, pageSize, pageNumber, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/inventory/supply/totalquantity/{supplyId}")
    public ResponseEntity<Integer> getTotalQuantityFromSupply(
            @PathVariable("supplyId") long supplyId
    ){
        Integer quantity = inventorySupplyService.getTotalQuantityFromSupply(supplyId);
        return new ResponseEntity<>(quantity, HttpStatus.OK);
    }

    @GetMapping("/admin/inventory/supply/totalquantity/{supplyId}/period")
    public ResponseEntity<Integer> getTotalQuantityFromSupplyByPeriod(
            @PathVariable("supplyId") long supplyId,
            @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant firstDate,
            @RequestParam(name = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant lastDate
    ){
        Integer quantity = inventorySupplyService.getTotalQuantityFromSupplyByPeriod(supplyId, firstDate, lastDate);
        return new ResponseEntity<>(quantity, HttpStatus.OK);
    }


}
