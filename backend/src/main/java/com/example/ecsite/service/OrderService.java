package com.example.ecsite.service;

import com.example.ecsite.dto.OrderItemResponse;
import com.example.ecsite.dto.OrderRequest;
import com.example.ecsite.dto.OrderResponse;
import com.example.ecsite.model.*;
import com.example.ecsite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = getCurrentUser();

        // カート取得
        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("カートが空です");
        }

        // 配送先取得
        Address address = addressRepository.findByIdAndUserId(request.getAddressId(), user.getId())
                .orElseThrow(() -> new IllegalArgumentException("配送先が見つかりません"));

        // 在庫チェック
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException("在庫不足: " + product.getName()
                        + "（在庫: " + product.getStock() + "個、注文: " + cartItem.getQuantity() + "個）");
            }
        }

        // 注文作成
        Order order = new Order();
        order.setUser(user);
        order.setAddressName(address.getName());
        order.setAddressPostalCode(address.getPostalCode());
        order.setAddressPrefecture(address.getPrefecture());
        order.setAddressCity(address.getCity());
        order.setAddressLine(address.getAddressLine());
        order.setAddressPhone(address.getPhone());
        order.setStatus(OrderStatus.PENDING);

        // 注文アイテム作成 & 在庫減算
        double totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setProductName(product.getName());
            orderItem.setProductImageUrl(product.getImageUrl());
            orderItems.add(orderItem);

            totalAmount += product.getPrice() * cartItem.getQuantity();

            // 在庫減算
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);
        Order saved = orderRepository.save(order);

        // カートクリア
        cartItemRepository.deleteByUserId(user.getId());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders() {
        User user = getCurrentUser();
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        User user = getCurrentUser();
        Order order = orderRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("注文が見つかりません"));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("注文が見つかりません"));
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("無効な注文ステータスです: " + status);
        }
        order.setStatus(newStatus);
        return toResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getRecentOrders() {
        return orderRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("ログインユーザーが見つかりません"));
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse r = new OrderResponse();
        r.setId(order.getId());
        r.setAddressName(order.getAddressName());
        r.setAddressPostalCode(order.getAddressPostalCode());
        r.setAddressPrefecture(order.getAddressPrefecture());
        r.setAddressCity(order.getAddressCity());
        r.setAddressLine(order.getAddressLine());
        r.setAddressPhone(order.getAddressPhone());
        r.setTotalAmount(order.getTotalAmount());
        r.setStatus(order.getStatus().name());
        r.setCreatedAt(order.getCreatedAt().toString());
        if (order.getItems() != null) {
            r.setItems(order.getItems().stream().map(this::toItemResponse).collect(Collectors.toList()));
        }
        return r;
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        OrderItemResponse r = new OrderItemResponse();
        r.setId(item.getId());
        r.setProductId(item.getProduct().getId());
        r.setProductName(item.getProductName());
        r.setProductImageUrl(item.getProductImageUrl());
        r.setQuantity(item.getQuantity());
        r.setPrice(item.getPrice());
        return r;
    }
}
