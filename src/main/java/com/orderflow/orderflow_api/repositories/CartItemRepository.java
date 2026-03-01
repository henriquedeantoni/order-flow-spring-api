package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Cart;
import com.orderflow.orderflow_api.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1")
    void deleteAllByCartId(Long cartId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.item.id = ?2")
    CartItem findCartItemByItemIdAndCartId(Long cartId, Long itemId);
}
