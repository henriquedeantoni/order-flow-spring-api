package com.orderflow.orderflow_api.services;

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
import static org.mockito.BDDMockito.given;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
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
        categoryOne.setCategoryName("Category A");
        categoryOne.setIncludedDate(firstDate);

        categoryTwo.setCategoryName("Category B");
        categoryTwo.setIncludedDate(firstDate);

        categoryThree.setCategoryName("Category C");
        categoryThree.setIncludedDate(firstDate);
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

    @DisplayName("JUnit test for Given Categories List when get All Categories Then return Category List")
    @Test
    void testGivenGetAllCategoriesListWhenGetAllCategoriesPageAscendingThenReturnAllCategoriesPageAscending() {
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

    @DisplayName("JUnit test for Given Categories List when get All Categories Then return Category List")
    @Test
    void testGivenGetAllCategoriesListWhenGetAllCategoriesPageDescendingThenReturnAllCategoriesPageDescending() {
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

    @DisplayName("JUnit test for Given Categories L")
    @Test
    void testGivenCategoryWhenDeleteCategoriyThenReturnDeletedCategory() {
        // Given/Arrange
        categoryOne.setCategoryId(1L);
        given(categoryRepository.findByCategoryId(anyLong())).willReturn(categoryOne);
        willDoNothing().given(categoryService).deleteCategory(categoryOne.getCategoryId());

        // When/Act
        CategoryDTO savedCategoryDTO =  categoryService.deleteCategory(categoryOne.getCategoryId());

        // Then/Assert
        verify(categoryRepository, times(1)).delete(categoryOne);

    }
}
