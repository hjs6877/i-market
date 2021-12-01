package com.imarket.marketdomain.service;

import com.imarket.marketdomain.domain.Order;
import com.imarket.marketdomain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveOrder(Order order) {
        String orderNumber = UUID.randomUUID().toString().toUpperCase();
        order.setOrderNumber(orderNumber);
        return orderRepository.save(order);
    }
}
