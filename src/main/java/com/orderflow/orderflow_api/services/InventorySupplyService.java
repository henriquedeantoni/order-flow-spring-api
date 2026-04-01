package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.InventoryResponse;
import com.orderflow.orderflow_api.payload.InventorySupplyDTO;

public interface InventorySupplyService{
    InventorySupplyDTO registerSupplyOnInventory(InventorySupplyDTO inventorySupplyDTO);

    InventoryResponse getAllInventoryItems(Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

    InventoryResponse moveSupplyOnInventory(int quantity, InventorySupplyDTO inventorySupplyDTO, int pageSize, int pageNumber);
}
