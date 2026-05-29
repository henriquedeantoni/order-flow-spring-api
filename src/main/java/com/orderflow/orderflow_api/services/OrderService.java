package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.OrderStatus;
import com.orderflow.orderflow_api.payload.OrderDTO;

public interface OrderService {
    OrderDTO placeOrder(String emailAuth, Long localId, int paymentMethod, String pagName, String pagPaymentId, String pagResponseMessage);
}
