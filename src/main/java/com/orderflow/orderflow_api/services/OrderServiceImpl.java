package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.OrderDTO;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public OrderDTO placeOrder(String emailAuth, Long localId, String paymentMethod, String pagName, String pagPaymentId, String pagStatus, String pagResponseMessage) {
        return null;
    }
}
