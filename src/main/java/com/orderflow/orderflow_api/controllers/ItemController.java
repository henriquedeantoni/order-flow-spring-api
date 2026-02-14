package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.config.AppConsts;
import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.payload.ItemResponse;
import com.orderflow.orderflow_api.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/public/items")
    public ResponseEntity<ItemResponse> getAllItems(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_ITEMS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ){
        ItemResponse itemResponse = itemService.getAllItems(keyword, category, pageSize, pageNumber, sortBy, sortOrder);

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
            @RequestParam Long itemId){
        ItemDTO updatedItemDTO = itemService.updateItem(itemDTO, itemId);

        return new ResponseEntity<>(updatedItemDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/items/{itemId}/item/{categoryId}")
    public ResponseEntity<ItemDTO> updateItemAndCategory(
            @RequestBody ItemDTO itemDTO,
            @RequestParam Long itemId,
            @RequestParam Long categoryId){
        ItemDTO updatedItemDTO = itemService.updateItemAndCategory(itemDTO, itemId, categoryId);

        return new ResponseEntity<>(updatedItemDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/items/{itemId}/item/{status}")
    public ResponseEntity<ItemDTO> updateItemStatus(
            @RequestParam Long itemId,
            @RequestParam String status
    ){
        ItemDTO updatedItemDTO = itemService.updateItemStatus(itemId, status);

        return new ResponseEntity<>(updatedItemDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/items/{itemId}/image/{imageId}")
    public ResponseEntity<ItemDTO> updatedItemImage(
            @RequestParam Long itemId,
            @RequestParam Long imageId){
        ItemDTO updateItem = itemService.updatedItemImage(itemId, imageId);

        return new ResponseEntity<>(updateItem, HttpStatus.OK);
    }



}
