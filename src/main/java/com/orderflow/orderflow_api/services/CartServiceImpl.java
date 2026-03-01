package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Cart;
import com.orderflow.orderflow_api.models.CartItem;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.payload.CartDTO;
import com.orderflow.orderflow_api.payload.CartItemDTO;
import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.repositories.CartItemRepository;
import com.orderflow.orderflow_api.repositories.CartRepository;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import com.orderflow.orderflow_api.security.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> cartList = cartRepository.findAll();

        if(cartList.isEmpty()){
            throw new APIException("There isnt any cart registered.");
        }

        List<CartDTO> cartDTOs = cartList.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ItemDTO> itemDTOList = cart.getCartItems().stream().map(cartItem -> {
                ItemDTO itemDTO = modelMapper.map(cartItem.getItem(), ItemDTO.class);
                itemDTO.setQuantity(cartItem.getQuantity());
                return itemDTO;
            }).toList();

            cartDTO.setItems(itemDTOList);

            return cartDTO;
        }).toList();

        return cartDTOs;
    }

    @Override
    public String createCartWithItems(List<CartItemDTO> cartItemsDTO) {
        String email = authUtil.emailOnLoggedSession();

        Cart existingUserCart = cartRepository.findCartByEmail(email);
        if(existingUserCart == null){
            existingUserCart = new Cart();
            existingUserCart.setTotalPrice(0.0);
            existingUserCart.setUser(authUtil.userOnLoggedSession());
            existingUserCart = cartRepository.save(existingUserCart);
        } else {
            cartItemRepository.deleteAllByCartId(existingUserCart.getCartId()); //clear all cart items from current cart
        }

        double totalPrice = 0.0;

        for (CartItemDTO cartItemDTO : cartItemsDTO) {
            Long itemId = cartItemDTO.getItemId();
            Integer quantity = cartItemDTO.getQuantity();

            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId));

            totalPrice += item.getPrice()*quantity;

            CartItem cartItem = new CartItem();
            cartItem.setItem(item);
            cartItem.setCart(existingUserCart);
            cartItem.setItemPrice(item.getPrice());
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        existingUserCart.setTotalPrice(totalPrice);
        cartRepository.save(existingUserCart);

        return "Cart created or updated successfully";
    }

    @Override
    public CartDTO addItemToCart(String itemId, Integer quantity) {
        return null;
    }

    @Override
    public Cart findCartByEmail(String emailId) {
        return cartRepository.findCartByEmail(emailId);
    }

    @Override
    public CartDTO getCart(String email, Long cartId) {
        Cart cartFromDb = cartRepository.findCartByEmailAndCartId(email, cartId);
        if(cartFromDb == null){
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        CartDTO cartDTO = modelMapper.map(cartFromDb, CartDTO.class);

        cartFromDb.getCartItems().forEach(cartItem -> cartItem.getItem().setQuantity(cartItem.getQuantity()));

        List<ItemDTO> items = cartFromDb.getCartItems().stream()
                .map(ci -> modelMapper.map(ci.getItem(), ItemDTO.class)).toList();

        cartDTO.setItems(items);

        return cartDTO;
    }

    private Cart createCartOrUpdateCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.emailOnLoggedSession());

        if(userCart != null){
            return userCart;
        }

        Cart cart = new Cart();
        cart.setUser(authUtil.userOnLoggedSession());
        cart.setTotalPrice(0.0);

        Cart savedCart = cartRepository.save(cart);
        return savedCart;
    }

}
