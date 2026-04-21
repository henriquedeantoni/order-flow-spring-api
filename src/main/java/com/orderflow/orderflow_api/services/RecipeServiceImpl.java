package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.models.Recipe;
import com.orderflow.orderflow_api.models.RecipeSupply;
import com.orderflow.orderflow_api.payload.RecipeDTO;
import com.orderflow.orderflow_api.payload.RecipeSupplyDTO;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import com.orderflow.orderflow_api.repositories.RecipeRepository;
import com.orderflow.orderflow_api.repositories.RecipeSupplyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeSupplyRepository recipeSupplyRepository;

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
    public RecipeDTO registerRecipe(RecipeDTO recipeDTO, Long itemId, List<RecipeSupplyDTO> recipeList) {
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(()-> new ResourceNotFoundException("Item", "itemId", itemId));

        for(RecipeSupplyDTO recipeSupplyDTO : recipeList){
            var recipeSupplyFromDb = recipeSupplyRepository.findById(recipeSupplyDTO.getRecipeSupplyId())
                    .orElseThrow(()-> new ResourceNotFoundException("RecipeSupply", "recipeSupplyId", recipeSupplyDTO.getRecipeSupplyId()));
        }

        Recipe recipe = new Recipe();
        recipe.setRecipeName(recipeDTO.getRecipeName());
        recipe.setPreparationDescription(recipeDTO.getPreparationDescription());
        recipe.setTimeMinutesToPrepare(recipeDTO.getTimeMinutesToPrepare());
        recipe.setIncludedDate(OffsetDateTime.now());
        recipe.setUpdatedDate(OffsetDateTime.now());
        recipe.setItem(itemFromDb);

        List<RecipeSupply> recipeSupply = new ArrayList<>();
        recipeSupply = recipeList.stream()
                        .map(rec ->{
                            return modelMapper.map(rec, RecipeSupply.class);
                        }).toList();

        recipe.setRecipeSupplies(recipeSupply);

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

    @Override
    public RecipeDTO updateRecipeList(List<RecipeSupplyDTO> recipeList, Long recipeId) {
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
