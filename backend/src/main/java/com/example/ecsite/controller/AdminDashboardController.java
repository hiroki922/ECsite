package com.example.ecsite.controller;

import com.example.ecsite.dto.OrderResponse;
import com.example.ecsite.model.OrderStatus;
import com.example.ecsite.repository.OrderRepository;
import com.example.ecsite.repository.ProductRepository;
import com.example.ecsite.repository.UserRepository;
import com.example.ecsite.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("productCount", productRepository.count());
        stats.put("userCount", userRepository.count());
        stats.put("pendingOrderCount", orderRepository.countByStatus(OrderStatus.PENDING));
        stats.put("totalOrderCount", orderRepository.count());
        return stats;
    }

    @GetMapping("/recent-orders")
    public List<OrderResponse> getRecentOrders() {
        return orderService.getRecentOrders();
    }
}
