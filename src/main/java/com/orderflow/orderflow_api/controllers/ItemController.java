package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.payload.ItemResponse;
import com.orderflow.orderflow_api.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
