package com.orderflow.orderflow_api.payload;

import com.orderflow.orderflow_api.models.Supply;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventorySupplyDTO {
    private Long inventorySupplyId;
    private String supplyReference;
    private String codeBar;
    private String section;

    @Pattern(regexp = "STOCK_IN|STOCK_OUT", message = "Error: Status pattern not allowed")
    private String status;

    private LocalDate valDate;
    private OffsetDateTime movmentDate;
}
