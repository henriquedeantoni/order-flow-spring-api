package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.CartDTO;

import java.util.List;

public interface CartService {
    List<CartDTO> getAllCarts();
}
