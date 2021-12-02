package com.imarket.marketapi.apis.dto;

import com.imarket.marketdomain.domain.*;
import lombok.Data;
import org.springframework.data.domain.Page;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDto {
    private long orderId;
    private String email;
    private String name;
    private String orderNumber;
    private Order.OrderStatus orderStatus;
    private String productName;
    private int amount;
    private LocalDateTime createdAt;

    @Data
    public static class Post {
        @Positive
        private long productId;

        @Positive
        private long buyerId;

        @Positive
        private long sellerId;

        @Positive
        private long paymentId;

        @Positive
        private int amount;
    }

    public Order toOrder(OrderDto.Post orderPostDto) {
        Seller seller = new Seller();
        Buyer buyer = new Buyer();
        Product product = new Product();
        Payment payment = new Payment();
        Order order = new Order();

        seller.setSellerId(orderPostDto.getSellerId());
        buyer.setBuyerId(orderPostDto.getBuyerId());
        product.setProductId(orderPostDto.getProductId());
        payment.setPaymentId(orderPostDto.getPaymentId());
        order.setSeller(seller);
        order.setBuyer(buyer);
        order.setProduct(product);
        order.setPayment(payment);
        order.setAmount(orderPostDto.getAmount());
        return order;
    }

    public OrderDto toOrderDto(Order order) {
        this.setOrderId(order.getOrderId());
        this.setOrderNumber(order.getOrderNumber());
        this.setOrderStatus(order.getOrderStatus());
        this.setProductName(order.getProduct().getProductName());
        this.setAmount(order.getAmount());
        this.setCreatedAt(order.getCreatedAt());

        return this;
    }

    public static List<OrderDto> toOrderDtoList(Page<Order> orderPage) {
        return orderPage.getContent()
                .stream()
                .map(order -> {
                    OrderDto orderDto = new OrderDto();
                    orderDto.setEmail(order.getBuyer().getMember().getEmail());
                    orderDto.setName(order.getBuyer().getMember().getName());
                    return orderDto.toOrderDto(order);
                }).collect(Collectors.toList());
    }
}
