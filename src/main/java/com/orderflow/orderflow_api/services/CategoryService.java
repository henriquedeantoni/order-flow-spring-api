package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.payload.CategoryDTO;
import com.orderflow.orderflow_api.payload.CategoryResponse;
import jakarta.validation.Valid;

public interface CategoryService {
    CategoryResponse getAllCategories() throws APIException;

    CategoryDTO addCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(@Valid CategoryDTO categoryDTO, Long categoryId);
}
