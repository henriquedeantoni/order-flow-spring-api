package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.payload.ItemResponse;

public interface ItemService {
    ItemResponse getAllItems(String keyword, String category, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

    ItemDTO addItem(Long categoryId, ItemDTO itemDTO);
}
