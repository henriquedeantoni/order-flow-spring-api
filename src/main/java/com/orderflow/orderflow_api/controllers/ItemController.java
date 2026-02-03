package com.orderflow.orderflow_api.controllers;

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
            @RequestParam(name = "category", required = false) String category

    ){
        ItemResponse itemResponse = itemService.getAllItems(keyword, category);

        return new ResponseEntity<>(itemResponse, HttpStatus.OK);
    }

    @PostMapping("/admin/categories/{categoryId}/item")
    public ResponseEntity<ItemDTO> createItem(
            @RequestBody ItemDTO itemDTO,
            @PathVariable Long categoryId){
        ItemDTO savedItemDTO = itemService.addItem(categoryId, itemDTO);
        return new ResponseEntity<>(savedItemDTO, HttpStatus.CREATED);
    }
}
