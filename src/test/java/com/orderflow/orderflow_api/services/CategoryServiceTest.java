package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.User;
import com.orderflow.orderflow_api.payload.CategoryDTO;
import com.orderflow.orderflow_api.repositories.CategoryRepository;
import com.orderflow.orderflow_api.security.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private ModelMapper modelMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private final OffsetDateTime firstDate = OffsetDateTime.of(
            2025, 1, 1, 12, 00, 0, 0, ZoneOffset.UTC
    );

    private final Category categoryOne = new Category();
    private final Category categoryTwo = new Category();
    private final Category categoryThree = new Category();

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(categoryService, "modelMapper", new ModelMapper());

        // Given/Arrange
        categoryOne.setCategoryName("CategoryOne");
        categoryOne.setIncludedDate(firstDate);

        categoryTwo.setCategoryName("CategoryTwo");
        categoryTwo.setIncludedDate(firstDate);
    }

    @DisplayName("JUnit test for Given Category Object when save category then Return Category Object")
    @Test
    void testGivenSaveCategoryObjectWhenSaveCategoryThenReturnCategoryObject() {
        // Given/Arrange
        given(categoryRepository.findByCategoryId(anyLong())).willReturn(categoryOne);
        given(categoryRepository.save(any(Category.class))).willReturn(categoryOne);
        CategoryDTO categoryOneDTO = new ModelMapper().map(categoryOne, CategoryDTO.class);

        // When/Act
        CategoryDTO savedCategory = categoryService.addCategory(categoryOneDTO);

        // Then/Assert
        assertNotNull(savedCategory);
        assertEquals("CategoryOne", savedCategory.getCategoryName());
    }

    @Test
    @DisplayName("Should add a new category with success")
    void shouldAddCategorySuccessfully()
    {
        // ------------ ARRANGE --------------
        CategoryDTO categoryInputDTO = new CategoryDTO();
        categoryInputDTO.setCategoryName("Category Name");

        Category mappedCategory = new Category();
        mappedCategory.setCategoryName("Category Name");

        User user = new User();

        Category savedCategory = new Category();
        savedCategory.setCategoryId(1L);
        savedCategory.setCategoryName("Category Name");

        CategoryDTO categoryOutputDTO = new CategoryDTO();
        categoryOutputDTO.setCategoryId(1L);
        categoryOutputDTO.setCategoryName("Category Name");

        when(modelMapper.map(categoryInputDTO, Category.class)).thenReturn(mappedCategory);

        when(authUtil.userOnLoggedSession()).thenReturn(user);

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        when(modelMapper.map(savedCategory, CategoryDTO.class)).thenReturn(categoryInputDTO);

        // ------------ ACT --------------

        CategoryDTO resultCategoryDTO = categoryService.addCategory(categoryInputDTO);

        // ------------ ASSERT --------------

        assertNotNull(resultCategoryDTO);

        assertEquals(1L, resultCategoryDTO.getCategoryId());
        assertEquals("Category Name", resultCategoryDTO.getCategoryName());

        verify(categoryRepository, times(1) )
                .findById(categoryInputDTO.getCategoryId());

        verify(modelMapper, times(1))
                .map(categoryInputDTO, Category.class);

        verify(authUtil, times(1))
                .userOnLoggedSession();

        verify(categoryRepository, times(1))
                .save(any(Category.class));

        verify(modelMapper, times(1))
                .map(savedCategory, Category.class);
    }

    @Test
    @DisplayName("Should update a category with success")
    void shouldUpdateCategorySuccessfully()
    {
        // ------------ ARRANGE --------------
        Long categoryId = 1L;

        Category existingCategory = new Category();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setCategoryName("Old category Name");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(categoryId);
        categoryDTO.setCategoryName("New category Name");

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(existingCategory));

        when(modelMapper.map(categoryDTO, Category.class))
                .thenReturn(new Category());

        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(modelMapper.map(existingCategory, CategoryDTO.class))
                .thenReturn(categoryDTO);

        // ------------ ACT --------------
        CategoryDTO categoryResult = categoryService.updateCategory(categoryDTO, categoryId);

        // ------------ ASSERT --------------
        assertEquals("New category Name", categoryResult.getCategoryName());
        verify(categoryRepository, times(1)).save(existingCategory);
    }
}
