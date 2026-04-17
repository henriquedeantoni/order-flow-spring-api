package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {
    private Long recipeId;
    private String recipeName;
    private String preparationDescription;
    public OffsetDateTime includedDate = OffsetDateTime.now();
    public OffsetDateTime updatedDate;
    public int timeMinutesToPrepare;
}
