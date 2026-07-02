package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Cart;
import com.orderflow.orderflow_api.models.CartItem;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private final OffsetDateTime createDate = OffsetDateTime.of(
            2020, 1, 1, 12, 00, 0, 0, ZoneOffset.UTC
    );

    private Cart cartOne;
    private Cart cartTwo;

    private Item itemOne;
    private Item itemTwo;
    private Item itemThree;

    private CartItem cartItemOne;
    private CartItem cartItemTwo;
    private CartItem cartItemThree;

    private CartItem cartItemFour;
    private CartItem cartItemFive;
    private CartItem cartItemSix;

    private User userOne;
    private User userTwo;
    private User userThree;

    @BeforeEach
    public void setUp(){

        userOne = new User(
                "UserOne",
                "userone@mail.com",
                "hashPassword1",
                "John",
                "Doe"
        );

        userTwo = new User(
                "UserTwo",
                "userTwo@mail.com",
                "hashPassword2",
                "John",
                "Doe Two"
        );

        userThree = new User(
                "UserThree",
                "userThree@mail.com",
                "hashPassword3",
                "John",
                "Doe Three"
        );

        cartOne = new Cart(createDate, userOne, 25.00);
        cartTwo = new Cart(createDate, userTwo, 15.00);

        cartItemOne = new CartItem(2, 20.0, 125.00);
        cartItemTwo = new CartItem(3, 5.0, 50.00);
        cartItemThree = new CartItem(1, 0.0, 80.00);

        cartItemFour = new CartItem(2, 20.0, 125.00);
        cartItemFive = new CartItem(3, 5.0, 50.00);
        cartItemSix = new CartItem(1, 0.0, 80.00);

        itemOne = new Item("Item One","Item one description" ,1 ,125.00);
        itemTwo = new Item("Item Two","Item two description" ,1 ,50.00);
        itemThree = new Item("Item Three","Item three description" ,1 ,80.00);

        itemOne.getItems().add(cartItemOne);
        itemTwo.getItems().add(cartItemTwo);
        itemThree.getItems().add(cartItemThree);

        itemOne.getItems().add(cartItemFour);
        itemTwo.getItems().add(cartItemFive);
        itemThree.getItems().add(cartItemSix);

        cartItemOne.setItem(itemOne);
        cartItemTwo.setItem(itemTwo);
        cartItemThree.setItem(itemThree);
        cartItemFour.setItem(itemOne);
        cartItemFive.setItem(itemTwo);
        cartItemSix.setItem(itemThree);

        //cartOne.setCartItems(List.of(cartItemOne, cartItemTwo, cartItemThree));
        cartOne.setCartItems(new ArrayList<>(List.of(
                cartItemOne,
                cartItemTwo,
                cartItemThree
        )));

        cartTwo.setCartItems(new ArrayList<>(List.of(
                cartItemFour,
                cartItemFive,
                cartItemSix
        )));

        itemRepository.save(itemOne);
        itemRepository.save(itemTwo);
        itemRepository.save(itemThree);

        userRepository.save(userOne);
        userRepository.save(userTwo);
        userRepository.save(userThree);
    }

    @DisplayName("JUnit test for Given Cart Object when save then Return Cart Object")
    @Test
    void testGivenCartObject_whenSave_thenReturnCartObject(){
        // Given/Arrange

        // When/Act
        CartItem savedCartItem = cartItemRepository.save(cartItemOne);

        // Then/Assert
        assertNotNull(savedCartItem);
        assertTrue(savedCartItem.getCartItemId()>0);
        assertEquals(cartItemOne.getCartItemId(), savedCartItem.getCartItemId());
        assertEquals("Item One", savedCartItem.getItem().getItemName());
    }

    @DisplayName("JUnit test for Given Cart Object when findById then Return Cart Object")
    @Test
    void testGivenCartObject_whenFindById_thenReturnCartObject(){
        // Given/Arrange
        cartItemRepository.save(cartItemOne);

        // When/Act
        CartItem savedCartItem = cartItemRepository.findById(cartItemOne.getCartItemId()).get();

        // Then/Assert
        assertNotNull(savedCartItem);
        assertTrue(savedCartItem.getCartItemId()>0);
        assertEquals(cartItemOne.getCartItemId(), savedCartItem.getCartItemId());
        assertEquals("Item One", savedCartItem.getItem().getItemName());
    }

    @DisplayName("JUnit test for Given Cart Object when updated object then Return Updated Cart")
    @Test
    void testGivenCartObject_whenUpdatedObject_thenReturnUpdatedCart(){
        // Given/Arrange
        cartItemRepository.save(cartItemOne);

        // When/Act
        CartItem savedCartItem = cartItemRepository.findById(cartItemOne.getCartItemId()).get();
        savedCartItem.setItemPrice(99.99);
        savedCartItem.setDiscount(0.01);
        savedCartItem.setQuantity(99);

        CartItem updatedCartItem = cartItemRepository.save(savedCartItem);

        // Then/Assert
        assertNotNull(updatedCartItem);
        assertTrue(updatedCartItem.getCartItemId()>0);
        assertEquals(savedCartItem.getCartItemId(), updatedCartItem.getCartItemId());
        assertEquals(99.99 , updatedCartItem.getItemPrice(), 0.0001);
        assertEquals(0.01 , updatedCartItem.getDiscount(), 0.0001);
        assertEquals(99, updatedCartItem.getQuantity());
    }

    @DisplayName("JUnit test for Given Cart when delete by id then Remove Object")
    @Test
    void testGivenCartObject_whenDeleteById_thenReturnCartObject(){
        // Given/Arrange
        cartItemRepository.save(cartItemOne);

        // When/Act
        cartItemRepository.deleteById(cartItemOne.getCartItemId());

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemOne.getCartItemId());

        // Then/Assert
        assertTrue(cartItemOptional.isEmpty());
    }

}
