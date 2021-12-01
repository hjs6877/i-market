package com.imarket.marketdomain.service;

import com.imarket.marketdomain.domain.Order;
import com.imarket.marketdomain.exception.OrderNotFoundException;
import com.imarket.marketdomain.repository.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // Requirement: 주문 번호는 중복이 불가능한 임의의 영문 대문자, 숫자 조합이어야 한다.
        String orderNumber = UUID.randomUUID().toString().toUpperCase();
        order.setOrderNumber(orderNumber);
        return orderRepository.save(order);
    }

    public Order findOrderById(long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.orElseThrow(OrderNotFoundException::new);
    }
}
