package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.*;
import com.orderflow.orderflow_api.payload.OrderDTO;
import com.orderflow.orderflow_api.payload.OrderItemDTO;
import com.orderflow.orderflow_api.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDTO placeOrder(String emailAuth, Long localId, int paymentMethod, String pagName, String pagPaymentId, String pagResponseMessage) {
        Cart cart = cartRepository.findCartByEmail(emailAuth);
        if(cart==null){
            throw new ResourceNotFoundException("Cart", "email", emailAuth);
        }

        Local local = localRepository.findById(localId).orElseThrow(() -> new ResourceNotFoundException("Local", "id", localId));

        Order order = new Order();
        order.setEmail(emailAuth);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus(OrderStatus.STATUS_PENDING);
        order.setPrevStatus(OrderStatus.STATUS_PENDING);
        order.setLocal(local);

        Payment payment = new Payment(PaymentMethod.valueOf(paymentMethod), pagPaymentId, PaymentStatus.PAYMENT_PENDING, pagResponseMessage, pagName);
        payment.setOrder(order);
        Payment savedPayment = paymentRepository.save(payment);
        order.setPayment(savedPayment);

        Order savedOrder = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()){
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(cartItem.getItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderProductPrice(cartItem.getItemPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(cartItem -> {
            int quantity = cartItem.getQuantity();
            Item item = cartItem.getItem();
            item.setQuantity(quantity); //TODO desenvolver melhor a lógica de quantidade
            itemRepository.save(item);

            cartService.deleteItemFromCart(cart.getCartId(), cartItem.getItem().getItemId());
        });

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

        orderItems.forEach(orderItem -> orderDTO.getOrderItems().add(modelMapper.map(orderItem, OrderItemDTO.class)));
        orderDTO.setLocalId(localId);

        return orderDTO;
    }
}
