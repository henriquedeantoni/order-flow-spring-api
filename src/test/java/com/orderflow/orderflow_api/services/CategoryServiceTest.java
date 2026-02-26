package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.User;
import com.orderflow.orderflow_api.payload.CategoryDTO;
import com.orderflow.orderflow_api.repositories.CategoryRepository;
import com.orderflow.orderflow_api.security.util.AuthUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    @Mock
    ModelMapper modelMapper;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private CategoryService categoryService;

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
}
