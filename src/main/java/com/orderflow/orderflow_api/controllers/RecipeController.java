package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.payload.RecipeDTO;
import com.orderflow.orderflow_api.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("v1/")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/admin/recipes/{recipeId}")
    public ResponseEntity<RecipeDTO> getRecipe(
            @PathVariable Long recipeId) {
        RecipeDTO response = recipeService.findRecipeById(recipeId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
