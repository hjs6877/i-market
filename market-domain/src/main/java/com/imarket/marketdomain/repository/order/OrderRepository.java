package com.imarket.marketdomain.repository.order;

import com.imarket.marketdomain.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByBuyerBuyerId(long buyerId, Pageable pageable);
    Page<Order> findBySellerSellerId(long sellerId, Pageable pageable);
}
