package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.config.AppConsts;
import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.payload.CategoryDTO;
import com.orderflow.orderflow_api.payload.CategoryResponse;
import com.orderflow.orderflow_api.services.CategoryService;
import jakarta.validation.Valid;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse = categoryService.getAllCategories(pageSize, pageNumber, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategoryDTO = categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(createdCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryDTO categoryDeleted = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(categoryDeleted, HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }

}
