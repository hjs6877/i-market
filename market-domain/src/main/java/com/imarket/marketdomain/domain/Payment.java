package com.imarket.marketdomain.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"buyer", "cardNumber"})
@Entity
public class Payment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;

    @Column(nullable = false)
    private String cardNickName;

    @Column(nullable = false)
    private String cardNumber;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @OneToMany(mappedBy = "payment")
    private List<Order> orders = new ArrayList<>();

    public Payment(){}
    public Payment(String cardNickName, String cardNumber) {
        this.cardNickName = cardNickName;
        this.cardNumber = cardNumber;
    }

    public void setBuyer(Buyer buyer) {
        if (this.buyer != null) {
            this.buyer.getPayments().remove(this);
        }

        this.buyer = buyer;

        if (!this.buyer.getPayments().contains(this)) {
            this.buyer.getPayments().add(this);
        }
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        if (order.getPayment() != this) {
            order.setPayment(this);
        }
    }
}
