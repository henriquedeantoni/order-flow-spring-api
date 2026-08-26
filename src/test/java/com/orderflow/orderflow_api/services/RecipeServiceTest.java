package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.*;
import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.payload.RecipeDTO;
import com.orderflow.orderflow_api.payload.RecipeSupplyDTO;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import com.orderflow.orderflow_api.repositories.RecipeRepository;
import com.orderflow.orderflow_api.repositories.RecipeSupplyRepository;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
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
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class RecipeServiceTest {

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeSupplyRepository recipeSupplyRepository;

    @Mock
    private SupplyRepository supplyRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ModelMapper modelMapper;

    private Recipe recipeOne;
    private Recipe recipeTwo;
    private Recipe recipeThree;

    private RecipeSupplyDTO recipeSupplyOneDTO = new RecipeSupplyDTO();
    private RecipeSupplyDTO recipeSupplyTwoDTO = new RecipeSupplyDTO();

    private RecipeSupply recipeSupplyOne = new RecipeSupply();
    private RecipeSupply recipeSupplyTwo = new RecipeSupply();

    private RecipeDTO recipeOneDTO;

    private OffsetDateTime dateTimeOne = OffsetDateTime.of(2025, 10, 10, 10, 10, 10, 0, ZoneOffset.UTC);

    private Item itemOne = new Item("item one","description", 100, 10.00);

    private Supply supplyOne = new Supply();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        recipeOne = new Recipe( "Recipe One", "Description One", dateTimeOne, 20);
        recipeTwo = new Recipe("Recipe Two", "Description Two", dateTimeOne, 20);
        recipeThree = new Recipe("Recipe Three", "Description Three", dateTimeOne, 20);

        recipeOneDTO = new RecipeDTO(1L, "Recipe One", "Description One", 20);

    }

    @DisplayName("JUnit test for Given Recipe Object When FindRecipeById Then Return RecipeDTO object")
    @Test
    void testGivenRecipeObjectWhenFindRecipeByIdThenReturnRecipeDTOObject() {
        // Given / Arrange
        Long validId = 1L;
        given(recipeRepository.findById(validId)).willReturn(Optional.of(recipeOne));
        given(modelMapper.map(recipeOne, RecipeDTO.class)).willReturn(recipeOneDTO);

        // When / Act
        RecipeDTO result = recipeService.findRecipeById(validId);

        // Then / Assert
        assertNotNull(result);
        assertEquals("Recipe One", result.getRecipeName());
        assertEquals("Description One", result.getPreparationDescription());
        assertEquals(20, result.getTimeMinutesToPrepare());
    }

    @DisplayName("JUnit test for Given Recipe Object When FindRecipeById With Invalid Id Then Throws Resource Not Found")
    @Test
    void testGivenRecipeObjectWhenFindRecipeByIdWithInvalidIdThenThrowsResourceNotFound() {
        // Given / Arrange
        Long validId = 1L;
        Long invalidId = 2L;
        given(recipeRepository.findById(validId)).willReturn(Optional.of(recipeOne));
        given(recipeRepository.findById(invalidId)).willReturn(Optional.empty());
        given(modelMapper.map(recipeOne, RecipeDTO.class)).willReturn(recipeOneDTO);

        // When / Act
        RecipeDTO result = recipeService.findRecipeById(invalidId);

        // Then / Assert
        assertNotNull(result);
        assertEquals("Recipe One", result.getRecipeName());
        assertEquals("Description One", result.getPreparationDescription());
        assertEquals(20, result.getTimeMinutesToPrepare());
    }

    @DisplayName("JUnit test for Given Recipe Object When Register Recipe then Returns RecipeDto Object")
    @Test
    void testGivenRecipeObjectWhenRegisterRecipeThenReturnRecipeDtoObject() {
        // Given/Arrange
        recipeOne.setRecipeId(1L);
        Long validItemId = 1L;
        List<RecipeSupplyDTO> recipeSupplyList = List.of(recipeSupplyOneDTO, recipeSupplyTwoDTO);
        recipeSupplyOneDTO.setSupplyId(1L);
        recipeSupplyTwoDTO.setSupplyId(2L);
        given(itemRepository.findById(validItemId)).willReturn(Optional.of(itemOne));

        given(modelMapper.map(recipeSupplyOneDTO, RecipeSupply.class))
                .willReturn(recipeSupplyOne);

        given(modelMapper.map(recipeSupplyTwoDTO, RecipeSupply.class))
                .willReturn(recipeSupplyTwo);

        given(recipeRepository.save(any(Recipe.class)))
                .willReturn(recipeOne);

        given(modelMapper.map(any(Recipe.class), eq(RecipeDTO.class)))
                .willReturn(recipeOneDTO);

        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.of(recipeOne));

        given(supplyRepository.findById(anyLong())).willReturn(Optional.of(supplyOne));

        given(modelMapper.map(any(RecipeSupplyDTO.class), eq(RecipeSupply.class)))
                .willReturn(recipeSupplyOne);

        // When/Act
        RecipeDTO result = recipeService.registerRecipe(recipeOneDTO,validItemId , recipeSupplyList);

        // Then/Assert
        assertNotNull(result);
        assertEquals("Recipe One", result.getRecipeName());
        assertEquals("Description One", result.getPreparationDescription());
        assertEquals(20, result.getTimeMinutesToPrepare());
    }

    @DisplayName("JUnit test for Given Recipe Object When Register Recipe With Invalid ItemId then Returns RecipeDto Object")
    @Test
    void testGivenRecipeObjectWhenRegisterRecipeWithInvalidItemIdThenReturnRecipeDtoObject() {
        // Given/Arrange
        recipeOne.setRecipeId(1L);
        Long validItemId = 1L;
        Long inValidItemId = 2L;
        List<RecipeSupplyDTO> recipeSupplyList = List.of(recipeSupplyOneDTO, recipeSupplyTwoDTO);
        recipeSupplyOneDTO.setSupplyId(1L);
        recipeSupplyTwoDTO.setSupplyId(2L);
        given(itemRepository.findById(validItemId)).willReturn(Optional.of(itemOne));

        given(modelMapper.map(recipeSupplyOneDTO, RecipeSupply.class))
                .willReturn(recipeSupplyOne);

        given(modelMapper.map(recipeSupplyTwoDTO, RecipeSupply.class))
                .willReturn(recipeSupplyTwo);

        given(recipeRepository.save(any(Recipe.class)))
                .willReturn(recipeOne);

        given(modelMapper.map(any(Recipe.class), eq(RecipeDTO.class)))
                .willReturn(recipeOneDTO);

        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.of(recipeOne));

        given(supplyRepository.findById(anyLong())).willReturn(Optional.of(supplyOne));

        given(modelMapper.map(any(RecipeSupplyDTO.class), eq(RecipeSupply.class)))
                .willReturn(recipeSupplyOne);

        // When/Act

        assertThrows(ResourceNotFoundException.class, () -> {
            recipeService.registerRecipe(recipeOneDTO,inValidItemId , recipeSupplyList);
        });

        // Then/Assert
        verify(recipeRepository, never()).save(any(Recipe.class));
    }
}
