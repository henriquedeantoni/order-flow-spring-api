package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.models.Recipe;
import com.orderflow.orderflow_api.payload.RecipeDTO;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import com.orderflow.orderflow_api.repositories.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public RecipeDTO findRecipeById(Long recipeId) {
        Recipe recipeFromDb = recipeRepository.findById(recipeId)
                .orElseThrow(()-> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        return modelMapper.map(recipeFromDb, RecipeDTO.class);
    }


    @Override
    public RecipeDTO registerRecipe(RecipeDTO recipeDTO, Long itemId) {
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(()-> new ResourceNotFoundException("Item", "itemId", itemId));

        Recipe recipe = new Recipe();
        recipe.setRecipeName(recipeDTO.getRecipeName());
        recipe.setPreparationDescription(recipeDTO.getPreparationDescription());
        recipe.setTimeMinutesToPrepare(recipeDTO.getTimeMinutesToPrepare());
        recipe.setIncludedDate(OffsetDateTime.now());
        recipe.setUpdatedDate(OffsetDateTime.now());
        recipe.setItem(itemFromDb);

        recipeRepository.save(recipe);

        return modelMapper.map(recipe, RecipeDTO.class);
    }

    @Override
    public RecipeDTO updateRecipe(RecipeDTO recipeDTO, Long recipeId) {
        Recipe recipeFromDb = recipeRepository.findById(recipeId)
                .orElseThrow(()-> new ResourceNotFoundException("Recipe", "recipeId", recipeId));

        recipeFromDb.setRecipeName(recipeDTO.getRecipeName());
        recipeFromDb.setPreparationDescription(recipeDTO.getPreparationDescription());
        recipeFromDb.setTimeMinutesToPrepare(recipeDTO.getTimeMinutesToPrepare());
        recipeFromDb.setUpdatedDate(OffsetDateTime.now());
        recipeRepository.save(recipeFromDb);

        return modelMapper.map(recipeFromDb, RecipeDTO.class);
    }
}
