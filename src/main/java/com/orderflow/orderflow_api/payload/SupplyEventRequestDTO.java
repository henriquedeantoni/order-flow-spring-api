package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyEventRequestDTO {
    private Long supplyEventId;
    private Long supplyId;
    private String eventType;
    private Integer quantityMoved;
}
