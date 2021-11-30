package com.imarket.marketdomain.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "product_order")
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

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
}
