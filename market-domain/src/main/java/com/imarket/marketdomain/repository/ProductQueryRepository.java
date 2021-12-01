package com.imarket.marketdomain.repository;

import com.imarket.marketdomain.domain.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductQueryRepository {
    Page<Product> searchProductPageable(String productName, String description, Pageable pageable);
}
