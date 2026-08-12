package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Recipe;
import com.orderflow.orderflow_api.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@ExtendWith(SpringExtension.class)
public class RecipeServiceTest {

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private ModelMapper modelMapper;

    private Recipe recipeOne;
    private Recipe recipeTwo;
    private Recipe recipeThree;

    private OffsetDateTime dateTimeOne = OffsetDateTime.of(2025, 10, 10, 10, 10, 10, 0, ZoneOffset.UTC);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        recipeOne = new Recipe("Recipe One", "Description One", dateTimeOne, 20);
        recipeTwo = new Recipe("Recipe Two", "Description Two", dateTimeOne, 20);
        recipeThree = new Recipe("Recipe Three", "Description Three", dateTimeOne, 20);
    }

    @DisplayName("JUnit test for Given Recipe Object When FindRecipeById Then Return RecipeDTO object")
    @Test
    void testGivenRecipeObjectWhenFindRecipeByIdThenReturnRecipeDTOObject() {
        // Given / Arrange


        // When / Act


        // Then / Assert


    }
}
