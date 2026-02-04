package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.payload.CategoryResponse;
import com.orderflow.orderflow_api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoryResponse> getAllCategories() {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
}
