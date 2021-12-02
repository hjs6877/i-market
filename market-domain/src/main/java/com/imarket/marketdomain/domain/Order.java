package com.imarket.marketdomain.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"buyer", "seller", "product"})
@Entity(name = "product_order")
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST;

    @Column(nullable = false)
    private int amount;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime paidAt;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public void setBuyer(Buyer buyer) {
        if (this.buyer != null) {
            this.buyer.getOrders().remove(this);
        }

        this.buyer = buyer;

        if (!this.buyer.getOrders().contains(this)) {
            this.buyer.getOrders().add(this);
        }
    }

    public void setSeller(Seller seller) {
        if (this.seller != null) {
            this.seller.getOrders().remove(this);
        }

        this.seller = seller;

        if (!this.seller.getOrders().contains(this)) {
            this.seller.getOrders().add(this);
        }
    }

    public void setProduct(Product product) {
        if (this.product != null) {
            this.product.getOrders().remove(this);
        }
        this.product = product;

        if (!this.product.getOrders().contains(this)) {
            this.product.getOrders().add(this);
        }
    }

    public void setPayment(Payment payment) {
        if (this.payment != null) {
            this.payment.getOrders().remove(this);
        }
        this.payment = payment;

        if (this.payment.getOrders().contains(this)) {
            this.payment.getOrders().add(this);
        }
    }
    public enum OrderStatus{
        ORDER_REQUEST,
        ORDER_CANCEL,
        ORDER_RETURN,
        ORDER_EXCHANGE
    }
}
