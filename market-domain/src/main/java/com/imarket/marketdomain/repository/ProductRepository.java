package com.imarket.marketdomain.repository;

import com.imarket.marketdomain.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryRepository {
}
