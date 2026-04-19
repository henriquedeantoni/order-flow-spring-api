package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.RecipeDTO;

public interface RecipeService {
    RecipeDTO findRecipeById(Long recipeId);

    RecipeDTO registerRecipe(RecipeDTO recipeDTO, Long itemId);

    RecipeDTO updateRecipe(RecipeDTO recipeDTO, Long recipeId);
}
