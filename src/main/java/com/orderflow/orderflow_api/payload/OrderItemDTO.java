package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private ItemDTO item;
    private Integer quantity;
    private double discount;
    private double orderProductPrice;
}
