package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.RecipeDTO;
import com.orderflow.orderflow_api.payload.RecipeSupplyDTO;

import java.util.List;

public interface RecipeService {
    RecipeDTO findRecipeById(Long recipeId);

    RecipeDTO registerRecipe(RecipeDTO recipeDTO, Long itemId, List<RecipeSupplyDTO> recipeList);

    RecipeDTO updateRecipe(RecipeDTO recipeDTO, Long recipeId);
}
