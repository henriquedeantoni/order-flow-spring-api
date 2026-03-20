package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventorySupplyDTO {
    private Long inventorySupplyId;
    private String supplyReference;
    private String codeBar;
    private String section;
}
