package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.payload.ItemResponse;
import com.orderflow.orderflow_api.services.ItemService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/public/items")
    public ResponseEntity<ItemResponse> getAllItems(){
        ItemResponse itemResponse = itemService.getAllItems();

        return new ResponseEntity<>(itemResponse, HttpStatus.OK);
    }
}
