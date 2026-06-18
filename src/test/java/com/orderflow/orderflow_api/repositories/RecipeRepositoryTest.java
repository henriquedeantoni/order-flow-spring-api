package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    private Recipe recipeOne;
    private Recipe recipeTwo;
    private Recipe recipeThree;
    private Recipe recipeFour;

    private final OffsetDateTime includedDate = OffsetDateTime.of(
            2020, 1, 1, 12, 00, 0, 0, ZoneOffset.UTC
    );

    @BeforeEach
    public void setup() {

        recipeOne = new Recipe(
                "Recipe One",
                "Recipe One Preparation Description",
                includedDate,
                25
        );
        recipeTwo = new Recipe(
                "Recipe Two",
                "Recipe Two Preparation Description",
                includedDate,
                50
        );
        recipeThree = new Recipe(
                "Recipe Three",
                "Recipe Three Preparation Description",
                includedDate,
                75
        );
        recipeFour = new Recipe(
                "Recipe Four",
                "Recipe Four Preparation Description",
                includedDate,
                50
        );

/*
        simpleImageOne.setAlbumImage(albumImageOne);
        simpleImageTwo.setAlbumImage(albumImageOne);
        simpleImageThree.setAlbumImage(albumImageOne);
        simpleImageFour.setAlbumImage(albumImageOne);
        */
    }

    @DisplayName("JUnit test for Given Recipe Object when Save then Return Recipe Object")
    @Test
    void testGivenRecipeObject_whenSave_thenReturnRecipeObject(){
        // Given/Arrange

        // When/Act
        Recipe savedRecipe = recipeRepository.save(recipeOne);

        // Then/Assert
        assertNotNull(savedRecipe);
        assertTrue(savedRecipe.getRecipeId()>0);
        assertEquals("Recipe One", savedRecipe.getRecipeName());
        assertEquals("Recipe One Preparation Description", savedRecipe.getPreparationDescription());
        assertEquals(includedDate, savedRecipe.getIncludedDate());
        assertEquals(25, savedRecipe.getTimeMinutesToPrepare());
    }

    @DisplayName("JUnit test for Given Recipe Object when FindById then Return Recipe Object")
    @Test
    void testGivenRecipeObject_whenFindById_thenReturnRecipeObject(){
        // Given/Arrange
        recipeRepository.save(recipeOne);
        recipeRepository.save(recipeTwo);

        // When/Act
        Recipe firstRecipe = recipeRepository.findById(recipeOne.getRecipeId()).get();
        Recipe secondRecipe = recipeRepository.findById(recipeTwo.getRecipeId()).get();

        // Then/Assert
        assertNotNull(firstRecipe);
        assertNotNull(secondRecipe);
        assertEquals(recipeOne.getRecipeId(), firstRecipe.getRecipeId());
        assertEquals(recipeTwo.getRecipeId(), secondRecipe.getRecipeId());
    }

    @DisplayName("JUnit test for Given Recipe Object when Update Recipe then Return Recipe Object")
    @Test
    void testGivenSimpleRecipeObject_whenUpdateRecipe_thenReturnRecipeObject(){
        // Given/Arrange
        recipeRepository.save(recipeOne);

        // When/Act
        Recipe savedRecipe = recipeRepository.findById(recipeOne.getRecipeId()).get();
        savedRecipe.setRecipeName("Changed Name");
        savedRecipe.setPreparationDescription("Changed Description");

        Recipe updatedRecipe = recipeRepository.save(savedRecipe);

        // Then/Assert
        assertNotNull(updatedRecipe);
        assertEquals("Changed Name", updatedRecipe.getRecipeName());
        assertEquals("Changed Description", updatedRecipe.getPreparationDescription());
    }

    @DisplayName("JUnit test for Given Recipe Object when Delete Recipe then Remove Recipe Object")
    @Test
    void testGivenRecipeObject_whenDeleteRecipeById_thenRemoveRecipe(){
        // Given/Arrange
        recipeRepository.save(recipeOne);

        // When/Act
        recipeRepository.deleteById(recipeOne.getRecipeId());
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeOne.getRecipeId());

        // Then/Assert
        assertTrue(recipeOptional.isEmpty());
    }
}
