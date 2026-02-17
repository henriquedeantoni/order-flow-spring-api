package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.models.Cart;
import com.orderflow.orderflow_api.payload.CartDTO;
import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.repositories.CartRepository;
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
    private ModelMapper modelMapper;

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


}
