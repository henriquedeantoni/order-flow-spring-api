package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.SupplyDTO;

public interface SupplyService {
    SupplyDTO registerSupply(SupplyDTO supplyDTO);

    SupplyDTO updateSupply(Long supplyId, SupplyDTO supplyId1);
}
