package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Cart;
import com.orderflow.orderflow_api.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    private final OffsetDateTime createDate = OffsetDateTime.of(
            2020, 1, 1, 12, 00, 0, 0, ZoneOffset.UTC
    );

    private Cart cartOne;
    private Cart cartTwo;
    private Cart cartThree;

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
        cartThree = new Cart(createDate, userThree, 50.00);

        userRepository.save(userOne);
        userRepository.save(userTwo);
        userRepository.save(userThree);
    }

    @DisplayName("JUnit test for Given Cart Object when save then Return Cart Object")
    @Test
    void testGivenCartObject_whenSave_thenReturnCartObject(){
        // Given/Arrange

        // When/Act
        Cart savedCart = cartRepository.save(cartOne);

        // Then/Assert
        assertNotNull(savedCart);
        assertTrue(savedCart.getCartId()>0);
        assertEquals("UserOne", savedCart.getUser().getUsername());
        assertNotEquals("UserTwo", savedCart.getUser().getUsername());
        assertEquals(createDate,  savedCart.getCreateDate());
        assertEquals(25.00, savedCart.getTotalPrice(), 0);
    }

    @DisplayName("JUnit test for Given Cart List when save then Return Cart List")
    @Test
    void testGivenCartList_whenSave_thenReturnCartList(){
        // Given/Arrange
        cartRepository.save(cartOne);
        cartRepository.save(cartTwo);
        cartRepository.save(cartThree);

        // When/Act
        List<Cart> savedCartList = cartRepository.findAll();

        // Then/Assert
        assertNotNull(savedCartList);
        assertTrue(savedCartList.size()>0);
        assertEquals(3, savedCartList.size());
    }

    @DisplayName("JUnit test for Given Cart Object When FindById then Return Cart Object")
    @Test
    void testGivenCartObject_whenFindById_thenReturnCartObject(){
        // Given/Arrange
        cartRepository.save(cartOne);
        cartRepository.save(cartTwo);

        // When/Act
        Cart savedCartOne = cartRepository.findById(cartOne.getCartId()).get();
        Cart savedCartTwo = cartRepository.findById(cartTwo.getCartId()).get();

        // Then/Assert
        assertNotNull(savedCartOne);
        assertEquals(cartOne.getCartId(), savedCartOne.getCartId());
        assertEquals(cartTwo.getCartId(), savedCartTwo.getCartId());
    }

    @DisplayName("JUnit test for Given Cart Object when Update Cart then Return Update Object")
    @Test
    void testGivenCartObject_whenUpdate_thenReturnCartObject(){
        // Given/Arrange
        cartRepository.save(cartOne);

        // When/Act
        Cart savedCartOne = cartRepository.findById(cartOne.getCartId()).get();
        savedCartOne.setTotalPrice(100.00);

        Cart updatedCart = cartRepository.save(savedCartOne);

        // Then/Assert
        assertNotNull(updatedCart);
        assertEquals(100.00, updatedCart.getTotalPrice(), 0);
    }

    @DisplayName("JUnit test for Given Cart Object when Delete Cart Then Remove Cart Object")
    @Test
    void testGivenCartObject_whenDelete_thenReturnCartObject(){
        // Given/Arrange
        cartRepository.save(cartOne);

        // When/Act
        cartRepository.deleteById(cartOne.getCartId());
        Optional<Cart> deletedCart = cartRepository.findById(cartOne.getCartId());

        // Then/Assert
        assertTrue(deletedCart.isEmpty());
    }

    @DisplayName("JUnit test for Given User email and Cart Object when find Cart By Email then Return Cart Object  ")
    @Test
    void testGivenUserEmailAndCart_whenFindCartByEmail_thenReturnCartObject(){
        // Given/Arrange
        cartRepository.save(cartOne);
        String userEmail = "userone@mail.com";

        // When/Act
        Cart savedCart = cartRepository.findCartByEmail(userEmail);

        // Then/Assert
        assertNotNull(savedCart);
        assertTrue(savedCart.getCartId()>0);
        assertEquals(userEmail, savedCart.getUser().getEmail());
    }
}
