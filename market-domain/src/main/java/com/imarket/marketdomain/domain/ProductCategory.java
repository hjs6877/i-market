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
@ToString
@Entity
public class ProductCategory {
    public ProductCategory() {}
    public ProductCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id")
    private long id;

    @Column(nullable = false, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "productCategory")
    private List<Product> product = new ArrayList<>();


}
