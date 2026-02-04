package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories() throws APIException;
}
