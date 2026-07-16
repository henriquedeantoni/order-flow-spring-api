package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.User;
import com.orderflow.orderflow_api.payload.CategoryDTO;
import com.orderflow.orderflow_api.payload.CategoryResponse;
import com.orderflow.orderflow_api.repositories.CategoryRepository;
import com.orderflow.orderflow_api.security.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private ModelMapper modelMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private final OffsetDateTime firstDate = OffsetDateTime.of(
            2025, 1, 1, 12, 00, 0, 0, ZoneOffset.UTC
    );

    private Category categoryOne;
    private Category categoryTwo;
    private Category categoryThree;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(categoryService, "modelMapper", new ModelMapper());

        // Given/Arrange
        categoryOne = new Category("Category A", firstDate);

        categoryTwo = new Category("Category B", firstDate);

        categoryThree = new Category("Category C", firstDate);
    }

    @DisplayName("JUnit test for Given Category Object when save category then Return Category Object")
    @Test
    void testGivenSaveCategoryObjectWhenSaveCategoryThenReturnCategoryObject() {
        // Given/Arrange
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(categoryOne));
        given(categoryRepository.save(any(Category.class))).willReturn(categoryOne);
        CategoryDTO categoryOneDTO = new ModelMapper().map(categoryOne, CategoryDTO.class);

        // When/Act
        CategoryDTO savedCategory = categoryService.addCategory(categoryOneDTO);

        // Then/Assert
        assertNotNull(savedCategory);
        assertEquals("Category A", savedCategory.getCategoryName());
    }

    @DisplayName("JUnit test for Given Categories List when get All Categories Page Ascending Then return Categories Page Ascending")
    @Test
    void testGivenAllCategoriesListWhenGetAllCategoriesPageAscendingThenReturnCategoriesPageAscending() {
        // Given/Arrange
        List<Category> mockCategories = List.of(
                categoryOne,
                categoryTwo,
                categoryThree);

        Page<Category> mockPage = new PageImpl(mockCategories, PageRequest.of(0, 10), mockCategories.size());

        given(categoryRepository.findAll(any(Pageable.class))).willReturn(mockPage);

        // When/Act
        CategoryResponse categoryResponse = categoryService.getAllCategories(10, 0, "categoryName", "asc");

        // Then/Assert
        assertNotNull(categoryResponse);
        assertEquals(3L, categoryResponse.getTotalElements());
        assertEquals(0, categoryResponse.getPageNumber());
        assertEquals(1, categoryResponse.getTotalPages());
        assertEquals("Category C", categoryResponse.getContent().get(2).getCategoryName());
        assertTrue(categoryResponse.isLastPage());
    }

    @DisplayName("JUnit test for Given Categories List when get All Categories Then return Categories Page Descending")
    @Test
    void testGivenAllCategoriesListWhenGetAllCategoriesPageDescendingThenReturnCategoriesPageDescending() {
        // Given/Arrange
        List<Category> mockCategories = List.of(
                categoryThree,
                categoryTwo,
                categoryOne);

        Page<Category> mockPage = new PageImpl(mockCategories, PageRequest.of(0, 10), mockCategories.size());

        given(categoryRepository.findAll(any(Pageable.class))).willReturn(mockPage);

        // When/Act
        CategoryResponse categoryResponse = categoryService.getAllCategories(10, 0, "categoryName", "desc");

        // Then/Assert
        assertNotNull(categoryResponse);
        assertEquals(3L, categoryResponse.getTotalElements());
        assertEquals(0, categoryResponse.getPageNumber());
        assertEquals(1, categoryResponse.getTotalPages());
        assertEquals("Category A", categoryResponse.getContent().get(2).getCategoryName());
        assertTrue(categoryResponse.isLastPage());
    }

    @DisplayName("JUnit test for Given Empty Categories List when get All Categories Then Throws API Exception")
    @Test
    void testGivenEmptyCategoriesListWhenGetAllCategoriesPageThenThrowsAPIException() {
        // Given/Arrange

        Page<Category> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        given(categoryRepository.findAll(any(Pageable.class))).willReturn(mockPage);

        // When/Act
        assertThrows(APIException.class, () -> {
            categoryService.getAllCategories(10, 0, "categoryName", "desc");
        });

        // Then/Assert
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @DisplayName("JUnit test for Given Category when Delete Category then Return Deleted Category DTO")
    @Test
    void testGivenCategoryWhenDeleteCategoryThenReturnDeletedCategoryDTO() {
        // Given/Arrange
        categoryOne.setCategoryId(1L);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(categoryOne));
        willDoNothing().given(categoryRepository).deleteById(anyLong());

        // When/Act
        CategoryDTO savedCategoryDTO =  categoryService.deleteCategory(categoryOne.getCategoryId());

        // Then/Assert
        verify(categoryRepository, times(1)).delete(categoryOne);
        assertEquals("Category A", savedCategoryDTO.getCategoryName());
    }

    @DisplayName("JUnit test Given Category when Update Category then Return Category DTO")
    @Test
    void testGivenCategoryWhenUpdateCategoryThenReturnCategoryDTO() {
        // Given/Arrange

        categoryOne.setCategoryId(1L);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(categoryOne));

        given(categoryRepository.save(any(Category.class))).willReturn(categoryOne);

        CategoryDTO categoryOneDTO = new ModelMapper().map(categoryOne, CategoryDTO.class);

        // When/Act
        categoryOneDTO.setCategoryName("New Category Name");
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryOneDTO, 1L);

        // Then/Assert
        assertNotNull(updatedCategoryDTO);
        assertEquals("Category A", updatedCategoryDTO.getCategoryName());
    }

    @DisplayName("JUnit test Given Category when Update Category Not Existent Then Throws API Exception")
    @Test
    void testGivenCategoryWhenUpdateCategoryNotExistentThenThrowsAPIException() {
        // Given/Arrange
        categoryOne.setCategoryId(1L);
        given(categoryRepository.findById(categoryOne.getCategoryId())).willReturn(Optional.of(categoryOne));

        given(categoryRepository.save(any(Category.class))).willReturn(categoryOne);

        CategoryDTO categoryOneDTO = new ModelMapper().map(categoryOne, CategoryDTO.class);

        // When/Act
        categoryOneDTO.setCategoryName("New Category Name");
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.updateCategory(categoryOneDTO, 1000L);
        });

        // Then/Assert
        verify(categoryRepository, never()).save(any(Category.class));
    }
}
