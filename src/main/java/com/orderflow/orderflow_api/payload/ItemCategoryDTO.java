package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategoryDTO {
    private Long itemId;
    private String itemName;
    private String description;
    private Integer quantity;
    private String categoryName;
}
