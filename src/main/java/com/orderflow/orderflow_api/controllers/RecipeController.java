package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.payload.RecipeDTO;
import com.orderflow.orderflow_api.payload.RecipeSupplyDTO;
import com.orderflow.orderflow_api.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/admin/recipes")
    public ResponseEntity<RecipeDTO> registerRecipe(
            @RequestParam Long itemId,
            @RequestBody RecipeDTO recipeDTO,
            @RequestBody List<RecipeSupplyDTO> recipeList

    ) {
        RecipeDTO createdRecipe = recipeService.registerRecipe(recipeDTO, itemId, recipeList);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    @PutMapping("/admin/recipes/{recipeId}")
    public ResponseEntity<RecipeDTO> updateRecipe(
            @RequestBody RecipeDTO recipeDTO,
            @PathVariable Long recipeId
    ) {
        RecipeDTO createdRecipe = recipeService.updateRecipe(recipeDTO, recipeId);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    @PutMapping("/admin/recipes/{recipeId}/list")
    public ResponseEntity<RecipeDTO> updateRecipeList(
            @RequestBody List<RecipeSupplyDTO> recipeList,
            @PathVariable Long recipeId
    ) {
        RecipeDTO createdRecipe = recipeService.updateRecipeList(recipeList, recipeId);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }
}
