package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.OrderDTO;

public interface OrderService {
    OrderDTO placeOrder(String emailAuth, Long localId, String paymentMethod, String pagName, String pagPaymentId, String pagStatus, String pagResponseMessage);
}
