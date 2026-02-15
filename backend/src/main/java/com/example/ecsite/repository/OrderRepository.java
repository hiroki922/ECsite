package com.example.ecsite.repository;

import com.example.ecsite.model.Order;
import com.example.ecsite.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Order> findByIdAndUserId(Long id, Long userId);
    long countByStatus(OrderStatus status);
    List<Order> findTop10ByOrderByCreatedAtDesc();
    List<Order> findAllByOrderByCreatedAtDesc();
}
