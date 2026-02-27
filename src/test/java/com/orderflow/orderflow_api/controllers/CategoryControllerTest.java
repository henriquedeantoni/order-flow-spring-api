package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.payload.CategoryDTO;
import com.orderflow.orderflow_api.services.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    @DisplayName("Should do nothing on delete case, when with success")
    void shouldDeleteCategoryWhenSuccess() throws Exception {
        // ------------ ARRANGE --------------
        Long categoryId = 1L;

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(categoryId);
        categoryDTO.setCategoryName("Category name 1");

        // ------------ ACT --------------
        when(categoryService.deleteCategory(categoryId)).thenReturn(categoryDTO);
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/categories/{categoryId}"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.categoryId").value(categoryId))
                .andExpect((ResultMatcher) jsonPath("$.categoryName").value("Category name 1"));

        // ------------ ASSERT --------------

        verify(categoryService, times(1)).deleteCategory(categoryId);
    }
}
