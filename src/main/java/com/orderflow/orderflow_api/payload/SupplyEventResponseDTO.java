package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyEventResponseDTO {
    private Long supplyEventId;
    private Long itemId;
    private String eventType;
    private Integer quantityMoved;
    private OffsetDateTime eventDate;
}
