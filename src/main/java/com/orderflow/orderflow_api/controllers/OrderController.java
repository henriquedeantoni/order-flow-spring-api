package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.payload.OrderDTO;
import com.orderflow.orderflow_api.payload.OrderRequestDTO;
import com.orderflow.orderflow_api.security.util.AuthUtil;
import com.orderflow.orderflow_api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/order/allusers/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderItems(
            @PathVariable("paymentMethod") String paymentMethod,
            @RequestBody OrderRequestDTO orderRequestDTO) {

        String emailAuth = authUtil.emailOnLoggedSession();
        OrderDTO orderDTO = orderService.placeOrder(
                emailAuth,
                orderRequestDTO.getLocalId(),
                paymentMethod,
                orderRequestDTO.getPagName(),
                orderRequestDTO.getPagPaymentId(),
                orderRequestDTO.getPagStatus(),
                orderRequestDTO.getPagResponseMessage()
        );
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
