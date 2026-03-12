package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.models.Cart;
import com.orderflow.orderflow_api.payload.CartDTO;
import com.orderflow.orderflow_api.payload.CartItemDTO;
import com.orderflow.orderflow_api.repositories.CartRepository;
import com.orderflow.orderflow_api.security.util.AuthUtil;
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

    @Autowired
    private AuthUtil authUtil;

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

    @PostMapping("/carts/items/{itemId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addItemToCart(
            @PathVariable("itemId") Long itemId,
            @PathVariable("quantity") Integer quantity) {
        CartDTO cartDTO = cartService.addItemToCart(itemId, quantity);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO>  getCartsByUserId() {
        String email = authUtil.emailOnLoggedSession();
        Cart cart = cartService.findCartByEmail(email);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(email, cartId);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/carts/items/{itemId}/quantity/{operation}")
    public ResponseEntity<CartDTO>  updateCartItem(
            @PathVariable Long itemId,
            @PathVariable String operation) {
        CartDTO cartDTO = cartService.updateItemQuantityInCart(itemId, operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/items/{itemId}")
    public ResponseEntity<String> deleteItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        String response = cartService.deleteItemFromCart(cartId, itemId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


