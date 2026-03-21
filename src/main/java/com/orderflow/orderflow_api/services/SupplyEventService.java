package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.SupplyEvent;
import com.orderflow.orderflow_api.payload.SupplyEventResponseDTO;

import java.util.List;

public interface SupplyEventService {
    SupplyEventResponseDTO firstSupplyEvent(Long supplyId);

    SupplyEventResponseDTO increaseQuantityMovedEvent(Long supplyId, Integer quantityMoved);

    SupplyEventResponseDTO decreaseQuantityMovedEvent(Long supplyId, Integer quantityMoved);

    List<SupplyEvent> getSupplyEventList(Long supplyId);
}
