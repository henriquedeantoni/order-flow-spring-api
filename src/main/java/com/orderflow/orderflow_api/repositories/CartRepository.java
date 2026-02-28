package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findCartByEmail(String email);

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.item p WHERE p.id = ?1")
    List<Cart> findCartsByItemId(Long itemId);
}
