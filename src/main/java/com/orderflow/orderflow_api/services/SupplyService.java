package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.SupplyDTO;
import com.orderflow.orderflow_api.payload.SupplyResponse;

import java.time.OffsetDateTime;

public interface SupplyService {
    SupplyDTO registerSupply(SupplyDTO supplyDTO);

    SupplyDTO updateSupply(Long supplyId, SupplyDTO supplyId1);

    SupplyResponse getAllSupplyRegistered(String keyword, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

    SupplyResponse getAllSupplyRegisteredByPeriod(String keyword, OffsetDateTime firstDate, OffsetDateTime lastDate, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);
}
