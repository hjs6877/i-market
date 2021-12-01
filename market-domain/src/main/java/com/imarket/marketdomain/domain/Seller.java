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
public class Seller extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.PERSIST)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<Order> orders = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;
        if (member.getSeller() != this) {
            member.setSeller(this);
        }
    }

    public void addProduct(Product product) {
        this.products.add(product);
        if (product.getSeller() != this) {
            product.setSeller(this);
        }
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        if (order.getSeller() != this) {
            order.setSeller(this);
        }
    }
}
