package com.imarket.marketdomain.repository;

import com.imarket.marketdomain.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
