package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.InventoryResponse;
import com.orderflow.orderflow_api.payload.InventorySupplyDTO;

import java.time.Instant;
import java.time.OffsetDateTime;

public interface InventorySupplyService{
    InventorySupplyDTO registerSupplyOnInventory(InventorySupplyDTO inventorySupplyDTO);

    InventoryResponse getAllInventoryItems(Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

    InventoryResponse moveSupplyOnInventory(int quantity, InventorySupplyDTO inventorySupplyDTO, int pageSize, int pageNumber);

    InventoryResponse moveSupplyOutInventory(int quantity, String supplyReference, Integer pageSize, Integer pageNumber);

    InventoryResponse movementsSupplyOnPeriod(OffsetDateTime firstDate, OffsetDateTime lastDate, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

    Integer getTotalQuantityFromSupply(long supplyId);

    Integer getTotalQuantityFromSupplyByPeriod(long supplyId, OffsetDateTime firstDate, OffsetDateTime lastDate);

    InventoryResponse getSuppliesExpirationWeek(Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

    InventoryResponse getSuppliesExpirationMonth(Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);
}
