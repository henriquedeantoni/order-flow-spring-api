package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.payload.ItemResponse;
import java.time.Instant;

public interface ItemService {
    ItemResponse getAllItems(String keyword, String category, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

    ItemDTO addItem(Long categoryId, ItemDTO itemDTO);

    ItemDTO updateItem(ItemDTO itemDTO, Long itemId);

    ItemDTO updateItemAndCategory(ItemDTO itemDTO, Long itemId, Long categoryId);

    ItemDTO updateItemStatus(Long itemId, String status);

    ItemDTO updatedItemImage(Long itemId, Long imageId);

    ItemDTO findById(Long itemId);

    ItemResponse getAllItemsByCategoryId(Long categoryId, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

    ItemResponse getAllItemsByKeyword(String keyword, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

    ItemResponse getItemsCreatedInInterval(Instant firstDate, Instant lastDate, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

    ItemDTO deleteItem(Long itemId);

    String createDashboardBarItemByCategories(Integer qtyLayers, String axisLabelName, String valuesLabelName, String chartTitleName);

    String createDashboardPieItemByCategories(Integer qtyLayers, String chartTitleName);

    String createDashboardTimeSeriesMonthlyItem(Instant firstDate, Instant lastDate, String chartTitleName, String axisTitleName, String valuesTitleName);

    String createDashboardTimeSeriesYearItem(Instant firstDate, Instant lastDate, String chartTitleName, String axisTitleName, String valuesTitleName);
}
