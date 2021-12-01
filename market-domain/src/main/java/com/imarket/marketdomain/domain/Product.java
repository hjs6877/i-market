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
@ToString(exclude = {"seller", "productCategory"})
@Entity
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private long price;
    private String description;
    private boolean canPurchase = true;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToMany(mappedBy = "product")
    private List<Order> orders = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    public Product(){}
    public Product(String productName, long price) {
        this.productName = productName;
        this.price = price;
    }

    public void setSeller(Seller seller) {
        if (this.seller != null) {
            this.seller.getProducts().remove(this);
        }
        this.seller = seller;

        if (!this.seller.getProducts().contains(this)) {
            this.seller.getProducts().add(this);
        }
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        if (order.getProduct() != this) {
            order.setProduct(this);
        }
    }

    public void setProductCategory(ProductCategory productCategory) {
        if (this.productCategory != null) {
            this.productCategory.getProduct().remove(this);
        }
        this.productCategory = productCategory;

        if (!this.productCategory.getProduct().contains(this)) {
            this.productCategory.getProduct().add(this);
        }
    }

    @Override
    public String toString(){
        return null;
    }
}
