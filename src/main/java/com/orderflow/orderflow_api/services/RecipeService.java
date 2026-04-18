package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.RecipeDTO;

public interface RecipeService {
    RecipeDTO findRecipeById(Long recipeId);
}
