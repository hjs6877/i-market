package com.imarket.marketapi.apis.dto;

import com.imarket.marketdomain.domain.*;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class OrderDto {
    private long orderId;
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

        seller.setSellerId(orderPostDto.getSellerId());
        buyer.setBuyerId(orderPostDto.getBuyerId());
        product.setProductId(orderPostDto.getProductId());

        order.setSeller(seller);
        order.setBuyer(buyer);
        order.setProduct(product);
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
}
