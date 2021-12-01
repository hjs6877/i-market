package com.imarket.marketdomain.repository.product;

import com.imarket.marketdomain.domain.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryRepository {
    Page<Product> searchProduct(String productName, String description, Pageable pageable);
}
