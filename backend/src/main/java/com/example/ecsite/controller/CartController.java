package com.example.ecsite.controller;

import com.example.ecsite.dto.CartItemRequest;
import com.example.ecsite.dto.CartItemResponse;
import com.example.ecsite.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public List<CartItemResponse> getCart() {
        return cartService.getCartItems();
    }

    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(@RequestBody CartItemRequest request) {
        CartItemResponse item = cartService.addToCart(request);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public CartItemResponse updateQuantity(@PathVariable Long id, @RequestBody CartItemRequest request) {
        return cartService.updateQuantity(id, request.getQuantity());
    }

    @DeleteMapping("/{id}")
    public void removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
    }
}
