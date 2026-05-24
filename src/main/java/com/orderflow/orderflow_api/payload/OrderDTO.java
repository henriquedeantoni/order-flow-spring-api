package com.orderflow.orderflow_api.payload;

import com.orderflow.orderflow_api.models.OrderStatus;
import com.orderflow.orderflow_api.models.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;
    private List<OrderItemDTO> orderItems;
    private LocalDate orderDate;
    private PaymentMethod paymentMethod;
    private OrderStatus orderStatus;
    private Long localId;
}
