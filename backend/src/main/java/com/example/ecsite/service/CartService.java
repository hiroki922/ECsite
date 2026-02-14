package com.example.ecsite.service;

import com.example.ecsite.dto.CartItemRequest;
import com.example.ecsite.dto.CartItemResponse;
import com.example.ecsite.model.CartItem;
import com.example.ecsite.model.Product;
import com.example.ecsite.model.User;
import com.example.ecsite.repository.CartItemRepository;
import com.example.ecsite.repository.ProductRepository;
import com.example.ecsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems() {
        User user = getCurrentUser();
        return cartItemRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartItemResponse addToCart(CartItemRequest request) {
        User user = getCurrentUser();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("商品が見つかりません"));

        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new IllegalArgumentException("数量は1以上を指定してください");
        }

        // 既にカートにある場合は数量を加算
        var existing = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId());
        if (existing.isPresent()) {
            CartItem item = existing.get();
            int newQty = item.getQuantity() + request.getQuantity();
            if (newQty > product.getStock()) {
                throw new IllegalStateException("在庫不足です（在庫: " + product.getStock() + "個）");
            }
            item.setQuantity(newQty);
            return toResponse(cartItemRepository.save(item));
        }

        if (request.getQuantity() > product.getStock()) {
            throw new IllegalStateException("在庫不足です（在庫: " + product.getStock() + "個）");
        }

        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(request.getQuantity());
        return toResponse(cartItemRepository.save(item));
    }

    @Transactional
    public CartItemResponse updateQuantity(Long cartItemId, Integer quantity) {
        User user = getCurrentUser();
        CartItem item = cartItemRepository.findByIdAndUserId(cartItemId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("カートアイテムが見つかりません"));

        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("数量は1以上を指定してください");
        }
        if (quantity > item.getProduct().getStock()) {
            throw new IllegalStateException("在庫不足です（在庫: " + item.getProduct().getStock() + "個）");
        }

        item.setQuantity(quantity);
        return toResponse(cartItemRepository.save(item));
    }

    @Transactional
    public void removeFromCart(Long cartItemId) {
        User user = getCurrentUser();
        CartItem item = cartItemRepository.findByIdAndUserId(cartItemId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("カートアイテムが見つかりません"));
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("ログインユーザーが見つかりません"));
    }

    private CartItemResponse toResponse(CartItem item) {
        CartItemResponse r = new CartItemResponse();
        r.setId(item.getId());
        r.setProductId(item.getProduct().getId());
        r.setProductName(item.getProduct().getName());
        r.setProductPrice(item.getProduct().getPrice());
        r.setProductImageUrl(item.getProduct().getImageUrl());
        r.setQuantity(item.getQuantity());
        r.setStock(item.getProduct().getStock());
        return r;
    }
}
