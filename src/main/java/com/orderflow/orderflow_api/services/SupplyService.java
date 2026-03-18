package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.SupplyDTO;
import org.springframework.stereotype.Service;

public interface SupplyService {
    SupplyDTO registerSupply(SupplyDTO supplyDTO);
}
