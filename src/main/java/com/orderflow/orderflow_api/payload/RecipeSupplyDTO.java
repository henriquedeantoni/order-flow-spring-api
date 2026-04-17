package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSupplyDTO {
    private Long recipeSupplyId;
    private Integer quantity;
    private String unit;
}
