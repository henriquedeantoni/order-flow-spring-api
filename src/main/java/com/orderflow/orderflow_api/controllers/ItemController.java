package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.config.AppConsts;
import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.payload.ItemResponse;
import com.orderflow.orderflow_api.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/v1/auth")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/public/items")
    public ResponseEntity<ItemResponse> getAllItems(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_ITEMS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ){
        ItemResponse itemResponse = itemService.getAllItems(keyword, pageSize, pageNumber, sortBy, sortOrder);

        return new ResponseEntity<>(itemResponse, HttpStatus.OK);
    }

    @PostMapping("/admin/items/{categoryId}/item")
    public ResponseEntity<ItemDTO> createItem(
            @RequestBody ItemDTO itemDTO,
            @PathVariable Long categoryId){
        ItemDTO savedItemDTO = itemService.addItem(categoryId, itemDTO);
        return new ResponseEntity<>(savedItemDTO, HttpStatus.CREATED);
    }

    @PutMapping("/admin/items/{itemId}/item")
    public ResponseEntity<ItemDTO> updateItem(
            @RequestBody ItemDTO itemDTO,
            @PathVariable Long itemId){
        ItemDTO updatedItemDTO = itemService.updateItem(itemDTO, itemId);

        return new ResponseEntity<>(updatedItemDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/items/{itemId}/item/{categoryId}")
    public ResponseEntity<ItemDTO> updateItemAndCategory(
            @RequestBody ItemDTO itemDTO,
            @PathVariable Long itemId,
            @PathVariable Long categoryId){
        ItemDTO updatedItemDTO = itemService.updateItemAndCategory(itemDTO, itemId, categoryId);

        return new ResponseEntity<>(updatedItemDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/items/{itemId}/item/{status}")
    public ResponseEntity<ItemDTO> updateItemStatus(
            @PathVariable Long itemId,
            @PathVariable String status
    ){
        ItemDTO updatedItemDTO = itemService.updateItemStatus(itemId, status);

        return new ResponseEntity<>(updatedItemDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/items/{itemId}/image/{imageId}")
    public ResponseEntity<ItemDTO> updatedItemImage(
            @PathVariable Long itemId,
            @PathVariable Long imageId){
        ItemDTO updateItem = itemService.updatedItemImage(itemId, imageId);

        return new ResponseEntity<>(updateItem, HttpStatus.OK);
    }

    @GetMapping("/public/items/item/{itemId}")
    public ResponseEntity<ItemDTO> getItemById(
            @PathVariable Long itemId){
        ItemDTO itemDTO = itemService.findById(itemId);

        return new ResponseEntity<>(itemDTO, HttpStatus.FOUND);
    }

    @GetMapping("/public/items/category/{categoryId}")
    public ResponseEntity<ItemResponse> getAllItemsByCategoryId(
            @PathVariable(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_ITEMS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ){
        ItemResponse itemResponse = itemService.getAllItemsByCategoryId( categoryId, pageSize, pageNumber, sortBy, sortOrder);

        return new ResponseEntity<>(itemResponse, HttpStatus.OK);
    }

    @GetMapping("/public/items/category/{categoryId}/keyword")
    public ResponseEntity<ItemResponse> getAllItemsByCategoryIdAndKeyword(
            @PathVariable Long categoryId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_ITEMS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ){

        ItemResponse itemResponse = itemService.getAllItemsByCategoryIdAndKeyword(categoryId, keyword, pageSize, pageNumber, sortBy, sortOrder);

        return new ResponseEntity<>(itemResponse, HttpStatus.OK);
    }

    @GetMapping("/admin/items/date-created/between")
    public ResponseEntity<ItemResponse> getItemsByDateIntervalCreated(
            @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant firstDate,
            @RequestParam(name = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant lastDate,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_ITEMS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder)
        {
            ItemResponse itemResponse = itemService.getItemsCreatedInInterval(firstDate, lastDate, pageSize, pageNumber, sortBy, sortOrder);
            return new ResponseEntity<>(itemResponse, HttpStatus.OK);
    }

    @DeleteMapping("/admin/items/{itemId}")
    public ResponseEntity<ItemDTO> deleteItemById(@PathVariable Long itemId){
        return new ResponseEntity<>(itemService.deleteItem(itemId), HttpStatus.OK);
    }

    @GetMapping(value = "/admin/dashboard/barchart/items/categories")
    public ResponseEntity<String> getDashboardBarItemByCategories(
            @RequestParam(name = "quantityLayers", defaultValue = AppConsts.QUANTITY_LAYERS, required = true) Integer qtyLayers,
            @RequestParam(name = "axisLabelName", required = true) String axisLabelName,
            @RequestParam(name = "valuesLabelName", required = true) String valuesLabelName,
            @RequestParam(name = "chartTitleName", required = true) String chartTitleName
    ){
        String response = itemService.createDashboardBarItemByCategories(qtyLayers, axisLabelName, valuesLabelName, chartTitleName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/dashboard/piechart/items/categories")
    public ResponseEntity<String> getDashboardPieItemByCategories(
            @RequestParam(name = "quantityLayers", defaultValue = AppConsts.QUANTITY_LAYERS, required = true) Integer qtyLayers,
            @RequestParam(name = "chartTitleName", required = true) String chartTitleName
    ){
        String response = itemService.createDashboardPieItemByCategories(qtyLayers, chartTitleName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/dashboard/timeseries/monthly/items")
    public ResponseEntity<String> getDashboardTimeSeriesMonthlyItem(
            @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant firstDate,
            @RequestParam(name = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant lastDate,
            @RequestParam(name = "chartTitleName", required = true) String chartTitleName,
            @RequestParam(name = "axisLabelName", required = true) String axisLabelName,
            @RequestParam(name = "valuesLabelName", required = true) String valuesLabelName
    ){
        String response = itemService.createDashboardTimeSeriesMonthlyItem(firstDate, lastDate, chartTitleName, axisLabelName, valuesLabelName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
