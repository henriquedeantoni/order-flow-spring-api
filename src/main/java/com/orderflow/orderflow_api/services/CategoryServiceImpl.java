package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.payload.CategoryDTO;
import com.orderflow.orderflow_api.payload.CategoryResponse;
import com.orderflow.orderflow_api.repositories.CategoryRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(){
        List<Category> categoryList = categoryRepository.findAll();

        if(categoryList.isEmpty() || categoryList == null)
            throw new APIException("Any category has created by now.");

        List<CategoryDTO> categoriesDTO = categoryList.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoriesDTO);

        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category createdCategory = modelMapper.map(categoryDTO, Category.class);
        Category categoryDb = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if(categoryDb != null)
            throw new APIException("Category with name " + categoryDTO.getCategoryName() + " already exists.");
        createdCategory = categoryRepository.save(createdCategory);
        CategoryDTO createdCategoryDTO = modelMapper.map(createdCategory, CategoryDTO.class);
        return createdCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId ));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

}
