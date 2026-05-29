package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
