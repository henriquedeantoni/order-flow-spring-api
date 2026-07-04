package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Recipe;
import com.orderflow.orderflow_api.models.RecipeSupply;
import com.orderflow.orderflow_api.models.Supply;
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
public class RecipeSupplyRepositoryTest {

    @Autowired
    private RecipeSupplyRepository recipeSupplyRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    private Recipe recipeOne;
    private Recipe recipeTwo;

    private Supply supplyOne;
    private Supply supplyTwo;

    private RecipeSupply recipeSupplyOne;
    private RecipeSupply recipeSupplyTwo;

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

        supplyOne = new Supply(
                "Supply One",
                "Supply One Reference",
                "Supply One Brand Name",
                "Supply One Description",
                "Supply One Code",
                "Supply One Unit"
        );

        supplyTwo = new Supply(
                "Supply Two",
                "Supply Two Reference",
                "Supply Two Brand Name",
                "Supply Two Description",
                "Supply Two Code",
                "Supply Two Unit"
        );

        recipeRepository.save(recipeOne);
        recipeRepository.save(recipeTwo);

        supplyRepository.save(supplyOne);
        supplyRepository.save(supplyTwo);

        recipeSupplyOne = new RecipeSupply(
                supplyOne,
                recipeOne,
                20,
                "gr"
        );

        recipeSupplyTwo = new RecipeSupply(
                supplyTwo,
                recipeTwo,
                25,
                "gr"
        );
    }

    @DisplayName("JUnit test for Given Recipe Supply Object when Save Object then Return Recipe Supply Object")
    @Test
    void testGivenRecipeSupplyObject_whenSaveObject_thenReturnRecipeSupplyObject(){
        // Given/Arrange

        // When/Act
        RecipeSupply savedRecipeSupply = recipeSupplyRepository.save(recipeSupplyOne);

        // Then/Assert
        assertNotNull(savedRecipeSupply);
        assertTrue(savedRecipeSupply.getRecipeSupplyId()>0);
        assertEquals(supplyOne, savedRecipeSupply.getSupply());
        assertEquals(recipeOne, savedRecipeSupply.getRecipe());
        assertEquals(20, savedRecipeSupply.getQuantity());
        assertEquals("gr",  savedRecipeSupply.getUnit());
    }

    @DisplayName("JUnit test for Given Recipe Supply Object when Save Object then Return Recipe Supply Object")
    @Test
    void testGivenRecipeSupplyObject_whenFindById_thenReturnRecipeSupplyObject(){
        // Given/Arrange
        recipeSupplyRepository.save(recipeSupplyOne);
        // When/Act
        RecipeSupply savedRecipeSupply = recipeSupplyRepository.findById(recipeSupplyOne.getRecipeSupplyId()).get();

        // Then/Assert
        assertNotNull(savedRecipeSupply);
        assertTrue(savedRecipeSupply.getRecipeSupplyId()>0);
        assertEquals(recipeSupplyOne.getRecipeSupplyId(), savedRecipeSupply.getRecipeSupplyId());
        assertEquals(supplyOne, savedRecipeSupply.getSupply());
        assertEquals(recipeOne, savedRecipeSupply.getRecipe());
        assertEquals(20, savedRecipeSupply.getQuantity());
        assertEquals("gr",  savedRecipeSupply.getUnit());
    }

    @DisplayName("JUnit test for Given Recipe Supply Object when Save Object then Return Recipe Supply Object")
    @Test
    void testGivenRecipeSupplyObject_whenUpdateRecipeSupply_thenReturnRecipeSupplyUpdated(){
        // Given/Arrange
        recipeSupplyRepository.save(recipeSupplyOne);
        // When/Act
        RecipeSupply savedRecipeSupply = recipeSupplyRepository.findById(recipeSupplyOne.getRecipeSupplyId()).get();

        savedRecipeSupply.setSupply(supplyTwo);
        savedRecipeSupply.setRecipe(recipeTwo);
        savedRecipeSupply.setQuantity(1000);
        recipeSupplyRepository.save(savedRecipeSupply);

        // Then/Assert
        assertNotNull(savedRecipeSupply);
        assertTrue(savedRecipeSupply.getRecipeSupplyId()>0);
        assertEquals(recipeSupplyOne.getRecipeSupplyId(), savedRecipeSupply.getRecipeSupplyId());
        assertEquals(supplyTwo, savedRecipeSupply.getSupply());
        assertEquals(recipeTwo, savedRecipeSupply.getRecipe());
        assertEquals(1000, savedRecipeSupply.getQuantity());
        assertEquals("gr",  savedRecipeSupply.getUnit());
    }

    @DisplayName("JUnit test for Given Recipe Supply Object when Save Object then Return Recipe Supply Object")
    @Test
    void testGivenRecipeSupplyObject_whenDeleteRecipeSupplyById_thenReturnRecipeSupplyUpdated(){
        // Given/Arrange
        recipeSupplyRepository.save(recipeSupplyOne);

        // When/Act
       recipeSupplyRepository.deleteById(recipeSupplyOne.getRecipeSupplyId());

       Optional<RecipeSupply> recipeSupplyOptional = recipeSupplyRepository.findById(recipeSupplyOne.getRecipeSupplyId());

        // Then/Assert
        assertTrue(recipeSupplyOptional.isEmpty());
    }


}
