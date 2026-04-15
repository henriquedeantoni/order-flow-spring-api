package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyDTO {
    private Long supplyId;
    private String supplyName;
    private String supplyReference;
    private String brandName;
    private String supplyDescription;
    private String supplyCode;
    private String supplyUnit;
    private OffsetDateTime addDate;
}
