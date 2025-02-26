package com.hafsa.ecommerce.repository;

import com.hafsa.ecommerce.model.Order;
import com.hafsa.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
}
