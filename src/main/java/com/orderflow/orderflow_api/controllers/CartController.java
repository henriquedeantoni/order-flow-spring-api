package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.payload.CartDTO;
import com.orderflow.orderflow_api.payload.CartItemDTO;
import com.orderflow.orderflow_api.repositories.CartRepository;
import com.orderflow.orderflow_api.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/admin/carts")
    public ResponseEntity<List<CartDTO>>  getCarts() {
        List<CartDTO> cartList = cartService.getAllCarts();

        return new ResponseEntity<>(cartList, HttpStatus.FOUND);
    }

    @PostMapping("/cart/create")
    public ResponseEntity<String>  createCart(@RequestBody List<CartItemDTO> cartItemsDTO) {
        String response = cartService.createCartWithItems(cartItemsDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}


