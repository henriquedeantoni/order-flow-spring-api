package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Cart;
import com.orderflow.orderflow_api.payload.CartDTO;
import com.orderflow.orderflow_api.payload.CartItemDTO;

import java.util.List;

public interface CartService {
    List<CartDTO> getAllCarts();

    String createCartWithItems(List<CartItemDTO> cartItemsDTO);

    CartDTO addItemToCart(Long itemId, Integer quantity);

    Cart findCartByEmail(String emailId);

    CartDTO getCart(String email, Long cartId);

    CartDTO updateItemQuantityInCart(Long itemId, Integer quantity);

    String deleteItemFromCart(Long cartId, Long itemId);
}
