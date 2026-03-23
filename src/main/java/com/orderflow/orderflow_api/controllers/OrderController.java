package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.security.util.AuthUtil;
import com.orderflow.orderflow_api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/order/allusers/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderbA
}
