package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.SupplyEventResponseDTO;

import java.time.OffsetDateTime;
import java.util.List;

public interface SupplyEventService {
    SupplyEventResponseDTO firstSupplyEvent(Long supplyId);

    SupplyEventResponseDTO increaseQuantityMovedEvent(Long supplyId, Integer quantityMoved);

    SupplyEventResponseDTO decreaseQuantityMovedEvent(Long supplyId, Integer quantityMoved);

    List<SupplyEventResponseDTO> getSupplyEventList(Long supplyId);

    String createDashboardTimeSeriesMonthlySupply(OffsetDateTime firstDate, OffsetDateTime lastDate, String chartTitleName, String axisLabelName, String valuesLabelName, Long supplyId);

    String createDashboardTimeSeriesDailySupply(OffsetDateTime firstDate, OffsetDateTime lastDate, String chartTitleName, String axisLabelName, String valuesLabelName, Long supplyId);

    String createDashboardTimeSeriesYearlySupply(OffsetDateTime firstDate, OffsetDateTime lastDate, String chartTitleName, String axisLabelName, String valuesLabelName, Long supplyId);
}
