package com.imarket.marketdomain.repository.product_category;

import com.imarket.marketdomain.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
