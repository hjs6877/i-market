package com.imarket.marketapi.apis.dto;

import com.imarket.marketdomain.domain.*;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class OrderDto {
    private long id;
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
        private int amount;
    }

    public Order toOrder(OrderDto.Post orderPostDto) {
        Seller seller = new Seller();
        Buyer buyer = new Buyer();
        Product product = new Product();
        Order order = new Order();

        seller.setId(orderPostDto.getSellerId());
        buyer.setId(orderPostDto.getBuyerId());
        product.setId(orderPostDto.getProductId());

        order.setSeller(seller);
        order.setBuyer(buyer);
        order.setProduct(product);
        order.setAmount(orderPostDto.getAmount());
        return order;
    }

    public OrderDto toOrderDto(Order order) {
        this.setId(order.getId());
        this.setOrderNumber(order.getOrderNumber());
        this.setOrderStatus(order.getOrderStatus());
        this.setAmount(order.getAmount());
        this.setCreatedAt(order.getCreatedAt());

        // TODO Product name 채우는 작업
        return this;
    }
}
