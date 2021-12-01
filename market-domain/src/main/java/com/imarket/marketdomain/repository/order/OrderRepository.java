package com.imarket.marketdomain.repository.order;

import com.imarket.marketdomain.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
