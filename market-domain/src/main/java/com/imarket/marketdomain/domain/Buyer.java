package com.imarket.marketdomain.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"member"})
@Entity
public class Buyer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buyer_id")
    private long buyerId;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "buyer")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "buyer")
    private List<Order> orders = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;
        if (member.getBuyer() != this) {
            member.setBuyer(this);
        }
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
        if (payment.getBuyer() != this) {
            payment.setBuyer(this);
        }
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        if (order.getBuyer() != this) {
            order.setBuyer(this);
        }
    }
}
