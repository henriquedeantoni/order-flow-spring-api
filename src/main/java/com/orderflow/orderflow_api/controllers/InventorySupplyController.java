package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.config.AppConsts;
import com.orderflow.orderflow_api.payload.InventoryResponse;
import com.orderflow.orderflow_api.payload.InventorySupplyDTO;
import com.orderflow.orderflow_api.services.InventorySupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class InventorySupplyController {
    @Autowired
    private InventorySupplyService inventorySupplyService;

    @PostMapping("/admin/inventory/supplies")
    public ResponseEntity<InventorySupplyDTO> registerSupplyOnInventory(
            @RequestBody InventorySupplyDTO inventorySupplyDTO ) {
        InventorySupplyDTO inventoryItem = inventorySupplyService.registerSupplyOnInventory(inventorySupplyDTO);
        return new ResponseEntity<>(inventoryItem, HttpStatus.CREATED);
    }

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
    public ResponseEntity<InventoryResponse> moveSupplyOnInventory(
            @RequestParam int quantity,
            @RequestBody InventorySupplyDTO inventorySupplyDTO,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber
    ){
        InventoryResponse response = inventorySupplyService.moveSupplyOnInventory(quantity, inventorySupplyDTO, pageSize, pageNumber);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
