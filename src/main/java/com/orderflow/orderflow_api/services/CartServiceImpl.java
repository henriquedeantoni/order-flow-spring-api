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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Transactional
    @Override
    public String deleteItemFromCart(Long cartId, Long itemId){
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findCartItemByItemIdAndCartId(cartId, itemId);

        if(cartItem == null){
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getItemPrice()*cartItem.getQuantity());

        cartItemRepository.deleteCartItemByItemIdAndCartId(itemId, cartId);

        return "Item " + cartItem.getItem().getItemName() + " has been deleted successfully";
    }

    @Override
    public CartDTO addItemToCart(Long itemId, Integer quantity) {
        Cart cart = createCartOrUpdateCart();

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId));

        CartItem cartItem = cartItemRepository.findCartItemByItemIdAndCartId(cart.getCartId(), itemId);

        if (cartItem != null){
            throw new APIException("Item" + item.getItemName() + " already exists in this cart.");
        }

        if (item.getQuantity() == 0){
            throw new APIException(item.getItemName() + " is not avaliable.");
        }

        if (item.getQuantity() < quantity){
            throw new APIException(item.getItemName() + " is not avaliable.");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setItem(item);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setItemPrice(item.getPrice());
        newCartItem.setQuantity(quantity);

        cartItemRepository.save(newCartItem);

        item.setQuantity(item.getQuantity());

        cart.setTotalPrice(item.getPrice()*quantity);

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ItemDTO> itemStream = cartItems.stream().map(it -> {
            ItemDTO map = modelMapper.map(it.getItem(), ItemDTO.class);
            map.setQuantity(it.getQuantity());
            return map;
        });

        cartDTO.setItems(itemStream.collect(Collectors.toList()));

        return cartDTO;
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

    @Override
    public CartDTO updateItemQuantityInCart(Long itemId, Integer quantity) {
        String email = authUtil.emailOnLoggedSession();
        Cart userCart = cartRepository.findCartByEmail(email);

        Long cartId = userCart.getCartId();

        Cart cartFromDb = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId));

        if(item.getQuantity() == 0){
            throw new APIException(item.getItemName() + " is not avaliable.");
        }

        if(item.getQuantity() < quantity){
            throw new APIException("Item " + item.getItemName()
                    + " has a stock quantity less than or equal to " + quantity + ". Please choose another quantity.");
        }

        CartItem cartItem = cartItemRepository.findCartItemByItemIdAndCartId(cartFromDb.getCartId(), itemId);

        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0){
            throw new APIException("Error: the resulting quantity cannot be negative.");
        }

        if (newQuantity == 0){
            deleteItemFromCart(cartId, itemId);
        } else {
            cartItem.setQuantity(newQuantity);
            cartItem.setItemPrice(item.getPrice()*quantity);
            cartItem.setDiscount(item.getDiscount());
            cartFromDb.setTotalPrice(cartFromDb.getTotalPrice() + (cartItem.getItemPrice()*quantity));
            cartRepository.save(cartFromDb);
        }

        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        if(updatedCartItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedCartItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cartFromDb, CartDTO.class);

        List<CartItem> cartItems = cartFromDb.getCartItems();

        Stream<ItemDTO> itemStream = cartItems.stream().map(it -> {
                   ItemDTO itemDTO = modelMapper.map(it.getItem(), ItemDTO.class);
                   itemDTO.setQuantity(it.getQuantity());
                   return itemDTO;
                });

        cartDTO.setItems(itemStream.collect(Collectors.toList()));

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
