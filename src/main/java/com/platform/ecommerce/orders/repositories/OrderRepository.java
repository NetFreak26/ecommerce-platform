package com.platform.ecommerce.orders.repositories;

import com.platform.ecommerce.orders.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
