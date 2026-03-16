package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemTimeDTO {
    private Long itemId;
    private String itemName;
    private OffsetDateTime includedDate;
}
